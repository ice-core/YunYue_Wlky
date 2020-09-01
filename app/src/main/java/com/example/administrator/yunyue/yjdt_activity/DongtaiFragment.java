package com.example.administrator.yunyue.yjdt_activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.example.administrator.yunyue.sc.CircleImageView;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DongtaiFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DongtaiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DongtaiFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DongtaiFragment() {
        // Required empty public constructor
    }

    private static final String TAG = DongtaiFragment.class.getSimpleName();

    /**
     * 供应方
     */
    private LinearLayout ll_qiuying_dt_gyf;
    private TextView tv_qiuying_dt_gyf, tv_qiuying_dt_gyf_xhx;

    /**
     * 需求方
     */
    private LinearLayout ll_qiuying_dt_xqf;
    private TextView tv_qiuying_dt_xqf, tv_qiuying_dt_xqf_xhx;

    /**
     * 发布
     */
    private ImageView iv_dongtai_fb;
    /**
     * 动态类型
     * 1供方 2需方
     */
    private String sType = "1";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DongtaiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DongtaiFragment newInstance(String param1, String param2) {
        DongtaiFragment fragment = new DongtaiFragment();
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

    private MyAdapter myAdapter;
    /**
     * 动态列表
     */
    private ListView lv_dongtai;

    private int iPage;
    private List<QiuyingDongtaiData> mList = new ArrayList<>();
    @BindView(R.id.srl_control_shouye)
    SmartRefreshLayout srlControlShouye;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dongtai, container, false);
    }

    public void onViewCreated(View view, Bundle saveInstancesState) {
        super.onViewCreated(view, saveInstancesState);
        ll_qiuying_dt_gyf = view.findViewById(R.id.ll_qiuying_dt_gyf);
        tv_qiuying_dt_gyf = view.findViewById(R.id.tv_qiuying_dt_gyf);
        tv_qiuying_dt_gyf_xhx = view.findViewById(R.id.tv_qiuying_dt_gyf_xhx);
        tv_qiuying_dt_xqf = view.findViewById(R.id.tv_qiuying_dt_xqf);
        tv_qiuying_dt_xqf_xhx = view.findViewById(R.id.tv_qiuying_dt_xqf_xhx);
        iv_dongtai_fb = view.findViewById(R.id.iv_dongtai_fb);
        lv_dongtai = view.findViewById(R.id.lv_dongtai);
        srlControlShouye = view.findViewById(R.id.srl_control_shouye);
        myAdapter = new MyAdapter(getActivity());
        ll_qiuying_dt_gyf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sType = "1";
                tv_qiuying_dt_gyf.setTextColor(tv_qiuying_dt_gyf.getResources().getColor(R.color.theme));
                tv_qiuying_dt_xqf.setTextColor(tv_qiuying_dt_xqf.getResources().getColor(R.color.bbbccd));
                tv_qiuying_dt_gyf_xhx.setVisibility(View.VISIBLE);
                tv_qiuying_dt_xqf_xhx.setVisibility(View.GONE);
                iPage = 1;
                sDynamicIndex();
            }
        });
        ll_qiuying_dt_xqf = view.findViewById(R.id.ll_qiuying_dt_xqf);
        ll_qiuying_dt_xqf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sType = "2";
                tv_qiuying_dt_gyf.setTextColor(tv_qiuying_dt_gyf.getResources().getColor(R.color.bbbccd));
                tv_qiuying_dt_xqf.setTextColor(tv_qiuying_dt_xqf.getResources().getColor(R.color.theme));
                tv_qiuying_dt_gyf_xhx.setVisibility(View.GONE);
                tv_qiuying_dt_xqf_xhx.setVisibility(View.VISIBLE);
                iPage = 1;
                sDynamicIndex();
            }
        });
        iv_dongtai_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QiuyingFbDtActivity.class);
                intent.putExtra("type", sType);
                startActivity(intent);
            }
        });
        iPage = 1;
        sDynamicIndex();
        smartRefresh();
    }

    //监听下拉和上拉状态
    public void smartRefresh() {
        //下拉刷新
        srlControlShouye.setOnRefreshListener(refreshlayout -> {
            srlControlShouye.setEnableRefresh(true);//启用刷新
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 Data() 来模拟我们刷新出来的数据
             */
            iPage = 1;
            sDynamicIndex();

        });
        //上拉加载
        srlControlShouye.setOnLoadmoreListener(refreshlayout -> {
            srlControlShouye.setEnableLoadmore(true);//启用加载
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 MoreDatas() 来模拟我们加载出来的数据
             */
            iPage += 1;
            sDynamicIndex();
        });
    }

    /**
     * 方法名：sDynamicIndex()
     * 功  能：动态接口
     * 参  数：appId
     * title--标题
     * img--详情图片
     * type--类型
     */
    private void sDynamicIndex() {
        String url = Api.sUrl + Api.sDynamicIndex;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("type", sType);
        params.put("page", String.valueOf(iPage));
        // params.put("address", sType);

        JSONObject jsonObject = new JSONObject(params);
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (iPage == 1) {
                            srlControlShouye.finishRefresh();//结束刷新
                        } else {
                            srlControlShouye.finishLoadmore();//结束加载
                        }
                        hideDialogin();
                        String sDate = response.toString();
                        System.out.println(sDate);
                        try {
                            JSONObject jsonObject = new JSONObject(sDate);
                            String resultMsg = jsonObject.getString("msg");
                            int resultCode = jsonObject.getInt("code");
                            mList = new ArrayList<>();
                            if (resultCode > 0) {

                                JSONArray resultJsonArray = jsonObject.getJSONArray("data");

                                for (int i = 0; i < resultJsonArray.length(); i++) {
                                    jsonObject = resultJsonArray.getJSONObject(i);
                                    String resultId = jsonObject.getString("id");
                                    String resultTitle = jsonObject.getString("title");
                                    String resultUserId = jsonObject.getString("user_id");
                                    String resultAddTime = jsonObject.getString("add_time");
                                    String resultNicName = jsonObject.getString("nickname");
                                    String resultHeadimgurl = jsonObject.getString("headimgurl");
                                    JSONArray jsonArrayImglist = jsonObject.getJSONArray("img");
                                    QiuyingDongtaiData model = new QiuyingDongtaiData();
                                    for (int a = 0; a < jsonArrayImglist.length(); a++) {
                                        String imge = jsonArrayImglist.get(a).toString();
                                        model.urlList.add(imge);
                                    }
                                    model.id = resultId;
                                    model.title = resultTitle;
                                    model.user_id = resultUserId;
                                    model.add_time = resultAddTime;
                                    model.nickname = resultNicName;
                                    model.headimgurl = resultHeadimgurl;
                                    mList.add(model);
                                }
                                if (iPage == 1) {
                                    gridviewdata(mList);
                                } else {

                                    if (mList.size() == 0) {
                                        iPage -= 1;
                                    } else {
                                        gridviewdata1(mList);
                                    }
                                }


                            } else {
                                QiuyingDongtaiData model = new QiuyingDongtaiData();

                                mList.add(model);
                                if (iPage == 1) {
                                    gridviewdata(mList);
                                } else {
                                    gridviewdata1(mList);
                                }

                            }
                        } catch (JSONException e) {
                            hideDialogin();
                            e.printStackTrace();
                        }

                        Log.d(TAG, "response -> " + response.toString());
                        //   Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (iPage == 1) {
                    srlControlShouye.finishRefresh();//结束刷新
                } else {
                    srlControlShouye.finishLoadmore();//结束加载
                }
                hideDialogin();
                //  Hint(error.getMessage(), HintDialog.ERROR);
                error(error);
            }
        });
        requestQueue.add(jsonRequest);
    }


    /**
     * 热门帖子
     */
    private void gridviewdata(List<QiuyingDongtaiData> mList) {
        myAdapter = new MyAdapter(getActivity());
        myAdapter.listdata = mList;
        //  mgv_jysq_rmtz.setAdapter(myAdapterShequ);
        lv_dongtai.setAdapter(myAdapter);
        setListViewHeightBasedOnChildren(lv_dongtai);
    }

    private void gridviewdata1(List<QiuyingDongtaiData> mList) {
        iPage += 1;
        myAdapter.listdata.addAll(mList);
        //  mgv_jysq_rmtz.setAdapter(myAdapterShequ);
        lv_dongtai.setAdapter(myAdapter);
        setListViewHeightBasedOnChildren(lv_dongtai);

    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        List<QiuyingDongtaiData> listdata;


        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            listdata = new ArrayList<>();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listdata.size();
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
                view = inflater.inflate(R.layout.qiuying_dt_item, null);
            }
            CircleImageView civ_qiuying_dt_item_headimgurl = view.findViewById(R.id.civ_qiuying_dt_item_headimgurl);
            TextView tv_qiuying_dt_item_nickname = view.findViewById(R.id.tv_qiuying_dt_item_nickname);
            TextView tv_qiuying_dt_item_add_time = view.findViewById(R.id.tv_qiuying_dt_item_add_time);
            TextView tv_qiuying_dt_item_title = view.findViewById(R.id.tv_qiuying_dt_item_title);
            LinearLayout ll_qiuying_dt_item_img = view.findViewById(R.id.ll_qiuying_dt_item_img);
            ImageView iv_qiuying_dt_item_img1 = view.findViewById(R.id.iv_qiuying_dt_item_img1);
            ImageView iv_qiuying_dt_item_img2 = view.findViewById(R.id.iv_qiuying_dt_item_img2);
            ImageView iv_qiuying_dt_item_img3 = view.findViewById(R.id.iv_qiuying_dt_item_img3);
            tv_qiuying_dt_item_nickname.setText(listdata.get(position).nickname);
            tv_qiuying_dt_item_add_time.setText(listdata.get(position).add_time);
            tv_qiuying_dt_item_title.setText(listdata.get(position).title);
            Glide.with(getActivity())
                    .load( Api.sUrl+ listdata.get(position).headimgurl)
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(civ_qiuying_dt_item_headimgurl);
            if (listdata.get(position).urlList.size() == 0) {
                ll_qiuying_dt_item_img.setVisibility(View.GONE);
            } else {
                ll_qiuying_dt_item_img.setVisibility(View.VISIBLE);

                if (listdata.get(position).urlList.size() >= 1) {
                    Glide.with(getActivity())
                            .load( Api.sUrl+ listdata.get(position).urlList.get(0))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_qiuying_dt_item_img1);
                }
                if (listdata.get(position).urlList.size() >= 2) {
                    Glide.with(getActivity())
                            .load( Api.sUrl+ listdata.get(position).urlList.get(1))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_qiuying_dt_item_img2);
                }
                if (listdata.get(position).urlList.size() >= 3) {
                    Glide.with(getActivity())
                            .load( Api.sUrl+ listdata.get(position).urlList.get(2))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_qiuying_dt_item_img3);
                }
            }
            return view;
        }
    }


    /**
     * listview自适应高度
     */
    public void setListViewHeightBasedOnChildren(ListView listView1) {
        BaseAdapter listAdapter = (BaseAdapter) listView1.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        //获取listView的宽度
        ViewGroup.LayoutParams params = listView1.getLayoutParams();
        int listViewWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int widthSpec = View.MeasureSpec.makeMeasureSpec(listViewWidth, View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView1);
            //给item的measure设置参数是listView的宽度就可以获取到真正每一个item的高度
            listItem.measure(widthSpec, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        params.height = totalHeight + (listView1.getDividerHeight() * (listAdapter.getCount() + 1));
        listView1.setLayoutParams(params);
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

    private void error(VolleyError error) {
        Log.e(TAG, error.getMessage(), error);
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
