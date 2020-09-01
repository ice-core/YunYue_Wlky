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
import android.widget.GridView;
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

public class YhkglActivity extends AppCompatActivity {
    private static final String TAG = YhkglActivity.class.getSimpleName();

    GridView gv_yhkgl;
    MyAdapter myAdapter;
    private String isCb;
    private TextView tv_yhkgl_bj;
    private Button bt_yhkgl;
    private LinearLayout ll_yhkgl_tjyhk;
    RequestQueue queue = null;
    private String sUser_id;

    private SharedPreferences pref;
    public ArrayList<String> arrCb;
    private ImageView iv_yhkgl_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_yhkgl);
        queue = Volley.newRequestQueue(YhkglActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        gv_yhkgl = (GridView) findViewById(R.id.gv_yhkgl);
        tv_yhkgl_bj = (TextView) findViewById(R.id.tv_yhkgl_bj);
        bt_yhkgl = (Button) findViewById(R.id.bt_yhkgl);
        ll_yhkgl_tjyhk = (LinearLayout) findViewById(R.id.ll_yhkgl_tjyhk);
        iv_yhkgl_back = (ImageView) findViewById(R.id.iv_yhkgl_back);
        iv_yhkgl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        bt_yhkgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = "";
                for (int i = 0; i < arrCb.size(); i++) {
                    if (arrCb.get(i).equals("1")) {
                        id = myAdapter.arrmylist.get(i).get("ItemId");
                    }
                }
                dialogin("");
                delete(id);
            }
        });

        isCb = "1";
        tv_yhkgl_bj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCb.equals("1")) {
                    arrCb = new ArrayList<String>();
                    for (int i = 0; i < myAdapter.arrmylist.size(); i++) {
                        arrCb.add("0");
                    }

                    bt_yhkgl.setVisibility(View.VISIBLE);
                    ll_yhkgl_tjyhk.setVisibility(View.GONE);
                    isCb = "0";
                } else {
                    bt_yhkgl.setVisibility(View.GONE);
                    ll_yhkgl_tjyhk.setVisibility(View.VISIBLE);
                    isCb = "1";
                }
                myAdapter.notifyDataSetChanged();
            }
        });
        ll_yhkgl_tjyhk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(YhkglActivity.this, TjyhkActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialogin("");
        huoqu();
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

    private void huoqu() {
        String url = Api.sUrl + "Bank/getBankList/user_id/" + sUser_id;
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
                        arrCb = new ArrayList<String>();
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("result");
                        myAdapter = new MyAdapter(YhkglActivity.this);
                        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("ItemId", jsonObject.getString("id"));
                            map.put("ItemBank_No", jsonObject.getString("bank_no"));
                            map.put("ItemBank_Name", jsonObject.getString("bank_name"));
                            map.put("ItemUser_Id", jsonObject.getString("user_id"));
                            map.put("ItemAdd_Time", jsonObject.getString("add_time"));
                            map.put("ItemType", jsonObject.getString("type"));

                            mylist.add(map);
                            arrCb.add("0");
                        }
                        bt_yhkgl.setVisibility(View.GONE);
                        ll_yhkgl_tjyhk.setVisibility(View.VISIBLE);
                        isCb = "1";
                        myAdapter.arrmylist = mylist;
                        gv_yhkgl.setAdapter(myAdapter);


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

    private void delete(String sId) {
        String url = Api.sUrl + "Bank/delBank/user_id/" + sUser_id + "/id/" + sId;
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
                        dialogin("");
                        huoqu();
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


    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(YhkglActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(YhkglActivity.this, R.style.dialog, sHint, type, true).show();
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
                view = inflater.inflate(R.layout.yhkgl_item, null);
            }
            TextView tv_yhkgl_r = view.findViewById(R.id.tv_yhkgl_r);
            final CheckBox cb_yhkgl_ye = view.findViewById(R.id.cb_yhkgl_ye);
            TextView tv_yhkgl_name = view.findViewById(R.id.tv_yhkgl_name);
            TextView tv_yhkgl_no = view.findViewById(R.id.tv_yhkgl_no);
            TextView tv_yhkgl_type = view.findViewById(R.id.tv_yhkgl_type);
            if (isCb.equals("1")) {
                cb_yhkgl_ye.setVisibility(View.GONE);
                tv_yhkgl_r.setVisibility(View.VISIBLE);
            } else {
                cb_yhkgl_ye.setVisibility(View.VISIBLE);
                tv_yhkgl_r.setVisibility(View.GONE);
            }
            if (arrCb.get(position).equals("1")) {
                cb_yhkgl_ye.setChecked(true);
            } else {
                cb_yhkgl_ye.setChecked(false);
            }

            cb_yhkgl_ye.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < arrCb.size(); i++) {
                        if (i == position) {
                            arrCb.set(i, "1");
                        } else {
                            arrCb.set(i, "0");
                        }
                    }
                    notifyDataSetChanged();
                }
            });

            tv_yhkgl_type.setText(arrmylist.get(position).get("ItemType"));
            tv_yhkgl_name.setText(arrmylist.get(position).get("ItemBank_Name"));
            tv_yhkgl_no.setText(arrmylist.get(position).get("ItemBank_No"));
            return view;
        }
    }
}
