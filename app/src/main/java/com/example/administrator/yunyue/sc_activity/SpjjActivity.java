package com.example.administrator.yunyue.sc_activity;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
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
import com.example.administrator.yunyue.sc.MyFragmentPagerAdapter;
import com.example.administrator.yunyue.sc.ObservableScrollView;
import com.example.administrator.yunyue.sc.ScrollByViewpager;
import com.example.administrator.yunyue.sc_fragment.ChanpinFragment;
import com.example.administrator.yunyue.sc_fragment.JianjieFragment;
import com.example.administrator.yunyue.sc_fragment.PinglunFragment;
import com.example.administrator.yunyue.sc_gridview.GridViewAdapterchanpin;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.loonggg.rvbanner.lib.RecyclerViewBanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpjjActivity extends AppCompatActivity implements View.OnClickListener, ChanpinFragment.OnFragmentInteractionListener,
        JianjieFragment.OnFragmentInteractionListener, PinglunFragment.OnFragmentInteractionListener {

    private static final String TAG = SpjjActivity.class.getSimpleName();
    int iFrragment = 0;
    Fragment chanpin;
    Fragment jianjie;
    Fragment pinglun;
    private TextView tvCurrent, tv_chanpin, tv_jianjie, tv_pinglun;
    private TextView tv_chanpin1, tv_jianjie1, tv_pinglun1;
    private ScrollByViewpager viewPager;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private SharedPreferences pref;
    private LinearLayout ll_chanpin, ll_jianjie, ll_pinglun;
    private LinearLayout ll_chanpin1, ll_jianjie1, ll_pinglun1;
    private ImageView iv_spjj_back;
    private String sId;
    private LinearLayout ll_spjj_log;
    private int imageHeight;
    private ObservableScrollView sv_spjj_camera;
    private LinearLayout ll_spjj_xuanze;
    private LinearLayout ll_spjj_xuanze1;
    private String goods_id;
    private String data;
    private Dialog dialog;
    private View inflate;
    private String activty;
    RecyclerViewBanner recyclerViewBanner1;
    //   private LinearLayout ll_spjj_shopp;
    private LinearLayout ll_spjj_jrgwc;
    private ImageView iv_sptcc_image;
    private Button bt_sptcc_save;
    private TextView tv_sptcc_price;
    private TextView tv_sptcc_storecount;
    private LinearLayout ll_sptcc_jia, ll_sptcc_jian;
    private ImageView iv_spttc_sc;
    private TextView tv_sptcc_number;
    private ImageView iv_sptcc_jia, iv_sptcc_jian;
    private TextView tv_sptcc_xuanze;
    private GridView gv_sptcc;
    int sp_number = 1;
    GridViewAdapterchanpin mAdapter;
    private String sItemItemId;
    private String user_id;
    RequestQueue queue = null;
    ArrayList<HashMap<String, Object>> mylistImage;
    ArrayList<HashMap<String, Object>> mylist_spec_goods_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_spjj);
        queue = Volley.newRequestQueue(SpjjActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(SpjjActivity.this);
        boolean isRemember = pref.getBoolean("user", false);
        user_id = pref.getString("user_id", "");
        Intent intent = getIntent();
        data = intent.getStringExtra("data");
        goods_id = intent.getStringExtra("goods_id");
        activty = intent.getStringExtra("activty");
        ll_spjj_log = (LinearLayout) findViewById(R.id.ll_spjj_log);
        iv_spjj_back = (ImageView) findViewById(R.id.iv_spjj_back);
        sv_spjj_camera = (ObservableScrollView) findViewById(R.id.sv_spjj_camera);
        ll_spjj_xuanze = (LinearLayout) findViewById(R.id.ll_spjj_xuanze);
        ll_spjj_xuanze1 = (LinearLayout) findViewById(R.id.ll_spjj_xuanze1);
        recyclerViewBanner1 = (RecyclerViewBanner) findViewById(R.id.rv_cpjj_1);
        //  ll_spjj_shopp = (LinearLayout) findViewById(R.id.ll_spjj_shopp);
        ll_spjj_jrgwc = (LinearLayout) findViewById(R.id.ll_spjj_jrgwc);

        iv_spjj_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
   /*     ll_spjj_shopp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpjjActivity.this, ShoppingActivity.class);
                startActivity(intent);
            }
        });*/
        ll_spjj_jrgwc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });
        initialize();
        initView();
        initListeners();
        data(data);

    }

    public void show() {
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    private void initialize() {
        dialog = new Dialog(SpjjActivity.this, R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(SpjjActivity.this).inflate(R.layout.sptcc_item, null);
        iv_sptcc_image = (ImageView) inflate.findViewById(R.id.iv_sptcc_image);
        tv_sptcc_price = (TextView) inflate.findViewById(R.id.tv_sptcc_price);
        tv_sptcc_storecount = (TextView) inflate.findViewById(R.id.tv_sptcc_storecount);
        iv_spttc_sc = (ImageView) inflate.findViewById(R.id.iv_spttc_sc);
        tv_sptcc_number = (TextView) inflate.findViewById(R.id.tv_sptcc_number);
        iv_sptcc_jia = (ImageView) inflate.findViewById(R.id.iv_sptcc_jia);
        iv_sptcc_jian = (ImageView) inflate.findViewById(R.id.iv_sptcc_jia);
        ll_sptcc_jia = (LinearLayout) inflate.findViewById(R.id.ll_sptcc_jia);
        ll_sptcc_jian = (LinearLayout) inflate.findViewById(R.id.ll_sptcc_jian);
        tv_sptcc_xuanze = (TextView) inflate.findViewById(R.id.tv_sptcc_xuanze);
        bt_sptcc_save = (Button) inflate.findViewById(R.id.bt_sptcc_save);
        gv_sptcc = (GridView) inflate.findViewById(R.id.gv_sptcc);
        tv_sptcc_number.setText(String.valueOf(sp_number));
        gv_sptcc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mAdapter.setSelection(i);
                mAdapter.notifyDataSetChanged();
                HashMap<String, String> map = (HashMap<String, String>) gv_sptcc.getItemAtPosition(i);
                sItemItemId = map.get("ItemItemId");
                String sItemName = map.get("ItemName");
                tv_sptcc_storecount.setText("库存" + map.get("ItemStoreCount") + "件");
                tv_sptcc_price.setText("￥" + map.get("ItemPrice"));
                tv_sptcc_xuanze.setText("已选：" + sItemName);

            }
        });
        iv_spttc_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp_number = 1;
                dialog.dismiss();
            }
        });
        ll_sptcc_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sp_number > 1) {
                    sp_number = sp_number - 1;
                    tv_sptcc_number.setText(String.valueOf(sp_number));
                }

            }
        });
        ll_sptcc_jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp_number = sp_number + 1;
                tv_sptcc_number.setText(String.valueOf(sp_number));
            }
        });
        bt_sptcc_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogin("");
                gouwuche(goods_id, String.valueOf(sp_number), sItemItemId, "1");
            }
        });
    }

    private void gouwuche(String goods_id, String goods_num, String item_id, String type) {
        String url = Api.sUrl + "Order/cartAdd/user_id/"
                + user_id + "/goods_id/" + goods_id + "/goods_num/"
                + goods_num + "" + "/item_id/" + item_id + "/type/" + type
                +"/appId/" + Api.sApp_Id;
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
                        sp_number = 1;
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

    private void gridviewdata(ArrayList<HashMap<String, Object>> myList) {
        //    myList = getMenuAdapter();
        mAdapter = new GridViewAdapterchanpin(SpjjActivity.this, myList);
        gv_sptcc.setAdapter(mAdapter);
    }

    private void data(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String resultMsg = jsonObject.getString("msg");
            int resultCode = jsonObject.getInt("code");
            if (resultCode > 0) {
                String resultData = jsonObject.getString("data");
                JSONObject jsonObjectData = new JSONObject(resultData);
                String resultgoods_images_list = jsonObjectData.getString("goods_images_list");
                JSONObject jsonObjectgoods_images_list = new JSONObject(resultgoods_images_list);
                JSONArray resultJsonArray = jsonObjectgoods_images_list.getJSONArray("0");
                final List<Banner> banners = new ArrayList<>();
                for (int i = 0; i < resultJsonArray.length(); i++) {
                    JSONObject jsonObject0 = resultJsonArray.getJSONObject(i);
                    banners.add(new Banner( jsonObject0.getString("image_url")));
                }

                recyclerViewBanner1.setRvBannerData(banners);
                recyclerViewBanner1.setOnSwitchRvBannerListener(new RecyclerViewBanner.OnSwitchRvBannerListener() {
                    @Override
                    public void switchBanner(int position, AppCompatImageView bannerView) {
                        Glide.with(bannerView.getContext())
                                .load( Api.sUrl+banners.get(position).getUrl())
                                .asBitmap()
                                .placeholder(R.mipmap.error)
                                .dontAnimate()
                                .into(bannerView);
                    }
                });
                recyclerViewBanner1.setOnRvBannerClickListener(new RecyclerViewBanner.OnRvBannerClickListener() {
                    @Override
                    public void onClick(int position) {

                    }
                });

                JSONArray resultJsonArray1 = jsonObjectgoods_images_list.getJSONArray("1");
                mylistImage = new ArrayList<HashMap<String, Object>>();
                for (int i = 0; i < resultJsonArray1.length(); i++) {
                    JSONObject jsonObjecti = resultJsonArray1.getJSONObject(i);
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("goods_id", jsonObjecti.getString("goods_id"));
                    map.put("image_url", jsonObjecti.getString("image_url"));
                    mylistImage.add(map);
                }
                try {
                    Glide.with(SpjjActivity.this)
                            .load( Api.sUrl+ mylistImage.get(0).get("image_url"))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_sptcc_image);
                } catch (Exception e) {
                    iv_sptcc_image.setImageResource(R.mipmap.error);
                    e.printStackTrace();
                }


                JSONArray resultJsonArrayspec_goods_price = jsonObject.getJSONArray("spec_goods_price");
                mylist_spec_goods_price = new ArrayList<HashMap<String, Object>>();
                for (int i = 0; i < resultJsonArrayspec_goods_price.length(); i++) {
                    jsonObject = resultJsonArrayspec_goods_price.getJSONObject(i);
                    String resultItemId = jsonObject.getString("item_id");
                    String resultName = jsonObject.getString("name");
                    String resultPrice = jsonObject.getString("price");
                    String resultStoreCount = jsonObject.getString("store_count");
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("ItemItemId", resultItemId);
                    map.put("ItemName", resultName);
                    map.put("ItemPrice", resultPrice);
                    map.put("ItemStoreCount", resultStoreCount);
                    mylist_spec_goods_price.add(map);
                    if (i == 0) {
                        sItemItemId = resultItemId;
                        tv_sptcc_storecount.setText("库存" + resultStoreCount + "件");
                        tv_sptcc_price.setText("￥" + resultPrice);
                        tv_sptcc_xuanze.setText("已选：" + resultName);
                        tv_sptcc_xuanze.setText("已选：" + resultName);
                    }
                }
                gridviewdata(mylist_spec_goods_price);

            } else {
                //  Hint(resultMsg, HintDialog.ERROR);
            }
        } catch (JSONException e) {
            //  hideDialogin();
            e.printStackTrace();
        }
    }

    private void initListeners() {
        // 获取顶部图片高度后，设置滚动监听
        ViewTreeObserver vto = ll_spjj_log.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //  ll_spjj_log.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                ll_spjj_log.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                imageHeight = ll_spjj_log.getHeight();
/*
                ll_spjj_log.measure(0, 0);

                //获取组件高度
                final int height_tjq = ll_spjj_log.getMeasuredHeight();*/
                final int height_tjq = ll_spjj_xuanze1.getMeasuredHeight();
                sv_spjj_camera.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
                    @Override
                    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                        // TODO Auto-generated method stub
                        // Log.i("TAG", "y--->" + y + "    height-->" + height);
                        if (y >= imageHeight) {
                            if (y > oldy) {
                                if (y >= imageHeight) {
                                    if (iFrragment == 0) {
                                        ll_spjj_jrgwc.setVisibility(View.GONE);
                                    }
                                    //  ll_spjj_xuanze1.setVisibility(View.GONE);
                                    ll_spjj_xuanze.setVisibility(View.VISIBLE);

                                }
                            } else {
                                if (iFrragment == 0) {
                                    ll_spjj_jrgwc.setVisibility(View.GONE);
                                }
                                //  ll_spjj_xuanze1.setVisibility(View.GONE);
                                ll_spjj_xuanze.setVisibility(View.VISIBLE);
                            }
                        } else if (y <= (imageHeight + height_tjq)) {

                            if (iFrragment == 0) {
                                ll_spjj_jrgwc.setVisibility(View.VISIBLE);
                            }
                            ll_spjj_xuanze.setVisibility(View.GONE);

                            //    ll_spjj_xuanze1.setVisibility(View.VISIBLE);
                        }

                    }
                });

            }
        });


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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

    private void back() {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Log.d(TAG, "onKeyDown KEYCODE_BACK");
                //   showDialog();
                back();
                break;
            /*
            * Home键是系统事件，不能通过KeyDown监听
            * 此处log不会打印
            */
            case KeyEvent.KEYCODE_HOME:
                Log.d(TAG, "onKeyDown KEYCODE_HOME");
                break;
            case KeyEvent.KEYCODE_MENU:
                Log.d(TAG, "onKeyDown KEYCODE_MENU");
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     *  * 方法名：initView()
     *  * 功    能：控件初始化
     *  * 参    数：无
     *  * 返回值：无
     */
    private void initView() {
        viewPager = (ScrollByViewpager) findViewById(R.id.vp_cpjj);
        ll_chanpin = (LinearLayout) findViewById(R.id.ll_chanpin);
        ll_jianjie = (LinearLayout) findViewById(R.id.ll_jianjie);
        ll_pinglun = (LinearLayout) findViewById(R.id.ll_pinglun);
        ll_chanpin1 = (LinearLayout) findViewById(R.id.ll_chanpin1);
        ll_jianjie1 = (LinearLayout) findViewById(R.id.ll_jianjie1);
        ll_pinglun1 = (LinearLayout) findViewById(R.id.ll_pinglun1);


        ll_chanpin.setOnClickListener(this);
        ll_jianjie.setOnClickListener(this);
        ll_pinglun.setOnClickListener(this);
        ll_chanpin1.setOnClickListener(this);
        ll_jianjie1.setOnClickListener(this);
        ll_pinglun1.setOnClickListener(this);


        tv_chanpin = (TextView) findViewById(R.id.tv_chanpin);
        tv_jianjie = (TextView) findViewById(R.id.tv_jianjie);
        tv_pinglun = (TextView) findViewById(R.id.tv_pinglun);
        tv_chanpin1 = (TextView) findViewById(R.id.tv_chanpin1);
        tv_jianjie1 = (TextView) findViewById(R.id.tv_jianjie1);
        tv_pinglun1 = (TextView) findViewById(R.id.tv_pinglun1);


        tv_chanpin.setSelected(true);
        tvCurrent = tv_chanpin;

        tv_chanpin.setTextColor(tv_chanpin.getResources().getColor(R.color.theme));
        tv_chanpin1.setTextColor(tv_chanpin1.getResources().getColor(R.color.theme));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(4);
        initData();
    }

    /**
     *  * 方法名：initData()
     *  * 功    能：fragment页面初始加载
     *  * 参    数：无
     *  * 返回值：无
     */
    private void initData() {
        Bundle bundle = new Bundle();
        bundle.putString("goods_id", goods_id);
        bundle.putString("data", data);

        chanpin = new ChanpinFragment();
        chanpin.setArguments(bundle);


        jianjie = new JianjieFragment();
        jianjie.setArguments(bundle);

        pinglun = new PinglunFragment();
        pinglun.setArguments(bundle);

        fragments.add(chanpin);
        fragments.add(jianjie);
        fragments.add(pinglun);

        pref = PreferenceManager.getDefaultSharedPreferences(SpjjActivity.this);

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
    }

    /**
     *  * 方法名： onClick(View view)
     *  * 功    能：页面切换
     *  * 参    数：View v - 按钮的View
     *  * 返回值：无
     */

    public void onClick(View view) {
        changeTab(view.getId());
    }

    /**
     *  * 方法名：changeTab(int id)
     *  * 功    能：页面切换
     *  * 参    数：int id --页面的id
     *  * 返回值：无
     */
    private void changeTab(int id) {
        iFrragment = id;
        if (iFrragment == 0) {
            ll_spjj_jrgwc.setVisibility(View.VISIBLE);
        } else {
            ll_spjj_jrgwc.setVisibility(View.GONE);
        }
        tvCurrent.setSelected(false);
        switch (id) {
            case R.id.ll_chanpin:
                viewPager.setCurrentItem(0);
                ll_chanpin.setBackgroundResource(R.drawable.bg_null);
                ll_jianjie.setBackgroundResource(R.drawable.bg1);
                ll_pinglun.setBackgroundResource(R.drawable.bg1);
                tv_chanpin.setTextColor(tv_chanpin.getResources().getColor(R.color.theme));
                tv_jianjie.setTextColor(tv_jianjie.getResources().getColor(R.color.gray));
                tv_pinglun.setTextColor(tv_pinglun.getResources().getColor(R.color.gray));
                tv_chanpin1.setTextColor(tv_chanpin1.getResources().getColor(R.color.theme));
                tv_jianjie1.setTextColor(tv_jianjie1.getResources().getColor(R.color.gray));
                tv_pinglun1.setTextColor(tv_pinglun1.getResources().getColor(R.color.gray));
            case R.id.ll_chanpin1:
                viewPager.setCurrentItem(0);
                ll_chanpin1.setBackgroundResource(R.drawable.bg_null);
                ll_jianjie1.setBackgroundResource(R.drawable.bg1);
                ll_pinglun1.setBackgroundResource(R.drawable.bg1);
                tv_chanpin.setTextColor(tv_chanpin.getResources().getColor(R.color.theme));
                tv_jianjie.setTextColor(tv_jianjie.getResources().getColor(R.color.gray));
                tv_pinglun.setTextColor(tv_pinglun.getResources().getColor(R.color.gray));
                tv_chanpin1.setTextColor(tv_chanpin1.getResources().getColor(R.color.theme));
                tv_jianjie1.setTextColor(tv_jianjie1.getResources().getColor(R.color.gray));
                tv_pinglun1.setTextColor(tv_pinglun1.getResources().getColor(R.color.gray));
            case 0:
                ll_chanpin.setBackgroundResource(R.drawable.bg_null);
                ll_jianjie.setBackgroundResource(R.drawable.bg1);
                ll_pinglun.setBackgroundResource(R.drawable.bg1);
                ll_chanpin1.setBackgroundResource(R.drawable.bg_null);
                ll_jianjie1.setBackgroundResource(R.drawable.bg1);
                ll_pinglun1.setBackgroundResource(R.drawable.bg1);
                tv_chanpin.setTextColor(tv_chanpin.getResources().getColor(R.color.theme));
                tv_jianjie.setTextColor(tv_jianjie.getResources().getColor(R.color.gray));
                tv_pinglun.setTextColor(tv_pinglun.getResources().getColor(R.color.gray));
                tv_chanpin1.setTextColor(tv_chanpin1.getResources().getColor(R.color.theme));
                tv_jianjie1.setTextColor(tv_jianjie1.getResources().getColor(R.color.gray));
                tv_pinglun1.setTextColor(tv_pinglun1.getResources().getColor(R.color.gray));
                tvCurrent = tv_chanpin;
                break;
            case R.id.ll_jianjie:
                viewPager.setCurrentItem(1);
                ll_chanpin.setBackgroundResource(R.drawable.bg1);
                ll_jianjie.setBackgroundResource(R.drawable.bg_null);
                ll_pinglun.setBackgroundResource(R.drawable.bg1);
                tv_chanpin.setTextColor(tv_chanpin.getResources().getColor(R.color.gray));
                tv_jianjie.setTextColor(tv_jianjie.getResources().getColor(R.color.theme));
                tv_pinglun.setTextColor(tv_pinglun.getResources().getColor(R.color.gray));
                tv_chanpin1.setTextColor(tv_chanpin1.getResources().getColor(R.color.gray));
                tv_jianjie1.setTextColor(tv_jianjie1.getResources().getColor(R.color.theme));
                tv_pinglun1.setTextColor(tv_pinglun1.getResources().getColor(R.color.gray));
            case R.id.ll_jianjie1:
                viewPager.setCurrentItem(1);
                ll_chanpin1.setBackgroundResource(R.drawable.bg1);
                ll_jianjie1.setBackgroundResource(R.drawable.bg_null);
                ll_pinglun1.setBackgroundResource(R.drawable.bg1);
                tv_chanpin.setTextColor(tv_chanpin.getResources().getColor(R.color.gray));
                tv_jianjie.setTextColor(tv_jianjie.getResources().getColor(R.color.theme));
                tv_pinglun.setTextColor(tv_pinglun.getResources().getColor(R.color.gray));
                tv_chanpin1.setTextColor(tv_chanpin1.getResources().getColor(R.color.gray));
                tv_jianjie1.setTextColor(tv_jianjie1.getResources().getColor(R.color.theme));
                tv_pinglun1.setTextColor(tv_pinglun1.getResources().getColor(R.color.gray));
            case 1:
                ll_chanpin.setBackgroundResource(R.drawable.bg1);
                ll_jianjie.setBackgroundResource(R.drawable.bg_null);
                ll_pinglun.setBackgroundResource(R.drawable.bg1);
                ll_chanpin1.setBackgroundResource(R.drawable.bg1);
                ll_jianjie1.setBackgroundResource(R.drawable.bg_null);
                ll_pinglun1.setBackgroundResource(R.drawable.bg1);
                tv_chanpin.setTextColor(tv_chanpin.getResources().getColor(R.color.gray));
                tv_jianjie.setTextColor(tv_jianjie.getResources().getColor(R.color.theme));
                tv_pinglun.setTextColor(tv_pinglun.getResources().getColor(R.color.gray));
                tv_chanpin1.setTextColor(tv_chanpin1.getResources().getColor(R.color.gray));
                tv_jianjie1.setTextColor(tv_jianjie1.getResources().getColor(R.color.theme));
                tv_pinglun1.setTextColor(tv_pinglun1.getResources().getColor(R.color.gray));
                tvCurrent = tv_jianjie;
                break;
            case R.id.ll_pinglun:
                viewPager.setCurrentItem(2);
                ll_chanpin.setBackgroundResource(R.drawable.bg1);
                ll_jianjie.setBackgroundResource(R.drawable.bg1);
                ll_pinglun.setBackgroundResource(R.drawable.bg_null);
                tv_chanpin.setTextColor(tv_chanpin.getResources().getColor(R.color.gray));
                tv_jianjie.setTextColor(tv_jianjie.getResources().getColor(R.color.gray));
                tv_pinglun.setTextColor(tv_pinglun.getResources().getColor(R.color.theme));
                tv_chanpin1.setTextColor(tv_chanpin1.getResources().getColor(R.color.gray));
                tv_jianjie1.setTextColor(tv_jianjie1.getResources().getColor(R.color.gray));
                tv_pinglun1.setTextColor(tv_pinglun1.getResources().getColor(R.color.theme));
            case R.id.ll_pinglun1:
                viewPager.setCurrentItem(2);
                ll_chanpin1.setBackgroundResource(R.drawable.bg1);
                ll_jianjie1.setBackgroundResource(R.drawable.bg1);
                ll_pinglun1.setBackgroundResource(R.drawable.bg_null);
                tv_chanpin.setTextColor(tv_chanpin.getResources().getColor(R.color.gray));
                tv_jianjie.setTextColor(tv_jianjie.getResources().getColor(R.color.gray));
                tv_pinglun.setTextColor(tv_pinglun.getResources().getColor(R.color.theme));
                tv_chanpin1.setTextColor(tv_chanpin1.getResources().getColor(R.color.gray));
                tv_jianjie1.setTextColor(tv_jianjie1.getResources().getColor(R.color.gray));
                tv_pinglun1.setTextColor(tv_pinglun1.getResources().getColor(R.color.theme));
            case 2:
                ll_chanpin.setBackgroundResource(R.drawable.bg1);
                ll_jianjie.setBackgroundResource(R.drawable.bg1);
                ll_pinglun.setBackgroundResource(R.drawable.bg_null);
                ll_chanpin1.setBackgroundResource(R.drawable.bg1);
                ll_jianjie1.setBackgroundResource(R.drawable.bg1);
                ll_pinglun1.setBackgroundResource(R.drawable.bg_null);
                tv_chanpin.setTextColor(tv_chanpin.getResources().getColor(R.color.gray));
                tv_jianjie.setTextColor(tv_jianjie.getResources().getColor(R.color.gray));
                tv_pinglun.setTextColor(tv_pinglun.getResources().getColor(R.color.theme));
                tv_chanpin1.setTextColor(tv_chanpin1.getResources().getColor(R.color.gray));
                tv_jianjie1.setTextColor(tv_jianjie1.getResources().getColor(R.color.gray));
                tv_pinglun1.setTextColor(tv_pinglun1.getResources().getColor(R.color.theme));
                tvCurrent = tv_pinglun;
                break;
            default:
                break;
        }
    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(SpjjActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(SpjjActivity.this, R.style.dialog, sHint, type, true).show();
    }
}
