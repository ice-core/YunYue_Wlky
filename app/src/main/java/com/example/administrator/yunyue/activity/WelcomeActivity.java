package com.example.administrator.yunyue.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.administrator.yunyue.Api;

import com.example.administrator.yunyue.MainActivity_Wlky;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.dialog.HintDialog;

import com.example.administrator.yunyue.dialog.PromptDialog;
import com.zenglb.downloadinstaller.DownloadInstaller;
import com.zenglb.downloadinstaller.DownloadProgressCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;


public class WelcomeActivity extends AppCompatActivity {
    private static final String TAG = WelcomeActivity.class.getSimpleName();


    private SharedPreferences pref;
    private String sUser;

    private SharedPreferences.Editor editor;
    private String sPassword = "";
    private String sAppId = "";
    RequestQueue queue = null;

    /**
     * 当前版本
     */
    String verName = "";
    //提示语
    private String updateMsg = "有最新的软件包哦，亲快下载吧~";

    //返回的安装包url
    private String apkUrl = "";

    private final int SDK_PERMISSION_REQUEST = 127;
    private String permissionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏，注意一定要在绘制view之前调用这个方法，不然会出现
        //AndroidRuntimeException: requestFeature() must be called before adding content 这个错误。
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   //全屏显示
        Window _window;
        /**
         * 隐藏pad底部虚拟键
         */
        _window = getWindow();
        closeAndroidPDialog();

