//
//  LoginViewController.m
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/10/12.
//  Copyright (c) 2012 Business Labs. All rights reserved.
//

#import "LoginViewController.h"
//#import "ASIFormDataRequest.h"
#import "SBJson.h"
#import "NSString+MD5.h"
#import "TraderListTVC.h"
#import "TraderAddViewController.h"
#import "HistoryViewController.h"
#import "AnalyticsViewController.h"
#import "TraderEditViewController.h"


@implementation LoginViewController
@synthesize email;
@synthesize password;
@synthesize jsonData;
@synthesize jsonURL;
@synthesize jsonArray;



-(IBAction)checkLogin:(id)sender
{
    
    
    //[self performSegueWithIdentifier:@"UserTabBar" sender:self];
    
    
    NSString *email_ = email.text;
    NSString *pwd_ = password.text;
    
    NSString *HashedPassword = [pwd_ MD5];
    
    UIAlertView *someError = [[UIAlertView alloc] initWithTitle: @"Login error" message: @"Error trying login in the server" delegate: self cancelButtonTitle: @"Ok" otherButtonTitles: nil];
    NSString *ugp = @"ADMIN";
    
    //[someError release];
    
    NSString *urlString_ = [NSString stringWithFormat:@"http://localhost:8080/MockStockWebClientTwo/servlets/enter?action=check&email=%@&password=%@",email_,HashedPassword];
    
    jsonURL = [NSURL URLWithString:urlString_];
    jsonData = [[NSString alloc] initWithContentsOfURL:jsonURL];
    
    NSUInteger *jsonDataLength = [jsonData length];
    self.jsonArray = [jsonData JSONValue];
    NSString *gettingEmail = [jsonArray objectForKey:@"email"];
    NSString *gettingUserGroup = [jsonArray objectForKey:@"usrGroup"];
    NSString *gettingUserStatut = [jsonArray objectForKey:@"usrStatus"];
    
    if(jsonDataLength < 20){
        NSLog(@"connection failed ");
        NSLog(@"%i",jsonDataLength);
        [someError show];
    }else if ([gettingUserGroup isEqualToString:@"ADMIN"]&&[gettingUserStatut isEqualToString:@"ACTIVATED"]) {
        
        [self performSegueWithIdentifier:@"admin" sender:self];
        //[self logAsAdmin];
        NSLog(@"Hello Admin");
        NSLog(@"%i",jsonDataLength);
        NSLog(@"email        %@ :",gettingEmail);
        NSLog(@"groupUser   %@  :",gettingUserGroup);
       // [self logAsAdmin];
        
    }else {
        [self performSegueWithIdentifier:@"user" sender:self];
        NSLog(@"%@",jsonArray);
        NSLog(@"jsonData length :%i  ",[jsonData length]);
        NSLog(@"Hello User");
        
        
        NSLog(@"email        %@ :",gettingEmail);
        NSLog(@"groupUser   %@  :",gettingUserGroup);   
    }
    
    
}


//
//-(void)logAsAdmin{
//    
//    window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
//    AdminTabBar = [[UITabBarController alloc] init];
//    
//    //TraderListTVC *vc1 = [[TraderListTVC alloc] init];
//    TraderAddViewController *vc1 = [[TraderAddViewController alloc] init];
//   // HistoryViewController *vc2 = [[HistoryViewController alloc] init];
//    AnalyticsViewController *vc3 = [[AnalyticsViewController alloc] init];
//    TraderEditViewController *vc4 = [[TraderEditViewController alloc] init];
//    
//    
//    NSArray* controllers = [NSArray arrayWithObjects:vc1, vc2,vc3,vc4, nil];
//    AdminTabBar.viewControllers = controllers;
//    
//    // window.rootViewController = AdminTabBar;
//    [window addSubview:AdminTabBar.view];
//    
//}



- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

/*
 // Implement loadView to create a view hierarchy programmatically, without using a nib.
 - (void)loadView
 {
 }
 */


// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad
{
    [super viewDidLoad];
    //    jsonURL = [NSURL URLWithString:@"mockStock/request" ];
    //    jsonData = [[NSString alloc] initWithContentsOfURL:jsonURL];
    //    
    //    if([jsonData length] == 0){
    //        NSLog(@"connection failed ");
    //    }
    //    
    //    self.jsonArray = [jsonData JSONValue];
    //    NSLog(@"%@",jsonArray);
}


- (void)viewDidUnload
{
    [self setEmail:nil];
    [self setPassword:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

-(void)dealoc{
    // [self dealloc];
    // [jsonData release];
    
    
    
}

@end
