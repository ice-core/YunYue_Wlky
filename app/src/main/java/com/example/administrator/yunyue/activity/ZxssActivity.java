package com.example.administrator.yunyue.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
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
import com.example.administrator.yunyue.sc_activity.GuanggaoActivity;
import com.example.administrator.yunyue.tu_model.NineGridTestModel;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ZxssActivity extends AppCompatActivity {
    RequestQueue queue = null;
    private int iPage;
    private List<NineGridTestModel> mList = new ArrayList<>();
    private PullToRefreshGridView pull_refresh_grid_zxss;
    private MyAdapterLIST myAdapterlist;
    private SharedPreferences pref;
    private String sUser_id = "";
    private String sZhixun = "";
    private EditText et_zxss_query;
    private String sQueryText = "";
    private ImageView iv_zxss_back;
    private boolean isDx = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_zxss);
        iPage = 1;
        queue = Volley.newRequestQueue(this);
        pref = PreferenceManager.getDefaultSharedPreferences(ZxssActivity.this);
        boolean isRemember = pref.getBoolean("user", false);
        sUser_id = pref.getString("user_id", "");
        sZhixun = pref.getString("zhixun", "");
        pull_refresh_grid_zxss = (PullToRefreshGridView) findViewById(R.id.pull_refresh_grid_zxss);
        et_zxss_query = findViewById(R.id.et_zxss_query);
        iv_zxss_back = findViewById(R.id.iv_zxss_back);
        iv_zxss_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            }
        });
        pull_refresh_grid_zxss.setMode(PullToRefreshBase.Mode.BOTH);
        myAdapterlist = new MyAdapterLIST(this);
        pull_refresh_grid_zxss.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
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
                isDx = false;
                // AsyncTask异步交互加载数据
                // new GetDataTask().execute(URL + z);
                iPage = 1;
                // huoqu();
                sQueryText = et_zxss_query.getText().toString();
                if (sQueryText.equals("")) {
                    pull_refresh_grid_zxss.onRefreshComplete();
                } else {
                    zx();
                }
                //   huoqu(iPage);

            }

            // 上拉加载跟多
            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                Log.e("TAG", "onPullUpToRefresh"); // Do work to refresh
                if (isDx == false) {
                    sQueryText = et_zxss_query.getText().toString();
                    if (sQueryText.equals("")) {
                        pull_refresh_grid_zxss.onRefreshComplete();
                    } else {
                        zx();
                    }
                } else {
                    myAdapterlist.notifyDataSetChanged();
                    pull_refresh_grid_zxss.onRefreshComplete();
                }
                //   huoqu(iPage);
                // 页数
                //   p = p + 1;
                // AsyncTask异步交互加载数据
                // new GetDataTask2().execute(URL + p);
            }
        });
        // if (ZixunFragment.iPosition == 0) {
        mList = new ArrayList<>();
        // 刷新适配器
        myAdapterlist.notifyDataSetChanged();

        et_zxss_query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO) {
                    sQueryText = et_zxss_query.getText().toString();
                    if (sQueryText.equals("")) {
                    } else {
                        iPage = 1;
                        dialogin("");
                        zx();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    }
                }
                return false;
            }
        });
    }

    /**
     * 咨询列表
     */
    private void zx() {
        // String url = Api.sUrl + "GetZhixun/getType/user_id/" + sUser_id;
        // String url = Api.sUrl + "GetZhixun/getNewList/page/" + iPage + "/keyword/" + sQueryText;

        String url = Api.sUrl + "Api/Getnew/getNewList/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/page/" + iPage + "/keyword/" + sQueryText;

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
                        if (iPage == 1) {
                            mList = new ArrayList<>();
                        }
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");

                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            String resultId = jsonObject.getString("id");
                            String resultTitle = jsonObject.getString("title");
                            String resultDetail = jsonObject.getString("detail");
                            String resultImg = jsonObject.getString("img");
                            String resultTime = jsonObject.getString("time");
                            String resultOneName = jsonObject.getString("one_name");
                            String resultGetName = jsonObject.getString("getname");
                            String resultPinglunNum = jsonObject.getString("pinglun_num");
                            String resultUrl = jsonObject.getString("url");
                            String resultView = jsonObject.getString("view");
                            JSONArray jsonArrayImglist = jsonObject.getJSONArray("imglist");


                            NineGridTestModel model = new NineGridTestModel();

                            for (int a = 0; a < jsonArrayImglist.length(); a++) {
                                String imge = jsonArrayImglist.get(a).toString();
                                model.urlList.add(imge);
                            }
                            model.isShowAll = false;
                            // model4.urlList.add(mUrls[i]);
                            model.id = resultId;
                            model.title = resultTitle;
                            model.detail = resultDetail;
                            model.img = resultImg;
                            model.time = resultTime;
                            model.one_name = resultOneName;
                            model.getName = resultGetName;
                            model.pinglun_num = resultPinglunNum;
                            model.url = resultUrl;
                            model.view = resultView;
                            mList.add(model);
                            //  otherChannelList.add(new ChannelItem(Integer.valueOf(resultId), resultName, Integer.valueOf(resultId), Integer.valueOf(resultId)));
                        }
                        if (iPage == 1) {
                            gridviewdata();
                        } else {
                            gridviewdata1();
                        }
                        iPage += 1;

                    } else {
                        NineGridTestModel model = new NineGridTestModel();
                        // mList = new ArrayList<>();
                        if (iPage == 1) {
                            model.isDx = true;
                            model.tishi = "暂时没有资料哦~";
                        } else {
                            isDx = true;
                            model.isDx = true;
                            model.tishi = "没有了!我是有底线的!!!";
                        }
                        mList.add(model);
                        // mList = new ArrayList<>();
                        if (iPage == 1) {
                            gridviewdata();
                        } else {
                            gridviewdata1();
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

    private void gridviewdata() {
        pull_refresh_grid_zxss.setAdapter(myAdapterlist);
        // 刷新适配器
        myAdapterlist.notifyDataSetChanged();
        // 关闭刷新下拉
        pull_refresh_grid_zxss.onRefreshComplete();

    }

    private void gridviewdata1() {
        // myList = getMenuAdapter();
        // iPage += 1;
        // 刷新适配器
        myAdapterlist.notifyDataSetChanged();
        // Call onRefreshComplete when the list has been refreshed.
        // 关闭上拉加载
        pull_refresh_grid_zxss.onRefreshComplete();

    }


    private class MyAdapterLIST extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater1;
        /*        public ArrayList<String> arrdz;
                public ArrayList<HashMap<String, Object>> arrmylist;*/


        public MyAdapterLIST(Context context) {
            super();
            this.context = context;
            inflater1 = LayoutInflater.from(context);
         /*   arrdz = new ArrayList<>();
           arrmylist = new ArrayList<HashMap<String, Object>>();*/

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mList.size();
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
            String sImg = mList.get(position).img;
     /*       if (ZixunFragment.iPositionId == 0) {
                view = inflater1.inflate(R.layout.zx_tj1_1, null);
            } else if (ZixunFragment.iPositionId == 1) {

            } else if (ZixunFragment.iPositionId == 2) {
                view = inflater1.inflate(R.layout.zx_zt1_1, null);
            } else if (ZixunFragment.iPositionId == 3) {
                view = inflater1.inflate(R.layout.zx_tj3_1, null);
            } else {*/
            // if (ZixunFragment.iPositionId == 8) {
            if (mList.get(position).isDx) {
                view = inflater1.inflate(R.layout.ts_item, null);
                TextView ti_item = view.findViewById(R.id.ti_item);
                ti_item.setText(mList.get(position).tishi);
            } else {
                if (sZhixun.equals("")) {
                } else if (sZhixun.equals("1")) {
                    if (sImg.equals("") && mList.get(position).urlList.size() == 0) {
                        view = inflater1.inflate(R.layout.zx_tj1_4, null);
                        LinearLayout ll_zx_tj1_4_item = view.findViewById(R.id.ll_zx_tj1_4_item);
                        TextView tv_zx_tj1_4_title = view.findViewById(R.id.tv_zx_tj1_4_title);
                        TextView tv_zx_tj1_4_getName = view.findViewById(R.id.tv_zx_tj1_4_getName);
                        TextView tv_zx_tj1_4_time = view.findViewById(R.id.tv_zx_tj1_4_time);
                        TextView tv_zx_tj1_4_pinglun_num = view.findViewById(R.id.tv_zx_tj1_4_pinglun_num);
                        tv_zx_tj1_4_title.setText(mList.get(position).title);
                        tv_zx_tj1_4_getName.setText(mList.get(position).getName);
                        tv_zx_tj1_4_time.setText(mList.get(position).time);
                        tv_zx_tj1_4_pinglun_num.setText(mList.get(position).pinglun_num);
                        ll_zx_tj1_4_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ZxssActivity.this, GuanggaoActivity.class);
                                intent.putExtra("link", mList.get(position).url + sUser_id);
                                startActivity(intent);
                            }
                        });
                    } else if (mList.get(position).urlList.size() > 0) {
                        view = inflater1.inflate(R.layout.zx_tj1_2, null);
                        LinearLayout ll_zx_tj1_2_item = view.findViewById(R.id.ll_zx_tj1_2_item);
                        TextView tv_zx_tj1_2_title = view.findViewById(R.id.tv_zx_tj1_2_title);
                        TextView tv_zx_tj1_2_getName = view.findViewById(R.id.tv_zx_tj1_2_getName);
                        TextView tv_zx_tj1_2_time = view.findViewById(R.id.tv_zx_tj1_2_time);
                        TextView tv_zx_tj1_2_pinglun_num = view.findViewById(R.id.tv_zx_tj1_2_pinglun_num);
                        //     NineGridTestLayout layout_nine_grid = view.findViewById(R.id.layout_nine_grid);
                        tv_zx_tj1_2_title.setText(mList.get(position).title);
                        tv_zx_tj1_2_getName.setText(mList.get(position).getName);
                        tv_zx_tj1_2_time.setText(mList.get(position).time);
                        tv_zx_tj1_2_pinglun_num.setText(mList.get(position).pinglun_num + "评论");
                        // layout_nine_grid.setUrlList(mList.get(position).urlList);
                        ImageView iv_zx_tj1_2_img = view.findViewById(R.id.iv_zx_tj1_2_img);
                        ImageView iv_zx_tj1_2_img1 = view.findViewById(R.id.iv_zx_tj1_2_img1);
                        ImageView iv_zx_tj1_2_img2 = view.findViewById(R.id.iv_zx_tj1_2_img2);
                        if (mList.get(position).urlList.size() == 1) {
                            Glide.with(ZxssActivity.this).load( Api.sUrl+mList.get(position).urlList.get(0))
                                    .asBitmap()
                                    .placeholder(R.mipmap.error)
                                    .error(R.mipmap.error)
                                    .into(iv_zx_tj1_2_img);
                        } else if (mList.get(position).urlList.size() == 2) {
                            Glide.with(ZxssActivity.this).load( Api.sUrl+mList.get(position).urlList.get(0))
                                    .asBitmap()
                                    .placeholder(R.mipmap.error)
                                    .error(R.mipmap.error)
                                    .into(iv_zx_tj1_2_img);
                            Glide.with(ZxssActivity.this).load( Api.sUrl+mList.get(position).urlList.get(1))
                                    .asBitmap()
                                    .placeholder(R.mipmap.error)
                                    .error(R.mipmap.error)
                                    .into(iv_zx_tj1_2_img1);
                        } else if (mList.get(position).urlList.size() == 3) {
                            Glide.with(ZxssActivity.this).load( Api.sUrl+mList.get(position).urlList.get(0))
                                    .asBitmap()
                                    .placeholder(R.mipmap.error)
                                    .error(R.mipmap.error)
                                    .into(iv_zx_tj1_2_img);
                            Glide.with(ZxssActivity.this).load( Api.sUrl+mList.get(position).urlList.get(1))
                                    .asBitmap()
                                    .placeholder(R.mipmap.error)
                                    .error(R.mipmap.error)
                                    .into(iv_zx_tj1_2_img1);
                            Glide.with(ZxssActivity.this).load( Api.sUrl+mList.get(position).urlList.get(2))
                                    .asBitmap()
                                    .placeholder(R.mipmap.error)
                                    .error(R.mipmap.error)
                                    .into(iv_zx_tj1_2_img2);
                        }
                        ll_zx_tj1_2_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ZxssActivity.this, GuanggaoActivity.class);
                                intent.putExtra("link", mList.get(position).url + sUser_id);
                                startActivity(intent);
                            }
                        });

                    } else if (!sImg.equals("")) {
                        view = inflater1.inflate(R.layout.zx_tj1_1, null);
                        LinearLayout ll_zx_tj1_1_item = view.findViewById(R.id.ll_zx_tj1_1_item);
                        TextView tv_zx_tj1_1_title = view.findViewById(R.id.tv_zx_tj1_1_title);
                        TextView tv_zx_tj1_1_getName = view.findViewById(R.id.tv_zx_tj1_1_getName);
                        TextView tv_zx_tj1_1_time = view.findViewById(R.id.tv_zx_tj1_1_time);
                        TextView tv_zx_tj1_1_pinglun_num = view.findViewById(R.id.tv_zx_tj1_1_pinglun_num);
                        ImageView iv_zx_tj1_1_img = view.findViewById(R.id.iv_zx_tj1_1_img);
                        tv_zx_tj1_1_title.setText(mList.get(position).title);
                        tv_zx_tj1_1_getName.setText(mList.get(position).getName);
                        tv_zx_tj1_1_time.setText(mList.get(position).time);
                        tv_zx_tj1_1_pinglun_num.setText(mList.get(position).pinglun_num + "评论");
                        Glide.with(ZxssActivity.this).load( Api.sUrl+mList.get(position).img)
                                .asBitmap()
                                .placeholder(R.mipmap.error)
                                .error(R.mipmap.error)
                                .into(iv_zx_tj1_1_img);

                        ll_zx_tj1_1_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ZxssActivity.this, GuanggaoActivity.class);
                                intent.putExtra("link", mList.get(position).url + sUser_id);
                                startActivity(intent);
                            }
                        });
                    } else if (position == 3) {
                        view = inflater1.inflate(R.layout.zx_tj1_3, null);
                    }
                } else if (sZhixun.equals("2")) {
                    if (sImg.equals("") && mList.get(position).urlList.size() == 0) {
                        view = inflater1.inflate(R.layout.zx_tj1_4, null);
                        LinearLayout ll_zx_tj1_4_item = view.findViewById(R.id.ll_zx_tj1_4_item);
                        TextView tv_zx_tj1_4_title = view.findViewById(R.id.tv_zx_tj1_4_title);
                        TextView tv_zx_tj1_4_getName = view.findViewById(R.id.tv_zx_tj1_4_getName);
                        TextView tv_zx_tj1_4_time = view.findViewById(R.id.tv_zx_tj1_4_time);
                        TextView tv_zx_tj1_4_pinglun_num = view.findViewById(R.id.tv_zx_tj1_4_pinglun_num);
                        tv_zx_tj1_4_title.setText(mList.get(position).title);
                        tv_zx_tj1_4_getName.setText(mList.get(position).getName);
                        tv_zx_tj1_4_time.setText(mList.get(position).time);
                        tv_zx_tj1_4_pinglun_num.setText(mList.get(position).pinglun_num);
                        ll_zx_tj1_4_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ZxssActivity.this, GuanggaoActivity.class);
                                intent.putExtra("link", mList.get(position).url + sUser_id);
                                startActivity(intent);
                            }
                        });
                    } else {
                        view = inflater1.inflate(R.layout.zx_tj2_1, null);
                        LinearLayout ll_zx_tj2_1_item = view.findViewById(R.id.ll_zx_tj2_1_item);
                        TextView tv_zx_tj2_1_title = view.findViewById(R.id.tv_zx_tj2_1_title);
                        TextView tv_zx_tj2_1_xq = view.findViewById(R.id.tv_zx_tj2_1_xq);
                        TextView tv_zx_tj2_1_time = view.findViewById(R.id.tv_zx_tj2_1_time);
                        TextView tv_zx_tj2_1_pinglun_num = view.findViewById(R.id.tv_zx_tj2_1_pinglun_num);
                        ImageView iv_zx_tj2_1_img = view.findViewById(R.id.iv_zx_tj2_1_img);
                        tv_zx_tj2_1_title.setText(mList.get(position).title);
                        tv_zx_tj2_1_xq.setText(mList.get(position).detail);
                        //    tv_zx_tj1_1_getName.setText(mList.get(position).getName);
                        tv_zx_tj2_1_time.setText(mList.get(position).time);
                        tv_zx_tj2_1_pinglun_num.setText(mList.get(position).pinglun_num + "评论");
                        Glide.with(ZxssActivity.this).load( Api.sUrl+mList.get(position).img)
                                .asBitmap()
                                .placeholder(R.mipmap.error)
                                .error(R.mipmap.error)
                                .into(iv_zx_tj2_1_img);

                        ll_zx_tj2_1_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ZxssActivity.this, GuanggaoActivity.class);
                                intent.putExtra("link", mList.get(position).url + sUser_id);
                                startActivity(intent);
                            }
                        });
                    }
                } else if (sZhixun.equals("3")) {
                    if (sImg.equals("") && mList.get(position).urlList.size() == 0) {
                        view = inflater1.inflate(R.layout.zx_tj1_4, null);
                        LinearLayout ll_zx_tj1_4_item = view.findViewById(R.id.ll_zx_tj1_4_item);
                        TextView tv_zx_tj1_4_title = view.findViewById(R.id.tv_zx_tj1_4_title);
                        TextView tv_zx_tj1_4_getName = view.findViewById(R.id.tv_zx_tj1_4_getName);
                        TextView tv_zx_tj1_4_time = view.findViewById(R.id.tv_zx_tj1_4_time);
                        TextView tv_zx_tj1_4_pinglun_num = view.findViewById(R.id.tv_zx_tj1_4_pinglun_num);
                        tv_zx_tj1_4_title.setText(mList.get(position).title);
                        tv_zx_tj1_4_getName.setText(mList.get(position).getName);
                        tv_zx_tj1_4_time.setText(mList.get(position).time);
                        tv_zx_tj1_4_pinglun_num.setText(mList.get(position).pinglun_num);
                        ll_zx_tj1_4_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ZxssActivity.this, GuanggaoActivity.class);
                                intent.putExtra("link", mList.get(position).url + sUser_id);
                                startActivity(intent);
                            }
                        });
                    } else {
                        view = inflater1.inflate(R.layout.zx_tj3_1, null);
                        LinearLayout ll_zx_tj3_1_item = view.findViewById(R.id.ll_zx_tj3_1_item);
                        TextView tv_zx_tj3_1_title = view.findViewById(R.id.tv_zx_tj3_1_title);
                        TextView tv_zx_tj3_1_xq = view.findViewById(R.id.tv_zx_tj3_1_xq);
                        TextView tv_zx_tj3_1_pinglun_num = view.findViewById(R.id.tv_zx_tj3_1_pinglun_num);
                        //     NineGridTestLayout layout_nine_grid = view.findViewById(R.id.layout_nine_grid);
                        TextView tv_zx_tj3_1_view = view.findViewById(R.id.tv_zx_tj3_1_view);
                        tv_zx_tj3_1_title.setText(mList.get(position).title);
                        tv_zx_tj3_1_view.setText(mList.get(position).view + "次浏览");
                        tv_zx_tj3_1_xq.setText(mList.get(position).detail);
                        //tv_zx_tj1_2_time.setText(mList.get(position).time);
                        tv_zx_tj3_1_pinglun_num.setText(mList.get(position).pinglun_num + "评论");
                        // layout_nine_grid.setUrlList(mList.get(position).urlList);
                        ImageView iv_zx_tj3_1_img = view.findViewById(R.id.iv_zx_tj3_1_img);
                        ImageView iv_zx_tj3_1_img1 = view.findViewById(R.id.iv_zx_tj3_1_img1);
                        ImageView iv_zx_tj3_1_img2 = view.findViewById(R.id.iv_zx_tj3_1_img2);
                        if (mList.get(position).urlList.size() == 0) {
                            Glide.with(ZxssActivity.this).load( Api.sUrl+mList.get(position).img)
                                    .asBitmap()
                                    .placeholder(R.mipmap.error)
                                    .error(R.mipmap.error)
                                    .into(iv_zx_tj3_1_img);
                        } else {
                            if (mList.get(position).urlList.size() == 1) {
                                Glide.with(ZxssActivity.this).load( Api.sUrl+mList.get(position).urlList.get(0))
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .into(iv_zx_tj3_1_img);
                            } else if (mList.get(position).urlList.size() == 2) {
                                Glide.with(ZxssActivity.this).load( Api.sUrl+mList.get(position).urlList.get(0))
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .into(iv_zx_tj3_1_img);
                                Glide.with(ZxssActivity.this).load( Api.sUrl+mList.get(position).urlList.get(1))
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .into(iv_zx_tj3_1_img1);
                            } else if (mList.get(position).urlList.size() == 3) {
                                Glide.with(ZxssActivity.this).load( Api.sUrl+mList.get(position).urlList.get(0))
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .into(iv_zx_tj3_1_img);
                                Glide.with(ZxssActivity.this).load( Api.sUrl+mList.get(position).urlList.get(1))
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .into(iv_zx_tj3_1_img1);
                                Glide.with(ZxssActivity.this).load( Api.sUrl+mList.get(position).urlList.get(2))
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .into(iv_zx_tj3_1_img2);
                            }
                        }
                        ll_zx_tj3_1_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ZxssActivity.this, GuanggaoActivity.class);
                                intent.putExtra("link", mList.get(position).url + sUser_id);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
      /*      } else {
                view = inflater1.inflate(R.layout.zx_zt1_1, null);
            }*/

            return view;
        }
    }


    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(ZxssActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(ZxssActivity.this, R.style.dialog, sHint, type, true).show();
    }
}
