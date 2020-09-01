package com.example.administrator.yunyue.sc_fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc_activity.FenleiActivity;
import com.example.administrator.yunyue.sc_activity.SplbActivity;
import com.example.administrator.yunyue.sc_fenlei.ScrollBean;
import com.example.administrator.yunyue.sc_fenlei.ScrollRightAdapter;
import com.example.administrator.yunyue.sc_gridview.GridViewAdaptersc;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FenleiFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FenleiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FenleiFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    //Fragment的View加载完毕的标记
    private boolean isViewCreated;
    //Fragment对用户可见的标记
    private boolean isUIVisible;
    private static final String TAG = FenleiFragment.class.getSimpleName();
    private ListView lv_sc_lb;
    /**
     * 推荐
     */
    private MyGridView mgv_fenlei_tj;
    /**
     * 热门推荐
     */
    private MyGridView mgv_fenlei_rmtj;
    /**
     * 猜你喜欢
     */
    private MyGridView mgv_fenlei_cnxh;
    private GridViewAdaptersc mAdapter;

    RequestQueue queue = null;
    String sListData;
    /**
     * 猜你喜欢
     */
    ArrayList<HashMap<String, String>> mylistxihuan;
    /**
     * 推荐
     */
    ArrayList<HashMap<String, String>> mylisttuijian;
    /**
     * 热门排行
     */
    ArrayList<HashMap<String, String>> mylistpaihang;

    /**
     * 推荐
     */
    private LinearLayout ll_fenlei_tj;

    /**
     * 热门排行
     */
    private LinearLayout ll_fenlei_rmtj;

    /**
     * 猜你喜欢
     */
    private LinearLayout ll_fenlei_cnxh;
    /**
     * 分类
     */
    private GridView gv_fl_zuo;
    /**
     * 一级分类ID
     */
    String ItemId;
    /**
     * 一级分类
     * 默认选中第一项
     */
    int positionItme = 0;

    private RecyclerView recRight;
    private GridLayoutManager rightManager;
    private Context mContext;
    private ScrollRightAdapter rightAdapter;
    //记录右侧当前可见的第一个item的position
    private int first = 0;
    private List<String> left;
    private List<ScrollBean> right;


    public FenleiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FenleiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FenleiFragment newInstance(String param1, String param2) {
        FenleiFragment fragment = new FenleiFragment();
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
        return inflater.inflate(R.layout.fragment_fenlei, container, false);
    }


    public void onViewCreated(View view, Bundle saveInstancesState) {
        super.onViewCreated(view, saveInstancesState);
        mContext = getActivity();
        mylistxihuan = new ArrayList<HashMap<String, String>>();
        mylisttuijian = new ArrayList<HashMap<String, String>>();
        mylistpaihang = new ArrayList<HashMap<String, String>>();
        queue = Volley.newRequestQueue(getActivity());

        lv_sc_lb = view.findViewById(R.id.lv_sc_lb);
        mgv_fenlei_tj = view.findViewById(R.id.mgv_fenlei_tj);
        mgv_fenlei_cnxh = view.findViewById(R.id.mgv_fenlei_cnxh);
        mgv_fenlei_rmtj = view.findViewById(R.id.mgv_fenlei_rmtj);
        ll_fenlei_tj = view.findViewById(R.id.ll_fenlei_tj);
        ll_fenlei_rmtj = view.findViewById(R.id.ll_fenlei_rmtj);
        ll_fenlei_cnxh = view.findViewById(R.id.ll_fenlei_cnxh);
        gv_fl_zuo = view.findViewById(R.id.gv_fl_zuo);
        recRight = view.findViewById(R.id.rec_right);

  /*      lv_sc_lb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> map = (HashMap<String, String>) lv_sc_lb.getItemAtPosition(i);
                ItemId = map.get("ItemId");
                erjileibian(ItemId);
            }
        });*/
        hideDialogin();
        dialogin("");
        // qingqiu();
        /**
         * 一级分类
         * */
        yjfl();
        initRight();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
            hideDialogin();
       /*     dialogin("");
            qingqiu();*/
            yjfl();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;

            //    printLog(mTextviewContent+"可见,加载数据");
        }
    }

    private void initRight() {

        rightManager = new GridLayoutManager(mContext, 3);

        if (rightAdapter == null) {
            rightAdapter = new ScrollRightAdapter(R.layout.scroll_right, R.layout.layout_right_title, null);
            recRight.setLayoutManager(rightManager);
            recRight.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    outRect.set(dpToPx(mContext, getDimens(mContext, R.dimen.dp3))
                            , 0
                            , dpToPx(mContext, getDimens(mContext, R.dimen.dp3))
                            , dpToPx(mContext, getDimens(mContext, R.dimen.dp3)));
                }
            });
            recRight.setAdapter(rightAdapter);
        } else {
            rightAdapter.notifyDataSetChanged();
        }
        //  rightAdapter.setNewData(right);


        rightAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    //点击左侧列表的相应item,右侧列表相应的title置顶显示
                    //(最后一组内容若不能填充右侧整个可见页面,则显示到右侧列表的最底端)
                    case R.id.ll_you:
                        //   HashMap<String, String> map = (HashMap<String, String>) lv_sc_lb.getItemAtPosition(position);
                 /*       Intent intent = new Intent(FenleiActivity.this, FwzxActivity.class);
                        startActivity(intent);*/
                        ScrollBean s = right.get(position);
                        ScrollBean.ScrollItemBean t = s.t;

                        Intent intent = new Intent(getActivity(), SplbActivity.class);
                        intent.putExtra("id", ItemId);
                        intent.putExtra("er_id", "");
                        intent.putExtra("miaoshu", t.getText());
                        intent.putExtra("san_id", t.getId());
                        startActivity(intent);
                        break;
                }
            }
        });

    }


    /**
     * 获得资源 dimens (dp)
     *
     * @param context
     * @param id      资源id
     * @return
     */
    public float getDimens(Context context, int id) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float px = context.getResources().getDimension(id);
        return px / dm.density;
    }

    /**
     * dp转px
     *
     * @param context
     * @param dp
     * @return
     */
    public int dpToPx(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5f);
    }

    private void yjfl() {
        String url = Api.sUrl + "Api/Good/typelist/appId/" + Api.sApp_Id;

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
                    if (resultCode > 0) {
                        ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String, String>>();
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("ItemId", jsonObject.getString("id"));//id
                            map.put("ItemName", jsonObject.getString("name"));//
                            map.put("ItemLogo", jsonObject.getString("logo"));//
                            myList.add(map);
                            if (i == 0) {
                                hideDialogin();
                                dialogin("");
                                ejfl(jsonObject.getString("id"));
                            }
                        }
                        data(myList);

                        // setGridView();
                        //  tv_camera_guanggao.setText(resultAdName);
                    } else {

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


    private void ejfl(String fast_id) {
        left = new ArrayList<>();
        right = new ArrayList<>();
        String url = Api.sUrl + "Api/Good/typelisttwo/appId/" + Api.sApp_Id
                + "/fast_id/" + fast_id;
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
                    left = new ArrayList<>();
                    right = new ArrayList<>();
                    if (resultCode > 0) {
                        ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String, String>>();
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            JSONObject jsonObjectdate = resultJsonArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("ItemId", jsonObjectdate.getString("name"));//id
                            map.put("ItemName", jsonObjectdate.getString("name"));//
                            map.put("ItemLogo", jsonObjectdate.getString("logo"));//
                            right.add(new ScrollBean(true, jsonObjectdate.getString("name")));
                            JSONArray resultJsonArray_type = jsonObjectdate.getJSONArray("type");
                            for (int a = 0; a < resultJsonArray_type.length(); a++) {
                                JSONObject jsonObjecttype = resultJsonArray_type.getJSONObject(a);
                                String name = jsonObjecttype.getString("name");
                                right.add(new ScrollBean(new ScrollBean.ScrollItemBean(jsonObjecttype.getString("id"),
                                        jsonObjecttype.getString("name"), jsonObjecttype.getString("logo"), jsonObjectdate.getString("name"))));
                            }
                        }
                    } else {
                    }
                    rightAdapter.setNewData(right);
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


    private void erjileibian(String sId) {
        /**
         * 猜你喜欢
         */
        ArrayList<HashMap<String, String>> mylistxihuan_data = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < mylistxihuan.size(); i++) {
            if (mylistxihuan.get(i).get("ItemLbId").equals(sId)) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ItemId", mylistxihuan.get(i).get("ItemId"));
                map.put("ItemName", mylistxihuan.get(i).get("ItemName"));
                map.put("ItemImage", mylistxihuan.get(i).get("ItemImage"));
                mylistxihuan_data.add(map);
            }
        }
        if (mylistxihuan_data.size() == 0) {
            ll_fenlei_cnxh.setVisibility(View.GONE);
        } else {
            ll_fenlei_cnxh.setVisibility(View.VISIBLE);
        }

        setGridViewCnxh(mylistxihuan_data);

    }

    private void qingqiu() {
        String url = Api.sUrl + "Api/Good/typelist/appId/" + Api.sApp_Id;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                sListData = sDate;
                System.out.println(sDate);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        ArrayList<HashMap<String, String>> mylistZuo = new ArrayList<HashMap<String, String>>();
                        JSONArray resultJsonArrayData = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < resultJsonArrayData.length(); i++) {
                            jsonObject = resultJsonArrayData.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String name = jsonObject.getString("name");
                            HashMap<String, String> map1 = new HashMap<String, String>();
                            map1.put("ItemId", id);
                            map1.put("ItemText", name);
                            mylistZuo.add(map1);

                   /*         String resultType = jsonObject.getString("type");
                            JSONObject jsonObject_resultType = new JSONObject(resultType);*/
                            JSONArray resultJsonArrayType = jsonObject.getJSONArray("type");
                            /**
                             * 猜你喜欢
                             * */
                            for (int a = 0; a < resultJsonArrayType.length(); a++) {
                                jsonObject = resultJsonArrayType.getJSONObject(a);
                                HashMap<String, String> maptmenu = new HashMap<String, String>();
                                maptmenu.put("ItemLbId", id);
                                maptmenu.put("ItemId", jsonObject.getString("id"));
                                maptmenu.put("ItemName", jsonObject.getString("name"));
                                maptmenu.put("ItemImage", jsonObject.getString("logo"));
                                mylistxihuan.add(maptmenu);
                            }
                        }
                        data(mylistZuo);
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



