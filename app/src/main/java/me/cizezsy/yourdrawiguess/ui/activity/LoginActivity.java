package me.cizezsy.yourdrawiguess.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }


    //TODO 向服务器发起登录请求时调用此方法
    private void login(String username, String password) {

    }

    //TODO 验证输入的正确性
    private boolean checkInput(String username, String password) {
        return false;
    }

}
