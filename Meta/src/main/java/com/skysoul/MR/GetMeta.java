package com.skysoul.MR;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;
import com.skysoul.MR.GetMeta;
/**
 * Created by yangpeng on 16/8/10.
 */
public class GetMeta extends UnityPlayerActivity {

    private static String GetMetaDataString(Context context, String name){
        try{
            ApplicationInfo info;
            info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String cm = info.metaData.getString(name);
            Log.e("ganga", "channel= "+cm);
            return cm;
        }
        catch (Throwable e){
            Log.e("ganga","Throwable");
            return null;
        }
    }

    public static void GetChannel(final String gameobject,final String arg1)
    {
        //com.snowfish.channel
//        this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
                String channel = GetMetaDataString(UnityPlayer.currentActivity, arg1);
                UnityPlayer.UnitySendMessage(gameobject,"ChannelCallback",channel);
                Log.d("onetwo", "run: " + channel + "//" + gameobject);
//            }
//        });
    }



}
