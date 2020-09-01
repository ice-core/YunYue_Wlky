package com.example.administrator.yunyue.sc_activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.sc_fragment.ShouyeFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class FukuanActivity extends AppCompatActivity {
    private TextView tv_fukuan_name;
    private TextView tv_fukuan_fk;
    private String sRdsQrcode = "";
    private EditText et_fukuan_money;
    private TextView tv_fukuan_bz;
    private ImageView iv_fukuan_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_fukuan);
        tv_fukuan_name = findViewById(R.id.tv_fukuan_name);
        tv_fukuan_fk = findViewById(R.id.tv_fukuan_fk);
        et_fukuan_money = findViewById(R.id.et_fukuan_money);
        tv_fukuan_bz = findViewById(R.id.tv_fukuan_bz);
        iv_fukuan_back = findViewById(R.id.iv_fukuan_back);
        et_fukuan_money.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        JSONObject jsonObjectdate = null;
        try {
            jsonObjectdate = new JSONObject(ShouyeFragment.sSaomiao);
            sRdsQrcode = jsonObjectdate.getString("rdsQrcode");
            String sStorename = jsonObjectdate.getString("storename");
            tv_fukuan_name.setText(sStorename);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tv_fukuan_fk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FukuanActivity.this, ZffsActivity.class);
                intent.putExtra("money", et_fukuan_money.getText().toString());
                intent.putExtra("rid", sRdsQrcode);
                if (tv_fukuan_bz.getText().equals("添加备注")) {
                } else {
                    intent.putExtra("user_note", sRdsQrcode);
                }

                startActivity(intent);

            }
        });
        tv_fukuan_bz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog();
            }
        });
        iv_fukuan_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void dialog() {
        final Dialog dialog = new Dialog(FukuanActivity.this, R.style.ActionSheetDialogStyle);
        final View inflate = LayoutInflater.from(FukuanActivity.this).inflate(R.layout.fukuan_bz_dialog, null);

        final EditText et_fukuan_bz = inflate.findViewById(R.id.et_fukuan_bz);
        TextView tv_fukuan_qx = inflate.findViewById(R.id.tv_fukuan_qx);
        TextView tv_fukuan_qr = inflate.findViewById(R.id.tv_fukuan_qr);
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
        dialog.show();
        tv_fukuan_qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_fukuan_bz.setText("添加备注");
                dialog.cancel();
            }
        });
        tv_fukuan_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_fukuan_bz.setText(et_fukuan_bz.getText().toString());
                dialog.cancel();
            }
        });
    }

}
