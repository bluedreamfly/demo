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
#import <AMapSearchKit/AMapSearchKit.h>
#import <React/RCTViewManager.h>
#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>
#import "RCTCustomMap.h"

@interface RNTMapManager : RCTViewManager
  @property (nonatomic, strong) AMapSearchAPI* search;
@property (nonatomic, strong) RCTCustomMap* _mapView;
@property (nonatomic, strong) MAPolyline* polyline;
@property (nonatomic, strong) NSDictionary *start;
@property (nonatomic, strong) NSDictionary *end;


@end
@interface RNTMapManager() <MAMapViewDelegate, AMapSearchDelegate>
@end
@implementation RNTMapManager
//暴露模块给react-native
RCT_EXPORT_MODULE(Amap)

//返回一个视图
- (UIView *)view
{
//  MAMapView *_mapView = [[MAMapView alloc] init];
  RCTCustomMap *_mapView = [[RCTCustomMap alloc] init];
  _mapView.showsUserLocation = YES;
  self._mapView = _mapView;
  _mapView.zoomLevel = 18;
  _mapView.userTrackingMode = MAUserTrackingModeFollow;
  
  _mapView.delegate = self; //设置代理
  self.search = [[AMapSearchAPI alloc] init];
  self.search.delegate = self;
  
  return _mapView;
}

RCT_EXPORT_VIEW_PROPERTY(onChange, RCTBubblingEventBlock)

//暴露一个操作地图的方法
RCT_EXPORT_METHOD(addPoint:(nonnull NSNumber *)reactTag
                  args:(NSDictionary *)args)
{
  [self.bridge.uiManager addUIBlock:
   ^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, RCTCustomMap *> *viewRegistry) {
     //数据转换
     //第一个点
     double lat = [[RCTConvert NSNumber:args[@"lat"]] doubleValue];
     double lon = [[RCTConvert NSNumber:args[@"lon"]] doubleValue];
     
     NSLog(@"long %f lat %f", lon, lat);
     //获取注册的视图对象，在js端是通过ref来引用的
     //第二个点
     RCTCustomMap *view = viewRegistry[reactTag];
     if (!view || ![view isKindOfClass:[MAMapView class]]) {
       RCTLogError(@"Cannot find MAMapView with tag #%@", reactTag);
       return;
     }
     //绘制大头针标注
     MAPointAnnotation *pointAnnotation = [[MAPointAnnotation alloc] init];
     
     
     pointAnnotation.coordinate = CLLocationCoordinate2DMake(lat, lon);
     pointAnnotation.title = @"方恒国际1111";
     pointAnnotation.subtitle = @"阜通东大街6号";
     
     [view addAnnotation:pointAnnotation];
   }];
}


RCT_EXPORT_METHOD(getRoutePath: (NSDictionary *)start end: (NSDictionary *)end)
{
  double startLat = [[RCTConvert NSNumber:start[@"lat"]] doubleValue];
  double startLng = [[RCTConvert NSNumber:start[@"lng"]] doubleValue];
  double endLat = [[RCTConvert NSNumber:end[@"lat"]] doubleValue];
  double endLng = [[RCTConvert NSNumber:end[@"lng"]] doubleValue];
  
  self.start = start;
  self.end = end;
  
  AMapWalkingRouteSearchRequest *navi = [[AMapWalkingRouteSearchRequest alloc] init];
  
  
  navi.origin = [AMapGeoPoint locationWithLatitude:startLat
                                         longitude:startLng];
  
  navi.destination = [AMapGeoPoint locationWithLatitude:endLat
                                              longitude:endLng];
  
  [self.search AMapWalkingRouteSearch:navi];


}


