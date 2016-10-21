package com.skysoul.MR;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by yangpeng on 16/10/18.
 */
public class SDKBaseActivity extends Activity{

    protected void Init(String message)
    {}

    protected void Login()
    {}

    protected void Pay()
    {}

    protected void Logout()
    {}

//    protected void SwitchAccount()
//    {}

    protected void InitResult(String message)//json "success"
    {}
    protected void LoginResult(String message)//json "success" userId userName user...
    {}
    protected void PayResult(String message)//json "success"
    {}
    protected void LogoutResult(String message)//json "success"
    {}
    protected void SwitchAccountResult(String message)//json "success" userId
    {}

}
