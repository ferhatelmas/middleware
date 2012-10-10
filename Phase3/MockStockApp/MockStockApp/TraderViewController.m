//
//  TraderViewController.m
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/10/12.
//  Copyright (c) 2012 Business Labs. All rights reserved.
//

#import "TraderViewController.h"
#import <QuartzCore/QuartzCore.h>
#import "AppDelegate.h"

@implementation TraderViewController

static NSTimer *timer;
@synthesize buyButton;
@synthesize sellButton;

-(IBAction)textFieldReturn:(id)sender
{
    [sender resignFirstResponder];
}

-(void)fetchPortfolio 
{
    SBJsonParser *parser = [[SBJsonParser alloc] init];
    AppDelegate *appDelegate = (AppDelegate *) [[UIApplication sharedApplication] delegate];
    NSString *url = [[NSString alloc] initWithFormat:@"%@servlets/trading?action=getPortfolioProducts", 
                     appDelegate.serverUrl];
    NSData* data = [NSData dataWithContentsOfURL:
                    [NSURL URLWithString: url]];
    NSString *json_string = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
    portfolio = [parser objectWithString:json_string error:nil];
    [self updatePortfolio:portfolio];
}

-(void)updatePortfolio:(NSArray *)currentPortfolio
{
    NSNumberFormatter *numberFormatter = [[NSNumberFormatter alloc] init];
    double traderResult = 0;
    for (int i = 0; i < [currentPortfolio count]; i++)
    {
        NSDictionary *product = [currentPortfolio objectAtIndex:i];
        NSString *name = [[product objectForKey:@"stockProduct"] objectForKey:@"name"];
        if([name isEqualToString:@"Sun"]) {
            sunQuantity.text = [NSString stringWithFormat:@"%@ stocks", [product objectForKey:@"quantity"]];
            NSNumber *resultNumber = [numberFormatter numberFromString:[NSString stringWithFormat:@"%@", [product objectForKey:@"stockResult"]]];
            sunResult.text = [NSString stringWithFormat:@"$%@", [product objectForKey:@"stockResult"]];
            traderResult += [resultNumber doubleValue];
        } else if([name isEqualToString:@"Apple"]) {  
            appleQuantity.text = [NSString stringWithFormat:@"%@ stocks", [product objectForKey:@"quantity"]];
            NSNumber *resultNumber = [numberFormatter numberFromString:[NSString stringWithFormat:@"%@", [product objectForKey:@"stockResult"]]];
            appleResult.text = [NSString stringWithFormat:@"$%@", [product objectForKey:@"stockResult"]];
            traderResult += [resultNumber doubleValue];
        } else if([name isEqualToString:@"IBM"]) {  
            ibmQuantity.text = [NSString stringWithFormat:@"%@ stocks", [product objectForKey:@"quantity"]];
            NSNumber *resultNumber = [numberFormatter numberFromString:[NSString stringWithFormat:@"%@", [product objectForKey:@"stockResult"]]];
            ibmResult.text = [NSString stringWithFormat:@"$%@", [product objectForKey:@"stockResult"]];
            traderResult += [resultNumber doubleValue];
        }
    }
    [numberFormatter setMaximumFractionDigits:2];
    [numberFormatter setRoundingMode:NSNumberFormatterRoundHalfUp];
    totalResult.text = [NSString stringWithFormat:@"$%@", [numberFormatter stringFromNumber:[NSNumber numberWithDouble:traderResult]]];
    
}

