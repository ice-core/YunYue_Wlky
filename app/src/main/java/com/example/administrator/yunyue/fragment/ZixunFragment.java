package com.example.administrator.yunyue.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.Utils;
import com.example.administrator.yunyue.activity.ZxssActivity;
import com.example.administrator.yunyue.dao.ChannelItem;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.edit.ChannelActivity;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.example.administrator.yunyue.view.ColumnHorizontalScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ZixunFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ZixunFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ZixunFragment extends Fragment implements TabFragment.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    /**
     * Description：仿今日头条首页tab动态添加和删除
     * <p>
     * Created by Mjj on 2016/11/18.
     */
    private ColumnHorizontalScrollView mColumnHorizontalScrollView; // 自定义HorizontalScrollView

    private ColumnHorizontalScrollView mColumnHorizontalScrollView1;

    private LinearLayout mRadioGroup_content; // 每个标题

    private LinearLayout ll_more_columns; // 右边+号的父布局
    private ImageView button_more_columns; // 标题右边的+号

    private RelativeLayout rl_column; // +号左边的布局：包括HorizontalScrollView和左右阴影部分
    public ImageView shade_left; // 左阴影部分
    public ImageView shade_right; // 右阴影部分
    public ImageView shade_left1; // 左阴影部分
    public ImageView shade_right1; // 右阴影部分

    private int columnSelectIndex = 0; // 当前选中的栏目索引
    private int mItemWidth = 0; // Item宽度：每个标题的宽度
    private int mScreenWidth = 0; // 屏幕宽度
    public final static int CHANNELREQUEST = 1; // 请求码
    public final static int CHANNELRESULT = 10; // 返回码

    // tab集合：HorizontalScrollView的数据源
    public static ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();

    public static ArrayList<ChannelItem> otherChannelList = new ArrayList<ChannelItem>();
    private ViewPager mViewPager;
    private ArrayList<android.support.v4.app.Fragment> fragments = new ArrayList<android.support.v4.app.Fragment>();
    public static int iPositionId = 0;
    public static int iPosition = 0;
    RequestQueue queue = null;
    private SharedPreferences pref;
    private String sUser_id;
    private GridView gv_zx_lb;
    private MyAdapter myAdapter;

    private LinearLayout ll_zixun_query;

    public ZixunFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ZixunFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ZixunFragment newInstance(String param1, String param2) {
        ZixunFragment fragment = new ZixunFragment();
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
        return inflater.inflate(R.layout.fragment_zixun, container, false);
    }

    public void onViewCreated(View view, Bundle saveInstancesState) {
        super.onViewCreated(view, saveInstancesState);
        queue = Volley.newRequestQueue(getActivity());
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sUser_id = pref.getString("user_id", "");
        mScreenWidth = Utils.getWindowsWidth(getActivity());
        mItemWidth = mScreenWidth / 5; // 一个Item宽度为屏幕的1/7
        mColumnHorizontalScrollView = (ColumnHorizontalScrollView) view.findViewById(R.id.mColumnHorizontalScrollView);
        mColumnHorizontalScrollView1 = (ColumnHorizontalScrollView) view.findViewById(R.id.mColumnHorizontalScrollView1);
        mRadioGroup_content = (LinearLayout) view.findViewById(R.id.mRadioGroup_content);
        ll_more_columns = (LinearLayout) view.findViewById(R.id.ll_more_columns);
        rl_column = (RelativeLayout) view.findViewById(R.id.rl_column);
        button_more_columns = (ImageView) view.findViewById(R.id.button_more_columns);
        shade_left = (ImageView) view.findViewById(R.id.shade_left);
        shade_right = (ImageView) view.findViewById(R.id.shade_right);
        shade_left1 = (ImageView) view.findViewById(R.id.shade_left1);
        shade_right1 = (ImageView) view.findViewById(R.id.shade_right1);
        mViewPager = (ViewPager) view.findViewById(R.id.mViewPager);
        ll_zixun_query = view.findViewById(R.id.ll_zixun_query);
        gv_zx_lb = view.findViewById(R.id.gv_zx_lb);
        initView();
        liebiao();
        qb_liebiao();
    }

    /**
     * 设置GirdView参数，绑定数据
     */
    private void setGridView() {
        myAdapter = new MyAdapter(getActivity());
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < userChannelList.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemName", userChannelList.get(i).getName());
            map.put("ItemId", String.valueOf(userChannelList.get(i).getId()));
            mylist.add(map);
        }
        myAdapter.arrlist = mylist;
        int size = mylist.size();
        int length = mItemWidth;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                mItemWidth * userChannelList.size() + mItemWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gv_zx_lb.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gv_zx_lb.setColumnWidth(mItemWidth); // 设置列表项宽
        gv_zx_lb.setHorizontalSpacing(10); // 设置列表项水平间距
        gv_zx_lb.setStretchMode(GridView.NO_STRETCH);
        gv_zx_lb.setNumColumns(size); // 设置列数量=列表集合数
        gv_zx_lb.setAdapter(myAdapter);
        /*sv_shouye.smoothScrollTo(0, 20);
        sv_shouye.setFocusable(true);*/
    }

    /**
     * 获取tab
     */
    private void liebiao() {
        String url = Api.sUrl + "Api/Getnew/getfellei/appId/" + Api.sApp_Id;
        // String url = Api.sUrl + "GetZhixun/getType/user_id/" + sUser_id;
        // String url = Api.sUrl + "GetZhixun/getType";
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
                        //    userChannelList = new ArrayList<ChannelItem>();
                        ArrayList<ChannelItem> ChannelList = new ArrayList<ChannelItem>();
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            String resultId = jsonObject.getString("id");
                            String resultName = jsonObject.getString("name");
                            if (i == 0) {
                                iPositionId = Integer.valueOf(resultId);
                            }
                            ChannelList.add(new ChannelItem(Integer.valueOf(resultId), resultName, Integer.valueOf(resultId), Integer.valueOf(resultId)));
                        }
                        if (userChannelList.size() == 0) {
                            userChannelList = ChannelList;
                        }
                        setGridView();
                        initColumnData();
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
     * 获取tab
     */
    private void qb_liebiao() {
        // String url = Api.sUrl + "GetZhixun/getType/user_id/" + sUser_id;
        String url = Api.sUrl + "GetZhixun/getTypeList/user_id/" + sUser_id;
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
                        ArrayList<ChannelItem> ChannelList = new ArrayList<ChannelItem>();
                        //   otherChannelList = new ArrayList<ChannelItem>();
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            String resultId = jsonObject.getString("id");
                            String resultName = jsonObject.getString("name");
                            ChannelList.add(new ChannelItem(Integer.valueOf(resultId), resultName, Integer.valueOf(resultId), Integer.valueOf(resultId)));
                            otherChannelList.add(new ChannelItem(Integer.valueOf(resultId), resultName, Integer.valueOf(resultId), Integer.valueOf(resultId)));
                        }
                        if (otherChannelList.size() == 0) {
                            otherChannelList = ChannelList;
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
        queue.add(request);
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

    private void initView() {
        // + 号监听
        button_more_columns.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent_channel = new Intent(getActivity().getApplicationContext(), ChannelActivity.class);
                startActivityForResult(intent_channel, CHANNELREQUEST);
            }
        });
        ll_zixun_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ZxssActivity.class);
                startActivity(intent);
            }
        });
        //  setChangelView();
    }

    /**
     * 当栏目项发生变化时候调用
     */
    private void setChangelView() {
        liebiao();
    }

    /**
     * 获取Column栏目 数据
     */
    private void initColumnData() {
        initTabColumn();
        initFragment();
        //userChannelList.add(new ChannelItem(1, "推荐", 1, 1));
        // userChannelList = ((ArrayList<ChannelItem>) ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).getUserChannel());
    }

    /**
     * 初始化Column栏目项
     */
    private void initTabColumn() {
        // mRadioGroup_content.removeAllViews();
        int count = userChannelList.size();
        mColumnHorizontalScrollView.setParam(getActivity(), mScreenWidth, mRadioGroup_content, shade_left, shade_right, ll_more_columns, rl_column);
        mColumnHorizontalScrollView1.setParam(getActivity(), mScreenWidth, mRadioGroup_content, shade_left1, shade_right1, ll_more_columns, rl_column);
/*        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            TextView columnTextView = new TextView(getActivity());
            columnTextView.setGravity(Gravity.CENTER);
            columnTextView.setPadding(5, 5, 5, 5);
            columnTextView.setId(i);
            columnTextView.setText(userChannelList.get(i).getName());
            columnTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
            if (columnSelectIndex == i) {
                columnTextView.setSelected(true);
            }

            // 单击监听
            columnTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
                        View localView = mRadioGroup_content.getChildAt(i);
                        if (localView != v) {
                            localView.setSelected(false);
                        } else {
                            localView.setSelected(true);
                            mViewPager.setCurrentItem(i);
                        }
                    }
                    Toast.makeText(getActivity().getApplicationContext(), userChannelList.get(v.getId()).getName(), Toast.LENGTH_SHORT).show();
                }
            });
            mRadioGroup_content.addView(columnTextView, i, params);

        }*/
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        fragments.clear();//清空
        int count = userChannelList.size();
        for (int i = 0; i < count; i++) {
            //   NewsFragment newfragment = new NewsFragment();
            TabFragment tabFragment = new TabFragment();
            fragments.add(tabFragment);
        }
        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapetr);
        mViewPager.addOnPageChangeListener(pageListener);
    }


    /**
     * ViewPager切换监听方法
     */
    public ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            mViewPager.setCurrentItem(position);
            selectTab(position);
        }
    };

    /**
     * 选择的Column里面的Tab
     */
    private void selectTab(int tab_postion) {
        iPositionId = Integer.valueOf(userChannelList.get(tab_postion).getId());
        iPosition = tab_postion;
        myAdapter.notifyDataSetChanged();
    /*    columnSelectIndex = tab_postion;
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            View checkView = mRadioGroup_content.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - mScreenWidth / 2;
            mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
            mColumnHorizontalScrollView1.smoothScrollTo(i2, 0);

        }*/
        int i2 = 0;
        if (tab_postion == 0) {
            i2 = mItemWidth + mItemWidth / 2 - mScreenWidth / 2;
        } else {
            i2 = mItemWidth * tab_postion + mItemWidth / 2 - mScreenWidth / 2;
        }
        mColumnHorizontalScrollView1.smoothScrollTo(i2, 0);
        //判断是否选中
     /*     for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
            View checkView = mRadioGroup_content.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView.setSelected(ischeck);
        }*/
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
                view = inflater.inflate(R.layout.zx_fl_item, null);
            }
            LinearLayout ll_fl_item = view.findViewById(R.id.ll_fl_item);
            TextView zx_fl_item_name = view.findViewById(R.id.zx_fl_item_name);
            TextView zx_fl_item = view.findViewById(R.id.zx_fl_item);
            zx_fl_item_name.setText(arrlist.get(position).get("ItemName"));
            if (iPositionId == Integer.valueOf(arrlist.get(position).get("ItemId"))) {
                zx_fl_item_name.setTextColor(zx_fl_item_name.getResources().getColor(R.color.theme));
                zx_fl_item.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.theme));
            } else {
                zx_fl_item_name.setTextColor(zx_fl_item_name.getResources().getColor(R.color.hui999999));
                zx_fl_item.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.touming));
            }
            ll_fl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iPositionId = Integer.valueOf(arrlist.get(position).get("ItemId"));
                    iPosition = position;
                    notifyDataSetChanged();
                    mViewPager.setCurrentItem(position);
                }
            });

            return view;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHANNELREQUEST:
                if (resultCode == CHANNELRESULT) {
                    setChangelView();
                }
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

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
