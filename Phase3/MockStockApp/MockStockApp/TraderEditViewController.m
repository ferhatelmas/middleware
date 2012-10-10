//
//  TraderEditViewController.m
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/17/12.
//  Copyright (c) 2012 Business Labs. All rights reserved.
//

#import "TraderEditViewController.h"
#import "SBJson.h"
#import "NSString+MD5.h"
#import <QuartzCore/QuartzCore.h>
#import "AppDelegate.h"


@interface TraderEditViewController ()
- (void)configureView;
@end

@implementation TraderEditViewController
@synthesize deleteTrader;

@synthesize detailItem = _detailItem;

-(IBAction)textFieldReturn:(id)sender
{
    [sender resignFirstResponder];
}

- (IBAction)deleteTrader:(id)sender
{
	if([sender isKindOfClass:[UIButton class]]){
        NSDictionary *trader = self.detailItem;
        
        SBJsonParser *parser = [[SBJsonParser alloc] init];
        AppDelegate *appDelegate = (AppDelegate *) [[UIApplication sharedApplication] delegate];
        NSString *url = [NSString stringWithFormat:@"%@/servlets/administration", appDelegate.serverUrl];
        NSMutableURLRequest *request =[NSMutableURLRequest requestWithURL:[NSURL URLWithString:url]];
        [request setHTTPMethod:@"POST"];
        NSString *post = [NSString stringWithFormat:@"action=removeTrader&email=%@&password=%@", email.text, [trader objectForKey:@"password"]];
        [request setHTTPBody:[post dataUsingEncoding:NSUTF8StringEncoding]];
        NSURLResponse *response;
        NSError *err;
        NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&err];
        
        NSString *json_string = [[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding];
        NSDictionary *data = (NSDictionary *) [parser objectWithString:json_string error:nil];
        
        NSString *error = [data objectForKey:@"error"];
        
        if(error == nil) {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Hurray" message:@"Trader was successfully deleted." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
			[alert show];
            [self.navigationController popViewControllerAnimated:YES];
        } else {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:error delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
			[alert show];
        }
        
        
	}
}

- (IBAction)updateTrader:(id)sender
{
	if([sender isKindOfClass:[UIBarButtonItem class]]){
        if (![self stringIsValidEmail:email.text]) {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"E-mail address is not valid." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [alert show]; 
        } else if ([newPassword.text length] != 0 && [newPassword.text length] < 6) {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Password must contain at least 6 characters." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [alert show]; 
        } else {
        NSDictionary *trader = self.detailItem;
        
        SBJsonParser *parser = [[SBJsonParser alloc] init];
            AppDelegate *appDelegate = (AppDelegate *) [[UIApplication sharedApplication] delegate];
            NSString *url = [NSString stringWithFormat:@"%@/servlets/administration", appDelegate.serverUrl];
        NSMutableURLRequest *request =[NSMutableURLRequest requestWithURL:[NSURL URLWithString:url]];
        [request setHTTPMethod:@"POST"];
        
        NSString *currentStatus;
        if (status.selectedSegmentIndex == 1) {
            currentStatus = @"NOT_ACTIVE";
        } else if (status.selectedSegmentIndex == 2) {
            currentStatus = @"DISABLED";
        } else {
            currentStatus = @"ACTIVATED";
        }
        NSString *currentUsrGroup;
        if (usrGroup.selectedSegmentIndex == 0) {
            currentUsrGroup = @"ADMIN";
        } else {
            currentUsrGroup = @"USER";
        }
        
        NSString *hashedPassword;
        if ([newPassword.text length] != 0) {
            hashedPassword = [newPassword.text MD5];
        } else {
            hashedPassword = newPassword.text;
        }
        NSString *post = [NSString stringWithFormat:
                          @"action=updateTrader&email=%@&password=%@&newPassword=%@&newEmail=%@&lastName=%@&firstName=%@&usrStatus=%@&usrRight=%@", 
                          [trader objectForKey:@"email"], [trader objectForKey:@"password"], hashedPassword, email.text, lastName.text, firstName.text, currentStatus, currentUsrGroup];
        [request setHTTPBody:[post dataUsingEncoding:NSUTF8StringEncoding]];
        NSURLResponse *response;
        NSError *err;
        NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&err];
        
        
        NSString *json_string = [[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding];
        NSDictionary *data = (NSDictionary *) [parser objectWithString:json_string error:nil];
        
        NSString *error = [data objectForKey:@"error"];
        
        if(error == nil) {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Hurray" message:@"Changes were successfully saved." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [alert show];
            [trader setValue:[data objectForKey:@"lastName"] forKey:@"lastName"];
            [trader setValue:[data objectForKey:@"firstName"] forKey:@"firstName"];
            [trader setValue:[data objectForKey:@"email"] forKey:@"email"];
            [trader setValue:[data objectForKey:@"password"] forKey:@"password"];
            [trader setValue:[data objectForKey:@"usrStatus"] forKey:@"usrStatus"];
            [trader setValue:[data objectForKey:@"usrGroup"] forKey:@"usrGroup"];
            self.detailItem = trader;
        } else {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:error delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [alert show];
        }
        }
	}
}

-(BOOL)stringIsValidEmail:(NSString *)checkString
{
    BOOL stricterFilter = YES; 
    NSString *stricterFilterString = @"[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
    NSString *laxString = @".+@.+\\.[A-Za-z]{2}[A-Za-z]*";
    NSString *emailRegex = stricterFilter ? stricterFilterString : laxString;
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", emailRegex];
    return [emailTest evaluateWithObject:checkString];
}


#pragma mark - Managing the detail item

- (void)setDetailItem:(id)newDetailItem
{
    if (_detailItem != newDetailItem) {
    _detailItem = newDetailItem;
    [self configureView];
    }
}

- (void)configureView
{
    if (self.detailItem) {
        NSDictionary *trader = self.detailItem;
        
        firstName.text = [trader objectForKey:@"firstName"];
        NSString *lastname = [trader objectForKey:@"lastName"];
        lastName.text = lastname;
        email.text = [trader objectForKey:@"email"];
        NSString *currentStatus = [trader objectForKey:@"usrStatus"];
        NSString *currentGroup = [trader objectForKey:@"usrGroup"];
        
        if([currentStatus isEqualToString:@"ACTIVATED"]){
            status.selectedSegmentIndex = 0;
        } else if([currentStatus isEqualToString:@"NOT_ACTIVE"]){
            status.selectedSegmentIndex = 1;
        } else {
            status.selectedSegmentIndex = 2;
        }
        
        if([currentGroup isEqualToString:@"ADMIN"]){
            usrGroup.selectedSegmentIndex = 0;
        } else {
            usrGroup.selectedSegmentIndex = 1;
        }
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
    deleteTrader.layer.borderWidth = 0.5;
    deleteTrader.layer.cornerRadius = 4.0;
    [self configureView];
}

- (void)viewDidUnload
{
    [self setDeleteTrader:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated
{
	[super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated
{
	[super viewDidDisappear:animated];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
}

@end
