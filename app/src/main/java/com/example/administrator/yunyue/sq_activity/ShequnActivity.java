package com.example.administrator.yunyue.sq_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
import com.bumptech.glide.Glide;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.erci.activity.CwhyActivity;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ShequnActivity extends AppCompatActivity {
    /**
     * 返回
     */
    private LinearLayout ll_shequn_back;

    /**
     * 新建
     */
    private LinearLayout ll_shequn_xj;
    private GridView gv_shequn;
    RequestQueue queue = null;
    private EditText et_shequn_query;
    private SharedPreferences pref;
    private String sIs_vip = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_shequn);
        queue = Volley.newRequestQueue(ShequnActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        sIs_vip = pref.getString("is_vip", "");
        ll_shequn_back = findViewById(R.id.ll_shequn_back);
        ll_shequn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ll_shequn_xj = findViewById(R.id.ll_shequn_xj);
        ll_shequn_xj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(ShequnActivity.this, XjsqActivity.class);
                    startActivity(intent);

            }
        });
        gv_shequn = findViewById(R.id.gv_shequn);
        et_shequn_query = findViewById(R.id.et_shequn_query);

        et_shequn_query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO) {
                    String sQueryText = et_shequn_query.getText().toString();

                    if (sQueryText.equals("")) {
                    } else {
                        hideDialogin();
                        dialogin("");
                        query(sQueryText);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    }
                }
                return false;
            }
        });


        query("");
    }

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initview();
    }

    private void initview() {
        query("");
    }

    private void query(String name) {
        String url = Api.sUrl + "Community/Api/shequnLists/appId/" + Api.sApp_Id;
        if (!name.equals("")) {
            url = url + "/name/" + name;
        }
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
                    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                    if (resultCode > 0) {
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            String resultcommunity_id = jsonObject.getString("community_id");
                            String resultname = jsonObject.getString("name");
                            String resultlogo = jsonObject.getString("logo");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("community_id", resultcommunity_id);
                            map.put("name", resultname);
                            map.put("logo", resultlogo);
                            mylist.add(map);
                        }
                        gv_sqtj(mylist);
                    } else {
                        gv_sqtj(mylist);
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
        loadingDialog = new LoadingDialog(ShequnActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(ShequnActivity.this, R.style.dialog, sHint, type, true).show();
    }


    /**
     * 社区推荐
     */
    private void gv_sqtj(ArrayList<HashMap<String, String>> mylist) {
        MyAdapter myAdapter = new MyAdapter(this);
    /*    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 12; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemName", "");

            mylist.add(map);
        }*/
        myAdapter.arrlist = mylist;
        gv_shequn.setAdapter(myAdapter);

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
                view = inflater.inflate(R.layout.shequn_item, null);
            }
            LinearLayout ll_shequn_item = view.findViewById(R.id.ll_shequn_item);
            ll_shequn_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ShequnActivity.this, ShequnXqActivity.class);
                    intent.putExtra("id", arrlist.get(position).get("community_id"));
                    startActivity(intent);

                }
            });

            TextView tv_shequn_item_name = view.findViewById(R.id.tv_shequn_item_name);
            tv_shequn_item_name.setText(arrlist.get(position).get("name"));
            ImageView iv_shequn_item_logo = view.findViewById(R.id.iv_shequn_item_logo);
            Glide.with(ShequnActivity.this)
                    .load( Api.sUrl+ arrlist.get(position).get("logo"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_shequn_item_logo);


            return view;
        }
    }

}
