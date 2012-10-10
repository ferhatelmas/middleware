//
//  MarketIndexViewController.h
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/30/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MarketIndexViewController : UIViewController <UIWebViewDelegate> {
    
    IBOutlet UIWebView *webView;
}

@property (strong, nonatomic) id detailItem;
@property (nonatomic, retain) UIWebView *webView;

@end
