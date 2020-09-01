package com.example.administrator.yunyue.erci.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.data.JkyTzData;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc.CircleImageView;
import com.example.administrator.yunyue.sc_activity.Sc_WdddActivity;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.sq_activity.DakaActivity;
import com.example.administrator.yunyue.tu_model.NineGridTestModel;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.alien95.resthttp.request.RestHttp;
import cn.lemon.multi.BuildConfig;
import cn.lemon.multi.MultiView;
import master.flame.danmaku.danmaku.model.FBDanmaku;

import static io.rong.imkit.utilities.RongUtils.getScreenWidth;

public class JikangyeActivity extends AppCompatActivity {
    private static final String TAG = JikangyeActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_jky_back;
    /**
     * 标题
     */
    private GridView gv_jky_hint;
    private RecyclerView rv_jky_lb;
    private String[] tab = {"养老院", "康养需求", "帖子"};
    private MyAdapter myAdapter;
    private int iPosition = 0;

    private RecycleAdapterDome recycleAdapterDome;
    private int iPage = 1;
    private String sUser_id = "";
    private String sIs_vip = "";
    private SharedPreferences pref;
    SmartRefreshLayout srlControl;
    /**
     * 发布
     */
    private ImageView iv_jikangyu_fabu;
    /**
     * 发帖子
     */
    private TextView tv_jikangyu_ftz;
    /**
     * 发需求
     */
    private TextView tv_jikangyu_fgy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_jikangye);
        RestHttp.initialize(this);
        if (BuildConfig.DEBUG) {
            RestHttp.setDebug(true, "network");
        }
        pref = PreferenceManager.getDefaultSharedPreferences(JikangyeActivity.this);
        sUser_id = pref.getString("user_id", "");
        sIs_vip = pref.getString("is_vip", "");
        srlControl = findViewById(R.id.srl_control);
        ll_jky_back = findViewById(R.id.ll_jky_back);
        ll_jky_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gv_jky_hint = findViewById(R.id.gv_jky_hint);
        rv_jky_lb = findViewById(R.id.rv_jky_lb);
        setGridView();
        smartRefresh();
        iv_jikangyu_fabu = findViewById(R.id.iv_jikangyu_fabu);
        tv_jikangyu_ftz = findViewById(R.id.tv_jikangyu_ftz);
        tv_jikangyu_fgy = findViewById(R.id.tv_jikangyu_fgy);
        Drawable drawable_jia = getResources().getDrawable(R.drawable.jky_fb);
        // ImageView
        iv_jikangyu_fabu.setBackground(drawable_jia);

        iv_jikangyu_fabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable.ConstantState buttonConstantState = iv_jikangyu_fabu.getBackground().getConstantState();
                Drawable.ConstantState resourceConstantState = getResources().getDrawable(
                        R.drawable.jky_fb).getConstantState();
                boolean isEqual = buttonConstantState.equals(resourceConstantState);
                if (isEqual) {
                    Drawable drawable_jian = getResources().getDrawable(R.drawable.fabu_jian);
                    // ImageView
                    iv_jikangyu_fabu.setBackground(drawable_jian);
                    //   iv_jikangyu_fabu.setImageResource(R.drawable.fabu_jian);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tv_jikangyu_ftz.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    tv_jikangyu_fgy.setVisibility(View.VISIBLE);
                                }
                            }, 50);
                        }
                    }, 50);

                } else {
                    gone();
                }
            }
        });

        tv_jikangyu_ftz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(JikangyeActivity.this, FabutieziActivity.class);
                    startActivity(intent);
                    gone();

            }
        });
        tv_jikangyu_fgy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(JikangyeActivity.this, FabukyxqActivity.class);
                    startActivity(intent);
                    gone();

            }
        });
    }

    private void gone() {
        Drawable drawable_jia = getResources().getDrawable(R.drawable.jky_fb);
        // ImageView
        iv_jikangyu_fabu.setBackground(drawable_jia);
        //  iv_jikangyu_fabu.setImageResource(R.drawable.jky_fb);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_jikangyu_fgy.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_jikangyu_ftz.setVisibility(View.GONE);
                    }
                }, 50);
            }
        }, 50);
    }

    private void setGridView() {
        myAdapter = new MyAdapter(JikangyeActivity.this);
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < tab.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemName", tab[i]);
            map.put("ItemId", String.valueOf(i));
            mylist.add(map);
        }
        myAdapter.arrlist = mylist;
        gv_jky_hint.setAdapter(myAdapter);
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
                zx_fl_item.setBackgroundColor(ContextCompat.getColor(JikangyeActivity.this, R.color.theme));
            } else {
                zx_fl_item_name.setTextColor(zx_fl_item_name.getResources().getColor(R.color.hui999999));
                zx_fl_item.setBackgroundColor(ContextCompat.getColor(JikangyeActivity.this, R.color.touming));
            }
            ll_fl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iPosition = position;
                    notifyDataSetChanged();
                    iPage = 1;
                    sGetUrl();

                }
            });
            return view;
        }
    }

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        iPage = 1;
        sGetUrl();
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

            iPage = 1;
            sGetUrl();

        });
        //上拉加载
        srlControl.setOnLoadmoreListener(refreshlayout -> {
            srlControl.setEnableLoadmore(true);//启用加载
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 MoreDatas() 来模拟我们加载出来的数据
             */
            //  refreshAdapter.loadMore(MoreDatas());
            iPage += 1;
            sGetUrl();

        });
    }

    /**
     * 方法名：sGetUrl()
     * 功  能：济康业
     * 参  数：appId
     * sRestHome--养老院接口
     * sRecoveryNeed--康养需求接口
     * sRestInvitation--帖子接口
     */
    private void sGetUrl() {
        String url = Api.sUrl;
        if (iPosition == 0) {
            url = url + Api.sRestHome;
        } else if (iPosition == 1) {
            url = url + Api.sRecoveryNeed;
        } else if (iPosition == 2) {
            url = url + Api.sRestInvitation;
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        //   params.put("user_id", sUser_id);
        params.put("page", String.valueOf(iPage));
        JSONObject jsonObject = new JSONObject(params);
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (iPage == 1) {
                            srlControl.finishRefresh();//结束刷新
                        } else {
                            srlControl.finishLoadmore();//结束加载
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
                                if (iPosition == 0) {
                                    for (int i = 0; i < jsonArray_data.length(); i++) {
                                        JSONObject jsonObject2 = (JSONObject) jsonArray_data.opt(i);
                                        //养老院id
                                        String ItemId = jsonObject2.getString("id");
                                        //养老院名称
                                        String ItemNursing_Homes_Name = jsonObject2.getString("nursing_homes_name");
                                        //定金价格
                                        String ItemPrice = jsonObject2.getString("price");
                                        //图片
                                        String ItemLogo = jsonObject2.getString("logo");
                                        HashMap<String, String> map = new HashMap<String, String>();
                                        map.put("id", ItemId);
                                        map.put("nursing_homes_name", ItemNursing_Homes_Name);
                                        map.put("price", ItemPrice);
                                        map.put("logo", ItemLogo);
                                        mylist.add(map);
                                    }
                                } else if (iPosition == 1) {
                                    for (int i = 0; i < jsonArray_data.length(); i++) {
                                        JSONObject jsonObject2 = (JSONObject) jsonArray_data.opt(i);
                                        //需求id
                                        String ItemId = jsonObject2.getString("id");
                                        //用户id
                                        String ItemUser_Id = jsonObject2.getString("user_id");
                                        //预算工分
                                        String ItemBudget = jsonObject2.getString("budget");
                                        //发布时间
                                        String ItemCreate_Time = jsonObject2.getString("create_time");
                                        //需求标题
                                        String ItemTitle = jsonObject2.getString("title");
                                        //需求描述
                                        String ItemDescribe = jsonObject2.getString("describe");
                                        //服务地点
                                        String ItemService_Place = jsonObject2.getString("service_place");
                                        //服务时间
                                        String ItemService_Time = jsonObject2.getString("service_time");
                                        //用户昵称
                                        String ItemNickname = jsonObject2.getString("nickname");
                                        //用户头像
                                        String ItemHeadimgurl = jsonObject2.getString("headimgurl");
                                        HashMap<String, String> map = new HashMap<String, String>();
                                        map.put("id", ItemId);
                                        map.put("user_id", ItemUser_Id);
                                        map.put("budget", ItemBudget);
                                        map.put("create_time", ItemCreate_Time);
                                        map.put("title", ItemTitle);
                                        map.put("describe", ItemDescribe);
                                        map.put("service_place", ItemService_Place);
                                        map.put("service_time", ItemService_Time);
                                        map.put("nickname", ItemNickname);
                                        map.put("headimgurl", ItemHeadimgurl);
                                        mylist.add(map);
                                    }
                                } else if (iPosition == 2) {
                                    for (int i = 0; i < jsonArray_data.length(); i++) {
                                        JSONObject jsonObject_data = jsonArray_data.getJSONObject(i);
                                        String resultId = jsonObject_data.getString("id");
                                        String resultUser_id = jsonObject_data.getString("user_id");
                                        String resultContent = jsonObject_data.getString("content");
                                        String resultCreate_time = jsonObject_data.getString("create_time");
                                        String resultNickname = jsonObject_data.getString("nickname");
                                        String resultHeadimgurl = jsonObject_data.getString("headimgurl");
                                        JSONArray jsonArrayImglist = jsonObject_data.getJSONArray("img");
                                        JkyTzData jkyTzData = new JkyTzData();
                                        for (int a = 0; a < jsonArrayImglist.length(); a++) {
                                            String imge = jsonArrayImglist.get(a).toString();
                                            jkyTzData.imgList.add(imge);
                                        }
                                        jkyTzData.isShowAll = false;
                                        // model4.urlList.add(mUrls[i]);
                                        jkyTzData.id = resultId;
                                        jkyTzData.user_id = resultUser_id;
                                        jkyTzData.content = resultContent;
                                        jkyTzData.create_time = resultCreate_time;
                                        jkyTzData.nickname = resultNickname;
                                        jkyTzData.headimgurl = resultHeadimgurl;
                                        mList.add(jkyTzData);
                                        //  otherChannelList.add(new ChannelItem(Integer.valueOf(resultId), resultName, Integer.valueOf(resultId), Integer.valueOf(resultId)));
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
                                // Hint(resultMsg, HintDialog.SUCCESS);
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
        recycleAdapterDome = new RecycleAdapterDome(JikangyeActivity.this, mylist, mList);
        recycleAdapterDome.setHasStableIds(true);
        rv_jky_lb.setAdapter(recycleAdapterDome);
        LinearLayoutManager manager = new LinearLayoutManager(JikangyeActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        //RecyclerView.LayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        //GridLayoutManager manager1 = new GridLayoutManager(context,2);
        //manager1.setOrientation(GridLayoutManager.VERTICAL);
        //StaggeredGridLayoutManager manager2 = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rv_jky_lb.setLayoutManager(manager);
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
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //创建ViewHolder，返回每一项的布局
            if (iPosition == 0) {
                inflater = LayoutInflater.from(context).inflate(R.layout.jky_yly_item, parent, false);
            } else if (iPosition == 1) {
                inflater = LayoutInflater.from(context).inflate(R.layout.jky_kyxq_item, parent, false);
            } else if (iPosition == 2) {
                inflater = LayoutInflater.from(context).inflate(R.layout.jky_tz_item, parent, false);
            }
            MyViewHolder myViewHolder = new MyViewHolder(inflater);
            return myViewHolder;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            //将数据和控件绑定
            if (iPosition == 0) {
                holder.tv_jky_yly_item_nursing_homes_name.setText(arrlist.get(position).get("nursing_homes_name"));
                holder.tv_jky_yly_item_price.setText(arrlist.get(position).get("price"));
                Glide.with(JikangyeActivity.this)
                        .load( Api.sUrl+arrlist.get(position).get("logo"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(holder.iv_jky_yly_item_logo);
                holder.ll_jky_yly_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(JikangyeActivity.this, YlyxqActivity.class);
                        intent.putExtra("id", arrlist.get(position).get("id"));
                        startActivity(intent);
                    }
                });
            } else if (iPosition == 1) {
                if (position == 0) {
                    holder.tv_jky_kyxq_item.setVisibility(View.VISIBLE);
                } else {
                    holder.tv_jky_kyxq_item.setVisibility(View.GONE);
                }
                Glide.with(JikangyeActivity.this)
                        .load( Api.sUrl+arrlist.get(position).get("ItemHeadimgurl"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(holder.civ_jky_kyxq_item_headimgurl);
                holder.tv_jky_kyxq_item_nickname.setText(arrlist.get(position).get("nickname"));
                holder.tv_jky_kyxq_item_create_time.setText(arrlist.get(position).get("create_time"));
                holder.tv_jky_kyxq_item_budget.setText(arrlist.get(position).get("budget"));
                holder.tv_jky_kyxq_item_title.setText(arrlist.get(position).get("title"));
                holder.tv_jky_kyxq_item_describe.setText(arrlist.get(position).get("describe"));
                holder.tv_jky_kyxq_item_service_place.setText("服务地点：" + arrlist.get(position).get("service_place"));
                holder.tv_jky_kyxq_item_service_time.setText("服务时间：" + arrlist.get(position).get("service_time"));
                holder.ll_jky_kyxq_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(JikangyeActivity.this, KyxqXqActivity.class);
                        intent.putExtra("id", arrlist.get(position).get("id"));
                        startActivity(intent);
                    }
                });
            } else if (iPosition == 2) {
                if (position == 0) {
                    holder.tv_jky_tz_item.setVisibility(View.VISIBLE);
                } else {
                    holder.tv_jky_tz_item.setVisibility(View.GONE);
                }
                Glide.with(JikangyeActivity.this)
                        .load( Api.sUrl+mList.get(position).headimgurl)
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(holder.civ_jky_tz_item_headimgurl);
                holder.tv_jky_tz_item_nickname.setText(mList.get(position).nickname);
                holder.tv_jky_tz_item_create_time.setText(mList.get(position).create_time);
                holder.tv_jky_tz_item_content.setText(mList.get(position).content);
                holder.mv_jky_tz_item_img.setLayoutParams(new LinearLayout.LayoutParams(900, ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.mv_jky_tz_item_img.setImages(mList.get(position).imgList);
                if (mList.get(position).imgList.size() > 0) {
                    holder.mv_jky_tz_item_img.setVisibility(View.VISIBLE);
                } else {
                    holder.mv_jky_tz_item_img.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public int getItemCount() {
            //返回Item总条数
            int num = 0;
            if (iPosition == 2) {
                num = mList.size();
            } else {
                num = arrlist.size();
            }
            return num;
        }

        //内部类，绑定控件
        class MyViewHolder extends RecyclerView.ViewHolder {

            LinearLayout ll_jky_yly_item;
            ImageView iv_jky_yly_item_logo;
            TextView tv_jky_yly_item_nursing_homes_name;
            TextView tv_jky_yly_item_price;

            LinearLayout ll_jky_kyxq_item;
            TextView tv_jky_kyxq_item;
            CircleImageView civ_jky_kyxq_item_headimgurl;
            TextView tv_jky_kyxq_item_nickname;
            TextView tv_jky_kyxq_item_create_time;
            TextView tv_jky_kyxq_item_budget;
            TextView tv_jky_kyxq_item_title;
            TextView tv_jky_kyxq_item_describe;
            TextView tv_jky_kyxq_item_service_place;
            TextView tv_jky_kyxq_item_service_time;

            TextView tv_jky_tz_item;
            CircleImageView civ_jky_tz_item_headimgurl;
            TextView tv_jky_tz_item_nickname;
            TextView tv_jky_tz_item_create_time;
            TextView tv_jky_tz_item_content;
            MultiView mv_jky_tz_item_img;

            public MyViewHolder(View itemView) {
                super(itemView);
                if (iPosition == 0) {
                    ll_jky_yly_item = itemView.findViewById(R.id.ll_jky_yly_item);
                    iv_jky_yly_item_logo = itemView.findViewById(R.id.iv_jky_yly_item_logo);
                    tv_jky_yly_item_nursing_homes_name = itemView.findViewById(R.id.tv_jky_yly_item_nursing_homes_name);
                    tv_jky_yly_item_price = itemView.findViewById(R.id.tv_jky_yly_item_price);
                } else if (iPosition == 1) {
                    ll_jky_kyxq_item = itemView.findViewById(R.id.ll_jky_kyxq_item);
                    tv_jky_kyxq_item = itemView.findViewById(R.id.tv_jky_kyxq_item);
                    civ_jky_kyxq_item_headimgurl = itemView.findViewById(R.id.civ_jky_kyxq_item_headimgurl);
                    tv_jky_kyxq_item_nickname = itemView.findViewById(R.id.tv_jky_kyxq_item_nickname);
                    tv_jky_kyxq_item_create_time = itemView.findViewById(R.id.tv_jky_kyxq_item_create_time);
                    tv_jky_kyxq_item_budget = itemView.findViewById(R.id.tv_jky_kyxq_item_budget);
                    tv_jky_kyxq_item_title = itemView.findViewById(R.id.tv_jky_kyxq_item_title);
                    tv_jky_kyxq_item_describe = itemView.findViewById(R.id.tv_jky_kyxq_item_describe);
                    tv_jky_kyxq_item_service_place = itemView.findViewById(R.id.tv_jky_kyxq_item_service_place);
                    tv_jky_kyxq_item_service_time = itemView.findViewById(R.id.tv_jky_kyxq_item_service_time);

                } else if (iPosition == 2) {
                    tv_jky_tz_item = itemView.findViewById(R.id.tv_jky_tz_item);
                    civ_jky_tz_item_headimgurl = itemView.findViewById(R.id.civ_jky_tz_item_headimgurl);
                    tv_jky_tz_item_nickname = itemView.findViewById(R.id.tv_jky_tz_item_nickname);
                    tv_jky_tz_item_create_time = itemView.findViewById(R.id.tv_jky_tz_item_create_time);
                    tv_jky_tz_item_content = itemView.findViewById(R.id.tv_jky_tz_item_content);
                    mv_jky_tz_item_img = itemView.findViewById(R.id.mv_jky_tz_item_img);

                }
            }
        }

    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(JikangyeActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(JikangyeActivity.this, R.style.dialog, sHint, type, true).show();
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

}
