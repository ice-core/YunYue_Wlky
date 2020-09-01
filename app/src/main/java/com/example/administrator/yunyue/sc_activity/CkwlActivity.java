package com.example.administrator.yunyue.sc_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
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

public class CkwlActivity extends AppCompatActivity {
    private static final String TAG = CkwlActivity.class.getSimpleName();
    private ListView lv_ckwl;
    private ImageView iv_ckwl_back;
    private String sDh = "";
    RequestQueue queue = null;
    /**
     * 快递单号
     */
    private TextView tv_ckwl_logistic_code;
    /**
     * 快递公司
     */
    private TextView tv_ckwl_name;
    /**
     * 空
     */
    private TextView tv_ckwl_kong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_ckwl);
        queue = Volley.newRequestQueue(CkwlActivity.this);
        Intent intent = getIntent();
        sDh = intent.getStringExtra("dh");
        lv_ckwl = findViewById(R.id.lv_ckwl);
        iv_ckwl_back = (ImageView) findViewById(R.id.iv_ckwl_back);
        tv_ckwl_logistic_code = findViewById(R.id.tv_ckwl_logistic_code);
        tv_ckwl_name = findViewById(R.id.tv_ckwl_name);
        tv_ckwl_kong = findViewById(R.id.tv_ckwl_kong);
        iv_ckwl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        tv_ckwl_kong.setVisibility(View.GONE);
        hideDialogin();
        dialogin("");
        query();
    }


    private void query() {
        String url = Api.sUrl + "Api/order/getkuaidi/appId/" + Api.sApp_Id
                + "/num/" + sDh;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        JSONObject jsonObjectdate = jsonObject1.getJSONObject("data");
                        String LogisticCode = jsonObjectdate.getString("LogisticCode");
                        tv_ckwl_logistic_code.setText(LogisticCode);
                        String ShipperCode = jsonObjectdate.getString("ShipperCode");
                        String State = jsonObjectdate.getString("State");
                        String EBusinessID = jsonObjectdate.getString("EBusinessID");
                        String Success = jsonObjectdate.getString("Success");
                        String name = jsonObjectdate.getString("name");
                        tv_ckwl_name.setText(name);
                        JSONArray resultJsonArray = jsonObjectdate.getJSONArray("Traces");
                        MyAdapter myAdapter = new MyAdapter(CkwlActivity.this);
                        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            JSONObject jsonObjectTraces = resultJsonArray.getJSONObject(i);
                            String resultTime = jsonObjectTraces.getString("AcceptTime");
                            String resultContext = jsonObjectTraces.getString("AcceptStation");
                            //  String resultLocation = jsonObject.getString("location");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("ItemTime", resultTime);
                            map.put("ItemContext", resultContext);
                            //  map.put("ItemLocation", resultLocation);
                            mylist.add(map);
                        }
                        myAdapter.arrlist = mylist;
                        lv_ckwl.setAdapter(myAdapter);
                        setListViewHeightBasedOnChildren(lv_ckwl);
                        if (mylist.size() == 0) {
                            tv_ckwl_kong.setVisibility(View.VISIBLE);
                        } else {
                            tv_ckwl_kong.setVisibility(View.GONE);
                        }

                        //    Hint(resultMsg);
                    } else {
                        Hint(resultMsg, HintDialog.ERROR);
                        // Toast.makeText(NoteActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "response -> " + jsonObject.toString());
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
     * listview自适应高度
     */
    public void setListViewHeightBasedOnChildren(ListView listView1) {
        BaseAdapter listAdapter = (BaseAdapter) listView1.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        //获取listView的宽度
        ViewGroup.LayoutParams params = listView1.getLayoutParams();
        int listViewWidth =getWindowManager().getDefaultDisplay().getWidth();
        int widthSpec = View.MeasureSpec.makeMeasureSpec(listViewWidth, View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView1);
            //给item的measure设置参数是listView的宽度就可以获取到真正每一个item的高度
            listItem.measure(widthSpec, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        params.height = totalHeight + (listView1.getDividerHeight() * (listAdapter.getCount() + 1));
        listView1.setLayoutParams(params);
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
        loadingDialog = new LoadingDialog(CkwlActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(CkwlActivity.this, R.style.dialog, sHint, type, true).show();
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
                view = inflater.inflate(R.layout.ckwl_item, null);
            }
            TextView tv_ckwl_bg = view.findViewById(R.id.tv_ckwl_bg);
            TextView tv_ckwl_time = view.findViewById(R.id.tv_ckwl_time);
            TextView tv_ckwl_context = view.findViewById(R.id.tv_ckwl_context);
            ImageView iv_ckwl = view.findViewById(R.id.iv_ckwl);
            if (position == 0) {
                tv_ckwl_bg.setVisibility(View.INVISIBLE);
                iv_ckwl.setImageResource(R.mipmap.zfcg);
            } else {
                tv_ckwl_bg.setVisibility(View.VISIBLE);
                iv_ckwl.setImageResource(R.mipmap.paisongz);
            }
            tv_ckwl_time.setText(arrlist.get(position).get("ItemTime"));
            tv_ckwl_context.setText(arrlist.get(position).get("ItemContext"));
            return view;
        }
    }
}
