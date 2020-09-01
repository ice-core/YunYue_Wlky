package com.example.administrator.yunyue.sc_activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class YetxActivity extends AppCompatActivity {
    private static final String TAG = YetxActivity.class.getSimpleName();
    private EditText et_yetx_ye;
    ListView lv_dialog;
    private Dialog dialog;
    private View inflate;
    private LinearLayout ll_yetx_yhk;
    MyAdapterlist myAdapterlist;
    RequestQueue queue = null;
    private TextView tv_yetx_yhk;

    private String sAmount;
    private String sHeadline;
    private Button bt_yetx_qrzc;
    private LinearLayout ll_tetx_back;
    private TextView tv_tx_headline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_yetx);
        Intent intent = getIntent();
        sAmount = intent.getStringExtra("amount");
        sHeadline = intent.getStringExtra("headline");
        queue = Volley.newRequestQueue(YetxActivity.this);
        et_yetx_ye = (EditText) findViewById(R.id.et_yetx_ye);
        ll_yetx_yhk = (LinearLayout) findViewById(R.id.ll_yetx_yhk);
        tv_yetx_yhk = (TextView) findViewById(R.id.tv_yetx_yhk);
        bt_yetx_qrzc = (Button) findViewById(R.id.bt_yetx_qrzc);
        ll_tetx_back = (LinearLayout) findViewById(R.id.ll_tetx_back);
        tv_tx_headline = (TextView) findViewById(R.id.tv_tx_headline);
        ll_tetx_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        ll_yetx_yhk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  dialogin("");
                huoqu("0", "1");*/
            }
        });
        et_yetx_ye.setText(sAmount);
        bt_yetx_qrzc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(YetxActivity.this, TixianActivity.class);
                intent.putExtra("amount", et_yetx_ye.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        tv_tx_headline.setText(sHeadline);
        show();
    }

    public void show() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(this).inflate(R.layout.yetx_item, null);
        lv_dialog = (ListView) inflate.findViewById(R.id.lv_dialog);
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        //  WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);

    }

    private void huoqu(String city, String level) {
        String url = Api.sUrl + "Address/cityList/city/" + city + "/level/" + level;
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

                        myAdapterlist.arrListIdsheng = new ArrayList<String>();
                        myAdapterlist.arrlistNamesheng = new ArrayList<String>();


                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            String resultId = jsonObject.getString("id");
                            String resultName = jsonObject.getString("name");

                            myAdapterlist.arrListIdsheng.add(resultId);
                            myAdapterlist.arrlistNamesheng.add(resultName);

                          /*  myAdapterlist.arrListIdqu.add(resultId);
                            myAdapterlist.arrlistNamequ.add(resultName);*/
                        }
                        lv_dialog.setAdapter(myAdapterlist);
                        myAdapterlist.notifyDataSetChanged();
                        dialog.show();
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
/*        Intent intent = new Intent(this, ScMainActivity.class);
        intent.putExtra("ID", "4");
        startActivity(intent);*/
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
        loadingDialog = new LoadingDialog(YetxActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(YetxActivity.this, R.style.dialog, sHint, type, true).show();
    }

    private class MyAdapterlist extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        public ArrayList<String> arrListIdsheng;
        public ArrayList<String> arrlistNamesheng;


        //public ArrayList<String> arr;

        public MyAdapterlist(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arrListIdsheng = new ArrayList<String>();
            arrlistNamesheng = new ArrayList<String>();

        }

        @Override
        public int getCount() {
            int returen = 0;
            // TODO Auto-generated method stub

            returen = arrListIdsheng.size();

            return returen;//arrListIdqu.size();
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


        @Override
        public View getView(final int position, View view, ViewGroup arg2) {
            // TODO Auto-generated method stub
            if (view == null) {
                view = inflater.inflate(R.layout.listview_item, null);
            }
            TextView tv_lv_dialog_id = (TextView) view.findViewById(R.id.tv_lv_dialog_id);
            TextView tv_lv_dialog_name = (TextView) view.findViewById(R.id.tv_lv_dialog_name);
            tv_lv_dialog_id.setText(arrListIdsheng.get(position));
            tv_lv_dialog_name.setText(arrlistNamesheng.get(position));
          /*  tv_lv_dialog_id.setText(arrListIdqu.get(position));
            tv_lv_dialog_name.setText(arrlistNamequ.get(position));*/
            tv_lv_dialog_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_yetx_yhk.setText(arrlistNamesheng.get(position));
              /*      tv_xzdz_qu.setText(arrlistNamequ.get(position));*/
                    dialog.dismiss();
                }
            });
            return view;
        }
    }
}
