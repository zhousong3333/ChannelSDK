package com.skysoul.MR.nearme.gamecenter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.json.JSONException;
import org.json.JSONObject;
import com.nearme.game.sdk.GameCenterSDK;
import com.nearme.game.sdk.callback.ApiCallback;
import com.nearme.game.sdk.callback.GameExitCallback;
import com.nearme.game.sdk.common.model.biz.PayInfo;
import com.nearme.game.sdk.common.model.biz.ReportUserGameInfoParam;
import com.nearme.game.sdk.common.model.biz.ReqUserInfoParam;
import com.nearme.game.sdk.common.util.AppUtil;
import com.nearme.platform.opensdk.pay.PayResponse;
import com.skysoul.MR.SDKBaseActivity;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends SDKBaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	protected void Login()
	{
		sdkDoLoign();
	}

	@Override
	protected void Pay(String message)
	{
//		JSONObject json = new JSONObject(message);
//		json.getString("price");
//		doPay(price, Name, Desc, orderId);
	}

	@Override
	protected void Logout()
	{
		Logout();
	}

	@Override
	public void Exit()
	{
		GameCenterSDK.getInstance().onExit(MainActivity.this,
				new GameExitCallback() {

					@Override
					public void exitGame() {
						// CP 实现游戏退出操作，也可以直接调用
						// AppUtil工具类里面的实现直接强杀进程~
						AppUtil.exitGameProcess(MainActivity.this);
					}
				});
	}

	public void GetTokenAndSsoid() {
		doGetTokenAndSsoid();
	}

	public void SendRoleInfo(String gameId, String service, String role, String grade) {
		sendRoleInfo(gameId, service, role, grade);
	}
	@SuppressLint("InflateParams")
	private void sendRoleInfo(String gameId, String service, String role, String grade) {
		GameCenterSDK.getInstance().doReportUserGameInfoData(
				new ReportUserGameInfoParam(gameId, service, role, grade), new ApiCallback() {

					@Override
					public void onSuccess(String resultMsg) {
//						Toast.makeText(MainActivity.this, "success",
//								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onFailure(String resultMsg, int resultCode) {
//						Toast.makeText(MainActivity.this, resultMsg,
//								Toast.LENGTH_LONG).show();
					}
				});
	}

	private void sdkDoLoign() {
		GameCenterSDK.getInstance().doLogin(this, new ApiCallback() {

			@Override
			public void onSuccess(String resultMsg) {
//				Toast.makeText(MainActivity.this, resultMsg, Toast.LENGTH_LONG)
//						.show();
//				UnitySend("LoginResult","success");
				GetTokenAndSsoid();
			}

			@Override
			public void onFailure(String resultMsg, int resultCode) {
//				Toast.makeText(MainActivity.this, resultMsg, Toast.LENGTH_LONG)
//						.show();
				LoginResult("fail");
			}
		});
	}

	private void doPay(String price,String Name,String Desc,String orderId) {
		// CP 支付参数
		int amount = Integer.parseInt(price); // 支付金额，单位分
		if(orderId.isEmpty())
		{
			orderId = System.currentTimeMillis() + new Random().nextInt(1000) + "";
		}
		PayInfo payInfo = new PayInfo( orderId, "自定义字段", amount);
		payInfo.setProductDesc(Desc);
		payInfo.setProductName(Name);
//		payInfo.setCallbackUrl("http://gamecenter.wanyol.com:8080/gamecenter/callback_test_url");

		GameCenterSDK.getInstance().doPay(this, payInfo, new ApiCallback() {

			@Override
			public void onSuccess(String resultMsg) {
//				Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_SHORT)
//						.show();
//				UnitySend("PayResult","success");
				PayResult(CODE_SUCCESS);
			}

			@Override
			public void onFailure(String resultMsg, int resultCode) {
				if (PayResponse.CODE_CANCEL != resultCode) {
//					Toast.makeText(MainActivity.this, "支付失败",
//							Toast.LENGTH_SHORT).show();
//					UnitySend("PayResult","fail");
					PayResult(CODE_FAIL);
				} else {
					// 取消支付处理
//					Toast.makeText(MainActivity.this, "支付取消",
//							Toast.LENGTH_SHORT).show();
//					UnitySend("PayResult","cancle");
					PayResult(CODE_CANCLE);
				}
			}
		});
	}

	public void doGetTokenAndSsoid() {
		GameCenterSDK.getInstance().doGetTokenAndSsoid(new ApiCallback() {

			@Override
			public void onSuccess(String resultMsg) {
				try {
					JSONObject json = new JSONObject(resultMsg);
					String token = json.getString("token");
					String ssoid = json.getString("ssoid");
//					Toast.makeText(MainActivity.this,
//							"token = " + token + "ssoid = " + ssoid,
//							Toast.LENGTH_LONG).show();
					doGetUserInfoByCpClient(token, ssoid);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String content, int resultCode) {

			}
		});
	}

	private void doGetUserInfoByCpClient(String token, String ssoid) {
		GameCenterSDK.getInstance().doGetUserInfo(
				new ReqUserInfoParam(token, ssoid), new ApiCallback() {

					@Override
					public void onSuccess(String resultMsg) {
//						Toast.makeText(MainActivity.this, resultMsg,
//								Toast.LENGTH_LONG).show();
						try {
							JSONObject json = new JSONObject(resultMsg);
							String userName = json.getString("userName");
							String openid = json.getString("ssoid");

							Map map = new HashMap();
							map.put("openid",openid);
							map.put("username",userName);
							JSONObject json1 = new JSONObject(map);
							String message = json1.toString();
//							UnitySend("LoginResult",message);
							LoginResult(message);
						} catch (JSONException e) {
							e.printStackTrace();
						}
//						UnitySend("LoginResult",resultMsg);

					}

					@Override
					public void onFailure(String resultMsg, int resultCode) {

					}
				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		GameCenterSDK.getInstance().onResume(this);
	}

	@Override
	protected void onPause() {
		GameCenterSDK.getInstance().onPause();
		super.onPause();
	}
}