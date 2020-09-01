package com.example.administrator.yunyue.sc_activity;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.sc.MyFragmentPagerAdapter;
import com.example.administrator.yunyue.sc_fragment.DaifaHuoFragment;
import com.example.administrator.yunyue.sc_fragment.DaifukuanFragment;
import com.example.administrator.yunyue.sc_fragment.DaipinglunFragment;
import com.example.administrator.yunyue.sc_fragment.DaishouhuoFragment;
import com.example.administrator.yunyue.sc_fragment.QuanbuFragment;
import com.example.administrator.yunyue.sc_fragment.ShouhouFragment;

import java.util.ArrayList;
import java.util.List;


public class WdddActivity extends AppCompatActivity implements View.OnClickListener, QuanbuFragment.OnFragmentInteractionListener,
        DaifukuanFragment.OnFragmentInteractionListener, DaifaHuoFragment.OnFragmentInteractionListener,
        DaishouhuoFragment.OnFragmentInteractionListener, DaipinglunFragment.OnFragmentInteractionListener,
        ShouhouFragment.OnFragmentInteractionListener {
    private static final String TAG = WdddActivity.class.getSimpleName();
    private ViewPager viewPager;
    int iFrragment = 0;
    int iId = 0;
    private ImageView iv_wddd_back;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private LinearLayout ll_wddd_qb, ll_wddd_dfk, ll_wddd_dsh, ll_wddd_dpl, ll_wddd_sh;
    private TextView tv_wddd_qb, tv_wddd_dfk, tv_wddd_dsh, tv_wddd_dpl, tv_wddd_sh;
    private ImageView iv_wddd_qb, iv_wddd_dfk, iv_wddd_dsh, iv_wddd_dpl, iv_wddd_sh;
    Fragment daifukuanfragment, daifahuofragment, daishouhuofragment, daipinglunfragment, shouhoufragment;
    QuanbuFragment quanbufragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_wddd);
        Intent intent = getIntent();
        iFrragment = Integer.valueOf(intent.getStringExtra("id"));
        iv_wddd_back = findViewById(R.id.iv_wddd_back);
        iv_wddd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        initView();
        initData();
        changeTab(iFrragment);
        viewPager.setCurrentItem(iFrragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        initData();
        changeTab(iFrragment);
        viewPager.setCurrentItem(iFrragment);//刷新数据
    }


    private void back() {
/*        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("ID", "4");
        startActivity(intent);*/
        finish();
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
                //   showDialog();
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

    /**
     *  * 方法名：initView()
     *  * 功    能：控件初始化
     *  * 参    数：无
     *  * 返回值：无
     */
    private void initView() {

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ll_wddd_qb = (LinearLayout) findViewById(R.id.ll_wddd_qb);
        ll_wddd_dfk = (LinearLayout) findViewById(R.id.ll_wddd_dfk);
        ll_wddd_dsh = (LinearLayout) findViewById(R.id.ll_wddd_dsh);
        ll_wddd_dpl = (LinearLayout) findViewById(R.id.ll_wddd_dpl);
        ll_wddd_sh = (LinearLayout) findViewById(R.id.ll_wddd_sh);

        ll_wddd_qb.setOnClickListener(this);
        ll_wddd_dfk.setOnClickListener(this);
        ll_wddd_dsh.setOnClickListener(this);
        ll_wddd_dpl.setOnClickListener(this);
        ll_wddd_sh.setOnClickListener(this);

        iv_wddd_qb = (ImageView) findViewById(R.id.iv_wddd_qb);
        iv_wddd_dfk = (ImageView) findViewById(R.id.iv_wddd_dfk);
        iv_wddd_dsh = (ImageView) findViewById(R.id.iv_wddd_dsh);
        iv_wddd_dpl = (ImageView) findViewById(R.id.iv_wddd_dpl);
        iv_wddd_sh = (ImageView) findViewById(R.id.iv_wddd_sh);

        tv_wddd_qb = (TextView) findViewById(R.id.tv_wddd_qb);
        tv_wddd_dfk = (TextView) findViewById(R.id.tv_wddd_dfk);
        tv_wddd_dsh = (TextView) findViewById(R.id.tv_wddd_dsh);
        tv_wddd_dpl = (TextView) findViewById(R.id.tv_wddd_dpl);
        tv_wddd_sh = (TextView) findViewById(R.id.tv_wddd_sh);

        tv_wddd_qb.setSelected(true);
        // tvCurrent = tv_wddd_qb;
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
        viewPager.setOffscreenPageLimit(2);
    }

    /**
     *  * 方法名：initData()
     *  * 功    能：fragment页面初始加载
     *  * 参    数：无
     *  * 返回值：无
     */
    private void initData() {
        Bundle bundle = new Bundle();
        quanbufragment = new QuanbuFragment();
        quanbufragment.setArguments(bundle);

        daifukuanfragment = new DaifukuanFragment();
        daifukuanfragment.setArguments(bundle);

        daifahuofragment = new DaifaHuoFragment();
        daifahuofragment.setArguments(bundle);

        daishouhuofragment = new DaishouhuoFragment();
        daishouhuofragment.setArguments(bundle);

        daipinglunfragment = new DaipinglunFragment();
        daipinglunfragment.setArguments(bundle);

        shouhoufragment = new ShouhouFragment();
        shouhoufragment.setArguments(bundle);

       // fragments.add(quanbufragment);
        fragments.add(daifukuanfragment);
        fragments.add(daifahuofragment);
        fragments.add(daishouhuofragment);
        fragments.add(daipinglunfragment);


        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
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

        // tvCurrent.setSelected(false);
        switch (id) {
            case R.id.ll_wddd_qb:
                tv_wddd_qb.setTextColor(tv_wddd_qb.getResources().getColor(R.color.black));
                tv_wddd_dfk.setTextColor(tv_wddd_dfk.getResources().getColor(R.color.hui999999));
                tv_wddd_dsh.setTextColor(tv_wddd_dsh.getResources().getColor(R.color.hui999999));
                tv_wddd_dpl.setTextColor(tv_wddd_dpl.getResources().getColor(R.color.hui999999));
                tv_wddd_sh.setTextColor(tv_wddd_sh.getResources().getColor(R.color.hui999999));
                iv_wddd_qb.setBackgroundColor(ContextCompat.getColor(this, R.color.theme));
                iv_wddd_dfk.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_dsh.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_dpl.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_sh.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                viewPager.setCurrentItem(0);
            case 0:
                tv_wddd_qb.setTextColor(tv_wddd_qb.getResources().getColor(R.color.black));
                tv_wddd_dfk.setTextColor(tv_wddd_dfk.getResources().getColor(R.color.hui999999));
                tv_wddd_dsh.setTextColor(tv_wddd_dsh.getResources().getColor(R.color.hui999999));
                tv_wddd_dpl.setTextColor(tv_wddd_dpl.getResources().getColor(R.color.hui999999));
                tv_wddd_sh.setTextColor(tv_wddd_sh.getResources().getColor(R.color.hui999999));
                iv_wddd_qb.setBackgroundColor(ContextCompat.getColor(this, R.color.theme));
                iv_wddd_dfk.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_dsh.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_dpl.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_sh.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                break;
            case R.id.ll_wddd_dfk:
                tv_wddd_qb.setTextColor(tv_wddd_qb.getResources().getColor(R.color.hui999999));
                tv_wddd_dfk.setTextColor(tv_wddd_dfk.getResources().getColor(R.color.black));
                tv_wddd_dsh.setTextColor(tv_wddd_dsh.getResources().getColor(R.color.hui999999));
                tv_wddd_dpl.setTextColor(tv_wddd_dpl.getResources().getColor(R.color.hui999999));
                tv_wddd_sh.setTextColor(tv_wddd_sh.getResources().getColor(R.color.hui999999));
                iv_wddd_qb.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_dfk.setBackgroundColor(ContextCompat.getColor(this, R.color.theme));
                iv_wddd_dsh.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_dpl.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_sh.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                viewPager.setCurrentItem(1);
            case 1:
                tv_wddd_qb.setTextColor(tv_wddd_qb.getResources().getColor(R.color.hui999999));
                tv_wddd_dfk.setTextColor(tv_wddd_dfk.getResources().getColor(R.color.black));
                tv_wddd_dsh.setTextColor(tv_wddd_dsh.getResources().getColor(R.color.hui999999));
                tv_wddd_dpl.setTextColor(tv_wddd_dpl.getResources().getColor(R.color.hui999999));
                tv_wddd_sh.setTextColor(tv_wddd_sh.getResources().getColor(R.color.hui999999));
                iv_wddd_qb.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_dfk.setBackgroundColor(ContextCompat.getColor(this, R.color.theme));
                iv_wddd_dsh.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_dpl.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_sh.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                break;

            case R.id.ll_wddd_dsh:
                tv_wddd_qb.setTextColor(tv_wddd_qb.getResources().getColor(R.color.hui999999));
                tv_wddd_dfk.setTextColor(tv_wddd_dfk.getResources().getColor(R.color.hui999999));
                tv_wddd_dsh.setTextColor(tv_wddd_dsh.getResources().getColor(R.color.black));
                tv_wddd_dpl.setTextColor(tv_wddd_dpl.getResources().getColor(R.color.hui999999));
                tv_wddd_sh.setTextColor(tv_wddd_sh.getResources().getColor(R.color.hui999999));
                iv_wddd_qb.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_dfk.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_dsh.setBackgroundColor(ContextCompat.getColor(this, R.color.theme));
                iv_wddd_dpl.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_sh.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                viewPager.setCurrentItem(2);
            case 2:
                tv_wddd_qb.setTextColor(tv_wddd_qb.getResources().getColor(R.color.hui999999));
                tv_wddd_dfk.setTextColor(tv_wddd_dfk.getResources().getColor(R.color.hui999999));
                tv_wddd_dsh.setTextColor(tv_wddd_dsh.getResources().getColor(R.color.black));
                tv_wddd_dpl.setTextColor(tv_wddd_dpl.getResources().getColor(R.color.hui999999));
                tv_wddd_sh.setTextColor(tv_wddd_sh.getResources().getColor(R.color.hui999999));
                iv_wddd_qb.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_dfk.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_dsh.setBackgroundColor(ContextCompat.getColor(this, R.color.theme));
                iv_wddd_dpl.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_sh.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                break;
            case R.id.ll_wddd_dpl:
                tv_wddd_qb.setTextColor(tv_wddd_qb.getResources().getColor(R.color.hui999999));
                tv_wddd_dfk.setTextColor(tv_wddd_dfk.getResources().getColor(R.color.hui999999));
                tv_wddd_dsh.setTextColor(tv_wddd_dsh.getResources().getColor(R.color.hui999999));
                tv_wddd_dpl.setTextColor(tv_wddd_dpl.getResources().getColor(R.color.black));
                tv_wddd_sh.setTextColor(tv_wddd_sh.getResources().getColor(R.color.hui999999));
                iv_wddd_qb.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_dfk.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_dsh.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_dpl.setBackgroundColor(ContextCompat.getColor(this, R.color.theme));
                iv_wddd_sh.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                viewPager.setCurrentItem(3);
            case 3:
                tv_wddd_qb.setTextColor(tv_wddd_qb.getResources().getColor(R.color.hui999999));
                tv_wddd_dfk.setTextColor(tv_wddd_dfk.getResources().getColor(R.color.hui999999));
                tv_wddd_dsh.setTextColor(tv_wddd_dsh.getResources().getColor(R.color.hui999999));
                tv_wddd_dpl.setTextColor(tv_wddd_dpl.getResources().getColor(R.color.black));
                tv_wddd_sh.setTextColor(tv_wddd_sh.getResources().getColor(R.color.hui999999));
                iv_wddd_qb.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_dfk.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_dsh.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_dpl.setBackgroundColor(ContextCompat.getColor(this, R.color.theme));
                iv_wddd_sh.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                break;
            case R.id.ll_wddd_sh:
                tv_wddd_qb.setTextColor(tv_wddd_qb.getResources().getColor(R.color.hui999999));
                tv_wddd_dfk.setTextColor(tv_wddd_dfk.getResources().getColor(R.color.hui999999));
                tv_wddd_dsh.setTextColor(tv_wddd_dsh.getResources().getColor(R.color.hui999999));
                tv_wddd_dpl.setTextColor(tv_wddd_dpl.getResources().getColor(R.color.hui999999));
                tv_wddd_sh.setTextColor(tv_wddd_sh.getResources().getColor(R.color.black));
                iv_wddd_qb.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_dfk.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_dsh.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_dpl.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_sh.setBackgroundColor(ContextCompat.getColor(this, R.color.theme));
                viewPager.setCurrentItem(4);
            case 4:
                tv_wddd_qb.setTextColor(tv_wddd_qb.getResources().getColor(R.color.hui999999));
                tv_wddd_dfk.setTextColor(tv_wddd_dfk.getResources().getColor(R.color.hui999999));
                tv_wddd_dsh.setTextColor(tv_wddd_dsh.getResources().getColor(R.color.hui999999));
                tv_wddd_dpl.setTextColor(tv_wddd_dpl.getResources().getColor(R.color.hui999999));
                tv_wddd_sh.setTextColor(tv_wddd_sh.getResources().getColor(R.color.black));
                iv_wddd_qb.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_dfk.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_dsh.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_dpl.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                iv_wddd_sh.setBackgroundColor(ContextCompat.getColor(this, R.color.theme));
                break;
            default:
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}

