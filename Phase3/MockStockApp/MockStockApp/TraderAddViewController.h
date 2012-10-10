//
//  TraderAddViewController.h
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/17/12.
//  Copyright (c) 2012 Business Labs. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface TraderAddViewController : UIViewController {
    IBOutlet UITextField *firstName;
    IBOutlet UITextField *lastName;
    IBOutlet UITextField *email;
    IBOutlet UITextField *password;
    IBOutlet UISegmentedControl *usrGroup;
    IBOutlet UISegmentedControl *status;
}
-(IBAction)textFieldReturn:(id)sender;
-(IBAction)textFieldShouldReturn:(id)sender;
-(IBAction)addTrader:(id)sender;
-(BOOL)stringIsValidEmail:(NSString *)checkString;

@end
