package com.example.administrator.yunyue.erci.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.GridSpacingItemDecoration;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.utils.NullTranslator;
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
import java.util.Map;

import master.flame.danmaku.danmaku.model.FBDanmaku;

public class FangjianxiangqingActivity extends AppCompatActivity implements OnBannerListener {
    private static final String TAG = FangjianxiangqingActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_fjxq_back;
    /**
     * 房间id
     */
    private String sRoom_id = "";

    private SharedPreferences pref;
    private String sUser_id = "";
    private String sIs_vip = "";
    /**
     * 轮播图
     */
    private Banner banner_fjxq;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;
    /**
     * 房间名称
     */
    private TextView tv_fjxq_title;
    /**
     * 房间价格
     */
    private TextView tv_fjxq_price;
    /**
     * 房间定金
     */
    private TextView tv_fjxq_deposit;
    /**
     * 面积平方
     */
    private TextView tv_fjxq_area;
    /**
     * 房间数量
     */
    private TextView tv_fjxq_number;
    /**
     * 朝向
     */
    private TextView tv_fjxq_orientation;
    /**
     * 房间层数
     */
    private TextView tv_fjxq_floors;

    /**
     * 房间描述
     */
    private TextView tv_fjxq_presentation;
    /**
     * 特色设施
     */
    private RecyclerView rv_fjxq_feature;