-(void)fetchMarketData 
{
    SBJsonParser *parser = [[SBJsonParser alloc] init];
    AppDelegate *appDelegate = (AppDelegate *) [[UIApplication sharedApplication] delegate];
    NSString *urlProduct = [[NSString alloc] initWithFormat:@"%@servlets/trading?action=getStockProducts", 
                            appDelegate.serverUrl];
    NSString *urlPrice = [[NSString alloc] initWithFormat:@"%@servlets/trading?action=getStockDiffs", 
                          appDelegate.serverUrl];
    NSData* productData = [NSData dataWithContentsOfURL:
                           [NSURL URLWithString: urlProduct]];
    NSString *jsonProductString = [[NSString alloc] initWithData:productData encoding:NSUTF8StringEncoding];
    NSData* priceData = [NSData dataWithContentsOfURL:
                         [NSURL URLWithString: urlPrice]];
    NSString *jsonPriceString = [[NSString alloc] initWithData:priceData encoding:NSUTF8StringEncoding];
    
    stockProducts = [parser objectWithString:jsonProductString error:nil]; 
    priceDifferences = [parser objectWithString:jsonPriceString error:nil];
    
    for (int i = 0; i < [stockProducts count]; i++) {
        NSDictionary *product = [stockProducts objectAtIndex:i];
        NSDictionary *priceUpdate = [priceDifferences objectAtIndex:i];
        if([[product objectForKey:@"name"] isEqualToString:@"Sun"]) {  
            sunPrice.text = [NSString stringWithFormat:@"$%@", [product objectForKey:@"price"]];
            sunDiff.text = [NSString stringWithFormat:@"%@", [priceUpdate objectForKey:@"name"]];
            if ([sunDiff.text rangeOfString:@"+"].location == NSNotFound) {
                sunDiff.textColor = [UIColor redColor];
            } else {
                sunDiff.textColor = [UIColor colorWithRed:0.0 green:163.0/255.0 blue:0.0 alpha:1];
            }
        } else if([[product objectForKey:@"name"] isEqualToString:@"Apple"]) {  
            applePrice.text = [NSString stringWithFormat:@"$%@", [product objectForKey:@"price"]];
            appleDiff.text = [NSString stringWithFormat:@"%@", [priceUpdate objectForKey:@"name"]];
            if ([appleDiff.text rangeOfString:@"+"].location == NSNotFound) {
                appleDiff.textColor = [UIColor redColor];
            } else {
                appleDiff.textColor = [UIColor colorWithRed:0.0 green:163.0/255.0 blue:0.0 alpha:1];
            }
        } else if([[product objectForKey:@"name"] isEqualToString:@"IBM"]) {  
            ibmPrice.text = [NSString stringWithFormat:@"$%@", [product objectForKey:@"price"]];
            ibmDiff.text = [NSString stringWithFormat:@"%@", [priceUpdate objectForKey:@"name"]];
            if ([ibmDiff.text rangeOfString:@"+"].location == NSNotFound) {
                ibmDiff.textColor = [UIColor redColor];
            } else {
                ibmDiff.textColor = [UIColor colorWithRed:0.0 green:163.0/255.0 blue:0.0 alpha:1];            }        }
    }
    
}

-(IBAction)buyTransaction:(id)sender
{
    [quantity resignFirstResponder];
    if([sender isKindOfClass:[UIButton class]]){
        if ([quantity.text isEqualToString:@"0"] || [quantity.text isEqualToString:@""]) {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Quantity is not valid" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
			[alert show];
        } else {
            SBJsonParser *parser = [[SBJsonParser alloc] init];
            AppDelegate *appDelegate = (AppDelegate *) [[UIApplication sharedApplication] delegate];
            NSDate *date = [NSDate date];
            double timestamp = [date timeIntervalSince1970] * -1000;
            NSString *stockname;
            if (stock.selectedSegmentIndex == 0) {
                stockname = @"Sun";
            } else if (stock.selectedSegmentIndex == 1) {
                stockname = @"Apple";
            } else stockname = @"IBM";
            NSString *url = [[NSString alloc] initWithFormat:@"%@servlets/trading", appDelegate.serverUrl];
            NSMutableURLRequest *request = 
            [NSMutableURLRequest requestWithURL:
             [NSURL URLWithString:url]];
            [request setHTTPMethod:@"POST"];
            NSString *post = [NSString stringWithFormat:@"action=createTransaction&stock=%@&quantity=%@&type=BUY&date=%d", stockname, quantity.text, timestamp];
            [request setHTTPBody:[post dataUsingEncoding:NSUTF8StringEncoding]];
            NSURLResponse *response;
            NSError *err;
            NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&err];
            
            NSString *json_string = [[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding];
            NSDictionary *data = (NSDictionary *) [parser objectWithString:json_string error:nil];
            
            
            NSString *error = [data objectForKey:@"error"];
            
            if(error == nil) {
                NSArray *currentPortfolio = [data objectForKey:@"portfolio"];
                [self updatePortfolio:currentPortfolio];
                quantity.text = nil;
                /*
                NSArray *currentPortfolio = [data objectForKey:@"portfolio"];
                for (int i = 0; i < [currentPortfolio count]; i++)
                {
                    NSDictionary *product = [currentPortfolio objectAtIndex:i];
                    NSString *name = [[product objectForKey:@"stockProduct"] objectForKey:@"name"];
                    if([name isEqualToString:@"Sun"]) {  
                        sunQuantity.text = [NSString stringWithFormat:@"%@", [product objectForKey:@"quantity"]];
                        sunResult.text = [NSString stringWithFormat:@"$%@", [product objectForKey:@"stockResult"]];
                    } else if([name isEqualToString:@"Apple"]) {  
                        appleQuantity.text = [NSString stringWithFormat:@"%@", [product objectForKey:@"quantity"]];
                        appleResult.text = [NSString stringWithFormat:@"$%@", [product objectForKey:@"stockResult"]];
                    } else if([name isEqualToString:@"IBM"]) {  
                        ibmQuantity.text = [NSString stringWithFormat:@"%@", [product objectForKey:@"quantity"]];
                        ibmResult.text = [NSString stringWithFormat:@"$%@", [product objectForKey:@"stockResult"]];
                    }
                }
                totalResult.text = [NSString stringWithFormat:@"$%@", [data objectForKey:@"totalResult"]];
                quantity.text = nil;
                 */
            } else {
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:error delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                [alert show];
            }  
        }
	}
}

