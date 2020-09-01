package com.example.administrator.yunyue.sq_activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

public class SqjjGzActivity extends AppCompatActivity {
    /**
     * 返回
     */
    private LinearLayout ll_sqjj_gz_back;
    /**
     * 关注
     */
    private TextView tv_sqjj_gz;
    RequestQueue queue = null;
    private SharedPreferences pref;
    private String sUser_id;
    private ImageView iv_sqjj_gz_logo;
    private TextView tv_sqjj_gz_name;
    /**
     * 口号
     */
    private TextView tv_sqjj_gz_slogan;
    /**
     * 简介
     */
    private TextView tv_sqjj_gz_desc;
    private TextView tv_sqjj_gz_hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_sqjj_gz);
        queue = Volley.newRequestQueue(SqjjGzActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(SqjjGzActivity.this);
        sUser_id = pref.getString("user_id", "");
        Intent intent = getIntent();
        final String sId = intent.getStringExtra("id");
        ll_sqjj_gz_back = findViewById(R.id.ll_sqjj_gz_back);
        iv_sqjj_gz_logo = findViewById(R.id.iv_sqjj_gz_logo);
        tv_sqjj_gz_name = findViewById(R.id.tv_sqjj_gz_name);
        tv_sqjj_gz_slogan = findViewById(R.id.tv_sqjj_gz_slogan);
        tv_sqjj_gz_desc = findViewById(R.id.tv_sqjj_gz_desc);
        tv_sqjj_gz_hint = findViewById(R.id.tv_sqjj_gz_hint);
        ll_sqjj_gz_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_sqjj_gz = findViewById(R.id.tv_sqjj_gz);
        tv_sqjj_gz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDialogin();
                dialogin("");
                jiaruShequn(sId);
            }
        });
        sqxx(sId);
    }

    /**
     * 社群信息
     */
    private void sqxx(String community_id) {
        String url = Api.sUrl + "Community/Api/shequn/appId/" + Api.sApp_Id
                + "/community_id/" + community_id;
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
                        JSONObject jsonObjectdate = jsonObject1.getJSONObject("data");
                        //社群ID
                        String community_id = jsonObjectdate.getString("community_id");
                        //用户ID
                        String user_id = jsonObjectdate.getString("user_id");
                        //社群名称
                        String name = jsonObjectdate.getString("name");
                        //社群LOGO
                        String logo = jsonObjectdate.getString("logo");
                        //口号
                        String slogan = jsonObjectdate.getString("slogan");
                        //简介
                        String desc = jsonObjectdate.getString("desc");
                        Glide.with(SqjjGzActivity.this)
                                .load( Api.sUrl+ logo)
                                .asBitmap()
                                .placeholder(R.mipmap.error)
                                .error(R.mipmap.error)
                                .dontAnimate()
                                .into(iv_sqjj_gz_logo);
                        tv_sqjj_gz_name.setText(name);
                        tv_sqjj_gz_slogan.setText(slogan);
                        tv_sqjj_gz_desc.setText(desc);
                        tv_sqjj_gz_hint.setText(name);
                        // Hint(resultMsg, HintDialog.SUCCESS);
                    } else {
                        Hint(resultMsg, HintDialog.ERROR);
                    }
                } catch (JSONException e) {
                    hideDialogin();
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


    /**
     * 关注社群
     */
    private void jiaruShequn(String community_id) {
        String url = Api.sUrl + "Community/Api/jiaruShequn/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/community_id/" + community_id;
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
                        Hint(resultMsg, HintDialog.SUCCESS);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1200);
                        // Hint(resultMsg, HintDialog.SUCCESS);
                    } else {
                        Hint(resultMsg, HintDialog.ERROR);
                    }
                } catch (JSONException e) {
                    hideDialogin();
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


    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(SqjjGzActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(SqjjGzActivity.this, R.style.dialog, sHint, type, true).show();
    }

}
