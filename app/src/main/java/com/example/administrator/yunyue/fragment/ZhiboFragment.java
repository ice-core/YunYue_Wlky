package com.example.administrator.yunyue.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.example.administrator.yunyue.sc.ObservableScrollView;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.example.administrator.yunyue.zb_activity.GengduoActivity;
import com.example.administrator.yunyue.zb_activity.LiebiaoActivity;
import com.example.administrator.yunyue.zb_activity.SousuoActivity;
import com.example.administrator.yunyue.zb_activity.ZhiboActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ZhiboFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ZhiboFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ZhiboFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    @BindView(R.id.srl_control)
    SmartRefreshLayout srlControl;

    public ZhiboFragment() {
        // Required empty public constructor
    }

    /**
     * 热门推荐
     */
    private MyGridView mgv_rmtj;
    /**
     * 分类
     */
    private MyGridView mgv_zhibo_fenlei;
    /**
     * 搜索
     */
    private LinearLayout ll_zhibo_ss;
    RequestQueue queue = null;

    private static final int NUM_PAGES = 5;
    private ViewPager mPager;
    private MyAdViewPagerAdapter mPagerAdapter;
    private ObservableScrollView sv_zhibo;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ZhiboFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ZhiboFragment newInstance(String param1, String param2) {
        ZhiboFragment fragment = new ZhiboFragment();
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
        return inflater.inflate(R.layout.fragment_zhibo, container, false);
    }

    public void onViewCreated(View view, Bundle saveInstancesState) {
        super.onViewCreated(view, saveInstancesState);
        queue = Volley.newRequestQueue(getActivity());
        mgv_rmtj = view.findViewById(R.id.mgv_rmtj);
        mgv_zhibo_fenlei = view.findViewById(R.id.mgv_zhibo_fenlei);
        ll_zhibo_ss = view.findViewById(R.id.ll_zhibo_ss);
        sv_zhibo = view.findViewById(R.id.sv_zhibo);
        ll_zhibo_ss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SousuoActivity.class);
                startActivity(intent);
            }
        });
        srlControl = view.findViewById(R.id.srl_control);
        mPager = (ViewPager) view.findViewById(R.id.pager);
        query();
        smartRefresh();
    }

    //监听下拉和上拉状态
    public void smartRefresh() {
        //下拉刷新
        srlControl.setOnRefreshListener(refreshlayout -> {
            srlControl.setEnableRefresh(true);//启用刷新
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 Data() 来模拟我们刷新出来的数据
             */
            query();
            // refreshAdapter.strList.clear();
            // refreshAdapter.refreshData(Data());
            //  iPage = 1;
            // query();
            // srlControl.finishRefresh();//结束刷新

        });
       /* //上拉加载
        srlControl.setOnLoadmoreListener(refreshlayout -> {
            srlControl.setEnableLoadmore(true);//启用加载
            *//**
         * 正常来说，应该在这里加载网络数据
         * 这里我们就使用模拟数据 MoreDatas() 来模拟我们加载出来的数据
         *//*
            //  refreshAdapter.loadMore(MoreDatas());
            //  iPage += 1;
            //  query();
            srlControl.finishLoadmore();//结束加载
        });*/
    }

    /**
     * 直播首页信息获取
     */
    private void query() {
        String url = Api.sUrl + "Broadcast/Api/index/appId/" + Api.sApp_Id;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                srlControl.finishRefresh();//结束刷新
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        JSONObject jsonObjectdata = new JSONObject(sDate.toString()).getJSONObject("data");
                        JSONArray jsonArraybanner = jsonObjectdata.getJSONArray("banner");
                        ArrayList<HashMap<String, String>> mylist_banner = new ArrayList<HashMap<String, String>>();
                        ArrayList<ImageView> imageViews = new ArrayList();
                        for (int i = 0; i < jsonArraybanner.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArraybanner.opt(i);
              /*              String ItemId = jsonObject2.getString("id");
                            String Itemvideo_img = jsonObject2.getString("video_img");
                            String Itemvideo_address = jsonObject2.getString("video_address");*/
                            String Itemurl = jsonObject2.getString("url");
                            HashMap<String, String> map = new HashMap<String, String>();
               /*             map.put("id", ItemId);
                            map.put("video_img", Itemvideo_img);
                            map.put("video_address", Itemvideo_address);*/
                            mylist_banner.add(map);
                            ImageView imageView = new ImageView(getActivity());
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            imageView.setPadding(50, 0, 50, 0);
                            //imageView.setImageResource(R.drawable.image1);
                            Glide.with(getActivity())
                                    .load( Api.sUrl+Itemurl)
                                    .asBitmap()
                                    .placeholder(R.mipmap.error)
                                    .error(R.mipmap.error)
                                    .dontAnimate()
                                    .into(imageView);
                            imageViews.add(imageView);
                        }
                        // mPager.setBackgroundResource(R.drawable.link);
                        mPagerAdapter = new MyAdViewPagerAdapter(getActivity(), imageViews);
                        mPager.setAdapter(mPagerAdapter);
                        mPager.setPageMargin(20);
                        // setGridView_fenlei(mylist_banner);

                        JSONArray jsonArraytype = jsonObjectdata.getJSONArray("type");
                        ArrayList<HashMap<String, String>> mylist_type = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < jsonArraytype.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArraytype.opt(i);
                            String Itemid = jsonObject2.getString("id");
                            String Itemtypename = jsonObject2.getString("typename");
                            String Itemtype_img = jsonObject2.getString("type_img");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", Itemid);
                            map.put("typename", Itemtypename);
                            map.put("type_img", Itemtype_img);
                            mylist_type.add(map);
                        }
                        setGridView_fenlei(mylist_type);
                        JSONArray jsonArrayvideo = jsonObjectdata.getJSONArray("video");
                        ArrayList<HashMap<String, String>> mylist_video = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < jsonArrayvideo.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArrayvideo.opt(i);
                            String Itemid = jsonObject2.getString("id");
                            String Itemtitle = jsonObject2.getString("title");
                            String Itemvideo_img = jsonObject2.getString("video_img");
                            String Itemvideo_address = jsonObject2.getString("video_address");
                            String Itemplay_cont = jsonObject2.getString("play_cont");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", Itemid);
                            map.put("title", Itemtitle);
                            map.put("video_img", Itemvideo_img);
                            map.put("video_address", Itemvideo_address);
                            map.put("play_cont", Itemplay_cont);
                            mylist_video.add(map);
                        }
                        setGridView_rmtj(mylist_video);

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


    public class MyAdViewPagerAdapter extends PagerAdapter {
//    @Override
//    public float getPageWidth(int position) {
//       return 0.9f;
//    }

        private ArrayList<ImageView> pagerList;
        private Context ctx;

        public MyAdViewPagerAdapter(Context ctx, ArrayList<ImageView> pagerList) {
            this.ctx = ctx;
            this.pagerList = pagerList;
        }

        @Override
        public int getCount() {
            return pagerList.size();
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(pagerList.get(position));
            return pagerList.get(position);
        }

        @Override
        public void destroyItem(View container, int position, Object view) {
            ((ViewPager) container).removeView(pagerList.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public void startUpdate(View arg0) {
        }

        @Override
        public void finishUpdate(View arg0) {
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

    /**
     * 分类
     * 设置GirdView参数，绑定数据
     */
    private void setGridView_fenlei(ArrayList<HashMap<String, String>> mylist) {
        MyAdapter_Fenlei myAdapter_fenlei = new MyAdapter_Fenlei(getActivity());

        myAdapter_fenlei.arrlist = mylist;
        mgv_zhibo_fenlei.setAdapter(myAdapter_fenlei);
    }

    /**
     * 热门推荐
     * 设置GirdView参数，绑定数据
     */
    private void setGridView_rmtj(ArrayList<HashMap<String, String>> mylist) {
        MyAdapter_Rmtj myAdapter_rmtj = new MyAdapter_Rmtj(getActivity());

        myAdapter_rmtj.arrlist = mylist;
        mgv_rmtj.setAdapter(myAdapter_rmtj);
        sv_zhibo.smoothScrollTo(0, 20);
    }

    private class MyAdapter_Fenlei extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;


        public MyAdapter_Fenlei(Context context) {
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
                view = inflater.inflate(R.layout.zhibo_fenlei, null);
            }
            LinearLayout ll_zhibo_fenlei = view.findViewById(R.id.ll_zhibo_fenlei);
            ll_zhibo_fenlei.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (arrlist.get(position).get("id").equals("0")) {
                        Intent intent = new Intent(getActivity(), GengduoActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), LiebiaoActivity.class);
                        intent.putExtra("type_id", arrlist.get(position).get("id"));
                        intent.putExtra("typename", arrlist.get(position).get("typename"));
                        startActivity(intent);
                    }
                }
            });
            ImageView iv_zhibo_fl_item = view.findViewById(R.id.iv_zhibo_fl_item);
            TextView tv_zhibo_fl_item = view.findViewById(R.id.tv_zhibo_fl_item);
            tv_zhibo_fl_item.setText(arrlist.get(position).get("typename"));
            Glide.with(getActivity())
                    .load( Api.sUrl+arrlist.get(position).get("type_img"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_zhibo_fl_item);

            return view;
        }
    }

    private class MyAdapter_Rmtj extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;


        public MyAdapter_Rmtj(Context context) {
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
                view = inflater.inflate(R.layout.zhibo_rmtj, null);
            }
            LinearLayout ll_zhibo_rmtj = view.findViewById(R.id.ll_zhibo_rmtj);
            ll_zhibo_rmtj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ZhiboActivity.class);
                    intent.putExtra("id", arrlist.get(position).get("id"));
                    startActivity(intent);
                }
            });
            ImageView iv_zhibo_rmtj_img = view.findViewById(R.id.iv_zhibo_rmtj_img);
            TextView tv_zhibo_rmtj_title = view.findViewById(R.id.tv_zhibo_rmtj_title);
            TextView tv_zhibo_rmtj_cont = view.findViewById(R.id.tv_zhibo_rmtj_cont);
            tv_zhibo_rmtj_title.setText(arrlist.get(position).get("title"));
            tv_zhibo_rmtj_cont.setText(arrlist.get(position).get("play_cont"));

            Glide.with(getActivity())
                    .load( Api.sUrl+arrlist.get(position).get("video_img"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_zhibo_rmtj_img);


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
