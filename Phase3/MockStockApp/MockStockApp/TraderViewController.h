//
//  TraderViewController.h
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/10/12.
//  Copyright (c) 2012 Business Labs. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SBJson.h"

@interface TraderViewController : UIViewController {
    IBOutlet UITextField *quantity;
    IBOutlet UISegmentedControl *stock;
    IBOutlet UILabel *appleQuantity;
    IBOutlet UILabel *appleResult;
    IBOutlet UILabel *sunQuantity;
    IBOutlet UILabel *sunResult;
    IBOutlet UILabel *ibmQuantity;
    IBOutlet UILabel *ibmResult;
    IBOutlet UILabel *totalResult;
    NSArray *portfolio;
    NSArray *stockProducts;
    NSArray *priceDifferences;
    IBOutlet UILabel *ibmDiff;
    IBOutlet UILabel *ibmPrice;
    IBOutlet UILabel *appleDiff;
    IBOutlet UILabel *applePrice;
    IBOutlet UILabel *sunPrice;
    IBOutlet UILabel *sunDiff;
    UIButton *sellButton;
    UIButton *buyButton;
}
@property (strong, nonatomic) IBOutlet UIButton *buyButton;
@property (strong, nonatomic) IBOutlet UIButton *sellButton;
-(IBAction)textFieldReturn:(id)sender;
-(IBAction)buyTransaction:(id)sender;
-(IBAction)sellTransaction:(id)sender;
-(void)fetchPortfolio;
-(void)updatePortfolio:(NSArray *)currentPortfolio;
-(void)fetchMarketData;
@end
