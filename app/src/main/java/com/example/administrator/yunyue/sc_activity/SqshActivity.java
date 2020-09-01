package com.example.administrator.yunyue.sc_activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SqshActivity extends AppCompatActivity {
    private static final String TAG = SqshActivity.class.getSimpleName();
    private LinearLayout ll_shfw_jtk;
    private LinearLayout ll_shfw_thtk;
    private ImageView iv_qgsc_shfw_back;
    RequestQueue queue = null;
    private String sUser_id;
    private SharedPreferences pref;
    private String sId;
    private String sActivity = "";
    private String sShippingCode = "";
    private MyGridView gv_qgsc_shfw_sp;
    private LinearLayout ll_shfw_lxkf;
    private LinearLayout ll_shfw_thtk_1;
    private String sState = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_sqsh);
        queue = Volley.newRequestQueue(SqshActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        Intent intent = getIntent();
        sActivity = intent.getStringExtra("activity");
        sId = intent.getStringExtra("id");
        sState = intent.getStringExtra("state");
        ll_shfw_jtk = findViewById(R.id.ll_shfw_jtk);
        ll_shfw_thtk = findViewById(R.id.ll_shfw_thtk);
        iv_qgsc_shfw_back = findViewById(R.id.iv_qgsc_shfw_back);
        gv_qgsc_shfw_sp = findViewById(R.id.gv_qgsc_shfw_sp);
        ll_shfw_lxkf = findViewById(R.id.ll_shfw_lxkf);
        ll_shfw_thtk_1 = findViewById(R.id.ll_shfw_thtk_1);
        ll_shfw_jtk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SqshActivity.this, SqtkActivity.class);
                intent.putExtra("type", "1");
                intent.putExtra("id", sId);
                intent.putExtra("state", sState);
                startActivity(intent);
                finish();
            }
        });
        ll_shfw_thtk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SqshActivity.this, SqtkActivity.class);
                intent.putExtra("type", "2");
                intent.putExtra("id", sId);
                intent.putExtra("state", sState);
                startActivity(intent);
                finish();
            }
        });
        iv_qgsc_shfw_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        ll_shfw_lxkf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* dialog(CkddActivity.resultPhone);*/
            }
        });
        dialogin("");
        huoqu(sId, sState);
    }

    public void dialog(final String phone) {
        Dialog dialog = new Dialog(SqshActivity.this, R.style.ActionSheetDialogStyle);
        final View inflate = LayoutInflater.from(SqshActivity.this).inflate(R.layout.lxmj_dialog, null);
        final TextView tv_lxmj_dialog = inflate.findViewById(R.id.tv_lxmj_dialog);
        tv_lxmj_dialog.setText(phone);
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
        dialog.show();
        tv_lxmj_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diallPhone(phone);
            }
        });
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void diallPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    private void huoqu(String sId, String sState) {
        String url = Api.sUrl + "Api/Order/orderservice/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/order_id/" + sId + "/state/" + sState;
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
                        JSONObject jsonObjectdate = jsonObject1.getJSONObject("data");
                        JSONArray resultJsonArraylist = jsonObjectdate.getJSONArray("list");
                        MyAdapter myAdapter = new MyAdapter(SqshActivity.this);
                        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < resultJsonArraylist.length(); i++) {
                            JSONObject jsonObjectResultlist = resultJsonArraylist.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            //商品id
                            map.put("ItemGoods_id", jsonObjectResultlist.getString("goods_id"));
                            //商品图片
                            map.put("ItemGood_pic", jsonObjectResultlist.getString("good_pic"));
                            //商品名称
                            map.put("ItemGoods_Name", jsonObjectResultlist.getString("goods_name"));
                            //商品规格
                            map.put("ItemSpec_Sey_Name", jsonObjectResultlist.getString("spec_key_name"));
                            //商品金额
                            map.put("ItemGoods_Price", jsonObjectResultlist.getString("goods_price"));
                            //商品数量
                            map.put("ItemGoods_Num", jsonObjectResultlist.getString("goods_num"));

                            mylist.add(map);
                        }
                        myAdapter.arrlist = mylist;
                        gv_qgsc_shfw_sp.setAdapter(myAdapter);

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
        loadingDialog = new LoadingDialog(SqshActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(SqshActivity.this, R.style.dialog, sHint, type, true).show();
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
                view = inflater.inflate(R.layout.sqtk_item, null);
            }
        /*    map.put("ItemGoods_id", jsonObjectResultlist.getString("goods_id"));
            //商品图片
            map.put("ItemGood_pic", jsonObjectResultlist.getString("good_pic"));
            //商品名称
            map.put("ItemGoods_Name", jsonObjectResultlist.getString("goods_name"));
            //商品规格
            map.put("ItemSpec_Sey_Name", jsonObjectResultlist.getString("spec_key_name"));
            //商品金额
            map.put("ItemGoods_Price", jsonObjectResultlist.getString("goods_price"));
            //商品数量
            map.put("ItemGoods_Num", jsonObjectResultlist.getString("goods_num"));*/

            ImageView iv_ckdd_image = view.findViewById(R.id.iv_ckdd_image);
            TextView tv_ckdd_goods_name = view.findViewById(R.id.tv_ckdd_goods_name);
            //  TextView tv_ckdd_spec_key_name = view.findViewById(R.id.tv_ckdd_spec_key_name);
            TextView tv_ckdd_order_amount = view.findViewById(R.id.tv_ckdd_order_amount);
            TextView tv_ckdd_goods_num = view.findViewById(R.id.tv_ckdd_goods_num);
            Glide.with(SqshActivity.this)
                    .load( Api.sUrl+ arrlist.get(position).get("ItemGood_pic"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_ckdd_image);
            tv_ckdd_goods_name.setText(arrlist.get(position).get("ItemGoods_Name"));
            // tv_ckdd_spec_key_name.setText(arrlist.get(position).get("ItemSpec_Sey_Name"));
            tv_ckdd_order_amount.setText("￥" + arrlist.get(position).get("ItemGoods_Price"));
            tv_ckdd_goods_num.setText(arrlist.get(position).get("ItemGoods_Num"));

            return view;
        }
    }
}