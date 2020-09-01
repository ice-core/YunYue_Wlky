/**
 * 文件名：Fjsc_ShouyeActivity
 * 描  述：附近商城首页
 * 作  者：icecore
 * 版  权：
 */
package com.example.administrator.yunyue.fjsc_activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.bumptech.glide.Glide;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.AppApplication;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.data.Fjsc_Shouye_SjlbData;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc_activity.GuanggaoActivity;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.service.LocationService;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.loonggg.rvbanner.lib.RecyclerViewBanner;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;


public class Fjsc_ShouyeActivity extends AppCompatActivity {
    private static final String TAG = Fjsc_ShouyeActivity.class.getSimpleName();
    @BindView(R.id.srl_control_fjsc)
    SmartRefreshLayout srlControlFjsc;
    //店铺列表分页
    int iPage = 1;
    private LocationService locationService;

    private String sType_Id = "",
            sKeyword = "";
    /**
     * 商家服务id（筛选）
     */
    private int iService_Id = 0;
    /**
     * 1为品牌商家 2为新商家 （筛选
     */
    private int iSift_Id = 0;
    /**
     * 1为综合排序 2为销量排序 3距离排序
     */
    int iOrdertype = 1;

    public static Boolean ISQUERY = true;
    private MyAdapter myAdapter;
    List<String> catList1_ad_id = new ArrayList<String>();//图片id
    List<String> catList1_ad_link = new ArrayList<String>();//图片链接

    RecyclerViewBanner rv_banner_fjsc;

    /**
     * 附近商城商家列表
     */
    private MyGridView mgv_fjsc_shouye;
    /**
     * 分类
     */
    private MyGridView mgv_fjsc_shouye_fenlei;
    /**
     * 返回
     */
    private LinearLayout ll_fjsc_shouye_back;

    /**
     * 定位地址
     */
    private TextView tv_fjsc_shouye_hine;
    public static String sHint = "";
    //维度
    public static String sLatitude = "31.252884";
    //经度
    public static String sLongitude = "121.424210";
    /**
     * 购物车
     */
    private ImageView iv_fjsc_gwc;

