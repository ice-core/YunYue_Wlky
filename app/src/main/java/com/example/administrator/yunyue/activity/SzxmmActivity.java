package com.example.administrator.yunyue.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

public class SzxmmActivity extends AppCompatActivity {
    private static final String TAG = SzxmmActivity.class.getSimpleName();
    private EditText et_szxmm_mm, et_szxmm_qmm;
    private TextView tv_szxmm_qr;
    private Switch sc_szxmm_sx;
    /**
     * 返回
     */
    private LinearLayout ll_szxmm_back;
    RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white);
        }
        setContentView(R.layout.activity_szxmm);
        queue = Volley.newRequestQueue(SzxmmActivity.this);
        ll_szxmm_back = findViewById(R.id.ll_szxmm_back);
        ll_szxmm_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        et_szxmm_mm = findViewById(R.id.et_szxmm_mm);
        et_szxmm_qmm = findViewById(R.id.et_szxmm_qmm);
        tv_szxmm_qr = findViewById(R.id.tv_szxmm_qr);
        sc_szxmm_sx = findViewById(R.id.sc_szxmm_sx);
        et_szxmm_mm.setTransformationMethod(PasswordTransformationMethod.getInstance());//密码隐藏
        et_szxmm_qmm.setTransformationMethod(PasswordTransformationMethod.getInstance());//密码隐藏
        tv_szxmm_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_szxmm_mm.getText().toString().equals(et_szxmm_qmm.getText().toString())) {
                    dialogin("");
                    save();
                } else {
                    Hint("两次密码不一致！", HintDialog.WARM);
                }
            }
        });
        sc_szxmm_sx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sc_szxmm_sx.isChecked()) {
                    et_szxmm_mm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_szxmm_qmm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//密码显示
                } else {
                    et_szxmm_mm.setTransformationMethod(PasswordTransformationMethod.getInstance());//密码隐藏
                    et_szxmm_qmm.setTransformationMethod(PasswordTransformationMethod.getInstance());//密码隐藏
                }
            }
        });
    }

    private void save() {
        String url = Api.sUrl + "Api/Login/repwd/appId/" + Api.sApp_Id + "/mobile/"
                + ZhmmActivity.sPhone + "/code/" + ZhmmActivity.sYzm + "/password/" + et_szxmm_qmm.getText().toString();
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    Log.d(TAG, "response -> " + jsonObject.toString());
                    //     Toast.makeText(NoteActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    if (resultCode > 0) {
                        et_szxmm_mm.setText("");
                        et_szxmm_qmm.setText("");
                        Hint(resultMsg, HintDialog.SUCCESS);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SzxmmActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
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
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }


    private void back() {
 /*       Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);*/
        finish();
    }


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
        loadingDialog = new LoadingDialog(SzxmmActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(SzxmmActivity.this, R.style.dialog, sHint, type, true).show();
    }

}
