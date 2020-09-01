package com.example.administrator.yunyue.sq_activity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
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

public class CjqlActivity extends AppCompatActivity {
    private LinearLayout ll_cjql_back;
    /**
     * 群聊名称
     */
    private EditText et_cjql_name;
    /**
     * 任何人均可加入
     */
    private CheckBox cb_cjql_rhr;
    /**
     * 会员可加入
     */
    private CheckBox cb_cjql_hy;
    /**
     * 任何人均不可加入
     */
    private CheckBox cb_cjql_bkjr;
    /**
     * 管理员审核
     */
    private Switch sc_cjql_glysh;

    private static final String TAG = CjqlActivity.class.getSimpleName();
    RequestQueue queue = null;
    private SharedPreferences pref;
    private String sUser_id;
    private TextView tv_cjql_qr;
    private int iAuth = 1;
    private int iTiaojian = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white);
        }
        setContentView(R.layout.activity_cjql);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        queue = Volley.newRequestQueue(CjqlActivity.this);
        ll_cjql_back = findViewById(R.id.ll_cjql_back);
        ll_cjql_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        et_cjql_name = findViewById(R.id.et_cjql_name);
        cb_cjql_rhr = findViewById(R.id.cb_cjql_rhr);
        cb_cjql_hy = findViewById(R.id.cb_cjql_hy);
        cb_cjql_bkjr = findViewById(R.id.cb_cjql_bkjr);
        tv_cjql_qr = findViewById(R.id.tv_cjql_qr);
        sc_cjql_glysh = findViewById(R.id.sc_cjql_glysh);
        sc_cjql_glysh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sc_cjql_glysh.isChecked()) {
                    iAuth = 1;
                } else {
                    iAuth = 0;
                }
            }
        });
        tv_cjql_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sName = et_cjql_name.getText().toString();
                if (sName.equals("")) {
                    Hint("群聊名称不能为空！", HintDialog.WARM);
                }
                hideDialogin();
                dialogin("");
                Save(sName, QlzxActivity.community_id, String.valueOf(iAuth), String.valueOf(iTiaojian));
            }
        });
        cb_cjql_rhr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iTiaojian = 1;
                cb_cjql_rhr.setChecked(true);
                cb_cjql_hy.setChecked(false);
                cb_cjql_bkjr.setChecked(false);
            }
        });
        cb_cjql_hy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iTiaojian = 2;
                cb_cjql_rhr.setChecked(false);
                cb_cjql_hy.setChecked(true);
                cb_cjql_bkjr.setChecked(false);
            }
        });

        cb_cjql_bkjr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iTiaojian = 3;
                cb_cjql_rhr.setChecked(false);
                cb_cjql_hy.setChecked(false);
                cb_cjql_bkjr.setChecked(true);
            }
        });
    }

    private void Save(String name, String community_id, String auth, String tiaojian) {
        String url = Api.sUrl + "Community/Api/group/appId/" + Api.sApp_Id
                + "/name/" + name + "/auth/" + auth + "/members/" + "[" + sUser_id + "]" + "/tiaojian/" + tiaojian;
        if (!community_id.equals("")) {
            url = url + "/community_id/" + community_id;
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

                        //  huoqu(sUser_id);
                        Hint(resultMsg, HintDialog.SUCCESS);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1500);
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
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(CjqlActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(CjqlActivity.this, R.style.dialog, sHint, type, true).show();
    }

}
