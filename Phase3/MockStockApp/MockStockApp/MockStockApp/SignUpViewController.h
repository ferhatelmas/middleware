//
//  SignUpViewController.h
//  MockStockApp
//
//  Created by Tarek on 20.05.12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SignUpViewController : UIViewController

-(IBAction)signUP:(id)sender;

@property (nonatomic,retain) IBOutlet UITextField *firstName;
@property (nonatomic,retain) IBOutlet UITextField *name;
@property (nonatomic,retain) IBOutlet UITextField *signUpEmail;
@property (nonatomic,retain) IBOutlet UITextField *signUpPassword;

@end
