package com.example.administrator.yunyue.zb_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
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

public class GengduoActivity extends AppCompatActivity {
    /**
     * 列表
     */
    private GridView gv_gd;
    /**
     * 返回
     */
    private LinearLayout ll_gd_back;
    RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white);
        }
        setContentView(R.layout.activity_gengduo);
        queue = Volley.newRequestQueue(GengduoActivity.this);
        gv_gd = findViewById(R.id.gv_gd);
        ll_gd_back = findViewById(R.id.ll_gd_back);
        ll_gd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        query();
    }


    /**
     * 搜索记录，热门搜索信息获取
     */
    private void query() {
        String url = Api.sUrl + "Broadcast/Api/typeAll/appId/" + Api.sApp_Id;
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
                        JSONArray jsonArraydata = jsonObject1.getJSONArray("data");
                        ArrayList<HashMap<String, String>> mylist_data = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < jsonArraydata.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArraydata.opt(i);
                            String ItemId = jsonObject2.getString("id");
                            String Itemtypename = jsonObject2.getString("typename");
                            String Itemtype_img = jsonObject2.getString("type_img");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", ItemId);
                            map.put("typename", Itemtypename);
                            map.put("type_img", Itemtype_img);
                            mylist_data.add(map);
                        }

                        setGridView(mylist_data);
                        // Hint(resultMsg, HintDialog.SUCCESS);
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

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(GengduoActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(GengduoActivity.this, R.style.dialog, sHint, type, true).show();
    }

    /**
     * 设置GirdView参数，绑定数据
     */
    private void setGridView(ArrayList<HashMap<String, String>> mylist) {
        MyAdapter myAdapter = new MyAdapter(GengduoActivity.this);
    /*    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemName", "");

            mylist.add(map);
        }*/
        myAdapter.arrlist = mylist;
        gv_gd.setAdapter(myAdapter);
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
                view = inflater.inflate(R.layout.zhibo_gengduo_item, null);
            }
            TextView tv_zhibo_gengduo_item = view.findViewById(R.id.tv_zhibo_gengduo_item);
            tv_zhibo_gengduo_item.setText(arrlist.get(position).get("typename"));
            tv_zhibo_gengduo_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GengduoActivity.this, LiebiaoActivity.class);
                    intent.putExtra("type_id", arrlist.get(position).get("id"));
                    intent.putExtra("typename", arrlist.get(position).get("typename"));
                    startActivity(intent);
                }
            });

            return view;
        }
    }
}
