//
//  RCTCustomMap.h
//  AmapTest
//
//  Created by zhenhui huang on 2017/7/7.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import <MAMapKit/MAMapKit.h>
#import <React/RCTComponent.h>

#import <AMapSearchKit/AMapSearchKit.h>


@interface RCTCustomMap : MAMapView

@property (nonatomic, copy) RCTBubblingEventBlock onChange;

-(MAPolyline *)drawPolyline:(NSMutableArray<NSDictionary *> *) paths;

@end
