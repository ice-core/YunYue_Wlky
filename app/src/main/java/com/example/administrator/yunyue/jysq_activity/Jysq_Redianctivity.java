package com.example.administrator.yunyue.jysq_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.bumptech.glide.Glide;
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

import butterknife.BindView;

public class Jysq_Redianctivity extends AppCompatActivity {
    private String sUser_id = "";
    RequestQueue queue = null;
    private SharedPreferences pref;

    /**
     * 返回
     */
    private LinearLayout lv_rd_back;

    /**
     * 热点列表
     */
    private MyGridView myv_rd;
    private String sName = "";
    private int iPage = 1;
    @BindView(R.id.srl_jysq_rd)
    SmartRefreshLayout srl_jysq_rd;
    private MyAdapter myAdapterShequ;
    private LinearLayout ll_jysq_re_kong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white);
        }
        setContentView(R.layout.activity_jysq__redian);
        queue = Volley.newRequestQueue(Jysq_Redianctivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(Jysq_Redianctivity.this);
        sUser_id = pref.getString("user_id", "");
        sName = getIntent().getStringExtra("name");
        lv_rd_back = findViewById(R.id.lv_rd_back);
        ll_jysq_re_kong = findViewById(R.id.ll_jysq_re_kong);
        lv_rd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        myv_rd = findViewById(R.id.myv_rd);
        srl_jysq_rd = findViewById(R.id.srl_jysq_rd);
        smartRefresh();
        hideDialogin();
        dialogin("");
        query();
    }

    //监听下拉和上拉状态
    public void smartRefresh() {
        //下拉刷新
        srl_jysq_rd.setOnRefreshListener(refreshlayout -> {
            srl_jysq_rd.setEnableRefresh(true);//启用刷新
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 Data() 来模拟我们刷新出来的数据
             */
            iPage = 1;
            query();
            // refreshAdapter.strList.clear();
            // refreshAdapter.refreshData(Data());
            //  iPage = 1;
            // query();
            // srlControl.finishRefresh();//结束刷新
        });
        //上拉加载
        srl_jysq_rd.setOnLoadmoreListener(refreshlayout -> {
            srl_jysq_rd.setEnableLoadmore(true);//启用加载
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 MoreDatas() 来模拟我们加载出来的数据
             */
            //  refreshAdapter.loadMore(MoreDatas());
            iPage += 1;
            query();


        });
    }

    /**
     * 热点
     */
    private void query() {
        String url = Api.sUrl + "Shequn/Api/shequnLists/appId/" + Api.sApp_Id + "/user_id/" + sUser_id + "/page/" + iPage;
        if (sName == null) {
        } else if (sName.equals("")) {
        } else {
            url = url + "/name/" + sName;
        }
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (iPage == 1) {
                    srl_jysq_rd.finishRefresh();//结束刷新
                } else {
                    srl_jysq_rd.finishLoadmore();//结束加载
                }
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        JSONArray jsonArray_redian = jsonObject1.getJSONArray("data");
                        ArrayList<HashMap<String, String>> mylist_redian = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < jsonArray_redian.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArray_redian.opt(i);
                            String ItemId = jsonObject2.getString("id");
                            String ItemTitle = jsonObject2.getString("title");
                            String ItemImg = jsonObject2.getString("img");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", ItemId);
                            map.put("title", ItemTitle);
                            map.put("img", ItemImg);
                            mylist_redian.add(map);
                        }
                        if (mylist_redian.size() > 0) {
                            ll_jysq_re_kong.setVisibility(View.GONE);
                        } else {
                            ll_jysq_re_kong.setVisibility(View.VISIBLE);
                        }

                        if (iPage == 1) {
                            setGridView(mylist_redian);
                        } else {
                            if (mylist_redian.size() == 0) {
                                iPage -= 1;
                            } else {
                                setGridView1(mylist_redian);
                            }
                        }

                    } else {
                        ll_jysq_re_kong.setVisibility(View.GONE);
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


    /**
     * 热门帖子
     */
    private void setGridView(ArrayList<HashMap<String, String>> mylist) {
        myAdapterShequ = new MyAdapter(Jysq_Redianctivity.this);
        myAdapterShequ.arrlist = mylist;
        myv_rd.setAdapter(myAdapterShequ);

    }

    private void setGridView1(ArrayList<HashMap<String, String>> mylist) {

        myAdapterShequ.arrlist.addAll(mylist);
        myv_rd.setAdapter(myAdapterShequ);

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
                view = inflater.inflate(R.layout.jysq_rdlb_item, null);
            }
        /*    map.put("id", ItemId);
            map.put("title", ItemTitle);
            map.put("img", ItemImg);*/
            LinearLayout ll_jysq_rdlb_item = view.findViewById(R.id.ll_jysq_rdlb_item);
            TextView tv_jysq_rdlb_item_title = view.findViewById(R.id.tv_jysq_rdlb_item_title);
            tv_jysq_rdlb_item_title.setText(arrlist.get(position).get("title"));

            ImageView iv_jysq_rdlb_item_img = view.findViewById(R.id.iv_jysq_rdlb_item_img);
            Glide.with(Jysq_Redianctivity.this)
                    .load( Api.sUrl+ arrlist.get(position).get("img"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_jysq_rdlb_item_img);
            ll_jysq_rdlb_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Jysq_Redianctivity.this, Jysq_XqActivity.class);
                    intent.putExtra("id", arrlist.get(position).get("id"));
                    startActivity(intent);
                }
            });
            return view;
        }
    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(Jysq_Redianctivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(Jysq_Redianctivity.this, R.style.dialog, sHint, type, true).show();
    }
}
