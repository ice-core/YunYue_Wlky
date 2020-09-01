package com.example.administrator.yunyue.jysq_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.BottomScrollView;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc_activity.GuanggaoActivity;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

public class Jysq_XqActivity extends AppCompatActivity {
    private String sUser_id = "";
    RequestQueue queue = null;
    private SharedPreferences pref;
    /**
     * 返回
     */
    private LinearLayout ll_jysq_xq_back;
    /**
     * 列表
     */
    //  private MyGridView mgv_jysq_xq;
    private ListView mListView;
    private String sRid = "";
    /**
     * 发帖参与话题
     */
    private TextView tv_jysq_xq_ftcyht;
    private TextView tv_jysq_xq_content;
    private ImageView iv_jysq_xq_logo;
    private ImageView iv_jysq_xq_img;
    private TextView tv_jysq_xq_title;

    private int iPage = 1;
    @BindView(R.id.srl_jysq_xq)
    SmartRefreshLayout srl_jysq_xq;
    MyAdapter myAdapter;
    private float mLastX;
    private float mLastY;
    private boolean isSvToBottom = false;
    private BottomScrollView mScrollView;
    /**
     * listview竖向滑动的阈值
     */
    private static final int THRESHOLD_Y_LIST_VIEW = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white);
        }
        setContentView(R.layout.activity_jysq_xq);
        queue = Volley.newRequestQueue(Jysq_XqActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(Jysq_XqActivity.this);
        sUser_id = pref.getString("user_id", "");
        sRid = getIntent().getStringExtra("id");
        ll_jysq_xq_back = findViewById(R.id.ll_jysq_xq_back);
        tv_jysq_xq_content = findViewById(R.id.tv_jysq_xq_content);
        iv_jysq_xq_logo = findViewById(R.id.iv_jysq_xq_logo);
        iv_jysq_xq_img = findViewById(R.id.iv_jysq_xq_img);
        tv_jysq_xq_title = findViewById(R.id.tv_jysq_xq_title);

        ll_jysq_xq_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mListView = (ListView) findViewById(R.id.main_list_view);
        // mgv_jysq_xq = findViewById(R.id.mgv_jysq_xq);
        tv_jysq_xq_ftcyht = findViewById(R.id.tv_jysq_xq_ftcyht);
        tv_jysq_xq_ftcyht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Jysq_XqActivity.this, Jysq_FbtzActivity.class);
                intent.putExtra("id", sRid);
                startActivity(intent);
            }
        });
        mScrollView = (BottomScrollView) findViewById(R.id.mScrollView);
        srl_jysq_xq = findViewById(R.id.srl_jysq_xq);
        smartRefresh();
        query();
    }


    //监听下拉和上拉状态
    public void smartRefresh() {
        //下拉刷新
        srl_jysq_xq.setOnRefreshListener(refreshlayout -> {
            srl_jysq_xq.setEnableRefresh(true);//启用刷新
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
        srl_jysq_xq.setOnLoadmoreListener(refreshlayout -> {
            srl_jysq_xq.setEnableLoadmore(true);//启用加载
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 MoreDatas() 来模拟我们加载出来的数据
             */
            //  refreshAdapter.loadMore(MoreDatas());
            iPage += 1;
            query();


        });
        // ListView滑动冲突解决
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    mLastY = event.getY();
                }
                if (action == MotionEvent.ACTION_MOVE) {
                    try {
                        int top = mListView.getChildAt(0).getTop();
                        float nowY = event.getY();
                        if (!isSvToBottom) {
                            // 允许scrollview拦截点击事件, scrollView滑动
                            mScrollView.requestDisallowInterceptTouchEvent(false);
                        } else if (top == 0 && nowY - mLastY > THRESHOLD_Y_LIST_VIEW) {
                            // 允许scrollview拦截点击事件, scrollView滑动
                            mScrollView.requestDisallowInterceptTouchEvent(false);
                        } else {
                            // 不允许scrollview拦截点击事件， listView滑动
                            mScrollView.requestDisallowInterceptTouchEvent(true);
                        }
                    } catch (Exception e) {
                        mScrollView.requestDisallowInterceptTouchEvent(false);
                        hideDialogin();
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
    }

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initview();
    }

    private void initview() {
        query();
    }

    /**
     * 热点
     */
    private void query() {
        String url = Api.sUrl + "Shequn/Api/shequnDetails/appId/" + Api.sApp_Id +
                "/rid/" + sRid + "/user_id/" + sUser_id + "/page/" + iPage;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (iPage == 1) {
                    srl_jysq_xq.finishRefresh();//结束刷新
                } else {
                    srl_jysq_xq.finishLoadmore();//结束加载
                }
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        JSONObject jsonObjectdata = new JSONObject(sDate.toString()).getJSONObject("data");
                        JSONObject jsonObject_redian = new JSONObject(jsonObjectdata.getString("redian"));

                        String result_id = jsonObject_redian.getString("id");
                        String result_title = jsonObject_redian.getString("title");
                        String result_img = jsonObject_redian.getString("img");
                        String result_content = jsonObject_redian.getString("content");
                        String result_logo = jsonObject_redian.getString("logo");
                        tv_jysq_xq_content.setText(result_content);
                        tv_jysq_xq_title.setText(result_title);
                        Glide.with(Jysq_XqActivity.this)
                                .load( Api.sUrl+ result_img)
                                .asBitmap()
                                .placeholder(R.mipmap.error)
                                .error(R.mipmap.error)
                                .dontAnimate()
                                .into(iv_jysq_xq_img);
                        Glide.with(Jysq_XqActivity.this)
                                .load( Api.sUrl+ result_logo)
                                .asBitmap()
                                .placeholder(R.mipmap.error)
                                .error(R.mipmap.error)
                                .dontAnimate()
                                .into(iv_jysq_xq_logo);
                        JSONArray jsonArray_dynamic = jsonObjectdata.getJSONArray("dynamic");
                        ArrayList<HashMap<String, String>> mylist_dynamic = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < jsonArray_dynamic.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArray_dynamic.opt(i);
                            String ItemUser_Id = jsonObject2.getString("user_id");
                            String ItemDynamic_Id = jsonObject2.getString("dynamic_id");
                            String ItemNickName = jsonObject2.getString("nickname");
                            String ItemHeadimgurl = jsonObject2.getString("headimgurl");
                            String ItemTitle = jsonObject2.getString("title");
                            String ItemContent = jsonObject2.getString("content");
                            String ItemTime = jsonObject2.getString("time");
                            String ItemImg1 = jsonObject2.getString("img1");
                            String ItemImg2 = jsonObject2.getString("img2");
                            String ItemImg3 = jsonObject2.getString("img3");
                            String ItemZan = jsonObject2.getString("zan");
                            String ItemComment = jsonObject2.getString("comment");
                            String ItemUrl = jsonObject2.getString("url");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("nickname", ItemNickName);
                            map.put("user_id", ItemUser_Id);
                            map.put("dynamic_id", ItemDynamic_Id);
                            map.put("headimgurl", ItemHeadimgurl);
                            map.put("title", ItemTitle);
                            map.put("content", ItemContent);
                            map.put("time", ItemTime);
                            map.put("img1", ItemImg1);
                            map.put("img2", ItemImg2);
                            map.put("img3", ItemImg3);
                            map.put("zan", ItemZan);
                            map.put("comment", ItemComment);
                            map.put("url", ItemUrl);
                            mylist_dynamic.add(map);
                        }
                        if (iPage == 1) {
                            setGridView(mylist_dynamic);
                        } else {
                            if (mylist_dynamic.size() == 0) {
                                iPage -= 1;
                            } else {
                                setGridView1(mylist_dynamic);
                            }
                        }
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
        request.setShouldCache(false);
        queue.add(request);
    }


    /**
     * 热门帖子
     */
    private void setGridView(ArrayList<HashMap<String, String>> mylist) {
        myAdapter = new MyAdapter(Jysq_XqActivity.this);
        myAdapter.arrlist = mylist;
        mListView.setAdapter(myAdapter);
        //  setListViewHeightBasedOnChildren(mlv_jysq_xq);
    }

    private void setGridView1(ArrayList<HashMap<String, String>> mylist) {

        myAdapter.arrlist.addAll(mylist);
        mListView.setAdapter(myAdapter);
        //  setListViewHeightBasedOnChildren(mlv_jysq_xq);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {

        // 获取ListView对应的Adapter

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {

            return;

        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目

            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(0, 0); // 计算子项View 的宽高

            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        // listView.getDividerHeight()获取子项间分隔符占用的高度

        // params.height最后得到整个ListView完整显示需要的高度

        listView.setLayoutParams(params);

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
                view = inflater.inflate(R.layout.jysq_rm_item, null);
            }
            TextView tv_jysq_rm_item_mw = view.findViewById(R.id.tv_jysq_rm_item_mw);
            LinearLayout ll_jysq_rm_item = view.findViewById(R.id.ll_jysq_rm_item);
            ImageView civ_jysq_rm_item_headimgurl = view.findViewById(R.id.civ_jysq_rm_item_headimgurl);
            TextView tv_jysq_rm_item_nickname = view.findViewById(R.id.tv_jysq_rm_item_nickname);
            TextView tv_jysq_rm_item_title = view.findViewById(R.id.tv_jysq_rm_item_title);
            TextView tv_jysq_rm_item_content = view.findViewById(R.id.tv_jysq_rm_item_content);

            ImageView iv_jysq_rm_item_img1 = view.findViewById(R.id.iv_jysq_rm_item_img1);
            ImageView iv_jysq_rm_item_img2 = view.findViewById(R.id.iv_jysq_rm_item_img2);
            ImageView iv_jysq_rm_item_img3 = view.findViewById(R.id.iv_jysq_rm_item_img3);

            TextView tv_jysq_rm_item_zan = view.findViewById(R.id.tv_jysq_rm_item_zan);
            TextView tv_jysq_rm_item_comment = view.findViewById(R.id.tv_jysq_rm_item_comment);
            TextView tv_jysq_rm_item_fgx = view.findViewById(R.id.tv_jysq_rm_item_fgx);
            tv_jysq_rm_item_fgx.setVisibility(View.VISIBLE);
            if (arrlist.size() == (position + 1)) {
                tv_jysq_rm_item_mw.setVisibility(View.VISIBLE);
            } else {
                tv_jysq_rm_item_mw.setVisibility(View.GONE);
            }
            Glide.with(Jysq_XqActivity.this)
                    .load( Api.sUrl+ arrlist.get(position).get("headimgurl"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(civ_jysq_rm_item_headimgurl);
            tv_jysq_rm_item_nickname.setText(arrlist.get(position).get("nickname"));
            tv_jysq_rm_item_title.setText(arrlist.get(position).get("title"));
            tv_jysq_rm_item_content.setText(arrlist.get(position).get("content"));


            tv_jysq_rm_item_zan.setText(arrlist.get(position).get("zan"));
            tv_jysq_rm_item_comment.setText(arrlist.get(position).get("comment"));
            LinearLayout ll_jysq_rm_item_img = view.findViewById(R.id.ll_jysq_rm_item_img);
           /* civ_jysq_rm_item_headimgurl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Jysq_XqActivity.this, GrtzActivity.class);
                    GrtzActivity.sFriend_id = arrlist.get(position).get("user_id");
                    ShequnXqActivity.community_id = arrlist.get(position).get("community_id");
                    startActivity(intent);
                }
            });*/

            if (arrlist.get(position).get("img1").equals("")) {
                ll_jysq_rm_item_img.setVisibility(View.GONE);
            } else {

                Glide.with(Jysq_XqActivity.this)
                        .load( Api.sUrl+ arrlist.get(position).get("img1"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_jysq_rm_item_img1);
            }
            if (arrlist.get(position).get("img2").equals("")) {
            } else {

                Glide.with(Jysq_XqActivity.this)
                        .load( Api.sUrl+ arrlist.get(position).get("img2"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_jysq_rm_item_img2);
            }
            if (arrlist.get(position).get("img3").equals("")) {
            } else {
                Glide.with(Jysq_XqActivity.this)
                        .load( Api.sUrl+ arrlist.get(position).get("img3"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_jysq_rm_item_img3);
            }

            ll_jysq_rm_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Jysq_XqActivity.this, GuanggaoActivity.class);
                    intent.putExtra("link", arrlist.get(position).get("url"));
                    startActivity(intent);
                }
            });
            return view;
        }
    }


    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(Jysq_XqActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(Jysq_XqActivity.this, R.style.dialog, sHint, type, true).show();
    }
}
