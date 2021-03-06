package com.example.administrator.yunyue.fjsc_activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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
import com.bumptech.glide.Glide;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
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

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fjsc_WdddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fjsc_WdddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fjsc_WdddFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RequestQueue queue = null;
    private SharedPreferences pref;
    private String sUser_id;
    private GridView gv_wode_quanbu;
    //Fragment的View加载完毕的标记
    private boolean isViewCreated;
    //Fragment对用户可见的标记
    private boolean isUIVisible;
    private PullToRefreshGridView pull_refresh_gv_fjsc_wddd;
    private int iPage;
    private MyAdapter myAdapter;
    private ArrayList<HashMap<String, String>> arrmylist;
    private LinearLayout ll_dd_kong;


    private OnFragmentInteractionListener mListener;

    public Fjsc_WdddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fjsc_WdddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Fjsc_WdddFragment newInstance(String param1, String param2) {
        Fjsc_WdddFragment fragment = new Fjsc_WdddFragment();
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
        return inflater.inflate(R.layout.fragment_fjsc__wddd, container, false);
    }


    public void onViewCreated(View view, Bundle saveInstancesState) {
        super.onViewCreated(view, saveInstancesState);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        iPage = 1;
        sUser_id = pref.getString("user_id", "");
        queue = Volley.newRequestQueue(getActivity());
        pull_refresh_gv_fjsc_wddd = view.findViewById(R.id.pull_refresh_gv_fjsc_wddd);
        pull_refresh_gv_fjsc_wddd.setMode(PullToRefreshBase.Mode.BOTH);
        gv_wode_quanbu = view.findViewById(R.id.gv_wode_quanbu);
        ll_dd_kong = view.findViewById(R.id.ll_dd_kong);
        myAdapter = new MyAdapter(getActivity());
        arrmylist = new ArrayList<HashMap<String, String>>();
        isViewCreated = true;
        lazyLoad();
        pull_refresh_gv_fjsc_wddd.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
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
                sOrderlist();
            }

            // 上拉加载跟多
            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                Log.e("TAG", "onPullUpToRefresh"); // Do work to refresh
                sOrderlist();
                // 页数
                //   p = p + 1;
                // AsyncTask异步交互加载数据
                // new GetDataTask2().execute(URL + p);
            }
        });
