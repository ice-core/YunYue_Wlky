package com.example.administrator.yunyue.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
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
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.Jjcc;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc_activity.GuanggaoActivity;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.example.administrator.yunyue.yjdt_activity.FenleiLiebiaoActivity;
import com.example.administrator.yunyue.yjdt_activity.GuanggaoxiangqingActivity;
import com.loonggg.rvbanner.lib.RecyclerViewBanner;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static io.rong.imkit.utilities.RongUtils.getScreenWidth;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GuanggaolanmuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GuanggaolanmuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuanggaolanmuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerViewBanner rv_banner_gglm;
    private MyGridView mgv_gglm_fl;
    private static final String TAG = GuanggaolanmuFragment.class.getSimpleName();

    List<String> catList1_ad_id = new ArrayList<String>();//图片id
    List<String> catList1_ad_link = new ArrayList<String>();//图片链接
    private MyAdapter_AdvertisingIndex myAdapter_advertisingIndex;
    @BindView(R.id.srl_control)
    SmartRefreshLayout srlControl;
    int iPage = 1;
    /**
     * 广告栏目
     */
    private MyGridView mgv_gglm_gglm;

    public GuanggaolanmuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuanggaolanmuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GuanggaolanmuFragment newInstance(String param1, String param2) {
        GuanggaolanmuFragment fragment = new GuanggaolanmuFragment();
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
        return inflater.inflate(R.layout.fragment_guanggaolanmu, container, false);
    }

    /**
     *  * 方法名：initView()
     *  * 功    能：控件初始化
     *  * 参    数：无
     *  * 返回值：无
     */
    public void onViewCreated(View view, Bundle saveInstancesState) {
        super.onViewCreated(view, saveInstancesState);
        rv_banner_gglm = view.findViewById(R.id.rv_banner_gglm);
        mgv_gglm_fl = view.findViewById(R.id.mgv_gglm_fl);
        srlControl = view.findViewById(R.id.srl_control);
        mgv_gglm_gglm = view.findViewById(R.id.mgv_gglm_gglm);
        sAdvertisingApp();
        sAdvertisingIndex();
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
            iPage = 1;
            sAdvertisingApp();
            sAdvertisingIndex();
            // srlControl.finishRefresh();//结束刷新

        });
        //上拉加载
        srlControl.setOnLoadmoreListener(refreshlayout -> {
            srlControl.setEnableLoadmore(true);//启用加载
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 MoreDatas() 来模拟我们加载出来的数据*/

            iPage += 1;
            sAdvertisingIndex();
          //  srlControl.finishLoadmore();//结束加载
        });
    }

    /**
     * 方法名：sAdvertisingApp()
     * 功  能：广告接口
     * 参  数：appId
     */
    private void sAdvertisingApp() {
        String url = Api.sUrl + Api.sAdvertisingApp;
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

                                JSONObject jsonObject1 = new JSONObject(sDate.toString()).getJSONObject("data");
                                JSONArray jsonArray = jsonObject1.getJSONArray("type");
                                ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
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
                                JSONArray jsonArray_lun = jsonObject1.getJSONArray("banner");
                                final List<GuanggaolanmuFragment.Banner> banners = new ArrayList<>();
                                for (int i = 0; i < jsonArray_lun.length(); i++) {
                                    JSONObject jsonObject5 = (JSONObject) jsonArray_lun.opt(i);
                                    catList1_ad_link.add(jsonObject5.getString("link"));
                                    catList1_ad_id.add(jsonObject5.getString("id"));

                                    banners.add(new GuanggaolanmuFragment.Banner(jsonObject5.getString("logo")));
                                }
                                rv_banner_gglm.setRvBannerData(banners);
                                rv_banner_gglm.setOnSwitchRvBannerListener(new RecyclerViewBanner.OnSwitchRvBannerListener() {
                                    @Override
                                    public void switchBanner(int position, AppCompatImageView bannerView) {
                         /*       Glide.with(bannerView.getContext())
                                        .load( Api.sUrl+banners.get(position).getUrl())
                                        .asBitmap().dontAnimate()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .into(bannerView);*/
                                        int screenwidth = getScreenWidth(); //获取屏幕的宽度
                                        ViewGroup.LayoutParams layoutParams = rv_banner_gglm.getLayoutParams();//获取banner组件的参数

                                        Glide.with(bannerView.getContext())
                                                .load( Api.sUrl+banners.get(position).getUrl())
                                                .asBitmap()
                                                .placeholder(R.mipmap.error)
                                                .error(R.mipmap.error)
                                                .skipMemoryCache(true)
                                                .into(new SimpleTarget<Bitmap>() {
                                                    @Override
                                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                                        bannerView.setImageBitmap(resource);
                                                        //比例
                                                        double bl = 0;
                                                        bl = Jjcc.div(Double.valueOf(resource.getHeight()), Double.valueOf(resource.getWidth()));
                                                        //比例
                                                        double gao = 0;
                                                        gao = Jjcc.mul(Double.valueOf(screenwidth), bl);
                                                        int iGao = (new Double(gao)).intValue();
                                                        layoutParams.height = iGao; //这里设置轮播图的长度等于宽度
                                                        rv_banner_gglm.setLayoutParams(layoutParams); //设置参数
                                                    }
                                                });
                                    }
                                });
                                rv_banner_gglm.setOnRvBannerClickListener(new RecyclerViewBanner.OnRvBannerClickListener() {
                                    @Override
                                    public void onClick(int position) {
                                        dialogin("");

                                        Intent intent = new Intent(getActivity(), GuanggaoActivity.class);
                                        intent.putExtra("link", catList1_ad_link.get(position));
                                        startActivity(intent);
                                    }
                                });
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
     * 方法名：sAdvertisingIndex()
     * 功  能：广告栏目接口
     * 参  数：appId
     */
    private void sAdvertisingIndex() {
        String url = Api.sUrl + Api.sAdvertisingIndex;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("page", String.valueOf(iPage));
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
                                JSONObject jsonObject1 = new JSONObject(sDate.toString()).getJSONObject("data");
                                JSONArray jsonArray = jsonObject1.getJSONArray("advertising");
                                ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
                                    String ItemId = jsonObject2.getString("id");
                                    String ItemLogo = jsonObject2.getString("logo");
                                    String ItemTitle = jsonObject2.getString("title");
                                    String ItemEvery_share = jsonObject2.getString("every_share");
                                    String ItemPrice = jsonObject2.getString("price");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("id", ItemId);
                                    map.put("logo", ItemLogo);
                                    map.put("title", ItemTitle);
                                    map.put("every_share", ItemEvery_share);
                                    map.put("price", ItemPrice);
                                    mylist.add(map);
                                }
                                if (iPage == 1) {
                                    gridviewAdvertisingIndex(mylist);
                                } else {
                                    if (mylist.size() == 0) {
                                        iPage -= 1;
                                    } else {
                                        gridviewAdvertisingIndex1(mylist);
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


    private class Banner {

        String url;

        public Banner(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

    private void gridviewdata(final ArrayList<HashMap<String, String>> myList) {
        MyAdapter_Fenlei myAdapter_fenlei = new MyAdapter_Fenlei(getActivity());
        int num = 0;
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
        myAdapter_fenlei.arrlist = myList;
        mgv_gglm_fl.setNumColumns(num);
        mgv_gglm_fl.setAdapter(myAdapter_fenlei);

    }

    private void gridviewAdvertisingIndex(final ArrayList<HashMap<String, String>> myList) {
        myAdapter_advertisingIndex = new MyAdapter_AdvertisingIndex(getActivity());
        myAdapter_advertisingIndex.arrlist = myList;
        mgv_gglm_gglm.setAdapter(myAdapter_advertisingIndex);
    }

    private void gridviewAdvertisingIndex1(final ArrayList<HashMap<String, String>> myList) {
        myAdapter_advertisingIndex.arrlist.addAll(myList);
        mgv_gglm_gglm.setAdapter(myAdapter_advertisingIndex);
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

                    } else {
                        Intent intent = new Intent(getActivity(), FenleiLiebiaoActivity.class);
                        intent.putExtra("id", arrlist.get(position).get("id"));
                        intent.putExtra("name", arrlist.get(position).get("name"));
                        startActivity(intent);
                    }
                }
            });
            ImageView iv_zhibo_fl_item = view.findViewById(R.id.iv_zhibo_fl_item);
            TextView tv_zhibo_fl_item = view.findViewById(R.id.tv_zhibo_fl_item);
            tv_zhibo_fl_item.setText(arrlist.get(position).get("name"));
            Glide.with(getActivity())
                    .load( Api.sUrl+ arrlist.get(position).get("type_img"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_zhibo_fl_item);

            return view;
        }
    }

    private class MyAdapter_AdvertisingIndex extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;


        public MyAdapter_AdvertisingIndex(Context context) {
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
                view = inflater.inflate(R.layout.gglm_item, null);
            }     /*  map.put("id", ItemId);
            map.put("logo", ItemLogo);
            map.put("title", ItemTitle);
            map.put("every_share", ItemEvery_share);
            map.put("price", ItemPrice);*/

            LinearLayout ll_gglm_item = view.findViewById(R.id.ll_gglm_item);
            ll_gglm_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), GuanggaoxiangqingActivity.class);
                    intent.putExtra("id", arrlist.get(position).get("id"));
                    startActivity(intent);
                }
            });
            ImageView iv_gglm_item_logo = view.findViewById(R.id.iv_gglm_item_logo);
            TextView tv_gglm_item_title = view.findViewById(R.id.tv_gglm_item_title);
            TextView tv_gglm_item_price = view.findViewById(R.id.tv_gglm_item_price);
            tv_gglm_item_title.setText(arrlist.get(position).get("title"));
            tv_gglm_item_price.setText(arrlist.get(position).get("price") + "元/人");
            Glide.with(getActivity())
                    .load( Api.sUrl+arrlist.get(position).get("logo"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_gglm_item_logo);

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
