//
//  MarketIndexViewController.m
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/30/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "MarketIndexViewController.h"

@interface MarketIndexViewController ()
- (void)configureView;
@end

@implementation MarketIndexViewController

@synthesize detailItem = _detailItem;
@synthesize webView;

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
        NSString *fullURL;
        NSDictionary *stock = self.detailItem;
        if ([[stock objectForKey:@"name"] isEqualToString:@"Sun"]) {
           fullURL = @"http://localhost:8080/user/analyticsmobsun.xhtml";
        } else if ([[stock objectForKey:@"name"] isEqualToString:@"Apple"]) {
            fullURL = @"http://localhost:8080/user/analyticsmobapple.xhtml";
        } else if ([[stock objectForKey:@"name"] isEqualToString:@"IBM"]) {
            fullURL = @"http://localhost:8080/user/analyticsmobibm.xhtml";
        } else {
            fullURL = @"http://localhost:8080/errormessage.xhtml";
        }
        NSURL *url = [NSURL URLWithString:fullURL];
        NSURLRequest *requestObj = [NSURLRequest requestWithURL:url];
        [webView loadRequest:requestObj];
    }
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
    [super viewDidLoad];
    [self configureView];
}


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
