package com.example.administrator.yunyue.fjsc_activity;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.BottomScrollView;
import com.example.administrator.yunyue.Jjcc;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.erci.activity.CwhyActivity;
import com.example.administrator.yunyue.image.ImagePagerActivity;
import com.example.administrator.yunyue.sc_activity.SpxqActivity;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.tu_model.ListPinglun;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.administrator.yunyue.widget.BadgeView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;


public class Fjsc_DpxqActivity extends AppCompatActivity {
    private static final String TAG = Fjsc_DpxqActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_fjsc_dpxq_back;
    /**
     * 商家ID
     */
    private String sShangjia_id = "";
    /**
     * 商家名称
     */
    private TextView tv_fjsc_dpxq_shangjianame;
    /**
     * 商家logo
     */
    private ImageView iv_fjsc_dpxq_logo;
    /**
     * 商家公告
     */
    private TextView tv_fjsc_dpxq_notice;
    /**
     * 起送价格
     */
    private TextView tv_fjsc_dpxp_uptosend;
    /**
     * 配送时间
     */
    private TextView tv_fjsc_dpxq_duration;

    private RecyclerView recLeft;
    private RecyclerView recRight;
    private TextView rightTitle;

    private List<String> left;
    private List<ScrollBean_fjsc> right;
    private ScrollLeftAdapter leftAdapter;
    private ScrollRightAdapter_fjsc rightAdapter;
    //右侧title在数据中所对应的position集合
    private List<Integer> tPosition = new ArrayList<>();
    private Context mContext;
    //title的高度
    private int tHeight;
    //记录右侧当前可见的第一个item的position
    private int first = 0;
    private GridLayoutManager rightManager;
    private BottomScrollView bsv_fjsc_dpxq;
    private float mLastY;

    private Dialog dialog, dialog_gwc;
    private View inflate;

    //选规格规格类型
    private TextView tv_spxq_xzgg_dialog_xgg1, tv_spxq_xzgg_dialog_xgg2, tv_spxq_xzgg_dialog_xgg3, tv_fjsc_spxq_xzgg_dialog_name;
    private MyGridView mgv_spxq_xzgg_dialog_xgg1, mgv_spxq_xzgg_dialog_xgg2, mgv_spxq_xzgg_dialog_xgg3;


    private TextView tv_spxq_xzgg_dialog_shop_price;
    private ImageView iv_spxq_xzgg_dialog_image;

    /**
     * 规格确认
     */
    private Button bt_spxq_xzgg_dialog_xgg_qr;

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
    /**
     * 规格id
     */
    private String sGg_id = "";
    private String sItem_Id = "";
    private String sItem_gg_name = "";

    private ArrayList<HashMap<String, String>> mylistGuiGe;
    private SharedPreferences pref;
    private String sUser_id;
    private String sIs_vip = "";
    /**
     * 商品ID
     */
    private String sGoods_id = "";

    /**
     * 起送价格
     */
    public static String sUptosend = "";
    /**
     * 购物车
     */
    private MyGridView mgv_fjsc_dpsp_xlgwc;
    private TextView tv_fjsc_dpsp_xlgwc_num;
    private TextView tv_fjsc_dpsp_xlgwc_price;
    private TextView tv_fjsc_dpsp_xlgwc_qjs;
    private MyAdapterGwc myAdapterGwc;

    private TextView tv_fjsc_dpsp_gwc_price;
    private LinearLayout ll_fjsc_dpsp_gwc;
    /**
     * 商家
     */
    private TextView tv_fjsc_dpxq_sj;
    private TextView tv_fjsc_dpxq_top;
    private ScrollView sv_fjsc_sj;
    private LinearLayout ll_fjsc_dpxq_pj;
    private ImageView iv_fjsc_dpxq_sj, iv_fjsc_dpxq_sp;
    /**
     * 商品
     */
    private TextView tv_fjsc_dpxq_sp;
    /**
     * 评价
     */
    private TextView tv_fjsc_dpxq_pj;
    private ImageView iv_fjsc_dpxq_pj;
    /**
     * 商家，商家名称
     */
    private TextView tv_fjsc_dpxq_fj_shangjianame;
    /**
     * 商家电话
     */
    private TextView tv_fjsc_dpxq_fj_lianxiphone;
    /**
     * 商家品类
     */
    private TextView tv_fjsc_dpxq_fj_type_name;
    /**
     * 配送时间
     */
    private TextView tv_fjsc_dpxq_fj_business_hours;
    /**
     * 安全档案
     */
    private LinearLayout ll_fjsc_dpxq_aqda;
    /**
     * 购物车
     */
    private LinearLayout ll_fjsc_dpxq_gwc;
    int iPage = 1;

    /**
     * 全部评论列表
     */
    private PullToRefreshGridView pull_refresh_grid_xpsq_pj;
    /**
     * 全部评论列表
     */
    private List<ListPinglun> pl_List = new ArrayList<>();

    private LinearLayout ll_pj_kong;

