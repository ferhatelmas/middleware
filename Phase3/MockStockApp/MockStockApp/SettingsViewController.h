//
//  SettingsViewController.h
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/23/12.
//  Copyright (c) 2012 Business Labs. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SettingsViewController : UIViewController {
    IBOutlet UITextField *newPassword;
    IBOutlet UITextField *password;
    IBOutlet UITextField *email;
    IBOutlet UITextField *lastName;
    IBOutlet UITextField *firstName;
}
-(IBAction)textFieldReturn:(id)sender;
-(IBAction)updateSettings:(id)sender;
-(void)loadSettings;
@end
