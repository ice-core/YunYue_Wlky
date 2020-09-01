package com.example.administrator.yunyue;


import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.example.administrator.yunyue.Im.ConversationListAdapterEx;
import com.example.administrator.yunyue.Im.Friend;
import com.example.administrator.yunyue.Im.HaoyouActivity;
import com.example.administrator.yunyue.dialog.PromptDialog;
import com.example.administrator.yunyue.erci.activity.CwhyActivity;
import com.example.administrator.yunyue.fjsc_activity.Fjsc_ShouyeActivity;
import com.example.administrator.yunyue.fragment.GrzxFragment;
import com.example.administrator.yunyue.fragment.LiaoTianFragment;
import com.example.administrator.yunyue.fragment.NewsFragmentPagerAdapter;
import com.example.administrator.yunyue.fragment.ShequnFragment;
import com.example.administrator.yunyue.fragment.Shouye_TwoFragment;
import com.example.administrator.yunyue.fragment.Shouye_TwoFragment_Jyx;
import com.example.administrator.yunyue.fragment.TabFragment;
import com.example.administrator.yunyue.fragment.ZhiboFragment;
import com.example.administrator.yunyue.fragment.ZixunFragment;
import com.example.administrator.yunyue.jysq_activity.Jysq_ShouyeFragment;
import com.example.administrator.yunyue.sc_fragment.FenleiFragment;
import com.example.administrator.yunyue.sc_fragment.Shouye2Fragment;
import com.example.administrator.yunyue.sc_fragment.WodeFragment;
import com.example.administrator.yunyue.service.LocationService;
import com.example.administrator.yunyue.sq_activity.TjhyActivity;
import com.example.administrator.yunyue.yjdt_activity.FenleiLiebiaoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

import static com.example.administrator.yunyue.fjsc_activity.Fjsc_ShouyeActivity.ISQUERY;


