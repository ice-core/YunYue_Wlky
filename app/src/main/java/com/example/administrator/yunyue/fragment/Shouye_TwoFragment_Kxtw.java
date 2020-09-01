package com.example.administrator.yunyue.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.Jjcc;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.ViewPagerAdapter;
import com.example.administrator.yunyue.activity.ShouyeActivity;
import com.example.administrator.yunyue.activity.ZixunActivity;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.dialog.PromptDialog;
import com.example.administrator.yunyue.jysq_activity.Jysq_Redianctivity;
import com.example.administrator.yunyue.jysq_activity.Jysq_XqActivity;
import com.example.administrator.yunyue.sc.ObservableScrollView;
import com.example.administrator.yunyue.sc_activity.BzyfkActivity;
import com.example.administrator.yunyue.sc_activity.GuanggaoActivity;
import com.example.administrator.yunyue.sc_activity.SjlbActivity;
import com.example.administrator.yunyue.sc_activity.SplbActivity;
import com.example.administrator.yunyue.sc_activity.WdddActivity;
import com.example.administrator.yunyue.sc_activity.XsmsActivity;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.example.administrator.yunyue.yjdt_activity.YjdtActivity;
import com.example.administrator.yunyue.zb_activity.ZhiboActivity;
import com.loonggg.rvbanner.lib.RecyclerViewBanner;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

