//
//  TraderListTVC.h
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/17/12.
//  Copyright (c) 2012 Business Labs. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface TraderListTVC : UITableViewController {
    NSMutableArray *traders;
}

- (void)fetchTraders;

@end
