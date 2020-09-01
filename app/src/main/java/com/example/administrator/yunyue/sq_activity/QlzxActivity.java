package com.example.administrator.yunyue.sq_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Base64;
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
import com.example.administrator.yunyue.Im.ConversationActivity;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.erci.activity.CwhyActivity;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

public class QlzxActivity extends AppCompatActivity {
    /**
     * 返回
     */
    private LinearLayout ll_qlzx_back;
    /**
     * 发起群聊
     */
    private TextView tv_qlzx_fqql;

    /**
     * 列表
     */
    private PullToRefreshGridView pull_refresh_grid_qlzx;
    RequestQueue queue = null;
    private SharedPreferences pref;
    private String sUser_id = "";
    private String sIs_vip = "";
    public static String community_id = "";
    int iPage = 1;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white);
        }
        setContentView(R.layout.activity_qlzx);
        int iPage = 1;
        queue = Volley.newRequestQueue(QlzxActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        sIs_vip = pref.getString("is_vip", "");
        ll_qlzx_back = findViewById(R.id.ll_qlzx_back);
        tv_qlzx_fqql = findViewById(R.id.tv_qlzx_fqql);
        ll_qlzx_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_qlzx_fqql.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    Intent intent = new Intent(QlzxActivity.this, CjqlActivity.class);
                    startActivity(intent);

            }
        });
        pull_refresh_grid_qlzx = findViewById(R.id.pull_refresh_grid_qlzx);
        pull_refresh_grid_qlzx.setMode(PullToRefreshBase.Mode.BOTH);
        pull_refresh_grid_qlzx.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            // 下拉刷新加载
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                Log.e("TAG", "onPullDownToRefresh"); // Do work to
                // 刷新时间
                String label = DateUtils.formatDateTime(
                        getApplicationContext(),
                        System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                // AsyncTask异步交互加载数据

                query();
            }

            // 上拉加载跟多
            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                Log.e("TAG", "onPullUpToRefresh"); // Do work to refresh
                query();

            }
        });
        query();
    }

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initview();
    }

    private void initview() {
        query();
    }

    private void query() {
        String url = Api.sUrl + "Community/Api/groupList/appId/" + Api.sApp_Id + "/user_id/" + sUser_id;
        if (!community_id.equals("")) {
            url = url + "/community_id/" + community_id;
        }
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
                    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                    if (resultCode > 0) {
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            String resultId = jsonObject.getString("id");
                            String resultLogo = jsonObject.getString("logo");
                            String resultName = jsonObject.getString("name");
                            String resultMembers = jsonObject.getString("members");
                            String resultAuth = jsonObject.getString("auth");
                            String resultType = jsonObject.getString("type");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", resultId);
                            map.put("logo", resultLogo);
                            map.put("name", resultName);
                            map.put("members", resultMembers);
                            map.put("auth", resultAuth);
                            map.put("type", resultType);
                            mylist.add(map);
                        }
                        if (iPage == 1) {
                            gridviewdata(mylist);
                        } else {
                            gridviewdata1(mylist);
                        }
                        if (mylist.size() == 0) {
                            iPage = iPage - 1;
                        }
                    } else {
                        if (iPage == 1) {
                            gridviewdata(mylist);
                        } else {
                            gridviewdata1(mylist);
                        }
                        if (iPage > 0) {
                            iPage -= 1;
                        }
                        Hint(resultMsg, HintDialog.ERROR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Hint(String.valueOf(volleyError), HintDialog.ERROR);
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }

    private void gridviewdata(ArrayList<HashMap<String, String>> mylist) {
        // iPage += 1;
        myAdapter = new MyAdapter(QlzxActivity.this);
        myAdapter.mylist = mylist;
        pull_refresh_grid_qlzx.setAdapter(myAdapter);
        // 刷新适配器
        myAdapter.notifyDataSetChanged();
        // 关闭刷新下拉
        pull_refresh_grid_qlzx.onRefreshComplete();

    }

    private void gridviewdata1(ArrayList<HashMap<String, String>> mylist) {
        //    myList = getMenuAdapter();
        iPage += 1;

        // 刷新适配器
        myAdapter.mylist.addAll(mylist);
        myAdapter.notifyDataSetChanged();
        // Call onRefreshComplete when the list has been refreshed.
        // 关闭上拉加载
        pull_refresh_grid_qlzx.onRefreshComplete();

    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(QlzxActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(QlzxActivity.this, R.style.dialog, sHint, type, true).show();
    }


    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        public ArrayList<HashMap<String, String>> mylist;

        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            mylist = new ArrayList<HashMap<String, String>>();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mylist.size();
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

        public Bitmap stringToBitmap(String string) {    // 将字符串转换成Bitmap类型
            Bitmap bitmap = null;
            try {
                byte[] bitmapArray;
                bitmapArray = Base64.decode(string, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }


        @Override
        public View getView(final int position, View view, ViewGroup arg2) {
            // TODO Auto-generated method stub
            if (view == null) {
                view = inflater.inflate(R.layout.qlzx_item, null);
            }
            LinearLayout ll_qlzx_item = view.findViewById(R.id.ll_qlzx_item);
            TextView tv_qlzx_nickname = view.findViewById(R.id.tv_qlzx_nickname);
            TextView tv_qlzx_members = view.findViewById(R.id.tv_qlzx_members);
            ImageView iv_qlzx_item_headimgurl = view.findViewById(R.id.iv_qlzx_item_headimgurl);
            tv_qlzx_nickname.setText(mylist.get(position).get("name"));
            tv_qlzx_members.setText(mylist.get(position).get("members"));
            Glide.with(QlzxActivity.this)
                    .load( Api.sUrl+ mylist.get(position).get("logo"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_qlzx_item_headimgurl);
            ll_qlzx_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
         /*           ConversationActivity.sType = "qunliao";

                    //跳转到融云群聊天界面
                    RongIM.getInstance().startConversation(QlzxActivity.this, Conversation.ConversationType.GROUP,
                            mylist.get(position).get("id"), mylist.get(position).get("name"));
*/
                    ConversationActivity.sType = "geren";
                    if (mylist.get(position).get("type").equals("0")) {
                        if (mylist.get(position).get("auth").equals("0")) {
                            jrql(mylist.get(position).get("name"), mylist.get(position).get("id"));
                        } else if (mylist.get(position).get("auth").equals("1")) {
                            // jrql(arrlist.get(position).get("name"), arrlist.get(position).get("id"));
                            Intent intent = new Intent(QlzxActivity.this, YanzhengActivity.class);
                            intent.putExtra("name", mylist.get(position).get("name"));
                            intent.putExtra("id", mylist.get(position).get("id"));
                            startActivity(intent);
                        }
                    } else if (mylist.get(position).get("type").equals("1")) {
                        RongIM.getInstance().startConversation(QlzxActivity.this, Conversation.ConversationType.GROUP,
                                mylist.get(position).get("id"), mylist.get(position).get("name"));
                    }
                }
            });
            return view;
        }
    }

    private void jrql(final String name, final String id) {
        String url = Api.sUrl
                + "Community/Api/joins/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/community_id/" + community_id + "/name/" + name + "/id/" + id;
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
                        if (resultCode == 3) {
                            RongIM.getInstance().startConversation(QlzxActivity.this, Conversation.ConversationType.GROUP, id, name);
                        } else if (resultCode == 6) {

                        }
                    } else {
                        Hint(resultMsg, HintDialog.ERROR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Hint(String.valueOf(volleyError), HintDialog.ERROR);
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }
}
