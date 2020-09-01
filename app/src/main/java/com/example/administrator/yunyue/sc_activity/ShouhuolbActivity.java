package com.example.administrator.yunyue.sc_activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.example.administrator.yunyue.sc_fragment.QuanbuFragment;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ShouhuolbActivity extends AppCompatActivity {
    RequestQueue queue = null;
    private SharedPreferences pref;
    private String sUser_id;
    private GridView gv_wode_quanbu;
    //Fragment的View加载完毕的标记
    private boolean isViewCreated;
    //Fragment对用户可见的标记
    private boolean isUIVisible;
    private PullToRefreshGridView pull_refresh_wode_quanbu;
    private int iPage;
    private QuanbuFragment.OnFragmentInteractionListener mListener;
    private MyAdapter myAdapter;
    private ArrayList<HashMap<String, String>> arrmylist;
    private LinearLayout ll_dd_kong;
    private LinearLayout ll_shouhuolb_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_shouhuolb);
        pref = PreferenceManager.getDefaultSharedPreferences(ShouhuolbActivity.this);
        iPage = 1;
        sUser_id = pref.getString("user_id", "");
        queue = Volley.newRequestQueue(ShouhuolbActivity.this);
        pull_refresh_wode_quanbu = findViewById(R.id.pull_refresh_wode_quanbu);
        pull_refresh_wode_quanbu.setMode(PullToRefreshBase.Mode.BOTH);
        gv_wode_quanbu = findViewById(R.id.gv_wode_quanbu);
        ll_dd_kong = findViewById(R.id.ll_dd_kong);
        myAdapter = new MyAdapter(ShouhuolbActivity.this);
        arrmylist = new ArrayList<HashMap<String, String>>();
        ll_shouhuolb_back = findViewById(R.id.ll_shouhuolb_back);
        ll_shouhuolb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        isViewCreated = true;
        iPage = 1;
        huoqu(iPage);
        pull_refresh_wode_quanbu.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
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
                huoqu(iPage);
            }

            // 上拉加载跟多
            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                Log.e("TAG", "onPullUpToRefresh"); // Do work to refresh
                huoqu(iPage);
                // 页数
                //   p = p + 1;
                // AsyncTask异步交互加载数据
                // new GetDataTask2().execute(URL + p);
            }
        });
    }

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        iPage = 1;
        huoqu(iPage);
    }

    private void Delete(String order_id) {
        String url = Api.sUrl + "Api/Order/orderdel/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/order_id/" + order_id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    String resultMsg = jsonObject.getString("msg");
                    int resultCode = jsonObject.getInt("code");
                    if (resultCode > 0) {
                        hideDialogin();
                        dialogin("");
                        iPage = iPage - 1;
                        huoqu(iPage);
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

  /*  Order/UrgedDelivery*/

    private void Cuicu(String order_id, String rid) {
        String url = Api.sUrl + "Order/UrgedDelivery/order_id/" + order_id + "/rid/" + rid;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    String resultMsg = jsonObject.getString("msg");
                    int resultCode = jsonObject.getInt("code");
                    if (resultCode > 0) {
                        Hint(resultMsg, HintDialog.SUCCESS);
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

    private void huoqu(final int page) {
        String url = Api.sUrl + "Api/Order/servicelist/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/page/" + page;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (page == 1) {
                        myAdapter = new MyAdapter(ShouhuolbActivity.this);
                        arrmylist = new ArrayList<HashMap<String, String>>();
                    }
                    if (resultCode > 0) {
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            //订单状态
                            //0 全部 1待付款 2已付款待发货 3已发货待收货 4已收货待评价
                            map.put("ItemState", jsonObject.getString("state"));//id


                            JSONArray resultJsonArrayGoodlist = jsonObject.getJSONArray("list");
                            int Good_num = 0;
                            for (int a = 0; a < resultJsonArrayGoodlist.length(); a++) {
                                JSONObject jsonObjectGood = resultJsonArrayGoodlist.getJSONObject(a);
                                HashMap<String, String> mapgood = new HashMap<String, String>();
                                //店铺名称
                                mapgood.put("ItemShangjia_name", jsonObject.getString("shangjia_name"));
                                //订单状态
                                //0 全部 1待付款 2已付款待发货 3已发货待收货 4已收货待评价
                                mapgood.put("ItemState", jsonObject.getString("state"));
                                //是否申请售后 “”为未申请 1审核中 2审核失败 3审核通过 5已退款
                                mapgood.put("ItemShouhoustate", jsonObject.getString("shouhoustate"));
                                //商品图片
                                mapgood.put("ItemGood_pic", jsonObjectGood.getString("good_pic"));
                                //商品名称
                                mapgood.put("ItemGoods_Name", jsonObjectGood.getString("goods_name"));
                                //商品规格
                                mapgood.put("ItemSpec_Key_Name", jsonObjectGood.getString("spec_key_name"));
                                //商品数量
                                mapgood.put("ItemGoods_num", jsonObjectGood.getString("goods_num"));
                                Good_num += jsonObjectGood.getInt("goods_num");
                                mapgood.put("ItemGoods_Num_And", String.valueOf(Good_num));
                                //商品总价
                                mapgood.put("ItemNew_price", jsonObject.getString("new_price"));
                                //订单ID
                                mapgood.put("ItemId", jsonObject.getString("id"));
                                //商家ID
                                mapgood.put("ItemShangjia_id", jsonObject.getString("shangjia_id"));
                                mapgood.put("ItemShangJiaphone", jsonObject.getString("lianxiphone"));
                                arrmylist.add(mapgood);
                            }
                        }
                        if (page == 1) {
                            if (arrmylist.size() == 0) {
                                ll_dd_kong.setVisibility(View.VISIBLE);
                            } else {
                                ll_dd_kong.setVisibility(View.GONE);
                            }
                            gridviewdata();
                        } else {
                            gridviewdata1();
                        }
                        iPage += 1;

                    } else {
                        if (page == 1) {
                            if (arrmylist.size() == 0) {
                                ll_dd_kong.setVisibility(View.VISIBLE);
                            } else {
                                ll_dd_kong.setVisibility(View.GONE);
                            }
                            gridviewdata();
                        } else {
                            gridviewdata1();
                        }
                        // gv_wode_quanbu.setAdapter(myAdapter);
                    }
                    myAdapter.notifyDataSetChanged();
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


    private void gridviewdata() {
        //iPage += 1;

        pull_refresh_wode_quanbu.setAdapter(myAdapter);
        // 刷新适配器
        myAdapter.notifyDataSetChanged();
        // 关闭刷新下拉
        pull_refresh_wode_quanbu.onRefreshComplete();

    }

    private void gridviewdata1() {
        // myList = getMenuAdapter();
        // iPage += 1;
        // 刷新适配器
        myAdapter.notifyDataSetChanged();
        // Call onRefreshComplete when the list has been refreshed.
        // 关闭上拉加载
        pull_refresh_wode_quanbu.onRefreshComplete();

    }


    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(ShouhuolbActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(ShouhuolbActivity.this, R.style.dialog, sHint, type, true).show();
    }

    private void queren(String sId) {
        String url = Api.sUrl + "Api/Order/shouhuo/appId/" + Api.sApp_Id
                + "/order_id/" + sId + "/user_id/" + sUser_id;
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
                        hideDialogin();
                        dialogin("");
                        iPage = iPage - 1;
                        huoqu(iPage);
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
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }

    public void dialog(final String phone) {
        Dialog dialog = new Dialog(ShouhuolbActivity.this, R.style.ActionSheetDialogStyle);
        final View inflate = LayoutInflater.from(ShouhuolbActivity.this).inflate(R.layout.lxmj_dialog, null);
        final TextView tv_lxmj_dialog = inflate.findViewById(R.id.tv_lxmj_dialog);
        tv_lxmj_dialog.setText(phone);
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
        dialog.show();
        tv_lxmj_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diallPhone(phone);
            }
        });
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void diallPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        //     public ArrayList<String> arr;
        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arrmylist.size();
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
                view = inflater.inflate(R.layout.wddd_item, null);
            }
            TextView tv_wddd_tc = view.findViewById(R.id.tv_wddd_tc);
            LinearLayout ll_wddd_tou = view.findViewById(R.id.ll_wddd_tou);
            LinearLayout ll_wddd_wei = view.findViewById(R.id.ll_wddd_wei);
            LinearLayout ll_wddd_wei1 = view.findViewById(R.id.ll_wddd_wei1);
            TextView tv_wddd_storename = view.findViewById(R.id.tv_wddd_storename);
            final TextView tv_wddd_status = view.findViewById(R.id.tv_wddd_status);
            ImageView iv_wddd = view.findViewById(R.id.iv_wddd);
            TextView tv_wddd_goods_name = view.findViewById(R.id.tv_wddd_goods_name);
            TextView tv_wddd_spec_key_name = view.findViewById(R.id.tv_wddd_spec_key_name);
            TextView tv_wddd_goods_num = view.findViewById(R.id.tv_wddd_goods_num);
            TextView tv_wddd_order_amount = view.findViewById(R.id.tv_wddd_order_amount);
            LinearLayout ll_wddd = view.findViewById(R.id.ll_wddd);
            final TextView tv_wddd_bt = view.findViewById(R.id.tv_wddd_bt);
            final TextView tv_wddd_bt1 = view.findViewById(R.id.tv_wddd_bt1);
            TextView tv_wddd_num = view.findViewById(R.id.tv_wddd_num);
            tv_wddd_num.setText("X" + arrmylist.get(position).get("ItemGoods_num"));
            TextView tv_wddd_shzt = view.findViewById(R.id.tv_wddd_shzt);
            tv_wddd_bt.setVisibility(View.GONE);
            tv_wddd_bt1.setVisibility(View.GONE);
            tv_wddd_tc.setVisibility(View.VISIBLE);
            ll_wddd_tou.setVisibility(View.VISIBLE);
            ll_wddd_wei1.setVisibility(View.VISIBLE);
            ll_wddd_wei.setVisibility(View.VISIBLE);
            if (arrmylist.size() == 0) {
                tv_wddd_tc.setVisibility(View.VISIBLE);
                ll_wddd_tou.setVisibility(View.VISIBLE);
                ll_wddd_wei1.setVisibility(View.VISIBLE);
                ll_wddd_wei.setVisibility(View.VISIBLE);
            } else if (position < arrmylist.size() - 1) {
                if (arrmylist.get(position).get("ItemId").equals(arrmylist.get(position + 1).get("ItemId"))) {
                    if (position == 0) {
                        tv_wddd_tc.setVisibility(View.VISIBLE);
                        ll_wddd_tou.setVisibility(View.VISIBLE);
                        ll_wddd_wei1.setVisibility(View.GONE);
                        ll_wddd_wei.setVisibility(View.GONE);
                    } else if (arrmylist.get(position).get("ItemId").equals(arrmylist.get(position - 1).get("ItemId"))) {
                        tv_wddd_tc.setVisibility(View.GONE);
                        ll_wddd_tou.setVisibility(View.GONE);
                        ll_wddd_wei1.setVisibility(View.GONE);
                        ll_wddd_wei.setVisibility(View.GONE);
                    } else {
                        tv_wddd_tc.setVisibility(View.VISIBLE);
                        ll_wddd_tou.setVisibility(View.VISIBLE);
                        ll_wddd_wei1.setVisibility(View.GONE);
                        ll_wddd_wei.setVisibility(View.GONE);
                    }
                } else {
                    if (position == 0) {
                        tv_wddd_tc.setVisibility(View.VISIBLE);
                        ll_wddd_tou.setVisibility(View.VISIBLE);
                        ll_wddd_wei1.setVisibility(View.VISIBLE);
                        ll_wddd_wei.setVisibility(View.VISIBLE);
                    } else if (arrmylist.get(position - 1).get("ItemId").equals(arrmylist.get(position).get("ItemId"))) {
                        tv_wddd_tc.setVisibility(View.GONE);
                        ll_wddd_tou.setVisibility(View.GONE);
                        ll_wddd_wei1.setVisibility(View.VISIBLE);
                        ll_wddd_wei.setVisibility(View.VISIBLE);
                    } else {
                        tv_wddd_tc.setVisibility(View.VISIBLE);
                        ll_wddd_tou.setVisibility(View.VISIBLE);
                        ll_wddd_wei1.setVisibility(View.VISIBLE);
                        ll_wddd_wei.setVisibility(View.VISIBLE);
                    }
                }
            } else if (position == arrmylist.size() - 1) {
                try {
                    if (arrmylist.get(position - 1).get("ItemId").equals(arrmylist.get(position).get("ItemId"))) {
                        tv_wddd_tc.setVisibility(View.GONE);
                        ll_wddd_tou.setVisibility(View.GONE);
                        ll_wddd_wei1.setVisibility(View.VISIBLE);
                        ll_wddd_wei.setVisibility(View.VISIBLE);
                    } else {
                        tv_wddd_tc.setVisibility(View.VISIBLE);
                        ll_wddd_tou.setVisibility(View.VISIBLE);
                        ll_wddd_wei1.setVisibility(View.VISIBLE);
                        ll_wddd_wei.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    tv_wddd_tc.setVisibility(View.VISIBLE);
                    ll_wddd_tou.setVisibility(View.VISIBLE);
                    ll_wddd_wei1.setVisibility(View.VISIBLE);
                    ll_wddd_wei.setVisibility(View.VISIBLE);
                }
            }

            tv_wddd_storename.setText(arrmylist.get(position).get("ItemShangjia_name"));
            if (arrmylist.get(position).get("ItemState").equals("1")) {
                tv_wddd_status.setText("等待买家付款");
                tv_wddd_shzt.setVisibility(View.GONE);
                tv_wddd_bt.setVisibility(View.VISIBLE);
                tv_wddd_bt1.setVisibility(View.VISIBLE);
                tv_wddd_bt.setText("取消订单");
                tv_wddd_bt1.setText("立即付款");
            } else if (arrmylist.get(position).get("ItemState").equals("2")) {
                tv_wddd_status.setText("等待卖家发货");
                tv_wddd_shzt.setVisibility(View.VISIBLE);
                tv_wddd_bt.setVisibility(View.GONE);
                tv_wddd_bt1.setVisibility(View.VISIBLE);
                tv_wddd_bt1.setText("查看详情");
            } else if (arrmylist.get(position).get("ItemState").equals("3")) {
                tv_wddd_status.setText("等待收货");
                tv_wddd_shzt.setVisibility(View.VISIBLE);
                tv_wddd_bt.setVisibility(View.GONE);
                tv_wddd_bt1.setVisibility(View.VISIBLE);
                tv_wddd_bt1.setText("确认收货");
            } else if (arrmylist.get(position).get("ItemState").equals("4")) {
                tv_wddd_status.setText("等待评论");
                tv_wddd_shzt.setVisibility(View.VISIBLE);
                tv_wddd_bt.setVisibility(View.GONE);
                tv_wddd_bt1.setVisibility(View.VISIBLE);
                tv_wddd_bt.setText("联系卖家");
                tv_wddd_bt1.setText("评论商品");
            } else if (arrmylist.get(position).get("ItemState").equals("5")) {
                tv_wddd_status.setText("交易成功");
                tv_wddd_shzt.setVisibility(View.VISIBLE);
                tv_wddd_bt.setVisibility(View.VISIBLE);
                tv_wddd_bt1.setVisibility(View.GONE);
                tv_wddd_bt.setText("联系卖家");
                tv_wddd_bt1.setText("评论商品");
            }
            if (arrmylist.get(position).get("ItemShouhoustate").equals("")) {
                tv_wddd_shzt.setText("申请售后");
            } else if (arrmylist.get(position).get("ItemShouhoustate").equals("1")) {
                tv_wddd_status.setText("售后审核中");
                tv_wddd_shzt.setText("售后审核中");
            } else if (arrmylist.get(position).get("ItemShouhoustate").equals("2")) {
                tv_wddd_shzt.setText("售后审核失败");
                tv_wddd_status.setText("售后审核失败");
            } else if (arrmylist.get(position).get("ItemShouhoustate").equals("3")) {
                tv_wddd_shzt.setText("售后审核通过");
                tv_wddd_status.setText("售后审核通过");
            } else if (arrmylist.get(position).get("ItemShouhoustate").equals("4")) {
                tv_wddd_shzt.setText("退款中");
                tv_wddd_status.setText("退款中");
            } else if (arrmylist.get(position).get("ItemShouhoustate").equals("5")) {
                tv_wddd_shzt.setText("已退款");
                tv_wddd_status.setText("已退款");
            }

            tv_wddd_shzt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (arrmylist.get(position).get("ItemShouhoustate").equals("")) {
                        Intent intent = new Intent(ShouhuolbActivity.this, SqshActivity.class);
                        intent.putExtra("id", arrmylist.get(position).get("ItemId").toString());
                        intent.putExtra("state", arrmylist.get(position).get("ItemState").toString());
                        startActivity(intent);
                        //  tv_wddd_shzt.setText("申请售后");
                    } else {
                        Intent intent = new Intent(ShouhuolbActivity.this, ShouhouZtActivity.class);
                        intent.putExtra("shouhuostate", arrmylist.get(position).get("ItemShouhoustate"));
                        intent.putExtra("state", arrmylist.get(position).get("ItemState"));
                        intent.putExtra("id", arrmylist.get(position).get("ItemId"));
                        startActivity(intent);
                    }
                }
            });
            Glide.with(ShouhuolbActivity.this)
                    .load( Api.sUrl+ arrmylist.get(position).get("ItemGood_pic"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_wddd);
            tv_wddd_goods_name.setText(arrmylist.get(position).get("ItemGoods_Name"));
            tv_wddd_spec_key_name.setText(arrmylist.get(position).get("ItemSpec_Key_Name"));
            tv_wddd_goods_num.setText("共" + arrmylist.get(position).get("ItemGoods_Num_And") + "件商品");
            tv_wddd_order_amount.setText("￥" + arrmylist.get(position).get("ItemNew_price"));
            tv_wddd_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv_wddd_bt.getText().toString().equals("取消订单")) {
                        hideDialogin();
                        dialogin("");
                        Delete(arrmylist.get(position).get("ItemId").toString());
                    } else if (tv_wddd_bt.getText().toString().equals("联系卖家")) {
                        dialog(arrmylist.get(position).get("ItemShangJiaphone").toString());
                    }
                }
            });
            tv_wddd_bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv_wddd_bt1.getText().toString().equals("立即付款")) {
                        Intent intent = new Intent(ShouhuolbActivity.this, ZffsActivity.class);
                        intent.putExtra("activity", "wddd");
                        intent.putExtra("data", arrmylist.get(position).get("ItemId"));
                        intent.putExtra("money", arrmylist.get(position).get("ItemNew_price"));
                        startActivity(intent);
                    } else if (tv_wddd_bt1.getText().toString().equals("查看详情")) {
                        if (arrmylist.get(position).get("ItemShouhoustate").equals("5")) {
                            Intent intent = new Intent(ShouhuolbActivity.this, TkcgActivity.class);
                            intent.putExtra("id", arrmylist.get(position).get("ItemId"));
                            startActivity(intent);
                        } else if (arrmylist.get(position).get("ItemShouhoustate").equals("3")) {
                            Intent intent = new Intent(ShouhuolbActivity.this, ShouhouZtActivity.class);
                            intent.putExtra("shouhuostate", arrmylist.get(position).get("ItemShouhoustate"));
                            intent.putExtra("state", arrmylist.get(position).get("ItemState"));
                            intent.putExtra("id", arrmylist.get(position).get("ItemId"));
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(ShouhuolbActivity.this, CkddActivity.class);
                            intent.putExtra("activity", tv_wddd_status.getText().toString());
                            intent.putExtra("id", arrmylist.get(position).get("ItemId"));
                            startActivity(intent);
                        }
                        // Cuicu(arrmylist.get(position).get("ItemOrder_Id"), arrmylist.get(position).get("ItemRid"));
                    } else if (tv_wddd_bt1.getText().toString().equals("确认收货")) {
                        hideDialogin();
                        dialogin("");
                        queren(arrmylist.get(position).get("ItemId"));
                    } else if (tv_wddd_bt1.getText().toString().equals("评论商品")) {
                        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                        Intent intent = new Intent(ShouhuolbActivity.this, PinglunActivity.class);
                        for (int i = 0; i < arrmylist.size(); i++) {
                            if (arrmylist.get(position).get("ItemId").equals(arrmylist.get(i).get("ItemId"))) {
                                list.add(arrmylist.get(i));
                            }
                        }
                        PinglunActivity.list = list;
                        intent.putExtra("id", arrmylist.get(position).get("ItemId").toString());
                        startActivity(intent);
                        //  }
                    }
                }
            });
            ll_wddd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ShouhuolbActivity.this, CkddActivity.class);
                    intent.putExtra("activity", tv_wddd_status.getText().toString());
                    intent.putExtra("id", arrmylist.get(position).get("ItemId"));
                    startActivity(intent);
                }
            });

            return view;
        }
    }
}
