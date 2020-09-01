package com.example.administrator.yunyue.edit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
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
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.AppApplication;
import com.example.administrator.yunyue.MainActivity;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.adapter.DragAdapter;
import com.example.administrator.yunyue.adapter.OtherAdapter;
import com.example.administrator.yunyue.dao.ChannelItem;
import com.example.administrator.yunyue.dao.ChannelManage;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.fragment.ZixunFragment;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.example.administrator.yunyue.view.DragGrid;
import com.example.administrator.yunyue.view.OtherGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Description：对tab进行添加删除排序操作
 * ScrollView嵌套两个GridView
 * <p>
 * Created by Mjj on 2016/11/18.
 */

public class ChannelActivity extends GestureDetectorActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = ChannelActivity.class.getSimpleName();
    /**
     * 用户栏目
     */
    private DragGrid userGridView; // GridView
    DragAdapter userAdapter; // 适配器
    ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();

    /**
     * 其它栏目
     */
    private OtherGridView otherGridView; // GridView
    OtherAdapter otherAdapter; // 适配器
    ArrayList<ChannelItem> otherChannelList = new ArrayList<ChannelItem>(); // 数据源
    private ImageView iv_dy_back;
    /**
     * 是否在移动，由于是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。
     */
    boolean isMove = false;
    private SharedPreferences pref;
    private String sUser_id;

    RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.channel);
        queue = Volley.newRequestQueue(this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        userChannelList = ZixunFragment.userChannelList;
        otherChannelList = ZixunFragment.otherChannelList;
        // userChannelList = ((ArrayList<ChannelItem>) ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).getUserChannel());
        // otherChannelList = ((ArrayList<ChannelItem>) ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).getOtherChannel());
        userAdapter = new DragAdapter(this, userChannelList);
        userGridView.setAdapter(userAdapter);
        otherAdapter = new OtherAdapter(this, otherChannelList);
        otherGridView.setAdapter(otherAdapter);
        //设置GRIDVIEW的ITEM的点击监听
        otherGridView.setOnItemClickListener(this);
        userGridView.setOnItemClickListener(this);
        iv_dy_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//  finish();
                saveChannel();
                if (userAdapter.isListChanged()) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    setResult(ZixunFragment.CHANNELRESULT, intent);
                    finish();
                } else {
                    finish();
                }
            }
        });
    }

    /**
     * 初始化布局
     */
    private void initView() {
        userGridView = (DragGrid) findViewById(R.id.userGridView);
        otherGridView = (OtherGridView) findViewById(R.id.otherGridView);
        iv_dy_back = findViewById(R.id.iv_dy_back);
    }

    /**
     * GRIDVIEW对应的ITEM点击监听接口
     */
    @Override
    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
        //如果点击的时候，之前动画还没结束，那么就让点击事件无效
        if (isMove) {
            return;
        }
        switch (parent.getId()) {
            case R.id.userGridView:
                //position为 0 的不进行任何操作

                dialogin("");
                jianshao(parent, view, position);
                break;
            case R.id.otherGridView:
                // 其它GridView
                dialogin("");
                tianjia(parent, view, position);

                break;
            default:
                break;
        }
    }


    /**
     * 添加
     */
    private void tianjia(final AdapterView<?> parent, final View view, final int position) {
        String url = Api.sUrl + "GetZhixun/addType/user_id/" + sUser_id + "/type_id/" + otherChannelList.get(position).getId();
        // String url = Api.sUrl + "GetZhixun/getType";
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
                        final ImageView moveImageView = getView(view);
                        if (moveImageView != null) {
                            TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                            final int[] startLocation = new int[2];
                            newTextView.getLocationInWindow(startLocation);
                            final ChannelItem channel = ((OtherAdapter) parent.getAdapter()).getItem(position);
                            userAdapter.setVisible(false);
                            //添加到最后一个
                            userAdapter.addItem(channel);
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    try {
                                        int[] endLocation = new int[2];
                                        //获取终点的坐标
                                        userGridView.getChildAt(userGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                        MoveAnim(moveImageView, startLocation, endLocation, channel, otherGridView);
                                        otherAdapter.setRemove(position);
                                    } catch (Exception localException) {
                                    }
                                }
                            }, 50L);
                        }

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
     * 减少
     */
    private void jianshao(final AdapterView<?> parent, final View view, final int position) {
        String url = Api.sUrl + "GetZhixun/delType/user_id/" + sUser_id + "/type_id/" + userChannelList.get(position).getId();
        // String url = Api.sUrl + "GetZhixun/getType";
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
                        if (position != 0) {
                            final ImageView moveImageView = getView(view);
                            if (moveImageView != null) {
                                TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                                final int[] startLocation = new int[2];
                                newTextView.getLocationInWindow(startLocation);
                                final ChannelItem channel = ((DragAdapter) parent.getAdapter()).getItem(position);//获取点击的频道内容
                                otherAdapter.setVisible(false);
                                //添加到最后一个
                                otherAdapter.addItem(channel);
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        try {
                                            int[] endLocation = new int[2];
                                            //获取终点的坐标
                                            otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                            MoveAnim(moveImageView, startLocation, endLocation, channel, userGridView);
                                            userAdapter.setRemove(position);
                                        } catch (Exception localException) {
                                        }
                                    }
                                }, 50L);
                            }
                        }

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
        loadingDialog = new LoadingDialog(this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(this, R.style.dialog, sHint, type, true).show();
    }

    /**
     * 点击ITEM移动动画
     *
     * @param moveView
     * @param startLocation
     * @param endLocation
     * @param moveChannel
     * @param clickGridView
     */
    private void MoveAnim(View moveView, int[] startLocation, int[] endLocation, final ChannelItem moveChannel,
                          final GridView clickGridView) {
        int[] initLocation = new int[2];
        //获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        //得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
        //创建移动动画
        TranslateAnimation moveAnimation = new TranslateAnimation(
                startLocation[0], endLocation[0], startLocation[1],
                endLocation[1]);
        moveAnimation.setDuration(300L);//动画时间
        //动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);//动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isMove = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);
                // instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
                if (clickGridView instanceof DragGrid) {
                    otherAdapter.setVisible(true);
                    otherAdapter.notifyDataSetChanged();
                    userAdapter.remove();
                } else {
                    userAdapter.setVisible(true);
                    userAdapter.notifyDataSetChanged();
                    otherAdapter.remove();
                }
                isMove = false;
            }
        });
    }

    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     *
     * @param viewGroup
     * @param view
     * @param initLocation
     * @return
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     */
    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(this);
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    /**
     * 获取点击的Item的对应View，
     *
     * @param view
     * @return
     */
    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(cache);
        return iv;
    }

    /**
     * 退出时候保存选择后数据库的设置
     */
    private void saveChannel() {
        ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).deleteAllChannel();
        ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).saveUserChannel(userAdapter.getChannnelLst());
        ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).saveOtherChannel(otherAdapter.getChannnelLst());
        String sId = "";
        for (int i = 0; i < userAdapter.getChannnelLst().size(); i++) {
            int id = userAdapter.getChannnelLst().get(i).getId();
            if (i == 0) {
                sId = String.valueOf(id);
            } else {
                sId = sId + "," + String.valueOf(id);
            }
        }
        paixu(sId);
    }

    @Override
    public void onBackPressed() {
        saveChannel();
        if (userAdapter.isListChanged()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            setResult(ZixunFragment.CHANNELRESULT, intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 排序
     */
    private void paixu(String type) {
        String url = Api.sUrl + "GetZhixun/drag/user_id/" + sUser_id + "/type/" + type;
        // String url = Api.sUrl + "GetZhixun/getType";
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
