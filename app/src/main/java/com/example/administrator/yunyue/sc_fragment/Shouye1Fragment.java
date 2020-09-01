package com.example.administrator.yunyue.sc_fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc_activity.FenleiActivity;
import com.example.administrator.yunyue.sc_activity.FukuanActivity;
import com.example.administrator.yunyue.sc_activity.GuanggaoActivity;
import com.example.administrator.yunyue.sc_activity.ScMainActivity;
import com.example.administrator.yunyue.sc_activity.SpjjActivity;
import com.example.administrator.yunyue.sc_activity.SplbActivity;
import com.example.administrator.yunyue.sc_activity.XsmsActivity;
import com.example.administrator.yunyue.sc_gridview.GridViewAdaptershangcheng;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.loonggg.rvbanner.lib.RecyclerViewBanner;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Shouye1Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Shouye1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Shouye1Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    List<String> favourite_goods_goods_name = new ArrayList<String>();//商品名
    List<String> favourite_goods_original_img = new ArrayList<String>();//商品图片链接
    List<String> favourite_goods_goods_id = new ArrayList<String>();//商品id
    List<String> favourite_goods_cat_id = new ArrayList<String>();//分类id
    List<String> favourite_goods_extend_cat_id = new ArrayList<String>();//扩展分类id
    List<String> favourite_goods_goods_sn = new ArrayList<String>();//商品编号
    List<String> favourite_goods_click_count = new ArrayList<String>();//点击数
    List<String> favourite_goods_brand_id = new ArrayList<String>();//品牌id
    List<String> favourite_goods_store_count = new ArrayList<String>();//库存数量
    List<String> favourite_goods_comment_count = new ArrayList<String>();//商品评论数
    List<String> favourite_goods_market_price = new ArrayList<String>();//市场价
    List<String> favourite_goods_keywords = new ArrayList<String>();//商品关键词
    List<String> favourite_goods_goods_remark = new ArrayList<String>();//商品简单描述
    List<String> favourite_goods_mobile_content = new ArrayList<String>();//手机端商品详情
    List<String> favourite_goods_collect_sum = new ArrayList<String>();//收藏量
    List<String> favourite_goods_is_free_shipping = new ArrayList<String>();//是否包邮0否1是

    List<String> catList1_ad_link = new ArrayList<String>();//图片链接
    List<String> catList1_ad_id = new ArrayList<String>();//图片id
    List<String> catList1_ad_name = new ArrayList<String>();//轮播图名
    List<String> catList1_ad_code = new ArrayList<String>();//图片路径
    List<String> catList1_gid = new ArrayList<String>();//id

    List<String> catList1_ad_link1 = new ArrayList<String>();//图片链接
    List<String> catList1_ad_id1 = new ArrayList<String>();//图片id
    List<String> catList1_ad_name1 = new ArrayList<String>();//轮播图名
    List<String> catList1_ad_code1 = new ArrayList<String>();//图片路径
    List<String> catList1_gid1 = new ArrayList<String>();//id

    List<String> catList1_ad_link2 = new ArrayList<String>();//图片链接
    List<String> catList1_ad_id2 = new ArrayList<String>();//图片id
    List<String> catList1_ad_name2 = new ArrayList<String>();//轮播图名
    List<String> catList1_ad_code2 = new ArrayList<String>();//图片路径
    List<String> catList1_gid2 = new ArrayList<String>();//id

    RecyclerViewBanner recyclerViewBanner1;
    RecyclerViewBanner recyclerViewBanner2;
    RecyclerViewBanner recyclerViewBanner3;
    private ImageView iv_shangcheng_favourite_img, iv_shangcheng_favourite_img1, iv_shangcheng_favourite_img2, iv_shangcheng_favourite_img3, iv_shangcheng_favourite_img4;
    private TextView iv_shangcheng_favourite_name, iv_shangcheng_favourite_name1, iv_shangcheng_favourite_name2, iv_shangcheng_favourite_name3, iv_shangcheng_favourite_name4;
    private TextView iv_shangcheng_favourite_remark, iv_shangcheng_favourite_remark1, iv_shangcheng_favourite_remark2, iv_shangcheng_favourite_remark3, iv_shangcheng_favourite_remark4;
    private TextView iv_shangcheng_favourite_price, iv_shangcheng_favourite_price1, iv_shangcheng_favourite_price2, iv_shangcheng_favourite_price3, iv_shangcheng_favourite_price4;
    private LinearLayout ll_shangchang_favourite, ll_shangchang_favourite1, ll_shangchang_favourite2, ll_shangchang_favourite3, ll_shangchang_favourite4;
    private MyGridView gv_shangcheng;
    private MyAdapter myAdapter;
    private MyGridView gv_shangcheng_hot;
    private ImageView iv_shangcheng_query;
    RequestQueue queue = null;
    private GridViewAdaptershangcheng mAdapter;
    //Fragment的View加载完毕的标记
    private boolean isViewCreated;
    //Fragment对用户可见的标记
    private boolean isUIVisible;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private LinearLayout ll_shouye_saomiao;
    private int REQUEST_CODE_SCAN = 111;
    public static String sSaomiao = "";
    private MyGridView gv_shangcheng_mx;
    private LinearLayout ll_shangcheng_item_mx;
    private TextView tv_shangcheng_mx_gdsp;
    private TextView tv_shangcheng_mx_time_s, tv_shangcheng_mx_time_f;

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
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };


    public Shouye1Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShouyeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Shouye1Fragment newInstance(String param1, String param2) {
        Shouye1Fragment fragment = new Shouye1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shouye1, container, false);
    }

    public void onViewCreated(View view, Bundle saveInstancesState) {
        super.onViewCreated(view, saveInstancesState);
        recyclerViewBanner1 = (RecyclerViewBanner) view.findViewById(R.id.rv_banner_1);
        recyclerViewBanner2 = (RecyclerViewBanner) view.findViewById(R.id.rv_banner_2);
        recyclerViewBanner3 = (RecyclerViewBanner) view.findViewById(R.id.rv_banner_3);
        iv_shangcheng_favourite_img = (ImageView) view.findViewById(R.id.iv_shangcheng_favourite_img);
        iv_shangcheng_favourite_img1 = (ImageView) view.findViewById(R.id.iv_shangcheng_favourite_img1);
        iv_shangcheng_favourite_img2 = (ImageView) view.findViewById(R.id.iv_shangcheng_favourite_img2);
        iv_shangcheng_favourite_img3 = (ImageView) view.findViewById(R.id.iv_shangcheng_favourite_img3);
        iv_shangcheng_favourite_img4 = (ImageView) view.findViewById(R.id.iv_shangcheng_favourite_img4);
        iv_shangcheng_favourite_name = (TextView) view.findViewById(R.id.iv_shangcheng_favourite_name);
        iv_shangcheng_favourite_name1 = (TextView) view.findViewById(R.id.iv_shangcheng_favourite_name1);
        iv_shangcheng_favourite_name2 = (TextView) view.findViewById(R.id.iv_shangcheng_favourite_name2);
        iv_shangcheng_favourite_name3 = (TextView) view.findViewById(R.id.iv_shangcheng_favourite_name3);
        iv_shangcheng_favourite_name4 = (TextView) view.findViewById(R.id.iv_shangcheng_favourite_name4);
        iv_shangcheng_favourite_remark = (TextView) view.findViewById(R.id.iv_shangcheng_favourite_remark);
        iv_shangcheng_favourite_remark1 = (TextView) view.findViewById(R.id.iv_shangcheng_favourite_remark1);
        iv_shangcheng_favourite_remark2 = (TextView) view.findViewById(R.id.iv_shangcheng_favourite_remark2);
        iv_shangcheng_favourite_remark3 = (TextView) view.findViewById(R.id.iv_shangcheng_favourite_remark3);
        iv_shangcheng_favourite_remark4 = (TextView) view.findViewById(R.id.iv_shangcheng_favourite_remark4);
        iv_shangcheng_favourite_price = (TextView) view.findViewById(R.id.iv_shangcheng_favourite_price);
        iv_shangcheng_favourite_price1 = (TextView) view.findViewById(R.id.iv_shangcheng_favourite_price1);
        iv_shangcheng_favourite_price2 = (TextView) view.findViewById(R.id.iv_shangcheng_favourite_price2);
        iv_shangcheng_favourite_price3 = (TextView) view.findViewById(R.id.iv_shangcheng_favourite_price3);
        iv_shangcheng_favourite_price4 = (TextView) view.findViewById(R.id.iv_shangcheng_favourite_price4);
        ll_shangchang_favourite = (LinearLayout) view.findViewById(R.id.ll_shangchang_favourite);
        ll_shangchang_favourite1 = (LinearLayout) view.findViewById(R.id.ll_shangchang_favourite1);
        ll_shangchang_favourite2 = (LinearLayout) view.findViewById(R.id.ll_shangchang_favourite2);
        ll_shangchang_favourite3 = (LinearLayout) view.findViewById(R.id.ll_shangchang_favourite3);
        ll_shangchang_favourite4 = (LinearLayout) view.findViewById(R.id.ll_shangchang_favourite4);
        iv_shangcheng_query = view.findViewById(R.id.iv_shangcheng_query);
        tv_shangcheng_mx_time_s = view.findViewById(R.id.tv_shangcheng_mx_time_s);
        tv_shangcheng_mx_time_f = view.findViewById(R.id.tv_shangcheng_mx_time_f);
        ll_shangcheng_item_mx = view.findViewById(R.id.ll_shangcheng_item_mx);
        gv_shangcheng_mx = view.findViewById(R.id.gv_shangcheng_mx);
        ll_shouye_saomiao = view.findViewById(R.id.ll_shouye_saomiao);
        tv_shangcheng_mx_gdsp = view.findViewById(R.id.tv_shangcheng_mx_gdsp);
        tv_shangcheng_mx_gdsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), XsmsActivity.class);

                startActivity(intent);
            }
        });
        ll_shouye_saomiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndPermission.with(getActivity())
                        .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
                        .onGranted(new Action() {
                            @Override
                            public void onAction(List<String> permissions) {
                                Intent intent = new Intent(getActivity(), CaptureActivity.class);
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

                                Toast.makeText(getActivity(), "没有权限无法扫描呦", Toast.LENGTH_LONG).show();
                            }
                        }).start();

            }
        });
        iv_shangcheng_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SplbActivity.class);
                intent.putExtra("", "shangcheng");
                startActivity(intent);
            }
        });
        ll_shangchang_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDialogin();
                dialogin("");
                jianjei(favourite_goods_goods_id.get(0));
            }
        });
        ll_shangchang_favourite1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDialogin();
                dialogin("");
                jianjei(favourite_goods_goods_id.get(1));
            }
        });
        ll_shangchang_favourite2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDialogin();
                dialogin("");
                jianjei(favourite_goods_goods_id.get(2));
            }
        });
        ll_shangchang_favourite3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDialogin();
                dialogin("");
                jianjei(favourite_goods_goods_id.get(3));
            }
        });
        ll_shangchang_favourite4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDialogin();
                dialogin("");
                jianjei(favourite_goods_goods_id.get(4));
            }
        });

        gv_shangcheng = (MyGridView) view.findViewById(R.id.gv_shangcheng);
        gv_shangcheng_hot = (MyGridView) view.findViewById(R.id.gv_shangcheng_hot);
        queue = Volley.newRequestQueue(getActivity());

        gv_shangcheng.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> map = (HashMap<String, String>) gv_shangcheng.getItemAtPosition(i);
                String sId = map.get("id");
                if (sId.equals("0")) {
                 /*   Intent intent = new Intent(getActivity(), ShangchengActivity.class);
                    intent.putExtra("id", sId);
                    startActivity(intent);*/
                    // getActivity().finish();
             /*       main(1, "0");*/
                    Intent intent = new Intent(getActivity(), FenleiActivity.class);
                    //intent.putExtra("id", sId);
                    startActivity(intent);

                } else {
                    //  Toast.makeText(ShangchengActivity.this, "点击了" + sItem, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), SplbActivity.class);
                    intent.putExtra("id", sId);
                    startActivity(intent);
                    //   getActivity().finish();
                }

            }
        });
        isViewCreated = true;
        lazyLoad();
    }

    /**
     * 秒杀
     */
    private void mx() {
        // String url = Api.sUrl + "GetZhixun/getType/user_id/" + sUser_id;
        String url = Api.sUrl + "Goods/miaoshaindex";
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
                        MyAdapterMx myAdapterMx = new MyAdapterMx(getActivity());
                        ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            String resultOriginalImg = jsonObject.getString("original_img");
                            String resultGoodsId = jsonObject.getString("goods_id");
                            String resultOldPrice = jsonObject.getString("old_price");
                            String resultShopPrice = jsonObject.getString("shop_price");
                            String resultRid = jsonObject.getString("rid");
                            String resultGoodsName = jsonObject.getString("goods_name");
                            String resultSalesSum = jsonObject.getString("sales_sum");
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("ItemOriginalImg", resultOriginalImg);
                            map.put("ItemGoodsId", resultGoodsId);
                            map.put("ItemOldPrice", resultOldPrice);
                            map.put("ItemShopPrice", resultShopPrice);
                            map.put("ItemRid", resultRid);
                            map.put("ItemGoodsName", resultGoodsName);
                            map.put("ItemSalesSum", resultSalesSum);
                            mylist.add(map);
                        }
                        myAdapterMx.arrmylist = mylist;
                        gv_shangcheng_mx.setAdapter(myAdapterMx);
                        String resultTime = jsonObject1.getString("time");
                        String[] Time = resultTime.split(":");
                        String s = Time[0];
                        String f = Time[1];
                        tv_shangcheng_mx_time_s.setText(s);
                        tv_shangcheng_mx_time_f.setText(f);
                    } else {
                        ll_shangcheng_item_mx.setVisibility(View.GONE);
                        // Hint(resultMsg, HintDialog.ERROR);

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


    private void tiem() {
        String hour;
        String minute;
        Calendar cal;
        cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        if (cal.get(Calendar.AM_PM) == 0) {
            hour = String.valueOf(cal.get(Calendar.HOUR));
        } else {
            hour = String.valueOf(cal.get(Calendar.HOUR) + 12);
        }
        minute = String.valueOf(cal.get(Calendar.MINUTE));
        int iHour = Integer.valueOf(hour);

        DateFormat df = new SimpleDateFormat("HH:mm");
        long diff = 0;
        long minutes = 0;
        try {
            Date d1 = df.parse(iHour + 1 + ":00");
            Date d2 = df.parse(hour + ":" + minute);

            diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            System.out.println("" + days + "天" + hours + "小时" + minutes + "分");
        } catch (Exception e) {
        }
        timer.schedule(task, diff, 60 * 60 * 1000);
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
                    jsonObjectdate = new JSONObject(Shouye1Fragment.sSaomiao);
                    String sRdsQrcode = jsonObjectdate.getString("rdsQrcode");
                    String sStorename = jsonObjectdate.getString("storename");
                    Intent intent = new Intent(getActivity(), FukuanActivity.class);
                    startActivity(intent);
                } catch (JSONException e) {
                    Hint("非本系统二维码，无法识别", HintDialog.ERROR);
                    //    Toast.makeText(getActivity(), "非本系统二维码，无法识别", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


            }
        }
    }

    private void main(final int iItem, final String sItem) {
        final ScMainActivity mainActivity = (ScMainActivity) getActivity();
        mainActivity.setFragment2Fragment(new ScMainActivity.Fragment2Fragment() {
            @Override
            public void gotoFragment(ViewPager viewPager) {
                //fragment传递数据
                // mainActivity.setFragmentArgu();
                mainActivity.setStr(sItem);
                viewPager.setCurrentItem(iItem);
            }
        });
        mainActivity.forSkip();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }

    private void jianjei(final String pid) {
        String url = Api.sUrl + "Goods/goodsDetail/pid/" + pid;
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
                        Intent intent1 = new Intent(getActivity(), SpjjActivity.class);
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

    private void lazyLoad() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUIVisible) {
            hideDialogin();
            dialogin("");
            shangcheng();
            mx();
            tiem();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;
            //    printLog(mTextviewContent+"可见,加载数据");
        }
    }

    private void shangcheng() {
        String url = Api.sUrl + "Goods";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    JSONObject jsonObject0 = new JSONObject(sDate);
                    String resultMsg = jsonObject0.getString("msg");
                    int resultCode = jsonObject0.getInt("code");
                    if (resultCode > 0) {
                        myAdapter = new MyAdapter(getActivity());
                        ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
                        JSONObject jsonObject1 = new JSONObject(sDate.toString()).getJSONObject("data");
                        JSONArray jsonArray = jsonObject1.getJSONArray("catList");
                        //  for (int i = 0; i < 2; i++) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
                            String ItemId = jsonObject2.getString("id");
                            String ItemName = jsonObject2.getString("name");
                            String ItemImage = jsonObject2.getString("image");
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("iv_image", ItemImage);
                            map.put("miaoshu", ItemName);
                            map.put("id", ItemId);
                            mylist.add(map);
                        }
                        gridviewdata(mylist);
                        JSONArray jsonArray1 = jsonObject1.getJSONArray("hot_goods");
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            if (i <= 3) {
                                JSONObject jsonObject3 = (JSONObject) jsonArray1.opt(i);
                                myAdapter.arrHotimg.add(jsonObject3.getString("original_img"));
                                myAdapter.arrHotName.add(jsonObject3.getString("goods_name"));
                                myAdapter.arrHotRemark.add(jsonObject3.getString("collect_sum"));
                                myAdapter.arrHotPrice.add(jsonObject3.getString("shop_price"));
                                myAdapter.arrGoodsId.add(jsonObject3.getString("goods_id"));
                            }
                        }
                        gv_shangcheng_hot.setAdapter(myAdapter);
                        JSONArray jsonArray2 = jsonObject1.getJSONArray("favourite_goods");
                        int iFavouriteGoods = jsonArray2.length();
                        for (int i = 0; i < iFavouriteGoods; i++) {
                            JSONObject jsonObject4 = (JSONObject) jsonArray2.opt(i);
                            favourite_goods_goods_name.add(jsonObject4.getString("goods_name"));
                            favourite_goods_original_img.add(jsonObject4.getString("original_img"));
                            favourite_goods_goods_id.add(jsonObject4.getString("goods_id"));
                            favourite_goods_cat_id.add(jsonObject4.getString("cat_id"));
                            favourite_goods_extend_cat_id.add(jsonObject4.getString("extend_cat_id"));
                            favourite_goods_goods_sn.add(jsonObject4.getString("goods_sn"));
                            favourite_goods_click_count.add(jsonObject4.getString("click_count"));
                            favourite_goods_brand_id.add(jsonObject4.getString("brand_id"));
                            favourite_goods_store_count.add(jsonObject4.getString("store_count"));
                            favourite_goods_comment_count.add(jsonObject4.getString("comment_count"));
                            favourite_goods_market_price.add(jsonObject4.getString("shop_price"));
                            favourite_goods_keywords.add(jsonObject4.getString("keywords"));
                            favourite_goods_goods_remark.add(jsonObject4.getString("goods_remark"));
                            favourite_goods_mobile_content.add(jsonObject4.getString("mobile_content"));
                            favourite_goods_collect_sum.add(jsonObject4.getString("collect_sum"));
                            favourite_goods_is_free_shipping.add(jsonObject4.getString("is_free_shipping"));
                        }
                        if (iFavouriteGoods > 0) {
                            Glide.with(getActivity())
                                    .load( Api.sUrl+ favourite_goods_original_img.get(0))
                                    .asBitmap().dontAnimate()
                                    .into(iv_shangcheng_favourite_img);
                            iv_shangcheng_favourite_name.setText(favourite_goods_goods_name.get(0));
                            iv_shangcheng_favourite_remark.setText(favourite_goods_goods_remark.get(0));
                            iv_shangcheng_favourite_price.setText("￥" + favourite_goods_market_price.get(0));
                        }
                        if (iFavouriteGoods > 1) {
                            Glide.with(getActivity())
                                    .load( Api.sUrl+favourite_goods_original_img.get(1))
                                    .asBitmap().dontAnimate()
                                    .into(iv_shangcheng_favourite_img1);
                            iv_shangcheng_favourite_name1.setText(favourite_goods_goods_name.get(1));
                            iv_shangcheng_favourite_remark1.setText(favourite_goods_goods_remark.get(1));
                            iv_shangcheng_favourite_price1.setText("￥" + favourite_goods_market_price.get(1));
                        }
                        if (iFavouriteGoods > 2) {
                            Glide.with(getActivity())
                                    .load( Api.sUrl+favourite_goods_original_img.get(2))
                                    .asBitmap().dontAnimate()
                                    .into(iv_shangcheng_favourite_img2);
                            iv_shangcheng_favourite_name2.setText(favourite_goods_goods_name.get(2));
                            iv_shangcheng_favourite_remark2.setText(favourite_goods_goods_remark.get(2));
                            iv_shangcheng_favourite_price2.setText("￥" + favourite_goods_market_price.get(2));
                        }
                        if (iFavouriteGoods > 3) {
                            Glide.with(getActivity())
                                    .load( Api.sUrl+favourite_goods_original_img.get(3))
                                    .asBitmap().dontAnimate()
                                    .into(iv_shangcheng_favourite_img3);
                            iv_shangcheng_favourite_name3.setText(favourite_goods_goods_name.get(3));
                            iv_shangcheng_favourite_remark3.setText(favourite_goods_goods_remark.get(3));
                            iv_shangcheng_favourite_price3.setText("￥" + favourite_goods_market_price.get(3));
                        }
                        if (iFavouriteGoods > 4) {
                            Glide.with(getActivity())
                                    .load( Api.sUrl+ favourite_goods_original_img.get(4))
                                    .asBitmap().dontAnimate()
                                    .into(iv_shangcheng_favourite_img4);
                            iv_shangcheng_favourite_name4.setText(favourite_goods_goods_name.get(4));
                            iv_shangcheng_favourite_remark4.setText(favourite_goods_goods_remark.get(4));
                            iv_shangcheng_favourite_price4.setText("￥" + favourite_goods_market_price.get(4));
                        }

                        JSONArray jsonArray3 = jsonObject1.getJSONArray("catList1");
                        final List<Banner> banners = new ArrayList<>();
                        for (int i = 0; i < jsonArray3.length(); i++) {
                            JSONObject jsonObject5 = (JSONObject) jsonArray3.opt(i);
                            catList1_ad_link.add(jsonObject5.getString("ad_link"));
                            catList1_ad_id.add(jsonObject5.getString("ad_id"));
                            catList1_ad_name.add(jsonObject5.getString("ad_name"));
                            catList1_ad_code.add(jsonObject5.getString("ad_code"));
                            catList1_gid.add(jsonObject5.getString("gid"));
                            banners.add(new Banner(jsonObject5.getString("ad_code")));
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
                         /*       dialogin("");
                                jianjei(catList1_gid.get(position));*/
                                Intent intent = new Intent(getActivity(), GuanggaoActivity.class);
                                intent.putExtra("link", catList1_ad_link.get(position));
                                startActivity(intent);
                                //Toast.makeText(getActivity(), "position: " + position, Toast.LENGTH_SHORT).show();
                            }
                        });

                        JSONArray jsonArray4 = jsonObject1.getJSONArray("catList2");
                        final List<Banner> banners2 = new ArrayList<>();
                        for (int i = 0; i < jsonArray4.length(); i++) {
                            JSONObject jsonObject5 = (JSONObject) jsonArray4.opt(i);
                            catList1_ad_link1.add(jsonObject5.getString("ad_link"));
                            catList1_ad_id1.add(jsonObject5.getString("ad_id"));
                            catList1_ad_name1.add(jsonObject5.getString("ad_name"));
                            catList1_ad_code1.add(jsonObject5.getString("ad_code"));
                            catList1_gid1.add(jsonObject5.getString("gid"));
                            banners2.add(new Banner(jsonObject5.getString("ad_code")));
                        }
                        recyclerViewBanner2.setRvBannerData(banners2);
                        recyclerViewBanner2.setOnSwitchRvBannerListener(new RecyclerViewBanner.OnSwitchRvBannerListener() {
                            @Override
                            public void switchBanner(int position, AppCompatImageView bannerView) {
                                Glide.with(bannerView.getContext())
                                        .load( Api.sUrl+banners2.get(position).getUrl())
                                        .asBitmap().dontAnimate()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .into(bannerView);
                            }
                        });
                        recyclerViewBanner2.setOnRvBannerClickListener(new RecyclerViewBanner.OnRvBannerClickListener() {
                            @Override
                            public void onClick(int position) {
                                hideDialogin();
                                dialogin("");
                                jianjei(catList1_gid1.get(position));
                                //   Toast.makeText(getActivity(), "position: " + position, Toast.LENGTH_SHORT).show();
                            }
                        });

                        JSONArray jsonArray5 = jsonObject1.getJSONArray("catList3");
                        final List<Banner> banners3 = new ArrayList<>();
                        for (int i = 0; i < jsonArray5.length(); i++) {
                            JSONObject jsonObject5 = (JSONObject) jsonArray5.opt(i);
                            catList1_ad_link2.add(jsonObject5.getString("ad_link"));
                            catList1_ad_id2.add(jsonObject5.getString("ad_id"));
                            catList1_ad_name2.add(jsonObject5.getString("ad_name"));
                            catList1_ad_code2.add(jsonObject5.getString("ad_code"));
                            catList1_gid2.add(jsonObject5.getString("gid"));
                            banners3.add(new Banner( jsonObject5.getString("ad_code")));
                        }
                        recyclerViewBanner3.setRvBannerData(banners3);
                        recyclerViewBanner3.setOnSwitchRvBannerListener(new RecyclerViewBanner.OnSwitchRvBannerListener() {
                            @Override
                            public void switchBanner(int position, AppCompatImageView bannerView) {
                                Glide.with(bannerView.getContext())
                                        .load( Api.sUrl+banners3.get(position).getUrl())
                                        .asBitmap().dontAnimate()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .into(bannerView);
                            }
                        });
                        recyclerViewBanner3.setOnRvBannerClickListener(new RecyclerViewBanner.OnRvBannerClickListener() {
                            @Override
                            public void onClick(int position) {
                                hideDialogin();
                                dialogin("");
                                jianjei(catList1_gid2.get(position));
                                //  Toast.makeText(getActivity(), "position: " + position, Toast.LENGTH_SHORT).show();
                            }
                        });

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
        mAdapter = new GridViewAdaptershangcheng(getActivity(), myList);
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(getActivity(), R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(getActivity(), R.style.dialog, sHint, type, true).show();
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        public ArrayList<String> arrHotimg;
        public ArrayList<String> arrHotName;
        public ArrayList<String> arrHotRemark;
        public ArrayList<String> arrHotPrice;
        public ArrayList<String> arrGoodsId;

        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arrHotimg = new ArrayList<String>();
            arrHotName = new ArrayList<String>();
            arrHotRemark = new ArrayList<String>();
            arrHotPrice = new ArrayList<String>();
            arrGoodsId = new ArrayList<String>();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arrHotimg.size();
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
                view = inflater.inflate(R.layout.shangcheng_hot_item1, null);
            }
            ImageView iv_shangcheng_hot_img = (ImageView) view.findViewById(R.id.iv_shangcheng_hot_img);
            LinearLayout ll_shangcheng_hot_item = (LinearLayout) view.findViewById(R.id.ll_shangcheng_hot_item);
            TextView iv_shangcheng_hot_name = (TextView) view.findViewById(R.id.iv_shangcheng_hot_name);
            TextView iv_shangcheng_hot_remark = (TextView) view.findViewById(R.id.iv_shangcheng_hot_remark);
            TextView iv_shangcheng_hot_price = (TextView) view.findViewById(R.id.iv_shangcheng_hot_price);
            iv_shangcheng_hot_name.setText(arrHotName.get(position));
            iv_shangcheng_hot_remark.setText(arrHotRemark.get(position) + "人付款");
            iv_shangcheng_hot_price.setText(arrHotPrice.get(position));
            Glide.with(getActivity()).load( Api.sUrl+ arrHotimg.get(position))
                    .asBitmap()
                    .dontAnimate()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .into(iv_shangcheng_hot_img);
            ll_shangcheng_hot_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideDialogin();
                    dialogin("");
                    jianjei(arrGoodsId.get(position));
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
            tv_shangcheng_mx_name.setText(arrmylist.get(position).get("ItemGoodsName").toString());
            tv_shangcheng_mx_sales_sum.setText("已抢" + arrmylist.get(position).get("ItemSalesSum").toString() + "件");
            tv_shangcheng_mx_shop_price.setText(arrmylist.get(position).get("ItemShopPrice").toString());
            Glide.with(getActivity()).load( Api.sUrl+ arrmylist.get(position).get("ItemOriginalImg"))
                    .asBitmap()
                    .dontAnimate()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .into(iv_shangcheng_mx_img);
            ll_shangcheng_mx_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideDialogin();
                    dialogin("");
                    jianjei(arrmylist.get(position).get("ItemGoodsId").toString());
                }
            });
      /*      map.put("ItemOriginalImg", resultOriginalImg);
            map.put("ItemGoodsId", resultGoodsId);
            map.put("ItemOldPrice", resultOldPrice);
            map.put("ItemShopPrice", resultShopPrice);
            map.put("ItemRid", resultRid);
            map.put("ItemGoodsName", resultGoodsName);
            map.put("ItemSalesSum", resultSalesSum);
            mylist.add(map);*/

            return view;
        }
    }
}
