package com.example.administrator.yunyue.sc_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.example.administrator.yunyue.Utils;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class XsmsActivity extends AppCompatActivity {


    RequestQueue queue = null;
    private GridView gv_xsmx_sj;
    private int mItemWidth = 0; // Item宽度：每个标题的宽度
    private int mScreenWidth = 0; // 屏幕宽度
    private String sSj = "";

    private PullToRefreshGridView pull_refresh_grid_xsms;

    public ArrayList<HashMap<String, Object>> arrmylist = new ArrayList<HashMap<String, Object>>();
    private String sTime = "";
    private int iPage;
    private MyAdapter myAdapter;
    /**
     * 返回
     */
    private ImageView iv_xsms_back;
    private LinearLayout ll_xsms_back;

    private HorizontalScrollView hsv_xsms;
    private String sUser_id = "";
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.theme);
        }
        setContentView(R.layout.activity_xsms);
        pref = PreferenceManager.getDefaultSharedPreferences(XsmsActivity.this);
        sUser_id = pref.getString("user_id", "");
        queue = Volley.newRequestQueue(XsmsActivity.this);
        iPage = 1;
        gv_xsmx_sj = findViewById(R.id.gv_xsmx_sj);
        hsv_xsms = findViewById(R.id.hsv_xsms);
        pull_refresh_grid_xsms = (PullToRefreshGridView) findViewById(R.id.pull_refresh_grid_xsms);
        iv_xsms_back = findViewById(R.id.iv_xsms_back);
        ll_xsms_back = findViewById(R.id.ll_xsms_back);
        ll_xsms_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iv_xsms_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pull_refresh_grid_xsms.setMode(PullToRefreshBase.Mode.BOTH);
        mScreenWidth = Utils.getWindowsWidth(XsmsActivity.this);
        mItemWidth = mScreenWidth / 5; // 一个Item宽度为屏幕的1/7
        dialogin("");
        shijian();
        myAdapter = new MyAdapter(XsmsActivity.this);
        pull_refresh_grid_xsms.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            // 下拉刷新加载
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                Log.e("TAG", "onPullDownToRefresh"); // Do work to
                // 刷新时间
                String label = DateUtils.formatDateTime(
                        getApplicationContext(),
                        System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                // AsyncTask异步交互加载数据
                // new GetDataTask().execute(URL + z);
                iPage = 1;
                // huoqu();
                mx();
                //   huoqu(iPage);

            }

            // 上拉加载跟多
            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                Log.e("TAG", "onPullUpToRefresh"); // Do work to refresh

                mx();
                //   huoqu(iPage);
                // 页数
                //   p = p + 1;
                // AsyncTask异步交互加载数据
                // new GetDataTask2().execute(URL + p);
            }
        });
        // if (ZixunFragment.iPosition == 0) {
        arrmylist = new ArrayList<HashMap<String, Object>>();
        // 刷新适配器
        myAdapter.notifyDataSetChanged();
    }


    /**
     * 设置GirdView参数，绑定数据
     */
    private void setGridView(JSONArray resultJsonArray) {
        MyAdapterXj myAdapterXj = new MyAdapterXj(XsmsActivity.this);
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        try {
            for (int i = 0; i < resultJsonArray.length(); i++) {
                JSONObject jsonObject = resultJsonArray.getJSONObject(i);
                String resultId = jsonObject.getString("id");
                String resultTime = jsonObject.getString("time");
                String resultIsOk = jsonObject.getString("is_ok");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ItemId", resultId);
                map.put("ItemTime", resultTime);
                map.put("ItemIsOk", resultIsOk);
                mylist.add(map);
            }
            myAdapterXj.arrlist = mylist;
            //gv_xsmx.setAdapter(myAdapterXj);
        } catch (JSONException e) {
            hideDialogin();
            e.printStackTrace();
        }
        int size = mylist.size();
        int length = mItemWidth;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                mItemWidth * (size + 1), LinearLayout.LayoutParams.FILL_PARENT);
        gv_xsmx_sj.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gv_xsmx_sj.setColumnWidth(mItemWidth); // 设置列表项宽
        gv_xsmx_sj.setHorizontalSpacing(10); // 设置列表项水平间距
        gv_xsmx_sj.setStretchMode(GridView.NO_STRETCH);
        gv_xsmx_sj.setNumColumns(size); // 设置列数量=列表集合数
        gv_xsmx_sj.setAdapter(myAdapterXj);
        /*sv_shouye.smoothScrollTo(0, 20);
        sv_shouye.setFocusable(true);*/
    }


    /**
     * 秒杀
     */
    private void mx() {
        // String url = Api.sUrl + "GetZhixun/getType/user_id/" + sUser_id;
        String url = Api.sUrl + "Api/Good/miaoshalist/appId/" + Api.sApp_Id
                + "/time/" + sTime + "/page/" + iPage;
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
                        if (iPage == 1) {
                            arrmylist = new ArrayList<HashMap<String, Object>>();
                        }
                        //  ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            String resultId = jsonObject.getString("id");
                            String resultName = jsonObject.getString("name");
                            String resultLogo = jsonObject.getString("logo");
                            String resultPrice = jsonObject.getString("price");
                            String resultMktprice = jsonObject.getString("mktprice");
                            String resultStock = jsonObject.getString("stock");
                            String resultBuy_Count = jsonObject.getString("buy_count");
                      /*      String resultBili = jsonObject.getString("bili");*/
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("id", resultId);
                            map.put("name", resultName);
                            map.put("logo", resultLogo);
                            map.put("price", resultPrice);
                            map.put("mktprice", resultMktprice);
                            map.put("stock", resultStock);
                            map.put("buy_count", resultBuy_Count);
                         /*   map.put("ItemBili", resultBili);*/
                            arrmylist.add(map);
                        }
                        if (iPage == 1) {
                            gridviewdata();
                        } else {
                            gridviewdata1();
                        }
                        iPage += 1;
                        //  String resultTime = jsonObject1.getString("time");
                    } else {
                        if (iPage == 1) {
                            gridviewdata();
                        } else {
                            gridviewdata1();
                        }
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

    private void gridviewdata() {
        pull_refresh_grid_xsms.setAdapter(myAdapter);
        // 刷新适配器
        myAdapter.notifyDataSetChanged();
        // 关闭刷新下拉
        pull_refresh_grid_xsms.onRefreshComplete();

    }

    private void gridviewdata1() {
        // myList = getMenuAdapter();
        // iPage += 1;
        // 刷新适配器
        myAdapter.notifyDataSetChanged();
        // Call onRefreshComplete when the list has been refreshed.
        // 关闭上拉加载
        pull_refresh_grid_xsms.onRefreshComplete();

    }

    /**
     * 秒杀
     */
    private void shijian() {
        // String url = Api.sUrl + "GetZhixun/getType/user_id/" + sUser_id;
        String url = Api.sUrl + "Api/Good/miaoshatime/appId/" + Api.sApp_Id;
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
                        //   cshsv_xsms.getLinear().removeAllViews();
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                        setGridView(resultJsonArray);


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

    private void jianjei(final String pid) {
        String url = Api.sUrl + "Api/Good/gooddetail/appId/" + Api.sApp_Id
                + "/good_id/" + pid + "/user_id/" + sUser_id;
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
                        Intent intent1 = new Intent(XsmsActivity.this, SpxqActivity.class);
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


    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(XsmsActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(XsmsActivity.this, R.style.dialog, sHint, type, true).show();
    }

    private class MyAdapterXj extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;

        public MyAdapterXj(Context context) {
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
                view = inflater.inflate(R.layout.xsmx_sj_item, null);
            }
            LinearLayout ll_xsms_sj_item = view.findViewById(R.id.ll_xsms_sj_item);
            TextView tv_xsms_sj = view.findViewById(R.id.tv_xsms_sj);
            TextView tv_xsms_type = view.findViewById(R.id.tv_xsms_type);
            tv_xsms_sj.setText(arrlist.get(position).get("ItemTime"));
            String hour;
            Calendar cal;
            // cshsv_xsms.addItemView(ll_xsms_sj_item, position);
            cal = Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            if (cal.get(Calendar.AM_PM) == 0) {
                hour = String.valueOf(cal.get(Calendar.HOUR));
            } else {
                hour = String.valueOf(cal.get(Calendar.HOUR) + 12);
            }
            String[] Time = arrlist.get(position).get("ItemTime").split(":");
            int iTime = Integer.valueOf(Time[0]);
            int iHour = Integer.valueOf(hour);
            if (iTime == iHour) {
                tv_xsms_type.setText("秒杀进行中");
                if (sSj.equals("")) {
                 /*   arrmylist = new ArrayList<HashMap<String, Object>>();
                    pull_refresh_grid_xsms.setAdapter(myAdapter);*/
                    tv_xsms_sj.setTextColor(tv_xsms_sj.getResources().getColor(R.color.white));
                    tv_xsms_type.setTextColor(tv_xsms_type.getResources().getColor(R.color.white));
                    sTime = arrlist.get(position).get("ItemTime");
                    mx();
                    hsv_xsms.smoothScrollTo(mItemWidth * (position - 1) - mItemWidth / 2, 0);
                    //   cshsv_xsms.onClicked(ll_xsms_sj_item);
                }
            } else if (iTime == iHour - 1) {
                tv_xsms_type.setText("秒杀进行中");
                if (sSj.equals("")) {
                 /*   arrmylist = new ArrayList<HashMap<String, Object>>();
                    pull_refresh_grid_xsms.setAdapter(myAdapter);*/
                    tv_xsms_sj.setTextColor(tv_xsms_sj.getResources().getColor(R.color.white));
                    tv_xsms_type.setTextColor(tv_xsms_type.getResources().getColor(R.color.white));
                    sTime = arrlist.get(position).get("ItemTime");
                    mx();
                    hsv_xsms.smoothScrollTo(mItemWidth * (position - 1) - mItemWidth / 2, 0);
                    //   cshsv_xsms.onClicked(ll_xsms_sj_item);
                }
            } else {
                tv_xsms_sj.setTextColor(tv_xsms_sj.getResources().getColor(R.color.hui999999));
                tv_xsms_type.setTextColor(tv_xsms_type.getResources().getColor(R.color.hui999999));
                if (iTime < iHour) {
                    tv_xsms_type.setText("已开抢");
                } else {
                    tv_xsms_type.setText("未开抢");
                }
            }

            ll_xsms_sj_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sSj = arrlist.get(position).get("ItemId");
                    notifyDataSetChanged();
                    sTime = arrlist.get(position).get("ItemId");
                    iPage = 1;
                    mx();
                    hsv_xsms.smoothScrollTo(mItemWidth * (position - 1) - mItemWidth / 2, 0);
                    //  cshsv_xsms.onClicked(view);
                }
            });
            if (sSj.equals("")) {
            } else if (arrlist.get(position).get("ItemId").equals(sSj)) {
                tv_xsms_sj.setTextColor(tv_xsms_sj.getResources().getColor(R.color.white));
                tv_xsms_type.setTextColor(tv_xsms_type.getResources().getColor(R.color.white));
            } else {
                tv_xsms_sj.setTextColor(tv_xsms_sj.getResources().getColor(R.color.hui999999));
                tv_xsms_type.setTextColor(tv_xsms_type.getResources().getColor(R.color.hui999999));
            }


            return view;
        }
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime   当前时间
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime   当前时间
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     * @author jqlin
     */
    public static boolean isStartDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (!date.after(begin)) {
            return true;
        } else {
            return false;
        }
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;


        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);

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
                view = inflater.inflate(R.layout.shangcheng_xsms_item, null);
            }
            LinearLayout ll_shangcheng_xsms_item = view.findViewById(R.id.ll_shangcheng_xsms_item);
            ImageView iv_shangcheng_xsms_img = view.findViewById(R.id.iv_shangcheng_xsms_img);
            TextView tv_shangcheng_xsms_name = view.findViewById(R.id.tv_shangcheng_xsms_name);
            TextView tv_shangcheng_xsms_price = view.findViewById(R.id.tv_shangcheng_xsms_price);
            TextView tv_shangcheng_xsmx_yj = view.findViewById(R.id.tv_shangcheng_xsmx_yj);
            TextView tv_shangcheng_xsms_xl = view.findViewById(R.id.tv_shangcheng_xsms_xl);
            TextView tv_shangcheng_xsms_bl = view.findViewById(R.id.tv_shangcheng_xsms_bl);
            ProgressBar pb_progressBar1 = view.findViewById(R.id.pb_progressBar1);
            tv_shangcheng_xsmx_yj.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线
      /*      map.put("id", resultId);
            map.put("name", resultName);
            map.put("logo", resultLogo);
            map.put("price", resultPrice);
            map.put("mktprice", resultMktprice);
            map.put("stock", resultStock);
            map.put("buy_count", resultBuy_Count);*/

            Glide.with(XsmsActivity.this).load( Api.sUrl+ arrmylist.get(position).get("logo"))
                    .asBitmap()
                    .dontAnimate()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .into(iv_shangcheng_xsms_img);
            tv_shangcheng_xsms_name.setText(arrmylist.get(position).get("name").toString());
            tv_shangcheng_xsmx_yj.setText(arrmylist.get(position).get("mktprice").toString());
            tv_shangcheng_xsms_price.setText(arrmylist.get(position).get("price").toString());
            tv_shangcheng_xsms_xl.setText("已抢" + arrmylist.get(position).get("buy_count").toString() + "件");
            float pb = Integer.valueOf(arrmylist.get(position).get("buy_count").toString()) / Integer.valueOf(arrmylist.get(position).get("stock").toString());
            pb_progressBar1.setProgress((int) pb);
            pb_progressBar1.setSecondaryProgress((int) pb + 10);
            tv_shangcheng_xsms_bl.setText(pb + "%");
            ll_shangcheng_xsms_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogin("");
                    jianjei(arrmylist.get(position).get("id").toString());
                }
            });

/*            map.put("ItemOriginalImg", resultOriginalImg);
            map.put("ItemGoodsId", resultGoodsId);
            map.put("ItemOldPrice", resultOldPrice);
            map.put("ItemShopPrice", resultShopPrice);
            map.put("ItemRid", resultRid);
            map.put("ItemGoodsName", resultGoodsName);
            map.put("ItemSalesSum", resultSalesSum);*/

            return view;
        }
    }
}
