package com.example.administrator.yunyue.fjsc_activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
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
import com.example.administrator.yunyue.sc_activity.ZffsActivity;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fjsc_DdxqActivity extends AppCompatActivity {
    private static final String TAG = Fjsc_DdxqActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_fjsc_ckdd_back;
    /**
     * 申请退款
     */
    private TextView tv_fjsc_ddxq_sqtk;
    private TextView tv_fjsc_ddxq_sqtk1;
    private SharedPreferences pref;
    private String sUser_id = "";
    private String sOrder_id = "";

    private String resultPhone = "";
    private MyAdapter myAdapter;
    /**
     * 订单详情商品列表
     */
    private MyGridView mgv_fjsc_ddxq;
    /**
     * 店铺名称
     */
    private TextView tv_fjsc_ddxq_storename;
    /**
     * 实付款
     */
    private TextView tv_fjsc_ddxq_totle_price;
    /**
     * 预计送达时间
     */
    private TextView tv_fjsc_ddxq_time;
    /**
     * 订单编号
     */
    private TextView tv_fjsc_ddxq_order_no;
    /**
     * 创建时间
     */
    private TextView tv_fjsc_ddxq_start_time;
    /**
     * 支付时间
     */
    private TextView tv_fjsc_ddxq_pay_time;
    /**
     * 共用按键
     */
    private TextView tv_fjsc_ddxq_tab1, tv_fjsc_ddxq_tab2;

    /**
     * 订单状态
     */
    private TextView tv_fjsc_ddxq_state;
    private TextView tv_fjsc_ddxq_state1;
    /**
     * 订单状态
     */
    private int state = 0;
    //商家名称
    private String shangjia_name = "";
    //商家id
    private String shangjia_id = "";

    /**
     * 商家logo
     */
    private String sLogo = "";

    /**
     * 收货人姓名
     */
    private TextView tv_fjsc_ddxq_name;
    /**
     * 收货人电话
     */
    private TextView tv_fjsc_ddxq_mobile;
    /**
     * 收货人地址
     */
    private TextView tv_fjsc_ddxq_dizhi;

    /**
     * 支付完成
     */
    private LinearLayout ll_fjsc_ddxq_zfwc;
    /**
     * 底部
     */
    private LinearLayout ll_fjsc_ddxq_bottom;
    /**
     * 订单完成
     */
    private LinearLayout ll_fjsc_ddxq_ddwc;
    /**
     * 店铺评价
     */
    private TextView tv_fjsc_ddxq_pj;
    /**
     * 完成描述
     */
    private TextView tv_fjsc_ddxq_text;
    /**
     * 预计送达时间
     */
    private LinearLayout ll_fjsc_ddxq_yjsdsj;
    /**
     * 退款原因
     */
    private TextView tv_fjsc_ddxq_cause;
    /**
     * 退款金额
     */
    private TextView tv_fjsc_ddxq_goods_price, tv_fjsc_ddxq_goods_price1;
    /**
     * 申请件数
     */
    private TextView tv_fjsc_ddxq_goods_num;
    /**
     * 申请时间
     */
    private TextView tv_fjsc_ddxq_apply_time;
    /**
     * 退款编号
     */
    private TextView tv_fjsc_ddxq_refund_no;
    private LinearLayout ll_fjsc_ddxq_tk;
    private LinearLayout ll_fjsc_ddxq_tkcg;
    /**
     * 退款时间
     */
    private TextView tv_fjsc_ddxq_tksj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_fjsc__ddxq);
        pref = PreferenceManager.getDefaultSharedPreferences(Fjsc_DdxqActivity.this);
        sUser_id = pref.getString("user_id", "");
        sOrder_id = getIntent().getStringExtra("order_id");
        ll_fjsc_ckdd_back = findViewById(R.id.ll_fjsc_ckdd_back);
        mgv_fjsc_ddxq = findViewById(R.id.mgv_fjsc_ddxq);
        tv_fjsc_ddxq_storename = findViewById(R.id.tv_fjsc_ddxq_storename);
        tv_fjsc_ddxq_totle_price = findViewById(R.id.tv_fjsc_ddxq_totle_price);
        tv_fjsc_ddxq_time = findViewById(R.id.tv_fjsc_ddxq_time);
        tv_fjsc_ddxq_order_no = findViewById(R.id.tv_fjsc_ddxq_order_no);
        tv_fjsc_ddxq_start_time = findViewById(R.id.tv_fjsc_ddxq_start_time);
        tv_fjsc_ddxq_pay_time = findViewById(R.id.tv_fjsc_ddxq_pay_time);
        tv_fjsc_ddxq_tab1 = findViewById(R.id.tv_fjsc_ddxq_tab1);
        tv_fjsc_ddxq_tab2 = findViewById(R.id.tv_fjsc_ddxq_tab2);
        tv_fjsc_ddxq_state = findViewById(R.id.tv_fjsc_ddxq_state);
        tv_fjsc_ddxq_state1 = findViewById(R.id.tv_fjsc_ddxq_state1);
        tv_fjsc_ddxq_name = findViewById(R.id.tv_fjsc_ddxq_name);
        tv_fjsc_ddxq_mobile = findViewById(R.id.tv_fjsc_ddxq_mobile);
        tv_fjsc_ddxq_dizhi = findViewById(R.id.tv_fjsc_ddxq_dizhi);
        ll_fjsc_ddxq_zfwc = findViewById(R.id.ll_fjsc_ddxq_zfwc);
        ll_fjsc_ddxq_bottom = findViewById(R.id.ll_fjsc_ddxq_bottom);
        ll_fjsc_ddxq_ddwc = findViewById(R.id.ll_fjsc_ddxq_ddwc);
        tv_fjsc_ddxq_text = findViewById(R.id.tv_fjsc_ddxq_text);
        ll_fjsc_ddxq_yjsdsj = findViewById(R.id.ll_fjsc_ddxq_yjsdsj);

        ll_fjsc_ddxq_tk = findViewById(R.id.ll_fjsc_ddxq_tk);
        ll_fjsc_ddxq_tkcg = findViewById(R.id.ll_fjsc_ddxq_tkcg);
        tv_fjsc_ddxq_tksj = findViewById(R.id.tv_fjsc_ddxq_tksj);
        tv_fjsc_ddxq_cause = findViewById(R.id.tv_fjsc_ddxq_cause);
        tv_fjsc_ddxq_goods_price = findViewById(R.id.tv_fjsc_ddxq_goods_price);
        tv_fjsc_ddxq_goods_price1 = findViewById(R.id.tv_fjsc_ddxq_goods_price1);
        tv_fjsc_ddxq_goods_num = findViewById(R.id.tv_fjsc_ddxq_goods_num);
        tv_fjsc_ddxq_apply_time = findViewById(R.id.tv_fjsc_ddxq_apply_time);
        tv_fjsc_ddxq_refund_no = findViewById(R.id.tv_fjsc_ddxq_refund_no);

        ll_fjsc_ckdd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_fjsc_ddxq_tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog();
            }
        });
        tv_fjsc_ddxq_tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_fjsc_ddxq_tab2.getText().toString().equals("立即支付")) {
                    Intent intent = new Intent(Fjsc_DdxqActivity.this, ZffsActivity.class);
                    intent.putExtra("activity", "Fjsc_ddxq");
                    intent.putExtra("data", sOrder_id);
                    intent.putExtra("money", tv_fjsc_ddxq_totle_price.getText().toString());
                    startActivity(intent);
                } else if (tv_fjsc_ddxq_tab2.getText().toString().equals("确认收货")) {
                    dialogin("");
                    sShouhuo();
                } else if (tv_fjsc_ddxq_tab2.getText().toString().equals("评论")) {
                    Intent intent = new Intent(Fjsc_DdxqActivity.this, Fjsc_DppjActivity.class);
                    Fjsc_DppjActivity.sOrder_id = sOrder_id;
                    Fjsc_DppjActivity.sLogo = sLogo;
                    Fjsc_DppjActivity.sShangjia_id = shangjia_id;
                    Fjsc_DppjActivity.sShangjia_name = shangjia_name;
                    startActivity(intent);
                }

            }
        });
        tv_fjsc_ddxq_pj = findViewById(R.id.tv_fjsc_ddxq_pj);
        tv_fjsc_ddxq_pj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Fjsc_DdxqActivity.this, Fjsc_DppjActivity.class);
                Fjsc_DppjActivity.sOrder_id = sOrder_id;
                Fjsc_DppjActivity.sLogo = sLogo;
                Fjsc_DppjActivity.sShangjia_id = shangjia_id;
                Fjsc_DppjActivity.sShangjia_name = shangjia_name;
                startActivity(intent);
            }
        });

        tv_fjsc_ddxq_sqtk = findViewById(R.id.tv_fjsc_ddxq_sqtk);
        tv_fjsc_ddxq_sqtk1 = findViewById(R.id.tv_fjsc_ddxq_sqtk1);
        tv_fjsc_ddxq_sqtk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_fjsc_ddxq_sqtk.getText().toString().equals("申请退款")) {
                    Intent intent = new Intent(Fjsc_DdxqActivity.this, Fjsc_SqtkActivity.class);
                    intent.putExtra("id", sOrder_id);
                    intent.putExtra("state", String.valueOf(state));
                    startActivity(intent);
                }
            }
        });
        tv_fjsc_ddxq_sqtk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_fjsc_ddxq_sqtk.getText().toString().equals("申请退款")) {
                    Intent intent = new Intent(Fjsc_DdxqActivity.this, Fjsc_SqtkActivity.class);
                    intent.putExtra("id", sOrder_id);
                    intent.putExtra("state", String.valueOf(state));
                    startActivity(intent);
                }
            }
        });

        sOrderdetail();
    }

    /**
     * 方法名：sOrderdetail()
     * 功  能：订单
     * 参  数：appId
     * order_id--订单id
     */
    private void sOrderdetail() {
        String url = Api.sUrl + Api.sOrderdetail;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("order_id", sOrder_id);
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
                                //JSONObject jsonObjectdata = new JSONObject(jsonObject.getString("data"));
                                JSONObject jsonObjectdetails = jsonObject.getJSONObject("data");
                                //	状态 1待付款 2已付款待发货 3已发货待收货 4已收货待评价
                                state = jsonObjectdetails.getInt("state");
                                //	1审核中 2审核失败 3审核通过 4同意退款 5已退款
                                int shouhoustate = jsonObjectdetails.getInt("shouhoustate");
                                //订单id
                                int id = jsonObjectdetails.getInt("id");
                                //退款成功时间
                                String sEnd_time = jsonObjectdetails.getString("end_time");
                                tv_fjsc_ddxq_tksj.setText(sEnd_time);
                                if (state == 1) {
                                    ll_fjsc_ddxq_bottom.setVisibility(View.VISIBLE);
                                    tv_fjsc_ddxq_state.setText("待付款");
                                    ll_fjsc_ddxq_zfwc.setVisibility(View.VISIBLE);
                                    ll_fjsc_ddxq_ddwc.setVisibility(View.GONE);
                                    tv_fjsc_ddxq_state1.setText("当前订单待付款");
                                    tv_fjsc_ddxq_tab2.setVisibility(View.VISIBLE);
                                    tv_fjsc_ddxq_tab2.setText("立即支付");
                                    ll_fjsc_ddxq_zfwc.setVisibility(View.VISIBLE);
                                    ll_fjsc_ddxq_yjsdsj.setVisibility(View.VISIBLE);

                                } else if (state == 2) {
                                    ll_fjsc_ddxq_bottom.setVisibility(View.VISIBLE);
                                    ll_fjsc_ddxq_zfwc.setVisibility(View.VISIBLE);
                                    ll_fjsc_ddxq_ddwc.setVisibility(View.GONE);
                                    tv_fjsc_ddxq_state1.setText("当前订单支付完成");
                                    tv_fjsc_ddxq_state.setText("支付完成");
                                    tv_fjsc_ddxq_tab2.setVisibility(View.GONE);
                                    ll_fjsc_ddxq_yjsdsj.setVisibility(View.VISIBLE);
                                } else if (state == 3) {
                                    ll_fjsc_ddxq_bottom.setVisibility(View.VISIBLE);
                                    ll_fjsc_ddxq_zfwc.setVisibility(View.VISIBLE);
                                    ll_fjsc_ddxq_ddwc.setVisibility(View.GONE);
                                    tv_fjsc_ddxq_state.setText("支付完成");
                                    tv_fjsc_ddxq_state1.setText("当前订单支付完成");
                                    tv_fjsc_ddxq_tab2.setVisibility(View.VISIBLE);
                                    tv_fjsc_ddxq_tab2.setText("确认收货");
                                    ll_fjsc_ddxq_yjsdsj.setVisibility(View.VISIBLE);
                                } else if (state == 4) {
                                    ll_fjsc_ddxq_bottom.setVisibility(View.GONE);
                                    ll_fjsc_ddxq_zfwc.setVisibility(View.GONE);
                                    ll_fjsc_ddxq_ddwc.setVisibility(View.VISIBLE);
                                    tv_fjsc_ddxq_state1.setText("当前订单支付完成");
                                    tv_fjsc_ddxq_state.setText("支付完成");
                                    tv_fjsc_ddxq_tab2.setVisibility(View.VISIBLE);
                                    tv_fjsc_ddxq_tab2.setText("评论");
                                    ll_fjsc_ddxq_yjsdsj.setVisibility(View.GONE);
                                } else {
                                    ll_fjsc_ddxq_ddwc.setVisibility(View.VISIBLE);
                                    ll_fjsc_ddxq_bottom.setVisibility(View.GONE);
                                    ll_fjsc_ddxq_zfwc.setVisibility(View.GONE);
                                    tv_fjsc_ddxq_state1.setText("当前订单支付完成");
                                    tv_fjsc_ddxq_state.setText("支付完成");
                                    tv_fjsc_ddxq_pj.setVisibility(View.GONE);
                                    tv_fjsc_ddxq_tab2.setVisibility(View.GONE);
                                    ll_fjsc_ddxq_yjsdsj.setVisibility(View.GONE);

                                }
                           /*     if (shouhoustate == 0) {
                                    ll_fjsc_ddxq_tk.setVisibility(View.GONE);
                                    ll_fjsc_ddxq_tkcg.setVisibility(View.GONE);
                                    tv_fjsc_ddxq_sqtk.setText("申请退款");
                                    tv_fjsc_ddxq_sqtk.setText("申请退款");
                                    tv_fjsc_ddxq_text.setText("感谢您对生态云商城的信任，期待再次光临");
                                    tv_fjsc_ddxq_sqtk.setVisibility(View.VISIBLE);
                                } else if (shouhoustate == 2) {
                                    ll_fjsc_ddxq_ddwc.setVisibility(View.GONE);
                                    ll_fjsc_ddxq_zfwc.setVisibility(View.GONE);
                                    ll_fjsc_ddxq_tkcg.setVisibility(View.VISIBLE);
                                    ll_fjsc_ddxq_tk.setVisibility(View.VISIBLE);
                                    tv_fjsc_ddxq_text.setText("售后审核失败");
                                    tv_fjsc_ddxq_sqtk.setVisibility(View.GONE);
                                    tv_fjsc_ddxq_sqtk.setText("售后审核失败");
                                } else {
                                    ll_fjsc_ddxq_ddwc.setVisibility(View.GONE);
                                    ll_fjsc_ddxq_zfwc.setVisibility(View.GONE);
                                    ll_fjsc_ddxq_tkcg.setVisibility(View.VISIBLE);
                                    ll_fjsc_ddxq_tk.setVisibility(View.VISIBLE);
                                    tv_fjsc_ddxq_text.setText("退款成功");
                                    tv_fjsc_ddxq_sqtk.setVisibility(View.GONE);
                                    tv_fjsc_ddxq_sqtk.setText("退款成功");
                                }*/
                                if (shouhoustate == 0) {
                                    ll_fjsc_ddxq_tk.setVisibility(View.GONE);
                                    ll_fjsc_ddxq_tkcg.setVisibility(View.GONE);
                                    tv_fjsc_ddxq_sqtk.setText("申请退款");
                                    tv_fjsc_ddxq_sqtk.setText("申请退款");
                                    tv_fjsc_ddxq_text.setText("感谢您对生态云商城的信任，期待再次光临");
                                    ll_fjsc_ddxq_yjsdsj.setVisibility(View.VISIBLE);
                                    tv_fjsc_ddxq_sqtk.setVisibility(View.VISIBLE);
                                } else if (shouhoustate == 1) {
                                    ll_fjsc_ddxq_ddwc.setVisibility(View.VISIBLE);
                                    ll_fjsc_ddxq_zfwc.setVisibility(View.GONE);
                                    ll_fjsc_ddxq_tkcg.setVisibility(View.GONE);
                                    ll_fjsc_ddxq_tk.setVisibility(View.VISIBLE);
                                    tv_fjsc_ddxq_text.setText("售后审核中");
                                    tv_fjsc_ddxq_sqtk.setVisibility(View.GONE);
                                    tv_fjsc_ddxq_sqtk1.setVisibility(View.GONE);
                                    ll_fjsc_ddxq_yjsdsj.setVisibility(View.GONE);

                                    tv_fjsc_ddxq_sqtk.setText("售后审核中");
                                } else if (shouhoustate == 2) {
                                    ll_fjsc_ddxq_ddwc.setVisibility(View.VISIBLE);
                                    ll_fjsc_ddxq_zfwc.setVisibility(View.GONE);
                                    ll_fjsc_ddxq_tkcg.setVisibility(View.GONE);
                                    ll_fjsc_ddxq_tk.setVisibility(View.VISIBLE);
                                    tv_fjsc_ddxq_text.setText("售后审核失败");
                                    tv_fjsc_ddxq_sqtk.setVisibility(View.GONE);
                                    tv_fjsc_ddxq_sqtk1.setVisibility(View.GONE);
                                    ll_fjsc_ddxq_yjsdsj.setVisibility(View.GONE);
                                    tv_fjsc_ddxq_sqtk.setText("售后审核失败");
                                } else if (shouhoustate == 5) {
                                    ll_fjsc_ddxq_ddwc.setVisibility(View.GONE);
                                    ll_fjsc_ddxq_zfwc.setVisibility(View.GONE);
                                    ll_fjsc_ddxq_tkcg.setVisibility(View.VISIBLE);
                                    ll_fjsc_ddxq_tk.setVisibility(View.VISIBLE);
                                    tv_fjsc_ddxq_text.setText("退款成功");
                                    tv_fjsc_ddxq_sqtk.setVisibility(View.GONE);
                                    tv_fjsc_ddxq_sqtk1.setVisibility(View.GONE);
                                    ll_fjsc_ddxq_yjsdsj.setVisibility(View.GONE);
                                    tv_fjsc_ddxq_sqtk.setText("退款成功");
                                } else {
                                    ll_fjsc_ddxq_ddwc.setVisibility(View.VISIBLE);
                                    ll_fjsc_ddxq_zfwc.setVisibility(View.GONE);
                                    ll_fjsc_ddxq_tkcg.setVisibility(View.GONE);
                                    ll_fjsc_ddxq_tk.setVisibility(View.VISIBLE);
                                    tv_fjsc_ddxq_text.setText("售后审核通过");
                                    tv_fjsc_ddxq_sqtk.setVisibility(View.GONE);
                                    tv_fjsc_ddxq_sqtk1.setVisibility(View.GONE);
                                    ll_fjsc_ddxq_yjsdsj.setVisibility(View.GONE);
                                    tv_fjsc_ddxq_sqtk.setText("售后审核通过");
                                }


                                //商家id
                                shangjia_id = jsonObjectdetails.getString("shangjia_id");
                                tv_fjsc_ddxq_storename.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Fjsc_DdxqActivity.this, Fjsc_DpxqActivity.class);
                                        intent.putExtra("shangjia_id", shangjia_id);
                                        startActivity(intent);
                                    }
                                });

                                sLogo = jsonObjectdetails.getString("logo");
                                //商家名称
                                shangjia_name = jsonObjectdetails.getString("shangjia_name");
                                tv_fjsc_ddxq_storename.setText(shangjia_name);
                                //订单编号
                                String order_sn = jsonObjectdetails.getString("order_no");
                                tv_fjsc_ddxq_order_no.setText("订单编号：" + order_sn);
                                //下单时间
                                String start_time = jsonObjectdetails.getString("start_time");
                                tv_fjsc_ddxq_start_time.setText("创建时间：" + start_time);
                                //支付时间
                                String pay_time = jsonObjectdetails.getString("pay_time");
                                tv_fjsc_ddxq_pay_time.setText("支付时间：" + pay_time);
                                //实际价格
                                String new_price = jsonObjectdetails.getString("new_price");
                                //最终实际支付价格
                                String totle_price = jsonObjectdetails.getString("totle_price");
                                tv_fjsc_ddxq_totle_price.setText("￥" + totle_price);
                                //用户电话
                                String mobile = jsonObjectdetails.getString("mobile");
                                tv_fjsc_ddxq_mobile.setText(mobile);
                                //用户名称
                                String user_name = jsonObjectdetails.getString("user_name");
                                tv_fjsc_ddxq_name.setText(user_name);
                                //用户地址
                                String address = jsonObjectdetails.getString("address");
                                tv_fjsc_ddxq_dizhi.setText(address);
                                //商家电话
                                resultPhone = jsonObjectdetails.getString("lianxiphone");
                                //预计送达时间
                                String time = jsonObjectdetails.getString("time");
                                tv_fjsc_ddxq_time.setText(time);

                                //退款原因
                                String cause = jsonObjectdetails.getString("cause");
                                tv_fjsc_ddxq_cause.setText("退款原因：" + cause);
                                tv_fjsc_ddxq_goods_price.setText("退款金额：" + totle_price);
                                tv_fjsc_ddxq_goods_price1.setText("￥" + totle_price);
                                //商品数量
                                String goods_num = jsonObjectdetails.getString("goods_num");
                                tv_fjsc_ddxq_goods_num.setText("申请件数：" + goods_num);

                                //申请时间
                                String apply_time = jsonObjectdetails.getString("apply_time");
                                tv_fjsc_ddxq_apply_time.setText("申请时间：" + apply_time);

                                //退款编号
                                String refund_no = jsonObjectdetails.getString("refund_no");
                                tv_fjsc_ddxq_refund_no.setText("退款编号：" + refund_no);


                                JSONArray resultJsonArrayList = jsonObjectdetails.getJSONArray("list");
                                myAdapter = new MyAdapter(Fjsc_DdxqActivity.this);
                                ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < resultJsonArrayList.length(); i++) {
                                    JSONObject jsonObjectResultData = resultJsonArrayList.getJSONObject(i);
                                    //商品id
                                    String resultGoods_id = jsonObjectResultData.getString("goods_id");
                                    //商品名称
                                    String resultGoods_name = jsonObjectResultData.getString("goods_name");
                                    //商品数量
                                    String resultGoods_num = jsonObjectResultData.getString("goods_num");
                                    //支付价格
                                    String resultFinal_price = jsonObjectResultData.getString("final_price");
                                    //规格
                                    String resultSpec_key_name = jsonObjectResultData.getString("spec_key_name");
                                    //商品图片
                                    String resultGood_pic = jsonObjectResultData.getString("good_pic");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("ItemGood_id", resultGoods_id);
                                    map.put("ItemGood_pic", resultGood_pic);
                                    map.put("ItemGoods_Name", resultGoods_name);
                                    map.put("ItemSpec_Sey_Name", resultSpec_key_name);
                                    map.put("ItemGoods_Price", resultFinal_price);
                                    map.put("ItemGoods_Num", resultGoods_num);
                                    map.put("ItemShangjia_id", shangjia_id);
                                    mylist.add(map);
                                }
                                myAdapter.arrlist = mylist;
                                mgv_fjsc_ddxq.setAdapter(myAdapter);


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
                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialogin();
                //  Hint(error.getMessage(), HintDialog.ERROR);
                error(error);
            }
        });
        requestQueue.add(jsonRequest);
    }


    /**
     * 方法名：sShouhuo()
     * 功  能：用户收货接口
     * 参  数：appId
     * order_id--订单id
     */
    private void sShouhuo() {
        String url = Api.sUrl + Api.sShouhuo;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("order_id", sOrder_id);
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
                                Hint(resultMsg, HintDialog.SUCCESS);
                                sOrderdetail();
                            } else {

                                Hint(resultMsg, HintDialog.ERROR);
                                //   Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "response -> " + response.toString());
                        //   Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener()

        {
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
        loadingDialog = new LoadingDialog(Fjsc_DdxqActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(Fjsc_DdxqActivity.this, R.style.dialog, sHint, type, true).show();
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;


        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            new ArrayList<HashMap<String, String>>();


        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arrlist.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }


        @Override
        public View getView(final int position, View view, ViewGroup arg2) {
            // TODO Auto-generated method stub
            if (view == null) {
                view = inflater.inflate(R.layout.ckdd_item, null);
            }
            LinearLayout ll_ckdd_item = view.findViewById(R.id.ll_ckdd_item);
            ll_ckdd_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //    hideDialogin();
                   /* dialogin("");
                    jianjei(arrlist.get(position).get("ItemGood_id"));*/
                }
            });
            ImageView iv_ckdd_image = view.findViewById(R.id.iv_ckdd_image);
            TextView tv_ckdd_goods_name = view.findViewById(R.id.tv_ckdd_goods_name);
            TextView tv_ckdd_spec_key_name = view.findViewById(R.id.tv_ckdd_spec_key_name);
            TextView tv_ckdd_order_amount = view.findViewById(R.id.tv_ckdd_order_amount);
            TextView tv_ckdd_goods_num = view.findViewById(R.id.tv_ckdd_goods_num);
            Glide.with(Fjsc_DdxqActivity.this)
                    .load( Api.sUrl+arrlist.get(position).get("ItemGood_pic"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_ckdd_image);
            tv_ckdd_goods_name.setText(arrlist.get(position).get("ItemGoods_Name"));
            tv_ckdd_spec_key_name.setText(arrlist.get(position).get("ItemSpec_Sey_Name"));
            tv_ckdd_order_amount.setText("￥" + arrlist.get(position).get("ItemGoods_Price"));
            tv_ckdd_goods_num.setText("数量：" + arrlist.get(position).get("ItemGoods_Num"));


            return view;
        }

    }

    public void dialog() {
        Dialog dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        final View inflate = LayoutInflater.from(this).inflate(R.layout.lxmj_dialog, null);
        final TextView tv_lxmj_dialog = inflate.findViewById(R.id.tv_lxmj_dialog);
        tv_lxmj_dialog.setText(resultPhone);
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
        dialog.show();
        tv_lxmj_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diallPhone(resultPhone);
            }
        });
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void diallPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }
}
