package com.example.administrator.yunyue.erci.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.activity.SzxmmActivity;
import com.example.administrator.yunyue.activity.ZhmmActivity;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc.Validator;
import com.example.administrator.yunyue.sc_activity.ZfmmActivity;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.rong.imageloader.utils.L;

public class SfyzActivity extends AppCompatActivity {
    private static final String TAG = SfyzActivity.class.getSimpleName();
    private TextView tv_zhmm_xyb;

    private TextView tv_zhmm_hqyzm;
    private EditText et_zhmm_phone, et_zhmm_yzm;
    Validator validator = new Validator();
    private TimeCount time;
    private LinearLayout ll_sfzr_back;
    RequestQueue queue = null;
    private String sUser_id = "";
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white);
        }
        setContentView(R.layout.activity_sfyz);
        pref = PreferenceManager.getDefaultSharedPreferences(SfyzActivity.this);
        sUser_id = pref.getString("user_id", "");
        queue = Volley.newRequestQueue(SfyzActivity.this);
        time = new TimeCount(60000, 1000);
        tv_zhmm_xyb = findViewById(R.id.tv_zhmm_xyb);
        et_zhmm_phone = findViewById(R.id.et_zhmm_phone);
        tv_zhmm_hqyzm = findViewById(R.id.tv_zhmm_hqyzm);
        et_zhmm_yzm = findViewById(R.id.et_zhmm_yzm);
        ll_sfzr_back = findViewById(R.id.ll_sfzr_back);
        ll_sfzr_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_zhmm_xyb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sMobile = et_zhmm_phone.getText().toString();
                String sCode = et_zhmm_yzm.getText().toString();
                if (sMobile.equals("")) {
                    Hint("手机号不能为空！", HintDialog.WARM);
                } else if (sCode.equals("")) {
                    Hint("验证码不能为空！", HintDialog.WARM);
                } else {
                    sVerify(sMobile, sCode);
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

    /**
     * 方法名：sVerify()
     * 功  能：验证身份接口
     * 参  数：appId
     */
    private void sVerify(String sMobile, String sCode) {
        hideDialogin();
        dialogin("");
        String url = Api.sUrl + Api.sVerify;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("mobile", sMobile);
        params.put("code", sCode);
        JSONObject jsonObject = new JSONObject(params);
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialogin();
                        String sDate = response.toString();
                        System.out.println(sDate);
                        try {
                            JSONObject jsonObject0 = new JSONObject(sDate);
                            String resultMsg = jsonObject0.getString("msg");
                            int resultCode = jsonObject0.getInt("code");
                            if (resultCode > 0) {
                                Intent intent = new Intent(SfyzActivity.this, ZfmmActivity.class);
                                startActivity(intent);
                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "response -> " + response.toString());
                        //   Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialogin();
                //  Hint(error.getMessage(), HintDialog.ERROR);
                error(error);
            }
        });
        requestQueue.add(jsonRequest);
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
        loadingDialog = new LoadingDialog(SfyzActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(SfyzActivity.this, R.style.dialog, sHint, type, true).show();
    }

    private void error(VolleyError error) {
        Log.e(TAG, error.getMessage(), error);
        if (error != null) {
            if (error instanceof TimeoutError) {
                // Toast.makeText(mActivity,"网络请求超时，请重试！",Toast.LENGTH_SHORT).show();
                Hint("网络请求超时，请重试！", HintDialog.ERROR);
                return;
            }
            if (error instanceof ServerError) {
                //  Toast.makeText(mActivity,"服务器异常",Toast.LENGTH_SHORT).show();
                Hint("服务器异常", HintDialog.ERROR);
                return;
            }
            if (error instanceof NetworkError) {
                // Toast.makeText(mActivity,"请检查网络",Toast.LENGTH_SHORT).show();
                Hint("请检查网络", HintDialog.ERROR);
                return;
            }
            if (error instanceof ParseError) {
                Hint("数据格式错误", HintDialog.ERROR);
                //  Toast.makeText(mActivity,"数据格式错误",Toast.LENGTH_SHORT).show();
                return;
            }

            Hint(error.toString(), HintDialog.ERROR);
        }
    }
}