/*    private void data(final ArrayList<HashMap<String, String>> mylist) {
        SimpleAdapter mSchedule = new SimpleAdapter(this, //没什么解释
                mylist,//数据来源
                R.layout.sc_z_lb_item,//ListItem的XML实现
                new String[]{"ItemText"},
                new int[]{R.id.tv_sc_z_lb});
        //添加并且显示
        lv_sc_lb.setAdapter(mSchedule);
    }*/

    /**
     * 猜你喜欢
     * 设置GirdView参数，绑定数据
     */
    private void data(final ArrayList<HashMap<String, String>> mylist) {
        MyAdapter myAdapter = new MyAdapter(getActivity());
        myAdapter.arrlist = mylist;
        lv_sc_lb.setAdapter(myAdapter);
    }


    /**
     * 猜你喜欢
     * 设置GirdView参数，绑定数据
     */
    private void setGridViewCnxh(final ArrayList<HashMap<String, String>> mylist) {
        MyAdapterCnxh myAdapterCnxh = new MyAdapterCnxh(getActivity());
        myAdapterCnxh.arrlist = mylist;
        gv_fl_zuo.setAdapter(myAdapterCnxh);
    }

    /**
     * 一级分类
     */
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
                view = inflater.inflate(R.layout.sc_z_lb_item, null);
            }
            TextView tv_sc_z_lb = view.findViewById(R.id.tv_sc_z_lb);
            tv_sc_z_lb.setText(arrlist.get(position).get("ItemName"));
            tv_sc_z_lb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    positionItme = position;
                    notifyDataSetChanged();
                    hideDialogin();
                    dialogin("");
                    ejfl(arrlist.get(position).get("ItemId"));
                }
            });
            if (positionItme == position) {
                tv_sc_z_lb.setTextColor(tv_sc_z_lb.getResources().getColor(R.color.theme));
                ItemId = arrlist.get(position).get("ItemId");

            } else {
                tv_sc_z_lb.setTextColor(tv_sc_z_lb.getResources().getColor(R.color.black));
            }
/*            map.put("ItemId", jsonObject.getString("id"));//id
            map.put("ItemName", jsonObject.getString("name"));//*/
            return view;
        }
    }


    /**
     * 猜你喜欢
     */
    private class MyAdapterCnxh extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        ArrayList<HashMap<String, String>> arrlist;

        public MyAdapterCnxh(Context context) {
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
                view = inflater.inflate(R.layout.sc_y_lb_item, null);
            }
            LinearLayout ll_sc_y_lb_item = view.findViewById(R.id.ll_sc_y_lb_item);
            ll_sc_y_lb_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), SplbActivity.class);
                    intent.putExtra("id", ItemId);
                    intent.putExtra("er_id", arrlist.get(position).get("ItemId"));
                    intent.putExtra("miaoshu", arrlist.get(position).get("ItemName"));
                    startActivity(intent);
                }
            });
            ImageView iv_sc_y = view.findViewById(R.id.iv_sc_y);
            TextView tv_sc_y = view.findViewById(R.id.tv_sc_y);
            Glide.with(getActivity())
                    .load( Api.sUrl+Api.sUrl + arrlist.get(position).get("ItemImage"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_sc_y);
            tv_sc_y.setText(arrlist.get(position).get("ItemName"));

            return view;
        }
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
