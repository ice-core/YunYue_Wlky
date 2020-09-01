package com.example.administrator.yunyue.sq_activity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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
import com.example.administrator.yunyue.Im.ConversationActivity;
import com.example.administrator.yunyue.Im.HaoyouActivity;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imkit.RongIM;

public class HyxqActivity extends AppCompatActivity {
    private static final String TAG = HyxqActivity.class.getSimpleName();
    private LinearLayout ll_hyxq_back;

    private ImageView iv_hyxq;
    private TextView tv_hyxq_name;
    private ImageView iv_hyxq_sex;
    private SharedPreferences pref;
    private String sUser_id;

    RequestQueue queue = null;
    private TextView tv_hyxq_tjhy;
    private TextView tv_hyxq_mobile;
    public static String sId = "";
    private String resultId = "";
    private String resultNickname = "";
    /**
     * 发消息
     */
    private TextView tv_hyxq_fxx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white);
        }
        setContentView(R.layout.activity_hyxq);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(HyxqActivity.this);
        sUser_id = pref.getString("user_id", "");
        ll_hyxq_back = findViewById(R.id.ll_hyxq_back);
        iv_hyxq = findViewById(R.id.iv_hyxq);
        tv_hyxq_name = findViewById(R.id.tv_hyxq_name);
        iv_hyxq_sex = findViewById(R.id.iv_hyxq_sex);
        tv_hyxq_tjhy = findViewById(R.id.tv_hyxq_tjhy);
        tv_hyxq_mobile = findViewById(R.id.tv_hyxq_mobile);
        tv_hyxq_fxx = findViewById(R.id.tv_hyxq_fxx);
        ll_hyxq_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (sId.equals("")) {
            if (TjhyActivity.resultId.equals("")) {
                tv_hyxq_fxx.setVisibility(View.VISIBLE);
                query(HaoyouActivity.sId);
                //tv_hyxq_tjhy.setText("删除好友");
            } else {
                tv_hyxq_fxx.setVisibility(View.GONE);
                tv_hyxq_tjhy.setText("添加好友");
                Glide.with(HyxqActivity.this)
                        .load( Api.sUrl+ TjhyActivity.resultHeadimgurl)
                        .asBitmap()
                        .dontAnimate()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .into(iv_hyxq);
                tv_hyxq_name.setText(TjhyActivity.resultNickname);
                if (TjhyActivity.resultSex.equals("1")) {
                    iv_hyxq_sex.setImageResource(R.drawable.icon_man);
                } else {
                    iv_hyxq_sex.setImageResource(R.drawable.icon_woman);
                }
                tv_hyxq_mobile.setText("手机号：" + TjhyActivity.resultMobile);

            }
        } else {
            tv_hyxq_fxx.setVisibility(View.VISIBLE);
            // tv_hyxq_tjhy.setText("添加好友");
            query(sId);
        }
        tv_hyxq_tjhy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_hyxq_tjhy.getText().toString().equals("添加好友")) {
                    dialogin("");
                    save("Community/Api/addFriends" + "/friend_id/" + TjhyActivity.resultId);
                } else if (tv_hyxq_tjhy.getText().toString().equals("删除好友")) {
                    dialogin("");
                    save("Community/Api/delfriend" + "/friend_id/" + resultId);
                }
            }
        });
        tv_hyxq_fxx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TjhyActivity.resultId.equals("")) {
                    RongIM.getInstance().startPrivateChat(HyxqActivity.this, resultId, resultNickname);
                } else {
                    RongIM.getInstance().startPrivateChat(HyxqActivity.this, TjhyActivity.resultId, TjhyActivity.resultNickname);
                }
                ConversationActivity.sType = "";
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
                finish();
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

    private void query(String friend_id) {
        String url = Api.sUrl
                + "Community/Api/findFriends/appId/" + Api.sApp_Id + "/user_id/" + sUser_id + "/friend_id/" + friend_id;
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
                        JSONObject jsonObjectdate = jsonObject1.getJSONObject("data");
                        resultId = jsonObjectdate.getString("id");
                        resultNickname = jsonObjectdate.getString("nickname");
                        String resultHeadimgurl = jsonObjectdate.getString("headimgurl");
                        String resultSex = jsonObjectdate.getString("sex");
                        String resultMobile = jsonObjectdate.getString("mobile");
                        Glide.with(HyxqActivity.this)
                                .load( Api.sUrl+ resultHeadimgurl)
                                .asBitmap()
                                .dontAnimate()
                                .placeholder(R.mipmap.error)
                                .error(R.mipmap.error)
                                .into(iv_hyxq);
                        tv_hyxq_name.setText(resultNickname);
                        if (resultSex.equals("1")) {
                            iv_hyxq_sex.setImageResource(R.drawable.icon_man);
                        } else {
                            iv_hyxq_sex.setImageResource(R.drawable.icon_woman);
                        }
                        tv_hyxq_mobile.setText("手机号：" + resultMobile);
                        tv_hyxq_tjhy.setText("删除好友");
                    } else {
                        Hint(resultMsg, HintDialog.ERROR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Hint(String.valueOf(volleyError), HintDialog.ERROR);
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }

    private void save(String sUrl) {
        String url = Api.sUrl
                + sUrl + "/appId/" + Api.sApp_Id + "/user_id/" + sUser_id;
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
                        TjhyActivity.resultId = "";
                        Hint(resultMsg, HintDialog.SUCCESS);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1500);
                    } else {
                        Hint(resultMsg, HintDialog.ERROR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Hint(String.valueOf(volleyError), HintDialog.ERROR);
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(HyxqActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(HyxqActivity.this, R.style.dialog, sHint, type, true).show();
    }

}
