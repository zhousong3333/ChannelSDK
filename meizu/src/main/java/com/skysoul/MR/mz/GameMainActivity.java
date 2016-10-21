package com.skysoul.MR.mz;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.meizu.gamesdk.model.callback.MzPayListener;
import com.meizu.gamesdk.model.model.MzPayParams;
import com.meizu.gamesdk.model.model.PayResultCode;
import com.meizu.gamesdk.offline.core.MzGameCenterPlatform;
import com.meizu.gamesdk.utils.MD5Utils;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

public class GameMainActivity extends UnityPlayerActivity implements MzPayListener{

	private static String Tag = "MzGameSDK";
	private String UnityGameObject = "Main Camera";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MzGameCenterPlatform.orderQueryConfirm(this, this);
		// MzGameCenterPlatform.init(this, GameConstants.GAME_ID,
		// GameConstants.GAME_KEY);
	}

	public void Init(String obj)
	{
		UnityGameObject = obj;
	}

	public void Pay(String name, String price) {
		Bundle payInfo = generatePayInfo(price,name);
		if (payInfo == null) {
			return;
		}
		try {
			MzGameCenterPlatform.singlePay(this, payInfo, this);
		}
		catch(Exception exp){
			Log.d("zhousong", exp.toString());

		}

	}

	public void onPayResult(int code, Bundle info, String errorMsg) {
		String orderid = null;
		if (info != null) {
			orderid = info.getString(MzPayParams.ORDER_KEY_ORDER_ID);
		}
		if (code == PayResultCode.PAY_SUCCESS) {
//			appendMsg("支付成功:" + orderid);
			Log.d(Tag,"支付成功:" + orderid);
			UnityPlayer.UnitySendMessage(UnityGameObject,"PayResultSuccess","支付成功");
		} else if (code == PayResultCode.PAY_USE_SMS) {
//			appendMsg("短信支付:" + orderid);
			Log.d(Tag,"短信支付:" + orderid);
//			UnityPlayer.UnitySendMessage(UnityGameObject,"PayResult","success");
		} else if (code == PayResultCode.PAY_ERROR_CANCEL) {
//			appendMsg("用户取消:" + orderid);
			Log.d(Tag,"用户取消:" + orderid);
			UnityPlayer.UnitySendMessage(UnityGameObject,"PayResultFail","用户取消");
		} else if (code == PayResultCode.PAY_ERROR_CODE_DUPLICATE_PAY) {
//			appendMsg("重复支付:" + orderid);
			Log.d(Tag,"重复支付:" + orderid);
			UnityPlayer.UnitySendMessage(UnityGameObject,"PayResultFail","重复支付");
		} else if (code == PayResultCode.PAY_ERROR_GAME_VERIFY_ERROR) {
//			appendMsg(errorMsg);
			UnityPlayer.UnitySendMessage(UnityGameObject,"PayResultFail","errorMsg");
		} else {
//			appendMsg("支付失败:" + code + "," + errorMsg);
			UnityPlayer.UnitySendMessage(UnityGameObject,"PayResultFail","支付失败");
		}

		Log.i("MzGameSDK", errorMsg + code);
	}

	private Bundle generatePayInfo(String price,String orderName) {
		double amount = 0;
		try {
			amount = Double.parseDouble(price);
		} catch (Exception e) {
		}

		if (amount <= 0 || TextUtils.isEmpty(orderName)) {
//			appendMsg("订单信息错误");
			Log.d(Tag,"订单信息错误");
			return null;
		}

		String appid = GameConstants.GAME_ID;
		String cpUserInfo = "test";
		String totalPrice = String.valueOf(amount);
		String orderId = "" + System.currentTimeMillis();
		String productId = "001";
		String productSubject = orderName;
		String productBody = "";
		int payType = 0;
		long createTiem = System.currentTimeMillis();

		StringBuilder builder = new StringBuilder();
		final String equalStr = "=";
		final String andStr = "&";
		builder.append("app_id" + equalStr + appid + andStr);
		builder.append("cp_order_id" + equalStr + orderId + andStr);
		builder.append("create_time" + equalStr + createTiem + andStr);
		builder.append("pay_type" + equalStr + payType + andStr);
		builder.append("product_body" + equalStr + productBody + andStr);
		builder.append("product_id" + equalStr + productId + andStr);
		builder.append("product_subject" + equalStr + productSubject + andStr);
		builder.append("total_price" + equalStr + totalPrice + andStr);
		builder.append("user_info" + equalStr + cpUserInfo);
		builder.append(":" + GameConstants.GAME_SECRET);
		String sign = MD5Utils.sign(builder.toString());
		String signType = "md5";

		Bundle payInfo = new Bundle();
		// appid
		payInfo.putString(MzPayParams.ORDER_KEY_ORDER_APPID, appid);
		// cp自定义信息
		payInfo.putString(MzPayParams.ORDER_KEY_CP_INFO, cpUserInfo);
		// 金额
		payInfo.putString(MzPayParams.ORDER_KEY_AMOUNT, totalPrice);
		// 订单id
		payInfo.putString(MzPayParams.ORDER_KEY_ORDER_ID, orderId);
		// 产品id
		payInfo.putString(MzPayParams.ORDER_KEY_PRODUCT_BODY, productBody);
		// 产品id
		payInfo.putString(MzPayParams.ORDER_KEY_PRODUCT_ID, productId);
		// 产品描述
		payInfo.putString(MzPayParams.ORDER_KEY_PRODUCT_SUBJECT, productSubject);

		// 支付方式，默认值：”0”（即定额支付）
		payInfo.putInt(MzPayParams.ORDER_KEY_PAY_TYPE, payType);
		// 签名
		payInfo.putString(MzPayParams.ORDER_KEY_SIGN, sign);
		// 签名类型
		payInfo.putString(MzPayParams.ORDER_KEY_SIGN_TYPE, signType);
		// 是否关闭短信支付,默认是打开状态
		payInfo.putBoolean(MzPayParams.ORDER_KEY_DISABLE_PAY_TYPE_SMS,
				true);
		payInfo.putLong(MzPayParams.ORDER_KEY_CREATE_TIME, createTiem);
		// 优先支付渠道 0:支付宝 1:微信 2:银联 3:短信
		payInfo.putInt(MzPayParams.ORDER_KEY_PAY_CHANNEL, 0);
		return payInfo;

	}

//	private void appendMsg(final String msg) {
//		mTvMsg.post(new Runnable() {
//			@Override
//			public void run() {
//				mTvMsg.append(msg + "\n");
//			}
//		});
//	}
//
//	private void setRecord(String key, String value) {
//		SharedPreferences sp = getSharedPreferences("local_history",
//				MODE_PRIVATE);
//		sp.edit().putString(key, value).apply();
//	}
//
//	private String getRecord(String key) {
//		SharedPreferences sp = getSharedPreferences("local_history",
//				MODE_PRIVATE);
//		return sp.getString(key, null);
//	}
//
//	public static boolean closeInputMethod(Context context,
//			EditText... editTexts) {
//		InputMethodManager imm = (InputMethodManager) context
//				.getSystemService(Context.INPUT_METHOD_SERVICE);
//		if (imm.isActive()) {
//			for (EditText editText : editTexts) {
//				if (editText.hasFocus()) {
//					return imm.hideSoftInputFromWindow(
//							editText.getApplicationWindowToken(), 0);
//				}
//			}
//		}
//		return false;
//	}
}
