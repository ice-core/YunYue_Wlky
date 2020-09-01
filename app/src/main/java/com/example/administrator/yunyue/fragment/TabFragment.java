package com.example.administrator.yunyue.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
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

import com.example.administrator.yunyue.activity.ZixunActivity;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = TabFragment.class.getSimpleName();
    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;

    private SharedPreferences pref;

    //Fragment的View加载完毕的标记
    private boolean isViewCreated;
    //Fragment对用户可见的标记
    private boolean isUIVisible;

    String str = "0";

    RequestQueue queue = null;
    private int iPage;

    private MyAdapterLIST myAdapterlist;

    private PullToRefreshGridView mPullRefreshListView;

    private List<NineGridTestModel> mList = new ArrayList<>();
    private String[] mUrls = new String[]{"http://d.hiphotos.baidu.com/image/h%3D200/sign=201258cbcd80653864eaa313a7dca115/ca1349540923dd54e54f7aedd609b3de9c824873.jpg",
            "http://img3.fengniao.com/forum/attachpics/537/165/21472986.jpg",
            "http://d.hiphotos.baidu.com/image/h%3D200/sign=ea218b2c5566d01661199928a729d498/a08b87d6277f9e2fd4f215e91830e924b999f308.jpg",
            "http://img4.imgtn.bdimg.com/it/u=3445377427,2645691367&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=2644422079,4250545639&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=1444023808,3753293381&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=882039601,2636712663&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=4119861953,350096499&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2437456944,1135705439&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=3251359643,4211266111&fm=21&gp=0.jpg",
            "http://img4.duitang.com/uploads/item/201506/11/20150611000809_yFe5Z.jpeg",
            "http://img5.imgtn.bdimg.com/it/u=1717647885,4193212272&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2024625579,507531332&fm=21&gp=0.jpg"};
    private String sUser_id = "";
    private String sZhixun = "";
    private boolean isDx = false;

    public TabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShequFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabFragment newInstance(String param1, String param2) {
        TabFragment fragment = new TabFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();
        if (bundle != null) {

        }
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    /**
     *  * 方法名：initView()
     *  * 功    能：控件初始化
     *  * 参    数：无
     *  * 返回值：无
     */
    public void onViewCreated(View view, Bundle saveInstancesState) {
        super.onViewCreated(view, saveInstancesState);
        queue = Volley.newRequestQueue(getActivity());
        iPage = 1;
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean isRemember = pref.getBoolean("user", false);
        sUser_id = pref.getString("user_id", "");
        sZhixun = pref.getString("zhixun", "");
        mPullRefreshListView = (PullToRefreshGridView) view.findViewById(R.id.pull_refresh_grid);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        isViewCreated = true;
        myAdapterlist = new MyAdapterLIST(getActivity());

        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
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
                myAdapterlist = new MyAdapterLIST(getActivity());
                isDx = false;
                iPage = 1;
                // huoqu();
                zx();
                //   huoqu(iPage);
            }

            // 上拉加载跟多
            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                Log.e("TAG", "onPullUpToRefresh"); // Do work to refresh
                if (isDx == false) {
                    zx();
                } else {
                    myAdapterlist.notifyDataSetChanged();
                    mPullRefreshListView.onRefreshComplete();
                }
                //   huoqu(iPage);
                // 页数
                //   p = p + 1;
                // AsyncTask异步交互加载数据
                // new GetDataTask2().execute(URL + p);
            }
        });

        mList = new ArrayList<>();
        // 刷新适配器
        myAdapterlist.notifyDataSetChanged();
        if (ZixunFragment.userChannelList.size() > 0) {
            if (ZixunFragment.iPositionId == ZixunFragment.userChannelList.get(0).getId()) {
                zx();
            }
        }
        // }
    }

    /**
     * 咨询列表
     */
    private void zx() {
        // String url = Api.sUrl + "GetZhixun/getType/user_id/" + sUser_id;
        String url = Api.sUrl + "Api/Getnew/getNewList/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/type_id/" + ZixunFragment.iPositionId + "/page/" + iPage;

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
                        if (iPage == 1) {
                            gridviewdata();
                        } else {
                            gridviewdata1();
                        }
                        //  Hint(resultMsg, HintDialog.ERROR);
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

    private void gridviewdata() {
        mPullRefreshListView.setAdapter(myAdapterlist);
        // 刷新适配器
        myAdapterlist.notifyDataSetChanged();
        // 关闭刷新下拉
        mPullRefreshListView.onRefreshComplete();
    }

    private void gridviewdata1() {
        // myList = getMenuAdapter();
        // iPage += 1;
        // 刷新适配器
        myAdapterlist.notifyDataSetChanged();
        // Call onRefreshComplete when the list has been refreshed.
        // 关闭上拉加载
        mPullRefreshListView.onRefreshComplete();
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
            //   dialogin("");
            myAdapterlist = new MyAdapterLIST(getActivity());
            iPage = 1;
            // huoqu();
            mList = new ArrayList<>();
            zx();
            /*    }*/
            //数据加载完毕,恢复标记,防止重复加载
   /*         isViewCreated = false;
            isUIVisible = false;*/
            //printLog(mTextviewContent+"可见,加载数据");
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
                                Intent intent = new Intent(getActivity(), GuanggaoActivity.class);
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
                        //NineGridTestLayout layout_nine_grid = view.findViewById(R.id.layout_nine_grid);
                        tv_zx_tj1_2_title.setText(mList.get(position).title);
                        tv_zx_tj1_2_getName.setText(mList.get(position).getName);
                        tv_zx_tj1_2_time.setText(mList.get(position).time);
                        tv_zx_tj1_2_pinglun_num.setText(mList.get(position).pinglun_num + "评论");
                        //layout_nine_grid.setUrlList(mList.get(position).urlList);
                        ImageView iv_zx_tj1_2_img = view.findViewById(R.id.iv_zx_tj1_2_img);
                        ImageView iv_zx_tj1_2_img1 = view.findViewById(R.id.iv_zx_tj1_2_img1);
                        ImageView iv_zx_tj1_2_img2 = view.findViewById(R.id.iv_zx_tj1_2_img2);
                        if (mList.get(position).urlList.size() == 1) {
                            Glide.with(getActivity()).load(mList.get(position).urlList.get(0))
                                    .asBitmap()
                                    .placeholder(R.mipmap.error)
                                    .error(R.mipmap.error)
                                    .into(iv_zx_tj1_2_img);
                        } else if (mList.get(position).urlList.size() == 2) {
                            Glide.with(getActivity()).load(mList.get(position).urlList.get(0))
                                    .asBitmap()
                                    .placeholder(R.mipmap.error)
                                    .error(R.mipmap.error)
                                    .into(iv_zx_tj1_2_img);
                            Glide.with(getActivity()).load(mList.get(position).urlList.get(1))
                                    .asBitmap()
                                    .placeholder(R.mipmap.error)
                                    .error(R.mipmap.error)
                                    .into(iv_zx_tj1_2_img1);
                        } else if (mList.get(position).urlList.size() == 3) {
                            Glide.with(getActivity()).load(mList.get(position).urlList.get(0))
                                    .asBitmap()
                                    .placeholder(R.mipmap.error)
                                    .error(R.mipmap.error)
                                    .into(iv_zx_tj1_2_img);
                            Glide.with(getActivity()).load(mList.get(position).urlList.get(1))
                                    .asBitmap()
                                    .placeholder(R.mipmap.error)
                                    .error(R.mipmap.error)
                                    .into(iv_zx_tj1_2_img1);
                            Glide.with(getActivity()).load(mList.get(position).urlList.get(2))
                                    .asBitmap()
                                    .placeholder(R.mipmap.error)
                                    .error(R.mipmap.error)
                                    .into(iv_zx_tj1_2_img2);
                        }
                        ll_zx_tj1_2_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), GuanggaoActivity.class);
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
                        Glide.with(getActivity()).load(mList.get(position).img)
                                .asBitmap()
                                .placeholder(R.mipmap.error)
                                .error(R.mipmap.error)
                                .into(iv_zx_tj1_1_img);

                        ll_zx_tj1_1_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), GuanggaoActivity.class);
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
                                Intent intent = new Intent(getActivity(), GuanggaoActivity.class);
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
                        //tv_zx_tj1_1_getName.setText(mList.get(position).getName);
                        tv_zx_tj2_1_time.setText(mList.get(position).time);
                        tv_zx_tj2_1_pinglun_num.setText(mList.get(position).pinglun_num + "评论");
                        Glide.with(getActivity()).load(mList.get(position).img)
                                .asBitmap()
                                .placeholder(R.mipmap.error)
                                .error(R.mipmap.error)
                                .into(iv_zx_tj2_1_img);

                        ll_zx_tj2_1_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), GuanggaoActivity.class);
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
                                Intent intent = new Intent(getActivity(), GuanggaoActivity.class);
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
                            Glide.with(getActivity()).load(mList.get(position).img)
                                    .asBitmap()
                                    .placeholder(R.mipmap.error)
                                    .error(R.mipmap.error)
                                    .into(iv_zx_tj3_1_img);
                        } else {
                            if (mList.get(position).urlList.size() == 1) {
                                Glide.with(getActivity()).load(mList.get(position).urlList.get(0))
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .into(iv_zx_tj3_1_img);
                            } else if (mList.get(position).urlList.size() == 2) {
                                Glide.with(getActivity()).load(mList.get(position).urlList.get(0))
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .into(iv_zx_tj3_1_img);
                                Glide.with(getActivity()).load(mList.get(position).urlList.get(1))
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .into(iv_zx_tj3_1_img1);
                            } else if (mList.get(position).urlList.size() == 3) {
                                Glide.with(getActivity()).load(mList.get(position).urlList.get(0))
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .into(iv_zx_tj3_1_img);
                                Glide.with(getActivity()).load(mList.get(position).urlList.get(1))
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .into(iv_zx_tj3_1_img1);
                                Glide.with(getActivity()).load(mList.get(position).urlList.get(2))
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .into(iv_zx_tj3_1_img2);
                            }
                        }
                        ll_zx_tj3_1_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (mList.size() > 0) {
                                    Intent intent = new Intent(getActivity(), GuanggaoActivity.class);
                                    intent.putExtra("link", mList.get(position).url + sUser_id);
                                    startActivity(intent);
                                }
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
}
