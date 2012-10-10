//
//  LoginViewController.h
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/10/12.
//  Copyright (c) 2012 Business Labs. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LoginViewController2 : UIViewController
@property (nonatomic,strong) IBOutlet UITextField *email;
@property (nonatomic,strong) IBOutlet UITextField *password;
@property (nonatomic, retain) IBOutlet UILabel *message;
@property (nonatomic, retain) NSMutableData *webData;


-(IBAction)buttonClickLogin: (id) sender;
@end
