package com.example.administrator.yunyue.sc_activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.sc.MyFragmentPagerAdapter;
import com.example.administrator.yunyue.sc_fragment.DengluFragment;
import com.example.administrator.yunyue.sc_fragment.ZhuceFragment;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, DengluFragment.OnFragmentInteractionListener, ZhuceFragment.OnFragmentInteractionListener {
    private ViewPager viewPager;
    private LinearLayout ll_login, ll_zhuce;
    private ImageView ivlogin, ivzhuce;
    private TextView tvlogin, tvzhuce, tvCurrent;
    private ImageView ivlogin_bg, ivzhuce_bg;
    Fragment longinfragment;
    Fragment zhucefragment;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private SharedPreferences pref;
    int iFrragment = 0;
    int iId = 0;
    private String permissionInfo;
    private final int SDK_PERMISSION_REQUEST = 127;
    private LinearLayout ll_main_login;
    private String sUser, sPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }

        Window _window;
        /**
         * 隐藏pad底部虚拟键
         */
        _window = getWindow();

        WindowManager.LayoutParams params = _window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        boolean isRemember = pref.getBoolean("user", false);

        sUser = pref.getString("account", "");
        sPassword = pref.getString("password", "");
        ll_main_login = findViewById(R.id.ll_main_login);
        initView();
        initData();
        changeTab(iId);
        viewPager.setCurrentItem(iId);
        getPersimmions();
        if (sUser.equals("")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ll_main_login.setVisibility(View.GONE);
                }
            }, 2000);

        } else {
            ll_main_login.setVisibility(View.GONE);
        }
    }

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }


    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    /**
     *  * 方法名：initView()
     *  * 功    能：控件初始化
     *  * 参    数：无
     *  * 返回值：无
     */
    private void initView() {

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ll_login = (LinearLayout) findViewById(R.id.ll_login);
        ll_zhuce = (LinearLayout) findViewById(R.id.ll_zhuce);

        ll_login.setOnClickListener(this);
        ll_zhuce.setOnClickListener(this);
        ivlogin = (ImageView) findViewById(R.id.ivlogin);
        ivzhuce = (ImageView) findViewById(R.id.ivzhuce);
        ivlogin_bg = (ImageView) findViewById(R.id.ivlogin_bg);
        ivzhuce_bg = (ImageView) findViewById(R.id.ivzhuce_bg);

        tvlogin = (TextView) findViewById(R.id.tvlogin);
        tvzhuce = (TextView) findViewById(R.id.tvzhuce);

        tvlogin.setSelected(true);
        tvCurrent = tvlogin;

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
        longinfragment = new DengluFragment();
        longinfragment.setArguments(bundle);

        zhucefragment = new ZhuceFragment();
        zhucefragment.setArguments(bundle);

        fragments.add(longinfragment);
        fragments.add(zhucefragment);

        pref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
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
        tvCurrent.setSelected(false);
        switch (id) {
            case R.id.ll_login:
                ivlogin.setImageResource(R.mipmap.denglu_xuanzhong);
                ivzhuce.setImageResource(R.mipmap.zhuce);
                tvlogin.setTextColor(tvlogin.getResources().getColor(R.color.theme));
                tvzhuce.setTextColor(tvzhuce.getResources().getColor(R.color.black));
                ivlogin_bg.setBackgroundColor(ContextCompat.getColor(this, R.color.theme));
                ivzhuce_bg.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                viewPager.setCurrentItem(0);
            case 0:
                ivlogin.setImageResource(R.mipmap.denglu_xuanzhong);
                ivzhuce.setImageResource(R.mipmap.zhuce);
                tvlogin.setTextColor(tvlogin.getResources().getColor(R.color.theme));
                tvzhuce.setTextColor(tvzhuce.getResources().getColor(R.color.black));
                ivlogin_bg.setBackgroundColor(ContextCompat.getColor(this, R.color.theme));
                ivzhuce_bg.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                tvCurrent = tvlogin;
                break;

            case R.id.ll_zhuce:
                ivlogin.setImageResource(R.mipmap.denglu);
                ivzhuce.setImageResource(R.mipmap.zhuce_xuanzhong);
                tvlogin.setTextColor(tvlogin.getResources().getColor(R.color.black));
                tvzhuce.setTextColor(tvzhuce.getResources().getColor(R.color.theme));
                ivlogin_bg.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                ivzhuce_bg.setBackgroundColor(ContextCompat.getColor(this, R.color.theme));
                viewPager.setCurrentItem(1);
            case 1:
                ivlogin.setImageResource(R.mipmap.denglu);
                ivzhuce.setImageResource(R.mipmap.zhuce_xuanzhong);
                tvlogin.setTextColor(tvlogin.getResources().getColor(R.color.black));
                tvzhuce.setTextColor(tvzhuce.getResources().getColor(R.color.theme));
                ivlogin_bg.setBackgroundColor(ContextCompat.getColor(this, R.color.touming));
                ivzhuce_bg.setBackgroundColor(ContextCompat.getColor(this, R.color.theme));
                tvCurrent = tvzhuce;
                break;

            default:
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

