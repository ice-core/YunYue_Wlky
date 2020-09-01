package com.example.administrator.yunyue.erci.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sq_activity.DakaActivity;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RwzxActivity extends AppCompatActivity {
    private static final String TAG = RwzxActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_rwzx_back;
    /**
     * 今日签到
     */
    private TextView tv_rwzx_qd;
    /**
     * 推荐注册
     */
    private TextView tv_rwzx_zc;
    /**
     * 实名认证
     */
    private TextView tv_rwzx_rz;
    private SharedPreferences pref;
    private String sUser_id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_rwzx);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        ll_rwzx_back = findViewById(R.id.ll_rwzx_back);
        ll_rwzx_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_rwzx_qd = findViewById(R.id.tv_rwzx_qd);
        tv_rwzx_qd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RwzxActivity.this, DakaActivity.class);
                startActivity(intent);
            }
        });

        tv_rwzx_zc = findViewById(R.id.tv_rwzx_zc);
        tv_rwzx_zc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RwzxActivity.this, WdmpActivity.class);
                startActivity(intent);
            }
        });
        tv_rwzx_rz = findViewById(R.id.tv_rwzx_rz);
        tv_rwzx_rz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RwzxActivity.this, GrzlActivity.class);
                startActivity(intent);
            }
        });
        sTaskCenter();
    }

    /**
     * 方法名：sTaskCenter()
     * 功  能：任务中心接口
     * 参  数：appId
     */
    private void sTaskCenter() {
        hideDialogin();
        dialogin("");
        String url = Api.sUrl + Api.sTaskCenter;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id",sUser_id);
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
                                JSONObject jsonObject1 = new JSONObject(sDate.toString()).getJSONObject("data");
                                String resultSign = jsonObject1.getString("sign");
                                String resultIdentification = jsonObject1.getString("identification");

                                if (resultSign.equals("1")) {
                                    tv_rwzx_qd.setClickable(false);
                                    tv_rwzx_qd.setText("已完成");
                                    tv_rwzx_qd.setBackgroundResource(R.drawable.bj_dfdfdf_14);
                                    tv_rwzx_qd.setTextColor(tv_rwzx_qd.getResources().getColor(R.color.hui999999));
                                } else {
                                    tv_rwzx_qd.setClickable(true);
                                    tv_rwzx_qd.setText("去签到");
                                    tv_rwzx_qd.setBackgroundResource(R.drawable.bk_theme_14);
                                    tv_rwzx_qd.setTextColor(tv_rwzx_qd.getResources().getColor(R.color.theme));
                                }


                                if (resultIdentification.equals("1")) {
                                    tv_rwzx_rz.setClickable(false);
                                    tv_rwzx_rz.setText("已完成");
                                    tv_rwzx_rz.setBackgroundResource(R.drawable.bj_dfdfdf_14);
                                    tv_rwzx_rz.setTextColor(tv_rwzx_qd.getResources().getColor(R.color.hui999999));
                                } else {
                                    tv_rwzx_rz.setClickable(true);
                                    tv_rwzx_rz.setText("去认证");
                                    tv_rwzx_rz.setBackgroundResource(R.drawable.bk_theme_14);
                                    tv_rwzx_rz.setTextColor(tv_rwzx_qd.getResources().getColor(R.color.theme));
                                }

                            } else {
                                //  Hint(resultMsg, HintDialog.ERROR);
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

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(RwzxActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(RwzxActivity.this, R.style.dialog, sHint, type, true).show();
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
