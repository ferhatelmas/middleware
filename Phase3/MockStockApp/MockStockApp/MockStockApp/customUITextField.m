//
//  customUITextField.m
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/23/12.
//  Copyright (c) 2012 Business Labs. All rights reserved.
//

#import "customUITextField.h"
#import <QuartzCore/QuartzCore.h>


@implementation customUITextField

- (CGRect)textRectForBounds:(CGRect)bounds {
    CGRect inset = CGRectMake(bounds.origin.x + 10, bounds.origin.y, bounds.size.width - 10, bounds.size.height);
    return inset;
}

- (CGRect)editingRectForBounds:(CGRect)bounds {
    CGRect inset = CGRectMake(bounds.origin.x + 10, bounds.origin.y, bounds.size.width - 10, bounds.size.height);
    return inset;
}

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

- (void)drawRect:(CGRect)rect
{
    self.layer.cornerRadius = 4.0;
    self.layer.borderWidth = 0.5;
    [super drawRect:rect];
}

@end
