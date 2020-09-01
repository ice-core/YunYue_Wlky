package com.example.administrator.yunyue.sc_fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Base64;
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
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc_activity.CkddActivity;
import com.example.administrator.yunyue.sc_activity.PinglunActivity;
import com.example.administrator.yunyue.sc_activity.ShouhouZtActivity;
import com.example.administrator.yunyue.sc_activity.SqshActivity;
import com.example.administrator.yunyue.sc_activity.TkcgActivity;
import com.example.administrator.yunyue.sc_activity.ZffsActivity;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DaishouhuoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DaishouhuoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DaishouhuoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GridView gv_wode_daishouhuo;
    RequestQueue queue = null;
    private SharedPreferences pref;
    private String sUser_id;
    //Fragment的View加载完毕的标记
    private boolean isViewCreated;
    //Fragment对用户可见的标记
    private boolean isUIVisible;
    private MyAdapter myAdapter;
    private ArrayList<HashMap<String, String>> arrmylist;
    private int iPage;
    private OnFragmentInteractionListener mListener;
    private PullToRefreshGridView pull_refresh_wode_daishouhuo;
    private LinearLayout ll_dd_kong;

    public DaishouhuoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuerenshouhuoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DaishouhuoFragment newInstance(String param1, String param2) {
        DaishouhuoFragment fragment = new DaishouhuoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daishouhuo, container, false);
    }

    public void onViewCreated(View view, Bundle saveInstancesState) {
        super.onViewCreated(view, saveInstancesState);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        pull_refresh_wode_daishouhuo = view.findViewById(R.id.pull_refresh_wode_daishouhuo);
        sUser_id = pref.getString("user_id", "");
        queue = Volley.newRequestQueue(getActivity());
        gv_wode_daishouhuo = view.findViewById(R.id.gv_wode_daishouhuo);
        isViewCreated = true;
        ll_dd_kong = view.findViewById(R.id.ll_dd_kong);
        myAdapter = new MyAdapter(getActivity());
        arrmylist = new ArrayList<HashMap<String, String>>();
        iPage = 1;
        lazyLoad();
        pull_refresh_wode_daishouhuo.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            // 下拉刷新加载
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                Log.e("TAG", "onPullDownToRefresh"); // Do work to
                // 刷新时间
                String label = DateUtils.formatDateTime(
                        getActivity().getApplicationContext(),
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }

    private void lazyLoad() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUIVisible) {
            //  dialogin("");
            iPage = 1;
            huoqu(iPage);
            //数据加载完毕,恢复标记,防止重复加载
   /*         isViewCreated = false;
            isUIVisible = false;*/
            //printLog(mTextviewContent+"可见,加载数据");
        } /*else if (isUIVisible) {
            huoqu();
        }*/
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

    private void huoqu(final int page) {
        String url = Api.sUrl + "Api/Order/orderlist/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/state/3" + "/page/" + page;
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
                        myAdapter = new MyAdapter(getActivity());
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

        pull_refresh_wode_daishouhuo.setAdapter(myAdapter);
        // 刷新适配器
        myAdapter.notifyDataSetChanged();
        // 关闭刷新下拉
        pull_refresh_wode_daishouhuo.onRefreshComplete();

    }

    private void gridviewdata1() {
        // myList = getMenuAdapter();
        // iPage += 1;
        // 刷新适配器
        myAdapter.notifyDataSetChanged();
        // Call onRefreshComplete when the list has been refreshed.
        // 关闭上拉加载
        pull_refresh_wode_daishouhuo.onRefreshComplete();

    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(getActivity(), R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(getActivity(), R.style.dialog, sHint, type, true).show();
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
            TextView tv_wddd_shzt = view.findViewById(R.id.tv_wddd_shzt);
            TextView tv_wddd_num = view.findViewById(R.id.tv_wddd_num);
            tv_wddd_num.setText("X" + arrmylist.get(position).get("ItemGoods_num"));
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
                tv_wddd_shzt.setVisibility(View.GONE);
                tv_wddd_bt.setVisibility(View.GONE);
                tv_wddd_bt1.setVisibility(View.VISIBLE);
                tv_wddd_bt1.setText("催促发货");
            } else if (arrmylist.get(position).get("ItemState").equals("3")) {
                tv_wddd_status.setText("等待收货");
                tv_wddd_shzt.setVisibility(View.GONE);
                tv_wddd_bt.setVisibility(View.GONE);
                tv_wddd_bt1.setVisibility(View.VISIBLE);
                tv_wddd_bt1.setText("确认收货");
            } else if (arrmylist.get(position).get("ItemState").equals("4")) {
                tv_wddd_status.setText("等待评论");
                tv_wddd_shzt.setVisibility(View.GONE);
                tv_wddd_bt.setVisibility(View.GONE);
                tv_wddd_bt1.setVisibility(View.VISIBLE);
                tv_wddd_bt.setText("联系卖家");
                tv_wddd_bt1.setText("评论商品");
            }
            Glide.with(getActivity())
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
            tv_wddd_shzt.setVisibility(View.VISIBLE);
            if (arrmylist.get(position).get("ItemShouhoustate").equals("")) {
                tv_wddd_shzt.setText("申请售后");
            } else if (arrmylist.get(position).get("ItemShouhoustate").equals("1")) {
                tv_wddd_shzt.setText("售后审核中");
            } else if (arrmylist.get(position).get("ItemShouhoustate").equals("2")) {
                tv_wddd_shzt.setText("售后审核失败");
            } else if (arrmylist.get(position).get("ItemShouhoustate").equals("3")) {
                tv_wddd_shzt.setText("售后审核通过");
            } else if (arrmylist.get(position).get("ItemShouhoustate").equals("5")) {
                tv_wddd_shzt.setText("已退款");
            }
            tv_wddd_shzt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (arrmylist.get(position).get("ItemShouhoustate").equals("")) {
                        Intent intent = new Intent(getActivity(), SqshActivity.class);
                        intent.putExtra("id", arrmylist.get(position).get("ItemId").toString());
                        intent.putExtra("state", arrmylist.get(position).get("ItemState").toString());
                        startActivity(intent);
                        //  tv_wddd_shzt.setText("申请售后");
                    } else {
                        Intent intent = new Intent(getActivity(), ShouhouZtActivity.class);
                        intent.putExtra("state", arrmylist.get(position).get("ItemShouhoustate"));
                        intent.putExtra("id", arrmylist.get(position).get("ItemId"));
                        startActivity(intent);
                    }
                }
            });

            tv_wddd_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
             /*       if (tv_wddd_bt.getText().toString().equals("取消订单")) {
                        dialogin("");
                        Delete(arrmylist.get(position).get("ItemId").toString());
                    } else if (tv_wddd_bt.getText().toString().equals("联系卖家")) {
                        dialog(arrmylist.get(position).get("ItemShangJiaphone").toString());
                    }*/
                }
            });
            tv_wddd_bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv_wddd_bt1.getText().toString().equals("立即付款")) {
                        Intent intent = new Intent(getActivity(), ZffsActivity.class);
                        intent.putExtra("activity", "wddd");
                        intent.putExtra("data", arrmylist.get(position).get("ItemId"));
                        intent.putExtra("money", arrmylist.get(position).get("ItemNew_price"));
                        startActivity(intent);
                    } else if (tv_wddd_bt1.getText().toString().equals("催促发货")) {
                        hideDialogin();
                        dialogin("");
                        // Cuicu(arrmylist.get(position).get("ItemOrder_Id"), arrmylist.get(position).get("ItemRid"));
                    } else if (tv_wddd_bt1.getText().toString().equals("确认收货")) {
                        hideDialogin();
                        dialogin("");
                        queren(arrmylist.get(position).get("ItemId"));
                    } else if (tv_wddd_bt1.getText().toString().equals("评论商品")) {
                        Intent intent = new Intent(getActivity(), PinglunActivity.class);
                        intent.putExtra("id", arrmylist.get(position).get("ItemId").toString());
                        //intent.putExtra("good_id", arrmylist.get(position).get("ItemGoods_Id").toString());
                        //intent.putExtra("rec_id", arrmylist.get(position).get("ItemRec_id").toString());
                        startActivity(intent);
                        //  }
                    } else if (tv_wddd_bt1.getText().toString().equals("查看详情")) {
                        if (arrmylist.get(position).get("ItemIsSh").equals("4")) {
                            Intent intent = new Intent(getActivity(), TkcgActivity.class);
                            intent.putExtra("id", arrmylist.get(position).get("ItemOrder_Id"));
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getActivity(), ShouhouZtActivity.class);
                            intent.putExtra("state", arrmylist.get(position).get("ItemIsSh"));
                            intent.putExtra("id", arrmylist.get(position).get("ItemOrder_Id"));
                            //intent.putExtra("shouhuotype", arrmylist.get(position).get("ItemShouhuotype"));
                            startActivity(intent);
                        }
                    }

                }
            });
            ll_wddd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), CkddActivity.class);
                    intent.putExtra("activity", tv_wddd_status.getText().toString());
                    intent.putExtra("id", arrmylist.get(position).get("ItemId"));
                    startActivity(intent);
                }
            });


            return view;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
