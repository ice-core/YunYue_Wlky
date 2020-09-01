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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.yunyue.Api;
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

public class HbkjActivity extends AppCompatActivity {
    private static final String TAG = HbkjActivity.class.getSimpleName();
    private GridView gv_hbkj;
    private SharedPreferences pref;
    RequestQueue queue = null;
    private String sUser_id;
    private LinearLayout ll_hbkj_back;
    private LinearLayout ll_hbkj_sjh;
    private Button bt_hbkj_zr;
    private TextView tv_hbkj_hb, tv_hbkj_kj;
    private MyAdapter myAdapter;
    private String sType = "";
    private EditText et_hbkj_mobile;
    private String sCid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_hbkj);
        //sType = "hb";
        queue = Volley.newRequestQueue(HbkjActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        gv_hbkj = (GridView) findViewById(R.id.gv_hbkj);
        bt_hbkj_zr = (Button) findViewById(R.id.bt_hbkj_zr);
        tv_hbkj_hb = (TextView) findViewById(R.id.tv_hbkj_hb);
        tv_hbkj_kj = (TextView) findViewById(R.id.tv_hbkj_kj);
        ll_hbkj_sjh = (LinearLayout) findViewById(R.id.ll_hbkj_sjh);
        ll_hbkj_back = findViewById(R.id.ll_hbkj_back);
        et_hbkj_mobile = (EditText) findViewById(R.id.et_hbkj_mobile);
        myAdapter = new MyAdapter(HbkjActivity.this);
        myAdapter.cb = "0";
        ll_hbkj_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        tv_hbkj_kj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sType = "kj";
                dialogin("");
                query();
                tv_hbkj_kj.setTextColor(tv_hbkj_kj.getResources().getColor(R.color.theme));
                tv_hbkj_hb.setTextColor(tv_hbkj_hb.getResources().getColor(R.color.black));
                bt_hbkj_zr.setVisibility(View.GONE);
                ll_hbkj_sjh.setVisibility(View.GONE);
            }
        });

        tv_hbkj_hb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hongbao();
            }
        });

        bt_hbkj_zr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt_hbkj_zr.getText().equals("转让红包")) {
                    myAdapter.cb = "1";
                    myAdapter.notifyDataSetChanged();
                    ll_hbkj_sjh.setVisibility(View.VISIBLE);
                    bt_hbkj_zr.setText("确认转让");
                } else {
                    if (sCid.equals("")) {
                        Hint("未选择红包！", HintDialog.WARM);
                    } else {
                        dialogin("");
                        zhuanrang();
                    }
                }
            }
        });
        sType = "kj";
        hideDialogin();
        dialogin("");
        query();
        tv_hbkj_kj.setTextColor(tv_hbkj_kj.getResources().getColor(R.color.theme));
        tv_hbkj_hb.setTextColor(tv_hbkj_hb.getResources().getColor(R.color.black));
        bt_hbkj_zr.setVisibility(View.GONE);
        ll_hbkj_sjh.setVisibility(View.GONE);
