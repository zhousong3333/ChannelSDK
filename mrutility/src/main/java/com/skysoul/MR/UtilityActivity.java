//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.skysoul.MR;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

public class UtilityActivity extends UnityPlayerActivity {
    private static String UnityGameObject = "Main Camera";
    public void Init(String message)
    {
        UnityGameObject = message;
    }
    //向Unity端发送消息。
    public static void UnitySendMessage(String method, String message)
    {
        UnityPlayer.UnitySendMessage(UnityGameObject,method,message);
    }

    //============显示dialog
public static void DialogQuit() {
        Runnable _runnable = new Runnable() {
            public void run() {
                Builder builder = new Builder(UnityPlayer.currentActivity);
                builder.setMessage("确定要退出吗?");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UnitySendMessage("ExitResult","true");
                    }
                });
                builder.setNegativeButton("取消", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        };
        UnityPlayer.currentActivity.runOnUiThread(_runnable);
    }
//显示Toast
    public static void showToast(final String msg)
    {
        UnityPlayer.currentActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(UnityPlayer.currentActivity, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    //获取meta自定义变量
    public static void GetMeta(final String arg1)
    {
        //com.snowfish.channel
        String channel = GetMetaDataString(UnityPlayer.currentActivity, arg1);
        UnitySendMessage("GetMetaResult",channel);
    }
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

}
