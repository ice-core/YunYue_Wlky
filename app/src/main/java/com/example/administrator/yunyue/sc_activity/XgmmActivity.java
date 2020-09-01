package com.example.administrator.yunyue.sc_activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
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
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class XgmmActivity extends AppCompatActivity {
    private static final String TAG = XgmmActivity.class.getSimpleName();
    RequestQueue queue = null;
    private String sUser_id;
    private Button bt_xgmm_save;
    /*   private EditText et_xgyx_email;*/
    private SharedPreferences pref;
    private EditText et_xgmm_password, et_xgmm_checkpassword;
    private ImageView iv_xgmm_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }

        setContentView(R.layout.activity_xgmm);
        iv_xgmm_back = (ImageView) findViewById(R.id.iv_xgmm_back);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        bt_xgmm_save = (Button) findViewById(R.id.bt_xgmm_save);
        et_xgmm_password = (EditText) findViewById(R.id.et_xgmm_password);
        et_xgmm_checkpassword = (EditText) findViewById(R.id.et_xgmm_checkpassword);
        bt_xgmm_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = et_xgmm_password.getText().toString();
                String checkpassword = et_xgmm_checkpassword.getText().toString();
                if (et_xgmm_password.getText().equals("")) {
                    Hint("请输入新密码", HintDialog.WARM);
                } else if (et_xgmm_checkpassword.getText().equals("")) {
                    Hint("请输入确认密码", HintDialog.WARM);
                } else if (!password.equals(checkpassword)) {
                    Hint("新密码和确认密码不一致", HintDialog.WARM);
                } else {
                    hideDialogin();
                    dialogin("");
                    save(et_xgmm_password.getText().toString(), et_xgmm_checkpassword.getText().toString());
                }
            }
        });
        iv_xgmm_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
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

    private void save(String password, String checkpassword) {
        String url = Api.sUrl + "Login/restPwd/";
        RequestQueue requestQueue = Volley.newRequestQueue(XgmmActivity.this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", sUser_id);
        params.put("password", password);
        params.put("checkpassword", checkpassword);
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
                                Hint(resultMsg);
                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
                                // Toast.makeText(NoteActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "response -> " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialogin();
                Log.e(TAG, error.getMessage(), error);
                Hint(error.getMessage(), HintDialog.ERROR);
            }
        });
        requestQueue.add(jsonRequest);
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
        loadingDialog = new LoadingDialog(XgmmActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(XgmmActivity.this, R.style.dialog, sHint, type, true).show();
    }
}
