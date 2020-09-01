package com.example.administrator.yunyue.sc_fragment;

import android.app.Dialog;
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
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc_activity.CkddActivity;
import com.example.administrator.yunyue.sc_activity.ShouhouZtActivity;
import com.example.administrator.yunyue.sc_activity.SqshActivity;
import com.example.administrator.yunyue.sc_activity.TkcgActivity;
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
 * {@link ShouhouFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShouhouFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShouhouFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Fragment的View加载完毕的标记
    private boolean isViewCreated;
    //Fragment对用户可见的标记
    private boolean isUIVisible;
    private SharedPreferences pref;
    private String sUser_id;
    RequestQueue queue = null;
    private GridView gv_wode_shouhou;

    private OnFragmentInteractionListener mListener;

    private MyAdapter myAdapter;
    private ArrayList<HashMap<String, String>> arrmylist;
    private int iPage;
    private PullToRefreshGridView pull_refresh_wode_shouhou;

    public ShouhouFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShouhouFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShouhouFragment newInstance(String param1, String param2) {
        ShouhouFragment fragment = new ShouhouFragment();
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
        return inflater.inflate(R.layout.fragment_shouhou, container, false);
    }

    public void onViewCreated(View view, Bundle saveInstancesState) {
        super.onViewCreated(view, saveInstancesState);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        sUser_id = pref.getString("user_id", "");
        queue = Volley.newRequestQueue(getActivity());
        gv_wode_shouhou = view.findViewById(R.id.gv_wode_shouhou);
        pull_refresh_wode_shouhou = view.findViewById(R.id.pull_refresh_wode_shouhou);
        isViewCreated = true;
        myAdapter = new MyAdapter(getActivity());
        arrmylist = new ArrayList<HashMap<String, String>>();
        iPage = 1;
        lazyLoad();
        pull_refresh_wode_shouhou.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
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
            // dialogin("");
            huoqu(iPage);
            //数据加载完毕,恢复标记,防止重复加载
       /*     isViewCreated = false;
            isUIVisible = false;*/
            //printLog(mTextviewContent+"可见,加载数据");
        }/* else if (isUIVisible) {
            huoqu();
        }*/
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

    private void huoqu(final int page) {
        String url = Api.sUrl + "Order/orderList/user_id/" + sUser_id + "/pay_status/4" + "/page/" + page;
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

                        ArrayList<HashMap<String, String>> ListData = new ArrayList<HashMap<String, String>>();
                        ArrayList<HashMap<String, String>> ListGoodlist = new ArrayList<HashMap<String, String>>();
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("ItemShipment_Status", jsonObject.getString("shipment_status"));//id
                            ListData.add(map);
                            if (jsonObject.getString("shipment_status").equals("4")) {
                                JSONArray resultJsonArrayGoodlist = jsonObject.getJSONArray("goodlist");
                                for (int a = 0; a < resultJsonArrayGoodlist.length(); a++) {
                                    JSONObject jsonObjectGood = resultJsonArrayGoodlist.getJSONObject(a);
                                    HashMap<String, String> mapgood = new HashMap<String, String>();
                                    mapgood.put("ItemStorename", jsonObjectGood.getString("storename"));
                                    mapgood.put("ItemShipment_Status", jsonObject.getString("shipment_status"));
                                    mapgood.put("ItemOriginal_Img", jsonObjectGood.getString("original_img"));
                                    mapgood.put("ItemGoods_Name", jsonObjectGood.getString("goods_name"));
                                    mapgood.put("ItemSpec_Key_Name", jsonObjectGood.getString("spec_key_name"));
                                    mapgood.put("ItemGoods_Num", jsonObjectGood.getString("goods_num"));
                                    mapgood.put("ItemOrder_Amount", jsonObjectGood.getString("order_amount"));
                                    mapgood.put("ItemGoods_Num_And", jsonObject.getString("goods_num_and"));
                                    mapgood.put("ItemOrder_Sn", jsonObjectGood.getString("order_sn"));
                                    mapgood.put("ItemOrder_Id", jsonObjectGood.getString("order_id"));
                                    mapgood.put("ItemRec_id", jsonObjectGood.getString("rec_id"));
                                    mapgood.put("ItemRid", jsonObject.getString("rid"));
                                    mapgood.put("ItemIsSh", jsonObject.getString("is_sh"));
                                    mapgood.put("ItemShouhuotype", jsonObject.getString("shouhuotype"));
                                    mapgood.put("ItemShangJiaphone", jsonObjectGood.getString("shangjiaphone"));
                                    arrmylist.add(mapgood);
                                }
                            }
                        }
                        if (page == 1) {
                            gridviewdata();
                        } else {
                            gridviewdata1();
                        }
                        iPage += 1;
                        // myAdapter.arrmylist = ListGoodlist;
                        //   gv_wode_shouhou.setAdapter(myAdapter);
                        // setGridView();
                        //  tv_camera_guanggao.setText(resultAdName);
                    } else {
                        if (page == 1) {
                            gridviewdata();
                        } else {
                            gridviewdata1();
                        }
                        //  gv_wode_shouhou.setAdapter(myAdapter);
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
        queue.add(request);
    }

    private void gridviewdata() {
        //iPage += 1;

        pull_refresh_wode_shouhou.setAdapter(myAdapter);
        // 刷新适配器
        myAdapter.notifyDataSetChanged();
        // 关闭刷新下拉
        pull_refresh_wode_shouhou.onRefreshComplete();

    }

    private void gridviewdata1() {
        // myList = getMenuAdapter();
        // iPage += 1;
        // 刷新适配器
        myAdapter.notifyDataSetChanged();
        // Call onRefreshComplete when the list has been refreshed.
        // 关闭上拉加载
        pull_refresh_wode_shouhou.onRefreshComplete();

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
        //  public ArrayList<HashMap<String, String>> arrmylist;

        //     public ArrayList<String> arr;


        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            // arrmylist = new ArrayList<HashMap<String, String>>();

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
            TextView tv_wddd_shzt = view.findViewById(R.id.tv_wddd_shzt);
            TextView tv_wddd_bt = view.findViewById(R.id.tv_wddd_bt);
            TextView tv_wddd_bt1 = view.findViewById(R.id.tv_wddd_bt1);
            LinearLayout ll_wddd = view.findViewById(R.id.ll_wddd);
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
                if (arrmylist.get(position).get("ItemOrder_Sn").equals(arrmylist.get(position + 1).get("ItemOrder_Sn"))) {
                    if (position == 0) {
                        tv_wddd_tc.setVisibility(View.VISIBLE);
                        ll_wddd_tou.setVisibility(View.VISIBLE);
                        ll_wddd_wei1.setVisibility(View.GONE);
                        ll_wddd_wei.setVisibility(View.GONE);
                    } else if (arrmylist.get(position).get("ItemOrder_Sn").equals(arrmylist.get(position - 1).get("ItemOrder_Sn"))) {
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
                    } else if (arrmylist.get(position - 1).get("ItemOrder_Sn").equals(arrmylist.get(position).get("ItemOrder_Sn"))) {
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
                    if (arrmylist.get(position - 1).get("ItemOrder_Sn").equals(arrmylist.get(position).get("ItemOrder_Sn"))) {
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


            tv_wddd_storename.setText(arrmylist.get(position).get("ItemStorename"));
            if (arrmylist.get(position).get("ItemShipment_Status").equals("0")) {
                tv_wddd_status.setText("等待买家付款");
                tv_wddd_shzt.setVisibility(View.GONE);
                tv_wddd_bt.setVisibility(View.VISIBLE);
                tv_wddd_bt1.setVisibility(View.VISIBLE);
                tv_wddd_bt.setText("取消订单");
                tv_wddd_bt1.setText("立即付款");
            } else if (arrmylist.get(position).get("ItemShipment_Status").equals("1")) {
                tv_wddd_status.setText("等待卖家发货");
                tv_wddd_shzt.setVisibility(View.GONE);
                tv_wddd_bt.setVisibility(View.GONE);
                tv_wddd_bt1.setVisibility(View.VISIBLE);
                tv_wddd_bt1.setText("催促发货");
            } else if (arrmylist.get(position).get("ItemShipment_Status").equals("2")) {
                tv_wddd_status.setText("等到收货");
                tv_wddd_shzt.setVisibility(View.GONE);
                tv_wddd_bt.setVisibility(View.GONE);
                tv_wddd_bt1.setVisibility(View.VISIBLE);
                tv_wddd_bt1.setText("确认收货");
            } else if (arrmylist.get(position).get("ItemShipment_Status").equals("3")) {
                tv_wddd_status.setText("交易成功");
                tv_wddd_shzt.setVisibility(View.GONE);
                tv_wddd_bt.setVisibility(View.VISIBLE);
                tv_wddd_bt1.setVisibility(View.VISIBLE);
                tv_wddd_bt.setText("联系卖家");
                tv_wddd_bt1.setText("评论商品");
            } else if (arrmylist.get(position).get("ItemShipment_Status").equals("4")) {
                tv_wddd_status.setText("售后");
                tv_wddd_shzt.setVisibility(View.VISIBLE);
                tv_wddd_bt.setVisibility(View.VISIBLE);
                tv_wddd_bt1.setVisibility(View.VISIBLE);
                tv_wddd_bt.setText("联系卖家");
                tv_wddd_bt1.setText("查看详情");
                if (arrmylist.get(position).get("ItemIsSh").equals("1")) {
                    tv_wddd_shzt.setText("审核中");
                } else if (arrmylist.get(position).get("ItemIsSh").equals("2")) {
                    tv_wddd_shzt.setText("审核失败");
                } else if (arrmylist.get(position).get("ItemIsSh").equals("3")) {
                    tv_wddd_shzt.setText("审核通过");
                } else if (arrmylist.get(position).get("ItemIsSh").equals("4")) {
                    tv_wddd_shzt.setText("退款成功");
                }
            }
            tv_wddd_shzt.setVisibility(View.VISIBLE);
            tv_wddd_shzt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), SqshActivity.class);
                    intent.putExtra("id", arrmylist.get(position).get("ItemId").toString());
                    intent.putExtra("state", arrmylist.get(position).get("ItemState").toString());
                    startActivity(intent);
                }
            });
            Glide.with(getActivity())
                    .load( Api.sUrl+ arrmylist.get(position).get("ItemOriginal_Img"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_wddd);
            tv_wddd_goods_name.setText(arrmylist.get(position).get("ItemGoods_Name"));
            tv_wddd_spec_key_name.setText(arrmylist.get(position).get("ItemSpec_Key_Name"));
            tv_wddd_goods_num.setText("共" + arrmylist.get(position).get("ItemGoods_Num_And") + "件商品");
            tv_wddd_order_amount.setText("￥" + arrmylist.get(position).get("ItemOrder_Amount"));
            tv_wddd_bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (arrmylist.get(position).get("ItemIsSh").equals("4")) {
                        Intent intent = new Intent(getActivity(), TkcgActivity.class);
                        intent.putExtra("id", arrmylist.get(position).get("ItemOrder_Id"));
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), ShouhouZtActivity.class);
                        intent.putExtra("state", arrmylist.get(position).get("ItemIsSh"));
                        intent.putExtra("id", arrmylist.get(position).get("ItemOrder_Id"));
                        intent.putExtra("shouhuotype", arrmylist.get(position).get("ItemShouhuotype"));
                        startActivity(intent);
                    }
                }
            });
            tv_wddd_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog(arrmylist.get(position).get("ItemShangJiaphone").toString());
                }
            });
            ll_wddd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), CkddActivity.class);
                    intent.putExtra("activity", tv_wddd_status.getText().toString());
                    intent.putExtra("id", arrmylist.get(position).get("ItemOrder_Id"));
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
