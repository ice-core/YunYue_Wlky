package com.example.administrator.yunyue.fjsc_activity;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.example.administrator.yunyue.Jjcc;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.data.Fjsc_TuijianData;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.image.ImagePagerActivity;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.example.administrator.yunyue.widget.BadgeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Fjsc_SpxqActivity extends AppCompatActivity implements OnBannerListener {
    private static final String TAG = Fjsc_SpxqActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_fjsc_spxq_back;
    /**
     * 用户id
     */
    private String sUser_id = "";
    private SharedPreferences pref;
    /**
     * 商家ID
     */
    private String sShangjia_id = "";
    /**
     * 商品ID
     */
    private String sGood_id = "";
    private String sGg_id = "";
    private String sItem_Id = "";

    private MyAdapterGwc myAdapterGwc;

    private Dialog dialog, dialog_gwc;
    private View inflate;

    /**
     * 购物车
     */
    private MyGridView mgv_fjsc_dpsp_xlgwc;
    private TextView tv_fjsc_dpsp_xlgwc_num;
    private TextView tv_fjsc_dpsp_xlgwc_price;
    private TextView tv_fjsc_dpsp_xlgwc_qjs;

    /**
     * 规格弹框
     */
    private ImageView iv_spxq_xzgg_dialog_image;
    private TextView tv_spxq_xzgg_dialog_shop_price;
    private TextView tv_spxq_xzgg_dialog_xgg1, tv_spxq_xzgg_dialog_xgg2, tv_spxq_xzgg_dialog_xgg3, tv_fjsc_spxq_xzgg_dialog_name;
    private MyGridView mgv_spxq_xzgg_dialog_xgg1, mgv_spxq_xzgg_dialog_xgg2, mgv_spxq_xzgg_dialog_xgg3;
    /**
     * 规格确认
     */
    private Button bt_spxq_xzgg_dialog_xgg_qr;

    /**
     * 产品价格
     */
    private TextView tv_fjsc_spxp_gwc_price;
    /**
     * 去结算
     */
    private TextView tv_fjsc_spxq_uptosend;

    /**
     * 标题
     */
    private TextView tv_fjsc_spxq_hine;
    /**
     * 商品名称
     */
    private TextView tv_fjsc_spxq_name;
    /**
     * 月销
     */
    private TextView tv_fjsc_spxq_buy_count;
    /**
     * 商品价格
     */
    private TextView tv_fjsc_spxq_price;
    /**
     * 简介
     */
    private TextView tv_fjsc_spxq_intro;
    /**
     * 选规格
     */
    private TextView tv_fjsc_spxq_xgg;


    /**
     * 规格1-id
     */
    private String sGg1_id = "";
    /**
     * 规格1
     */
    private String sGg1 = "";
    /**
     * 规格2-id
     */
    private String sGg2_id = "";
    /**
     * 规格2
     */
    private String sGg2 = "";
    /**
     * 规格3-id
     */
    private String sGg3_id = "";
    /**
     * 规格3
     */
    private String sGg3 = "";


    /**
     * 规格
     */
    private String sGg = "";

    private ArrayList<HashMap<String, String>> mylistGuiGe;
    private ArrayList<HashMap<String, String>> newgoodguige;
    private JSONObject jsonObjectGuige_xp;
    /**
     * 购物车价格
     */
    private LinearLayout ll_fjsc_spxq_gwc;
    /**
     * 加入购物车
     */
    private ImageView iv_fjsc_spxq_jia;
    /**
     * 推荐
     */
    private MyGridView mgv_fjsc_spxq_tj;
    /**
     * banner
     */
    private Banner banner_fjsc_spjj;

    private ArrayList<String> list_path;
    private ImageView iv_fjsc_spxp_gwc, iv_fjsc_dpsp_xlgwc_gwc;
    BadgeView badgeView;
    BadgeView badgeView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_fjsc__spxq);
        badgeView = new BadgeView(Fjsc_SpxqActivity.this);
        badgeView1 = new BadgeView(Fjsc_SpxqActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(Fjsc_SpxqActivity.this);
        sUser_id = pref.getString("user_id", "");
        sGood_id = getIntent().getStringExtra("good_id");
        sShangjia_id = getIntent().getStringExtra("shangjia_id");
        ll_fjsc_spxq_back = findViewById(R.id.ll_fjsc_spxq_back);
        ll_fjsc_spxq_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_fjsc_spxp_gwc_price = findViewById(R.id.tv_fjsc_spxp_gwc_price);
        tv_fjsc_spxq_uptosend = findViewById(R.id.tv_fjsc_spxq_uptosend);
        tv_fjsc_spxq_hine = findViewById(R.id.tv_fjsc_spxq_hine);
        tv_fjsc_spxq_name = findViewById(R.id.tv_fjsc_spxq_name);
        tv_fjsc_spxq_buy_count = findViewById(R.id.tv_fjsc_spxq_buy_count);
        tv_fjsc_spxq_price = findViewById(R.id.tv_fjsc_spxq_price);
        tv_fjsc_spxq_intro = findViewById(R.id.tv_fjsc_spxq_intro);
        tv_fjsc_spxq_xgg = findViewById(R.id.tv_fjsc_spxq_xgg);
        iv_fjsc_spxq_jia = findViewById(R.id.iv_fjsc_spxq_jia);
        mgv_fjsc_spxq_tj = findViewById(R.id.mgv_fjsc_spxq_tj);
        banner_fjsc_spjj = findViewById(R.id.banner_fjsc_spjj);
        iv_fjsc_spxp_gwc = findViewById(R.id.iv_fjsc_spxp_gwc);
        iv_fjsc_spxq_jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogin("");
                sCartadd(sGood_id, sItem_Id);
            }
        });
        show_gwc();
        initialize();
        sGooddetail();
        sCartlist();
        sRecommended();
        tv_fjsc_spxq_xgg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sGg1_id = "";
                    sGg2_id = "";
                    sGg3_id = "";
                    mylistGuiGe = new ArrayList<>();
                    mylistGuiGe = newgoodguige;
                    tv_spxq_xzgg_dialog_xgg1.setText("");
                    tv_spxq_xzgg_dialog_xgg2.setText("");
                    tv_spxq_xzgg_dialog_xgg3.setText("");
                    bt_spxq_xzgg_dialog_xgg_qr.setBackgroundResource(R.drawable.bj_30000000_19);
                    bt_spxq_xzgg_dialog_xgg_qr.setEnabled(false);
                    tv_fjsc_spxq_xzgg_dialog_name.setText(tv_fjsc_spxq_hine.getText().toString());
                    JSONObject jsonObjectGuige = jsonObjectGuige_xp;
                    Iterator<String> it = jsonObjectGuige.keys();
                    while (it.hasNext()) {
                        ArrayList<String> mylist = new ArrayList<>();
                        // 获得key
                        String key = it.next();

                        JSONArray jsonObjectkey = null;

                        jsonObjectkey = jsonObjectGuige.getJSONArray(key);

                        for (int b = 0; b < jsonObjectkey.length(); b++) {
                            String imge = jsonObjectkey.get(b).toString();
                            mylist.add(imge);
                        }
                        if (tv_spxq_xzgg_dialog_xgg1.getText().toString().equals("")) {
                            tv_spxq_xzgg_dialog_xgg1.setVisibility(View.VISIBLE);
                            tv_spxq_xzgg_dialog_xgg1.setText(key);
                            setGridViewXgg1(mylist);
                        } else if (tv_spxq_xzgg_dialog_xgg2.getText().toString().equals("")) {
                            tv_spxq_xzgg_dialog_xgg2.setVisibility(View.VISIBLE);
                            tv_spxq_xzgg_dialog_xgg2.setText(key);
                            setGridViewXgg2(mylist);
                        } else if (tv_spxq_xzgg_dialog_xgg3.getText().toString().equals("")) {
                            tv_spxq_xzgg_dialog_xgg3.setVisibility(View.VISIBLE);
                            tv_spxq_xzgg_dialog_xgg3.setText(key);
                            setGridViewXgg3(mylist);
                        }

                    }
                    show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        ll_fjsc_spxq_gwc = findViewById(R.id.ll_fjsc_spxq_gwc);
        ll_fjsc_spxq_gwc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_fjsc_spxp_gwc_price.getText().toString().equals("未选购商品")) {
                } else {
                    dialogin("");
                    sCartlist();
                    dialog_gwc.show();
                }
            }
        });
        tv_fjsc_spxq_uptosend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_fjsc_dpsp_xlgwc_qjs.getText().toString().equals("去结算")) {
                    Intent intent = new Intent(Fjsc_SpxqActivity.this, Fjsc_QrddActivity.class);
                    Fjsc_QrddActivity.sId = sShangjia_id;
                    Fjsc_QrddActivity.iDz = 0;
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 选规格1
     */
    private void setGridViewXgg1(ArrayList<String> mylist) {
        MyAdapterXgg1 myAdapterXgg1 = new MyAdapterXgg1(Fjsc_SpxqActivity.this);
        myAdapterXgg1.arrlist = mylist;
        if (mylist.size() > 0) {
            mgv_spxq_xzgg_dialog_xgg1.setVisibility(View.VISIBLE);
        } else {
            mgv_spxq_xzgg_dialog_xgg1.setVisibility(View.GONE);
        }
        mgv_spxq_xzgg_dialog_xgg1.setAdapter(myAdapterXgg1);
    }

    private class MyAdapterXgg1 extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<String> arrlist;

        public MyAdapterXgg1(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arrlist = new ArrayList<String>();


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
                view = inflater.inflate(R.layout.xgg_item, null);
            }
            TextView tv_xgg_item = view.findViewById(R.id.tv_xgg_item);
            tv_xgg_item.setText(arrlist.get(position));
            tv_xgg_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sGg1_id = tv_spxq_xzgg_dialog_xgg1.getText().toString() + ":" + arrlist.get(position);
                    sGg1 = tv_spxq_xzgg_dialog_xgg1.getText().toString() + ":" + arrlist.get(position);
                    spgg();
                    notifyDataSetChanged();
                }
            });
            if (sGg1_id.equals(tv_spxq_xzgg_dialog_xgg1.getText().toString() + ":" + arrlist.get(position))) {
                tv_xgg_item.setBackgroundResource(R.drawable.bk_4350b6_6);
                tv_xgg_item.setTextColor(tv_xgg_item.getResources().getColor(R.color.lan));
            } else {
                tv_xgg_item.setBackgroundResource(R.drawable.bj_f4f4f4_6);
                tv_xgg_item.setTextColor(tv_xgg_item.getResources().getColor(R.color.qian_hei));
            }

            return view;
        }
    }

    /**
     * 选规格2
     */
    private void setGridViewXgg2(ArrayList<String> mylist) {
        MyAdapterXgg2 myAdapterXgg2 = new MyAdapterXgg2(Fjsc_SpxqActivity.this);
        myAdapterXgg2.arrlist = mylist;
        if (mylist.size() > 0) {
            mgv_spxq_xzgg_dialog_xgg2.setVisibility(View.VISIBLE);
        } else {
            mgv_spxq_xzgg_dialog_xgg2.setVisibility(View.GONE);
        }
        mgv_spxq_xzgg_dialog_xgg2.setAdapter(myAdapterXgg2);
    }

    private class MyAdapterXgg2 extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<String> arrlist;


        public MyAdapterXgg2(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arrlist = new ArrayList<String>();


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
                view = inflater.inflate(R.layout.xgg_item, null);
            }
            TextView tv_xgg_item = view.findViewById(R.id.tv_xgg_item);
            tv_xgg_item.setText(arrlist.get(position));
            tv_xgg_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sGg2_id = tv_spxq_xzgg_dialog_xgg2.getText().toString() + ":" + arrlist.get(position);
                    sGg2 = tv_spxq_xzgg_dialog_xgg2.getText().toString() + ":" + arrlist.get(position);
                    if (!sGg1_id.equals("")) {
                        spgg();
                        notifyDataSetChanged();
                    }

                }
            });
            if (sGg2_id.equals(tv_spxq_xzgg_dialog_xgg2.getText().toString() + ":" + arrlist.get(position))) {
                tv_xgg_item.setBackgroundResource(R.drawable.bk_4350b6_6);
                tv_xgg_item.setTextColor(tv_xgg_item.getResources().getColor(R.color.lan));
            } else {
                tv_xgg_item.setBackgroundResource(R.drawable.bj_f4f4f4_6);
                tv_xgg_item.setTextColor(tv_xgg_item.getResources().getColor(R.color.qian_hei));
            }
            return view;
        }
    }

    /**
     * 选规格3
     */
    private void setGridViewXgg3(ArrayList<String> mylist) {
        MyAdapterXgg3 myAdapterXgg3 = new MyAdapterXgg3(Fjsc_SpxqActivity.this);
        myAdapterXgg3.arrlist = mylist;
        if (mylist.size() > 0) {
            mgv_spxq_xzgg_dialog_xgg3.setVisibility(View.VISIBLE);
        } else {
            mgv_spxq_xzgg_dialog_xgg3.setVisibility(View.GONE);
        }
        mgv_spxq_xzgg_dialog_xgg3.setAdapter(myAdapterXgg3);
    }

    private class MyAdapterXgg3 extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<String> arrlist;


        public MyAdapterXgg3(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arrlist = new ArrayList<String>();
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
                view = inflater.inflate(R.layout.xgg_item, null);
            }
            TextView tv_xgg_item = view.findViewById(R.id.tv_xgg_item);
            tv_xgg_item.setText(arrlist.get(position));
            tv_xgg_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sGg3_id = tv_spxq_xzgg_dialog_xgg3.getText().toString() + ":" + arrlist.get(position);
                    sGg3 = tv_spxq_xzgg_dialog_xgg3.getText().toString() + ":" + arrlist.get(position);
                    if (!sGg2_id.equals("")) {
                        spgg();
                        notifyDataSetChanged();
                    }
                }
            });
            if (sGg3_id.equals(tv_spxq_xzgg_dialog_xgg3.getText().toString() + ":" + arrlist.get(position))) {
                tv_xgg_item.setBackgroundResource(R.drawable.bk_4350b6_6);
                tv_xgg_item.setTextColor(tv_xgg_item.getResources().getColor(R.color.lan));
            } else {
                tv_xgg_item.setBackgroundResource(R.drawable.bj_f4f4f4_6);
                tv_xgg_item.setTextColor(tv_xgg_item.getResources().getColor(R.color.qian_hei));
            }
            return view;
        }
    }

    private void spgg() {
        sGg = "";
        sGg_id = "";
        sItem_Id = "";
        if (mgv_spxq_xzgg_dialog_xgg1.getVisibility() == View.VISIBLE) {
            sGg = sGg1;
            sGg_id = sGg1_id;
            if (mgv_spxq_xzgg_dialog_xgg2.getVisibility() == View.VISIBLE) {
                sGg = sGg + "+" + sGg2;
                sGg_id = sGg_id + "," + sGg2_id;
                if (mgv_spxq_xzgg_dialog_xgg3.getVisibility() == View.VISIBLE) {
                    sGg = sGg + "+" + sGg3;
                    sGg_id = sGg_id + "," + sGg3_id;
                }
            }
        }
        if (mylistGuiGe.size() > 0) {
            String[] key = mylistGuiGe.get(0).get("guige").split(",");
            String[] Gg_id = sGg_id.split(",");
            if (key.length == Gg_id.length) {
                gugequzhi();
            }
        }
    }

    private void gugequzhi() {
        for (int i = 0; i < mylistGuiGe.size(); i++) {
            if (sGg_id.equals(mylistGuiGe.get(i).get("guige"))) {
                sItem_Id = mylistGuiGe.get(i).get("id");
                tv_spxq_xzgg_dialog_shop_price.setText(mylistGuiGe.get(i).get("jiage"));
              /*  tv_spxq_shop_price.setText(mylistGuiGe.get(i).get("jiage"));
                tv_spxq_xzgg_dialog_storecount.setText("库存" + mylistGuiGe.get(i).get("kucun") + "件");
                tv_spxq_xzgg_dialog_name.setText(mylistGuiGe.get(i).get("guige"));
                //tv_spxq_xgg.setText(mylistGuiGe.get(i).get("guige"));
                sItem_gg_name = mylistGuiGe.get(i).get("guige");*/
            }
        }
        if (sItem_Id.equals("")) {
            tv_spxq_xzgg_dialog_shop_price.setText("0");
       /*     tv_spxq_shop_price.setText(resultshop_price1);
            tv_spxq_xzgg_dialog_storecount.setText("库存0件");
            tv_spxq_xzgg_dialog_name.setText(sGg);*/
            Hint("当前规格库存为0", HintDialog.WARM);
            bt_spxq_xzgg_dialog_xgg_qr.setBackgroundResource(R.drawable.bj_30000000_19);
            bt_spxq_xzgg_dialog_xgg_qr.setEnabled(false);
        } else {
            bt_spxq_xzgg_dialog_xgg_qr.setBackgroundResource(R.drawable.bj_ff4350b6_19);
            bt_spxq_xzgg_dialog_xgg_qr.setEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // dialogin("");
        sGood_id = getIntent().getStringExtra("good_id");
        sCartlist();
        sRecommended();
        // refreshData();//刷新数据
    }

    /**
     * 方法名：sGooddetail()
     * 功  能：商品详情
     * 参  数：appId
     * good_id--商品id
     */
    private void sGooddetail() {
        hideDialogin();
        dialogin("");
        String url = Api.sUrl + Api.sGooddetail;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("good_id", sGood_id);
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
                            myAdapterGwc = new MyAdapterGwc(Fjsc_SpxqActivity.this);
                            if (resultCode > 0) {
                                JSONObject jsonObjectdata = new JSONObject(sDate.toString()).getJSONObject("data");
                                JSONObject JSONObjectdetail = jsonObjectdata.getJSONObject("detail");
                                String name = JSONObjectdetail.getString("name");
                                String buy_count = JSONObjectdetail.getString("buy_count");
                                String price = JSONObjectdetail.getString("price");
                                String intro = JSONObjectdetail.getString("intro");
                                tv_fjsc_spxq_hine.setText(name);
                                tv_fjsc_spxq_name.setText(name);
                                tv_fjsc_spxq_buy_count.setText("月销" + buy_count);
                                tv_fjsc_spxq_price.setText("￥" + price);
                                tv_fjsc_spxq_intro.setText(Html.fromHtml(intro));

                                jsonObjectGuige_xp = jsonObjectdata.getJSONObject("guige");

                                newgoodguige = new ArrayList<HashMap<String, String>>();

                                JSONArray resultLunimg = jsonObjectdata.getJSONArray("lunimg");
                                list_path = new ArrayList<>();
                                ArrayList<String> list_title = new ArrayList<>();
                                for (int i = 0; i < resultLunimg.length(); i++) {
                                    JSONObject jsonObject0 = resultLunimg.getJSONObject(i);
                                    list_path.add( jsonObject0.getString("url"));
                                    list_title.add("");
                                }
                                //设置样式，里面有很多种样式可以自己都看看效果
                                banner_fjsc_spjj.setBannerStyle(BannerConfig.NUM_INDICATOR);
                                //设置图片加载器
                                banner_fjsc_spjj.setImageLoader(new MyLoader());
                                //设置轮播的动画效果,里面有很多种特效,可以都看看效果。
                                banner_fjsc_spjj.setBannerAnimation(Transformer.Default);
                                //轮播图片的文字
                                //  banner.setBannerTitles(list_title);
                                //设置轮播间隔时间
                                banner_fjsc_spjj.setDelayTime(3000);
                                //设置是否为自动轮播，默认是true
                                banner_fjsc_spjj.isAutoPlay(true);
                                //设置指示器的位置，小点点，居中显示
                                banner_fjsc_spjj.setIndicatorGravity(BannerConfig.CENTER);
                                //设置图片加载地址
                                banner_fjsc_spjj.setImages(list_path)
                                        //轮播图的监听
                                        .setOnBannerListener(Fjsc_SpxqActivity.this)
                                        //开始调用的方法，启动轮播图。
                                        .start();

                                JSONArray resultgoodguige = jsonObjectdata.getJSONArray("newgoodguige");
                                for (int c = 0; c < resultgoodguige.length(); c++) {
                                    JSONObject jsonObject0 = resultgoodguige.getJSONObject(c);
                                    String resultId = jsonObject0.getString("id");
                                    String resultGuige = jsonObject0.getString("guige");
                                    String resultJiage = jsonObject0.getString("jiage");
                                    String resultKucun = jsonObject0.getString("kucun");
                                    HashMap<String, String> map1 = new HashMap<String, String>();
                                    map1.put("id", resultId);
                                    map1.put("guige", resultGuige);
                                    map1.put("jiage", resultJiage);
                                    map1.put("kucun", resultKucun);
                                    newgoodguige.add(map1);
                                }
                                if (newgoodguige.size() == 1) {
                                    iv_fjsc_spxq_jia.setVisibility(View.VISIBLE);
                                    tv_fjsc_spxq_xgg.setVisibility(View.GONE);
                                    sItem_Id = newgoodguige.get(0).get("id");
                                } else {
                                    iv_fjsc_spxq_jia.setVisibility(View.GONE);
                                    tv_fjsc_spxq_xgg.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (JSONException e) {
                            hideDialogin();
                            e.printStackTrace();
                        }
                        Log.d(TAG, "response -> " + response.toString());

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

    /**
     * 轮播监听
     *
     * @param position
     */
    @Override
    public void OnBannerClick(int position) {
        //  Toast.makeText(this, "你点了第" + (position + 1) + "张轮播图", Toast.LENGTH_SHORT).show();
        ImagePagerActivity.startImagePagerActivity(Fjsc_SpxqActivity.this, list_path, 0, new ImagePagerActivity
                .ImageSize(500, 500));
    }

    /**
     * 网络加载图片
     * 使用了Glide图片加载框架
     */
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context.getApplicationContext())
                    .load( Api.sUrl+(String) path)
                    .into(imageView);
        }
    }

    /**
     * 选规格
     */
    public void show() {
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
       /* lp.width = (int) (display.getWidth())-500;//设置宽度
        lp.height = (int) (display.getHeight()) - 1000;//设置高度
        dialog.getWindow().setAttributes(lp);*/
        dialog.show();

    }


    public void show_gwc() {
        dialog_gwc = new Dialog(this, R.style.ActionSheetDialogStyle);
        View inflate_gwc = LayoutInflater.from(this).inflate(R.layout.fjsc_dpsp_xlgwc_dialog, null);
        mgv_fjsc_dpsp_xlgwc = inflate_gwc.findViewById(R.id.mgv_fjsc_dpsp_xlgwc);
        LinearLayout ll_fjsc_dpsp_xlgwc_dialog = inflate_gwc.findViewById(R.id.ll_fjsc_dpsp_xlgwc_dialog);
        iv_fjsc_dpsp_xlgwc_gwc = inflate_gwc.findViewById(R.id.iv_fjsc_dpsp_xlgwc_gwc);
        tv_fjsc_dpsp_xlgwc_num = inflate_gwc.findViewById(R.id.tv_fjsc_dpsp_xlgwc_num);
        tv_fjsc_dpsp_xlgwc_price = inflate_gwc.findViewById(R.id.tv_fjsc_dpsp_xlgwc_price);
        tv_fjsc_dpsp_xlgwc_qjs = inflate_gwc.findViewById(R.id.tv_fjsc_dpsp_xlgwc_qjs);
        TextView tv_fjsc_dpsp_xlgwc_qkgwc = inflate_gwc.findViewById(R.id.tv_fjsc_dpsp_xlgwc_qkgwc);
        dialog_gwc.setContentView(inflate_gwc);
        Window dialogWindow = dialog_gwc.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
/*        lp.y = 0;*/
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
        ll_fjsc_dpsp_xlgwc_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_gwc.dismiss();
            }
        });
        tv_fjsc_dpsp_xlgwc_qjs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_fjsc_dpsp_xlgwc_qjs.getText().toString().equals("去结算")) {
                    Intent intent = new Intent(Fjsc_SpxqActivity.this, Fjsc_QrddActivity.class);
                    Fjsc_QrddActivity.sId = sShangjia_id;
                    Fjsc_QrddActivity.iDz = 0;
                    startActivity(intent);
                }
            }
        });
        tv_fjsc_dpsp_xlgwc_qkgwc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cart_id = "";
                for (int i = 0; myAdapterGwc.arrmylist.size() > i; i++) {
                    if (cart_id.equals("")) {
                        cart_id = myAdapterGwc.arrmylist.get(i).get("ItemId");
                    } else {
                        cart_id = cart_id + "-" + myAdapterGwc.arrmylist.get(i).get("ItemId");
                    }
                }
                dialogin("");
                sCartdel(cart_id);
            }
        });
        tv_fjsc_dpsp_xlgwc_qjs.setText(tv_fjsc_spxq_uptosend.getText().toString());
    }


    private void initialize() {
        dialog = new Dialog(Fjsc_SpxqActivity.this, R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(Fjsc_SpxqActivity.this).inflate(R.layout.fjsc_spxq_xzgg_dialog, null);
        iv_spxq_xzgg_dialog_image = inflate.findViewById(R.id.iv_spxq_xzgg_dialog_image);
        tv_spxq_xzgg_dialog_shop_price = inflate.findViewById(R.id.tv_spxq_xzgg_dialog_shop_price);
        tv_spxq_xzgg_dialog_xgg1 = inflate.findViewById(R.id.tv_spxq_xzgg_dialog_xgg1);
        tv_spxq_xzgg_dialog_xgg2 = inflate.findViewById(R.id.tv_spxq_xzgg_dialog_xgg2);
        tv_spxq_xzgg_dialog_xgg3 = inflate.findViewById(R.id.tv_spxq_xzgg_dialog_xgg3);
        tv_fjsc_spxq_xzgg_dialog_name = inflate.findViewById(R.id.tv_fjsc_spxq_xzgg_dialog_name);
        mgv_spxq_xzgg_dialog_xgg1 = inflate.findViewById(R.id.mgv_spxq_xzgg_dialog_xgg1);
        mgv_spxq_xzgg_dialog_xgg2 = inflate.findViewById(R.id.mgv_spxq_xzgg_dialog_xgg2);
        mgv_spxq_xzgg_dialog_xgg3 = inflate.findViewById(R.id.mgv_spxq_xzgg_dialog_xgg3);
        tv_spxq_xzgg_dialog_xgg1.setVisibility(View.GONE);
        tv_spxq_xzgg_dialog_xgg2.setVisibility(View.GONE);
        tv_spxq_xzgg_dialog_xgg3.setVisibility(View.GONE);
        mgv_spxq_xzgg_dialog_xgg1.setVisibility(View.GONE);
        mgv_spxq_xzgg_dialog_xgg2.setVisibility(View.GONE);
        mgv_spxq_xzgg_dialog_xgg3.setVisibility(View.GONE);
        bt_spxq_xzgg_dialog_xgg_qr = inflate.findViewById(R.id.bt_spxq_xzgg_dialog_xgg_qr);
        bt_spxq_xzgg_dialog_xgg_qr.setBackgroundResource(R.drawable.bj_30000000_19);
        bt_spxq_xzgg_dialog_xgg_qr.setEnabled(false);

        bt_spxq_xzgg_dialog_xgg_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if (!sItem_gg_name.equals("")) {
                dialog.dismiss();
                sCartadd(sGood_id, sItem_Id);
            }
        });
    }

    /**
     * 方法名：sCartadd()
     * 功  能：添加购物车
     * 参  数：appId
     */
    private void sCartadd(String good_id, String guige_id) {
        String url = Api.sUrl + Api.sCartadd;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("good_id", good_id);
        params.put("user_id", sUser_id);
        params.put("guige_id", guige_id);
        params.put("goods_num", "1");
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

                                Hint(resultMsg, HintDialog.SUCCESS);
                                dialogin("");
                                sCartlist();
                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
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

    /**
     * 方法名：sCartedit()
     * 功  能：编辑购物车数量
     * 参  数：appId
     */
    private void sCartedit(String cart_id, String goods_num) {
        String url = Api.sUrl + Api.sCartedit;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("cart_id", cart_id);
        params.put("goods_num", goods_num);
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
                                Hint(resultMsg, HintDialog.SUCCESS);
                                dialogin("");
                                sCartlist();
                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
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

    /**
     * 方法名：sCartdel()
     * 功  能：删除购物车
     * 参  数：appId
     */
    private void sCartdel(String cart_id) {
        String url = Api.sUrl + Api.sCartdel;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("cart_id", cart_id);
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
                                Hint(resultMsg, HintDialog.SUCCESS);
                                dialogin("");
                                sCartlist();
                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
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


    /**
     * 方法名：sCartlist()
     * 功  能：购物车列表
     * 参  数：appId
     * shangjia_id--商家ID
     */
    private void sCartlist() {
        String url = Api.sUrl + Api.sCartlist;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("shangjia_id", sShangjia_id);
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
                            double price = 0;
                            int num = 0;
                            myAdapterGwc = new MyAdapterGwc(Fjsc_SpxqActivity.this);
                            if (resultCode > 0) {
                                JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                                ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < resultJsonArray.length(); i++) {
                                    JSONObject jsonObject = resultJsonArray.getJSONObject(i);
                                    JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                                    for (int a = 0; a < jsonArrayList.length(); a++) {
                                        jsonObject = jsonArrayList.getJSONObject(a);
                                        String resultId = jsonObject.getString("id");
                                        String resultGoodsId = jsonObject.getString("good_id");
                                        String resultGoodsName = jsonObject.getString("good_name");
                                        String resultGoodsPrice = jsonObject.getString("goods_price");
                                        String resultGoodsNum = jsonObject.getString("goods_num");
                                        String resultSpecKeyName = jsonObject.getString("guige");
                                        String resultItemId = jsonObject.getString("item_id");
                                        HashMap<String, String> map = new HashMap<String, String>();
                                        map.put("ItemId", resultId);
                                        map.put("ItemGoodsId", resultGoodsId);
                                        map.put("ItemGoodsName", resultGoodsName);
                                        map.put("ItemGoodsPrice", resultGoodsPrice);
                                        map.put("ItemGoodsNum", resultGoodsNum);
                                        map.put("ItemSpecKeyName", resultSpecKeyName);
                                        map.put("ItemItemId", resultItemId);
                                        num += Integer.valueOf(resultGoodsNum);
                                        // price += Double.parseDouble(resultGoodsPrice) * Integer.valueOf(resultGoodsNum);
                                        price = Jjcc.add(price, Jjcc.mul(Double.parseDouble(resultGoodsPrice), Integer.valueOf(resultGoodsNum)));
                                        mylist.add(map);
                                    }
                                }
                                tv_fjsc_spxp_gwc_price.setText("￥" + price);
                                tv_fjsc_dpsp_xlgwc_price.setText("￥" + price);
                                tv_fjsc_dpsp_xlgwc_num.setText("(共" + num + "件商品)");
                                myAdapterGwc.arrmylist = mylist;
                                mgv_fjsc_dpsp_xlgwc.setAdapter(myAdapterGwc);
                            } else {
                                dialog_gwc.dismiss();
                                tv_fjsc_spxp_gwc_price.setText("未选购商品");
                                // Hint(resultMsg, HintDialog.ERROR);
                                mgv_fjsc_dpsp_xlgwc.setAdapter(myAdapterGwc);
                            }
                            myAdapterGwc.notifyDataSetChanged();
                            if (num > 0) {
                                badgeView.setTargetView(iv_fjsc_spxp_gwc);
                                badgeView.setBadgeGravity(Gravity.RIGHT);
                                badgeView.setBadgeCount(num);
                                badgeView1.setTargetView(iv_fjsc_dpsp_xlgwc_gwc);
                                badgeView1.setBadgeGravity(Gravity.RIGHT);
                                badgeView1.setBadgeCount(num);
                            }else {
                                badgeView.setVisibility(View.GONE);
                                badgeView1.setVisibility(View.GONE);
                            }
                            if (price >= Double.parseDouble(Fjsc_DpxqActivity.sUptosend)) {
                                if (price == 0) {
                                    tv_fjsc_spxq_uptosend.setText("0元起送");
                                    tv_fjsc_spxq_uptosend.setBackgroundResource(R.mipmap.bg_gouwuche2);
                                    tv_fjsc_dpsp_xlgwc_qjs.setText("0元起送");
                                    tv_fjsc_dpsp_xlgwc_qjs.setBackgroundResource(R.mipmap.bg_gouwuche2);
                                } else {
                                    tv_fjsc_spxq_uptosend.setText("去结算");
                                    tv_fjsc_spxq_uptosend.setBackgroundResource(R.mipmap.bg_gouwuche2_current_3x);
                                    tv_fjsc_dpsp_xlgwc_qjs.setText("去结算");
                                    tv_fjsc_dpsp_xlgwc_qjs.setBackgroundResource(R.mipmap.bg_gouwuche2_current_3x);
                                }
                            } else {
                                double dQiSong = 0;
                                dQiSong = Jjcc.sub(Double.parseDouble(Fjsc_DpxqActivity.sUptosend), price);
                                tv_fjsc_spxq_uptosend.setText("差￥" + dQiSong + "起送");
                                tv_fjsc_spxq_uptosend.setBackgroundResource(R.mipmap.bg_gouwuche2);
                                tv_fjsc_dpsp_xlgwc_qjs.setText("差￥" + dQiSong + "起送");
                                tv_fjsc_dpsp_xlgwc_qjs.setBackgroundResource(R.mipmap.bg_gouwuche2);
                            }
                        } catch (JSONException e) {
                            hideDialogin();
                            e.printStackTrace();
                        }
                        Log.d(TAG, "response -> " + response.toString());
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


    /**
     * 方法名：sRecommended()
     * 功  能：商品推荐接口
     * 参  数：appId
     * shangjia_id--商家ID
     */
    private void sRecommended() {
        hideDialogin();
        dialogin("");
        String url = Api.sUrl + Api.sRecommended;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("good_id", sGood_id);
        params.put("shangjia_id", sShangjia_id);
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
                            MyAdapter myAdapter = new MyAdapter(Fjsc_SpxqActivity.this);
                            List<Fjsc_TuijianData> mList = new ArrayList<>();
                            myAdapterGwc = new MyAdapterGwc(Fjsc_SpxqActivity.this);
                            if (resultCode > 0) {
                                JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                                ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < resultJsonArray.length(); i++) {
                                    JSONObject jsonObject = resultJsonArray.getJSONObject(i);
                                    Fjsc_TuijianData model = new Fjsc_TuijianData();
                                    String id = jsonObject.getString("id");
                                    String shangjia_id = jsonObject.getString("shangjia_id");
                                    String name = jsonObject.getString("name");
                                    String logo = jsonObject.getString("logo");
                                    String buy_count = jsonObject.getString("buy_count");
                                    String price = jsonObject.getString("price");
                                    JSONObject jsonObjectGuige = jsonObject.getJSONObject("guige");
                                    model.id = id;
                                    model.shangjia_id = shangjia_id;
                                    model.name = name;
                                    model.logo = logo;
                                    model.buy_count = buy_count;
                                    model.price = price;
                                    model.guige = jsonObjectGuige;
                                    ArrayList<HashMap<String, String>> newgoodguige = new ArrayList<HashMap<String, String>>();
                                    JSONArray resultgoodguige = jsonObject.getJSONArray("newgoodguige");
                                    for (int c = 0; c < resultgoodguige.length(); c++) {
                                        JSONObject jsonObject0 = resultgoodguige.getJSONObject(c);
                                        String resultId = jsonObject0.getString("id");
                                        String resultGuige = jsonObject0.getString("guige");
                                        String resultJiage = jsonObject0.getString("jiage");
                                        String resultKucun = jsonObject0.getString("kucun");
                                        HashMap<String, String> map1 = new HashMap<String, String>();
                                        map1.put("id", resultId);
                                        map1.put("guige", resultGuige);
                                        map1.put("jiage", resultJiage);
                                        map1.put("kucun", resultKucun);
                                        newgoodguige.add(map1);
                                        model.newgoodguige.add(map1);
                                    }
                                    mList.add(model);
                                }
                                myAdapter.mList = mList;
                                mgv_fjsc_spxq_tj.setAdapter(myAdapter);
                            } else {

                            }
                        } catch (JSONException e) {
                            hideDialogin();
                            e.printStackTrace();
                        }
                        Log.d(TAG, "response -> " + response.toString());

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

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater1;
        public List<Fjsc_TuijianData> mList;


        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater1 = LayoutInflater.from(context);
            mList = new ArrayList<>();

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mList.size();
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
                view = inflater1.inflate(R.layout.scroll_right_fjsc, null);
            }
            LinearLayout ll_you = view.findViewById(R.id.ll_you);
            ImageView right_image = view.findViewById(R.id.right_image);
            TextView right_text = view.findViewById(R.id.right_text);
            TextView right_buy_count = view.findViewById(R.id.right_buy_count);
            TextView right_price = view.findViewById(R.id.right_price);
            ImageView right_add = view.findViewById(R.id.right_add);
            TextView right_guge = view.findViewById(R.id.right_guge);
            if (mList.get(position).newgoodguige.size() == 1) {
                right_add.setVisibility(View.VISIBLE);
                right_guge.setVisibility(View.GONE);
            } else {
                right_add.setVisibility(View.GONE);
                right_guge.setVisibility(View.VISIBLE);
            }
            right_text.setText(mList.get(position).name);
            right_buy_count.setText("月销" + mList.get(position).buy_count);
            right_price.setText("￥" + mList.get(position).price);

            Glide.with(Fjsc_SpxqActivity.this).load( Api.sUrl+ mList.get(position).logo)
                    .asBitmap()
                    .dontAnimate()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .into(right_image);
            right_guge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        sGg1_id = "";
                        sGg2_id = "";
                        sGg3_id = "";
                        mylistGuiGe = new ArrayList<>();
                        mylistGuiGe = mList.get(position).newgoodguige;
                        tv_spxq_xzgg_dialog_xgg1.setText("");
                        tv_spxq_xzgg_dialog_xgg2.setText("");
                        tv_spxq_xzgg_dialog_xgg3.setText("");
                        bt_spxq_xzgg_dialog_xgg_qr.setBackgroundResource(R.drawable.bj_30000000_19);
                        bt_spxq_xzgg_dialog_xgg_qr.setEnabled(false);
                        tv_fjsc_spxq_xzgg_dialog_name.setText(tv_fjsc_spxq_hine.getText().toString());
                        JSONObject jsonObjectGuige = mList.get(position).guige;
                        Iterator<String> it = jsonObjectGuige.keys();
                        while (it.hasNext()) {
                            ArrayList<String> mylist = new ArrayList<>();
                            // 获得key
                            String key = it.next();

                            JSONArray jsonObjectkey = null;

                            jsonObjectkey = jsonObjectGuige.getJSONArray(key);

                            for (int b = 0; b < jsonObjectkey.length(); b++) {
                                String imge = jsonObjectkey.get(b).toString();
                                mylist.add(imge);
                            }
                            if (tv_spxq_xzgg_dialog_xgg1.getText().toString().equals("")) {
                                tv_spxq_xzgg_dialog_xgg1.setVisibility(View.VISIBLE);
                                tv_spxq_xzgg_dialog_xgg1.setText(key);
                                setGridViewXgg1(mylist);
                            } else if (tv_spxq_xzgg_dialog_xgg2.getText().toString().equals("")) {
                                tv_spxq_xzgg_dialog_xgg2.setVisibility(View.VISIBLE);
                                tv_spxq_xzgg_dialog_xgg2.setText(key);
                                setGridViewXgg2(mylist);
                            } else if (tv_spxq_xzgg_dialog_xgg3.getText().toString().equals("")) {
                                tv_spxq_xzgg_dialog_xgg3.setVisibility(View.VISIBLE);
                                tv_spxq_xzgg_dialog_xgg3.setText(key);
                                setGridViewXgg3(mylist);
                            }

                        }
                        show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            right_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogin("");
                    sCartadd(mList.get(position).id, mList.get(position).newgoodguige.get(0).get("id"));
                }
            });
            ll_you.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Fjsc_SpxqActivity.this, Fjsc_SpxqActivity.class);
                    intent.putExtra("good_id", mList.get(position).id);
                    intent.putExtra("shangjia_id", mList.get(position).shangjia_id);
                    //sGood_id = mList.get(position).id;
                    startActivity(intent);
                }
            });
            return view;
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

    private class MyAdapterGwc extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater1;

        public ArrayList<HashMap<String, String>> arrmylist;
        public ArrayList<String> arrItem;


        public MyAdapterGwc(Context context) {
            super();
            this.context = context;
            inflater1 = LayoutInflater.from(context);
            arrmylist = new ArrayList<HashMap<String, String>>();
            arrItem = new ArrayList<String>();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arrmylist.size();
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
                view = inflater1.inflate(R.layout.fjsc_dpsp_xlgwc_item, null);
            }
            TextView tv_fjsc_dpsp_xlgwc_name = view.findViewById(R.id.tv_fjsc_dpsp_xlgwc_name);
            TextView tv_fjsc_dpsp_xlgwc_spec_key_name = view.findViewById(R.id.tv_fjsc_dpsp_xlgwc_spec_key_name);
            TextView tv_fjsc_dpsp_xlgwc_price = view.findViewById(R.id.tv_fjsc_dpsp_xlgwc_price);
            TextView tv_fjsc_dpsp_xlgwc_num = view.findViewById(R.id.tv_fjsc_dpsp_xlgwc_num);
            ImageView iv_fjsc_dpsp_xlgwc_jian = view.findViewById(R.id.iv_fjsc_dpsp_xlgwc_jian);
            ImageView iv_fjsc_dpsp_xlgwc_jia = view.findViewById(R.id.iv_fjsc_dpsp_xlgwc_jia);
            tv_fjsc_dpsp_xlgwc_name.setText(arrmylist.get(position).get("ItemGoodsName"));
            tv_fjsc_dpsp_xlgwc_spec_key_name.setText(arrmylist.get(position).get("ItemSpecKeyName"));
            tv_fjsc_dpsp_xlgwc_price.setText(arrmylist.get(position).get("ItemGoodsPrice"));
            tv_fjsc_dpsp_xlgwc_num.setText(arrmylist.get(position).get("ItemGoodsNum"));
            iv_fjsc_dpsp_xlgwc_jian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (Integer.valueOf(arrmylist.get(position).get("ItemGoodsNum")) > 1) {
                        dialogin("");
                        sCartedit(arrmylist.get(position).get("ItemId"), String.valueOf(Integer.valueOf(arrmylist.get(position).get("ItemGoodsNum")) - 1));
                    } else {
                        dialogin("");
                        sCartdel(arrmylist.get(position).get("ItemId"));
                    }
                }
            });
            iv_fjsc_dpsp_xlgwc_jia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogin("");
                    sCartedit(arrmylist.get(position).get("ItemId"), String.valueOf(Integer.valueOf(arrmylist.get(position).get("ItemGoodsNum")) + 1));


                }
            });
            return view;
        }
    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(Fjsc_SpxqActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(Fjsc_SpxqActivity.this, R.style.dialog, sHint, type, true).show();
    }
}

