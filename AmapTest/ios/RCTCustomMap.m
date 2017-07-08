//
//  RCTCustomMap.m
//  AmapTest
//
//  Created by zhenhui huang on 2017/7/7.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import "RCTCustomMap.h"


@implementation RCTCustomMap

//MAPolyline* drawPolyline(CLLocationCoordinate2D *a)
//{
////  CLLocationCoordinate2D commonPolylineCoords[4];
////  commonPolylineCoords[0].latitude = 39.832136;
////  commonPolylineCoords[0].longitude = 116.34095;
////  
////  commonPolylineCoords[1].latitude = 39.832136;
////  commonPolylineCoords[1].longitude = 116.42095;
////  
////  commonPolylineCoords[2].latitude = 39.902136;
////  commonPolylineCoords[2].longitude = 116.42095;
////  
////  commonPolylineCoords[3].latitude = 39.902136;
////  commonPolylineCoords[3].longitude = 116.44095;
//  
//  
//  
//  
//  //构造折线对象
//  MAPolyline *commonPolyline = [MAPolyline polylineWithCoordinates:a count: sizeof(a)/sizeof(a[0])];
//  
//  //在地图上添加折线对象
//  [self addOverlay: commonPolyline];
//  
//  return commonPolyline;
//}

-(MAPolyline *)drawPolyline:(NSMutableArray<NSDictionary *> *) paths
{
  
  
  CLLocationCoordinate2D *commonPolylineCoords = (CLLocationCoordinate2D*)malloc(paths.count * sizeof(CLLocationCoordinate2D));

  for (int i = 0; i < paths.count; i++) {
    
    NSLog(@"commonPolylineCoords%f", [paths[i][@"lat"] doubleValue]);
    NSLog(@"commonPolylineCoords%f", [paths[i][@"lng"] doubleValue]);
    commonPolylineCoords[i].latitude = [paths[i][@"lat"] doubleValue];
    commonPolylineCoords[i].longitude = [paths[i][@"lng"] doubleValue];
  }
  
//  NSLog(@"commonPolylineCoords%@", commonPolylineCoords);
  

  //构造折线对象
  MAPolyline *commonPolyline = [MAPolyline polylineWithCoordinates:commonPolylineCoords count: paths.count];
  
  //在地图上添加折线对象
  [self addOverlay: commonPolyline];
  
  return commonPolyline;

}

@end
