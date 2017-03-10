package me.cizezsy.yourdrawiguess.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

//TODO 自定义EditText，加入输入框右侧小叉，清除内容
public class ClearEditText extends EditText {

    public ClearEditText(Context context) {
        super(context);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
