package com.example.administrator.yunyue.sc_activity;

import android.content.Context;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

public class JymxActivity extends AppCompatActivity {

    private static final String TAG = JymxActivity.class.getSimpleName();
    private GridView gv_jymx;
    MyAdapter myAdapter;
    private SharedPreferences pref;
    RequestQueue queue = null;
    private String sUser_id;
    private ImageView iv_jymx_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_jymx);
        queue = Volley.newRequestQueue(JymxActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        gv_jymx = (GridView) findViewById(R.id.gv_jymx);
        iv_jymx_back = (ImageView) findViewById(R.id.iv_jymx_back);
        iv_jymx_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
/*        myAdapter = new MyAdapter(JymxActivity.this);
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 3; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemId", "");
            mylist.add(map);
        }
        myAdapter.arrmylist = mylist;
        gv_jymx.setAdapter(myAdapter);*/
        dialogin("");
        query();
    }

    private void back() {
/*        Intent intent = new Intent(this, ScMainActivity.class);
        intent.putExtra("ID", "4");
        startActivity(intent);*/
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

    private void query() {
        String url = Api.sUrl + "Order/payList/user_id/" + sUser_id;
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
                        MyAdapter myAdapter = new MyAdapter(JymxActivity.this);
                        ArrayList<HashMap<String, String>> myListrm = new ArrayList<HashMap<String, String>>();
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("ItemId", jsonObject.getString("id"));//id
                            map.put("ItemTitle", jsonObject.getString("title"));//
                            map.put("ItemCon", jsonObject.getString("con"));//
                            map.put("ItemCreatetime", jsonObject.getString("createtime"));//
                            map.put("ItemMoney", jsonObject.getString("money"));//

                            map.put("ItemStatus", jsonObject.getString("status"));//
                            myListrm.add(map);
                        }
                        myAdapter.arrmylist = myListrm;
                        gv_jymx.setAdapter(myAdapter);
                        // setGridView();
                        //  tv_camera_guanggao.setText(resultAdName);
                    } else {

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
        loadingDialog = new LoadingDialog(JymxActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(JymxActivity.this, R.style.dialog, sHint, type, true).show();
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
                view = inflater.inflate(R.layout.jymx_item, null);
            }
            TextView tv_jymx_title = view.findViewById(R.id.tv_jymx_title);
            TextView tv_jymx_createtime = view.findViewById(R.id.tv_jymx_createtime);
            TextView tv_jymx_money = view.findViewById(R.id.tv_jymx_money);
            tv_jymx_title.setText(arrmylist.get(position).get("ItemTitle").toString());
            tv_jymx_createtime.setText(arrmylist.get(position).get("ItemCreatetime").toString());
            if (arrmylist.get(position).get("ItemStatus").toString().equals("0")) {
                tv_jymx_money.setText("-" + arrmylist.get(position).get("ItemMoney").toString());
                tv_jymx_money.setTextColor(tv_jymx_money.getResources().getColor(R.color.black));
            } else {
                tv_jymx_money.setText("+" + arrmylist.get(position).get("ItemMoney").toString());
                tv_jymx_money.setTextColor(tv_jymx_money.getResources().getColor(R.color.theme));
            }

            return view;
        }
    }


}
