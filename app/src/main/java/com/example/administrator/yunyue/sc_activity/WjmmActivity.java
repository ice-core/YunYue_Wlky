package com.example.administrator.yunyue.sc_activity;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
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

import java.util.HashMap;
import java.util.Map;

public class WjmmActivity extends AppCompatActivity {
    private static final String TAG = WjmmActivity.class.getSimpleName();
    private TimeCount time;
    private Button bt_wjmm_fs;
    Validator validator = new Validator();
    private EditText et_wjmm_phone;
    private EditText et_wjmm_yzm;
    private EditText et_wjmm_password;
    private Button bt_wjmm_save;
    private ImageView iv_wjmm_back;
    private EditText et_wjmm_checkpassword;
    private TextView tv_wjmm_password;
    private TextView tv_wjmm_checkpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_wjmm);
        time = new TimeCount(60000, 1000);
        et_wjmm_phone = (EditText) findViewById(R.id.et_wjmm_phone);
        bt_wjmm_fs = (Button) findViewById(R.id.bt_wjmm_fs);
        et_wjmm_yzm = (EditText) findViewById(R.id.et_wjmm_yzm);
        iv_wjmm_back = (ImageView) findViewById(R.id.iv_wjmm_back);
        et_wjmm_password = (EditText) findViewById(R.id.et_wjmm_password);
        et_wjmm_checkpassword = (EditText) findViewById(R.id.et_wjmm_checkpassword);
        et_wjmm_password.setTransformationMethod(PasswordTransformationMethod.getInstance());//密码隐藏
        et_wjmm_checkpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());//密码隐藏
        bt_wjmm_save = (Button) findViewById(R.id.bt_wjmm_save);
        tv_wjmm_password = (TextView) findViewById(R.id.tv_wjmm_password);
        tv_wjmm_checkpassword = (TextView) findViewById(R.id.tv_wjmm_checkpassword);
        bt_wjmm_save.setEnabled(false);
        iv_wjmm_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        bt_wjmm_fs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validator.isMobile(et_wjmm_phone.getText().toString())) {
                    dialogin("");
                    yanzhengma();
                    time.start();
                } else {
                    Hint("请输入正确的手机号", HintDialog.WARM);
                    //  Toast.makeText(NoteActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bt_wjmm_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validator.isMobile(et_wjmm_phone.getText().toString())) {
                    if (et_wjmm_yzm.getText().toString().equals("")) {
                        Hint("验证码为空", HintDialog.WARM);
                    } else {
                        if (et_wjmm_password.getText().toString().equals("")) {
                            Hint("请填写密码", HintDialog.WARM);
                        } else {
                            dialogin("");
                            save();
                        }
                    }
                } else {
                    Hint("请输入正确的手机号", HintDialog.WARM);
                    //  Toast.makeText(NoteActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
            }
        });

        et_wjmm_checkpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_wjmm_password.getText().toString().equals(String.valueOf(editable))) {
                    tv_wjmm_password.setTextColor(tv_wjmm_checkpassword.getResources().getColor(R.color.black));
                    tv_wjmm_checkpassword.setTextColor(tv_wjmm_checkpassword.getResources().getColor(R.color.black));
                    bt_wjmm_save.setEnabled(true);
                } else {
                    tv_wjmm_checkpassword.setTextColor(tv_wjmm_checkpassword.getResources().getColor(R.color.hong));
                    bt_wjmm_save.setEnabled(false);
                }
            }
        });
    }

    private void initialize() {
        et_wjmm_phone.setText("");
        et_wjmm_yzm.setText("");
        et_wjmm_password.setText("");
        bt_wjmm_fs.setBackgroundResource(R.drawable.login);
    }

    private void save() {
        String url = Api.sUrl + "Login/getPwd/";
        RequestQueue requestQueue = Volley.newRequestQueue(WjmmActivity.this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", et_wjmm_phone.getText().toString());
        params.put("dxcode", et_wjmm_yzm.getText().toString());
        params.put("password", et_wjmm_password.getText().toString());
        JSONObject jsonObject = new JSONObject(params);
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialogin();
                        String sDate = response.toString();
                        System.out.println(sDate);
                        JSONObject jsonObject1 = null;
                        try {
                            jsonObject1 = new JSONObject(sDate);
                            String resultMsg = jsonObject1.getString("msg");
                            int resultCode = jsonObject1.getInt("code");
                            Log.d(TAG, "response -> " + response.toString());
                            //     Toast.makeText(NoteActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            if (resultCode > 0) {
                                initialize();
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
            public void onErrorResponse(VolleyError error) {
                hideDialogin();
                Log.e(TAG, error.getMessage(), error);
                Hint(error.getMessage(), HintDialog.ERROR);
                //   Toast.makeText(NoteActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        String url = "http://aicomm.bndvip.com/Api/Login/dxSendCode/";
        RequestQueue requestQueue = Volley.newRequestQueue(WjmmActivity.this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", et_wjmm_phone.getText().toString());
        params.put("type", "3");
        JSONObject jsonObject = new JSONObject(params);
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialogin();
                        String sDate = response.toString();
                        System.out.println(sDate);
                        JSONObject jsonObject1 = null;
                        try {
                            jsonObject1 = new JSONObject(sDate);
                            String resultMsg = jsonObject1.getString("msg");
                            int resultCode = jsonObject1.getInt("code");
                            Log.d(TAG, "response -> " + response.toString());
                            //     Toast.makeText(NoteActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            if (resultCode > 0) {
                                et_wjmm_yzm.setText("");
                                Hint(resultCode + "*****" + resultMsg, HintDialog.SUCCESS);
                            } else {
                                Hint(resultCode + "*****" + resultMsg, HintDialog.ERROR);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialogin();
                Log.e(TAG, error.getMessage(), error);
                Hint(error.getMessage(), HintDialog.ERROR);
                //   Toast.makeText(NoteActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonRequest);
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
            bt_wjmm_fs.setBackgroundResource(R.drawable.jf);
            bt_wjmm_fs.setClickable(false);
            bt_wjmm_fs.setText("  " + millisUntilFinished / 1000 + "秒后重新发送  ");
        }

        @Override
        public void onFinish() {
            bt_wjmm_fs.setText(" 重新获取验证码 ");
            bt_wjmm_fs.setClickable(true);
            bt_wjmm_fs.setBackgroundResource(R.drawable.login);

        }
    }


    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(WjmmActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(WjmmActivity.this, R.style.dialog, sHint, type, true).show();
    }
}
