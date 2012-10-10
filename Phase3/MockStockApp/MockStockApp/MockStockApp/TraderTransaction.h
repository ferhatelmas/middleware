//
//  TraderTransaction.h
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/10/12.
//  Copyright (c) 2012 Business Labs. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface TraderTransaction : NSObject {
    NSString *description;
	NSDate *createdAt;
}
@property (nonatomic, strong) NSString *description;
@property (nonatomic, strong) NSDate *createdAt;

@end
