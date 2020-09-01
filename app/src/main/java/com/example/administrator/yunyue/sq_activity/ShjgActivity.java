package com.example.administrator.yunyue.sq_activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
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

public class ShjgActivity extends AppCompatActivity {
    /**
     * 返回
     */
    private LinearLayout ll_shjg_back;
    RequestQueue queue = null;
    /**
     * 头像
     */
    private ImageView iv_shjg_log;
    /**
     * 名称
     */
    private TextView tv_shjg_name;
    /**
     * 描述
     */
    private TextView tv_shjg_hint;
    private TextView tv_shjg_miaoshu;
    private TextView tv_shjg_tgsh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_shjg);
        queue = Volley.newRequestQueue(ShjgActivity.this);
        Intent intent = getIntent();
        final String sId = intent.getStringExtra("id");
        final String sGroup_id = intent.getStringExtra("group_id");
        String sHeadimgurl = intent.getStringExtra("headimgurl");
        String sNickname = intent.getStringExtra("nickname");
        String sGroup_name = intent.getStringExtra("group_name");
        String sInfo = intent.getStringExtra("info");
/*        intent.putExtra("headimgurl", mylist.get(position).get("headimgurl"));
        intent.putExtra("nickname", mylist.get(position).get("nickname"));
        intent.putExtra("group_name", "申请加入" + mylist.get(position).get("group_name"));
        intent.putExtra("info", mylist.get(position).get("info"));*/
        ll_shjg_back = findViewById(R.id.ll_shjg_back);
        iv_shjg_log = findViewById(R.id.iv_shjg_log);
        tv_shjg_name = findViewById(R.id.tv_shjg_name);
        tv_shjg_hint = findViewById(R.id.tv_shjg_hint);
        tv_shjg_miaoshu = findViewById(R.id.tv_shjg_miaoshu);
        Glide.with(ShjgActivity.this)
                .load( Api.sUrl+ sHeadimgurl)
                .asBitmap()
                .placeholder(R.mipmap.error)
                .error(R.mipmap.error)
                .dontAnimate()
                .into(iv_shjg_log);
        tv_shjg_name.setText(sNickname);
        tv_shjg_hint.setText(sGroup_name);
        tv_shjg_miaoshu.setText(sInfo);
        ll_shjg_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_shjg_tgsh = findViewById(R.id.tv_shjg_tgsh);
        tv_shjg_tgsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDialogin();
                dialogin("");
                save(sId,sGroup_id);
            }
        });
    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(ShjgActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(ShjgActivity.this, R.style.dialog, sHint, type, true).show();
    }

    /**
     * 通过审核
     */
    private void save(String user_id, String group_id) {
        String url = Api.sUrl + "Community/Api/tongyi/appId/" + Api.sApp_Id
                + "/user_id/" + user_id + "/group_id/" + group_id;
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
                Hint(String.valueOf(volleyError), HintDialog.ERROR);
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }

}
