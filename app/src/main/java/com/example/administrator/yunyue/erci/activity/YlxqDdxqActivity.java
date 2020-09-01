package com.example.administrator.yunyue.erci.activity;

import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc_activity.Sc_WdddActivity;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.rong.imageloader.utils.L;
import master.flame.danmaku.danmaku.model.FBDanmaku;

public class YlxqDdxqActivity extends AppCompatActivity {
    private static final String TAG = YlxqDdxqActivity.class.getSimpleName();
    private String sRest_id = "";
    /**
     * 返回
     */
    private LinearLayout ll_ylxq_ddxq_back;

    /**
     * 订单状态
     */
    private ImageView iv_ylxq_ddxq_status, iv_ylxq_ddxq_status1, iv_ylxq_ddxq_status2, iv_ylxq_ddxq_status3;
    private TextView tv_ylxq_ddxq_status;
    private TextView tv_ylxq_ddxq_status1;
    private LinearLayout ll_ylxq_ddxq_status1;
    private TextView tv_ylxq_ddxq_status_1;
    /**
     * 发布时间
     */
    private TextView tv_ylxq_ddxq_create_time;
    /**
     * 需求标题
     */
    private TextView tv_ylxq_ddxq_title;
    /**
     * 服务地点
     */
    private TextView tv_ylxq_ddxq_service_place;
    /**
     * 服务时间
     */
    private TextView tv_ylxq_ddxq_service_time;
    /**
     * 点金
     */
    private TextView tv_ylxq_ddxq_budget;
    private TextView tv_ylxq_ddxq_budget1;
    /**
     * 服务描述
     */
    private TextView tv_ylxq_ddxq_describe;
    /**
     * 订单编号
     */
    private TextView tv_ylxq_ddxq_need_num;

    /**
     * 接单时间
     */
    private TextView tv_ylxq_ddxq_order_time;
    /**
     * 付款时间
     */
    private TextView tv_ylxq_ddxq_end_time;
    private String sStatus = "";
    /**
     * 开始服务
     */
    private TextView tv_ylxq_ddxq_ksfw;
    /**
     * 确认完成
     */
    private TextView tv_ylxq_ddxq_qxdd;

