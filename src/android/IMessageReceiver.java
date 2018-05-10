package com.leo.aliyunpush;

/**
 * Created by leo on 2018/4/23.
 */

import android.content.Context;
import android.util.Log;
import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;

import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class IMessageReceiver extends MessageReceiver {
    /** LOG TAG */
    private static final String LOG_TAG = IMessageReceiver.class.getSimpleName();
    /** 回调类型 */
    private static final String ONMESSAGE="onMessage";
    private static final String ONNOTIFICATION="onNotification";
    private static final String ONNOTIFICATIONOPENED="onNotificationOpened";
    private static final String ONNOTIFICATIONRECEIVED="onNotificationReceived";
    private static final String ONNOTIFICATIONREMOVED="onNotificationRemoved";

    /**
     * 阿里云推送通知接收回调
     */
    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
//       Log.i(LOG_TAG, "Receive notification, title: " + title + ", summary: " + summary + ", extraMap: " + extraMap);
        Log.d(LOG_TAG, "AliyunPushReceiver#onNotification");

        try {
            JSONObject data = null;
            if (extraMap != null && !"".equals(extraMap)) {
                data = new JSONObject(extraMap);
            } else {
                data = new JSONObject();
            }
            setStringData(data, "title", title);
            setStringData(data, "content", summary);
            AliyunPush.sendPushData(ONNOTIFICATION,data);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
//       Log.i(LOG_TAG, "onMessage, messageId: " + cPushMessage.getMessageId() + ", title: " + cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());
        Log.d(LOG_TAG, "AliyunReceiver#onMessage");

        try {
            JSONObject data = new JSONObject();
            setStringData(data, "messageType", cPushMessage.getMessageId());
            setStringData(data, "title", cPushMessage.getMessageId());
            setStringData(data, "content", cPushMessage.getMessageId());
            AliyunPush.sendPushData(ONMESSAGE,data);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    /**
     * 阿里云推送通知点击回调
     */
    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
//       Log.i(LOG_TAG, "onNotificationOpened, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
        Log.d(LOG_TAG, "AliyunPushReceiver#onNotificationOpened");

        try {
            JSONObject data = null;
            if (extraMap != null && !"".equals(extraMap)) {
                data = new JSONObject(extraMap);
            } else {
                data = new JSONObject();
            }
            setStringData(data, "title", title);
            setStringData(data, "content", summary);

            AliyunPush.sendPushData(ONNOTIFICATIONOPENED,data);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
//       Log.i(LOG_TAG, "onNotificationClickedWithNoAction, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
        try {
            JSONObject data = null;
            if (extraMap != null && !"".equals(extraMap)) {
                data = new JSONObject(extraMap);
            } else {
                data = new JSONObject();
            }
            setStringData(data, "title", title);
            setStringData(data, "content", summary);

            AliyunPush.sendPushData(ONNOTIFICATIONOPENED,data);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    /**
     * 阿里云推送通知接收回调
     */
    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
//       Log.i(LOG_TAG, "onNotificationReceivedInApp, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap + ", openType:" + openType + ", openActivity:" + openActivity + ", openUrl:" + openUrl);
        Log.d(LOG_TAG, "AliyunPushReceiver#onNotificationReceivedInApp");

        try {
            JSONObject data = null;
            if (extraMap != null && !"".equals(extraMap)) {
                data = new JSONObject(extraMap);
            } else {
                data = new JSONObject();
            }
            setStringData(data, "title", title);
            setStringData(data, "content", summary);
            setStringData(data, "openType", title);
            setStringData(data, "openActivity", summary);
            setStringData(data, "openUrl", summary);
            AliyunPush.sendPushData(ONNOTIFICATIONRECEIVED,data);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    @Override
    protected void onNotificationRemoved(Context context, String messageId) {
//       Log.i(LOG_TAG, "onNotificationRemoved");
        Log.d(LOG_TAG, "AliyunPushReceiver#onNotificationRemoved");

        try {
            JSONObject data = new JSONObject();
            setStringData(data, "messageType", messageId);
            AliyunPush.sendPushData(ONNOTIFICATIONREMOVED,data);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    /**
     * 接收推送内容并返回给前端JS
     *
     * @param jsonObject JSON对象
     */
    private void sendPushData(JSONObject jsonObject) {
        Log.d(LOG_TAG, "AliyunPushReceiver#sendPushData: " + (jsonObject != null ? jsonObject.toString() : "null"));

        if (AliyunPush.pushCallbackContext != null) {
            PluginResult result = new PluginResult(PluginResult.Status.OK, jsonObject);
            result.setKeepCallback(true);
            AliyunPush.pushCallbackContext.sendPluginResult(result);
        }
    }

    /**
     * 设定字符串类型JSON对象，如值为空时不设定
     *
     * @param jsonObject JSON对象
     * @param name 关键字
     * @param value 值
     * @throws JSONException JSON异常
     */
    private void setStringData(JSONObject jsonObject, String name, String value) throws JSONException {
        if (value != null && !"".equals(value)) {
            jsonObject.put(name, value);
        }
    }
}