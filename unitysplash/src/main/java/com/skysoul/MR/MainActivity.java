package com.skysoul.MR;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.unity3d.player.UnityPlayerActivity;

public class MainActivity extends UnityPlayerActivity
{
    static final String TAG = "com.u3d.plugins";
    private ImageView bgView = null;
    private View view = null;
    AnimationDrawable animationDrawable = null;

    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle arg0)
    {
        super.onCreate(arg0);

        bgView=new ImageView(this);
        String lanStr=Locale.getDefault().getLanguage();
        String bgName="splash_bg_en";
        if(lanStr.equals("zh"))
        {
            bgName="splash_bg_zh";
        }
        Log.d(TAG, "System Lan:"+bgName);
        int splash_bg=getResources().getIdentifier(bgName, "drawable", getPackageName());
        bgView.setBackgroundResource(splash_bg);
//		this.addContentView(bgView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mUnityPlayer.addView(bgView);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float scaleX=dm.widthPixels/1024f;
        float scaleY=dm.heightPixels/600f;
        Log.d(TAG, "Screen Width:"+dm.widthPixels+";Screen Height:"+dm.heightPixels);
        LayoutInflater flater = LayoutInflater.from(this);
        int layoutID=getResources().getIdentifier("activity_splash", "layout", getPackageName());
        view = flater.inflate(layoutID, null);

        int frame_id=view.getResources().getIdentifier("splash_frame", "id", getPackageName());
        ImageView frameView=(ImageView)view.findViewById(frame_id);//new ImageView(this);
        //frameView.setBackgroundResource(R.anim.splash_gif);
//		this.addContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mUnityPlayer.addView(view);

        frameView.setScaleX(scaleX);
        frameView.setScaleY(scaleY);

//		frameView=new ImageView(this);
//		frameView.setBackgroundResource(R.anim.splash_gif);
//
//		LinearLayout ll=new LinearLayout(this);
//		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//		params.leftMargin=scaleX*620;
//		ll.setLayoutParams(params);
//		ll.addView(frameView);
//		mUnityPlayer.addView(ll);

        animationDrawable = (AnimationDrawable) frameView.getBackground();
        animationDrawable.start();
    }
    public void HideSplash()
    {
        Log.d(TAG, "HideSplash");
        new Handler(Looper.getMainLooper()).post(new Runnable()
        {
            @Override
            public void run()
            {
                Log.d(TAG, "HideSplash run");
                animationDrawable.stop();
                mUnityPlayer.removeView(bgView);
                mUnityPlayer.removeView(view);
//				((ViewGroup)bgView.getParent()).removeView(bgView);
//				((ViewGroup)view.getParent()).removeView(view);
                bgView=null;
                view=null;
                animationDrawable=null;
            }
        });
    }
    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        Log.d(TAG, "onRestart");
        new Handler(Looper.getMainLooper()).post(new Runnable()
        {
            @Override
            public void run()
            {
                muteAudioFocus(MainActivity.this,true);
            }
        });
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }
    @Override
    protected void onPause()
    {
        // TODO Auto-generated method stub
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        Log.d(TAG, "onStop");
        new Handler(Looper.getMainLooper()).post(new Runnable()
        {
            @Override
            public void run()
            {
                muteAudioFocus(MainActivity.this,false);
            }
        });
    }
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Log.d(TAG, "onStart");
        new Handler(Looper.getMainLooper()).post(new Runnable()
        {
            @Override
            public void run()
            {
                muteAudioFocus(MainActivity.this,true);
            }
        });
    }
    /**@param bMute 值为true时为关闭背景音乐。*/
    public boolean muteAudioFocus(Context context, boolean bMute)
    {
        if(context == null)
        {
            Log.d(TAG, "context is null.");
            return false;
        }
        boolean bool = false;
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if(bMute)
        {
            int result = am.requestAudioFocus(afChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN);
            bool = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        }
        else
        {
            int result = am.abandonAudioFocus(afChangeListener);
            bool = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        }
        Log.d(TAG, "pauseMusic bMute="+bMute +" result="+bool);
        return bool;
    }
    OnAudioFocusChangeListener afChangeListener = new OnAudioFocusChangeListener()
    {
        public void onAudioFocusChange(int focusChange)
        {
            Log.d(TAG, "focusChange:"+focusChange);
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT)
            {
                // Pause playback
            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS)
            {
                muteAudioFocus(MainActivity.this,false);
                // Stop playback
            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)
            {
                // Lower the volume
            }
            else if (focusChange == AudioManager.AUDIOFOCUS_GAIN)
            {
                // Resume playback or Raise it back to normal
            }
        }
    };
}
