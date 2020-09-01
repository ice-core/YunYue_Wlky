package com.example.administrator.yunyue.sq_activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
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
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.example.administrator.yunyue.sc_activity.GuanggaoActivity;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GrtzActivity extends AppCompatActivity {
    /**
     * 返回
     */
    private LinearLayout ll_grtz_back;

    RequestQueue queue = null;
    private SharedPreferences pref;
    private String sUser_id;
    public static String sFriend_id = "";
    private ImageView iv_grtz_img;
    private TextView tv_grtz_name;
    private TextView tv_grtz_note;

    /**
     * 备注
     */
    private LinearLayout ll_grtz_bz;
    private PullToRefreshGridView mPullRefreshListView;
    int iPage = 1;
    private MyAdapterDt myAdapterDt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_grtz);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        queue = Volley.newRequestQueue(GrtzActivity.this);
        ll_grtz_back = findViewById(R.id.ll_grtz_back);
        ll_grtz_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mPullRefreshListView = (PullToRefreshGridView) findViewById(R.id.pull_refresh_grid);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        iv_grtz_img = findViewById(R.id.iv_grtz_img);
        tv_grtz_name = findViewById(R.id.tv_grtz_name);
        tv_grtz_note = findViewById(R.id.tv_grtz_note);
        ll_grtz_bz = findViewById(R.id.ll_grtz_bz);

        ll_grtz_bz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MorePopWindow_grtz morePopWindow_grtz = new MorePopWindow_grtz(GrtzActivity.this);
                morePopWindow_grtz.showPopupWindow(ll_grtz_bz);
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
                iPage += 1;
                query();

                // 页数
                //   p = p + 1;
                // AsyncTask异步交互加载数据
                // new GetDataTask2().execute(URL + p);
            }
        });
        query();
    }

    private void query() {
        String url = Api.sUrl + "Community/Api/dynamicMe/appId/"
                + Api.sApp_Id + "/user_id/" + sUser_id + "/page/" + iPage;
        if (!ShequnXqActivity.community_id.equals("")) {
            url = url + "/community_id/" + ShequnXqActivity.community_id;
        }
        if (!sFriend_id.equals("")) {
            url = url + "/friend_id/" + sFriend_id;
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
                    if (resultCode > 0) {
                        JSONObject jsonObjectdata = new JSONObject(sDate.toString()).getJSONObject("data");
                        if (iPage == 1) {
                            JSONObject jsonArraygroup = jsonObjectdata.getJSONObject("user");
                            //用户名称
                            String sNickname = jsonArraygroup.getString("nickname");
                            tv_grtz_name.setText("昵称："+sNickname);
                            //用户头像
                            String sHeadimgurl = jsonArraygroup.getString("headimgurl");
                            //备注
                            String sNote = jsonArraygroup.getString("note");
                            if (sNote.equals("")) {
                                tv_grtz_note.setText(sNickname);
                            } else {
                                tv_grtz_note.setText(sNote);
                            }//是否好友
                            String sFriend = jsonArraygroup.getString("friend");
                            if (sFriend.equals("1")) {
                                ll_grtz_bz.setVisibility(View.VISIBLE);
                            } else {
                                ll_grtz_bz.setVisibility(View.GONE);
                            }
                            Glide.with(GrtzActivity.this)
                                    .load( Api.sUrl+ sHeadimgurl)
                                    .asBitmap()
                                    .placeholder(R.mipmap.error)
                                    .error(R.mipmap.error)
                                    .dontAnimate()
                                    .into(iv_grtz_img);
                        }
                        JSONArray jsonArraydynamic = jsonObjectdata.getJSONArray("dynamic");
                        ArrayList<HashMap<String, String>> mylist_dynamic = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < jsonArraydynamic.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArraydynamic.opt(i);
                            // String ItemUser_Id = jsonObject2.getString("user_id");
                            String ItemDynamic_Id = jsonObject2.getString("dynamic_id");
                            String ItemNickname = jsonObject2.getString("nickname");
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
                            //  map.put("user_id", ItemUser_Id);
                            map.put("dynamic_id", ItemDynamic_Id);
                            map.put("nickname", ItemNickname);
                            map.put("headimgurl", ItemHeadimgurl);
                            map.put("title", ItemTitle);
                            map.put("content", ItemContent);
                            map.put("time", ItemTime);
                            map.put("img1", ItemImg1);
                            map.put("img2", ItemImg2);
                            map.put("img3", ItemImg3);
                            map.put("zan", ItemZan);
                            map.put("comment", ItemComment);
                            //  map.put("guanzhu", "0");
                            map.put("url", ItemUrl);
                            mylist_dynamic.add(map);
                        }
                        if (iPage == 1) {
                            gv_xiaoxi(mylist_dynamic);
                        } else {
                            if (jsonArraydynamic.length() == 0) {
                                if (iPage > 1) {
                                    iPage = iPage - 1;
                                }
                            }
                            gv_xiaoxi1(mylist_dynamic);

                        }
                        //    myAdapter.arrlist = mylist_dynamic;
                        // mgv_qxq_cy.setAdapter(myAdapter);
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

    /**
     * 动态
     */
    private void gv_xiaoxi(ArrayList<HashMap<String, String>> mylist) {
        myAdapterDt = new MyAdapterDt(this);
        myAdapterDt.arrlist = mylist;
        mPullRefreshListView.setAdapter(myAdapterDt);
        // setListViewHeightBasedOnChildren(gv_grtz);
        mPullRefreshListView.scrollTo(0, 0);
        // 关闭刷新下拉
        mPullRefreshListView.onRefreshComplete();
    }

    private void gv_xiaoxi1(ArrayList<HashMap<String, String>> mylist) {
        myAdapterDt.arrlist.addAll(mylist);
        mPullRefreshListView.setAdapter(myAdapterDt);
        // 关闭上拉加载
        mPullRefreshListView.onRefreshComplete();
    }

    /**
     * listview自适应高度
     */
    public void setListViewHeightBasedOnChildren(ListView listView1) {
        BaseAdapter listAdapter = (BaseAdapter) listView1.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        //获取listView的宽度
        ViewGroup.LayoutParams params = listView1.getLayoutParams();
        int listViewWidth = getWindowManager().getDefaultDisplay().getWidth();
        int widthSpec = View.MeasureSpec.makeMeasureSpec(listViewWidth, View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView1);
            //给item的measure设置参数是listView的宽度就可以获取到真正每一个item的高度
            listItem.measure(widthSpec, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        params.height = totalHeight + (listView1.getDividerHeight() * (listAdapter.getCount() + 1));
        listView1.setLayoutParams(params);
    }


    private class MyAdapterDt extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;


        public MyAdapterDt(Context context) {
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
                view = inflater.inflate(R.layout.shequnxq_dt_item, null);
            }
            LinearLayout ll_shequn_tuijian_item = view.findViewById(R.id.ll_shequn_tuijian_item);
            ImageView iv_shequnxq_dt_item = view.findViewById(R.id.iv_shequnxq_dt_item);
        /*    iv_shequnxq_dt_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GrtzActivity.this, GrtzActivity.class);
                    GrtzActivity.sFriend_id = arrlist.get(position).get("user_id");
                    startActivity(intent);
                }
            });*/
            TextView tv_shequnxq_dt_item_nickname = view.findViewById(R.id.tv_shequnxq_dt_item_nickname);
            TextView tv_shequnxq_dt_item_time = view.findViewById(R.id.tv_shequnxq_dt_item_time);
            TextView tv_shequnxq_dt_item_title = view.findViewById(R.id.tv_shequnxq_dt_item_title);
            TextView tv_shequnxq_dt_item_content = view.findViewById(R.id.tv_shequnxq_dt_item_content);
            TextView tv_shequn_dt_item_dianzhan = view.findViewById(R.id.tv_shequn_dt_item_dianzhan);
            TextView tv_shequn_dt_item_comment = view.findViewById(R.id.tv_shequn_dt_item_comment);
            ImageView iv_shequn_dt_item = view.findViewById(R.id.iv_shequn_dt_item);
            LinearLayout ll_shequn_dt_item = view.findViewById(R.id.ll_shequn_dt_item);
            ImageView iv_shequn_dt_item_1 = view.findViewById(R.id.iv_shequn_dt_item_1);
            ImageView iv_shequn_dt_item_2 = view.findViewById(R.id.iv_shequn_dt_item_2);
            ImageView iv_shequn_dt_item_3 = view.findViewById(R.id.iv_shequn_dt_item_3);
            Glide.with(GrtzActivity.this)
                    .load( Api.sUrl+ arrlist.get(position).get("headimgurl"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_shequnxq_dt_item);
            tv_shequnxq_dt_item_nickname.setText(arrlist.get(position).get("nickname"));
            tv_shequnxq_dt_item_time.setText(arrlist.get(position).get("time"));
            tv_shequnxq_dt_item_title.setText(arrlist.get(position).get("title"));
            tv_shequnxq_dt_item_content.setText(arrlist.get(position).get("content"));
            tv_shequn_dt_item_dianzhan.setText(arrlist.get(position).get("zan"));
            tv_shequn_dt_item_comment.setText(arrlist.get(position).get("comment"));
            if (arrlist.get(position).get("img2").equals("")) {
                ll_shequn_dt_item.setVisibility(View.GONE);
                iv_shequn_dt_item.setVisibility(View.VISIBLE);
                if (arrlist.get(position).get("img1").equals("")) {
                    iv_shequn_dt_item.setVisibility(View.GONE);
                } else {
                    iv_shequn_dt_item.setVisibility(View.VISIBLE);
                    Glide.with(GrtzActivity.this)
                            .load( Api.sUrl+ arrlist.get(position).get("img1"))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_shequn_dt_item);
                }
            } else {
                iv_shequn_dt_item.setVisibility(View.GONE);
                ll_shequn_dt_item.setVisibility(View.VISIBLE);
                if (arrlist.get(position).get("img1").equals("")) {
                } else {
                    Glide.with(GrtzActivity.this)
                            .load( Api.sUrl+ arrlist.get(position).get("img1"))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_shequn_dt_item_1);
                }
                if (arrlist.get(position).get("img2").equals("")) {
                } else {

                    Glide.with(GrtzActivity.this)
                            .load( Api.sUrl+ arrlist.get(position).get("img2"))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_shequn_dt_item_2);
                }
                if (arrlist.get(position).get("img3").equals("")) {
                } else {
                    Glide.with(GrtzActivity.this)
                            .load( Api.sUrl+ arrlist.get(position).get("img3"))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_shequn_dt_item_3);
                }
            }
            ll_shequn_tuijian_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GrtzActivity.this, GuanggaoActivity.class);
                    intent.putExtra("link", arrlist.get(position).get("url"));
                    startActivity(intent);
                }
            });
            return view;
        }
    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(GrtzActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(GrtzActivity.this, R.style.dialog, sHint, type, true).show();
    }


    public class MorePopWindow_grtz extends PopupWindow {

        @SuppressLint("InflateParams")
        public MorePopWindow_grtz(final Activity context) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View content = inflater.inflate(R.layout.popupwindow_grtz_bz, null);

            // 设置SelectPicPopupWindow的View
            this.setContentView(content);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            // 设置SelectPicPopupWindow弹出窗体可点击
            this.setFocusable(true);
            this.setOutsideTouchable(true);
            // 刷新状态
            this.update();
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0000000000);
            // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
            this.setBackgroundDrawable(dw);

            // 设置SelectPicPopupWindow弹出窗体动画效果
            this.setAnimationStyle(R.style.AnimationPreview);

            TextView tv_grtz_window_bz = content.findViewById(R.id.tv_grtz_window_bz);
            TextView tv_grtz_window_schy = content.findViewById(R.id.tv_grtz_window_schy);

            tv_grtz_window_bz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GrtzActivity.this, SzbzActivity.class);
                    intent.putExtra("id", sFriend_id);
                    intent.putExtra("name", tv_grtz_note.getText().toString());
                    startActivity(intent);
                    dismiss();
                }
            });
            tv_grtz_window_schy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideDialogin();
                    dialogin("");
                    delete(sFriend_id);
                    dismiss();
                }
            });

        }

        /**
         * 显示popupWindow
         *
         * @param parent
         */
        public void showPopupWindow(View parent) {
            if (!this.isShowing()) {
                // 以下拉方式显示popupwindow
                this.showAsDropDown(parent, 0, -10);
            } else {
                this.dismiss();
            }
        }

    }

    private void delete(String friend_id) {
        String url = Api.sUrl + "Community/Api/delfriend/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/friend_id/" + friend_id;
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

}
