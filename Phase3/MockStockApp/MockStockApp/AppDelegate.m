//
//  AppDelegate.m
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/10/12.
//  Copyright (c) 2012 Business Labs. All rights reserved.
//

#import "AppDelegate.h"
#import "Login2ViewController.h"
#import "BonjourBrowser.h"

#define kWebServiceType @"_trading._tcp"
#define kInitialDomain  @"local"

@implementation AppDelegate

@synthesize loginData;
@synthesize window = _window;
@synthesize browser;
@synthesize serverUrl;
@synthesize sb;

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    // Override point for customization after application launch.
    
    /* we need this trick otherwise storyboard always empty, couldnt find correct bundle name, tried 
     nil, mainbundle and unil.eda.gr12.MockStockApp */
    self.sb = self.window.rootViewController.storyboard;
    
	BonjourBrowser *aBrowser = [[BonjourBrowser alloc] initForType:kWebServiceType
														  inDomain:kInitialDomain
													 customDomains:nil // we won't save any additional domains added by the user
										  showDisclosureIndicators:YES
												  showCancelButton:NO];
	self.browser = aBrowser;
	self.browser.delegate = self;
    self.browser.searchingForServicesString = NSLocalizedString(@"Searching for Trading servers...", @"Searching for web services string");
	
	[self.window setRootViewController:self.browser];
    return YES;
}
							
- (void)applicationWillResignActive:(UIApplication *)application
{
    /*
     Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
     Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
     */
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    
    NSString *url = [NSString stringWithFormat:@"%@servlets/trading", serverUrl];
    NSMutableURLRequest *request =[NSMutableURLRequest requestWithURL:[NSURL URLWithString:url]];
    [request setHTTPMethod:@"POST"];
    NSString *post = [NSString stringWithFormat:@"action=logout"];
    [request setHTTPBody:[post dataUsingEncoding:NSUTF8StringEncoding]];
    NSURLResponse *response;
    NSError *err;
    NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&err];
    
    NSString *json_string = [[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding];    
    NSLog(@"logout answer %@", json_string);
    //loginData = nil;

    /*
    //[self.window.rootViewController.navigationController removeFromParentViewController];
    self.window.rootViewController.navigationController.viewControllers = nil;
                                                                          

    NSLog(@"do logout in background...");

    NSString *logoutUrl = [[NSString alloc] initWithFormat:@"%@servlets/trading?action=logout", serverUrl];
    NSURL *url = [NSURL URLWithString:logoutUrl];
    
    //NSURLRequest *request = [NSURLRequest requestWithURL:url];
    //NSURLResponse *response = nil;
    
    NSData* data = [NSData dataWithContentsOfURL:
                    url];
    NSString *json_string = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
    
    // we have nothing to do in case of error
    // servlet will already timeout in 30 min, we can change the parameter to make it shorter but it is enough, I think
    //NSData *data = 
    //[NSURLConnection sendSynchronousRequest:request returningResponse:&response error:NULL];
    loginData = nil;
     */
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    UINavigationController *aunc = (UINavigationController*)[self.sb instantiateInitialViewController];
    //self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    [self.window setRootViewController:aunc];
    /*
    Login2ViewController *nextViewController=[[Login2ViewController alloc]init];
    UINavigationController *navBar=[[UINavigationController alloc]initWithRootViewController:nextViewController];
    [[self.window rootViewController] presentModalViewController:navBar animated:YES];
     */
    
    //self.sb = self.window.rootViewController.storyboard;
    
    //UIStoryboard *sb = [UIStoryboard storyboardWithName:@"MainStoryboard_iPhone" bundle:nil];
    //UIViewController *vc= [self.sb instantiateInitialViewController];
    //[self.window addSubview:vc.view];
    //[self.window makeKeyAndVisible];
    //UINavigationController *aunc = (UINavigationController*)[self.sb instantiateInitialViewController];
    //self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    //[self.window setRootViewController:aunc];
    //[self.window.rootViewController addChildViewController:<#(UIViewController *)#>]
    //[self.window makeKeyAndVisible];
    /*
    UINavigationController* navController = (UINavigationController*)[self.window.s rootViewController];
    [navController setViewControllers:[NSArray arrayWithObject:nextViewController] animated:NO];
     */
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    /*
     Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
     */
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    /*
     Called when the application is about to terminate.
     Save data if appropriate.
     See also applicationDidEnterBackground:.
     */
}

- (void) bonjourBrowser:(BonjourBrowser*)browser didResolveInstance:(NSNetService*)service {
	
    NSString *host = [service hostName];
	NSString *portStr = @"";
	
    NSInteger port = [service port];
    if (port != 0 && port != 80)
        portStr = [[NSString alloc] initWithFormat:@":%d",port];
    
    NSString* path = @"/";
    
    NSString* string = [[NSString alloc] initWithFormat:@"http://%@%@%@",
                        host,
                        portStr,
                        path];
	
	self.serverUrl = [[NSURL alloc] initWithString:string];
    
    UINavigationController *aunc = (UINavigationController*)[self.sb instantiateInitialViewController];
    
    [[self.browser view] removeFromSuperview];
    [self.window setRootViewController:aunc];
    
    // if you wanna see url
    //NSLog(@"%@", serverUrl);
}

@end
	
