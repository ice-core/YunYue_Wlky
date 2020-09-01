package com.example.administrator.yunyue.yjdt_activity;

import android.content.Intent;
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
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WdtgActivity extends AppCompatActivity {
    private static final String TAG = WdtgActivity.class.getSimpleName();

    private SharedPreferences pref;

    /**
     * 用户id
     */
    private String sUser_id = "";
    /**
     * 返回
     */
    private LinearLayout ll_wdtg_back;
    /**
     * 可提金额
     */
    private TextView tv_wdtg_promote_price;
    /**
     * 累计收益
     */
    private TextView tv_wdtg_promote_income;
    /**
     * 头像地址
     */
    private ImageView iv_wdtg_headimgurl;
    /**
     * 名称
     */
    private TextView tv_wdtg_nickname;
    /**
     * 推广码
     */
    private TextView tv_wdtg_promote_code;
    /**
     * 二维码Url
     */
    private ImageView iv_wdtg_code;
    /**
     * 提现
     */
    private LinearLayout ll_wdtg_tx;
    /**
     * 账单
     */
    private LinearLayout ll_wdtg_zd;
    /**
     * 邀请记录
     */
    private LinearLayout ll_wdtg_yq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_wdtg);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        ll_wdtg_back = findViewById(R.id.ll_wdtg_back);
        ll_wdtg_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_wdtg_promote_price = findViewById(R.id.tv_wdtg_promote_price);
        tv_wdtg_promote_income = findViewById(R.id.tv_wdtg_promote_income);
        iv_wdtg_headimgurl = findViewById(R.id.iv_wdtg_headimgurl);
        tv_wdtg_nickname = findViewById(R.id.tv_wdtg_nickname);
        tv_wdtg_promote_code = findViewById(R.id.tv_wdtg_promote_code);
        iv_wdtg_code = findViewById(R.id.iv_wdtg_code);
        ll_wdtg_tx = findViewById(R.id.ll_wdtg_tx);
        ll_wdtg_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WdtgActivity.this, YjdtTixianActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);
            }
        });
        ll_wdtg_zd = findViewById(R.id.ll_wdtg_zd);
        ll_wdtg_zd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WdtgActivity.this, YjdtZdActivity.class);
                startActivity(intent);
            }
        });
        ll_wdtg_yq = findViewById(R.id.ll_wdtg_yq);
        ll_wdtg_yq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WdtgActivity.this, YjdtYqjlActivity.class);
                startActivity(intent);
            }
        });
        sPromoteList();
    }

    /**
     * 方法名：sPromoteList()
     * 功  能：我的推广接口
     * 参  数：appId
     */
    private void sPromoteList() {
        String url = Api.sUrl + Api.sPromoteList;
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
                        try {
                            JSONObject jsonObject = new JSONObject(sDate);
                            String resultMsg = jsonObject.getString("msg");
                            int resultCode = jsonObject.getInt("code");
                            if (resultCode > 0) {

                                JSONObject jsonObjectdata = jsonObject.getJSONObject("data");
                                //推广可提现金额
                                String sPromote_price = jsonObjectdata.getString("promote_price");
                                //累计收益
                                String sPromote_income = jsonObjectdata.getString("promote_income");
                                //头像
                                String sHeadimgurl = jsonObjectdata.getString("headimgurl");
                                //名称
                                String sNickname = jsonObjectdata.getString("nickname");
                                //推广码
                                String sPromote_code = jsonObjectdata.getString("promote_code");
                                //二维码url
                                String sCode = jsonObjectdata.getString("code");
                                tv_wdtg_promote_price.setText(sPromote_price);
                                tv_wdtg_promote_income.setText(sPromote_income);
                                tv_wdtg_nickname.setText(sNickname);
                                tv_wdtg_promote_code.setText("推广码：" + sPromote_code);

                                Glide.with(WdtgActivity.this)
                                        .load( Api.sUrl+ sHeadimgurl)
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .dontAnimate()
                                        .into(iv_wdtg_headimgurl);
                                Glide.with(WdtgActivity.this)
                                        .load( Api.sUrl+ sCode)
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .dontAnimate()
                                        .into(iv_wdtg_code);
                            } else {
                                hideDialogin();
                                Hint(resultMsg, HintDialog.ERROR);
                                //   Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
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

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(WdtgActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(WdtgActivity.this, R.style.dialog, sHint, type, true).show();
    }

}
