package com.example.administrator.yunyue.sc_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc_fenlei.ScrollBean;
import com.example.administrator.yunyue.sc_fenlei.ScrollRightAdapter;
import com.example.administrator.yunyue.sc_gridview.GridViewAdaptersc;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FenleiActivity extends AppCompatActivity {
    private static final String TAG = FenleiActivity.class.getSimpleName();
    private ListView lv_sc_lb;
    /**
     * 推荐
     */
    private MyGridView mgv_fenlei_tj;
    /**
     * 热门推荐
     */
    private MyGridView mgv_fenlei_rmtj;
    /**
     * 猜你喜欢
     */
    private MyGridView mgv_fenlei_cnxh;
    private GridViewAdaptersc mAdapter;
    private ImageView iv_shangcheng_back;
    RequestQueue queue = null;
    String sListData;
    /**
     * 猜你喜欢
     */
    ArrayList<HashMap<String, String>> mylistxihuan;
    /**
     * 推荐
     */
    ArrayList<HashMap<String, String>> mylisttuijian;
    /**
     * 热门排行
     */
    ArrayList<HashMap<String, String>> mylistpaihang;

    /**
     * 推荐
     */
    private LinearLayout ll_fenlei_tj;

    /**
     * 热门排行
     */
    private LinearLayout ll_fenlei_rmtj;

    /**
     * 猜你喜欢
     */
    private LinearLayout ll_fenlei_cnxh;
    /**
     * 分类
     */
    private GridView gv_fl_zuo;
    /**
     * 一级分类ID
     */
    String ItemId;
    /**
     * 一级分类
     * 默认选中第一项
     */
    int positionItme = 0;

    private RecyclerView recRight;
    private GridLayoutManager rightManager;
    private Context mContext;
    private ScrollRightAdapter rightAdapter;
    //记录右侧当前可见的第一个item的position
    private int first = 0;
    private List<String> left;
    private List<ScrollBean> right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_fenlei);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        mContext = this;
        mylistxihuan = new ArrayList<HashMap<String, String>>();
        mylisttuijian = new ArrayList<HashMap<String, String>>();
        mylistpaihang = new ArrayList<HashMap<String, String>>();
        queue = Volley.newRequestQueue(FenleiActivity.this);
        iv_shangcheng_back = (ImageView) findViewById(R.id.iv_shangcheng_back);
        lv_sc_lb = (ListView) findViewById(R.id.lv_sc_lb);
        mgv_fenlei_tj = findViewById(R.id.mgv_fenlei_tj);
        mgv_fenlei_cnxh = findViewById(R.id.mgv_fenlei_cnxh);
        mgv_fenlei_rmtj = findViewById(R.id.mgv_fenlei_rmtj);
        ll_fenlei_tj = findViewById(R.id.ll_fenlei_tj);
        ll_fenlei_rmtj = findViewById(R.id.ll_fenlei_rmtj);
        ll_fenlei_cnxh = findViewById(R.id.ll_fenlei_cnxh);
        gv_fl_zuo = findViewById(R.id.gv_fl_zuo);
        recRight = findViewById(R.id.rec_right);
        iv_shangcheng_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
  /*      lv_sc_lb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> map = (HashMap<String, String>) lv_sc_lb.getItemAtPosition(i);
                ItemId = map.get("ItemId");
                erjileibian(ItemId);
            }
        });*/
        hideDialogin();
        dialogin("");
        // qingqiu();
        /**
         * 一级分类
         * */
        yjfl();
        initRight();

    }

    private void initRight() {

        rightManager = new GridLayoutManager(mContext, 3);

        if (rightAdapter == null) {
            rightAdapter = new ScrollRightAdapter(R.layout.scroll_right, R.layout.layout_right_title, null);
            recRight.setLayoutManager(rightManager);
            recRight.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    outRect.set(dpToPx(mContext, getDimens(mContext, R.dimen.dp3))
                            , 0
                            , dpToPx(mContext, getDimens(mContext, R.dimen.dp3))
                            , dpToPx(mContext, getDimens(mContext, R.dimen.dp3)));
                }
            });
            recRight.setAdapter(rightAdapter);
        } else {
            rightAdapter.notifyDataSetChanged();
        }
        //  rightAdapter.setNewData(right);


        rightAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    //点击左侧列表的相应item,右侧列表相应的title置顶显示
                    //(最后一组内容若不能填充右侧整个可见页面,则显示到右侧列表的最底端)
                    case R.id.ll_you:
                        //   HashMap<String, String> map = (HashMap<String, String>) lv_sc_lb.getItemAtPosition(position);
                 /*       Intent intent = new Intent(FenleiActivity.this, FwzxActivity.class);
                        startActivity(intent);*/
                        ScrollBean s = right.get(position);
                        ScrollBean.ScrollItemBean t = s.t;

                        Intent intent = new Intent(FenleiActivity.this, SplbActivity.class);
                        intent.putExtra("id", ItemId);
                        intent.putExtra("er_id", "");
                        intent.putExtra("miaoshu", t.getText());
                        intent.putExtra("san_id", t.getId());
                        startActivity(intent);
                        break;
                }
            }
        });

    }


    /**
     * 获得资源 dimens (dp)
     *
     * @param context
     * @param id      资源id
     * @return
     */
    public float getDimens(Context context, int id) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float px = context.getResources().getDimension(id);
        return px / dm.density;
    }

    /**
     * dp转px
     *
     * @param context
     * @param dp
     * @return
     */
    public int dpToPx(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5f);
    }

    private void yjfl() {
        String url = Api.sUrl + "Api/Good/typelist/appId/" + Api.sApp_Id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
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
                        ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String, String>>();
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("ItemId", jsonObject.getString("id"));//id
                            map.put("ItemName", jsonObject.getString("name"));//
                            map.put("ItemLogo", jsonObject.getString("logo"));//
                            myList.add(map);
                            if (i == 0) {
                                hideDialogin();
                                dialogin("");
                                ejfl(jsonObject.getString("id"));
                            }
                        }
                        data(myList);

                        // setGridView();
                        //  tv_camera_guanggao.setText(resultAdName);
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
        queue.add(request);
    }


    private void ejfl(String fast_id) {
        left = new ArrayList<>();
        right = new ArrayList<>();
        String url = Api.sUrl + "Api/Good/typelisttwo/appId/" + Api.sApp_Id
                + "/fast_id/" + fast_id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    left = new ArrayList<>();
                    right = new ArrayList<>();
                    if (resultCode > 0) {
                        ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String, String>>();
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            JSONObject jsonObjectdate = resultJsonArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("ItemId", jsonObjectdate.getString("name"));//id
                            map.put("ItemName", jsonObjectdate.getString("name"));//
                            map.put("ItemLogo", jsonObjectdate.getString("logo"));//
                            right.add(new ScrollBean(true, jsonObjectdate.getString("name")));
                            JSONArray resultJsonArray_type = jsonObjectdate.getJSONArray("type");
                            for (int a = 0; a < resultJsonArray_type.length(); a++) {
                                JSONObject jsonObjecttype = resultJsonArray_type.getJSONObject(a);
                                String name = jsonObjecttype.getString("name");
                                right.add(new ScrollBean(new ScrollBean.ScrollItemBean(jsonObjecttype.getString("id"),
                                        jsonObjecttype.getString("name"),  jsonObjecttype.getString("logo"), jsonObjectdate.getString("name"))));
                            }
                        }
                    } else {
                    }
                    rightAdapter.setNewData(right);
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


    private void erjileibian(String sId) {
        /**
         * 猜你喜欢
         */
        ArrayList<HashMap<String, String>> mylistxihuan_data = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < mylistxihuan.size(); i++) {
            if (mylistxihuan.get(i).get("ItemLbId").equals(sId)) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ItemId", mylistxihuan.get(i).get("ItemId"));
                map.put("ItemName", mylistxihuan.get(i).get("ItemName"));
                map.put("ItemImage", mylistxihuan.get(i).get("ItemImage"));
                mylistxihuan_data.add(map);
            }
        }
        if (mylistxihuan_data.size() == 0) {
            ll_fenlei_cnxh.setVisibility(View.GONE);
        } else {
            ll_fenlei_cnxh.setVisibility(View.VISIBLE);
        }

        setGridViewCnxh(mylistxihuan_data);

    }

    private void qingqiu() {
        String url = Api.sUrl + "Api/Good/typelist/appId/" + Api.sApp_Id;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                sListData = sDate;
                System.out.println(sDate);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        ArrayList<HashMap<String, String>> mylistZuo = new ArrayList<HashMap<String, String>>();
                        JSONArray resultJsonArrayData = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < resultJsonArrayData.length(); i++) {
                            jsonObject = resultJsonArrayData.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String name = jsonObject.getString("name");
                            HashMap<String, String> map1 = new HashMap<String, String>();
                            map1.put("ItemId", id);
                            map1.put("ItemText", name);
                            mylistZuo.add(map1);

                   /*         String resultType = jsonObject.getString("type");
                            JSONObject jsonObject_resultType = new JSONObject(resultType);*/
                            JSONArray resultJsonArrayType = jsonObject.getJSONArray("type");
                            /**
                             * 猜你喜欢
                             * */
                            for (int a = 0; a < resultJsonArrayType.length(); a++) {
                                jsonObject = resultJsonArrayType.getJSONObject(a);
                                HashMap<String, String> maptmenu = new HashMap<String, String>();
                                maptmenu.put("ItemLbId", id);
                                maptmenu.put("ItemId", jsonObject.getString("id"));
                                maptmenu.put("ItemName", jsonObject.getString("name"));
                                maptmenu.put("ItemImage", jsonObject.getString("logo"));
                                mylistxihuan.add(maptmenu);
                            }
                        }
                        data(mylistZuo);
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


    private void back() {
        finish();
    }

