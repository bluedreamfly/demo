//
//  RCTCustomMap.m
//  AmapTest
//
//  Created by zhenhui huang on 2017/7/7.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import "RCTCustomMap.h"



@implementation RCTCustomMap

-(MAPolyline *)drawPolyline:(NSMutableArray<NSDictionary *> *) paths
{
  
  CLLocationCoordinate2D *commonPolylineCoords = (CLLocationCoordinate2D*)malloc(paths.count * sizeof(CLLocationCoordinate2D));

  for (int i = 0; i < paths.count; i++) {
    
    commonPolylineCoords[i].latitude = [paths[i][@"lat"] doubleValue];
    commonPolylineCoords[i].longitude = [paths[i][@"lng"] doubleValue];
    
  }
  
  //构造折线对象
  MAPolyline *commonPolyline = [MAPolyline polylineWithCoordinates:commonPolylineCoords count: paths.count];
  
  //在地图上添加折线对象
  [self addOverlay: commonPolyline];
  
  return commonPolyline;

}

@end
