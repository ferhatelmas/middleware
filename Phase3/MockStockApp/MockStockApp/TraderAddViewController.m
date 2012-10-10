//
//  TraderAddViewController.m
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/17/12.
//  Copyright (c) 2012 Business Labs. All rights reserved.
//

#import "TraderAddViewController.h"
#import "SBJson.h"
#import "NSString+MD5.h"
#import "AppDelegate.h"

@implementation TraderAddViewController

-(IBAction)textFieldReturn:(id)sender
{
    [sender resignFirstResponder];
}

-(BOOL)textFieldShouldReturn:(UITextField*)textField;
{
    NSInteger nextTag = textField.tag + 1;
    UIResponder* nextResponder = [textField.superview viewWithTag:nextTag];
    if (nextResponder) {
        [nextResponder becomeFirstResponder];
    } else {
        [textField resignFirstResponder];
    }
    return NO;
}

- (IBAction)addTrader:(id)sender
{
    if (![self stringIsValidEmail:email.text]) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"No valid e-mail address found." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alert show]; 
    } else if ([password.text length] < 6) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Password must contain at least 6 characters." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alert show]; 
    } else {
        AppDelegate *appDelegate = (AppDelegate *) [[UIApplication sharedApplication] delegate];
        NSString *url = [NSString stringWithFormat:@"%@/servlets/administration", appDelegate.serverUrl];
        NSMutableURLRequest *request =[NSMutableURLRequest requestWithURL:[NSURL URLWithString:url]];
        [request setHTTPMethod:@"POST"];
        
        SBJsonParser *parser = [[SBJsonParser alloc] init];
        
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
        
        NSString *hashedPassword = [password.text MD5];
        
        NSString *post = [NSString stringWithFormat:
                          @"action=addTrader&email=%@&password=%@&lastName=%@&firstName=%@&usrStatus=%@&usrRight=%@", 
                          email.text, hashedPassword, lastName.text, firstName.text, currentStatus, currentUsrGroup];
        [request setHTTPBody:[post dataUsingEncoding:NSUTF8StringEncoding]];
        
        NSURLResponse *response;
        NSError *err;
        NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&err];
        
        NSString *json_string = [[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding];
        NSDictionary *data = (NSDictionary *) [parser objectWithString:json_string error:nil];
        NSString *error = [data objectForKey:@"error"];
                
        if(error == nil) {
            //facilitates add of next new trader
            lastName.text = nil;
            firstName.text = nil;
            password.text = nil;
            email.text = nil;
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Hurray" message:@"Trader was successfully added." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [alert show];
        } else {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:error delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [alert show];
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
