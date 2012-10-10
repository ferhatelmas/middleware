//
//  customUITextField.h
//  MockStockApp
//
//  Created by Kenny Lienhard on 5/23/12.
//  Copyright (c) 2012 Business Labs. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface customUITextField : UITextField

- (CGRect)textRectForBounds:(CGRect)bounds;
- (CGRect)editingRectForBounds:(CGRect)bounds;

@end
