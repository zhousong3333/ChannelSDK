package com.skysoul.MR.mumayi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.mumayi.paymentmain.business.FindUserDataListener;
import com.mumayi.paymentmain.business.ResponseCallBack;
import com.mumayi.paymentmain.business.onLoginListener;
import com.mumayi.paymentmain.business.onTradeListener;
import com.mumayi.paymentmain.ui.PaymentCenterInstance;
import com.mumayi.paymentmain.ui.PaymentUsercenterContro;
import com.mumayi.paymentmain.ui.pay.MMYInstance;
import com.mumayi.paymentmain.ui.usercenter.PaymentFloatInteface;
import com.mumayi.paymentmain.util.PaymentConstants;
import com.mumayi.paymentmain.util.PaymentLog;
import com.mumayi.paymentmain.util.PaymentUserInfoUtil;
import com.mumayi.paymentmain.vo.UserBean;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yangpeng on 16/8/11.
 */
public class MumayiActivity extends UnityPlayerActivity implements onTradeListener,onLoginListener {

    private String Tag = "zhousong";

    private PaymentCenterInstance instance = null;
    private PaymentFloatInteface floatInteface;
    private PaymentUsercenterContro userCenter = null;

    private Context context;
    private String gameobject = "Main Camera";
    private boolean isFindUserData = false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = this;
        // 初始化支付SDK用户中心
        instance = PaymentCenterInstance.getInstance(MumayiActivity.this);
        instance.setTradeListener(this);
        userCenter = instance.getUsercenterApi(this);
        findLocalUserData();
        Log.d(Tag,"oncreate");
    }

    //查询本地用户数据并填写在登录框中。
    public void findLocalUserData()
    {
        // 确保每次在调用登陆接口时都会检测本地数据
        instance.findUserData(new FindUserDataListener() {
            @Override
            public void findUserDataComplete()
            {
                //在账号查找流程完成后，由开发者主动调用此接口进入登陆界面
//                instance.go2Login(context);
            }
        });
    }
    // 显示悬浮框
    public void ShowFloat()
    {
        if(floatInteface == null){
            Log.d(Tag,"ShowFloat");
            floatInteface = instance.createFloat();
        }
        if(floatInteface != null) {
            floatInteface.show();
        }
    }
    public void HideFloat()
    {
        if(floatInteface != null) {
            floatInteface.close();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        ShowFloat();
        // 检查用户是否登录。没登陆就跳转到登录页面(注：请注意不要和go2Login接口冲突，否则可能会打开多个登陆界面)
//		userCenter.checkLogin();

    }
    @Override
    protected void onPause() {
        super.onPause();
        HideFloat();
    }

    //Init
    public void Init(String appKey, String appName, String userArea, String userName)
    {

        instance.initial(appKey, appName);
//          设置角色所在区服（注：如果初始化时不知道区服，等级的话可以暂时不设置，只需要在调用支付之前确定设置即可！）
        instance.setUserArea(userArea);
//        //设置角色名
        instance.setUserName(userName);
//        //设置角色等级
//        instance.setUserLevel(99);
//        // 设置是否开启bug模式， true打开可以显示Log日志， false不显示
        instance.setTestMode(true);
//        // 设置监听器
////        instance.setListeners(this);
//        // 设置切换完账号后是否自动跳转登陆
//        instance.setChangeAccountAutoToLogin(true);
        ShowFloat();
    }


    //支付
    public void Pay(final String obj,final String itemName,final String price,final String dec)
    {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameobject = obj;
                userCenter.pay(MumayiActivity.this, itemName, price, dec);
            }
        });

    }
    /**
     * 支付接口回调
     */
    @Override
    public void onTradeFinish(String tradeType, int tradeCode, Intent intent) {
        // 可在此处获取到提交的商品信息
        Bundle bundle = intent.getExtras();
        String orderId = bundle.getString("orderId");
        String productName = bundle.getString("productName");
        String productPrice = bundle.getString("productPrice");
        String productDesc = bundle.getString("productDesc");
        if (tradeCode == MMYInstance.PAY_RESULT_SUCCESS) {
            // 在每次支付回调结束时候，调用此接口检查用户是否完善了资料
            userCenter.checkUserState(MumayiActivity.this);

            Toast.makeText(this, productName + "支付成功 支付金额:" + productPrice, Toast.LENGTH_SHORT).show();
            UnityPlayer.UnitySendMessage(gameobject,"PayCallback","success");
        } else if (tradeCode == MMYInstance.PAY_RESULT_FAILED) {
            Toast.makeText(this, productName + "支付失败 支付金额:" + productPrice, Toast.LENGTH_SHORT).show();
            UnityPlayer.UnitySendMessage(gameobject,"PayCallback","fail");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 关闭悬浮框
        floatInteface.close();
        instance.finish();
        // 释放资源，退出程序
        instance.exit();
    }

    @Override
    public void onLoginFinish(String s) {

    }

    @Override
    public void onLoginOut(String s) {

    }

//    //登出
//    private void loginOut() {
//        // 获取当前用户信息
//        UserBean user = PaymentConstants.NOW_LOGIN_USER;
//        userCenter.loginOut(MumayiActivity.this, user.getName(), new ResponseCallBack() {
//            @Override
//            public void onSuccess(Object obj) {
//                try {
//                    JSONObject loginoutJson = (JSONObject) obj;
//                    String loginoutCode = loginoutJson.getString("loginOutCode");
//                    if (loginoutCode.equals("success")) {
//                        // 注销成功之后回到登录界面
//                    } else {
//                        // 注销失败
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFail(Object obj) {
//                // 注销失败
//            }
//        });
//    }

    /**
     * 登录接口回调
     */
//    @Override
//    public void onLoginFinish(String loginResult)
//    {
//        try
//        {
//            if (loginResult != null)
//            {
//                JSONObject loginJson = new JSONObject(loginResult);
//                String loginState = loginJson.getString(PaymentConstants.LOGIN_STATE);
//                // 登陆成功
//                if (loginState != null
//                        && loginState.equals(PaymentConstants.STATE_SUCCESS))
//                {
//                    String uname = loginJson.getString("uname");
//                    String uid = loginJson.getString("uid");
//                    String token = loginJson.getString("token");
//                    String session = loginJson.getString("session");
//                    PaymentLog.getInstance().d(
//                            "成功  token>>" + token + "\n session>>" + session);
//                    // uname:用户名， uid:用户ID
//                    // ,token:是用来服务器验证登录，注册是不是成功，用seesion来解签,解签方法见文档
//                    // 所有注册，一键注册，登录的接口成功最后都会走这个回调接口
//                    // 在这里进入游戏
//                    Intent go2play_intent = new Intent(MumayiActivity.this,
//                            MumayiActivity.class);
//                    startActivity(go2play_intent);
//                    finish();
//                }
//                else
//                {
//                    // 登录失败
//                    String error = loginJson.getString("error");
//                    if (error != null && error.trim().length() > 0
//                            && error.equals("cancel_login"))
//                    {
//                        // 用如果用户在登陆界面选择退出登陆界面，应当在此重新调用进入登陆界面
//                        PaymentLog.getInstance().d("login_failed:" + error);
////                        instance.go2Login(context);
//                    }
//                    else if (error != null && error.trim().length() > 0)
//                    {
//                        // 正常登陆失败的原因
//                    }
//                }
//            }
//        }
//        catch (JSONException e)
//        {
//            PaymentLog.getInstance().E("WelcomeActivity", e);
//        }
//    }

    /**
     * 注销接口回调
     */
//    @Override
//    public void onLoginOut(String loginOutCallBackStr)
//    {
//        try
//        {
//            JSONObject json = new JSONObject(loginOutCallBackStr);
//            String code = json.getString("loginOutCode");
//            if (code.equals("success"))
//            {
//                String uid = json.getString("uid");
//                String name = json.getString("uname");
//                // 注销成功
//                PaymentLog.getInstance().d(
//                        "注销帐号:" + name + "成功噢>>" + loginOutCallBackStr
//                                + " uid:" + uid);
//            }
//            else
//            {
//                // 注销失败
//                PaymentLog.getInstance().d("注销失败噢>>" + loginOutCallBackStr);
//            }
//        }
//        catch (JSONException e)
//        {
//            PaymentLog.getInstance().E("WelcomeActivity", e);
//        }
//    }



}
