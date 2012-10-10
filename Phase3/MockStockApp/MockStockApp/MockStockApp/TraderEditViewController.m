//
//  TraderEditViewController.m
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/17/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "TraderEditViewController.h"
#import "SBJson.h"
#import "NSString+MD5.h"


@interface TraderEditViewController ()
- (void)configureView;
@end

@implementation TraderEditViewController

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

        NSMutableURLRequest *request =[NSMutableURLRequest requestWithURL:[NSURL URLWithString:@"http://localhost:8080/servlets/administration?action=removeTrader"]];
        [request setHTTPMethod:@"POST"];
        NSString *hashedPassword = [[trader objectForKey:@"password"] MD5];
        NSString *post = [NSString stringWithFormat:@"email=%@&password=%@", email.text, hashedPassword];
        [request setHTTPBody:[post dataUsingEncoding:NSUTF8StringEncoding]];
        NSLog(@"email: %@", email.text);
        NSLog(@"pwd: %@", hashedPassword);
        NSURLResponse *response;
        NSError *err;
        NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&err];
    
        NSString *json_string = [[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding];
        NSLog(@"json answer: %@", json_string);
        NSDictionary *data = (NSDictionary *) [parser objectWithString:json_string error:nil];
        //NSDictionary *response2 = [responseArray objectAtIndex:1];
        
        NSString *success = [data objectForKey:@"isRemoved"];
        
        if([success isEqualToString:@"deleted"]) {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Hurray" message:@"Trader was successfully deleted." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
			[alert show];
            [self.navigationController popViewControllerAnimated:YES];
        } else {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Trader could not be deleted." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
			[alert show];
        }
        

	}
}

- (IBAction)updateTrader:(id)sender
{
    //There is already a trader with this e-mail address.
    //New password must be a min. of 6 characters.
    
	if([sender isKindOfClass:[UIButton class]]){
        NSDictionary *trader = self.detailItem;
        
        SBJsonParser *parser = [[SBJsonParser alloc] init];
        
        NSMutableURLRequest *request =[NSMutableURLRequest requestWithURL:[NSURL URLWithString:@"http://localhost:8080/servlets/administration?action=updateTrader"]];
        [request setHTTPMethod:@"POST"];
                
        NSString *currentStatus;
        if (status.selectedSegmentIndex == 1) {
            currentStatus = @"NOT_ACTIVE";
        } else if (usrGroup.selectedSegmentIndex == 2) {
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
                          @"email=%@&password=%@&newPassword=%@&newEmail=%@&lastName=%@&firstName=%@&usrStatus=%@&usrRight=%@", 
                          [trader objectForKey:@"email"], [trader objectForKey:@"password"], hashedPassword, email.text, lastName.text, firstName.text, currentStatus, currentUsrGroup];
        [request setHTTPBody:[post dataUsingEncoding:NSUTF8StringEncoding]];
        NSURLResponse *response;
        NSError *err;
        NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&err];
        

        NSString *json_string = [[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding];
        NSDictionary *data = (NSDictionary *) [parser objectWithString:json_string error:nil];
        
        NSString *success = [data objectForKey:@"email"];
                
        if([success isEqualToString:email.text]) {
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
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Changes could not be saved." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [alert show];
        }        
        
	}
}


#pragma mark - Managing the detail item

- (void)setDetailItem:(id)newDetailItem
{
    //if (_detailItem != newDetailItem) {
        _detailItem = newDetailItem;
        
        // Update the view.
        [self configureView];
    //}
}

- (void)configureView
{
    if (self.detailItem) {
        NSDictionary *trader = self.detailItem;
        //NSString *text = [[trader objectForKey:@"user"] objectForKey:@"name"];
        
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
            status.selectedSegmentIndex = 1;
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
	// Do any additional setup after loading the view, typically from a nib.
    [self configureView];
}

- (void)viewDidUnload
{
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
