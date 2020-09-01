package com.example.administrator.yunyue.erci.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.GridSpacingItemDecoration;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.data.Txdd_Yhj_GridData;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc_activity.SpjjActivity;
import com.example.administrator.yunyue.sc_activity.SpxqActivity;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.loonggg.rvbanner.lib.RecyclerViewBanner;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import master.flame.danmaku.danmaku.model.FBDanmaku;

import static io.rong.imkit.utilities.RongUtils.getScreenWidth;

public class YlyxqActivity extends AppCompatActivity implements OnBannerListener {
    private static final String TAG = YlyxqActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_ylyxq_back;
    private SharedPreferences pref;
    private String sUser_id = "";
    /**
     * 轮播图
     */
    private Banner banner_ylyxq;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;
    /**
     * 养老院id
     */
    private String sRest_id = "";
    /**
     * 房屋类型
     */
    private RecyclerView rv_ylyxq_room;
    /**
     * 养老院名称
     */
    private TextView tv_ylyxq_nursing_homes_name;
    /**
     * 定金
     */
    private TextView tv_ylyxq_price;
    /**
     * 地址
     */
    private TextView tv_ylyxq_address;
    /**
     * 详情
     */
    private TextView tv_ylyxq_info;
    /**
     * 承若
     */
    private RecyclerView rv_ylyxq_commitment;
    /**
     * 服务
     */
    private RecyclerView rv_ylyxq_server;
    /**
     * 设备
     */
    private RecyclerView rv_ylyxq_facility;
    /**
     * 温馨提示
     */
    private TextView tv_ylyxq_prompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.theme);
        }
        setContentView(R.layout.activity_ylyxq);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        sRest_id = getIntent().getStringExtra("id");
        ll_ylyxq_back = findViewById(R.id.ll_ylyxq_back);
        ll_ylyxq_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        banner_ylyxq = findViewById(R.id.banner_ylyxq);
        rv_ylyxq_room = findViewById(R.id.rv_ylyxq_room);
        tv_ylyxq_nursing_homes_name = findViewById(R.id.tv_ylyxq_nursing_homes_name);
        tv_ylyxq_price = findViewById(R.id.tv_ylyxq_price);
        tv_ylyxq_address = findViewById(R.id.tv_ylyxq_address);
        tv_ylyxq_info = findViewById(R.id.tv_ylyxq_info);
        rv_ylyxq_commitment = findViewById(R.id.rv_ylyxq_commitment);
        rv_ylyxq_server = findViewById(R.id.rv_ylyxq_server);
        rv_ylyxq_facility = findViewById(R.id.rv_ylyxq_facility);
        tv_ylyxq_prompt = findViewById(R.id.tv_ylyxq_prompt);

        sRestHomeList();
    }

    /**
     * 方法名：sRestHomeList()
     * 功  能：养老院详情页面接口
     * 参  数：appId
     */
    private void sRestHomeList() {
        hideDialogin();
        dialogin("");
        String url = Api.sUrl + Api.sRestHomeList;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("rest_id", sRest_id);
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
                                JSONObject jsonObjectData = new JSONObject(resultData);

                                /**
                                 * 轮播图
                                 * */
                                list_path = new ArrayList<>();
                                list_title = new ArrayList<>();
                                JSONArray jsonObjectdata1 = jsonObjectData.getJSONArray("banner");
                                for (int i = 0; i < jsonObjectdata1.length(); i++) {
                                    String imagrURL = jsonObjectdata1.optString(i);
                                    list_path.add(imagrURL);
                                    list_title.add("");
                                }

                                //设置样式，里面有很多种样式可以自己都看看效果
                                banner_ylyxq.setBannerStyle(BannerConfig.NUM_INDICATOR);
                                //设置图片加载器
                                banner_ylyxq.setImageLoader(new MyLoader());
                                //设置轮播的动画效果,里面有很多种特效,可以都看看效果。
                                banner_ylyxq.setBannerAnimation(Transformer.Default);
                                //轮播图片的文字
                                //  banner.setBannerTitles(list_title);
                                //设置轮播间隔时间
                                banner_ylyxq.setDelayTime(3000);
                                //设置是否为自动轮播，默认是true
                                banner_ylyxq.isAutoPlay(true);
                                //设置指示器的位置，小点点，居中显示
                                banner_ylyxq.setIndicatorGravity(BannerConfig.CENTER);
                                //设置图片加载地址
                                banner_ylyxq.setImages(list_path)
                                        //轮播图的监听
                                        .setOnBannerListener(YlyxqActivity.this)
                                        //开始调用的方法，启动轮播图。
                                        .start();

                                //	养老院名称
                                String sNursing_homes_name = jsonObjectData.getString("nursing_homes_name");
                                tv_ylyxq_nursing_homes_name.setText(sNursing_homes_name);
                                //定金
                                String sPrice = jsonObjectData.getString("price");
                                tv_ylyxq_price.setText(sPrice);
                                //地址
                                String sAddress = jsonObjectData.getString("address");
                                tv_ylyxq_address.setText(sAddress);
                                //详情
                                String sInfo = jsonObjectData.getString("info");
                                tv_ylyxq_info.setText(sInfo);
                                //温馨提示
                                String sPrompt = jsonObjectData.getString("prompt");
                                tv_ylyxq_prompt.setText(sPrompt);
                                /**
                                 * 房间列表
                                 * */
                                ArrayList<HashMap<String, String>> mylist_room = new ArrayList<HashMap<String, String>>();
                                JSONArray resultroom = jsonObjectData.getJSONArray("room");
                                for (int i = 0; i < resultroom.length(); i++) {
                                    JSONObject jsonObject = resultroom.getJSONObject(i);
                                    //评论id
                                    String resultId = jsonObject.getString("id");
                                    //房间名称
                                    String resultTitle = jsonObject.getString("title");
                                    //房间描述
                                    String resultPresentation = jsonObject.getString("presentation");
                                    //房间定金
                                    String resultDeposit = jsonObject.getString("deposit");
                                    //	房间价格
                                    String resultPrice = jsonObject.getString("price");
                                    String resultNumber = jsonObject.getString("number");
                                    String resultBooking_Num = jsonObject.getString("booking_num");
                                    //房间图片
                                    String resultLogo = jsonObject.getString("logo");
                                    //是否有房源
                                    String resultHousing = jsonObject.getString("housing");

                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("id", resultId);
                                    map.put("title", resultTitle);
                                    map.put("presentation", resultPresentation);
                                    map.put("deposit", resultDeposit);
                                    map.put("price", resultPrice);
                                    map.put("number", resultNumber);
                                    map.put("booking_num", resultBooking_Num);
                                    map.put("logo", resultLogo);
                                    map.put("housing", resultHousing);

                                    mylist_room.add(map);
                                }
                                RecycleAdapterDome recycleAdapterDome = new RecycleAdapterDome(YlyxqActivity.this, mylist_room);
                                rv_ylyxq_room.setAdapter(recycleAdapterDome);

                                GridLayoutManager manager = new GridLayoutManager(YlyxqActivity.this, 2);
                                manager.setOrientation(GridLayoutManager.VERTICAL);
                                rv_ylyxq_room.addItemDecoration(new GridSpacingItemDecoration(2, 15, false));
                                // StaggeredGridLayoutManager manager2 = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                                rv_ylyxq_room.setLayoutManager(manager);

                                /**
                                 * 承诺
                                 * */
                                ArrayList<HashMap<String, String>> mylist_commitment = new ArrayList<HashMap<String, String>>();
                                JSONArray resultcommitment = jsonObjectData.getJSONArray("commitment");
                                for (int i = 0; i < resultcommitment.length(); i++) {
                                    JSONObject jsonObject = resultcommitment.getJSONObject(i);
                                    //名称
                                    String resultName = jsonObject.getString("name");
                                    //图片
                                    String resultLogo = jsonObject.getString("logo");

                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("name", resultName);
                                    map.put("logo", resultLogo);
                                    mylist_commitment.add(map);
                                }
                                RecycleAdapter recycleAdapter = new RecycleAdapter(YlyxqActivity.this, mylist_commitment);
                                rv_ylyxq_commitment.setAdapter(recycleAdapter);
                                rv_ylyxq_commitment.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));

                                /**
                                 * 服务
                                 * */
                                ArrayList<HashMap<String, String>> mylist_server = new ArrayList<HashMap<String, String>>();
                                JSONArray resultserver = jsonObjectData.getJSONArray("server");
                                for (int i = 0; i < resultserver.length(); i++) {
                                    JSONObject jsonObject = resultserver.getJSONObject(i);
                                    //名称
                                    String resultName = jsonObject.getString("name");
                                    //图片
                                    String resultLogo = jsonObject.getString("logo");

                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("name", resultName);
                                    map.put("logo", resultLogo);
                                    mylist_server.add(map);
                                }
                                RecycleAdapter recycleAdapter_server = new RecycleAdapter(YlyxqActivity.this, mylist_server);
                                rv_ylyxq_server.setAdapter(recycleAdapter_server);
                                rv_ylyxq_server.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));


                                /**
                                 * 设备
                                 * */
                                ArrayList<HashMap<String, String>> mylist_facility = new ArrayList<HashMap<String, String>>();
                                JSONArray resultfacility = jsonObjectData.getJSONArray("facility");
                                for (int i = 0; i < resultfacility.length(); i++) {
                                    JSONObject jsonObject = resultfacility.getJSONObject(i);
                                    //名称
                                    String resultName = jsonObject.getString("name");
                                    //图片
                                    String resultLogo = jsonObject.getString("logo");

                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("name", resultName);
                                    map.put("logo", resultLogo);
                                    mylist_facility.add(map);
                                }
                                RecycleAdapter recycleAdapter_facility = new RecycleAdapter(YlyxqActivity.this, mylist_facility);
                                rv_ylyxq_facility.setAdapter(recycleAdapter_facility);
                                rv_ylyxq_facility.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));


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
                hideDialogin();
                //  Hint(error.getMessage(), HintDialog.ERROR);
                error(error);

            }
        });
        requestQueue.add(jsonRequest);
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

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(YlyxqActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(YlyxqActivity.this, R.style.dialog, sHint, type, true).show();
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

    public class RecycleAdapterDome extends RecyclerView.Adapter<RecycleAdapterDome.MyViewHolder> {
        private Context context;

        private View inflater;
        private ArrayList<HashMap<String, String>> arrlist;

        //构造方法，传入数据
        public RecycleAdapterDome(Context context, ArrayList<HashMap<String, String>> arrlist) {
            this.context = context;
            this.arrlist = arrlist;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //创建ViewHolder，返回每一项的布局
            inflater = LayoutInflater.from(context).inflate(R.layout.jky_ylyxq_item, parent, false);
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
            Glide.with(YlyxqActivity.this)
                    .load( Api.sUrl+arrlist.get(position).get("logo"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(holder.iv_jky_ylyxq_item_img);
            holder.tv_jky_ylyxq_item_title.setText(arrlist.get(position).get("title"));
            holder.tv_jky_ylyxq_item_presentation.setText(arrlist.get(position).get("presentation"));
            holder.tv_jky_ylyxq_item_price.setText("￥" + arrlist.get(position).get("price"));
            holder.tv_jky_ylyxq_item_deposit.setText(arrlist.get(position).get("deposit"));
            holder.ll_jky_ylyxq_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(YlyxqActivity.this, FangjianxiangqingActivity.class);
                    intent.putExtra("id", arrlist.get(position).get("id"));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            //返回Item总条数
            return arrlist.size();
        }

        //内部类，绑定控件
        class MyViewHolder extends RecyclerView.ViewHolder {
            LinearLayout ll_jky_ylyxq_item;
            ImageView iv_jky_ylyxq_item_img;
            TextView tv_jky_ylyxq_item_title;
            TextView tv_jky_ylyxq_item_presentation;
            TextView tv_jky_ylyxq_item_price;
            TextView tv_jky_ylyxq_item_deposit;


            public MyViewHolder(View itemView) {
                super(itemView);
                ll_jky_ylyxq_item = itemView.findViewById(R.id.ll_jky_ylyxq_item);
                iv_jky_ylyxq_item_img = itemView.findViewById(R.id.iv_jky_ylyxq_item_img);
                tv_jky_ylyxq_item_title = itemView.findViewById(R.id.tv_jky_ylyxq_item_title);
                tv_jky_ylyxq_item_presentation = itemView.findViewById(R.id.tv_jky_ylyxq_item_presentation);
                tv_jky_ylyxq_item_price = itemView.findViewById(R.id.tv_jky_ylyxq_item_price);
                tv_jky_ylyxq_item_deposit = itemView.findViewById(R.id.tv_jky_ylyxq_item_deposit);

            }
        }
    }


    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {
        private Context context;

        private View inflater;
        private ArrayList<HashMap<String, String>> arrlist;

        //构造方法，传入数据
        public RecycleAdapter(Context context, ArrayList<HashMap<String, String>> arrlist) {
            this.context = context;
            this.arrlist = arrlist;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //创建ViewHolder，返回每一项的布局
            inflater = LayoutInflater.from(context).inflate(R.layout.jky_yly_sb_item, parent, false);
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
            Glide.with(YlyxqActivity.this)
                    .load( Api.sUrl+arrlist.get(position).get("logo"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(holder.iv_jky_yly_sb_item_logo);
            holder.tv_jky_yly_sb_item_name.setText(arrlist.get(position).get("name"));
        }

        @Override
        public int getItemCount() {
            //返回Item总条数
            return arrlist.size();
        }

        //内部类，绑定控件
        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView iv_jky_yly_sb_item_logo;
            TextView tv_jky_yly_sb_item_name;

            public MyViewHolder(View itemView) {
                super(itemView);
                iv_jky_yly_sb_item_logo = itemView.findViewById(R.id.iv_jky_yly_sb_item_logo);
                tv_jky_yly_sb_item_name = itemView.findViewById(R.id.tv_jky_yly_sb_item_name);

            }
        }
    }
}
