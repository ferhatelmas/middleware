//
//  BonjourBrowser.h
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/10/12.
//  Copyright (c) 2012 Business Labs. All rights reserved.
//

#import <Foundation/Foundation.h>

@class BonjourBrowserDelegate;

@interface BonjourBrowser : NSObject {
    NSNetServiceBrowser* netServiceBrowser;
    NSMutableArray* servers;
    id<BonjourBrowserDelegate> delegate;
}

@property(nonatomic,readonly) NSArray* servers;
@property(nonatomic,retain) id<BonjourBrowserDelegate> delegate;

// Start browsing for Bonjour services
- (BOOL)start;

// Stop everything
- (void)stop;

@end
