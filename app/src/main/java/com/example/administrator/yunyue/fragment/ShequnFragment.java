package com.example.administrator.yunyue.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.example.administrator.yunyue.Im.HaoyouActivity;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.erci.activity.HudongActivity;
import com.example.administrator.yunyue.sc.ObservableScrollView;
import com.example.administrator.yunyue.sq_activity.DakaActivity;
import com.example.administrator.yunyue.sq_activity.GrtzActivity;
import com.example.administrator.yunyue.sq_activity.GuanzhuActivity;
import com.example.administrator.yunyue.sq_activity.ShequActivity;
import com.example.administrator.yunyue.sq_activity.ShequnActivity;
import com.example.administrator.yunyue.sq_activity.ShequnXqActivity;
import com.example.administrator.yunyue.sq_activity.SqjjGzActivity;
import com.example.administrator.yunyue.utils.NullTranslator;
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

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShequnFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShequnFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShequnFragment extends Fragment {
    private static final String TAG = ShequnFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GridView gv_shequn_tuijian;
    private ObservableScrollView sv_shequn;
    private ListView lv_shequ_yuijian;
    private LinearLayout ll_shequn_gdsq;
    private LinearLayout ll_shequn_gdsq1;
    private GridView gv_shequ_banner;
    private GridView gv_shequn_hyzl;
    private LinearLayout ll_shequn_shequ;
    private LinearLayout ll_shequn_shequn;
    private LinearLayout ll_shequn_guanzhu;
    private LinearLayout ll_shequn_haoyou;
    private LinearLayout ll_shequn_dk;
    private OnFragmentInteractionListener mListener;

    private TextView tv_shequn_gdshequn;
    private String[] tuijian = {"美不美看后背，三款国产轿跑SUV对比", "国六/9AT/新CVT很亮眼 别克昂科拉GX/新款昂科拉上海车展首秀",
            "smart驭风蓝特别版上市 售15.2888万元", "一键敞篷去“撒野” 牧马人电动敞篷版售47.99万元", "抢在2019上海车展前上市，这些SUV新车很有看头，该出手了",
            "最强10万元轿车来了，标配LED大灯和独悬，油耗仅5.8L", "3500转就能秒了你，它是隐藏在车流中的“超跑”？",
            "不花钱开新车 我“忽悠”女友老爸买了辆迈锐宝XL", "【两万公里风尘】终极艳红诱惑的毒药 T90原创手机写真",
            "满足你一切都想要“大”的需求 实拍全新奔驰GLE 350 4MATIC", "省油省心家用一级棒！17万起这几款大牌SUV靠得住！",
            "德日两大厂商的旗舰级对决 丰田亚洲龙对比大众辉昂", "4月新车大爆发！想买车、换车的你认准这15款准没错！",
            "编辑探店实拍2020款本田杰德 上市近6年还值得一买吗", "一键敞篷去“撒野” 牧马人电动敞篷版售47.99万元"};
    private String[] hyzl_name = {"污秋", "击穿者", "污秋", "污秋"};
    private String[] hyzl = {"企业家联合汇", "科创板", "区块链", "企业家联合汇"};
    private String[] tu = {"http://ecar.bonnidee.cn/images/detail1/41.jpg", "http://ecar.bonnidee.cn/images/detail1/3.jpg",
            "http://ecar.bonnidee.cn/images/detail1/7.jpg", "http://ecar.bonnidee.cn/images/detail1/9.jpg",
            "http://ecar.bonnidee.cn/images/detail1/12.jpg", "http://ecar.bonnidee.cn/images/detail1/13.jpg",
            "http://ecar.bonnidee.cn/images/detail1/16.jpg", "http://ecar.bonnidee.cn/images/detail1/18.jpg",
            "http://ecar.bonnidee.cn/images/detail1/21.jpg", "http://ecar.bonnidee.cn/images/detail1/24.jpg",
            "http://ecar.bonnidee.cn/images/detail1/27.jpg", "http://ecar.bonnidee.cn/images/detail1/30.jpg",
            "http://ecar.bonnidee.cn/images/detail1/33.jpg", "http://ecar.bonnidee.cn/images/detail1/35.jpg",
            "http://ecar.bonnidee.cn/images/detail1/38.jpg"};

    RequestQueue queue = null;
    private SharedPreferences pref;
    private String sUser_id;
    /**
     * 扫描
     */
    private LinearLayout ll_shequn_saomiao;
    private int REQUEST_CODE_SCAN = 111;
    private LinearLayout ll_shequn_query;
    @BindView(R.id.srl_control_shequn)
    SmartRefreshLayout srlControlShequn;

    public ShequnFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShequnFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShequnFragment newInstance(String param1, String param2) {
        ShequnFragment fragment = new ShequnFragment();
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
        return inflater.inflate(R.layout.fragment_shequn, container, false);
    }

    public void onViewCreated(View view, Bundle saveInstancesState) {
        super.onViewCreated(view, saveInstancesState);
        queue = Volley.newRequestQueue(getActivity());
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sUser_id = pref.getString("user_id", "");
        gv_shequn_tuijian = view.findViewById(R.id.gv_shequn_tuijian);
        srlControlShequn = view.findViewById(R.id.srl_control_shequn);
        sv_shequn = view.findViewById(R.id.sv_shequn);
        lv_shequ_yuijian = view.findViewById(R.id.lv_shequ_yuijian);
        ll_shequn_gdsq = view.findViewById(R.id.ll_shequn_gdsq);
        ll_shequn_gdsq1 = view.findViewById(R.id.ll_shequn_gdsq1);
        gv_shequ_banner = view.findViewById(R.id.gv_shequ_banner);
        gv_shequn_hyzl = view.findViewById(R.id.gv_shequn_hyzl);
        ll_shequn_shequ = view.findViewById(R.id.ll_shequn_shequ);
        ll_shequn_shequn = view.findViewById(R.id.ll_shequn_shequn);
        tv_shequn_gdshequn = view.findViewById(R.id.tv_shequn_gdshequn);
        ll_shequn_guanzhu = view.findViewById(R.id.ll_shequn_guanzhu);
        ll_shequn_haoyou = view.findViewById(R.id.ll_shequn_haoyou);
        ll_shequn_dk = view.findViewById(R.id.ll_shequn_dk);
        lv_shequ_yuijian.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gv_shequ_banner.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gv_shequn_hyzl.setSelector(new ColorDrawable(Color.TRANSPARENT));
        ll_shequn_saomiao = view.findViewById(R.id.ll_shequn_saomiao);
        ll_shequn_query = view.findViewById(R.id.ll_shequn_query);
        ll_shequn_saomiao.setOnClickListener(new View.OnClickListener() {
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
                                Uri packageURI = Uri.parse("package:" + getActivity().getPackageName());
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                                Toast.makeText(getActivity(), "没有权限无法扫描呦", Toast.LENGTH_LONG).show();
                            }
                        }).start();
            }
        });

        // setGridView_hyzl();
        ll_shequn_gdsq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ShequnActivity.class);
                startActivity(intent);
            }
        });
        ll_shequn_gdsq1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ShequnActivity.class);
                startActivity(intent);
            }
        });
        ll_shequn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ShequnActivity.class);
                startActivity(intent);
            }
        });

        ll_shequn_haoyou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HaoyouActivity.class);
                startActivity(intent);
            }
        });
        ll_shequn_shequn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ShequnXqActivity.class);
                startActivity(intent);
            }
        });
        tv_shequn_gdshequn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ShequnActivity.class);
                startActivity(intent);
            }
        });
        ll_shequn_dk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DakaActivity.class);
                startActivity(intent);
            }
        });
        ll_shequn_guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HudongActivity.class);
                startActivity(intent);
            }
        });
        ll_shequn_shequ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ShequActivity.class);
                startActivity(intent);
            }
        });
        query();
        smartRefresh();
    }

    //监听下拉和上拉状态
    public void smartRefresh() {
        //下拉刷新
        srlControlShequn.setOnRefreshListener(refreshlayout -> {
            srlControlShequn.setEnableRefresh(true);//启用刷新
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 Data() 来模拟我们刷新出来的数据
             */
            query();
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
                // result.setText("扫描结果为：" + content);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(content);
                    int resultId = jsonObject.getInt("id");
                    int resultType = jsonObject.getInt("type");
                    //type=1为社群type=2为群聊
                    if (resultType == 1) {
                        Intent intent = new Intent(getActivity(), SqjjGzActivity.class);
                        intent.putExtra("id", String.valueOf(resultId));
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), SqjjGzActivity.class);
                        intent.putExtra("id", String.valueOf(resultId));
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    /**
     * 社群首页信息获取
     */
    private void query() {
        String url = Api.sUrl + "Community/Api/index/appId/" + Api.sApp_Id + "/user_id/" + sUser_id;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                srlControlShequn.finishRefresh();//结束刷新
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        JSONObject jsonObjectdata = new JSONObject(sDate.toString()).getJSONObject("data");
                        JSONArray jsonArraybanner = jsonObjectdata.getJSONArray("banner");
                        ArrayList<HashMap<String, String>> mylist_banner = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < jsonArraybanner.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArraybanner.opt(i);
                            String ItemId = jsonObject2.getString("id");
                            String ItemPic_Url = jsonObject2.getString("pic_url");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", ItemId);
                            map.put("pic_url", ItemPic_Url);
                            mylist_banner.add(map);
                        }
                        setGridView_banner(mylist_banner);

                        JSONArray jsonArraycommunity = jsonObjectdata.getJSONArray("community");
                        ArrayList<HashMap<String, String>> mylist_community = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < jsonArraycommunity.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArraycommunity.opt(i);
                            String ItemCommunity_Id = jsonObject2.getString("community_id");
                            String ItemName = jsonObject2.getString("name");
                            String ItemLogo = jsonObject2.getString("logo");
                            String ItemGroup = jsonObject2.getString("group");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("community_id", ItemCommunity_Id);
                            map.put("name", ItemName);
                            map.put("logo", ItemLogo);
                            map.put("group", ItemGroup);
                            mylist_community.add(map);
                        }
                        setGridView(mylist_community);
                        JSONArray jsonArraydynamic = jsonObjectdata.getJSONArray("dynamic");
                        ArrayList<HashMap<String, String>> mylist_dynamic = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < jsonArraydynamic.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArraydynamic.opt(i);
                            String ItemUser_Id = jsonObject2.getString("user_id");
                            String ItemCommunity_Id = jsonObject2.getString("community_id");
                            String ItemName = jsonObject2.getString("name");
                            String ItemDynamic_Id = jsonObject2.getString("dynamic_id");
                            String ItemLogo = jsonObject2.getString("logo");
                            String ItemTitle = jsonObject2.getString("title");
                            String ItemContent = jsonObject2.getString("content");
                            String ItemTime = jsonObject2.getString("time");
                            String ItemImg1 = jsonObject2.getString("img1");
                            String ItemImg2 = jsonObject2.getString("img2");
                            String ItemImg3 = jsonObject2.getString("img3");
                            String ItemZan = jsonObject2.getString("zan");
                            String ItemComment = jsonObject2.getString("comment");
                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put("user_id", ItemUser_Id);
                            map.put("community_id", ItemCommunity_Id);
                            map.put("name", ItemName);
                            map.put("dynamic_id", ItemDynamic_Id);
                            map.put("logo", ItemLogo);
                            map.put("title", ItemTitle);
                            map.put("content", ItemContent);
                            map.put("time", ItemTime);
                            map.put("img1", ItemImg1);
                            map.put("img2", ItemImg2);
                            map.put("img3", ItemImg3);
                            map.put("zan", ItemZan);
                            map.put("comment", ItemComment);
                            map.put("guanzhu", "0");
                            mylist_dynamic.add(map);
                        }
                        gv_xiaoxi(mylist_dynamic);

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
        queue.add(request);
    }

    /**
     * 关注社群
     */
    private void jiaruShequn(String community_id) {
        String url = Api.sUrl + "Community/Api/jiaruShequn/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/community_id/" + community_id;
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
     * listview自适应高度
     */
    public void setListViewHeightBasedOnChildren(ListView listView1) {
        BaseAdapter listAdapter = (BaseAdapter) listView1.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        //获取listView的宽度
        ViewGroup.LayoutParams params = listView1.getLayoutParams();
        int listViewWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int widthSpec = View.MeasureSpec.makeMeasureSpec(listViewWidth, View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView1);
            //给item的measure设置参数是listView的宽度就可以获取到真正每一个item的高度
            listItem.measure(widthSpec, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        params.height = totalHeight + (listView1.getDividerHeight() * (listAdapter.getCount() + 1));
        listView1.setLayoutParams(params);
    }

    /**
     * 热门咨询
     */
    private void gv_xiaoxi(ArrayList<HashMap<String, String>> mylist) {
        MyAdapterShequ myAdapterShequ = new MyAdapterShequ(getActivity());
        myAdapterShequ.arrlist = mylist;
        lv_shequ_yuijian.setAdapter(myAdapterShequ);
        setListViewHeightBasedOnChildren(lv_shequ_yuijian);
    }


    /**
     * 社区banner
     * 设置GirdView参数，绑定数据
     */
    private void setGridView_banner(ArrayList<HashMap<String, String>> mylist) {
        MyAdapter_banner myAdapter_banner = new MyAdapter_banner(getActivity());
        myAdapter_banner.arrlist = mylist;
        int size = mylist.size();
        int length = 345;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gv_shequ_banner.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gv_shequ_banner.setColumnWidth(itemWidth); // 设置列表项宽
        gv_shequ_banner.setHorizontalSpacing(0); // 设置列表项水平间距
        gv_shequ_banner.setStretchMode(GridView.NO_STRETCH);
        gv_shequ_banner.setNumColumns(size); // 设置列数量=列表集合数

        gv_shequ_banner.setAdapter(myAdapter_banner);
        sv_shequn.smoothScrollTo(0, 20);
        sv_shequn.setFocusable(true);
    }


    /**
     * 社区好友在聊
     * 设置GirdView参数，绑定数据
     */
    private void setGridView_hyzl() {
        MyAdapter_hyzl myAdapter_hyzl = new MyAdapter_hyzl(getActivity());
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 3; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemName", hyzl_name[i]);
            map.put("Item", hyzl[i]);
            mylist.add(map);
        }
        myAdapter_hyzl.arrlist = mylist;
        int size = mylist.size();
        int length = 145;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gv_shequn_hyzl.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gv_shequn_hyzl.setColumnWidth(itemWidth); // 设置列表项宽
        gv_shequn_hyzl.setHorizontalSpacing(0); // 设置列表项水平间距
        gv_shequn_hyzl.setStretchMode(GridView.NO_STRETCH);
        gv_shequn_hyzl.setNumColumns(size); // 设置列数量=列表集合数

        gv_shequn_hyzl.setAdapter(myAdapter_hyzl);
        sv_shequn.smoothScrollTo(0, 20);
        sv_shequn.setFocusable(true);
    }

    /**
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
        gv_shequn_tuijian.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gv_shequn_tuijian.setColumnWidth(itemWidth); // 设置列表项宽
        gv_shequn_tuijian.setHorizontalSpacing(10); // 设置列表项水平间距
        gv_shequn_tuijian.setStretchMode(GridView.NO_STRETCH);
        gv_shequn_tuijian.setNumColumns(size); // 设置列数量=列表集合数
        gv_shequn_tuijian.setAdapter(myAdapter);
        sv_shequn.smoothScrollTo(0, 20);
        sv_shequn.setFocusable(true);

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
                view = inflater.inflate(R.layout.shequ_banner_item, null);
            }
            ImageView iv_shequn_banner = view.findViewById(R.id.iv_shequn_banner);
            Glide.with(getActivity())
                    .load( Api.sUrl+arrlist.get(position).get("pic_url"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_shequn_banner);
            return view;
        }
    }

    private class MyAdapter_hyzl extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;


        public MyAdapter_hyzl(Context context) {
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
                view = inflater.inflate(R.layout.shequn_hylt_item, null);
            }
            LinearLayout ll_shequn_hyzl_item = view.findViewById(R.id.ll_shequn_hyzl_item);
            TextView tv_shequn_hyzl_name = view.findViewById(R.id.tv_shequn_hyzl_name);
            TextView tv_shequn_hyzl = view.findViewById(R.id.tv_shequn_hyzl);
            ImageView iv_shequn_hyzl = view.findViewById(R.id.iv_shequn_hyzl);
            tv_shequn_hyzl_name.setText(arrlist.get(position).get("ItemName"));
            tv_shequn_hyzl.setText(arrlist.get(position).get("Item"));
            if (position == 0) {
                iv_shequn_hyzl.setImageResource(R.drawable.userpic1_1x);
            } else if (position == 1) {
                iv_shequn_hyzl.setImageResource(R.drawable.userpic2_1x);
            } else if (position == 2) {
                iv_shequn_hyzl.setImageResource(R.drawable.userpic3_1x);
            }


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
                view = inflater.inflate(R.layout.shequntuijian_item, null);
            }
            TextView tv_shequn_tuijian = view.findViewById(R.id.tv_shequn_tuijian);
            tv_shequn_tuijian.setText(arrlist.get(position).get("name"));
            LinearLayout ll_tuijian_item = view.findViewById(R.id.ll_tuijian_item);
            ImageView imageView = view.findViewById(R.id.iv_shequn_tuijian);
            Glide.with(getActivity())
                    .load( Api.sUrl+arrlist.get(position).get("logo"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(imageView);
            ll_tuijian_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ShequnXqActivity.class);
                    intent.putExtra("id", arrlist.get(position).get("community_id"));
                    startActivity(intent);
                }
            });
            return view;
        }
    }

    private class MyAdapterShequ extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;


        public MyAdapterShequ(Context context) {
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
                view = inflater.inflate(R.layout.shequtuijian_item, null);
            }
            ImageView iv_shequn_tuijian_item_name = view.findViewById(R.id.iv_shequn_tuijian_item_name);
            TextView tv_shequn_tuijian_item_name = view.findViewById(R.id.tv_shequn_tuijian_item_name);
            TextView tv_shequn_tuijian_item_time = view.findViewById(R.id.tv_shequn_tuijian_item_time);
            TextView tv_shequn_tuijian_name_title = view.findViewById(R.id.tv_shequn_tuijian_name_title);
            ImageView iv_shequn_dt_item = view.findViewById(R.id.iv_shequn_dt_item);
            LinearLayout ll_shequn_dt_item = view.findViewById(R.id.ll_shequn_dt_item);
            ImageView iv_shequn_dt_item_1 = view.findViewById(R.id.iv_shequn_tuijian_1);
            ImageView iv_shequn_dt_item_2 = view.findViewById(R.id.iv_shequn_tuijian_2);
            ImageView iv_shequn_dt_item_3 = view.findViewById(R.id.iv_shequn_tuijian_3);

            LinearLayout ll_shequn_tuijian_item = view.findViewById(R.id.ll_shequn_tuijian_item);
            final TextView tv_shequn_tuijian_name_guanzhu = view.findViewById(R.id.tv_shequn_tuijian_name_guanzhu);
            LinearLayout ll_shequn_shequ_item = view.findViewById(R.id.ll_shequn_shequ_item);
            final ImageView iv_shequn_shequ_item = view.findViewById(R.id.iv_shequn_shequ_item);
            TextView tv_shequn_tuijian_name = view.findViewById(R.id.tv_shequn_tuijian_name);
            final TextView tv_shequn_dianzhan = view.findViewById(R.id.tv_shequn_dianzhan);
            TextView tv_shequn_pinglun = view.findViewById(R.id.tv_shequn_pinglun);

            Glide.with(getActivity())
                    .load( Api.sUrl+arrlist.get(position).get("logo"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_shequn_tuijian_item_name);
            tv_shequn_tuijian_item_name.setText(arrlist.get(position).get("name"));
            tv_shequn_tuijian_item_time.setText(arrlist.get(position).get("time"));
            tv_shequn_tuijian_name_title.setText(arrlist.get(position).get("title"));
            tv_shequn_tuijian_name.setText(arrlist.get(position).get("content"));

            tv_shequn_dianzhan.setText(arrlist.get(position).get("zan"));
            tv_shequn_pinglun.setText(arrlist.get(position).get("comment"));


            iv_shequn_tuijian_item_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), GrtzActivity.class);
                    GrtzActivity.sFriend_id = arrlist.get(position).get("user_id");
                    ShequnXqActivity.community_id = arrlist.get(position).get("community_id");
                    startActivity(intent);
                }
            });
            if (arrlist.get(position).get("img2").equals("")) {
                ll_shequn_dt_item.setVisibility(View.GONE);
                iv_shequn_dt_item.setVisibility(View.VISIBLE);
                if (arrlist.get(position).get("img1").equals("")) {
                    iv_shequn_dt_item.setVisibility(View.GONE);
                } else {
                    iv_shequn_dt_item.setVisibility(View.VISIBLE);
                    Glide.with(getActivity())
                            .load( Api.sUrl+arrlist.get(position).get("img1"))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_shequn_dt_item);
                }
            } else {
                iv_shequn_dt_item.setVisibility(View.GONE);
                ll_shequn_dt_item.setVisibility(View.VISIBLE);
                if (arrlist.get(position).get("img1").equals("")) {
                } else {

                    Glide.with(getActivity())
                            .load( Api.sUrl+arrlist.get(position).get("img1"))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_shequn_dt_item_1);
                }
                if (arrlist.get(position).get("img2").equals("")) {
                } else {

                    Glide.with(getActivity())
                            .load( Api.sUrl+arrlist.get(position).get("img2"))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_shequn_dt_item_2);
                }
                if (arrlist.get(position).get("img3").equals("")) {
                } else {
                    Glide.with(getActivity())
                            .load( Api.sUrl+arrlist.get(position).get("img3"))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_shequn_dt_item_3);
                }
            }
            if (arrlist.get(position).get("guanzhu").equals("0")) {
                tv_shequn_tuijian_name_guanzhu.setText("关注");
                tv_shequn_tuijian_name_guanzhu.setBackgroundResource(R.drawable.bg_lan12);
                tv_shequn_tuijian_name_guanzhu.setTextColor(tv_shequn_tuijian_name_guanzhu.getResources().getColor(R.color.lan_4A90E2));
            } else {
                tv_shequn_tuijian_name_guanzhu.setText("取消关注");
                tv_shequn_tuijian_name_guanzhu.setBackgroundResource(R.drawable.bj_lan12);
                tv_shequn_tuijian_name_guanzhu.setTextColor(tv_shequn_tuijian_name_guanzhu.getResources().getColor(R.color.white_c));
            }

            ll_shequn_shequ_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (iv_shequn_shequ_item.getDrawable().getCurrent().getConstantState().equals(getResources()
                            .getDrawable(R.drawable.public_btn_like_current_3x).getConstantState())) {
                        //当image1的src为R.drawable.A时，设置image1的src为R.drawable.B
                        iv_shequn_shequ_item.setImageResource(R.drawable.public_btn_like_moren_3x);
                        tv_shequn_dianzhan.setText(String.valueOf(Integer.valueOf(tv_shequn_dianzhan.getText().toString()) - 1));
                    } else {
                        //否则设置image1的src为R.drawable.A
                        iv_shequn_shequ_item.setImageResource(R.drawable.public_btn_like_current_3x);
                        tv_shequn_dianzhan.setText(String.valueOf(Integer.valueOf(tv_shequn_dianzhan.getText().toString()) + 1));
                    }
                }
            });
            tv_shequn_tuijian_name_guanzhu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tv_shequn_tuijian_name_guanzhu.getText().equals("关注")) {
                        dialogin("");
                        jiaruShequn(arrlist.get(position).get("community_id"));
                        tv_shequn_tuijian_name_guanzhu.setText("取消关注");
                        tv_shequn_tuijian_name_guanzhu.setBackgroundResource(R.drawable.bj_lan12);
                        tv_shequn_tuijian_name_guanzhu.setTextColor(tv_shequn_tuijian_name_guanzhu.getResources().getColor(R.color.white_c));
                    } else {
                        tv_shequn_tuijian_name_guanzhu.setText("关注");
                        tv_shequn_tuijian_name_guanzhu.setBackgroundResource(R.drawable.bg_lan12);
                        tv_shequn_tuijian_name_guanzhu.setTextColor(tv_shequn_tuijian_name_guanzhu.getResources().getColor(R.color.lan_4A90E2));
                    }
                }
            });
            ll_shequn_tuijian_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ShequnXqActivity.class);
                    intent.putExtra("id", arrlist.get(position).get("community_id"));
                    startActivity(intent);
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
