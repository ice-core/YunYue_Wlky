package com.example.administrator.yunyue.sc_activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.administrator.yunyue.sc.BankInfoBean;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TjyhkActivity extends AppCompatActivity {
    private static final String TAG = TjyhkActivity.class.getSimpleName();
    private EditText et_tjyhk_name;
    private EditText et_tjyhk_kh;
    private EditText et_tjyhk_yh;
    private String cardnum;
    private BankInfoBean bankinfobean;
    private String sCardType = "";
    private SharedPreferences pref;
    private String sUser_id;
    private Button bt_tjthk_qrbd;
    private ImageView iv_tjyhk_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_tjyhk);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        et_tjyhk_name = (EditText) findViewById(R.id.et_tjyhk_name);
        et_tjyhk_kh = (EditText) findViewById(R.id.et_tjyhk_kh);
        et_tjyhk_yh = (EditText) findViewById(R.id.et_tjyhk_yh);
        bt_tjthk_qrbd = (Button) findViewById(R.id.bt_tjthk_qrbd);
        iv_tjyhk_back = (ImageView) findViewById(R.id.iv_tjyhk_back);
        iv_tjyhk_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        bt_tjthk_qrbd.setEnabled(false);
        et_tjyhk_kh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                cardnum = et_tjyhk_kh.getText().toString().trim();
                if (cardnum != null && checkBankCard(cardnum)) {
                    bankinfobean = new BankInfoBean(cardnum);
                    et_tjyhk_yh.setText(bankinfobean.getBankName());
                    //   tv_cardtype.setText(bankinfobean.getCardType());
                    sCardType = bankinfobean.getCardType();
                    bt_tjthk_qrbd.setEnabled(true);
                } else {
                    //   Toast.makeText(TjyhkActivity.this, "卡号 " + cardnum + " 不合法,请重新输入", Toast.LENGTH_LONG).show();
                }
            }
        });
        bt_tjthk_qrbd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDialogin();
                dialogin("");
                save();
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

    private void save() {
        String url = Api.sUrl + "Bank/addBank/";
        RequestQueue requestQueue = Volley.newRequestQueue(TjyhkActivity.this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", sUser_id);
        params.put("user_name", et_tjyhk_name.getText().toString());
        params.put("bank_no", cardnum);
        params.put("bank_name", et_tjyhk_yh.getText().toString());
        params.put("type", sCardType);
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
        Intent intent = new Intent(TjyhkActivity.this, YhkglActivity.class);
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
        loadingDialog = new LoadingDialog(TjyhkActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(TjyhkActivity.this, R.style.dialog, sHint, type, true).show();
    }


    /**
     * 校验过程：
     * 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
     * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
     * 3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
     * 校验银行卡卡号
     */
    public static boolean checkBankCard(String bankCard) {
        if (bankCard.length() < 15 || bankCard.length() > 19) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return bankCard.charAt(bankCard.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhn 校验算法获得校验位
     *
     * @param nonCheckCodeBankCard
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeBankCard) {
        if (nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0
                || !nonCheckCodeBankCard.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }
}
