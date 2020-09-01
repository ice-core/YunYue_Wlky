package com.example.administrator.yunyue.sc_activity;

import android.app.Fragment;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.sc.MyFragmentPagerAdapter;
import com.example.administrator.yunyue.sc_fragment.Yindao1Fragment;
import com.example.administrator.yunyue.sc_fragment.Yindao2Fragment;
import com.example.administrator.yunyue.sc_fragment.YindaoFragment;

import java.util.ArrayList;
import java.util.List;

public class YindaoActivity extends AppCompatActivity implements View.OnClickListener, YindaoFragment.OnFragmentInteractionListener, Yindao1Fragment.OnFragmentInteractionListener,
        Yindao2Fragment.OnFragmentInteractionListener {
    private ViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    Fragment yindaofragment, yindao1fragment, yindao2fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_yindao);
        initView();
        initData();
    }

    /**
     *  * 方法名：initView()
     *  * 功    能：控件初始化
     *  * 参    数：无
     *  * 返回值：无
     */
    private void initView() {

        viewPager = (ViewPager) findViewById(R.id.viewPager);

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
        viewPager.setOffscreenPageLimit(3);
    }

    /**
     *  * 方法名：initData()
     *  * 功    能：fragment页面初始加载
     *  * 参    数：无
     *  * 返回值：无
     */
    private void initData() {
        Bundle bundle = new Bundle();
        yindaofragment = new YindaoFragment();
        yindaofragment.setArguments(bundle);

        yindao1fragment = new Yindao1Fragment();
        yindao1fragment.setArguments(bundle);

        yindao2fragment = new Yindao2Fragment();
        yindao2fragment.setArguments(bundle);


        fragments.add(yindaofragment);
        fragments.add(yindao1fragment);
        fragments.add(yindao2fragment);

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
  /*      tvCurrent.setSelected(false);*/
        switch (id) {
            case 0:
                viewPager.setCurrentItem(0);
                break;
            case 1:
                viewPager.setCurrentItem(1);
                break;
            case 2:
                viewPager.setCurrentItem(2);
                break;
            default:
                break;
        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
