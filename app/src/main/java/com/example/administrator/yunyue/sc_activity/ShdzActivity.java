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
import com.example.administrator.yunyue.Im.HaoyouActivity;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.erci.activity.CwhyActivity;
import com.example.administrator.yunyue.erci.activity.FabukyxqActivity;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ShdzActivity extends AppCompatActivity {
    private static final String TAG = ShdzActivity.class.getSimpleName();
    private GridView gv_shdz;
    private SharedPreferences pref;
    private String sUser_id = "";
    private String sIs_vip = "";
    MyAdapter myAdapter;
    RequestQueue queue = null;
    private Button bt_shdz_xzdz;
    private ImageView iv_shdz_back;
    public static ArrayList<HashMap<String, String>> mylistQrdd;
    private String sActivity = "";
    private String sAmount = "";
    private LinearLayout ll_shdz_kong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_shdz);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(ShdzActivity.this);
        sUser_id = pref.getString("user_id", "");
        sIs_vip = pref.getString("is_vip", "");
        /*activity*/
        Intent intent = getIntent();
        sActivity = intent.getStringExtra("activity");
        sAmount = intent.getStringExtra("amount");
        gv_shdz = (GridView) findViewById(R.id.gv_shdz);
        bt_shdz_xzdz = (Button) findViewById(R.id.bt_shdz_xzdz);
        iv_shdz_back = (ImageView) findViewById(R.id.iv_shdz_back);
        ll_shdz_kong = findViewById(R.id.ll_shdz_kong);
        iv_shdz_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        bt_shdz_xzdz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(ShdzActivity.this, XjshdzActivity.class);
                    intent.putExtra("activity", sActivity);
                    intent.putExtra("amount", sAmount);
                    XjshdzActivity.mylistQrdd = mylistQrdd;
                    startActivity(intent);

            }
        });

        dialogin("");

        query();

    }

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initview();
    }

    private void initview() {
        query();
    }

    private void back() {
        if (sActivity == null) {
            finish();
        } else if (sActivity.equals("")) {
            finish();
        } else {
   /*         Intent intent = new Intent(ShdzActivity.this, TxddActivity.class);
            intent.putExtra("amount", sAmount);*/
            QrddActivity.mylist = mylistQrdd;
            //  startActivity(intent);
            finish();
        }

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
        String url = Api.sUrl + "Api/Order/addresslist/appId/" + Api.sApp_Id + "/user_id/" + sUser_id;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                myAdapter = new MyAdapter(ShdzActivity.this);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                    if (resultCode > 0) {
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");

                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            String resultId = jsonObject.getString("id");
                            String resultName = jsonObject.getString("username");
                            String resultMobile = jsonObject.getString("phone");
                            String resultSheng = jsonObject.getString("sheng");
                            String resultShi = jsonObject.getString("shi");
                            String resultQu = jsonObject.getString("qu");
                            String resultDetail = jsonObject.getString("detail");
                            String resultIsDefault = jsonObject.getString("is_default");

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("ItemId", resultId);
                            map.put("ItemName", resultName);
                            map.put("ItemMobile", resultMobile);
                            map.put("ItemSheng", resultSheng);
                            map.put("ItemShi", resultShi);
                            map.put("ItemQu", resultQu);
                            map.put("ItemDetail", resultDetail);
                            map.put("ItemIsDefault", resultIsDefault);
                            mylist.add(map);
                        }
                        myAdapter.mylist = mylist;
                        gv_shdz.setAdapter(myAdapter);
                        ll_shdz_kong.setVisibility(View.GONE);
                        if (mylist.size() == 0) {
                            ll_shdz_kong.setVisibility(View.VISIBLE);
                            gv_shdz.setVisibility(View.GONE);
                        }
                    } else {
                        ll_shdz_kong.setVisibility(View.GONE);
                        if (mylist.size() == 0) {
                            ll_shdz_kong.setVisibility(View.VISIBLE);
                            gv_shdz.setVisibility(View.GONE);
                        }
                        Hint(resultMsg, HintDialog.ERROR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Hint(String.valueOf(volleyError), HintDialog.ERROR);
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(ShdzActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(ShdzActivity.this, R.style.dialog, sHint, type, true).show();
    }


    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        public ArrayList<HashMap<String, String>> mylist;

        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            mylist = new ArrayList<HashMap<String, String>>();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mylist.size();
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
                view = inflater.inflate(R.layout.lsdz_item, null);
            }
            LinearLayout ll_lsdz = view.findViewById(R.id.ll_lsdz);
            TextView tv_shdz_name = view.findViewById(R.id.tv_shdz_name);
            TextView tv_shdz_phone = view.findViewById(R.id.tv_shdz_phone);
            TextView tv_shdz_isdefault = view.findViewById(R.id.tv_shdz_isdefault);
            TextView tv_shdz_site = view.findViewById(R.id.tv_shdz_site);
            ImageView iv_shdz_bianji = view.findViewById(R.id.iv_shdz_bianji);
            tv_shdz_name.setText(mylist.get(position).get("ItemName"));
            tv_shdz_phone.setText(mylist.get(position).get("ItemMobile"));
            if (mylist.get(position).get("ItemIsDefault").equals("1")) {
                tv_shdz_isdefault.setVisibility(View.VISIBLE);
            } else {
                tv_shdz_isdefault.setVisibility(View.GONE);
            }
            tv_shdz_site.setText(mylist.get(position).get("ItemSheng") + mylist.get(position).get("ItemShi")
                    + mylist.get(position).get("ItemQu") + mylist.get(position).get("ItemDetail"));
            iv_shdz_bianji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ShdzActivity.this, XjshdzActivity.class);
                    intent.putExtra("id", String.valueOf(position));
                    intent.putExtra("activity", sActivity);
                    intent.putExtra("amount", sAmount);
                    XjshdzActivity.mylistQrdd = mylistQrdd;
                    XjshdzActivity.mylist = mylist;
                    startActivity(intent);
                    //  finish();
                }
            });
            ll_lsdz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (sActivity == null) {
                        FabukyxqActivity.sService_place = mylist.get(position).get("ItemSheng") + mylist.get(position).get("ItemShi")
                                + mylist.get(position).get("ItemQu") + mylist.get(position).get("ItemDetail");
                    } else if (sActivity.equals("")) {
                        FabukyxqActivity.sService_place = mylist.get(position).get("ItemSheng") + mylist.get(position).get("ItemShi")
                                + mylist.get(position).get("ItemQu") + mylist.get(position).get("ItemDetail");
                    } else {
                        TxddActivity.mylistShdz = mylist;
                        TxddActivity.sId = String.valueOf(position);
                        //   startActivity(intent);
                    }
                    finish();
                }
            });
            return view;
        }
    }
}
