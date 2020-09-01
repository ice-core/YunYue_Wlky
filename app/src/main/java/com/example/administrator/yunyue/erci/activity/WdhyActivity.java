package com.example.administrator.yunyue.erci.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
import com.example.administrator.yunyue.MainActivity_Wlky;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.erci.FenxiangActivity;
import com.example.administrator.yunyue.sc.CircleImageView;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WdhyActivity extends AppCompatActivity {
    private static final String TAG = WdhyActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_wdhy_back;

    /**
     * 邀请记录
     */
    private TextView tv_wdhy_yqjl;
    /**
     * 头像
     */
    private CircleImageView civ_wdhy_img;
    /**
     * 昵称
     */
    private TextView tv_wdhy_name;
    /**
     * 邀请
     */
    private TextView tv_wdhy_yq1, tv_wdhy_yq2;
    private SharedPreferences pref;
    private String sNickname = "", sHeadimgurl = "";
    private String sUser_id = "";
    private String sActivity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_wdhy);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sNickname = pref.getString("nickname", "");
        sHeadimgurl = pref.getString("head_pic", "");
        sUser_id = pref.getString("user_id", "");
        sActivity = getIntent().getStringExtra("activity");
        ll_wdhy_back = findViewById(R.id.ll_wdhy_back);
        ll_wdhy_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        tv_wdhy_yqjl = findViewById(R.id.tv_wdhy_yqjl);
        tv_wdhy_yqjl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WdhyActivity.this, FenxiangActivity.class);
                startActivity(intent);
            }
        });
        civ_wdhy_img = findViewById(R.id.civ_wdhy_img);
        tv_wdhy_name = findViewById(R.id.tv_wdhy_name);
        tv_wdhy_yq1 = findViewById(R.id.tv_wdhy_yq1);
        tv_wdhy_yq2 = findViewById(R.id.tv_wdhy_yq2);
        tv_wdhy_name.setText(sNickname);
        Glide.with(WdhyActivity.this)
                .load( Api.sUrl+sHeadimgurl)
                .asBitmap()
                .placeholder(R.mipmap.error)
                .error(R.mipmap.error)
                .dontAnimate()
                .into(civ_wdhy_img);
        tv_wdhy_yq1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_wdhy_yq1.getText().toString().equals("去邀请")) {
                    Intent intent = new Intent(WdhyActivity.this, FenxiangActivity.class);
                    startActivity(intent);
                }
            }
        });
        tv_wdhy_yq2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_wdhy_yq2.getText().toString().equals("去邀请")) {
                    Intent intent = new Intent(WdhyActivity.this, FenxiangActivity.class);
                    startActivity(intent);
                }
            }
        });
        sUservip();
    }

    private void back() {
        if (sActivity.equals("wd")) {
            Intent intent = new Intent(WdhyActivity.this, MainActivity_Wlky.class);
            intent.putExtra("ID", "4");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        finish();
    }

    /**
     *  * 方法名：onKeyDown(int keyCode, KeyEvent event)
     *  * 功    能：菜单栏点击事件
     *  * 参    数：int keyCode, KeyEvent event
     *  * 返回值：无
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Log.d(TAG, "onKeyDown KEYCODE_BACK");
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

    private void sUservip() {
        String url = Api.sUrl + Api.sUservip;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
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
                                if (resultData.equals("0")) {
                                    tv_wdhy_yq1.setBackgroundResource(R.drawable.bj_theme_4);
                                    tv_wdhy_yq1.setText("去邀请");
                                    tv_wdhy_yq2.setBackgroundResource(R.drawable.bj_theme_4);
                                    tv_wdhy_yq2.setText("去邀请");
                                } else if (resultData.equals("1")) {
                                    tv_wdhy_yq1.setBackgroundResource(R.drawable.bj_4_cccccc);
                                    tv_wdhy_yq1.setText("已完成");
                                    tv_wdhy_yq2.setBackgroundResource(R.drawable.bj_theme_4);
                                    tv_wdhy_yq2.setText("去邀请");
                                } else if (resultData.equals("2")) {
                                    tv_wdhy_yq1.setBackgroundResource(R.drawable.bj_4_cccccc);
                                    tv_wdhy_yq1.setText("已完成");
                                    tv_wdhy_yq2.setBackgroundResource(R.drawable.bj_4_cccccc);
                                    tv_wdhy_yq2.setText("已完成");
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

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(WdhyActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(WdhyActivity.this, R.style.dialog, sHint, type, true).show();
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
