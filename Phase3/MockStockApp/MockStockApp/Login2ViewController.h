//
//  Login2ViewController.h
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/22/12.
//  Copyright (c) 2012 Business Labs. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Login2ViewController : UIViewController {
    IBOutlet UITextField *password;
    IBOutlet UITextField *email;
    UIButton *signUp;
}
-(IBAction)signIn:(id)sender;
-(IBAction)textFieldShouldReturn:(id)sender;
//-(IBAction)signUp:(id)sender;
-(BOOL)stringIsValidEmail:(NSString *)checkString;
@property (strong, nonatomic) IBOutlet UIButton *signUp;


@end
