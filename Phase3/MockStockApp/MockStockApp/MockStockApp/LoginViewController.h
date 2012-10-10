//
//  LoginViewController.h
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/10/12.
//  Copyright (c) 2012 Business Labs. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LoginViewController : UIViewController <UITableViewDataSource>
-(IBAction)checkLogin:(id)sender;
@property (strong, nonatomic) IBOutlet UITextField *email;
@property (strong, nonatomic) IBOutlet UITextField *password;
@property (nonatomic,retain) NSString *jsonData;
@property (nonatomic,retain) NSURL *jsonURL;
@property (nonatomic,retain) NSDictionary *jsonArray;

@end


