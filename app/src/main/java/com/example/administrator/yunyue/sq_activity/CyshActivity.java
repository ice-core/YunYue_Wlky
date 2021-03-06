package com.example.administrator.yunyue.sq_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
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
import com.bumptech.glide.Glide;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc.CircleImageView;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CyshActivity extends AppCompatActivity {
    private static final String TAG = CyshActivity.class.getSimpleName();
    /**
     * 退出
     */
    private LinearLayout ll_cysh_back;
    private SharedPreferences pref;
    private String sUser_id;
    MyAdapter myAdapter;
    RequestQueue queue = null;
    private PullToRefreshGridView mPullRefreshListView;
    int iPage = 1;
    ArrayList<HashMap<String, String>> mylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white);
        }
        setContentView(R.layout.activity_cysh);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(CyshActivity.this);
        sUser_id = pref.getString("user_id", "");
        ll_cysh_back = findViewById(R.id.ll_cysh_back);
        mPullRefreshListView = findViewById(R.id.pull_refresh_grid);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        ll_cysh_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
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
                query();

            }

            // 上拉加载跟多
            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                Log.e("TAG", "onPullUpToRefresh"); // Do work to refresh

                // iPage += 1;
                // 页数
                //   p = p + 1;
                // AsyncTask异步交互加载数据
                // new GetDataTask2().execute(URL + p);
            }
        });
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
        String url = Api.sUrl + "Community/Api/tongyiList/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/community_id/" + ShequnXqActivity.community_id;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                myAdapter = new MyAdapter(CyshActivity.this);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (iPage == 1) {
                        mylist = new ArrayList<HashMap<String, String>>();
                    }
                    if (resultCode > 0) {
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                        mylist = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            String resultId = jsonObject.getString("id");
                            //用户昵称
                            String resultNicName = jsonObject.getString("nickname");
                            //群聊名称
                            String resultGroup_name = jsonObject.getString("group_name");
                            //群聊ID
                            String resultGroup_id = jsonObject.getString("group_id");
                            //用户头像
                            String resultHeadimgurl = jsonObject.getString("headimgurl");
                            //验证信息
                            String resultInfo = jsonObject.getString("info");

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", resultId);
                            map.put("nickname", resultNicName);
                            map.put("group_name", resultGroup_name);
                            map.put("group_id", resultGroup_id);
                            map.put("headimgurl", resultHeadimgurl);
                            map.put("info", resultInfo);
                            mylist.add(map);
                        }
                        if (iPage == 1) {
                            gridviewdata();
                        } else {
                            gridviewdata1();
                        }
                        if (mylist.size() == 0) {
                            iPage = iPage - 1;
                        }
                    } else {
                        if (iPage > 0) {
                            iPage -= 1;
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

    private void gridviewdata() {
        // iPage += 1;

        myAdapter = new MyAdapter(this);

        mPullRefreshListView.setAdapter(myAdapter);
        // 刷新适配器
        myAdapter.notifyDataSetChanged();
        // 关闭刷新下拉
        mPullRefreshListView.onRefreshComplete();

    }

    private void gridviewdata1() {
        //    myList = getMenuAdapter();
        //iPage += 1;

        // 刷新适配器

        myAdapter.notifyDataSetChanged();
        // Call onRefreshComplete when the list has been refreshed.
        // 关闭上拉加载
        mPullRefreshListView.onRefreshComplete();

    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(CyshActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(CyshActivity.this, R.style.dialog, sHint, type, true).show();
    }

    /**
     * 通过审核
     */
    private void save(String user_id, String group_id) {
        String url = Api.sUrl + "Community/Api/tongyi/appId/" + Api.sApp_Id
                + "/user_id/" + user_id + "/group_id/" + group_id;
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
                        Hint(resultMsg, HintDialog.SUCCESS);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1500);
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
                Hint(String.valueOf(volleyError), HintDialog.ERROR);
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }


    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;


        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);

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


        @Override
        public View getView(final int position, View view, ViewGroup arg2) {
            // TODO Auto-generated method stub
            if (view == null) {
                view = inflater.inflate(R.layout.cysh_item, null);
            }
            CircleImageView iv_cysh_item_headimgurl = view.findViewById(R.id.iv_cysh_item_headimgurl);

            TextView tv_cysh_nickname = view.findViewById(R.id.tv_cysh_nickname);
            TextView tv_cysh_group_name = view.findViewById(R.id.tv_cysh_group_name);
            TextView tv_cysh_tg = view.findViewById(R.id.tv_cysh_tg);
            Glide.with(CyshActivity.this)
                    .load( Api.sUrl+ mylist.get(position).get("headimgurl"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_cysh_item_headimgurl);
            tv_cysh_nickname.setText(mylist.get(position).get("nickname"));
            tv_cysh_group_name.setText("申请加入" + mylist.get(position).get("group_name"));
            tv_cysh_tg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
           /*         dialogin("");
                    save(mylist.get(position).get("id"), mylist.get(position).get("group_id"));*/
                    Intent intent = new Intent(CyshActivity.this, ShjgActivity.class);

                    intent.putExtra("id",    mylist.get(position).get("id"));
                    intent.putExtra("group_id",    mylist.get(position).get("group_id"));
                    intent.putExtra("headimgurl", mylist.get(position).get("headimgurl"));
                    intent.putExtra("nickname", mylist.get(position).get("nickname"));
                    intent.putExtra("group_name", "申请加入" + mylist.get(position).get("group_name"));
                    intent.putExtra("info", mylist.get(position).get("info"));
                    startActivity(intent);

                }
            });
/*            map.put("id", resultId);
            map.put("nickname", resultNicName);
            map.put("group_name", resultGroup_name);
            map.put("group_id", resultGroup_id);
            map.put("headimgurl", resultHeadimgurl);
            map.put("info", resultInfo);*/
            return view;
        }
    }


}
