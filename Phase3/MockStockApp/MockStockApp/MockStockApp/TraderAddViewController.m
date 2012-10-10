//
//  TraderAddViewController.m
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/17/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "TraderAddViewController.h"
#import "SBJson.h"
#import "NSString+MD5.h"

@implementation TraderAddViewController

-(IBAction)textFieldReturn:(id)sender
{
    [sender resignFirstResponder];
}
- (IBAction)addTrader:(id)sender
{
    NSMutableURLRequest *request =[NSMutableURLRequest requestWithURL:[NSURL URLWithString:@"http://localhost:8080/servlets/administration?action=addTrader"]];
    [request setHTTPMethod:@"POST"];
    
    SBJsonParser *parser = [[SBJsonParser alloc] init];
    
    NSString *currentStatus;
    NSLog(@"status: %d", status.selectedSegmentIndex);
    if (status.selectedSegmentIndex == 1) {
        NSLog(@"yes, not active...");
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
    
    NSString *hashedPassword = [password.text MD5];
    
    NSString *post = [NSString stringWithFormat:@"email=%@&password=%@&lastName=%@&firstName=%@&usrStatus=%@&usrRight=%@", 
                      email.text, hashedPassword, lastName.text, firstName.text, currentStatus, currentUsrGroup];
    [request setHTTPBody:[post dataUsingEncoding:NSUTF8StringEncoding]];
    
    NSURLResponse *response;
    NSError *err;
    NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&err];
    
    NSString *json_string = [[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding];
    NSDictionary *data = (NSDictionary *) [parser objectWithString:json_string error:nil];
    NSString *success = [data objectForKey:@"email"];
    
    NSLog(@"JSON Response was: %@", json_string);
    
    if([success isEqualToString:email.text]) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Hurray" message:@"Trader was successfully added." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alert show];
    } else {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Trader could not be added." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alert show];
    }
    
    lastName.text = @"";
    firstName.text = @"";
    password.text = @"";
    email.text = @"";
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

/*
// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad
{
    [super viewDidLoad];
    [scrollView setScrollEnabled:YES];
    [scrollView setContentSize:CGSizeMake(320, 800)];
}
*/
- (void)viewDidUnload
{
    firstName = nil;
    lastName = nil;
    email = nil;
    password = nil;
    usrGroup = nil;
    status = nil;
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
