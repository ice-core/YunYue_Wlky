package com.example.administrator.yunyue.sc_activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

public class WdjfActivity extends AppCompatActivity {
    private static final String TAG = WdjfActivity.class.getSimpleName();
    RequestQueue queue = null;
    private SharedPreferences pref;
    private String sUser_id;
    private TextView tv_wdjf_jf;
    private TextView tv_wdjf_ktxje;
    private String sIntegral = "";
    private Button bt_wdjf_tx;
    double Money;
    private ImageView iv_wdjf_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_wdjf);
        queue = Volley.newRequestQueue(WdjfActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(WdjfActivity.this);
        sUser_id = pref.getString("user_id", "");
        tv_wdjf_jf = (TextView) findViewById(R.id.tv_wdjf_jf);
        tv_wdjf_ktxje = (TextView) findViewById(R.id.tv_wdjf_ktxje);
        bt_wdjf_tx = (Button) findViewById(R.id.bt_wdjf_tx);
        iv_wdjf_back = findViewById(R.id.iv_wdjf_back);
        iv_wdjf_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bt_wdjf_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Money > 100) {
                    Intent intent = new Intent(WdjfActivity.this, YetxActivity.class);
                    intent.putExtra("headline", "积分提现");
                    intent.putExtra("amount", tv_wdjf_ktxje.getText().toString());
                    startActivity(intent);
                    finish();
                } else {
                    Hint("可提现金额小于100，无法提现", HintDialog.WARM);
                }
            }
        });
        dialogin("");
        xiangqing();

    }


    private void xiangqing() {
        String url = Api.sUrl + "Login/userFind/";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
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
                            String resultData = jsonObject1.getString("data");
                            if (resultCode > 0) {
                                jsonObject1 = new JSONObject(resultData);
                                sIntegral = jsonObject1.getString("pay_points");
                                tv_wdjf_jf.setText(jsonObject1.getString("pay_points"));
                                dialogin("");
                                huilv();
                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "response -> " + response.toString());
                        //   Toast.makeText(HzrzActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialogin();
                Hint(error.toString(), HintDialog.ERROR);
                Log.e(TAG, error.getMessage(), error);
            }
        });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonRequest);
    }


    private void huilv() {
        String url = Api.sUrl + "Account/lv";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        //  params.put("user_id", sUser_id);
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
                                //  tv_wdjf_jf.getText() *resultCode;
                                double integral = Double.parseDouble(sIntegral);
                                double number = Double.parseDouble(resultData);
                                Money = integral * number;
                                tv_wdjf_ktxje.setText(String.valueOf(Money));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "response -> " + response.toString());
                        //   Toast.makeText(HzrzActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialogin();
                Hint(error.toString(), HintDialog.ERROR);
                Log.e(TAG, error.getMessage(), error);
            }
        });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonRequest);
    }


    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(this, R.style.dialog, sHint, type, true).show();
    }
}