public class MainActivity_lkdqsc extends AppCompatActivity implements View.OnClickListener,
        Shouye_TwoFragment.OnFragmentInteractionListener, Shouye_TwoFragment_Jyx.OnFragmentInteractionListener,
        ZixunFragment.OnFragmentInteractionListener, Shouye2Fragment.OnFragmentInteractionListener,
        ZhiboFragment.OnFragmentInteractionListener, ShequnFragment.OnFragmentInteractionListener,
        Jysq_ShouyeFragment.OnFragmentInteractionListener, LiaoTianFragment.OnFragmentInteractionListener,
        FenleiFragment.OnFragmentInteractionListener,
        WodeFragment.OnFragmentInteractionListener, TabFragment.OnFragmentInteractionListener, RongIM.UserInfoProvider, RongIM.GroupInfoProvider {

    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    Shouye_TwoFragment shouye_twoFragment;
    Shouye_TwoFragment_Jyx shouye_twoFragment_jyx;
    ZixunFragment zixunfragment;
    Shouye2Fragment shouye2Fragment;
    ZhiboFragment zhibofragment;
    ShequnFragment shequnfragment;
    Jysq_ShouyeFragment jysq_shouyeFragment;
    FenleiFragment fenleiFragment;
    LiaoTianFragment liaotianfragment;
    WodeFragment wodefragment;
    GrzxFragment grzxFragment;
    private ViewPager viewPager;
    private SharedPreferences pref;
    private static final String TAG = MainActivity.class.getSimpleName();
    private LinearLayout ll_zixun, ll_fenlei, ll_shangcheng, ll_zhibo, ll_shequn, ll_liaotian, ll_wode;
    private ImageView ivzixun, ivfenlei, ivshangcheng, ivzhibo, ivshequn, ivliaotian, ivwode;
    private TextView tvzixun, tvfenlei, tvshangcheng, tvzhibo, tvshequn, tvliaotian, tvwode, tvCurrent;
    int iFrragment = 0;
    int iId = 0;

    private String sShangchen = "";
    private String sToken = "";
    private boolean isDebug;
    /**
     * 会话列表的fragment
     */
    private ConversationListFragment mConversationListFragment = null;
    private Conversation.ConversationType[] mConversationsTypes = null;

    private List<Friend> userIdList;
    RequestQueue queue = null;
    private String sUser_id, sHead_pic, sNickname;
    private LinearLayout ll_main_hint;
    /**
     * 好友
     */
    private LinearLayout ll_xiaoxi_hy;
    /**
     * 添加好友
     */
    private LinearLayout ll_xiaoxi_tjhy;

    private LocationService locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_main_jyx);

        isDebug = getSharedPreferences("config", MODE_PRIVATE).getBoolean("isDebug", false);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sShangchen = pref.getString("shangchen", "");
        sToken = pref.getString("token", "");
        queue = Volley.newRequestQueue(MainActivity_lkdqsc.this);
        sUser_id = pref.getString("user_id", "");
        sHead_pic = pref.getString("head_pic", "");
        sNickname = pref.getString("nickname", "");
        connect(sToken);
        Intent intent = getIntent();
        String sId = intent.getStringExtra("ID");
        if (sId == null) {
        } else if (sId.equals("")) {

        } else {
            iId = Integer.valueOf(sId);
        }
        Button bt = findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_lkdqsc.this, ConversationListActivity.class);
                startActivity(intent);
            }
        });
        ll_main_hint = findViewById(R.id.ll_main_hint);
        initView();
        initData();
        changeTab(iId);
        viewPager.setCurrentItem(iId);

        ll_xiaoxi_hy = findViewById(R.id.ll_xiaoxi_hy);
        ll_xiaoxi_hy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_lkdqsc.this, HaoyouActivity.class);
                startActivity(intent);
            }
        });
        ll_xiaoxi_tjhy = findViewById(R.id.ll_xiaoxi_tjhy);
        ll_xiaoxi_tjhy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_lkdqsc.this, TjhyActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public UserInfo getUserInfo(String s) {
        query(s);
        return null;
    }

    @Override
    public Group getGroupInfo(String s) {
        query_sq(s);
        return null;
    }

    /**
     * 社群
     */
    private void query_sq(String sGroup_Id) {
        String url = Api.sUrl + "Community/Api/groupDetails/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/group_id/" + sGroup_Id;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
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
                        String sName = jsonArraygroup.getString("name");
                        //群聊头像
                        String sLogo = jsonArraygroup.getString("logo");
                        Group groupInfo;
                        groupInfo = new Group(sId, sName, Uri.parse(Api.sUrl + sLogo));
                        RongIM.getInstance().refreshGroupInfoCache(groupInfo);

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }

    /**
     * 好友
     */
    private void query(String friend_id) {
        String url = Api.sUrl
                + "Community/Api/findFriends/appId/" + Api.sApp_Id + "/user_id/" + sUser_id + "/friend_id/" + friend_id;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        JSONObject jsonObjectdate = jsonObject1.getJSONObject("data");
                        String resultId = jsonObjectdate.getString("id");
                        String resultNickname = jsonObjectdate.getString("nickname");
                        String resultHeadimgurl = jsonObjectdate.getString("headimgurl");
                        String resultSex = jsonObjectdate.getString("sex");
                        String resultMobile = jsonObjectdate.getString("mobile");
                        String resultInfo = jsonObjectdate.getString("info");
                        RongIM.getInstance().setCurrentUserInfo(new UserInfo(resultId, resultInfo, Uri.parse(Api.sUrl + resultHeadimgurl)));
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }

    /**
     * 连接融云服务器
     */
    private void connect(String token) {
        RongIMClient.connect(token, new RongIMClient.ConnectCallback() {

            /**
             *
             * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
             */
            @Override
            public void onTokenIncorrect() {
                Log.d(TAG, "Token 错误---onTokenIncorrect---" + '\n');
            }

            /**
             * 连接融云成功
             * @param userid 当前 token
             */
            @Override
            public void onSuccess(String userid) {
                Log.d(TAG, "连接融云成功---onSuccess---用户ID:" + userid + '\n');
                RongIM.setUserInfoProvider(MainActivity_lkdqsc.this, true);
                RongIM.setGroupInfoProvider(MainActivity_lkdqsc.this, true);
                //  query(userid);
                //  RongIM.getInstance().setCurrentUserInfo(new UserInfo(sUser_id, sNickname, Uri.parse(Api.sUrl + sHead_pic)));
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.d(TAG, "连接融云失败, 错误码: " + errorCode + '\n');
            }
        });
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
                showDialog();
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
     *  * 方法名：showDialog()
     *  * 功    能：退出消息确认
     *  * 参    数：无
     *  * 返回值：无
     */
    protected void showDialog() {
        PromptDialog pd = new PromptDialog(MainActivity_lkdqsc.this, R.style.dialog, "确定要退出吗?", new PromptDialog.OnClickListener() {
            @Override
            public void onCancelClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    dialog.dismiss();
                    //dialog.dismiss();
                }
            }

            @Override
            public void onConfimClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    dialog.dismiss();
                    int currentVersion = Build.VERSION.SDK_INT;
                    if (currentVersion > Build.VERSION_CODES.ECLAIR_MR1) {
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                        System.exit(0);
                    } else {// android2.1
                        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                        am.restartPackage(getPackageName());
                    }
                    // exit();
                    // forceStopAPK("com.example.administrator.yunyue");
                }

            }
        });
        pd.setPositiveName("是");
        pd.setNegativeName("否");
        pd.show();
    }

    /**
     *  * 方法名：initView()
     *  * 功    能：控件初始化
     *  * 参    数：无
     *  * 返回值：无
     */
    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ll_zixun = (LinearLayout) findViewById(R.id.ll_zixun);
        ll_fenlei = findViewById(R.id.ll_fenlei);
        ll_shangcheng = findViewById(R.id.ll_shangcheng);
        ll_zhibo = (LinearLayout) findViewById(R.id.ll_zhibo);
        ll_shequn = (LinearLayout) findViewById(R.id.ll_shequn);
        ll_liaotian = (LinearLayout) findViewById(R.id.ll_liaotian);
        ll_wode = (LinearLayout) findViewById(R.id.ll_wode);

        ll_zixun.setOnClickListener(this);
        ll_fenlei.setOnClickListener(this);
        ll_shangcheng.setOnClickListener(this);
        ll_zhibo.setOnClickListener(this);
        ll_shequn.setOnClickListener(this);
        ll_liaotian.setOnClickListener(this);
        ll_wode.setOnClickListener(this);

        ivzixun = (ImageView) findViewById(R.id.ivzixun);
        ivfenlei = findViewById(R.id.ivfenlei);
        ivshangcheng = findViewById(R.id.ivshangcheng);
        ivzhibo = (ImageView) findViewById(R.id.ivzhibo);
        ivshequn = (ImageView) findViewById(R.id.ivshequn);
        ivliaotian = (ImageView) findViewById(R.id.ivliaotian);
        ivwode = (ImageView) findViewById(R.id.ivwode);

        tvzixun = (TextView) findViewById(R.id.tvzixun);
        tvfenlei = findViewById(R.id.tvfenlei);
        tvshangcheng = findViewById(R.id.tvshangcheng);
        tvzhibo = (TextView) findViewById(R.id.tvzhibo);
        tvshequn = (TextView) findViewById(R.id.tvshequn);
        tvliaotian = (TextView) findViewById(R.id.tvliaotian);
        tvwode = (TextView) findViewById(R.id.tvwode);

        tvzixun.setSelected(true);
        tvCurrent = tvzixun;

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(6);
    }

    /**
     *  * 方法名：initData()
     *  * 功    能：fragment页面初始加载
     *  * 参    数：无
     *  * 返回值：无
     */
    private void initData() {
        Fragment conversationList = initConversationList();
        Bundle bundle = new Bundle();
        shouye_twoFragment = new Shouye_TwoFragment();
        shouye_twoFragment.setArguments(bundle);


        shouye_twoFragment_jyx = new Shouye_TwoFragment_Jyx();
        shouye_twoFragment_jyx.setArguments(bundle);

        fenleiFragment = new FenleiFragment();
        fenleiFragment.setArguments(bundle);

        zixunfragment = new ZixunFragment();
        zixunfragment.setArguments(bundle);

        shouye2Fragment = new Shouye2Fragment();
        shouye2Fragment.setArguments(bundle);

        zhibofragment = new ZhiboFragment();
        zhibofragment.setArguments(bundle);

        shequnfragment = new ShequnFragment();
        shequnfragment.setArguments(bundle);

        jysq_shouyeFragment = new Jysq_ShouyeFragment();
        jysq_shouyeFragment.setArguments(bundle);

        liaotianfragment = new LiaoTianFragment();
        liaotianfragment.setArguments(bundle);


        wodefragment = new WodeFragment();
        wodefragment.setArguments(bundle);

        grzxFragment = new GrzxFragment();
        grzxFragment.setArguments(bundle);

        fragments.add(shouye_twoFragment_jyx);
        fragments.add(fenleiFragment);
        fragments.add(zhibofragment);
        fragments.add(shequnfragment);
        /*        fragments.add(conversationList);*/
        fragments.add(grzxFragment);

        pref = PreferenceManager.getDefaultSharedPreferences(MainActivity_lkdqsc.this);
        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);

        viewPager.setAdapter(mAdapetr);
    }

    private Fragment initConversationList() {
        if (mConversationListFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
            listFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
            Uri uri;
            if (isDebug) {
                uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "true") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//群组
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                        .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
                        .build();
                mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                        Conversation.ConversationType.GROUP,
                        Conversation.ConversationType.PUBLIC_SERVICE,
                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
                        Conversation.ConversationType.SYSTEM,
                        Conversation.ConversationType.DISCUSSION
                };

            } else {
                uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                        .build();
                mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                        Conversation.ConversationType.GROUP,
                        Conversation.ConversationType.PUBLIC_SERVICE,
                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
                        Conversation.ConversationType.SYSTEM
                };
            }
            listFragment.setUri(uri);
            mConversationListFragment = listFragment;
            return listFragment;
        } else {
            return mConversationListFragment;
        }
    }

    /**
     *  * 方法名： onClick(View view)
     *  * 功    能：页面切换
     *  * 参    数：View v - 按钮的View
     *  * 返回值：无
     */
    @Override
    public void onClick(View view) {
        changeTab(view.getId());
    }

    /**
     *  * 方法名：changeTab(int id)
     *  * 功    能：页面切换
     *  * 参    数：int id --页面的id
     *  * 返回值：无
     */
    private void changeTab(int id) {
        iFrragment = id;
        tvCurrent.setSelected(false);
        switch (id) {
            case R.id.ll_zixun:
                ivzixun.setImageResource(R.mipmap.sy);
                ivfenlei.setImageResource(R.mipmap.icon_fenlei_new);
                ivzhibo.setImageResource(R.drawable.tab_live_normal_3x);
                ivshequn.setImageResource(R.drawable.tab_community_normal_3x);
                ivwode.setImageResource(R.drawable.tab_personal_center_normal_3x);
                tvzixun.setTextColor(tvzixun.getResources().getColor(R.color.theme));
                tvfenlei.setTextColor(tvfenlei.getResources().getColor(R.color.sy_hui));
                tvzhibo.setTextColor(tvzhibo.getResources().getColor(R.color.sy_hui));
                tvshequn.setTextColor(tvshequn.getResources().getColor(R.color.sy_hui));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.sy_hui));
                ll_main_hint.setVisibility(View.GONE);
                viewPager.setCurrentItem(0);
            case 0:
                ivzixun.setImageResource(R.mipmap.sy);
                ivfenlei.setImageResource(R.mipmap.icon_fenlei_new);
                ivzhibo.setImageResource(R.drawable.tab_live_normal_3x);
                ivshequn.setImageResource(R.drawable.tab_community_normal_3x);
                ivwode.setImageResource(R.drawable.tab_personal_center_normal_3x);
                tvzixun.setTextColor(tvzixun.getResources().getColor(R.color.theme));
                tvfenlei.setTextColor(tvfenlei.getResources().getColor(R.color.sy_hui));
                tvzhibo.setTextColor(tvzhibo.getResources().getColor(R.color.sy_hui));
                tvshequn.setTextColor(tvshequn.getResources().getColor(R.color.sy_hui));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.sy_hui));
                ll_main_hint.setVisibility(View.GONE);
                tvCurrent = tvzixun;
                break;
            case R.id.ll_fenlei:
                ivzixun.setImageResource(R.drawable.tab_home_normal_3x);
                ivfenlei.setImageResource(R.mipmap.icon_fenlei);
                ivzhibo.setImageResource(R.drawable.tab_live_normal_3x);
                ivshequn.setImageResource(R.drawable.tab_community_normal_3x);
                ivliaotian.setImageResource(R.drawable.tab_chatting_normal_3x);
                ivwode.setImageResource(R.drawable.tab_personal_center_normal_3x);
                tvzixun.setTextColor(tvzixun.getResources().getColor(R.color.sy_hui));
                tvfenlei.setTextColor(tvfenlei.getResources().getColor(R.color.theme));
                tvzhibo.setTextColor(tvzhibo.getResources().getColor(R.color.sy_hui));
                tvshequn.setTextColor(tvshequn.getResources().getColor(R.color.sy_hui));
                tvliaotian.setTextColor(tvliaotian.getResources().getColor(R.color.sy_hui));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.sy_hui));
                ll_main_hint.setVisibility(View.GONE);
                viewPager.setCurrentItem(1);
            case 1:
                ivzixun.setImageResource(R.drawable.tab_home_normal_3x);
                ivfenlei.setImageResource(R.mipmap.icon_fenlei);
                ivzhibo.setImageResource(R.drawable.tab_live_normal_3x);
                ivshequn.setImageResource(R.drawable.tab_community_normal_3x);
                ivliaotian.setImageResource(R.drawable.tab_chatting_normal_3x);
                ivwode.setImageResource(R.drawable.tab_personal_center_normal_3x);
                tvzixun.setTextColor(tvzixun.getResources().getColor(R.color.sy_hui));
                tvfenlei.setTextColor(tvfenlei.getResources().getColor(R.color.theme));
                tvzhibo.setTextColor(tvzhibo.getResources().getColor(R.color.sy_hui));
                tvshequn.setTextColor(tvshequn.getResources().getColor(R.color.sy_hui));
                tvliaotian.setTextColor(tvliaotian.getResources().getColor(R.color.sy_hui));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.sy_hui));
                ll_main_hint.setVisibility(View.GONE);
                tvCurrent = tvzhibo;
                break;
            case R.id.ll_zhibo:
                ivzixun.setImageResource(R.drawable.tab_home_normal_3x);
                ivfenlei.setImageResource(R.mipmap.icon_fenlei_new);
                ivzhibo.setImageResource(R.mipmap.zb);
                ivshequn.setImageResource(R.drawable.tab_community_normal_3x);
                ivliaotian.setImageResource(R.drawable.tab_chatting_normal_3x);
                ivwode.setImageResource(R.drawable.tab_personal_center_normal_3x);
                tvzixun.setTextColor(tvzixun.getResources().getColor(R.color.sy_hui));
                tvfenlei.setTextColor(tvfenlei.getResources().getColor(R.color.sy_hui));
                tvzhibo.setTextColor(tvzhibo.getResources().getColor(R.color.theme));
                tvshequn.setTextColor(tvshequn.getResources().getColor(R.color.sy_hui));
                tvliaotian.setTextColor(tvliaotian.getResources().getColor(R.color.sy_hui));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.sy_hui));
                ll_main_hint.setVisibility(View.GONE);
                viewPager.setCurrentItem(2);
            case 2:
                ivzixun.setImageResource(R.drawable.tab_home_normal_3x);
                ivfenlei.setImageResource(R.mipmap.icon_fenlei_new);
                ivzhibo.setImageResource(R.mipmap.zb);
                ivshequn.setImageResource(R.drawable.tab_community_normal_3x);
                ivliaotian.setImageResource(R.drawable.tab_chatting_normal_3x);
                ivwode.setImageResource(R.drawable.tab_personal_center_normal_3x);
                tvzixun.setTextColor(tvzixun.getResources().getColor(R.color.sy_hui));
                tvfenlei.setTextColor(tvfenlei.getResources().getColor(R.color.sy_hui));
                tvzhibo.setTextColor(tvzhibo.getResources().getColor(R.color.theme));
                tvshequn.setTextColor(tvshequn.getResources().getColor(R.color.sy_hui));
                tvliaotian.setTextColor(tvliaotian.getResources().getColor(R.color.sy_hui));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.sy_hui));
                ll_main_hint.setVisibility(View.GONE);
                tvCurrent = tvzhibo;
                break;
            case R.id.ll_shequn:
                ivzixun.setImageResource(R.drawable.tab_home_normal_3x);
                ivfenlei.setImageResource(R.mipmap.icon_fenlei_new);
                ivzhibo.setImageResource(R.drawable.tab_live_normal_3x);
                ivshequn.setImageResource(R.mipmap.sq);
                ivliaotian.setImageResource(R.drawable.tab_chatting_normal_3x);
                ivwode.setImageResource(R.drawable.tab_personal_center_normal_3x);
                tvzixun.setTextColor(tvzixun.getResources().getColor(R.color.sy_hui));
                tvfenlei.setTextColor(tvfenlei.getResources().getColor(R.color.sy_hui));
                tvzhibo.setTextColor(tvzhibo.getResources().getColor(R.color.sy_hui));
                tvshequn.setTextColor(tvshequn.getResources().getColor(R.color.theme));
                tvliaotian.setTextColor(tvliaotian.getResources().getColor(R.color.sy_hui));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.sy_hui));
                ll_main_hint.setVisibility(View.GONE);
                viewPager.setCurrentItem(3);
            case 3:
                ivzixun.setImageResource(R.drawable.tab_home_normal_3x);
                ivfenlei.setImageResource(R.mipmap.icon_fenlei_new);
                ivzhibo.setImageResource(R.drawable.tab_live_normal_3x);
                ivshequn.setImageResource(R.mipmap.sq);
                ivliaotian.setImageResource(R.drawable.tab_chatting_normal_3x);
                ivwode.setImageResource(R.drawable.tab_personal_center_normal_3x);
                tvzixun.setTextColor(tvzixun.getResources().getColor(R.color.sy_hui));
                tvfenlei.setTextColor(tvfenlei.getResources().getColor(R.color.sy_hui));
                tvzhibo.setTextColor(tvzhibo.getResources().getColor(R.color.sy_hui));
                tvshequn.setTextColor(tvshequn.getResources().getColor(R.color.theme));
                tvliaotian.setTextColor(tvliaotian.getResources().getColor(R.color.sy_hui));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.sy_hui));
                ll_main_hint.setVisibility(View.GONE);
                tvCurrent = tvshequn;
                break;
