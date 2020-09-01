package com.example.administrator.yunyue.yjdt_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.example.administrator.yunyue.fragment.QiuyingFragment;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QiuyingItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QiuyingItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QiuyingItemFragment extends Fragment {
    private static final String TAG = QiuyingItemFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    /**
     * 列表
     */
    private MyGridView mgv_qiuying_item;
    private SharedPreferences pref;
    private String sUser_id = "";

    @BindView(R.id.srl_control)
    SmartRefreshLayout srlControl;
    int iPage = 1;
    private MyAdapter myAdapter;
    //Fragment的View加载完毕的标记
    private boolean isViewCreated;
    //Fragment对用户可见的标记
    private boolean isUIVisible;

    public QiuyingItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QiuyingItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QiuyingItemFragment newInstance(String param1, String param2) {
        QiuyingItemFragment fragment = new QiuyingItemFragment();
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
        return inflater.inflate(R.layout.fragment_qiuying_item, container, false);
    }

    public void onViewCreated(View view, Bundle saveInstancesState) {
        super.onViewCreated(view, saveInstancesState);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sUser_id = pref.getString("user_id", "");
        srlControl = view.findViewById(R.id.srl_control);
        mgv_qiuying_item = view.findViewById(R.id.mgv_qiuying_item);
        isViewCreated = true;
        smartRefresh();
        if (QiuyingFragment.iPosition == 0) {
            int iPage = 1;
            sFocusIndex();
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
            iPage = 1;
            sFocusIndex();

        }

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
            iPage = 1;
            sFocusIndex();
            // srlControl.finishRefresh();//结束刷新

        });
        //上拉加载
        srlControl.setOnLoadmoreListener(refreshlayout -> {
            srlControl.setEnableLoadmore(true);//启用加载
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 MoreDatas() 来模拟我们加载出来的数据*/
            iPage += 1;
            sFocusIndex();
            srlControl.finishLoadmore();//结束加载
        });
    }

    /**
     * 方法名：sFocusIndex()
     * 功  能：通讯录接口
     * 参  数：appId
     */
    private void sFocusIndex() {
        String url = Api.sUrl + Api.sFocusIndex;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("page", String.valueOf(iPage));
        params.put("type", String.valueOf(QiuyingFragment.iPosition + 1));
        params.put("address", QiuyingFragment.address);
        JSONObject jsonObject = new JSONObject(params);
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (iPage == 1) {
                            srlControl.finishRefresh();//结束刷新
                        } else {
                            srlControl.finishLoadmore();//结束加载
                        }
                        hideDialogin();
                        String sDate = response.toString();
                        System.out.println(sDate);
                        try {
                            JSONObject jsonObject0 = new JSONObject(sDate);
                            String resultMsg = jsonObject0.getString("msg");
                            int resultCode = jsonObject0.getInt("code");
                            if (resultCode > 0) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
                                    //用户id
                                    String ItemId = jsonObject2.getString("id");
                                    //用户名称
                                    String ItemNickname = jsonObject2.getString("nickname");
                                    //头像
                                    String ItemHeadimgurl = jsonObject2.getString("headimgurl");
                                    //服务内容
                                    String ItemSaas = jsonObject2.getString("saas");
                                    //为已关注 0为未关注
                                    String ItemFocus = jsonObject2.getString("focus");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("id", ItemId);
                                    map.put("nickname", ItemNickname);
                                    map.put("headimgurl", ItemHeadimgurl);
                                    map.put("saas", ItemSaas);
                                    map.put("focus", ItemFocus);
                                    mylist.add(map);
                                }
                                if (iPage == 1) {
                                    gridview(mylist);
                                } else {
                                    if (mylist.size() == 0) {
                                        iPage -= 1;
                                    } else {
                                        gridview1(mylist);
                                    }
                                }

                            } else {
                                hideDialogin();
                                Hint(resultMsg, HintDialog.ERROR);
                                //   Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "response -> " + response.toString());
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


    /**
     * 方法名：sFocusType()
     * 功  能：关注接口
     * 参  数：appId
     */
    private void sFocusType(String sFocus_id, String sType) {
        String url = Api.sUrl + Api.sFocusType;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("focus_id", sFocus_id);
        params.put("type", sType);

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
                            JSONObject jsonObject0 = new JSONObject(sDate);
                            String resultMsg = jsonObject0.getString("msg");
                            int resultCode = jsonObject0.getInt("code");
                            if (resultCode > 0) {
                                sFocusIndex();
                            } else {
                                hideDialogin();
                                Hint(resultMsg, HintDialog.ERROR);
                                //   Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "response -> " + response.toString());
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

    private void gridview(final ArrayList<HashMap<String, String>> myList) {
        myAdapter = new MyAdapter(getActivity());
        myAdapter.arrlist = myList;
        mgv_qiuying_item.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }

    private void gridview1(final ArrayList<HashMap<String, String>> myList) {
        myAdapter.arrlist.addAll(myList);
        mgv_qiuying_item.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        ArrayList<HashMap<String, String>> arrlist;

        public MyAdapter(Context context) {
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
                view = inflater.inflate(R.layout.yjdt_txl_item, null);
            }

            CardView cv_yjdt_txl_item = view.findViewById(R.id.cv_yjdt_txl_item);

            ImageView iv_yjdt_txl_headimgurl = view.findViewById(R.id.iv_yjdt_txl_headimgurl);
            TextView tv_yjdt_txl_nickname = view.findViewById(R.id.tv_yjdt_txl_nickname);
            tv_yjdt_txl_nickname.setText(arrlist.get(position).get("nickname"));

            // TextView tv_yjdt_txl_time = view.findViewById(R.id.tv_yjdt_txl_time);
            TextView tv_yjdt_txl_saas = view.findViewById(R.id.tv_yjdt_txl_saas);
            tv_yjdt_txl_saas.setText(arrlist.get(position).get("saas"));

            TextView tv_yjdt_txl_gz = view.findViewById(R.id.tv_yjdt_txl_gz);
            if (arrlist.get(position).get("focus").equals("1")) {
                tv_yjdt_txl_gz.setBackgroundResource(R.drawable.bj_4_666666);
                tv_yjdt_txl_gz.setText("取消关注");
            } else {
                tv_yjdt_txl_gz.setBackgroundResource(R.drawable.bj_theme_4);
                tv_yjdt_txl_gz.setText("关注");
            }
            tv_yjdt_txl_gz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            Glide.with(getActivity())
                    .load( Api.sUrl+ arrlist.get(position).get("headimgurl"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_yjdt_txl_headimgurl);
            cv_yjdt_txl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), BjzlActivity.class);
                    intent.putExtra("id", arrlist.get(position).get("id"));
                    startActivity(intent);
                }
            });
            tv_yjdt_txl_gz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sType = "";
                    if (arrlist.get(position).get("focus").equals("1")) {
                        sType = "2";
                    } else {
                        sType = "1";
                    }
                    hideDialogin();
                    dialogin("");
                    sFocusType(arrlist.get(position).get("id"), sType);
                }
            });
            return view;
        }
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
