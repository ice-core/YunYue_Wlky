package com.example.administrator.yunyue.yjdt_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.bean.JsonBean;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class FenleiLiebiaoActivity extends AppCompatActivity {
    private static final String TAG = FenleiLiebiaoActivity.class.getSimpleName();
    /**
     * 分类ID
     */
    private String sId = "";
    /**
     * 分类name
     */
    private String sName = "";
    /**
     * 标题
     */
    private TextView tv_fenlei_liebiao_hint;
    /**
     * 分类列表
     */
    private MyGridView mgv_fenlei_liebiao;
    @BindView(R.id.srl_control)
    SmartRefreshLayout srlControl;
    int iPage = 1;
    private MyAdapter_AdvertisingIndex myAdapter_advertisingIndex;
    private SharedPreferences pref;
    private String sUser_id = "";

    private List<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    /**
     * 投放地址
     */
    private String sRegion = "";
    private LinearLayout ll_fenlei_liebiao_dz;
    private TextView tv_fenlei_liebiao_dz;
    /**
     * 投放地址
     */
    public static String address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_fenlei_liebiao);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        sId = getIntent().getStringExtra("id");
        sName = getIntent().getStringExtra("name");
        tv_fenlei_liebiao_hint = findViewById(R.id.tv_fenlei_liebiao_hint);
        tv_fenlei_liebiao_hint.setText(sName);
        mgv_fenlei_liebiao = findViewById(R.id.mgv_fenlei_liebiao);
        srlControl = findViewById(R.id.srl_control);
        ll_fenlei_liebiao_dz = findViewById(R.id.ll_fenlei_liebiao_dz);
        tv_fenlei_liebiao_dz = findViewById(R.id.tv_fenlei_liebiao_dz);
        sAdvertisingIndex();
        smartRefresh();
        initJsonData();
        ll_fenlei_liebiao_dz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickerView();
            }
        });
        tv_fenlei_liebiao_dz.setText(address);

    }

    private void showPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String opt1tx = options1Items.size() > 0 ?
                        options1Items.get(options1).getPickerViewText() : "";

                String opt2tx = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2) : "";

                String opt3tx = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3) : "";


                sRegion = opt1tx;
                address = opt1tx;
                if (!opt1tx.equals("不限")) {
                    if (!opt2tx.equals("不限")) {
                        address = opt2tx;
                        sRegion = sRegion + "-" + opt2tx;
                        if (!opt3tx.equals("不限")) {
                            address = opt2tx;
                            sRegion = sRegion + "-" + opt3tx;
                        }
                    }
                }
                //Toast.makeText(FbxxActivity.this, tx, Toast.LENGTH_SHORT).show();
                tv_fenlei_liebiao_dz.setText(address);
                iPage = 1;
                sAdvertisingIndex();
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                /*if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }*/
                city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }

        //   mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //   mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
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
            sAdvertisingIndex();
            // srlControl.finishRefresh();//结束刷新

        });
        //上拉加载
        srlControl.setOnLoadmoreListener(refreshlayout -> {
            srlControl.setEnableLoadmore(true);//启用加载
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 MoreDatas() 来模拟我们加载出来的数据*/

            iPage += 1;
            sAdvertisingIndex();
            srlControl.finishLoadmore();//结束加载
        });
    }

    /**
     * 方法名：sAdvertisingIndex()
     * 功  能：广告栏目接口
     * 参  数：appId
     */
    private void sAdvertisingIndex() {
        String url = Api.sUrl + Api.sAdvertisingIndex;
        RequestQueue requestQueue = Volley.newRequestQueue(FenleiLiebiaoActivity.this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        if (!sId.equals("")) {
            params.put("type_id", sId);
        } else {
            params.put("user_id", sUser_id);
            if (sName.equals("我的参与")) {
                params.put("type", "1");
            } else if (sName.equals("我的发布")) {
                params.put("type", "2");
            }
        }
        params.put("page", String.valueOf(iPage));
        if (!sRegion.equals("不限")) {
            params.put("address", sRegion);
        }
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
                            int resultCode = jsonObject0.getInt("code");
                            if (resultCode > 0) {
                                JSONObject jsonObject1 = new JSONObject(sDate.toString()).getJSONObject("data");
                                JSONArray jsonArray = jsonObject1.getJSONArray("advertising");
                                ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
                                    String ItemId = jsonObject2.getString("id");
                                    String ItemLogo = jsonObject2.getString("logo");
                                    String ItemTitle = jsonObject2.getString("title");
                                    String ItemEvery_share = jsonObject2.getString("every_share");
                                    String ItemPrice = jsonObject2.getString("price");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("id", ItemId);
                                    map.put("logo", ItemLogo);
                                    map.put("title", ItemTitle);
                                    map.put("every_share", ItemEvery_share);
                                    map.put("price", ItemPrice);
                                    mylist.add(map);
                                }
                                if (iPage == 1) {
                                    gridviewAdvertisingIndex(mylist);
                                } else {
                                    if (mylist.size() == 0) {
                                        iPage -= 1;
                                    } else {
                                        gridviewAdvertisingIndex1(mylist);
                                    }
                                }

                            } else {
                                hideDialogin();
                                Hint(resultMsg, HintDialog.ERROR);
                                //   Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
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

    private void gridviewAdvertisingIndex(final ArrayList<HashMap<String, String>> myList) {
        myAdapter_advertisingIndex = new MyAdapter_AdvertisingIndex(FenleiLiebiaoActivity.this);
        myAdapter_advertisingIndex.arrlist = myList;
        mgv_fenlei_liebiao.setAdapter(myAdapter_advertisingIndex);
    }

    private void gridviewAdvertisingIndex1(final ArrayList<HashMap<String, String>> myList) {
        myAdapter_advertisingIndex.arrlist.addAll(myList);
        mgv_fenlei_liebiao.setAdapter(myAdapter_advertisingIndex);
    }

    private class MyAdapter_AdvertisingIndex extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;


        public MyAdapter_AdvertisingIndex(Context context) {
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
                view = inflater.inflate(R.layout.gglm_item, null);
            }
            LinearLayout ll_gglm_item = view.findViewById(R.id.ll_gglm_item);
            ll_gglm_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(FenleiLiebiaoActivity.this, GuanggaoxiangqingActivity.class);
                    intent.putExtra("id", arrlist.get(position).get("id"));
                    startActivity(intent);
                }
            });
            ImageView iv_gglm_item_logo = view.findViewById(R.id.iv_gglm_item_logo);
            TextView tv_gglm_item_title = view.findViewById(R.id.tv_gglm_item_title);
            TextView tv_gglm_item_price = view.findViewById(R.id.tv_gglm_item_price);
            tv_gglm_item_title.setText(arrlist.get(position).get("title"));
            tv_gglm_item_price.setText(arrlist.get(position).get("price") + "元/人");
            Glide.with(FenleiLiebiaoActivity.this)
                    .load( Api.sUrl+ arrlist.get(position).get("logo"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_gglm_item_logo);

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
        loadingDialog = new LoadingDialog(FenleiLiebiaoActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(FenleiLiebiaoActivity.this, R.style.dialog, sHint, type, true).show();
    }
}
