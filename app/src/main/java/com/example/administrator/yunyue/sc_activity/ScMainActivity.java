package com.example.administrator.yunyue.sc_activity;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.dialog.PromptDialog;
import com.example.administrator.yunyue.fragment.NewsFragmentPagerAdapter;
import com.example.administrator.yunyue.sc_fragment.FenleiFragment;
import com.example.administrator.yunyue.sc_fragment.GouwucheFragment;
import com.example.administrator.yunyue.sc_fragment.ShouyeFragment;
import com.example.administrator.yunyue.sc_fragment.WodeFragment;

import java.util.ArrayList;

public class ScMainActivity extends AppCompatActivity implements View.OnClickListener, ShouyeFragment.OnFragmentInteractionListener,
        FenleiFragment.OnFragmentInteractionListener, GouwucheFragment.OnFragmentInteractionListener, WodeFragment.OnFragmentInteractionListener {
    private ViewPager viewPager;
    private static final String TAG = ScMainActivity.class.getSimpleName();
    private LinearLayout ll_shouye, ll_fenlei, ll_gouwuche, ll_wode;
    private ImageView ivshouye, ivfenlei, ivgouwuche, ivwode;
    private TextView tvshouye, tvfenlei, tvgouwuche, tvwode, tvCurrent;
    Fragment shouyefragment;
    Fragment fenleifragment;
    Fragment gouwuchefragment;
    Fragment wodefragment;
    private ArrayList<android.support.v4.app.Fragment> fragments = new ArrayList<android.support.v4.app.Fragment>();
    private SharedPreferences pref;
    int iFrragment = 0;
    int iId = 0;
    private Fragment2Fragment fragment2Fragment;

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

        setContentView(R.layout.activity_sc_main);
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
        PromptDialog pd = new PromptDialog(ScMainActivity.this, R.style.dialog, "确定要退出吗?", new PromptDialog.OnClickListener() {
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
                    finish();
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
        ll_shouye = (LinearLayout) findViewById(R.id.ll_shouye);
        ll_fenlei = (LinearLayout) findViewById(R.id.ll_fenlei);
        ll_gouwuche = (LinearLayout) findViewById(R.id.ll_gouwuche);
        ll_wode = (LinearLayout) findViewById(R.id.ll_wode);

        ll_shouye.setOnClickListener(this);
        ll_fenlei.setOnClickListener(this);
        ll_gouwuche.setOnClickListener(this);
        ll_wode.setOnClickListener(this);

        ivshouye = (ImageView) findViewById(R.id.ivshouye);
        ivfenlei = (ImageView) findViewById(R.id.ivfenlei);
        ivgouwuche = (ImageView) findViewById(R.id.ivgouwuche);
        ivwode = (ImageView) findViewById(R.id.ivwode);

        tvshouye = (TextView) findViewById(R.id.tvshouye);
        tvfenlei = (TextView) findViewById(R.id.tvfenlei);
        tvgouwuche = (TextView) findViewById(R.id.tvgouwuche);
        tvwode = (TextView) findViewById(R.id.tvwode);

        tvshouye.setSelected(true);
        tvCurrent = tvshouye;

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
        shouyefragment = new ShouyeFragment();
        shouyefragment.setArguments(bundle);

        fenleifragment = new FenleiFragment();
        fenleifragment.setArguments(bundle);

        gouwuchefragment = new GouwucheFragment();
        gouwuchefragment.setArguments(bundle);

        wodefragment = new WodeFragment();
        wodefragment.setArguments(bundle);

        fragments.add(shouyefragment);
        fragments.add(fenleifragment);
        fragments.add(gouwuchefragment);
        //    fragments.add(zhibofragment);
        fragments.add(wodefragment);

        pref = PreferenceManager.getDefaultSharedPreferences(ScMainActivity.this);

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
            case R.id.ll_shouye:
                ivshouye.setImageResource(R.mipmap.home_xz);
                ivfenlei.setImageResource(R.mipmap.qrcode);
                ivgouwuche.setImageResource(R.mipmap.shoppingcart);
                ivwode.setImageResource(R.mipmap.mine);
                tvshouye.setTextColor(tvshouye.getResources().getColor(R.color.theme));
                tvfenlei.setTextColor(tvfenlei.getResources().getColor(R.color.gray));
                tvgouwuche.setTextColor(tvgouwuche.getResources().getColor(R.color.gray));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.gray));
                viewPager.setCurrentItem(0);
            case 0:
                ivshouye.setImageResource(R.mipmap.home_xz);
                ivfenlei.setImageResource(R.mipmap.qrcode);
                ivgouwuche.setImageResource(R.mipmap.shoppingcart);
                ivwode.setImageResource(R.mipmap.mine);
                tvshouye.setTextColor(tvshouye.getResources().getColor(R.color.theme));
                tvfenlei.setTextColor(tvfenlei.getResources().getColor(R.color.gray));
                tvgouwuche.setTextColor(tvgouwuche.getResources().getColor(R.color.gray));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.gray));
                tvCurrent = tvshouye;
                break;

            case R.id.ll_fenlei:
                ivshouye.setImageResource(R.mipmap.home);
                ivfenlei.setImageResource(R.mipmap.qrcode_xz);
                ivgouwuche.setImageResource(R.mipmap.shoppingcart);
                ivwode.setImageResource(R.mipmap.mine);
                tvshouye.setTextColor(tvshouye.getResources().getColor(R.color.gray));
                tvfenlei.setTextColor(tvfenlei.getResources().getColor(R.color.theme));
                tvgouwuche.setTextColor(tvgouwuche.getResources().getColor(R.color.gray));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.gray));
                viewPager.setCurrentItem(1);
            case 1:
                ivshouye.setImageResource(R.mipmap.home);
                ivfenlei.setImageResource(R.mipmap.qrcode_xz);
                ivgouwuche.setImageResource(R.mipmap.shoppingcart);
                ivwode.setImageResource(R.mipmap.mine);
                tvshouye.setTextColor(tvshouye.getResources().getColor(R.color.gray));
                tvfenlei.setTextColor(tvfenlei.getResources().getColor(R.color.theme));
                tvgouwuche.setTextColor(tvgouwuche.getResources().getColor(R.color.gray));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.gray));
                tvCurrent = tvfenlei;
                break;
            case R.id.ll_gouwuche:
                ivshouye.setImageResource(R.mipmap.home);
                ivfenlei.setImageResource(R.mipmap.qrcode);
                ivgouwuche.setImageResource(R.mipmap.shoppingcart_xz);
                ivwode.setImageResource(R.mipmap.mine);
                tvshouye.setTextColor(tvshouye.getResources().getColor(R.color.gray));
                tvfenlei.setTextColor(tvfenlei.getResources().getColor(R.color.gray));
                tvgouwuche.setTextColor(tvgouwuche.getResources().getColor(R.color.theme));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.gray));
                viewPager.setCurrentItem(2);
            case 2:
                ivshouye.setImageResource(R.mipmap.home);
                ivfenlei.setImageResource(R.mipmap.qrcode);
                ivgouwuche.setImageResource(R.mipmap.shoppingcart_xz);
                ivwode.setImageResource(R.mipmap.mine);
                tvshouye.setTextColor(tvshouye.getResources().getColor(R.color.gray));
                tvfenlei.setTextColor(tvfenlei.getResources().getColor(R.color.gray));
                tvgouwuche.setTextColor(tvgouwuche.getResources().getColor(R.color.theme));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.gray));
                tvCurrent = tvgouwuche;
                break;

            case R.id.ll_wode:
                ivshouye.setImageResource(R.mipmap.home);
                ivfenlei.setImageResource(R.mipmap.qrcode);
                ivgouwuche.setImageResource(R.mipmap.shoppingcart);
                ivwode.setImageResource(R.mipmap.mine_xz);
                tvshouye.setTextColor(tvshouye.getResources().getColor(R.color.gray));
                tvfenlei.setTextColor(tvfenlei.getResources().getColor(R.color.gray));
                tvgouwuche.setTextColor(tvgouwuche.getResources().getColor(R.color.gray));
                tvwode.setTextColor(tvwode.getResources().getColor(R.color.theme));
                viewPager.setCurrentItem(3);
            case 3:
                ivshouye.setImageResource(R.mipmap.home);
                ivfenlei.setImageResource(R.mipmap.qrcode);
                ivgouwuche.setImageResource(R.mipmap.shoppingcart);
                ivwode.setImageResource(R.mipmap.mine_xz);
                tvshouye.setTextColor(tvshouye.getResources().getColor(R.color.gray));
                tvfenlei.setTextColor(tvfenlei.getResources().getColor(R.color.gray));
                tvgouwuche.setTextColor(tvgouwuche.getResources().getColor(R.color.gray));
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