    private String sUser_id = "";
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white);
        }
        setContentView(R.layout.activity_ylxq_xq);
        sRest_id = getIntent().getStringExtra("id");
        sStatus = getIntent().getStringExtra("status");
        pref = PreferenceManager.getDefaultSharedPreferences(YlxqDdxqActivity.this);
        sUser_id = pref.getString("user_id", "");
        ll_ylxq_ddxq_back = findViewById(R.id.ll_ylxq_ddxq_back);
        ll_ylxq_ddxq_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_ylxq_ddxq_status = findViewById(R.id.iv_ylxq_ddxq_status);
        iv_ylxq_ddxq_status1 = findViewById(R.id.iv_ylxq_ddxq_status1);
        iv_ylxq_ddxq_status2 = findViewById(R.id.iv_ylxq_ddxq_status2);
        iv_ylxq_ddxq_status3 = findViewById(R.id.iv_ylxq_ddxq_status3);
        tv_ylxq_ddxq_status = findViewById(R.id.tv_ylxq_ddxq_status);
        tv_ylxq_ddxq_create_time = findViewById(R.id.tv_ylxq_ddxq_create_time);
        tv_ylxq_ddxq_title = findViewById(R.id.tv_ylxq_ddxq_title);
        tv_ylxq_ddxq_service_place = findViewById(R.id.tv_ylxq_ddxq_service_place);
        tv_ylxq_ddxq_service_time = findViewById(R.id.tv_ylxq_ddxq_service_time);
        tv_ylxq_ddxq_budget = findViewById(R.id.tv_ylxq_ddxq_budget);
        tv_ylxq_ddxq_budget1 = findViewById(R.id.tv_ylxq_ddxq_budget1);
        tv_ylxq_ddxq_describe = findViewById(R.id.tv_ylxq_ddxq_describe);
        tv_ylxq_ddxq_need_num = findViewById(R.id.tv_ylxq_ddxq_need_num);
        tv_ylxq_ddxq_order_time = findViewById(R.id.tv_ylxq_ddxq_order_time);
        tv_ylxq_ddxq_end_time = findViewById(R.id.tv_ylxq_ddxq_end_time);
        tv_ylxq_ddxq_status1 = findViewById(R.id.tv_ylxq_ddxq_status1);
        ll_ylxq_ddxq_status1 = findViewById(R.id.ll_ylxq_ddxq_status1);
        tv_ylxq_ddxq_status_1 = findViewById(R.id.tv_ylxq_ddxq_status_1);
        tv_ylxq_ddxq_ksfw = findViewById(R.id.tv_ylxq_ddxq_ksfw);
        tv_ylxq_ddxq_qxdd = findViewById(R.id.tv_ylxq_ddxq_qxdd);
        if (sStatus.equals("0")) {
            ll_ylxq_ddxq_status1.setVisibility(View.GONE);
            tv_ylxq_ddxq_status_1.setVisibility(View.GONE);
            tv_ylxq_ddxq_status1.setText("待服务");
        } else if (sStatus.equals("1")) {
            ll_ylxq_ddxq_status1.setVisibility(View.VISIBLE);
            tv_ylxq_ddxq_status_1.setVisibility(View.VISIBLE);
            tv_ylxq_ddxq_status1.setText("待接单");
        }
        sRecoveryNeedInfo();
        tv_ylxq_ddxq_ksfw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sStartService(sRest_id);
            }
        });
        tv_ylxq_ddxq_qxdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sConfirmCompleted(sRest_id);
            }
        });
    }

    /**
     * 方法名：sConfirmCompleted()
     * 功  能：康养需求 确认完成
     * 参  数：appId
     */
    private void sConfirmCompleted(String sOrder_Id) {
        hideDialogin();
        dialogin("");
        String url = Api.sUrl + Api.sConfirmCompleted;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("order_id", sOrder_Id);
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
                                String resultData = jsonObject1.getString("data");
                                Hint(resultMsg, HintDialog.SUCCESS);
                                sRecoveryNeedInfo();
                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "response -> " + response.toString());
                        // Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialogin();
                //Hint(error.getMessage(), HintDialog.ERROR);
                error(error);

            }
        });
        requestQueue.add(jsonRequest);
    }

    /**
     * 方法名：sStartService()
     * 功  能：康养需求 开始服务
     * 参  数：appId
     */
    private void sStartService(String sOrder_Id) {
        hideDialogin();
        dialogin("");
        String url = Api.sUrl + Api.sStartService;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("order_id", sOrder_Id);
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
                                String resultData = jsonObject1.getString("data");
                                Hint(resultMsg, HintDialog.SUCCESS);
                                sRecoveryNeedInfo();
                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "response -> " + response.toString());
                        // Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialogin();
                //Hint(error.getMessage(), HintDialog.ERROR);
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
        params.put("need_id", sRest_id);
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
                                String resultStatus = jsonObject1.getString("status");
                                String resultEnd_time = jsonObject1.getString("end_time");
                                String resultOrder_time = jsonObject1.getString("order_time");
                                String resultNeed_num = jsonObject1.getString("need_num");
                                String resultNickname = jsonObject1.getString("nickname");
                                String resultHeadimgurl = jsonObject1.getString("headimgurl");
                                tv_ylxq_ddxq_qxdd.setVisibility(View.GONE);
                                tv_ylxq_ddxq_ksfw.setVisibility(View.GONE);
                                if (resultStatus.equals("1")) {
                                    iv_ylxq_ddxq_status.setImageResource(R.mipmap.icon_daijiedan_choice);
                                    tv_ylxq_ddxq_status.setText("待接单");
                                } else if (resultStatus.equals("2")) {
                                    if (sStatus.equals(0)) {
                                        tv_ylxq_ddxq_ksfw.setVisibility(View.VISIBLE);
                                    }
                                    tv_ylxq_ddxq_status.setText("待服务");
                                    iv_ylxq_ddxq_status.setImageResource(R.mipmap.icon_daijiedan_choice);
                                    iv_ylxq_ddxq_status1.setImageResource(R.mipmap.icon_yijiedan_choice);
                                } else if (resultStatus.equals("3")) {
                                    if (sStatus.equals(1)) {
                                        tv_ylxq_ddxq_qxdd.setVisibility(View.VISIBLE);
                                    }
                                    tv_ylxq_ddxq_status.setText("进行中");
                                    iv_ylxq_ddxq_status.setImageResource(R.mipmap.icon_daijiedan_choice);
                                    iv_ylxq_ddxq_status1.setImageResource(R.mipmap.icon_yijiedan_choice);
                                    iv_ylxq_ddxq_status2.setImageResource(R.mipmap.icon_incurrent_choice);
                                } else if (resultStatus.equals("4")) {
                                    tv_ylxq_ddxq_status.setText("完成");
                                    iv_ylxq_ddxq_status.setImageResource(R.mipmap.icon_daijiedan_choice);
                                    iv_ylxq_ddxq_status1.setImageResource(R.mipmap.icon_yijiedan_choice);
                                    iv_ylxq_ddxq_status2.setImageResource(R.mipmap.icon_incurrent_choice);
                                    iv_ylxq_ddxq_status3.setImageResource(R.mipmap.icon_dui_choice);
                                } else if (resultStatus.equals("4")) {
                                    tv_ylxq_ddxq_status.setText("已取消");
                                }
                                tv_ylxq_ddxq_order_time.setText(resultOrder_time);
                                tv_ylxq_ddxq_title.setText(resultTitle);
                                tv_ylxq_ddxq_service_place.setText("服务地点：" + resultService_place);
                                tv_ylxq_ddxq_service_time.setText("服务时间：" + resultService_time);
                                tv_ylxq_ddxq_budget.setText(resultBudget + "工分");
                                tv_ylxq_ddxq_budget1.setText(resultBudget + "工分");
                                tv_ylxq_ddxq_describe.setText(resultDescribe);
                                tv_ylxq_ddxq_need_num.setText(resultNeed_num);
                                tv_ylxq_ddxq_create_time.setText(resultCreate_time);
                                tv_ylxq_ddxq_end_time.setText(resultEnd_time);

                              /*  Glide.with(KyxqXqActivity.this)
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
                                tv_kyxqxq_describe.setText(resultDescribe);*/


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
        loadingDialog = new LoadingDialog(YlxqDdxqActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(YlxqDdxqActivity.this, R.style.dialog, sHint, type, true).show();
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
