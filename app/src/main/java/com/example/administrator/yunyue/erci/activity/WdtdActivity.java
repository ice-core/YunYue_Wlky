package com.example.administrator.yunyue.erci.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.administrator.yunyue.data.JkyTzData;
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

import cn.lemon.multi.MultiView;

public class WdtdActivity extends AppCompatActivity {
    private static final String TAG = WdtdActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_wdtd_back;
    private SmartRefreshLayout srl_control;
    private RecyclerView rv_wdtd_lb;
    private RecycleAdapterDome recycleAdapterDome;
    private int iPage = 1;
    private String sUser_id;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_wdtd);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        ll_wdtd_back = findViewById(R.id.ll_wdtd_back);
        ll_wdtd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        srl_control = findViewById(R.id.srl_control);
        rv_wdtd_lb = findViewById(R.id.rv_wdtd_lb);
        smartRefresh();
        iPage = 1;
        sMyTeam();
    }

    //监听下拉和上拉状态
    public void smartRefresh() {
        //下拉刷新
        srl_control.setOnRefreshListener(refreshlayout -> {
            srl_control.setEnableRefresh(true);//启用刷新
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 Data() 来模拟我们刷新出来的数据
             */

            iPage = 1;
            sMyTeam();
        });
        //上拉加载
        srl_control.setOnLoadmoreListener(refreshlayout -> {
            srl_control.setEnableLoadmore(true);//启用加载
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 MoreDatas() 来模拟我们加载出来的数据
             */
            //  refreshAdapter.loadMore(MoreDatas());
            iPage += 1;
            sMyTeam();

        });
    }

    /**
     * 方法名：sMyTeam()
     * 功  能：我的团队列表接口
     * 参  数：appId
     */
    private void sMyTeam() {
        String url = Api.sUrl + Api.sMyTeam;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("page", String.valueOf(iPage));
        JSONObject jsonObject = new JSONObject(params);
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (iPage == 1) {
                            srl_control.finishRefresh();//结束刷新
                        } else {
                            srl_control.finishLoadmore();//结束加载
                        }
                        hideDialogin();
                        String sDate = response.toString();
                        System.out.println(sDate);
                        try {
                            JSONObject jsonObject0 = new JSONObject(sDate);
                            String resultMsg = jsonObject0.getString("msg");
                            ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                            List<JkyTzData> mList = new ArrayList<>();
                            int resultCode = jsonObject0.getInt("code");
                            if (resultCode > 0) {
                                JSONArray jsonArray_data = jsonObject0.getJSONArray("data");

                                for (int i = 0; i < jsonArray_data.length(); i++) {
                                    JSONObject jsonObject2 = (JSONObject) jsonArray_data.opt(i);
                                    //用户id
                                    String ItemId = jsonObject2.getString("id");
                                    //用户昵称
                                    String ItemNickname = jsonObject2.getString("nickname");
                                    //用户头像
                                    String ItemHeadimgurl = jsonObject2.getString("headimgurl");
                                    //注册时间
                                    String ItemAddtime = jsonObject2.getString("addtime");
                                    //联系电话
                                    String ItemMobile = jsonObject2.getString("mobile");
                                    //会员状态
                                    String ItemIs_vip = jsonObject2.getString("is_vip");
                                    //会员级数
                                    String ItemRank = jsonObject2.getString("rank");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("id", ItemId);
                                    map.put("nickname", ItemNickname);
                                    map.put("headimgurl", ItemHeadimgurl);
                                    map.put("addtime", ItemAddtime);
                                    map.put("mobile", ItemMobile);
                                    map.put("is_vip", ItemIs_vip);
                                    map.put("rank", ItemRank);
                                    mylist.add(map);
                                }

                                if (iPage == 1) {
                                    Rv_data(mylist);
                                } else {
                                    if (mylist.size() == 0) {
                                        iPage -= 1;
                                    } else {
                                        Rv_data1(mylist);
                                    }
                                }
                                // Hint(resultMsg, HintDialog.SUCCESS);
                            } else {

                                Hint(resultMsg, HintDialog.ERROR);
                                if (iPage == 1) {
                                    Rv_data(mylist);
                                } else {
                                    if (mylist.size() == 0) {
                                        iPage -= 1;
                                    } else {
                                        Rv_data1(mylist);
                                    }
                                }
                            }
                        } catch (
                                JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "response -> " + response.toString());
                        //   Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (iPage == 1) {
                    srl_control.finishRefresh();//结束刷新
                } else {
                    srl_control.finishLoadmore();//结束加载
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
    private void Rv_data(ArrayList<HashMap<String, String>> mylist) {
        recycleAdapterDome = new RecycleAdapterDome(WdtdActivity.this, mylist);
        recycleAdapterDome.setHasStableIds(true);
        rv_wdtd_lb.setAdapter(recycleAdapterDome);
        LinearLayoutManager manager = new LinearLayoutManager(WdtdActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        //RecyclerView.LayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        //GridLayoutManager manager1 = new GridLayoutManager(context,2);
        //manager1.setOrientation(GridLayoutManager.VERTICAL);
        //StaggeredGridLayoutManager manager2 = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rv_wdtd_lb.setLayoutManager(manager);
    }

    private void Rv_data1(ArrayList<HashMap<String, String>> mylist) {
        recycleAdapterDome.arrlist.addAll(mylist);
        recycleAdapterDome.notifyDataSetChanged();
    }

    public class RecycleAdapterDome extends RecyclerView.Adapter<RecycleAdapterDome.MyViewHolder> {
        private Context context;

        private View inflater;
        private ArrayList<HashMap<String, String>> arrlist;

        //构造方法，传入数据
        public RecycleAdapterDome(Context context, ArrayList<HashMap<String, String>> arrlist) {
            this.context = context;
            this.arrlist = arrlist;
        }

        @Override
        public RecycleAdapterDome.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //创建ViewHolder，返回每一项的布局
            inflater = LayoutInflater.from(context).inflate(R.layout.wdtd_item, parent, false);

            RecycleAdapterDome.MyViewHolder myViewHolder = new RecycleAdapterDome.MyViewHolder(inflater);
            return myViewHolder;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public void onBindViewHolder(RecycleAdapterDome.MyViewHolder holder, int position) {
            //将数据和控件绑定

            Glide.with(WdtdActivity.this)
                    .load( Api.sUrl+arrlist.get(position).get("headimgurl"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(holder.civ_wdtd_item);
            holder.tv_wdtd_item_nickname.setText(arrlist.get(position).get("nickname"));
            holder.tv_wdtd_item_is_vip.setText(arrlist.get(position).get("is_vip"));
            holder.tv_wdtd_item_rank.setText(arrlist.get(position).get("rank"));
            holder.tv_wdtd_item_mobile.setText(arrlist.get(position).get("mobile"));
            holder.tv_wdtd_item_addtime.setText(arrlist.get(position).get("addtime"));

        }

        @Override
        public int getItemCount() {
            //返回Item总条数
            return arrlist.size();
        }

        //内部类，绑定控件
        class MyViewHolder extends RecyclerView.ViewHolder {
            CircleImageView civ_wdtd_item;
            TextView tv_wdtd_item_nickname;
            TextView tv_wdtd_item_is_vip;
            TextView tv_wdtd_item_rank;
            TextView tv_wdtd_item_mobile;
            TextView tv_wdtd_item_addtime;

            public MyViewHolder(View itemView) {
                super(itemView);
                civ_wdtd_item = itemView.findViewById(R.id.civ_wdtd_item);
                tv_wdtd_item_nickname = itemView.findViewById(R.id.tv_wdtd_item_nickname);
                tv_wdtd_item_is_vip = itemView.findViewById(R.id.tv_wdtd_item_is_vip);
                tv_wdtd_item_rank = itemView.findViewById(R.id.tv_wdtd_item_rank);
                tv_wdtd_item_mobile = itemView.findViewById(R.id.tv_wdtd_item_mobile);
                tv_wdtd_item_addtime = itemView.findViewById(R.id.tv_wdtd_item_addtime);
            }
        }

    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(WdtdActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(WdtdActivity.this, R.style.dialog, sHint, type, true).show();
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
}
