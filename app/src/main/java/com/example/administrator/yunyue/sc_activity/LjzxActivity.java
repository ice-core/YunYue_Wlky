package com.example.administrator.yunyue.sc_activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.data.LjzxData;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
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

public class LjzxActivity extends AppCompatActivity {
    private static final String TAG = LjzxActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_kajuan_back;
    /**
     * 全场通用
     */
    private LinearLayout ll_llzx_qcty;
    private TextView tv_llzx_qcty, tv_llzx_qcty_xhx;
    /**
     * 店铺专用
     */
    private LinearLayout ll_llzx_dpzy;
    private TextView tv_llzx_dpzy, tv_llzx_dpzy_xhx;
    @BindView(R.id.srl_control)
    SmartRefreshLayout srlControl;
    int iPage = 1;
    private MyAdapter myAdapter;
    /**
     * 列表
     */
    private MyGridView mgv_ljzx_liebiao;
    RequestQueue queue = null;
    private String sUser_id = "";
    private SharedPreferences pref;
    /**
     * 1为全场 2为商家
     */
    private String sType = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.theme);
        }
        setContentView(R.layout.activity_ljzx);
        queue = Volley.newRequestQueue(LjzxActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        ll_kajuan_back = findViewById(R.id.ll_kajuan_back);
        ll_kajuan_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mgv_ljzx_liebiao = findViewById(R.id.mgv_ljzx_liebiao);
        ll_llzx_qcty = findViewById(R.id.ll_llzx_qcty);
        tv_llzx_qcty = findViewById(R.id.tv_llzx_qcty);
        tv_llzx_qcty_xhx = findViewById(R.id.tv_llzx_qcty_xhx);
        ll_llzx_dpzy = findViewById(R.id.ll_llzx_dpzy);
        tv_llzx_dpzy = findViewById(R.id.tv_llzx_dpzy);
        tv_llzx_dpzy_xhx = findViewById(R.id.tv_llzx_dpzy_xhx);
        ll_llzx_qcty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sType = "1";
                tv_llzx_qcty.setTextColor(tv_llzx_qcty.getResources().getColor(R.color.theme));
                tv_llzx_qcty_xhx.setVisibility(View.VISIBLE);
                tv_llzx_dpzy.setTextColor(tv_llzx_dpzy.getResources().getColor(R.color.hei333333));
                tv_llzx_dpzy_xhx.setVisibility(View.GONE);
                hideDialogin();
                dialogin("");
                sAdvertisingIndex();
            }
        });
        ll_llzx_dpzy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sType = "2";
                tv_llzx_dpzy.setTextColor(tv_llzx_dpzy.getResources().getColor(R.color.theme));
                tv_llzx_dpzy_xhx.setVisibility(View.VISIBLE);
                tv_llzx_qcty.setTextColor(tv_llzx_qcty.getResources().getColor(R.color.hei333333));
                tv_llzx_qcty_xhx.setVisibility(View.GONE);
                hideDialogin();
                dialogin("");
                sAdvertisingIndex();

            }
        });
        srlControl = findViewById(R.id.srl_control);
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
            srlControl.finishLoadmore();//结束加载
        });
    }

    /**
     * 方法名：sCouponsList()
     * 功  能：优惠券列表
     * 参  数：appId
     * type--1为全场 2为商家
     */
    private void sAdvertisingIndex() {
        String url = Api.sUrl + Api.sCouponsList;
        RequestQueue requestQueue = Volley.newRequestQueue(LjzxActivity.this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("page", String.valueOf(iPage));
        params.put("user_id", sUser_id);
        params.put("type", sType);
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
                        List<LjzxData> mList = new ArrayList<>();
                        String sDate = response.toString();
                        System.out.println(sDate);
                        try {
                            JSONObject jsonObject0 = new JSONObject(sDate);
                            String resultMsg = jsonObject0.getString("msg");
                            int resultCode = jsonObject0.getInt("code");
                            if (resultCode > 0) {
                                JSONArray resultJsonArray = jsonObject0.getJSONArray("data");
                                for (int i = 0; i < resultJsonArray.length(); i++) {
                                    JSONObject jsonObject = resultJsonArray.getJSONObject(i);
                                    //优惠卷id
                                    String resultId = jsonObject.getString("id");
                                    //商家id
                                    String resultShangjia_id = jsonObject.getString("shangjia_id");
                                    //商家名称
                                    String resultShangjianame = jsonObject.getString("shangjia_name");
                                    //优惠券名称
                                    String resultName = jsonObject.getString("name");
                                    //用户是否领取 1为已领取
                                    String resultState = jsonObject.getString("state");
                                    LjzxData model = new LjzxData();
                                    //商品列表
                                    JSONArray jsonArrayCommodity = jsonObject.getJSONArray("commodity");
                                    for (int a = 0; a < jsonArrayCommodity.length(); a++) {
                                        JSONObject jsonObjectCommodity = jsonArrayCommodity.getJSONObject(a);
                                        HashMap<String, String> map = new HashMap<String, String>();
                                        map.put("id", jsonObjectCommodity.getString("id"));
                                        map.put("name", jsonObjectCommodity.getString("name"));
                                        map.put("logo", jsonObjectCommodity.getString("logo"));
                                        map.put("price", jsonObjectCommodity.getString("price"));
                                        model.goods.add(map);
                                    }
                                    model.id = resultId;
                                    model.shangjia_id = resultShangjia_id;
                                    model.shangjia_name = resultShangjianame;
                                    model.name = resultName;
                                    model.state = resultState;
                                    mList.add(model);
                                }
                                if (iPage == 1) {
                                    gridview(mList);
                                } else {
                                    if (mList.size() == 0) {
                                        iPage -= 1;
                                    } else {
                                        gridview1(mList);
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
     * 设置GirdView参数，绑定数据
     */
    private void gridview(List<LjzxData> mList) {
        myAdapter = new MyAdapter(LjzxActivity.this);
        myAdapter.List = mList;
        mgv_ljzx_liebiao.setAdapter(myAdapter);
    }

    private void gridview1(List<LjzxData> mList) {
        myAdapter.List.addAll(mList);
        mgv_ljzx_liebiao.setAdapter(myAdapter);
    }


    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        List<LjzxData> List;

        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            List = new ArrayList<>();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return List.size();
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
                view = inflater.inflate(R.layout.ljzx_item, null);
            }
            TextView tv_ljzx_item_shangjia_name = view.findViewById(R.id.tv_ljzx_item_shangjia_name);
            ImageView tv_ljzx_item_logo1 = view.findViewById(R.id.tv_ljzx_item_logo1);
            ImageView tv_ljzx_item_logo2 = view.findViewById(R.id.tv_ljzx_item_logo2);
            ImageView tv_ljzx_item_logo3 = view.findViewById(R.id.tv_ljzx_item_logo3);
            TextView tv_ljzx_item_price1 = view.findViewById(R.id.tv_ljzx_item_price1);
            TextView tv_ljzx_item_price2 = view.findViewById(R.id.tv_ljzx_item_price2);
            TextView tv_ljzx_item_price3 = view.findViewById(R.id.tv_ljzx_item_price3);
            TextView tv_ljzx_item_youhui = view.findViewById(R.id.tv_ljzx_item_youhui);
            TextView tv_ljzx_item_name = view.findViewById(R.id.tv_ljzx_item_name);
            TextView tv_ljzx_item_ljlq = view.findViewById(R.id.tv_ljzx_item_ljlq);
            tv_ljzx_item_shangjia_name.setText(List.get(position).shangjia_name);
            tv_ljzx_item_youhui.setText(List.get(position).youhui);
            tv_ljzx_item_name.setText(List.get(position).name);
            if (List.get(position).state.equals("1")) {
                tv_ljzx_item_ljlq.setBackgroundResource(R.drawable.bj_99999912);
                tv_ljzx_item_ljlq.setEnabled(false);
            } else {
                tv_ljzx_item_ljlq.setBackgroundResource(R.drawable.bj_lan12);
                tv_ljzx_item_ljlq.setEnabled(true);
            }
            if (List.get(position).goods.size() > 0) {
                Glide.with(LjzxActivity.this)
                        .load( Api.sUrl+ List.get(position).goods.get(0).get("logo"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(tv_ljzx_item_logo1);
                tv_ljzx_item_price1.setText(List.get(position).goods.get(0).get("price"));
            }
            if (List.get(position).goods.size() > 1) {
                Glide.with(LjzxActivity.this)
                        .load( Api.sUrl+ List.get(position).goods.get(1).get("logo"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(tv_ljzx_item_logo2);
                tv_ljzx_item_price2.setText(List.get(position).goods.get(1).get("price"));
            }
            if (List.get(position).goods.size() > 2) {
                Glide.with(LjzxActivity.this)
                        .load( Api.sUrl+ List.get(position).goods.get(2).get("logo"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(tv_ljzx_item_logo3);
                tv_ljzx_item_price3.setText(List.get(position).goods.get(2).get("price"));
            }
            tv_ljzx_item_ljlq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideDialogin();
                    dialogin("");
                    Lqyhj(List.get(position).id, List.get(position).shangjia_id);
                }
            });
            return view;
        }
    }

    private void Lqyhj(final String hong_id, String shangjia_id) {
        String url = Api.sUrl + "Api/Good/getcoupons/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/hong_id/" + hong_id + "/shangjia_id/" + shangjia_id;
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

                        Hint(resultMsg, HintDialog.SUCCESS);
                        sAdvertisingIndex();
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
        loadingDialog = new LoadingDialog(LjzxActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(LjzxActivity.this, R.style.dialog, sHint, type, true).show();
    }
}
