package com.example.administrator.yunyue.sc_activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.example.administrator.yunyue.data.SpflGridData;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.erci.activity.CwhyActivity;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
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
import java.util.List;

public class DpzyActivity extends AppCompatActivity implements OnBannerListener {
    /**
     * 返回
     */
    private ImageView iv_dpzy_back;
    /**
     * 商品列表
     */
    private MyGridView mgv_dpzy;
    /**
     * 店铺详情
     */
    private LinearLayout ll_dpzy_dpxq;
    RequestQueue queue = null;
    private String sUser_id = "";
    private String sIs_vip = "";
    private SharedPreferences pref;
    private String sId = "";
    /**
     * 店铺logo
     */
    private ImageView iv_dpzy_image;
    /**
     * 店铺名称
     */
    private TextView tv_dpzy_name;
    /**
     * 关注人数
     */
    private TextView tv_dpzy_count;
    /**
     * 是否关注
     */
    private TextView tv_dpzy_is_attention;

    /**
     * 产品列表名
     */
    private TextView tv_dpzy_cp;

    /**
     * 优惠卷
     */
    private GridView gv_dpzy_yhj;
    /**
     * 排序方式
     */
    private TextView tv_dpzy_tuijian, tv_dpzy_xl, tv_dpzy_jg, tv_dpzy_jkyh;
    private LinearLayout ll_dpzy_jg;
    private ImageView iv_dpzy_jg;

    /**
     * 首页
     */
    private LinearLayout ll_dpzy_sy;
    private TextView tv_dpzy_sy, tv_dpzy_sy_xhx;
    /**
     * 商品
     */
    private LinearLayout ll_dpzy_sp;
    private TextView tv_dpzy_sp, tv_dpzy_sp_xhx;

    /**
     * 活动
     */
    private LinearLayout ll_dpzy_hd;
    private TextView tv_dpzy_hd, tv_dpzy_hd_xhx;

    /**
     * 上新
     */
    private LinearLayout ll_dpzy_sx;
    private TextView tv_dpzy_sx, tv_dpzy_sx_xhx;


    /**
     * 店铺主页商品
     */
    private LinearLayout ll_dpzy_sy_zy;
    private ScrollView sv_dpzy_sy_zy;
    /**
     * 店铺主页其他
     */
    private LinearLayout ll_dpzy_zy;

    /**
     * 排序
     * 排序1推荐 2价格降序 3价格升序 4销量降序 5有货.
     */
    private int iOrder = 1;
    /**
     * 商品分类id.
     */
    private String sCategory = "";
    /**
     * 搜索的商品名称.
     */
    private String sText = "";
    /**
     * 热门分类
     */
    private TextView tv_dpzy_rmfl;
    private PullToRefreshGridView pull_refresh_grid_dpzy;
    int iPage = 1;
    private MyAdapterSplb myAdapterSplb;

    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

    private LinearLayout ll_dpzy_px;
    /**
     * 商品显示方式
     */
    private LinearLayout ll_dpzy_xsfs;
    private ImageView iv_dpzy_xsfs;

    /**
     * 0--方格
     * 1--列表
     */
    int iLb_Type = 0;