    /**
     * 基础设施
     */
    private RecyclerView rv_fjxq_basics;
    /**
     * 卫浴设施
     */
    private RecyclerView rv_fjxq_bathroom;
    /**
     * 适老化设施
     */
    private RecyclerView rv_fjxq_optimal_aging;
    /**
     * 订房
     */
    private TextView tv_fjxq_jd;
    //房间logo
    public static String sImg = "";
    //房间名称
    public static String sTitle = "";
    //房间价格
    public static String sPrice = "";
    //房间定金
    public static String sDeposit = "";
    //养老院名称
    public static String sHome = "";
    private String sHome_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white);
        }
        setContentView(R.layout.activity_fangjianxiangqing);
        ll_fjxq_back = findViewById(R.id.ll_fjxq_back);
        ll_fjxq_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        sIs_vip = pref.getString("is_vip", "");
        sRoom_id = getIntent().getStringExtra("id");
        banner_fjxq = findViewById(R.id.banner_fjxq);
        tv_fjxq_title = findViewById(R.id.tv_fjxq_title);
        tv_fjxq_price = findViewById(R.id.tv_fjxq_price);
        tv_fjxq_deposit = findViewById(R.id.tv_fjxq_deposit);
        tv_fjxq_area = findViewById(R.id.tv_fjxq_area);
        tv_fjxq_number = findViewById(R.id.tv_fjxq_number);
        tv_fjxq_orientation = findViewById(R.id.tv_fjxq_orientation);
        tv_fjxq_floors = findViewById(R.id.tv_fjxq_floors);
        tv_fjxq_presentation = findViewById(R.id.tv_fjxq_presentation);
        rv_fjxq_feature = findViewById(R.id.rv_fjxq_feature);
        rv_fjxq_basics = findViewById(R.id.rv_fjxq_basics);
        rv_fjxq_bathroom = findViewById(R.id.rv_fjxq_bathroom);
        rv_fjxq_optimal_aging = findViewById(R.id.rv_fjxq_optimal_aging);
        sRoomInfo();
        tv_fjxq_jd = findViewById(R.id.tv_fjxq_jd);
        tv_fjxq_jd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(FangjianxiangqingActivity.this, YlyTjddActivity.class);
                    intent.putExtra("id", sRoom_id);
                    intent.putExtra("home_id", sHome_id);
                    startActivity(intent);

            }
        });
    }

    /**
     * 方法名：sRoomInfo()
     * 功  能：房间详情页面接口
     * 参  数：appId
     */
    private void sRoomInfo() {
        hideDialogin();
        dialogin("");
        String url = Api.sUrl + Api.sRoomInfo;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("room_id", sRoom_id);
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
                                JSONArray jsonObjectdata1 = jsonObjectData.getJSONArray("logo");
                                for (int i = 0; i < jsonObjectdata1.length(); i++) {
                                    String imagrURL = jsonObjectdata1.optString(i);
                                    list_path.add(imagrURL);
                                    list_title.add("");
                                }

                                //设置样式，里面有很多种样式可以自己都看看效果
                                banner_fjxq.setBannerStyle(BannerConfig.NUM_INDICATOR);
                                //设置图片加载器
                                banner_fjxq.setImageLoader(new MyLoader());
                                //设置轮播的动画效果,里面有很多种特效,可以都看看效果。
                                banner_fjxq.setBannerAnimation(Transformer.Default);
                                //轮播图片的文字
                                //  banner.setBannerTitles(list_title);
                                //设置轮播间隔时间
                                banner_fjxq.setDelayTime(3000);
                                //设置是否为自动轮播，默认是true
                                banner_fjxq.isAutoPlay(true);
                                //设置指示器的位置，小点点，居中显示
                                banner_fjxq.setIndicatorGravity(BannerConfig.CENTER);
                                //设置图片加载地址
                                banner_fjxq.setImages(list_path)
                                        //轮播图的监听
                                        .setOnBannerListener(FangjianxiangqingActivity.this)
                                        //开始调用的方法，启动轮播图。
                                        .start();

                                //房间logo
                                sImg = jsonObjectData.getString("img");

                                //养老院id
                                sHome_id = jsonObjectData.getString("home_id");
                                //养老院名称
                                sHome = jsonObjectData.getString("home");
                                //房间名称
                                sTitle = jsonObjectData.getString("title");
                                tv_fjxq_title.setText(sTitle);
                                //房间价格
                                sPrice = jsonObjectData.getString("price");
                                tv_fjxq_price.setText("￥" + sPrice);
                                //房间定金
                                sDeposit = jsonObjectData.getString("deposit");
                                tv_fjxq_deposit.setText(sDeposit);
                                //面积平方
                                String sArea = jsonObjectData.getString("area");
                                tv_fjxq_area.setText(sArea);
                                //房间数量
                                String sNumber = jsonObjectData.getString("number");
                                tv_fjxq_number.setText(sNumber);
                                //朝向
                                String sOrientation = jsonObjectData.getString("orientation");
                                tv_fjxq_orientation.setText(sOrientation);
                                //房间层数
                                String sFloors = jsonObjectData.getString("floors");
                                tv_fjxq_floors.setText(sFloors);
                                //房间描述
                                String sPresentation = jsonObjectData.getString("presentation");
                                tv_fjxq_presentation.setText(sPresentation);

                                /**
                                 * 特色设施
                                 * */
                                ArrayList<HashMap<String, String>> mylist_feature = new ArrayList<HashMap<String, String>>();
                                JSONArray resultfeature = jsonObjectData.getJSONArray("feature");
                                for (int i = 0; i < resultfeature.length(); i++) {
                                    JSONObject jsonObject = resultfeature.getJSONObject(i);
                                    //名称
                                    String resultName = jsonObject.getString("name");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("name", resultName);
                                    mylist_feature.add(map);
                                }
                                RecycleAdapter recycleAdapter = new RecycleAdapter(FangjianxiangqingActivity.this, mylist_feature);
                                rv_fjxq_feature.setAdapter(recycleAdapter);
                                rv_fjxq_feature.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

                                /**
                                 * 基础设施
                                 * */
                                ArrayList<HashMap<String, String>> mylist_basics = new ArrayList<HashMap<String, String>>();
                                JSONArray resultbasics = jsonObjectData.getJSONArray("basics");
                                for (int i = 0; i < resultbasics.length(); i++) {
                                    JSONObject jsonObject = resultbasics.getJSONObject(i);
                                    //名称
                                    String resultName = jsonObject.getString("name");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("name", resultName);
                                    mylist_basics.add(map);
                                }
                                RecycleAdapter recycleAdapter_basics = new RecycleAdapter(FangjianxiangqingActivity.this, mylist_basics);
                                rv_fjxq_basics.setAdapter(recycleAdapter_basics);
                                rv_fjxq_basics.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));

                                /**
                                 * 卫浴设施
                                 * */
                                ArrayList<HashMap<String, String>> mylist_bathroom = new ArrayList<HashMap<String, String>>();
                                JSONArray resultbathroom = jsonObjectData.getJSONArray("bathroom");
                                for (int i = 0; i < resultbathroom.length(); i++) {
                                    JSONObject jsonObject = resultbathroom.getJSONObject(i);
                                    //名称
                                    String resultName = jsonObject.getString("name");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("name", resultName);
                                    mylist_bathroom.add(map);
                                }
                                RecycleAdapter recycleAdapter_bathroom = new RecycleAdapter(FangjianxiangqingActivity.this, mylist_bathroom);
                                rv_fjxq_bathroom.setAdapter(recycleAdapter_bathroom);
                                rv_fjxq_bathroom.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));


                                /**
                                 * 适老化设备
                                 * */
                                ArrayList<HashMap<String, String>> mylist_optimal_aging = new ArrayList<HashMap<String, String>>();
                                JSONArray resultoptimal_aging = jsonObjectData.getJSONArray("optimal_aging");
                                for (int i = 0; i < resultoptimal_aging.length(); i++) {
                                    JSONObject jsonObject = resultoptimal_aging.getJSONObject(i);
                                    //名称
                                    String resultName = jsonObject.getString("name");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("name", resultName);
                                    mylist_optimal_aging.add(map);
                                }
                                RecycleAdapter recycleAdapteroptimal_aging = new RecycleAdapter(FangjianxiangqingActivity.this, mylist_optimal_aging);
                                rv_fjxq_optimal_aging.setAdapter(recycleAdapteroptimal_aging);
                                rv_fjxq_optimal_aging.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));


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
        loadingDialog = new LoadingDialog(FangjianxiangqingActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(FangjianxiangqingActivity.this, R.style.dialog, sHint, type, true).show();
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
        public RecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //创建ViewHolder，返回每一项的布局
            inflater = LayoutInflater.from(context).inflate(R.layout.jky_fjxq_item, parent, false);
            RecycleAdapter.MyViewHolder myViewHolder = new RecycleAdapter.MyViewHolder(inflater);
            return myViewHolder;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public void onBindViewHolder(RecycleAdapter.MyViewHolder holder, int position) {
            //将数据和控件绑定
            holder.tv_fjxq_item_name.setText(arrlist.get(position).get("name"));
        }

        @Override
        public int getItemCount() {
            //返回Item总条数
            return arrlist.size();
        }

        //内部类，绑定控件
        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv_fjxq_item_name;

            public MyViewHolder(View itemView) {
                super(itemView);
                tv_fjxq_item_name = itemView.findViewById(R.id.tv_fjxq_item_name);
            }
        }
    }
}