        WindowManager.LayoutParams params = _window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        setContentView(R.layout.activity_welcome);

        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = getPackageManager().
                    getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            verName = getPackageManager().
                    getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        queue = Volley.newRequestQueue(WelcomeActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(WelcomeActivity.this);
        boolean isRemember = pref.getBoolean("user", false);
        sUser = pref.getString("account", "");
        sPassword = pref.getString("password", "");
        sAppId = pref.getString("AppId", "");
        // moban();
        editor = pref.edit();
        editor.putString("shangchen", "2");
        editor.putString("zhixun", "1");
        editor.apply();
        methodRequiresPermission();
        sBanben();
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

    /**
     * 方法名：sBanben()
     * 功  能：更新软件
     * 参  数：appId
     * edition--版本号
     * type--1安卓 2ios
     */
    private void sBanben() {
        String url = Api.sUrl + Api.sBanben + "/appId/" + Api.sApp_Id + "/edition/" + verName + "/type/1";
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        apkUrl = Api.sUrl + jsonObject1.getString("url");
                        //showNoticeDialog();
                        showUpdateDialog(resultMsg, true, apkUrl);
                        // showDialog();
                    } else {
                        //  Hint(resultMsg, HintDialog.ERROR);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (sPassword.equals("")) {
                                    Intent intent1 = new Intent(WelcomeActivity.this, LoginActivity.class);
                                    startActivity(intent1);
                                    finish();
                                } else {
                                    if (sAppId.equals(Api.sApp_Id)) {
                                        Intent intent1 = new Intent(WelcomeActivity.this, MainActivity_Wlky.class);
                                        startActivity(intent1);
                                        finish();
                                    } else {
                                        Intent intent1 = new Intent(WelcomeActivity.this, LoginActivity.class);
                                        startActivity(intent1);
                                        finish();
                                    }
                                }
                            }
                        }, 2000);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "response -> " + jsonObject.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println(error);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (sPassword.equals("")) {
                            Intent intent1 = new Intent(WelcomeActivity.this, LoginActivity.class);
                            startActivity(intent1);
                            finish();
                        } else {
                            if (sAppId.equals(Api.sApp_Id)) {
                                Intent intent1 = new Intent(WelcomeActivity.this, MainActivity_Wlky.class);
                                startActivity(intent1);
                                finish();
                            } else {
                                Intent intent1 = new Intent(WelcomeActivity.this, LoginActivity.class);
                                startActivity(intent1);
                                finish();
                            }
                        }
                    }
                }, 2000);
                if (error != null) {
                    if (error instanceof TimeoutError) {
                        // Toast.makeText(mActivity,"网络请求超时，请重试！",Toast.LENGTH_SHORT).show();
                        Hint("网络请求超时，请重试！", HintDialog.ERROR);
                        return;
                    }
                    if (error instanceof ServerError) {
                        //  Toast.makeText(mActivity,"服务器异常",Toast.LENGTH_SHORT).show();
                        Hint("服务器异常", HintDialog.ERROR);
                        return;
                    }
                    if (error instanceof NetworkError) {
                        // Toast.makeText(mActivity,"请检查网络",Toast.LENGTH_SHORT).show();
                        Hint("请检查网络", HintDialog.ERROR);
                        return;
                    }
                    if (error instanceof ParseError) {
                        Hint("数据格式错误", HintDialog.ERROR);
                        //  Toast.makeText(mActivity,"数据格式错误",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //  Hint(error.toString(), HintDialog.ERROR);
                    Log.e(TAG, error.getMessage(), error);
                }

            }
        });
        queue.add(request);
    }

    /**
     *  * 方法名：showDialog()
     *  * 功    能：退出消息确认
     *  * 参    数：无
     *  * 返回值：无
     */
    protected void showDialog() {
        PromptDialog pd = new PromptDialog(WelcomeActivity.this, R.style.dialog, updateMsg, new PromptDialog.OnClickListener() {
            @Override
            public void onCancelClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    dialog.dismiss();
                    //dialog.dismiss();
                    if (sPassword.equals("")) {
                        Intent intent1 = new Intent(WelcomeActivity.this, LoginActivity.class);
                        startActivity(intent1);
                        finish();
                    } else {
                        if (sAppId.equals(Api.sApp_Id)) {
                            Intent intent1 = new Intent(WelcomeActivity.this, MainActivity_Wlky.class);
                            startActivity(intent1);
                            finish();
                        } else {
                            Intent intent1 = new Intent(WelcomeActivity.this, LoginActivity.class);
                            startActivity(intent1);
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onConfimClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    dialog.dismiss();
                    // finish();
                    xiazai();
                    // exit();
                    // forceStopAPK("com.example.administrator.yunyue");
                }

            }
        });
        pd.setPositiveName("是");
        pd.setNegativeName("下次再说");
        pd.show();
    }

    private void xiazai() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(apkUrl);
        intent.setData(content_url);
        startActivity(intent);
        finish();
    }

    private void moban() {
        String url = Api.sUrl + "IpShow/muban";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        JSONObject jsonObject = new JSONObject(params);
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //    hideDialogin();
                        String sDate = response.toString();
                        System.out.println(sDate);
                        JSONObject jsonObject1 = null;
                        try {
                            jsonObject1 = new JSONObject(sDate);
                            String resultMsg = jsonObject1.getString("msg");
                            int resultCode = jsonObject1.getInt("code");
                            if (resultCode > 0) {
                                JSONObject jsonObjectdate = jsonObject1.getJSONObject("data");
                                String shangchen = jsonObjectdate.getString("shangchen");
                                String zhixun = jsonObjectdate.getString("zhixun");
                                editor = pref.edit();
                                editor.putString("shangchen", shangchen);
                                editor.putString("zhixun", zhixun);
                                editor.apply();
                                if (sPassword.equals("")) {
                                    Intent intent1 = new Intent(WelcomeActivity.this, LoginActivity.class);
                                    startActivity(intent1);
                                    finish();
                                } else {
                                    Intent intent1 = new Intent(WelcomeActivity.this, MainActivity_Wlky.class);
                                    startActivity(intent1);
                                    finish();
                                }
                            } else {
                                //  Hint(resultMsg, HintDialog.ERROR);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "response -> " + response.toString());
                        //   Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ///  hideDialogin();
                //  Hint(error.getMessage(), HintDialog.ERROR);
                Log.e(TAG, error.getMessage(), error);
                if (error != null) {
                    if (error instanceof TimeoutError) {
                        // Toast.makeText(mActivity,"网络请求超时，请重试！",Toast.LENGTH_SHORT).show();
                        Hint("网络请求超时，请重试！", HintDialog.ERROR);
                        return;
                    }
                    if (error instanceof ServerError) {
                        //  Toast.makeText(mActivity,"服务器异常",Toast.LENGTH_SHORT).show();
                        Hint("服务器异常", HintDialog.ERROR);
                        return;
                    }
                    if (error instanceof NetworkError) {
                        // Toast.makeText(mActivity,"请检查网络",Toast.LENGTH_SHORT).show();
                        Hint("请检查网络", HintDialog.ERROR);
                        return;
                    }
                    if (error instanceof ParseError) {
                        Hint("数据格式错误", HintDialog.ERROR);
                        //  Toast.makeText(mActivity,"数据格式错误",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Hint(error.toString(), HintDialog.ERROR);
                    Log.e(TAG, error.getMessage(), error);
                }
            }
        });
        requestQueue.add(jsonRequest);
    }

    /**
     * 消息提示
     */
    protected void Hint(String sHint, int type) {
        new HintDialog(this, R.style.dialog, sHint, type, true).show();
    }

    /**
     * 显示下载的对话框,是否要强制的升级还是正常的升级
     *
     * @param UpdateMsg     升级信息
     * @param isForceUpdate 是否是强制升级
     * @param downloadUrl   APK 下载URL
     */
    private void showUpdateDialog(String UpdateMsg, boolean isForceUpdate, String downloadUrl) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        final LayoutInflater inflater = LayoutInflater.from(this);
        View updateView = inflater.inflate(R.layout.update_layout, null);
        NumberProgressBar progressBar = updateView.findViewById(R.id.tips_progress);
        TextView updateMsg = updateView.findViewById(R.id.update_mess_txt);
        updateMsg.setText(UpdateMsg);
        builder.setTitle("发现新版本");
        String negativeBtnStr = "以后再说";

        if (isForceUpdate) {
            builder.setTitle("强制升级");
            negativeBtnStr = "退出应用";
        }

        builder.setView(updateView);
        builder.setNegativeButton(negativeBtnStr, null);
        builder.setPositiveButton(R.string.apk_update_yes, null);

        android.support.v7.app.AlertDialog downloadDialog = builder.create();
        downloadDialog.setCanceledOnTouchOutside(false);
        downloadDialog.setCancelable(false);
        downloadDialog.show();

        downloadDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (isForceUpdate) {
                progressBar.setVisibility(View.VISIBLE);

                //新加 isForceGrantUnKnowSource 参数

                //如果是企业内部应用升级，肯定是要这个权限，其他情况不要太流氓，TOAST 提示
                //这里演示要强制安装
                new DownloadInstaller(this, downloadUrl, true, new DownloadProgressCallBack() {
                    @Override
                    public void downloadProgress(int progress) {
                        runOnUiThread(() -> progressBar.setProgress(progress));
                        if (progress == 100) {
                            downloadDialog.dismiss();
                        }
                    }

                    @Override
                    public void downloadException(Exception e) {
                    }

                    @Override
                    public void onInstallStart() {
                        downloadDialog.dismiss();
                    }
                }).start();

                //升级按钮变灰色
                downloadDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);

            } else {
                new DownloadInstaller(this, downloadUrl).start();
                downloadDialog.dismiss();
            }
        });

        downloadDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v -> {
            if (isForceUpdate) {
                WelcomeActivity.this.finish();
            } else {
                downloadDialog.dismiss();
            }
        });

    }

    /**
     * 请求权限,创建目录的权限
     */
    private void methodRequiresPermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(WelcomeActivity.this, "App 升级需要储存权限", 10086, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}
