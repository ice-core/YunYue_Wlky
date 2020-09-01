package com.example.administrator.yunyue.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.yunyue.R;

import org.json.JSONException;


/**
 * 提示消息对话框
 */
public class HintDialog extends Dialog implements View.OnClickListener {

    public static final int WARM = 1;
    public static final int ERROR = 2;
    public static final int SUCCESS = 3;

    private int type;//成功,警告,失败
    private TextView contentTxt;
    private TextView submitTxt;
    private ImageView imageView;

    private Context context;//上下文
    private String message;//提示信息

    /**
     * 为false 点击屏幕空白区域时,对话框不会消失
     * 为true  点击屏幕空白区域,或点击手机返回键,对话框消失
     *
     * @param flag
     */
    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setContent(String message) {
        this.message = message;
    }

    public HintDialog(Context context) {
        super(context);
        this.context = context;
    }

    public HintDialog(Context context, String message) {
        super(context, R.style.dialog);
        this.context = context;
        this.message = message;
    }

    public HintDialog(Context context, int themeResId, String message) {
        super(context, themeResId);
        this.context = context;
        this.message = message;
    }

    public HintDialog(Context context, int themeResId, String message, int type, boolean isAutoCancel) {
        super(context, themeResId);
        this.type = type;
        this.context = context;
        this.message = message;
        setCancelable(isAutoCancel);
    }

    public HintDialog(Context context, int themeResId, String message, int type) {
        super(context, themeResId);
        this.type = type;
        this.context = context;
        this.message = message;
    }

    protected HintDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_hint_12);
        initView();
    }

    /**
     * 初始化对话框
     */
    private void initView() {

        contentTxt = (TextView) findViewById(R.id.tv_hint_dialog_message);
        submitTxt = (TextView) findViewById(R.id.btn_hint_dialog_confirm);
        imageView = (ImageView) findViewById(R.id.iv_hint_dialog_img);
        submitTxt.setOnClickListener(this);
        contentTxt.setText(message);

        if (this.type == SUCCESS) {
            imageView.setImageResource(R.drawable.success);
        } else if (type == ERROR) {
            imageView.setImageResource(R.drawable.error);
        } else {
            imageView.setImageResource(R.drawable.warm);
        }
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        cancel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 1200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击事件
     * 点击取消按钮
     * 点击确认按钮
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_hint_dialog_confirm:
                this.cancel();
                break;
        }
    }

}
