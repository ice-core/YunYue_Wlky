package com.example.administrator.yunyue.sc_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.data.TxddGridData;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc.SwipeBean;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GwcActivity extends AppCompatActivity {
    private ListView lv_shopping;
    private List<SwipeBean> mDatas;


    RequestQueue queue = null;
    private SharedPreferences pref;
    private String sUser_id;
    public MyAdapter adapter;
    private TextView tv_gouwuche_amount;
    private CheckBox cb_gouwuche_amount;

    private LinearLayout ll_shopping_heji;
    private TextView tv_shopping_bianji;
    /**
     * 提交订单
     */
    private Button bt_shopp_jiesuan;
    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
    public ArrayList<String> arrRid = new ArrayList<String>();

    private List<TxddGridData> mList = new ArrayList<>();
    /**
     * 编辑
     * 移入收藏
     * 删除
     */
    private LinearLayout ll_shopping_bj;
    /**
     * 删除
     */
    private TextView tv_shopp_delete;

    /**
     * 列表定位
     */
    int lv_dingwei = 0;
    private LinearLayout ll_gwc_kong;

    private TextView tv_gwc_shoucang;
    /**
     * 返回
     */
    private LinearLayout ll_gwc_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_gwc);
        pref = PreferenceManager.getDefaultSharedPreferences(GwcActivity.this);
        sUser_id = pref.getString("user_id", "");
        queue = Volley.newRequestQueue(GwcActivity.this);
        lv_shopping = (ListView) findViewById(R.id.lv_shopping);
        tv_gouwuche_amount = (TextView) findViewById(R.id.tv_gouwuche_amount);
        cb_gouwuche_amount = (CheckBox) findViewById(R.id.cb_gouwuche_amount);

        ll_shopping_heji = (LinearLayout) findViewById(R.id.ll_shopping_heji);
        bt_shopp_jiesuan = (Button) findViewById(R.id.bt_shopp_jiesuan);
        tv_shopp_delete = findViewById(R.id.tv_shopp_delete);
        tv_shopping_bianji = (TextView) findViewById(R.id.tv_shopping_bianji);
        tv_gwc_shoucang = findViewById(R.id.tv_gwc_shoucang);
        tv_gwc_shoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list_good_item_id_shoucang();
            }
        });
        ll_gwc_back = findViewById(R.id.ll_gwc_back);
        ll_gwc_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ll_shopping_bj = findViewById(R.id.ll_shopping_bj);
        ll_gwc_kong = findViewById(R.id.ll_gwc_kong);
        cb_gouwuche_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_gouwuche_amount.isChecked()) {
                    for (int i = 0; i < adapter.arrlist.size(); i++) {
                        adapter.arrlist.get(i).put("ItemStorename_Cb", "1");
                        adapter.arrlist.get(i).put("ItemGoods_Cb", "1");
                    }
                } else {
                    for (int i = 0; i < adapter.arrlist.size(); i++) {
                        adapter.arrlist.get(i).put("ItemStorename_Cb", "0");
                        adapter.arrlist.get(i).put("ItemGoods_Cb", "0");
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        tv_shopping_bianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_shopping_bianji.getText().toString().equals("编辑")) {
                    tv_shopping_bianji.setText("完成");
                    ll_shopping_heji.setVisibility(View.GONE);
                    ll_shopping_bj.setVisibility(View.VISIBLE);

                } else {
                    tv_shopping_bianji.setText("编辑");
                    ll_shopping_bj.setVisibility(View.GONE);
                    ll_shopping_heji.setVisibility(View.VISIBLE);
                }
            }
        });

        bt_shopp_jiesuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txdd();
            }
        });
        tv_shopp_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list_good_item_id();
            }
        });
    }

    /**
     * 选中商品集合
     */
    private void list_good_item_id() {
        String cart_id = "";
        for (int i = 0; i < adapter.arrlist.size(); i++) {
            if (adapter.arrlist.get(i).get("ItemGoods_Cb").equals("1")) {
                if (cart_id.equals("")) {
                    cart_id = adapter.arrlist.get(i).get("ItemId");
                } else {
                    cart_id = cart_id + "-" + adapter.arrlist.get(i).get("ItemId");
                }
            }
        }
        if (cart_id.equals("")) {
            Hint("请选择商品", HintDialog.WARM);
        } else {
            hideDialogin();
            dialogin("");
            delete(cart_id);
        }
    }

    /**
     * 选中商品集合
     */
    private void list_good_item_id_shoucang() {
        String cart_id = "";
        for (int i = 0; i < adapter.arrlist.size(); i++) {
            if (adapter.arrlist.get(i).get("ItemGoods_Cb").equals("1")) {
                if (cart_id.equals("")) {
                    cart_id = adapter.arrlist.get(i).get("ItemGoods_Id");
                } else {
                    cart_id = cart_id + "," + adapter.arrlist.get(i).get("ItemGoods_Id");
                }
            }
        }
        if (cart_id.equals("")) {
            Hint("请选择商品", HintDialog.WARM);
        } else {
            hideDialogin();
            dialogin("");
            spsc(cart_id);
        }
    }


    private void txdd() {
        ArrayList<HashMap<String, String>> arrlistxz = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < adapter.arrlist.size(); i++) {
            if (adapter.arrlist.get(i).get("ItemGoods_Cb").equals("1")) {
                arrlistxz.add(adapter.arrlist.get(i));
            }
        }
        mList = new ArrayList<>();
        for (int i = 0; i < arrlistxz.size(); i++) {
            TxddGridData txddGridData = new TxddGridData();
            if (i == 0) {
                txddGridData.sName_id = arrlistxz.get(i).get("ItemShangjia_Id");
                txddGridData.sName = arrlistxz.get(i).get("ItemStorename");
            } else {
                if (mList.get(mList.size() - 1).sName_id.equals(arrlistxz.get(i).get("ItemShangjia_Id"))) {
                    continue;
                } else {
                    txddGridData.sName_id = arrlistxz.get(i).get("ItemShangjia_Id");
                    txddGridData.sName = arrlistxz.get(i).get("ItemStorename");
                }
            }
            mList.add(txddGridData);
        }
        for (int i = 0; i < mList.size(); i++) {
            for (int a = 0; a < arrlistxz.size(); a++) {
                if (arrlistxz.get(a).get("ItemShangjia_Id").equals(mList.get(i).sName_id)) {
                    mList.get(i).mylist.add(arrlistxz.get(a));

                }
            }
        }
        if (mList.size() == 0) {
            Hint("请选择商品", HintDialog.WARM);
        } else {
            Intent intent = new Intent(GwcActivity.this, TxddActivity.class);
            ZffsActivity.iIs_spxq = 1;
            TxddActivity.mList = mList;
            TxddActivity.sActivity = "gwc";
            TxddActivity.sAmount = tv_gouwuche_amount.getText().toString();
            startActivity(intent);
        }

    }

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initview();
    }

    private void initview() {
        tv_gouwuche_amount.setText("0");
        cb_gouwuche_amount.setChecked(false);
        tv_shopping_bianji.setText("编辑");
        ll_shopping_bj.setVisibility(View.GONE);
        ll_shopping_heji.setVisibility(View.VISIBLE);
        // dialogin("");
        huoqu();
    }


    /**
     * 商品收藏
     */
    private void spsc(String good_id) {
        String url = Api.sUrl + "Api/Good/shouchang/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/good_id/" + good_id;
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
                        tv_shopping_bianji.setText("编辑");
                        ll_shopping_bj.setVisibility(View.GONE);
                        ll_shopping_heji.setVisibility(View.VISIBLE);
                        Hint(resultMsg, HintDialog.SUCCESS);
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

    private void huoqu() {
        String url = Api.sUrl + "Api/Order/cartlist/appId/" + Api.sApp_Id + "/user_id/" + sUser_id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    String resultMsg = jsonObject.getString("msg");
                    int resultCode = jsonObject.getInt("code");
                    mylist = new ArrayList<HashMap<String, String>>();
                    adapter = new MyAdapter(GwcActivity.this);
                    mDatas = new ArrayList<>();
                    if (resultCode > 0) {
                        JSONArray resultJsonArraydataList = jsonObject.getJSONArray("data");
                        for (int i = 0; i < resultJsonArraydataList.length(); i++) {
                            JSONObject jsonObjectdataList = resultJsonArraydataList.getJSONObject(i);
                            JSONArray resultJsonArraycartlist = jsonObjectdataList.getJSONArray("list");
                            for (int a = 0; a < resultJsonArraycartlist.length(); a++) {
                                JSONObject jsonObjectcartlist = resultJsonArraycartlist.getJSONObject(a);
                                //购物车id
                                String resultId = jsonObjectcartlist.getString("id");
                                //商品id
                                String resultGoods_Id = jsonObjectcartlist.getString("good_id");
                                //商品名称
                                String resultGoods_Name = jsonObjectcartlist.getString("good_name");
                                //市场价
                                String resultMarket_price = jsonObjectcartlist.getString("market_price");
                                //购买价
                                String resultGoods_Price = jsonObjectcartlist.getString("goods_price");
                                //数量
                                String resultGoods_Num = jsonObjectcartlist.getString("goods_num");
                                //商品图片
                                String resultGood_logo = jsonObjectcartlist.getString("good_logo");
                                //商家id
                                String resultShangjia_Id = jsonObjectcartlist.getString("shangjia_id");
                                //商家名称
                                String resultStorename = jsonObjectdataList.getString("shangjianname");
                                //规格
                                String resultGuige = jsonObjectcartlist.getString("guige");
                                HashMap<String, String> map = new HashMap<String, String>();

                                map.put("ItemId", resultId);
                                map.put("ItemGoods_Id", resultGoods_Id);
                                map.put("ItemGoods_Name", resultGoods_Name);
                                map.put("ItemMarket_price", resultMarket_price);
                                map.put("ItemGoods_Price", resultGoods_Price);
                                map.put("ItemGoods_Num", resultGoods_Num);
                                map.put("ItemGood_logo", resultGood_logo);
                                map.put("ItemShangjia_Id", resultShangjia_Id);
                                map.put("ItemStorename", resultStorename);
                                map.put("ItemStorename_Cb", "0");
                                map.put("ItemGoods_Cb", "0");
                                map.put("ItemGuige", resultGuige);
                                mylist.add(map);
                            }
                        }
                        adapter.arrlist = mylist;
                        lv_shopping.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        if (lv_dingwei == 0) {
                        } else {
                            lv_shopping.setSelection(lv_dingwei);
                        }
                        lv_dingwei = 0;
                        ll_gwc_kong.setVisibility(View.GONE);
                        if (mylist.size() == 0) {
                            ll_gwc_kong.setVisibility(View.VISIBLE);
                        }
                    } else {
                        tv_gouwuche_amount.setText("0.0");
                        if (mylist.size() == 0) {
                            ll_gwc_kong.setVisibility(View.VISIBLE);
                        }
                        lv_shopping.setAdapter(adapter);
                        //   Hint(resultMsg, HintDialog.ERROR);
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


    public View getView(int position, View convertView, ViewGroup Parent) {

        return convertView;
    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(GwcActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(GwcActivity.this, R.style.dialog, sHint, type, true).show();
    }

    /**
     * 删除购物车
     */
    private void delete(String cartid) {
        String url = Api.sUrl + "Api/Order/cartdel/appId/" + Api.sApp_Id + "/cart_id/" + cartid;
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
                        hideDialogin();
                        dialogin("");
                        huoqu();
                        Hint(resultMsg, HintDialog.SUCCESS);
                        cb_gouwuche_amount.setChecked(false);
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
     * 商品数量加减
     */
    private void jia_jian(String cartid, String goods_num) {
        String url = Api.sUrl + "Api/Order/cartedit/appId/" + Api.sApp_Id
                + "/cart_id/" + cartid + "/goods_num/" + goods_num;
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
                        hideDialogin();
                        dialogin("");
                        huoqu();
                        Hint(resultMsg, HintDialog.SUCCESS);
                        cb_gouwuche_amount.setChecked(false);
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
     * 商品详情
     */
    private void jianjei(final String pid) {
        String url = Api.sUrl + "Api/Good/gooddetail/appId/" + Api.sApp_Id
                + "/good_id/" + pid + "/user_id/" + sUser_id;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        Intent intent1 = new Intent(GwcActivity.this, SpxqActivity.class);
                        intent1.putExtra("goods_id", pid);
                        intent1.putExtra("data", sDate);
                        intent1.putExtra("activty", "main");
                        startActivity(intent1);
                    } else {
                        Hint(resultMsg, HintDialog.ERROR);
                    }
                    hideDialogin();
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
                view = inflater.inflate(R.layout.gouwuche_item, null);
            }
            LinearLayout ll_gwc_sp = view.findViewById(R.id.ll_gwc_sp);
            LinearLayout ll_gouwuche_item_storename = view.findViewById(R.id.ll_gouwuche_item_storename);
            TextView tv_gouwuche_item_yj = view.findViewById(R.id.tv_gouwuche_item_yj);
            tv_gouwuche_item_yj.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tv_gouwuche_item_yj.setText(arrlist.get(position).get("ItemMarket_price"));
            TextView tv_gouwuche_item_storename = view.findViewById(R.id.tv_gouwuche_item_storename);
            tv_gouwuche_item_storename.setText(arrlist.get(position).get("ItemStorename"));
            TextView tv_gouwuche_item_goods_name = view.findViewById(R.id.tv_gouwuche_item_goods_name);
            tv_gouwuche_item_goods_name.setText(arrlist.get(position).get("ItemGoods_Name"));
            TextView tv_gouwuche_goods_price = view.findViewById(R.id.tv_gouwuche_goods_price);
            tv_gouwuche_goods_price.setText(arrlist.get(position).get("ItemGoods_Price"));
            TextView tv_gouwuche_goods_num = view.findViewById(R.id.tv_gouwuche_goods_num);
            tv_gouwuche_goods_num.setText(arrlist.get(position).get("ItemGoods_Num"));
            ImageView iv_gouwuche_item_original_img = view.findViewById(R.id.iv_gouwuche_item_original_img);
            ImageView iv_gouwuche_item_jia = view.findViewById(R.id.iv_gouwuche_item_jia);
            TextView tv_gouwuche_item_goods_guige = view.findViewById(R.id.tv_gouwuche_item_goods_guige);
            tv_gouwuche_item_goods_guige.setText(arrlist.get(position).get("ItemGuige"));
            ImageView iv_gouwuche_item_jian = view.findViewById(R.id.iv_gouwuche_item_jian);
            if (position == 0) {
                notifyDataSetChanged();
            }
            ll_gwc_sp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideDialogin();
                    dialogin("");
                    jianjei(arrlist.get(position).get("ItemGoods_Id"));
                }
            });
            tv_gouwuche_item_storename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GwcActivity.this, DpzyActivity.class);
                    intent.putExtra("id", arrlist.get(position).get("ItemShangjia_Id"));
                    startActivity(intent);
                }
            });
            iv_gouwuche_item_jia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lv_dingwei = position;
                    int num = 0;
                    if (Integer.valueOf(arrlist.get(position).get("ItemGoods_Num")) > 1) {
                        num = Integer.valueOf(arrlist.get(position).get("ItemGoods_Num")) - 1;
                        hideDialogin();
                        dialogin("");
                        jia_jian(arrlist.get(position).get("ItemId"), String.valueOf(num));
                    }
                }
            });
            iv_gouwuche_item_jian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lv_dingwei = position;
                    int num = 0;
                    num = Integer.valueOf(arrlist.get(position).get("ItemGoods_Num")) + 1;
                    hideDialogin();
                    dialogin("");
                    jia_jian(arrlist.get(position).get("ItemId"), String.valueOf(num));
                }
            });
            Glide.with(GwcActivity.this)
                    .load( Api.sUrl+ arrlist.get(position).get("ItemGood_logo"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_gouwuche_item_original_img);
            final CheckBox cb_gouwuche_item_storename = view.findViewById(R.id.cb_gouwuche_item_storename);
            final CheckBox cb_gouwuche_item_goods = view.findViewById(R.id.cb_gouwuche_item_goods);
            if (position == 0) {
                ll_gouwuche_item_storename.setVisibility(View.VISIBLE);
            } else if (arrlist.get(position).get("ItemShangjia_Id").equals(arrlist.get(position - 1).get("ItemShangjia_Id"))) {
                ll_gouwuche_item_storename.setVisibility(View.GONE);
            } else {
                ll_gouwuche_item_storename.setVisibility(View.VISIBLE);
            }

            if (arrlist.get(position).get("ItemStorename_Cb").equals("0")) {
                cb_gouwuche_item_storename.setChecked(false);
            } else {
                cb_gouwuche_item_storename.setChecked(true);
            }
            if (arrlist.get(position).get("ItemGoods_Cb").equals("0")) {
                cb_gouwuche_item_goods.setChecked(false);
            } else {
                cb_gouwuche_item_goods.setChecked(true);
            }


            cb_gouwuche_item_storename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cb_gouwuche_item_storename.isChecked()) {
                        for (int i = 0; i < arrlist.size(); i++) {
                            if (arrlist.get(position).get("ItemShangjia_Id").equals(arrlist.get(i).get("ItemShangjia_Id"))) {
                                arrlist.get(i).put("ItemStorename_Cb", "1");
                                arrlist.get(i).put("ItemGoods_Cb", "1");
                            }
                        }
                    } else {
                        for (int i = 0; i < arrlist.size(); i++) {
                            if (arrlist.get(position).get("ItemShangjia_Id").equals(arrlist.get(i).get("ItemShangjia_Id"))) {
                                arrlist.get(i).put("ItemStorename_Cb", "0");
                                arrlist.get(i).put("ItemGoods_Cb", "0");
                            }
                        }
                    }

                    notifyDataSetChanged();
                }
            });
            cb_gouwuche_item_goods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cb_gouwuche_item_goods.isChecked()) {
                        arrlist.get(position).put("ItemGoods_Cb", "1");
                    } else {
                        arrlist.get(position).put("ItemGoods_Cb", "0");
                    }
                    /**
                     *同家商品是否全选
                     * 0-否，1-是
                     * */
                    int iQx_dp = 1;
                    for (int i = 0; i < arrlist.size(); i++) {
                        if (arrlist.get(position).get("ItemShangjia_Id").equals(arrlist.get(i).get("ItemShangjia_Id"))) {

                            if (arrlist.get(i).get("ItemGoods_Cb").equals("0")) {
                                iQx_dp = 0;
                            }
                        }
                    }
                    if (iQx_dp == 0) {
                        for (int i = 0; i < arrlist.size(); i++) {
                            if (arrlist.get(position).get("ItemShangjia_Id").equals(arrlist.get(i).get("ItemShangjia_Id"))) {
                                arrlist.get(i).put("ItemStorename_Cb", "0");
                            }
                        }
                    } else {
                        for (int i = 0; i < arrlist.size(); i++) {
                            if (arrlist.get(position).get("ItemShangjia_Id").equals(arrlist.get(i).get("ItemShangjia_Id"))) {
                                arrlist.get(i).put("ItemStorename_Cb", "1");
                            }
                        }
                    }
                    notifyDataSetChanged();
                }
            });
            /**
             *所有商品是否全选
             * 0-否，1-是
             * */
            int iQx = 1;
            for (int i = 0; i < arrlist.size(); i++) {
                if (arrlist.get(i).get("ItemStorename_Cb").equals("0")) {
                    iQx = 0;
                    break;
                }
            }
            if (iQx == 1) {
                cb_gouwuche_amount.setChecked(true);
            } else {
                cb_gouwuche_amount.setChecked(false);
            }
            /**
             * 总价
             * */
            double zongjia = 0.0;
            for (int i = 0; i < arrlist.size(); i++) {
                if (arrlist.get(i).get("ItemGoods_Cb").equals("1")) {
                    zongjia = add(zongjia, mul(Double.parseDouble(arrlist.get(i).get("ItemGoods_Price")), Integer.valueOf(arrlist.get(i).get("ItemGoods_Num"))));
                    //zongjia += Double.parseDouble(arrlist.get(i).get("ItemGoods_Price")) * Integer.valueOf(arrlist.get(i).get("ItemGoods_Num"));
                }
            }
            tv_gouwuche_amount.setText(String.valueOf(zongjia));

            return view;
        }
    }

    /**
     * 相加
     */
    public static double add(double a1, double b1) {
        BigDecimal a2 = new BigDecimal(Double.toString(a1));
        BigDecimal b2 = new BigDecimal(Double.toString(b1));
        return a2.add(b2).doubleValue();
    }

    /**
     * 相乘
     */
    public static double mul(double a1, double b1) {
        BigDecimal a2 = new BigDecimal(Double.toString(a1));
        BigDecimal b2 = new BigDecimal(Double.toString(b1));
        return a2.multiply(b2).doubleValue();
    }

}
