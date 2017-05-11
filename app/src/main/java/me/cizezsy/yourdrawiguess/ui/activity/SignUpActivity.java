package me.cizezsy.yourdrawiguess.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.cizezsy.yourdrawiguess.MyApplication;
import me.cizezsy.yourdrawiguess.R;
import me.cizezsy.yourdrawiguess.model.User;
import me.cizezsy.yourdrawiguess.net.YdigRetrofitFactory;
import me.cizezsy.yourdrawiguess.ui.widget.CleanEditText;
import me.cizezsy.yourdrawiguess.util.RegexUtils;
import me.cizezsy.yourdrawiguess.util.ToastUtils;
import me.cizezsy.yourdrawiguess.util.VerificationCodeManager;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignUpActivity extends Activity implements OnClickListener {

    // 界面控件
    @BindView(R.id.et_phone)
    CleanEditText phoneEdit;
    @BindView(R.id.et_password)
    CleanEditText passwordEdit;
    @BindView(R.id.et_verifiCode)
    CleanEditText verifyCodeEdit;
    @BindView(R.id.et_nickname)
    CleanEditText usernameEdit;
    @BindView(R.id.btn_send_verifi_code)
    Button getVerificationCodeButton;
    @BindView(R.id.btn_create_account)
    Button createAccountButton;
    @BindView(R.id.progressBar)
    ContentLoadingProgressBar mProgressBar;

    private VerificationCodeManager codeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        initViews();
        codeManager = new VerificationCodeManager(this, phoneEdit, getVerificationCodeButton);

    }

    private void initViews() {

        getVerificationCodeButton.setOnClickListener(this);
        phoneEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);// 下一步
        verifyCodeEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);// 下一步
        usernameEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_GO);
        passwordEdit.setOnEditorActionListener((v, actionId, event) -> {
            // 点击虚拟键盘的done
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
                commit();
            }
            return false;
        });

        createAccountButton.setOnClickListener(view -> commit());
    }

    private void commit() {
        String phone = phoneEdit.getText().toString().trim();
        String username = usernameEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        String code = verifyCodeEdit.getText().toString().trim();

        if (checkInput(phone, username, password, code)) {
            //TODO 加密密码
            mProgressBar.setVisibility(View.VISIBLE);
            YdigRetrofitFactory.getService()
                    .signUp(username, password, phone, code)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(message -> {
                        mProgressBar.setVisibility(View.GONE);
                        if (message.getStatusCode() != 200) {
                            ToastUtils.showShort(SignUpActivity.this, "注册失败" + message.getData());
                            return;
                        }
                        ToastUtils.showShort(SignUpActivity.this, "注册成功");
                        User user = new User();
                        user.setUsername(username);
                        user.setPhone(phone);
                        MyApplication.setUser(user);
                        enterMainActivity();
                    }, throwable -> {
                        mProgressBar.setVisibility(View.GONE);
                        ToastUtils.showShort(SignUpActivity.this, "注册失败" + throwable.getMessage());
                    });
        }
    }

    private boolean checkInput(String phone, String username, String password, String code) {
        if (TextUtils.isEmpty(phone)) { // 电话号码为空
            ToastUtils.showShort(this, R.string.tip_phone_can_not_be_empty);
        } else {
            if (!RegexUtils.checkMobile(phone)) { // 电话号码格式有误
                ToastUtils.showShort(this, R.string.tip_phone_regex_not_right);
            } else if (TextUtils.isEmpty(code)) { // 验证码不正确
                ToastUtils.showShort(this, R.string.tip_please_input_code);
            } else if (TextUtils.isEmpty(username)) {
                ToastUtils.showShort(this, R.string.please_enter_your_username);
            } else if (password.length() < 6 || password.length() > 32
                    || TextUtils.isEmpty(password)) { // 密码格式
                ToastUtils.showShort(this,
                        R.string.tip_please_input_6_32_password);
            } else {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_verifi_code:
                codeManager.getVerifyCode(VerificationCodeManager.REGISTER);
                break;

            default:
                break;
        }
    }

    private void enterMainActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        finish();
    }
}
