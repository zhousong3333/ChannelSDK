package cn.uc.paysdk;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Iterator;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import cn.uc.gamesdk.sa.UCGameSdk;
import cn.uc.gamesdk.sa.activity.UCSdkActivity;
import cn.uc.gamesdk.sa.iface.open.SDKConst;
import cn.uc.gamesdk.sa.iface.open.UCCallbackListener;
import cn.uc.gamesdk.sa.iface.open.UCGameSDKStatusCode;
import cn.uc.paysdk.SDKCore;
import cn.uc.paysdk.face.commons.Response;
import cn.uc.paysdk.face.commons.SDKCallbackListener;
import cn.uc.paysdk.face.commons.SDKError;
import cn.uc.paysdk.face.commons.SDKProtocolKeys;

import com.unity3d.player.UnityPlayer;
import com.skysoul.MR.SDKBaseActivity;
/**
 * SDK main entrance.
 *
 * @author www.9Game.com
 * @date 2015-09-21
 */
public class SDKDemoUnityCore extends SDKBaseActivity {

    private static final String TAG = "SDKUnityCore";
    private static String gameobject = "Main Camera";

    @Override
    protected void Init(String message)
    {
        initSDK(this,message);
    }
    @Override
    protected void Login()
    {
        Login();
    }

    @Override
    protected void Pay(String message)
    {
        Pay(message);
    }

    @Override
    protected void Logout()
    {
        Logout();
    }

    /**
     * 初始化支付SDK
     *
     * @param ctx
     *            Android程序当前界面上下文
     *
     * @param payInitListener
     *            初始化回调监听，此回调函数中可能会有历史订单处理结果，注意要处理这部分订单结果信息
     */
    public static void initSDK(final Activity context, final String jonString, final String objName) {
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                ex.printStackTrace();

            }
        });
        Intent intent = jsonStringToIntent(jonString);
        UCGameSdk.defaultSdk().setCallback(SDKConst.SDK_INIT_LISTENER,sdkInitListener);
        UCGameSdk.defaultSdk().setCallback(SDKConst.PAY_INIT_LISTENER, payInitListener);

        UCGameSdk.defaultSdk().init(context, intent.getExtras());
        gameobject = objName;
    }
    public static void initSDK(final Activity context, final String objName) {
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                ex.printStackTrace();

            }
        });
        UCGameSdk.defaultSdk().setCallback(SDKConst.SDK_INIT_LISTENER,sdkInitListener);
        UCGameSdk.defaultSdk().setCallback(SDKConst.PAY_INIT_LISTENER, payInitListener);

        Bundle payInitData = new Bundle();  //需要new出对象，不能为null
        UCGameSdk.defaultSdk().init(context, payInitData);
        gameobject = objName;
    }
    private static cn.uc.gamesdk.sa.iface.open.UCCallbackListener<String> sdkInitListener =  new cn.uc.gamesdk.sa.iface.open.UCCallbackListener<String>() {
        @Override
        public void callback(int statuscode, String msg) {
            switch (statuscode) {
                case UCGameSDKStatusCode.SUCCESS: {
                    try {
//                        UnityPlayer.UnitySendMessage(gameobject, "onSDKInitSuccessful", msg);
//                        InitResult(msg);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
                break;
                default: {
                    if (TextUtils.isEmpty(msg)) {
                        msg = "SDK occur error!";
                    }
                    notifyError(statuscode, msg);
                }
                break;
            }
        }

    };

    /**
     * 执行支付下单操作，此操作会调出支付界面。
     *
     * @param ctx
     *            Android程序当前界面上下文
     * @param intent
     *            需要传入的特定参数意图
     * @param payInitListener
     *            支付结果回调侦听器。游戏需实现该支付结果回调侦听器
     */
    public static void pay(Activity context, String jonString) throws Exception {
        Intent intent = jsonStringToIntent(jonString);
        SDKCore.pay(context, intent, payInitListener);

    }

    /**
     * 执行支付SDK资源清理工作。
     *
     * @param ctx
     *            Android程序当前界面上下文
     */
    public static void exitSDK(Activity context) {
        UCGameSdk.defaultSdk().exit(context, new UCCallbackListener<String>(){
            @Override
            public void callback(int paramInt, String paramT) {
                if (UCGameSDKStatusCode.SDK_EXIT == paramInt) {
                    Log.e("xxx", "1" + paramInt);
                    UnityPlayer.UnitySendMessage(gameobject, "onExitSDK", paramT);
                } else {
                    Log.e("xxx", "2" + paramInt);
                }
            }});
    }

    static SDKCallbackListener payInitListener = new SDKCallbackListener() {

        @Override
        public void onSuccessful(int code, Response response) {
            if (null != response) {
                try {
                    JSONObject result = new JSONObject();
                    result.put("code", code);
                    if (response.getType() == Response.LISTENER_TYPE_PAY) {
                        response.setMessage(Response.OPERATE_SUCCESS_MSG);
                    }
                    result.put("status", response.getStatus());
                    result.put("message", null == response.getMessage() ? "" : response.getMessage());
                    result.put("type", response.getType());
                    result.put("data", null == response.getData() ? "" : response.getData());
                    result.put("tradeId", null == response.getTradeId() ? "" : response.getTradeId());
                    UnityPlayer.UnitySendMessage(gameobject, "onPaySuccessful", result.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Call Unity onSuccessful CallBack Error:" + e.getMessage());
                }
            }
        }

        @Override
        public void onErrorResponse(SDKError error) {
            notifyError(error.getCode(), error.getMessage());
        }
    };

    private static void notifyError(int code, String message) {
        try {
            JSONObject result = new JSONObject();
            result.put("code", code);
            result.put("message", message);
            UnityPlayer.UnitySendMessage(gameobject, "onErrorResponse", result.toString());
        } catch (Exception e) {
            Log.e(TAG, "Call Unity ErrorResponse CallBack Error:" + e.getMessage());
        }
    }

    private static Intent jsonStringToIntent(String str) {
        Intent intent = new Intent();
        try {
            JSONObject object = new JSONObject(str);
            Iterator<String> keys = object.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = object.get(key).toString();
                intent.putExtra(key, value);
            }
        } catch (Exception e) {
            Log.d(TAG, "jsonDecodeError" + e.getMessage());
        }
        return intent;
    }

}
