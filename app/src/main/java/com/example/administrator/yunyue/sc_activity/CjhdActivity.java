package com.example.administrator.yunyue.sc_activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cretin.www.wheelsruflibrary.listener.RotateListener;
import com.cretin.www.wheelsruflibrary.view.WheelSurfView;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CjhdActivity extends AppCompatActivity {
    private static final String TAG = CjhdActivity.class.getSimpleName();
    private LinearLayout ll_cjhd_yqzc, ll_cjhd_wdjp;
    private SharedPreferences pref;
    RequestQueue queue = null;
    private String sUser_id;
    private int position = 1;
    WheelSurfView wheelSurfView2;
    private ImageView iv_cjhd_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_cjhd);
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        LinearLayout ll = (LinearLayout) this.findViewById(R.id.ll_cjhd);
        android.view.ViewGroup.LayoutParams lp = ll.getLayoutParams();
        double bl = ((double) 720 / 1683);
        double gao = ((double) width / bl);
        double bl1 = ((double) 335 / 375);
        double kuan = ((double) bl1 * width);
        lp.height = getInt(gao);
        LinearLayout ll_cjhd_zp = (LinearLayout) findViewById(R.id.ll_cjhd_zp);
        android.view.ViewGroup.LayoutParams para = ll_cjhd_zp.getLayoutParams();
        para.width = getInt(kuan);//修改宽度
        para.height = getInt(kuan);//修改高度
        ll_cjhd_zp.setLayoutParams(para);

        queue = Volley.newRequestQueue(CjhdActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        ll_cjhd_yqzc = (LinearLayout) findViewById(R.id.ll_cjhd_yqzc);
        ll_cjhd_wdjp = (LinearLayout) findViewById(R.id.ll_cjhd_wdjp);
        iv_cjhd_back = (ImageView) findViewById(R.id.iv_cjhd_back);
        ll_cjhd_yqzc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CjhdActivity.this, YqhyActivity.class);
                startActivity(intent);
            }
        });

        ll_cjhd_wdjp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CjhdActivity.this, WdjpActivity.class);
                startActivity(intent);
            }
        });
        iv_cjhd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        /**
         * 新增使用代码设置属性的方式
         *
         * 请注意：
         *  使用这种方式需要在引入布局文件的时候在布局文件中设置mTypeNums = -1 来告诉我你现在要用代码传入这些属性
         *  使用这种方式需要在引入布局文件的时候在布局文件中设置mTypeNums = -1 来告诉我你现在要用代码传入这些属性
         *  使用这种方式需要在引入布局文件的时候在布局文件中设置mTypeNums = -1 来告诉我你现在要用代码传入这些属性
         *
         *  重要的事情说三遍
         *
         *  例如
         *  <com.cretin.www.wheelsruflibrary.view.WheelSurfView
         *      android:id="@+id/wheelSurfView2"
         *      android:layout_width="match_parent"
         *      android:layout_height="match_parent"
         *      wheelSurfView:typenum="-1"
         *      android:layout_margin="20dp">
         *
         *  然后调用setConfig()方法来设置你的属性spjj
         *
         * 请注意：
         *  你在传入所有的图标文件之后需要调用 WheelSurfView.rotateBitmaps() 此方法来处理一下你传入的图片
         *  你在传入所有的图标文件之后需要调用 WheelSurfView.rotateBitmaps() 此方法来处理一下你传入的图片
         *  你在传入所有的图标文件之后需要调用 WheelSurfView.rotateBitmaps() 此方法来处理一下你传入的图片
         *
         *  重要的事情说三遍
         *
         * 请注意：
         *  .setmColors(colors)
         *  .setmDeses(des)
         *  .setmIcons(mListBitmap)
         *  这三个方法中的参数长度必须一致 否则会报运行时异常
         */
        //颜色
        Integer[] colors = new Integer[]{Color.parseColor("#FF6801"), Color.parseColor("#ffdecc")
                , Color.parseColor("#FF6801"), Color.parseColor("#ffdecc")
                , Color.parseColor("#FF6801"), Color.parseColor("#fbc6a9")
        };
        //文字
        String[] des = new String[]{"", "", ""
                , "", "", ""};
        //图标
        List<Bitmap> mListBitmap = new ArrayList<>();
        // for (int i = 0; i < colors.length; i++) {
        mListBitmap.add(BitmapFactory.decodeResource(getResources(), R.mipmap.zp0));
        mListBitmap.add(BitmapFactory.decodeResource(getResources(), R.mipmap.zp1));
        mListBitmap.add(BitmapFactory.decodeResource(getResources(), R.mipmap.zp0));
        mListBitmap.add(BitmapFactory.decodeResource(getResources(), R.mipmap.zp2));
        mListBitmap.add(BitmapFactory.decodeResource(getResources(), R.mipmap.zp0));
        mListBitmap.add(BitmapFactory.decodeResource(getResources(), R.mipmap.zp3));
        //  }
        //主动旋转一下图片
        mListBitmap = WheelSurfView.rotateBitmaps(mListBitmap);

        //获取第三个视图
        wheelSurfView2 = findViewById(R.id.wheelSurfView2);
        WheelSurfView.Builder build = new WheelSurfView.Builder()
                .setmColors(colors)
                .setmDeses(des)
                .setmIcons(mListBitmap)
                .setmType(1)
                .setmTypeNum(6)
                .build();
        wheelSurfView2.setConfig(build);

        //添加滚动监听
        wheelSurfView2.setRotateListener(new RotateListener() {
            @Override
            public void rotateEnd(int position, String des) {
                //       Toast.makeText(CjhdActivity.this, "结束了 位置：" + position + "   描述：" + des, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void rotating(ValueAnimator valueAnimator) {

            }

            @Override
            public void rotateBefore(ImageView goImg) {
              /*  AlertDialog.Builder builder = new AlertDialog.Builder(CjhdActivity.this);
                builder.setTitle("温馨提示");
                builder.setMessage("确定要花费100积分抽奖？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //模拟位置
                        int position = new Random().nextInt(6) - 1;
                        wheelSurfView2.startRotate(2);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();*/
                dialogin("");
                query();

            }

        });
    /*    dialogin("");
        query();*/
    }

    public static int getInt(double number) {
        BigDecimal bd = new BigDecimal(number).setScale(0, BigDecimal.ROUND_HALF_UP);
        return Integer.parseInt(bd.toString());
    }

    /*http://lenovo.bndvip.com/Api/Prize/getGift/store_id/1/user_id/2*/
    private void query() {
        String url = Api.sUrl + "Prize/getGift/user_id/" + sUser_id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
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
                        String resultLevel = jsonObject1.getString("level");
                        if (Integer.valueOf(resultLevel) == 1) {
                            position = 6;
                        } else if (Integer.valueOf(resultLevel) == 2) {
                            position = 4;
                        } else if (Integer.valueOf(resultLevel) == 3) {
                            position = 2;
                        } else {
                            position = 1;
                        }
                        wheelSurfView2.startRotate(position);
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

    private void back() {

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


    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(CjhdActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(CjhdActivity.this, R.style.dialog, sHint, type, true).show();
    }

}
