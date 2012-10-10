//
//  TraderTransactionListTVC.h
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/18/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface TraderTransactionListTVC : UITableViewController {
    NSMutableArray *transactions;
}
-(void)fetchTransactions;

@end
