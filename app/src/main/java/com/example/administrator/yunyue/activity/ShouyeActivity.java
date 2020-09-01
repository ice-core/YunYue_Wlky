package com.example.administrator.yunyue.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Base64;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.administrator.yunyue.sc_activity.FenleiActivity;
import com.example.administrator.yunyue.sc_activity.FukuanActivity;
import com.example.administrator.yunyue.sc_activity.GuanggaoActivity;
import com.example.administrator.yunyue.sc_activity.GwcActivity;
import com.example.administrator.yunyue.sc_activity.SplbActivity;
import com.example.administrator.yunyue.sc_activity.SpxqActivity;
import com.example.administrator.yunyue.sc_activity.XsmsActivity;
import com.example.administrator.yunyue.sc_fragment.Shouye2Fragment;
import com.example.administrator.yunyue.sc_gridview.GridViewAdaptershangcheng;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.loonggg.rvbanner.lib.RecyclerViewBanner;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

public class ShouyeActivity extends AppCompatActivity {
    List<String> catList1_ad_id = new ArrayList<String>();//图片id
    List<String> catList1_ad_link = new ArrayList<String>();//图片链接

    RecyclerViewBanner recyclerViewBanner1;
    RecyclerViewBanner recyclerViewBanner2;
    RecyclerViewBanner recyclerViewBanner3;
    private MyGridView gv_shangcheng;

    private MyGridView gv_shangcheng_hot;
    private LinearLayout ll_shangcheng_query;
    RequestQueue queue = null;
    private GridViewAdaptershangcheng mAdapter;
    //Fragment的View加载完毕的标记
    private boolean isViewCreated;
    //Fragment对用户可见的标记
    private boolean isUIVisible;

    private LinearLayout ll_shouye_saomiao;
    private int REQUEST_CODE_SCAN = 111;
    public static String sSaomiao = "";
    private MyGridView gv_shangcheng_mx;
    private LinearLayout ll_shangcheng_item_mx;
    private TextView tv_shangcheng_mx_gdsp;
    private TextView tv_shangcheng_mx_time_s, tv_shangcheng_mx_time_f, tv_shangcheng_mx_time_m;
    /**
     * 热门活动
     */
    private ImageView iv_shouye_rmhd1_yhj, iv_shouye_rmhd_bk, iv_shouye_rmhd_tqg, iv_shouye_rmhd_jhs;

    /**
     * 专场
     */
    private MyGridView mgv_shouye2_zhuanchang;
    private Button bt;
    private String sUser_id = "";
    private SharedPreferences pref;
    /**
     * 购物车
     */
    private ImageView iv_shouye2_gwc;

