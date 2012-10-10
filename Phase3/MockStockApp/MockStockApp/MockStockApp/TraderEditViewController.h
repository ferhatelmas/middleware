//
//  TraderEditViewController.h
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/17/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface TraderEditViewController : UIViewController {
    IBOutlet UITextField *firstName;
    IBOutlet UITextField *lastName;
    IBOutlet UITextField *email;
    IBOutlet UITextField *newPassword;
    IBOutlet UISegmentedControl *usrGroup;
    IBOutlet UISegmentedControl *status;
}

@property (strong, nonatomic) id detailItem;
-(IBAction)textFieldReturn:(id)sender;
-(IBAction)deleteTrader:(id)sender;
-(IBAction)updateTrader:(id)sender;

@end