import static io.rong.imkit.utilities.RongUtils.getScreenWidth;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Shouye_TwoFragment_Kxtw.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Shouye_TwoFragment_Kxtw#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Shouye_TwoFragment_Kxtw extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int[] imge = {};

    private GridView gv_tu;
    private ObservableScrollView sv_shouye;
    private MyGridView mgv_shouye_zixun;
    private LinearLayout ll_shouye_sc;
    private LinearLayout ll_shouye_sjrz;
    private LinearLayout ll_shouye_rdxw;
    private LinearLayout ll_shouye_yjdt;
    private LinearLayout ll_shouye_lxwm;
    private CardView cav_shouye_zhixun;
    private LinearLayout ll_shouye_zhibo;
    private GridView gv_lb_x_tu;

    private String[] banner = {"全国商城", "新闻资讯", "优质广告"};
    private String[] zhibo = {"宝马540i首次驾驶", "沃尔沃V60 T6 AWD"};
    private ArrayList<Image> arrayList = new ArrayList<Image>();
    private ArrayList<Bitmap> arrayListTu = new ArrayList<Bitmap>();
    private OnFragmentInteractionListener mListener;
    //private Banner shouye_banner;
    private ArrayList<Integer> imagPath;

    List<String> catList1_ad_id = new ArrayList<String>();//图片id
    List<String> catList1_ad_link = new ArrayList<String>();//图片链接
    private ViewPager mViewPager;
    private TextView mTvPagerTitle;

    private List<ImageView> mImageList;//轮播的图片集合
    //private String[] mImageTitles;//标题集合
    private int previousPosition = 0;//前一个被选中的position
    private List<View> mDots;//小点

    private boolean isStop = false;//线程是否停止
    private static int PAGER_TIOME = 5000;//间隔时间

    private LinearLayout linearLayoutDots;
    RequestQueue queue = null;
    private SharedPreferences pref;
    private String sUser_id;

    ArrayList<HashMap<String, String>> mylist_banner;
    private ImageView iv_shouye_two_guanggao;
    RecyclerViewBanner rv_banner_1;
    @BindView(R.id.srl_control_shouye)
    SmartRefreshLayout srlControlShouye;
    //咨询分页
    int iPage = 1;
    MyAdapterZixun myAdapterZixun;
    private TextView tv_shouye_two_count;

    private LinearLayout ll_shouye_two_query;

    /**
     * 话题热点查看更多
     */
    private TextView tv_jysq_shouye_re_ckgd;

    /**
     * 热点话题
     */
    private GridView gv_jysq_redian;

    public Shouye_TwoFragment_Kxtw() {
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
    public static Shouye_TwoFragment_Kxtw newInstance(String param1, String param2) {
        Shouye_TwoFragment_Kxtw fragment = new Shouye_TwoFragment_Kxtw();
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
        return inflater.inflate(R.layout.fragment_shouye_two_kxtw, container, false);
    }

    public void onViewCreated(View view, Bundle saveInstancesState) {
        super.onViewCreated(view, saveInstancesState);
        queue = Volley.newRequestQueue(getActivity());
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sUser_id = pref.getString("user_id", "");
        rv_banner_1 = (RecyclerViewBanner) view.findViewById(R.id.rv_banner_1);
        gv_tu = view.findViewById(R.id.gv_tu);
        sv_shouye = view.findViewById(R.id.sv_shouye);
        srlControlShouye = view.findViewById(R.id.srl_control_shouye);
        mgv_shouye_zixun = view.findViewById(R.id.mgv_shouye_zixun);
        mgv_shouye_zixun.setSelector(new ColorDrawable(Color.TRANSPARENT));
        ll_shouye_sc = view.findViewById(R.id.ll_shouye_sc);
        ll_shouye_sjrz = view.findViewById(R.id.ll_shouye_sjrz);
        ll_shouye_rdxw = view.findViewById(R.id.ll_shouye_rdxw);
        ll_shouye_yjdt = view.findViewById(R.id.ll_shouye_yjdt);
        ll_shouye_lxwm = view.findViewById(R.id.ll_shouye_lxwm);
        cav_shouye_zhixun = view.findViewById(R.id.cav_shouye_zhixun);
        ll_shouye_zhibo = view.findViewById(R.id.ll_shouye_zhibo);
        gv_lb_x_tu = view.findViewById(R.id.gv_lb_x_tu);
        tv_shouye_two_count = view.findViewById(R.id.tv_shouye_two_count);
        iv_shouye_two_guanggao = view.findViewById(R.id.iv_shouye_two_guanggao);
        ll_shouye_two_query = view.findViewById(R.id.ll_shouye_two_query);
        gv_jysq_redian = view.findViewById(R.id.gv_jysq_redian);
        tv_jysq_shouye_re_ckgd = view.findViewById(R.id.tv_jysq_shouye_re_ckgd);
        tv_jysq_shouye_re_ckgd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Jysq_Redianctivity.class);
                startActivity(intent);
            }
        });

        ll_shouye_two_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SplbActivity.class);
                intent.putExtra("", "shangcheng");
                startActivity(intent);
            }
        });
        ll_shouye_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WdddActivity.class);
                intent.putExtra("id", "0");
                startActivity(intent);
            }
        });
        ll_shouye_sjrz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SjlbActivity.class);
                startActivity(intent);
            }
        });
        ll_shouye_rdxw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sv_shouye.post(new Runnable() {
                    @Override
                    public void run() {
                        //  sv_shouye.scrollTo(0, cav_shouye_zhixun.getBottom());
                        Intent intent = new Intent(getActivity(), XsmsActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        ll_shouye_yjdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sv_shouye.post(new Runnable() {
                    @Override
                    public void run() {
                        //sv_shouye.scrollTo(0, ll_shouye_zhibo.getBottom());
                 /*       Intent intent = new Intent(getActivity(), HaoyouActivity.class);
                        startActivity(intent);*/
                        //Hint("敬请期待", HintDialog.WARM);
                        Intent intent = new Intent(getActivity(), YjdtActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        ll_shouye_lxwm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BzyfkActivity.class);
                startActivity(intent);
            /*    showDialog1("立即拨打平台客服联系我们\n" +
                        "15618960712");*/
            }
        });
        //setGridView();
        // gv_zixun();
        setGridViewbanner_xia();
        mViewPager = view.findViewById(R.id.viewPager);
        mTvPagerTitle = view.findViewById(R.id.tv_pager_title);
        linearLayoutDots = view.findViewById(R.id.lineLayout_dot);
        query();
      //  query_zhibo();
        query_zx();
        smartRefresh();
        query_sqrd();
    }

    //监听下拉和上拉状态
    public void smartRefresh() {
        //下拉刷新
        srlControlShouye.setOnRefreshListener(refreshlayout -> {
            srlControlShouye.setEnableRefresh(true);//启用刷新
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 Data() 来模拟我们刷新出来的数据
             */
            iPage = 1;
            query();
          //  query_zhibo();
            query_zx();
            query_sqrd();
        });
        //上拉加载
        srlControlShouye.setOnLoadmoreListener(refreshlayout -> {
            srlControlShouye.setEnableLoadmore(true);//启用加载
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 MoreDatas() 来模拟我们加载出来的数据
             */
            iPage += 1;
            query_zx();
        });
    }


    /**
     * 社群首页信息获取
     */
    private void query_sqrd() {
        String url = Api.sUrl + "Shequn/Api/index/appId/" + Api.sApp_Id + "/user_id/" + sUser_id + "/page/" + iPage;
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
                    ArrayList<HashMap<String, String>> mylist_post = new ArrayList<HashMap<String, String>>();
                    if (resultCode > 0) {
                        JSONObject jsonObjectdata = new JSONObject(sDate.toString()).getJSONObject("data");
                        JSONArray jsonArray_redian = jsonObjectdata.getJSONArray("redian");
                        ArrayList<HashMap<String, String>> mylist_redian = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < jsonArray_redian.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArray_redian.opt(i);
                            String ItemId = jsonObject2.getString("id");
                            String ItemTitle = jsonObject2.getString("title");
                            String ItemImg = jsonObject2.getString("img");

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", ItemId);
                            map.put("title", ItemTitle);
                            map.put("img", ItemImg);
                            mylist_redian.add(map);
                        }
                        setGridView(mylist_redian);
                        // Hint(resultMsg, HintDialog.SUCCESS);
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
     * 话题热点
     * 设置GirdView参数，绑定数据
     */
    private void setGridView(ArrayList<HashMap<String, String>> mylist) {
        MyAdapter myAdapter = new MyAdapter(getActivity());
        myAdapter.arrlist = mylist;
        int size = mylist.size();
        int length = 110;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gv_jysq_redian.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gv_jysq_redian.setColumnWidth(itemWidth); // 设置列表项宽
        gv_jysq_redian.setHorizontalSpacing(10); // 设置列表项水平间距
        gv_jysq_redian.setStretchMode(GridView.NO_STRETCH);
        gv_jysq_redian.setNumColumns(size); // 设置列数量=列表集合数
        gv_jysq_redian.setAdapter(myAdapter);
/*        sv_shequn.smoothScrollTo(0, 20);
        sv_shequn.setFocusable(true);*/

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
                view = inflater.inflate(R.layout.jysq_rd_item, null);
            }
        /*    map.put("id", ItemId);
            map.put("title", ItemTitle);
            map.put("img", ItemImg);*/
            LinearLayout ll_jysq_rd_item = view.findViewById(R.id.ll_jysq_rd_item);
            TextView tv_jysq_rd_item_title = view.findViewById(R.id.tv_jysq_rd_item_title);
            tv_jysq_rd_item_title.setText(arrlist.get(position).get("title"));

            ImageView iv_jysq_rd_item_img = view.findViewById(R.id.iv_jysq_rd_item_img);
            Glide.with(getActivity())
                    .load( Api.sUrl+arrlist.get(position).get("img"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_jysq_rd_item_img);
            ll_jysq_rd_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), Jysq_XqActivity.class);
                    intent.putExtra("id", arrlist.get(position).get("id"));
                    startActivity(intent);
                }
            });
            return view;
        }
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

    /**
     * 直播首页信息获取
     */
    private void query_zhibo() {
        String url = Api.sUrl + "Broadcast/Api/index/appId/" + Api.sApp_Id;
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
                        JSONObject jsonObjectdata = new JSONObject(sDate.toString()).getJSONObject("data");
                        JSONArray jsonArrayvideo = jsonObjectdata.getJSONArray("video");
                        ArrayList<HashMap<String, String>> mylist_video = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < jsonArrayvideo.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArrayvideo.opt(i);
                            String Itemid = jsonObject2.getString("id");
                            String Itemtitle = jsonObject2.getString("title");
                            String Itemvideo_img = jsonObject2.getString("video_img");
                            String Itemvideo_address = jsonObject2.getString("video_address");
                            String Itemplay_cont = jsonObject2.getString("play_cont");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", Itemid);
                            map.put("title", Itemtitle);
                            map.put("video_img", Itemvideo_img);
                            map.put("video_address", Itemvideo_address);
                            map.put("play_cont", Itemplay_cont);
                            mylist_video.add(map);
                        }

                        setGridViewzhibo(mylist_video);
                        // Hint(resultMsg, HintDialog.SUCCESS);
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
     * 社群首页信息获取
     */
    private void query() {
        String url = Api.sUrl + "Api/Index/index/appId/" + Api.sApp_Id;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                srlControlShouye.finishRefresh();//结束刷新
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        JSONObject jsonObjectdata = new JSONObject(sDate.toString()).getJSONObject("data");
                        JSONArray jsonArray_lun = jsonObjectdata.getJSONArray("banner");
                        final List<Banner> banners = new ArrayList<>();
                        for (int i = 0; i < jsonArray_lun.length(); i++) {
                            JSONObject jsonObject5 = (JSONObject) jsonArray_lun.opt(i);
                            catList1_ad_link.add(jsonObject5.getString("link"));
                            catList1_ad_id.add(jsonObject5.getString("id"));

                            banners.add(new Banner(jsonObject5.getString("url")));
                        }
                        rv_banner_1.setRvBannerData(banners);
                        rv_banner_1.setOnSwitchRvBannerListener(new RecyclerViewBanner.OnSwitchRvBannerListener() {
                            @Override
                            public void switchBanner(int position, AppCompatImageView bannerView) {
                                int screenwidth = getScreenWidth(); //获取屏幕的宽度
                                ViewGroup.LayoutParams layoutParams = rv_banner_1.getLayoutParams();//获取banner组件的参数

                                Glide.with(bannerView.getContext())
                                        .load( Api.sUrl+banners.get(position).getUrl())
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .skipMemoryCache(true)
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                                bannerView.setImageBitmap(resource);
                                                //比例
                                                double bl = 0;
                                                bl = Jjcc.div(Double.valueOf(resource.getHeight()), Double.valueOf(resource.getWidth()));
                                                //比例
                                                double gao = 0;
                                                gao = Jjcc.mul(Double.valueOf(screenwidth), bl);
                                                int iGao = (new Double(gao)).intValue();
                                                layoutParams.height = iGao; //这里设置轮播图的长度等于宽度
                                                rv_banner_1.setLayoutParams(layoutParams); //设置参数
                                            }
                                        });
                            }
                        });
                        rv_banner_1.setOnRvBannerClickListener(new RecyclerViewBanner.OnRvBannerClickListener() {
                            @Override
                            public void onClick(int position) {
                                Intent intent = new Intent(getActivity(), GuanggaoActivity.class);
                                intent.putExtra("link", catList1_ad_link.get(position));
                                startActivity(intent);
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
                srlControlShouye.finishLoadmore();//结束加载
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        request.setShouldCache(false);
        queue.add(request);
    }

    /**
     * 热门资讯
     */
    private void query_zx() {
        String url = Api.sUrl + "Api/Index/zixun/appId/" + Api.sApp_Id + "/page/" + iPage;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                srlControlShouye.finishLoadmore();//结束加载
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    ArrayList<HashMap<String, String>> mylist_data = new ArrayList<HashMap<String, String>>();
                    if (resultCode > 0) {

                        JSONObject jsonObjectdata = new JSONObject(sDate.toString()).getJSONObject("data");
                        int resultcount = jsonObjectdata.getInt("count");
                        if (resultcount > 99) {
                            tv_shouye_two_count.setText("99+");
                        } else {
                            tv_shouye_two_count.setText(String.valueOf(resultcount));
                        }
                        JSONArray jsonArray_lun = jsonObjectdata.getJSONArray("zixun");
                        for (int i = 0; i < jsonArray_lun.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArray_lun.opt(i);
                            String ItemId = jsonObject2.getString("id");
                            String ItemTitle = jsonObject2.getString("title");
                            String ItemDetail = jsonObject2.getString("detail");
                            String ItemTime = jsonObject2.getString("time");
                            String ItemImg = jsonObject2.getString("img");
                            String ItemPinglun_Num = jsonObject2.getString("pinglun_num");
                            String ItemZan = jsonObject2.getString("zan");
                            String ItemView = jsonObject2.getString("view");
                            String ItemUrl = jsonObject2.getString("url");

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", ItemId);
                            map.put("title", ItemTitle);
                            map.put("detail", ItemDetail);
                            map.put("time", ItemTime);
                            map.put("img", ItemImg);
                            map.put("pinglun_num", ItemPinglun_Num);
                            map.put("zan", ItemZan);
                            map.put("view", ItemView);
                            map.put("url", ItemUrl);
                            mylist_data.add(map);
                        }
                        if (iPage == 1) {
                            gv_zixun(mylist_data);
                        } else {
                            if (mylist_data.size() > 0) {
                                gv_zixun1(mylist_data);
                            } else {
                                iPage -= 1;
                            }
                        }
                    } else {
                        Hint(resultMsg, HintDialog.ERROR);
                        if (iPage == 1) {
                            gv_zixun(mylist_data);
                        } else {
                            if (mylist_data.size() > 0) {
                                gv_zixun1(mylist_data);
                            } else {
                                iPage -= 1;
                            }
                        }
                    }
                } catch (JSONException e) {
                    srlControlShouye.finishLoadmore();//结束加载
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

    /**
     * 第一步、初始化控件
     */
    public void init() {
        initData();//初始化数据
        initView();//初始化View，设置适配器
        autoPlayView();//开启线程，自动播放
    }

    /**
     * 第二步、初始化数据（图片、标题、点击事件）
     */
    public void initData() {
        //初始化标题列表和图片
        // mImageTitles = new String[]{"1/3", "2/3", "3/3"};
        int[] imageRess = new int[]{R.drawable.banner_2x, R.drawable.banner_2x, R.drawable.banner_2x};

        //添加图片到图片列表里
        mImageList = new ArrayList<>();
        ImageView iv;
        for (int i = 0; i < mylist_banner.size(); i++) {
            iv = new ImageView(getActivity());
            // iv.setBackgroundResource(imageRess[i]);
            // iv.setBackgroundResource(imageRess[i]);
            Glide.with(getActivity())
                    .load( Api.sUrl+mylist_banner.get(i).get("url"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv);
            //设置图片
            //  iv.setId(imgae_ids[i]);//顺便给图片设置id
            // iv.setOnClickListener(new pagerImageOnClick());//设置图片点击事件
            mImageList.add(iv);
        }

        //添加轮播点
        //  LinearLayout linearLayoutDots = (LinearLayout) findViewById(R.id.lineLayout_dot);
        mDots = addDots(linearLayoutDots, fromResToDrawable(getActivity(), R.drawable.svg_ic_unselected), mImageList.size());//其中fromResToDrawable()方法是我自定义的，目的是将资源文件转成Drawable

    }

    /**
     * 第三步、给PagerViw设置适配器，并实现自动轮播功能
     */
    public void initView() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mImageList, mViewPager);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //伪无限循环，滑到最后一张图片又从新进入第一张图片
                int newPosition = position % mImageList.size();
                // 把当前选中的点给切换了, 还有描述信息也切换
                //  mTvPagerTitle.setText(mImageTitles[newPosition]);//图片下面设置显示文本
                mTvPagerTitle.setText(mylist_banner.get(newPosition).get("text"));//图片下面设置显示文本
                //设置轮播点
                LinearLayout.LayoutParams newDotParams = (LinearLayout.LayoutParams) mDots.get(newPosition).getLayoutParams();
                newDotParams.width = 24;
                newDotParams.height = 24;

                LinearLayout.LayoutParams oldDotParams = (LinearLayout.LayoutParams) mDots.get(previousPosition).getLayoutParams();
                oldDotParams.width = 16;
                oldDotParams.height = 16;

                // 把当前的索引赋值给前一个索引变量, 方便下一次再切换.
                previousPosition = newPosition;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setFirstLocation();
    }

    /**
     * 第四步：设置刚打开app时显示的图片和文字
     */
    private void setFirstLocation() {
        mTvPagerTitle.setText(mylist_banner.get(previousPosition).get("text"));
        // 把ViewPager设置为默认选中Integer.MAX_VALUE / t2，从十几亿次开始轮播图片，达到无限循环目的;
        int m = (Integer.MAX_VALUE / 2) % mImageList.size();
        int currentPosition = Integer.MAX_VALUE / 2 - m;
        mViewPager.setCurrentItem(currentPosition);
    }

    /**
     * 第五步: 设置自动播放,每隔PAGER_TIOME秒换一张图片
     */
    private void autoPlayView() {
        //自动播放图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStop) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                        }
                    });
                    SystemClock.sleep(PAGER_TIOME);
                }
            }
        }).start();
    }

    /**
     * 资源图片转Drawable
     *
     * @param context
     * @param resId
     * @return
     */
    public Drawable fromResToDrawable(Context context, int resId) {
        return context.getResources().getDrawable(resId);
    }

    /**
     * 动态添加一个点
     *
     * @param linearLayout 添加到LinearLayout布局
     * @param backgount    设置
     * @return
     */
    public int addDot(final LinearLayout linearLayout, Drawable backgount) {
        final View dot = new View(getActivity());
        LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dotParams.width = 16;
        dotParams.height = 16;
        dotParams.setMargins(4, 0, 4, 0);
        dot.setLayoutParams(dotParams);
        dot.setBackground(backgount);
        dot.setId(View.generateViewId());
        linearLayout.addView(dot);
        return dot.getId();
    }

    /**
     * 添加多个轮播小点到横向线性布局
     *
     * @param linearLayout
     * @param backgount
     * @param number
     * @return
     */
    public List<View> addDots(final LinearLayout linearLayout, Drawable backgount, int number) {
        List<View> dots = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            int dotId = addDot(linearLayout, backgount);
            dots.add(getActivity().findViewById(dotId));
        }
        return dots;
    }

    /**
     *  * 方法名：showDialog()
     *  * 功    能：退出消息确认
     *  * 参    数：无
     *  * 返回值：无
     */
    protected void showDialog1(String msg) {
        PromptDialog pd = new PromptDialog(getActivity(), R.style.dialog, msg, new PromptDialog.OnClickListener() {
            @Override
            public void onCancelClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onConfimClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    diallPhone("15618960712");
                    dialog.dismiss();
                }
            }
        });
        pd.setPositiveName("拨打");
        pd.setNegativeName("取消");
        pd.setTitle("联系我们");
        pd.show();
        pd.setCanceledOnTouchOutside(false);
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
     * 热门咨询
     */
    private void gv_zixun(ArrayList<HashMap<String, String>> mylist) {
        myAdapterZixun = new MyAdapterZixun(getActivity());
        myAdapterZixun.arrlist = mylist;
        mgv_shouye_zixun.setAdapter(myAdapterZixun);
    }

    private void gv_zixun1(ArrayList<HashMap<String, String>> mylist) {
        myAdapterZixun.arrlist.addAll(mylist);
        mgv_shouye_zixun.setAdapter(myAdapterZixun);
    }

    /**
     * 设置GirdView参数，绑定数据 bannerxia
     */
    private void setGridViewbanner_xia() {
        MyAdapter_banner myAdapter_banner = new MyAdapter_banner(getActivity());
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < banner.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemName", banner[i]);
            mylist.add(map);
        }
        myAdapter_banner.arrlist = mylist;
        int size = mylist.size();
        int length = 154;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gv_lb_x_tu.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gv_lb_x_tu.setColumnWidth(itemWidth); // 设置列表项宽
        gv_lb_x_tu.setHorizontalSpacing(5); // 设置列表项水平间距
        gv_lb_x_tu.setStretchMode(GridView.NO_STRETCH);
        gv_lb_x_tu.setNumColumns(size); // 设置列数量=列表集合数

        gv_lb_x_tu.setAdapter(myAdapter_banner);
        sv_shouye.smoothScrollTo(0, 20);
        sv_shouye.setFocusable(true);

    }

    /**
     * 设置GirdView参数，绑定数据 bannerxia
     */
    private void setGridViewzhibo(ArrayList<HashMap<String, String>> mylist) {
        MyAdapter_zhibo myAdapter_zhibo = new MyAdapter_zhibo(getActivity());

        myAdapter_zhibo.arrlist = mylist;
        int size = mylist.size();
        int length = 270;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gv_tu.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gv_tu.setColumnWidth(itemWidth); // 设置列表项宽
        gv_tu.setHorizontalSpacing(5); // 设置列表项水平间距
        gv_tu.setStretchMode(GridView.NO_STRETCH);
        gv_tu.setNumColumns(size); // 设置列数量=列表集合数

        gv_tu.setAdapter(myAdapter_zhibo);
        sv_shouye.smoothScrollTo(0, 20);
        sv_shouye.setFocusable(true);

    }

    private class MyAdapter_banner extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        ArrayList<HashMap<String, String>> arrlist;

        public MyAdapter_banner(Context context) {
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
                view = inflater.inflate(R.layout.shouye_banner_xia_item, null);
            }
            TextView tv_banner_item_name = view.findViewById(R.id.tv_banner_item_name);
            tv_banner_item_name.setText(arrlist.get(position).get("ItemName"));
            LinearLayout ll_shouye_banner = view.findViewById(R.id.ll_shouye_banner);
            ImageView iv_shouye_banner_zi = view.findViewById(R.id.iv_shouye_banner_zi);
            if (position == 0) {
                ll_shouye_banner.setBackgroundResource(R.drawable.bg1_1x);
                iv_shouye_banner_zi.setImageResource(R.drawable.icon_qiye_1x);
            } else if (position == 1) {
                ll_shouye_banner.setBackgroundResource(R.drawable.bg2_1x);
                iv_shouye_banner_zi.setImageResource(R.drawable.icon_kechuang_1x);
            } else if (position == 2) {
                ll_shouye_banner.setBackgroundResource(R.drawable.bg3_1x);
                iv_shouye_banner_zi.setImageResource(R.drawable.icon_qukuailian_1x);
            }
            ll_shouye_banner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position == 0) {
                        Intent intent = new Intent(getActivity(), ShouyeActivity.class);
                        startActivity(intent);
                    } else if (position == 1) {
                        Intent intent = new Intent(getActivity(), ZixunActivity.class);
                        startActivity(intent);
                    } else if (position == 2) {
                        Hint("敬请期待", HintDialog.WARM);
                    }
                }
            });
            return view;
        }
    }

    private class MyAdapter_zhibo extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        ArrayList<HashMap<String, String>> arrlist;

        public MyAdapter_zhibo(Context context) {
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
                view = inflater.inflate(R.layout.shouye_zhibo_item, null);
            }
            TextView tv_shoye_zhibo_item_name = view.findViewById(R.id.tv_shoye_zhibo_item_name);
            TextView tv_shoye_zhibo_item_play_cont = view.findViewById(R.id.tv_shoye_zhibo_item_play_cont);
            ImageView iv_shoye_zhibo_item = view.findViewById(R.id.iv_shoye_zhibo_item);
            LinearLayout ll_shouye_zhibo = view.findViewById(R.id.ll_shouye_zhibo);

            Glide.with(getActivity())
                    .load( Api.sUrl+arrlist.get(position).get("video_img"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_shoye_zhibo_item);
            tv_shoye_zhibo_item_name.setText(arrlist.get(position).get("title"));
            tv_shoye_zhibo_item_play_cont.setText(arrlist.get(position).get("play_cont"));
            ll_shouye_zhibo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ZhiboActivity.class);
                    intent.putExtra("id", arrlist.get(position).get("id"));
                    startActivity(intent);

                }
            });
            return view;
        }
    }

    private class MyAdapterZixun extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;


        public MyAdapterZixun(Context context) {
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
                view = inflater.inflate(R.layout.shouye_zixun_item, null);
            }
            TextView tv_shouye_zixun_name = view.findViewById(R.id.tv_shouye_zixun_name);
            final LinearLayout ll_shouye_zixun_dz = view.findViewById(R.id.ll_shouye_zixun_dz);
            final ImageView iv_shouye_zixun_dz = view.findViewById(R.id.iv_shouye_zixun_dz);
            LinearLayout ll_shouye_zixun = view.findViewById(R.id.ll_shouye_zixun);
            final TextView tv_shouye_zixun_dianzhan = view.findViewById(R.id.tv_shouye_zixun_dianzhan);
            TextView tv_shouye_zixun_liulan = view.findViewById(R.id.tv_shouye_zixun_liulan);
            TextView tv_shouye_zixun_pinglun = view.findViewById(R.id.tv_shouye_zixun_pinglun);
            ImageView iv_shouye_zixun_item = view.findViewById(R.id.iv_shouye_zixun_item);
            TextView tv_shouye_zixun_time = view.findViewById(R.id.tv_shouye_zixun_time);
            ll_shouye_zixun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), GuanggaoActivity.class);
                    intent.putExtra("link", arrlist.get(position).get("url") + sUser_id);
                    startActivity(intent);
                }
            });

