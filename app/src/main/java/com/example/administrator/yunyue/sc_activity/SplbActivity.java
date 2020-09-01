package com.example.administrator.yunyue.sc_activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SplbActivity extends AppCompatActivity {
    private MyAdapterSplb myAdapterSplb;
    //  private GridView gv_splb;
    RequestQueue queue = null;
    private String sId = "";
    /**
     * 二级分类id
     */
    private String sEr_id = "";
    private String sSan_id = "";
    private String sMiaoshu = "";
    private static final String TAG = SplbActivity.class.getSimpleName();
    private ImageView iv_splb_back;
    private String sActivity = "";

    private EditText et_splb;
    private ImageView iv_splb_query;
    private TextView tv_splb_zonghe, tv_splb_xlpm, tv_splb_zkyh;
    private LinearLayout ll_splb_sx;
    //  ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    private PullToRefreshGridView mPullRefreshListView;
    int iPage = 1;
    /**
     * 1-推荐
     * 2-价格降序
     * 3-价格升序
     * 4-评论数
     * 5-销量降序
     * 6-有货
     */
    private String Order = "1";
    /**
     * 标题
     */
    private TextView tv_splb_title;
    /**
     * 最低价
     */
    private EditText et_fenlei_dialog_start;
    /**
     * 最高价
     */
    private EditText et_fenlei_dialog_end;
    /**
     * 搜索关键字
     */
    private String sKeywosrd = "";

    /**
     * 品牌id
     */
    private String sBrand_Id = "";

    private Dialog dialog;
    /**
     * 分类
     */
    private LinearLayout ll_splb_query;
    /**
     * 品牌
     */
    private MyGridView mgv_fenlei_dialog_pinpai;
    private MyAdapterPp myAdapterPp;

    /**
     * 显示样式
     */
    private LinearLayout ll_splb_xsfs;
    /**
     * 0--方格
     * 1--列表
     */
    int iLb_Type = 0;


    private ImageView iv_splb_xsfs;
    private String sUser_id = "";
    private SharedPreferences pref;

    /**
     * 空数据
     */
    private LinearLayout ll_splb_kong;

    /*    private Spinner spinner_px;


        private List<String> data_list;
        private ArrayAdapter<String> arr_adapter;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
   /*     getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);*/
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_splb);
        mPullRefreshListView = (PullToRefreshGridView) findViewById(R.id.pull_refresh_grid);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        iv_splb_query = (ImageView) findViewById(R.id.iv_splb_query);
        et_splb = (EditText) findViewById(R.id.et_splb);
        iv_splb_back = (ImageView) findViewById(R.id.iv_splb_back);
        ll_splb_kong = findViewById(R.id.ll_splb_kong);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        final Intent intent = getIntent();
        sId = intent.getStringExtra("id");
        sEr_id = intent.getStringExtra("er_id");
        sSan_id = intent.getStringExtra("san_id");
        sMiaoshu = intent.getStringExtra("miaoshu");
        sActivity = intent.getStringExtra("");
        queue = Volley.newRequestQueue(SplbActivity.this);
        //   gv_splb = (GridView) findViewById(R.id.gv_splb);
        tv_splb_zonghe = (TextView) findViewById(R.id.tv_splb_zonghe);
        tv_splb_xlpm = (TextView) findViewById(R.id.tv_splb_xlpm);
        tv_splb_zkyh = (TextView) findViewById(R.id.tv_splb_zkyh);
        tv_splb_title = findViewById(R.id.tv_splb_title);
        ll_splb_query = findViewById(R.id.ll_splb_query);
        // spinner_px = findViewById(R.id.spinner_px);
        tv_splb_title.setText(sMiaoshu);
        ll_splb_sx = findViewById(R.id.ll_splb_sx);
        ll_splb_xsfs = findViewById(R.id.ll_splb_xsfs);
        iv_splb_xsfs = findViewById(R.id.iv_splb_xsfs);
        if (sId == null) {
            if (sEr_id == null) {
                tv_splb_title.setVisibility(View.GONE);
                ll_splb_query.setVisibility(View.VISIBLE);
            } else {
                tv_splb_title.setVisibility(View.VISIBLE);
                ll_splb_query.setVisibility(View.GONE);
            }

        } else {

            tv_splb_title.setVisibility(View.VISIBLE);
            ll_splb_query.setVisibility(View.GONE);
        }

        /**
         * 列表切换
         * btn_mulu
         * */
        iv_splb_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sKeywosrd = et_splb.getText().toString();

                dialogin("");
                qingqiu(sId, iPage, et_fenlei_dialog_start.getText().toString(), et_fenlei_dialog_end.getText().toString(),
                        Order, sKeywosrd, sBrand_Id);
            }
        });
        tv_splb_zonghe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogin("");
                Order = "1";
                iPage = 1;
                qingqiu(sId, iPage, et_fenlei_dialog_start.getText().toString(), et_fenlei_dialog_end.getText().toString(),
                        Order, sKeywosrd, sBrand_Id);

                tv_splb_zonghe.setTextColor(tv_splb_zonghe.getResources().getColor(R.color.black));
                tv_splb_xlpm.setTextColor(tv_splb_xlpm.getResources().getColor(R.color.gray));
                tv_splb_zkyh.setTextColor(tv_splb_zkyh.getResources().getColor(R.color.gray));
            }
        });
        tv_splb_xlpm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogin("");
                Order = "5";
                iPage = 1;
                tv_splb_zonghe.setTextColor(tv_splb_zonghe.getResources().getColor(R.color.gray));
                tv_splb_xlpm.setTextColor(tv_splb_xlpm.getResources().getColor(R.color.black));
                tv_splb_zkyh.setTextColor(tv_splb_zkyh.getResources().getColor(R.color.gray));
                qingqiu(sId, iPage, et_fenlei_dialog_start.getText().toString(), et_fenlei_dialog_end.getText().toString(),
                        Order, sKeywosrd, sBrand_Id);
            }
        });
        tv_splb_zkyh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogin("");
                Order = "6";
                iPage = 1;
                tv_splb_zonghe.setTextColor(tv_splb_zonghe.getResources().getColor(R.color.gray));
                tv_splb_xlpm.setTextColor(tv_splb_xlpm.getResources().getColor(R.color.black));
                tv_splb_zkyh.setTextColor(tv_splb_zkyh.getResources().getColor(R.color.gray));
                tv_splb_zkyh.setTextColor(tv_splb_zkyh.getResources().getColor(R.color.black));
                qingqiu(sId, iPage, et_fenlei_dialog_start.getText().toString(), et_fenlei_dialog_end.getText().toString(),
                        Order, sKeywosrd, sBrand_Id);
            }
        });
   /*     mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String ItemId = list.get(i).get("tv_id").toString();
                dialogin("");
                jianjei(ItemId);
            }
        });*/

        iv_splb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
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
                qingqiu(sId, iPage, et_fenlei_dialog_start.getText().toString(), et_fenlei_dialog_end.getText().toString(),
                        Order, sKeywosrd, sBrand_Id);
            }

            // 上拉加载跟多
            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                Log.e("TAG", "onPullUpToRefresh"); // Do work to refresh
                iPage += 1;
                qingqiu(sId, iPage, et_fenlei_dialog_start.getText().toString(), et_fenlei_dialog_end.getText().toString(),
                        Order, sKeywosrd, sBrand_Id);

                // 页数
                //   p = p + 1;
                // AsyncTask异步交互加载数据
                // new GetDataTask2().execute(URL + p);
            }
        });
        ll_splb_sx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        et_splb.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO) {
                    String sQueryText = et_splb.getText().toString();
                    sKeywosrd = sQueryText;
                    if (sQueryText.equals("")) {
                    } else {
                        dialogin("");
                        Order = "1";
                        qingqiu(sId, iPage, et_fenlei_dialog_start.getText().toString(), et_fenlei_dialog_end.getText().toString(),
                                Order, sKeywosrd, sBrand_Id);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    }
                }
                return false;
            }
        });

        fl();
        dialogin("");
        Order = "1";
        qingqiu(sId, iPage, et_fenlei_dialog_start.getText().toString(), et_fenlei_dialog_end.getText().toString(),
                Order, sKeywosrd, sBrand_Id);

        ll_splb_xsfs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (iLb_Type == 0) {
                    iLb_Type = 1;
                    iv_splb_xsfs.setImageResource(R.drawable.btn_mulu_hover);
                    mPullRefreshListView.getRefreshableView().setNumColumns(1);
                } else if (iLb_Type == 1) {
                    iLb_Type = 0;
                    iv_splb_xsfs.setImageResource(R.drawable.btn_mulu);
                    mPullRefreshListView.getRefreshableView().setNumColumns(2);
                }
                myAdapterSplb.notifyDataSetChanged();
            }
        });

    }

    /**
     * 弹出分类框
     */
    public void fl() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(this).inflate(R.layout.fenlei_dialog, null);
 /*       lv_dialog = (ListView) inflate.findViewById(R.id.lv_dialog);*/
        ImageView iv_fenlei_gb = inflate.findViewById(R.id.iv_fenlei_gb);
        et_fenlei_dialog_start = inflate.findViewById(R.id.et_fenlei_dialog_start);
        et_fenlei_dialog_end = inflate.findViewById(R.id.et_fenlei_dialog_end);
        mgv_fenlei_dialog_pinpai = inflate.findViewById(R.id.mgv_fenlei_dialog_pinpai);
        TextView tv_fenlei_dialog_qr = inflate.findViewById(R.id.tv_fenlei_dialog_qr);
        TextView tv_fenlei_dialog_cz = inflate.findViewById(R.id.tv_fenlei_dialog_cz);
        iv_fenlei_gb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.RIGHT);
        //  WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() - 100);//设置宽度
        lp.height = (int) (display.getHeight());
        dialog.getWindow().setAttributes(lp);

        tv_fenlei_dialog_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialogin("");
                iPage = 1;
                qingqiu(sId, iPage, et_fenlei_dialog_start.getText().toString(), et_fenlei_dialog_end.getText().toString(),
                        Order, sKeywosrd, sBrand_Id);
            }
        });
        tv_fenlei_dialog_cz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sBrand_Id = "";
                et_fenlei_dialog_start.setText("");
                et_fenlei_dialog_end.setText("");
                //myAdapterPp.notifyDataSetChanged();
            }
        });
    }

    /**
     * 品牌
     */
    private class MyAdapterPp extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;


        public MyAdapterPp(Context context) {
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
                view = inflater.inflate(R.layout.pinpai_item, null);
            }
            TextView tv_pinpai_item = view.findViewById(R.id.tv_pinpai_item);
            tv_pinpai_item.setText(arrlist.get(position).get("name"));
            tv_pinpai_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sBrand_Id = arrlist.get(position).get("id");
                    notifyDataSetChanged();
                }
            });
            if (sBrand_Id.equals(arrlist.get(position).get("id"))) {
                tv_pinpai_item.setTextColor(tv_pinpai_item.getResources().getColor(R.color.white));
                tv_pinpai_item.setBackgroundResource(R.drawable.bj_lan2);
            } else {
                tv_pinpai_item.setTextColor(tv_pinpai_item.getResources().getColor(R.color.black));
                tv_pinpai_item.setBackgroundResource(R.drawable.bj_f4f4f4_2);
            }
            return view;
        }
    }

    private void qingqiu(final String sCatId, final int sPage, final String sStart, final String sEnd,
                         final String sOrder, final String sKeyword, final String sBrand_Id) {
        String url = Api.sUrl + "Api/Good/goodslist/appId/" + Api.sApp_Id
                + "/page/" + sPage + "/ordertype/" + sOrder;
        if (sCatId == null) {
        } else if (!sCatId.equals("")) {
            url = url + "/goods_one_type_id/" + sCatId;
        }
        if (!sStart.equals("")) {
            url = url + "/min/" + sStart;
        }
        if (!sEnd.equals("")) {
            url = url + "/max/" + sEnd;
        }
        if (!sKeyword.equals("")) {
            url = url + "/keyword/" + sKeyword;
        }
        if (!sBrand_Id.equals("")) {
            url = url + "/brand_id/" + sBrand_Id;
        }
        if (sEr_id == null) {
        } else if (!sEr_id.equals("")) {
            url = url + "/goods_type_id/" + sEr_id;
        }
        if (sSan_id == null) {
        } else if (!sSan_id.equals("")) {
            url = url + "/goods_two_type_id/" + sSan_id;
        }
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
                    ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();

                    if (resultCode > 0) {
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            String resultId = jsonObject.getString("id");
                            String resultName = jsonObject.getString("name");
                            String resultPrice = jsonObject.getString("price");
                            String resultLogo = jsonObject.getString("logo");
                            String resultShangjianame = jsonObject.getString("shangjianame");
                            String resultBuy_count = jsonObject.getString("buy_count");
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("id", resultId);
                            map.put("name", resultName);
                            map.put("price", resultPrice);
                            map.put("logo", resultLogo);
                            map.put("shangjianame", resultShangjianame);
                            map.put("buy_count", resultBuy_count);
                            mylist.add(map);
                        }
                        ll_splb_kong.setVisibility(View.GONE);
                        if (iPage == 1) {
                            if (mylist.size() == 0) {
                                ll_splb_kong.setVisibility(View.VISIBLE);
                            }
                            gridviewdata(mylist);
                        } else {
                            gridviewdata1(mylist);
                        }

                    } else {
                        if (iPage == 1) {
                            if (mylist.size() == 0) {
                                ll_splb_kong.setVisibility(View.VISIBLE);
                            }
                            gridviewdata(mylist);
                        } else {
                            gridviewdata1(mylist);
                        }
                        if (iPage > 1) {
                            iPage -= 1;
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
        request.setShouldCache(false);
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
                        Intent intent1 = new Intent(SplbActivity.this, SpxqActivity.class);
                        intent1.putExtra("goods_id", pid);
                        intent1.putExtra("data", sDate);
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
        request.setShouldCache(false);
        queue.add(request);
    }

    private void gridviewdata(ArrayList<HashMap<String, Object>> myList) {
        // iPage += 1;

        myAdapterSplb = new MyAdapterSplb(this);
        myAdapterSplb.arrlist = myList;
        mPullRefreshListView.setAdapter(myAdapterSplb);
        // 刷新适配器
        myAdapterSplb.notifyDataSetChanged();
        // 关闭刷新下拉
        mPullRefreshListView.onRefreshComplete();

    }

    private void gridviewdata1(ArrayList<HashMap<String, Object>> myList) {
        //    myList = getMenuAdapter();
        //iPage += 1;

        // 刷新适配器
        myAdapterSplb.arrlist.addAll(myList);
        myAdapterSplb.notifyDataSetChanged();
        // Call onRefreshComplete when the list has been refreshed.
        // 关闭上拉加载
        mPullRefreshListView.onRefreshComplete();

    }

    private void back() {
        finish();
    }

    /**
     *  * 方法名：onKeyDown(int keyCode, KeyEvent event)
     *  * 功    能：菜单栏点击事件
     *  * 参    数：int keyCode, KeyEvent event
     *  * 返回值：无
     */
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
        loadingDialog = new LoadingDialog(SplbActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(SplbActivity.this, R.style.dialog, sHint, type, true).show();
    }


    /**
     * 商品列表
     */
    private class MyAdapterSplb extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, Object>> arrlist;


        public MyAdapterSplb(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arrlist= new ArrayList<HashMap<String, Object>>();
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
            if (iLb_Type == 0) {
                view = inflater.inflate(R.layout.splb_item, null);

                LinearLayout ll_splb_item = view.findViewById(R.id.ll_splb_item);
                ImageView iv_splb = view.findViewById(R.id.iv_splb);
                TextView tv_splb = view.findViewById(R.id.tv_splb);
                TextView tv_splb_q = view.findViewById(R.id.tv_splb_q);
                TextView tv_splb_shangjianame = view.findViewById(R.id.tv_splb_shangjianame);
                Glide.with(SplbActivity.this).load( Api.sUrl+ arrlist.get(position).get("logo").toString())
                        .asBitmap()
                        .dontAnimate()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .into(iv_splb);
                tv_splb.setText(arrlist.get(position).get("name").toString());
                tv_splb_q.setText(arrlist.get(position).get("price").toString());
                ll_splb_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogin("");
                        jianjei(arrlist.get(position).get("id").toString());
                    }
                });
                tv_splb_shangjianame.setText(arrlist.get(position).get("shangjianame").toString());
            } else if (iLb_Type == 1) {
                view = inflater.inflate(R.layout.splb_item_1, null);
                LinearLayout ll_splb_item_1 = view.findViewById(R.id.ll_splb_item_1);
                ll_splb_item_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogin("");
                        jianjei(arrlist.get(position).get("id").toString());
                    }
                });
                ImageView iv_splb_1 = view.findViewById(R.id.iv_splb_1);
                Glide.with(SplbActivity.this).load( Api.sUrl+ arrlist.get(position).get("logo").toString())
                        .asBitmap()
                        .dontAnimate()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .into(iv_splb_1);
                TextView tv_splb_1 = view.findViewById(R.id.tv_splb_1);
                tv_splb_1.setText(arrlist.get(position).get("name").toString());
                TextView tv_splb_q_1 = view.findViewById(R.id.tv_splb_q_1);
                tv_splb_q_1.setText(arrlist.get(position).get("price").toString());
                TextView tv_splb_itenm_1_name = view.findViewById(R.id.tv_splb_itenm_1_name);
                tv_splb_itenm_1_name.setText(arrlist.get(position).get("shangjianame").toString());
                TextView tv_splb_xl_1 = view.findViewById(R.id.tv_splb_xl_1);
                tv_splb_xl_1.setText("销量" + arrlist.get(position).get("buy_count").toString());

            }
            return view;
        }
    }
}
