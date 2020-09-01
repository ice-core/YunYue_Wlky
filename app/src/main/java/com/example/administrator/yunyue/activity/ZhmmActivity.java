package com.example.administrator.yunyue.activity;

import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc.Validator;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

public class ZhmmActivity extends AppCompatActivity {
    private TextView tv_zhmm_xyb;
    private static final String TAG = SzxmmActivity.class.getSimpleName();
    private TextView tv_zhmm_hqyzm;
    private EditText et_zhmm_phone, et_zhmm_yzm;
    Validator validator = new Validator();
    private TimeCount time;
    public static String sPhone = "", sYzm = "";
    private ImageView iv_zhmm_back;
    RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white);
        }
        setContentView(R.layout.activity_zhmm);
        queue = Volley.newRequestQueue(ZhmmActivity.this);
        time = new TimeCount(60000, 1000);
        tv_zhmm_xyb = findViewById(R.id.tv_zhmm_xyb);
        et_zhmm_phone = findViewById(R.id.et_zhmm_phone);
        tv_zhmm_hqyzm = findViewById(R.id.tv_zhmm_hqyzm);
        et_zhmm_yzm = findViewById(R.id.et_zhmm_yzm);
        iv_zhmm_back = findViewById(R.id.iv_zhmm_back);
        iv_zhmm_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_zhmm_xyb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sPhone = et_zhmm_phone.getText().toString();
                sYzm = et_zhmm_yzm.getText().toString();
                if (sYzm.equals("")) {
                } else {
                    Intent intent = new Intent(ZhmmActivity.this, SzxmmActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        tv_zhmm_hqyzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validator.isMobile(et_zhmm_phone.getText().toString())) {

                    dialogin("");
                    yanzhengma();
                    time.start();
                } else {
                    Hint("请输入正确的手机号", HintDialog.WARM);
                    //  Toast.makeText(RegisterActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void back() {
 /*       Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);*/
        finish();
    }

    private void yanzhengma() {
        String url = Api.sUrl + "Api/Login/sendsms/appId/" + Api.sApp_Id + "/mobile/"
                + et_zhmm_phone.getText().toString() + "/state/3";
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        //et_zhmm_phone.setText("");
                        Hint(resultMsg, HintDialog.SUCCESS);
                    } else {
                        Hint(resultMsg, HintDialog.ERROR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Log.d(TAG, "onKeyDown KEYCODE_BACK");
                //   showDialog();
                back();
                break;
            /*
            * Home键是系统事件，不能通过KeyDown监听
            * 此处log不会打印
            */
            case KeyEvent.KEYCODE_HOME:
                Log.d(TAG, "onKeyDown KEYCODE_HOME");
                break;
            case KeyEvent.KEYCODE_MENU:
                Log.d(TAG, "onKeyDown KEYCODE_MENU");
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_zhmm_hqyzm.setBackgroundResource(R.drawable.jf);
            tv_zhmm_hqyzm.setClickable(false);
            tv_zhmm_hqyzm.setText("  " + millisUntilFinished / 1000 + "秒后重新发送  ");
        }

        @Override
        public void onFinish() {
            tv_zhmm_hqyzm.setText(" 重新获取验证码 ");
            tv_zhmm_hqyzm.setClickable(true);
            tv_zhmm_hqyzm.setBackgroundResource(R.drawable.bk_lan8);

        }
    }


    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(ZhmmActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
        loadingDialog.show();
    }

    private void hideDialogin() {
        if (!NullTranslator.isNullEmpty(loadingDialog)) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 消息提示
     */
    protected void Hint(String sHint, int type) {
        new HintDialog(ZhmmActivity.this, R.style.dialog, sHint, type, true).show();
    }
}
