package com.example.administrator.yunyue.erci.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
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
import com.example.administrator.yunyue.sc.CircleImageView;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KyxqXqActivity extends AppCompatActivity {
    private static final String TAG = KyxqXqActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_kyxqxq_back;
    /**
     * 用户头像
     */
    private CircleImageView civ_kyxqxq_headimgurl;
    /**
     * 用户昵称
     */
    private TextView tv_kyxqxq_nickname;
    /**
     * 发布时间
     */
    private TextView tv_kyxqxq_create_time;
    /**
     * 需求标题
     */
    private TextView tv_kyxqxq_title;
    /**
     * 预算工分
     */
    private TextView tv_kyxqxq_budget;
    /**
     * 服务时间
     */
    private TextView tv_kyxqxq_service_time;
    /**
     * 服务地点
     */
    private TextView tv_kyxqxq_service_place;
    /**
     * 服务描述
     */
    private TextView tv_kyxqxq_describe;
    /**
     * 接单
     */
    private TextView tv_kyxqxq_jd;
    /**
     * 需求id
     */
    private String sNeed_id = "";
    private String sUser_id = "";
    private SharedPreferences pref;
    private String sIs_vip = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_kyxq_xq);
        pref = PreferenceManager.getDefaultSharedPreferences(KyxqXqActivity.this);
        sUser_id = pref.getString("user_id", "");
        sIs_vip = pref.getString("is_vip", "");
        sNeed_id = getIntent().getStringExtra("id");
        ll_kyxqxq_back = findViewById(R.id.ll_kyxqxq_back);
        ll_kyxqxq_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        civ_kyxqxq_headimgurl = findViewById(R.id.civ_kyxqxq_headimgurl);
        tv_kyxqxq_nickname = findViewById(R.id.tv_kyxqxq_nickname);
        tv_kyxqxq_create_time = findViewById(R.id.tv_kyxqxq_create_time);
        tv_kyxqxq_title = findViewById(R.id.tv_kyxqxq_title);
        tv_kyxqxq_budget = findViewById(R.id.tv_kyxqxq_budget);
        tv_kyxqxq_service_time = findViewById(R.id.tv_kyxqxq_service_time);
        tv_kyxqxq_service_place = findViewById(R.id.tv_kyxqxq_service_place);
        tv_kyxqxq_describe = findViewById(R.id.tv_kyxqxq_describe);
        tv_kyxqxq_jd = findViewById(R.id.tv_kyxqxq_jd);
        sRecoveryNeedInfo();
        tv_kyxqxq_jd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    sGetRecoveryNeed();

            }
        });

    }


    /**
     * 方法名：sGetRecoveryNeed()
     * 功  能：接取需求接口
     * 参  数：appId
     */
    private void sGetRecoveryNeed() {
        hideDialogin();
        dialogin("");
        String url = Api.sUrl + Api.sGetRecoveryNeed;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("need_id", sNeed_id);
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
                                Hint(resultMsg, HintDialog.SUCCESS);
                                try {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                finish();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, 1200);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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


    /**
     * 方法名：sRecoveryNeedInfo()
     * 功  能：需求详情接口
     * 参  数：appId
     */
    private void sRecoveryNeedInfo() {
        hideDialogin();
        dialogin("");
        String url = Api.sUrl + Api.sRecoveryNeedInfo;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("need_id", sNeed_id);
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
                                String resultId = jsonObject1.getString("id");
                                String resultUser_id = jsonObject1.getString("user_id");
                                String resultBudget = jsonObject1.getString("budget");
                                String resultCreate_time = jsonObject1.getString("create_time");
                                String resultTitle = jsonObject1.getString("title");
                                String resultDescribe = jsonObject1.getString("describe");
                                String resultService_place = jsonObject1.getString("service_place");
                                String resultService_time = jsonObject1.getString("service_time");
                                String resultEnd_time = jsonObject1.getString("end_time");
                                String resultOrder_time = jsonObject1.getString("order_time");
                                String resultNickname = jsonObject1.getString("nickname");
                                String resultHeadimgurl = jsonObject1.getString("headimgurl");
                                Glide.with(KyxqXqActivity.this)
                                        .load( Api.sUrl+resultHeadimgurl)
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .dontAnimate()
                                        .into(civ_kyxqxq_headimgurl);
                                tv_kyxqxq_nickname.setText(resultNickname);
                                tv_kyxqxq_create_time.setText(resultCreate_time);
                                tv_kyxqxq_title.setText(resultTitle);
                                tv_kyxqxq_budget.setText(resultBudget);
                                tv_kyxqxq_service_time.setText(resultService_time);
                                tv_kyxqxq_service_place.setText(resultService_place);
                                tv_kyxqxq_describe.setText(resultDescribe);


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
        loadingDialog = new LoadingDialog(KyxqXqActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(KyxqXqActivity.this, R.style.dialog, sHint, type, true).show();
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
