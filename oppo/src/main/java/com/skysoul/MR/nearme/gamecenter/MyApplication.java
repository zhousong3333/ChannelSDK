package com.skysoul.MR.nearme.gamecenter;

import com.nearme.game.sdk.GameCenterSDK;
import com.nearme.game.sdk.common.util.AppUtil;
import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		String appSecret = "6260ad9aad8C5f11fca46A9e601143f8";
		GameCenterSDK.init(appSecret, this);
	}
}
