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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
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
import com.example.administrator.yunyue.erci.activity.ChongzhiActivity;
import com.example.administrator.yunyue.erci.activity.GftxActivity;
import com.example.administrator.yunyue.erci.activity.SfyzActivity;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WdqbActivity extends AppCompatActivity {

    private static final String TAG = WdqbActivity.class.getSimpleName();
    private Button bt_wdqb_tx;
    private SharedPreferences pref;
    RequestQueue queue = null;
    private String sUser_id, sMobile;

    private TextView tv_wdqb_money;
    private LinearLayout ll_wdqb_back;
    private SharedPreferences.Editor editor;

    private LinearLayout ll_wdqb_yhkgl;
    private LinearLayout ll_wdqb_zfmm;
    private LinearLayout ll_wdqb_jymx;

    /**
     * 工分提現
     */
    private LinearLayout ll_wdqb_tx;
    /**
     * 充值
     */
    private LinearLayout ll_wdqb_cz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_wdqb);
        tv_wdqb_money = (TextView) findViewById(R.id.tv_wdqb_money);
        ll_wdqb_back = findViewById(R.id.ll_wdqb_back);

        queue = Volley.newRequestQueue(WdqbActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        sMobile = pref.getString("mobile", "");
        bt_wdqb_tx = (Button) findViewById(R.id.bt_wdqb_tx);
        ll_wdqb_tx = findViewById(R.id.ll_wdqb_tx);
        ll_wdqb_yhkgl = (LinearLayout) findViewById(R.id.ll_wdqb_yhkgl);
        ll_wdqb_zfmm = (LinearLayout) findViewById(R.id.ll_wdqb_zfmm);
        ll_wdqb_jymx = (LinearLayout) findViewById(R.id.ll_wdqb_jymx);
        ll_wdqb_cz = findViewById(R.id.ll_wdqb_cz);
        ll_wdqb_cz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WdqbActivity.this, ChongzhiActivity.class);
                startActivity(intent);
            }
        });
        ll_wdqb_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WdqbActivity.this, GftxActivity.class);
                startActivity(intent);
            }
        });
        ll_wdqb_yhkgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WdqbActivity.this, YhkglActivity.class);
                startActivity(intent);
            }
        });
        ll_wdqb_zfmm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WdqbActivity.this, SfyzActivity.class);
                startActivity(intent);
            }
        });
        ll_wdqb_jymx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WdqbActivity.this, JymxActivity.class);
                startActivity(intent);
            }
        });
        bt_wdqb_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WdqbActivity.this, YetxActivity.class);
                intent.putExtra("headline", "余额提现");
                intent.putExtra("amount", tv_wdqb_money.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        ll_wdqb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
    }

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initview();
    }

    private void initview() {

        // dialogin("");
        sWorkpoints();
    }

    private void sWorkpoints() {
        String url = Api.sUrl + Api.sWorkpoints;
        RequestQueue requestQueue = Volley.newRequestQueue(WdqbActivity.this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("mobile", sMobile);

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
                            JSONObject jsonObject1 = new JSONObject(sDate);
                            String resultMsg = jsonObject1.getString("msg");
                            int resultCode = jsonObject1.getInt("code");
                            if (resultCode > 0) {
                                JSONObject jsonObjectdate = jsonObject1.getJSONObject("data");
                                String id = jsonObjectdate.getString("id");
                                String nickname = jsonObjectdate.getString("nickname");
                                String mobile = jsonObjectdate.getString("mobile");
                                String headimgurl = jsonObjectdate.getString("headimgurl");
                                String sex = jsonObjectdate.getString("sex");
                                //余额
                                String user_money = jsonObjectdate.getString("user_money");
                                //积分
                                String pay_points = jsonObjectdate.getString("pay_points");
                                tv_wdqb_money.setText(user_money);
                                editor = pref.edit();
                                editor.putString("head_pic", headimgurl);
                                editor.putString("nickname", nickname);
                                editor.putString("mobile", mobile);
                                editor.putString("sex", sex);
                                editor.putString("user_money", user_money);
                                editor.putString("pay_points", pay_points);
                                editor.apply();
                            } else {

                            }
                        } catch (JSONException e) {
                            hideDialogin();
                            e.printStackTrace();
                        }
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

/*    private void xiangqing() {
        String url = Api.sUrl + "Api/Login/udetails/appId/" + Api.sApp_Id + "/user_id/" + sUser_id + "/mobile/" + sMobile;
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
                        String id = jsonObjectdate.getString("id");
                        String nickname = jsonObjectdate.getString("nickname");
                        String mobile = jsonObjectdate.getString("mobile");
                        String headimgurl = jsonObjectdate.getString("headimgurl");
                        String sex = jsonObjectdate.getString("sex");
                        //余额
                        String user_money = jsonObjectdate.getString("user_money");
                        //积分
                        String pay_points = jsonObjectdate.getString("pay_points");
                        tv_wdqb_money.setText(user_money);
                        editor = pref.edit();
                        editor.putString("head_pic", headimgurl);
                        editor.putString("nickname", nickname);
                        editor.putString("mobile", mobile);
                        editor.putString("sex", sex);
                        editor.putString("user_money", user_money);
                        editor.putString("pay_points", pay_points);
                        editor.apply();
                    } else {

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
        request.setShouldCache(false);
        queue.add(request);
    }*/

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(WdqbActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        try {
            new HintDialog(WdqbActivity.this, R.style.dialog, sHint, type, true).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
/*        Intent intent = new Intent(this, ScMainActivity.class);
        intent.putExtra("ID", "4");
        startActivity(intent);*/
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
