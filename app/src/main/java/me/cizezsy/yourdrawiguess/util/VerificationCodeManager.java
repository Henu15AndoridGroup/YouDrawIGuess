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
import me.cizezsy.yourdrawiguess.net.YdigRetrofitFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VerificationCodeManager {
	
	public final static int REGISTER = 1;
	public final static int RESET_PWD = 2;
	public final static int BIND_PHONE = 3;

	private Context mContext;
	private int recLen = 60;
	private Timer timer = new Timer();
	private Handler mHandler = new Handler();
	private String phone;
	
	private EditText phoneEdit;
	private Button getVerificationCodeButton;
	
	public VerificationCodeManager(Context context, EditText editText, Button btn) {
		this.mContext = context;
		this.phoneEdit = editText;
		this.getVerificationCodeButton = btn;
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

		sendVerificationCode();

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

	private void sendVerificationCode() {
		YdigRetrofitFactory.getService()
				.getVerificationCode(phone)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(message -> {
                    if (message.getStatusCode() != 200) {
                        ToastUtils.showShort(mContext, "获取验证码失败" + message.getData());
                    }
                }, throwable -> ToastUtils.showShort(mContext, "获取验证码失败" + throwable.getMessage()));
	}

	private void setButtonStatusOff() {
		getVerificationCodeButton.setText(String.format(
				mContext.getResources().getString(R.string.count_down), recLen+""));
		recLen--;
		getVerificationCodeButton.setClickable(false);
		getVerificationCodeButton.setTextColor(Color.parseColor("#f3f4f8"));
		getVerificationCodeButton.setBackgroundColor(Color.parseColor("#b1b1b3"));
	}

	private void setButtonStatusOn() {
		timer.cancel();
		getVerificationCodeButton.setText("重新发送");
		getVerificationCodeButton.setTextColor(Color.parseColor("#b1b1b3"));
		getVerificationCodeButton.setBackgroundColor(Color.parseColor("#f3f4f8"));
		recLen = 60;
		getVerificationCodeButton.setClickable(true);
	}

}
