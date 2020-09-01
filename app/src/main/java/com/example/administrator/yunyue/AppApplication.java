package com.example.administrator.yunyue;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.example.administrator.yunyue.db.SQLHelper;
import com.example.administrator.yunyue.service.LocationService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import io.rong.imageloader.core.DisplayImageOptions;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;


public class AppApplication extends Application {

    private static AppApplication mAppApplication;
    private SQLHelper sqlHelper;
    public LocationService locationService;
    public Vibrator mVibrator;
    private static DisplayImageOptions options;
    RequestQueue queue = null;
    private String sUser_id = "";
    private SharedPreferences pref;
    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 融云Im
         */
        RongIM.init(this);
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
        mAppApplication = this;
        initImageLoader();
        closeAndroidPDialog();
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(this);
        sUser_id = pref.getString("user_id", "");
        //监听消息接收事件
        RongIM.setOnReceiveMessageListener((message, i) -> {
            UserInfo userInfo = message.getContent().getUserInfo();
            if (userInfo == null) {
                // LogUtils.getInstance().e("消息中的UserInfo为空......");
                String sUser_id = message.getTargetId();
                query(sUser_id);
                return false;
            }

            RongIM.getInstance().refreshUserInfoCache(userInfo);
            //返回false则依旧走融云默认的通知和铃声
            return false;
        });
    }

    /**
     * 好友
     */
    private void query(String friend_id) {
        String url = Api.sUrl
                + "Community/Api/findFriends/appId/" + Api.sApp_Id + "/user_id/" + sUser_id + "/friend_id/" + friend_id;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        JSONObject jsonObjectdate = jsonObject1.getJSONObject("data");
                        String resultId = jsonObjectdate.getString("id");
                        String resultNickname = jsonObjectdate.getString("nickname");
                        String resultHeadimgurl = jsonObjectdate.getString("headimgurl");
                        String resultSex = jsonObjectdate.getString("sex");
                        String resultMobile = jsonObjectdate.getString("mobile");
                        String resultInfo = jsonObjectdate.getString("info");
                        RongIM.getInstance().setCurrentUserInfo(new UserInfo(resultId, resultInfo, Uri.parse(Api.sUrl + resultHeadimgurl)));
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }

    public static DisplayImageOptions getOptions() {
        return options;
    }

    /**
     * 获取Application
     */
    public static AppApplication getApp() {
        return mAppApplication;
    }

    /**
     * 获取数据库Helper
     */
    public SQLHelper getSQLHelper() {
        if (sqlHelper == null)
            sqlHelper = new SQLHelper(mAppApplication);
        return sqlHelper;
    }

    @Override
    public void onTerminate() {
        if (sqlHelper != null)
            sqlHelper.close();
        super.onTerminate();
        //整体摧毁的时候调用这个方法
    }

    private void initImageLoader() {
        try {
            ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
            ImageLoader.getInstance().init(configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
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

}
