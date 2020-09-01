package com.example.administrator.yunyue.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.yunyue.R;


/**
 * 提示消息对话框
 */
public class PromptDialog extends Dialog implements View.OnClickListener{

    private TextView contentTxt;
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;
    private Context context;//上下文
    private String message;//提示信息
    private OnClickListener listener;//按钮点击事件接口
    private String positiveName;//确认按钮名称,默认为确认
    private String negativeName;//取消按钮名称,默认为取消

    //设置确认按钮名称
    public void setPositiveName(String positiveName) {
        this.positiveName = positiveName;
    }

    //设置取消按钮名称
    public void setNegativeName(String negativeName) {
        this.negativeName = negativeName;
    }

    private String title;//标题
    public PromptDialog(Context context) {
        super(context);
        this.context = context;
    }
    public PromptDialog(Context context, String message) {
        super(context, R.style.dialog);
        this.context = context;
        this.message = message;
    }

    public PromptDialog(Context context, int themeResId, String message) {
        super(context, themeResId);
        this.context = context;
        this.message = message;
    }

    public PromptDialog(Context context, int themeResId, String message, OnClickListener cancel) {
        super(context, themeResId);
        this.context = context;
        this.message = message;
        this.listener = cancel;
    }

    protected PromptDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }


    public PromptDialog setTitle(String title){
        this.title = title;
        return this;
    }

    public PromptDialog setPositiveButton(String name){
        this.positiveName = name;
        return this;
    }

    public PromptDialog setNegativeButton(String name){
        this.negativeName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_prompt);
        setCancelable(false);//点击手机返回键/屏幕空白区域对话框均不会消失
        initView();
    }

    /**
     * 初始化对话框
     */
    private void initView(){
        contentTxt = (TextView)findViewById(R.id.tv_prompt_dialog_message);
        titleTxt = (TextView)findViewById(R.id.tv_prompt_dialog_title);
        submitTxt = (TextView)findViewById(R.id.btn_prompt_dialog_confirm);
        submitTxt.setOnClickListener(this);
        cancelTxt = (TextView)findViewById(R.id.btn_prompt_dialog_cancel);
        cancelTxt.setOnClickListener(this);

        contentTxt.setText(message);
        if(!TextUtils.isEmpty(positiveName)){
            submitTxt.setText(positiveName);
        }

        if(!TextUtils.isEmpty(negativeName)){
            cancelTxt.setText(negativeName);
        }

        if(!TextUtils.isEmpty(title)){
            titleTxt.setText(title);
        }

    }

    /**
     * 点击事件
     * 点击取消按钮
     * 点击确认按钮
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_prompt_dialog_cancel:
                if(listener != null){
                    listener.onCancelClick(this, true);
                }
                break;
            case R.id.btn_prompt_dialog_confirm:
                if(listener != null){
                    listener.onConfimClick(this, true);
                }
                break;
        }
    }

    public interface OnClickListener{
        //取消按钮
        void onCancelClick(Dialog dialog, boolean confirm);
        //确定按钮
        void onConfimClick(Dialog dialog, boolean confirm);
    }

}
