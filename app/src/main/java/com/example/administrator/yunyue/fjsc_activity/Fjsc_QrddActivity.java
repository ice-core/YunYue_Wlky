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
import android.widget.EditText;
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
import com.example.administrator.yunyue.sc_activity.ZffsActivity;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fjsc_QrddActivity extends AppCompatActivity {
    private static final String TAG = Fjsc_QrddActivity.class.getSimpleName();
    private SharedPreferences pref;
    private String sUser_id;
    RequestQueue queue = null;
    private TextView tv_fjsc_qrdd_name;
    private TextView tv_fjsc_qrdd_phone;
    private TextView tv_fjsc_qrdd_dz;
    private LinearLayout ll_fjscqrdd_dzxz;
    public static String sId = "";
    private MyGridView mgv_fjsc_qrdd;
    private TextView tv_fjsc_qrdd_price;
    private TextView tv_fjsc_qrdd_zj;
    private TextView tv_fjsc_qrdd_qzf;
    private EditText et_fjsc_qrdd_message;
    private ArrayList<HashMap<String, String>> mylistgwc;
    //  private String resultAddressId = "";
    private TextView tv_fjsc_qrdd_dpname;

    private LinearLayout ll_fjsc_qrdd_back;
    private String resultRname = "";

    private TextView tv_fjsc_qrdd_freight;
    /**
     * 预计送达时间
     */
    private TextView tv_fjsc_qrdd_duration;

    /**
     * 免运费额度
     */
    private String freight_free = "";
    private ArrayList<HashMap<String, String>> mylist_dz;
    /**
     * 选择收货地址
     */
    public static int iDz = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_fjsc__qrdd);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
 /*       Intent intent = getIntent();
        sId = intent.getStringExtra("ID");*/

        queue = Volley.newRequestQueue(Fjsc_QrddActivity.this);
        mylistgwc = new ArrayList<HashMap<String, String>>();
        tv_fjsc_qrdd_name = findViewById(R.id.tv_fjsc_qrdd_name);
        tv_fjsc_qrdd_phone = findViewById(R.id.tv_fjsc_qrdd_phone);
        tv_fjsc_qrdd_dz = findViewById(R.id.tv_fjsc_qrdd_dz);
        ll_fjscqrdd_dzxz = findViewById(R.id.ll_fjscqrdd_dzxz);
        mgv_fjsc_qrdd = findViewById(R.id.mgv_fjsc_qrdd);
        tv_fjsc_qrdd_price = findViewById(R.id.tv_fjsc_qrdd_price);
        tv_fjsc_qrdd_zj = findViewById(R.id.tv_fjsc_qrdd_zj);
        tv_fjsc_qrdd_qzf = findViewById(R.id.tv_fjsc_qrdd_qzf);
        et_fjsc_qrdd_message = findViewById(R.id.et_fjsc_qrdd_message);
        tv_fjsc_qrdd_dpname = findViewById(R.id.tv_fjsc_qrdd_dpname);
        ll_fjsc_qrdd_back = findViewById(R.id.ll_fjsc_qrdd_back);
        tv_fjsc_qrdd_freight = findViewById(R.id.tv_fjsc_qrdd_freight);
        tv_fjsc_qrdd_duration = findViewById(R.id.tv_fjsc_qrdd_duration);
        ll_fjscqrdd_dzxz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Fjsc_QrddActivity.this, Fjsc_XzshdzActivity.class);
                startActivity(intent);

            }
        });
        ll_fjsc_qrdd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        dialogin("");
        sAddresslist();
        sCartlist();
        tv_fjsc_qrdd_qzf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cart_id = "";
                for (int i = 0; i < mylistgwc.size(); i++) {
                    if (i == 0) {
                        cart_id = mylistgwc.get(i).get("ItemId");
                    } else {
                        cart_id = cart_id + "-" + mylistgwc.get(i).get("ItemId");
                    }
                }
                dialogin("");
                // tijiao(resultAddressId, cart_id, sId+":"+et_fjsc_qrdd_message.getText().toString());
                sOrderadd(cart_id, sId + ":" + et_fjsc_qrdd_message.getText().toString());
            }
        });
    }


    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (iDz == 0) {
            sAddresslist();
        } else {
            for (int i = 0; i < mylist_dz.size(); i++) {
                if (mylist_dz.get(i).get("ItemId").equals(String.valueOf(iDz))) {
                    tv_fjsc_qrdd_name.setText(mylist_dz.get(i).get("ItemName"));
                    tv_fjsc_qrdd_phone.setText(mylist_dz.get(i).get("ItemMobile"));
                    tv_fjsc_qrdd_dz.setText(mylist_dz.get(i).get("ItemSheng") + mylist_dz.get(i).get("ItemShi")
                            + mylist_dz.get(i).get("ItemQu") + mylist_dz.get(i).get("ItemDetail"));
                }
            }
        }
        //sAddresslist();

    }


    /**
     * 方法名：sAddresslist()
     * 功  能：地址列表
     * 参  数：appId
     */
    private void sAddresslist() {
        String url = Api.sUrl + Api.sAddresslist;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
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
                            mylist_dz = new ArrayList<HashMap<String, String>>();

                            if (resultCode > 0) {
                                JSONArray resultJsonArray = jsonObject1.getJSONArray("data");

                                for (int i = 0; i < resultJsonArray.length(); i++) {
                                    JSONObject jsonObject = resultJsonArray.getJSONObject(i);
                                    String resultId = jsonObject.getString("id");
                                    String resultName = jsonObject.getString("username");
                                    String resultMobile = jsonObject.getString("phone");
                                    String resultSheng = jsonObject.getString("sheng");
                                    String resultShi = jsonObject.getString("shi");
                                    String resultQu = jsonObject.getString("qu");
                                    String resultDetail = jsonObject.getString("detail");
                                    String resultIsDefault = jsonObject.getString("is_default");

                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("ItemId", resultId);
                                    map.put("ItemName", resultName);
                                    map.put("ItemMobile", resultMobile);
                                    map.put("ItemSheng", resultSheng);
                                    map.put("ItemShi", resultShi);
                                    map.put("ItemQu", resultQu);
                                    map.put("ItemDetail", resultDetail);
                                    map.put("ItemIsDefault", resultIsDefault);
                                    mylist_dz.add(map);
                                }

                                if (mylist_dz.size() > 0) {
                                    sAddressdefault();
                                } else {
                                    Intent intent = new Intent(Fjsc_QrddActivity.this, Fjsc_XjshdzActivity.class);
                                    startActivity(intent);
                                }
                            } else {
                                if (mylist_dz.size() == 0) {

                                } else {
                                    Intent intent = new Intent(Fjsc_QrddActivity.this, Fjsc_XjshdzActivity.class);
                                    startActivity(intent);
                                }
                                Hint(resultMsg, HintDialog.ERROR);
                            }
                        } catch (JSONException e) {
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

    /**
     * 方法名：sOrderadd()
     * 功  能：添加订单
     * 参  数：appId
     * shangjia_id--商家ID
     */
    private void sOrderadd(String cart_id, String message) {
        String url = Api.sUrl + Api.sOrderadd;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("address_id", String.valueOf(iDz));
        params.put("cart", cart_id);
        if (!message.equals("")) {
            params.put("remark", message);
        }
        params.put("is_ziqu", sId);
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
                            String resultData = "";
                            if (resultCode > 0) {
                                JSONArray jsonArrayCommentCat = response.getJSONArray("data");
                                for (int a = 0; a < jsonArrayCommentCat.length(); a++) {
                                    String commentCat = jsonArrayCommentCat.get(a).toString();
                                    if (resultData.equals("")) {
                                        resultData = commentCat;
                                    } else {
                                        resultData = resultData + "-" + commentCat;
                                    }
                                }
                                Intent intent = new Intent(Fjsc_QrddActivity.this, ZffsActivity.class);
                                intent.putExtra("data", resultData);
                                intent.putExtra("money", tv_fjsc_qrdd_zj.getText().toString());
                                startActivity(intent);
                                finish();
                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
                            }
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
                //error(error);
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

    private void tijiao(String address_id, String cart_id, String message) {
        String url = Api.sUrl + "Order/orderAddFujin/address_id/" + address_id + "/cart_id/" + cart_id + "/user_id/" + sUser_id + "/message/" + message;

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
                        Intent intent = new Intent(Fjsc_QrddActivity.this, ZffsActivity.class);
                        intent.putExtra("data", resultData);
                        intent.putExtra("money", tv_fjsc_qrdd_zj.getText().toString());
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
        params.put("shangjia_id", sId);
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
                            String freight = "";
                            double price = 0;
                            MyAdapterGwc myAdapterGwc = new MyAdapterGwc(Fjsc_QrddActivity.this);
                            if (resultCode > 0) {
                                JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                                ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < resultJsonArray.length(); i++) {
                                    JSONObject jsonObject = resultJsonArray.getJSONObject(i);
                                    freight = jsonObject.getString("freight");
                                    String duration = jsonObject.getString("duration");
                                    tv_fjsc_qrdd_freight.setText(freight);
                                    tv_fjsc_qrdd_duration.setText("付款后" + duration + "内送达");
                                    /**
                                     * 免运费额度
                                     * */
                                    freight_free = jsonObject.getString("freight_free");

                                    JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                                    for (int a = 0; a < jsonArrayList.length(); a++) {
                                        jsonObject = jsonArrayList.getJSONObject(a);
                                        String resultId = jsonObject.getString("id");
                                        resultRname = jsonObject.getString("shangjia_name");
                                        String resultOriginalImg = jsonObject.getString("good_logo");
                                        String resultGoodsId = jsonObject.getString("good_id");
                                        String resultGoodsName = jsonObject.getString("good_name");
                                        String resultGoodsPrice = jsonObject.getString("goods_price");
                                        String resultGoodsNum = jsonObject.getString("goods_num");
                                        String resultSpecKeyName = jsonObject.getString("guige");
                                        String resultItemId = jsonObject.getString("item_id");
                                        HashMap<String, String> map = new HashMap<String, String>();
                                        map.put("ItemId", resultId);
                                        map.put("ItemGoodsId", resultGoodsId);
                                        map.put("ItemGoodsName", resultGoodsName);
                                        map.put("ItemGoodsPrice", resultGoodsPrice);
                                        map.put("ItemGoodsNum", resultGoodsNum);
                                        map.put("ItemSpecKeyName", resultSpecKeyName);
                                        map.put("ItemItemId", resultItemId);
                                        map.put("ItemOriginalImg", resultOriginalImg);
                                        // num += Integer.valueOf(resultGoodsNum);
                                        // price += Double.parseDouble(resultGoodsPrice) * Integer.valueOf(resultGoodsNum);
                                        price = Jjcc.add(price, Jjcc.mul(Double.parseDouble(resultGoodsPrice), Integer.valueOf(resultGoodsNum)));
                                        mylist.add(map);

                                    }
                                }
                                tv_fjsc_qrdd_dpname.setText(resultRname);
                                tv_fjsc_qrdd_price.setText(String.valueOf(price));
                                if (price >= Double.parseDouble(freight_free)) {
                                    tv_fjsc_qrdd_zj.setText(String.valueOf(price));
                                    tv_fjsc_qrdd_freight.setText("0");
                                } else {
                                    tv_fjsc_qrdd_zj.setText(String.valueOf(Jjcc.add(price, Double.parseDouble(freight))));

                                }


                       /*    tv_fjsc_dpsp_xlgwc_num.setText("(共" + num + "件商品)");*/
                                mylistgwc = mylist;
                                myAdapterGwc.arrmylist = mylist;
                                mgv_fjsc_qrdd.setAdapter(myAdapterGwc);
                            }
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
                //error(error);
            }
        });
        requestQueue.add(jsonRequest);
    }


    /**
     * 方法名：sAddressdefault()
     * 功  能：默认地址
     * 参  数：appId
     * shangjia_id--商家ID
     */
    private void sAddressdefault() {
        String url = Api.sUrl + Api.sAddressdefault;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
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
                                JSONObject jsonObject2 = new JSONObject(resultData);
                                //resultAddressId = jsonObject2.getString("id");
                                if (!jsonObject2.getString("id").equals("")) {
                                    iDz = Integer.valueOf(jsonObject2.getString("id"));
                                }
                                String resultConsignee = jsonObject2.getString("username");

                                String resultMobile = jsonObject2.getString("phone");

                                String resultProvinceName = jsonObject2.getString("sheng");
                                String resultDistrictName = jsonObject2.getString("shi");
                                String resultCityName = jsonObject2.getString("qu");
                                String resultAddress = jsonObject2.getString("detail");
                                tv_fjsc_qrdd_name.setText(resultConsignee);
                                tv_fjsc_qrdd_phone.setText(resultMobile);
                                tv_fjsc_qrdd_dz.setText(resultProvinceName + resultDistrictName + resultCityName + resultAddress);
             /*           tv_qrdd_name.setText(resultConsignee);
                        tv_qrdd_dizhi.setText("收货地址：" + resultAddress);
                        tv_qrdd_mobile.setText(resultMobile);*/
                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
                            }
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
                //error(error);
            }
        });
        requestQueue.add(jsonRequest);
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
        loadingDialog = new LoadingDialog(Fjsc_QrddActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(Fjsc_QrddActivity.this, R.style.dialog, sHint, type, true).show();
    }


    private class MyAdapterGwc extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater1;

        public ArrayList<HashMap<String, String>> arrmylist;
        public ArrayList<String> arrItem;


        public MyAdapterGwc(Context context) {
            super();
            this.context = context;
            inflater1 = LayoutInflater.from(context);
            arrmylist = new ArrayList<HashMap<String, String>>();
            arrItem = new ArrayList<String>();
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
                view = inflater1.inflate(R.layout.fjsc_qrdd_item, null);
            }
            ImageView iv_fjsc_qrdd = view.findViewById(R.id.iv_fjsc_qrdd);
            TextView tv_fjsc_qrdd_name = view.findViewById(R.id.tv_fjsc_qrdd_name);
            TextView tv_fjsc_qrdd_num = view.findViewById(R.id.tv_fjsc_qrdd_num);
            TextView tv_fjsc_qrdd_price = view.findViewById(R.id.tv_fjsc_qrdd_price);

            tv_fjsc_qrdd_name.setText(arrmylist.get(position).get("ItemGoodsName"));
            tv_fjsc_qrdd_price.setText("￥" + arrmylist.get(position).get("ItemGoodsPrice"));
            tv_fjsc_qrdd_num.setText("x" + arrmylist.get(position).get("ItemGoodsNum"));

            Glide.with(Fjsc_QrddActivity.this).load( Api.sUrl+ arrmylist.get(position).get("ItemOriginalImg"))
                    .asBitmap()
                    .dontAnimate()
                    .into(iv_fjsc_qrdd);
            return view;
        }
    }
}