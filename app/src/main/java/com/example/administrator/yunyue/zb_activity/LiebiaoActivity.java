package com.example.administrator.yunyue.zb_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.example.administrator.yunyue.utils.NullTranslator;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LiebiaoActivity extends AppCompatActivity {
    /**
     * 返回
     */
    private LinearLayout ll_lb_back;
    RequestQueue queue = null;
    private SharedPreferences pref;
    private String sUser_id;
    private int iPage = 1;
    private PullToRefreshGridView pull_refresh_grid_zblb;
    private MyAdapter myAdapter;
    /**
     * 标题
     */
    private TextView tv_sousuo_hint;
    private LinearLayout ll_sousuo_query;
    private TextView tv_liebiao_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white);
        }
        setContentView(R.layout.activity_liebiao);

        Intent intent = getIntent();
        final String sType_id = intent.getStringExtra("type_id");
        final String sTypename = intent.getStringExtra("typename");
        final String sContent = intent.getStringExtra("content");
        queue = Volley.newRequestQueue(LiebiaoActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        ll_lb_back = findViewById(R.id.ll_lb_back);
        tv_sousuo_hint = findViewById(R.id.tv_sousuo_hint);
        ll_sousuo_query = findViewById(R.id.ll_sousuo_query);
        tv_liebiao_query = findViewById(R.id.tv_liebiao_query);

        pull_refresh_grid_zblb = findViewById(R.id.pull_refresh_grid_zblb);
        pull_refresh_grid_zblb.setMode(PullToRefreshBase.Mode.BOTH);
        pull_refresh_grid_zblb.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            // 下拉刷新加载
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                Log.e("TAG", "onPullDownToRefresh"); // Do work to
                // 刷新时间
                String label = DateUtils.formatDateTime(
                        getApplicationContext(),
                        System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                // AsyncTask异步交互加载数据
                // new GetDataTask().execute(URL + z);
                iPage = 1;
                if (sType_id == null) {
                    query_content(sContent);
                } else if (sType_id.equals("")) {
                    query_content(sContent);
                } else {
                    query(sType_id);
                }
            }

            // 上拉加载跟多
            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                Log.e("TAG", "onPullUpToRefresh"); // Do work to refresh
                iPage += 1;
                if (sType_id == null) {
                    query_content(sContent);
                } else if (sType_id.equals("")) {
                    query_content(sContent);
                } else {
                    query(sType_id);
                }

            }
        });
        ll_lb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (sType_id == null) {
            ll_sousuo_query.setVisibility(View.VISIBLE);
            tv_sousuo_hint.setText("");
            tv_liebiao_query.setText(sContent);
            query_content(sContent);
        } else if (sType_id.equals("")) {
            ll_sousuo_query.setVisibility(View.VISIBLE);
            tv_sousuo_hint.setText("");
            tv_liebiao_query.setText(sContent);
            query_content(sContent);
        } else {
            ll_sousuo_query.setVisibility(View.GONE);
            tv_sousuo_hint.setText(sTypename);
            tv_liebiao_query.setText("");
            query(sType_id);
        }
        tv_liebiao_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 搜索记录，热门搜索信息获取
     */
    private void query(String sType_id) {
        String url = Api.sUrl + "Broadcast/Api/typelist/appId/"
                + Api.sApp_Id + "/type_id/" + sType_id + "/page/" + iPage;
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
                    ArrayList<HashMap<String, String>> mylist_video = new ArrayList<HashMap<String, String>>();
                    if (resultCode > 0) {
                        JSONArray jsonArraysearch_hot = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < jsonArraysearch_hot.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArraysearch_hot.opt(i);
                            String Itemid = jsonObject2.getString("id");
                            String Itemtitle = jsonObject2.getString("title");
                            String Itemvideo_img = jsonObject2.getString("video_img");
                            String Itemvideo_address = jsonObject2.getString("video_address");
                            String Itemplay_cont = jsonObject2.getString("play_cont");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", Itemid);
                            map.put("title", Itemtitle);
                            map.put("video_img", Itemvideo_img);
                            map.put("video_address", Itemvideo_address);
                            map.put("play_cont", Itemplay_cont);
                            mylist_video.add(map);
                        }
                        if (iPage == 1) {
                            setGridView_rmtj(mylist_video);
                        } else {
                            setGridView_rmtj1(mylist_video);
                        }
                    } else {
                        if (iPage == 1) {
                            setGridView_rmtj(mylist_video);
                        } else {
                            setGridView_rmtj1(mylist_video);
                        }
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
     * 搜索记录，热门搜索信息获取
     */
    private void query_content(String content) {
        String url = Api.sUrl + "Broadcast/Api/videoSearch/appId/"
                + Api.sApp_Id + "/content/" + content + "/page/" + iPage + "/user_id/" + sUser_id;
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
                    ArrayList<HashMap<String, String>> mylist_video = new ArrayList<HashMap<String, String>>();
                    if (resultCode > 0) {
                        JSONArray jsonArraysearch_hot = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < jsonArraysearch_hot.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArraysearch_hot.opt(i);
                            String Itemid = jsonObject2.getString("id");
                            String Itemtitle = jsonObject2.getString("title");
                            String Itemvideo_img = jsonObject2.getString("video_img");
                            String Itemvideo_address = jsonObject2.getString("video_address");
                            String Itemplay_cont = jsonObject2.getString("play_cont");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", Itemid);
                            map.put("title", Itemtitle);
                            map.put("video_img", Itemvideo_img);
                            map.put("video_address", Itemvideo_address);
                            map.put("play_cont", Itemplay_cont);
                            mylist_video.add(map);
                        }
                        if (iPage == 1) {
                            setGridView_rmtj(mylist_video);
                        } else {
                            setGridView_rmtj1(mylist_video);
                        }
                    } else {
                        if (iPage == 1) {
                            setGridView_rmtj(mylist_video);
                        } else {
                            setGridView_rmtj1(mylist_video);
                        }
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
        loadingDialog = new LoadingDialog(LiebiaoActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(LiebiaoActivity.this, R.style.dialog, sHint, type, true).show();
    }

    /**
     * 热门推荐
     * 设置GirdView参数，绑定数据
     */
    private void setGridView_rmtj(ArrayList<HashMap<String, String>> mylist) {
        myAdapter = new MyAdapter(LiebiaoActivity.this);
        myAdapter.arrlist = mylist;
        pull_refresh_grid_zblb.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
        // 关闭刷新下拉
        pull_refresh_grid_zblb.onRefreshComplete();
    }

    /**
     * 热门推荐
     * 设置GirdView参数，绑定数据
     */
    private void setGridView_rmtj1(ArrayList<HashMap<String, String>> mylist) {
        if (mylist.size() == 0) {
            if (iPage > 1) {
                iPage = iPage - 1;
            }
        }

        myAdapter.arrlist.addAll(mylist);
        pull_refresh_grid_zblb.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
        // Call onRefreshComplete when the list has been refreshed.
        // 关闭上拉加载
        pull_refresh_grid_zblb.onRefreshComplete();
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
                view = inflater.inflate(R.layout.zhibo_rmtj, null);
            }
            LinearLayout ll_zhibo_rmtj = view.findViewById(R.id.ll_zhibo_rmtj);
            ll_zhibo_rmtj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LiebiaoActivity.this, ZhiboActivity.class);
                    intent.putExtra("id", arrlist.get(position).get("id"));
                    startActivity(intent);
                }
            });
            ImageView iv_zhibo_rmtj_img = view.findViewById(R.id.iv_zhibo_rmtj_img);
            TextView tv_zhibo_rmtj_title = view.findViewById(R.id.tv_zhibo_rmtj_title);
            TextView tv_zhibo_rmtj_cont = view.findViewById(R.id.tv_zhibo_rmtj_cont);
            tv_zhibo_rmtj_title.setText(arrlist.get(position).get("title"));
            tv_zhibo_rmtj_cont.setText(arrlist.get(position).get("play_cont"));

            Glide.with(LiebiaoActivity.this)
                    .load( Api.sUrl+ arrlist.get(position).get("video_img"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_zhibo_rmtj_img);


            return view;
        }
    }
}
