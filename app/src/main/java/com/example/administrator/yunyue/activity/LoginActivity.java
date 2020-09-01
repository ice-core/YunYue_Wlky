package com.example.administrator.yunyue.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.MainActivity_Wlky;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc.Validator;
import com.example.administrator.yunyue.sc_activity.GuanggaoActivity;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private TextView tv_denglu, tv_denglu1, tv_zhuce, tv_zhuce1, tv_zhmm;
    private LinearLayout ll_denglu, ll_zhuce;
    private LinearLayout ll_hqyzm, ll_denglu_mm;
    private TextView tv_symmdl;
    private TextView tv_login_denglu, tv_login_zhuce;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText et_login_name, et_login_mima;
    private String user_id = "";
    private TextView tv_login_hqyzm;
    Validator validator = new Validator();
    private TimeCount time;
    private EditText et_zhuce_mima;
    private EditText et_login_yzm;
    private String sUser = "";
    private String sPassword = "";
    private String sType = "";
    private String permissionInfo;
    private final int SDK_PERMISSION_REQUEST = 127;
    boolean isRemember;
    RequestQueue queue = null;
    private TextView tv_login_yhxy;
    private EditText et_zhuce_invitation;
    private static final int VIDEO_PERMISSIONS_CODE = 1;
    String[] VIDEO_PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white);
        }
        setContentView(R.layout.activity_login);
        queue = Volley.newRequestQueue(LoginActivity.this);
        time = new TimeCount(60000, 1000);
        pref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);

        isRemember = pref.getBoolean("user", false);
        sUser = pref.getString("account", "");
        sPassword = pref.getString("password", "");
        tv_denglu = findViewById(R.id.tv_denglu);
        tv_denglu1 = findViewById(R.id.tv_denglu1);
        tv_zhuce = findViewById(R.id.tv_zhuce);
        tv_zhuce1 = findViewById(R.id.tv_zhuce1);
        ll_denglu = findViewById(R.id.ll_denglu);
        ll_zhuce = findViewById(R.id.ll_zhuce);
        tv_zhmm = findViewById(R.id.tv_zhmm);
        ll_hqyzm = findViewById(R.id.ll_hqyzm);
        ll_denglu_mm = findViewById(R.id.ll_denglu_mm);
        tv_symmdl = findViewById(R.id.tv_symmdl);
        tv_login_denglu = findViewById(R.id.tv_login_denglu);
        tv_login_zhuce = findViewById(R.id.tv_login_zhuce);
        et_login_name = findViewById(R.id.et_login_name);
        et_login_mima = findViewById(R.id.et_login_mima);
        tv_login_hqyzm = findViewById(R.id.tv_login_hqyzm);
        et_zhuce_mima = findViewById(R.id.et_zhuce_mima);
        et_login_yzm = findViewById(R.id.et_login_yzm);
        et_zhuce_invitation = findViewById(R.id.et_zhuce_invitation);
        et_login_mima.setTransformationMethod(PasswordTransformationMethod.getInstance());//密码隐藏
        et_zhuce_mima.setTransformationMethod(PasswordTransformationMethod.getInstance());//密码隐藏
        ll_denglu.setVisibility(View.VISIBLE);
        ll_zhuce.setVisibility(View.GONE);
        ll_hqyzm.setVisibility(View.VISIBLE);
        ll_denglu_mm.setVisibility(View.GONE);
        tv_login_yhxy = findViewById(R.id.tv_login_yhxy);
        tv_login_yhxy.setText(Html.fromHtml("注册即代表你同意" + getString(R.string.app_name) + "<font color='#4A90E2'>《用户协议》</font>"));
        tv_login_yhxy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, GuanggaoActivity.class);
                intent.putExtra("link", Api.sUrl + "/api/order/exemption/appId/" + Api.sApp_Id);
                startActivity(intent);
            }
        });
        tv_symmdl.setText("使用密码登录");
        tv_denglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_denglu.setTextColor(tv_denglu.getResources().getColor(R.color.theme));
                et_login_yzm.setText("");
                yzmcz();
                tv_zhuce.setTextColor(tv_zhuce.getResources().getColor(R.color.hui));
                tv_denglu1.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.theme));
                tv_zhuce1.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.hui));
                ll_denglu.setVisibility(View.VISIBLE);
                ll_zhuce.setVisibility(View.GONE);
            }
        });
        tv_zhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_hqyzm.setVisibility(View.VISIBLE);
                ll_denglu_mm.setVisibility(View.GONE);
                tv_symmdl.setText("使用密码登录");
                et_login_yzm.setText("");
                yzmcz();
                tv_denglu.setTextColor(tv_denglu.getResources().getColor(R.color.hui));
                tv_zhuce.setTextColor(tv_zhuce.getResources().getColor(R.color.theme));
                tv_denglu1.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.hui));
                tv_zhuce1.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.theme));
                ll_denglu.setVisibility(View.GONE);
                ll_zhuce.setVisibility(View.VISIBLE);
            }
        });
        tv_zhmm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ZhmmActivity.class);
                startActivity(intent);
            }
        });
        tv_symmdl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_symmdl.getText().toString().equals("使用密码登录")) {
                    ll_hqyzm.setVisibility(View.GONE);
                    ll_denglu_mm.setVisibility(View.VISIBLE);
                    tv_symmdl.setText("使用手机号登陆");
                } else if (tv_symmdl.getText().toString().equals("使用手机号登陆")) {
                    ll_hqyzm.setVisibility(View.VISIBLE);
                    ll_denglu_mm.setVisibility(View.GONE);
                    tv_symmdl.setText("使用密码登录");
                }
            }
        });
        tv_login_denglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_login_name.getText().toString().equals("")) {
                    Hint("请输入用户名", HintDialog.WARM);
                    //  Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                } else {
                    if (tv_symmdl.getText().toString().equals("使用手机号登陆")) {
                        if (et_login_mima.getText().toString().equals("")) {
                            Hint("请输入密码", HintDialog.WARM);
                            //  Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                        } else {
                            dialogin("");
                            login(et_login_mima.getText().toString());
                            tv_login_denglu.setEnabled(false);
                        }
                    } else if (tv_symmdl.getText().toString().equals("使用密码登录")) {
                        if (et_login_yzm.getText().toString().equals("")) {
                            Hint("验证码不能为空", HintDialog.WARM);
                            //  Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                        } else {
                            dialogin("");
                            logindx();

                            tv_login_denglu.setEnabled(false);
                        }
                    }
                }
            }
        });
        tv_login_zhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validator.isMobile(et_login_name.getText().toString())) {
                    if (et_login_yzm.getText().equals("")) {
                        Hint("请输入验证码", HintDialog.WARM);
                        // Toast.makeText(RegisterActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();

                    } else {
                        if (et_zhuce_mima.getText().toString().equals("")) {
                            Hint("请输入密码", HintDialog.WARM);
                            // Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                        } else {

                            dialogin("");
                            zhuce();

                        }
                    }
                } else {
                    Hint("请输入正确的手机号", HintDialog.WARM);
                }
            }
        });
        tv_login_hqyzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validator.isMobile(et_login_name.getText().toString())) {

                    if (ll_denglu.getVisibility() == View.VISIBLE) {
                        sType = "2";
                    } else {
                        sType = "5";
                    }
                    dialogin("");
                    yanzhengma(sType);
                    time.start();
                } else {
                    Hint("请输入正确的手机号", HintDialog.WARM);
                    //  Toast.makeText(RegisterActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //  getPersimmions();
        // moban();
        closeAndroidPDialog();
        requestPermission();
    }


    //申请权限
    private void requestPermission() {
        // 当API大于 23 时，才动态申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(LoginActivity.this, VIDEO_PERMISSIONS, VIDEO_PERMISSIONS_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case VIDEO_PERMISSIONS_CODE:
                //权限请求失败
                if (grantResults.length == VIDEO_PERMISSIONS.length) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            //弹出对话框引导用户去设置
                            showDialog_qx();
                            Toast.makeText(LoginActivity.this, "请求权限被拒绝", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "已授权", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    //弹出提示框
    private void showDialog_qx() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("录像需要相机、录音和读写权限，是否去设置？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        goToAppSetting();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_login_hqyzm.setBackgroundResource(R.drawable.jf);
            tv_login_hqyzm.setClickable(false);
            tv_login_hqyzm.setText("   " + millisUntilFinished / 1000 + "秒后重新发送   ");
        }

        @Override
        public void onFinish() {
            tv_login_hqyzm.setText("重新获取验证码");
            tv_login_hqyzm.setClickable(true);
            tv_login_hqyzm.setBackgroundResource(R.drawable.bk_lan8);
        }
    }

    private void yzmcz() {
        time.cancel();
        time = new TimeCount(60000, 1000);
        tv_login_hqyzm.setText("获取验证码");
        tv_login_hqyzm.setClickable(true);
        tv_login_hqyzm.setBackgroundResource(R.drawable.bk_lan8);
    }

    private void yanzhengma(String state) {
        String url = Api.sUrl + "Api/Login/sendsms/appId/" + Api.sApp_Id + "/mobile/"
                + et_login_name.getText().toString() + "/state/" + state;
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
                        et_login_yzm.setText("");
                        Hint(resultMsg, HintDialog.SUCCESS);
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
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }

    private void zhuce() {
        String url = Api.sUrl + "Api/Login/registers/appId/" + Api.sApp_Id + "/mobile/"
                + et_login_name.getText().toString() + "/code/" + et_login_yzm.getText().toString() + "/password/" + et_zhuce_mima.getText().toString();

        if (!et_zhuce_invitation.getText().toString().equals("")) {
            url = url + "/invitation/" + et_zhuce_invitation.getText().toString();
        }
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
                        tv_denglu.setTextColor(tv_denglu.getResources().getColor(R.color.theme));
                        et_login_yzm.setText("");
                        yzmcz();
                        tv_zhuce.setTextColor(tv_zhuce.getResources().getColor(R.color.hui));
                        tv_denglu1.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.theme));
                        tv_zhuce1.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.hui));
                        ll_denglu.setVisibility(View.VISIBLE);
                        ll_zhuce.setVisibility(View.GONE);
                        ll_hqyzm.setVisibility(View.GONE);
                        ll_denglu_mm.setVisibility(View.VISIBLE);
                        tv_symmdl.setText("使用手机号登陆");
                        Hint(resultMsg, HintDialog.SUCCESS);
                        login(et_zhuce_mima.getText().toString());

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
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }

    private void login(String sPassword) {
        String url = Api.sUrl + "Api/Login/Login/appId/" + Api.sApp_Id + "/mobile/"
                + et_login_name.getText().toString() + "/password/" + sPassword;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                tv_login_denglu.setEnabled(true);
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {

                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        main(sDate);
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
                hideDialogin();
                tv_login_denglu.setEnabled(true);
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }


    private void logindx() {
        String phone = et_login_name.getText().toString();
        String dxcode = et_login_yzm.getText().toString();
        String url = Api.sUrl + "Api/Login/codeLogin/appId/" + Api.sApp_Id + "/mobile/"
                + phone + "/code/" + dxcode;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                tv_login_denglu.setEnabled(true);
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        //Intent intent = new Intent(NoteActivity.this, MainActivity.class);
                        main(sDate);
                               /* startActivity(intent);
                                Hint(resultMsg, HintDialog.SUCCESS);*/
                    } else {
                        Hint(resultMsg, HintDialog.ERROR);
                        // Toast.makeText(NoteActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "response -> " + jsonObject.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideDialogin();
                tv_login_denglu.setEnabled(true);
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }


    private void main(String data) {
        System.out.println(data);
        JSONObject jsonObject1 = null;
        try {
            jsonObject1 = new JSONObject(data);
            String resultMsg = jsonObject1.getString("msg");
            int resultCode = jsonObject1.getInt("code");
            if (resultCode > 0) {
                JSONObject jsonObjectdate = jsonObject1.getJSONObject("data");
                user_id = jsonObjectdate.getString("id");

                String mobile = jsonObjectdate.getString("mobile");

                String head_pic = jsonObjectdate.getString("headimgurl");
                String nickname = jsonObjectdate.getString("nickname");

                String token = jsonObjectdate.getString("token");
              //  String is_vip = jsonObjectdate.getString("is_vip");

                editor = pref.edit();
                editor.putBoolean("user", true);

                editor.putString("password", user_id);

                editor.putString("user_id", user_id);

                editor.putString("mobile", mobile);

                editor.putString("head_pic", head_pic);
                editor.putString("nickname", nickname);
                editor.putString("token", token);
                editor.putString("AppId", Api.sApp_Id);
               // editor.putString("is_vip", is_vip);
                editor.apply();
                Intent intent = new Intent(this, MainActivity_Wlky.class);
                startActivity(intent);
                finish();
                hideDialogin();

                //   Hint(resultMsg, HintDialog.SUCCESS);
            } else {
                Hint(resultMsg, HintDialog.ERROR);
                //   Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

}
