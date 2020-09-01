package com.example.administrator.yunyue.sc_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.Im.HaoyouActivity;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.erci.activity.CwhyActivity;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SjlbActivity extends AppCompatActivity {
    private static final String TAG = SjlbActivity.class.getSimpleName();
    private LinearLayout ll_sjlb_back;
    private TextView tv_sjlb_wyrz;
    private MyGridView gv_sjlb;
    RequestQueue queue = null;
    private SharedPreferences pref;
    private String sUser_id = "";
    private String sIs_vip = "";
    private ImageView iv_sjlb_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_sjlb);
        queue = Volley.newRequestQueue(SjlbActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        sIs_vip = pref.getString("is_vip", "");
        ll_sjlb_back = (LinearLayout) findViewById(R.id.ll_sjlb_back);
        tv_sjlb_wyrz = (TextView) findViewById(R.id.tv_sjlb_wyrz);
        gv_sjlb = (MyGridView) findViewById(R.id.gv_sjlb);
        iv_sjlb_back = (ImageView) findViewById(R.id.iv_sjlb_back);
        iv_sjlb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        ll_sjlb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        tv_sjlb_wyrz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(SjlbActivity.this, SjrzActivity.class);
                    startActivity(intent);

            }
        });
        hideDialogin();
        dialogin("");
        liebiao();
    }

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initview();
    }

    private void initview() {
        // dialogin("");
        liebiao();
    }

    /**
     * 获取商家列表
     */
    private void liebiao() {
        String url = Api.sUrl + "Api/Good/userenter/appId/" + Api.sApp_Id + "/user_id/" + sUser_id;
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
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                        MyAdapter myAdapter = new MyAdapter(SjlbActivity.this);
                        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            String resultId = jsonObject.getString("id");
                            String resultStorename = jsonObject.getString("shangjianame");
                            String resultAddress = jsonObject.getString("address");
                            String resultStatus = jsonObject.getString("is_shenghe");
                            String resultCname = jsonObject.getString("typename");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("ItemId", resultId);
                            map.put("ItemStorename", resultStorename);
                            map.put("ItemAddress", resultAddress);
                            map.put("ItemStatus", resultStatus);
                            map.put("ItemCname", resultCname);
                            map.put("ItemType", "0");
                            mylist.add(map);
                        }
                        myAdapter.arrlist = mylist;
                        gv_sjlb.setAdapter(myAdapter);
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

    private void back() {
        finish();
    }

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
        loadingDialog = new LoadingDialog(SjlbActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(SjlbActivity.this, R.style.dialog, sHint, type, true).show();
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
                view = inflater.inflate(R.layout.sjlb_item, null);
            }
            LinearLayout ll_sjlb_item = (LinearLayout) view.findViewById(R.id.ll_sjlb_item);
            TextView tv_sjlb_storename = (TextView) view.findViewById(R.id.tv_sjlb_storename);
            TextView tv_sjlb_address = (TextView) view.findViewById(R.id.tv_sjlb_address);
            TextView tv_sjlb_status = (TextView) view.findViewById(R.id.tv_sjlb_status);
            TextView tv_sjlb_category = (TextView) view.findViewById(R.id.tv_sjlb_category);
            //    LinearLayout ll_sjlb_status = (LinearLayout) view.findViewById(R.id.ll_sjlb_status);
     /*       if (arrlist.get(position).get("ItemStatus").toString().equals("0")) {
                tv_sjlb_status.setBackgroundResource(R.drawable.zt_dsh);
                tv_sjlb_status.setText("待支付");
                tv_sjlb_status.setTextColor(tv_sjlb_status.getResources().getColor(R.color.gray));
            } else */
            if (arrlist.get(position).get("ItemStatus").toString().equals("0")) {
                tv_sjlb_status.setBackgroundResource(R.drawable.zt_dsh);
                tv_sjlb_status.setText("审核中");
                tv_sjlb_status.setTextColor(tv_sjlb_status.getResources().getColor(R.color.gray));
            } else if (arrlist.get(position).get("ItemStatus").toString().equals("1")) {
                tv_sjlb_status.setBackgroundResource(R.drawable.zt_tg);
                tv_sjlb_status.setText("已通过");
                tv_sjlb_status.setTextColor(tv_sjlb_status.getResources().getColor(R.color.huang));
            } else if (arrlist.get(position).get("ItemStatus").toString().equals("2")) {
                tv_sjlb_status.setBackgroundResource(R.drawable.zt_wtg);
                tv_sjlb_status.setText("未通过");
                tv_sjlb_status.setTextColor(tv_sjlb_status.getResources().getColor(R.color.white_c));
            }
            tv_sjlb_storename.setText(arrlist.get(position).get("ItemStorename").toString());
            tv_sjlb_address.setText(arrlist.get(position).get("ItemAddress").toString());
            tv_sjlb_category.setText("类目：" + arrlist.get(position).get("ItemCname").toString());
            ll_sjlb_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
           /*         if (arrlist.get(position).get("ItemStatus").toString().equals("0")) {
                        Intent intent = new Intent(SjlbActivity.this, TjcgActivity.class);
                        intent.putExtra("residence_id", arrlist.get(position).get("ItemId").toString());
                        startActivity(intent);
                    } else {*/
                    String state = "";
                    if (arrlist.get(position).get("ItemStatus").toString().equals("1")) {
                        state = "tg";
                    } else if (arrlist.get(position).get("ItemStatus").toString().equals("2")) {
                        state = "wtg";
                    } else if (arrlist.get(position).get("ItemStatus").toString().equals("0")) {
                        state = "dsh";
                    }
                    Intent intent = new Intent(SjlbActivity.this, ShztActivity.class);
                    ShztActivity.arrlist = arrlist;
                    intent.putExtra("position", String.valueOf(position));
                    intent.putExtra("state", state);
                    startActivity(intent);

                    //  }
                }
            });
            return view;
        }
    }
}
