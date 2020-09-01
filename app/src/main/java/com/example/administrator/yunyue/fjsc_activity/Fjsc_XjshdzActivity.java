/**
 * 文件名：Fjsc_XjshdzActivity
 * 描  述：新增收货地址
 * 作  者：icecore
 * 版  权：
 * 时  间：2019/10/15 11:05
 */
package com.example.administrator.yunyue.fjsc_activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.baidudingwei.Baidu_XqwzActivity;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fjsc_XjshdzActivity extends AppCompatActivity {
    private static final String TAG = Fjsc_XjshdzActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_fjsc_xjshdz_back;
    /**
     * 收货地址
     */
    private TextView tv_fjsc_xjshdz_dz;
    public static String sDz = "";
    /**
     * 详细地址
     */
    private EditText et_fjsc_xjshdz_xxdz;
    /**
     * 联系人
     */
    private EditText et_fjsc_xjshdz_lxr;
    /**
     * 男
     */
    private CheckBox cb_fjsc_xjshdz_nan;
    /**
     * 女
     */
    private CheckBox cb_fjsc_xjshdz_nv;
    /**
     * 手机号
     */
    private EditText et_fjsc_xjshdz_sjh;
    /**
     * 保存地址
     */
    private TextView tv_fjsc_xjshdz_bcdz;
    private SharedPreferences pref;
    private String sUser_id = "";
    /**
     * 1先生 2女士
     */
    int sex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_fjsc__xjshdz);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        ll_fjsc_xjshdz_back = findViewById(R.id.ll_fjsc_xjshdz_back);
        ll_fjsc_xjshdz_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        tv_fjsc_xjshdz_dz = findViewById(R.id.tv_fjsc_xjshdz_dz);
        tv_fjsc_xjshdz_dz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Fjsc_XjshdzActivity.this, Baidu_XqwzActivity.class);
                startActivity(intent);
            }
        });
        et_fjsc_xjshdz_xxdz = findViewById(R.id.et_fjsc_xjshdz_xxdz);
        et_fjsc_xjshdz_lxr = findViewById(R.id.et_fjsc_xjshdz_lxr);
        cb_fjsc_xjshdz_nan = findViewById(R.id.cb_fjsc_xjshdz_nan);
        cb_fjsc_xjshdz_nv = findViewById(R.id.cb_fjsc_xjshdz_nv);
        et_fjsc_xjshdz_sjh = findViewById(R.id.et_fjsc_xjshdz_sjh);
        tv_fjsc_xjshdz_bcdz = findViewById(R.id.tv_fjsc_xjshdz_bcdz);
        tv_fjsc_xjshdz_bcdz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = et_fjsc_xjshdz_sjh.getText().toString();
                String name = et_fjsc_xjshdz_lxr.getText().toString();
                String detail = et_fjsc_xjshdz_xxdz.getText().toString();
                if (detail.equals("")) {
                    Hint("请填写详细地址！", HintDialog.WARM);
                } else if (name.equals("")) {
                    Hint("请填写收货人姓名！", HintDialog.WARM);
                } else if (phone.equals("")) {
                    Hint("请填写收货人手机号！", HintDialog.WARM);
                } else {
                    sAddressadd(name, phone, detail);
                }
            }
        });
        cb_fjsc_xjshdz_nan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_fjsc_xjshdz_nan.isChecked()) {
                    cb_fjsc_xjshdz_nan.setChecked(true);
                    cb_fjsc_xjshdz_nv.setChecked(false);
                    sex = 1;
                }
            }
        });
        cb_fjsc_xjshdz_nv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_fjsc_xjshdz_nv.isChecked()) {
                    cb_fjsc_xjshdz_nan.setChecked(false);
                    cb_fjsc_xjshdz_nv.setChecked(true);
                    sex = 2;
                }
            }
        });

    }

    /**
     * 方法名：sAddressadd()
     * 功  能：新增地址
     * 参  数：appId
     * username--收件人名称
     * phone--收件人电话
     * sheng--省 名称
     * shi--市 名称
     * qu--	区 名称
     * detail--详细地址
     * sex--1先生 2女士
     * is_default--1为默认地址
     */
    private void sAddressadd(String username, String phone, String detail) {
        String url = Api.sUrl + Api.sAddressadd;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("username", username);
        params.put("phone", phone);
        params.put("sheng", sDz.replace("null", ""));
        params.put("shi", " ");
        params.put("qu", " ");
        params.put("detail", detail);
        params.put("sex", String.valueOf(sex));
        params.put("is_default", "1");
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
                            if (resultCode > 0) {
                                Hint(resultMsg, HintDialog.SUCCESS);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 1000);
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

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        tv_fjsc_xjshdz_dz.setText(sDz.replace("null", ""));
    }

    private void back() {
        sDz = "";
        finish();

    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(Fjsc_XjshdzActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(Fjsc_XjshdzActivity.this, R.style.dialog, sHint, type, true).show();
    }
}
