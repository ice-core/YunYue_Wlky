package com.example.administrator.yunyue.yjdt_activity;

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

public class YjdtActivity extends AppCompatActivity {
    private static final String TAG = YjdtActivity.class.getSimpleName();

    /**
     * 返回
     */
    private LinearLayout ll_yjdt_back;

    /**
     * 佣金池
     */
    private TextView tv_yjdt_total_price;

    /**
     * 佣金盘
     */
    private TextView tv_yjdt_avg_price;

    /**
     * 我的佣金
     */
    private TextView tv_yjdt_price;

    private SharedPreferences pref;

    /**
     * 用户id
     */
    private String sUser_id = "";

    /**
     * 佣金提现
     */
    private LinearLayout ll_yjdt_ytx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_yjdt);
        pref = PreferenceManager.getDefaultSharedPreferences(YjdtActivity.this);
        sUser_id = pref.getString("user_id", "");
        ll_yjdt_back = findViewById(R.id.ll_yjdt_back);
        ll_yjdt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_yjdt_total_price = findViewById(R.id.tv_yjdt_total_price);
        tv_yjdt_avg_price = findViewById(R.id.tv_yjdt_avg_price);
        tv_yjdt_price = findViewById(R.id.tv_yjdt_price);
        sCommissionIndex();
        ll_yjdt_ytx = findViewById(R.id.ll_yjdt_ytx);
        ll_yjdt_ytx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YjdtActivity.this, YjdtTixianActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
            }
        });
    }

    /**
     * 方法名：sCommissionIndex()
     * 功  能：佣金大厅接口
     * 参  数：appId
     */
    private void sCommissionIndex() {
        String url = Api.sUrl + Api.sCommissionIndex;
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
                                //佣金池
                                String sTotal_Price = jsonObjectdata.getString("total_price");
                                //佣金盘
                                String sAvg_Price = jsonObjectdata.getString("avg_price");
                                //我的佣金
                                String sPrice = jsonObjectdata.getString("price");
                                tv_yjdt_total_price.setText(sTotal_Price);
                                tv_yjdt_avg_price.setText(sAvg_Price);
                                tv_yjdt_price.setText(sPrice);
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
        loadingDialog = new LoadingDialog(YjdtActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(YjdtActivity.this, R.style.dialog, sHint, type, true).show();
    }

}
