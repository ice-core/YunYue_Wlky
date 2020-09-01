package com.example.administrator.yunyue.sq_activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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

public class GuanzhuActivity extends AppCompatActivity {
    /**
     * 返回
     */
    private ImageView iv_guanzhu_back;
    /**
     * 列表
     */
    private MyGridView mgv_guanzhu;
    private MyGridView mgv_guanzhu_jr;
    /**
     * 社群
     */
    private TextView tv_guanzhu_shequn;
    /**
     * 社群下划线
     */
    private TextView tv_guanzhu_shequn_bj;
    /**
     * 社区
     */
    private TextView tv_guanzhu_shequ;
    /**
     * 社区下划线
     */
    private TextView tv_guanzhu_shequ_bj;
    /**
     * 判断选中项
     * 0.社群1.社区
     */
    private int type = 0;
    RequestQueue queue = null;
    private SharedPreferences pref;
    private String sUser_id;
    /**
     * 我管理的
     */
    private LinearLayout ll_guanzhu_wgld;
    /**
     * 我加入的
     */
    private LinearLayout ll_guanzhu_wjrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_guanzhu);
        queue = Volley.newRequestQueue(GuanzhuActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        iv_guanzhu_back = findViewById(R.id.iv_guanzhu_back);
        mgv_guanzhu = findViewById(R.id.mgv_guanzhu);
        mgv_guanzhu_jr = findViewById(R.id.mgv_guanzhu_jr);
        tv_guanzhu_shequn = findViewById(R.id.tv_guanzhu_shequn);
        tv_guanzhu_shequn_bj = findViewById(R.id.tv_guanzhu_shequn_bj);
        tv_guanzhu_shequ = findViewById(R.id.tv_guanzhu_shequ);
        tv_guanzhu_shequ_bj = findViewById(R.id.tv_guanzhu_shequ_bj);
        ll_guanzhu_wgld = findViewById(R.id.ll_guanzhu_wgld);
        ll_guanzhu_wjrd = findViewById(R.id.ll_guanzhu_wjrd);
        iv_guanzhu_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        query();
    }

    private void query() {
        String url = Api.sUrl + "Community/Api/meshequnList/appId/" + Api.sApp_Id + "/user_id/" + sUser_id;
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
                        JSONObject jsonObjectdata = new JSONObject(sDate.toString()).getJSONObject("data");
                        JSONArray jsonArrayguanli = jsonObjectdata.getJSONArray("guanli");
                        ArrayList<HashMap<String, String>> mylist_guanli = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < jsonArrayguanli.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArrayguanli.opt(i);
                            String ItemCommunity_id = jsonObject2.getString("community_id");
                            String ItemName = jsonObject2.getString("name");
                            String ItemSlogan = jsonObject2.getString("slogan");
                            String ItemLogo = jsonObject2.getString("logo");
                            String ItemGroup = jsonObject2.getString("group");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("community_id", ItemCommunity_id);
                            map.put("name", ItemName);
                            map.put("slogan", ItemSlogan);
                            map.put("logo", ItemLogo);
                            map.put("group", ItemGroup);
                            mylist_guanli.add(map);
                        }
                        gv_guanli(mylist_guanli);


                        JSONArray jsonArrayguanzhu = jsonObjectdata.getJSONArray("guanzhu");
                        ArrayList<HashMap<String, String>> mylist_guanzhu = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < jsonArrayguanzhu.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArrayguanzhu.opt(i);
                            String ItemCommunity_id = jsonObject2.getString("community_id");
                            String ItemName = jsonObject2.getString("name");
                            String ItemSlogan = jsonObject2.getString("slogan");
                            String ItemLogo = jsonObject2.getString("logo");
                            String ItemGroup = jsonObject2.getString("group");

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("community_id", ItemCommunity_id);
                            map.put("name", ItemName);
                            map.put("slogan", ItemSlogan);
                            map.put("logo", ItemLogo);
                            map.put("group", ItemGroup);
                            mylist_guanzhu.add(map);
                        }
                        gv_jiaru(mylist_guanzhu);
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
        loadingDialog = new LoadingDialog(GuanzhuActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(GuanzhuActivity.this, R.style.dialog, sHint, type, true).show();
    }


    public void Qxgz(final String community_id, String name) {
        final Dialog dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        final View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_quxiaoguanzhu, null);
        TextView tv_qxgz_message = inflate.findViewById(R.id.tv_qxgz_message);
        TextView tv_qxgz_rygz = inflate.findViewById(R.id.tv_qxgz_rygz);
        TextView tv_qxgz_bzgz = inflate.findViewById(R.id.tv_qxgz_bzgz);
        tv_qxgz_message.setText("不再关注“" + name + "”\n" + "将不在收到其下发的信息");

        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
        dialog.show();
        tv_qxgz_rygz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_qxgz_bzgz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDialogin();
                dialogin("");
                tuiShequn(community_id);
                dialog.dismiss();
            }
        });
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
                        query();
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

    /**
     * 管理
     */
    private void gv_guanli(ArrayList<HashMap<String, String>> mylist) {
        if (mylist.size() == 0) {
            ll_guanzhu_wgld.setVisibility(View.GONE);
        } else {
            ll_guanzhu_wgld.setVisibility(View.VISIBLE);
        }
        MyAdapter_gl myAdapter_gl = new MyAdapter_gl(GuanzhuActivity.this);
        myAdapter_gl.arrlist = mylist;
        mgv_guanzhu.setAdapter(myAdapter_gl);
    }


    /**
     * 加入
     */
    private void gv_jiaru(ArrayList<HashMap<String, String>> mylist) {
        if (mylist.size() == 0) {
            ll_guanzhu_wjrd.setVisibility(View.GONE);
        } else {
            ll_guanzhu_wjrd.setVisibility(View.VISIBLE);
        }
        MyAdapter_jr myAdapter_jr = new MyAdapter_jr(GuanzhuActivity.this);

        myAdapter_jr.arrlist = mylist;
        mgv_guanzhu_jr.setAdapter(myAdapter_jr);
    }

    private class MyAdapter_gl extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;


        public MyAdapter_gl(Context context) {
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
                view = inflater.inflate(R.layout.guanzhu_shequ_item, null);
            }
            TextView tv_guanzhu_shequ_item_guanzhu = view.findViewById(R.id.tv_guanzhu_shequ_item_guanzhu);

            tv_guanzhu_shequ_item_guanzhu.setVisibility(View.GONE);
            LinearLayout ll_guanzhu_item = view.findViewById(R.id.ll_guanzhu_item);
            ImageView iv_gz = view.findViewById(R.id.iv_gz);
            TextView tv_gz_name = view.findViewById(R.id.tv_gz_name);
            TextView tv_gz_group = view.findViewById(R.id.tv_gz_group);
            tv_gz_name.setText(arrlist.get(position).get("name"));
            tv_gz_group.setText(arrlist.get(position).get("group") + "人在该社群");
            Glide.with(GuanzhuActivity.this)
                    .load( Api.sUrl+ arrlist.get(position).get("logo"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_gz);
            ll_guanzhu_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GuanzhuActivity.this, ShequnXqActivity.class);
                    intent.putExtra("id", arrlist.get(position).get("community_id"));
                    startActivity(intent);
                }
            });

            return view;
        }
    }

    private class MyAdapter_jr extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;


        public MyAdapter_jr(Context context) {
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
                view = inflater.inflate(R.layout.guanzhu_shequ_item, null);
            }
            TextView tv_guanzhu_shequ_item_guanzhu = view.findViewById(R.id.tv_guanzhu_shequ_item_guanzhu);

            tv_guanzhu_shequ_item_guanzhu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Qxgz(arrlist.get(position).get("community_id"), arrlist.get(position).get("name"));
                }
            });
            LinearLayout ll_guanzhu_item = view.findViewById(R.id.ll_guanzhu_item);
            ImageView iv_gz = view.findViewById(R.id.iv_gz);
            TextView tv_gz_name = view.findViewById(R.id.tv_gz_name);
            TextView tv_gz_group = view.findViewById(R.id.tv_gz_group);
            tv_gz_name.setText(arrlist.get(position).get("name"));
            tv_gz_group.setText(arrlist.get(position).get("group") + "人在该社群");
            Glide.with(GuanzhuActivity.this)
                    .load( Api.sUrl+ arrlist.get(position).get("logo"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_gz);
            ll_guanzhu_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GuanzhuActivity.this, ShequnXqActivity.class);
                    intent.putExtra("id", arrlist.get(position).get("community_id"));
                    startActivity(intent);
                }
            });

            return view;
        }
    }
}