/*        if (Fjsc_WdddActivity.iPositionId == 0) {
            iPage = 1;
            sOrderlist();
        }*/
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
            // dialogin("");
            iPage = 1;
            sOrderlist();

        }
    }


    /**
     * 方法名：sOrderdel()
     * 功  能：删除订单
     * 参  数：appId
     * order_id--订单id
     */
    private void sOrderdel(String sOrder_id) {
        String url = Api.sUrl + Api.sOrderdel;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("order_id", sOrder_id);
        JSONObject jsonObject = new JSONObject(params);
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialogin();
                        String sDate = response.toString();
                        System.out.println(sDate);
                        try {
                            JSONObject jsonObject = new JSONObject(sDate);
                            String resultMsg = jsonObject.getString("msg");
                            int resultCode = jsonObject.getInt("code");
                            if (resultCode > 0) {
                                Hint(resultMsg, HintDialog.SUCCESS);
                                iPage -= 1;
                                sOrderlist();
                            } else {

                                Hint(resultMsg, HintDialog.ERROR);
                                //   Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //  Log.d(TAG, "response -> " + response.toString());
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
        request.setShouldCache(false);
        queue.add(request);
    }


    /**
     * 方法名：sOrderlist()
     * 功  能：订单列表接口
     * 参  数：appId
     */
    private void sOrderlist() {
        String url = Api.sUrl + Api.sOrderlist;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("page", String.valueOf(iPage));
        params.put("state", String.valueOf(Fjsc_WdddActivity.iPositionId));

        JSONObject jsonObject = new JSONObject(params);
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        hideDialogin();
                        String sDate = response.toString();
                        System.out.println(sDate);
                        try {
                            JSONObject jsonObject1 = new JSONObject(sDate);
                            String resultMsg = jsonObject1.getString("msg");
                            int resultCode = jsonObject1.getInt("code");
                            if (iPage == 1) {
                                myAdapter = new MyAdapter(getActivity());
                                arrmylist = new ArrayList<HashMap<String, String>>();
                            }
                            if (resultCode > 0) {
                                JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                                for (int i = 0; i < resultJsonArray.length(); i++) {
                                    JSONObject jsonObject = resultJsonArray.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    //订单状态
                                    //0 全部 1待付款 2已付款待发货 3已发货待收货 4已收货待评价
                                    map.put("ItemState", jsonObject.getString("state"));//id

                                    JSONArray resultJsonArrayGoodlist = jsonObject.getJSONArray("list");
                                    int Good_num = 0;
                                    for (int a = 0; a < resultJsonArrayGoodlist.length(); a++) {
                                        JSONObject jsonObjectGood = resultJsonArrayGoodlist.getJSONObject(a);
                                        HashMap<String, String> mapgood = new HashMap<String, String>();
                                        //店铺logo
                                        mapgood.put("ItemLogo", jsonObject.getString("logo"));
                                        //店铺名称
                                        mapgood.put("ItemShangjia_name", jsonObject.getString("shangjia_name"));
                                        //订单状态
                                        //0 全部 1待付款 2已付款待发货 3已发货待收货 4已收货待评价
                                        mapgood.put("ItemState", jsonObject.getString("state"));
                                        //是否申请售后 “”为未申请 1审核中 2审核失败 3审核通过 5已退款
                                        mapgood.put("ItemShouhoustate", jsonObject.getString("shouhoustate"));
                                        //商品ID
                                        mapgood.put("ItemGood_id", jsonObjectGood.getString("goods_id"));
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
                                if (iPage == 1) {
                                    if (arrmylist.size() == 0) {
                                        ll_dd_kong.setVisibility(View.VISIBLE);
                                    } else {
                                        ll_dd_kong.setVisibility(View.GONE);
                                    }
                                    gridviewdata();
                                } else {
                                    gridviewdata1();
                                }
                                if (arrmylist.size() == 0) {
                                    iPage -= 1;
                                } else {
                                    iPage += 1;
                                }
                            } else {
                                if (iPage == 1) {
                                    if (arrmylist.size() == 0) {
                                        ll_dd_kong.setVisibility(View.VISIBLE);
                                    } else {
                                        ll_dd_kong.setVisibility(View.GONE);
                                    }
                                    gridviewdata();
                                } else {

                                    gridviewdata1();
                                }
                                iPage -= 1;
                                // gv_wode_quanbu.setAdapter(myAdapter);
                            }
                            myAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            hideDialogin();
                            e.printStackTrace();
                        }            //   Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
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

    private void error(VolleyError error) {
        //    Log.e(TAG, error.getMessage(), error);
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

    private void gridviewdata() {
        //iPage += 1;

        pull_refresh_gv_fjsc_wddd.setAdapter(myAdapter);
        // 刷新适配器
        myAdapter.notifyDataSetChanged();
        // 关闭刷新下拉
        pull_refresh_gv_fjsc_wddd.onRefreshComplete();

    }

    private void gridviewdata1() {
        // myList = getMenuAdapter();
        // iPage += 1;
        // 刷新适配器
        myAdapter.notifyDataSetChanged();
        // Call onRefreshComplete when the list has been refreshed.
        // 关闭上拉加载
        pull_refresh_gv_fjsc_wddd.onRefreshComplete();

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
        try {
            new HintDialog(getActivity(), R.style.dialog, sHint, type, true).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 方法名：sShouhuo()
     * 功  能：用户收货接口
     * 参  数：appId
     * order_id--订单id
     */
    private void sShouhuo(String sOrder_id) {
        String url = Api.sUrl + Api.sShouhuo;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("order_id", sOrder_id);
        JSONObject jsonObject = new JSONObject(params);
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialogin();
                        String sDate = response.toString();
                        System.out.println(sDate);
                        try {
                            JSONObject jsonObject = new JSONObject(sDate);
                            String resultMsg = jsonObject.getString("msg");
                            int resultCode = jsonObject.getInt("code");
                            if (resultCode > 0) {
                                Hint(resultMsg, HintDialog.SUCCESS);
                                iPage -= 1;
                                sOrderlist();
                            } else {

                                Hint(resultMsg, HintDialog.ERROR);
                                //   Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //  Log.d(TAG, "response -> " + response.toString());
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

    public void dialog(final String phone) {
        Dialog dialog = new Dialog(getActivity(), R.style.ActionSheetDialogStyle);
        final View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.lxmj_dialog, null);
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
        //public ArrayList<String> arr;

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
            if (arrmylist.get(position).get("ItemShouhoustate").equals("0")) {
                tv_wddd_shzt.setText("申请售后");
            } else if (arrmylist.get(position).get("ItemShouhoustate").equals("1")) {
                tv_wddd_shzt.setText("售后审核中");
            } else if (arrmylist.get(position).get("ItemShouhoustate").equals("2")) {
                tv_wddd_shzt.setText("售后审核失败");
            } else if (arrmylist.get(position).get("ItemShouhoustate").equals("3")) {
                tv_wddd_shzt.setText("售后通过");
            } else if (arrmylist.get(position).get("ItemShouhoustate").equals("4")) {
                tv_wddd_shzt.setText("售后通过");
            } else if (arrmylist.get(position).get("ItemShouhoustate").equals("5")) {
                tv_wddd_shzt.setText("已退款");
            }

            tv_wddd_shzt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (arrmylist.get(position).get("ItemShouhoustate").equals("0")) {
                        Intent intent = new Intent(getActivity(), Fjsc_SqtkActivity.class);
                        intent.putExtra("id", arrmylist.get(position).get("ItemId").toString());
                        intent.putExtra("state", arrmylist.get(position).get("ItemState").toString());
                        startActivity(intent);
                        //  tv_wddd_shzt.setText("申请售后");
                    } else if (arrmylist.get(position).get("ItemShouhoustate").equals("5")) {
                        Intent intent = new Intent(getActivity(), TkcgActivity.class);
                        intent.putExtra("id", arrmylist.get(position).get("ItemOrder_Id"));
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), Fjsc_DdxqActivity.class);
                        //intent.putExtra("activity", tv_wddd_status.getText().toString());
                        intent.putExtra("order_id", arrmylist.get(position).get("ItemId"));
                        startActivity(intent);
                    }
                }
            });
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
            tv_wddd_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv_wddd_bt.getText().toString().equals("取消订单")) {
                        hideDialogin();
                        dialogin("");
                        sOrderdel(arrmylist.get(position).get("ItemId").toString());

                    } else if (tv_wddd_bt.getText().toString().equals("联系卖家")) {
                        dialog(arrmylist.get(position).get("ItemShangJiaphone").toString());
                    }
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
                    } else if (tv_wddd_bt1.getText().toString().equals("查看详情")) {
                        if (arrmylist.get(position).get("ItemShouhoustate").equals("5")) {
                            Intent intent = new Intent(getActivity(), TkcgActivity.class);
                            intent.putExtra("id", arrmylist.get(position).get("ItemOrder_Id"));
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getActivity(), Fjsc_DdxqActivity.class);
                            //intent.putExtra("activity", tv_wddd_status.getText().toString());
                            intent.putExtra("order_id", arrmylist.get(position).get("ItemId"));
                            startActivity(intent);
                        }// Cuicu(arrmylist.get(position).get("ItemOrder_Id"), arrmylist.get(position).get("ItemRid"));
                    } else if (tv_wddd_bt1.getText().toString().equals("确认收货")) {
                        hideDialogin();
                        dialogin("");
                        sShouhuo(arrmylist.get(position).get("ItemId"));

                    } else if (tv_wddd_bt1.getText().toString().equals("评论商品")) {
                        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                        Intent intent = new Intent(getActivity(), Fjsc_DppjActivity.class);
                        Fjsc_DppjActivity.sOrder_id = arrmylist.get(position).get("ItemId").toString();
                        Fjsc_DppjActivity.sLogo = arrmylist.get(position).get("ItemLogo").toString();
                        Fjsc_DppjActivity.sShangjia_id = arrmylist.get(position).get("ItemShangjia_id").toString();
                        Fjsc_DppjActivity.sShangjia_name = arrmylist.get(position).get("ItemShangjia_name").toString();
                        startActivity(intent);
                        //  }
                    }

                }
            });
            ll_wddd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), Fjsc_DdxqActivity.class);
                    //  intent.putExtra("activity", tv_wddd_status.getText().toString());
                    intent.putExtra("order_id", arrmylist.get(position).get("ItemId"));
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

