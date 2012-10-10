//
//  TraderListTVC.m
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/17/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "TraderListTVC.h"
#import "SBJson.h"
#import "TraderEditViewController.h"

@implementation TraderListTVC

- (void)fetchTraders
{
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        SBJsonParser *parser = [[SBJsonParser alloc] init];
        
        //NSData* data = [NSData dataWithContentsOfURL:
                        //[NSURL URLWithString: @"https://api.twitter.com/1/statuses/public_timeline.json"]];
        NSData* data = [NSData dataWithContentsOfURL:
                        [NSURL URLWithString: @"http://localhost:8080/servlets/administration?action=getTraders"]];
        NSString *json_string = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
        //NSLog(json_string);
                
        traders = [parser objectWithString:json_string error:nil]; 
        //traders = [parser objectWithData:data];
        //NSLog(@"length: %d", traders.count);
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.tableView reloadData];
        });
    });
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([segue.identifier isEqualToString:@"editTrader"]) {
        
        NSInteger row = [[self tableView].indexPathForSelectedRow row];
        NSDictionary *trader = [traders objectAtIndex:row];
        
        TraderEditViewController *traderEditController = segue.destinationViewController;
        traderEditController.detailItem = trader;
    }
}

- (void)awakeFromNib
{
    [super awakeFromNib];
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
	[self fetchTraders];
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [self fetchTraders];
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

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return traders.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"trader";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    
    NSDictionary *trader = [traders objectAtIndex:indexPath.row];
    NSString *name = [NSString stringWithFormat:@"%@, %@", [trader objectForKey:@"lastName"], [trader objectForKey:@"firstName"]];
    NSString *email = [trader objectForKey:@"email"];
    //NSString *name = [[trader objectForKey:@"user"] objectForKey:@"name"];
    
    cell.textLabel.text = name;
    cell.detailTextLabel.text = email;
    
    return cell;
}

/*
 // Override to support conditional editing of the table view.
 - (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
 {
 // Return NO if you do not want the specified item to be editable.
 return YES;
 }
 */

/*
 // Override to support editing the table view.
 - (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
 {
 if (editingStyle == UITableViewCellEditingStyleDelete) {
 // Delete the row from the data source.
 [tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationFade];
 } else if (editingStyle == UITableViewCellEditingStyleInsert) {
 // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view.
 }   
 }
 */

/*
 // Override to support rearranging the table view.
 - (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
 {
 }
 */

/*
 // Override to support conditional rearranging of the table view.
 - (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
 {
 // Return NO if you do not want the item to be re-orderable.
 return YES;
 }
 */

@end