    /**
     * 综合排序
     */
    private TextView tv_zbsc_zh;
    /**
     * 销量排序
     */
    private TextView tv_zbsc_xl;
    /**
     * 距离排序
     */
    private TextView tv_zbsc_jl;
    /**
     * 排序筛选
     */
    private LinearLayout ll_zbsc_sx;
    private TextView tv_zbsc_sx;
    private LinearLayout ll_fjsc_shouye_shaixuan;
    /**
     * 筛选完成
     */
    private TextView tv_fjsc_shouye_sx_wc;
    /**
     * 筛选重置
     */
    private TextView tv_fjsc_shouye_sx_cz;
    /**
     * 筛选阴影
     */
    private TextView tv_fjsc_shouye_sxyy;
    /**
     * 搜索
     */
    private LinearLayout ll_fjsc_shouye_query;
    /**
     * 精选服务列表
     */
    private MyGridView mgv_fjsc_shouye_jxfw;
    /**
     * 商家服务列表
     */
    private MyGridView mgv_fjsc_shouye_sjfw;
    private ScrollView sv_control_fjsc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_fjsc__shouye);
        rv_banner_fjsc = findViewById(R.id.rv_banner_fjsc);
        mgv_fjsc_shouye = findViewById(R.id.mgv_fjsc_shouye);
        mgv_fjsc_shouye_fenlei = findViewById(R.id.mgv_fjsc_shouye_fenlei);
        srlControlFjsc = findViewById(R.id.srl_control_fjsc);
        tv_fjsc_shouye_hine = findViewById(R.id.tv_fjsc_shouye_hine);
        ll_fjsc_shouye_back = findViewById(R.id.ll_fjsc_shouye_back);
        sv_control_fjsc = findViewById(R.id.sv_control_fjsc);
        iv_fjsc_gwc = findViewById(R.id.iv_fjsc_gwc);
        iv_fjsc_gwc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Fjsc_ShouyeActivity.this, Fjsc_GwcActivity.class);
                startActivity(intent);
            }
        });
        ll_fjsc_shouye_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        judgePermission();
        smartRefresh();
        sShopindexApp();
        sNearbyshopfiltrate();
        tv_fjsc_shouye_hine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Fjsc_ShouyeActivity.this, Fjsc_XzshdzActivity.class);
                startActivity(intent);
            }
        });
        ll_fjsc_shouye_query = findViewById(R.id.ll_fjsc_shouye_query);
        tv_zbsc_zh = findViewById(R.id.tv_zbsc_zh);
        tv_zbsc_xl = findViewById(R.id.tv_zbsc_xl);
        tv_zbsc_jl = findViewById(R.id.tv_zbsc_jl);
        tv_zbsc_sx = findViewById(R.id.tv_zbsc_sx);
        ll_zbsc_sx = findViewById(R.id.ll_zbsc_sx);
        ll_fjsc_shouye_shaixuan = findViewById(R.id.ll_fjsc_shouye_shaixuan);
        tv_fjsc_shouye_sx_wc = findViewById(R.id.tv_fjsc_shouye_sx_wc);
        tv_fjsc_shouye_sx_cz = findViewById(R.id.tv_fjsc_shouye_sx_cz);
        mgv_fjsc_shouye_jxfw = findViewById(R.id.mgv_fjsc_shouye_jxfw);
        mgv_fjsc_shouye_sjfw = findViewById(R.id.mgv_fjsc_shouye_sjfw);
        tv_fjsc_shouye_sxyy = findViewById(R.id.tv_fjsc_shouye_sxyy);
        tv_zbsc_zh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iOrdertype = 1;
                iPage = 1;
                sNearbyshopindexapp();
                tv_zbsc_zh.setTextColor(tv_zbsc_zh.getResources().getColor(R.color.black));
                tv_zbsc_xl.setTextColor(tv_zbsc_xl.getResources().getColor(R.color.hei333333));
                tv_zbsc_jl.setTextColor(tv_zbsc_jl.getResources().getColor(R.color.hei333333));
            }
        });
        tv_zbsc_xl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iOrdertype = 2;
                iPage = 1;
                sNearbyshopindexapp();
                tv_zbsc_zh.setTextColor(tv_zbsc_zh.getResources().getColor(R.color.hei333333));
                tv_zbsc_xl.setTextColor(tv_zbsc_xl.getResources().getColor(R.color.black));
                tv_zbsc_jl.setTextColor(tv_zbsc_jl.getResources().getColor(R.color.hei333333));
            }
        });
        tv_zbsc_jl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iOrdertype = 3;
                iPage = 1;
                sNearbyshopindexapp();
                tv_zbsc_zh.setTextColor(tv_zbsc_zh.getResources().getColor(R.color.hei333333));
                tv_zbsc_xl.setTextColor(tv_zbsc_xl.getResources().getColor(R.color.hei333333));
                tv_zbsc_jl.setTextColor(tv_zbsc_jl.getResources().getColor(R.color.black));
            }
        });
        ll_zbsc_sx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_zbsc_sx.setTextColor(tv_zbsc_sx.getResources().getColor(R.color.black));
            /*    ll_fjsc_shouye_query.setVisibility(View.GONE);
                mgv_fjsc_shouye_fenlei.setVisibility(View.GONE);
                rv_banner_fjsc.setVisibility(View.GONE);*/
                ll_fjsc_shouye_shaixuan.setVisibility(View.VISIBLE);
                sv_control_fjsc.scrollTo(0, rv_banner_fjsc.getBottom());
            }
        });
        tv_fjsc_shouye_sx_wc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sxwc();
                iPage = 1;
                sNearbyshopindexapp();
            }
        });
        tv_fjsc_shouye_sx_cz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iService_Id = 0;
                iSift_Id = 0;
                tv_zbsc_sx.setTextColor(tv_zbsc_sx.getResources().getColor(R.color.hei333333));
                sNearbyshopfiltrate();

            }
        });
        tv_fjsc_shouye_sxyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sxwc();
                iPage = 1;
                sNearbyshopindexapp();
            }
        });
        ll_fjsc_shouye_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Fjsc_ShouyeActivity.this, Fjsc_FenleiActivity.class);
                intent.putExtra("id", "");
                startActivity(intent);
            }
        });
    }


    /**
     * 筛选完成
     */
    private void sxwc() {
 /*       ll_fjsc_shouye_query.setVisibility(View.VISIBLE);
        mgv_fjsc_shouye_fenlei.setVisibility(View.VISIBLE);
        rv_banner_fjsc.setVisibility(View.VISIBLE);*/
        ll_fjsc_shouye_shaixuan.setVisibility(View.GONE);
    }

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        tv_fjsc_shouye_hine.setText(sHint);
        iPage = 1;
        sNearbyshopindexapp();
    }

    //6.0之后要动态获取权限，重要！！！
    protected void judgePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝

            // sd卡权限
            String[] SdCardPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, SdCardPermission[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, SdCardPermission, 100);
            }

            //手机状态权限
            String[] readPhoneStatePermission = {Manifest.permission.READ_PHONE_STATE};
            if (ContextCompat.checkSelfPermission(this, readPhoneStatePermission[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, readPhoneStatePermission, 200);
            }

            //定位权限
            String[] locationPermission = {Manifest.permission.ACCESS_FINE_LOCATION};
            if (ContextCompat.checkSelfPermission(this, locationPermission[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, locationPermission, 300);
            }

            String[] ACCESS_COARSE_LOCATION = {Manifest.permission.ACCESS_COARSE_LOCATION};
            if (ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, ACCESS_COARSE_LOCATION, 400);
            }


            String[] READ_EXTERNAL_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, READ_EXTERNAL_STORAGE, 500);
            }

            String[] WRITE_EXTERNAL_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, WRITE_EXTERNAL_STORAGE, 600);
            }

        } else {
            //doSdCardResult();
        }
        //LocationClient.reStart();
    }

    //监听下拉和上拉状态
    public void smartRefresh() {
        //下拉刷新
        srlControlFjsc.setOnRefreshListener(refreshlayout -> {
            srlControlFjsc.setEnableRefresh(true);//启用刷新
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 Data() 来模拟我们刷新出来的数据
             */
            iPage = 1;
            ISQUERY = true;
            sNearbyshopindexapp();
            //srlControlFjsc.finishRefresh();//结束刷新
        });
        //上拉加载
        srlControlFjsc.setOnLoadmoreListener(refreshlayout -> {
            srlControlFjsc.setEnableLoadmore(true);//启用加载
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 MoreDatas() 来模拟我们加载出来的数据
             */
            iPage += 1;
            sNearbyshopindexapp();
            // srlControlFjsc.finishLoadmore();//结束加载
        });
    }


    /**
     * 方法名：sShopindexApp()
     * 功  能：商城首页数据接口
     * 参  数：appId
     */
    private void sShopindexApp() {
        String url = Api.sUrl + Api.sShopindexApp;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);

        JSONObject jsonObject = new JSONObject(params);
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (iPage == 1) {
                            srlControlFjsc.finishRefresh();//结束刷新
                        } else {
                            srlControlFjsc.finishLoadmore();//结束加载
                        }
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
                                JSONArray jsonArrayTypelist = jsonObjectdata.getJSONArray("typelist");
                                ArrayList<HashMap<String, String>> mylist_typelist = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < jsonArrayTypelist.length(); i++) {
                                    JSONObject jsonObject2 = (JSONObject) jsonArrayTypelist.opt(i);
                                    String ItemId = jsonObject2.getString("id");
                                    String ItemName = jsonObject2.getString("name");
                                    String ItemLogo = jsonObject2.getString("logo");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("id", ItemId);
                                    map.put("name", ItemName);
                                    map.put("logo", ItemLogo);
                                    mylist_typelist.add(map);
                                }
                                setGridView(mylist_typelist);


                                JSONArray jsonArray_lun = jsonObjectdata.getJSONArray("lun");
                                final List<Banner> banners = new ArrayList<>();
                                for (int i = 0; i < jsonArray_lun.length(); i++) {
                                    JSONObject jsonObject5 = (JSONObject) jsonArray_lun.opt(i);
                                    catList1_ad_link.add(jsonObject5.getString("link"));
                                    catList1_ad_id.add(jsonObject5.getString("id"));

                                    banners.add(new Banner(jsonObject5.getString("url")));
                                }
                                if (banners.size() == 0) {
                                    rv_banner_fjsc.setVisibility(View.GONE);
                                } else {
                                    rv_banner_fjsc.setVisibility(View.VISIBLE);

                                    rv_banner_fjsc.setRvBannerData(banners);
                                    rv_banner_fjsc.setOnSwitchRvBannerListener(new RecyclerViewBanner.OnSwitchRvBannerListener() {
                                        @Override
                                        public void switchBanner(int position, AppCompatImageView bannerView) {
                                            Glide.with(bannerView.getContext())
                                                    .load(Api.sUrl + banners.get(position).getUrl())
                                                    .asBitmap().dontAnimate()
                                                    .placeholder(R.mipmap.error)
                                                    .error(R.mipmap.error)
                                                    .into(bannerView);
                                        }
                                    });
                                    rv_banner_fjsc.setOnRvBannerClickListener(new RecyclerViewBanner.OnRvBannerClickListener() {
                                        @Override
                                        public void onClick(int position) {
                              /*          dialogin("");
                                        gglm(catList1_ad_id.get(position), catList1_ad_link.get(position));*/
                                            Intent intent = new Intent(Fjsc_ShouyeActivity.this, GuanggaoActivity.class);
                                            intent.putExtra("link", catList1_ad_link.get(position));
                                            startActivity(intent);
                                        }
                                    });
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
                if (iPage == 1) {
                    srlControlFjsc.finishRefresh();//结束刷新
                } else {
                    srlControlFjsc.finishLoadmore();//结束加载
                }
                hideDialogin();
                //  Hint(error.getMessage(), HintDialog.ERROR);
                error(error);
            }
        });
        requestQueue.add(jsonRequest);
    }

    /**
     * 方法名：sNearbyshopfiltrate()
     * 功  能：商家筛选接口
     * 参  数：appId
     */
    private void sNearbyshopfiltrate() {
        String url = Api.sUrl + Api.sNearbyshopfiltrate;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);

        JSONObject jsonObject = new JSONObject(params);
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (iPage == 1) {
                            srlControlFjsc.finishRefresh();//结束刷新
                        } else {
                            srlControlFjsc.finishLoadmore();//结束加载
                        }
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
                                JSONArray jsonArraySift = jsonObjectdata.getJSONArray("sift");
                                ArrayList<HashMap<String, String>> mylist_Sift = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < jsonArraySift.length(); i++) {
                                    JSONObject jsonObject2 = (JSONObject) jsonArraySift.opt(i);
                                    String ItemId = jsonObject2.getString("id");
                                    String ItemName = jsonObject2.getString("name");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("id", ItemId);
                                    map.put("name", ItemName);
                                    mylist_Sift.add(map);
                                }
                                setGridView_Jxfw(mylist_Sift);


                                JSONArray jsonArrayService = jsonObjectdata.getJSONArray("service");
                                ArrayList<HashMap<String, String>> mylist_Service = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < jsonArrayService.length(); i++) {
                                    JSONObject jsonObject2 = (JSONObject) jsonArrayService.opt(i);
                                    String ItemId = jsonObject2.getString("id");
                                    String ItemName = jsonObject2.getString("name");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("id", ItemId);
                                    map.put("name", ItemName);
                                    mylist_Service.add(map);
                                }
                                setGridView_Sjfw(mylist_Service);

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
                if (iPage == 1) {
                    srlControlFjsc.finishRefresh();//结束刷新
                } else {
                    srlControlFjsc.finishLoadmore();//结束加载
                }
                hideDialogin();
                //  Hint(error.getMessage(), HintDialog.ERROR);
                error(error);
            }
        });
        requestQueue.add(jsonRequest);
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

    private void setGridView(final ArrayList<HashMap<String, String>> myList) {
        //    myList = getMenuAdapter();
        MyAdapter_fenlei myAdapter_fenlei = new MyAdapter_fenlei(Fjsc_ShouyeActivity.this);
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
        myAdapter_fenlei.myList = myList;
        mgv_fjsc_shouye_fenlei.setNumColumns(num);
        mgv_fjsc_shouye_fenlei.setAdapter(myAdapter_fenlei);

    }

    private void setGridView_Jxfw(ArrayList<HashMap<String, String>> myList) {
        //    myList = getMenuAdapter();
        MyAdapter_Jxfw myAdapter_jxfw = new MyAdapter_Jxfw(Fjsc_ShouyeActivity.this);
        myAdapter_jxfw.myList = myList;
        mgv_fjsc_shouye_jxfw.setAdapter(myAdapter_jxfw);

    }

    private void setGridView_Sjfw(ArrayList<HashMap<String, String>> myList) {
        //    myList = getMenuAdapter();
        MyAdapter_Sjfw myAdapter_sjfw = new MyAdapter_Sjfw(Fjsc_ShouyeActivity.this);
        myAdapter_sjfw.myList = myList;
        mgv_fjsc_shouye_sjfw.setAdapter(myAdapter_sjfw);

    }


    /**
     * 方法名：sNearbyshopindexapp()
     * 功  能：商家列表
     * 参  数：longitude--经度
     * latitude--维度
     * type_id--分类ID
     * keyword--搜索关键字
     * ordertype--1为综合排序 2为销量排序 3距离排序
     * service_id--商家服务id（筛选）
     * sift_id--1为品牌商家 2为新商家 （筛选）
     * page--分页 默认为1
     */
    private void sNearbyshopindexapp() {
        hideDialogin();
        dialogin("");
        String url = Api.sUrl + Api.sNearbyshopindexapp;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("longitude", sLongitude);
        params.put("latitude", sLatitude);
        if (!sType_Id.equals("")) {
            params.put("type_id", sType_Id);
        }
        if (!sKeyword.equals("")) {
            params.put("keyword", sKeyword);
        }
        params.put("ordertype", String.valueOf(iOrdertype));
        if (iService_Id == 0) {

        } else {
            params.put("service_id", String.valueOf(iService_Id));
        }
        if (iSift_Id == 0) {

        } else {
            params.put("sift_id", String.valueOf(iSift_Id));
        }
        params.put("page", String.valueOf(iPage));

        JSONObject jsonObject = new JSONObject(params);
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (iPage == 1) {
                            srlControlFjsc.finishRefresh();//结束刷新
                        } else {
                            srlControlFjsc.finishLoadmore();//结束加载
                        }
                        List<Fjsc_Shouye_SjlbData> mList = new ArrayList<>();
                        hideDialogin();
                        String sDate = response.toString();
                        System.out.println(sDate);
                        JSONObject jsonObject1 = null;
                        try {
                            jsonObject1 = new JSONObject(sDate);
                            String resultMsg = jsonObject1.getString("msg");
                            int resultCode = jsonObject1.getInt("code");
                            if (resultCode > 0) {
                                JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                                for (int i = 0; i < resultJsonArray.length(); i++) {
                                    JSONObject jsonObject = resultJsonArray.getJSONObject(i);
                                    //距离
                                    String resultDistance = jsonObject.getString("distance");
                                    //商家id
                                    String resultId = jsonObject.getString("id");
                                    //商家名称
                                    String resultShangjianame = jsonObject.getString("shangjianame");
                                    //商家图片
                                    String resultLogo = jsonObject.getString("logo");
                                    //月售
                                    String resultXiao_Num = jsonObject.getString("xiao_num");
                                    //商家评分
                                    String resultShangjia_Fen = jsonObject.getString("shangjia_fen");
                                    //运费
                                    String resultFreight = jsonObject.getString("freight");
                                    //起送价格
                                    String resultUptosend = jsonObject.getString("uptosend");
                                    //配送时间
                                    String resultDuration = jsonObject.getString("duration");

                                    Fjsc_Shouye_SjlbData model = new Fjsc_Shouye_SjlbData();
                                    //商品列表
                                    JSONArray jsonArrayCommodity = jsonObject.getJSONArray("commodity");
                                    for (int a = 0; a < jsonArrayCommodity.length(); a++) {
                                        JSONObject jsonObjectCommodity = jsonArrayCommodity.getJSONObject(a);
                                        HashMap<String, String> map = new HashMap<String, String>();
                                        map.put("id", jsonObjectCommodity.getString("id"));
                                        map.put("name", jsonObjectCommodity.getString("name"));
                                        map.put("logo", jsonObjectCommodity.getString("logo"));
                                        map.put("price", jsonObjectCommodity.getString("price"));
                                        model.commodity.add(map);

                                    }
                                    //商家服务内容列表
                                    JSONArray jsonArrayService_Id = jsonObject.getJSONArray("service_id");
                                    for (int a = 0; a < jsonArrayService_Id.length(); a++) {
                                        JSONObject jsonObjectService_Id = jsonArrayService_Id.getJSONObject(a);
                                        String resultService_Id_name = jsonObjectService_Id.getString("name");
                                        model.service_id.add(resultService_Id_name);
                                    }
                                    model.distance = resultDistance;
                                    model.id = resultId;
                                    model.shangjianame = resultShangjianame;
                                    model.logo = resultLogo;
                                    model.xiao_num = resultXiao_Num;
                                    model.shangjia_fen = resultShangjia_Fen;
                                    model.freight = resultFreight;
                                    model.uptosend = resultUptosend;
                                    model.duration = resultDuration;
                                    mList.add(model);
                                }
                                if (iPage == 1) {
                                    if (mList.size() == 0) {
                                        //  ll_splb_kong.setVisibility(View.VISIBLE);
                                    }
                                    gridviewdata(mList);
                                } else {
                                    if (mList.size() == 0) {
                                        //  ll_splb_kong.setVisibility(View.VISIBLE);
                                        iPage -= 1;
                                    }
                                    gridviewdata1(mList);
                                }

                         /*       new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 1500);*/
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
                if (iPage == 1) {
                    srlControlFjsc.finishRefresh();//结束刷新
                } else {
                    srlControlFjsc.finishLoadmore();//结束加载
                }
                hideDialogin();
                //  Hint(error.getMessage(), HintDialog.ERROR);
                error(error);
            }
        });
        requestQueue.add(jsonRequest);
    }

    private void gridviewdata(List<Fjsc_Shouye_SjlbData> list) {
        // iPage += 1;
        myAdapter = new MyAdapter(this);
        myAdapter.list = list;
        mgv_fjsc_shouye.setAdapter(myAdapter);
    }

    private void gridviewdata1(List<Fjsc_Shouye_SjlbData> list) {

        // 刷新适配器
        myAdapter.list.addAll(list);

        mgv_fjsc_shouye.setAdapter(myAdapter);
    }

    /**
     * 商家分类
     */
    private class MyAdapter_fenlei extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> myList;


        public MyAdapter_fenlei(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            myList = new ArrayList<>();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return myList.size();
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

            view = inflater.inflate(R.layout.gridview_shangcheng_item, null);
            ImageView iv_sc_item = view.findViewById(R.id.iv_sc_item);
            TextView tv_sc_miaoshu = view.findViewById(R.id.tv_sc_miaoshu);
            TextView tv_sc_id = view.findViewById(R.id.tv_sc_id);
            Glide.with(Fjsc_ShouyeActivity.this).load(Api.sUrl + myList.get(position).get("logo").toString())
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .into(iv_sc_item);
            tv_sc_miaoshu.setText(myList.get(position).get("name").toString());
            tv_sc_id.setText(myList.get(position).get("id").toString());
            LinearLayout ll_sc_item = view.findViewById(R.id.ll_sc_item);
            ll_sc_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (myList.get(position).get("id").toString().equals("0")) {
                        Intent intent = new Intent(Fjsc_ShouyeActivity.this, Fjsc_Fenlei_LiebiaoActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(Fjsc_ShouyeActivity.this, Fjsc_FenleiActivity.class);
                        intent.putExtra("id", myList.get(position).get("id").toString());
                        intent.putExtra("name", myList.get(position).get("name").toString());
                        startActivity(intent);
                    }
                }
            });

            return view;
        }
    }


    /**
     * 筛选精选服务
     */
    private class MyAdapter_Jxfw extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> myList;


        public MyAdapter_Jxfw(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            myList = new ArrayList<>();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return myList.size();
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

            view = inflater.inflate(R.layout.fjsc_shouye_sx_item, null);
            TextView tv_fjsc_shouye_sx_item = view.findViewById(R.id.tv_fjsc_shouye_sx_item);
            tv_fjsc_shouye_sx_item.setText(myList.get(position).get("name"));
            tv_fjsc_shouye_sx_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iSift_Id = Integer.valueOf(myList.get(position).get("id"));
                    notifyDataSetChanged();
                }
            });
            if (iSift_Id == Integer.valueOf(myList.get(position).get("id"))) {
                tv_fjsc_shouye_sx_item.setTextColor(tv_fjsc_shouye_sx_item.getResources().getColor(R.color.white));
                tv_fjsc_shouye_sx_item.setBackgroundResource(R.drawable.bj_theme_2);
            } else {
                tv_fjsc_shouye_sx_item.setTextColor(tv_fjsc_shouye_sx_item.getResources().getColor(R.color.hei333333));
                tv_fjsc_shouye_sx_item.setBackgroundResource(R.drawable.bj_f4f4f4_2);
            }
            return view;
        }
    }

    /**
     * 筛选商家服务j
     */
    private class MyAdapter_Sjfw extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> myList;


        public MyAdapter_Sjfw(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            myList = new ArrayList<>();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return myList.size();
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

            view = inflater.inflate(R.layout.fjsc_shouye_sx_item, null);
            TextView tv_fjsc_shouye_sx_item = view.findViewById(R.id.tv_fjsc_shouye_sx_item);
            tv_fjsc_shouye_sx_item.setText(myList.get(position).get("name"));
            tv_fjsc_shouye_sx_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iService_Id = Integer.valueOf(myList.get(position).get("id"));
                    notifyDataSetChanged();
                }
            });
            if (iService_Id == Integer.valueOf(myList.get(position).get("id"))) {
                tv_fjsc_shouye_sx_item.setTextColor(tv_fjsc_shouye_sx_item.getResources().getColor(R.color.white));
                tv_fjsc_shouye_sx_item.setBackgroundResource(R.drawable.bj_theme_2);
            } else {
                tv_fjsc_shouye_sx_item.setTextColor(tv_fjsc_shouye_sx_item.getResources().getColor(R.color.hei333333));
                tv_fjsc_shouye_sx_item.setBackgroundResource(R.drawable.bj_f4f4f4_2);
            }
            return view;
        }
    }

    /**
     * 商家列表
     */
    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        List<Fjsc_Shouye_SjlbData> list;


        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            list = new ArrayList<>();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
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

            view = inflater.inflate(R.layout.fjsc_fjdj_item, null);
            LinearLayout ll_fjsc_shouye_sjlb_item = view.findViewById(R.id.ll_fjsc_shouye_sjlb_item);
            ll_fjsc_shouye_sjlb_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Fjsc_ShouyeActivity.this, Fjsc_DpxqActivity.class);
                    intent.putExtra("shangjia_id", list.get(position).id);
                    startActivity(intent);
                }
            });
            //商家logo
            ImageView iv_fjsc_shouye_sjlb_logo = view.findViewById(R.id.iv_fjsc_shouye_sjlb_logo);
            Glide.with(Fjsc_ShouyeActivity.this).load(Api.sUrl + list.get(position).logo)
                    .asBitmap()
                    .dontAnimate()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .into(iv_fjsc_shouye_sjlb_logo);
            //商家名称
            TextView tv_fjsc_shouye_sjlb_shangjianame = view.findViewById(R.id.tv_fjsc_shouye_sjlb_shangjianame);
            tv_fjsc_shouye_sjlb_shangjianame.setText(list.get(position).shangjianame);
            //商家评分
            TextView tv_fjsc_shouye_sjlb_shangjia_fen = view.findViewById(R.id.tv_fjsc_shouye_sjlb_shangjia_fen);
            tv_fjsc_shouye_sjlb_shangjia_fen.setText(list.get(position).shangjia_fen);
            ImageView iv_fjsc_splb_pj1 = view.findViewById(R.id.iv_fjsc_splb_pj1);
            ImageView iv_fjsc_splb_pj2 = view.findViewById(R.id.iv_fjsc_splb_pj2);
            ImageView iv_fjsc_splb_pj3 = view.findViewById(R.id.iv_fjsc_splb_pj3);
            ImageView iv_fjsc_splb_pj4 = view.findViewById(R.id.iv_fjsc_splb_pj4);
            ImageView iv_fjsc_splb_pj5 = view.findViewById(R.id.iv_fjsc_splb_pj5);
            if (Double.parseDouble(list.get(position).shangjia_fen) <= 0) {
                iv_fjsc_splb_pj1.setImageResource(R.drawable.star_normal);
                iv_fjsc_splb_pj2.setImageResource(R.drawable.star_normal);
                iv_fjsc_splb_pj3.setImageResource(R.drawable.star_normal);
                iv_fjsc_splb_pj4.setImageResource(R.drawable.star_normal);
                iv_fjsc_splb_pj5.setImageResource(R.drawable.star_normal);
            } else if (Double.parseDouble(list.get(position).shangjia_fen) <= 1) {
                iv_fjsc_splb_pj1.setImageResource(R.drawable.star_selected);
                iv_fjsc_splb_pj2.setImageResource(R.drawable.star_normal);
                iv_fjsc_splb_pj3.setImageResource(R.drawable.star_normal);
                iv_fjsc_splb_pj4.setImageResource(R.drawable.star_normal);
                iv_fjsc_splb_pj5.setImageResource(R.drawable.star_normal);
            } else if (Double.parseDouble(list.get(position).shangjia_fen) <= 2) {
                iv_fjsc_splb_pj1.setImageResource(R.drawable.star_selected);
                iv_fjsc_splb_pj2.setImageResource(R.drawable.star_selected);
                iv_fjsc_splb_pj3.setImageResource(R.drawable.star_normal);
                iv_fjsc_splb_pj4.setImageResource(R.drawable.star_normal);
                iv_fjsc_splb_pj5.setImageResource(R.drawable.star_normal);
            } else if (Double.parseDouble(list.get(position).shangjia_fen) <= 3) {
                iv_fjsc_splb_pj1.setImageResource(R.drawable.star_selected);
                iv_fjsc_splb_pj2.setImageResource(R.drawable.star_selected);
                iv_fjsc_splb_pj3.setImageResource(R.drawable.star_selected);
                iv_fjsc_splb_pj4.setImageResource(R.drawable.star_normal);
                iv_fjsc_splb_pj5.setImageResource(R.drawable.star_normal);
            } else if (Double.parseDouble(list.get(position).shangjia_fen) <= 4) {
                iv_fjsc_splb_pj1.setImageResource(R.drawable.star_selected);
                iv_fjsc_splb_pj2.setImageResource(R.drawable.star_selected);
                iv_fjsc_splb_pj3.setImageResource(R.drawable.star_selected);
                iv_fjsc_splb_pj4.setImageResource(R.drawable.star_selected);
                iv_fjsc_splb_pj5.setImageResource(R.drawable.star_normal);
            } else if (Double.parseDouble(list.get(position).shangjia_fen) <= 5) {
                iv_fjsc_splb_pj1.setImageResource(R.drawable.star_selected);
                iv_fjsc_splb_pj2.setImageResource(R.drawable.star_selected);
                iv_fjsc_splb_pj3.setImageResource(R.drawable.star_selected);
                iv_fjsc_splb_pj4.setImageResource(R.drawable.star_selected);
                iv_fjsc_splb_pj5.setImageResource(R.drawable.star_selected);
            }

            //月售
            TextView tv_fjsc_shouye_sjlb_xiao_num = view.findViewById(R.id.tv_fjsc_shouye_sjlb_xiao_num);
            tv_fjsc_shouye_sjlb_xiao_num.setText("月售" + list.get(position).xiao_num);
            //配送时间
            TextView tv_fjsc_shouye_sjlb_duration = view.findViewById(R.id.tv_fjsc_shouye_sjlb_duration);
            tv_fjsc_shouye_sjlb_duration.setText(list.get(position).duration);
            //距离
            TextView tv_fjsc_shouye_sjlb_distance = view.findViewById(R.id.tv_fjsc_shouye_sjlb_distance);
            tv_fjsc_shouye_sjlb_distance.setText(list.get(position).distance);
            //起送价格
            TextView tv_fjsc_shouye_sjlb_uptosend = view.findViewById(R.id.tv_fjsc_shouye_sjlb_uptosend);
            tv_fjsc_shouye_sjlb_uptosend.setText("起送 ¥" + list.get(position).uptosend);
            //服务
            TextView tv_fjsc_sjlb_fw1 = view.findViewById(R.id.tv_fjsc_sjlb_fw1);
            TextView tv_fjsc_sjlb_fw2 = view.findViewById(R.id.tv_fjsc_sjlb_fw2);
            TextView tv_fjsc_sjlb_fw3 = view.findViewById(R.id.tv_fjsc_sjlb_fw3);
            tv_fjsc_sjlb_fw1.setVisibility(View.GONE);
            tv_fjsc_sjlb_fw2.setVisibility(View.GONE);
            tv_fjsc_sjlb_fw3.setVisibility(View.GONE);
            if (list.get(position).service_id.size() > 0) {
                tv_fjsc_sjlb_fw1.setText(list.get(position).service_id.get(0));
                tv_fjsc_sjlb_fw1.setVisibility(View.VISIBLE);
            }
            if (list.get(position).service_id.size() > 1) {
                tv_fjsc_sjlb_fw2.setText(list.get(position).service_id.get(1));
                tv_fjsc_sjlb_fw2.setVisibility(View.VISIBLE);
            }
            if (list.get(position).service_id.size() > 2) {
                tv_fjsc_sjlb_fw3.setText(list.get(position).service_id.get(2));
                tv_fjsc_sjlb_fw3.setVisibility(View.VISIBLE);
            }
            //商品图片
            ImageView tv_fjsc_shouye_sjlb_sp_logo1 = view.findViewById(R.id.tv_fjsc_shouye_sjlb_sp_logo1);
            ImageView tv_fjsc_shouye_sjlb_sp_logo2 = view.findViewById(R.id.tv_fjsc_shouye_sjlb_sp_logo2);
            ImageView tv_fjsc_shouye_sjlb_sp_logo3 = view.findViewById(R.id.tv_fjsc_shouye_sjlb_sp_logo3);
            //商品名称
            TextView tv_fjsc_shouye_sjlb_sp_name1 = view.findViewById(R.id.tv_fjsc_shouye_sjlb_sp_name1);
            TextView tv_fjsc_shouye_sjlb_sp_name2 = view.findViewById(R.id.tv_fjsc_shouye_sjlb_sp_name2);
            TextView tv_fjsc_shouye_sjlb_sp_name3 = view.findViewById(R.id.tv_fjsc_shouye_sjlb_sp_name3);
            //商品价格
            TextView tv_fjsc_shouye_sjlb_sp_price1 = view.findViewById(R.id.tv_fjsc_shouye_sjlb_sp_price1);
            TextView tv_fjsc_shouye_sjlb_sp_price2 = view.findViewById(R.id.tv_fjsc_shouye_sjlb_sp_price2);
            TextView tv_fjsc_shouye_sjlb_sp_price3 = view.findViewById(R.id.tv_fjsc_shouye_sjlb_sp_price3);
            if (list.get(position).commodity.size() > 0) {
                Glide.with(Fjsc_ShouyeActivity.this).load(Api.sUrl + list.get(position).commodity.get(0).get("logo"))
                        .asBitmap()
                        .dontAnimate()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .into(tv_fjsc_shouye_sjlb_sp_logo1);
                tv_fjsc_shouye_sjlb_sp_name1.setText(list.get(position).commodity.get(0).get("name"));
                tv_fjsc_shouye_sjlb_sp_price1.setText("￥" + list.get(position).commodity.get(0).get("price"));
            }
            if (list.get(position).commodity.size() > 1) {
                Glide.with(Fjsc_ShouyeActivity.this).load(Api.sUrl + list.get(position).commodity.get(1).get("logo"))
                        .asBitmap()
                        .dontAnimate()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .into(tv_fjsc_shouye_sjlb_sp_logo2);
                tv_fjsc_shouye_sjlb_sp_name2.setText(list.get(position).commodity.get(1).get("name"));
                tv_fjsc_shouye_sjlb_sp_price2.setText("￥" + list.get(position).commodity.get(1).get("price"));
            }
            if (list.get(position).commodity.size() > 2) {
                Glide.with(Fjsc_ShouyeActivity.this).load(Api.sUrl + list.get(position).commodity.get(2).get("logo"))
                        .asBitmap()
                        .dontAnimate()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .into(tv_fjsc_shouye_sjlb_sp_logo3);
                tv_fjsc_shouye_sjlb_sp_name3.setText(list.get(position).commodity.get(2).get("name"));
                tv_fjsc_shouye_sjlb_sp_price3.setText("￥" + list.get(position).commodity.get(2).get("price"));
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
        loadingDialog = new LoadingDialog(Fjsc_ShouyeActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(Fjsc_ShouyeActivity.this, R.style.dialog, sHint, type, true).show();
    }


    /**
     * 百度定位
     * */
    /***
     * Stop location service
     */
    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener1); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // -----------location config ------------
        locationService = ((AppApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener1);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.start();// 定位SDK
    }

    /****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDAbstractLocationListener mListener1 = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");

                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                //   sLatitude = String.valueOf(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                //  sLongitude = String.valueOf(location.getLongitude());

                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                if (location.getCity() == null) {

                } else {
                    if (ISQUERY) {
                        ISQUERY = false;
                        sNearbyshopindexapp();
                        sHint = String.valueOf(location.getAddrStr());
                        tv_fjsc_shouye_hine.setText(String.valueOf(location.getAddrStr()));
                    }//  tianqi(location.getCity());
                    //locationService.stop();
                }
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                //   logMsg(sb.toString());
            }
        }

    };
}
