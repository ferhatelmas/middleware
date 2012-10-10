//
//  Login2ViewController.h
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/22/12.
//  Copyright (c) 2012 Business Labs. All rights reserved.
//

#import "Login2ViewController.h"
#import "NSString+MD5.h"
#import "SBJson.h"
#import "AppDelegate.h"
#import <QuartzCore/QuartzCore.h>

@implementation Login2ViewController
@synthesize signUp;

/*
 -(IBAction)signUp:(id)sender 
 {
 [self performSegueWithIdentifier:@"signUp" sender:self];
 }
 */

-(IBAction)signIn:(id)sender
{
    if (![self stringIsValidEmail:email.text]) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"No valid e-mail address found." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alert show];
    } else if ([password.text length] == 0) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Please provide your password." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alert show];
    } else {
        AppDelegate *appDelegate = (AppDelegate *) [[UIApplication sharedApplication] delegate];
        NSString *hashedPassword = [password.text MD5];
        SBJsonParser *parser = [[SBJsonParser alloc] init];
        NSString *loginUrl = [[NSString alloc] initWithFormat:@"%@servlets/enter?action=check&email=%@&password=%@", appDelegate.serverUrl, email.text, hashedPassword];
        NSData* data = [NSData dataWithContentsOfURL:
                        [NSURL URLWithString: loginUrl]];
        NSString *json_string = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
        NSDictionary *response = (NSDictionary *) [parser objectWithString:json_string error:nil];
        
        NSString *error = [response objectForKey:@"error"];
        
        if(error == nil) {
            appDelegate.loginData = response;
            if ([[response objectForKey:@"usrGroup"] isEqualToString:@"ADMIN"]) {
                [self performSegueWithIdentifier:@"goAdmin" sender:self];
            } else {
                [self performSegueWithIdentifier:@"goUser" sender:self];
            }
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

#pragma mark - View lifecycle

/*
 // Implement loadView to create a view hierarchy programmatically, without using a nib.
 - (void)loadView
 {
 }
 */

- (void)viewDidLoad
{
    signUp.layer.cornerRadius = 4.0;
    signUp.layer.borderWidth = 0.5;
    [super viewDidLoad];
}

- (void)viewDidUnload
{
    [self setSignUp:nil];
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
