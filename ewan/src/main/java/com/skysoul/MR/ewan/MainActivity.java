package com.skysoul.MR.ewan;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.skysoul.MR.SDKBaseActivity;
import com.unity3d.player.UnityPlayer;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.ewan.supersdk.open.CollectInfo;
import cn.ewan.supersdk.open.InitInfo;
import cn.ewan.supersdk.open.PayInfo;
import cn.ewan.supersdk.open.SuperCollectRoleData;
import cn.ewan.supersdk.open.SuperInitListener;
import cn.ewan.supersdk.open.SuperLogin;
import cn.ewan.supersdk.open.SuperLoginListener;
import cn.ewan.supersdk.open.SuperLogoutListener;
import cn.ewan.supersdk.open.SuperNearbyUserListener;
import cn.ewan.supersdk.open.SuperPayListener;
import cn.ewan.supersdk.open.SuperPlatform;
import cn.ewan.supersdk.open.SuperRoleBindInfo;
import cn.ewan.supersdk.open.SuperRoleBindListener;
import cn.ewan.supersdk.open.SuperShareListener;

/**
 * Created by yangpeng on 16/9/7.
 */
public class MainActivity extends SDKBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        SuperPlatform.getInstance().onCreate(this);
    }
    /*

    /**
     * 初始化SDK
     */
    private void initSdk(String appid, String signKey, String packetid, String obj)
    {
        // 封装初始化所需要的信息
        InitInfo info = new InitInfo();
        info.setAppId(appid);// 申请的应用ID 10000
        info.setSignKey(signKey);// 申请的客户端签名key S223423B4rLzby5Fl
        info.setPacketid(packetid);// 包id 10000
        UnityGameObject = obj;
        /**
         * 初始化接口
         */
        SuperPlatform.getInstance().init(this, info, new SuperInitListener()
        {
            @Override
            public void onSuccess()
            {
                showToast("初始化成功！");
                //游戏方做初始化成功后的相关操作
                //...........
            }
            @Override
            public void onFail(String msg)
            {
                showToast("初始化失败！");
                //游戏方做初始化失败后的相关处理
                //..............
            }
        });
    }
    /**
     * 调用益玩的登入接口
     */
    public void login()
    {
        SuperPlatform.getInstance().login(MainActivity.this, new SuperLoginListener()
        {
            @Override
            public void onLoginSuccess(SuperLogin login)
            {
                //正常登入成功的回调
                showToast("登入成功！");
                Log.i("", "登入成功");
                Log.i("", "openid = " + login.getOpenid());
                Log.i("", "username = " + login.getUsername());
                //开始加载数据角色
                String openid = login.getOpenid();
                String username = login.getUsername();
                Map map = new HashMap();
                map.put("openid",openid);
                map.put("username",username);
                JSONObject json = new JSONObject(map);
                String message = json.toString();
                UnitySendMessage("LoginResult",message);

                String message1 = map.toString();
                Log.i("zhousong","map's string : " + message1);
                Log.i("zhousong","json's string : " + message);
            }
            @Override
            public void onLoginFail(String msg)
            {
                //正常登入失败的回调
                showToast("登入失败\n" + "\nmsg = " + msg);
                UnitySendMessage("LoginResult","fail");
            }
            @Override
            public void onLoginCancel()
            {
                showToast("取消登入");
            }
            @Override
            public void onNoticeGameToSwitchAccount()
            {
                //游戏弹出登入页面
//                ShowToast("游戏弹出登入页面");
                UnitySendMessage("GameToSwitchAccount","");
            }
            @Override
            public void onSwitchAccountSuccess(SuperLogin login)
            {
                // TODO 自动生成的方法存根
                //切换帐号成功的回调
//                ShowToast("切换帐号成功\n先释放旧角色，再重新加载游戏角色！");
                //开始重新加载数据角色
                //先做旧角色释放工作
                Log.i("", "切换成功");
                Log.i("", "openid = " + login.getOpenid());
                Log.i("", "username = " + login.getUsername());

                Map map = new HashMap();
                map.put("openid",login.getOpenid());
                map.put("username",login.getUsername());
                JSONObject json = new JSONObject(map);
                String message = json.toString();
                UnitySendMessage("SwitchAccountSuccess",message);

            }
        });
    }
    /**
     * 切换帐号相关事件
     */
    private void SwitchAccount() {
        if (SuperPlatform.getInstance().isHasSwitchAccount()) {
            // TODO 自动生成的方法存根
            SuperPlatform.getInstance().switchAccount(MainActivity.this);
        }
    }
    /**
     * 支付事件
     */
    private void Pay(final String etContent,final String productName,final String productNumber, final String orderId) {
        // TODO 自动生成的方法存根
        if (etContent == null || etContent.equals("") || etContent.length() <= 0) {
            showToast("支付金额有误！");
            return;
        }
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int price = Integer.parseInt(etContent);
                int Number = Integer.parseInt(productNumber);
                if (price <= 0) {
                    showToast("支付金额有误！");
                    return;
                } else {
                    PayInfo payinfo = new PayInfo();

                    //x元对应x*10元宝
                    payinfo.setPrice(price);
                    payinfo.setServerId("1");

                    payinfo.setProductName(productName);
                    payinfo.setProductNumber(Number);
//					payinfo.setProductName((price * 10) + "金币");
//					payinfo.setProductNumber(1);

                    payinfo.setCutsomInfo(orderId);

                    //30元兑换1月卡
//					payinfo.setPrice(30);
//					payinfo.setServerId("1");
//					payinfo.setProductName("月卡");
//					payinfo.setProductNumber(1);
//					payinfo.setCutsomInfo("20130412191122-002-002");

                    SuperPlatform.getInstance().pay(MainActivity.this, payinfo, new SuperPayListener() {
                        @Override
                        public void onComplete() {
                            showToast("支付成功！");
                            UnitySendMessage("PayResult", "success");
                        }

                        @Override
                        public void onCancel() {
                            showToast("支付取消！");
                            UnitySendMessage("PayResult", "cancel");
                        }

                        @Override
                        public void onFail(String msg) {
                            showToast("支付失败！");
                            UnitySendMessage("PayResult", "fail");
                        }
                    });
                }
            }
        });
    }


        /**
         * 采集数据事件
         */
    private void CollectGameCreateRoleData(String userId, String userName)
    {
				/*
				 * 参数一：1为登录动作，2为创建角色（必传）
				 * 参数二：区服标识id（必传）
				 * 参数三：区服名称（必传）
				 * 参数四：角色id（必传）
				 * 参数五：角色名称（必传）
				 * 参数六：角色等级（必传）
				 * 参数七：扩展字段（可选）
				 */
                CollectInfo info = new CollectInfo(SuperCollectRoleData.getCollectRoleDataType(SuperCollectRoleData.createRole),
                        "1",
                        "益玩服",
                        userId,
                        userName,
                        1,
                        "create role");
                SuperPlatform.getInstance().collectData(MainActivity.this, info);
    }
    private void CollectGameRoleEntryData(String userId, String userName, String userLevel)
    {
				/*
				 * 参数一：1为登录动作，2为创建角色（必传）
				 * 参数二：区服标识id（必传）
				 * 参数三：区服名称（必传）
				 * 参数四：角色id（必传）
				 * 参数五：角色名称（必传）
				 * 参数六：角色等级（必传）
				 * 参数七：扩展字段（可选）
				 */
                CollectInfo info = new CollectInfo(SuperCollectRoleData.getCollectRoleDataType(SuperCollectRoleData.loginRole),
                        "1",
                        "益玩服",
                        userId,
                        userName,
                        Integer.parseInt(userLevel),
                        "login role");
                SuperPlatform.getInstance().collectData(MainActivity.this, info);
    }

        /**
         * 绑定角色
         */
    private void BindRole()
    {
        SuperRoleBindInfo rolebindinfo = new SuperRoleBindInfo("九王爷", "119", "1", "bind role");
        SuperPlatform.getInstance().bindRole(MainActivity.this, rolebindinfo, new SuperRoleBindListener()
        {
            @Override
            public void roleHasBinded()
            {
                // TODO 自动生成的方法存根
                showToast("角色已绑定");
            }
            @Override
            public void bindSuccess()
            {
                // TODO 自动生成的方法存根
                showToast("绑定成功");
            }
            @Override
            public void bindCancel()
            {
                // TODO 自动生成的方法存根
                showToast("绑定取消");
            }
        });
    }


        /**
         * 分享事件
         */
    private void Share()
    {
        SuperPlatform.getInstance().enterShareBoardView(MainActivity.this, 1, "1234567890", new SuperShareListener()
        {
            @Override
            public void onSuccess()
            {
                // TODO 自动生成的方法存根
                showToast("分享成功");
            }

            @Override
            public void onFail(String msg)
            {
                // TODO 自动生成的方法存根
                showToast("分享失败!");
            }

            @Override
            public void onCancel()
            {
                // TODO 自动生成的方法存根
                showToast("取消分享!");
            }
        });
    }

        /**
         * 附近用户事件
         */
    private void NearbyUser() {
        if (SuperPlatform.getInstance().isHasThirdNearbyUser()) {
            SuperPlatform.getInstance().entryThirdNearbyUser(MainActivity.this, new SuperNearbyUserListener() {
                @Override
                public void onCloseView() {
                    // TODO 自动生成的方法存根
                    showToast("附近用户界面关闭！");
                }
            });
        }
    }
        /**
         * 注册摇一摇相关事件
         */
        private void RegisterYao()
        {
            SuperPlatform.getInstance().registerShareShake(MainActivity.this, 1, "1234567890", new SuperShareListener()
            {
                @Override
                public void onSuccess()
                {
                    // TODO 自动生成的方法存根
                    showToast("分享成功");
                }
                @Override
                public void onFail(String msg)
                {
                    // TODO 自动生成的方法存根
                    showToast("分享失败!");
                }
                @Override
                public void onCancel()
                {
                    // TODO 自动生成的方法存根
                    showToast("取消分享!");
                }
            });
        }


    private void unregisterYaoUserBtn()
    {
        SuperPlatform.getInstance().unregisterShareShake(MainActivity.this);
    }

    public void ExitGame()
    {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 调用退出SDK功能
                SuperPlatform.getInstance().logout(MainActivity.this, new SuperLogoutListener()
                {
                    @Override
                    public void onGamePopExitDialog()
                    {
                        // TODO 自动生成的方法存根
                        Dialog dialog = new AlertDialog.Builder(MainActivity.this).setTitle("游戏自带退出框")
                                .setCancelable(false)
                                .setMessage("您确定要退出游戏吗?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        // 调用退出SDK功能
                                        dialog.dismiss();
                                        SuperPlatform.getInstance().exit(MainActivity.this);
                                        MainActivity.this.finish();
                                        exit();
                                    }
                                })
                                .setNeutralButton("取消", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        dialog.dismiss();
                                    }
                                })
                                .create();
                        dialog.show();
                    }

                    @Override
                    public void onGameExit()
                    {
                        // TODO 自动生成的方法存根
                        SuperPlatform.getInstance().exit(MainActivity.this);
                        MainActivity.this.finish();
                        exit();
                    }
                });
            }
        });

    }
    /**
     * [退出应用]<BR>
     * [功能详细描述]
     */
    private void exit()
    {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1)
        {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            System.exit(0);
        }
        else
        {// android2.1
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            am.restartPackage(getPackageName());
        }
    }

    /**
     * [登出接口]<BR>
     * [功能详细描述]
     */
    private void logout()
    {
//        onBackPressedCancel();
        UnitySendMessage("LogoutResult","");
    }

    @Override
    protected void onDestroy()
    {
        // TODO 自动生成的方法存根
        SuperPlatform.getInstance().onDestroy(this);
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        // TODO 自动生成的方法存根
        super.onNewIntent(intent);
        SuperPlatform.getInstance().onNewIntent(this, intent);
    }

    @Override
    protected void onPause()
    {
        // TODO 自动生成的方法存根
        super.onPause();
        SuperPlatform.getInstance().onPause(this);
    }

    @Override
    protected void onRestart()
    {
        // TODO 自动生成的方法存根
        super.onRestart();
        SuperPlatform.getInstance().onRestart(this);
    }

    @Override
    protected void onResume()
    {
        // TODO 自动生成的方法存根
        super.onResume();
        SuperPlatform.getInstance().onResume(this);
    }

    @Override
    protected void onStart()
    {
        // TODO 自动生成的方法存根
        super.onStart();
        SuperPlatform.getInstance().onStart(this);
    }

    @Override
    protected void onStop()
    {
        // TODO 自动生成的方法存根
        super.onStop();
        SuperPlatform.getInstance().onStop(this);
    }
}
