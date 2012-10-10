//
//  SignUpViewController.m
//  MockStockApp
//
//  Created by Tarek on 20.05.12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "SignUpViewController.h"
#import "SBJson.h"
#import "NSString+MD5.h"
#import "TraderListTVC.h"
#import "TraderAddViewController.h"
#import "HistoryViewController.h"
#import "AnalyticsViewController.h"
#import "TraderEditViewController.h"



@implementation SignUpViewController
@synthesize name;
@synthesize firstName;
@synthesize signUpEmail;
@synthesize signUpPassword;



-(IBAction)signUP:(id)sender
{

    NSLog(@"Heloooooo signUp");
    
    NSString *firstName_ = firstName.text;
    NSString *lastName_ = name.text;
    NSString *signUpEmail_ = signUpEmail.text;
    NSString *signPassword_ = signUpPassword.text;
    
    
    UIAlertView *successAlert = [[UIAlertView alloc] initWithTitle:@"Congratulations" message:@"signUp successfully added." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
    
    UIAlertView *failedAlert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"signUp failed please try again." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
    
    NSMutableURLRequest *request =[NSMutableURLRequest requestWithURL:[NSURL URLWithString:@"http://localhost:8080/MockStockWebClientTwo/servlets/enter"]];
    
    [request setHTTPMethod:@"POST"];
    
    SBJsonParser *parser = [[SBJsonParser alloc] init];
    
    NSLog(@"JSON parser %@ ",[parser description] );
    
    NSString *hashedPassword = [signUpPassword.text MD5];
    
    NSString *post = [NSString stringWithFormat:
                      @"action=signup&email=%@&password=%@&lastName=%@&firstName=%@", 
                      signUpEmail.text, hashedPassword, name.text, firstName.text];
    
    [request setHTTPBody:[post dataUsingEncoding:NSUTF8StringEncoding]];
    
    NSURLResponse *response;
    NSError *err;
    NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&err];
    
    NSString *json_string = [[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding];
    NSDictionary *data = (NSDictionary *) [parser objectWithString:json_string error:nil];
    
    
    NSString *success = [data objectForKey:@"email"];
    
    if([success isEqualToString:signUpEmail.text]) {
        [self performSegueWithIdentifier:@"signUpUser" sender:self];
        [successAlert show];
        
    } else {
        [failedAlert show];
    }
    
    //[self cleanFields];

}


@end
