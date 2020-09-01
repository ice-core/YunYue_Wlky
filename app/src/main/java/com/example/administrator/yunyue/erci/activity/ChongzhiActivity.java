package com.example.administrator.yunyue.erci.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc.WXPayUtils;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChongzhiActivity extends AppCompatActivity {
    private static final String TAG = ChongzhiActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_cz_back;
    /**
     * 充值列表
     */
    private MyGridView mgv_cz_lb;
    private SharedPreferences pref;
    private String sUser_id;
    ArrayList<HashMap<String, String>> myListrm;
    private String sPrice = "";
    /**
     * 下一步
     */
    private TextView tv_cz_xyb;
    private int ItemPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_chongzhi);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        ll_cz_back = findViewById(R.id.ll_cz_back);
        ll_cz_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mgv_cz_lb = findViewById(R.id.mgv_cz_lb);
        sGongfenlist();
        tv_cz_xyb = findViewById(R.id.tv_cz_xyb);
        tv_cz_xyb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sRecharge();
            }
        });
    }

    /**
     * 方法名：sRecharge()
     * 功  能：开通会员
     * 参  数：appId
     */
    private void sRecharge() {
        String url = Api.sUrl + Api.sRecharge;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("price", sPrice);
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
                                String sData = jsonObject1.getString("data");
                                JSONObject jsonObjectData = new JSONObject(sData);
                                String sAppid = jsonObjectData.getString("appid");
                                String sPartnerid = jsonObjectData.getString("partnerid");
                                String sPrepayid = jsonObjectData.getString("prepayid");
                                String sPackage = jsonObjectData.getString("package");
                                String sNoncestr = jsonObjectData.getString("noncestr");
                                String sTimestamp = jsonObjectData.getString("timestamp");
                                String sSign = jsonObjectData.getString("sign");
                                WXPayUtils.WXPayBuilder builder = new WXPayUtils.WXPayBuilder();
                                builder.setAppId(sAppid)
                                        .setPartnerId(sPartnerid)
                                        .setPrepayId(sPrepayid)
                                        .setPackageValue("Sign=WXPay")
                                        .setNonceStr(sNoncestr)
                                        .setTimeStamp(sTimestamp)
                                        .setSign(sSign)
                                        .build().toWXPayNotSign(ChongzhiActivity.this);
                            } else {
                                // et_zhifu_shuru.setText("");
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
                error(error);
            }
        });
        requestQueue.add(jsonRequest);
    }


    /**
     * 方法名：sGongfenlist()
     * 功  能：工分充值数量列表
     * 参  数：appId
     */
    private void sGongfenlist() {
        String url = Api.sUrl + Api.sGongfenlist;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        JSONObject jsonObject = new JSONObject(params);
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialogin();
                        String sDate = response.toString();
                        System.out.println(sDate);
                        try {
                            JSONObject jsonObject0 = new JSONObject(sDate);
                            String resultMsg = jsonObject0.getString("msg");
                            int resultCode = jsonObject0.getInt("code");
                            if (resultCode > 0) {
                                MyAdapter myAdapter = new MyAdapter(ChongzhiActivity.this);
                                myListrm = new ArrayList<HashMap<String, String>>();
                                JSONArray resultJsonArray = jsonObject0.getJSONArray("data");
                                for (int i = 0; i < resultJsonArray.length(); i++) {
                                    JSONObject jsonObject = resultJsonArray.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("ItemId", jsonObject.getString("id"));//id
                                    map.put("ItemWp_num", jsonObject.getString("wp_num"));//
                                    map.put("ItemPrice", jsonObject.getString("price"));//
                                    myListrm.add(map);
                                }
                                myAdapter.arrmylist = myListrm;
                                mgv_cz_lb.setAdapter(myAdapter);
                            } else {
                                hideDialogin();
                                //  Hint(resultMsg, HintDialog.ERROR);
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


    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(ChongzhiActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        try {
            new HintDialog(ChongzhiActivity.this, R.style.dialog, sHint, type, true).show();
        } catch (Exception e) {
            hideDialogin();
            e.printStackTrace();
        }
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        public ArrayList<HashMap<String, String>> arrmylist;

        //     public ArrayList<String> arr;


        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arrmylist = new ArrayList<HashMap<String, String>>();

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
                view = inflater.inflate(R.layout.sjcz_gv_item, null);
            }
/*            map.put("ItemWp_num", jsonObject.getString("wp_num"));//
            map.put("ItemPrice", jsonObject.getString("price"));//*/
            LinearLayout ll_sjcz_item = view.findViewById(R.id.ll_sjcz_item);
            final TextView tv_sjcz_name = view.findViewById(R.id.tv_sjcz_name);
            TextView tv_sjcz_describe = view.findViewById(R.id.tv_sjcz_describe);
            tv_sjcz_name.setText(arrmylist.get(position).get("ItemPrice") + "元");
            tv_sjcz_describe.setText("等于" + arrmylist.get(position).get("ItemWp_num") + "工分");
            if (position == ItemPosition) {
                ll_sjcz_item.setBackgroundResource(R.drawable.bj_theme_8);
                sPrice = arrmylist.get(position).get("ItemWp_num");
                tv_sjcz_name.setTextColor(tv_sjcz_name.getResources().getColor(R.color.white));
                tv_sjcz_describe.setTextColor(tv_sjcz_describe.getResources().getColor(R.color.white));
            } else {
                ll_sjcz_item.setBackgroundResource(R.drawable.bk_8_9b9b9b);
                tv_sjcz_name.setTextColor(tv_sjcz_name.getResources().getColor(R.color.black));
                tv_sjcz_describe.setTextColor(tv_sjcz_describe.getResources().getColor(R.color.hui9b9b9b));
            }
            ll_sjcz_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ItemPosition = position;
                    notifyDataSetChanged();
                }
            });
            return view;
        }
    }
}
