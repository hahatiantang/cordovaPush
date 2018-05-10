var exec = require('cordova/exec');

var AliyunPush = {
  registered: false,
  errorCallback: function(msg) {
    console.log('AliyunPush Callback Error: ' + msg)
  },

  callNative: function(name, args, successCallback, errorCallback) {
    if (errorCallback) {
      cordova.exec(successCallback, errorCallback, 'JPushPlugin', name, args)
    } else {
      cordova.exec(successCallback, this.errorCallback, 'JPushPlugin', name, args)
    }
  },

  /**
   *获取设备唯一标识deviceId，deviceId为阿里云移动推送过程中对设备的唯一标识（并不是设备UUID/UDID）
   */
  getRegisterId: function(successCallback) {
    this.callNative('getRegisterId', [], successCallback);
  },

  /**
   * 阿里云推送绑定账号名
   */
  bindAccount: function(account, successCallback, errorCallback) {
    this.callNative('bindAccount', [account], successCallback, errorCallback);
  },

  /**
   * 阿里云推送绑定标签
   */
  bindTags: function(tags, successCallback, errorCallback) {
    this.callNative('bindTags', [tags], successCallback, errorCallback)
  },

  /**
   * 阿里云推送解除绑定标签
   */
  unbindTags: function(tags, successCallback, errorCallback) {
    this.callNative('unbindTags', [tags], successCallback, errorCallback)
  },

  /**
   * 阿里云推送解除绑定标签
   */
  listTags: function(successCallback) {
    this.callNative('listTags', [], successCallback)
  },

  /**
   * 阿里云接收推送通知回调
   */
  onNotification: {},

  /**
   * 阿里云推送通知点击回调
   */
  onNotificationOpened: {},

  /**
   * 阿里云推送消息透传回调
   */
  onMessage: {},

  AliyunPush: AliyunPush
}
module.exports = AliyunPush;
