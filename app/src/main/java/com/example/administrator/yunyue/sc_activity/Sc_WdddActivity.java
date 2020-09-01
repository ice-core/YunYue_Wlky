package com.example.administrator.yunyue.sc_activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.data.JkyTzData;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.erci.activity.JikangyeActivity;
import com.example.administrator.yunyue.erci.activity.KyxqXqActivity;
import com.example.administrator.yunyue.erci.activity.YlxqDdxqActivity;
import com.example.administrator.yunyue.erci.activity.YlyDdxqActivity;
import com.example.administrator.yunyue.erci.activity.YlyxqActivity;
import com.example.administrator.yunyue.fragment.NewsFragmentPagerAdapter;
import com.example.administrator.yunyue.sc.CircleImageView;
import com.example.administrator.yunyue.sc_fragment.QuanbuFragment;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lemon.multi.MultiView;

public class Sc_WdddActivity extends AppCompatActivity implements QuanbuFragment.OnFragmentInteractionListener {
    private static final String TAG = Sc_WdddActivity.class.getSimpleName();
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    public static int iPosition = 0;
    private int iPosition_kyxq = 0;
    private String[] tab = {"全部", "待付款", "待发货", "待收货", "待评价"};
    private String[] tab_kyxq = {"我的服务", "我的需求"};
    /**
     * 附件商城atab
     */
    private GridView gv_fjsc_wddd_tab, gv_wddd_tab_kyxq;
    private MyAdapter myAdapter;
    /**
     * 返回
     */
    private LinearLayout ll_fjsc_wddd_back;
    private Spinner sp_wddd;
    private RecycleAdapterDome recycleAdapterDome;
    private SmartRefreshLayout srl_control;
    private int iPage = 1;
    private RecyclerView rv_wddd_lb;
    /**
     * 0--全国商城
     * 1--养老院
     * 2--康养需求
     */
    private int stype = 0;
    /**
     * 0--服务
     * 1--需求
     */
    private int fuwu_xuqiu = 0;
    // 默认为全部 1为待支付 2为可入住 4售后
    private String sRestHomeOrder_Type = "";
    //默认为全部 1为待接单 2为待服务 3为进行中 4为完成 5为取消需求
    private String sRecoveryNeedList_Type = "";

    private String sUser_id = "";
    private SharedPreferences pref;

