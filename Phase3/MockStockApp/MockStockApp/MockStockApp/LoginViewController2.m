//
//  LoginViewController.m
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/10/12.
//  Copyright (c) 2012 Business Labs. All rights reserved.
//

#import "LoginViewController2.h"

@implementation LoginViewController2
@synthesize email;
@synthesize password;
@synthesize webData;
@synthesize message;

-(IBAction)buttonClickLogin:(id)sender
{
    
	NSString* email_ = email.text;
	NSString* password_ = password.text;
	
	if([email.text isEqualToString:@"" ]|| [password.text isEqualToString:@""]) 
	{
		
		message.text = @"Input your email & password";
		[email resignFirstResponder];
		[password resignFirstResponder];
		return;
	}
	
	NSString *post = 
	[[NSString alloc] initWithFormat:@"username=%@&password=%@",email_,password_];
	
	NSData *postData = [post dataUsingEncoding:NSASCIIStringEncoding allowLossyConversion:YES];  
	
	NSString *postLength = [NSString stringWithFormat:@"%d", [postData length]];  
	
	NSURL *url = [NSURL URLWithString:@"url-address"];
	NSMutableURLRequest *theRequest = [NSMutableURLRequest requestWithURL:url];
	[theRequest setHTTPMethod:@"POST"];  
	[theRequest setValue:postLength forHTTPHeaderField:@"Content-Length"];  
	[theRequest setHTTPBody:postData];	
	
	
	NSURLConnection *theConnection = [[NSURLConnection alloc] initWithRequest:theRequest delegate:self];
	
	if( theConnection )
	{
		//webData = [[NSMutableData data] retain];
	}
	else
	{
		
	}
	
	[email resignFirstResponder];
	[password resignFirstResponder];
	email.text = nil;
	password.text = nil;
	
	
}

-(void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response
{
	[webData setLength: 0];
}
-(void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
{
	
	
	[webData appendData:data];
}
-(void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error
{
	
	//[connection release];
	//[webData release];
}
-(void)connectionDidFinishLoading:(NSURLConnection *)connection
{
	
	NSString *loginStatus = [[NSString alloc] initWithBytes: [webData mutableBytes] length:[webData length] encoding:NSUTF8StringEncoding];
	//NSLog(loginStatus);
	message.text = loginStatus;
	//[loginStatus release];
	
	
	
	//[connection release];
	//[webData release];
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
}
*/

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

- (void)dealloc {
    //[super dealloc];
}

@end
