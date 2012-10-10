//
//  SignUpViewController.m
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/24/12.
//  Copyright (c) 2012 Business Labs. All rights reserved.
//

#import "SignUpViewController.h"
#import "NSString+MD5.h"
#import "SBJson.h"
#import "AppDelegate.h"

@implementation SignUpViewController

-(IBAction)signUp:(id)sender
{
    if (![self stringIsValidEmail:email.text]) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"No valid e-mail address found." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alert show]; 
    } else if ([password.text length] < 6) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Password must contain at least 6 characters." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alert show]; 
    } else {
        
       // [self emailConfirmation];
        
        AppDelegate *appDelegate = (AppDelegate *) [[UIApplication sharedApplication] delegate];
        NSString *signUpUrl = [[NSString alloc] initWithFormat:@"%@servlets/enter?", appDelegate.serverUrl];
        NSMutableURLRequest *request =[NSMutableURLRequest requestWithURL:[NSURL URLWithString:signUpUrl]];
        [request setHTTPMethod:@"POST"];
        
        SBJsonParser *parser = [[SBJsonParser alloc] init];
        
        NSString *hashedPassword = [password.text MD5];
        
        NSString *post = [NSString stringWithFormat:
                          @"action=signup&firstName=%@&lastName=%@&email=%@&password=%@", 
                          firstName.text, lastName.text, email.text, hashedPassword];
        [request setHTTPBody:[post dataUsingEncoding:NSUTF8StringEncoding]];
        
        NSURLResponse *response;
        NSError *err;
        NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&err];
        
        NSString *json_string = [[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding];
        NSDictionary *data = (NSDictionary *) [parser objectWithString:json_string error:nil];
        
        NSString *error = [data objectForKey:@"error"];
        
        if(error == nil) {
            appDelegate.loginData = data;
            [self performSegueWithIdentifier:@"signUpUser" sender:self];
        } 
        else {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:error delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [alert show];
        }
    }
}


//
//-(void)emailConfirmation
//{
//    // check what if the device is able to send emails!
//    
//    if ([MFMailComposeViewController canSendMail])
//    {
//        
//       // MFMailComposeViewController *mailer = [[MFMailComposeViewController alloc] init];
//        MFMailComposeViewController *mailer = [[MFMailComposeViewController alloc] init];
//        
//        mailer.mailComposeDelegate = self;
//        
//        [mailer setSubject:@"Welcome to MockStock"];
//        
//        NSArray *toRecipients = [NSArray arrayWithObjects:@"%@",email.text,"benoudina@gmail.com", nil];
//        [mailer setToRecipients:toRecipients];
//        
//        UIImage *myImage = [UIImage imageNamed:@"Default.png"];
//        NSData *imageData = UIImagePNGRepresentation(myImage);
//        [mailer addAttachmentData:imageData mimeType:@"image/png" fileName:@"Default"]; 
//        
//        NSString *emailBody = @"Congratulations !! you've been signUp with success in MockStock ?";
//        [mailer setMessageBody:emailBody isHTML:NO];
//        
//        [self presentModalViewController:mailer animated:NO];
//        
//        
//    }
//    else
//    {
//        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failure"
//                                                        message:@"Your device doesn't support the composer sheet"
//                                                       delegate:nil
//                                              cancelButtonTitle:@"OK"
//                                              otherButtonTitles:nil];
//        [alert show];
//    }
//}


-(BOOL)stringIsValidEmail:(NSString *)checkString
{
    BOOL stricterFilter = YES; 
    NSString *stricterFilterString = @"[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
    NSString *laxString = @".+@.+\\.[A-Za-z]{2}[A-Za-z]*";
    NSString *emailRegex = stricterFilter ? stricterFilterString : laxString;
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", emailRegex];
    return [emailTest evaluateWithObject:checkString];
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

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        
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
 }
 */

- (void)viewDidUnload
{
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
