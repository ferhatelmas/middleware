//
//  SettingsViewController.m
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/23/12.
//  Copyright (c) 2012 Business Labs. All rights reserved.
//

#import "SettingsViewController.h"
#import "AppDelegate.h"
#import "SBJson.h"
#import "NSString+MD5.h"

@implementation SettingsViewController

-(IBAction)textFieldReturn:(id)sender
{
    [sender resignFirstResponder];
}

-(void)loadSettings 
{
    AppDelegate *appDelegate = (AppDelegate *) [[UIApplication sharedApplication] delegate];
    firstName.text = [appDelegate.loginData objectForKey:@"firstName"];
    lastName.text = [appDelegate.loginData objectForKey:@"lastName"];
    email.text = [appDelegate.loginData objectForKey:@"email"];    
}
-(IBAction)updateSettings:(id)sender 
{
    AppDelegate *appDelegate = (AppDelegate *) [[UIApplication sharedApplication] delegate];
    NSString *typedInPassword = [password.text MD5];
    if ([email.text rangeOfString:@"@"].location == NSNotFound) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"No valid e-mail address found." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alert show]; 
    } else if (![typedInPassword isEqualToString:[appDelegate.loginData objectForKey:@"password"]]) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Please type in your correct password." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alert show]; 
    } else {
        BOOL newPasswordOK = NO;
        //NSString *hashedPassword = [password.text MD5];
        NSString *hashedNewPassword = nil;
        if ([newPassword.text length] != 0 && [newPassword.text length] < 6) {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"New password must contain at least 6 characters." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [alert show];
        } else if ([newPassword.text length] != 0) {
            hashedNewPassword = [newPassword.text MD5];
            newPasswordOK = YES;
        } else {
            newPasswordOK = YES;
        }
        
        if (newPasswordOK) {
        SBJsonParser *parser = [[SBJsonParser alloc] init];
            NSString *url = [NSString stringWithFormat:@"%@/servlets/trading", appDelegate.serverUrl];
        NSMutableURLRequest *request =[NSMutableURLRequest requestWithURL:[NSURL URLWithString:url]];
        [request setHTTPMethod:@"POST"];
        

        NSString *post = [NSString stringWithFormat:
                          @"action=updateSettings&newPassword=%@&newEmail=%@&lastName=%@&firstName=%@", 
                          hashedNewPassword, email.text, lastName.text, firstName.text];
        [request setHTTPBody:[post dataUsingEncoding:NSUTF8StringEncoding]];
        NSURLResponse *response;
        NSError *err;
        NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&err];
        
        
        NSString *json_string = [[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding];
        NSDictionary *data = (NSDictionary *) [parser objectWithString:json_string error:nil];
        NSString *error = [data objectForKey:@"error"];
        
        if(error == nil) {
            password.text = nil;
            newPassword.text = nil;
            appDelegate.loginData = data;
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Hurray" message:@"Changes were successfully saved." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [alert show];
        } else {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:error delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [alert show];
        }
        }
	}
}

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
    [self loadSettings];
    [super viewDidLoad];
}


- (void)viewDidUnload
{
    firstName = nil;
    lastName = nil;
    email = nil;
    password = nil;
    newPassword = nil;
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

@end
