package com.leo.aliyunpush;

/**
 * Created by leo on 2018/4/23.
 */

import android.app.Activity;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.LOG;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AliyunPush extends CordovaPlugin {
    /** LOG TAG */
    private static final String LOG_TAG = AliyunPush.class.getSimpleName();

    /** JS回调接口对象 */
    public static CallbackContext pushCallbackContext = null;

    private static AliyunPush instance;

    private static Activity cordovaActivity;

    final CloudPushService pushService = PushServiceFactory.getCloudPushService();

    public AliyunPush() {
        instance = this;
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        LOG.d(LOG_TAG, "AliyunPush#initialize");
        super.initialize(cordova, webView);
        cordovaActivity = cordova.getActivity();
    }

    /**
     * 插件主入口
     */
    @Override
    public boolean execute(final String action, final JSONArray args,
                           final CallbackContext callbackContext) throws JSONException {
        LOG.d(LOG_TAG, "AliyunPush#execute");

        boolean ret = false;

        pushCallbackContext = callbackContext;

        if ("getRegisterId".equalsIgnoreCase(action)) {
            callbackContext.success(pushService.getDeviceId());
            ret =  true;
        }else if ("bindAccount".equalsIgnoreCase(action)) {
            final String account=args.getString(0);
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    LOG.d(LOG_TAG, "PushManager#bindAccount");
                        pushService.bindAccount(account, new CommonCallback() {
                            @Override
                            public void onSuccess(String s) {
                                callbackContext.success(s);
                            }

                            @Override
                            public void onFailed(String s, String s1) {
                                LOG.d(LOG_TAG,"onFailed reason:"+s+"res:"+s1);
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("response", s);
                                    jsonObject.put("reason", s1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                callbackContext.error(jsonObject);
                            }
                        });
                    }
            });
            ret = true;
        }
        else if ("bindTags".equalsIgnoreCase(action)) {

            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    LOG.d(LOG_TAG, "PushManager#bindTags");

                    List<String> tags = null;
                    if (args != null && args.length() > 0) {
                        int len = args.length();
                        tags = new ArrayList<String>(len);

                        for (int inx = 0; inx < len; inx++) {
                            try {
                                tags.add(args.getString(inx));
                            } catch (JSONException e) {
                                LOG.e(LOG_TAG, e.getMessage(), e);
                            }
                        }
                        String[] array=tags.toArray(new String[tags.size()]);
                        pushService.bindTag(pushService.ACCOUNT_TARGET, array, "", new CommonCallback() {
                            @Override
                            public void onSuccess(String s) {
                                callbackContext.success(s);
                            }

                            @Override
                            public void onFailed(String s, String s1) {
                                LOG.d(LOG_TAG,"onFailed reason:"+s+"res:"+s1);
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("response", s);
                                    jsonObject.put("reason", s1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                callbackContext.error(jsonObject);
                            }
                        });
                    }

                }
            });
            ret = true;
        } else if ("unbindTags".equalsIgnoreCase(action)) {

            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    LOG.d(LOG_TAG, "PushManager#unbindTags");

                    List<String> tags = null;
                    if (args != null && args.length() > 0) {
                        int len = args.length();
                        tags = new ArrayList<String>(len);

                        for (int inx = 0; inx < len; inx++) {
                            try {
                                tags.add(args.getString(inx));
                            } catch (JSONException e) {
                                LOG.e(LOG_TAG, e.getMessage(), e);
                            }
                        }
                        String[] array=tags.toArray(new String[tags.size()]);
                        pushService.unbindTag(pushService.ACCOUNT_TARGET,array,"",new CommonCallback(){
                            @Override
                            public void onFailed(String s, String s1) {
                                LOG.d(LOG_TAG,"onFailed reason:"+s+"res:"+s1);
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("response", s);
                                    jsonObject.put("reason", s1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                callbackContext.error(jsonObject);
                            }

                            @Override
                            public void onSuccess(String s) {
                                LOG.d(LOG_TAG,"onSuccess:"+s);
                                callbackContext.success(s);
                            }
                        });
                    }

                }
            });
            ret = true;
        }else if ("listTags".equalsIgnoreCase(action)) {

            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    LOG.d(LOG_TAG, "PushManager#listTags");
                        pushService.listTags(pushService.ACCOUNT_TARGET,new CommonCallback(){
                            @Override
                            public void onFailed(String s, String s1) {
                                LOG.d(LOG_TAG,"onFailed reason:"+s+"res:"+s1);
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("response", s);
                                    jsonObject.put("reason", s1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                callbackContext.error(jsonObject);
                            }

                            @Override
                            public void onSuccess(String s) {
                                LOG.d(LOG_TAG,"onSuccess:"+s);
                                callbackContext.success(s);
                            }
                        });

                }
            });
            ret = true;
        }
        return ret;
    }

    /**
     * 接收推送内容并返回给前端JS
     *
     * @param data JSON对象
     */
    static void sendPushData(String callback,JSONObject data) {
        LOG.d(LOG_TAG, "AliyunPushReceiver#sendPushData: " + (data != null ? data.toString() : "null"));
        String format = "window.AliyunPush.%s(%s);";
        final String js = String.format(format,callback, data.toString());
        cordovaActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                instance.webView.loadUrl("javascript:" + js);
            }
        });
    }
}