package com.example.administrator.yunyue.sq_activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
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
import com.example.administrator.yunyue.Im.HaoyouActivity;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.erci.activity.CwhyActivity;
import com.example.administrator.yunyue.sc_activity.GuanggaoActivity;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;


public class ShequnXqActivity extends AppCompatActivity {

    /**
     * 返回
     */
    private ImageView iv_shequnxq_back;

    /**
     * 群聊列表
     */
    private ListView lv_shequnxq_ql;

    /**
     * 动态列表
     */
    private ListView lv_shequnxq_dt;

    /**
     * 发起群聊
     */
    private TextView tv_shequnxq_fqql;

    /**
     * 二维码
     */
    private ImageView iv_shequnxq_ewm;
    RequestQueue queue = null;
    public static String community_id = "";
    private SharedPreferences pref;
    private String sUser_id = "";
    private String sIs_vip = "";

    /**
     * 更多社群
     */
    private TextView tv_shequnxq_gdql;

    /**
     * ···
     */
    private LinearLayout ll_shequnxq_xl;
    /**
     *
     */
    private ScrollView sv_shequnxq;

    /**
     * 社群二維碼
     */
    private String code = "";
    /**
     * 社群名称
     */
    private TextView tv_shequn_xq_name;

    /**
     * 社群图
     */
    private ImageView iv_shequn_xq_logo;
    private int index = 0;
    private int iPage = 1;
    private TextView tv_shequn_xq_jz;
    private MyAdapterDt myAdapterDt;
    //是否关注
    private String guanzhu = "";

