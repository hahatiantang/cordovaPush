/********* AliyunPush.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>
#import "AliyunNotificationLauncher.h"

@interface AliyunPush : CDVPlugin {

}

@end

@implementation AliyunPush

- (void)pluginInitialize{
    
    [super pluginInitialize];
    
    // 推送通知 注册
    [[NSNotificationCenter defaultCenter] addObserver:self
                                            selector:@selector(onNotificationReceived:)
                                                 name:@"AliyunNotification"
                                               object:nil];
    
    // 推送消息 注册
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(onMessageReceived:)
                                                 name:@"AliyunNotificationMessage"
                                               object:nil];
}


#pragma mark AliyunNotification通知
- (void)onNotificationReceived:(NSNotification *)notification {
    
    NSDictionary * info = notification.object;
    
    NSDictionary * message = @{@"data":info};
    // Serialise to JSON string
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:message options:(NSJSONWritingOptions) 0 error:nil];
    
    NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    
    if(info[@"body"] != [NSNull null]){
        
        NSString *requestData = [NSString stringWithFormat:@"onNotificationOpened('%@')",jsonString];
        
        [self.commandDelegate evalJs:[self NSStringToJson:requestData]];
    }
}

#pragma mark AliyunNotification消息

- (void)onMessageReceived:(NSNotification *)notification {
    
    NSDictionary * info = notification.object;
    
    NSDictionary * message = @{@"data":info};
    
    // Serialise to JSON string
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:message options:(NSJSONWritingOptions) 0 error:nil];
    
    NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    
    NSString *requestData = [NSString stringWithFormat:@"onMessage('%@')",jsonString];
    
    [self.commandDelegate evalJs:[self NSStringToJson:requestData]];

}


-(NSString *)NSStringToJson:(NSString *)str
{
    NSMutableString *s = [NSMutableString stringWithString:str];
    
    [s replaceOccurrencesOfString:@"\\" withString:@"\\\\" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [s length])];
    
    return [NSString stringWithString:s];
}


/**
 * 阿里云推送绑定账号名
 * 获取设备唯一标识deviceId，deviceId为阿里云移动推送过程中对设备的唯一标识（并不是设备UUID/UDID）
 */
- (void)getRegisterId:(CDVInvokedUrlCommand*)command{

    NSString *deviceId =  [[AliyunNotificationLauncher sharedSevenAppNotificationLauncher] getDeviceId];

    CDVPluginResult *result;
    
    if(deviceId.length != 0){
       result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:deviceId];
    }else{
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

/**
 * 阿里云推送绑定账号名
 */
- (void)bindAccount:(CDVInvokedUrlCommand*)command{
    
    NSString* account = [command.arguments objectAtIndex:0];
    
    if(account.length != 0){
     
        [[AliyunNotificationLauncher sharedSevenAppNotificationLauncher] bindAccountWithAccount:account andCallback:^(BOOL result) {
           
            CDVPluginResult *cdvresult;
            
            if(result){
                cdvresult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
            }else{
                cdvresult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
            }
            
            [self.commandDelegate sendPluginResult:cdvresult callbackId:command.callbackId];

        }];
    }

}


/**
 *绑定标签
 */
- (void)bindTags:(CDVInvokedUrlCommand*)command{
    NSArray *tags = [command.arguments objectAtIndex:0];
    
    [[AliyunNotificationLauncher sharedSevenAppNotificationLauncher] bindTagsWithTags:tags andCallback:^(BOOL result) {
      
        CDVPluginResult *cdvresult;
        
        if(result){
            cdvresult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        }else{
            cdvresult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
        }
        
        [self.commandDelegate sendPluginResult:cdvresult callbackId:command.callbackId];
        
    }];
}

/**
 *解绑定标签
 */
- (void)unbindTags:(CDVInvokedUrlCommand*)command{
    NSArray *tags = [command.arguments objectAtIndex:0];

    [[AliyunNotificationLauncher sharedSevenAppNotificationLauncher] unbindTagsWithTags:tags andCallback:^(BOOL result) {
        
        CDVPluginResult *cdvresult;
        
        if(result){
            cdvresult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        }else{
            cdvresult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
        }
        
        [self.commandDelegate sendPluginResult:cdvresult callbackId:command.callbackId];
        
    }];
    
}

/**
 *查询标签
 */
- (void)listTags:(CDVInvokedUrlCommand*)command{
    
    [[AliyunNotificationLauncher sharedSevenAppNotificationLauncher] listTagsAndCallback:^(id result) {
       
        CDVPluginResult *cdvresult;
        
        if(result == [NSNull null] ){
            
            cdvresult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
        }else{
            cdvresult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:(NSDictionary *)result];
        }
        
        [self.commandDelegate sendPluginResult:cdvresult callbackId:command.callbackId];
        
    }];
    
}




@end