/*            case R.id.ll_liaotian:
                ivzixun.setImageResource(R.drawable.tab_home_normal_3x);
                ivshangcheng.setImageResource(R.drawable.tab_shop_normal_3x);
                ivzhibo.setImageResource(R.drawable.tab_live_normal_3x);
                ivshequn.setImageResource(R.drawable.tab_community_normal_3x);
                ivliaotian.setImageResource(R.mipmap.im);
                ivwode.setImageResource(R.drawable.tab_personal_center_normal_3x);
                tvzixun.setTextColor(tvzixun.getResources().getColor(R.color.sy_hui));
                tvshangcheng.setTextColor(tvshangcheng.getResources().getColor(R.color.sy_hui));
                tvzhibo.setTextColor(tvzhibo.getResources().getColor(R.color.sy_hui));
                tvshequn.setTextColor(tvshequn.getResources().getColor(R.color.sy_hui));
                tvliaotian.setTextColor(tvliaotian.getResources().getColor(R.color.theme));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.sy_hui));
                ll_main_hint.setVisibility(View.GONE);
                viewPager.setCurrentItem(3);
            case 3:
                ivzixun.setImageResource(R.drawable.tab_home_normal_3x);
                ivshangcheng.setImageResource(R.drawable.tab_shop_normal_3x);
                ivzhibo.setImageResource(R.drawable.tab_live_normal_3x);
                ivshequn.setImageResource(R.drawable.tab_community_normal_3x);
                ivliaotian.setImageResource(R.mipmap.im);
                ivwode.setImageResource(R.drawable.tab_personal_center_normal_3x);
                tvzixun.setTextColor(tvzixun.getResources().getColor(R.color.sy_hui));
                tvshangcheng.setTextColor(tvshangcheng.getResources().getColor(R.color.sy_hui));
                tvzhibo.setTextColor(tvzhibo.getResources().getColor(R.color.sy_hui));
                tvshequn.setTextColor(tvshequn.getResources().getColor(R.color.sy_hui));
                tvliaotian.setTextColor(tvliaotian.getResources().getColor(R.color.theme));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.sy_hui));
                ll_main_hint.setVisibility(View.GONE);
                tvCurrent = tvshequn;
                break;*/
            case R.id.ll_wode:
                ivzixun.setImageResource(R.drawable.tab_home_normal_3x);
                ivfenlei.setImageResource(R.mipmap.icon_fenlei_new);
                ivzhibo.setImageResource(R.drawable.tab_live_normal_3x);
                ivshequn.setImageResource(R.drawable.tab_community_normal_3x);
                ivliaotian.setImageResource(R.drawable.tab_chatting_normal_3x);
                ivwode.setImageResource(R.mipmap.wd);
                tvzixun.setTextColor(tvzixun.getResources().getColor(R.color.sy_hui));
                tvfenlei.setTextColor(tvfenlei.getResources().getColor(R.color.sy_hui));
                tvzhibo.setTextColor(tvzhibo.getResources().getColor(R.color.sy_hui));
                tvshequn.setTextColor(tvshequn.getResources().getColor(R.color.sy_hui));
                tvliaotian.setTextColor(tvliaotian.getResources().getColor(R.color.sy_hui));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.theme));
                ll_main_hint.setVisibility(View.GONE);
                viewPager.setCurrentItem(4);
            case 4:
                ivzixun.setImageResource(R.drawable.tab_home_normal_3x);
                ivfenlei.setImageResource(R.mipmap.icon_fenlei_new);
                ivzhibo.setImageResource(R.drawable.tab_live_normal_3x);
                ivshequn.setImageResource(R.drawable.tab_community_normal_3x);
                ivliaotian.setImageResource(R.drawable.tab_chatting_normal_3x);
                ivwode.setImageResource(R.mipmap.wd);
                tvzixun.setTextColor(tvzixun.getResources().getColor(R.color.sy_hui));
                tvfenlei.setTextColor(tvfenlei.getResources().getColor(R.color.sy_hui));
                tvzhibo.setTextColor(tvzhibo.getResources().getColor(R.color.sy_hui));
                tvshequn.setTextColor(tvshequn.getResources().getColor(R.color.sy_hui));
                tvliaotian.setTextColor(tvliaotian.getResources().getColor(R.color.sy_hui));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.theme));
                ll_main_hint.setVisibility(View.GONE);
                tvCurrent = tvwode;
                break;
            default:
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * 百度定位
     * */
    /***
     * Stop location service
     */
    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener1); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // -----------location config ------------
        locationService = ((AppApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener1);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.start();// 定位SDK
    }

    /****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDAbstractLocationListener mListener1 = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");

                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                //   sLatitude = String.valueOf(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                //  sLongitude = String.valueOf(location.getLongitude());

                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市

                if (location.getCity() == null) {

                } else {
                    if (ISQUERY) {
                        ISQUERY = false;
                        //  sDz = String.valueOf(location.getDistrict());
                        Fjsc_ShouyeActivity.sHint = String.valueOf(location.getAddrStr());
                        CwhyActivity.address = String.valueOf(location.getCity());
                        FenleiLiebiaoActivity.address = String.valueOf(location.getCity());
                        //  sNearbyshopindexapp();
                    }//  tianqi(location.getCity());
                    //locationService.stop();
                }
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                //   logMsg(sb.toString());
            }
        }

    };
}