    /**
     * 社群创建人ID
     */
    private String user_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_shequn_xq);
        queue = Volley.newRequestQueue(ShequnXqActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        sIs_vip = pref.getString("is_vip", "");
        Intent intent = getIntent();
        community_id = intent.getStringExtra("id");
        iv_shequnxq_back = findViewById(R.id.iv_shequnxq_back);
        lv_shequnxq_dt = findViewById(R.id.lv_shequnxq_dt);
        lv_shequnxq_ql = findViewById(R.id.lv_shequnxq_ql);
        tv_shequnxq_fqql = findViewById(R.id.tv_shequnxq_fqql);
        iv_shequnxq_ewm = findViewById(R.id.iv_shequnxq_ewm);
        tv_shequnxq_gdql = findViewById(R.id.tv_shequnxq_gdql);
        ll_shequnxq_xl = findViewById(R.id.ll_shequnxq_xl);
        sv_shequnxq = findViewById(R.id.sv_shequnxq);
        tv_shequn_xq_jz = findViewById(R.id.tv_shequn_xq_jz);
        tv_shequn_xq_name = findViewById(R.id.tv_shequn_xq_name);
        iv_shequn_xq_logo = findViewById(R.id.iv_shequn_xq_logo);

        ll_shequnxq_xl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MorePopWindow morePopWindow = new MorePopWindow(ShequnXqActivity.this);
                morePopWindow.showPopupWindow(ll_shequnxq_xl);
            }
        });
        iv_shequnxq_ewm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShequnXqActivity.this, ShequnewmActivity.class);
                intent.putExtra("code", code);
                intent.putExtra("type", "shequn");
                startActivity(intent);
            }
        });
        iv_shequnxq_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_shequnxq_fqql.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ShequnXqActivity.this, CjqlActivity.class);
                startActivity(intent);
            }
        });
        tv_shequnxq_gdql.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_shequnxq_gdql.getText().toString().equals("发起群聊")) {
                    Intent intent = new Intent(ShequnXqActivity.this, CjqlActivity.class);
                    QlzxActivity.community_id = community_id;
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ShequnXqActivity.this, QlzxActivity.class);
                    QlzxActivity.community_id = community_id;
                    startActivity(intent);
                }
            }
        });
        iPage = 1;
        query();

        // 滑动加载
        sv_shequnxq.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        index++;
                        break;
                    default:
                        break;
                }
                if (event.getAction() == MotionEvent.ACTION_UP && index > 0) {
                    index = 0;
                    View view = ((ScrollView) v).getChildAt(0);
                    //目前总高度
                    int a = view.getMeasuredHeight();
                    //目前高度
                    int b = v.getScrollY() + v.getHeight();
                    if (a - 2000 <= b) {
                        //    tv_shequn_xq_jz.setVisibility(View.VISIBLE);
                        iPage += 1;
                        query();
                        //加载数据代码
                    }
                }
                return false;
            }
        });
    }


    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initview();
    }

    private void initview() {
        iPage = 1;
        query();
    }

    /**
     * 社群首页信息获取
     */
    private void query() {
        String url = Api.sUrl + "Community/Api/shequnDetails/appId/" + Api.sApp_Id
                + "/community_id/" + community_id + "/user_id/" + sUser_id + "/page/" + iPage;
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
                        //tv_shequn_xq_jz.setVisibility(View.GONE);
                        JSONObject jsonObjectdata = new JSONObject(sDate.toString()).getJSONObject("data");
                        if (iPage == 1) {
                            JSONObject jsonObjectcommunity = jsonObjectdata.getJSONObject("community");
                            user_id = jsonObjectcommunity.getString("user_id");
                            String name = jsonObjectcommunity.getString("name");
                            tv_shequn_xq_name.setText(name);
                            code = jsonObjectcommunity.getString("code");
                            String logo = jsonObjectcommunity.getString("logo");
                            //1关注0未关注
                            guanzhu = jsonObjectcommunity.getString("guanzhu");

                            Glide.with(ShequnXqActivity.this)
                                    .load( Api.sUrl+code)
                                    .asBitmap()
                                    .placeholder(R.mipmap.error)
                                    .error(R.mipmap.error)
                                    .dontAnimate()
                                    .into(iv_shequnxq_ewm);

                            Glide.with(ShequnXqActivity.this)
                                    .load( Api.sUrl+ logo)
                                    .asBitmap()
                                    .placeholder(R.mipmap.error)
                                    .error(R.mipmap.error)
                                    .dontAnimate()
                                    .into(iv_shequn_xq_logo);

                            JSONArray jsonArraygroup = jsonObjectdata.getJSONArray("group");
                            ArrayList<HashMap<String, String>> mylist_group = new ArrayList<HashMap<String, String>>();
                            for (int i = 0; i < jsonArraygroup.length(); i++) {
                                JSONObject jsonObject2 = (JSONObject) jsonArraygroup.opt(i);
                                String ItemId = jsonObject2.getString("id");
                                String Itemlogo = jsonObject2.getString("logo");
                                String Itemname = jsonObject2.getString("name");
                                String Itemmembers = jsonObject2.getString("members");
                                //是否审核0否1是
                                String Itemauth = jsonObject2.getString("auth");
                                //是否加入群0否1是
                                String Itemtype = jsonObject2.getString("type");
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("id", ItemId);
                                map.put("logo", Itemlogo);
                                map.put("name", Itemname);
                                map.put("members", Itemmembers);
                                map.put("auth", Itemauth);
                                map.put("type", Itemtype);
                                mylist_group.add(map);
                            }
                            gv_qunliao(mylist_group);
                        }

                        JSONArray jsonArraydynamic = jsonObjectdata.getJSONArray("dynamic");
                        ArrayList<HashMap<String, String>> mylist_dynamic = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < jsonArraydynamic.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArraydynamic.opt(i);
                            String ItemUser_Id = jsonObject2.getString("user_id");
                            String ItemDynamic_Id = jsonObject2.getString("dynamic_id");
                            String ItemNickname = jsonObject2.getString("nickname");
                            String ItemHeadimgurl = jsonObject2.getString("headimgurl");
                            String ItemTitle = jsonObject2.getString("title");
                            String ItemContent = jsonObject2.getString("content");
                            String ItemTime = jsonObject2.getString("time");
                            String ItemImg1 = jsonObject2.getString("img1");
                            String ItemImg2 = jsonObject2.getString("img2");
                            String ItemImg3 = jsonObject2.getString("img3");
                            String ItemZan = jsonObject2.getString("zan");
                            String ItemComment = jsonObject2.getString("comment");

                            String ItemUrl = jsonObject2.getString("url");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("user_id", ItemUser_Id);
                            map.put("dynamic_id", ItemDynamic_Id);
                            map.put("nickname", ItemNickname);
                            map.put("headimgurl", ItemHeadimgurl);
                            map.put("title", ItemTitle);
                            map.put("content", ItemContent);
                            map.put("time", ItemTime);
                            map.put("img1", ItemImg1);
                            map.put("img2", ItemImg2);
                            map.put("img3", ItemImg3);
                            map.put("zan", ItemZan);
                            map.put("comment", ItemComment);
                            map.put("url", ItemUrl);
                            //  map.put("guanzhu", "0");
                            mylist_dynamic.add(map);
                        }
                        if (iPage == 1) {
                            gv_xiaoxi(mylist_dynamic);
                        } else {
                            if (mylist_dynamic.size() == 0) {
                                iPage = iPage - 1;
                                tv_shequn_xq_jz.setVisibility(View.VISIBLE);
                                tv_shequn_xq_jz.setText("没有更多了...");
                            }
                            gv_xiaoxi1(mylist_dynamic);
                        }
                        // Hint(resultMsg, HintDialog.SUCCESS);
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
        queue.add(request);
    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(ShequnXqActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(ShequnXqActivity.this, R.style.dialog, sHint, type, true).show();
    }

    /**
     * 群聊
     */
    private void gv_qunliao(ArrayList<HashMap<String, String>> mylist) {
        MyAdapterQl myAdapterQl = new MyAdapterQl(this);

        myAdapterQl.arrlist = mylist;
        lv_shequnxq_ql.setAdapter(myAdapterQl);
        setListViewHeightBasedOnChildren(lv_shequnxq_ql);
        sv_shequnxq.scrollTo(0, 0);
        if (mylist.size() == 0) {
            tv_shequnxq_fqql.setVisibility(View.GONE);
            tv_shequnxq_gdql.setText("发起群聊");
        }

    }


    /**
     * 动态
     */
    private void gv_xiaoxi(ArrayList<HashMap<String, String>> mylist) {
        myAdapterDt = new MyAdapterDt(this);
        myAdapterDt.arrlist = mylist;
        lv_shequnxq_dt.setAdapter(myAdapterDt);
        setListViewHeightBasedOnChildren(lv_shequnxq_dt);
        sv_shequnxq.scrollTo(0, 0);
    }

    private void gv_xiaoxi1(ArrayList<HashMap<String, String>> mylist) {
        myAdapterDt.arrlist.addAll(mylist);
        myAdapterDt.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(lv_shequnxq_dt);
        //  sv_shequnxq.scrollTo(0, 0);
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
        int listViewWidth = getWindowManager().getDefaultDisplay().getWidth();
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

    private class MyAdapterQl extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;


        public MyAdapterQl(Context context) {
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
                view = inflater.inflate(R.layout.shequnxq_ql_item, null);
            }
            LinearLayout ll_shequnxq_ql_item = view.findViewById(R.id.ll_shequnxq_ql_item);
            ImageView iv_shequnxq_ql_item = view.findViewById(R.id.iv_shequnxq_ql_item);
            TextView iv_shequnxq_ql_item_name = view.findViewById(R.id.iv_shequnxq_ql_item_name);
            iv_shequnxq_ql_item_name.setText(arrlist.get(position).get("name"));
            TextView iv_shequnxq_ql_item_members = view.findViewById(R.id.iv_shequnxq_ql_item_members);
            Glide.with(ShequnXqActivity.this)
                    .load( Api.sUrl+arrlist.get(position).get("logo"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_shequnxq_ql_item);
            iv_shequnxq_ql_item_members.setText(arrlist.get(position).get("members"));
            ll_shequnxq_ql_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //跳转到融云群聊天界面
                    ConversationActivity.sType = "geren";
                    if (arrlist.get(position).get("type").equals("0")) {
                        if (arrlist.get(position).get("auth").equals("0")) {
                            jrql(arrlist.get(position).get("name"), arrlist.get(position).get("id"));
                        } else if (arrlist.get(position).get("auth").equals("1")) {
                            // jrql(arrlist.get(position).get("name"), arrlist.get(position).get("id"));
                            Intent intent = new Intent(ShequnXqActivity.this, YanzhengActivity.class);
                            intent.putExtra("name", arrlist.get(position).get("name"));
                            intent.putExtra("id", arrlist.get(position).get("id"));
                            startActivity(intent);
                        }
                    } else if (arrlist.get(position).get("type").equals("1")) {
                        RongIM.getInstance().startConversation(ShequnXqActivity.this, Conversation.ConversationType.GROUP,
                                arrlist.get(position).get("id"), arrlist.get(position).get("name"));
                    }
                }
            });
       /*     map.put("logo", Itemlogo);
            map.put("name", Itemname);
            map.put("members", Itemmembers);*/

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
                            RongIM.getInstance().startConversation(ShequnXqActivity.this, Conversation.ConversationType.GROUP,
                                    id, name);
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

    private class MyAdapterDt extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;


        public MyAdapterDt(Context context) {
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
                view = inflater.inflate(R.layout.shequnxq_dt_item, null);
            }
            LinearLayout ll_shequn_tuijian_item = view.findViewById(R.id.ll_shequn_tuijian_item);
            ImageView iv_shequnxq_dt_item = view.findViewById(R.id.iv_shequnxq_dt_item);
            iv_shequnxq_dt_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ShequnXqActivity.this, GrtzActivity.class);
                    GrtzActivity.sFriend_id = arrlist.get(position).get("user_id");
                    startActivity(intent);
                }
            });
            TextView tv_shequnxq_dt_item_nickname = view.findViewById(R.id.tv_shequnxq_dt_item_nickname);
            TextView tv_shequnxq_dt_item_time = view.findViewById(R.id.tv_shequnxq_dt_item_time);
            TextView tv_shequnxq_dt_item_title = view.findViewById(R.id.tv_shequnxq_dt_item_title);
            TextView tv_shequnxq_dt_item_content = view.findViewById(R.id.tv_shequnxq_dt_item_content);
            TextView tv_shequn_dt_item_dianzhan = view.findViewById(R.id.tv_shequn_dt_item_dianzhan);
            TextView tv_shequn_dt_item_comment = view.findViewById(R.id.tv_shequn_dt_item_comment);
            ImageView iv_shequn_dt_item = view.findViewById(R.id.iv_shequn_dt_item);
            LinearLayout ll_shequn_dt_item = view.findViewById(R.id.ll_shequn_dt_item);
            ImageView iv_shequn_dt_item_1 = view.findViewById(R.id.iv_shequn_dt_item_1);
            ImageView iv_shequn_dt_item_2 = view.findViewById(R.id.iv_shequn_dt_item_2);
            ImageView iv_shequn_dt_item_3 = view.findViewById(R.id.iv_shequn_dt_item_3);
            Glide.with(ShequnXqActivity.this)
                    .load( Api.sUrl+arrlist.get(position).get("headimgurl"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_shequnxq_dt_item);
            tv_shequnxq_dt_item_nickname.setText(arrlist.get(position).get("nickname"));
            tv_shequnxq_dt_item_time.setText(arrlist.get(position).get("time"));
            tv_shequnxq_dt_item_title.setText(arrlist.get(position).get("title"));
            tv_shequnxq_dt_item_content.setText(arrlist.get(position).get("content"));
            tv_shequn_dt_item_dianzhan.setText(arrlist.get(position).get("zan"));
            tv_shequn_dt_item_comment.setText(arrlist.get(position).get("comment"));
            if (arrlist.get(position).get("img2").equals("")) {
                ll_shequn_dt_item.setVisibility(View.GONE);
                iv_shequn_dt_item.setVisibility(View.VISIBLE);
                if (arrlist.get(position).get("img1").equals("")) {
                    iv_shequn_dt_item.setVisibility(View.GONE);
                } else {
                    iv_shequn_dt_item.setVisibility(View.VISIBLE);
                    Glide.with(ShequnXqActivity.this)
                            .load( Api.sUrl+arrlist.get(position).get("img1"))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_shequn_dt_item);
                }
            } else {
                iv_shequn_dt_item.setVisibility(View.GONE);
                ll_shequn_dt_item.setVisibility(View.VISIBLE);
                if (arrlist.get(position).get("img1").equals("")) {
                } else {

                    Glide.with(ShequnXqActivity.this)
                            .load( Api.sUrl+ arrlist.get(position).get("img1"))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_shequn_dt_item_1);
                }
                if (arrlist.get(position).get("img2").equals("")) {
                } else {

                    Glide.with(ShequnXqActivity.this)
                            .load( Api.sUrl+arrlist.get(position).get("img2"))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_shequn_dt_item_2);
                }
                if (arrlist.get(position).get("img3").equals("")) {
                } else {
                    Glide.with(ShequnXqActivity.this)
                            .load( Api.sUrl+arrlist.get(position).get("img3"))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_shequn_dt_item_3);
                }
            }
            ll_shequn_tuijian_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ShequnXqActivity.this, GuanggaoActivity.class);
                    intent.putExtra("link", arrlist.get(position).get("url"));
                    startActivity(intent);
                }
            });

            return view;
        }
    }


    public class MorePopWindow extends PopupWindow {

        @SuppressLint("InflateParams")
        public MorePopWindow(final Activity context) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View content = inflater.inflate(R.layout.popupwindow_add, null);

            // 设置SelectPicPopupWindow的View
            this.setContentView(content);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            // 设置SelectPicPopupWindow弹出窗体可点击
            this.setFocusable(true);
            this.setOutsideTouchable(true);
            // 刷新状态
            this.update();
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0000000000);
            // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
            this.setBackgroundDrawable(dw);

            // 设置SelectPicPopupWindow弹出窗体动画效果
            this.setAnimationStyle(R.style.AnimationPreview);

            TextView tv_fbdt = content.findViewById(R.id.tv_fbdt);
            TextView tv_cysh = content.findViewById(R.id.tv_cysh);
            TextView tv_wddt = content.findViewById(R.id.tv_wddt);
            TextView tv_gywm = content.findViewById(R.id.tv_gywm);
            TextView tv_gz = content.findViewById(R.id.tv_gz);
            if (guanzhu.equals("1")) {
                tv_gz.setText("取消关注");
            } else {
                tv_gz.setText("关注");
            }
            if (user_id.equals(sUser_id)) {
                tv_gz.setVisibility(View.GONE);
            } else {
                tv_gz.setVisibility(View.VISIBLE);
            }
            tv_fbdt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(new Intent(context, FbtzActivity.class));
                    // intent.putExtra("createGroup", true);
                    context.startActivity(intent);
                    dismiss();
                }
            });
            tv_cysh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                        Intent intent = new Intent(new Intent(context, CyshActivity.class));
                        //  intent.putExtra("createGroup", true);
                        context.startActivity(intent);
                        dismiss();

                }
            });
            tv_wddt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(new Intent(context, GrtzActivity.class));
                    //  intent.putExtra("createGroup", true);
                    GrtzActivity.sFriend_id = "";
                    context.startActivity(intent);
                    dismiss();
                }
            });
            tv_gywm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(new Intent(context, GywmActivity.class));
                    //  intent.putExtra("createGroup", true);
                    context.startActivity(intent);
                    dismiss();
                }
            });
            tv_gz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (guanzhu.equals("0")) {
                        hideDialogin();
                        dialogin("");
                        jiaruShequn(community_id);
                    } else {
                        hideDialogin();
                        dialogin("");
                        tuiShequn(community_id);
                    }
                    dismiss();
                }
            });

        }

        /**
         * 显示popupWindow
         *
         * @param parent
         */
        public void showPopupWindow(View parent) {
            if (!this.isShowing()) {
                // 以下拉方式显示popupwindow
                this.showAsDropDown(parent, 0, -10);
            } else {
                this.dismiss();
            }
        }
    }

    /**
     * 关注社群
     */
    private void jiaruShequn(String community_id) {
        String url = Api.sUrl + "Community/Api/jiaruShequn/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/community_id/" + community_id;
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
                        guanzhu = "1";
                        // Hint(resultMsg, HintDialog.SUCCESS);
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
        queue.add(request);
    }

    private void tuiShequn(String community_id) {
        String url = Api.sUrl + "Community/Api/tuiShequn/appId/" + Api.sApp_Id + "/user_id/" + sUser_id + "/community_id/" + community_id;
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
                        guanzhu = "0";
                        Hint(resultMsg, HintDialog.SUCCESS);
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
        queue.add(request);
    }

}
