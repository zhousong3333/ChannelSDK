/*
Copyright (C) Huawei Technologies Co., Ltd. 2015. All rights reserved.
See LICENSE.txt for this sample's licensing information.
 */
package com.skysoul.MR.huawei;


import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.huawei.pay.plugin.PayParameters;
import com.android.huawei.pay.util.HuaweiPayUtil;
import com.android.huawei.pay.util.Rsa;
import com.huawei.gameservice.sdk.GameServiceSDK;
import com.huawei.gameservice.sdk.api.GameEventHandler;
import com.huawei.gameservice.sdk.api.PayResult;
import com.huawei.gameservice.sdk.api.Result;
import com.huawei.gameservice.sdk.api.UserResult;
import com.huawei.gameservice.sdk.model.RoleInfo;
import com.huawei.gameservice.sdk.util.LogUtil;
import com.huawei.gameservice.sdk.util.StringUtil;
import com.skysoul.MR.huawei.net.ReqTask;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import org.json.JSONObject;

public class MainActivity extends UnityPlayerActivity {

	// 日志标签
	// definition the log tag
	public static final String TAG = "zhousong";
	private String UnityGameObject = "Main Camera";

	// 返回键是否可用
	// Identifies the back key is available
	private static boolean isBackKeyEnable = true;
	
	private Handler uiHandler = null;
	
