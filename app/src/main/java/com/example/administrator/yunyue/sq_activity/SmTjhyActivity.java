package com.example.administrator.yunyue.sq_activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
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

public class SmTjhyActivity extends AppCompatActivity {
    private static final String TAG = SmTjhyActivity.class.getSimpleName();
    private SharedPreferences pref;
    private String sUser_id;

    RequestQueue queue = null;
    private ImageView iv_sm_hyxq;
    private TextView tv_sm_hyxq_name;
    private String resultId = "";
    private ImageView iv_sm_hyxq_sex;
    private TextView tv_sm_hyxq_mobile;
    private TextView tv_sm_hyxq_tjhy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white);
        }
        setContentView(R.layout.activity_sm_tjhy);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(SmTjhyActivity.this);
        sUser_id = pref.getString("user_id", "");
        Intent intent = getIntent();
        String sId = intent.getStringExtra("id");
        iv_sm_hyxq = findViewById(R.id.iv_sm_hyxq);
        tv_sm_hyxq_name = findViewById(R.id.tv_sm_hyxq_name);
        iv_sm_hyxq_sex = findViewById(R.id.iv_sm_hyxq_sex);
        tv_sm_hyxq_mobile = findViewById(R.id.tv_sm_hyxq_mobile);
        tv_sm_hyxq_tjhy = findViewById(R.id.tv_sm_hyxq_tjhy);
        tv_sm_hyxq_tjhy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDialogin();
                dialogin("");
                save();
            }
        });
        query(sId);
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
                finish();
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

    private void query(String friend_id) {
        String url = Api.sUrl
                + "Community/Api/findFriends/appId/" + Api.sApp_Id + "/user_id/" + sUser_id + "/friend_id/" + friend_id;
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
                        resultId = jsonObjectdate.getString("id");
                        String resultNickname = jsonObjectdate.getString("nickname");
                        String resultHeadimgurl = jsonObjectdate.getString("headimgurl");
                        String resultSex = jsonObjectdate.getString("sex");
                        String resultMobile = jsonObjectdate.getString("mobile");
                        Glide.with(SmTjhyActivity.this)
                                .load( Api.sUrl+ resultHeadimgurl)
                                .asBitmap()
                                .dontAnimate()
                                .placeholder(R.mipmap.error)
                                .error(R.mipmap.error)
                                .into(iv_sm_hyxq);
                        tv_sm_hyxq_name.setText(resultNickname);
                        if (resultSex.equals("1")) {
                            iv_sm_hyxq_sex.setImageResource(R.drawable.icon_man);
                        } else {
                            iv_sm_hyxq_sex.setImageResource(R.drawable.icon_woman);
                        }
                        tv_sm_hyxq_mobile.setText("手机号：" + resultMobile);

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
                Hint(String.valueOf(volleyError), HintDialog.ERROR);
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }


    private void save() {
        String url = Api.sUrl
                + "Community/Api/delfriend" + "/friend_id/" + resultId + "/appId/" + Api.sApp_Id + "/user_id/" + sUser_id;
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
                        TjhyActivity.resultId = "";
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
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Hint(String.valueOf(volleyError), HintDialog.ERROR);
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(SmTjhyActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(SmTjhyActivity.this, R.style.dialog, sHint, type, true).show();
    }

}
