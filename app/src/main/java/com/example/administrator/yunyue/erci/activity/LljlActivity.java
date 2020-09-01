package com.example.administrator.yunyue.erci.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
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
import com.example.administrator.yunyue.sc_activity.SplbActivity;
import com.example.administrator.yunyue.sc_activity.SpxqActivity;
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

public class LljlActivity extends AppCompatActivity {
    private static final String TAG = LljlActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_lljl_back;


    private GridView gv_lljl;
    private RecyclerView rv_lljl_lb;
    private String[] tab = {"商城", "养老院"};
    private MyAdapter myAdapter;
    private int iPosition = 0;

    private RecycleAdapterDome recycleAdapterDome;
    private int iPage = 1;
    private String sUser_id = "";
    private SharedPreferences pref;
    SmartRefreshLayout srl_lljl;
    RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_lljl);
        queue = Volley.newRequestQueue(LljlActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(LljlActivity.this);
        sUser_id = pref.getString("user_id", "");
        ll_lljl_back = findViewById(R.id.ll_lljl_back);
        ll_lljl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gv_lljl = findViewById(R.id.gv_lljl);
        rv_lljl_lb = findViewById(R.id.rv_lljl_lb);
        srl_lljl = findViewById(R.id.srl_lljl);
        setGridView();
        smartRefresh();
        iPage = 1;
        sLookList();
    }

    private void setGridView() {
        myAdapter = new MyAdapter(LljlActivity.this);
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < tab.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemName", tab[i]);
            map.put("ItemId", String.valueOf(i));
            mylist.add(map);
        }
        myAdapter.arrlist = mylist;
        gv_lljl.setAdapter(myAdapter);
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
                view = inflater.inflate(R.layout.zx_fl_item, null);
            }
            LinearLayout ll_fl_item = view.findViewById(R.id.ll_fl_item);
            TextView zx_fl_item_name = view.findViewById(R.id.zx_fl_item_name);
            TextView zx_fl_item = view.findViewById(R.id.zx_fl_item);
            zx_fl_item_name.setText(arrlist.get(position).get("ItemName"));
            if (iPosition == Integer.valueOf(arrlist.get(position).get("ItemId"))) {
                zx_fl_item_name.setTextColor(zx_fl_item_name.getResources().getColor(R.color.theme));
                zx_fl_item.setBackgroundColor(ContextCompat.getColor(LljlActivity.this, R.color.theme));
            } else {
                zx_fl_item_name.setTextColor(zx_fl_item_name.getResources().getColor(R.color.hui999999));
                zx_fl_item.setBackgroundColor(ContextCompat.getColor(LljlActivity.this, R.color.touming));
            }
            ll_fl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iPosition = position;
                    notifyDataSetChanged();
                    iPage = 1;
                    sLookList();
                }
            });
            return view;
        }
    }

    //监听下拉和上拉状态
    public void smartRefresh() {
        //下拉刷新
        srl_lljl.setOnRefreshListener(refreshlayout -> {
            srl_lljl.setEnableRefresh(true);//启用刷新
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 Data() 来模拟我们刷新出来的数据
             */

            iPage = 1;
            sLookList();

        });
        //上拉加载
        srl_lljl.setOnLoadmoreListener(refreshlayout -> {
            srl_lljl.setEnableLoadmore(true);//启用加载
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 MoreDatas() 来模拟我们加载出来的数据
             */
            //  refreshAdapter.loadMore(MoreDatas());
            iPage += 1;
            sLookList();

        });
    }

    /**
     * 方法名：sLookList()
     * 功  能：浏览记录接口
     * 参  数：appId
     */
    private void sLookList() {
        String url = Api.sUrl + Api.sLookList;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("type", String.valueOf(iPosition + 1));
        JSONObject jsonObject = new JSONObject(params);
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (iPage == 1) {
                            srl_lljl.finishRefresh();//结束刷新
                        } else {
                            srl_lljl.finishLoadmore();//结束加载
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
                                    //标题
                                    String ItemName = jsonObject2.getString("name");
                                    //图片
                                    String ItemLogo = jsonObject2.getString("logo");
                                    //价格
                                    String ItemPrice = jsonObject2.getString("price");
                                    //时间
                                    String ItemTime = jsonObject2.getString("time");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("id", ItemId);
                                    map.put("name", ItemName);
                                    map.put("logo", ItemLogo);
                                    map.put("price", ItemPrice);
                                    map.put("time", ItemTime);
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
                    srl_lljl.finishRefresh();//结束刷新
                } else {
                    srl_lljl.finishLoadmore();//结束加载
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
        recycleAdapterDome = new RecycleAdapterDome(LljlActivity.this, mylist);
        recycleAdapterDome.setHasStableIds(true);
        rv_lljl_lb.setAdapter(recycleAdapterDome);
        LinearLayoutManager manager = new LinearLayoutManager(LljlActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        //RecyclerView.LayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        //GridLayoutManager manager1 = new GridLayoutManager(context,2);
        //manager1.setOrientation(GridLayoutManager.VERTICAL);
        //StaggeredGridLayoutManager manager2 = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rv_lljl_lb.setLayoutManager(manager);
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
            inflater = LayoutInflater.from(context).inflate(R.layout.lljl_item, parent, false);
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
            holder.tv_lljl_item_time.setText(arrlist.get(position).get("time"));
            holder.tv_lljl_item_name.setText(arrlist.get(position).get("name"));
            if (iPosition == 0) {
                holder.tv_lljl_item_price.setText("￥" + arrlist.get(position).get("price"));
            } else {
                holder.tv_lljl_item_price.setText(arrlist.get(position).get("price") + "工分");
            }
            Glide.with(LljlActivity.this)
                    .load( Api.sUrl+arrlist.get(position).get("logo"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(holder.iv_lljl_item_logo);
            holder.ll_lljl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iPosition == 1) {
                        Intent intent = new Intent(LljlActivity.this, YlyxqActivity.class);
                        intent.putExtra("id", arrlist.get(position).get("id"));
                        startActivity(intent);
                    } else if (iPosition == 0) {
                        jianjei(arrlist.get(position).get("id"));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            //返回Item总条数

            return arrlist.size();
        }

        //内部类，绑定控件
        class MyViewHolder extends RecyclerView.ViewHolder {
            LinearLayout ll_lljl_item;
            TextView tv_lljl_item_time;
            ImageView iv_lljl_item_logo;
            TextView tv_lljl_item_name;
            TextView tv_lljl_item_price;

            public MyViewHolder(View itemView) {
                super(itemView);
                ll_lljl_item = itemView.findViewById(R.id.ll_lljl_item);
                tv_lljl_item_time = itemView.findViewById(R.id.tv_lljl_item_time);
                iv_lljl_item_logo = itemView.findViewById(R.id.iv_lljl_item_logo);
                tv_lljl_item_name = itemView.findViewById(R.id.tv_lljl_item_name);
                tv_lljl_item_price = itemView.findViewById(R.id.tv_lljl_item_price);

            }
        }

    }

    private void jianjei(final String pid) {
        hideDialogin();
        dialogin("");
        String url = Api.sUrl + "Api/Good/gooddetail/appId/" + Api.sApp_Id
                + "/good_id/" + pid + "/user_id/" + sUser_id;
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
                        Intent intent1 = new Intent(LljlActivity.this, SpxqActivity.class);
                        intent1.putExtra("goods_id", pid);
                        intent1.putExtra("data", sDate);
                        startActivity(intent1);
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

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(LljlActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(LljlActivity.this, R.style.dialog, sHint, type, true).show();
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
