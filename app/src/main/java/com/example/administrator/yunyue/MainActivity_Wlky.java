package com.example.administrator.yunyue;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.administrator.yunyue.dialog.PromptDialog;
import com.example.administrator.yunyue.fragment.NewsFragmentPagerAdapter;
import com.example.administrator.yunyue.fragment.Shouye_TwoFragment;
import com.example.administrator.yunyue.fragment.TabFragment;
import com.example.administrator.yunyue.fragment.ZhiboFragment;
import com.example.administrator.yunyue.fragment.ZixunFragment;
import com.example.administrator.yunyue.jysq_activity.Jysq_ShouyeFragment;
import com.example.administrator.yunyue.sc_fragment.Shouye2Fragment;
import com.example.administrator.yunyue.sc_fragment.WodeFragment;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity_Wlky extends AppCompatActivity implements
        ZixunFragment.OnFragmentInteractionListener,
        Shouye2Fragment.OnFragmentInteractionListener,
        Jysq_ShouyeFragment.OnFragmentInteractionListener,
        WodeFragment.OnFragmentInteractionListener,
        TabFragment.OnFragmentInteractionListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.rv)
    RecyclerView rv;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private int iPosition = 0;
    private String[] tab_name = {"首页", "商城", "社区", "我的"};
    private int[] imgres = new int[]{R.mipmap.sy_no, R.mipmap.sc_no, R.mipmap.sq_no, R.mipmap.wd_no};
    private int[] imgres_is = new int[]{R.mipmap.sy, R.mipmap.sc, R.mipmap.sq, R.mipmap.wd};
    private RecycleAdapterDome recycleAdapterDome;

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
        setContentView(R.layout.activity_main_lkdq);
        ButterKnife.bind(this);
        initFragment();
        tab_data();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white);
        }
        //requestPermission();
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
        PromptDialog pd = new PromptDialog(MainActivity_Wlky.this, R.style.dialog, "确定要退出吗?", new PromptDialog.OnClickListener() {
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
                    // forceStopAPK("com.example.admin.shopping");
                }

            }
        });
        pd.setPositiveName("是");
        pd.setNegativeName("否");
        pd.show();
    }


    /**
     * 初始化Fragment
     */
    private void initFragment() {
        fragments.clear();//清空
        ZixunFragment zixunFragment = new ZixunFragment();
        Shouye2Fragment shouye2Fragment = new Shouye2Fragment();
        Jysq_ShouyeFragment jysq_shouyeFragment = new Jysq_ShouyeFragment();
        WodeFragment wodeFragment = new WodeFragment();

        fragments.add(zixunFragment);
        fragments.add(shouye2Fragment);
        fragments.add(jysq_shouyeFragment);
        fragments.add(wodeFragment);
        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(mAdapetr);
        viewpager.addOnPageChangeListener(pageListener);
        viewpager.setOffscreenPageLimit(5);
    }


    /**
     * ViewPager切换监听方法
     */
    public ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            viewpager.setCurrentItem(position);
            selectTab(position);
        }

    };


    /**
     * 选择的Column里面的Tab
     */
    private void selectTab(int tab_postion) {
        iPosition = tab_postion;
        daohang(tab_postion);
        recycleAdapterDome.notifyDataSetChanged();

    }

    private void daohang(int postion) {
        if (postion == 4) {
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
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                StatusBarUtil.setStatusBarColor(this, R.color.white);
            }
        }
    }

    private void tab_data() {
        recycleAdapterDome = new RecycleAdapterDome(MainActivity_Wlky.this, tab_name);
        recycleAdapterDome.setHasStableIds(true);
        rv.setAdapter(recycleAdapterDome);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(tab_name.length, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class RecycleAdapterDome extends RecyclerView.Adapter<RecycleAdapterDome.MyViewHolder> {
        private Context context;

        private View inflater;
        private String[] name;

        //构造方法，传入数据
        public RecycleAdapterDome(Context context, String[] name) {
            this.context = context;
            this.name = name;
        }

        @Override
        public RecycleAdapterDome.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //创建ViewHolder，返回每一项的布局
            inflater = LayoutInflater.from(context).inflate(R.layout.mian_tab_item, parent, false);
            RecycleAdapterDome.MyViewHolder myViewHolder = new RecycleAdapterDome.MyViewHolder(inflater);
            return myViewHolder;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @SuppressLint("RecyclerView")
        @Override
        public void onBindViewHolder(RecycleAdapterDome.MyViewHolder holder, int position) {

            holder.tv_tab_item.setText(name[position]);
            if (position == iPosition) {
                holder.tv_tab_item.setTextColor(holder.tv_tab_item.getResources().getColor(R.color.theme));
                Glide.with(MainActivity_Wlky.this)
                        .load(imgres_is[position])
                        .asBitmap()
                        .placeholder(R.color.hui999999)
                        .error(R.color.hui999999)
                        .dontAnimate()
                        .into(holder.iv_tab_item);
            } else {
                holder.tv_tab_item.setTextColor(holder.tv_tab_item.getResources().getColor(R.color.bbbccd));
                Glide.with(MainActivity_Wlky.this)
                        .load(imgres[position])
                        .asBitmap()
                        .placeholder(R.color.hui999999)
                        .error(R.color.hui999999)
                        .dontAnimate()
                        .into(holder.iv_tab_item);
            }
            holder.ll_tab_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    daohang(position);
                    iPosition = position;
                    viewpager.setCurrentItem(position);
                    notifyDataSetChanged();
                }
            });


        }

        @Override
        public int getItemCount() {
            //返回Item总条数
            return name.length;
        }

        //内部类，绑定控件
        class MyViewHolder extends RecyclerView.ViewHolder {
            LinearLayout ll_tab_item;
            ImageView iv_tab_item;
            TextView tv_tab_item;

            public MyViewHolder(View itemView) {
                super(itemView);
                ll_tab_item = itemView.findViewById(R.id.ll_tab_item);
                iv_tab_item = itemView.findViewById(R.id.iv_tab_item);
                tv_tab_item = itemView.findViewById(R.id.tv_tab_item);

            }
        }
    }

}