-(IBAction)sellTransaction:(id)sender
{
    [quantity resignFirstResponder];
    if([sender isKindOfClass:[UIButton class]]){
        if ([quantity.text isEqualToString:@"0"] || [quantity.text isEqualToString:@""]) {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Quantity is not valid" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
			[alert show];
        } else {
            SBJsonParser *parser = [[SBJsonParser alloc] init];
            AppDelegate *appDelegate = (AppDelegate *) [[UIApplication sharedApplication] delegate];
            NSDate *date = [NSDate date];
            double timestamp = [date timeIntervalSince1970] * -1000;
            NSString *stockname;
            if (stock.selectedSegmentIndex == 0) stockname = @"Sun";
            else if (stock.selectedSegmentIndex == 1) stockname = @"Apple";
            else stockname = @"IBM";
            NSString *url = [[NSString alloc] initWithFormat:@"%@servlets/trading", appDelegate.serverUrl];
            NSMutableURLRequest *request = 
            [NSMutableURLRequest requestWithURL:
             [NSURL URLWithString:url]];
            [request setHTTPMethod:@"POST"];
            NSString *post = [NSString stringWithFormat:@"action=createTransaction&stock=%@&quantity=%@&type=SELL&date=%d", stockname, quantity.text, timestamp];
            [request setHTTPBody:[post dataUsingEncoding:NSUTF8StringEncoding]];
            NSURLResponse *response;
            NSError *err;
            NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&err];
            
            NSString *json_string = [[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding];
            NSDictionary *data = (NSDictionary *) [parser objectWithString:json_string error:nil];
            
            
            NSString *error = [data objectForKey:@"error"];
            
            if(error == nil) {
                NSArray *currentPortfolio = [data objectForKey:@"portfolio"];
                [self updatePortfolio:currentPortfolio];
                quantity.text = nil;
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

/*
 // Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
 - (void)viewDidLoad
 {
 [super viewDidLoad];
 }
 */

- (void)viewDidUnload
{
    sunPrice = nil;
    sunDiff = nil;
    applePrice = nil;
    appleDiff = nil;
    ibmPrice = nil;
    ibmDiff = nil;
    [self setBuyButton:nil];
    [self setSellButton:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillDisappear:(BOOL)animated 
{
    //do not load market updates when view is not active
    [timer invalidate];
    timer = nil;
}
- (void)viewWillAppear:(BOOL)animated
{
    buyButton.layer.cornerRadius = 4.0;
    buyButton.layer.borderWidth = 0.5;
    sellButton.layer.cornerRadius = 4.0;
    sellButton.layer.borderWidth = 0.5;
    [self fetchPortfolio];
    [self fetchMarketData];
    if (timer == nil)
    timer = [NSTimer scheduledTimerWithTimeInterval:6.0 target:self selector:@selector(fetchMarketData) userInfo:nil repeats:YES];
    [super viewWillAppear:animated];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

@end