	private String buoyPrivateKey = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 设置无标题
		// set the window without title
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);


		// 设置全屏
		// set the window fullscreen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        uiHandler = new Handler(this.getMainLooper()) {
    		@Override
    		public void handleMessage(Message msg) {
    			if (msg.getData() == null) {
    				return;
    			}
    			String errorMsg = msg.getData().getString("errorMsg");
    			if (!StringUtil.isNull(errorMsg)) {
    				Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_LONG)
    						.show();
    			}
    		}
    	};
    	
    	// 为了安全把浮标密钥放到服务端，并使用https的方式获取下来存储到内存中，CP可以使用自己的安全方式处理
        // For safety, buoy key put into the server and use the https way to get down into the client's memory. 
        // By the way CP can also use their safe approach.
        ReqTask getBuoyPrivate = new ReqTask(new ReqTask.Delegate()
        {
            
            @Override
            public void execute(String result)
            {
                /**
                 * 从服务端获取的浮标私钥，由于没有部署最终的服务端，所以返回值写死一个值，CP需要从服务端获取，服务端代码参考华为Demo
                 * The demo app did not deployed the server, so the return value is written fixed.For real app,the CP need to get the buoy key from server.
                 */
                buoyPrivateKey =  GlobalParam.BUOY_SECRET;
                
                // SDK初始化
                // SDK initialization 
//                init();
            }
        }, null, GlobalParam.GET_BUOY_PRIVATE_KEY);
        getBuoyPrivate.execute();

	}

	private static final String a = "zhousong";

	protected void onResume() {
		super.onResume();
		LogUtil.d(a, "base activity onResume, create float windows");
		GameServiceSDK.showFloatWindow(this);
	}

	protected void onPause() {
		super.onPause();
		LogUtil.d(a, "base activity onPause, remove all float windows");
		GameServiceSDK.hideFloatWindow(this);
	}

	protected void onDestroy() {
		super.onDestroy();
		if(this.isTaskRoot()) {
			LogUtil.d(a, "base activity onDestroy, remove all float windows");
			GameServiceSDK.destroy(this);
		}

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isBackKeyEnable) {
				return super.onKeyDown(keyCode, event);
			} else {
				return true;
			}

		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 生成游戏签名
	 * generate the game sign
	 */
	private String createGameSign(String data){
		
		// 为了安全把浮标密钥放到服务端，并使用https的方式获取下来存储到内存中，CP可以使用自己的安全方式处理
        // For safety, buoy key put into the server and use the https way to get down into the client's memory. 
        // By the way CP can also use their safe approach.
       
		String str = data;
		try {
			String result = RSAUtil.sha256WithRsa(str.getBytes("UTF-8"), buoyPrivateKey);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 初始化
	 * initialization
	 */
	public void Init(String objName){
		UnityGameObject = objName;
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				GameServiceSDK.init(MainActivity.this, GlobalParam.APP_ID, GlobalParam.PAY_ID, "com.skysoul.MR.huawei.installnewtype.provider", new GameEventHandler(){

					@Override
					public void onResult(Result result) {
						if(result.rtnCode != Result.RESULT_OK){
							handleError("init the game service SDK failed:" + result.rtnCode);
							return;
						}
						login();
						checkUpdate();
						UnitySend("InitResult","success");
					}

					@Override
					public String getGameSign(String appId, String cpId, String ts){
						return createGameSign(appId+cpId+ts);
					}

				});
			}
		});
	}

	/*
向Unity发送消息
 */
	private void UnitySend(String method, String message)
	{
		UnityPlayer.UnitySendMessage(UnityGameObject,method,message);
		Log.d(TAG, message);
	}
	/**
	 * 帐号登录
	 * Login
	 */
	public void login(){
		int authType = 1;
		GameServiceSDK.login(MainActivity.this, new GameEventHandler(){
        	
			@Override
			public void onResult(Result result) {
                if (result.rtnCode != Result.RESULT_OK)
                {
                    handleError("login failed:" + result.toString());
					UnitySend("LoginResult","fail");
				}else {
					UserResult userResult = (UserResult) result;
                    if(userResult.isAuth != null && userResult.isAuth == 1)
                    {
                        boolean checkResult = checkSign(GlobalParam.APP_ID + userResult.ts + userResult.playerId, userResult.gameAuthSign);
                        if (checkResult)
                        {
                            handleError("login auth success:" + userResult.toString());
							Map map = new HashMap();
							map.put("openid",userResult.playerId);
							map.put("username",userResult.displayName);
							JSONObject json = new JSONObject(map);
							String message = json.toString();
							UnitySend("LoginResult",message);
                        }
                        else
                        {
                            handleError("login auth failed check game auth sign error");
                        }

                    }else if(userResult.isChange != null && userResult.isChange == 1){
                    	login();
                    }
                    else
                    {
                        handleError("login success:" + userResult.toString());
						Map map = new HashMap();
						map.put("openid",userResult.playerId);
						map.put("username",userResult.displayName);
						JSONObject json = new JSONObject(map);
						String message = json.toString();
						UnitySend("LoginResult",message);
                    }

				}
			}
			
			@Override
			public String getGameSign(String appId, String cpId, String ts){
				return createGameSign(appId+cpId+ts);
			}
			
		}, authType);
	}
	
	/**
	 * 校验签名
	 * check the
	 */
    protected boolean checkSign(String data, String gameAuthSign)
    {

    	/*
         * 建议CP获取签名后去游戏自己的服务器校验签名
         */
    	/*
         * The CP need to deployed a server for checking the sign.
         */
        try
        {
            return RSAUtil.verify(data.getBytes("UTF-8"), GlobalParam.LOGIN_RSA_PUBLIC, gameAuthSign);
        }
        catch (Exception e)
        {
            return false;
        }
    }
    
    /**
	 * 检测游戏更新
	 * check the update for game
	 */
    private void checkUpdate()
    {
		GameServiceSDK.checkUpdate(MainActivity.this, new GameEventHandler(){
        	
			@Override
			public void onResult(Result result) {
				if(result.rtnCode != Result.RESULT_OK){
					handleError("check update failed:" + result.rtnCode);
				}
			}
			
			@Override
			public String getGameSign(String appId, String cpId, String ts){
				return createGameSign(appId+cpId+ts);
			}
			
		});
	}
	
	public void addPlayerInfo(String gameRank, String gameRole, String gameArea, String GameSociaty){
		HashMap<String, String> playerInfo = new HashMap<String, String>();
        
        /**
         * 将用户的等级等信息保存起来，必须的参数为RoleInfo.GAME_RANK(等级)/RoleInfo.GAME_ROLE(角色名称)
         * /RoleInfo.GAME_AREA(角色所属区)/RoleInfo.GAME_SOCIATY(角色所属公会名称)
         * 全部使用String类型存放
         */
        /**
         * the CP save the level, role, area and sociaty of the game player into the SDK
         */
		playerInfo.put(RoleInfo.GAME_RANK, "15 level");
		playerInfo.put(RoleInfo.GAME_ROLE, "hunter");
		playerInfo.put(RoleInfo.GAME_AREA, "20");
		playerInfo.put(RoleInfo.GAME_SOCIATY, "Red Cliff II");
		GameServiceSDK.addPlayerInfo(MainActivity.this, playerInfo,
				new GameEventHandler(){

					@Override
					public void onResult(Result result) {
						if(result.rtnCode != Result.RESULT_OK){
							handleError("add player info failed:" + result.rtnCode);
						}
						
					}

					@Override
					public String getGameSign(String appId, String cpId,
							String ts) {
						return null;
					}
			
		});
	}
	
	private void getPlayerLevel(){
		
		GameServiceSDK.getPlayerLevel(MainActivity.this,
				new GameEventHandler(){

					@Override
					public void onResult(Result result) {
						if(result.rtnCode != Result.RESULT_OK){
							handleError("get player level failed:" + result.rtnCode);
							return;
						}
						UserResult userResult = (UserResult)result;
						handleError("player level:" + userResult.playerLevel);
					}

					@Override
					public String getGameSign(String appId, String cpId,
							String ts) {
						return null;
					}
			
		});
	}
	
	private void handleError(String errorMsg) {
//		Message msg = new Message();
//		Bundle data = new Bundle();
//		String strMsg = errorMsg;
//		data.putString("errorMsg", strMsg);
//		msg.setData(data);
//		uiHandler.sendMessage(msg);
	}

	/**
	 * 支付回调handler
	 */
	/**
	 * pay handler
	 */
	private GameEventHandler payHandler = new GameEventHandler()
	{
		@Override
		public String getGameSign(String appId, String cpId, String ts) {
			return null;
		}

		@Override
		public void onResult(Result result)
		{
			Map<String, String> payResp = ((PayResult)result).getResultMap();
			// 支付成功，进行验签
			// payment successful, then check the response value
			if ("0".equals(payResp.get(PayParameters.returnCode)))
			{
				if ("success".equals(payResp.get(PayParameters.errMsg)))
				{
					// 支付成功，验证信息的安全性；待验签字符串中如果有isCheckReturnCode参数且为yes，则去除isCheckReturnCode参数
					// If the response value contain the param "isCheckReturnCode" and its value is yes, then remove the param "isCheckReturnCode".
					if (payResp.containsKey("isCheckReturnCode") && "yes".equals(payResp.get("isCheckReturnCode")))
					{
						payResp.remove("isCheckReturnCode");

					}
					// 支付成功，验证信息的安全性；待验签字符串中如果没有isCheckReturnCode参数活着不为yes，则去除isCheckReturnCode和returnCode参数
					// If the response value does not contain the param "isCheckReturnCode" and its value is yes, then remove the param "isCheckReturnCode".
					else
					{
						payResp.remove("isCheckReturnCode");
						payResp.remove(PayParameters.returnCode);
					}
					// 支付成功，验证信息的安全性；待验签字符串需要去除sign参数
					// remove the param "sign" from response
					String sign = payResp.remove(PayParameters.sign);

					String noSigna = HuaweiPayUtil.getSignData(payResp);

					// 使用公钥进行验签
					// check the sign using RSA public key
					boolean s = Rsa.doCheck(noSigna, sign, GlobalParam.PAY_RSA_PUBLIC);
					if (s)
					{
						UnitySend("PayResult", "success");
					}
					else
					{
						UnitySend("PayResult", "fail");
					}
				}

			}
			else if ("30002".equals(payResp.get(PayParameters.returnCode)))
			{
				UnitySend("PayResult", "timeout");
			}
			Log.d(TAG, payResp.get(PayParameters.returnCode).toString());

			// 重新生成订单号，订单编号不能重复，所以使用时间的方式，CP可以根据实际情况进行修改，最长30字符
			// generate the pay ID using the date format, and it can not be repeated.
			// CP can generate the pay ID according to the actual situation, a maximum of 30 characters
//			DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS", Locale.US);
//			String requestId = format.format(new Date());

		}
	};



	public void Pay(final String price, final String productName, final String productDesc, final String requestId, final String productParam)
	{

		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// 价格必须精确到小数点后两位，使用正则进行匹配
				// The price must be accurate to two decimal places
				boolean priceChceckRet = Pattern.matches("^\\d+[.]\\d{2}$", price);
				if (!priceChceckRet)
				{
					Toast.makeText(getApplicationContext(), "价格必须精确到小数点后两位", Toast.LENGTH_SHORT).show();
					return;
				}

				if ("".equals(productName))
				{
					Toast.makeText(getApplicationContext(), "商品名称不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				// 禁止输入：# " & / ? $ ^ *:) \ < > | , =
				// the name can not input characters: # " & / ? $ ^ *:) \ < > | , =
				else if (Pattern.matches(".*[#\\$\\^&*)=|\",/<>\\?:].*", productName))
				{
					Toast.makeText(getApplicationContext(), "禁止输入：# \" & / ? $ ^ *:) \\ < > | , =", Toast.LENGTH_LONG).show();
					return;
				}
				if ("".equals(productDesc))
				{
					Toast.makeText(getApplicationContext(), "商品说明不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				// 禁止输入：# " & / ? $ ^ *:) \ < > | , =
				// the description can not input characters: # " & / ? $ ^ *:) \ < > | , =
				else if (Pattern.matches(".*[#\\$\\^&*)=|\",/<>\\\\?\\^:].*", productDesc))
				{
					Toast.makeText(getApplicationContext(), "禁止输入：# \" & / ? $ ^ *:) \\ < > | , =", Toast.LENGTH_LONG).show();
					return;
				}
				Map<String, String> param = getParam(productParam.trim());
				// 调用公共方法进行支付
				// call the pay method
				String request = requestId;

				if(requestId.isEmpty())
				{
					DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS", Locale.US);
					request = format.format(new Date());
				}

				GameBoxUtil.startPay(MainActivity.this, price, productName, productDesc, request, payHandler, param);
			}
		});
	}

	private Map<String, String> getParam(String param)
	{
		if (TextUtils.isEmpty(param))
		{
			return null;
		}
		else
		{
			String[] array = param.split("&");
			Map<String, String> map = new HashMap<String, String>();
			for (String str : array)
			{
				String key = str.substring(0, str.indexOf("="));
				String value = str.substring(str.indexOf("=") + 1, str.length());
				map.put(key, value);
			}
			return map;
		}
	}
}
