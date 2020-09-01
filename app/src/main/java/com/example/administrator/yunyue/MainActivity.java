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
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.yunyue.dialog.PromptDialog;
import com.example.administrator.yunyue.fragment.NewsFragmentPagerAdapter;
import com.example.administrator.yunyue.fragment.TabFragment;
import com.example.administrator.yunyue.fragment.ZixunFragment;
import com.example.administrator.yunyue.sc_fragment.GouwucheFragment;
import com.example.administrator.yunyue.sc_fragment.Shouye1Fragment;
import com.example.administrator.yunyue.sc_fragment.Shouye2Fragment;
import com.example.administrator.yunyue.sc_fragment.ShouyeFragment;
import com.example.administrator.yunyue.sc_fragment.WodeFragment;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        ZixunFragment.OnFragmentInteractionListener, Shouye1Fragment.OnFragmentInteractionListener, Shouye2Fragment.OnFragmentInteractionListener,
        ShouyeFragment.OnFragmentInteractionListener, GouwucheFragment.OnFragmentInteractionListener, WodeFragment.OnFragmentInteractionListener,
        TabFragment.OnFragmentInteractionListener {

    private ArrayList<android.support.v4.app.Fragment> fragments = new ArrayList<android.support.v4.app.Fragment>();
    ZixunFragment zixunfragment;
    Shouye1Fragment shouye1Fragment;
    Shouye2Fragment shouye2Fragment;
    ShouyeFragment shouyeFragment;
    GouwucheFragment gouwuchefragment;
    WodeFragment wodefragment;
    private ViewPager viewPager;
    private SharedPreferences pref;
    private static final String TAG = MainActivity.class.getSimpleName();
    private LinearLayout ll_zixun, ll_shangcheng, ll_gouwuche, ll_wode;
    private ImageView ivzixun, ivshangcheng, ivgouwuche, ivwode;
    private TextView tvzixun, tvshangcheng, tvgouwuche, tvwode, tvCurrent;
    int iFrragment = 0;
    int iId = 0;
    private Fragment2Fragment fragment2Fragment;
    private String sShangchen = "";

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
        setContentView(R.layout.activity_main);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sShangchen = pref.getString("shangchen", "");

        Intent intent = getIntent();
        String sId = intent.getStringExtra("ID");
        if (sId == null) {
        } else if (sId.equals("")) {

        } else {
            iId = Integer.valueOf(sId);
        }
        initView();
        initData();
        changeTab(iId);
        viewPager.setCurrentItem(iId);
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
                if (iId == 2) {
                    finish();
                } else {
                    showDialog();
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
     *  * 方法名：showDialog()
     *  * 功    能：退出消息确认
     *  * 参    数：无
     *  * 返回值：无
     */
    protected void showDialog() {
        PromptDialog pd = new PromptDialog(MainActivity.this, R.style.dialog, "确定要退出吗?", new PromptDialog.OnClickListener() {
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
                    int currentVersion = android.os.Build.VERSION.SDK_INT;
                    if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
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
        ll_shangcheng = (LinearLayout) findViewById(R.id.ll_shangcheng);
        ll_gouwuche = (LinearLayout) findViewById(R.id.ll_gouwuche);
        ll_wode = (LinearLayout) findViewById(R.id.ll_wode);

        ll_zixun.setOnClickListener(this);
        ll_shangcheng.setOnClickListener(this);
        ll_gouwuche.setOnClickListener(this);
        ll_wode.setOnClickListener(this);

        ivzixun = (ImageView) findViewById(R.id.ivzixun);
        ivshangcheng = (ImageView) findViewById(R.id.ivshangcheng);
        ivgouwuche = (ImageView) findViewById(R.id.ivgouwuche);
        ivwode = (ImageView) findViewById(R.id.ivwode);

        tvzixun = (TextView) findViewById(R.id.tvzixun);
        tvshangcheng = (TextView) findViewById(R.id.tvshangcheng);
        tvgouwuche = (TextView) findViewById(R.id.tvgouwuche);
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
        viewPager.setOffscreenPageLimit(4);
    }

    /**
     *  * 方法名：initData()
     *  * 功    能：fragment页面初始加载
     *  * 参    数：无
     *  * 返回值：无
     */
    private void initData() {
        Bundle bundle = new Bundle();
        zixunfragment = new ZixunFragment();
        zixunfragment.setArguments(bundle);

        shouye1Fragment = new Shouye1Fragment();
        shouye1Fragment.setArguments(bundle);

        shouye2Fragment = new Shouye2Fragment();
        shouye2Fragment.setArguments(bundle);

        shouyeFragment = new ShouyeFragment();
        shouyeFragment.setArguments(bundle);

        gouwuchefragment = new GouwucheFragment();
        gouwuchefragment.setArguments(bundle);

        wodefragment = new WodeFragment();
        wodefragment.setArguments(bundle);

        fragments.add(zixunfragment);
  /*       if (sShangchen.equals("")) {
        } else if (sShangchen.equals("1")) {
            fragments.add(shouyeFragment);
        } else if (sShangchen.equals("2")) {
            fragments.add(shouye1Fragment);
        } else if (sShangchen.equals("3")) {
            fragments.add(shouye2Fragment);
        }*/
        fragments.add(shouye2Fragment);
        fragments.add(gouwuchefragment);
        //    fragments.add(zhibofragment);
        fragments.add(wodefragment);

        pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);

        viewPager.setAdapter(mAdapetr);
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
                ivzixun.setImageResource(R.drawable.tab_home_selected_3x);
                ivshangcheng.setImageResource(R.drawable.tab_shop_normal_3x);
                ivgouwuche.setImageResource(R.drawable.tab_gouwuche_normal_3x);
                ivwode.setImageResource(R.drawable.tab_community_normal_3x);
                tvzixun.setTextColor(tvzixun.getResources().getColor(R.color.theme));
                tvshangcheng.setTextColor(tvshangcheng.getResources().getColor(R.color.sy_hui));
                tvgouwuche.setTextColor(tvgouwuche.getResources().getColor(R.color.sy_hui));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.sy_hui));
                viewPager.setCurrentItem(0);
            case 0:
                ivzixun.setImageResource(R.drawable.tab_home_selected_3x);
                ivshangcheng.setImageResource(R.drawable.tab_shop_normal_3x);
                ivgouwuche.setImageResource(R.drawable.tab_gouwuche_normal_3x);
                ivwode.setImageResource(R.drawable.tab_community_normal_3x);
                tvzixun.setTextColor(tvzixun.getResources().getColor(R.color.theme));
                tvshangcheng.setTextColor(tvshangcheng.getResources().getColor(R.color.sy_hui));
                tvgouwuche.setTextColor(tvgouwuche.getResources().getColor(R.color.sy_hui));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.sy_hui));
                tvCurrent = tvzixun;
                break;

            case R.id.ll_shangcheng:
                ivzixun.setImageResource(R.drawable.tab_home_normal_3x);
                ivshangcheng.setImageResource(R.drawable.tab_shop_selected_3x);
                ivgouwuche.setImageResource(R.drawable.tab_gouwuche_normal_3x);
                ivwode.setImageResource(R.drawable.tab_community_normal_3x);
                tvzixun.setTextColor(tvzixun.getResources().getColor(R.color.sy_hui));
                tvshangcheng.setTextColor(tvshangcheng.getResources().getColor(R.color.theme));
                tvgouwuche.setTextColor(tvgouwuche.getResources().getColor(R.color.sy_hui));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.sy_hui));
                viewPager.setCurrentItem(1);
            case 1:
                ivzixun.setImageResource(R.drawable.tab_home_normal_3x);
                ivshangcheng.setImageResource(R.drawable.tab_shop_selected_3x);
                ivgouwuche.setImageResource(R.drawable.tab_gouwuche_normal_3x);
                ivwode.setImageResource(R.drawable.tab_community_normal_3x);
                tvzixun.setTextColor(tvzixun.getResources().getColor(R.color.sy_hui));
                tvshangcheng.setTextColor(tvshangcheng.getResources().getColor(R.color.theme));
                tvgouwuche.setTextColor(tvgouwuche.getResources().getColor(R.color.sy_hui));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.sy_hui));
                tvCurrent = tvshangcheng;
                break;
            case R.id.ll_gouwuche:
                ivzixun.setImageResource(R.drawable.tab_home_normal_3x);
                ivshangcheng.setImageResource(R.drawable.tab_shop_normal_3x);
                ivgouwuche.setImageResource(R.drawable.tab_gouwuche_selected_3x);
                ivwode.setImageResource(R.drawable.tab_community_normal_3x);
                tvzixun.setTextColor(tvzixun.getResources().getColor(R.color.sy_hui));
                tvshangcheng.setTextColor(tvshangcheng.getResources().getColor(R.color.sy_hui));
                tvgouwuche.setTextColor(tvgouwuche.getResources().getColor(R.color.theme));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.sy_hui));
                viewPager.setCurrentItem(2);
            case 2:
                ivzixun.setImageResource(R.drawable.tab_home_normal_3x);
                ivshangcheng.setImageResource(R.drawable.tab_shop_normal_3x);
                ivgouwuche.setImageResource(R.drawable.tab_gouwuche_selected_3x);
                ivwode.setImageResource(R.drawable.tab_community_normal_3x);
                tvzixun.setTextColor(tvzixun.getResources().getColor(R.color.sy_hui));
                tvshangcheng.setTextColor(tvshangcheng.getResources().getColor(R.color.sy_hui));
                tvgouwuche.setTextColor(tvgouwuche.getResources().getColor(R.color.theme));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.sy_hui));
                tvCurrent = tvgouwuche;
                break;

            case R.id.ll_wode:
                ivzixun.setImageResource(R.drawable.tab_home_normal_3x);
                ivshangcheng.setImageResource(R.drawable.tab_shop_normal_3x);
                ivgouwuche.setImageResource(R.drawable.tab_gouwuche_normal_3x);
                ivwode.setImageResource(R.drawable.tab_community_selected_3x);
                tvzixun.setTextColor(tvzixun.getResources().getColor(R.color.sy_hui));
                tvshangcheng.setTextColor(tvshangcheng.getResources().getColor(R.color.sy_hui));
                tvgouwuche.setTextColor(tvgouwuche.getResources().getColor(R.color.sy_hui));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.theme));
                viewPager.setCurrentItem(3);
            case 3:
                ivzixun.setImageResource(R.drawable.tab_home_normal_3x);
                ivshangcheng.setImageResource(R.drawable.tab_shop_normal_3x);
                ivgouwuche.setImageResource(R.drawable.tab_gouwuche_normal_3x);
                ivwode.setImageResource(R.drawable.tab_community_selected_3x);
                tvzixun.setTextColor(tvzixun.getResources().getColor(R.color.sy_hui));
                tvshangcheng.setTextColor(tvshangcheng.getResources().getColor(R.color.sy_hui));
                tvgouwuche.setTextColor(tvgouwuche.getResources().getColor(R.color.sy_hui));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.theme));
                tvCurrent = tvwode;
                break;
            default:
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    public interface Fragment2Fragment {
        public void gotoFragment(ViewPager viewPager);
    }

    String name = "";

    public void setStr(String name) {
        this.name = name;
    }

    public String getStr() {
        return name;
    }

    public void setFragment2Fragment(Fragment2Fragment fragment2Fragment) {
        this.fragment2Fragment = fragment2Fragment;
    }

    public void forSkip() {
        if (fragment2Fragment != null) {
            fragment2Fragment.gotoFragment(viewPager);
        }
    }

}
