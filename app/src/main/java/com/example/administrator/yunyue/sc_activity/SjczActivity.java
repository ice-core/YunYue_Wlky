package com.example.administrator.yunyue.sc_activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.sc_gridview.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;

public class SjczActivity extends AppCompatActivity {
    private MyGridView mgv_sjcz;
    private String[] name = {"10", "20", "30", "50", "100", "200", "300", "500"};
    private String[] describe = {"售价：10.00元", "售价：20.00元", "售价：30.00元", "售价：50.00元",
            "售价：100.00元", "售价：200.00元", "售价：300.00元", "售价：500.00元"};
    private Dialog dialog;
    private View inflate;
    private ImageView iv_sjcz_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_sjcz);
        mgv_sjcz = (MyGridView) findViewById(R.id.mgv_sjcz);
        MyAdapter myAdapter = new MyAdapter(SjczActivity.this);
        ArrayList<HashMap<String, String>> myListrm = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < name.length; i++) {

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemName", name[i]);//id
            map.put("ItemDescribe", describe[i]);//id
            myListrm.add(map);
        }
        myAdapter.arrmylist = myListrm;
        iv_sjcz_back = findViewById(R.id.iv_sjcz_back);
        iv_sjcz_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mgv_sjcz.setAdapter(myAdapter);

    }

    public void show(String money) {
        dialog = new Dialog(SjczActivity.this, R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(SjczActivity.this).inflate(R.layout.sjcz_item, null);
        TextView tv_sjcz_money = (TextView) inflate.findViewById(R.id.tv_sjcz_money);
        TextView tv_sjcz_hbkj = (TextView) inflate.findViewById(R.id.tv_sjcz_hbkj);
        CheckBox cb_sjcz_hbkj = (CheckBox) inflate.findViewById(R.id.cb_sjcz_hbkj);
        Button bt_sjcz_qfk = (Button) inflate.findViewById(R.id.bt_sjcz_qfk);
        tv_sjcz_money.setText(money);
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }


    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        public ArrayList<HashMap<String, String>> arrmylist;

        //     public ArrayList<String> arr;


        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arrmylist = new ArrayList<HashMap<String, String>>();

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arrmylist.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        public Bitmap stringToBitmap(String string) {    // 将字符串转换成Bitmap类型
            Bitmap bitmap = null;
            try {
                byte[] bitmapArray;
                bitmapArray = Base64.decode(string, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }


        @Override
        public View getView(final int position, View view, ViewGroup arg2) {
            // TODO Auto-generated method stub
            if (view == null) {
                view = inflater.inflate(R.layout.sjcz_gv_item, null);
            }
            LinearLayout ll_sjcz_item = view.findViewById(R.id.ll_sjcz_item);
            final TextView tv_sjcz_name = view.findViewById(R.id.tv_sjcz_name);
            TextView tv_sjcz_describe = view.findViewById(R.id.tv_sjcz_describe);
            tv_sjcz_name.setText(arrmylist.get(position).get("ItemName"));
            tv_sjcz_describe.setText(arrmylist.get(position).get("ItemDescribe"));
            ll_sjcz_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    show(tv_sjcz_name.getText().toString() + ".00");
                }
            });
            return view;
        }
    }

}
