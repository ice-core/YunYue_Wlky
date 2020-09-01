package com.example.administrator.yunyue.sq_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
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
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

public class QunxiangqingActivity extends AppCompatActivity {
    private static final String TAG = QunxiangqingActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout iv_qunxiangqing_back;
    /**
     * 群成员
     */
    private MyGridView mgv_qxq_cy;
    RequestQueue queue = null;
    private SharedPreferences pref;
    private String sUser_id;
    public static String sGroup_Id = "";
    public static String sName = "";
    /**
     * 群聊名称
     */
    private TextView tv_qxq_name;
    /**
     * 加入条件
     */
    private TextView tv_qxq_auth;
    /**
     * 社群名称
     */
    private TextView tv_qxq_gname;
    /**
     * 二维码
     */
    private ImageView iv_qxq_code;

    /**
     * 我的群昵称
     */
    private TextView tv_qxq_uname;
    /**
     * 推出群聊
     */
    private TextView tv_qxq_tcql;

    /**
     * 群二维码
     */
    private LinearLayout ll_qunxiangqing_ewm;
    private String sCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_qunxiangqing);
        queue = Volley.newRequestQueue(QunxiangqingActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        iv_qunxiangqing_back = findViewById(R.id.iv_qunxiangqing_back);
        mgv_qxq_cy = findViewById(R.id.mgv_qxq_cy);
        tv_qxq_tcql = findViewById(R.id.tv_qxq_tcql);
        iv_qunxiangqing_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        tv_qxq_name = findViewById(R.id.tv_qxq_name);
        tv_qxq_auth = findViewById(R.id.tv_qxq_auth);
        tv_qxq_gname = findViewById(R.id.tv_qxq_gname);
        iv_qxq_code = findViewById(R.id.iv_qxq_code);
        tv_qxq_uname = findViewById(R.id.tv_qxq_uname);
        ll_qunxiangqing_ewm = findViewById(R.id.ll_qunxiangqing_ewm);
        ll_qunxiangqing_ewm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QunxiangqingActivity.this, ShequnewmActivity.class);
                intent.putExtra("code", sCode);
                intent.putExtra("type", "qun");
                startActivity(intent);
            }
        });
        query();
        tv_qxq_tcql.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDialogin();
                dialogin("");
                delete();
            }
        });
    }
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initview();
    }

    private void initview() {
        query();
    }

    private void delete() {
        String url = Api.sUrl + "Community/Api/tuiGroup/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/group_id/" + sGroup_Id;
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
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1200);
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

    private void back() {
        //跳转到融云群聊天界面
        RongIM.getInstance().startConversation(QunxiangqingActivity.this, Conversation.ConversationType.GROUP,
                sGroup_Id, tv_qxq_name.getText().toString());
        finish();
    }

    private void query() {
        String url = Api.sUrl + "Community/Api/groupDetails/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/group_id/" + sGroup_Id;
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
                        JSONObject jsonObjectdata = new JSONObject(sDate.toString()).getJSONObject("data");
                        JSONObject jsonArraygroup = jsonObjectdata.getJSONObject("group");
                        //群聊ID
                        String sId = jsonArraygroup.getString("id");
                        //群聊名称
                        sName = jsonArraygroup.getString("name");
                        tv_qxq_name.setText(sName);
                        //社群名称
                        String sGname = jsonArraygroup.getString("gname");
                        tv_qxq_gname.setText(sGname);
                        //权限0任何人都能加入1管理员审核
                        int iAuth = jsonArraygroup.getInt("auth");
                        if (iAuth == 0) {
                            tv_qxq_auth.setText("允许任何人加入");
                        } else if (iAuth == 1) {
                            tv_qxq_auth.setText("需管理员审核");
                        }
                        //群聊二维码
                        sCode = jsonArraygroup.getString("code");
                        Glide.with(QunxiangqingActivity.this)
                                .load( Api.sUrl+ sCode)
                                .asBitmap()
                                .placeholder(R.mipmap.error)
                                .error(R.mipmap.error)
                                .dontAnimate()
                                .into(iv_qxq_code);
                        //用户昵称
                        String sUname = jsonArraygroup.getString("uname");
                        tv_qxq_uname.setText(sUname);
                        //gv_qunliao(mylist_group);
                        MyAdapter myAdapter = new MyAdapter(QunxiangqingActivity.this);
                        JSONArray jsonArraygroupList = jsonObjectdata.getJSONArray("groupList");
                        ArrayList<HashMap<String, String>> mylist_groupList = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < jsonArraygroupList.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArraygroupList.opt(i);
                            //用户ID
                            String ItemId = jsonObject2.getString("id");
                            //用户群聊名称
                            String ItemHeadimgurl = jsonObject2.getString("headimgurl");
                            //头像
                            String ItemName = jsonObject2.getString("name");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", ItemId);
                            map.put("headimgurl", ItemHeadimgurl);
                            map.put("name", ItemName);
                            mylist_groupList.add(map);
                        }

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id", "");
                        map.put("headimgurl", "");
                        map.put("name", "");
                        mylist_groupList.add(map);
                        myAdapter.mylist = mylist_groupList;
                        mgv_qxq_cy.setAdapter(myAdapter);
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


    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(QunxiangqingActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(QunxiangqingActivity.this, R.style.dialog, sHint, type, true).show();
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
                view = inflater.inflate(R.layout.qunxiangqing_cy_item, null);
            }

            LinearLayout ll_qunxiangqing_cy_item = view.findViewById(R.id.ll_qunxiangqing_cy_item);
            ImageView iv_qunxiangqing_cy_item = view.findViewById(R.id.iv_qunxiangqing_cy_item);
            TextView tv_qunxiangqing_cy_item_name = view.findViewById(R.id.tv_qunxiangqing_cy_item_name);
            tv_qunxiangqing_cy_item_name.setText(mylist.get(position).get("name"));
            if (mylist.get(position).get("id").equals("")) {
                iv_qunxiangqing_cy_item.setImageResource(R.drawable.add);
            } else {
                Glide.with(QunxiangqingActivity.this)
                        .load( Api.sUrl+ mylist.get(position).get("headimgurl"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_qunxiangqing_cy_item);
            }
            ll_qunxiangqing_cy_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mylist.get(position).get("id").equals("")) {
                        Intent intent = new Intent(QunxiangqingActivity.this, XzlxrActivity.class);
                        startActivity(intent);
                    }
                }
            });
            return view;
        }
    }

    /**
     *  * 方法名：onKeyDown(int keyCode, KeyEvent event)
     *  * 功    能：菜单栏点击事件
     *  * 参    数：int keyCode, KeyEvent event
     *  * 返回值：无
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Log.d(TAG, "onKeyDown KEYCODE_BACK");
                back();
                break;
            /*
            * Home键是系统事件，不能通过KeyDown监听
            * 此处log不会打印
            */
            case KeyEvent.KEYCODE_HOME:
                Log.d(TAG, "onKeyDown KEYCODE_HOME");
                break;
            case KeyEvent.KEYCODE_MENU:
                Log.d(TAG, "onKeyDown KEYCODE_MENU");
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
