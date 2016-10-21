//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.skysoul.MR;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Process;
import android.widget.Toast;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

public class QuitDialog extends UnityPlayerActivity {
    public QuitDialog() {
    }

    public static void ShowQuitDialog() {
        Runnable _runnable = new Runnable() {
            public void run() {
                Builder builder = new Builder(UnityPlayer.currentActivity);
                builder.setMessage("确定要退出吗?");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Process.killProcess(Process.myPid());
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

    public static void ShowQuitDialogCallback(final String GameObject) {
        Runnable _runnable = new Runnable() {
            public void run() {
                Builder builder = new Builder(UnityPlayer.currentActivity);
                builder.setMessage("确定要退出吗?");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UnityPlayer.UnitySendMessage(GameObject,"ExitResult","true");
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

}
