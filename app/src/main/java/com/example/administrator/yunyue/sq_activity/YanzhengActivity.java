package com.example.administrator.yunyue.sq_activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

public class YanzhengActivity extends AppCompatActivity {
    /**
     * 返回
     */
    private LinearLayout ll_yanzheng_back;
    RequestQueue queue = null;

    private SharedPreferences pref;
    private String sUser_id;
    private String sName = "";
    private String sId = "";
    private TextView tv_yanzheng_fs;
    private EditText et_yanzheng_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_yanzheng);
        queue = Volley.newRequestQueue(YanzhengActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        Intent intent = getIntent();
        sName = intent.getStringExtra("name");
        sId = intent.getStringExtra("id");

        ll_yanzheng_back = findViewById(R.id.ll_yanzheng_back);
        ll_yanzheng_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        et_yanzheng_info = findViewById(R.id.et_yanzheng_info);
        tv_yanzheng_fs = findViewById(R.id.tv_yanzheng_fs);
        tv_yanzheng_fs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDialogin();
                dialogin("");
                jrql(sName, sId);
            }
        });
    }

    private void jrql(final String name, final String id) {
        String url = Api.sUrl
                + "Community/Api/joins/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/id/" + id;
        if (!et_yanzheng_info.getText().toString().equals("")) {
            url = url + "/info/" + et_yanzheng_info.getText().toString();
        }
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
        loadingDialog = new LoadingDialog(YanzhengActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(YanzhengActivity.this, R.style.dialog, sHint, type, true).show();
    }
}