/*            map.put("id", ItemId);
            map.put("title", ItemTitle);
            map.put("detail", ItemDetail);
            map.put("time", ItemTime);
            map.put("img", ItemImg);
            map.put("pinglun_num", ItemPinglun_Num);
            map.put("zan", ItemZan);
            map.put("view", ItemView);
            map.put("url", ItemUrl);*/

            Glide.with(getActivity())
                    .load( Api.sUrl+arrlist.get(position).get("img"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_shouye_zixun_item);
            tv_shouye_zixun_name.setText(arrlist.get(position).get("title"));
            tv_shouye_zixun_time.setText(arrlist.get(position).get("time"));
            int a = (int) (1 + Math.random() * (1000 - 1 + 1));
            int b = (int) (1 + Math.random() * (1000 - 1 + 1));
            int c = (int) (1 + Math.random() * (1000 - 1 + 1));
            tv_shouye_zixun_dianzhan.setText(arrlist.get(position).get("zan"));
            tv_shouye_zixun_liulan.setText(arrlist.get(position).get("view"));
            tv_shouye_zixun_pinglun.setText(arrlist.get(position).get("pinglun_num"));

            ll_shouye_zixun_dz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (iv_shouye_zixun_dz.getDrawable().getCurrent().getConstantState().equals(getResources()
                            .getDrawable(R.drawable.public_btn_like_current_3x).getConstantState())) {

                        //当image1的src为R.drawable.A时，设置image1的src为R.drawable.B

                        iv_shouye_zixun_dz.setImageResource(R.drawable.public_btn_like_moren_3x);
                        tv_shouye_zixun_dianzhan.setText(String.valueOf(Integer.valueOf(tv_shouye_zixun_dianzhan.getText().toString()) - 1));
                    } else {
                        //否则设置image1的src为R.drawable.A
                        iv_shouye_zixun_dz.setImageResource(R.drawable.public_btn_like_current_3x);
                        tv_shouye_zixun_dianzhan.setText(String.valueOf(Integer.valueOf(tv_shouye_zixun_dianzhan.getText().toString()) + 1));
                    }

                }
            });
            return view;
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
}