    /**
     * 商家电话
     */
    private String phone = "";
    private TextView tv_dpzy_phone;
    private Banner banner_dpzy;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;
    private EditText et_dpzy_text;
    private String sType = "";
    /**
     * 图标分类
     */
    private ImageView iv_dpzy_fl;
    public static List<SpflGridData> list_cat_id = new ArrayList<>();
    public static String shangjia_intro = "";
    public static String count = "";
    public static String time = "";
    public static String image = "";
    public static String storename = "";
    public static int guanzhu = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_dpzy);
        queue = Volley.newRequestQueue(DpzyActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        sIs_vip = pref.getString("is_vip", "");
        Intent intent = getIntent();
        sId = intent.getStringExtra("id");
        sType = intent.getStringExtra("type");
        mgv_dpzy = findViewById(R.id.mgv_dpzy);
        iv_dpzy_fl = findViewById(R.id.iv_dpzy_fl);
        ll_dpzy_dpxq = findViewById(R.id.ll_dpzy_dpxq);
        iv_dpzy_back = findViewById(R.id.iv_dpzy_back);
        iv_dpzy_image = findViewById(R.id.iv_dpzy_image);
        tv_dpzy_name = findViewById(R.id.tv_dpzy_name);
        tv_dpzy_count = findViewById(R.id.tv_dpzy_count);
        tv_dpzy_is_attention = findViewById(R.id.tv_dpzy_is_attention);
        tv_dpzy_cp = findViewById(R.id.tv_dpzy_cp);
        gv_dpzy_yhj = findViewById(R.id.gv_dpzy_yhj);
        ll_dpzy_sy_zy = findViewById(R.id.ll_dpzy_sy_zy);
        sv_dpzy_sy_zy = findViewById(R.id.sv_dpzy_sy_zy);
        ll_dpzy_zy = findViewById(R.id.ll_dpzy_zy);
        ll_dpzy_px = findViewById(R.id.ll_dpzy_px);
        ll_dpzy_sy = findViewById(R.id.ll_dpzy_sy);
        tv_dpzy_sy = findViewById(R.id.tv_dpzy_sy);
        tv_dpzy_sy_xhx = findViewById(R.id.tv_dpzy_sy_xhx);
        ll_dpzy_sp = findViewById(R.id.ll_dpzy_sp);
        tv_dpzy_sp = findViewById(R.id.tv_dpzy_sp);
        tv_dpzy_sp_xhx = findViewById(R.id.tv_dpzy_sp_xhx);
        ll_dpzy_hd = findViewById(R.id.ll_dpzy_hd);
        tv_dpzy_hd = findViewById(R.id.tv_dpzy_hd);
        tv_dpzy_hd_xhx = findViewById(R.id.tv_dpzy_hd_xhx);
        ll_dpzy_sx = findViewById(R.id.ll_dpzy_sx);
        tv_dpzy_sx = findViewById(R.id.tv_dpzy_sx);
        tv_dpzy_sx_xhx = findViewById(R.id.tv_dpzy_sx_xhx);
        et_dpzy_text = findViewById(R.id.et_dpzy_text);
        pull_refresh_grid_dpzy = findViewById(R.id.pull_refresh_grid_dpzy);
        pull_refresh_grid_dpzy.setMode(PullToRefreshBase.Mode.BOTH);
        ll_dpzy_xsfs = findViewById(R.id.ll_dpzy_xsfs);
        iv_dpzy_xsfs = findViewById(R.id.iv_dpzy_xsfs);
        tv_dpzy_phone = findViewById(R.id.tv_dpzy_phone);
        tv_dpzy_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(phone);

            }
        });
        //private TextView tv_dpzy_tuijian, tv_dpzy_xl, tv_dpzy_jg, tv_dpzy_jkyh;
        tv_dpzy_tuijian = findViewById(R.id.tv_dpzy_tuijian);
        tv_dpzy_xl = findViewById(R.id.tv_dpzy_xl);
        tv_dpzy_jg = findViewById(R.id.tv_dpzy_jg);
        tv_dpzy_jkyh = findViewById(R.id.tv_dpzy_jkyh);
        ll_dpzy_jg = findViewById(R.id.ll_dpzy_jg);
        iv_dpzy_jg = findViewById(R.id.iv_dpzy_jg);
        tv_dpzy_rmfl = findViewById(R.id.tv_dpzy_rmfl);
        tv_dpzy_rmfl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DpzyActivity.this, SpflActivity.class);
                startActivity(intent);
            }
        });
        iv_dpzy_fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DpzyActivity.this, SpflActivity.class);
                startActivity(intent);
            }
        });
        banner_dpzy = findViewById(R.id.banner_dpzy);
        tv_dpzy_tuijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogin("");
                iOrder = 1;
                iPage = 1;
                shangpin(sId, String.valueOf(iOrder), sCategory, sText, "");
                tv_dpzy_tuijian.setTextColor(tv_dpzy_tuijian.getResources().getColor(R.color.lan));
                tv_dpzy_xl.setTextColor(tv_dpzy_xl.getResources().getColor(R.color.black));
                tv_dpzy_jg.setTextColor(tv_dpzy_jg.getResources().getColor(R.color.black));
                tv_dpzy_jkyh.setTextColor(tv_dpzy_jkyh.getResources().getColor(R.color.black));
                iv_dpzy_jg.setImageResource(R.drawable.icon_arrow_selected);

            }
        });
        tv_dpzy_xl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogin("");
                iOrder = 2;
                iPage = 1;
                shangpin(sId, String.valueOf(iOrder), sCategory, sText, "");
                tv_dpzy_tuijian.setTextColor(tv_dpzy_tuijian.getResources().getColor(R.color.black));
                tv_dpzy_xl.setTextColor(tv_dpzy_xl.getResources().getColor(R.color.lan));
                tv_dpzy_jg.setTextColor(tv_dpzy_jg.getResources().getColor(R.color.black));
                tv_dpzy_jkyh.setTextColor(tv_dpzy_jkyh.getResources().getColor(R.color.black));
                iv_dpzy_jg.setImageResource(R.drawable.icon_arrow_selected);
            }
        });
        tv_dpzy_jg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iOrder == 3) {
                    iOrder = 4;
                } else {
                    iOrder = 3;
                }
                dialogin("");
                iPage = 1;
                shangpin(sId, String.valueOf(iOrder), sCategory, sText, "");
                tv_dpzy_tuijian.setTextColor(tv_dpzy_tuijian.getResources().getColor(R.color.black));
                tv_dpzy_xl.setTextColor(tv_dpzy_xl.getResources().getColor(R.color.black));
                tv_dpzy_jg.setTextColor(tv_dpzy_jg.getResources().getColor(R.color.lan));
                tv_dpzy_jkyh.setTextColor(tv_dpzy_jkyh.getResources().getColor(R.color.black));
                if (iOrder == 3) {
                    iv_dpzy_jg.setImageResource(R.drawable.icon_arrow_normal_j);
                } else {
                    iv_dpzy_jg.setImageResource(R.drawable.icon_arrow_selected_s);
                }
            }
        });
        tv_dpzy_jkyh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogin("");
                iOrder = 5;
                iPage = 1;
                shangpin(sId, String.valueOf(iOrder), sCategory, sText, "");
                tv_dpzy_tuijian.setTextColor(tv_dpzy_tuijian.getResources().getColor(R.color.black));
                tv_dpzy_xl.setTextColor(tv_dpzy_xl.getResources().getColor(R.color.black));
                tv_dpzy_jg.setTextColor(tv_dpzy_jg.getResources().getColor(R.color.black));
                tv_dpzy_jkyh.setTextColor(tv_dpzy_jkyh.getResources().getColor(R.color.lan));
                iv_dpzy_jg.setImageResource(R.drawable.icon_arrow_selected);
            }
        });
        ll_dpzy_sy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_dpzy_sy_zy.setVisibility(View.VISIBLE);
                sv_dpzy_sy_zy.setVisibility(View.VISIBLE);
                ll_dpzy_zy.setVisibility(View.GONE);
                tv_dpzy_sy.setTextColor(tv_dpzy_sy.getResources().getColor(R.color.lan));
                tv_dpzy_sy_xhx.setBackgroundResource(R.color.lan);
                tv_dpzy_sp.setTextColor(tv_dpzy_sp.getResources().getColor(R.color.hei333333));
                tv_dpzy_sp_xhx.setBackgroundResource(R.color.touming);
                tv_dpzy_hd.setTextColor(tv_dpzy_hd.getResources().getColor(R.color.hei333333));
                tv_dpzy_hd_xhx.setBackgroundResource(R.color.touming);
                tv_dpzy_sx.setTextColor(tv_dpzy_sx.getResources().getColor(R.color.hei333333));
                tv_dpzy_sx_xhx.setBackgroundResource(R.color.touming);
            }
        });
        ll_dpzy_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_dpzy_zy.setVisibility(View.VISIBLE);
                ll_dpzy_sy_zy.setVisibility(View.GONE);
                sv_dpzy_sy_zy.setVisibility(View.GONE);
                ll_dpzy_px.setVisibility(View.VISIBLE);
                tv_dpzy_sy.setTextColor(tv_dpzy_sy.getResources().getColor(R.color.hei333333));
                tv_dpzy_sy_xhx.setBackgroundResource(R.color.touming);
                tv_dpzy_sp.setTextColor(tv_dpzy_sp.getResources().getColor(R.color.lan));
                tv_dpzy_sp_xhx.setBackgroundResource(R.color.lan);
                tv_dpzy_hd.setTextColor(tv_dpzy_hd.getResources().getColor(R.color.hei333333));
                tv_dpzy_hd_xhx.setBackgroundResource(R.color.touming);
                tv_dpzy_sx.setTextColor(tv_dpzy_sx.getResources().getColor(R.color.hei333333));
                tv_dpzy_sx_xhx.setBackgroundResource(R.color.touming);
                dialogin("");
                iOrder = 1;
                iPage = 1;
                shangpin(sId, String.valueOf(iOrder), sCategory, sText, "");
            }
        });
        ll_dpzy_hd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_dpzy_zy.setVisibility(View.VISIBLE);
                ll_dpzy_sy_zy.setVisibility(View.GONE);
                sv_dpzy_sy_zy.setVisibility(View.GONE);
                ll_dpzy_px.setVisibility(View.GONE);
                tv_dpzy_sy.setTextColor(tv_dpzy_sy.getResources().getColor(R.color.hei333333));
                tv_dpzy_sy_xhx.setBackgroundResource(R.color.touming);
                tv_dpzy_sp.setTextColor(tv_dpzy_sp.getResources().getColor(R.color.hei333333));
                tv_dpzy_sp_xhx.setBackgroundResource(R.color.touming);
                tv_dpzy_hd.setTextColor(tv_dpzy_hd.getResources().getColor(R.color.lan));
                tv_dpzy_hd_xhx.setBackgroundResource(R.color.lan);
                tv_dpzy_sx.setTextColor(tv_dpzy_sx.getResources().getColor(R.color.hei333333));
                tv_dpzy_sx_xhx.setBackgroundResource(R.color.touming);
                dialogin("");
                iPage = 1;
                //   spxp("Store/goodsActivity", sId);
            }
        });
        ll_dpzy_sx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_dpzy_zy.setVisibility(View.VISIBLE);
                ll_dpzy_sy_zy.setVisibility(View.GONE);
                sv_dpzy_sy_zy.setVisibility(View.GONE);
                ll_dpzy_px.setVisibility(View.GONE);
                tv_dpzy_sy.setTextColor(tv_dpzy_sy.getResources().getColor(R.color.hei333333));
                tv_dpzy_sy_xhx.setBackgroundResource(R.color.touming);
                tv_dpzy_sp.setTextColor(tv_dpzy_sp.getResources().getColor(R.color.hei333333));
                tv_dpzy_sp_xhx.setBackgroundResource(R.color.touming);
                tv_dpzy_hd.setTextColor(tv_dpzy_hd.getResources().getColor(R.color.hei333333));
                tv_dpzy_hd_xhx.setBackgroundResource(R.color.touming);
                tv_dpzy_sx.setTextColor(tv_dpzy_sx.getResources().getColor(R.color.lan));
                tv_dpzy_sx_xhx.setBackgroundResource(R.color.lan);
                dialogin("");
                iPage = 1;
                shangpin(sId, String.valueOf(iOrder), sCategory, sText, "1");
            }
        });
        iv_dpzy_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ll_dpzy_dpxq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DpzyActivity.this, DpxqActivity.class);
                intent.putExtra("id", sId);
                startActivity(intent);
            }
        });
        tv_dpzy_is_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogin("");
                dpgz();

            }
        });
        pull_refresh_grid_dpzy.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
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
                shangpin(sId, String.valueOf(iOrder), sCategory, sText, "");
            }

            // 上拉加载跟多
            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                Log.e("TAG", "onPullUpToRefresh"); // Do work to refresh
                iPage += 1;
                shangpin(sId, String.valueOf(iOrder), sCategory, sText, "");

                // 页数
                //   p = p + 1;
                // AsyncTask异步交互加载数据
                // new GetDataTask2().execute(URL + p);
            }
        });
        ll_dpzy_xsfs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (iLb_Type == 0) {
                    iLb_Type = 1;
                    iv_dpzy_xsfs.setImageResource(R.drawable.btn_mulu_hover);
                    pull_refresh_grid_dpzy.getRefreshableView().setNumColumns(1);
                } else if (iLb_Type == 1) {
                    iLb_Type = 0;
                    iv_dpzy_xsfs.setImageResource(R.drawable.btn_mulu);
                    pull_refresh_grid_dpzy.getRefreshableView().setNumColumns(2);
                }
                myAdapterSplb.notifyDataSetChanged();
            }
        });
        et_dpzy_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sText = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_dpzy_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO) {
                    if (!sText.equals("")) {
                        ll_dpzy_zy.setVisibility(View.VISIBLE);
                        ll_dpzy_sy_zy.setVisibility(View.GONE);
                        sv_dpzy_sy_zy.setVisibility(View.GONE);
                        ll_dpzy_px.setVisibility(View.VISIBLE);
                        tv_dpzy_sy.setTextColor(tv_dpzy_sy.getResources().getColor(R.color.hei333333));
                        tv_dpzy_sy_xhx.setBackgroundResource(R.color.touming);
                        tv_dpzy_sp.setTextColor(tv_dpzy_sp.getResources().getColor(R.color.lan));
                        tv_dpzy_sp_xhx.setBackgroundResource(R.color.lan);
                        tv_dpzy_hd.setTextColor(tv_dpzy_hd.getResources().getColor(R.color.hei333333));
                        tv_dpzy_hd_xhx.setBackgroundResource(R.color.touming);
                        tv_dpzy_sx.setTextColor(tv_dpzy_sx.getResources().getColor(R.color.hei333333));
                        tv_dpzy_sx_xhx.setBackgroundResource(R.color.touming);
                        hideDialogin();
                        dialogin("");
                        iOrder = 1;
                        iPage = 1;
                        shangpin(sId, String.valueOf(iOrder), sCategory, sText, "");
                    }
                }
                return false;
            }
        });
        hideDialogin();
        dialogin("");
        jianjei(sId);
        dpyhj(sId);
        if (sType == null) {
        } else if (sType.equals("全部")) {
            sp();
        }
    }

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initview();
    }

    private void initview() {
        if (!SpflActivity.sFl_Id.equals("")) {
            if (!SpflActivity.sFl_Id.equals("全部")) {
                sCategory = SpflActivity.sFl_Id;
            }
            SpflActivity.sFl_Id = "";
            sp();
        }
    }

    private void sp() {
        ll_dpzy_zy.setVisibility(View.VISIBLE);
        ll_dpzy_sy_zy.setVisibility(View.GONE);
        sv_dpzy_sy_zy.setVisibility(View.GONE);
        ll_dpzy_px.setVisibility(View.VISIBLE);
        tv_dpzy_sy.setTextColor(tv_dpzy_sy.getResources().getColor(R.color.hei333333));
        tv_dpzy_sy_xhx.setBackgroundResource(R.color.touming);
        tv_dpzy_sp.setTextColor(tv_dpzy_sp.getResources().getColor(R.color.lan));
        tv_dpzy_sp_xhx.setBackgroundResource(R.color.lan);
        tv_dpzy_hd.setTextColor(tv_dpzy_hd.getResources().getColor(R.color.hei333333));
        tv_dpzy_hd_xhx.setBackgroundResource(R.color.touming);
        tv_dpzy_sx.setTextColor(tv_dpzy_sx.getResources().getColor(R.color.hei333333));
        tv_dpzy_sx_xhx.setBackgroundResource(R.color.touming);
        // dialogin("");
        iOrder = 1;
        iPage = 1;
        shangpin(sId, String.valueOf(iOrder), sCategory, sText, "");
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
                        if (tv_dpzy_is_attention.getText().toString().equals("关注")) {
                            tv_dpzy_is_attention.setBackgroundResource(R.drawable.bj_lan12);
                            tv_dpzy_is_attention.setTextColor(tv_dpzy_is_attention.getResources().getColor(R.color.white));
                            tv_dpzy_is_attention.setText("已关注");
                        } else {
                            tv_dpzy_is_attention.setBackgroundResource(R.drawable.bk_dfdfdf_12);
                            tv_dpzy_is_attention.setTextColor(tv_dpzy_is_attention.getResources().getColor(R.color.hui999999));
                            tv_dpzy_is_attention.setText("关注");
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

    private void jianjei(final String rid) {
        String url = Api.sUrl + "Api/Good/shangjiadetail/appId/" + Api.sApp_Id
                + "/shangjia_id/" + rid + "/user_id/" + sUser_id;
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
                        //店铺ID
                        String id = jsonObjectdate.getString("id");
                        //店铺logo
                        image = jsonObjectdate.getString("logo");
                        //商家名称
                        storename = jsonObjectdate.getString("shangjianame");
                        //是否关注
                        guanzhu = jsonObjectdate.getInt("guanzhu");
                        //商家联系电话
                        phone = jsonObjectdate.getString("lianxiphone");
                        //关注人数
                        count = jsonObjectdate.getString("guanzhu_num");
                        //商家简介
                        shangjia_intro = jsonObjectdate.getString("shangjia_intro");
                        //开始经营时间
                        time = jsonObjectdate.getString("time");
                        Glide.with(DpzyActivity.this)
                                .load( Api.sUrl+image)
                                .asBitmap()
                                .placeholder(R.mipmap.error)
                                .error(R.mipmap.error)
                                .dontAnimate()
                                .into(iv_dpzy_image);
                        tv_dpzy_name.setText(storename);
                        tv_dpzy_count.setText(count);
                        /**
                         * 1关注 0未关注.
                         * */
                        if (guanzhu == 0) {
                            tv_dpzy_is_attention.setBackgroundResource(R.drawable.bk_dfdfdf_12);
                            tv_dpzy_is_attention.setTextColor(tv_dpzy_is_attention.getResources().getColor(R.color.hui999999));
                            tv_dpzy_is_attention.setText("关注");
                        } else {
                            tv_dpzy_is_attention.setBackgroundResource(R.drawable.bj_lan12);
                            tv_dpzy_is_attention.setTextColor(tv_dpzy_is_attention.getResources().getColor(R.color.white));
                            tv_dpzy_is_attention.setText("已关注");
                        }
          /*              JSONArray resultJsonArraycat_id = jsonObject1.getJSONArray("cat_id");
                        for (int i = 0; i < resultJsonArraycat_id.length(); i++) {
                            SpflGridData model = new SpflGridData();
                            jsonObject = resultJsonArraycat_id.getJSONObject(i);
                            model.sName = jsonObject.getString("name");
                            JSONArray resultJsonArraylist = jsonObject.getJSONArray("list");
                            for (int a = 0; a < resultJsonArraylist.length(); a++) {
                                jsonObject = resultJsonArraylist.getJSONObject(a);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("id", jsonObject.getString("id"));
                                map.put("name", jsonObject.getString("name"));
                                model.mylist.add(map);
                            }
                            list_cat_id.add(model);
                        }*/


                        /**
                         * 轮播图
                         * */
                        list_path = new ArrayList<>();
                        list_title = new ArrayList<>();
                        JSONArray jsonArrayCommentCat = jsonObjectdate.getJSONArray("shangjia_img");
                        for (int a = 0; a < jsonArrayCommentCat.length(); a++) {
                            String commentCat = jsonArrayCommentCat.get(a).toString();
                            list_path.add(commentCat);
                            list_title.add("");
                        }
                        //设置样式，里面有很多种样式可以自己都看看效果
                        banner_dpzy.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                        //设置图片加载器
                        banner_dpzy.setImageLoader(new MyLoader());
                        //设置轮播的动画效果,里面有很多种特效,可以都看看效果。
                        banner_dpzy.setBannerAnimation(Transformer.Default);
                        //轮播图片的文字
                        //  banner.setBannerTitles(list_title);
                        //设置轮播间隔时间
                        banner_dpzy.setDelayTime(3000);
                        //设置是否为自动轮播，默认是true
                        banner_dpzy.isAutoPlay(true);
                        //设置指示器的位置，小点点，居中显示
                        banner_dpzy.setIndicatorGravity(BannerConfig.CENTER);
                        //设置图片加载地址
                        banner_dpzy.setImages(list_path)
                                //轮播图的监听
                                .setOnBannerListener(DpzyActivity.this)
                                //开始调用的方法，启动轮播图。
                                .start();

                        JSONArray resultJsonArray = jsonObjectdate.getJSONArray("goodlist");
                        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            //商品ID
                            String resulgoods_id = jsonObject.getString("id");
                            //商品图片
                            String resultoriginal_img = jsonObject.getString("logo");
                            //商品名称
                            String resultgoods_name = jsonObject.getString("name");
                            //商品价格
                            String resultshop_price = jsonObject.getString("price");
                 /*           String resultcomment_count = jsonObject.getString("comment_count");
                            String resultname = jsonObject.getString("name");
                            String resultcomment = jsonObject.getString("comment");*/
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("goods_id", resulgoods_id);
                            map.put("original_img", resultoriginal_img);
                            map.put("goods_name", resultgoods_name);
                            map.put("shop_price", resultshop_price);
           /*                 map.put("comment_count", resultcomment_count);
                            map.put("comment", resultcomment);
                            map.put("name", resultname);*/
                            mylist.add(map);
                        }
                        if (mylist.size() > 0) {
                            tv_dpzy_cp.setText(mylist.get(0).get("name"));
                        }
                        setGridView(mylist);
                       /* JSONArray resultJsonArraycoupons = jsonObject1.getJSONArray("coupons");
                        ArrayList<HashMap<String, String>> mylistcoupons = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < resultJsonArraycoupons.length(); i++) {
                            jsonObject = resultJsonArraycoupons.getJSONObject(i);
                            String resulid = jsonObject.getString("id");
                            String resultrid = jsonObject.getString("rid");
                            String resultamount = jsonObject.getString("amount");
                            String resultdesc = jsonObject.getString("desc");
                            String resultcondition = jsonObject.getString("condition");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", resulid);
                            map.put("rid", resultrid);
                            map.put("amount", resultamount);
                            map.put("desc", resultdesc);
                            map.put("condition", resultcondition);
                            mylistcoupons.add(map);
                        }
                        setGridViewYhj(mylistcoupons);*/
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
                    .load( Api.sUrl+(String) path)
                    .into(imageView);
        }
    }

    /**
     * 商品
     */
    private void shangpin(final String rid, String order, String category, String text, String is_new) {
        String url = Api.sUrl + "Api/Good/goodslist/appId/" + Api.sApp_Id
                + "/page/" + iPage + "/ordertype/" + order + "/shangjia_id/" + rid;
        if (!is_new.equals("")) {
            url = url + "/is_new/" + is_new;
        }
        if (!text.equals("")) {
            url = url + "/keyword/" + text;
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
                    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                    if (resultCode > 0) {
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            String resulgoods_id = jsonObject.getString("id");
                            String resultoriginal_img = jsonObject.getString("logo");
                            String resultgoods_name = jsonObject.getString("name");
                            String resultshop_price = jsonObject.getString("price");
                            String resultcomment_count = jsonObject.getString("comments_count");
                            // String resultname = jsonObject.getString("name");
                            //String resultcomment = jsonObject.getString("comment");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("goods_id", resulgoods_id);
                            map.put("original_img", resultoriginal_img);
                            map.put("goods_name", resultgoods_name);
                            map.put("shop_price", resultshop_price);
                            map.put("comment_count", resultcomment_count);
                            //   map.put("comment", resultcomment);
                            mylist.add(map);
                        }
                        if (iPage == 1) {
                            gridviewdata(mylist);
                        } else {
                            gridviewdata1(mylist);
                        }
                    } else {
                        if (iPage == 1) {
                            gridviewdata(mylist);
                        } else {
                            gridviewdata1(mylist);
                        }
                        if (iPage > 1) {
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

    /**
     * 店铺优惠券
     */
    private void dpyhj(final String rid) {
        String url = Api.sUrl + "Api/Good/goodscoupons/appId/" + Api.sApp_Id
                + "/shangjia_id/" + rid + "/user_id/" + sUser_id;
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

                        JSONArray resultJsonArraydata = jsonObject1.getJSONArray("data");
                        ArrayList<HashMap<String, String>> mylistdata = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < resultJsonArraydata.length(); i++) {
                            jsonObject = resultJsonArraydata.getJSONObject(i);
                            String resulid = jsonObject.getString("id");
                            String resultrid = jsonObject.getString("shangjia_id");
                            String resultamount = jsonObject.getString("youhui");
                            String resultdesc = jsonObject.getString("desc");
                            String resultcondition = jsonObject.getString("zuidi");
                            String resultlingqu = jsonObject.getString("state");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", resulid);
                            map.put("rid", resultrid);
                            map.put("amount", resultamount);
                            map.put("desc", resultdesc);
                            map.put("condition", resultcondition);
                            map.put("lingqu", resultlingqu);
                            mylistdata.add(map);
                        }
                        setGridViewYhj(mylistdata);
                    } else {
                        //Hint(resultMsg, HintDialog.ERROR);
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
        request.setShouldCache(false);
        queue.add(request);
    }

    private void gridviewdata(ArrayList<HashMap<String, String>> myList) {
        // iPage += 1;
        myAdapterSplb = new MyAdapterSplb(this);
        myAdapterSplb.arrlist = myList;
        pull_refresh_grid_dpzy.setAdapter(myAdapterSplb);
        // 刷新适配器
        myAdapterSplb.notifyDataSetChanged();
        // 关闭刷新下拉
        pull_refresh_grid_dpzy.onRefreshComplete();

    }

    private void gridviewdata1(ArrayList<HashMap<String, String>> myList) {
        //    myList = getMenuAdapter();
        //iPage += 1;
        // 刷新适配器
        myAdapterSplb.arrlist.addAll(myList);
        myAdapterSplb.notifyDataSetChanged();
        // Call onRefreshComplete when the list has been refreshed.
        // 关闭上拉加载
        pull_refresh_grid_dpzy.onRefreshComplete();

    }

    /**
     * 宝贝评论
     * 设置GirdView参数，绑定数据
     */
    private void setGridViewYhj(ArrayList<HashMap<String, String>> mylist) {
        MyAdapterYhj myAdapterYhj = new MyAdapterYhj(this);

        myAdapterYhj.arrlist = mylist;
        int size = mylist.size();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)
        int length = 110;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gv_dpzy_yhj.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gv_dpzy_yhj.setColumnWidth(itemWidth); // 设置列表项宽
        gv_dpzy_yhj.setHorizontalSpacing(10); // 设置列表项水平间距
        gv_dpzy_yhj.setStretchMode(GridView.NO_STRETCH);
        gv_dpzy_yhj.setNumColumns(size); // 设置列数量=列表集合数
        gv_dpzy_yhj.setAdapter(myAdapterYhj);
       /* sv_shequn.smoothScrollTo(0, 20);
        sv_shequn.setFocusable(true);*/
    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(DpzyActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(DpzyActivity.this, R.style.dialog, sHint, type, true).show();
    }

    private void Xiangqing(final String pid) {
        String url = Api.sUrl + "Api/Good/gooddetail/appId/" + Api.sApp_Id
                + "/good_id/" + pid + "/user_id/" + sUser_id;
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
                        Intent intent1 = new Intent(DpzyActivity.this, SpxqActivity.class);
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

    private void Lqyhj(final String hong_id) {
        String url = Api.sUrl + "Api/Good/getcoupons/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/hong_id/" + hong_id + "/shangjia_id/" + sId;
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
                        dpyhj(sId);
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
        request.setShouldCache(false);
        queue.add(request);
    }

    /**
     * 设置GirdView参数，绑定数据
     */
    private void setGridView(ArrayList<HashMap<String, String>> mylist) {
        MyAdapter myAdapter = new MyAdapter(DpzyActivity.this);
        myAdapter.arrlist = mylist;
        mgv_dpzy.setAdapter(myAdapter);
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
                view = inflater.inflate(R.layout.dpzy_sp_item, null);
            }
            LinearLayout ll_dpzy_sp_item = view.findViewById(R.id.ll_dpzy_sp_item);
            ImageView iv_dpzy_sp_item_img = view.findViewById(R.id.iv_dpzy_sp_item_img);
            TextView tv_dpzy_sp_item_goods_name = view.findViewById(R.id.tv_dpzy_sp_item_goods_name);
            TextView tv_dpzy_sp_item_shop_price = view.findViewById(R.id.tv_dpzy_sp_item_shop_price);
            TextView tv_dpzy_sp_item_comment_count = view.findViewById(R.id.tv_dpzy_sp_item_comment_count);
            TextView tv_dpzy_sp_item_comment = view.findViewById(R.id.tv_dpzy_sp_item_comment);
            tv_dpzy_sp_item_goods_name.setText(arrlist.get(position).get("goods_name"));
            tv_dpzy_sp_item_shop_price.setText(arrlist.get(position).get("shop_price"));
            tv_dpzy_sp_item_comment_count.setVisibility(View.GONE);
            tv_dpzy_sp_item_comment.setVisibility(View.GONE);
            // tv_dpzy_sp_item_comment_count.setText(arrlist.get(position).get("comment_count") + "条评价");
        /*    if (arrlist.get(position).get("comment").equals("null")) {
            } else {
                tv_dpzy_sp_item_comment.setText("好评" + arrlist.get(position).get("comment") + "%");
            }*/
            Glide.with(DpzyActivity.this)
                    .load( Api.sUrl+arrlist.get(position).get("original_img"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_dpzy_sp_item_img);
            ll_dpzy_sp_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideDialogin();
                    dialogin("");
                    Xiangqing(arrlist.get(position).get("goods_id"));
                }
            });
            return view;
        }
    }

    private class MyAdapterSplb extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        ArrayList<HashMap<String, String>> arrlist;

        public MyAdapterSplb(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arrlist = new ArrayList<HashMap<String, String>>();
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
            if (iLb_Type == 0) {
                view = inflater.inflate(R.layout.dpzy_sp_item, null);
                LinearLayout ll_dpzy_sp_item = view.findViewById(R.id.ll_dpzy_sp_item);
                ImageView iv_dpzy_sp_item_img = view.findViewById(R.id.iv_dpzy_sp_item_img);
                TextView tv_dpzy_sp_item_goods_name = view.findViewById(R.id.tv_dpzy_sp_item_goods_name);
                TextView tv_dpzy_sp_item_shop_price = view.findViewById(R.id.tv_dpzy_sp_item_shop_price);
                TextView tv_dpzy_sp_item_comment_count = view.findViewById(R.id.tv_dpzy_sp_item_comment_count);
                TextView tv_dpzy_sp_item_comment = view.findViewById(R.id.tv_dpzy_sp_item_comment);
                tv_dpzy_sp_item_goods_name.setText(arrlist.get(position).get("goods_name"));
                tv_dpzy_sp_item_shop_price.setText(arrlist.get(position).get("shop_price"));
                tv_dpzy_sp_item_comment_count.setText(arrlist.get(position).get("comment_count") + "条评价");
                tv_dpzy_sp_item_comment_count.setVisibility(View.GONE);
                tv_dpzy_sp_item_comment.setVisibility(View.GONE);
        /*        if (arrlist.get(position).get("comment").equals("null")) {
                } else {
                    tv_dpzy_sp_item_comment.setText("好评" + arrlist.get(position).get("comment") + "%");
                }*/
                Glide.with(DpzyActivity.this)
                        .load( Api.sUrl+arrlist.get(position).get("original_img"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_dpzy_sp_item_img);
                ll_dpzy_sp_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideDialogin();
                        dialogin("");
                        Xiangqing(arrlist.get(position).get("goods_id"));
                    }
                });
            } else if (iLb_Type == 1) {
                view = inflater.inflate(R.layout.dpzy_sp_item_1, null);
                LinearLayout ll_dpzy_sp_item_1 = view.findViewById(R.id.ll_dpzy_sp_item_1);
                ll_dpzy_sp_item_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideDialogin();
                        dialogin("");
                        Xiangqing(arrlist.get(position).get("goods_id"));
                    }
                });
                ImageView iv_dpzy_sp_item_img_1 = view.findViewById(R.id.iv_dpzy_sp_item_img_1);
                TextView tv_dpzy_sp_item_goods_name_1 = view.findViewById(R.id.tv_dpzy_sp_item_goods_name_1);
                TextView tv_dpzy_sp_item_shop_price_1 = view.findViewById(R.id.tv_dpzy_sp_item_shop_price_1);
                TextView tv_dpzy_sp_item_comment_count_1 = view.findViewById(R.id.tv_dpzy_sp_item_comment_count_1);
                TextView tv_dpzy_sp_item_comment_1 = view.findViewById(R.id.tv_dpzy_sp_item_comment_1);

                Glide.with(DpzyActivity.this)
                        .load( Api.sUrl+arrlist.get(position).get("original_img"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_dpzy_sp_item_img_1);
        /*        if (arrlist.get(position).get("comment").equals("null")) {
                } else {
                    tv_dpzy_sp_item_comment_1.setText("好评" + arrlist.get(position).get("comment") + "%");
                }*/
                tv_dpzy_sp_item_goods_name_1.setText(arrlist.get(position).get("goods_name"));
                tv_dpzy_sp_item_shop_price_1.setText(arrlist.get(position).get("shop_price"));
                tv_dpzy_sp_item_comment_count_1.setText(arrlist.get(position).get("comment_count") + "条评价");
            }
            return view;
        }
    }

    private class MyAdapterYhj extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        ArrayList<HashMap<String, String>> arrlist;

        public MyAdapterYhj(Context context) {
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
                view = inflater.inflate(R.layout.dpzy_yhj_item, null);
            }
            TextView tv_dpzy_yhj_item_amount = view.findViewById(R.id.tv_dpzy_yhj_item_amount);
            tv_dpzy_yhj_item_amount.setText(arrlist.get(position).get("amount"));
            TextView tv_dpzy_yhj_item_desc = view.findViewById(R.id.tv_dpzy_yhj_item_desc);
            TextView tv_dpzy_yhj_item_desc1 = view.findViewById(R.id.tv_dpzy_yhj_item_desc1);
            tv_dpzy_yhj_item_desc.setText(arrlist.get(position).get("desc"));
            //tv_dpzy_yhj_item_desc1.setText(arrlist.get(position).get("desc"));
            TextView tv_dpzy_yhj_item_ljlq = view.findViewById(R.id.tv_dpzy_yhj_item_ljlq);
            if (arrlist.get(position).get("lingqu").equals("1")) {
                tv_dpzy_yhj_item_ljlq.setBackgroundResource(R.drawable.bj_666666_6);
                tv_dpzy_yhj_item_ljlq.setClickable(false);
                tv_dpzy_yhj_item_ljlq.setText("已领取");
            } else {
                tv_dpzy_yhj_item_ljlq.setBackgroundResource(R.drawable.bj_theme_6);
                tv_dpzy_yhj_item_ljlq.setClickable(true);
                tv_dpzy_yhj_item_ljlq.setText("立即领取");
            }
            tv_dpzy_yhj_item_ljlq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideDialogin();
                    dialogin("");
                    Lqyhj(arrlist.get(position).get("id"));
                }
            });

            return view;
        }
    }
}