    int itime = 0;
    private String resultLinetime = "";
    private LinearLayout ll_shangcheng_item_mx1;
    private long totalSec = 0;
    @BindView(R.id.srl_control)
    SmartRefreshLayout srlControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.fragment_shouye2);
        pref = PreferenceManager.getDefaultSharedPreferences(ShouyeActivity.this);
        sUser_id = pref.getString("user_id", "");
        recyclerViewBanner1 = (RecyclerViewBanner) findViewById(R.id.rv_banner_1);
        recyclerViewBanner2 = (RecyclerViewBanner) findViewById(R.id.rv_banner_2);
        recyclerViewBanner3 = (RecyclerViewBanner) findViewById(R.id.rv_banner_3);

        ll_shangcheng_query = (LinearLayout) findViewById(R.id.ll_shangcheng_query);
        tv_shangcheng_mx_time_s = findViewById(R.id.tv_shangcheng_mx_time_s);
        tv_shangcheng_mx_time_f = findViewById(R.id.tv_shangcheng_mx_time_f);
        tv_shangcheng_mx_time_m = findViewById(R.id.tv_shangcheng_mx_time_m);
        ll_shangcheng_item_mx = findViewById(R.id.ll_shangcheng_item_mx);
        gv_shangcheng_mx = findViewById(R.id.gv_shangcheng_mx);
        ll_shouye_saomiao = findViewById(R.id.ll_shouye_saomiao);
        tv_shangcheng_mx_gdsp = findViewById(R.id.tv_shangcheng_mx_gdsp);
        iv_shouye_rmhd1_yhj = findViewById(R.id.iv_shouye_rmhd1_yhj);

        iv_shouye_rmhd_bk = findViewById(R.id.iv_shouye_rmhd_bk);
        iv_shouye_rmhd_tqg = findViewById(R.id.iv_shouye_rmhd_tqg);
        iv_shouye_rmhd_jhs = findViewById(R.id.iv_shouye_rmhd_jhs);
        mgv_shouye2_zhuanchang = findViewById(R.id.mgv_shouye2_zhuanchang);
        iv_shouye2_gwc = findViewById(R.id.iv_shouye2_gwc);

        tv_shangcheng_mx_gdsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShouyeActivity.this, XsmsActivity.class);
                startActivity(intent);
            }
        });
        ll_shangcheng_item_mx1 = findViewById(R.id.ll_shangcheng_item_mx1);
        ll_shangcheng_item_mx1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShouyeActivity.this, XsmsActivity.class);
                startActivity(intent);
            }
        });
        ll_shouye_saomiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndPermission.with(ShouyeActivity.this)
                        .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
                        .onGranted(new Action() {
                            @Override
                            public void onAction(List<String> permissions) {
                                Intent intent = new Intent(ShouyeActivity.this, CaptureActivity.class);
                                /*ZxingConfig是配置类
                                 *可以设置是否显示底部布局，闪光灯，相册，
                                 * 是否播放提示音  震动
                                 * 设置扫描框颜色等
                                 * 也可以不传这个参数
                                 * */
                                ZxingConfig config = new ZxingConfig();
                                // config.setPlayBeep(false);//是否播放扫描声音 默认为true
                                //  config.setShake(false);//是否震动  默认为true
                                // config.setDecodeBarCode(false);//是否扫描条形码 默认为true
//                                config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
//                                config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
//                                config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
                                config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
                                intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                                startActivityForResult(intent, REQUEST_CODE_SCAN);
                            }
                        })
                        .onDenied(new Action() {
                            @Override
                            public void onAction(List<String> permissions) {
                            /*    Uri packageURI = Uri.parse("package:" + getPackageName());
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);*/

                                Toast.makeText(ShouyeActivity.this, "没有权限无法扫描呦", Toast.LENGTH_LONG).show();
                            }
                        }).start();

            }
        });
        ll_shangcheng_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShouyeActivity.this, SplbActivity.class);
                intent.putExtra("", "shangcheng");
                startActivity(intent);
            }
        });
        gv_shangcheng = (MyGridView) findViewById(R.id.gv_shangcheng);
        gv_shangcheng_hot = (MyGridView) findViewById(R.id.gv_shangcheng_hot);
        queue = Volley.newRequestQueue(ShouyeActivity.this);
        srlControl = findViewById(R.id.srl_control);
        gv_shangcheng.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> map = (HashMap<String, String>) gv_shangcheng.getItemAtPosition(i);
                String sId = map.get("id");
                String sMiaoshu = map.get("name");
                if (sId.equals("0")) {
                    Intent intent = new Intent(ShouyeActivity.this, FenleiActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ShouyeActivity.this, SplbActivity.class);
                    intent.putExtra("id", sId);
                    intent.putExtra("miaoshu", sMiaoshu);
                    startActivity(intent);
                }
            }
        });
        smartRefresh();
        isViewCreated = true;

        bt = findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shangcheng();
                mx();
            }
        });
        iv_shouye2_gwc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShouyeActivity.this, GwcActivity.class);
                startActivity(intent);
            }
        });
        //lazyLoad();
        //dialogin("");
        shangcheng();
        mx();
    }

    //监听下拉和上拉状态
    public void smartRefresh() {
        //下拉刷新
        srlControl.setOnRefreshListener(refreshlayout -> {
            srlControl.setEnableRefresh(true);//启用刷新
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 Data() 来模拟我们刷新出来的数据
             */
            shangcheng();
            mx();
            // refreshAdapter.strList.clear();
            // refreshAdapter.refreshData(Data());
            //  iPage = 1;
            // query();
            // srlControl.finishRefresh();//结束刷新
        });
        //上拉加载
        srlControl.setOnLoadmoreListener(refreshlayout -> {
            srlControl.setEnableLoadmore(true);//启用加载
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 MoreDatas() 来模拟我们加载出来的数据
             */
            //  refreshAdapter.loadMore(MoreDatas());
            //  iPage += 1;
            //  query();
            srlControl.finishLoadmore();//结束加载
        });
    }


    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    update();
                    break;
            }
            super.handleMessage(msg);
        }

        void update() {
//刷新msg的内容
            mx();
        }

    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            totalSec--;
            String formatLongToTimeStr = formatLongToTimeStr(totalSec);
            String[] split = formatLongToTimeStr.split(":");
            for (int i = 0; i < split.length; i++) {
                if (i == 0) {
                    String s = "";
                    if (Integer.valueOf(split[0]) < 10) {
                        s = "0" + split[0];
                    } else {
                        s = split[0];
                    }
                    tv_shangcheng_mx_time_s.setText(s);

                }
                if (i == 1) {
                    String f = "";
                    if (Integer.valueOf(split[1]) < 10) {
                        f = "0" + split[1];
                    } else {
                        f = split[1];
                    }
                    tv_shangcheng_mx_time_f.setText(f);
                }
                if (i == 2) {
                    String m = "";
                    if (Integer.valueOf(split[2]) < 10) {
                        m = "0" + split[2];
                    } else {
                        m = split[2];
                    }
                    tv_shangcheng_mx_time_m.setText(m);
                }

            }
            if (totalSec > 0) {
                handler.postDelayed(this, 1000);
            }
        }
    };

    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    public String formatLongToTimeStr(Long l) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        second = l.intValue();
        if (second > 60) {
            minute = second / 60;   //取整
            second = second % 60;   //取余
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        String strtime = hour + ":" + minute + ":" + second;
        return strtime;
    }

    /**
     * 秒杀
     */
    private void mx() {
        String url = Api.sUrl + "Api/Good/miaoshaindex/appId/" + Api.sApp_Id;
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
                        ll_shangcheng_item_mx.setVisibility(View.VISIBLE);
                        String resultData = jsonObject1.getString("data");
                        JSONObject jsonObject_resultData = new JSONObject(resultData);
                        MyAdapterMx myAdapterMx = new MyAdapterMx(ShouyeActivity.this);
                        ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
                        resultLinetime = jsonObject_resultData.getString("linetime");
                        if (itime == 0) {
                            itime = 1;
                            tiem();
                        } else {
                            handler.removeCallbacks(runnable);
                            timer.cancel();
                            tiem();
                        }
                        JSONArray resultJsonArray = jsonObject_resultData.getJSONArray("goodlsit");
                        int num = 0;
                        if (resultJsonArray.length() > 3) {
                            num = 3;
                        } else {
                            num = resultJsonArray.length();
                        }
                        for (int i = 0; i < num; i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            String resultId = jsonObject.getString("id");
                            String resultName = jsonObject.getString("name");
                            String resultLogo = jsonObject.getString("logo");
                            String resultPrice = jsonObject.getString("price");
                            String resultMktprice = jsonObject.getString("mktprice");
                            String resultStock = jsonObject.getString("stock");
                            String resultBuy_count = jsonObject.getString("buy_count");
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("id", resultId);
                            map.put("name", resultName);
                            map.put("logo", resultLogo);
                            map.put("price", resultPrice);
                            map.put("mktprice", resultMktprice);
                            map.put("stock", resultStock);
                            map.put("buy_count", resultBuy_count);
                            mylist.add(map);
                        }
                        myAdapterMx.arrmylist = mylist;
                        gv_shangcheng_mx.setAdapter(myAdapterMx);
                        if (num == 0) {
                            ll_shangcheng_item_mx.setVisibility(View.GONE);
                        } else {
                            ll_shangcheng_item_mx.setVisibility(View.VISIBLE);
                        }

                    } else {

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
     * 倒计时
     */
    private void tiem() {
        timer.cancel();
        task.cancel();
        timer = new Timer();
        task = new TimerTask() {
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        // resultLinetime = "00:00:15";
        String[] my = resultLinetime.split(":");
        int hour = Integer.parseInt(my[0]);
        int min = Integer.parseInt(my[1]);
        int sec = Integer.parseInt(my[2]);
        totalSec = hour * 3600 + min * 60 + sec;
        timer.schedule(task, totalSec * 1000, totalSec * 1000);
        handler.postDelayed(runnable, 1000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
                //   Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
                //    result.setText("扫描结果为：" + content);
                sSaomiao = content;
                JSONObject jsonObjectdate = null;
                try {
                    jsonObjectdate = new JSONObject(Shouye2Fragment.sSaomiao);
                    String sRdsQrcode = jsonObjectdate.getString("rdsQrcode");
                    String sStorename = jsonObjectdate.getString("storename");
                    Intent intent = new Intent(ShouyeActivity.this, FukuanActivity.class);
                    startActivity(intent);
                } catch (JSONException e) {
                    Hint("非本系统二维码，无法识别", HintDialog.ERROR);
                    //    Toast.makeText(getActivity(), "非本系统二维码，无法识别", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }
    }

/*    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }*/

    /**
     * 商品详情
     */
    private void jianjei(final String pid) {
        String url = Api.sUrl + "Api/Good/gooddetail/appId/" + Api.sApp_Id
                + "/good_id/" + pid + "/user_id/" + sUser_id;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        Intent intent1 = new Intent(ShouyeActivity.this, SpxqActivity.class);
                        intent1.putExtra("goods_id", pid);
                        intent1.putExtra("data", sDate);
                        intent1.putExtra("activty", "main");
                        startActivity(intent1);
                    } else {
                        Hint(resultMsg, HintDialog.ERROR);
                    }
                    hideDialogin();
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

    private void lazyLoad() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUIVisible) {
            dialogin("");
            shangcheng();
            mx();
            //  tiem();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;
            //    printLog(mTextviewContent+"可见,加载数据");
        }
    }

    private void shangcheng() {
        String url =Api.sUrl + "Api/Good/shopindexApp/appId/" + Api.sApp_Id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                srlControl.finishRefresh();//结束刷新
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    JSONObject jsonObject0 = new JSONObject(sDate);
                    String resultMsg = jsonObject0.getString("msg");
                    int resultCode = jsonObject0.getInt("code");
                    if (resultCode > 0) {

                        JSONObject jsonObject1 = new JSONObject(sDate.toString()).getJSONObject("data");
                        JSONArray jsonArray = jsonObject1.getJSONArray("typelist");
                        ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
                            String ItemId = jsonObject2.getString("id");
                            String ItemName = jsonObject2.getString("name");
                            String ItemLogo = jsonObject2.getString("logo");
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("logo", ItemLogo);
                            map.put("name", ItemName);
                            map.put("id", ItemId);
                            mylist.add(map);
                        }
                        gridviewdata(mylist);
                        JSONArray jsonArray_lun = jsonObject1.getJSONArray("lun");
                        final List<Banner> banners = new ArrayList<>();
                        for (int i = 0; i < jsonArray_lun.length(); i++) {
                            JSONObject jsonObject5 = (JSONObject) jsonArray_lun.opt(i);
                            catList1_ad_link.add(jsonObject5.getString("link"));
                            catList1_ad_id.add(jsonObject5.getString("id"));

                            banners.add(new Banner(jsonObject5.getString("url")));
                        }
                        recyclerViewBanner1.setRvBannerData(banners);
                        recyclerViewBanner1.setOnSwitchRvBannerListener(new RecyclerViewBanner.OnSwitchRvBannerListener() {
                            @Override
                            public void switchBanner(int position, AppCompatImageView bannerView) {
                                Glide.with(bannerView.getContext())
                                        .load( Api.sUrl+banners.get(position).getUrl())
                                        .asBitmap().dontAnimate()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .into(bannerView);
                            }
                        });
                        recyclerViewBanner1.setOnRvBannerClickListener(new RecyclerViewBanner.OnRvBannerClickListener() {
                            @Override
                            public void onClick(int position) {
                                dialogin("");
                                gglm(catList1_ad_id.get(position), catList1_ad_link.get(position));
                            }
                        });
                        try {
                            String result_you = jsonObject1.getString("you");
                            JSONObject jsonObject_result_you = new JSONObject(result_you);
                            String result_you_url = jsonObject_result_you.getString("url");
                            if (result_you_url.equals("")) {
                                iv_shouye_rmhd1_yhj.setVisibility(View.GONE);
                            } else {
                                iv_shouye_rmhd1_yhj.setVisibility(View.VISIBLE);
                                Glide.with(ShouyeActivity.this)
                                        .load( Api.sUrl+ result_you_url)
                                        .asBitmap().dontAnimate()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .into(iv_shouye_rmhd1_yhj);
                            }
                        } catch (JSONException e) {
                            iv_shouye_rmhd1_yhj.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                        try {
                            String result_bao = jsonObject1.getString("bao");
                            JSONObject jsonObject_result_bao = new JSONObject(result_bao);
                            String result_bao_url = jsonObject_result_bao.getString("url");
                            final String resultId = jsonObject_result_bao.getString("id");
                            if (result_bao_url.equals("")) {
                                iv_shouye_rmhd_bk.setVisibility(View.GONE);
                            } else {
                                iv_shouye_rmhd_bk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(ShouyeActivity.this, SplbActivity.class);
                                        intent.putExtra("er_id", resultId);
                                        intent.putExtra("miaoshu", "爆款集合");
                                        startActivity(intent);
                                    }
                                });
                                iv_shouye_rmhd_bk.setVisibility(View.VISIBLE);
                                Glide.with(ShouyeActivity.this)
                                        .load( Api.sUrl+ result_bao_url)
                                        .asBitmap().dontAnimate()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .into(iv_shouye_rmhd_bk);
                            }
                        } catch (JSONException e) {
                            iv_shouye_rmhd_bk.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                        try {
                            String result_tao = jsonObject1.getString("tao");
                            JSONObject jsonObject_result_tao = new JSONObject(result_tao);
                            String result_tao_url = jsonObject_result_tao.getString("url");
                            final String resultId = jsonObject_result_tao.getString("id");
                            if (result_tao_url.equals("")) {
                                iv_shouye_rmhd_tqg.setVisibility(View.GONE);
                            } else {
                                iv_shouye_rmhd_tqg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(ShouyeActivity.this, SplbActivity.class);
                                        intent.putExtra("er_id", resultId);
                                        intent.putExtra("miaoshu", "淘抢购");
                                        startActivity(intent);
                                    }
                                });
                                iv_shouye_rmhd_tqg.setVisibility(View.VISIBLE);
                                Glide.with(ShouyeActivity.this)
                                        .load( Api.sUrl+ result_tao_url)
                                        .asBitmap().dontAnimate()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .into(iv_shouye_rmhd_tqg);
                            }
                        } catch (JSONException e) {
                            iv_shouye_rmhd_tqg.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                        try {
                            String result_ju = jsonObject1.getString("ju");
                            JSONObject jsonObject_result_ju = new JSONObject(result_ju);
                            String result_ju_url = jsonObject_result_ju.getString("url");
                            final String resultId = jsonObject_result_ju.getString("id");
                            if (result_ju_url.equals("")) {
                                iv_shouye_rmhd_jhs.setVisibility(View.GONE);
                            } else {
                                iv_shouye_rmhd_jhs.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(ShouyeActivity.this, SplbActivity.class);
                                        intent.putExtra("er_id", resultId);
                                        intent.putExtra("miaoshu", "聚划算");
                                        startActivity(intent);
                                    }
                                });
                                iv_shouye_rmhd_jhs.setVisibility(View.VISIBLE);
                                Glide.with(ShouyeActivity.this)
                                        .load( Api.sUrl+result_ju_url)
                                        .asBitmap().dontAnimate()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .into(iv_shouye_rmhd_jhs);
                            }
                        } catch (JSONException e) {
                            iv_shouye_rmhd_jhs.setVisibility(View.GONE);
                            e.printStackTrace();
                        }

                        /**
                         * 专场
                         * */
                        MyAdapterZhuanchang myAdapterZhuanchang = new MyAdapterZhuanchang(ShouyeActivity.this);
                        ArrayList<HashMap<String, String>> mylistZhuanchang = new ArrayList<HashMap<String, String>>();
                        JSONArray jsonArrayzhuanchang = jsonObject1.getJSONArray("zhuantype");
                        for (int i = 0; i < jsonArrayzhuanchang.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArrayzhuanchang.opt(i);
                            String ItemId = jsonObject2.getString("id");
                            String ItemiName = jsonObject2.getString("name");
                            //   String ItemiDescribe = jsonObject2.getString("describe");
                            String ItemiZhuanimg = jsonObject2.getString("zhuanimg");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", ItemId);
                            map.put("name", ItemiName);
                            //  map.put("describe", ItemiDescribe);
                            map.put("zhuanimg", ItemiZhuanimg);
                            mylistZhuanchang.add(map);
                        }
                        myAdapterZhuanchang.arrlist = mylistZhuanchang;
                        mgv_shouye2_zhuanchang.setAdapter(myAdapterZhuanchang);

                        /**
                         * 精品优选
                         * */
                        MyAdapter myAdapter = new MyAdapter(ShouyeActivity.this);
                        JSONArray jsonArray_jinpinggoods = jsonObject1.getJSONArray("jinpinggoods");
                        ArrayList<HashMap<String, String>> mylist_jsonArray_jinpinggoods = new ArrayList<HashMap<String, String>>();
                        //  for (int i = 0; i < 2; i++) {
                        for (int i = 0; i < jsonArray_jinpinggoods.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArray_jinpinggoods.opt(i);
                            String ItemId = jsonObject2.getString("id");
                            String ItemName = jsonObject2.getString("name");
                            String ItemLogo = jsonObject2.getString("logo");
                            String ItemPrice = jsonObject2.getString("price");

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("logo", ItemLogo);
                            map.put("name", ItemName);
                            map.put("id", ItemId);
                            map.put("price", ItemPrice);
                            mylist_jsonArray_jinpinggoods.add(map);
                        }
                        myAdapter.arrlist = mylist_jsonArray_jinpinggoods;
                        gv_shangcheng_hot.setAdapter(myAdapter);


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
                srlControl.finishRefresh();//结束刷新
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        request.setShouldCache(false);
        queue.add(request);
    }

    /**
     * 广告联盟
     */
    private void gglm(final String sId, String sLink) {
        String url = Api.sUrl + "Api/Good/getguanggao/appId/" + Api.sApp_Id
                + "/id/" + sId + "/type/1";
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    Intent intent = new Intent(ShouyeActivity.this, GuanggaoActivity.class);
                    intent.putExtra("link", sLink);
                    startActivity(intent);
                /*    if (resultCode > 0) {
                        Intent intent = new Intent(getActivity(), GuanggaoActivity.class);
                        intent.putExtra("link", sLink);
                        startActivity(intent);
                    } else {
                        Hint(resultMsg, HintDialog.ERROR);
                    }*/
                    hideDialogin();
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

    private void gridviewdata(final ArrayList<HashMap<String, Object>> myList) {
        //    myList = getMenuAdapter();
        mAdapter = new GridViewAdaptershangcheng(ShouyeActivity.this, myList);
        int num = 0;
        num = myList.size();
        if (num > 10) {
            num = 5;
        } else if (num > 5) {
            // num = num % 2;
            if (num % 2 == 0) {
                num = num / 2;
            } else {
                num = (num + 1) / 2;
            }
        }
        gv_shangcheng.setNumColumns(num);
        gv_shangcheng.setAdapter(mAdapter);

    }

    private class Banner {

        String url;

        public Banner(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(ShouyeActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(ShouyeActivity.this, R.style.dialog, sHint, type, true).show();
    }

    /**
     * 专场
     */
    private class MyAdapterZhuanchang extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        ArrayList<HashMap<String, String>> arrlist;

        public MyAdapterZhuanchang(Context context) {
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
            if (view == null) {
                view = inflater.inflate(R.layout.shouye2_zhuanchang_item, null);
            }
            LinearLayout ll_shouye2_zhuanchang_item = view.findViewById(R.id.ll_shouye2_zhuanchang_item);
            TextView tv_shouye2_zhuanchang_item_name = view.findViewById(R.id.tv_shouye2_zhuanchang_item_name);
            TextView tv_shouye2_zhuanchang_item_describe = view.findViewById(R.id.tv_shouye2_zhuanchang_item_describe);
            ImageView iv_shouye2_zhuanchang_item_image = view.findViewById(R.id.iv_shouye2_zhuanchang_item_image);
            tv_shouye2_zhuanchang_item_name.setText(arrlist.get(position).get("name"));
            //  tv_shouye2_zhuanchang_item_describe.setText(arrlist.get(position).get("describe"));
            Glide.with(ShouyeActivity.this).load( Api.sUrl+ arrlist.get(position).get("zhuanimg"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_shouye2_zhuanchang_item_image);
            ll_shouye2_zhuanchang_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ShouyeActivity.this, SplbActivity.class);
                    intent.putExtra("er_id", arrlist.get(position).get("id"));
                    intent.putExtra("miaoshu", arrlist.get(position).get("name"));
                    startActivity(intent);
                }
            });


            return view;
        }
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        ArrayList<HashMap<String, String>> arrlist;

        public MyAdapter(Context context) {
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

        public Bitmap stringToBitmap(String string) {    // 将字符串转换成Bitmap类型
            Bitmap bitmap = null;
            try {
                byte[] bitmapArray;
                bitmapArray = Base64.decode(string, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }


        @Override
        public View getView(final int position, View view, ViewGroup arg2) {
            // TODO Auto-generated method stub
            if (view == null) {
                view = inflater.inflate(R.layout.shangcheng_hot_item, null);
            }

            ImageView iv_shangcheng_hot_img = (ImageView) view.findViewById(R.id.iv_shangcheng_hot_img);
            LinearLayout ll_shangcheng_hot_item = (LinearLayout) view.findViewById(R.id.ll_shangcheng_hot_item);
            TextView iv_shangcheng_hot_name = (TextView) view.findViewById(R.id.iv_shangcheng_hot_name);
            TextView iv_shangcheng_hot_remark = (TextView) view.findViewById(R.id.iv_shangcheng_hot_remark);
            TextView iv_shangcheng_hot_price = (TextView) view.findViewById(R.id.iv_shangcheng_hot_price);
            iv_shangcheng_hot_name.setText(arrlist.get(position).get("name"));
            iv_shangcheng_hot_price.setText(arrlist.get(position).get("price"));
            Glide.with(ShouyeActivity.this).load( Api.sUrl+ arrlist.get(position).get("logo"))
                    .asBitmap()
                    .dontAnimate()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .into(iv_shangcheng_hot_img);
            ll_shangcheng_hot_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogin("");
                    jianjei(arrlist.get(position).get("id"));
                }
            });

            return view;
        }
    }

    private class MyAdapterMx extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        public ArrayList<HashMap<String, Object>> arrmylist;


        public MyAdapterMx(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);

            arrmylist = new ArrayList<HashMap<String, Object>>();

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
                view = inflater.inflate(R.layout.shangcheng_ms_item, null);
            }
            LinearLayout ll_shangcheng_mx_item = view.findViewById(R.id.ll_shangcheng_mx_item);
            ImageView iv_shangcheng_mx_img = view.findViewById(R.id.iv_shangcheng_mx_img);
            TextView tv_shangcheng_mx_name = view.findViewById(R.id.tv_shangcheng_mx_name);
            TextView tv_shangcheng_mx_shop_price = view.findViewById(R.id.tv_shangcheng_mx_shop_price);
            TextView tv_shangcheng_mx_sales_sum = view.findViewById(R.id.tv_shangcheng_mx_sales_sum);
            tv_shangcheng_mx_name.setText(arrmylist.get(position).get("name").toString());
            tv_shangcheng_mx_sales_sum.setText("已抢" + arrmylist.get(position).get("buy_count").toString() + "件");
            tv_shangcheng_mx_shop_price.setText(arrmylist.get(position).get("price").toString());
            zsy(tv_shangcheng_mx_shop_price);
            Glide.with(ShouyeActivity.this).load( Api.sUrl+ arrmylist.get(position).get("logo"))
                    .asBitmap()
                    .dontAnimate()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .into(iv_shangcheng_mx_img);
            ll_shangcheng_mx_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogin("");
                    jianjei(arrmylist.get(position).get("id").toString());
                }
            });


            return view;
        }
    }

    private void zsy(TextView view) {
        //获取80dp转换后的设备pix值。
        int mTextViewWidth = dip2px(ShouyeActivity.this, 60);
        while (true) {
            //计算所有文本占有的屏幕宽度(pix)
            float textWidth = view.getPaint().measureText(view.getText().toString());

            //如果所有文本的宽度超过TextView自身限定的宽度，那么就尝试迭代的减小字体的textSize，直到不超过TextView的宽度为止。
            if (textWidth > mTextViewWidth) {
                int textSize = (int) view.getTextSize();
                textSize = textSize - 2;
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            } else {
                break;
            }
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
