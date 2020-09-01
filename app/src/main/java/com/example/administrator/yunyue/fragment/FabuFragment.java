package com.example.administrator.yunyue.fragment;

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
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.example.administrator.yunyue.yjdt_activity.FbxxActivity;
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
 * {@link FabuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FabuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FabuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = FabuFragment.class.getSimpleName();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private MyGridView mgv_kjfb;
    @BindView(R.id.srl_control)
    SmartRefreshLayout srlControl;

    public FabuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FabuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FabuFragment newInstance(String param1, String param2) {
        FabuFragment fragment = new FabuFragment();
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
        return inflater.inflate(R.layout.fragment_fabu, container, false);
    }

    public void onViewCreated(View view, Bundle saveInstancesState) {
        super.onViewCreated(view, saveInstancesState);
        mgv_kjfb = view.findViewById(R.id.mgv_kjfb);
        srlControl = view.findViewById(R.id.srl_control);
        smartRefresh();
        sTypeAllIndex();
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
            sTypeAllIndex();

            // srlControl.finishRefresh();//结束刷新

        });
        //上拉加载
/*        srlControl.setOnLoadmoreListener(refreshlayout -> {
            srlControl.setEnableLoadmore(true);//启用加载*/
        /**
         * 正常来说，应该在这里加载网络数据
         * 这里我们就使用模拟数据 MoreDatas() 来模拟我们加载出来的数据*/


        // srlControl.finishLoadmore();//结束加载
       /* });*/
    }


    /**
     * 方法名：sTypeAllIndex()
     * 功  能：分类接口
     * 参  数：appId
     */
    private void sTypeAllIndex() {
        String url = Api.sUrl + Api.sTypeAllIndex;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        JSONObject jsonObject = new JSONObject(params);
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        srlControl.finishRefresh();//结束刷新
                        hideDialogin();
                        String sDate = response.toString();
                        System.out.println(sDate);
                        try {
                            JSONObject jsonObject0 = new JSONObject(sDate);
                            String resultMsg = jsonObject0.getString("msg");
                            int resultCode = jsonObject0.getInt("code");
                            if (resultCode > 0) {
                                JSONArray resultJsonArray = jsonObject0.getJSONArray("data");
                                ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < resultJsonArray.length(); i++) {
                                    JSONObject jsonObject2 = (JSONObject) resultJsonArray.opt(i);
                                    String ItemId = jsonObject2.getString("id");
                                    String ItemName = jsonObject2.getString("name");
                                    String ItemType_img = jsonObject2.getString("type_img");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("type_img", ItemType_img);
                                    map.put("name", ItemName);
                                    map.put("id", ItemId);
                                    mylist.add(map);
                                }
                                gridviewdata(mylist);

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
                srlControl.finishRefresh();//结束刷新
                hideDialogin();
                //  Hint(error.getMessage(), HintDialog.ERROR);
                error(error);
            }
        });
        requestQueue.add(jsonRequest);
    }

    private void gridviewdata(final ArrayList<HashMap<String, String>> myList) {
        MyAdapter_Fenlei myAdapter_fenlei = new MyAdapter_Fenlei(getActivity());
/*        int num = 0;
        num = myList.size();
        if (num > 10) {
            num = 5;
        } else if (num > 5) {
            // num = num % 2;
            if (num % 2 == 0) {
                num = num / 2;
            } else {
                num = (num + 1) / 2;
            }
        }
        mgv_kjfb.setNumColumns(num);*/
        myAdapter_fenlei.arrlist = myList;

        mgv_kjfb.setAdapter(myAdapter_fenlei);

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
                    Intent intent = new Intent(getActivity(), FbxxActivity.class);
                    intent.putExtra("id", arrlist.get(position).get("id"));
                    startActivity(intent);
                }
            });
            ImageView iv_zhibo_fl_item = view.findViewById(R.id.iv_zhibo_fl_item);
            TextView tv_zhibo_fl_item = view.findViewById(R.id.tv_zhibo_fl_item);
            tv_zhibo_fl_item.setText(arrlist.get(position).get("name"));
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