/*        dialogin("");
      *//*  query();*/
    }

    private void hongbao() {
        sType = "hb";
        dialogin("");
        query();
        myAdapter.notifyDataSetChanged();
        tv_hbkj_kj.setTextColor(tv_hbkj_kj.getResources().getColor(R.color.black));
        tv_hbkj_hb.setTextColor(tv_hbkj_hb.getResources().getColor(R.color.theme));
        bt_hbkj_zr.setVisibility(View.VISIBLE);
        ll_hbkj_sjh.setVisibility(View.GONE);
        bt_hbkj_zr.setText("转让红包");
    }

    private void back() {
        if (sType.equals("hb")) {
            if (bt_hbkj_zr.getText().equals("确认转让")) {
                myAdapter.cb = "0";
                myAdapter.notifyDataSetChanged();
                ll_hbkj_sjh.setVisibility(View.GONE);
                bt_hbkj_zr.setText("转让红包");
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return isCosumenBackKey();
        }
        return false;
    }

    private boolean isCosumenBackKey() {
        back();
        // 这儿做返回键的控制，如果自己处理返回键逻辑就返回true，如果返回false,代表继续向下传递back事件，由系统去控制
        return true;
    }

    private void zhuanrang() {
        String url = Api.sUrl + "Account/transfer";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", et_hbkj_mobile.getText().toString());
        params.put("cid", sCid);
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
                                hongbao();
                            }
                            Hint(resultMsg, HintDialog.ERROR);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "response -> " + response.toString());
                        //   Toast.makeText(HzrzActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialogin();
                Hint(error.toString(), HintDialog.ERROR);
                Log.e(TAG, error.getMessage(), error);
            }
        });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonRequest);
    }

    private void query() {
        String url = Api.sUrl + "Api/Good/usercoupons/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id;
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
                        myAdapter = new MyAdapter(HbkjActivity.this);
                        myAdapter.cb = "0";
                        ArrayList<HashMap<String, String>> myListrm = new ArrayList<HashMap<String, String>>();
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("ItemId", jsonObject.getString("id"));//id
                            // map.put("ItemType", jsonObject.getString("type"));//
                            map.put("ItemMoney", jsonObject.getString("youhui"));//
                            map.put("ItemCondition", jsonObject.getString("zuidi"));//
                            map.put("ItemUse_Start_Time", jsonObject.getString("star"));//

                            map.put("ItemShangjia_Id", jsonObject.getString("shangjia_id"));//

                            map.put("ItemUse_End_Time", jsonObject.getString("end"));//
                            myListrm.add(map);
                            myAdapter.arrCb.add("0");
                        }
                        myAdapter.arrmylist = myListrm;
                        gv_hbkj.setAdapter(myAdapter);
                        // setGridView();
                        //  tv_camera_guanggao.setText(resultAdName);
                    } else {
                        myAdapter = null;
                        gv_hbkj.setAdapter(myAdapter);

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
        loadingDialog = new LoadingDialog(HbkjActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(HbkjActivity.this, R.style.dialog, sHint, type, true).show();
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        public ArrayList<HashMap<String, String>> arrmylist;

        private String cb = "";
        public ArrayList<String> arrCb;

        //     public ArrayList<String> arr;

        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arrmylist = new ArrayList<HashMap<String, String>>();
            arrCb = new ArrayList<String>();

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
                view = inflater.inflate(R.layout.hbkj_item, null);
            }
            final CheckBox cb_hbkj = view.findViewById(R.id.cb_hbkj);
            TextView tv_hbkj = view.findViewById(R.id.tv_hbkj);
            TextView tv_hbkj_money = view.findViewById(R.id.tv_hbkj_money);
            TextView tv_hbkj_key = view.findViewById(R.id.tv_hbkj_key);
            TextView tv_hbkj_yxq = view.findViewById(R.id.tv_hbkj_yxq);
            TextView tv_hbkj_ljsy = view.findViewById(R.id.tv_hbkj_ljsy);
            tv_hbkj_money.setText(arrmylist.get(position).get("ItemMoney"));
            tv_hbkj_key.setText("满" + arrmylist.get(position).get("ItemCondition") + "可用");
            tv_hbkj_yxq.setText("有效期至：" + arrmylist.get(position).get("ItemUse_End_Time"));
            if (cb.equals("0")) {
                cb_hbkj.setVisibility(View.GONE);
                tv_hbkj.setVisibility(View.VISIBLE);
            } else {
                cb_hbkj.setVisibility(View.VISIBLE);
                tv_hbkj.setVisibility(View.GONE);
            }
            if (arrCb.get(position).equals("0")) {
                cb_hbkj.setChecked(false);
            } else {
                cb_hbkj.setChecked(true);
            }
            tv_hbkj_ljsy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HbkjActivity.this, DpzyActivity.class);
                    intent.putExtra("id", arrmylist.get(position).get("ItemShangjia_Id"));
                    startActivity(intent);
                }
            });
            if (arrmylist.get(position).get("ItemShangjia_Id").equals("0")) {
                tv_hbkj_ljsy.setVisibility(View.GONE);
            } else {
                tv_hbkj_ljsy.setVisibility(View.VISIBLE);
            }
            cb_hbkj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < arrmylist.size(); i++) {
                        if (i == position) {
                            if (cb_hbkj.isChecked()) {
                                arrCb.set(position, "1");
                                sCid = arrmylist.get(position).get("ItemId");
                            } else {
                                arrCb.set(position, "0");
                                sCid = "";
                            }
                        } else {
                            arrCb.set(i, "0");
                        }
                    }
                    notifyDataSetChanged();
                }
            });
            return view;
        }
    }
}
