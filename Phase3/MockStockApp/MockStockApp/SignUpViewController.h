//
//  SignUpViewController.h
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/24/12.
//  Copyright (c) 2012 Business Labs. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MessageUI/MessageUI.h>
#import <MessageUI/MFMailComposeViewController.h>

@interface SignUpViewController : UIViewController {   //  <MFMailComposeViewControllerDelegate>
    IBOutlet UITextField *password;
    IBOutlet UITextField *email;
    IBOutlet UITextField *lastName;
    IBOutlet UITextField *firstName;
}
-(IBAction)signUp:(id)sender;
-(IBAction)textFieldShouldReturn:(id)sender;
-(BOOL)stringIsValidEmail:(NSString *)checkString;

@end
