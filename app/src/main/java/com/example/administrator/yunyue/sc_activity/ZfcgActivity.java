package com.example.administrator.yunyue.sc_activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
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
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.MainActivity_Wlky;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;


public class ZfcgActivity extends AppCompatActivity {
    private static final String TAG = ZfcgActivity.class.getSimpleName();
    private LinearLayout ll_zfcg_jxgw;
    private LinearLayout ll_zfcg_ckdd;
    private ImageView iv_zfcg_back;
    private TextView tv_zfcg;
    private SharedPreferences pref;
    private String user_id, mobile;
    private SharedPreferences.Editor editor;
    RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_zfcg);
        queue = Volley.newRequestQueue(this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = pref.getString("user_id", "");
        mobile = pref.getString("mobile", "");
        tv_zfcg = (TextView) findViewById(R.id.tv_zfcg);
        ll_zfcg_jxgw = (LinearLayout) findViewById(R.id.ll_zfcg_jxgw);
        iv_zfcg_back = (ImageView) findViewById(R.id.iv_zfcg_back);
        ll_zfcg_ckdd = (LinearLayout) findViewById(R.id.ll_zfcg_ckdd);
        ll_zfcg_jxgw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ScMainActivity.main(1, "0");
                back();
            }
        });
        ll_zfcg_ckdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ZfcgActivity.this, CkddActivity.class);
                startActivity(intent);
                finish();
            }
        });
        iv_zfcg_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        xiangqing();
    }


    private void xiangqing() {
        String url = Api.sUrl + "Api/Login/udetails/appId/" + Api.sApp_Id + "/user_id/" + user_id + "/mobile/" + mobile;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                //   hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        JSONObject jsonObjectdate = jsonObject1.getJSONObject("data");
                        String id = jsonObjectdate.getString("id");
                        String nickname = jsonObjectdate.getString("nickname");
                        String mobile = jsonObjectdate.getString("mobile");
                        String headimgurl = jsonObjectdate.getString("headimgurl");
                        String sIs_vip = jsonObjectdate.getString("is_vip");

                        editor = pref.edit();
                        editor.putString("is_vip", sIs_vip);
                        editor.putString("head_pic", headimgurl);
                        editor.putString("nickname", nickname);
                        editor.putString("mobile", mobile);
                        editor.putString("sex", mobile);

                        editor.apply();
                    } else {

                    }
                } catch (JSONException e) {
                    //  hideDialogin();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //   hideDialogin();
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }

    private void back() {
        Intent intent = new Intent(ZfcgActivity.this, MainActivity_Wlky.class);
        intent.putExtra("ID", "0");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

}
