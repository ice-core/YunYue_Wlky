package com.example.administrator.yunyue.sc_activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
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

public class DpxqActivity extends AppCompatActivity {
    /**
     * 返回
     */
    private ImageView iv_dpxq_back;
    private String sId = "";
    RequestQueue queue = null;
    private String sUser_id = "";
    private SharedPreferences pref;

    /**
     * 店铺Logo
     */
    private ImageView iv_dpxq_image;
    /**
     * 店铺名称
     */
    private TextView tv_dpxq_name;
    /**
     * 店铺简介
     */
    private TextView tv_dpxq_note;
    /**
     * 开店时间
     */
    private TextView tv_dpxq_createtime;
    /**
     * 关注人数
     */
    private TextView tv_dpxq_count;
    /**
     * 是否关注
     */
    private TextView tv_dpxq_is_attention;

    /**
     * 查看全部
     */
    private LinearLayout ll_dpxq_ckqb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_dpxq);
        queue = Volley.newRequestQueue(DpxqActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        Intent intent = getIntent();
        sId = intent.getStringExtra("id");
        iv_dpxq_back = findViewById(R.id.iv_dpxq_back);
        iv_dpxq_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iv_dpxq_image = findViewById(R.id.iv_dpxq_image);
        tv_dpxq_name = findViewById(R.id.tv_dpxq_name);
        tv_dpxq_note = findViewById(R.id.tv_dpxq_note);
        tv_dpxq_createtime = findViewById(R.id.tv_dpxq_createtime);
        tv_dpxq_count = findViewById(R.id.tv_dpxq_count);
        tv_dpxq_is_attention = findViewById(R.id.tv_dpxq_is_attention);
        ll_dpxq_ckqb = findViewById(R.id.ll_dpxq_ckqb);
    /*    dialogin("");
        xiangqiang(sId);*/
        tv_dpxq_is_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogin("");
                dpgz();
            }
        });
        ll_dpxq_ckqb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DpxqActivity.this, DpzyActivity.class);
                intent.putExtra("id", sId);
                intent.putExtra("type", "全部");
                startActivity(intent);
            }
        });
        Glide.with(DpxqActivity.this)
                .load( Api.sUrl+ DpzyActivity.image)
                .asBitmap()
                .placeholder(R.mipmap.error)
                .error(R.mipmap.error)
                .dontAnimate()
                .into(iv_dpxq_image);
        tv_dpxq_name.setText(DpzyActivity.storename);
        tv_dpxq_createtime.setText(DpzyActivity.time);
        tv_dpxq_count.setText(DpzyActivity.count);
        tv_dpxq_note.setText(DpzyActivity.shangjia_intro);
        if (DpzyActivity.guanzhu == 0) {
            tv_dpxq_is_attention.setBackgroundResource(R.drawable.bk_dfdfdf_12);
            tv_dpxq_is_attention.setTextColor(tv_dpxq_is_attention.getResources().getColor(R.color.hui999999));
            tv_dpxq_is_attention.setText("关注");
        } else {
            tv_dpxq_is_attention.setBackgroundResource(R.drawable.bj_lan12);
            tv_dpxq_is_attention.setTextColor(tv_dpxq_is_attention.getResources().getColor(R.color.white));
            tv_dpxq_is_attention.setText("已关注");
        }
    }


    /**
     * 店铺关注
     */
    private void dpgz() {
        String url = Api.sUrl + "Api/Good/guanzhushangjia/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/shangjia_id/" + sId;
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
                        if (tv_dpxq_is_attention.getText().toString().equals("关注")) {
                            tv_dpxq_is_attention.setBackgroundResource(R.drawable.bj_lan12);
                            tv_dpxq_is_attention.setTextColor(tv_dpxq_is_attention.getResources().getColor(R.color.white));
                            tv_dpxq_is_attention.setText("已关注");
                        } else {
                            tv_dpxq_is_attention.setBackgroundResource(R.drawable.bk_dfdfdf_12);
                            tv_dpxq_is_attention.setTextColor(tv_dpxq_is_attention.getResources().getColor(R.color.hui999999));
                            tv_dpxq_is_attention.setText("关注");
                        }
                        Hint(resultMsg, HintDialog.SUCCESS);
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

    private void xiangqiang(final String rid) {
        String url = Api.sUrl + "Store/residenceDetail/rid/" + rid + "/user_id/" + sUser_id;
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
                    String resultCount = jsonObject1.getString("count");
                    /**
                     * 1关注 0未关注.
                     * */
                    int resultis_attention = jsonObject1.getInt("is_attention");
                    if (resultCode > 0) {
                        JSONObject jsonObjectdate = jsonObject1.getJSONObject("residence");
                        String id = jsonObjectdate.getString("id");
                        String image = jsonObjectdate.getString("image");
                        String storename = jsonObjectdate.getString("storename");
                        String createtime = jsonObjectdate.getString("createtime");
                        String note = jsonObjectdate.getString("note");
                        Glide.with(DpxqActivity.this)
                                .load( Api.sUrl+ image)
                                .asBitmap()
                                .placeholder(R.mipmap.error)
                                .error(R.mipmap.error)
                                .dontAnimate()
                                .into(iv_dpxq_image);
                        tv_dpxq_name.setText(storename);
                        tv_dpxq_createtime.setText(createtime);
                        tv_dpxq_count.setText(resultCount);
                        tv_dpxq_note.setText(note);
                        // tv_dpzy_count.setText(count);
                        if (resultis_attention == 0) {
                            tv_dpxq_is_attention.setBackgroundResource(R.drawable.bk_dfdfdf_12);
                            tv_dpxq_is_attention.setTextColor(tv_dpxq_is_attention.getResources().getColor(R.color.hui999999));
                            tv_dpxq_is_attention.setText("关注");
                        } else {
                            tv_dpxq_is_attention.setBackgroundResource(R.drawable.bj_lan12);
                            tv_dpxq_is_attention.setTextColor(tv_dpxq_is_attention.getResources().getColor(R.color.white));
                            tv_dpxq_is_attention.setText("已关注");
                        }
                        /**
                         * 1关注 0未关注.
                         * */
                      /*  if (is_attention.equals("0")) {
                            tv_dpzy_is_attention.setText("关注");
                        } else {
                            tv_dpzy_is_attention.setText("已关注");
                        }*/
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
        loadingDialog = new LoadingDialog(DpxqActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(DpxqActivity.this, R.style.dialog, sHint, type, true).show();
    }
}
