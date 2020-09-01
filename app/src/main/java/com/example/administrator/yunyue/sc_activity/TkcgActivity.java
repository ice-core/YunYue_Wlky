package com.example.administrator.yunyue.sc_activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TkcgActivity extends AppCompatActivity {
    private static final String TAG = TkcgActivity.class.getSimpleName();
    RequestQueue queue = null;
    private String sUser_id;
    private SharedPreferences pref;

    private ImageView iv_fjsc_tkxq_back;
    private TextView tv_tkcg_time;
    private TextView tv_tkcg_money;
    private TextView tv_tkcg_name;
    private TextView tv_tkcg_hjmoney;
    private MyGridView gv_tkcg_sp;
    private String sId = "";
    private String resultPhone;
    private TextView tv_tkcg_order_sn;
    private TextView tv_tkcg_tkmoney;
    private TextView tv_tkcg_order_yuanyin;
    private TextView tv_tkcg_num;
    private TextView tv_tkcg_sqtime;
    private TextView tv_tkcg_bh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_tkcg);
        queue = Volley.newRequestQueue(TkcgActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        Intent intent = getIntent();
        sId = intent.getStringExtra("id");
        iv_fjsc_tkxq_back = findViewById(R.id.iv_fjsc_tkxq_back);
        tv_tkcg_time = findViewById(R.id.tv_tkcg_time);
        tv_tkcg_money = findViewById(R.id.tv_tkcg_money);
        tv_tkcg_name = findViewById(R.id.tv_tkcg_name);
        tv_tkcg_hjmoney = findViewById(R.id.tv_tkcg_hjmoney);
        gv_tkcg_sp = findViewById(R.id.gv_tkcg_sp);
        tv_tkcg_order_sn = findViewById(R.id.tv_tkcg_order_sn);
        tv_tkcg_tkmoney = findViewById(R.id.tv_tkcg_tkmoney);
        tv_tkcg_order_yuanyin = findViewById(R.id.tv_tkcg_order_yuanyin);
        tv_tkcg_num = findViewById(R.id.tv_tkcg_num);
        tv_tkcg_sqtime = findViewById(R.id.tv_tkcg_sqtime);
        tv_tkcg_bh = findViewById(R.id.tv_tkcg_bh);
        iv_fjsc_tkxq_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        dialogin("");
        huoqu(sId);
    }

    private void huoqu(String sId) {
        String url = Api.sUrl + "Api/Order/refundwin/appId/" +
                Api.sApp_Id + "/user_id/" + sUser_id + "/order_id/" + sId;
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
                        JSONObject jsonObjectdata = new JSONObject(jsonObject1.getString("data"));

                        tv_tkcg_order_sn.setText(jsonObjectdata.getString("order_no"));
                        tv_tkcg_order_yuanyin.setText(jsonObjectdata.getString("cause"));
                        tv_tkcg_num.setText(jsonObjectdata.getString("num"));
                        tv_tkcg_sqtime.setText(jsonObjectdata.getString("state_time"));
                        tv_tkcg_bh.setText(jsonObjectdata.getString("refund_no"));
                        tv_tkcg_time.setText(jsonObjectdata.getString("end_time"));
                        tv_tkcg_name.setText(jsonObjectdata.getString("shangjia_name"));
                        tv_tkcg_money.setText("￥" + jsonObjectdata.getString("new_price"));
                        tv_tkcg_hjmoney.setText("￥" + jsonObjectdata.getString("new_price"));
                        tv_tkcg_tkmoney.setText("￥" + jsonObjectdata.getString("new_price"));
                        JSONArray resultJsonArrayGoodlist = jsonObjectdata.getJSONArray("list");
                        MyAdapter myAdapter = new MyAdapter(TkcgActivity.this);
                        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                        for (int a = 0; a < resultJsonArrayGoodlist.length(); a++) {
                            JSONObject jsonObjecGoodlist = resultJsonArrayGoodlist.getJSONObject(a);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("ItemGoods_Name", jsonObjecGoodlist.getString("goods_name"));
                            map.put("ItemSpec_Sey_Name", jsonObjecGoodlist.getString("spec_key_name"));
                            map.put("ItemGoods_Price", jsonObjecGoodlist.getString("goods_price"));
                            map.put("ItemGoods_Num", jsonObjecGoodlist.getString("goods_num"));
                            mylist.add(map);
                        }
                        myAdapter.arrlist = mylist;
                        gv_tkcg_sp.setAdapter(myAdapter);


                    } else {
                        Hint(resultMsg, HintDialog.ERROR);
                    }
                } catch (JSONException e) {
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


    public void dialog(String resultPhone) {
        Dialog dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        final View inflate = LayoutInflater.from(this).inflate(R.layout.lxmj_dialog, null);
        final TextView tv_lxmj_dialog = inflate.findViewById(R.id.tv_lxmj_dialog);
        tv_lxmj_dialog.setText(resultPhone);
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
        dialog.show();
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
        loadingDialog = new LoadingDialog(TkcgActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(TkcgActivity.this, R.style.dialog, sHint, type, true).show();
    }

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
                view = inflater.inflate(R.layout.ckdd_item, null);
            }
            ImageView iv_ckdd_image = view.findViewById(R.id.iv_ckdd_image);
            TextView tv_ckdd_goods_name = view.findViewById(R.id.tv_ckdd_goods_name);
            TextView tv_ckdd_spec_key_name = view.findViewById(R.id.tv_ckdd_spec_key_name);
            TextView tv_ckdd_order_amount = view.findViewById(R.id.tv_ckdd_order_amount);
            TextView tv_ckdd_goods_num = view.findViewById(R.id.tv_ckdd_goods_num);
            Glide.with(TkcgActivity.this)
                    .load( Api.sUrl+ arrlist.get(position).get("ItemOriginal_Img"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_ckdd_image);
            tv_ckdd_goods_name.setText(arrlist.get(position).get("ItemGoods_Name"));
            tv_ckdd_spec_key_name.setText(arrlist.get(position).get("ItemSpec_Sey_Name"));
            tv_ckdd_order_amount.setText("￥" + arrlist.get(position).get("ItemGoods_Price"));
            tv_ckdd_goods_num.setText("x" + arrlist.get(position).get("ItemGoods_Num"));


            return view;
        }
    }
}
