//
//  AppDelegate.h
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/10/12.
//  Copyright (c) 2012 Business Labs. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BonjourBrowser.h"

@class Login2ViewController;
@interface AppDelegate : NSObject <UIApplicationDelegate, BonjourBrowserDelegate> {
    UIWindow *window;
	BonjourBrowser *browser;
	NSURL *serverUrl;
    NSDictionary *loginData;
    UIStoryboard *sb;
}

@property (strong, nonatomic) NSDictionary *loginData;
@property (strong, nonatomic) UIWindow *window;
@property (strong, nonatomic) BonjourBrowser *browser;
@property (strong, nonatomic) NSURL *serverUrl;
@property (strong, nonatomic) UIStoryboard *sb;
@end