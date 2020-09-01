package com.example.administrator.yunyue.Listview;

import android.util.Log;

public class LogUtil {

    private static boolean isDebug = true;

    public static void d(String TAG, String content) {
        if (isDebug) {
            Log.d(TAG, content);
        }
    }

    public static void d(String content) {
        if (isDebug) {
            Log.d("LogUtil", content);
        }
    }

    //log太长
    private static int LOG_MAXLENGTH = 2000;

    public static void dLong(String TAG, String msg) {
        int strLength = msg.length();
        int start = 0;
        int end = LOG_MAXLENGTH;
        for (int i = 0; i < 100; i++) {
            if (strLength > end) {
                Log.e(TAG + i, msg.substring(start, end));
                start = end;
                end = end + LOG_MAXLENGTH;
            } else {
                Log.e(TAG, msg.substring(start, strLength));
                break;
            }
        }
    }

}
