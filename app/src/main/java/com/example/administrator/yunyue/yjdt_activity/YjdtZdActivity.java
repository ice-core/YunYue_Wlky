package com.example.administrator.yunyue.yjdt_activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class YjdtZdActivity extends AppCompatActivity {
    private static final String TAG = YjdtZdActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_zd_back;
    /**
     * 支出
     */
    private LinearLayout ll_yjdt_zc;
    private TextView tv_yjdt_zc, tv_yjdt_zc_xhx;
    /**
     * 收入
     */
    private LinearLayout ll_yjdt_sr;
    private TextView tv_yjdt_sr, tv_yjdt_sr_xhx;
    /**
     * 1提现 2收入
     */
    private String sType = "1";


    @BindView(R.id.srl_control)
    SmartRefreshLayout srlControl;
    int iPage = 1;
    private MyAdapter myAdapter;
    private SharedPreferences pref;

    /**
     * 用户id
     */
    private String sUser_id = "";
    /**
     * 账单列表
     */
    private MyGridView mgv_yjdt_zd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_yjdt_zd);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        srlControl = findViewById(R.id.srl_control);
        mgv_yjdt_zd = findViewById(R.id.mgv_yjdt_zd);
        ll_zd_back = findViewById(R.id.ll_zd_back);
        ll_zd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_yjdt_zc = findViewById(R.id.ll_yjdt_zc);
        tv_yjdt_zc = findViewById(R.id.tv_yjdt_zc);
        tv_yjdt_zc_xhx = findViewById(R.id.tv_yjdt_zc_xhx);
        ll_yjdt_sr = findViewById(R.id.ll_yjdt_sr);
        tv_yjdt_sr = findViewById(R.id.tv_yjdt_sr);
        tv_yjdt_sr_xhx = findViewById(R.id.tv_yjdt_sr_xhx);
        ll_yjdt_zc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sType = "1";
                tv_yjdt_zc.setTextColor(tv_yjdt_zc.getResources().getColor(R.color.theme));
                tv_yjdt_zc_xhx.setVisibility(View.VISIBLE);
                tv_yjdt_sr.setTextColor(tv_yjdt_sr.getResources().getColor(R.color.bbbccd));
                tv_yjdt_sr_xhx.setVisibility(View.GONE);
                dialogin("");
                sBill();
            }
        });
        ll_yjdt_sr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sType = "2";
                tv_yjdt_zc.setTextColor(tv_yjdt_zc.getResources().getColor(R.color.bbbccd));
                tv_yjdt_zc_xhx.setVisibility(View.GONE);
                tv_yjdt_sr.setTextColor(tv_yjdt_sr.getResources().getColor(R.color.theme));
                tv_yjdt_sr_xhx.setVisibility(View.VISIBLE);
                dialogin("");
                sBill();
            }
        });
        smartRefresh();

        sBill();
    }

    //监听下拉和上拉状态
    public void smartRefresh() {
        //下拉刷新
        srlControl.setOnRefreshListener(refreshlayout -> {
            srlControl.setEnableRefresh(true);//启用刷新
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 Data() 来模拟我们刷新出来的数据
             */
            iPage = 1;
            sBill();
            // srlControl.finishRefresh();//结束刷新

        });
        //上拉加载
        srlControl.setOnLoadmoreListener(refreshlayout -> {
            srlControl.setEnableLoadmore(true);//启用加载
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 MoreDatas() 来模拟我们加载出来的数据*/

            iPage += 1;
            sBill();
            srlControl.finishLoadmore();//结束加载
        });
    }

    /**
     * 方法名：sBill()
     * 功  能：账单接口
     * 参  数：appId
     */
    private void sBill() {
        String url = Api.sUrl + Api.sBill;
        RequestQueue requestQueue = Volley.newRequestQueue(YjdtZdActivity.this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("type", sType);
        params.put("page", String.valueOf(iPage));
        JSONObject jsonObject = new JSONObject(params);
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (iPage == 1) {
                            srlControl.finishRefresh();//结束刷新
                        } else {
                            srlControl.finishLoadmore();//结束加载
                        }
                        hideDialogin();
                        String sDate = response.toString();
                        System.out.println(sDate);
                        try {
                            JSONObject jsonObject0 = new JSONObject(sDate);
                            String resultMsg = jsonObject0.getString("msg");
                            int resultCode = jsonObject0.getInt("code");
                            if (resultCode > 0) {
                                JSONArray jsonArray = jsonObject0.getJSONArray("advertising");
                                ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
                                    String ItemId = jsonObject2.getString("id");
                                    //操作时间
                                    String ItemAction_time = jsonObject2.getString("action_time");
                                    //手机号
                                    String ItemPrice = jsonObject2.getString("price");
                                    //（提现）1为处理中 2为提现成功
                                    String ItemStater = jsonObject2.getString("stater");

                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("id", ItemId);
                                    map.put("action_time", ItemAction_time);
                                    map.put("price", ItemPrice);
                                    map.put("stater", ItemStater);

                                    mylist.add(map);
                                }
                                if (iPage == 1) {
                                    gridview(mylist);
                                } else {
                                    if (mylist.size() == 0) {
                                        iPage -= 1;
                                    } else {
                                        gridview1(mylist);
                                    }
                                }

                            } else {
                                hideDialogin();
                                Hint(resultMsg, HintDialog.ERROR);
                                //   Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "response -> " + response.toString());
                        //   Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialogin();
                //  Hint(error.getMessage(), HintDialog.ERROR);
                error(error);
            }
        });
        requestQueue.add(jsonRequest);
    }

    private void gridview(final ArrayList<HashMap<String, String>> myList) {
        myAdapter = new MyAdapter(YjdtZdActivity.this);
        myAdapter.arrlist = myList;
        mgv_yjdt_zd.setAdapter(myAdapter);
    }

    private void gridview1(final ArrayList<HashMap<String, String>> myList) {
        myAdapter.arrlist.addAll(myList);
        mgv_yjdt_zd.setAdapter(myAdapter);
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
                view = inflater.inflate(R.layout.yjdt_zd_item, null);
            }
            TextView tv_yjdt_zd_type = view.findViewById(R.id.tv_yjdt_zd_type);
            TextView tv_yjdt_zd_action_time = view.findViewById(R.id.tv_yjdt_zd_action_time);
            TextView tv_yjdt_zd_price = view.findViewById(R.id.tv_yjdt_zd_price);
            if (sType.equals("1")) {
                tv_yjdt_zd_type.setText("支出");
                if (arrlist.get(position).get("stater").equals("1")) {
                    tv_yjdt_zd_action_time.setText(arrlist.get(position).get("action_time") + "（处理中）");
                } else {
                    tv_yjdt_zd_action_time.setText(arrlist.get(position).get("action_time"));
                }
                tv_yjdt_zd_price.setText("-" + arrlist.get(position).get("price"));
            } else {
                tv_yjdt_zd_type.setText("收入");
                tv_yjdt_zd_action_time.setText(arrlist.get(position).get("action_time"));
                tv_yjdt_zd_price.setText("+" + arrlist.get(position).get("price"));
            }
            return view;
        }
    }

    private void error(VolleyError error) {
        Log.e(TAG, error.getMessage(), error);
        if (error != null) {
            if (error instanceof TimeoutError) {
                // Toast.makeText(mActivity,"网络请求超时，请重试！",Toast.LENGTH_SHORT).show();
                Hint("网络请求超时，请重试！", HintDialog.ERROR);
                return;
            }
            if (error instanceof ServerError) {
                //  Toast.makeText(mActivity,"服务器异常",Toast.LENGTH_SHORT).show();
                Hint("服务器异常", HintDialog.ERROR);
                return;
            }
            if (error instanceof NetworkError) {
                // Toast.makeText(mActivity,"请检查网络",Toast.LENGTH_SHORT).show();
                Hint("请检查网络", HintDialog.ERROR);
                return;
            }
            if (error instanceof ParseError) {
                Hint("数据格式错误", HintDialog.ERROR);
                //  Toast.makeText(mActivity,"数据格式错误",Toast.LENGTH_SHORT).show();
                return;
            }

            Hint(error.toString(), HintDialog.ERROR);
        }
    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(YjdtZdActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(YjdtZdActivity.this, R.style.dialog, sHint, type, true).show();
    }
}