    private Dialog dialog_qxyy;
    private View inflate_qxyy;
    private GridView gv_tkyy;
    private String sTkyy = "";
    private String sQxId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_sc_wddd);
        pref = PreferenceManager.getDefaultSharedPreferences(Sc_WdddActivity.this);
        sUser_id = pref.getString("user_id", "");
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        gv_fjsc_wddd_tab = findViewById(R.id.gv_fjsc_wddd_tab);
        gv_fjsc_wddd_tab.setNumColumns(tab.length);
        ll_fjsc_wddd_back = findViewById(R.id.ll_fjsc_wddd_back);
        ll_fjsc_wddd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sp_wddd = findViewById(R.id.sp_wddd);
        sp_wddd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cardNumber = Sc_WdddActivity.this.getResources().getStringArray(R.array.dd)[position];
                if (cardNumber.equals("商城")) {
                    stype = 0;
                    tab = new String[]{"全部", "待付款", "待发货", "待收货", "待评价"};
                    gv_wddd_tab_kyxq.setVisibility(View.GONE);
                    setGridView();
                    initFragment();
                    srl_control.setVisibility(View.GONE);
                    mViewPager.setVisibility(View.VISIBLE);
                } else if (cardNumber.equals("养老院")) {
                    stype = 1;
                    gv_wddd_tab_kyxq.setVisibility(View.GONE);
                    tab = new String[]{"全部", "可入住", "售后"};
                    setGridView();
                    mViewPager.setVisibility(View.GONE);
                    srl_control.setVisibility(View.VISIBLE);
                    iPage = 1;
                    sGetUrL();
                } else if (cardNumber.equals("康养需求")) {
                    stype = 2;
                    gv_wddd_tab_kyxq.setVisibility(View.VISIBLE);
                    tab = new String[]{"全部", "待完成", "进行中", "已完成"};
                    setGridView();
                    mViewPager.setVisibility(View.GONE);
                    srl_control.setVisibility(View.VISIBLE);
                    iPage = 1;
                    sGetUrL();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        gv_wddd_tab_kyxq = findViewById(R.id.gv_wddd_tab_kyxq);
        srl_control = findViewById(R.id.srl_control);
        rv_wddd_lb = findViewById(R.id.rv_wddd_lb);
        iPosition_kyxq = 0;
        setGridView();
        initFragment();
        setGridView_kyxq();
        smartRefresh();
        initialize();

    }

    //监听下拉和上拉状态
    public void smartRefresh() {
        //下拉刷新
        srl_control.setOnRefreshListener(refreshlayout -> {
            srl_control.setEnableRefresh(true);//启用刷新
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 Data() 来模拟我们刷新出来的数据
             */

            iPage = 1;
            sGetUrL();

        });
        //上拉加载
        srl_control.setOnLoadmoreListener(refreshlayout -> {
            srl_control.setEnableLoadmore(true);//启用加载
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 MoreDatas() 来模拟我们加载出来的数据
             */
            //  refreshAdapter.loadMore(MoreDatas());
            iPage += 1;
            sGetUrL();

        });
    }

    /**
     * 方法名：sRestHomeOrder()
     * 功  能：养老院房间订单列表接口
     * 参  数：appId
     * sRestHomeOrder()--老院房间订单列表接口
     * sRecoveryNeedList()--需求订单列表接口
     */
    private void sGetUrL() {
        hideDialogin();
        dialogin("");
        String url = Api.sUrl;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        if (stype == 1) {
            url = url + Api.sRestHomeOrder;
            if (!sRestHomeOrder_Type.equals("")) {
                params.put("type", sRestHomeOrder_Type);
            }
        } else if (stype == 2) {
            url = url + Api.sRecoveryNeedList;
            params.put("status", String.valueOf(iPosition_kyxq + 1));
            if (!sRecoveryNeedList_Type.equals("")) {
                params.put("type", sRecoveryNeedList_Type);
            }
        }
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);

        params.put("page", String.valueOf(iPage));
        JSONObject jsonObject = new JSONObject(params);
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (iPage == 1) {
                            srl_control.finishRefresh();//结束刷新
                        } else {
                            srl_control.finishLoadmore();//结束加载
                        }
                        hideDialogin();
                        String sDate = response.toString();
                        System.out.println(sDate);
                        try {
                            JSONObject jsonObject0 = new JSONObject(sDate);
                            String resultMsg = jsonObject0.getString("msg");
                            ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                            List<JkyTzData> mList = new ArrayList<>();
                            int resultCode = jsonObject0.getInt("code");
                            if (resultCode > 0) {
                                JSONArray jsonArray_data = jsonObject0.getJSONArray("data");
                                if (stype == 1) {
                                    for (int i = 0; i < jsonArray_data.length(); i++) {
                                        JSONObject jsonObject2 = (JSONObject) jsonArray_data.opt(i);
                                        //	订单id
                                        String ItemId = jsonObject2.getString("id");
                                        //房间id
                                        String ItemRoom_id = jsonObject2.getString("room_id");
                                        //养老院id
                                        String ItemRest_id = jsonObject2.getString("rest_id");
                                        //时间
                                        String ItemTime = jsonObject2.getString("time");
                                        //养老院名称
                                        String ItemName = jsonObject2.getString("name");
                                        //养老院名称
                                        String ItemTitle = jsonObject2.getString("title");
                                        //养老院名称
                                        String ItemLogo = jsonObject2.getString("logo");
                                        //	价格
                                        String ItemPrice = jsonObject2.getString("price");
                                        //	定金
                                        String ItemPeposit = jsonObject2.getString("deposit");
                                        //1为待支付 2为可入住 3为取消订单 4售后 5已退款
                                        String ItemStatus = jsonObject2.getString("status");
                                        //售后原因
                                        String ItemReasons = jsonObject2.getString("reasons");
                                        HashMap<String, String> map = new HashMap<String, String>();
                                        map.put("id", ItemId);
                                        map.put("room_id", ItemRoom_id);
                                        map.put("rest_id", ItemRest_id);
                                        map.put("time", ItemTime);
                                        map.put("name", ItemName);
                                        map.put("title", ItemTitle);
                                        map.put("logo", ItemLogo);
                                        map.put("price", ItemPrice);
                                        map.put("deposit", ItemPeposit);
                                        map.put("status", ItemStatus);
                                        map.put("reasons", ItemReasons);
                                        mylist.add(map);
                                    }
                                } else if (stype == 2) {
                                    for (int i = 0; i < jsonArray_data.length(); i++) {
                                        JSONObject jsonObject2 = (JSONObject) jsonArray_data.opt(i);
                                        //订单id
                                        String ItemId = jsonObject2.getString("id");
                                        //用户昵称
                                        String ItemNickname = jsonObject2.getString("nickname");
                                        //服务标题
                                        String ItemTitle = jsonObject2.getString("title");
                                        //服务地点
                                        String ItemService_place = jsonObject2.getString("service_place");
                                        //服务时间
                                        String ItemService_time = jsonObject2.getString("service_time");
                                        //预算工分
                                        String ItemBudget = jsonObject2.getString("budget");
                                        //发布时间
                                        String ItemCreate_time = jsonObject2.getString("create_time");
                                        //1为待接单 2为待服务 3为进行中 4为完成 5为取消需求
                                        String ItemStatus = jsonObject2.getString("status");
                                        HashMap<String, String> map = new HashMap<String, String>();
                                        map.put("id", ItemId);
                                        map.put("nickname", ItemNickname);
                                        map.put("title", ItemTitle);
                                        map.put("service_place", ItemService_place);
                                        map.put("service_time", ItemService_time);
                                        map.put("budget", ItemBudget);
                                        map.put("create_time", ItemCreate_time);
                                        map.put("status", ItemStatus);
                                        mylist.add(map);
                                    }

                                }
                                if (iPage == 1) {
                                    Rv_data(mylist, mList);
                                } else {
                                    if (mylist.size() == 0) {
                                        iPage -= 1;
                                    } else {
                                        Rv_data1(mylist, mList);
                                    }
                                }
                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
                                if (iPage == 1) {
                                    Rv_data(mylist, mList);
                                } else {
                                    if (mylist.size() == 0) {
                                        iPage -= 1;
                                    } else {
                                        Rv_data1(mylist, mList);
                                    }
                                }
                            }
                        } catch (
                                JSONException e) {
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
     * 热门帖子
     */
    private void Rv_data(ArrayList<HashMap<String, String>> mylist, List<JkyTzData> mList) {
        recycleAdapterDome = new RecycleAdapterDome(Sc_WdddActivity.this, mylist, mList);
        recycleAdapterDome.setHasStableIds(true);
        rv_wddd_lb.setAdapter(recycleAdapterDome);
        LinearLayoutManager manager = new LinearLayoutManager(Sc_WdddActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        //RecyclerView.LayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        //GridLayoutManager manager1 = new GridLayoutManager(context,2);
        //manager1.setOrientation(GridLayoutManager.VERTICAL);
        //StaggeredGridLayoutManager manager2 = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rv_wddd_lb.setLayoutManager(manager);
    }

    private void Rv_data1(ArrayList<HashMap<String, String>> mylist, List<JkyTzData> mList) {
        recycleAdapterDome.arrlist.addAll(mylist);
        recycleAdapterDome.mList.addAll(mList);
        recycleAdapterDome.notifyDataSetChanged();
    }

    public class RecycleAdapterDome extends RecyclerView.Adapter<RecycleAdapterDome.MyViewHolder> {
        private Context context;

        private View inflater;
        private ArrayList<HashMap<String, String>> arrlist;
        private List<JkyTzData> mList;

        //构造方法，传入数据
        public RecycleAdapterDome(Context context, ArrayList<HashMap<String, String>> arrlist, List<JkyTzData> mList) {
            this.context = context;
            this.arrlist = arrlist;
            this.mList = mList;
        }

        @Override
        public RecycleAdapterDome.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //创建ViewHolder，返回每一项的布局
            if (stype == 1) {
                inflater = LayoutInflater.from(context).inflate(R.layout.yly_ddlb_item, parent, false);
            } else if (stype == 2) {
                inflater = LayoutInflater.from(context).inflate(R.layout.kyxq_wdfw_ddlb_item, parent, false);
            }
            RecycleAdapterDome.MyViewHolder myViewHolder = new RecycleAdapterDome.MyViewHolder(inflater);
            return myViewHolder;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @SuppressLint("RecyclerView")
        @Override
        public void onBindViewHolder(RecycleAdapterDome.MyViewHolder holder, int position) {
            //将数据和控件绑定
            if (stype == 1) {
                holder.tv_yly_ddlb_item_name.setText(arrlist.get(position).get("name"));
                holder.tv_yly_ddlb_item_title.setText(arrlist.get(position).get("title"));
                holder.tv_yly_ddlb_item_time.setText(arrlist.get(position).get("time"));
                holder.tv_yly_ddlb_item_price.setText("￥" + arrlist.get(position).get("price"));
                holder.tv_yly_ddlb_item_deposit.setText(arrlist.get(position).get("deposit") + "工分");
                Glide.with(Sc_WdddActivity.this)
                        .load( Api.sUrl+arrlist.get(position).get("logo"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(holder.iv_yly_ddlb_item_logo);
                holder.tv_yly_ddlb_item_hj_deposit.setText("合计：" + arrlist.get(position).get("deposit") + "工分");
                String sStatus = "";
                if (arrlist.get(position).get("status").equals("2")) {
                    holder.tv_yly_ddlb_item_qxdd.setVisibility(View.VISIBLE);
                    sStatus = "可入住";
                } else if (arrlist.get(position).get("status").equals("3")) {
                    holder.tv_yly_ddlb_item_qxdd.setVisibility(View.GONE);
                    sStatus = "已取消";
                } else if (arrlist.get(position).get("status").equals("4")) {
                    holder.tv_yly_ddlb_item_qxdd.setVisibility(View.GONE);
                    sStatus = "售后中";
                } else if (arrlist.get(position).get("status").equals("5")) {
                    holder.tv_yly_ddlb_item_qxdd.setVisibility(View.GONE);
                    sStatus = "已退款";
                }
                holder.tv_yly_ddlb_item_status.setText(sStatus);
                holder.tv_yly_ddlb_item_ckxq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Sc_WdddActivity.this, YlyDdxqActivity.class);
                        intent.putExtra("id", arrlist.get(position).get("id"));
                        startActivity(intent);
                    }
                });
                holder.tv_yly_ddlb_item_qxdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sQxId = arrlist.get(position).get("id");
                        sRefund();
                    }
                });
                holder.ll_yly_ddlb_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Sc_WdddActivity.this, YlyDdxqActivity.class);
                        intent.putExtra("id", arrlist.get(position).get("id"));
                        startActivity(intent);
                    }
                });
            } else if (stype == 2) {

                String sStatus = "";

                if (iPosition_kyxq == 0) {
                    holder.tv_kyxq_wdfw_ddlb_item_qxfw.setText("取消服务");
                    holder.tv_kyxq_wdfw_ddlb_item_name.setText(arrlist.get(position).get("nickname"));
                    holder.tv_kyxq_wdfw_ddlb_item_ksfw.setVisibility(View.VISIBLE);
                    holder.tv_kyxq_wdfw_ddlb_item_qrwc.setVisibility(View.GONE);
                    if (arrlist.get(position).get("status").equals("2")) {
                        holder.tv_kyxq_wdfw_ddlb_item_ksfw.setVisibility(View.VISIBLE);
                        holder.tv_kyxq_wdfw_ddlb_item_qxfw.setVisibility(View.VISIBLE);
                        sStatus = "待服务";
                    } else if (arrlist.get(position).get("status").equals("3")) {
                        holder.tv_kyxq_wdfw_ddlb_item_ksfw.setVisibility(View.GONE);
                        holder.tv_kyxq_wdfw_ddlb_item_qxfw.setVisibility(View.GONE);
                        sStatus = "进行中";
                    } else if (arrlist.get(position).get("status").equals("4")) {
                        holder.tv_kyxq_wdfw_ddlb_item_ksfw.setVisibility(View.GONE);
                        holder.tv_kyxq_wdfw_ddlb_item_qxfw.setVisibility(View.GONE);
                        sStatus = "完成";
                    }
                } else if (iPosition_kyxq == 1) {
                    holder.tv_kyxq_wdfw_ddlb_item_qxfw.setText("取消需求");
                    holder.tv_kyxq_wdfw_ddlb_item_name.setText(arrlist.get(position).get("create_time"));
                    holder.tv_kyxq_wdfw_ddlb_item_ksfw.setVisibility(View.GONE);
                    holder.tv_kyxq_wdfw_ddlb_item_qrwc.setVisibility(View.VISIBLE);
                    if (arrlist.get(position).get("status").equals("1")) {
                        holder.tv_kyxq_wdfw_ddlb_item_ksfw.setVisibility(View.GONE);
                        holder.tv_kyxq_wdfw_ddlb_item_qrwc.setVisibility(View.GONE);
                        holder.tv_kyxq_wdfw_ddlb_item_qxfw.setVisibility(View.VISIBLE);
                        sStatus = "待接单";
                    } else if (arrlist.get(position).get("status").equals("2")) {
                        holder.tv_kyxq_wdfw_ddlb_item_ksfw.setVisibility(View.GONE);
                        holder.tv_kyxq_wdfw_ddlb_item_qrwc.setVisibility(View.GONE);
                        holder.tv_kyxq_wdfw_ddlb_item_qxfw.setVisibility(View.VISIBLE);
                        sStatus = "待服务";
                    } else if (arrlist.get(position).get("status").equals("3")) {
                        holder.tv_kyxq_wdfw_ddlb_item_ksfw.setVisibility(View.GONE);
                        holder.tv_kyxq_wdfw_ddlb_item_qrwc.setVisibility(View.VISIBLE);
                        holder.tv_kyxq_wdfw_ddlb_item_qxfw.setVisibility(View.GONE);
                        sStatus = "进行中";
                    } else if (arrlist.get(position).get("status").equals("4")) {
                        holder.tv_kyxq_wdfw_ddlb_item_ksfw.setVisibility(View.GONE);
                        holder.tv_kyxq_wdfw_ddlb_item_qrwc.setVisibility(View.GONE);
                        holder.tv_kyxq_wdfw_ddlb_item_qxfw.setVisibility(View.GONE);
                        sStatus = "完成";
                    } else if (arrlist.get(position).get("status").equals("4")) {
                        sStatus = "已取消";
                        holder.tv_kyxq_wdfw_ddlb_item_ksfw.setVisibility(View.GONE);
                        holder.tv_kyxq_wdfw_ddlb_item_qrwc.setVisibility(View.GONE);
                        holder.tv_kyxq_wdfw_ddlb_item_qxfw.setVisibility(View.GONE);
                    }
                }
                holder.tv_kyxq_wdfw_ddlb_item_title.setText(arrlist.get(position).get("title"));
                holder.tv_kyxq_wdfw_ddlb_item_service_place.setText("服务地点:" + arrlist.get(position).get("service_place"));
                holder.tv_kyxq_wdfw_ddlb_item_service_time.setText("服务时间:" + arrlist.get(position).get("service_time"));
                holder.tv_kyxq_wdfw_ddlb_item_budget.setText(arrlist.get(position).get("budget"));
                holder.tv_kyxq_wdfw_ddlb_item_budget_hj.setText("合计：" + arrlist.get(position).get("budget") + "工分");

                holder.tv_kyxq_wdfw_ddlb_item_status.setText(sStatus);
                holder.tv_kyxq_wdfw_ddlb_item_ksfw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sStartService(arrlist.get(position).get("id"));
                    }
                });
                holder.tv_kyxq_wdfw_ddlb_item_qrwc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sConfirmCompleted(arrlist.get(position).get("id"));
                    }
                });
                holder.tv_kyxq_wdfw_ddlb_item_qxfw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sQxId = arrlist.get(position).get("id");
                        sRefund();
                    }
                });
                holder.ll_kyxq_wdfw_ddlb_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Sc_WdddActivity.this, YlxqDdxqActivity.class);
                        intent.putExtra("id", arrlist.get(position).get("id"));
                        intent.putExtra("status", String.valueOf(iPosition_kyxq));
                        startActivity(intent);
                    }
                });

            }
        }

        @Override
        public int getItemCount() {
            //返回Item总条数
            return arrlist.size();
        }

        //内部类，绑定控件
        class MyViewHolder extends RecyclerView.ViewHolder {
            LinearLayout ll_yly_ddlb_item;
            TextView tv_yly_ddlb_item_name;
            ImageView iv_yly_ddlb_item_logo;
            TextView tv_yly_ddlb_item_title;
            TextView tv_yly_ddlb_item_time;
            TextView tv_yly_ddlb_item_price;
            TextView tv_yly_ddlb_item_deposit;
            TextView tv_yly_ddlb_item_hj_deposit;
            TextView tv_yly_ddlb_item_status;
            TextView tv_yly_ddlb_item_qxdd;
            TextView tv_yly_ddlb_item_ckxq;


            LinearLayout ll_kyxq_wdfw_ddlb_item;
            TextView tv_kyxq_wdfw_ddlb_item_name;
            TextView tv_kyxq_wdfw_ddlb_item_status;
            TextView tv_kyxq_wdfw_ddlb_item_title;
            TextView tv_kyxq_wdfw_ddlb_item_service_place;
            TextView tv_kyxq_wdfw_ddlb_item_service_time;
            TextView tv_kyxq_wdfw_ddlb_item_budget;
            TextView tv_kyxq_wdfw_ddlb_item_budget_hj;
            TextView tv_kyxq_wdfw_ddlb_item_ksfw;
            TextView tv_kyxq_wdfw_ddlb_item_ckxq;
            TextView tv_kyxq_wdfw_ddlb_item_qrwc;
            TextView tv_kyxq_wdfw_ddlb_item_qxfw;


            public MyViewHolder(View itemView) {
                super(itemView);
                if (stype == 1) {
                    ll_yly_ddlb_item = itemView.findViewById(R.id.ll_yly_ddlb_item);
                    tv_yly_ddlb_item_name = itemView.findViewById(R.id.tv_yly_ddlb_item_name);
                    iv_yly_ddlb_item_logo = itemView.findViewById(R.id.iv_yly_ddlb_item_logo);
                    tv_yly_ddlb_item_title = itemView.findViewById(R.id.tv_yly_ddlb_item_title);
                    tv_yly_ddlb_item_time = itemView.findViewById(R.id.tv_yly_ddlb_item_time);
                    tv_yly_ddlb_item_price = itemView.findViewById(R.id.tv_yly_ddlb_item_price);
                    tv_yly_ddlb_item_deposit = itemView.findViewById(R.id.tv_yly_ddlb_item_deposit);
                    tv_yly_ddlb_item_hj_deposit = itemView.findViewById(R.id.tv_yly_ddlb_item_hj_deposit);
                    tv_yly_ddlb_item_status = itemView.findViewById(R.id.tv_yly_ddlb_item_status);
                    tv_yly_ddlb_item_qxdd = itemView.findViewById(R.id.tv_yly_ddlb_item_qxdd);
                    tv_yly_ddlb_item_ckxq = itemView.findViewById(R.id.tv_yly_ddlb_item_ckxq);
                } else if (stype == 2) {
                    ll_kyxq_wdfw_ddlb_item = itemView.findViewById(R.id.ll_kyxq_wdfw_ddlb_item);
                    tv_kyxq_wdfw_ddlb_item_name = itemView.findViewById(R.id.tv_kyxq_wdfw_ddlb_item_name);
                    tv_kyxq_wdfw_ddlb_item_status = itemView.findViewById(R.id.tv_kyxq_wdfw_ddlb_item_status);
                    tv_kyxq_wdfw_ddlb_item_title = itemView.findViewById(R.id.tv_kyxq_wdfw_ddlb_item_title);
                    tv_kyxq_wdfw_ddlb_item_service_place = itemView.findViewById(R.id.tv_kyxq_wdfw_ddlb_item_service_place);
                    tv_kyxq_wdfw_ddlb_item_service_time = itemView.findViewById(R.id.tv_kyxq_wdfw_ddlb_item_service_time);
                    tv_kyxq_wdfw_ddlb_item_budget = itemView.findViewById(R.id.tv_kyxq_wdfw_ddlb_item_budget);
                    tv_kyxq_wdfw_ddlb_item_budget_hj = itemView.findViewById(R.id.tv_kyxq_wdfw_ddlb_item_budget_hj);
                    tv_kyxq_wdfw_ddlb_item_ksfw = itemView.findViewById(R.id.tv_kyxq_wdfw_ddlb_item_ksfw);
                    tv_kyxq_wdfw_ddlb_item_ckxq = itemView.findViewById(R.id.tv_kyxq_wdfw_ddlb_item_ckxq);
                    tv_kyxq_wdfw_ddlb_item_qrwc = itemView.findViewById(R.id.tv_kyxq_wdfw_ddlb_item_qrwc);
                    tv_kyxq_wdfw_ddlb_item_qxfw = itemView.findViewById(R.id.tv_kyxq_wdfw_ddlb_item_qxfw);
                }
            }
        }
    }


    /**
     * 方法名：sStartService()
     * 功  能：康养需求 开始服务
     * 参  数：appId
     */
    private void sStartService(String sOrder_Id) {
        hideDialogin();
        dialogin("");
        String url = Api.sUrl + Api.sStartService;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("order_id", sOrder_Id);
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
                                String resultData = jsonObject1.getString("data");
                                Hint(resultMsg, HintDialog.SUCCESS);
                                iPage = 1;
                                sGetUrL();
                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "response -> " + response.toString());
                        // Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialogin();
                //Hint(error.getMessage(), HintDialog.ERROR);
                error(error);

            }
        });
        requestQueue.add(jsonRequest);
    }

    /**
     * 方法名：sConfirmCompleted()
     * 功  能：康养需求 确认完成
     * 参  数：appId
     */
    private void sConfirmCompleted(String sOrder_Id) {
        hideDialogin();
        dialogin("");
        String url = Api.sUrl + Api.sConfirmCompleted;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("order_id", sOrder_Id);
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
                                String resultData = jsonObject1.getString("data");
                                Hint(resultMsg, HintDialog.SUCCESS);
                                iPage = 1;
                                sGetUrL();
                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "response -> " + response.toString());
                        // Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialogin();
                //Hint(error.getMessage(), HintDialog.ERROR);
                error(error);

            }
        });
        requestQueue.add(jsonRequest);
    }

    /**
     * 方法名：sRefund()
     * 功  能：退款原因
     * 参  数：appId
     */
    private void sRefund() {
        hideDialogin();
        dialogin("");
        String url = Api.sUrl + Api.sRefund;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
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
                                JSONArray resultJsonArraytype = jsonObject1.getJSONArray("data");
                                MyAdapterTkyy myAdapterTkyy = new MyAdapterTkyy(Sc_WdddActivity.this);
                                ArrayList<HashMap<String, String>> mylist_type = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < resultJsonArraytype.length(); i++) {
                                    jsonObject1 = resultJsonArraytype.getJSONObject(i);
                                    String resultId = jsonObject1.getString("id");
                                    String resultName = jsonObject1.getString("name");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("ItemId", resultId);
                                    map.put("ItemName", resultName);
                                    myAdapterTkyy.arr_Cb.add("0");
                                    mylist_type.add(map);
                                }
                                myAdapterTkyy.arrlist = mylist_type;
                                gv_tkyy.setAdapter(myAdapterTkyy);
                                show();
                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "response -> " + response.toString());
                        // Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialogin();
                //Hint(error.getMessage(), HintDialog.ERROR);
                error(error);

            }
        });
        requestQueue.add(jsonRequest);
    }


    /**
     * 方法名：sAddRefund()
     * 功  能：养老院订单 申请退款
     * 参  数：appId
     */
    private void sAddRefund(String sOrder_Id, String sReasons) {
        hideDialogin();
        dialogin("");
        String url = Api.sUrl + Api.sAddRefund;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("order_id", sOrder_Id);
        params.put("reasons", sReasons);
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
                                String resultData = jsonObject1.getString("data");
                                Hint(resultMsg, HintDialog.SUCCESS);
                                iPage = 1;
                                sGetUrL();
                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "response -> " + response.toString());
                        // Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialogin();
                //Hint(error.getMessage(), HintDialog.ERROR);
                error(error);

            }
        });
        requestQueue.add(jsonRequest);
    }

    public void show() {
        dialog_qxyy.setContentView(inflate_qxyy);
        Window dialogWindow = dialog_qxyy.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog_qxyy.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog_qxyy.getWindow().setAttributes(lp);
        dialog_qxyy.show();
    }

    private void initialize() {
        dialog_qxyy = new Dialog(Sc_WdddActivity.this, R.style.ActionSheetDialogStyle);
        inflate_qxyy = LayoutInflater.from(Sc_WdddActivity.this).inflate(R.layout.tkyy_dialog, null);
        TextView tv_tkyy_gb = (TextView) inflate_qxyy.findViewById(R.id.tv_tkyy_gb);
        gv_tkyy = inflate_qxyy.findViewById(R.id.gv_tkyy);
        tv_tkyy_gb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_qxyy.dismiss();
            }
        });
    }


    private class MyAdapterTkyy extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;
        public ArrayList<String> arr_Cb;

        public MyAdapterTkyy(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arrlist = new ArrayList<HashMap<String, String>>();
            arr_Cb = new ArrayList<String>();

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
                view = inflater.inflate(R.layout.tkyy_dialog_item, null);
            }
            final TextView tv_tkyy_dialog_item_name = view.findViewById(R.id.tv_tkyy_dialog_item_name);
            final CheckBox cb_tkyy_dialog_item = view.findViewById(R.id.cb_tkyy_dialog_item);
            cb_tkyy_dialog_item.setVisibility(View.GONE);
            tv_tkyy_dialog_item_name.setText(arrlist.get(position).get("ItemName"));
            tv_tkyy_dialog_item_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sTkyy = arrlist.get(position).get("ItemId");
                    dialog_qxyy.dismiss();
                    sAddRefund(sQxId, sTkyy);

                }
            });

            return view;
        }
    }


    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(Sc_WdddActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(Sc_WdddActivity.this, R.style.dialog, sHint, type, true).show();
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


    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        setGridView();
        initFragment();
    }

    private void setGridView() {
        myAdapter = new MyAdapter(Sc_WdddActivity.this);
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < tab.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemName", tab[i]);
            map.put("ItemId", String.valueOf(i));
            mylist.add(map);
        }
        gv_fjsc_wddd_tab.setNumColumns(tab.length);
        myAdapter.arrlist = mylist;
        gv_fjsc_wddd_tab.setAdapter(myAdapter);
        /*sv_shouye.smoothScrollTo(0, 20);
        sv_shouye.setFocusable(true);*/
    }

    private void setGridView_kyxq() {
        MyAdapter_kyxq myAdapter_kyxq = new MyAdapter_kyxq(Sc_WdddActivity.this);
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < tab_kyxq.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemName", tab_kyxq[i]);
            map.put("ItemId", String.valueOf(i));
            mylist.add(map);
        }
        myAdapter_kyxq.arrlist = mylist;
        gv_wddd_tab_kyxq.setAdapter(myAdapter_kyxq);
        /*sv_shouye.smoothScrollTo(0, 20);
        sv_shouye.setFocusable(true);*/
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
                view = inflater.inflate(R.layout.zx_fl_item, null);
            }
            LinearLayout ll_fl_item = view.findViewById(R.id.ll_fl_item);
            TextView zx_fl_item_name = view.findViewById(R.id.zx_fl_item_name);
            TextView zx_fl_item = view.findViewById(R.id.zx_fl_item);
            zx_fl_item_name.setText(arrlist.get(position).get("ItemName"));
            if (iPosition == Integer.valueOf(arrlist.get(position).get("ItemId"))) {
                zx_fl_item_name.setTextColor(zx_fl_item_name.getResources().getColor(R.color.theme));
                zx_fl_item.setBackgroundColor(ContextCompat.getColor(Sc_WdddActivity.this, R.color.theme));
            } else {
                zx_fl_item_name.setTextColor(zx_fl_item_name.getResources().getColor(R.color.hui999999));
                zx_fl_item.setBackgroundColor(ContextCompat.getColor(Sc_WdddActivity.this, R.color.touming));
            }
            ll_fl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    iPosition = position;
                    notifyDataSetChanged();
                    if (stype == 0) {
                        mViewPager.setCurrentItem(position);
                    } else if (stype == 1) {
                        iPage = 1;
                        if (position == 0) {
                            sRestHomeOrder_Type = "";
                        } else if (position == 1) {
                            sRestHomeOrder_Type = "2";
                        } else if (position == 2) {
                            sRestHomeOrder_Type = "4";
                        }
                        sGetUrL();
                    } else if (stype == 2) {
                        iPage = 1;
                        if (iPosition_kyxq == 0) {
                            if (position == 0) {
                                sRecoveryNeedList_Type = "";
                            } else if (position == 1) {
                                sRecoveryNeedList_Type = "2";
                            } else if (position == 2) {
                                sRecoveryNeedList_Type = "3";
                            } else if (position == 3) {
                                sRecoveryNeedList_Type = "4";
                            }
                        } else if (iPosition_kyxq == 1) {
                            if (position == 0) {
                                sRecoveryNeedList_Type = "";
                            } else if (position == 1) {
                                sRecoveryNeedList_Type = "1";
                            } else if (position == 2) {
                                sRecoveryNeedList_Type = "2";
                            } else if (position == 3) {
                                sRecoveryNeedList_Type = "3";
                            } else if (position == 4) {
                                sRecoveryNeedList_Type = "4";
                            } else if (position == 5) {
                                sRecoveryNeedList_Type = "5";
                            }
                        }
                        sGetUrL();
                    }
                }
            });

            return view;
        }
    }

    private class MyAdapter_kyxq extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;

        public MyAdapter_kyxq(Context context) {
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
                view = inflater.inflate(R.layout.zx_fl_item, null);
            }
            LinearLayout ll_fl_item = view.findViewById(R.id.ll_fl_item);
            TextView zx_fl_item_name = view.findViewById(R.id.zx_fl_item_name);
            TextView zx_fl_item = view.findViewById(R.id.zx_fl_item);
            zx_fl_item_name.setText(arrlist.get(position).get("ItemName"));
            if (iPosition_kyxq == Integer.valueOf(arrlist.get(position).get("ItemId"))) {
                zx_fl_item_name.setTextColor(zx_fl_item_name.getResources().getColor(R.color.theme));
                zx_fl_item.setBackgroundColor(ContextCompat.getColor(Sc_WdddActivity.this, R.color.theme));
            } else {
                zx_fl_item_name.setTextColor(zx_fl_item_name.getResources().getColor(R.color.hui999999));
                zx_fl_item.setBackgroundColor(ContextCompat.getColor(Sc_WdddActivity.this, R.color.touming));
            }
            ll_fl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iPosition_kyxq = position;
                    iPosition = 0;
                    notifyDataSetChanged();
                    if (position == 0) {
                        tab = new String[]{"全部", "待完成", "进行中", "已完成"};
                        fuwu_xuqiu = 0;
                    } else if (position == 1) {
                        tab = new String[]{"全部", "待接单", "待服务", "进行中", "已完成", "已取消"};
                        fuwu_xuqiu = 1;
                    }
                    setGridView();
                    iPage = 1;
                    sGetUrL();
                }
            });

            return view;
        }
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        fragments.clear();//清空
        int count = tab.length;
        for (int i = 0; i < count; i++) {
            //   NewsFragment newfragment = new NewsFragment();
            QuanbuFragment quanbuFragment = new QuanbuFragment();
            fragments.add(quanbuFragment);
        }
        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapetr);
        mViewPager.addOnPageChangeListener(pageListener);
        mViewPager.setCurrentItem(iPosition);
    }


    /**
     * ViewPager切换监听方法
     */
    public ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            mViewPager.setCurrentItem(position);
            selectTab(position);
        }
    };

    /**
     * 选择的Column里面的Tab
     */
    private void selectTab(int tab_postion) {

        iPosition = tab_postion;
        myAdapter.notifyDataSetChanged();

    }

}
