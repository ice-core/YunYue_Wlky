package com.example.administrator.yunyue.sc_activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.MainActivity_Wlky;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.data.TxddGridData;
import com.example.administrator.yunyue.data.Txdd_Yhj_GridData;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TxddActivity_test extends AppCompatActivity {
    private static final String TAG = TxddActivity_test.class.getSimpleName();
    /**
     * 返回
     */
    private ImageView iv_txdd_back;
    /**
     * 订单列表
     */
    private MyGridView mgv_txdd;

    private ScrollView sv_txdd;
    private GridView gv_txdd_yhj_dialog;
    RequestQueue queue = null;
    private SharedPreferences pref;
    private String sUser_id;

    /**
     * 收货地址id
     */
    private String resultAddressId = "";
    /**
     * 收货人姓名
     */
    private TextView tv_txdd_consignee;
    /**
     * 收货人电话
     */
    private TextView tv_txdd_mobile;
    /**
     * 收货地址
     */
    private TextView tv_txdd_dizhi;
    private LinearLayout ll_txdd_shdz;
    /**
     * 收货地址
     */
    public static ArrayList<HashMap<String, String>> mylistShdz;
    public static String sId = "";

    public static List<TxddGridData> mList;


    public static String sActivity = "";

    public static String sAmount = "";

    private List<Txdd_Yhj_GridData> mList_yhj = new ArrayList<Txdd_Yhj_GridData>();

    private MyAdapter myAdapter;
    /**
     * 1-个人
     * 2-单位
     */
    int fp_type = 0;

    /**
     * 提交订单
     */
    private Button bt_txdd_tjdd;
    private Dialog dialog_yhj;
    private Dialog dialog_spqd;
    /**
     * 订单总额
     */
    private TextView tv_txdd_money;

    /**
     * 商品清单列表
     */
    private GridView gv_spqd_dialog;
    /**
     * 商品数量
     */
    private int num = 0;
    /**
     * 返回
     */
    private LinearLayout ll_qrdd_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_txdd);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        queue = Volley.newRequestQueue(TxddActivity_test.this);
        mgv_txdd = findViewById(R.id.mgv_txdd);
        sv_txdd = findViewById(R.id.sv_txdd);
        iv_txdd_back = findViewById(R.id.iv_txdd_back);
        tv_txdd_consignee = findViewById(R.id.tv_txdd_consignee);
        tv_txdd_mobile = findViewById(R.id.tv_txdd_mobile);
        tv_txdd_dizhi = findViewById(R.id.tv_txdd_dizhi);
        ll_txdd_shdz = findViewById(R.id.ll_txdd_shdz);
        bt_txdd_tjdd = findViewById(R.id.bt_txdd_tjdd);
        tv_txdd_money = findViewById(R.id.tv_txdd_money);
        ll_qrdd_back = findViewById(R.id.ll_qrdd_back);
        ll_qrdd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        initview();

    }


    private void back() {
        Intent intent = new Intent(TxddActivity_test.this, MainActivity_Wlky.class);
        intent.putExtra("ID", "2");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initview();
    }

    private void initview() {
        iv_txdd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // showCommentDailog();
        ll_txdd_shdz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TxddActivity_test.this, ShdzActivity.class);
                intent.putExtra("activity", "txdd");
                startActivity(intent);
                // finish();
            }
        });
        if (sId == null) {
            // dialogin("");
            huoqu();
        } else if (sId.equals("")) {
            //  dialogin("");
            huoqu();
        } else {

            resultAddressId = mylistShdz.get(Integer.valueOf(sId)).get("ItemId");
            tv_txdd_consignee.setText(mylistShdz.get(Integer.valueOf(sId)).get("ItemName"));
            tv_txdd_mobile.setText(mylistShdz.get(Integer.valueOf(sId)).get("ItemMobile"));
            tv_txdd_dizhi.setText(mylistShdz.get(Integer.valueOf(sId)).get("ItemSheng") + mylistShdz.get(Integer.valueOf(sId)).get("ItemShi") +
                    mylistShdz.get(Integer.valueOf(sId)).get("ItemQu") + mylistShdz.get(Integer.valueOf(sId)).get("ItemDetail"));
        }
        String sRidlist = "";
        for (int i = 0; i < mList.size(); i++) {
            if (i == 0) {
                sRidlist = "[" + mList.get(i).sName_id;
            } else {
                sRidlist = sRidlist + "," + mList.get(i).sName_id;
            }
        }
        query_yhj(sRidlist);

        setGridViewtxdd();
        bt_txdd_tjdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sActivity.equals("spxq")) {
                    llgm_tjdd();
                } else {
                    tjdd();
                }

            }
        });
        tv_txdd_money.setText(sAmount);
    }

    /**
     * 填写订单
     * 优惠卷
     * 发票
     */
    private void gwc_tjdd() {
        /**
         * 购物车
         * */
        String Sgwc = "";
        for (int i = 0; i < mList.size(); i++) {
            if (i == 0) {
                Sgwc = "{\"" + mList.get(i).sName_id + "\":\"";
                for (int a = 0; a < mList.get(i).mylist.size(); a++) {
                    if (a == 0) {
                        Sgwc = Sgwc + mList.get(i).mylist.get(a).get("ItemCart_Id");
                    } else {
                        Sgwc = Sgwc + "-" + mList.get(i).mylist.get(a).get("ItemCart_Id");
                    }
                }
                Sgwc = Sgwc + "\"";
            } else {
                Sgwc = Sgwc + ",\"" + mList.get(i).sName_id + "\":\"";
                for (int a = 0; a < mList.get(i).mylist.size(); a++) {
                    if (a == 0) {
                        Sgwc = Sgwc + mList.get(i).mylist.get(a).get("ItemCart_Id");
                    } else {
                        Sgwc = Sgwc + "-" + mList.get(i).mylist.get(a).get("ItemCart_Id");
                    }
                }
                Sgwc = Sgwc + "\"";
            }
        }
        Sgwc = Sgwc + "}";

        /**
         * 留言
         * */
        String sLiuyan = "";
        for (int i = 0; i < mList.size(); i++) {
            if (i == 0) {
                sLiuyan = "{\"" + mList.get(i).sName_id + "\":\"" + mList.get(i).sLiuyan + "\"";
            } else {
                sLiuyan = sLiuyan + ",\"" + mList.get(i).sName_id + "\":\"" + mList.get(i).sLiuyan + "\"";
            }
        }
        sLiuyan = sLiuyan + "}";

        /**
         * 优惠卷
         * */
        String sYhj = "";
        for (int i = 0; i < mList.size(); i++) {
            if (i == 0) {
                sYhj = "{\"" + mList.get(i).sName_id + "\":\"" + mList.get(i).sYhj_id + "\"";
            } else {
                sYhj = sYhj + ",\"" + mList.get(i).sName_id + "\":\"" + mList.get(i).sYhj_id + "\"";
            }
        }
        sYhj = sYhj + "}";

        /**
         * 发票
         * */
        String sFp = "";
        for (int i = 0; i < mList.size(); i++) {
            if (i == 0) {
                sFp = "{\"" + mList.get(i).sName_id + "\":{\"invoice_title\":" + mList.get(i).sFapiao_Name + ",\"invoice_desc\":" + mList.get(i).sFapiao_Name + ",\"invoice_desc\":" + mList.get(i).sFapiao_Nsh + "}";
            } else {
                sFp = sFp + ",\"" + mList.get(i).sName_id + "\":{\"invoice_title\":" + mList.get(i).sFapiao_Name + ",\"invoice_desc\":" + mList.get(i).sFapiao_Name + ",\"invoice_desc\":" + mList.get(i).sFapiao_Nsh + "}";
            }
        }
        sFp = sFp + "}";

        dialogin("");
        gwc_tijiaodd(resultAddressId, Sgwc, sLiuyan, sYhj, sFp);
    }


    /**
     * 填写订单
     */
    private void tjdd() {
        /**
         * 购物车
         * */
        String Sgwc = "";
        for (int i = 0; i < mList.size(); i++) {
            if (i == 0) {
                for (int a = 0; a < mList.get(i).mylist.size(); a++) {
                    if (a == 0) {
                        Sgwc = Sgwc + mList.get(i).mylist.get(a).get("ItemId");
                    } else {
                        Sgwc = Sgwc + "-" + mList.get(i).mylist.get(a).get("ItemId");
                    }
                }
            } else {
                for (int a = 0; a < mList.get(i).mylist.size(); a++) {
                    if (a == 0) {
                        if (Sgwc.equals("")) {
                            Sgwc = Sgwc + mList.get(i).mylist.get(a).get("ItemId");
                        } else {
                            Sgwc = Sgwc + "-" + mList.get(i).mylist.get(a).get("ItemId");
                        }
                    } else {
                        Sgwc = Sgwc + "-" + mList.get(i).mylist.get(a).get("ItemId");
                    }
                }
            }
        }
        Sgwc = Sgwc;


        /**
         * 留言
         * */
        String sLiuyan = "";
        for (int i = 0; i < mList.size(); i++) {
            if (sLiuyan.equals("")) {
                if (mList.get(i).sLiuyan.equals("")) {
                } else {
                    sLiuyan = mList.get(i).sName_id + ":" + mList.get(i).sLiuyan + "";
                }
            } else {
                sLiuyan = sLiuyan + "," + mList.get(i).sName_id + ":" + mList.get(i).sLiuyan;
            }
        }

        dialogin("");
        tijiaodd(resultAddressId, Sgwc, sLiuyan);
    }


    private void llgm_tjdd() {
        /**
         * 发票
         * */
        String sFp = "{\"invoice_title:" + mList.get(0).sFapiao_Name + ",\"invoice_desc\":" + mList.get(0).sFapiao_Name + ",\"invoice_desc\":" + mList.get(0).sFapiao_Nsh + "}";

        dialogin("");
        ljgm_tijiaodd(resultAddressId, mList.get(0).mylist.get(0).get("ItemGoods_Id"),
                mList.get(0).sLiuyan, mList.get(0).sYhj_id, mList.get(0).sGg_id, mList.get(0).sNum, sFp);
    }


    /**
     * 购物车--提交订单
     */
    private void tijiaodd(String address_id, String cart, String remark) {
        String url = Api.sUrl + "Api/Order/orderadd/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/address_id/" + address_id + "/cart/" + cart;
        if (!remark.equals("")) {
            url = url + "/remark/" + remark;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    String resultMsg = jsonObject.getString("msg");
                    int resultCode = jsonObject.getInt("code");
                    if (resultCode > 0) {
                        String resultData = "";
                        // String resultData = jsonObject.getString("data");
                        JSONArray jsonArrayCommentCat = jsonObject.getJSONArray("data");
                        for (int a = 0; a < jsonArrayCommentCat.length(); a++) {
                            String commentCat = jsonArrayCommentCat.get(a).toString();
                            if (resultData.equals("")) {
                                resultData = commentCat;
                            } else {
                                resultData = resultData + "-" + commentCat;
                            }
                        }
                        Intent intent = new Intent(TxddActivity_test.this, ZffsActivity.class);
                        intent.putExtra("data", resultData);
                        intent.putExtra("activity", "txdd");
                        intent.putExtra("money", tv_txdd_money.getText().toString());
                        startActivity(intent);
                        finish();

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
     * 购物车--提交订单
     * 优惠卷
     * 发票
     */
    private void gwc_tijiaodd(String address_id, String cart_id, String message, String coupon, String invoice) {
        String url = Api.sUrl + "Order/orderAdd/";
        RequestQueue requestQueue = Volley.newRequestQueue(TxddActivity_test.this);
        Map<String, String> params = new HashMap<String, String>();
        //地址
        if (!address_id.equals("")) {
            params.put("address_id", address_id);
        }
        //购物车
        if (!cart_id.equals("")) {
            params.put("cart_id", cart_id);
        }
        //用户ID
        params.put("user_id", sUser_id);
        //留言
        if (!message.equals("")) {
            params.put("message", message);
        }
        //优惠卷
        if (!coupon.equals("")) {
            params.put("coupon", coupon);
        }
        //发票
        if (!invoice.equals("")) {
            params.put("invoice", invoice);
        }
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
                            //   Log.d(TAG, "response -> " + response.toString());
                            //     Toast.makeText(NoteActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            if (resultCode > 0) {
                                String resultData = jsonObject1.getString("data");
                                Intent intent = new Intent(TxddActivity_test.this, ZffsActivity.class);
                                intent.putExtra("data", resultData);
                                //   intent.putExtra("money", tv_qrdd_money.getText().toString());
                                startActivity(intent);
                                finish();
                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialogin();
                //  Log.e(TAG, error.getMessage(), error);
                Hint(error.getMessage(), HintDialog.ERROR);
                //   Toast.makeText(NoteActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonRequest);
    }

    /**
     * 立即购买--提交订单
     */
    private void ljgm_tijiaodd(String address_id, String goods_id, String message, String coupon,
                               String item_id, String num, String invoice) {
        String url = Api.sUrl + "Order/lorderAdd";
        RequestQueue requestQueue = Volley.newRequestQueue(TxddActivity_test.this);
        Map<String, String> params = new HashMap<String, String>();
        //地址
        if (!address_id.equals("")) {
            params.put("address_id", address_id);
        }
        //购物车
        if (!goods_id.equals("")) {
            params.put("goods_id", goods_id);
        }
        //用户ID
        params.put("user_id", sUser_id);
        //留言
        if (!message.equals("")) {
            params.put("message", message);
        }
        //优惠卷
        if (!coupon.equals("")) {
            params.put("coupon", coupon);
        }
        //规格DID
        params.put("item_id", item_id);
        //数量
        params.put("num", num);
        //发票
        if (!invoice.equals("")) {
            params.put("invoice", invoice);
        }
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
                            //   Log.d(TAG, "response -> " + response.toString());
                            //     Toast.makeText(NoteActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            if (resultCode > 0) {
                                String resultData = jsonObject1.getString("data");
                                Intent intent = new Intent(TxddActivity_test.this, ZffsActivity.class);
                                intent.putExtra("data", resultData);
                                //   intent.putExtra("money", tv_qrdd_money.getText().toString());
                                startActivity(intent);
                                finish();
                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialogin();
                //  Log.e(TAG, error.getMessage(), error);
                Hint(error.getMessage(), HintDialog.ERROR);
                //   Toast.makeText(NoteActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonRequest);
    }

    /**
     * 获取默认收货地址
     */
    private void huoqu() {
        String url = Api.sUrl + "Api/Order/addressdefault/appId/" + Api.sApp_Id + "/user_id/" + sUser_id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    String resultMsg = jsonObject.getString("msg");
                    int resultCode = jsonObject.getInt("code");
                    if (resultCode > 0) {
                        String resultData = jsonObject.getString("data");
                        JSONObject jsonObject2 = new JSONObject(resultData);
                        resultAddressId = jsonObject2.getString("id");
                        String resultConsignee = jsonObject2.getString("username");
                        String resultAddress = jsonObject2.getString("detail");
                        String resultMobile = jsonObject2.getString("phone");
                        String resultProvince_Name = jsonObject2.getString("sheng");
                        String resultDistrict_Name = jsonObject2.getString("shi");
                        String resultCity_Name = jsonObject2.getString("qu");
                        tv_txdd_consignee.setText(resultConsignee);
                        tv_txdd_mobile.setText(resultMobile);
                        tv_txdd_dizhi.setText(resultProvince_Name + resultDistrict_Name + resultCity_Name + resultAddress);

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
     * 优惠卷
     */
    private void query_yhj(String sRidlist) {
        String url = Api.sUrl + "Api/Good/usecoupons/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/shangjia_id/" + sRidlist;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    String resultMsg = jsonObject.getString("msg");
                    int resultCode = jsonObject.getInt("code");
                    if (resultCode > 0) {
                        JSONObject jsonObjectdata = jsonObject.getJSONObject("data");
                        Iterator<String> it = jsonObjectdata.keys();
                        mList_yhj = new ArrayList<Txdd_Yhj_GridData>();
                        while (it.hasNext()) {
                            // 获得key
                            String key = it.next();
                            JSONArray jsonObjectkey = jsonObjectdata.getJSONArray(key);

                            Txdd_Yhj_GridData txdd_yhj_gridData = new Txdd_Yhj_GridData();
                            ArrayList<HashMap<String, String>> arrlistxz = new ArrayList<HashMap<String, String>>();
              /*              JSONArray resultJsonArraytmenu = jsonObjectkey.getJSONArray("value");*/
                            for (int i = 0; i < jsonObjectkey.length(); i++) {
                                JSONObject jsonObjectvalue = jsonObjectkey.getJSONObject(i);
                                HashMap<String, String> maptmenu = new HashMap<String, String>();
                                maptmenu.put("Item_Id", jsonObjectvalue.getString("id"));
                                maptmenu.put("Item_Rid", jsonObjectvalue.getString("rid"));
                                maptmenu.put("Item_Amount", jsonObjectvalue.getString("amount"));
                                maptmenu.put("Item_Use_end_time", jsonObjectvalue.getString("use_end_time"));
                                maptmenu.put("Item_Use_start_time", jsonObjectvalue.getString("use_start_time"));
                                maptmenu.put("Item_Type", jsonObjectvalue.getString("type"));
                                maptmenu.put("Item_Desc", jsonObjectvalue.getString("desc"));
                                maptmenu.put("Item_Condition", jsonObjectvalue.getString("condition"));
                                maptmenu.put("Item_Coupons_id", jsonObjectvalue.getString("coupons_id"));
                                maptmenu.put("Item_Storename", jsonObjectvalue.getString("storename"));
                                arrlistxz.add(maptmenu);
                            }
                            txdd_yhj_gridData.sName_id = key;
                            txdd_yhj_gridData.mylist = arrlistxz;
                            mList_yhj.add(txdd_yhj_gridData);
                        }
                        yhj_num();

                    } else {
                        Hint(resultMsg, HintDialog.ERROR);
                    }
                    setGridViewtxdd();
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


    private void yhj_num() {
        for (int i = 0; i < mList.size(); i++) {
            for (int a = 0; a < mList_yhj.size(); a++) {
                if (mList_yhj.get(a).sName_id.equals(mList.get(i).sName_id)) {
                    mList.get(i).sYhj_num = String.valueOf(mList_yhj.get(a).mylist.size());
                }
            }
        }
        int a = mList.size();
    }

    /**
     * 设置GirdView参数，绑定数据
     */
    private void setGridViewtxdd() {
        myAdapter = new MyAdapter(TxddActivity_test.this);
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
/*        for (int i = 0; i < mList.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemId", mList.get(i).sName);
            mylist.add(map);
        }
        myAdapter.arrlist = mylist;*/
        mgv_txdd.setAdapter(myAdapter);
        sv_txdd.smoothScrollTo(0, 20);
        sv_txdd.setFocusable(true);
    }

    public void fpxzxhsm(String sType) {
        final Dialog dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        final View inflate = LayoutInflater.from(this).inflate(R.layout.fpxzshsm_dialog, null);
        TextView tv_fpxzshsm = inflate.findViewById(R.id.tv_fpxzshsm);
        ScrollView sv_fpxzshsm_shsm = inflate.findViewById(R.id.sv_fpxzshsm_shsm);
        LinearLayout ll_fpxzshsm_fpxz = inflate.findViewById(R.id.ll_fpxzshsm_fpxz);
        TextView tv_fpxzshsm_wzdl = inflate.findViewById(R.id.tv_fpxzshsm_wzdl);
        if (sType.equals("fpxz")) {
            tv_fpxzshsm.setText("发票须知");
            sv_fpxzshsm_shsm.setVisibility(View.GONE);
            ll_fpxzshsm_fpxz.setVisibility(View.VISIBLE);
        } else {
            tv_fpxzshsm.setText("发票税号说明");
            sv_fpxzshsm_shsm.setVisibility(View.VISIBLE);
            ll_fpxzshsm_fpxz.setVisibility(View.GONE);
        }
        tv_fpxzshsm_wzdl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        lp.width = (int) (display.getWidth());
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    /**
     * 弹出发票框
     */
    public void fp(final String sRid) {
        final Dialog dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(this).inflate(R.layout.txdd_dzfp_dialog, null);
 /*       lv_dialog = (ListView) inflate.findViewById(R.id.lv_dialog);*/
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        //  WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth());//设置宽度
        lp.height = (int) (display.getHeight() - 300);
        dialog.getWindow().setAttributes(lp);
        ImageView iv_txdd_dzfp_dialog_gb = inflate.findViewById(R.id.iv_txdd_dzfp_dialog_gb);
        TextView tv_txdd_dzfp_dialog_fpxz = inflate.findViewById(R.id.tv_txdd_dzfp_dialog_fpxz);
        final TextView tv_txdd_dzfp_dialog_gr = inflate.findViewById(R.id.tv_txdd_dzfp_dialog_gr);
        final TextView tv_txdd_dzfp_dialog_dw = inflate.findViewById(R.id.tv_txdd_dzfp_dialog_dw);
        final LinearLayout ll_txdd_dzfp_dialog_danwei = inflate.findViewById(R.id.ll_txdd_dzfp_dialog_danwei);
        ImageView iv_txdd_dzfp_dialog_shsm = inflate.findViewById(R.id.iv_txdd_dzfp_dialog_shsm);
        TextView tv_txdd_dzfp_dialog_xyb = inflate.findViewById(R.id.tv_txdd_dzfp_dialog_xyb);
        final EditText et_txdd_dzfp_dialog_name = inflate.findViewById(R.id.et_txdd_dzfp_dialog_name);
        final EditText et_txdd_dzfp_dialog_nsh = inflate.findViewById(R.id.et_txdd_dzfp_dialog_nsh);
        iv_txdd_dzfp_dialog_gb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_txdd_dzfp_dialog_fpxz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fpxzxhsm("fpxz");
            }
        });
        iv_txdd_dzfp_dialog_shsm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fpxzxhsm("shsm");
            }
        });
        fp_type = 1;
        tv_txdd_dzfp_dialog_gr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_txdd_dzfp_dialog_gr.setTextColor(tv_txdd_dzfp_dialog_gr.getResources().getColor(R.color.E51C23));
                tv_txdd_dzfp_dialog_gr.setBackgroundResource(R.drawable.bk_e51c23_13);
                tv_txdd_dzfp_dialog_dw.setTextColor(tv_txdd_dzfp_dialog_dw.getResources().getColor(R.color.hei333333));
                tv_txdd_dzfp_dialog_dw.setBackgroundResource(R.drawable.bk_dfdfdf_12);
                ll_txdd_dzfp_dialog_danwei.setVisibility(View.GONE);
                fp_type = 1;
            }
        });
        tv_txdd_dzfp_dialog_dw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_txdd_dzfp_dialog_gr.setTextColor(tv_txdd_dzfp_dialog_gr.getResources().getColor(R.color.hei333333));
                tv_txdd_dzfp_dialog_gr.setBackgroundResource(R.drawable.bk_dfdfdf_12);
                tv_txdd_dzfp_dialog_dw.setTextColor(tv_txdd_dzfp_dialog_dw.getResources().getColor(R.color.E51C23));
                tv_txdd_dzfp_dialog_dw.setBackgroundResource(R.drawable.bk_e51c23_13);
                ll_txdd_dzfp_dialog_danwei.setVisibility(View.VISIBLE);
                fp_type = 2;
            }
        });
        tv_txdd_dzfp_dialog_xyb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fp_type == 1) {
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i).sName_id.equals(sRid)) {
                            mList.get(i).sFapiao_Type = "个人";
                            mList.get(i).sFapiao_Nsh = "1";
                        }
                    }
                } else if (fp_type == 2) {
                    if (et_txdd_dzfp_dialog_name.getText().toString().equals("")) {
                        Hint("单位名称为空！", HintDialog.WARM);
                    } else if (et_txdd_dzfp_dialog_nsh.getText().toString().equals("")) {
                        Hint("纳税人识别号为空！", HintDialog.WARM);
                    } else {
                        for (int i = 0; i < mList.size(); i++) {
                            if (mList.get(i).sName_id.equals(sRid)) {
                                mList.get(i).sFapiao_Type = "单位";
                                mList.get(i).sFapiao_Name = et_txdd_dzfp_dialog_name.getText().toString();
                                mList.get(i).sFapiao_Nsh = et_txdd_dzfp_dialog_nsh.getText().toString();
                            }
                        }
                    }
                }
                myAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

        });
        dialog.show();
    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(TxddActivity_test.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(TxddActivity_test.this, R.style.dialog, sHint, type, true).show();
    }

    /**
     * 弹出优惠卷框
     */
    public void yhj(String Rid) {
        dialog_yhj = new Dialog(this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(this).inflate(R.layout.txdd_yhj_dialog, null);
 /*       lv_dialog = (ListView) inflate.findViewById(R.id.lv_dialog);*/
        dialog_yhj.setContentView(inflate);
        Window dialogWindow = dialog_yhj.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        //  WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog_yhj.getWindow().getAttributes();
        lp.width = (int) (display.getWidth());//设置宽度
        lp.height = (int) (display.getHeight() - 400);
        dialog_yhj.getWindow().setAttributes(lp);
        LinearLayout ll_txdd_yhj_dialog_gb = inflate.findViewById(R.id.ll_txdd_yhj_dialog_gb);
        gv_txdd_yhj_dialog = inflate.findViewById(R.id.gv_txdd_yhj_dialog);
        setGridViewYhj(Rid);
        ll_txdd_yhj_dialog_gb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_yhj.dismiss();
            }
        });
        dialog_yhj.show();
    }


    /**
     * 弹出商品清单
     */
    public void spqd(String Rid, int good_num) {
        dialog_spqd = new Dialog(this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(this).inflate(R.layout.spqd_dialog, null);
 /*       lv_dialog = (ListView) inflate.findViewById(R.id.lv_dialog);*/
        dialog_spqd.setContentView(inflate);
        Window dialogWindow = dialog_spqd.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        //  WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog_spqd.getWindow().getAttributes();
        lp.width = (int) (display.getWidth());//设置宽度
        lp.height = (int) (display.getHeight() - 400);
        dialog_spqd.getWindow().setAttributes(lp);
        TextView tv_spqd_dialog_num = inflate.findViewById(R.id.tv_spqd_dialog_num);
        tv_spqd_dialog_num.setText("共" + String.valueOf(good_num) + "件");
        LinearLayout ll_spqd_dialog_gb = inflate.findViewById(R.id.ll_spqd_dialog_gb);
        gv_spqd_dialog = inflate.findViewById(R.id.gv_spqd_dialog);
        setGridViewSpqd(Rid);
        ll_spqd_dialog_gb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_spqd.dismiss();
            }
        });
        dialog_spqd.show();
    }

    /**
     * 设置GirdView参数，绑定数据
     */
    private void setGridViewYhj(String sRid) {
        MyAdapterYhj myAdapterYhj = new MyAdapterYhj(TxddActivity_test.this);
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < mList_yhj.size(); i++) {
            if (mList_yhj.get(i).sName_id.equals(sRid)) {
                myAdapterYhj.arrlist = mList_yhj.get(i).mylist;
            }
        }
        gv_txdd_yhj_dialog.setAdapter(myAdapterYhj);
    }


    /**
     * 设置GirdView参数，绑定数据
     * 商品清单
     */
    private void setGridViewSpqd(String sRid) {
        MyAdapterSpqd myAdapterSpqd = new MyAdapterSpqd(TxddActivity_test.this);

        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).sName_id.equals(sRid)) {
                myAdapterSpqd.arrlist = mList.get(i).mylist;
            }
        }
        gv_spqd_dialog.setAdapter(myAdapterSpqd);
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        //  ArrayList<HashMap<String, String>> arrlist;

        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            //  new ArrayList<HashMap<String, String>>();


        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mList.size();
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
                view = inflater.inflate(R.layout.txdd_item, null);
            }
            LinearLayout ll_txdd_item_spqd = view.findViewById(R.id.ll_txdd_item_spqd);
            TextView tv_txdd_item_name = view.findViewById(R.id.tv_txdd_item_name);
            LinearLayout ll_txdd_item_fp = view.findViewById(R.id.ll_txdd_item_fp);
            LinearLayout ll_txdd_item_yhj = view.findViewById(R.id.ll_txdd_item_yhj);
            //  TextView tv_txdd_item_yhj = view.findViewById(R.id.tv_txdd_item_yhj);
            ImageView iv_txdd_item_image1 = view.findViewById(R.id.iv_txdd_item_image1);
            ImageView iv_txdd_item_image2 = view.findViewById(R.id.iv_txdd_item_image2);
            ImageView iv_txdd_item_image3 = view.findViewById(R.id.iv_txdd_item_image3);
            ImageView iv_txdd_item_image4 = view.findViewById(R.id.iv_txdd_item_image4);
            TextView tv_txdd_item_spnum = view.findViewById(R.id.tv_txdd_item_spnum);
            // TextView tv_txdd_item_fp = view.findViewById(R.id.tv_txdd_item_fp);
            num = 0;
            for (int i = 0; i < mList.get(position).mylist.size(); i++) {
                num += Integer.valueOf(mList.get(position).mylist.get(i).get("ItemGoods_Num"));
            }
            double zongjia = 0;
            for (int i = 0; i < mList.size(); i++) {
                if (!mList.get(i).sYhj_amount.equals("")) {
                    if (zongjia == 0) {
                        zongjia = sub(Double.parseDouble(sAmount), Double.parseDouble(mList.get(i).sYhj_amount));
                        //  zongjia = Double.parseDouble(sAmount) - Double.parseDouble(mList.get(i).sYhj_amount);
                    } else {
                        zongjia = sub(zongjia, Double.parseDouble(mList.get(i).sYhj_amount));
                        // zongjia = zongjia - Double.parseDouble(mList.get(i).sYhj_amount);
                    }
                }
            }
            if (zongjia == 0) {
            } else {
                tv_txdd_money.setText(String.valueOf(zongjia));
            }
            tv_txdd_item_spnum.setText("共" + num + "件");
            tv_txdd_item_name.setText(mList.get(position).sName);
            EditText et_txdd_item_liuyan = view.findViewById(R.id.et_txdd_item_liuyan);
      /*      if (mList.get(position).sYhj_name.equals("")) {
                if (Integer.valueOf(mList.get(position).sYhj_num) > 0) {
                    tv_txdd_item_yhj.setText("可用" + mList.get(position).sYhj_num + "张");
                }
            } else {
                tv_txdd_item_yhj.setText(mList.get(position).sYhj_name);
            }
            if (!mList.get(position).sFapiao_Type.equals("")) {
                tv_txdd_item_fp.setText(mList.get(position).sFapiao_Type);
            }*/
            if (mList.get(position).mylist.size() > 0) {
                Glide.with(TxddActivity_test.this)
                        .load( Api.sUrl+ mList.get(position).mylist.get(0).get("ItemGood_logo"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_txdd_item_image1);
            }
            if (mList.get(position).mylist.size() > 1) {
                Glide.with(TxddActivity_test.this)
                        .load( Api.sUrl+ mList.get(position).mylist.get(1).get("ItemGood_logo"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_txdd_item_image2);
            }
            if (mList.get(position).mylist.size() > 2) {
                Glide.with(TxddActivity_test.this)
                        .load( Api.sUrl+ mList.get(position).mylist.get(2).get("ItemGood_logo"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_txdd_item_image3);
            }
            if (mList.get(position).mylist.size() > 3) {
                Glide.with(TxddActivity_test.this)
                        .load( Api.sUrl+ mList.get(position).mylist.get(3).get("ItemGood_logo"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_txdd_item_image4);
            }
            ll_txdd_item_fp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fp(mList.get(position).sName_id);

                }
            });
            ll_txdd_item_yhj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    yhj(mList.get(position).sName_id);
                }
            });
            ll_txdd_item_spqd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    spqd(mList.get(position).sName_id, num);
                }
            });
            et_txdd_item_liuyan.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    mList.get(position).sLiuyan = charSequence.toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
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
     * 相减
     */
    public static double sub(double a1, double b1) {
        BigDecimal a2 = new BigDecimal(Double.toString(a1));
        BigDecimal b2 = new BigDecimal(Double.toString(b1));
        return a2.subtract(b2).doubleValue();
    }

    /**
     * 相乘
     */
    public static double mul(double a1, double b1) {
        BigDecimal a2 = new BigDecimal(Double.toString(a1));
        BigDecimal b2 = new BigDecimal(Double.toString(b1));
        return a2.multiply(b2).doubleValue();
    }


    private class MyAdapterYhj extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        ArrayList<HashMap<String, String>> arrlist;

        public MyAdapterYhj(Context context) {
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
                view = inflater.inflate(R.layout.txdd_yhj_dialog_item, null);
            }
            LinearLayout ll_txdd_yhj_dialog_item = view.findViewById(R.id.ll_txdd_yhj_dialog_item);
            TextView tv_txdd_yhj_dialog_item_amount = view.findViewById(R.id.tv_txdd_yhj_dialog_item_amount);
            tv_txdd_yhj_dialog_item_amount.setText(arrlist.get(position).get("Item_Amount"));
            TextView tv_txdd_yhj_dialog_item_type = view.findViewById(R.id.tv_txdd_yhj_dialog_item_type);
            if (arrlist.get(position).get("Item_Type").equals("1")) {
                tv_txdd_yhj_dialog_item_type.setText("商家卷");
            } else if (arrlist.get(position).get("Item_Type").equals("2")) {
                tv_txdd_yhj_dialog_item_type.setText("平台卷");
            }
            TextView tv_txdd_yhj_dialog_item_desc = view.findViewById(R.id.tv_txdd_yhj_dialog_item_desc);
            tv_txdd_yhj_dialog_item_desc.setText(arrlist.get(position).get("Item_Desc"));
            TextView tv_txdd_yhj_dialog_item_start_end = view.findViewById(R.id.tv_txdd_yhj_dialog_item_start_end);
            tv_txdd_yhj_dialog_item_start_end.setText(arrlist.get(position).get("Item_Use_start_time")
                    + "-" + arrlist.get(position).get("Item_Use_end_time"));
            TextView tv_txdd_yhj_dialog_item_storename = view.findViewById(R.id.tv_txdd_yhj_dialog_item_storename);
            tv_txdd_yhj_dialog_item_storename.setText(arrlist.get(position).get("Item_Storename"));
            TextView tv_txdd_yhj_dialog_item_condition = view.findViewById(R.id.tv_txdd_yhj_dialog_item_condition);
            tv_txdd_yhj_dialog_item_condition.setText("满" + arrlist.get(position).get("Item_Condition")
                    + "减" + arrlist.get(position).get("Item_Amount") + "元");
            TextView tv_txdd_yhj_dialog_item_condition1 = view.findViewById(R.id.tv_txdd_yhj_dialog_item_condition1);
            tv_txdd_yhj_dialog_item_condition1.setText("满" + arrlist.get(position).get("Item_Condition")
                    + "减" + arrlist.get(position).get("Item_Amount") + "元");
            ll_txdd_yhj_dialog_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i).sName_id.equals(arrlist.get(position).get("Item_Rid"))) {
                            mList.get(i).sYhj_id = arrlist.get(position).get("Item_Id");
                            mList.get(i).sYhj_name = "满" + arrlist.get(position).get("Item_Condition")
                                    + "减" + arrlist.get(position).get("Item_Amount") + "元";
                            mList.get(i).sYhj_amount = arrlist.get(position).get("Item_Amount");
                        }
                    }
                    dialog_yhj.dismiss();


                    myAdapter.notifyDataSetChanged();
                }
            });
            return view;
        }
    }

    /**
     * 商品清单
     */
    private class MyAdapterSpqd extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        ArrayList<HashMap<String, String>> arrlist;

        public MyAdapterSpqd(Context context) {
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
                view = inflater.inflate(R.layout.spqd_item, null);
            }
            ImageView iv_spqd_item_image = view.findViewById(R.id.iv_spqd_item_image);
            TextView tv_spqd_item_name = view.findViewById(R.id.tv_spqd_item_name);
            TextView tv_spqd_item_gg = view.findViewById(R.id.tv_spqd_item_gg);
            TextView tv_spqd_item_price = view.findViewById(R.id.tv_spqd_item_price);
            TextView tv_spqd_item_num = view.findViewById(R.id.tv_spqd_item_num);

            tv_spqd_item_name.setText(arrlist.get(position).get("ItemGoods_Name"));
            tv_spqd_item_gg.setText(arrlist.get(position).get("ItemGuige"));
            tv_spqd_item_price.setText(arrlist.get(position).get("ItemGoods_Price"));
            tv_spqd_item_num.setText("x" + arrlist.get(position).get("ItemGoods_Num"));
            Glide.with(TxddActivity_test.this)
                    .load( Api.sUrl+ arrlist.get(position).get("ItemGood_logo"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_spqd_item_image);
            return view;
        }
    }

}
