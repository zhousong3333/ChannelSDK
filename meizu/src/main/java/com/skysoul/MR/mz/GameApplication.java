package com.skysoul.MR.mz;

import android.app.Application;

import com.meizu.gamesdk.offline.core.MzGameCenterPlatform;


/**
 * Created by aiden on 10/27/15.
 */
public class GameApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //LeakCanary.install(this);

        MzGameCenterPlatform.init(this, GameConstants.GAME_ID, GameConstants.GAME_KEY);
    }
}