/*    private void data(final ArrayList<HashMap<String, String>> mylist) {
        SimpleAdapter mSchedule = new SimpleAdapter(this, //没什么解释
                mylist,//数据来源
                R.layout.sc_z_lb_item,//ListItem的XML实现
                new String[]{"ItemText"},
                new int[]{R.id.tv_sc_z_lb});
        //添加并且显示
        lv_sc_lb.setAdapter(mSchedule);
    }*/

    /**
     * 猜你喜欢
     * 设置GirdView参数，绑定数据
     */
    private void data(final ArrayList<HashMap<String, String>> mylist) {
        MyAdapter myAdapter = new MyAdapter(FenleiActivity.this);
        myAdapter.arrlist = mylist;
        lv_sc_lb.setAdapter(myAdapter);
    }


    /**
     * 猜你喜欢
     * 设置GirdView参数，绑定数据
     */
    private void setGridViewCnxh(final ArrayList<HashMap<String, String>> mylist) {
        MyAdapterCnxh myAdapterCnxh = new MyAdapterCnxh(FenleiActivity.this);
        myAdapterCnxh.arrlist = mylist;
        gv_fl_zuo.setAdapter(myAdapterCnxh);
    }

    /**
     * 一级分类
     */
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
                view = inflater.inflate(R.layout.sc_z_lb_item, null);
            }
            TextView tv_sc_z_lb = view.findViewById(R.id.tv_sc_z_lb);
            tv_sc_z_lb.setText(arrlist.get(position).get("ItemName"));
            tv_sc_z_lb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    positionItme = position;
                    notifyDataSetChanged();
                    hideDialogin();
                    dialogin("");
                    ejfl(arrlist.get(position).get("ItemId"));
                }
            });
            if (positionItme == position) {
                tv_sc_z_lb.setTextColor(tv_sc_z_lb.getResources().getColor(R.color.theme));
                ItemId = arrlist.get(position).get("ItemId");

            } else {
                tv_sc_z_lb.setTextColor(tv_sc_z_lb.getResources().getColor(R.color.black));
            }
