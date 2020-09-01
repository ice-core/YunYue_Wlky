package com.example.administrator.yunyue.sc_activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.MyImageView;
import com.example.administrator.yunyue.MyListView;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.data.TxddGridData;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.erci.activity.CwhyActivity;
import com.example.administrator.yunyue.sc.ObservableScrollView;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.tu_model.ListPinglun;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SpxqActivity extends AppCompatActivity implements OnBannerListener {

    private int num = 1;
    LayoutInflater mInflater;
    /**
     * banner图片
     */
    private ImageView iv_spxq_banner;
    /**
     * 返回
     */
    private ImageView iv_spxq_back1;
    private LinearLayout ll_spxq_back;
    /**
     * 宝贝评论标签
     */
    private TagFlowLayout mFlowLayout;
    private TagFlowLayout flowlayout_spxq_pj;

    /**
     * 宝贝评论列表
     */
    private GridView gv_spxq_bbpl;
    /**
     * 店铺推荐列表
     */
    private MyGridView mgv_chenpin;

    private int imageHeight;

    private FrameLayout fl_spxq_title;

    private ObservableScrollView sv_spxq_camera;
    /**
     * 购物车
     */
    private LinearLayout ll_spxq_gwc;

    /**
     * 商品、详情、评论
     */
    private LinearLayout ll_spxq_sxp;

    /**
     * 导航栏下划线
     */
    private TextView tv_xpxq_title_xh;


    /**
     * 立即购买
     */
    private TextView tv_spxq_ljgm;
    /**
     * 进店逛逛
     */
    private TextView tv_spxq_jdgg;
    /**
     * 商品名称
     */
    private TextView tv_spxq_goods_name;
    /**
     * 商品价格
     */
    private TextView tv_spxq_shop_price;

    /**
     * 店铺头像
     */
    private ImageView iv_spxq_img;

    /**
     * 店铺名称
     */
    private TextView tv_spxq_storename;

    /**
     * 宝贝描述
     */
    private TextView tv_spxq_goods_rank;
    /**
     * 卖家服务
     */
    private TextView tv_spxq_service_rank;
    /**
     * 物流服务
     */
    private TextView tv_spxq_deliver_rank;
    /**
     * 商家等级
     */
    private ImageView iv_spxq_scorc, iv_spxq_scorc1, iv_spxq_scorc2, iv_spxq_scorc3, iv_spxq_scorc4;
    private LinearLayout ll_spxq_pj_sxp;
    private TextView tv_spxq_pj_sxp, tv_spxq_pj_sxp_bj;
    private LinearLayout ll_spxq_xq_sxp;
    private TextView tv_spxq_xq_sxp, tv_spxq_xq_sxp_bj;
    private LinearLayout ll_spxq_pl;
    private LinearLayout ll_spxq_xq;
    private String sType = "";
    /**
     * 详情页面
     */
    private MyGridView mgv_spxq_xq;

    private Banner banner;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;
    private String user_id = "";
    private String sIs_vip = "";
    RequestQueue queue = null;
    private SharedPreferences pref;
    private String goods_id;
    private String data;
    /**
     * 店铺ID
     */
    private String resultRid = "";

    private Dialog dialog;
    private View inflate;
    private Dialog dialog_cs;

    /**
     * 选择规格
     */
    private TextView tv_spxq_xgg;
    /**
     * 参数
     */
    private TextView tv_spxq_cs;

    /**
     * 参数列表
     */
    private GridView gv_spxq_cs_dialog;

    /**
     * 服务
     */
    private TextView tv_spxq_fw;
    //商品主图
    private String resultOriginal_Img = "";
    //商品价格
    private String resultshop_price1 = "";
    //选规格规格类型
    private TextView tv_spxq_xzgg_dialog_xgg1, tv_spxq_xzgg_dialog_xgg2, tv_spxq_xzgg_dialog_xgg3;
    private MyGridView mgv_spxq_xzgg_dialog_xgg1, mgv_spxq_xzgg_dialog_xgg2, mgv_spxq_xzgg_dialog_xgg3;


    private TextView tv_spxq_xzgg_dialog_shop_price;
    private ImageView iv_spxq_xzgg_dialog_image;

    /**
     * 保障
     */
    private ArrayList<HashMap<String, String>> mylistbaozhang;
    /**
     * 参数
     */
    private ArrayList<HashMap<String, String>> mylistattr;
    /**
     * 弹出提示
     */
    private TextView tv_spxq_cs_dialog_bt;
    /**
     * 加入购物车
     */
    private TextView tv_spxq_jrgwc;
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


    /**
     * 保障
     */
    private ArrayList<HashMap<String, String>> mylistGuiGe;

    /**
     * 库存
     */
    private TextView tv_spxq_xzgg_dialog_storecount;
    /**
     * 规格
     */
    private TextView tv_spxq_xzgg_dialog_name;
    /**
     * 规格确认
     */
    private Button bt_spxq_xzgg_dialog_xgg_qr;

    /**
     * 规格确认
     * 0--未确认
     * 1--确认
     */
    private int is_ggqr = 0;

    /**
     * 月销量
     */
    private TextView tv_spxq_sales_sum;

    private List<TxddGridData> mList = new ArrayList<>();


    /**
     * 商品id
     */
    private String resultgoods_id = "";
    /**
     * 商品收藏
     */
    private LinearLayout ll_spxq_spsc;
    private TextView tv_spxq_spsc;
    private ImageView iv_spxq_spsc;
    /**
     * 联系客服
     */
    private LinearLayout ll_spxq_lxkf;

    /**
     * 商家电话
     */
    private String resultPhone = "";
    /**
     * 店铺关注
     */
    private LinearLayout ll_spxq_dpgz;
    private TextView tv_spxq_dpgz;
    private ImageView iv_spxq_dpgz;
    /**
     * 评论查看全部
     */
    private LinearLayout ll_pl_ckqb;

    /**
     * 商品查看全部
     */
    private LinearLayout ll_spxq_sp_ckqb;

    /**
     * 全部评论列表
     */
    private List<ListPinglun> pl_List = new ArrayList<>();

    /**
     * 全部评论列表
     */
    private PullToRefreshGridView pull_refresh_grid_xpsq_pj;
    int iPage = 1;
    private MyAdapterPj myAdapterPj;
    // private MyAdapterXgg myAdapterXgg;
    /**
     * 选规格弹框
     */
    private int gg_xsk = 0;
    /**
     * 评论次数
     */
    private TextView tv_spxq_pl_num;

    /**
     * 全部宝贝
     */
    private TextView tv_spxq_qbbb;

    int is_shouchang = 0;
    private LinearLayout ll_spxq_xq_ssp;
    private ImageView iv_spxq_xq_item, iv_spxq_xq_item1, iv_spxq_xq_item2;
    private LinearLayout ll_pj_kong;
    private MyListView mlv_spxq_xq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_spxq);
        queue = Volley.newRequestQueue(SpxqActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(SpxqActivity.this);
        boolean isRemember = pref.getBoolean("user", false);
        user_id = pref.getString("user_id", "");
        sIs_vip = pref.getString("is_vip", "");
        Intent intent = getIntent();
        data = intent.getStringExtra("data");
        goods_id = intent.getStringExtra("goods_id");
        //获取布局填充器,一会将tv.xml文件填充到标签内.
        mInflater = LayoutInflater.from(this);
        mFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);
        gv_spxq_bbpl = findViewById(R.id.gv_spxq_bbpl);
        mgv_chenpin = findViewById(R.id.mgv_chenpin);
        iv_spxq_back1 = findViewById(R.id.iv_spxq_back1);
        ll_spxq_back = findViewById(R.id.ll_spxq_back);
        iv_spxq_banner = findViewById(R.id.iv_spxq_banner);
        fl_spxq_title = findViewById(R.id.fl_spxq_title);
        sv_spxq_camera = findViewById(R.id.sv_spxq_camera);
        ll_spxq_gwc = findViewById(R.id.ll_spxq_gwc);
        ll_spxq_sxp = findViewById(R.id.ll_spxq_sxp);
        tv_xpxq_title_xh = findViewById(R.id.tv_xpxq_title_xh);
        flowlayout_spxq_pj = findViewById(R.id.flowlayout_spxq_pj);
        tv_spxq_pl_num = findViewById(R.id.tv_spxq_pl_num);
        tv_spxq_qbbb = findViewById(R.id.tv_spxq_qbbb);
        pull_refresh_grid_xpsq_pj = findViewById(R.id.pull_refresh_grid_xpsq_pj);
        pull_refresh_grid_xpsq_pj.setMode(PullToRefreshBase.Mode.BOTH);
        ll_spxq_pj_sxp = findViewById(R.id.ll_spxq_pj_sxp);
        tv_spxq_pj_sxp = findViewById(R.id.tv_spxq_pj_sxp);
        tv_spxq_pj_sxp_bj = findViewById(R.id.tv_spxq_pj_sxp_bj);
        ll_spxq_xq_sxp = findViewById(R.id.ll_spxq_xq_sxp);
        ll_spxq_xq_ssp = findViewById(R.id.ll_spxq_xq_ssp);
        tv_spxq_xq_sxp = findViewById(R.id.tv_spxq_xq_sxp);
        tv_spxq_xq_sxp_bj = findViewById(R.id.tv_spxq_xq_sxp_bj);
        ll_spxq_pl = findViewById(R.id.ll_spxq_pl);
        ll_spxq_xq = findViewById(R.id.ll_spxq_xq);
        tv_spxq_ljgm = findViewById(R.id.tv_spxq_ljgm);
        tv_spxq_jdgg = findViewById(R.id.tv_spxq_jdgg);
        mgv_spxq_xq = findViewById(R.id.mgv_spxq_xq);
        banner = findViewById(R.id.banner);
        iv_spxq_img = findViewById(R.id.iv_spxq_img);
        tv_spxq_storename = findViewById(R.id.tv_spxq_storename);
        tv_spxq_goods_rank = findViewById(R.id.tv_spxq_goods_rank);
        tv_spxq_service_rank = findViewById(R.id.tv_spxq_service_rank);
        tv_spxq_deliver_rank = findViewById(R.id.tv_spxq_deliver_rank);
        iv_spxq_scorc = findViewById(R.id.iv_spxq_scorc);
        iv_spxq_scorc1 = findViewById(R.id.iv_spxq_scorc1);
        iv_spxq_scorc2 = findViewById(R.id.iv_spxq_scorc2);
        iv_spxq_scorc3 = findViewById(R.id.iv_spxq_scorc3);
        iv_spxq_scorc4 = findViewById(R.id.iv_spxq_scorc4);
        tv_spxq_goods_name = findViewById(R.id.tv_spxq_goods_name);
        tv_spxq_shop_price = findViewById(R.id.tv_spxq_shop_price);
        tv_spxq_xgg = findViewById(R.id.tv_spxq_xgg);
        tv_spxq_cs = findViewById(R.id.tv_spxq_cs);
        tv_spxq_fw = findViewById(R.id.tv_spxq_fw);
        tv_spxq_jrgwc = findViewById(R.id.tv_spxq_jrgwc);
        tv_spxq_sales_sum = findViewById(R.id.tv_spxq_sales_sum);
        ll_spxq_spsc = findViewById(R.id.ll_spxq_spsc);
        iv_spxq_spsc = findViewById(R.id.iv_spxq_spsc);
        tv_spxq_spsc = findViewById(R.id.tv_spxq_spsc);
        ll_spxq_lxkf = findViewById(R.id.ll_spxq_lxkf);
        ll_spxq_dpgz = findViewById(R.id.ll_spxq_dpgz);
        tv_spxq_dpgz = findViewById(R.id.tv_spxq_dpgz);
        iv_spxq_dpgz = findViewById(R.id.iv_spxq_dpgz);
        ll_pl_ckqb = findViewById(R.id.ll_pl_ckqb);
        iv_spxq_xq_item = findViewById(R.id.iv_spxq_xq_item);
        iv_spxq_xq_item1 = findViewById(R.id.iv_spxq_xq_item1);
        iv_spxq_xq_item2 = findViewById(R.id.iv_spxq_xq_item2);
        ll_spxq_sp_ckqb = findViewById(R.id.ll_spxq_sp_ckqb);
        ll_pj_kong = findViewById(R.id.ll_pj_kong);
        mlv_spxq_xq = findViewById(R.id.mlv_spxq_xq);
        pl_List = new ArrayList<>();
        ll_pl_ckqb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qbpl();
            }
        });
        ll_spxq_sp_ckqb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SpxqActivity.this, DpzyActivity.class);
                intent.putExtra("id", resultRid);
                intent.putExtra("type", "全部");
                startActivity(intent);
            }
        });
        tv_spxq_qbbb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SpxqActivity.this, DpzyActivity.class);
                intent.putExtra("id", resultRid);
                intent.putExtra("type", "全部");
                startActivity(intent);
            }
        });

        iv_spxq_back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ll_spxq_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ll_spxq_gwc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        /*        Intent intent = new Intent(SpxqActivity.this, MainActivity.class);
                intent.putExtra("ID", "2");
                startActivity(intent);*/
                Intent intent = new Intent(SpxqActivity.this, GwcActivity.class);
                //  intent.putExtra("ID", "2");
                startActivity(intent);
            }
        });

        ll_spxq_pj_sxp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qbpl();

            }
        });
        ll_spxq_xq_sxp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sType = "xq";
                tv_spxq_pj_sxp.setTextColor(tv_spxq_pj_sxp.getResources().getColor(R.color.qian_hei));
                tv_spxq_pj_sxp_bj.setBackgroundColor(ContextCompat.getColor(SpxqActivity.this, R.color.touming));
                tv_spxq_xq_sxp.setTextColor(tv_spxq_xq_sxp.getResources().getColor(R.color.theme));
                tv_spxq_xq_sxp_bj.setBackgroundColor(ContextCompat.getColor(SpxqActivity.this, R.color.theme));
                ll_spxq_pl.setVisibility(View.GONE);
                ll_spxq_xq.setVisibility(View.VISIBLE);
                fl_spxq_title.measure(0, 0);
                final int height = fl_spxq_title.getMeasuredHeight();
                sv_spxq_camera.scrollTo(0, mgv_chenpin.getBottom() - height);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sv_spxq_camera.scrollTo(0, mgv_chenpin.getBottom() - height);
                    }
                }, 10);

            }
        });
        ll_spxq_xq_ssp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sType = "sp";
                ll_spxq_pl.setVisibility(View.GONE);
                ll_spxq_xq.setVisibility(View.VISIBLE);
                fl_spxq_title.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));//AGB由相关工具获得，或者美工提供
                ll_spxq_gwc.setVisibility(View.VISIBLE);
                ll_spxq_sxp.setVisibility(View.GONE);
                tv_xpxq_title_xh.setVisibility(View.GONE);
                iv_spxq_back1.setVisibility(View.GONE);
                ll_spxq_back.setVisibility(View.VISIBLE);

                sv_spxq_camera.scrollTo(0, 0);
            }
        });

        tv_spxq_jdgg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SpxqActivity.this, DpzyActivity.class);
                intent.putExtra("id", resultRid);
                startActivity(intent);
            }
        });
        initialize();
        initialize_cs();
        initListeners();
        data(data);

        tv_spxq_xgg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //    myAdapterXgg.notifyDataSetChanged();
                gg_xsk = 0;
                show();

            }
        });
        tv_spxq_jrgwc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                gg_xsk = 1;
                if (is_ggqr == 0) {
                    show();
                } else {
                    dialogin("");
                    gouwuche(goods_id, String.valueOf(num), sItem_Id);
                }
            }

        });
        tv_spxq_ljgm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gg_xsk = 2;
                if (is_ggqr == 0) {
                    show();
                } else {
                    txdd();
                }

            }
        });
        ll_spxq_spsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogin("");
                spsc();

            }
        });
        ll_spxq_lxkf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog(resultPhone);

            }
        });
        ll_spxq_dpgz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SpxqActivity.this, DpzyActivity.class);
                intent.putExtra("id", resultRid);
                startActivity(intent);
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
                pingjia(goods_id);

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
                pingjia(goods_id);
                // 页数
                //   p = p + 1;
                // AsyncTask异步交互加载数据
                // new GetDataTask2().execute(URL + p);
            }
        });
    }

    /**
     * 评论查看全部
     */
    private void qbpl() {
        sType = "pj";
        tv_spxq_pj_sxp.setTextColor(tv_spxq_pj_sxp.getResources().getColor(R.color.theme));
        tv_spxq_pj_sxp_bj.setBackgroundColor(ContextCompat.getColor(SpxqActivity.this, R.color.theme));
        tv_spxq_xq_sxp.setTextColor(tv_spxq_xq_sxp.getResources().getColor(R.color.qian_hei));
        tv_spxq_xq_sxp_bj.setBackgroundColor(ContextCompat.getColor(SpxqActivity.this, R.color.touming));
        ll_spxq_pl.setVisibility(View.VISIBLE);
        ll_spxq_xq.setVisibility(View.GONE);
        iPage = 1;
        pingjia(goods_id);
    }

    public void dialog(final String resultPhone) {
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

    private void txdd() {
        mList = new ArrayList<>();
        TxddGridData txddGridData = new TxddGridData();
        txddGridData.sName = tv_spxq_storename.getText().toString();
        txddGridData.sName_id = resultRid;
        txddGridData.sGg_id = sItem_Id;
        txddGridData.sNum = String.valueOf(num);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("ItemGood_logo", resultOriginal_Img);
        map.put("ItemGoods_Name", tv_spxq_goods_name.getText().toString());
        map.put("ItemGoods_Num", String.valueOf(num));
        map.put("ItemGoods_Price", tv_spxq_xzgg_dialog_shop_price.getText().toString());
        map.put("ItemGoods_Id", resultgoods_id);
        map.put("ItemGuige", tv_spxq_xzgg_dialog_name.getText().toString());
        txddGridData.mylist.add(map);
        mList.add(txddGridData);
        Intent intent = new Intent(SpxqActivity.this, TxddActivity.class);
        TxddActivity.mList = mList;
        TxddActivity.sActivity = "spxq";
        double zongjia = Double.parseDouble(tv_spxq_xzgg_dialog_shop_price.getText().toString());
        TxddActivity.sAmount = String.valueOf(zongjia * num);
        startActivity(intent);
    }

    /**
     * 加入购物车
     * goods_id-商品ID
     * goods_num-商品数量
     * item_id-商品规格ID
     * type-状态
     */
    private void gouwuche(String good_id, String goods_num, String guige_id) {
        String url = Api.sUrl + "Api/Order/cartadd/appId/" + Api.sApp_Id
                + "/user_id/" + user_id + "/good_id/" + good_id + "/goods_num/" + goods_num + "" + "/guige_id/" + guige_id;
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
                        dialog.dismiss();
                        num = 1;
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

    private void Xiangqing(final String pid) {
        String url = Api.sUrl + "Api/Good/gooddetail/appId/" + Api.sApp_Id
                + "/good_id/" + pid + "/user_id/" + user_id;
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
                        Intent intent1 = new Intent(SpxqActivity.this, SpxqActivity.class);
                        intent1.putExtra("goods_id", pid);
                        intent1.putExtra("data", sDate);
                        intent1.putExtra("activty", "main");
                        startActivity(intent1);
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

    /**
     * 商品收藏
     */
    private void spsc() {
        String url = Api.sUrl + "Api/Good/shouchang/appId/" + Api.sApp_Id
                + "/user_id/" + user_id + "/good_id/" + resultgoods_id;
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
                        if (is_shouchang == 0) {
                            is_shouchang = 1;
                            iv_spxq_spsc.setImageResource(R.mipmap.shoucang);
                            tv_spxq_spsc.setTextColor(tv_spxq_spsc.getResources().getColor(R.color.theme));
                        } else {
                            is_shouchang = 0;
                            iv_spxq_spsc.setImageResource(R.drawable.icon_shoucang_normal);
                            tv_spxq_spsc.setTextColor(tv_spxq_spsc.getResources().getColor(R.color.hei666666));
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


    private void pingjia(final String pid) {
        String url = Api.sUrl + "Api/Good/pinglunlist/appId/" + Api.sApp_Id
                + "/good_id/" + pid + "/page/" + iPage;
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
                        if (iPage == 1) {
                            pl_List = new ArrayList<>();
                        }
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
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

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(SpxqActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(SpxqActivity.this, R.style.dialog, sHint, type, true).show();
    }

    private void data(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String resultMsg = jsonObject.getString("msg");
            int resultCode = jsonObject.getInt("code");
            if (resultCode > 0) {
                String resultData = jsonObject.getString("data");
                JSONObject jsonObjectData = new JSONObject(resultData);

                /**
                 * 轮播图
                 * */
  /*              String resultgoods_images_list = jsonObjectData.getString("goods_images_list");
                JSONObject jsonObjectgoods_images_list = new JSONObject(resultgoods_images_list);*/
                JSONArray resultJsonArray = jsonObjectData.getJSONArray("lunimg");
                list_path = new ArrayList<>();
                list_title = new ArrayList<>();
                for (int i = 0; i < resultJsonArray.length(); i++) {
                    JSONObject jsonObject0 = resultJsonArray.getJSONObject(i);
                    list_path.add(jsonObject0.getString("url"));
                    list_title.add("");
                }
                //设置样式，里面有很多种样式可以自己都看看效果
                banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
                //设置图片加载器
                banner.setImageLoader(new MyLoader());
                //设置轮播的动画效果,里面有很多种特效,可以都看看效果。
                banner.setBannerAnimation(Transformer.Default);
                //轮播图片的文字
                //  banner.setBannerTitles(list_title);
                //设置轮播间隔时间
                banner.setDelayTime(3000);
                //设置是否为自动轮播，默认是true
                banner.isAutoPlay(true);
                //设置指示器的位置，小点点，居中显示
                banner.setIndicatorGravity(BannerConfig.CENTER);
                //设置图片加载地址
                banner.setImages(list_path)
                        //轮播图的监听
                        .setOnBannerListener(this)
                        //开始调用的方法，启动轮播图。
                        .start();


                /**
                 * 宝贝评论
                 * */
                ArrayList<HashMap<String, String>> mylistbbpl = new ArrayList<HashMap<String, String>>();
                JSONArray resultcommentList = jsonObjectData.getJSONArray("pinglun");
                for (int i = 0; i < resultcommentList.length(); i++) {
                    JSONObject jsonObject0 = resultcommentList.getJSONObject(i);
                    //评论id
                    String resultId = jsonObject0.getString("id");
                    //评论头像
                    String resultUser_Logo = jsonObject0.getString("user_logo");
                    //评论名称
                    String resultUser_Name = jsonObject0.getString("user_name");
                    //评论等级
                    String resultFen = jsonObject0.getString("fen");
                    //评论内容
                    String resultText = jsonObject0.getString("text");

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("id", resultId);
                    map.put("user_logo", resultUser_Logo);
                    map.put("user_name", resultUser_Name);
                    map.put("fen", resultFen);
                    map.put("text", resultText);

                    mylistbbpl.add(map);
                }
                setGridView(mylistbbpl);
                //是否关注
                is_shouchang = jsonObjectData.getInt("is_shouchang");
                if (is_shouchang == 1) {
                    iv_spxq_spsc.setImageResource(R.mipmap.shoucang);
                    tv_spxq_spsc.setTextColor(tv_spxq_spsc.getResources().getColor(R.color.theme));
                } else {
                    iv_spxq_spsc.setImageResource(R.drawable.icon_shoucang_normal);
                    tv_spxq_spsc.setTextColor(tv_spxq_spsc.getResources().getColor(R.color.hei666666));
                }

                String resultgoods = jsonObjectData.getString("detail");
                JSONObject jsonObjectgoods = new JSONObject(resultgoods);
                //商品id
                resultgoods_id = jsonObjectgoods.getString("id");
                //店铺Id
                resultRid = jsonObjectgoods.getString("shangjia_id");
                //商品名称
                String resultgoods_name = jsonObjectgoods.getString("name");
                tv_spxq_goods_name.setText(resultgoods_name);
                //商品价格
                // resultshop_price1 = jsonObjectgoods.getString("shop_price");
                resultshop_price1 = jsonObjectgoods.getString("price");
                //商品主图
                resultOriginal_Img = jsonObjectgoods.getString("logo");
                //总库存
                String resultstore_count = jsonObjectgoods.getString("stock");
                tv_spxq_xzgg_dialog_storecount.setText("库存" + resultstore_count + "件");
                //月销量
                String resultsales_sum1 = jsonObjectgoods.getString("buy_count");
                //评论次数
                String resultcomments_count = jsonObjectgoods.getString("comments_count");
                tv_spxq_pl_num.setText("宝贝评论（" + resultcomments_count + ")");
                String resultFuwu = jsonObjectgoods.getString("fuwu");
                tv_spxq_fw.setText(resultFuwu);
                String resultWeight = jsonObjectgoods.getString("weight");
                tv_spxq_cs.setText(resultWeight);
                tv_spxq_sales_sum.setText("总销量" + resultsales_sum1);
                tv_spxq_shop_price.setText(resultshop_price1);
                tv_spxq_xzgg_dialog_shop_price.setText(resultshop_price1);
                Glide.with(SpxqActivity.this)
                        .load(Api.sUrl + resultOriginal_Img)
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_spxq_xzgg_dialog_image);
                /**
                 * 店铺推荐
                 * */
                ArrayList<HashMap<String, String>> mylistDptj = new ArrayList<HashMap<String, String>>();
                JSONArray resulttuijiangoods = jsonObjectData.getJSONArray("tuijianlist");
                for (int i = 0; i < resulttuijiangoods.length(); i++) {
                    JSONObject jsonObject0 = resulttuijiangoods.getJSONObject(i);
                    //商品id
                    String resultId = jsonObject0.getString("id");
                    //商品图片
                    String resultLogo = jsonObject0.getString("logo");
                    //商品名称
                    String resultName = jsonObject0.getString("name");
                    //商品价格
                    String resultPrice = jsonObject0.getString("price");

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("ItemGoods_Id", resultId);
                    map.put("ItemOriginal_Img", resultLogo);
                    map.put("ItemGoods_Name", resultName);
                    map.put("ItemShop_Price", resultPrice);
                    mylistDptj.add(map);
                }
                setGridViewDptj(mylistDptj);

                //商家内容
                String resultstrore = jsonObjectData.getString("shangjia");
                JSONObject jsonObjectstrore = new JSONObject(resultstrore);
                //	商家logo
                String resultImg = jsonObjectstrore.getString("logo");
                //商家名称
                String resultStorename = jsonObjectstrore.getString("shangjianame");
                //  String resultScorc = jsonObjectstrore.getString("scorc");
                //good_fen
                String resultGoods_Rank = jsonObjectstrore.getString("good_fen");
                //商家均分
                String resultService_Rank = jsonObjectstrore.getString("shangjia_fen");
                //物流均分
                String resultDeliver_Rank = jsonObjectstrore.getString("wuliu_fen");
                //商家联系电话
                resultPhone = jsonObjectstrore.getString("lianxiphone");
                NumberFormat nfFormat = NumberFormat.getInstance();
                nfFormat.setMaximumFractionDigits(0);
                String resultScorc = nfFormat.format(Double.parseDouble(resultService_Rank) / 2);
                Glide.with(SpxqActivity.this)
                        .load(Api.sUrl + resultImg)
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_spxq_img);
                tv_spxq_storename.setText(resultStorename);
                tv_spxq_goods_rank.setText(resultGoods_Rank);
                tv_spxq_service_rank.setText(resultService_Rank);
                tv_spxq_deliver_rank.setText(resultDeliver_Rank);
                if (Integer.valueOf(resultScorc) == 0) {
                    iv_spxq_scorc.setImageResource(R.drawable.star_normal);
                    iv_spxq_scorc1.setImageResource(R.drawable.star_normal);
                    iv_spxq_scorc2.setImageResource(R.drawable.star_normal);
                    iv_spxq_scorc3.setImageResource(R.drawable.star_normal);
                    iv_spxq_scorc4.setImageResource(R.drawable.star_normal);
                } else if (Integer.valueOf(resultScorc) == 1) {
                    iv_spxq_scorc.setImageResource(R.drawable.star_selected);
                    iv_spxq_scorc1.setImageResource(R.drawable.star_normal);
                    iv_spxq_scorc2.setImageResource(R.drawable.star_normal);
                    iv_spxq_scorc3.setImageResource(R.drawable.star_normal);
                    iv_spxq_scorc4.setImageResource(R.drawable.star_normal);
                } else if (Integer.valueOf(resultScorc) == 2) {
                    iv_spxq_scorc.setImageResource(R.drawable.star_selected);
                    iv_spxq_scorc1.setImageResource(R.drawable.star_selected);
                    iv_spxq_scorc2.setImageResource(R.drawable.star_normal);
                    iv_spxq_scorc3.setImageResource(R.drawable.star_normal);
                    iv_spxq_scorc4.setImageResource(R.drawable.star_normal);
                } else if (Integer.valueOf(resultScorc) == 3) {
                    iv_spxq_scorc.setImageResource(R.drawable.star_selected);
                    iv_spxq_scorc1.setImageResource(R.drawable.star_selected);
                    iv_spxq_scorc2.setImageResource(R.drawable.star_selected);
                    iv_spxq_scorc3.setImageResource(R.drawable.star_normal);
                    iv_spxq_scorc4.setImageResource(R.drawable.star_normal);
                } else if (Integer.valueOf(resultScorc) == 4) {
                    iv_spxq_scorc.setImageResource(R.drawable.star_selected);
                    iv_spxq_scorc1.setImageResource(R.drawable.star_selected);
                    iv_spxq_scorc2.setImageResource(R.drawable.star_selected);
                    iv_spxq_scorc3.setImageResource(R.drawable.star_selected);
                    iv_spxq_scorc4.setImageResource(R.drawable.star_normal);
                } else if (Integer.valueOf(resultScorc) == 5) {
                    iv_spxq_scorc.setImageResource(R.drawable.star_selected);
                    iv_spxq_scorc1.setImageResource(R.drawable.star_selected);
                    iv_spxq_scorc2.setImageResource(R.drawable.star_selected);
                    iv_spxq_scorc3.setImageResource(R.drawable.star_selected);
                    iv_spxq_scorc4.setImageResource(R.drawable.star_selected);
                }

                ArrayList<HashMap<String, String>> mylistXq = new ArrayList<HashMap<String, String>>();
                JSONArray resultgooddetail = jsonObjectData.getJSONArray("gooddetail");
                for (int i = 0; i < resultgooddetail.length(); i++) {
                    JSONObject jsonObject0 = resultgooddetail.getJSONObject(i);
                    String resulturl = jsonObject0.getString("url");
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("Item", resulturl);
                    mylistXq.add(map);
                }
                // setGridViewXq(mylistXq);

                LinearLayout llGroup = (LinearLayout) findViewById(R.id.ll_group);

                //size:代码中获取到的图片数量

                llGroup.removeAllViews();  //clear linearlayout
                for (int i = 0; i < mylistXq.size(); i++) {
                    final ImageView imageView = new ImageView(this);
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));  //设置图片宽高
                    //    imageView.setImageResource(R.drawable.ic_launcher); //图片资源
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setAdjustViewBounds(true);
                 /*   Glide.with(SpxqActivity.this)
                            .load( Api.sUrl+ mylistXq.get(i).get("Item"))
                            .asBitmap()
                            .dontAnimate()
                            .into(imageView);*/

                    Glide.with(SpxqActivity.this)
                            .load(Api.sUrl + mylistXq.get(i).get("Item"))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .skipMemoryCache(true)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    imageView.setImageBitmap(resource);
                                }
                            });
                    llGroup.addView(imageView); //动态添加图片
                }

                JSONObject jsonObjectdata1 = jsonObjectData.getJSONObject("guige");
                Iterator<String> it = jsonObjectdata1.keys();
                while (it.hasNext()) {
                    ArrayList<String> mylist = new ArrayList<>();
                    // 获得key
                    String key = it.next();


                    JSONArray jsonObjectkey = jsonObjectdata1.getJSONArray(key);
                    for (int a = 0; a < jsonObjectkey.length(); a++) {
                        String imge = jsonObjectkey.get(a).toString();
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

                ArrayList<HashMap<String, String>> mylistgoodguige = new ArrayList<HashMap<String, String>>();
                mylistGuiGe = new ArrayList<HashMap<String, String>>();
                JSONArray resultgoodguige = jsonObjectData.getJSONArray("newgoodguige");
                for (int i = 0; i < resultgoodguige.length(); i++) {
                    JSONObject jsonObject0 = resultgoodguige.getJSONObject(i);
                    String resultId = jsonObject0.getString("id");
                    String resultGuige = jsonObject0.getString("guige");
                    String resultJiage = jsonObject0.getString("jiage");
                    String resultKucun = jsonObject0.getString("kucun");
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("id", resultId);
                    map.put("guige", resultGuige);
                    map.put("jiage", resultJiage);
                    map.put("kucun", resultKucun);
                    mylistgoodguige.add(map);
                    mylistGuiGe.add(map);
                }
                if (mylistGuiGe.size() == 1) {
                    is_ggqr = 1;
                    sItem_Id = mylistGuiGe.get(0).get("id");
                    tv_spxq_shop_price.setText(mylistGuiGe.get(0).get("jiage"));
                    tv_spxq_xzgg_dialog_shop_price.setText(mylistGuiGe.get(0).get("jiage"));
                    tv_spxq_xzgg_dialog_storecount.setText("库存" + mylistGuiGe.get(0).get("kucun") + "件");
                    tv_spxq_xzgg_dialog_name.setText(mylistGuiGe.get(0).get("guige"));
                    //tv_spxq_xgg.setText(mylistGuiGe.get(i).get("guige"));
                    sItem_gg_name = mylistGuiGe.get(0).get("guige");
                    tv_spxq_xgg.setText(sItem_gg_name);
                }
                //  setGridViewXgg(mylistgoodguige);
            } else {
                //  Hint(resultMsg, HintDialog.ERROR);
            }
        } catch (JSONException e) {
            //  hideDialogin();
            e.printStackTrace();
        }
    }

    /**
     * 选规格
     */
    public void show() {
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth());//设置宽度
        lp.height = (int) (display.getHeight()) - 500;//设置高度
        dialog.getWindow().setAttributes(lp);
        dialog.show();

    }

    private void initialize() {
        dialog = new Dialog(SpxqActivity.this, R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(SpxqActivity.this).inflate(R.layout.spxq_xzgg_dialog, null);
        ImageView iv_spxq_xzgg_dialog_gb = inflate.findViewById(R.id.iv_spxq_xzgg_dialog_gb);
        iv_spxq_xzgg_dialog_image = inflate.findViewById(R.id.iv_spxq_xzgg_dialog_image);
        tv_spxq_xzgg_dialog_shop_price = inflate.findViewById(R.id.tv_spxq_xzgg_dialog_shop_price);
        tv_spxq_xzgg_dialog_xgg1 = inflate.findViewById(R.id.tv_spxq_xzgg_dialog_xgg1);
        tv_spxq_xzgg_dialog_xgg2 = inflate.findViewById(R.id.tv_spxq_xzgg_dialog_xgg2);
        tv_spxq_xzgg_dialog_xgg3 = inflate.findViewById(R.id.tv_spxq_xzgg_dialog_xgg3);
        mgv_spxq_xzgg_dialog_xgg1 = inflate.findViewById(R.id.mgv_spxq_xzgg_dialog_xgg1);
        mgv_spxq_xzgg_dialog_xgg2 = inflate.findViewById(R.id.mgv_spxq_xzgg_dialog_xgg2);
        mgv_spxq_xzgg_dialog_xgg3 = inflate.findViewById(R.id.mgv_spxq_xzgg_dialog_xgg3);
        tv_spxq_xzgg_dialog_xgg1.setVisibility(View.GONE);
        tv_spxq_xzgg_dialog_xgg2.setVisibility(View.GONE);
        tv_spxq_xzgg_dialog_xgg3.setVisibility(View.GONE);
        mgv_spxq_xzgg_dialog_xgg1.setVisibility(View.GONE);
        mgv_spxq_xzgg_dialog_xgg2.setVisibility(View.GONE);
        mgv_spxq_xzgg_dialog_xgg3.setVisibility(View.GONE);
        ImageView iv_spxq_xzgg_dialog_xgg_jian = inflate.findViewById(R.id.iv_spxq_xzgg_dialog_xgg_jian);
        final TextView tv_spxq_xzgg_dialog_xgg_number = inflate.findViewById(R.id.tv_spxq_xzgg_dialog_xgg_number);
        LinearLayout ll_spxq_xzgg_dialog_xgg_jia = inflate.findViewById(R.id.ll_spxq_xzgg_dialog_xgg_jia);
        bt_spxq_xzgg_dialog_xgg_qr = inflate.findViewById(R.id.bt_spxq_xzgg_dialog_xgg_qr);
        bt_spxq_xzgg_dialog_xgg_qr.setBackgroundResource(R.drawable.bj_30000000_19);
        bt_spxq_xzgg_dialog_xgg_qr.setEnabled(false);
        tv_spxq_xzgg_dialog_storecount = inflate.findViewById(R.id.tv_spxq_xzgg_dialog_storecount);
        tv_spxq_xzgg_dialog_name = inflate.findViewById(R.id.tv_spxq_xzgg_dialog_name);
        iv_spxq_xzgg_dialog_gb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        iv_spxq_xzgg_dialog_xgg_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (num > 1) {
                    num -= 1;
                }
                tv_spxq_xzgg_dialog_xgg_number.setText(String.valueOf(num));
            }
        });
        ll_spxq_xzgg_dialog_xgg_jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num += 1;
                tv_spxq_xzgg_dialog_xgg_number.setText(String.valueOf(num));
            }
        });
        tv_spxq_xzgg_dialog_xgg_number.setText(String.valueOf(num));
        bt_spxq_xzgg_dialog_xgg_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if (!sItem_gg_name.equals("")) {
                dialog.dismiss();
                tv_spxq_xgg.setText(sItem_gg_name);
                is_ggqr = 1;
                if (gg_xsk == 1) {
                    dialogin("");
                    gouwuche(goods_id, String.valueOf(num), sItem_Id);
                } else if (gg_xsk == 2) {
                    txdd();
                }
                // }
            }
        });
    }

    /**
     * 参数弹出框
     */
    private void initialize_cs() {
        dialog_cs = new Dialog(SpxqActivity.this, R.style.ActionSheetDialogStyle);
        final View inflate = LayoutInflater.from(SpxqActivity.this).inflate(R.layout.spxq_cs_dialog, null);
        ImageView iv_spxq_cs_dialog_gb = inflate.findViewById(R.id.iv_spxq_cs_dialog_gb);
        Button bt_spxq_cs_wancheng = inflate.findViewById(R.id.bt_spxq_cs_wancheng);

        tv_spxq_cs_dialog_bt = inflate.findViewById(R.id.tv_spxq_cs_dialog_bt);
        iv_spxq_cs_dialog_gb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_cs.dismiss();
            }
        });
        bt_spxq_cs_wancheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_cs.dismiss();
            }
        });
        gv_spxq_cs_dialog = inflate.findViewById(R.id.gv_spxq_cs_dialog);
        dialog_cs.setContentView(inflate);
        Window dialogWindow = dialog_cs.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog_cs.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        //  lp.height = (int) (display.getHeight()) - 200;//设置高度
        dialog_cs.getWindow().setAttributes(lp);


    }

    /**
     * 轮播监听
     *
     * @param position
     */
    @Override
    public void OnBannerClick(int position) {
        //  Toast.makeText(this, "你点了第" + (position + 1) + "张轮播图", Toast.LENGTH_SHORT).show();
    }

    /**
     * 网络加载图片
     * 使用了Glide图片加载框架
     */
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context.getApplicationContext())
                    .load(Api.sUrl + (String) path)
                    .into(imageView);
        }
    }


    /**
     * 详情图片加载
     * 设置GirdView参数，绑定数据
     */
    private void setGridViewXq(ArrayList<HashMap<String, String>> mylist) {
        MyAdapterXq myAdapterXq = new MyAdapterXq(SpxqActivity.this);
        myAdapterXq.arrlist = mylist;
        mlv_spxq_xq.setAdapter(myAdapterXq);
        // myAdapterXq.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(mlv_spxq_xq);
    }


    public void setListViewHeightBasedOnChildren(ListView listView) {

        // 获取ListView对应的Adapter

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {

            return;

        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目

            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(0, 0); // 计算子项View 的宽高

            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        // listView.getDividerHeight()获取子项间分隔符占用的高度

        // params.height最后得到整个ListView完整显示需要的高度

        listView.setLayoutParams(params);

    }


    /**
     * 宝贝评论
     * 设置GirdView参数，绑定数据
     */
    private void setGridView(ArrayList<HashMap<String, String>> mylist) {
        MyAdapter myAdapter = new MyAdapter(this);

        myAdapter.arrlist = mylist;
        int size = mylist.size();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)
        int length = screenWidth - 30;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gv_spxq_bbpl.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gv_spxq_bbpl.setColumnWidth(itemWidth); // 设置列表项宽
        gv_spxq_bbpl.setHorizontalSpacing(10); // 设置列表项水平间距
        gv_spxq_bbpl.setStretchMode(GridView.NO_STRETCH);
        gv_spxq_bbpl.setNumColumns(size); // 设置列数量=列表集合数

        gv_spxq_bbpl.setAdapter(myAdapter);
       /* sv_shequn.smoothScrollTo(0, 20);
        sv_shequn.setFocusable(true);*/
        if (mylist.size() > 0) {
            gv_spxq_bbpl.setVisibility(View.VISIBLE);
        } else {
            gv_spxq_bbpl.setVisibility(View.GONE);
        }
        sv_spxq_camera.scrollTo(0, 0);

    }

    /**
     * 店铺推荐
     * 设置GirdView参数，绑定数据
     */
    private void setGridViewDptj(ArrayList<HashMap<String, String>> mylist) {
        MyAdapterDptj myAdapterDptj = new MyAdapterDptj(SpxqActivity.this);
        myAdapterDptj.arrlist = mylist;
        mgv_chenpin.setAdapter(myAdapterDptj);

    }


    private void initListeners() {
        // 获取顶部图片高度后，设置滚动监听
        ViewTreeObserver vto = iv_spxq_banner.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                iv_spxq_banner.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                imageHeight = iv_spxq_banner.getHeight();
                sv_spxq_camera.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
                    @Override
                    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                        // TODO Auto-generated method stub
                        // Log.i("TAG", "y--->" + y + "    height-->" + height);
                        if (sType.equals("pj")) {
                            fl_spxq_title.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                            ll_spxq_sxp.setVisibility(View.VISIBLE);
                            tv_xpxq_title_xh.setVisibility(View.VISIBLE);
                            iv_spxq_back1.setVisibility(View.VISIBLE);
                            ll_spxq_back.setVisibility(View.GONE);
                        } else {
                            if (y <= 0) {
//                          设置文字背景颜色，白色
                                fl_spxq_title.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));//AGB由相关工具获得，或者美工提供
                                ll_spxq_gwc.setVisibility(View.VISIBLE);
                                ll_spxq_sxp.setVisibility(View.GONE);
                                tv_xpxq_title_xh.setVisibility(View.GONE);
                                iv_spxq_back1.setVisibility(View.GONE);
                                ll_spxq_back.setVisibility(View.VISIBLE);
//                          设置文字颜色，黑色
                                //  rl_camera_title.setTextColor(Color.argb((int) 0, 255, 255, 255));
                                Log.e("111", "y <= 0");
                            } else if (y > 0 && y <= imageHeight) {
                                float scale = (float) y / imageHeight;
                                float alpha = (255 * scale);
                                // 只是layout背景透明(仿知乎滑动效果)白色透明
                                fl_spxq_title.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                                ll_spxq_gwc.setVisibility(View.GONE);
                                ll_spxq_sxp.setVisibility(View.GONE);
                                tv_xpxq_title_xh.setVisibility(View.GONE);
                                iv_spxq_back1.setVisibility(View.GONE);
                                ll_spxq_back.setVisibility(View.VISIBLE);
                                //                          设置文字颜色，黑色，加透明度
                                // textView.setTextColor(Color.argb((int) alpha, 0, 0, 0));
                                Log.e("111", "y > 0 && y <= imageHeight");
                            } else {
//							白色不透明
                                fl_spxq_title.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                                ll_spxq_sxp.setVisibility(View.VISIBLE);
                                tv_xpxq_title_xh.setVisibility(View.VISIBLE);
                                iv_spxq_back1.setVisibility(View.VISIBLE);
                                ll_spxq_back.setVisibility(View.GONE);
                                //                          设置文字颜色
                                //黑色
                                // textView.setTextColor(Color.argb((int) 255, 0, 0, 0));
                                Log.e("111", "else");
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 宝贝评论
     */
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
                view = inflater.inflate(R.layout.spxq_bbpl_item, null);
            }

            //用户头像
            ImageView iv_spxq_bbpl_item_pead_pic = view.findViewById(R.id.iv_spxq_bbpl_item_pead_pic);
            //用户名称
            TextView tv_spxq_bbpl_item_username = view.findViewById(R.id.tv_spxq_bbpl_item_username);
            //评论内容
            TextView tv_spxq_bbpl_item_content = view.findViewById(R.id.tv_spxq_bbpl_item_content);
            //点赞
            TextView tv_spxq_bbpl_item_zan_num = view.findViewById(R.id.tv_spxq_bbpl_item_zan_num);
            //评分
            ImageView iv_spxq_bbpl_item_goods_rank = view.findViewById(R.id.iv_spxq_bbpl_item_goods_rank);
            ImageView iv_spxq_bbpl_item_goods_rank1 = view.findViewById(R.id.iv_spxq_bbpl_item_goods_rank1);
            ImageView iv_spxq_bbpl_item_goods_rank2 = view.findViewById(R.id.iv_spxq_bbpl_item_goods_rank2);
            ImageView iv_spxq_bbpl_item_goods_rank3 = view.findViewById(R.id.iv_spxq_bbpl_item_goods_rank3);
            ImageView iv_spxq_bbpl_item_goods_rank4 = view.findViewById(R.id.iv_spxq_bbpl_item_goods_rank4);

    /*        map.put("id", resultId);
            map.put("user_logo", resultUser_Logo);
            map.put("user_name", resultUser_Name);
            map.put("fen", resultFen);
            map.put("text", resultText);*/
            tv_spxq_bbpl_item_username.setText(arrlist.get(position).get("user_name"));
            tv_spxq_bbpl_item_content.setText(arrlist.get(position).get("text"));
            //tv_spxq_bbpl_item_zan_num.setText(arrlist.get(position).get("ItemZan_Num"));
            Glide.with(SpxqActivity.this)
                    .load(Api.sUrl + arrlist.get(position).get("user_logo"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_spxq_bbpl_item_pead_pic);
            if (Integer.valueOf(arrlist.get(position).get("fen")) / 2 == 0) {
                iv_spxq_bbpl_item_goods_rank.setImageResource(R.drawable.star_normal);
                iv_spxq_bbpl_item_goods_rank1.setImageResource(R.drawable.star_normal);
                iv_spxq_bbpl_item_goods_rank2.setImageResource(R.drawable.star_normal);
                iv_spxq_bbpl_item_goods_rank3.setImageResource(R.drawable.star_normal);
                iv_spxq_bbpl_item_goods_rank4.setImageResource(R.drawable.star_normal);
            } else if (Integer.valueOf(arrlist.get(position).get("fen")) / 2 == 1) {
                iv_spxq_bbpl_item_goods_rank.setImageResource(R.drawable.star_selected);
                iv_spxq_bbpl_item_goods_rank1.setImageResource(R.drawable.star_normal);
                iv_spxq_bbpl_item_goods_rank2.setImageResource(R.drawable.star_normal);
                iv_spxq_bbpl_item_goods_rank3.setImageResource(R.drawable.star_normal);
                iv_spxq_bbpl_item_goods_rank4.setImageResource(R.drawable.star_normal);
            } else if (Integer.valueOf(arrlist.get(position).get("fen")) / 2 == 2) {
                iv_spxq_bbpl_item_goods_rank.setImageResource(R.drawable.star_selected);
                iv_spxq_bbpl_item_goods_rank1.setImageResource(R.drawable.star_selected);
                iv_spxq_bbpl_item_goods_rank2.setImageResource(R.drawable.star_normal);
                iv_spxq_bbpl_item_goods_rank3.setImageResource(R.drawable.star_normal);
                iv_spxq_bbpl_item_goods_rank4.setImageResource(R.drawable.star_normal);
            } else if (Integer.valueOf(arrlist.get(position).get("fen")) / 2 == 3) {
                iv_spxq_bbpl_item_goods_rank.setImageResource(R.drawable.star_selected);
                iv_spxq_bbpl_item_goods_rank1.setImageResource(R.drawable.star_selected);
                iv_spxq_bbpl_item_goods_rank2.setImageResource(R.drawable.star_selected);
                iv_spxq_bbpl_item_goods_rank3.setImageResource(R.drawable.star_normal);
                iv_spxq_bbpl_item_goods_rank4.setImageResource(R.drawable.star_normal);
            } else if (Integer.valueOf(arrlist.get(position).get("fen")) / 2 == 4) {
                iv_spxq_bbpl_item_goods_rank.setImageResource(R.drawable.star_selected);
                iv_spxq_bbpl_item_goods_rank1.setImageResource(R.drawable.star_selected);
                iv_spxq_bbpl_item_goods_rank2.setImageResource(R.drawable.star_selected);
                iv_spxq_bbpl_item_goods_rank3.setImageResource(R.drawable.star_selected);
                iv_spxq_bbpl_item_goods_rank4.setImageResource(R.drawable.star_normal);
            } else if (Integer.valueOf(arrlist.get(position).get("fen")) / 2 == 5) {
                iv_spxq_bbpl_item_goods_rank.setImageResource(R.drawable.star_selected);
                iv_spxq_bbpl_item_goods_rank1.setImageResource(R.drawable.star_selected);
                iv_spxq_bbpl_item_goods_rank2.setImageResource(R.drawable.star_selected);
                iv_spxq_bbpl_item_goods_rank3.setImageResource(R.drawable.star_selected);
                iv_spxq_bbpl_item_goods_rank4.setImageResource(R.drawable.star_selected);
            }
            return view;
        }
    }

    /**
     * 店铺推荐
     */
    private class MyAdapterDptj extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;

        public MyAdapterDptj(Context context) {
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
                view = inflater.inflate(R.layout.spxq_dptj_item, null);
            }
            LinearLayout ll_spxq_dptj_item = view.findViewById(R.id.ll_spxq_dptj_item);
            ImageView iv_spxq_dptj_item_original_img = view.findViewById(R.id.iv_spxq_dptj_item_original_img);
            TextView iv_spxq_dptj_item_goods_name = view.findViewById(R.id.iv_spxq_dptj_item_goods_name);
            TextView iv_spxq_dptj_item_shop_price = view.findViewById(R.id.iv_spxq_dptj_item_shop_price);
            Glide.with(SpxqActivity.this)
                    .load(Api.sUrl + arrlist.get(position).get("ItemOriginal_Img"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_spxq_dptj_item_original_img);
            iv_spxq_dptj_item_goods_name.setText(arrlist.get(position).get("ItemGoods_Name"));
            iv_spxq_dptj_item_shop_price.setText("￥" + arrlist.get(position).get("ItemShop_Price"));
            ll_spxq_dptj_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogin("");
                    Xiangqing(arrlist.get(position).get("ItemGoods_Id"));
                }
            });

            return view;
        }
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

            if (pl_List.get(position).listurl.size() == 1) {
                Glide.with(SpxqActivity.this)
                        .load(Api.sUrl + pl_List.get(position).listurl.get(0))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .into(iv_spxq_pj_item_img1);
            } else if (pl_List.get(position).listurl.size() == 2) {
                Glide.with(SpxqActivity.this)
                        .load(Api.sUrl + pl_List.get(position).listurl.get(0))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .into(iv_spxq_pj_item_img1);
                Glide.with(SpxqActivity.this)
                        .load(Api.sUrl + pl_List.get(position).listurl.get(1))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .into(iv_spxq_pj_item_img2);
            } else if (pl_List.get(position).listurl.size() == 3) {
                Glide.with(SpxqActivity.this)
                        .load(Api.sUrl + pl_List.get(position).listurl.get(0))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .into(iv_spxq_pj_item_img1);
                Glide.with(SpxqActivity.this)
                        .load(Api.sUrl + pl_List.get(position).listurl.get(1))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .into(iv_spxq_pj_item_img2);
                Glide.with(SpxqActivity.this)
                        .load(Api.sUrl + pl_List.get(position).listurl.get(2))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .into(iv_spxq_pj_item_img3);
            }
           /* if (pl_List.get(position).listurl.get(0).equals("null")) {
                iv_spxq_pj_item_img1.setVisibility(View.GONE);
            } else {
                iv_spxq_pj_item_img1.setVisibility(View.VISIBLE);
                Glide.with(SpxqActivity.this)
                        .load( Api.sUrl+ pl_List.get(position).listurl.get(0))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_spxq_pj_item_img1);
            }
            if (pl_List.get(position).listurl.get(1).equals("null")) {
                iv_spxq_pj_item_img2.setVisibility(View.GONE);
            } else {
                iv_spxq_pj_item_img2.setVisibility(View.VISIBLE);
                Glide.with(SpxqActivity.this)
                        .load( Api.sUrl+ pl_List.get(position).listurl.get(1))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_spxq_pj_item_img2);
            }
            if (pl_List.get(position).listurl.get(2).equals("null")) {
                iv_spxq_pj_item_img3.setVisibility(View.GONE);
            } else {
                iv_spxq_pj_item_img3.setVisibility(View.VISIBLE);
                Glide.with(SpxqActivity.this)
                        .load( Api.sUrl+ pl_List.get(position).listurl.get(2))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_spxq_pj_item_img3);
            }
*/
            Glide.with(SpxqActivity.this)
                    .load(Api.sUrl + pl_List.get(position).user_logo)
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
     * 商品详情
     */
    private class MyAdapterXq extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;


        public MyAdapterXq(Context context) {
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
                view = inflater.inflate(R.layout.spxq_xq_item, null);
            }
            MyImageView miv_spxq_xq_item = view.findViewById(R.id.miv_spxq_xq_item);
            //  ImageView iv_spxq_xq_item = view.findViewById(R.id.iv_spxq_xq_item);
            Glide.with(SpxqActivity.this)
                    .load(Api.sUrl + arrlist.get(position).get("Item"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(miv_spxq_xq_item);

            return view;
        }
    }

    /*    */

    /**
     * 选规格1
     *//*
    private void setGridViewXgg(ArrayList<HashMap<String, String>> mylist) {
        myAdapterXgg = new MyAdapterXgg(SpxqActivity.this);
        myAdapterXgg.arrlist = mylist;

        mgv_spxq_xzgg_dialog_xgg1.setAdapter(myAdapterXgg);
    }*/

    private class MyAdapterXgg extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;

        public MyAdapterXgg(Context context) {
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
                view = inflater.inflate(R.layout.xgg_item, null);
            }
            TextView tv_xgg_item = view.findViewById(R.id.tv_xgg_item);
            tv_xgg_item.setText(arrlist.get(position).get("guige"));
            tv_xgg_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sItem_Id = arrlist.get(position).get("id");
                    sItem_gg_name = arrlist.get(position).get("guige");
                    tv_spxq_xzgg_dialog_shop_price.setText(arrlist.get(position).get("jiage"));
                    tv_spxq_xzgg_dialog_storecount.setText("库存" + arrlist.get(position).get("kucun") + "件");
                    notifyDataSetChanged();
                }
            });

            if (sItem_Id.equals(arrlist.get(position).get("id"))) {
                tv_xgg_item.setBackgroundResource(R.drawable.bk_4350b6_6);
                tv_xgg_item.setTextColor(tv_xgg_item.getResources().getColor(R.color.theme));
            } else {
                tv_xgg_item.setBackgroundResource(R.drawable.bj_f4f4f4_6);
                tv_xgg_item.setTextColor(tv_xgg_item.getResources().getColor(R.color.qian_hei));
            }


            return view;
        }
    }


    private class MyAdapterCs extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;


        public MyAdapterCs(Context context) {
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

            if (tv_spxq_cs_dialog_bt.getText().toString().equals("产品参数")) {
                view = inflater.inflate(R.layout.cs_item, null);
                TextView tv_cs_item_attr_name = view.findViewById(R.id.tv_cs_item_attr_name);
                TextView tv_cs_item_attr_value = view.findViewById(R.id.tv_cs_item_attr_value);
                tv_cs_item_attr_name.setText(arrlist.get(position).get("attr_name"));
                tv_cs_item_attr_value.setText(arrlist.get(position).get("attr_value"));
            } else {
                view = inflater.inflate(R.layout.baozheng_item, null);
                ImageView iv_baozheng_item = view.findViewById(R.id.iv_baozheng_item);
                TextView tv_cs_item_name = view.findViewById(R.id.tv_cs_item_name);
                TextView tv_cs_item_value = view.findViewById(R.id.tv_cs_item_value);
                Glide.with(SpxqActivity.this)
                        .load(Api.sUrl + arrlist.get(position).get("img"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_baozheng_item);
                tv_cs_item_name.setText(arrlist.get(position).get("name"));
                tv_cs_item_value.setText(arrlist.get(position).get("value"));
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
                tv_spxq_shop_price.setText(mylistGuiGe.get(i).get("jiage"));
                tv_spxq_xzgg_dialog_shop_price.setText(mylistGuiGe.get(i).get("jiage"));
                tv_spxq_xzgg_dialog_storecount.setText("库存" + mylistGuiGe.get(i).get("kucun") + "件");
                tv_spxq_xzgg_dialog_name.setText(mylistGuiGe.get(i).get("guige"));
                //tv_spxq_xgg.setText(mylistGuiGe.get(i).get("guige"));
                sItem_gg_name = mylistGuiGe.get(i).get("guige");
            }
        }
        if (sItem_Id.equals("")) {
            tv_spxq_shop_price.setText(resultshop_price1);
            tv_spxq_xzgg_dialog_shop_price.setText("0");
            tv_spxq_xzgg_dialog_storecount.setText("库存0件");
            tv_spxq_xzgg_dialog_name.setText(sGg);
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
        MyAdapterXgg1 myAdapterXgg1 = new MyAdapterXgg1(SpxqActivity.this);
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
        MyAdapterXgg2 myAdapterXgg2 = new MyAdapterXgg2(SpxqActivity.this);
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
        MyAdapterXgg3 myAdapterXgg3 = new MyAdapterXgg3(SpxqActivity.this);
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

}
