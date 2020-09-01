package com.example.administrator.yunyue.sq_activity;

import android.os.Build;
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

public class GywmActivity extends AppCompatActivity {
    /**
     * 返回
     */
    private LinearLayout ll_gywm_back;
    RequestQueue queue = null;
    private ImageView iv_gywm_logo;
    private TextView tv_gywm_name;
    private TextView tv_gywm_slogan;
    private TextView tv_gywm_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_gywm);
        queue = Volley.newRequestQueue(GywmActivity.this);
        ll_gywm_back = findViewById(R.id.ll_gywm_back);
        ll_gywm_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iv_gywm_logo = findViewById(R.id.iv_gywm_logo);
        tv_gywm_name = findViewById(R.id.tv_gywm_name);
        tv_gywm_slogan = findViewById(R.id.tv_gywm_slogan);
        tv_gywm_desc = findViewById(R.id.tv_gywm_desc);
        query();
    }

    private void query() {
        String url = Api.sUrl + "Community/Api/guanyuwomen/appId/"
                + Api.sApp_Id + "/community_id/" + ShequnXqActivity.community_id;
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
                        String name = jsonObjectdate.getString("name");
                        String logo = jsonObjectdate.getString("logo");
                        String slogan = jsonObjectdate.getString("slogan");
                        String desc = jsonObjectdate.getString("desc");
                        Glide.with(GywmActivity.this)
                                .load( Api.sUrl+ logo)
                                .asBitmap()
                                .placeholder(R.mipmap.error)
                                .error(R.mipmap.error)
                                .dontAnimate()
                                .into(iv_gywm_logo);
                        tv_gywm_name.setText(name);
                        tv_gywm_slogan.setText(slogan);
                        tv_gywm_desc.setText(desc);
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
        loadingDialog = new LoadingDialog(GywmActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(GywmActivity.this, R.style.dialog, sHint, type, true).show();
    }
}
