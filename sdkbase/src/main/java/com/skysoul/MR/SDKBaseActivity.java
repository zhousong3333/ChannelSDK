package com.skysoul.MR;

import android.widget.Toast;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

/**
 * Created by yangpeng on 16/10/18.
 */
public class SDKBaseActivity extends UnityPlayerActivity{
    protected String Tag="sdkbase";
    protected String UnityGameObject = "Main Camera";
//    public static final int CODE_CANCEL = 1004;
    public static final String CODE_SUCCESS = "success";
    public static final String CODE_CANCLE="cancle";
    public static final String CODE_FAIL="failed";

    protected void UnitySendMessage(String method, String message)
    {
        UnityPlayer.UnitySendMessage(UnityGameObject, method, message);
    }
    protected void showToast(final String msg)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(SDKBaseActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void Init(String message)
    {}

    protected void Login()
    {}

    protected void Pay(String message)
    {}

    protected void Logout()
    {}

    protected void Exit()
    {}

    protected  void SendRoleInfo(String message)
    {}

//    protected void SwitchAccount()
//    {}

    //使用不同的回调， 或者使用同一个回调，参数中区别对待， 或者传入回调函数。
    protected void InitResult(String message)//string "success"
    {
        UnitySendMessage("InitResult",message);
    }
    protected void LoginResult(String message)//json "success" userId userName user...
    {
        UnitySendMessage("LoginResult",message);
    }
    protected void PayResult(String message)//string "success"
    {
        UnitySendMessage("PayResult",message);
    }
    protected void LogoutResult(String message)//string "success"
    {
        UnitySendMessage("LogoutResult",message);
    }
    protected void SwitchAccountResult(String message)//json "success"  userId userName user...
    {
        UnitySendMessage("SwitchAccountResult",message);
    }

    protected void SendRoleInfoResult(String message)
    {
        UnitySendMessage("SendRoleInfoResult",message);
    }

}
