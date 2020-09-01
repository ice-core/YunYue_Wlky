package com.example.administrator.yunyue.yjdt_activity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class YjdtZfmmActivity extends AppCompatActivity {
    private static final String TAG = YjdtZfmmActivity.class.getSimpleName();
    private ImageView iv_szmm_back;
    private EditText et_xgmm_password;
    private EditText et_xgmm_checkpassword;
    private TextView tv_xgmm_password;
    private TextView tv_xgmm_checkpassword;
    RequestQueue queue = null;
    private Button bt_szmm;
    private String sUser_id = "";
    private SharedPreferences pref;
    private TextView tv_szmm_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_zfmm);
        queue = Volley.newRequestQueue(YjdtZfmmActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        iv_szmm_back = (ImageView) findViewById(R.id.iv_szmm_back);
        et_xgmm_password = (EditText) findViewById(R.id.et_xgmm_password);
        et_xgmm_checkpassword = (EditText) findViewById(R.id.et_xgmm_checkpassword);
        tv_xgmm_password = (TextView) findViewById(R.id.tv_xgmm_password);
        tv_xgmm_checkpassword = (TextView) findViewById(R.id.tv_xgmm_checkpassword);
        tv_szmm_title = (TextView) findViewById(R.id.tv_szmm_title);
        bt_szmm = (Button) findViewById(R.id.bt_szmm);

        et_xgmm_password.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_xgmm_password.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); //最大输入长度
        et_xgmm_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        et_xgmm_checkpassword.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_xgmm_checkpassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); //最大输入长度
        et_xgmm_checkpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        bt_szmm.setEnabled(false);

        iv_szmm_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        et_xgmm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 6) {
                    tv_xgmm_password.setTextColor(tv_xgmm_password.getResources().getColor(R.color.black));
                } else {
                    tv_xgmm_password.setTextColor(tv_xgmm_password.getResources().getColor(R.color.hong));
                    bt_szmm.setEnabled(false);
                }

            }
        });
        et_xgmm_checkpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 6) {
                    if (et_xgmm_password.getText().toString().equals(String.valueOf(editable))) {
                        tv_xgmm_checkpassword.setTextColor(tv_xgmm_checkpassword.getResources().getColor(R.color.black));
                        bt_szmm.setEnabled(true);
                    } else {
                        tv_xgmm_checkpassword.setTextColor(tv_xgmm_checkpassword.getResources().getColor(R.color.hong));
                        bt_szmm.setEnabled(false);
                    }
                } else {
                    tv_xgmm_checkpassword.setTextColor(tv_xgmm_checkpassword.getResources().getColor(R.color.hong));
                    bt_szmm.setEnabled(false);
                }
            }
        });
        bt_szmm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogin("");
                sPaypwdInput(et_xgmm_password.getText().toString());
            }
        });
    }

    /**
     * 方法名：sPaypwdInput()
     * 功  能：设置支付密码
     * 参  数：appId
     */
    private void sPaypwdInput(String sPaypwd) {
        String url = Api.sUrl + Api.sPaypwdInput;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("paypwd", sPaypwd);
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
                            JSONObject jsonObject = new JSONObject(sDate);
                            String resultMsg = jsonObject.getString("msg");
                            int resultCode = jsonObject.getInt("code");
                            if (resultCode > 0) {

                                Hint(resultMsg, HintDialog.SUCCESS);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 1200);
                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "response -> " + response.toString());
                        //   Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialogin();
                //  Hint(error.getMessage(), HintDialog.ERROR);
                error(error);
            }
        });
        requestQueue.add(jsonRequest);
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


    private void back() {
        finish();
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

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(YjdtZfmmActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(YjdtZfmmActivity.this, R.style.dialog, sHint, type, true).show();
    }
}
