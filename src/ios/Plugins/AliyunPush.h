//
//  AliyunPush.h
//  PushCordova
//
//  Created by ☺strum☺ on 2018/4/11.
//
#import <Cordova/CDV.h>

@interface AliyunPush : CDVPlugin


/**
 * 阿里云推送绑定账号名
 * 获取设备唯一标识deviceId，deviceId为阿里云移动推送过程中对设备的唯一标识（并不是设备UUID/UDID）
 */
- (void)getRegisterId:(CDVInvokedUrlCommand*)command;

/**
 * 阿里云推送绑定账号名
 */
- (void)bindAccount:(CDVInvokedUrlCommand*)command;

/**
 *绑定标签
 */
- (void)bindTags:(CDVInvokedUrlCommand*)command;

/**
 *解绑定标签
 */
- (void)unbindTags:(CDVInvokedUrlCommand*)command;

/**
 *查询标签
 */
- (void)listTags:(CDVInvokedUrlCommand*)command;

@end
