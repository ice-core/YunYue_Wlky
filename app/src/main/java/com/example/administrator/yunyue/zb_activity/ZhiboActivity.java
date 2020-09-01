package com.example.administrator.yunyue.zb_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.example.administrator.yunyue.CustomScrollView;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.erci.activity.CwhyActivity;
import com.example.administrator.yunyue.sc.CircleImageView;
import com.example.administrator.yunyue.sc_activity.DpzyActivity;
import com.example.administrator.yunyue.sc_activity.GuanggaoActivity;
import com.example.administrator.yunyue.sc_activity.SpxqActivity;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tencent.liteav.demo.play.SuperPlayerGlobalConfig;
import com.tencent.liteav.demo.play.SuperPlayerModel;
import com.tencent.liteav.demo.play.SuperPlayerView;
import com.tencent.rtmp.TXLiveConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ZhiboActivity extends AppCompatActivity implements SuperPlayerView.OnSuperPlayerViewCallback,
        SwipeRefreshLayout.OnRefreshListener, CustomScrollView.OnScrollChangeListener {
    private static final String TAG = ZhiboActivity.class.getSimpleName();
    private SuperPlayerView mSuperPlayerView;
    /**
     * 返回
     */
    private LinearLayout ll_zhibo_back;
    /**
     * 标题
     */
    private LinearLayout ll_zhibo_title;
    /**
     * 回放视频
     */
    private MyGridView mgv_zhibo_huifang;

    private CustomScrollView csv_zhibo;
    /**
     * 更多加载
     */
    private LinearLayout ll_zhibo_gdjz;
    /**
     * 动态
     */
    private TextView tv_zhibo_dt;
    /**
     * 回放
     */
    private TextView tv_zhibo_hf;

    /**
     * 0--回放
     * 1--动态
     */
    private int type_dh = 0;

    /**
     * 0--主播
     * 1--聊天
     */
    private int type_zl = 0;
    /**
     * 主播
     */
    private TextView tv_zhibo_zb;
    /**
     * 聊天
     */
    private TextView tv_zhibo_lt;

    private MyAdapter_huifang myAdapter_huifang;

    /**
     * 聊天列表阴影
     */
    private TextView tv_zhibo_lt_lbyy;
    /**
     * 直播简介
     */
    private LinearLayout ll_zhibo_jj;

    /**
     * 评论
     */
    private FrameLayout fl_zhibo_pinglun;
    /**
     * 观看
     */
    private GridView gv_zhibo_gk;
    RequestQueue queue = null;
    private SharedPreferences pref;
    private String sUser_id="";
   // private String sIs_vip="";
    private String id = "";

    private TextView tv_zhibo_brief;
    /**
     * 回放列表
     */
    private ArrayList<HashMap<String, String>> mylist_all_hf;

    /**
     * 动态列表
     */
    private ArrayList<HashMap<String, String>> mylist_all_dt;
    /**
     * 评论列表
     */
    private ArrayList<HashMap<String, String>> mylist_view_pl;
    /**
     * 评论输入框
     */
    private EditText et_zhibo_pl;

    /**
     * 直播关注
     */
    private TextView tv_zhibo_gz;

    private int hf_page = 1, dt_page = 1, lt_page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_zhibo);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");

        queue = Volley.newRequestQueue(ZhiboActivity.this);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        ll_zhibo_back = findViewById(R.id.ll_zhibo_back);

        ll_zhibo_title = findViewById(R.id.ll_zhibo_title);
        mSuperPlayerView = findViewById(R.id.main_super_player_view);
        mgv_zhibo_huifang = findViewById(R.id.mgv_zhibo_huifang);
        ll_zhibo_gdjz = findViewById(R.id.ll_zhibo_gdjz);
        tv_zhibo_dt = findViewById(R.id.tv_zhibo_dt);
        tv_zhibo_hf = findViewById(R.id.tv_zhibo_hf);
        tv_zhibo_zb = findViewById(R.id.tv_zhibo_zb);
        gv_zhibo_gk = findViewById(R.id.gv_zhibo_gk);
        tv_zhibo_brief = findViewById(R.id.tv_zhibo_brief);
        TextPaint paint_zb = tv_zhibo_zb.getPaint();
        paint_zb.setFakeBoldText(true);
        tv_zhibo_lt = findViewById(R.id.tv_zhibo_lt);
        tv_zhibo_lt_lbyy = findViewById(R.id.tv_zhibo_lt_lbyy);
        ll_zhibo_jj = findViewById(R.id.ll_zhibo_jj);
        fl_zhibo_pinglun = findViewById(R.id.fl_zhibo_pinglun);
        tv_zhibo_gz = findViewById(R.id.tv_zhibo_gz);
        ll_zhibo_gdjz.setVisibility(GONE);
        csv_zhibo = findViewById(R.id.csv_zhibo);
        csv_zhibo.setOnScrollChangeListener(this);
        mSuperPlayerView.setPlayerViewCallback(this);
        mylist_view_pl = new ArrayList<HashMap<String, String>>();
        mylist_all_hf = new ArrayList<HashMap<String, String>>();
        mylist_all_dt = new ArrayList<>();
        tv_zhibo_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query_dt();
                tv_zhibo_dt.setTextColor(tv_zhibo_dt.getResources().getColor(R.color.white));
                tv_zhibo_hf.setTextColor(tv_zhibo_hf.getResources().getColor(R.color.hei333333));
                tv_zhibo_dt.setBackgroundResource(R.drawable.bj_theme_2);
                tv_zhibo_hf.setBackgroundResource(R.drawable.bk_cccccc_2);
                myAdapter_huifang.notifyDataSetChanged();
            }
        });
        tv_zhibo_hf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                query_hf();
                tv_zhibo_dt.setTextColor(tv_zhibo_dt.getResources().getColor(R.color.hei333333));
                tv_zhibo_hf.setTextColor(tv_zhibo_hf.getResources().getColor(R.color.white));
                tv_zhibo_dt.setBackgroundResource(R.drawable.bk_cccccc_2);
                tv_zhibo_hf.setBackgroundResource(R.drawable.bj_theme_2);
                myAdapter_huifang.notifyDataSetChanged();
            }
        });
        tv_zhibo_zb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_zhibo_lt_lbyy.setVisibility(GONE);
                ll_zhibo_jj.setVisibility(VISIBLE);
                fl_zhibo_pinglun.setVisibility(GONE);
                type_zl = 0;
                tv_zhibo_zb.setTextColor(tv_zhibo_zb.getResources().getColor(R.color.hei333333));
                tv_zhibo_zb.setTextSize(18);//textStyle
                TextPaint paint_zb = tv_zhibo_zb.getPaint();
                paint_zb.setFakeBoldText(true);
                tv_zhibo_lt.setTextColor(tv_zhibo_lt.getResources().getColor(R.color.hui999999));
                tv_zhibo_lt.setTextSize(16);
                TextPaint paint_lt = tv_zhibo_lt.getPaint();
                paint_lt.setFakeBoldText(false);
            }
        });
        tv_zhibo_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query_pl();
                tv_zhibo_lt_lbyy.setVisibility(VISIBLE);
                ll_zhibo_jj.setVisibility(GONE);
                fl_zhibo_pinglun.setVisibility(VISIBLE);
                type_zl = 1;
                tv_zhibo_zb.setTextColor(tv_zhibo_zb.getResources().getColor(R.color.hui999999));
                tv_zhibo_zb.setTextSize(16);//textStyle
                TextPaint paint_zb = tv_zhibo_zb.getPaint();
                paint_zb.setFakeBoldText(false);
                tv_zhibo_lt.setTextColor(tv_zhibo_lt.getResources().getColor(R.color.hei333333));
                tv_zhibo_lt.setTextSize(18);
                TextPaint paint_lt = tv_zhibo_lt.getPaint();
                paint_lt.setFakeBoldText(true);
            }
        });
        et_zhibo_pl = findViewById(R.id.et_zhibo_pl);

        et_zhibo_pl.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO) {

                        String sQueryText = et_zhibo_pl.getText().toString();
                        if (sQueryText.equals("")) {
                        } else {
                            dialogin("");
                            pl(sQueryText);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                        }

                }
                return false;
            }
        });
        // 播放器配置
        SuperPlayerGlobalConfig prefs = SuperPlayerGlobalConfig.getInstance();
        // 开启悬浮窗播放
        prefs.enableFloatWindow = true;
        // 设置悬浮窗的初始位置和宽高
        SuperPlayerGlobalConfig.TXRect rect = new SuperPlayerGlobalConfig.TXRect();
        rect.x = 0;
        rect.y = 0;
        rect.width = 810;
        rect.height = 540;
        prefs.floatViewRect = rect;
        // 播放器默认缓存个数
        prefs.maxCacheItem = 5;
        // 设置播放器渲染模式
        prefs.enableHWAcceleration = true;
        prefs.renderMode = TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION;
        query();
        query_pl();
        query_hf();
        pmld();
        ll_zhibo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoViewAway();
                mSuperPlayerView.resetPlayer();
                finish();

            }
        });
    }

    /**
     * 屏幕亮度
     */

    private void pmld() {
        try {
            //获取系统亮度
            int brightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            //设置当前屏幕亮度
            Window window = this.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            if (brightness == -1) {
                lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
            } else {
                lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
            }
            window.setAttributes(lp);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 直播评论信息获取
     */
    private void query_pl() {
        String url = Api.sUrl + "Broadcast/Api/videoChatList/appId/" + Api.sApp_Id
                + "/video_id/" + id + "/page/" + lt_page;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                ll_zhibo_gdjz.setVisibility(GONE);
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        JSONArray jsonArrayview = jsonObject1.getJSONArray("data");
                        if (lt_page == 1) {
                            mylist_view_pl = new ArrayList<HashMap<String, String>>();
                        }
                        for (int i = 0; i < jsonArrayview.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArrayview.opt(i);
                            String Itemcontent = jsonObject2.getString("content");
                            String Itemadd_time = jsonObject2.getString("add_time");
                            String Itemnickname = jsonObject2.getString("user_name");
                            String Itemheadimgurl = jsonObject2.getString("user_logo");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("content", Itemcontent);
                            map.put("add_time", Itemadd_time);
                            map.put("nickname", Itemnickname);
                            map.put("headimgurl", Itemheadimgurl);
                            mylist_view_pl.add(map);
                        }
                        if (lt_page > 1) {
                            if (jsonArrayview.length() == 0) {
                                lt_page -= 1;
                            }
                        }
                        // setGridView_fenlei(mylist_banner);
                        gv_huifang();

                    } else {
                        if (lt_page > 1) {
                            lt_page -= 1;
                        }
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
                ll_zhibo_gdjz.setVisibility(GONE);
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }


    /**
     * 直播评论信息
     */
    private void pl(String content) {
        String url = Api.sUrl + "Broadcast/Api/videoChat/appId/" + Api.sApp_Id
                + "/video_id/" + id + "/user_id/" + sUser_id + "/content/" + content;
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
                        et_zhibo_pl.setText("");
                        query_pl();
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


    /**
     * 直播信息获取
     */
    private void query() {
        String url = Api.sUrl + "Broadcast/Api/videoInfo/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/video_id/" + id;
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
                        JSONObject jsonObjectfind = new JSONObject(jsonObjectdata.getString("find"));
                        String resultid = jsonObjectfind.getString("id");
                        String resulttitle = jsonObjectfind.getString("title");
                        String resultvideo_address = jsonObjectfind.getString("video_address");
                        String resultcreate_time = jsonObjectfind.getString("create_time");
                        String resultbrief = jsonObjectfind.getString("brief");
                        String resultplay_cont = jsonObjectfind.getString("play_cont");
                        // 通过URL方式的视频信息配置
                        SuperPlayerModel model2 = new SuperPlayerModel();
                        model2.title = resulttitle;
                        model2.url =Api.sUrl+ resultvideo_address;
                        //model2.url = "http://1252463788.vod2.myqcloud.com/95576ef5vodtransgzp1252463788/68e3febf4564972819220421305/v.f30.mp4";
                        // 开始播放
                        tv_zhibo_brief.setText(resultbrief);
                        mSuperPlayerView.playWithModel(model2);
                        JSONArray jsonArrayview = jsonObjectdata.getJSONArray("view");
                        ArrayList<HashMap<String, String>> mylist_view = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < jsonArrayview.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArrayview.opt(i);
                            String Itemheadimgurl = jsonObject2.getString("headimgurl");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("headimgurl", Itemheadimgurl);
                            mylist_view.add(map);
                        }

                        setGridViewt_gk(mylist_view);

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
     * 直播回放信息获取
     */
    private void query_hf() {
        String url = Api.sUrl + "Broadcast/Api/videoPlayBack/appId/" + Api.sApp_Id
                + "/page/" + hf_page;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                ll_zhibo_gdjz.setVisibility(GONE);
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        JSONArray jsonArrayall = jsonObject1.getJSONArray("data");
                        if (hf_page == 1) {
                            mylist_all_hf = new ArrayList<HashMap<String, String>>();
                        }
                        for (int i = 0; i < jsonArrayall.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArrayall.opt(i);
                            String Itemid = jsonObject2.getString("id");
                            String Itemtitle = jsonObject2.getString("title");
                            String Itemvideo_img = jsonObject2.getString("video_img");
                            String Itemvideo_address = jsonObject2.getString("video_address");
                            String Itemcreate_time = jsonObject2.getString("create_time");
                            String Itemplay_cont = jsonObject2.getString("play_cont");
                            String Itemcount = jsonObject2.getString("count(c.id)");
                            String Itemvideo_time = jsonObject2.getString("video_time");
                            String Itemupload_time_h = jsonObject2.getString("upload_time_h");

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", Itemid);
                            map.put("title", Itemtitle);
                            map.put("video_img", Itemvideo_img);
                            map.put("video_address", Itemvideo_address);
                            map.put("create_time", Itemcreate_time);
                            map.put("play_cont", Itemplay_cont);
                            map.put("count(c.id)", Itemcount);
                            map.put("upload_time_h", Itemupload_time_h);
                            map.put("video_time", Itemvideo_time);
                            mylist_all_hf.add(map);
                        }
                        if (hf_page > 1) {
                            if (jsonArrayall.length() == 0) {
                                hf_page -= 1;
                            }
                        }

                        type_dh = 0;
                        gv_huifang();
                    } else {
                        if (hf_page > 1) {
                            hf_page -= 1;
                        }
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
                ll_zhibo_gdjz.setVisibility(GONE);
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }

    /**
     * 直播动态信息获取
     */
    private void query_dt() {
        String url = Api.sUrl + "Broadcast/Api/videoDynamic/appId/" + Api.sApp_Id
                + "/page/" + dt_page + "/video_id/" + id;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                ll_zhibo_gdjz.setVisibility(GONE);
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        JSONArray jsonArrayall = jsonObject1.getJSONArray("data");
                        if (dt_page == 1) {
                            mylist_all_dt = new ArrayList<HashMap<String, String>>();
                        }
                        for (int i = 0; i < jsonArrayall.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArrayall.opt(i);
                            //商品链接URL
                            String ItemGoods_link = jsonObject2.getString("goods_link");
                            //上传时间
                            String ItemAdd_time = jsonObject2.getString("add_time");
                            //商品ID
                            String ItemGoods_id = jsonObject2.getString("goods_id");


                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("goods_link", ItemGoods_link);
                            map.put("add_time", ItemAdd_time);
                            map.put("goods_id", ItemGoods_id);

                            mylist_all_dt.add(map);
                        }
                        type_dh = 1;
                        gv_huifang();
                        if (dt_page > 1) {
                            if (jsonArrayall.length() == 0) {
                                dt_page -= 1;
                            }
                        }

                    } else {
                        if (dt_page > 1) {
                            dt_page -= 1;
                        }
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
                ll_zhibo_gdjz.setVisibility(GONE);
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }

    /**
     * 离开视频
     */
    private void videoViewAway() {
        String url = Api.sUrl + "Broadcast/Api/videoViewAway/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/video_id/" + id;
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
                        //  Hint(resultMsg, HintDialog.SUCCESS);
                    } else {
                        // Hint(resultMsg, HintDialog.ERROR);
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
        loadingDialog = new LoadingDialog(ZhiboActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(ZhiboActivity.this, R.style.dialog, sHint, type, true).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Log.d(TAG, "onKeyDown KEYCODE_BACK");
                //   showDialog();
                if (ll_zhibo_title.getVisibility() == GONE) {
                    ll_zhibo_title.setVisibility(VISIBLE);
                    mSuperPlayerView.playBack();
                } else if (ll_zhibo_title.getVisibility() == VISIBLE) {
                    videoViewAway();
                    mSuperPlayerView.resetPlayer();
                }
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


    /**
     * 设置GirdView参数，绑定数据
     * <p>
     * 他们在参与
     */
    private void setGridViewt_gk(ArrayList<HashMap<String, String>> mylist) {
        MyAdapter_gk myAdapter_gk = new MyAdapter_gk(ZhiboActivity.this);
   /*     ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemName", "");
            mylist.add(map);
        }*/
        myAdapter_gk.arrlist = mylist;
        int size = mylist.size();
        int length = 31;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        gv_zhibo_gk.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gv_zhibo_gk.setColumnWidth(itemWidth); // 设置列表项宽
        gv_zhibo_gk.setHorizontalSpacing(6); // 设置列表项水平间距
        gv_zhibo_gk.setStretchMode(GridView.NO_STRETCH);
        gv_zhibo_gk.setNumColumns(size); // 设置列数量=列表集合数
        gv_zhibo_gk.setAdapter(myAdapter_gk);
        mgv_zhibo_huifang.scrollTo(0, 0);
    }

    @Override
    public void onScrollToStart() {
        //Toast.makeText(this, "滑动到顶部了", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onScrollToEnd() {
        //  Toast.makeText(this, "滑动到底部了", Toast.LENGTH_SHORT).show();

        if (type_zl == 0) {
            if (type_dh == 0) {
                hf_page += 1;
                query_hf();
            } else if (type_dh == 1) {
                dt_page += 1;
                query_dt();
            }
        } else if (type_zl == 1) {
            lt_page += 1;
            query_pl();
        }

        ll_zhibo_gdjz.setVisibility(VISIBLE);

/*        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ll_zhibo_gdjz.setVisibility(GONE);
            }
        }, 5000);*/
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 重新开始播放
        if (mSuperPlayerView.getPlayState() == SuperPlayerConst.PLAYSTATE_PLAY) {
            mSuperPlayerView.onResume();
            if (mSuperPlayerView.getPlayMode() == SuperPlayerConst.PLAYMODE_FLOAT) {
                mSuperPlayerView.requestPlayMode(SuperPlayerConst.PLAYMODE_WINDOW);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 停止播放
        if (mSuperPlayerView.getPlayMode() != SuperPlayerConst.PLAYMODE_FLOAT) {
            mSuperPlayerView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放
        mSuperPlayerView.release();
        if (mSuperPlayerView.getPlayMode() != SuperPlayerConst.PLAYMODE_FLOAT) {
            mSuperPlayerView.resetPlayer();
        }
    }

    @Override
    public void onStartFullScreenPlay() {
        // 隐藏其他元素实现全屏
        ll_zhibo_title.setVisibility(GONE);
/*        if (mIvAdd != null) {
            mIvAdd.setVisibility(GONE);
        }*/
    }

    @Override
    public void onStopFullScreenPlay() {
        // 恢复原有元素
        ll_zhibo_title.setVisibility(VISIBLE);
   /*     if (mIvAdd != null) {
            mIvAdd.setVisibility(VISIBLE);
        }*/
    }

    @Override
    public void onClickFloatCloseBtn() {

    }

    @Override
    public void onClickSmallReturnBtn() {

    }

    @Override
    public void onStartFloatWindowPlay() {

    }

    @Override
    public void onRefresh() {

    }

    /**
     * 加入
     */
    private void gv_huifang() {
        myAdapter_huifang = new MyAdapter_huifang(ZhiboActivity.this);
        mgv_zhibo_huifang.setAdapter(myAdapter_huifang);
    }


    private class MyAdapter_gk extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;


        public MyAdapter_gk(Context context) {
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
                view = inflater.inflate(R.layout.zhibo_gk_item, null);
            }
            CircleImageView iv_zhibo_gk_item = view.findViewById(R.id.iv_zhibo_gk_item);
            Glide.with(ZhiboActivity.this)
                    .load( Api.sUrl+ arrlist.get(position).get("headimgurl"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_zhibo_gk_item);
            //map.put("headimgurl", Itemheadimgurl);
            return view;
        }
    }

    private class MyAdapter_huifang extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;


        public MyAdapter_huifang(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            new ArrayList<HashMap<String, String>>();


        }

        @Override
        public int getCount() {
            int size = 0;
            // TODO Auto-generated method stub
            if (type_zl == 0) {
                if (type_dh == 0) {
                    size = mylist_all_hf.size();
                } else if (type_dh == 1) {
                    size = mylist_all_dt.size();
                }
            } else if (type_zl == 1) {
                size = mylist_view_pl.size();
            }
            return size;
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
            if (type_zl == 0) {
                if (type_dh == 0) {
                    view = inflater.inflate(R.layout.zhibo_huifang_item, null);
                    TextView tv_zhibo_huifang_item_create_time = view.findViewById(R.id.tv_zhibo_huifang_item_create_time);
                    TextView tv_zhibo_huifang_item_title = view.findViewById(R.id.tv_zhibo_huifang_item_title);
                    ImageView iv_zhibo_huifang_item_video_img = view.findViewById(R.id.iv_zhibo_huifang_item_video_img);
                    TextView tv_zhibo_huifang_item_play_cont = view.findViewById(R.id.tv_zhibo_huifang_item_play_cont);
                    TextView tv_zhibo_huifang_item_count = view.findViewById(R.id.tv_zhibo_huifang_item_count);
                    TextView tv_zhibo_huifang_item_time = view.findViewById(R.id.tv_zhibo_huifang_item_time);
                    TextView tv_zhibo_huifang_item_upload_time_h = view.findViewById(R.id.tv_zhibo_huifang_item_upload_time_h);
                    tv_zhibo_huifang_item_create_time.setText(mylist_all_hf.get(position).get("create_time"));
                    tv_zhibo_huifang_item_title.setText(mylist_all_hf.get(position).get("title"));
                    tv_zhibo_huifang_item_upload_time_h.setText(mylist_all_hf.get(position).get("upload_time_h"));
                    Glide.with(ZhiboActivity.this)
                            .load( Api.sUrl+ mylist_all_hf.get(position).get("video_img"))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_zhibo_huifang_item_video_img);
                    tv_zhibo_huifang_item_play_cont.setText(mylist_all_hf.get(position).get("play_cont"));
                    tv_zhibo_huifang_item_count.setText(mylist_all_hf.get(position).get("count(c.id)"));
                    tv_zhibo_huifang_item_time.setText(mylist_all_hf.get(position).get("video_time"));
                    iv_zhibo_huifang_item_video_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mSuperPlayerView.resetPlayer();
                            Intent intent = new Intent(ZhiboActivity.this, ZhiboActivity.class);
                            intent.putExtra("id", mylist_all_hf.get(position).get("id"));
                            startActivity(intent);
                        }
                    });

                } else if (type_dh == 1) {
                    view = inflater.inflate(R.layout.zhibo_dongtai_item, null);
                    TextView tv_zhibo_dongtai = view.findViewById(R.id.tv_zhibo_dongtai);
                    tv_zhibo_dongtai.setText(mylist_all_dt.get(position).get("goods_link"));
                    tv_zhibo_dongtai.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                   /*         dialogin("");
                            jianjei(mylist_all_dt.get(position).get("goods_id"));*/
                            Intent intent = new Intent(ZhiboActivity.this, GuanggaoActivity.class);
                            intent.putExtra("link", mylist_all_dt.get(position).get("goods_link"));
                            startActivity(intent);
                        }
                    });

                }
            } else if (type_zl == 1) {
                view = inflater.inflate(R.layout.zhibo_liaotian_item, null);
                CircleImageView iv_zhibo_lt_headimgurl = view.findViewById(R.id.iv_zhibo_lt_headimgurl);
                TextView tv_zhibo_lt_nickname = view.findViewById(R.id.tv_zhibo_lt_nickname);
                TextView tv_zhibo_lt_content = view.findViewById(R.id.tv_zhibo_lt_content);
                Glide.with(ZhiboActivity.this)
                        .load( Api.sUrl+ mylist_view_pl.get(position).get("headimgurl"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_zhibo_lt_headimgurl);
                tv_zhibo_lt_nickname.setText(mylist_view_pl.get(position).get("nickname"));
                tv_zhibo_lt_content.setText(mylist_view_pl.get(position).get("content"));
        /*        map.put("content", Itemcontent);
                map.put("add_time", Itemadd_time);
                map.put("nickname", Itemnickname);
                map.put("headimgurl", Itemheadimgurl);*/
            }
            return view;
        }
    }

    private void jianjei(final String pid) {
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
                        Intent intent1 = new Intent(ZhiboActivity.this, SpxqActivity.class);
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
        queue.add(request);
    }

}
