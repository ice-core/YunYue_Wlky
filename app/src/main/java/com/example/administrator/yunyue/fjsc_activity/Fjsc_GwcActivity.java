package com.example.administrator.yunyue.fjsc_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
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
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.Jjcc;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fjsc_GwcActivity extends AppCompatActivity {
    private static final String TAG = Fjsc_GwcActivity.class.getSimpleName();
    private GridView gv_fjsc_gwc;
    private LinearLayout ll_fjsc_gwc_sc;
    private TextView tv_fjsc_gwc_bj;
    RequestQueue queue = null;
    private SharedPreferences pref;
    private String sUser_id = "";
    private CheckBox cb_fjsc_gwc_qb;
    private MyAdapter myAdapter;
    private boolean cbdp = false;
    private TextView tv_fjsc_gwc_sc;
    private ImageView iv_fjsc_gwc_back;
    private LinearLayout ll_shopping_kong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_fjsc__gwc);
        pref = PreferenceManager.getDefaultSharedPreferences(Fjsc_GwcActivity.this);
        sUser_id = pref.getString("user_id", "");
        queue = Volley.newRequestQueue(Fjsc_GwcActivity.this);
        gv_fjsc_gwc = findViewById(R.id.gv_fjsc_gwc);
        ll_fjsc_gwc_sc = findViewById(R.id.ll_fjsc_gwc_sc);
        tv_fjsc_gwc_bj = findViewById(R.id.tv_fjsc_gwc_bj);
        cb_fjsc_gwc_qb = findViewById(R.id.cb_fjsc_gwc_qb);
        tv_fjsc_gwc_sc = findViewById(R.id.tv_fjsc_gwc_sc);
        iv_fjsc_gwc_back = findViewById(R.id.iv_fjsc_gwc_back);
        ll_shopping_kong = findViewById(R.id.ll_shopping_kong);
        tv_fjsc_gwc_bj.setEnabled(false);
        tv_fjsc_gwc_bj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_fjsc_gwc_bj.getText().equals("编辑")) {
                    tv_fjsc_gwc_bj.setText("取消");
                    ll_fjsc_gwc_sc.setVisibility(View.VISIBLE);
                    cbdp = true;
                } else {
                    tv_fjsc_gwc_bj.setText("编辑");
                    ll_fjsc_gwc_sc.setVisibility(View.GONE);
                    cbdp = false;
                }
                myAdapter.notifyDataSetChanged();
            }
        });
        cb_fjsc_gwc_qb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_fjsc_gwc_qb.isChecked()) {
                    for (int i = 0; i < myAdapter.arrDp_Cb.size(); i++) {
                        myAdapter.arrDp_Cb.set(i, "1");
                        myAdapter.arrSp_Cb.set(i, "1");
                    }
                } else {
                    for (int i = 0; i < myAdapter.arrDp_Cb.size(); i++) {
                        myAdapter.arrDp_Cb.set(i, "0");
                        myAdapter.arrSp_Cb.set(i, "0");
                    }
                }
                myAdapter.notifyDataSetChanged();
            }
        });
        tv_fjsc_gwc_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cart_id = "";
                for (int i = 0; myAdapter.arrSp_Cb.size() > i; i++) {
                    if (myAdapter.arrSp_Cb.get(i).equals("1")) {
                        if (cart_id.equals("")) {
                            cart_id = myAdapter.arrmylist.get(i).get("ItemId");
                        } else {
                            cart_id = cart_id + "-" + myAdapter.arrmylist.get(i).get("ItemId");
                        }
                    }
                }
                dialogin("");
                sCartdel(cart_id);
            }
        });
        iv_fjsc_gwc_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        dialogin("");
        sCartlist();
    }

    /**
     * 方法名：sCartdel()
     * 功  能：删除购物车
     * 参  数：appId
     */
    private void sCartdel(String cart_id) {
        String url = Api.sUrl + Api.sCartdel;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("cart_id", cart_id);
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
                                Hint(resultMsg, HintDialog.SUCCESS);
                                dialogin("");
                                sCartlist();
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
     * 方法名：sCartlist()
     * 功  能：购物车列表
     * 参  数：appId
     * shangjia_id--商家ID
     */
    private void sCartlist() {
        String url = Api.sUrl + Api.sCartlist;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        // params.put("shangjia_id", sShangjia_id);
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

                            int num = 0;
                            myAdapter = new MyAdapter(Fjsc_GwcActivity.this);
                            if (resultCode > 0) {
                                tv_fjsc_gwc_bj.setVisibility(View.VISIBLE);
                                ll_shopping_kong.setVisibility(View.GONE);
                                tv_fjsc_gwc_bj.setEnabled(true);
                                JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                                ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < resultJsonArray.length(); i++) {
                                    JSONObject jsonObject = resultJsonArray.getJSONObject(i);
                                    // String resultMoneysum = jsonObject.getString("moneysum");
                                    double price = 0;
                                    JSONArray resultJsonArrayCartlist = jsonObject.getJSONArray("list");
                                    for (int a = 0; a < resultJsonArrayCartlist.length(); a++) {
                                        HashMap<String, String> map = new HashMap<String, String>();
                                        JSONObject jsonObjectCartlist = resultJsonArrayCartlist.getJSONObject(a);
                                        //购物车id
                                        String resultId = jsonObjectCartlist.getString("id");
                                        String resultRname = jsonObjectCartlist.getString("shangjia_name");
                                        String resultRid = jsonObject.getString("id");
                                        String resultOriginalImg = jsonObjectCartlist.getString("good_logo");
                                        String resultGoodsId = jsonObjectCartlist.getString("good_id");
                                        String resultGoodsName = jsonObjectCartlist.getString("good_name");
                                        String resultGoodsPrice = jsonObjectCartlist.getString("goods_price");
                                        String resultGoodsNum = jsonObjectCartlist.getString("goods_num");
                                        String resultSpecKeyName = jsonObjectCartlist.getString("guige");
                                        String resultItemId = jsonObjectCartlist.getString("item_id");
                                        price = Jjcc.add(price, Jjcc.mul(Double.parseDouble(resultGoodsPrice), Integer.valueOf(resultGoodsNum)));
                                        map.put("ItemId", resultId);
                                        map.put("ItemRid", resultRid);
                                        map.put("ItemRname", resultRname);
                                        map.put("ItemGoodsId", resultGoodsId);
                                        map.put("ItemGoodsName", resultGoodsName);
                                        map.put("ItemGoodsPrice", resultGoodsPrice);
                                        map.put("ItemGoodsNum", resultGoodsNum);
                                        map.put("ItemSpecKeyName", resultSpecKeyName);
                                        map.put("ItemItemId", resultItemId);
                                        map.put("ItemOriginalImg", resultOriginalImg);
                                        map.put("ItemMoneysum", String.valueOf(price));
                                        mylist.add(map);
                                        myAdapter.arrDp_Cb.add("0");
                                        myAdapter.arrSp_Cb.add("0");
                                    }

                                }
                                myAdapter.arrmylist = mylist;
                                gv_fjsc_gwc.setAdapter(myAdapter);

                            } else {
                                tv_fjsc_gwc_bj.setVisibility(View.GONE);
                                gv_fjsc_gwc.setAdapter(myAdapter);
                                ll_shopping_kong.setVisibility(View.VISIBLE);
                                tv_fjsc_gwc_bj.setEnabled(false);
                                gv_fjsc_gwc.setAdapter(myAdapter);
                                tv_fjsc_gwc_bj.setText("编辑");
                                ll_fjsc_gwc_sc.setVisibility(View.GONE);
                                cbdp = false;
                            }
                            myAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            hideDialogin();
                            e.printStackTrace();
                        }
                        Log.d(TAG, "response -> " + response.toString());

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


    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater1;
        public ArrayList<HashMap<String, String>> arrmylist;
        public ArrayList<String> arrDp_Cb;
        public ArrayList<String> arrSp_Cb;


        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater1 = LayoutInflater.from(context);
            arrmylist = new ArrayList<HashMap<String, String>>();
            arrDp_Cb = new ArrayList<String>();
            arrSp_Cb = new ArrayList<String>();
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
                view = inflater1.inflate(R.layout.fjsc_gwc_item, null);

            }
            LinearLayout ll_fjsc_gwc_item_tou = view.findViewById(R.id.ll_fjsc_gwc_item_tou);
            final CheckBox cb_fjsc_gwc_item_dp = view.findViewById(R.id.cb_fjsc_gwc_item_dp);
            final CheckBox cb_fjsc_gwc_item_sp = view.findViewById(R.id.cb_fjsc_gwc_item_sp);
            TextView tv_fjsc_gwc_item_name = view.findViewById(R.id.tv_fjsc_gwc_item_name);
            ImageView iv_fjsc_gwc_item = view.findViewById(R.id.iv_fjsc_gwc_item);
            TextView tv_fjsc_gwc_item_goods_name = view.findViewById(R.id.tv_fjsc_gwc_item_goods_name);
            TextView tv_fjsc_gwc_item_goods_price = view.findViewById(R.id.tv_fjsc_gwc_item_goods_price);
            TextView tv_fjsc_gwc_item_goods_num = view.findViewById(R.id.tv_fjsc_gwc_item_goods_num);
            TextView tv_fjsc_gwc_item_moneysum = view.findViewById(R.id.tv_fjsc_gwc_item_moneysum);
            LinearLayout ll_fjsc_gwc_item_di = view.findViewById(R.id.ll_fjsc_gwc_item_di);
            TextView tv_fjsc_gwc_item_qjs = view.findViewById(R.id.tv_fjsc_gwc_item_qjs);
            tv_fjsc_gwc_item_name.setText(arrmylist.get(position).get("ItemRname"));
            tv_fjsc_gwc_item_goods_name.setText(arrmylist.get(position).get("ItemGoodsName"));
            tv_fjsc_gwc_item_goods_price.setText("￥" + arrmylist.get(position).get("ItemGoodsPrice"));
            tv_fjsc_gwc_item_goods_num.setText("x" + arrmylist.get(position).get("ItemGoodsNum"));
            tv_fjsc_gwc_item_moneysum.setText("￥" + arrmylist.get(position).get("ItemMoneysum"));
            Glide.with(Fjsc_GwcActivity.this).load( Api.sUrl+ arrmylist.get(position).get("ItemOriginalImg"))
                    .asBitmap()
                    .dontAnimate()
                    .into(iv_fjsc_gwc_item);

            if (arrDp_Cb.get(position).equals("0")) {
                cb_fjsc_gwc_item_dp.setChecked(false);
                if (arrSp_Cb.get(position).equals("0")) {
                    cb_fjsc_gwc_item_sp.setChecked(false);
                } else {
                    cb_fjsc_gwc_item_sp.setChecked(true);
                }
            } else {
                cb_fjsc_gwc_item_dp.setChecked(true);
                if (arrSp_Cb.get(position).equals("0")) {
                    cb_fjsc_gwc_item_sp.setChecked(false);
                } else {
                    cb_fjsc_gwc_item_sp.setChecked(true);
                }
            }

            /**
             * 编辑全选
             * */
            boolean cbqx = false;
            for (int i = 0; i < arrSp_Cb.size(); i++) {
                if (arrSp_Cb.get(i).equals("1")) {
                    cbqx = true;
                } else {
                    cbqx = false;
                    break;
                }
            }
            if (cbqx) {
                cb_fjsc_gwc_qb.setChecked(true);
            } else {
                cb_fjsc_gwc_qb.setChecked(false);
            }

            /**
             * 购物车item 显示
             * * */
            if (arrmylist.size() == 0) {
                ll_fjsc_gwc_item_tou.setVisibility(View.VISIBLE);
                ll_fjsc_gwc_item_di.setVisibility(View.VISIBLE);
            } else if (position < arrmylist.size() - 1) {
                if (arrmylist.get(position).get("ItemRid").equals(arrmylist.get(position + 1).get("ItemRid"))) {
                    if (position == 0) {
                        ll_fjsc_gwc_item_tou.setVisibility(View.VISIBLE);
                        ll_fjsc_gwc_item_di.setVisibility(View.GONE);
                    } else if (arrmylist.get(position).get("ItemRid").equals(arrmylist.get(position - 1).get("ItemRid"))) {
                        ll_fjsc_gwc_item_tou.setVisibility(View.GONE);
                        ll_fjsc_gwc_item_di.setVisibility(View.GONE);
                    } else {
                        ll_fjsc_gwc_item_tou.setVisibility(View.VISIBLE);
                        ll_fjsc_gwc_item_di.setVisibility(View.GONE);
                    }
                } else {
                    if (position == 0) {
                        ll_fjsc_gwc_item_tou.setVisibility(View.VISIBLE);
                        ll_fjsc_gwc_item_di.setVisibility(View.VISIBLE);
                    } else if (arrmylist.get(position - 1).get("ItemRid").equals(arrmylist.get(position).get("ItemRid"))) {
                        ll_fjsc_gwc_item_tou.setVisibility(View.GONE);
                        ll_fjsc_gwc_item_di.setVisibility(View.VISIBLE);
                    } else {
                        ll_fjsc_gwc_item_tou.setVisibility(View.VISIBLE);
                        ll_fjsc_gwc_item_di.setVisibility(View.VISIBLE);
                    }
                }
            } else if (position == arrmylist.size() - 1) {
                try {
                    if (arrmylist.get(position - 1).get("ItemRid").equals(arrmylist.get(position).get("ItemRid"))) {
                        ll_fjsc_gwc_item_tou.setVisibility(View.GONE);
                        ll_fjsc_gwc_item_di.setVisibility(View.VISIBLE);
                    } else {
                        ll_fjsc_gwc_item_tou.setVisibility(View.VISIBLE);
                        ll_fjsc_gwc_item_di.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    ll_fjsc_gwc_item_tou.setVisibility(View.VISIBLE);
                    ll_fjsc_gwc_item_di.setVisibility(View.VISIBLE);
                }
            }
            cb_fjsc_gwc_item_dp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cb_fjsc_gwc_item_dp.isChecked()) {
                        for (int i = 0; i < arrDp_Cb.size(); i++) {
                            if (arrmylist.get(position).get("ItemRid").equals(arrmylist.get(i).get("ItemRid"))) {
                                arrDp_Cb.set(i, "1");
                                arrSp_Cb.set(i, "1");
                            }
                        }
                    } else {
                        for (int i = 0; i < arrDp_Cb.size(); i++) {
                            if (arrmylist.get(position).get("ItemRid").equals(arrmylist.get(i).get("ItemRid"))) {
                                arrDp_Cb.set(i, "0");
                                arrSp_Cb.set(i, "0");
                            }
                        }
                    }
                    notifyDataSetChanged();
                }
            });
            cb_fjsc_gwc_item_sp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cb_fjsc_gwc_item_sp.isChecked()) {
                        arrSp_Cb.set(position, "1");
                        boolean dp = true;
                        for (int i = 0; i < arrSp_Cb.size(); i++) {
                            if (arrmylist.get(position).get("ItemRid").equals(arrmylist.get(i).get("ItemRid"))) {
                                if (arrSp_Cb.get(i).equals("0")) {
                                    dp = false;
                                }
                            }
                        }
                        if (dp) {
                            for (int i = 0; i < arrDp_Cb.size(); i++) {
                                if (arrmylist.get(position).get("ItemRid").equals(arrmylist.get(i).get("ItemRid"))) {
                                    arrDp_Cb.set(i, "1");
                                }
                            }
                        }

                    } else {
                        for (int i = 0; i < arrDp_Cb.size(); i++) {
                            if (arrmylist.get(position).get("ItemRid").equals(arrmylist.get(i).get("ItemRid"))) {
                                arrDp_Cb.set(i, "0");
                            }
                        }
                        arrSp_Cb.set(position, "0");
                    }
                    notifyDataSetChanged();
                }
            });
            if (cbdp) {
                cb_fjsc_gwc_item_dp.setVisibility(View.VISIBLE);
                cb_fjsc_gwc_item_sp.setVisibility(View.VISIBLE);
                ll_fjsc_gwc_item_di.setVisibility(View.GONE);
            } else {
                cb_fjsc_gwc_item_dp.setVisibility(View.GONE);
                cb_fjsc_gwc_item_sp.setVisibility(View.GONE);
                if (ll_fjsc_gwc_item_di.getVisibility() == View.VISIBLE) {
                    ll_fjsc_gwc_item_di.setVisibility(View.VISIBLE);
                }

            }
            tv_fjsc_gwc_item_qjs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Fjsc_GwcActivity.this, Fjsc_QrddActivity.class);
                    Fjsc_QrddActivity.sId = arrmylist.get(position).get("ItemRid");
                    Fjsc_QrddActivity.iDz = 0;
                    startActivity(intent);
                }
            });


            return view;
        }
    }


    private void back() {
        finish();
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
        loadingDialog = new LoadingDialog(Fjsc_GwcActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(Fjsc_GwcActivity.this, R.style.dialog, sHint, type, true).show();
    }

}