    private MyAdapterPj myAdapterPj;
    RequestQueue queue = null;
    /**
     * 商家地址
     */
    private TextView tv_fjsc_dpxq_fj_address;
    /**
     * 商家信息图
     */
    private MyGridView mgv_fjsc_dpxq_fjxx;
    private ImageView iv_fjsc_dpsp_gwc, iv_fjsc_dpsp_xlgwc_gwc;
    BadgeView badgeView;
    BadgeView badgeView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_fjsc__dpxq);
        badgeView = new BadgeView(Fjsc_DpxqActivity.this);
        badgeView1 = new BadgeView(Fjsc_DpxqActivity.this);
        queue = Volley.newRequestQueue(Fjsc_DpxqActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        sIs_vip = pref.getString("is_vip", "");
        mContext = this;
        sShangjia_id = getIntent().getStringExtra("shangjia_id");
        tv_fjsc_dpxq_shangjianame = findViewById(R.id.tv_fjsc_dpxq_shangjianame);
        iv_fjsc_dpxq_logo = findViewById(R.id.iv_fjsc_dpxq_logo);
        tv_fjsc_dpxq_notice = findViewById(R.id.tv_fjsc_dpxq_notice);
        tv_fjsc_dpxp_uptosend = findViewById(R.id.tv_fjsc_dpxp_uptosend);
        tv_fjsc_dpxq_duration = findViewById(R.id.tv_fjsc_dpxq_duration);
        ll_fjsc_dpxq_back = findViewById(R.id.ll_fjsc_dpxq_back);
        pull_refresh_grid_xpsq_pj = findViewById(R.id.pull_refresh_grid_xpsq_pj);
        pull_refresh_grid_xpsq_pj.setMode(PullToRefreshBase.Mode.BOTH);
        mylistGuiGe = new ArrayList<>();
      /*  bsv_fjsc_dpxq = findViewById(R.id.bsv_fjsc_dpxq);
        bsv_fjsc_dpxq.requestDisallowInterceptTouchEvent(false);*/
        iv_fjsc_dpsp_gwc = findViewById(R.id.iv_fjsc_dpsp_gwc);
        tv_fjsc_dpsp_gwc_price = findViewById(R.id.tv_fjsc_dpsp_gwc_price);
        ll_pj_kong = findViewById(R.id.ll_pj_kong);

        tv_fjsc_dpxq_fj_shangjianame = findViewById(R.id.tv_fjsc_dpxq_fj_shangjianame);
        tv_fjsc_dpxq_fj_lianxiphone = findViewById(R.id.tv_fjsc_dpxq_fj_lianxiphone);
        tv_fjsc_dpxq_fj_type_name = findViewById(R.id.tv_fjsc_dpxq_fj_type_name);
        tv_fjsc_dpxq_fj_business_hours = findViewById(R.id.tv_fjsc_dpxq_fj_business_hours);
        ll_fjsc_dpxq_aqda = findViewById(R.id.ll_fjsc_dpxq_aqda);
        tv_fjsc_dpxq_fj_address = findViewById(R.id.tv_fjsc_dpxq_fj_address);
        mgv_fjsc_dpxq_fjxx = findViewById(R.id.mgv_fjsc_dpxq_fjxx);
        ll_fjsc_dpxq_aqda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Fjsc_DpxqActivity.this, Fjsc_SpaqdaActivity.class);
                intent.putExtra("shangjia_id", sShangjia_id);
                startActivity(intent);
            }
        });

        ll_fjsc_dpxq_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initialize();
        sGoodslist();
        show_gwc();
        sShangjiadetail();
        recLeft = findViewById(R.id.rec_left);
        recRight = findViewById(R.id.rec_right);
        rightTitle = findViewById(R.id.right_title);
        //  initData();
        left = new ArrayList<>();
        right = new ArrayList<>();
        ll_fjsc_dpsp_gwc = findViewById(R.id.ll_fjsc_dpsp_gwc);
        ll_fjsc_dpsp_gwc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_fjsc_dpsp_gwc_price.getText().toString().equals("未选购商品")) {
                } else {
                    dialogin("");
                    sCartlist();
                    dialog_gwc.show();
                }
            }
        });
        tv_fjsc_dpxp_uptosend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_fjsc_dpsp_xlgwc_qjs.getText().toString().equals("去结算")) {
                    Intent intent = new Intent(Fjsc_DpxqActivity.this, Fjsc_QrddActivity.class);
                    Fjsc_QrddActivity.sId = sShangjia_id;
                    Fjsc_QrddActivity.iDz = 0;
                    startActivity(intent);
                }
            }
        });

        tv_fjsc_dpxq_sj = findViewById(R.id.tv_fjsc_dpxq_sj);
        tv_fjsc_dpxq_top = findViewById(R.id.tv_fjsc_dpxq_top);
        sv_fjsc_sj = findViewById(R.id.sv_fjsc_sj);
        tv_fjsc_dpxq_sp = findViewById(R.id.tv_fjsc_dpxq_sp);
        iv_fjsc_dpxq_sj = findViewById(R.id.iv_fjsc_dpxq_sj);
        iv_fjsc_dpxq_sp = findViewById(R.id.iv_fjsc_dpxq_sp);
        tv_fjsc_dpxq_pj = findViewById(R.id.tv_fjsc_dpxq_pj);
        iv_fjsc_dpxq_pj = findViewById(R.id.iv_fjsc_dpxq_pj);
        ll_fjsc_dpxq_pj = findViewById(R.id.ll_fjsc_dpxq_pj);
        ll_fjsc_dpxq_gwc = findViewById(R.id.ll_fjsc_dpxq_gwc);
        tv_fjsc_dpxq_sj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_fjsc_dpxq_sj.setTextColor(tv_fjsc_dpxq_sj.getResources().getColor(R.color.hui4a4a4a));
                tv_fjsc_dpxq_sp.setTextColor(tv_fjsc_dpxq_sp.getResources().getColor(R.color.hui9b9b9b));
                tv_fjsc_dpxq_top.setVisibility(View.GONE);
                sv_fjsc_sj.setVisibility(View.VISIBLE);
                iv_fjsc_dpxq_sj.setVisibility(View.VISIBLE);
                iv_fjsc_dpxq_sp.setVisibility(View.GONE);
                iv_fjsc_dpxq_pj.setVisibility(View.GONE);
                ll_fjsc_dpxq_pj.setVisibility(View.GONE);
                ll_fjsc_dpxq_gwc.setVisibility(View.GONE);
                tv_fjsc_dpxq_pj.setTextColor(tv_fjsc_dpxq_pj.getResources().getColor(R.color.hui9b9b9b));
            }
        });
        tv_fjsc_dpxq_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_fjsc_dpxq_top.setVisibility(View.VISIBLE);
                sv_fjsc_sj.setVisibility(View.GONE);
                tv_fjsc_dpxq_sp.setTextColor(tv_fjsc_dpxq_sp.getResources().getColor(R.color.hui4a4a4a));
                tv_fjsc_dpxq_sj.setTextColor(tv_fjsc_dpxq_sj.getResources().getColor(R.color.hui9b9b9b));
                iv_fjsc_dpxq_sj.setVisibility(View.GONE);
                iv_fjsc_dpxq_pj.setVisibility(View.GONE);
                iv_fjsc_dpxq_sp.setVisibility(View.VISIBLE);
                ll_fjsc_dpxq_pj.setVisibility(View.GONE);
                ll_fjsc_dpxq_gwc.setVisibility(View.VISIBLE);
                tv_fjsc_dpxq_pj.setTextColor(tv_fjsc_dpxq_pj.getResources().getColor(R.color.hui9b9b9b));
            }
        });
        tv_fjsc_dpxq_pj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sPinglunlist();
                tv_fjsc_dpxq_top.setVisibility(View.GONE);
                sv_fjsc_sj.setVisibility(View.GONE);
                iv_fjsc_dpxq_sj.setVisibility(View.GONE);
                iv_fjsc_dpxq_pj.setVisibility(View.VISIBLE);
                iv_fjsc_dpxq_sp.setVisibility(View.GONE);
                ll_fjsc_dpxq_gwc.setVisibility(View.GONE);
                ll_fjsc_dpxq_pj.setVisibility(View.VISIBLE);
                tv_fjsc_dpxq_sp.setTextColor(tv_fjsc_dpxq_sp.getResources().getColor(R.color.hui9b9b9b));
                tv_fjsc_dpxq_sj.setTextColor(tv_fjsc_dpxq_sj.getResources().getColor(R.color.hui9b9b9b));
                tv_fjsc_dpxq_pj.setTextColor(tv_fjsc_dpxq_pj.getResources().getColor(R.color.hui4a4a4a));
            }
        });
        pull_refresh_grid_xpsq_pj.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            // 下拉刷新加载
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                Log.e("TAG", "onPullDownToRefresh"); // Do work to
                // 刷新时间
                String label = DateUtils.formatDateTime(
                        getApplicationContext(),
                        System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                // AsyncTask异步交互加载数据
                // new GetDataTask().execute(URL + z);
                iPage = 1;
                sPinglunlist();

            /*        qingqiu(sId, iPage, et_fenlei_dialog_start.getText().toString(), et_fenlei_dialog_end.getText().toString(),
                        Order, sKeywosrd, sBrand_Id);*/
            }

            // 上拉加载跟多
            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                Log.e("TAG", "onPullUpToRefresh"); // Do work to refresh
        /*        qingqiu(sId, iPage, et_fenlei_dialog_start.getText().toString(), et_fenlei_dialog_end.getText().toString(),
                        Order, sKeywosrd, sBrand_Id);*/
                iPage += 1;
                sPinglunlist();
                // 页数
                //   p = p + 1;
                // AsyncTask异步交互加载数据
                // new GetDataTask2().execute(URL + p);
            }
        });

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
                    Intent intent = new Intent(Fjsc_DpxqActivity.this, Fjsc_QrddActivity.class);
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
                //  delete(cart_id);
                sCartdel(cart_id);
            }
        });
        tv_fjsc_dpsp_xlgwc_qjs.setText(tv_fjsc_dpxp_uptosend.getText().toString());
    }


    private void initialize() {
        dialog = new Dialog(Fjsc_DpxqActivity.this, R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(Fjsc_DpxqActivity.this).inflate(R.layout.fjsc_spxq_xzgg_dialog, null);
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
                sCartadd(sGoods_id, sItem_Id);
            }
        });
    }


    /**
     * 方法名：sShangjiadetail()
     * 功  能：商家详情
     * 参  数：appId
     * shangjia_id--商家ID
     */
    private void sShangjiadetail() {

        String url = Api.sUrl + Api.sShangjiadetail;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
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
                            if (resultCode > 0) {
                                JSONObject jsonObjectdata = new JSONObject(sDate.toString()).getJSONObject("data");
                                //商家名称
                                String shangjianame = jsonObjectdata.getString("shangjianame");
                                tv_fjsc_dpxq_fj_shangjianame.setText(shangjianame);
                                //商家品类
                                String type_name = jsonObjectdata.getString("type_name");
                                tv_fjsc_dpxq_fj_type_name.setText(type_name);
                                //商家电话
                                String lianxiphone = jsonObjectdata.getString("lianxiphone");
                                tv_fjsc_dpxq_fj_lianxiphone.setText(lianxiphone);
                                //配送时间
                                String business_hours = jsonObjectdata.getString("business_hours");
                                tv_fjsc_dpxq_fj_business_hours.setText(business_hours);
                                //  String shangjianame = jsonObjectdata.getString("shangjianame");
                                //商家地址
                                String address = jsonObjectdata.getString("address");
                                tv_fjsc_dpxq_fj_address.setText(address);
                                JSONArray jsonArrayTypelist = jsonObjectdata.getJSONArray("service_name");
                                ArrayList<String> mylist_typelist = new ArrayList();
                                for (int i = 0; i < jsonArrayTypelist.length(); i++) {
                                    JSONObject jsonObject2 = (JSONObject) jsonArrayTypelist.opt(i);
                                    String ItemName = jsonObject2.getString("name");
                                    mylist_typelist.add(ItemName);
                                }
                                JSONArray jsonArrayLogo = jsonObjectdata.getJSONArray("logo");
                                ArrayList<String> mylist_Logo = new ArrayList();
                                for (int i = 0; i < jsonArrayLogo.length(); i++) {
                                    JSONObject jsonObject2 = (JSONObject) jsonArrayLogo.opt(i);
                                    String ItemInfo_Img = jsonObject2.getString("info_img");
                                    mylist_Logo.add(Api.sUrl + ItemInfo_Img);
                                }
                                MyAdapterSjt myAdapterSjt = new MyAdapterSjt(Fjsc_DpxqActivity.this);
                                myAdapterSjt.arrayList = mylist_Logo;
                                mgv_fjsc_dpxq_fjxx.setAdapter(myAdapterSjt);
                                // setGridView(mylist_typelist);
                                LinearLayout llGroup = (LinearLayout) findViewById(R.id.ll_fjsc_dpxq_sjfw);
                                //size:代码中获取到的图片数量
                                llGroup.removeAllViews();  //clear linearlayout
                                for (int i = 0; i < mylist_typelist.size(); i++) {
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    final TextView textView = new TextView(Fjsc_DpxqActivity.this);
                                    layoutParams.setMargins(20, 0, 0, 0);
                                    textView.setTextSize(12);
                                    textView.setTextColor(textView.getResources().getColor(R.color.hui9b9b9b));
                                    textView.setLayoutParams(layoutParams);  //设置图片宽高
                                    textView.setText(mylist_typelist.get(i));
                                    llGroup.addView(textView); //动态添加图片
                                }

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
     * 方法名：sGoodslist()
     * 功  能：商品列表
     * 参  数：appId
     * shangjia_id--商家ID
     * longitude--经度
     * latitude--纬度
     */
    private void sGoodslist() {
        hideDialogin();
        dialogin("");
        String url = Api.sUrl + Api.sGoodslist;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("shangjia_id", sShangjia_id);
        params.put("longitude", Fjsc_ShouyeActivity.sLongitude);
        params.put("latitude", Fjsc_ShouyeActivity.sLatitude);
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
                                JSONObject jsonObjectShangjialist = new JSONObject(sDate.toString()).getJSONObject("shangjialist");
                                //商家名称
                                String shangjianame = jsonObjectShangjialist.getString("shangjianame");
                                tv_fjsc_dpxq_shangjianame.setText(shangjianame);
                                //商家logo
                                String logo = jsonObjectShangjialist.getString("logo");
                                Glide.with(Fjsc_DpxqActivity.this).load( Api.sUrl+logo)
                                        .asBitmap()
                                        .dontAnimate()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .into(iv_fjsc_dpxq_logo);
                                //月售
                                String xiao_num = jsonObjectShangjialist.getString("xiao_num");
                                //商家公告l
                                String notice = jsonObjectShangjialist.getString("notice");
                                tv_fjsc_dpxq_notice.setText("公告：" + notice);
                                //起送价格
                                sUptosend = jsonObjectShangjialist.getString("uptosend");
                                tv_fjsc_dpxp_uptosend.setText("差￥" + sUptosend + "起送");
                                //配送时间
                                String duration = jsonObjectShangjialist.getString("duration");
                                tv_fjsc_dpxq_duration.setText("配送" + duration + "分钟");

                                left = new ArrayList<>();
                                right = new ArrayList<>();
                                JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                                /*   JSONArray jsonArrayData = jsonObjectShangjialist.getJSONArray("data");*/
                                for (int i = 0; i < resultJsonArray.length(); i++) {
                                    JSONObject jsonObjectdate = resultJsonArray.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("ItemId", jsonObjectdate.getString("id"));//id
                                    map.put("ItemName", jsonObjectdate.getString("name"));//
                                    left.add(jsonObjectdate.getString("name"));
                                    right.add(new ScrollBean_fjsc(true, jsonObjectdate.getString("name")));
                                    JSONArray resultJsonArray_type = jsonObjectdate.getJSONArray("good");
                                    for (int a = 0; a < resultJsonArray_type.length(); a++) {
                                        JSONObject jsonObjecttype = resultJsonArray_type.getJSONObject(a);
                                        String name = jsonObjecttype.getString("name");
                                        JSONObject jsonObjectGuige = jsonObjecttype.getJSONObject("guige");

                                        ArrayList<HashMap<String, String>> newgoodguige = new ArrayList<HashMap<String, String>>();
                                        JSONArray resultgoodguige = jsonObjecttype.getJSONArray("newgoodguige");
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
                                            sItem_Id = newgoodguige.get(0).get("id");
                                        }
                                        right.add(new ScrollBean_fjsc(new ScrollBean_fjsc.ScrollItemBean(jsonObjecttype.getString("id"),
                                                name, jsonObjecttype.getString("logo"), jsonObjecttype.getString("price"),
                                                jsonObjecttype.getString("buy_count"), newgoodguige, jsonObjectGuige, jsonObjectdate.getString("name"))));
                                    }
                                }
                                //rightAdapter.setNewData(right);
                                for (int i = 0; i < right.size(); i++) {
                                    if (right.get(i).isHeader) {
                                        //遍历右侧列表,判断如果是header,则将此header在右侧列表中所在的position添加到集合中
                                        tPosition.add(i);
                                    }
                                }
                                // setGridView(mylist_typelist);
                                initLeft();
                                initRight();
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
                            myAdapterGwc = new MyAdapterGwc(Fjsc_DpxqActivity.this);
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
                                tv_fjsc_dpsp_gwc_price.setText("￥" + price);
                                tv_fjsc_dpsp_xlgwc_price.setText("￥" + price);
                                tv_fjsc_dpsp_xlgwc_num.setText("(共" + num + "件商品)");
                                myAdapterGwc.arrmylist = mylist;
                                mgv_fjsc_dpsp_xlgwc.setAdapter(myAdapterGwc);
                            } else {
                                dialog_gwc.dismiss();
                                tv_fjsc_dpsp_gwc_price.setText("未选购商品");
                                // Hint(resultMsg, HintDialog.ERROR);
                                mgv_fjsc_dpsp_xlgwc.setAdapter(myAdapterGwc);
                            }
                            myAdapterGwc.notifyDataSetChanged();
                            if (num > 0) {
                                badgeView.setTargetView(iv_fjsc_dpsp_gwc);
                                badgeView.setBadgeGravity(Gravity.RIGHT);
                                badgeView.setBadgeCount(num);

                                badgeView1.setTargetView(iv_fjsc_dpsp_xlgwc_gwc);
                                badgeView1.setBadgeGravity(Gravity.RIGHT);
                                badgeView1.setBadgeCount(num);
                            } else {
                                badgeView.setVisibility(View.GONE);
                                badgeView1.setVisibility(View.GONE);
                            }

                            if (price >= Double.parseDouble(sUptosend)) {
                                if (price == 0) {
                                    tv_fjsc_dpxp_uptosend.setText("0元起送");
                                    tv_fjsc_dpxp_uptosend.setBackgroundResource(R.mipmap.bg_gouwuche2);
                                    tv_fjsc_dpsp_xlgwc_qjs.setText("0元起送");
                                    tv_fjsc_dpsp_xlgwc_qjs.setBackgroundResource(R.mipmap.bg_gouwuche2);
                                } else {
                                    tv_fjsc_dpxp_uptosend.setText("去结算");
                                    tv_fjsc_dpxp_uptosend.setBackgroundResource(R.mipmap.bg_gouwuche2_current_3x);
                                    tv_fjsc_dpsp_xlgwc_qjs.setText("去结算");
                                    tv_fjsc_dpsp_xlgwc_qjs.setBackgroundResource(R.mipmap.bg_gouwuche2_current_3x);
                                }
                            } else {
                                double dQiSong = 0;
                                dQiSong = Jjcc.sub(Double.parseDouble(sUptosend), price);
                                tv_fjsc_dpxp_uptosend.setText("差￥" + dQiSong + "起送");
                                tv_fjsc_dpxp_uptosend.setBackgroundResource(R.mipmap.bg_gouwuche2);
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
     * 方法名：sPinglunlist()
     * 功  能：商品评论列表
     * 参  数：appId
     * shangjia_id--商家ID
     */
    private void sPinglunlist() {
        String url = Api.sUrl + Api.sPinglunlist;
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
                            if (resultCode > 0) {
                                if (iPage == 1) {
                                    pl_List = new ArrayList<>();
                                }
                                JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                                for (int i = 0; i < resultJsonArray.length(); i++) {
                                    JSONObject jsonObject = resultJsonArray.getJSONObject(i);
                                    //评论id
                                    String resultId = jsonObject.getString("id");
                                    //评论头像
                                    String resultUser_logo = jsonObject.getString("user_logo");
                                    //评论名称
                                    String resultUser_name = jsonObject.getString("user_name");
                                    //评论等级
                                    String resultFen = jsonObject.getString("fen");
                                    //评论时间
                                    String resultTime = jsonObject.getString("time");
                                    //评论内容
                                    String resultText = jsonObject.getString("text");

                                    JSONArray jsonArrayImglist = jsonObject.getJSONArray("listurl");
                                    ListPinglun model = new ListPinglun();

                                    for (int a = 0; a < jsonArrayImglist.length(); a++) {
                                        String imge = jsonArrayImglist.get(a).toString();
                                        model.listurl.add(imge);
                                    }
                                    // model4.urlList.add(mUrls[i]);
                                    model.id = resultId;
                                    model.text = resultText;
                                    model.fen = resultFen;
                                    model.user_logo = resultUser_logo;
                                    model.user_name = resultUser_name;
                                    model.time = resultTime;
                                    pl_List.add(model);
                                }
                                ll_pj_kong.setVisibility(View.GONE);
                                if (iPage == 1) {
                                    if (pl_List.size() == 0) {
                                        ll_pj_kong.setVisibility(View.VISIBLE);
                                    }
                                    setGridViewPj();
                                } else {
                                    setGridViewPj1();
                                }
                                if (resultJsonArray.length() == 0) {
                                    if (iPage > 0) {
                                        iPage -= 1;
                                    }
                                }
                            } else {
                                if (pl_List.size() == 0) {
                                    ll_pj_kong.setVisibility(View.VISIBLE);
                                }

                                if (iPage > 0) {
                                    iPage -= 1;
                                }
                                Hint(resultMsg, HintDialog.ERROR);
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


    private void setGridViewPj() {

        myAdapterPj = new MyAdapterPj(this);
        pull_refresh_grid_xpsq_pj.setAdapter(myAdapterPj);
        // 刷新适配器
        myAdapterPj.notifyDataSetChanged();
        // 关闭刷新下拉
        pull_refresh_grid_xpsq_pj.onRefreshComplete();

    }

    private void setGridViewPj1() {
        myAdapterPj.notifyDataSetChanged();
        // Call onRefreshComplete when the list has been refreshed.
        // 关闭上拉加载
        pull_refresh_grid_xpsq_pj.onRefreshComplete();
    }

    /**
     * 评价
     */
    private class MyAdapterPj extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;


        public MyAdapterPj(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return pl_List.size();
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
                view = inflater.inflate(R.layout.spxq_pj_item, null);
            }
            LinearLayout ll_spxq_pj_item_image = view.findViewById(R.id.ll_spxq_pj_item_image);
            ImageView iv_spxq_pj_item_head_pic = view.findViewById(R.id.iv_spxq_pj_item_head_pic);
            TextView tv_spxq_pj_item_username = view.findViewById(R.id.tv_spxq_pj_item_username);
            TextView tv_spxq_pj_item_add_time = view.findViewById(R.id.tv_spxq_pj_item_add_time);
            ImageView iv_spxq_pj_item_goods_rank = view.findViewById(R.id.iv_spxq_pj_item_goods_rank);
            ImageView iv_spxq_pj_item_goods_rank1 = view.findViewById(R.id.iv_spxq_pj_item_goods_rank1);
            ImageView iv_spxq_pj_item_goods_rank2 = view.findViewById(R.id.iv_spxq_pj_item_goods_rank2);
            ImageView iv_spxq_pj_item_goods_rank3 = view.findViewById(R.id.iv_spxq_pj_item_goods_rank3);
            ImageView iv_spxq_pj_item_goods_rank4 = view.findViewById(R.id.iv_spxq_pj_item_goods_rank4);
            TextView tv_spxq_pj_item_content = view.findViewById(R.id.tv_spxq_pj_item_content);
            TextView tv_spxq_pj_item_zan_num = view.findViewById(R.id.tv_spxq_pj_item_zan_num);
            ImageView iv_spxq_pj_item_img1 = view.findViewById(R.id.iv_spxq_pj_item_img1);
            ImageView iv_spxq_pj_item_img2 = view.findViewById(R.id.iv_spxq_pj_item_img2);
            ImageView iv_spxq_pj_item_img3 = view.findViewById(R.id.iv_spxq_pj_item_img3);

            tv_spxq_pj_item_username.setText(pl_List.get(position).user_name);
            tv_spxq_pj_item_add_time.setText(pl_List.get(position).time);
            tv_spxq_pj_item_content.setText(pl_List.get(position).text);

            if (pl_List.get(position).listurl.size() == 0) {
                ll_spxq_pj_item_image.setVisibility(View.GONE);
            } else {
                ll_spxq_pj_item_image.setVisibility(View.VISIBLE);
            }

            if (pl_List.get(position).listurl.size() == 1) {
                Glide.with(Fjsc_DpxqActivity.this)
                        .load( Api.sUrl+pl_List.get(position).listurl.get(0))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .into(iv_spxq_pj_item_img1);
            } else if (pl_List.get(position).listurl.size() == 2) {
                Glide.with(Fjsc_DpxqActivity.this)
                        .load( Api.sUrl+pl_List.get(position).listurl.get(0))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .into(iv_spxq_pj_item_img1);
                Glide.with(Fjsc_DpxqActivity.this)
                        .load( Api.sUrl+pl_List.get(position).listurl.get(1))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .into(iv_spxq_pj_item_img2);
            } else if (pl_List.get(position).listurl.size() == 3) {
                Glide.with(Fjsc_DpxqActivity.this)
                        .load( Api.sUrl+pl_List.get(position).listurl.get(0))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .into(iv_spxq_pj_item_img1);
                Glide.with(Fjsc_DpxqActivity.this)
                        .load( Api.sUrl+pl_List.get(position).listurl.get(1))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .into(iv_spxq_pj_item_img2);
                Glide.with(Fjsc_DpxqActivity.this)
                        .load( Api.sUrl+pl_List.get(position).listurl.get(2))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .into(iv_spxq_pj_item_img3);
            }

            Glide.with(Fjsc_DpxqActivity.this)
                    .load( Api.sUrl+pl_List.get(position).user_logo)
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_spxq_pj_item_head_pic);
            if (Integer.valueOf(pl_List.get(position).fen) / 2 == 0) {
                iv_spxq_pj_item_goods_rank.setImageResource(R.drawable.star_normal);
                iv_spxq_pj_item_goods_rank1.setImageResource(R.drawable.star_normal);
                iv_spxq_pj_item_goods_rank2.setImageResource(R.drawable.star_normal);
                iv_spxq_pj_item_goods_rank3.setImageResource(R.drawable.star_normal);
                iv_spxq_pj_item_goods_rank4.setImageResource(R.drawable.star_normal);
            } else if (Integer.valueOf(pl_List.get(position).fen) / 2 == 1) {
                iv_spxq_pj_item_goods_rank.setImageResource(R.drawable.star_selected);
                iv_spxq_pj_item_goods_rank1.setImageResource(R.drawable.star_normal);
                iv_spxq_pj_item_goods_rank2.setImageResource(R.drawable.star_normal);
                iv_spxq_pj_item_goods_rank3.setImageResource(R.drawable.star_normal);
                iv_spxq_pj_item_goods_rank4.setImageResource(R.drawable.star_normal);
            } else if (Integer.valueOf(pl_List.get(position).fen) / 2 == 2) {
                iv_spxq_pj_item_goods_rank.setImageResource(R.drawable.star_selected);
                iv_spxq_pj_item_goods_rank1.setImageResource(R.drawable.star_selected);
                iv_spxq_pj_item_goods_rank2.setImageResource(R.drawable.star_normal);
                iv_spxq_pj_item_goods_rank3.setImageResource(R.drawable.star_normal);
                iv_spxq_pj_item_goods_rank4.setImageResource(R.drawable.star_normal);
            } else if (Integer.valueOf(pl_List.get(position).fen) / 2 == 3) {
                iv_spxq_pj_item_goods_rank.setImageResource(R.drawable.star_selected);
                iv_spxq_pj_item_goods_rank1.setImageResource(R.drawable.star_selected);
                iv_spxq_pj_item_goods_rank2.setImageResource(R.drawable.star_selected);
                iv_spxq_pj_item_goods_rank3.setImageResource(R.drawable.star_normal);
                iv_spxq_pj_item_goods_rank4.setImageResource(R.drawable.star_normal);
            } else if (Integer.valueOf(pl_List.get(position).fen) / 2 == 4) {
                iv_spxq_pj_item_goods_rank.setImageResource(R.drawable.star_selected);
                iv_spxq_pj_item_goods_rank1.setImageResource(R.drawable.star_selected);
                iv_spxq_pj_item_goods_rank2.setImageResource(R.drawable.star_selected);
                iv_spxq_pj_item_goods_rank3.setImageResource(R.drawable.star_selected);
                iv_spxq_pj_item_goods_rank4.setImageResource(R.drawable.star_normal);
            } else if (Integer.valueOf(pl_List.get(position).fen) / 2 == 5) {
                iv_spxq_pj_item_goods_rank.setImageResource(R.drawable.star_selected);
                iv_spxq_pj_item_goods_rank1.setImageResource(R.drawable.star_selected);
                iv_spxq_pj_item_goods_rank2.setImageResource(R.drawable.star_selected);
                iv_spxq_pj_item_goods_rank3.setImageResource(R.drawable.star_selected);
                iv_spxq_pj_item_goods_rank4.setImageResource(R.drawable.star_selected);
            }

            return view;
        }
    }

    /**
     * 商家信息
     */
    private class MyAdapterSjt extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private ArrayList<String> arrayList;


        public MyAdapterSjt(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arrayList = new ArrayList<>();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arrayList.size();
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
                view = inflater.inflate(R.layout.fjsc_dpxq_sjxx_item, null);
            }
            ImageView iv_fjsc_dpxq_sjxx_item = view.findViewById(R.id.iv_fjsc_dpxq_sjxx_item);

            Glide.with(Fjsc_DpxqActivity.this)
                    .load( Api.sUrl+arrayList.get(position))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_fjsc_dpxq_sjxx_item);
      /*      final List<String> list = new ArrayList<>();
            list.add(Api.sUrl + pref.getString("head_pic", ""));*/
            iv_fjsc_dpxq_sjxx_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImagePagerActivity.startImagePagerActivity(Fjsc_DpxqActivity.this, arrayList, 0, new ImagePagerActivity
                            .ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight()));
                }
            });

            return view;
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

    /**
     * 选规格1
     */
    private void setGridViewXgg1(ArrayList<String> mylist) {
        MyAdapterXgg1 myAdapterXgg1 = new MyAdapterXgg1(Fjsc_DpxqActivity.this);
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
        MyAdapterXgg2 myAdapterXgg2 = new MyAdapterXgg2(Fjsc_DpxqActivity.this);
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
        MyAdapterXgg3 myAdapterXgg3 = new MyAdapterXgg3(Fjsc_DpxqActivity.this);
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
        loadingDialog = new LoadingDialog(Fjsc_DpxqActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(Fjsc_DpxqActivity.this, R.style.dialog, sHint, type, true).show();
    }


    private void initRight() {

        rightManager = new GridLayoutManager(mContext, 2);

        if (rightAdapter == null) {
            rightAdapter = new ScrollRightAdapter_fjsc(R.layout.scroll_right_fjsc, R.layout.layout_right_title, null);
            recRight.setLayoutManager(rightManager);
            recRight.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    outRect.set(dpToPx(mContext, getDimens(mContext, R.dimen.dp3))
                            , 0
                            , dpToPx(mContext, getDimens(mContext, R.dimen.dp3))
                            , dpToPx(mContext, getDimens(mContext, R.dimen.dp3)));
                }
            });
            recRight.setAdapter(rightAdapter);
        } else {
            rightAdapter.notifyDataSetChanged();
        }

        rightAdapter.setNewData(right);

        //设置右侧初始title
        if (right.get(first).isHeader) {
            rightTitle.setText(right.get(first).header);
        }

        recRight.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获取右侧title的高度
                tHeight = rightTitle.getHeight();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //判断如果是header
                if (right.get(first).isHeader) {
                    //获取此组名item的view
                    View view = rightManager.findViewByPosition(first);
                    if (view != null) {
                        //如果此组名item顶部和父容器顶部距离大于等于title的高度,则设置偏移量
                        if (view.getTop() >= tHeight) {
                            rightTitle.setY(view.getTop() - tHeight);
                        } else {
                            //否则不设置
                            rightTitle.setY(0);
                        }
                    }
                }

                //因为每次滑动之后,右侧列表中可见的第一个item的position肯定会改变,并且右侧列表中可见的第一个item的position变换了之后,
                //才有可能改变右侧title的值,所以这个方法内的逻辑在右侧可见的第一个item的position改变之后一定会执行
                int firstPosition = rightManager.findFirstVisibleItemPosition();
                if (first != firstPosition && firstPosition >= 0) {
                    //给first赋值
                    first = firstPosition;
                    //不设置Y轴的偏移量

                    rightTitle.setY(0);

                    //判断如果右侧可见的第一个item是否是header,设置相应的值
                    if (right.get(first).isHeader) {
                        rightTitle.setText(right.get(first).header);
                    } else {
                        rightTitle.setText(right.get(first).t.getType());
                    }
                }

                //遍历左边列表,列表对应的内容等于右边的title,则设置左侧对应item高亮
                for (int i = 0; i < left.size(); i++) {
                    if (left.get(i).equals(rightTitle.getText().toString())) {
                        leftAdapter.selectItem(i);
                    }
                }

                //如果右边最后一个完全显示的item的position,等于bean中最后一条数据的position(也就是右侧列表拉到底了),
                //则设置左侧列表最后一条item高亮
                if (rightManager.findLastCompletelyVisibleItemPosition() == right.size() - 1) {
                    leftAdapter.selectItem(left.size() - 1);
                }
            }
        });
        rightAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    //点击左侧列表的相应item,右侧列表相应的title置顶显示
                    //(最后一组内容若不能填充右侧整个可见页面,则显示到右侧列表的最底端)
                    case R.id.ll_you:
                        ScrollBean_fjsc sbf = right.get(position);
                        ScrollBean_fjsc.ScrollItemBean tbf = sbf.t;
                        Intent intent = new Intent(Fjsc_DpxqActivity.this, Fjsc_SpxqActivity.class);
                        intent.putExtra("good_id", tbf.getId());
                        intent.putExtra("shangjia_id", sShangjia_id);
                        startActivity(intent);
                        break;
                    case R.id.right_add:

                            ScrollBean_fjsc s = right.get(position);
                            ScrollBean_fjsc.ScrollItemBean t = s.t;
                            t.getMylistGuiGe().get(0).get("id");
                            sCartadd(t.getId(), t.getMylistGuiGe().get(0).get("id"));

                        break;
                    case R.id.right_guge:

                            try {
                                ScrollBean_fjsc s_fjsc = right.get(position);
                                ScrollBean_fjsc.ScrollItemBean sitb = s_fjsc.t;
                                sGg1_id = "";
                                sGg2_id = "";
                                sGg3_id = "";
                                sGoods_id = sitb.getId();
                                mylistGuiGe = new ArrayList<>();
                                mylistGuiGe = sitb.getMylistGuiGe();
                                tv_spxq_xzgg_dialog_xgg1.setText("");
                                tv_spxq_xzgg_dialog_xgg2.setText("");
                                tv_spxq_xzgg_dialog_xgg3.setText("");
                                bt_spxq_xzgg_dialog_xgg_qr.setBackgroundResource(R.drawable.bj_30000000_19);
                                bt_spxq_xzgg_dialog_xgg_qr.setEnabled(false);
                                tv_fjsc_spxq_xzgg_dialog_name.setText(sitb.getText());
                                JSONObject jsonObjectGuige = sitb.getJsonObjectGuige();
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

                        break;
                }
            }
        });

    }

    private void initLeft() {
        if (leftAdapter == null) {
            leftAdapter = new ScrollLeftAdapter(R.layout.scroll_left, null);
            recLeft.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            recLeft.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
            recLeft.setAdapter(leftAdapter);
        } else {
            leftAdapter.notifyDataSetChanged();
        }

        leftAdapter.setNewData(left);

        leftAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    //点击左侧列表的相应item,右侧列表相应的title置顶显示
                    //(最后一组内容若不能填充右侧整个可见页面,则显示到右侧列表的最底端)
                    case R.id.item:
                        leftAdapter.selectItem(position);
                        rightManager.scrollToPositionWithOffset(tPosition.get(position), 0);
                        break;
                }
            }
        });
    }

    //获取数据(若请求服务端数据,请求到的列表需有序排列)

    /**
     * 获得资源 dimens (dp)
     *
     * @param context
     * @param id      资源id
     * @return
     */
    public float getDimens(Context context, int id) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float px = context.getResources().getDimension(id);
        return px / dm.density;
    }

    /**
     * dp转px
     *
     * @param context
     * @param dp
     * @return
     */
    public int dpToPx(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5f);
    }

}
