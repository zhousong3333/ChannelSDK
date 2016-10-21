package com.skysoul.MR;

/**
 * Created by yangpeng on 16/8/4.
 */
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.snowfish.cn.ganga.base.IUtils;
import com.snowfish.cn.ganga.helper.SFOnlineHelper;
import com.snowfish.cn.ganga.helper.SFOnlineInitListener;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;
import com.tencent.bugly.unity.UnityAgent;

public class MainActivity extends UnityPlayerActivity {
    public static Handler hd = new Handler();

    public MainActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SFOnlineHelper.onCreate(this, new SFOnlineInitListener() {
            public void onResponse(String tag, String value) {
                if(tag.equalsIgnoreCase("success")) {
                    Log.e("unity", "success");
                    UnityPlayer.UnitySendMessage("Main Camera", "InitResult", "success");
                } else if(tag.equalsIgnoreCase("fail")) {
                    Log.e("unity", "fail");
                    UnityPlayer.UnitySendMessage("Main Camera", "InitResult", "fail");
                }

            }
        });

        UnityAgent.getInstance().initSDK("900030535");
    }

    protected void onDestroy() {
        super.onDestroy();
        SFOnlineHelper.onDestroy(this);
    }

    protected void onResume() {
        super.onResume();
        hd.postDelayed(new Runnable() {
            public void run() {
                SFOnlineHelper.onResume(MainActivity.this);
            }
        }, 1000L);
    }

    protected void onStop() {
        super.onStop();
        SFOnlineHelper.onStop(this);
    }

    protected void onPause() {
        super.onPause();
        SFOnlineHelper.onPause(this);
    }

    protected void onRestart() {
        super.onRestart();
        SFOnlineHelper.onRestart(this);
    }

    public void GetChannel(final String gameobject,final String arg1)
    {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String channel = IUtils.getMetaDataString(MainActivity.this, arg1);
                UnityPlayer.UnitySendMessage(gameobject,"ChannelCallback",channel);
            }
        });
    }
}
