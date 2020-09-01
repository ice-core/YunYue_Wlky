package com.example.administrator.yunyue.sc_activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.example.administrator.yunyue.sc.Validator;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class XjshdzActivity extends AppCompatActivity {
    private static final String TAG = XjshdzActivity.class.getSimpleName();
    private EditText et_xjshdz_name, et_xjshdz_mobile, et_xjshdz_address;
    private Switch sw_xjshdz_default;
    private Button bt_xjshdz_save;
    private TextView tv_xjshdz_sheng, tv_xjshdz_shi, tv_xjshdz_qu;
    MyAdapterlist myAdapterlist;
    private LinearLayout ll_xjshdz_back;
    RequestQueue queue = null;
    ListView lv_dialog;
    LinearLayout ll_ssq_sheng;
    LinearLayout ll_ssq_shi;
    LinearLayout ll_ssq_qu;
    TextView tv_ssq_sheng;
    TextView tv_ssq_shi;
    TextView tv_ssq_qu;
    TextView tv_ssq_sheng_xhx;
    TextView tv_ssq_shi_xhx;
    TextView tv_ssq_qu_xhx;
    private Dialog dialog;
    private View inflate;
    Validator validator = new Validator();
    private SharedPreferences pref;
    private String sUser_id;
    private ImageView iv_xjshdz_back;
    public static ArrayList<HashMap<String, String>> mylist;
    private String sId = "";
    private TextView tv_bjshdz_sc;
    private TextView tv_xjshdz_hint;
    public static ArrayList<HashMap<String, String>> mylistQrdd;
    private String sActivity = "";

    /**
     * 省--0
     * 市--1
     * 区--2
     */
    private int iType = 0;
    private String itemid = "";
    private String sCitylist = "";
    private String sheng = "", shi = "", qu = "";
    private String sDz = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_xjshdz);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        Intent intent = getIntent();
        sId = intent.getStringExtra("id");
        sActivity = intent.getStringExtra("activity");

        queue = Volley.newRequestQueue(XjshdzActivity.this);
        et_xjshdz_name = (EditText) findViewById(R.id.et_xjshdz_name);
        tv_xjshdz_sheng = (TextView) findViewById(R.id.tv_xjshdz_sheng);
        tv_xjshdz_shi = (TextView) findViewById(R.id.tv_xjshdz_shi);
        tv_xjshdz_qu = (TextView) findViewById(R.id.tv_xjshdz_qu);
        et_xjshdz_mobile = (EditText) findViewById(R.id.et_xjshdz_mobile);
        et_xjshdz_address = (EditText) findViewById(R.id.et_xjshdz_address);
        sw_xjshdz_default = (Switch) findViewById(R.id.sw_xjshdz_default);
        bt_xjshdz_save = (Button) findViewById(R.id.bt_xjshdz_save);
        ll_xjshdz_back = (LinearLayout) findViewById(R.id.ll_xjshdz_back);
        iv_xjshdz_back = (ImageView) findViewById(R.id.iv_xjshdz_back);
        tv_bjshdz_sc = (TextView) findViewById(R.id.tv_bjshdz_sc);
        tv_xjshdz_hint = (TextView) findViewById(R.id.tv_xjshdz_hint);
        if (sId == null) {
            tv_bjshdz_sc.setVisibility(View.GONE);
            tv_xjshdz_hint.setText(this.getString(R.string.xjshdz));
        } else if (sId.equals("")) {
            tv_xjshdz_hint.setText(this.getString(R.string.xjshdz));
        } else {
            tv_xjshdz_hint.setText(this.getString(R.string.bjshdz));
            tv_bjshdz_sc.setVisibility(View.VISIBLE);
            int iId = Integer.valueOf(sId);
            et_xjshdz_name.setText(mylist.get(iId).get("ItemName"));
            et_xjshdz_mobile.setText(mylist.get(iId).get("ItemMobile"));
            tv_xjshdz_sheng.setText(mylist.get(iId).get("ItemSheng") + mylist.get(iId).get("ItemShi") + mylist.get(iId).get("ItemQu"));
            et_xjshdz_address.setText(mylist.get(iId).get("ItemDetail"));
            sheng = mylist.get(iId).get("ItemSheng");
            shi = mylist.get(iId).get("ItemShi");
            qu = mylist.get(iId).get("ItemQu");
            if (mylist.get(iId).get("ItemIsDefault").equals("0")) {
                sw_xjshdz_default.setChecked(false);
            } else if (mylist.get(iId).get("ItemIsDefault").equals("1")) {
                sw_xjshdz_default.setChecked(true);
            }
        }
        tv_bjshdz_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int iId = Integer.valueOf(sId);
                dialogin("");
                Delete(mylist.get(iId).get("ItemId"));
            }
        });

        iv_xjshdz_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        ll_xjshdz_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        myAdapterlist = new MyAdapterlist(XjshdzActivity.this);
        tv_xjshdz_sheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sDz = tv_xjshdz_sheng.getText().toString();
                iType = 0;
                tv_xjshdz_sheng.setText("");
                tv_xjshdz_shi.setText("");
                tv_xjshdz_qu.setText("");
                hideDialogin();
                dialogin("");
                myAdapterlist.option = "sheng";
                huoqu("0", "1");

            }
        });
        tv_xjshdz_shi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemid = "";
                tv_xjshdz_qu.setText("");
                for (int i = 0; i < myAdapterlist.arrListIdsheng.size(); i++) {
                    if (myAdapterlist.arrlistNamesheng.get(i).equals(tv_xjshdz_sheng.getText().toString())) {
                        itemid = myAdapterlist.arrListIdsheng.get(i);
                        //  back();
                        break;
                    }
                }
                if (itemid.equals("")) {
                    Hint("请选择省", HintDialog.WARM);
                } else {
                    dialogin("");
                    myAdapterlist.option = "shi";
                    huoqu(itemid, "2");
                }
            }
        });
        tv_xjshdz_qu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemid = "";
                for (int i = 0; i < myAdapterlist.arrListIdshi.size(); i++) {
                    if (myAdapterlist.arrlistNameshi.get(i).equals(tv_xjshdz_shi.getText().toString())) {
                        itemid = myAdapterlist.arrListIdshi.get(i);
                        //   back();
                        break;
                    }
                }
                if (itemid.equals("")) {
                    Hint("请选择市", HintDialog.WARM);
                } else {
                    dialogin("");
                    myAdapterlist.option = "qu";
                    huoqu(itemid, "3");
                }
            }
        });
        bt_xjshdz_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_xjshdz_name.getText().equals("")) {
                    Hint("姓名不能为空！", HintDialog.WARM);
                } else if (!validator.isMobile(et_xjshdz_mobile.getText().toString())) {
                    Hint("请输入正确的手机号", HintDialog.WARM);
                } else if (sheng.equals("")) {
                    Hint("请选择所在地区！", HintDialog.WARM);
                } else if (shi.equals("")) {
                    Hint("请选择市！", HintDialog.WARM);
                } else if (qu.equals("")) {
                    Hint("请选择区！", HintDialog.WARM);
                } else if (et_xjshdz_address.getText().equals("")) {
                    Hint("请填写详细地址！", HintDialog.WARM);
                } else {
                    String sDefault = "";
                    if (sw_xjshdz_default.isChecked()) {
                        sDefault = "1";
                    } else {
                        sDefault = "2";
                    }

                    dialogin("");
                    if (sId == null) {
                        save(et_xjshdz_name.getText().toString(), et_xjshdz_mobile.getText().toString(), et_xjshdz_address.getText().toString(), sDefault);
                    } else if (sId.equals("")) {
                        save(et_xjshdz_name.getText().toString(), et_xjshdz_mobile.getText().toString(), et_xjshdz_address.getText().toString(), sDefault);
                    } else {
                        int iId = Integer.valueOf(sId);
                        update(mylist.get(iId).get("ItemId"), et_xjshdz_name.getText().toString(), et_xjshdz_mobile.getText().toString(), et_xjshdz_address.getText().toString(), sDefault);
                    }
                }
            }
        });
        show();
    }

    /**
     * 修改地址
     */
    private void update(String address_id, String consignee, String mobile, String address, String sDefault) {
        String url = Api.sUrl + "Api/Order/addressedit/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/address_id/" + address_id + "/username/" + consignee + "/phone/" + mobile + "/sheng/" + sheng
                + "/shi/" + shi + "/qu/" + qu + "/detail/" + address + "/is_default/" + sDefault;
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
                        et_xjshdz_name.setText("");
                        et_xjshdz_mobile.setText("");
                        tv_xjshdz_sheng.setText("");
                        tv_xjshdz_shi.setText("");
                        tv_xjshdz_qu.setText("");
                        et_xjshdz_address.setText("");
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

    /**
     * 新增地址
     */

    private void save(String consignee, String mobile, String address, String sDefault) {
        String url = Api.sUrl + "Api/Order/addressadd/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/username/" + consignee + "/phone/" + mobile + "/sheng/" + sheng
                + "/shi/" + shi + "/qu/" + qu + "/detail/" + address + "/is_default/" + sDefault;
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

                        et_xjshdz_name.setText("");
                        et_xjshdz_mobile.setText("");
                        tv_xjshdz_sheng.setText("");
                        tv_xjshdz_shi.setText("");
                        tv_xjshdz_qu.setText("");
                        et_xjshdz_address.setText("");
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

    public void show() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(this).inflate(R.layout.list_item, null);
        lv_dialog = (ListView) inflate.findViewById(R.id.lv_dialog);
        ImageView iv_shdz_gb = inflate.findViewById(R.id.iv_shdz_gb);
        ll_ssq_sheng = inflate.findViewById(R.id.ll_ssq_sheng);
        ll_ssq_shi = inflate.findViewById(R.id.ll_ssq_shi);
        ll_ssq_qu = inflate.findViewById(R.id.ll_ssq_qu);
        tv_ssq_sheng = inflate.findViewById(R.id.tv_ssq_sheng);
        tv_ssq_shi = inflate.findViewById(R.id.tv_ssq_shi);
        tv_ssq_qu = inflate.findViewById(R.id.tv_ssq_qu);
        tv_ssq_sheng_xhx = inflate.findViewById(R.id.tv_ssq_sheng_xhx);
        tv_ssq_shi_xhx = inflate.findViewById(R.id.tv_ssq_shi_xhx);
        tv_ssq_qu_xhx = inflate.findViewById(R.id.tv_ssq_qu_xhx);
        iv_shdz_gb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_xjshdz_sheng.setText(sDz);
                dialog.dismiss();
            }
        });
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        //  WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setAttributes(lp);
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

    private void Delete(String address_id) {
        String url = Api.sUrl + "Api/Order/addressdel/appId/" + Api.sApp_Id + "/address_id/" + address_id;
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

    private void huoqu(String city, String level) {
        String url = Api.sUrl + "Api/Order/cityList/appId/" + Api.sApp_Id
                + "/city/" + city + "/level/" + level;
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

                        }
                        lv_dialog.setAdapter(myAdapterlist);
                        myAdapterlist.notifyDataSetChanged();
                        dialog.show();
                        if (iType == 0) {
                            iType = 1;
                        } else if (iType == 1) {
                            iType = 2;
                        } else if (iType == 2) {
                            iType = 3;
                        }

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
        ShdzActivity.mylistQrdd = mylistQrdd;
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
        loadingDialog = new LoadingDialog(XjshdzActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(XjshdzActivity.this, R.style.dialog, sHint, type, true).show();
    }


    private class MyAdapterlist extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        public ArrayList<String> arrListIdsheng;
        public ArrayList<String> arrlistNamesheng;
        public ArrayList<String> arrListIdshi;
        public ArrayList<String> arrlistNameshi;
        public ArrayList<String> arrListIdqu;
        public ArrayList<String> arrlistNamequ;
        public String option = "";

        //public ArrayList<String> arr;

        public MyAdapterlist(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arrListIdsheng = new ArrayList<String>();
            arrlistNamesheng = new ArrayList<String>();
            arrListIdshi = new ArrayList<String>();
            arrlistNameshi = new ArrayList<String>();
            arrListIdqu = new ArrayList<String>();
            arrlistNamequ = new ArrayList<String>();

        }

        @Override
        public int getCount() {
            int returen = 0;
            // TODO Auto-generated method stub
            if (myAdapterlist.option.equals("sheng")) {
                returen = arrListIdsheng.size();
            } else if (myAdapterlist.option.equals("shi")) {
                returen = arrListIdshi.size();
            } else if (myAdapterlist.option.equals("qu")) {
                returen = arrListIdqu.size();
            }
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
            tv_lv_dialog_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    tv_xjshdz_sheng.setText(tv_xjshdz_sheng.getText().toString() + arrlistNamesheng.get(position));
                    if (iType == 1) {
                        sCitylist = arrListIdsheng.get(position);
                        ll_ssq_shi.setVisibility(View.VISIBLE);
                        tv_ssq_sheng.setText(arrlistNamesheng.get(position));
                        sheng = arrlistNamesheng.get(position);
                        tv_ssq_sheng.setTextColor(tv_ssq_sheng.getResources().getColor(R.color.black));
                        tv_ssq_sheng_xhx.setVisibility(View.GONE);
                        itemid = arrListIdsheng.get(position);

                        dialogin("");
                        huoqu(itemid, String.valueOf(iType + 1));
                    } else if (iType == 2) {
                        sCitylist = sCitylist + "-" + arrListIdsheng.get(position);
                        ll_ssq_qu.setVisibility(View.VISIBLE);
                        tv_ssq_shi.setText(arrlistNamesheng.get(position));
                        shi = arrlistNamesheng.get(position);
                        tv_ssq_shi.setTextColor(tv_ssq_shi.getResources().getColor(R.color.black));
                        tv_ssq_shi_xhx.setVisibility(View.GONE);
                        itemid = arrListIdsheng.get(position);
                        dialogin("");
                        huoqu(itemid, String.valueOf(iType + 1));
                    } else if (iType == 3) {
                        sCitylist = sCitylist + "-" + arrListIdsheng.get(position);
                        tv_ssq_qu.setText(arrlistNamesheng.get(position));
                        qu = arrlistNamesheng.get(position);
                        tv_ssq_qu.setTextColor(tv_ssq_qu.getResources().getColor(R.color.black));
                        tv_ssq_qu_xhx.setVisibility(View.GONE);
                        itemid = arrListIdsheng.get(position);

                        dialog.dismiss();
                    }
              /*      tv_xzdz_qu.setText(arrlistNamequ.get(position));*/

                }
            });
            return view;
        }
    }
}