/*            map.put("ItemId", jsonObject.getString("id"));//id
            map.put("ItemName", jsonObject.getString("name"));//*/
            return view;
        }
    }


    /**
     * 猜你喜欢
     */
    private class MyAdapterCnxh extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        ArrayList<HashMap<String, String>> arrlist;

        public MyAdapterCnxh(Context context) {
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
                view = inflater.inflate(R.layout.sc_y_lb_item, null);
            }
            LinearLayout ll_sc_y_lb_item = view.findViewById(R.id.ll_sc_y_lb_item);
            ll_sc_y_lb_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(FenleiActivity.this, SplbActivity.class);
                    intent.putExtra("id", ItemId);
                    intent.putExtra("er_id", arrlist.get(position).get("ItemId"));
                    intent.putExtra("miaoshu", arrlist.get(position).get("ItemName"));
                    startActivity(intent);
                }
            });
            ImageView iv_sc_y = view.findViewById(R.id.iv_sc_y);
            TextView tv_sc_y = view.findViewById(R.id.tv_sc_y);
            Glide.with(FenleiActivity.this)
                    .load( Api.sUrl+ arrlist.get(position).get("ItemImage"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_sc_y);
            tv_sc_y.setText(arrlist.get(position).get("ItemName"));

            return view;
        }
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

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(FenleiActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(FenleiActivity.this, R.style.dialog, sHint, type, true).show();
    }

}
