//
//  Amap.m
//  AmapTest
//
//  Created by zhenhui huang on 2017/5/31.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MAMapKit/MAMapKit.h>
#import <AMapFoundationKit/AMapFoundationKit.h>

#import <React/RCTViewManager.h>
#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>

@interface RNTMapManager : RCTViewManager
@end

@implementation RNTMapManager
//暴露模块给react-native
RCT_EXPORT_MODULE(Amap)

//返回一个视图
- (UIView *)view
{
  MAMapView *_mapView = [[MAMapView alloc] init];
  _mapView.delegate = self; //设置代理
  return _mapView;
}


//暴露一个操作地图的方法
RCT_EXPORT_METHOD(addPoint:(nonnull NSNumber *)reactTag
                  args:(NSDictionary *)args)
{
  [self.bridge.uiManager addUIBlock:
   ^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, MAMapView *> *viewRegistry) {
     //数据转换
     //第一个点
     double lat = [[RCTConvert NSNumber:args[@"lat"]] doubleValue];
     double lon = [[RCTConvert NSNumber:args[@"lon"]] doubleValue];
     
     NSLog(@"long %f lat %f", lon, lat);
     //获取注册的视图对象，在js端是通过ref来引用的
     //第二个点
     MAMapView *view = viewRegistry[reactTag];
     if (!view || ![view isKindOfClass:[MAMapView class]]) {
       RCTLogError(@"Cannot find MAMapView with tag #%@", reactTag);
       return;
     }
     //绘制大头针标注
     MAPointAnnotation *pointAnnotation = [[MAPointAnnotation alloc] init];
     pointAnnotation.coordinate = CLLocationCoordinate2DMake(lat, lon);
     pointAnnotation.title = @"方恒国际";
     pointAnnotation.subtitle = @"阜通东大街6号";
     
     [view addAnnotation:pointAnnotation];
   }];
}

//绘制大头针标注对应的配置，通过代理的方式调用
-(MAAnnotationView *)mapView:(MAMapView *)mapView viewForAnnotation:(id <MAAnnotation>)annotation
{
  if ([annotation isKindOfClass:[MAPointAnnotation class]])
  {
    static NSString *pointReuseIndentifier = @"pointReuseIndentifier";
    MAPinAnnotationView*annotationView = (MAPinAnnotationView*)[mapView dequeueReusableAnnotationViewWithIdentifier:pointReuseIndentifier];
    if (annotationView == nil)
    {
      annotationView = [[MAPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:pointReuseIndentifier];
    }
    annotationView.canShowCallout= YES;       //设置气泡可以弹出，默认为NO
    annotationView.animatesDrop = YES;        //设置标注动画显示，默认为NO
    annotationView.draggable = YES;        //设置标注可以拖动，默认为NO
    annotationView.pinColor = MAPinAnnotationColorPurple;
    return annotationView;
  }
  return nil;
}

@end
