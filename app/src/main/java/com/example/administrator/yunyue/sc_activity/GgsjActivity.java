package com.example.administrator.yunyue.sc_activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc.Validator;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GgsjActivity extends AppCompatActivity {
    private static final String TAG = GgsjActivity.class.getSimpleName();
    private Button bt_ggsj_hqyzm;
    Validator validator = new Validator();
    private TimeCount time;
    private EditText et_ggsj_shouji, et_ggsj_yzm;
    private ImageView iv_ggsj_sc;
    private TextView tv_ggsf_save;
    RequestQueue queue = null;
    private String sUser_id;
    private SharedPreferences pref;
    private ImageView iv_ggsj_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_ggsj);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        queue = Volley.newRequestQueue(GgsjActivity.this);
        time = new TimeCount(60000, 1000);
        bt_ggsj_hqyzm = (Button) findViewById(R.id.bt_ggsj_hqyzm);
        et_ggsj_shouji = (EditText) findViewById(R.id.et_ggsj_shouji);
        et_ggsj_yzm = (EditText) findViewById(R.id.et_ggsj_yzm);
        iv_ggsj_sc = (ImageView) findViewById(R.id.iv_ggsj_sc);
        tv_ggsf_save = (TextView) findViewById(R.id.tv_ggsf_save);
        iv_ggsj_back = (ImageView) findViewById(R.id.iv_ggsj_back);
        iv_ggsj_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        bt_ggsj_hqyzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validator.isMobile(et_ggsj_shouji.getText().toString())) {
                    dialogin("");
                    yanzhengma();
                    time.start();
                } else {
                    Hint("请输入正确的手机号", HintDialog.WARM);
                    //  Toast.makeText(RegisterActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
            }
        });
        iv_ggsj_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_ggsj_shouji.setText("");
            }
        });
        tv_ggsf_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validator.isMobile(et_ggsj_shouji.getText().toString())) {
                    if (et_ggsj_yzm.equals("")) {
                        Hint("验证码不能为空", HintDialog.WARM);
                    } else {
                        dialogin("");
                        Save(sUser_id, et_ggsj_shouji.getText().toString(), et_ggsj_yzm.getText().toString());
                    }
                } else {
                    Hint("请输入正确的手机号", HintDialog.WARM);
                    //  Toast.makeText(RegisterActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void Save(String user_id, String mobile, String code) {
        String url = Api.sUrl + "My/userModify/user_id/" + user_id + "/mobile/" + mobile + "/code/" + code;
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
                        //   Hint(resultMsg, HintDialog.SUCCESS);
                        Hint(resultMsg);
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


    public void Hint(String hint) {
        final Dialog dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        final View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_hint, null);
        final ImageView iv_hint_dialog_img = inflate.findViewById(R.id.iv_hint_dialog_img);
        final TextView tv_hint_dialog_message = inflate.findViewById(R.id.tv_hint_dialog_message);
        TextView btn_hint_dialog_confirm = inflate.findViewById(R.id.btn_hint_dialog_confirm);
        iv_hint_dialog_img.setImageResource(R.drawable.success);
        tv_hint_dialog_message.setText(hint);
        btn_hint_dialog_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                back();
            }
        });
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    private void yanzhengma() {
        String url = Api.sUrl + "Login/dxSendCode/";
        RequestQueue requestQueue = Volley.newRequestQueue(GgsjActivity.this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", et_ggsj_shouji.getText().toString());
        params.put("type", "4");
        JSONObject jsonObject = new JSONObject(params);
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialogin();
                        String sDate = response.toString();
                        System.out.println(sDate);
                        JSONObject jsonObject1 = null;
                        try {
                            jsonObject1 = new JSONObject(sDate);
                            String resultMsg = jsonObject1.getString("msg");
                            int resultCode = jsonObject1.getInt("code");
                            if (resultCode > 0) {
                                et_ggsj_yzm.setText("");
                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
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
                hideDialogin();
                Hint(error.getMessage(), HintDialog.ERROR);
                Log.e(TAG, error.getMessage(), error);
            }
        });
        requestQueue.add(jsonRequest);
    }

    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            bt_ggsj_hqyzm.setBackgroundResource(R.drawable.jf);
            bt_ggsj_hqyzm.setClickable(false);
            bt_ggsj_hqyzm.setText("  " + millisUntilFinished / 1000 + "秒后重新发送  ");
        }

        @Override
        public void onFinish() {
            bt_ggsj_hqyzm.setText(" 重新获取验证码 ");
            bt_ggsj_hqyzm.setClickable(true);
            bt_ggsj_hqyzm.setBackgroundResource(R.drawable.login);

        }
    }

    private void back() {
        Intent intent = new Intent(this, ZhszActivity.class);
        startActivity(intent);
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
        loadingDialog = new LoadingDialog(GgsjActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(GgsjActivity.this, R.style.dialog, sHint, type, true).show();
    }
}