//绘制大头针标注对应的配置，通过代理的方式调用
-(MAAnnotationView *)mapView:(RCTCustomMap *)mapView viewForAnnotation:(id <MAAnnotation>)annotation
{
  if ([annotation isKindOfClass:[MAPointAnnotation class]])
  {
    static NSString *pointReuseIndentifier = @"pointReuseIndentifier";
    MAAnnotationView *annotationView = (MAAnnotationView*)[mapView dequeueReusableAnnotationViewWithIdentifier:pointReuseIndentifier];
    if (annotationView == nil)
    {
      annotationView = [[MAPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:pointReuseIndentifier];
//      UIButton* aButton = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 75, 30)];
//      [aButton setTitle:@"Directions" forState:UIControlStateNormal];
//      annotationView.rightCalloutAccessoryView = aButton;
    }
    
    annotationView.image = [UIImage imageNamed:@"bike"];
//    UIImage imageNamed:@"
    annotationView.canShowCallout= NO;       //设置气泡可以弹出，默认为NO
//    annotationView.animatesDrop = YES;        //设置标注动画显示，默认为NO
    annotationView.draggable = NO;        //设置标注可以拖动，默认为NO
//    annotationView.pinColor = MAPinAnnotationColorPurple;
    return annotationView;
  }
  return nil;
}


- (MAOverlayRenderer *)mapView:(RCTCustomMap *)mapView rendererForOverlay:(id <MAOverlay>)overlay
{
  if ([overlay isKindOfClass:[MAPolyline class]])
  {
    MAPolylineRenderer *polylineRenderer = [[MAPolylineRenderer alloc] initWithPolyline:overlay];
    
    polylineRenderer.lineWidth    = 2.f;
    polylineRenderer.strokeColor  = [UIColor colorWithRed:1 green:0 blue:0 alpha:0.6];
//    polylineRenderer.lineJoin = kCALineJoinRound;
    
    
    return polylineRenderer;
  }
  return nil;
}

- (void)mapView:(RCTCustomMap *)mapView didSelectAnnotationView:(MAAnnotationView *)view
{
//    NSLog(@"%@", view.annotation.coordinate.latitude);
  if (!mapView.onChange) {
    return;
  }
  mapView.onChange(@{
                     @"name": @"selected",
                     @"type": @"selected",
                     @"latitude": @(view.annotation.coordinate.latitude),
                     @"longitude": @(view.annotation.coordinate.longitude)
                     });
  
}

- (void)mapView:(RCTCustomMap *)mapView didDeselectAnnotationView:(MAAnnotationView *)view;
{
  mapView.onChange(@{
                     @"name": @"unselected",
                     @"type": @"unselected"
                     });
  
}

- (void)mapView:(MAMapView *)mapView didUpdateUserLocation:(MAUserLocation *)userLocation updatingLocation:(BOOL)updatingLocation
{
  
  
}

- (void)mapView:(RCTCustomMap *)mapView didSingleTappedAtCoordinate:(CLLocationCoordinate2D)coordinate {

  NSLog(@"%f, %f", coordinate.latitude, coordinate.longitude);
  
  mapView.onChange(@{
                     @"name": @"clickmap",
                     @"type": @"clickmap",
                     @"latitude": @(coordinate.latitude),
                     @"longitude": @(coordinate.longitude)
                     });

}


- (void)onRouteSearchDone:(AMapRouteSearchBaseRequest *)request response:(AMapRouteSearchResponse *)response
{
  if (response.route == nil)
  {
    return;
  }
  
  NSMutableArray<NSDictionary *> *paths = [[NSMutableArray alloc] init];
  
  
  [paths addObject:@{@"lng": self.start[@"lng"], @"lat": self.start[@"lat"]}];
  
  for(int i = 0; i < response.route.paths[0].steps.count; i++) {
    NSString *polyline = response.route.paths[0].steps[i].polyline;
  
    NSArray * points = [polyline componentsSeparatedByString:@";"];
  
  
    NSLog(@"polyline%@", polyline);
    NSLog(@"points%@", points);
  
  for (int i = 0; i < points.count; i++) {
    
    
    NSArray * point = [points[i] componentsSeparatedByString:@","];
    
    [paths addObject:@{@"lng": point[0], @"lat": point[1]}];
    
  }
  }
  
  [paths addObject:@{@"lng": self.end[@"lng"], @"lat": self.end[@"lat"]}];
  
//    NSLog(@"paths%@", points);
//
  if (self.polyline != nil) {
    [self._mapView removeOverlay:self.polyline];
  }
  
  self.polyline = [self._mapView drawPolyline: paths];
  
  
//    NSLog(@"polyline%@", polyline);
//    NSLog(@"response.route.paths %lu", (unsigned long)response.route.paths.count);
//    NSLog(@"response.route.paths %lu", (unsigned long)response.route.paths[0].steps.count);
//    NSArray *points = [polyline componentsSeparatedByString:@";"];
    
//    [paths addObject:@{@"lng": }]
//  }
  
//  [self._mapView drawPolyline];  TODO
//  NSLog(@"%@", response.route.paths[0].steps[0].polyline);
  
  
  
  //解析response获取路径信息，具体解析见 Demo
}



- (void)AMapSearchRequest:(id)request didFailWithError:(NSError *)error
{
  NSLog(@"Error: %@", error);
}









@end
