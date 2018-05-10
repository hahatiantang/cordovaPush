# cordova-plugin-aliyunpush

# cordova-plugin-aliyunpush / Cordova Plugin

## Install

> 注意：
> - 应用的包名一定要和 APP_KEY 对应应用的包名一致，否则推送服务无法注册成功。
> - 在使用 8 或以上版本的 Xcode 调试 iOS 项目时，需要先在项目配置界面的 Capabilities 中打开 Push Notifications 开关。
> - ANDROID_KEY:对应的android key
> - ANDROID_SECRET:ANDROID SECRET
> - IOS_KEY:ios key
> - IOS_SECRET:ios SECRET

- 通过 Cordova Plugins 安装，要求 Cordova CLI 5.0+：

  ```shell
  cordova plugin add cordova-plugin-aliyunpush --variable ANDROID_KEY=${ANDROID_KEY} --variable ANDROID_SECRET=${ANDROID_SECRET} --variable IOS_KEY=${IOS_KEY} --variable IOS_SECRET=${IOS_SECRET}
  ```

- 或下载到本地安装：

  ```shell
  cordova plugin add Your_Plugin_Path --variable ANDROID_KEY=${ANDROID_KEY} --variable ANDROID_SECRET=${ANDROID_SECRET} --variable IOS_KEY=${IOS_KEY} --variable IOS_SECRET=${IOS_SECRET}
  ```


## Usage

### API

```
  /**
   *获取设备唯一标识deviceId，deviceId为阿里云移动推送过程中对设备的唯一标识（并不是设备UUID/UDID）
   */
  getRegisterId(): Promise < any > ;

  /**
   * 阿里云推送绑定账号名
   * @param  {string}  account 登录账号
   * @return {Promise}         [description]
   */
  bindAccount(account: string): Promise < any > ;

  /**
   * 阿里云推送绑定标签
   * @param  {string[]} tags 绑定标签，数组格式
   * @return {Promise}       [description]
   */
  bindTags(tags: string[]): Promise < any > ;

  /**
   * 阿里云推送解除绑定标签
   * @param  {string[]} tags 绑定标签，数组格式
   * @return {Promise}       [description]
   */
  unbindTags(tags: string[]): Promise < any > ;

  /**
   * 阿里云推送解除绑定标签
   * @return {Promise} [description]
   */
  listTags(): Promise < string[] > ;

  /**
   * 阿里云接收推送通知回调
   * @return {Promise} [description]
   */
  onNotification(): Promise < string[] > ;

  /**
   * 阿里云推送通知点击回调
   * @return {Promise} [description]
   */
  onNotificationOpened(): Promise < string[] > ;

  /**
   * 阿里云推送消息透传回调
   * @return {Promise} [description]
   */
  onMessage(): Promise < string[] > ;
```


