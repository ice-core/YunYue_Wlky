package com.example.administrator.yunyue.sc_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QrddActivity extends AppCompatActivity {
    private MyGridView lv_qrdd;
    private ImageView iv_dzbj;
    private ImageView iv_qrdd_back;
    private static final String TAG = QrddActivity.class.getSimpleName();
    public static ArrayList<String> arrId;
    public static ArrayList<String> arrImageData;
    public static ArrayList<String> arrNameData;
    public static ArrayList<String> arrKeyNameData;
    public static ArrayList<String> arrPriceData;
    public static ArrayList<String> arrNumData;
    public ArrayList<String> arrRid;
    public static ArrayList<String> arrCb;
    public static ArrayList<HashMap<String, String>> mylist;
    public MyAdapter adapter;
    RequestQueue queue = null;
    private SharedPreferences pref;
    private String sUser_id;
    private TextView tv_qrdd_name;
    private TextView tv_qrdd_dizhi;
    private TextView tv_qrdd_mobile;
    private TextView tv_qrdd_money;
    private TextView tv_qrdd_spze;
    private TextView tv_qrdd_zj;
    private LinearLayout ll_qrdd_dzxz;
    int numData = 0;
    private TextView tv_qrdd_hjnum;
    private LinearLayout ll_qrdd_back;
    private Button bt_qrdd_tj;
    private String resultAddressId = "";
    private EditText et_qrdd_message;
    public static ArrayList<HashMap<String, String>> mylistShdz;
    private String sId;
    private TextView tv_qrdd_amount;
    private String sAmount = "";
    private CheckBox cb_qrdd_hb;
    private String resultHongbao = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_qrdd);
        arrRid = new ArrayList<String>();
        Intent intent = getIntent();
        sAmount = intent.getStringExtra("amount");
        sId = intent.getStringExtra("id");
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        queue = Volley.newRequestQueue(QrddActivity.this);
        iv_qrdd_back = (ImageView) findViewById(R.id.iv_qrdd_back);
        tv_qrdd_name = (TextView) findViewById(R.id.tv_qrdd_name);
        lv_qrdd = (MyGridView) findViewById(R.id.lv_qrdd);
        tv_qrdd_dizhi = (TextView) findViewById(R.id.tv_qrdd_dizhi);
        tv_qrdd_mobile = (TextView) findViewById(R.id.tv_qrdd_mobile);
        tv_qrdd_money = (TextView) findViewById(R.id.tv_qrdd_money);
        tv_qrdd_spze = (TextView) findViewById(R.id.tv_qrdd_spze);
        tv_qrdd_zj = (TextView) findViewById(R.id.tv_qrdd_zj);
        ll_qrdd_dzxz = (LinearLayout) findViewById(R.id.ll_qrdd_dzxz);
        tv_qrdd_hjnum = (TextView) findViewById(R.id.tv_qrdd_hjnum);
        ll_qrdd_back = (LinearLayout) findViewById(R.id.ll_qrdd_back);
        bt_qrdd_tj = (Button) findViewById(R.id.bt_qrdd_tj);
        et_qrdd_message = (EditText) findViewById(R.id.et_qrdd_message);
        tv_qrdd_amount = (TextView) findViewById(R.id.tv_qrdd_amount);
        cb_qrdd_hb = (CheckBox) findViewById(R.id.cb_qrdd_hb);
        cb_qrdd_hb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_qrdd_hb.isChecked()) {
                    DecimalFormat df = new DecimalFormat("0.00");
                    tv_qrdd_zj.setText(df.format(Double.parseDouble(tv_qrdd_zj.getText().toString()) - Double.parseDouble(resultHongbao)));
                    tv_qrdd_money.setText(df.format(Double.parseDouble(tv_qrdd_money.getText().toString()) - Double.parseDouble(resultHongbao)));
                } else {
                    tv_qrdd_zj.setText(sAmount);
                    tv_qrdd_money.setText(sAmount);
                }
            }
        });

        for (int i = 0; arrCb.size() > i; i++) {
            if (arrCb.get(i).equals("1")) {
                if (arrRid.size() == 0) {
                    arrRid.add(mylist.get(i).get("rid"));
                } else {
                    for (int a = 0; a < mylist.size(); a++) {
                        if (arrRid.size() == 1) {
                            if (arrRid.get(0).equals(mylist.get(a).get("rid"))) {
                            } else {
                                arrRid.add(mylist.get(a).get("rid"));
                            }
                        } else if (arrRid.get(i - 1).equals(mylist.get(a).get("rid"))) {

                        } else {
                            arrRid.add(mylist.get(a).get("rid"));
                        }

                    }

                }
            }
        }
        bt_qrdd_tj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sHe = "";
                String sCart_id = "";
                String sRid = "";
                for (int i = 0; arrRid.size() > i; i++) {
                    if (i == 0) {
                        sRid = "\"" + arrRid.get(i) + "\":";
                        int number = 0;
                        for (int a = 0; mylist.size() > a; a++) {
                            if (arrCb.get(a).equals("1")) {
                                if (mylist.get(a).get("rid").equals(arrRid.get(i))) {
                                    if (number == 0) {
                                        number = 1;
                                        sCart_id = mylist.get(a).get("id");
                                    } else {
                                        sCart_id = sCart_id + "-" + mylist.get(a).get("id");
                                    }
                                }
                            }
                        }
                        number = 0;
                        sHe = sRid + "\"" + sCart_id + "\"";
                    } else {
                        sRid = "\"" + arrRid.get(i) + "\":";
                        int number = 0;
                        for (int a = 0; mylist.size() > a; a++) {
                            if (mylist.get(a).get("rid").equals(arrRid.get(i))) {
                                if (arrCb.get(a).equals("1")) {
                                    if (number == 0) {
                                        number = 1;
                                        sCart_id = mylist.get(a).get("id");
                                    } else {
                                        sCart_id = sCart_id + "-" + mylist.get(a).get("id");
                                    }
                                }
                            }
                        }
                        number = 0;
                        sHe = sHe + "," + sRid + "\"" + sCart_id + "\"";
                    }
                }
                sHe = "{" + sHe + "}";
                hideDialogin();
                dialogin("");
                tijiao(resultAddressId, sHe, et_qrdd_message.getText().toString());
            }
        });
        ll_qrdd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        ll_qrdd_dzxz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QrddActivity.this, ShdzActivity.class);
                intent.putExtra("", "qrdd");
                intent.putExtra("amount", sAmount);
                ShdzActivity.mylistQrdd = mylist;
                startActivity(intent);
                finish();
            }
        });
        tv_qrdd_zj.setText(sAmount);
        tv_qrdd_spze.setText(sAmount);
        tv_qrdd_money.setText(sAmount);
        iv_qrdd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        adapter = new MyAdapter(QrddActivity.this);
        adapter.arrImage = arrImageData;
        adapter.arrName = arrNameData;
        adapter.arrKeyName = arrKeyNameData;
        adapter.arrPrice = arrPriceData;
        adapter.arrNum = arrNumData;

        for (int i = 0; i < arrNumData.size(); i++) {
            numData += Integer.valueOf(arrNumData.get(i));
        }
        tv_qrdd_hjnum.setText(String.valueOf(numData));
        lv_qrdd.setAdapter(adapter);
        //  dialogin("");
        hongbao();
        if (sId == null) {
            hideDialogin();
            dialogin("");
            huoqu();
        } else if (sId.equals("")) {
            hideDialogin();
            dialogin("");
            huoqu();
        } else {
            int iId = Integer.valueOf(sId);
            tv_qrdd_name.setText(mylistShdz.get(iId).get("ItemName"));
            tv_qrdd_dizhi.setText("收货地址：" + mylistShdz.get(iId).get("ItemProvince") + mylistShdz.get(iId).get("ItemCity") + mylistShdz.get(iId).get("ItemDistrict") + mylistShdz.get(iId).get("ItemAddress"));
            tv_qrdd_mobile.setText(mylistShdz.get(iId).get("ItemMobile"));
            resultAddressId = mylistShdz.get(iId).get("ItemId");
        }
    }

    private void huoqu() {
        String url = Api.sUrl + "Address/AddressDefault/user_id/" + sUser_id;
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
                        resultAddressId = jsonObject2.getString("address_id");
                        String resultConsignee = jsonObject2.getString("consignee");
                        String resultAddress = jsonObject2.getString("address");
                        String resultMobile = jsonObject2.getString("mobile");
                        String resultProvince_Name = jsonObject2.getString("province_name");
                        String resultDistrict_Name = jsonObject2.getString("district_name");
                        String resultCity_Name = jsonObject2.getString("city_name");

                        tv_qrdd_name.setText(resultConsignee);
                        tv_qrdd_dizhi.setText("收货地址：" + resultProvince_Name + resultDistrict_Name + resultCity_Name + resultAddress);
                        tv_qrdd_mobile.setText(resultMobile);
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


    private void hongbao() {
        String url = Api.sUrl + "Order/coupons/";
        RequestQueue requestQueue = Volley.newRequestQueue(QrddActivity.this);
        Map<String, String> params = new HashMap<String, String>();
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
                            Log.d(TAG, "response -> " + response.toString());
                            //     Toast.makeText(NoteActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            if (resultCode > 0) {
                                String resultData = jsonObject1.getString("data");
                                jsonObject1 = new JSONObject(resultData);
                                resultHongbao = jsonObject1.getString("amount");
                                tv_qrdd_amount.setText(resultHongbao + "元");
                            } else {
                                tv_qrdd_amount.setText("");
                                // Hint(resultMsg, HintDialog.ERROR);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialogin();
                Log.e(TAG, error.getMessage(), error);
                Hint(error.getMessage(), HintDialog.ERROR);
                //   Toast.makeText(NoteActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonRequest);
    }


    private void tijiao(String address_id, String cart_id, String message) {
        String url = Api.sUrl + "Order/orderAdd/";
        RequestQueue requestQueue = Volley.newRequestQueue(QrddActivity.this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("address_id", address_id);
        params.put("cart_id", cart_id);
        params.put("user_id", sUser_id);
        if (cb_qrdd_hb.isChecked()) {
            params.put("coupon", resultHongbao);
        }
        params.put("message", message);
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
                            Log.d(TAG, "response -> " + response.toString());
                            //     Toast.makeText(NoteActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            if (resultCode > 0) {
                                String resultData = jsonObject1.getString("data");
                                Intent intent = new Intent(QrddActivity.this, ZffsActivity.class);
                                intent.putExtra("data", resultData);
                                intent.putExtra("money", tv_qrdd_money.getText().toString());
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
                Log.e(TAG, error.getMessage(), error);
                Hint(error.getMessage(), HintDialog.ERROR);
                //   Toast.makeText(NoteActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        loadingDialog = new LoadingDialog(QrddActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(QrddActivity.this, R.style.dialog, sHint, type, true).show();
    }


    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        public ArrayList<String> arrImage;
        public ArrayList<String> arrName;
        public ArrayList<String> arrKeyName;
        public ArrayList<String> arrPrice;
        public ArrayList<String> arrNum;

        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arrImage = new ArrayList<String>();
            arrName = new ArrayList<String>();
            arrKeyName = new ArrayList<String>();
            arrPrice = new ArrayList<String>();
            arrNum = new ArrayList<String>();

/*
            arrimage = new ArrayList<Bitmap>();
            arr = new ArrayList<String>();
            for (int i = 0; i < 1; i++) {
                //listview初始化3个子项
                arr.add("");
                arrimage.add(null);
            }*/
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arrNameData.size();
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

        public Bitmap stringToBitmap(String string) {    // 将字符串转换成Bitmap类型
            Bitmap bitmap = null;
            try {
                byte[] bitmapArray;
                bitmapArray = Base64.decode(string, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }


        @Override
        public View getView(final int position, View view, ViewGroup arg2) {
            // TODO Auto-generated method stub
            if (view == null) {
                view = inflater.inflate(R.layout.qrdd_lv_item, null);
            }
            final ImageView iv_qrdd_item = (ImageView) view.findViewById(R.id.iv_qrdd_item);
            TextView tv_qrdd_name_item = (TextView) view.findViewById(R.id.tv_qrdd_name_item);
            TextView tv_qrdd_color_item = (TextView) view.findViewById(R.id.tv_qrdd_color_item);
            TextView tv_qrdd_price_item = (TextView) view.findViewById(R.id.tv_qrdd_price_item);
            TextView tv_qrdd_number_item = (TextView) view.findViewById(R.id.tv_qrdd_number_item);
            Glide.with(QrddActivity.this)
                    .load( Api.sUrl+ arrImageData.get(position))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_qrdd_item);
            tv_qrdd_name_item.setText(arrNameData.get(position));
            tv_qrdd_color_item.setText(arrKeyNameData.get(position));
            tv_qrdd_price_item.setText(arrPriceData.get(position));
            tv_qrdd_number_item.setText(arrNumData.get(position));

            return view;
        }
    }

}
