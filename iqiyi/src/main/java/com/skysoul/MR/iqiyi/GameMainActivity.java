package com.skysoul.MR.iqiyi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.iqiyigame.single.sdk.GameSingleSDKPlatform;
import com.iqiyigame.single.sdk.SDKInitListener;
import com.iqiyigame.single.sdk.SDKPayCallback;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

/**
 * Created by yangpeng on 16/8/30.
 */
public class GameMainActivity extends UnityPlayerActivity {
    private static boolean isInit = false;
    private static GameSingleSDKPlatform platform;
    private String UnityGameObject = "Main Camera";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        platform = GameSingleSDKPlatform.getInstance();
    }
    //======================init================
    public void Init(String obj, String gameId)
    {
        initSDK(gameId);
        UnityGameObject = obj;
    }
    //======================pay=================
    public void Pay(String moneyStr)
    {
        String[] fileList = {"支付宝"," 微信","银行卡"};
        createListFile(fileList,moneyStr);
    }

    private void initSDK(final String GameId){

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(TextUtils.isEmpty(GameId)){
                    Toast.makeText(GameMainActivity.this, "请输入有效的游戏id", Toast.LENGTH_SHORT).show();
                    return ;
                }

//        progressDialog = new ProgressDialog(this);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.setMessage("正在初始化"); // 设置进度条的提示信息
//        progressDialog.setIcon(R.drawable.ic_launcher); // 设置进度条的图标
//        progressDialog.setIndeterminate(false); // 设置进度条是否为不明确
//        progressDialog.setCancelable(true); // 设置进度条是否按返回键取消
//        progressDialog.show();
                platform.initSDK(GameMainActivity.this, GameId, new SDKInitListener() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
//                progressDialog .dismiss();
                        Toast.makeText(getApplication(), "初始化成功！", Toast.LENGTH_SHORT).show();
                        isInit = true;
//                sdkVersion.setText(platform.getSDKVersion(GameMainActivity.this));
                    }

                    @Override
                    public void onFail(String arg0) {
                        // TODO Auto-generated method stub
//                progressDialog .dismiss();
                        Toast.makeText(getApplication(), "初始化失败", Toast.LENGTH_SHORT).show();
                        isInit = false;
                    }
                });
            }
        });
    }

    private final void createListFile(final String[] fileList, final String moneyStr) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(GameMainActivity.this)
                        .setTitle("支付方式")
//                        .setIcon(R.drawable.app_icon)
                        .setSingleChoiceItems(fileList, 0, null)
                        .setPositiveButton("支付", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                                int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
//						Log.d("zhousong",String.valueOf(whichButton));
                                Log.d("zhousong",String.valueOf(selectedPosition));
                                switch (selectedPosition)
                                {
                                    case 0:
                                        alipay(moneyStr);
                                        break;
                                    case 1:
                                        webchat(moneyStr);
                                        break;
                                    case 2:
                                        bankcard(moneyStr);
                                        break;
                                }
                                // Do something useful withe the position of the selected radio button
                            }
                        }).show();
            }
        });
    }
    private void alipay(final String moneyStr){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(isInit){
                    try {
                        int money = Integer.parseInt(moneyStr);
                        platform.alipay(GameMainActivity.this, money,"cp developer", new SDKPayCallback() {

                            @Override
                            public void onSuccess(String orderId, int money,String developerInfo) {
                                // TODO Auto-generated method stub
                                Toast.makeText(getApplication(), "支付成功", Toast.LENGTH_LONG).show();
                                UnitySendMessage("success","支付成功");
                            }

                            @Override
                            public void onFail(String developerInfo,String arg0) {
                                // TODO Auto-generated method stub
                                Toast.makeText(getApplication(), arg0, Toast.LENGTH_LONG).show();
                                UnitySendMessage("fail","支付失败");
                            }
                        });
                    } catch (NumberFormatException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Toast.makeText(getApplication(), "请输入有效金额", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void bankcard(final String moneyStr){
       this.runOnUiThread(new Runnable() {
           @Override
           public void run() {
               if(isInit){
                   try {
                       int money = Integer.parseInt(moneyStr);
                       platform.bankCard(GameMainActivity.this, money, "cp developer",new SDKPayCallback() {

                           @Override
                           public void onSuccess(String orderId, int money,String developerInfo) {
                               // TODO Auto-generated method stub
                               Toast.makeText(getApplication(), "支付成功", Toast.LENGTH_LONG).show();
                               UnitySendMessage("success","支付成功");
                           }

                           @Override
                           public void onFail(String developerInfo,String arg0) {
                               // TODO Auto-generated method stub
                               Toast.makeText(getApplication(), arg0, Toast.LENGTH_LONG).show();
                               UnitySendMessage("fail","支付失败");
                           }
                       });
                   } catch (NumberFormatException e) {
                       // TODO Auto-generated catch block
                       e.printStackTrace();
                       Toast.makeText(getApplication(), "请输入有效金额", Toast.LENGTH_SHORT).show();
                   }
               }
           }
       });
    }

    private void webchat(final String moneyStr){
       this.runOnUiThread(new Runnable() {
           @Override
           public void run() {
               if(isInit){
                   try {
                       int money = Integer.parseInt(moneyStr);
                       platform.webchat(GameMainActivity.this, money, "cp developer",new SDKPayCallback() {

                           @Override
                           public void onSuccess(String orderId, int money,String developerInfo) {
                               // TODO Auto-generated method stub
                               Toast.makeText(getApplication(), "支付成功", Toast.LENGTH_LONG).show();
                               UnitySendMessage("success","支付成功");
                           }

                           @Override
                           public void onFail(String developerInfo,String arg0) {
                               // TODO Auto-generated method stub
                               Toast.makeText(getApplication(), arg0, Toast.LENGTH_LONG).show();
                               UnitySendMessage("fail","支付失败");
                           }
                       });
                   } catch (NumberFormatException e) {
                       // TODO Auto-generated catch block
                       e.printStackTrace();
//                Toast.makeText(getApplication(), "请输入有效金额", Toast.LENGTH_SHORT).show();
                   }
               }
           }
       });
    }

    private void UnitySendMessage(String result,String message)
    {
        UnityPlayer.UnitySendMessage(UnityGameObject,result,message);
    }
}
