package me.cizezsy.yourdrawiguess.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.cizezsy.yourdrawiguess.MyApplication;
import me.cizezsy.yourdrawiguess.R;
import me.cizezsy.yourdrawiguess.model.User;
import me.cizezsy.yourdrawiguess.net.YdigRetrofitFactory;
import me.cizezsy.yourdrawiguess.ui.widget.CleanEditText;
import me.cizezsy.yourdrawiguess.util.RegexUtils;
import me.cizezsy.yourdrawiguess.util.ToastUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.view.View.OnClickListener;

public class LoginActivity extends Activity implements OnClickListener {

    private static final int REQUEST_CODE_TO_REGISTER = 0x001;

    // 界面控件
    @BindView(R.id.et_email_phone)
    CleanEditText phoneEdit;
    @BindView(R.id.et_password)
    CleanEditText passwordEdit;
    @BindView(R.id.progressBar)
    ContentLoadingProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initViews();
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        phoneEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        phoneEdit.setTransformationMethod(HideReturnsTransformationMethod
                .getInstance());
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_GO);
        passwordEdit.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
        passwordEdit.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE
                    || actionId == EditorInfo.IME_ACTION_GO) {
                clickLogin();
            }
            return false;
        });
    }

    private void clickLogin() {
        String phone = phoneEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        if (checkInput(phone, password)) {
            mProgressBar.setVisibility(View.VISIBLE);
            YdigRetrofitFactory.getService()
                    .login(phone, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(message -> {
                        mProgressBar.setVisibility(View.GONE);
                        if (message.getStatusCode() != 200) {
                            ToastUtils.showShort(LoginActivity.this, "登录失败" + message.getData().toString());
                            return;
                        }
                        User user = new User();
                        user.setUsername(message.getData().toString());
                        MyApplication.setUser(user);
                        enterMainActivity();
                    }, throwable -> {
                        mProgressBar.setVisibility(View.GONE);
                        ToastUtils.showShort(LoginActivity.this, "登录失败" + throwable.getMessage());
                    });
        }
    }

    public boolean checkInput(String account, String password) {
        // 账号为空时提示
        if (account == null || account.trim().equals("")) {
            Toast.makeText(this, R.string.tip_account_empty, Toast.LENGTH_LONG)
                    .show();
        } else {
            // 账号不匹配手机号格式（11位数字且以1开头）
            if (!RegexUtils.checkMobile(account)) {
                Toast.makeText(this, R.string.tip_account_regex_not_right,
                        Toast.LENGTH_LONG).show();
            } else if (password == null || password.trim().equals("")) {
                Toast.makeText(this, R.string.tip_password_can_not_be_empty,
                        Toast.LENGTH_LONG).show();
            } else {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
                finish();
                break;
            case R.id.btn_login:
                clickLogin();
                break;
            case R.id.tv_create_account:
                enterRegister();
                break;
            default:
                break;
        }
    }

    /**
     * 跳转到注册页面
     */
    private void enterRegister() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivityForResult(intent, REQUEST_CODE_TO_REGISTER);
    }

    private void enterMainActivity() {
        Intent intent = new Intent(this, RoomListActivity.class);
        startActivity(intent);
        finish();
    }
}
