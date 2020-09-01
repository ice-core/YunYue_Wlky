package com.example.administrator.yunyue.sc_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
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
import com.bumptech.glide.Glide;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc.CircleImageView;
import com.example.administrator.yunyue.tu_model.NineGridTestModel;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoucangActivity extends AppCompatActivity {
    private static final String TAG = ShoucangActivity.class.getSimpleName();
    private Button bu_shoucang_delete;//删除

    private TextView tv_shoucang_bianji;//编辑
    private SharedPreferences pref;
    private RequestQueue queue = null;
    private String sUser_id;
    private MyAdapter myAdapter;
    private ArrayList<String> sItem;
    private ImageView iv_shoucang_back;
    private int iCollectId = 0;
    private LinearLayout ll_shoucang_kong;
    private PullToRefreshGridView pull_refresh_grid_sc;
    int iPage = 1;
    private LinearLayout ll_shoucang_delete;
    private String[] tab = {"资讯", "店铺", "商品"};
    private GridView gv_sc_tab;
    private int tab_position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_shoucang);
        sItem = new ArrayList<String>();

        queue = Volley.newRequestQueue(ShoucangActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        tv_shoucang_bianji = (TextView) findViewById(R.id.tv_shoucang_bianji);
        bu_shoucang_delete = (Button) findViewById(R.id.bu_shoucang_delete);
        ll_shoucang_delete = findViewById(R.id.ll_shoucang_delete);
        iv_shoucang_back = (ImageView) findViewById(R.id.iv_shoucang_back);
        ll_shoucang_kong = findViewById(R.id.ll_shoucang_kong);
        gv_sc_tab = findViewById(R.id.gv_sc_tab);
        bu_shoucang_delete.setVisibility(View.GONE);
        ll_shoucang_delete.setVisibility(View.GONE);
        pull_refresh_grid_sc = (PullToRefreshGridView) findViewById(R.id.pull_refresh_grid_sc);
        pull_refresh_grid_sc.setMode(PullToRefreshBase.Mode.BOTH);
        pull_refresh_grid_sc.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
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
                huoqu();
            }

            // 上拉加载跟多
            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                Log.e("TAG", "onPullUpToRefresh"); // Do work to refresh
                huoqu();
                iPage += 1;
                // 页数
                //   p = p + 1;
                // AsyncTask异步交互加载数据
                // new GetDataTask2().execute(URL + p);
            }
        });
        tv_shoucang_bianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_shoucang_bianji.getText().equals("编辑")) {
                    tv_shoucang_bianji.setText("完成");
                    myAdapter.Visibility = "V";
                    myAdapter.notifyDataSetChanged();
                    bu_shoucang_delete.setVisibility(View.VISIBLE);
                    ll_shoucang_delete.setVisibility(View.VISIBLE);
                } else {
                    tv_shoucang_bianji.setText("编辑");
                    myAdapter.Visibility = "G";
                    myAdapter.notifyDataSetChanged();
                    bu_shoucang_delete.setVisibility(View.GONE);
                    ll_shoucang_delete.setVisibility(View.GONE);
                }
            }
        });
        bu_shoucang_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (iCollectId == 0) {
                    Hint("未进行选择！", HintDialog.WARM);
                } else {
                    dialogin("");
                    delete(String.valueOf(iCollectId));
                }
            }
        });
        iv_shoucang_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        dialogin("");
        huoqu();
        tab();
    }

    private void tab() {
        MyAdapter_tab myAdapter_tab = new MyAdapter_tab(ShoucangActivity.this);
        for (int i = 0; i < tab.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemName", tab[i]);
            myAdapter_tab.arrlist.add(map);
        }
        gv_sc_tab.setAdapter(myAdapter_tab);
    }


    /**
     * 删除商品收藏
     */
    private void delete(String good_id) {
        String url = Api.sUrl;
        if (tab_position == 0) {
            url = url + "Api/Getnew/shouchangdel" + "/news_id/" + good_id;
        } else if (tab_position == 1) {
            url = url + "Api/Good/guanzhushangjia" + "/shangjia_id/" + good_id;
        } else if (tab_position == 2) {
            url = url + "Api/Good/shouchang" + "/good_id/" + good_id;
        }
        url = url + "/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id;
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
                        huoqu();
                        // Hint(resultMsg, HintDialog.SUCCESS);
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


    private void jianjei(final String pid) {
        String url = Api.sUrl + "Api/Good/gooddetail/appId/" + Api.sApp_Id
                + "/good_id/" + pid + "/user_id/" + sUser_id;
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
                        Intent intent1 = new Intent(ShoucangActivity.this, SpxqActivity.class);
                        intent1.putExtra("goods_id", pid);
                        intent1.putExtra("data", sDate);
                        intent1.putExtra("activty", "main");
                        startActivity(intent1);
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

    private void huoqu() {
        String url = Api.sUrl;
        if (tab_position == 0) {
            url = url + "Api/Getnew/getshouchanglist/";
        } else if (tab_position == 1) {
            url = url + "Api/Good/shouchangdian/";
        } else if (tab_position == 2) {
            url = url + "Api/Good/shouchanglist/";
        }
        url = url + "appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/page/" + iPage;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                myAdapter = new MyAdapter(ShoucangActivity.this);
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                tv_shoucang_bianji.setText("编辑");
                myAdapter.Visibility = "G";
                myAdapter.notifyDataSetChanged();
                bu_shoucang_delete.setVisibility(View.GONE);
                ll_shoucang_delete.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    List<NineGridTestModel> mList = new ArrayList<>();
                    ArrayList<HashMap<String, String>> mylist_dp = new ArrayList<HashMap<String, String>>();
                    ArrayList<HashMap<String, String>> mylist_sp = new ArrayList<HashMap<String, String>>();
                    if (resultCode > 0) {
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");

                        if (tab_position == 0) {

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
                                String resultView = "";
                                try {
                                    resultView = jsonObject.getString("view");
                                } catch (JSONException e) {
                                    resultView = "";
                                    hideDialogin();
                                    e.printStackTrace();
                                }
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
                        } else if (tab_position == 1) {
                            for (int i = 0; i < resultJsonArray.length(); i++) {
                                jsonObject = resultJsonArray.getJSONObject(i);
                                //String resultAddressId = jsonObject.getString("head_pic");
                                String resultShangjia_Id = jsonObject.getString("shangjia_id");
                                String resultShangjianame = jsonObject.getString("shangjianame");
                                String resultShangjialogo = jsonObject.getString("shangjialogo");
                                String resultNewgoods = jsonObject.getString("newgoods");
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("shangjia_id", resultShangjia_Id);
                                map.put("shangjianame", resultShangjianame);
                                map.put("shangjialogo", resultShangjialogo);
                                map.put("newgoods", resultNewgoods);
                                mylist_dp.add(map);
                            }
                        } else if (tab_position == 2) {
                            for (int i = 0; i < resultJsonArray.length(); i++) {
                                jsonObject = resultJsonArray.getJSONObject(i);
                                //String resultAddressId = jsonObject.getString("head_pic");
                                String resultId = jsonObject.getString("id");
                                String resultLogo = jsonObject.getString("logo");
                                String resultName = jsonObject.getString("name");
                                String resultPrice = jsonObject.getString("price");
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("id", resultId);
                                map.put("logo", resultLogo);
                                map.put("name", resultName);
                                map.put("price", resultPrice);
                                mylist_sp.add(map);
                            }
                        }
                        if (iPage == 1) {
                            if (tab_position == 0) {
                                if (mList.size() == 0) {
                                    ll_shoucang_kong.setVisibility(View.VISIBLE);
                                    pull_refresh_grid_sc.setVisibility(View.GONE);
                                } else {
                                    ll_shoucang_kong.setVisibility(View.GONE);
                                    pull_refresh_grid_sc.setVisibility(View.VISIBLE);
                                }
                            } else if (tab_position == 1) {
                                if (mylist_dp.size() == 0) {
                                    ll_shoucang_kong.setVisibility(View.VISIBLE);
                                    pull_refresh_grid_sc.setVisibility(View.GONE);
                                } else {
                                    ll_shoucang_kong.setVisibility(View.GONE);
                                    pull_refresh_grid_sc.setVisibility(View.VISIBLE);
                                }
                            } else if (tab_position == 2) {
                                if (mylist_sp.size() == 0) {
                                    ll_shoucang_kong.setVisibility(View.VISIBLE);
                                    pull_refresh_grid_sc.setVisibility(View.GONE);
                                } else {
                                    ll_shoucang_kong.setVisibility(View.GONE);
                                    pull_refresh_grid_sc.setVisibility(View.VISIBLE);
                                }
                            }
                            gridviewdata(mList, mylist_dp, mylist_sp);
                        } else {
                            gridviewdata1(mList, mylist_dp, mylist_sp);
                        }

                    } else {
                        if (iPage == 1) {
                            if (tab_position == 0) {
                                if (mList.size() == 0) {
                                    ll_shoucang_kong.setVisibility(View.VISIBLE);
                                    pull_refresh_grid_sc.setVisibility(View.GONE);
                                } else {
                                    ll_shoucang_kong.setVisibility(View.GONE);
                                    pull_refresh_grid_sc.setVisibility(View.VISIBLE);
                                }
                            } else if (tab_position == 1) {
                                if (mylist_dp.size() == 0) {
                                    ll_shoucang_kong.setVisibility(View.VISIBLE);
                                    pull_refresh_grid_sc.setVisibility(View.GONE);
                                } else {
                                    ll_shoucang_kong.setVisibility(View.GONE);
                                    pull_refresh_grid_sc.setVisibility(View.VISIBLE);
                                }
                            } else if (tab_position == 2) {
                                if (mylist_sp.size() == 0) {
                                    ll_shoucang_kong.setVisibility(View.VISIBLE);
                                    pull_refresh_grid_sc.setVisibility(View.GONE);
                                } else {
                                    ll_shoucang_kong.setVisibility(View.GONE);
                                    pull_refresh_grid_sc.setVisibility(View.VISIBLE);
                                }
                            }
                            gridviewdata(mList, mylist_dp, mylist_sp);
                        } else {
                            gridviewdata1(mList, mylist_dp, mylist_sp);
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

    private void gridviewdata(List<NineGridTestModel> List, ArrayList<HashMap<String, String>> myList_dp, ArrayList<HashMap<String, String>> myList_sp) {
        // iPage += 1;
        myAdapter = new MyAdapter(ShoucangActivity.this);
        myAdapter.mList = List;
        myAdapter.arrlist_dp = myList_dp;
        myAdapter.arrlist_sp = myList_sp;
        pull_refresh_grid_sc.setAdapter(myAdapter);
        // 刷新适配器
        myAdapter.notifyDataSetChanged();
        // 关闭刷新下拉
        pull_refresh_grid_sc.onRefreshComplete();
    }

    private void gridviewdata1(List<NineGridTestModel> List, ArrayList<HashMap<String, String>> myList_dp, ArrayList<HashMap<String, String>> myList_sp) {
        //    myList = getMenuAdapter();
        //iPage += 1;
        // 刷新适配器
        myAdapter.mList.addAll(List);
        myAdapter.arrlist_dp.addAll(myList_dp);
        myAdapter.arrlist_sp.addAll(myList_sp);
        myAdapter.notifyDataSetChanged();
        // Call onRefreshComplete when the list has been refreshed.
        // 关闭上拉加载
        pull_refresh_grid_sc.onRefreshComplete();

    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(ShoucangActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(ShoucangActivity.this, R.style.dialog, sHint, type, true).show();
    }


    private void back() {
       /* Intent intent = new Intent(this, ScMainActivity.class);
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

    private class MyAdapter_tab extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;


        public String Visibility = "G";

        ArrayList<HashMap<String, String>> arrlist;

        public MyAdapter_tab(Context context) {
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
                view = inflater.inflate(R.layout.tab_item, null);
            }
            TextView tv_tab_item_name = view.findViewById(R.id.tv_tab_item_name);
            TextView tv_tab_item_xhx = view.findViewById(R.id.tv_tab_item_xhx);
            tv_tab_item_name.setText(arrlist.get(position).get("ItemName"));
            if (position == tab_position) {
                tv_tab_item_name.setTextColor(tv_tab_item_name.getResources().getColor(R.color.theme));
                tv_tab_item_xhx.setBackgroundResource(R.color.theme);
            } else {
                tv_tab_item_name.setTextColor(tv_tab_item_name.getResources().getColor(R.color.hei333333));
                tv_tab_item_xhx.setBackgroundResource(R.color.touming);
            }
            tv_tab_item_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tab_position = position;
                    dialogin("");
                    huoqu();
                    notifyDataSetChanged();
                }
            });


            return view;
        }
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        public String Visibility = "G";
        List<NineGridTestModel> mList;
        ArrayList<HashMap<String, String>> arrlist_dp;
        ArrayList<HashMap<String, String>> arrlist_sp;


        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            mList = new ArrayList<>();
            arrlist_dp = new ArrayList<HashMap<String, String>>();
            arrlist_sp = new ArrayList<HashMap<String, String>>();

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            int num = 0;
            if (tab_position == 0) {
                num = mList.size();
            } else if (tab_position == 1) {
                num = arrlist_dp.size();
            } else if (tab_position == 2) {
                num = arrlist_sp.size();
            }
            return num;
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
            if (tab_position == 0) {

                view = inflater.inflate(R.layout.zx_tj1_1, null);
                LinearLayout ll_zx_tj1_1_item = view.findViewById(R.id.ll_zx_tj1_1_item);
                TextView tv_zx_tj1_1_title = view.findViewById(R.id.tv_zx_tj1_1_title);
                TextView tv_zx_tj1_1_getName = view.findViewById(R.id.tv_zx_tj1_1_getName);
                TextView tv_zx_tj1_1_time = view.findViewById(R.id.tv_zx_tj1_1_time);
                TextView tv_zx_tj1_1_pinglun_num = view.findViewById(R.id.tv_zx_tj1_1_pinglun_num);
                ImageView iv_zx_tj1_1_img = view.findViewById(R.id.iv_zx_tj1_1_img);
                CheckBox ck_zx_shoucang = view.findViewById(R.id.ck_zx_shoucang);

                if (Visibility.equals("V")) {
                    ll_zx_tj1_1_item.scrollBy(-100, 0);
                    ck_zx_shoucang.setVisibility(View.VISIBLE);
                } else if (Visibility.equals("G")) {
                    ll_zx_tj1_1_item.scrollBy(0, 0);
                    ck_zx_shoucang.setVisibility(View.GONE);
                }
                tv_zx_tj1_1_title.setText(mList.get(position).title);
                tv_zx_tj1_1_getName.setText(mList.get(position).getName);
                tv_zx_tj1_1_time.setText(mList.get(position).time);
                tv_zx_tj1_1_pinglun_num.setText(mList.get(position).pinglun_num + "评论");
                Glide.with(ShoucangActivity.this).load( Api.sUrl+mList.get(position).img)
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .into(iv_zx_tj1_1_img);

                ll_zx_tj1_1_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ShoucangActivity.this, GuanggaoActivity.class);
                        intent.putExtra("link", mList.get(position).url + sUser_id);
                        startActivity(intent);
                    }
                });

                ck_zx_shoucang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        iCollectId = Integer.valueOf(mList.get(position).id);
                        notifyDataSetChanged();
                    }
                });
                if (iCollectId == Integer.valueOf(mList.get(position).id)) {
                    ck_zx_shoucang.setChecked(true);
                } else {
                    ck_zx_shoucang.setChecked(false);
                }

            } else if (tab_position == 1) {


                view = inflater.inflate(R.layout.dp_sc_item, null);
                LinearLayout ll_dp_sc_item = view.findViewById(R.id.ll_dp_sc_item);
                CircleImageView civ_dp_sc_item = view.findViewById(R.id.civ_dp_sc_item);
                TextView tv_sp_sc_item_shangjianame = view.findViewById(R.id.tv_sp_sc_item_shangjianame);
                TextView tv_sp_sc_item_newgoods = view.findViewById(R.id.tv_sp_sc_item_newgoods);
                CheckBox ck_dp_sc = view.findViewById(R.id.ck_dp_sc);
                Glide.with(ShoucangActivity.this)
                        .load( Api.sUrl+ arrlist_dp.get(position).get("shangjialogo"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .into(civ_dp_sc_item);
                tv_sp_sc_item_shangjianame.setText(arrlist_dp.get(position).get("shangjianame"));
                tv_sp_sc_item_newgoods.setText(arrlist_dp.get(position).get("newgoods"));
                if (Visibility.equals("V")) {
                    ll_dp_sc_item.scrollBy(-100, 0);
                    ck_dp_sc.setVisibility(View.VISIBLE);
                } else if (Visibility.equals("G")) {
                    ll_dp_sc_item.scrollBy(0, 0);
                    ck_dp_sc.setVisibility(View.GONE);
                }
                ck_dp_sc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        iCollectId = Integer.valueOf(arrlist_dp.get(position).get("shangjia_id"));
                        notifyDataSetChanged();
                    }
                });
                if (iCollectId == Integer.valueOf(arrlist_dp.get(position).get("shangjia_id"))) {
                    ck_dp_sc.setChecked(true);
                } else {
                    ck_dp_sc.setChecked(false);
                }
                ll_dp_sc_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ShoucangActivity.this, DpzyActivity.class);
                        intent.putExtra("id", arrlist_dp.get(position).get("shangjia_id"));
                        startActivity(intent);
                    }
                });
   /*             map.put("shangjia_id", resultShangjia_Id);
                map.put("shangjianame", resultShangjianame);
                map.put("shangjialogo", resultShangjialogo);
                map.put("newgoods", resultNewgoods);*/

            } else if (tab_position == 2) {
                view = inflater.inflate(R.layout.shoucang_item, null);
                LinearLayout ll_shoucang = (LinearLayout) view.findViewById(R.id.ll_shoucang);
                final CheckBox ck_shoucang = (CheckBox) view.findViewById(R.id.ck_shoucang);
                TextView tv_shoucang_id = (TextView) view.findViewById(R.id.tv_shoucang_id);
                ImageView iv_shoucang_image = (ImageView) view.findViewById(R.id.iv_shoucang_image);
                TextView tv_shoucang_name = (TextView) view.findViewById(R.id.tv_shoucang_name);
                TextView tv_shoucang_jg = (TextView) view.findViewById(R.id.tv_shoucang_jg);
                tv_shoucang_name.setText(arrlist_sp.get(position).get("name"));
                tv_shoucang_jg.setText(arrlist_sp.get(position).get("price"));

                if (Visibility.equals("V")) {
                    ck_shoucang.setVisibility(View.VISIBLE);
                } else if (Visibility.equals("G")) {
                    ck_shoucang.setVisibility(View.GONE);

                }

                Glide.with(ShoucangActivity.this)
                        .load( Api.sUrl+ arrlist_sp.get(position).get("logo"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_shoucang_image);
                ck_shoucang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        iCollectId = Integer.valueOf(arrlist_sp.get(position).get("id"));
                        notifyDataSetChanged();
                    }
                });
                ll_shoucang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogin("");
                        jianjei(arrlist_sp.get(position).get("id"));
                    }
                });
                if (iCollectId == Integer.valueOf(arrlist_sp.get(position).get("id"))) {
                    ck_shoucang.setChecked(true);
                } else {
                    ck_shoucang.setChecked(false);
                }


            }
            return view;
        }
    }
}
