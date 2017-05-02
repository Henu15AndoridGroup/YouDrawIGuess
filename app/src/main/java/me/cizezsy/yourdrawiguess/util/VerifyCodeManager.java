package me.cizezsy.yourdrawiguess.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

import me.cizezsy.yourdrawiguess.R;

public class VerifyCodeManager {
	
	public final static int REGISTER = 1;
	public final static int RESET_PWD = 2;
	public final static int BIND_PHONE = 3;

	private Context mContext;
	private int recLen = 60;
	private Timer timer = new Timer();
	private Handler mHandler = new Handler();
	private String phone;
	
	private EditText phoneEdit;
	private Button getVerifiCodeButton;
	
	public VerifyCodeManager(Context context, EditText editText, Button btn) {
		this.mContext = context;
		this.phoneEdit = editText;
		this.getVerifiCodeButton = btn;
	}

	public void getVerifyCode(int type) {
		// 获取验证码之前先判断手机号

		phone = phoneEdit.getText().toString().trim();

		if (TextUtils.isEmpty(phone)) {
			ToastUtils.showShort(mContext, R.string.tip_please_input_phone);
			return;
		} else if (phone.length() < 11) {
			ToastUtils.showShort(mContext, R.string.tip_phone_regex_not_right);
			return;
		} else if (!RegexUtils.checkMobile(phone)) {
			ToastUtils.showShort(mContext, R.string.tip_phone_regex_not_right);
			return;
		}

		// 2. 请求服务端，由服务端为客户端发送验证码
//		HttpRequestHelper.getInstance().getVerifyCode(mContext, phone, type,
//				getVerifyCodeHandler);

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				mHandler.post(() -> {
                    setButtonStatusOff();
                    if (recLen < 1) {
                        setButtonStatusOn();
                    }
                });
			}
		};

		timer = new Timer();
		timer.schedule(task, 0, 1000);

	}

	private void setButtonStatusOff() {
		getVerifiCodeButton.setText(String.format(
				mContext.getResources().getString(R.string.count_down), recLen+""));
		recLen--;
		getVerifiCodeButton.setClickable(false);
		getVerifiCodeButton.setTextColor(Color.parseColor("#f3f4f8"));
		getVerifiCodeButton.setBackgroundColor(Color.parseColor("#b1b1b3"));
	}

	private void setButtonStatusOn() {
		timer.cancel();
		getVerifiCodeButton.setText("重新发送");
		getVerifiCodeButton.setTextColor(Color.parseColor("#b1b1b3"));
		getVerifiCodeButton.setBackgroundColor(Color.parseColor("#f3f4f8"));
		recLen = 60;
		getVerifiCodeButton.setClickable(true);
	}

}
