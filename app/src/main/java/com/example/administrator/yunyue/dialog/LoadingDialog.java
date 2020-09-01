package com.example.administrator.yunyue.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.yunyue.R;

import pl.droidsonroids.gif.GifImageView;


/**
 * 提示消息对话框
 */
public class LoadingDialog extends Dialog {

    public static final int WAITING_A = 1;
    public static final int WAITING_B = 2;
    public static final int WAITING_C = 3;
    public static final int WAITING_D = 4;

    private int type;
    private TextView contentTxt;
    private GifImageView imageView;

    private Context context;//上下文
    private String message;//提示信息

    private String title;//标题

    public void setType(int type) {
        this.type = type;
    }

    public LoadingDialog(Context context) {
        super(context);
        this.context = context;
    }

    public LoadingDialog(Context context, String message) {
        super(context, R.style.dialog);
        this.context = context;
        this.message = message;
    }

    public LoadingDialog(Context context, int themeResId, String message, int type) {
        super(context, themeResId);
        this.context = context;
        this.message = message;
        this.type = type;
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }


    public LoadingDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        setCancelable(false);//点击手机返回键/屏幕空白区域对话框均不会消失
        initView();
    }

    /**
     * 初始化对话框
     */
    private void initView() {
        contentTxt = (TextView) findViewById(R.id.tv_loading_dialog_message);
        imageView = (GifImageView) findViewById(R.id.iv_loading_dialog_img);
        if (message.equals("")) {
            contentTxt.setVisibility(View.GONE);
        } else {
            contentTxt.setVisibility(View.VISIBLE);
            contentTxt.setText(message);
        }

        if (this.type == WAITING_A) {
            imageView.setImageResource(R.drawable.dengdai);
        } else if (type == WAITING_B) {
            imageView.setImageResource(R.drawable.dengdai);
        } else if (type == WAITING_C) {
            imageView.setImageResource(R.drawable.dengdai);
        } else {
            imageView.setImageResource(R.drawable.dengdai);
        }
    }

}
