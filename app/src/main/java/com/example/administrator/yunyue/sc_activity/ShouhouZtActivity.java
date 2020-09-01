package com.example.administrator.yunyue.sc_activity;

import android.app.Dialog;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShouhouZtActivity extends AppCompatActivity {
    private static final String TAG = ShouhouZtActivity.class.getSimpleName();
    private SharedPreferences pref;
    private String sUser_id;
    private ImageView iv_shouhouzt_back;
    private ImageView iv_shfw;
    private TextView tv_shfw_shzt;
    private TextView tv_shfw_hint;
    private TextView tv_shfw_sckdbh;
    private String sShouhuoState = "";
    private String sState = "";
    private String sId = "";
    //  private String sShouhuotype = "";
    private Spinner sp_tkyy_tkgs;
    private Dialog dialog;
    private List<String> list_category;
    private List<String> list_category_id;
    private String sCategory = "";
    private ArrayAdapter<String> arr_adapter;
    RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_shouhou_zt);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        queue = Volley.newRequestQueue(ShouhouZtActivity.this);

        Intent intent = getIntent();
        sState = intent.getStringExtra("state");
        sShouhuoState = intent.getStringExtra("shouhuostate");
        sId = intent.getStringExtra("id");
        // sShouhuotype = intent.getStringExtra("shouhuotype");

        iv_shouhouzt_back = findViewById(R.id.iv_shouhouzt_back);
        iv_shouhouzt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        iv_shfw = findViewById(R.id.iv_shfw);
        tv_shfw_shzt = findViewById(R.id.tv_shfw_shzt);
        tv_shfw_hint = findViewById(R.id.tv_shfw_hint);
        tv_shfw_sckdbh = findViewById(R.id.tv_shfw_sckdbh);
        tv_shfw_sckdbh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_shfw_sckdbh.getText().toString().equals("上传快递编号")) {
                    dialog.show();
                } else {
                    Intent intent = new Intent(ShouhouZtActivity.this, SqshActivity.class);
                    intent.putExtra("id", sId);
                    intent.putExtra("state", sState);
                    startActivity(intent);
                }
            }
        });
        dialog();

        if (sShouhuoState.equals("1")) {
            iv_shfw.setImageResource(R.drawable.image_shenhezhong_3x);
            tv_shfw_shzt.setText("审核中");
            tv_shfw_hint.setText("卖家审核中，请您耐心等待");
            tv_shfw_sckdbh.setVisibility(View.GONE);
        } else if (sShouhuoState.equals("2")) {
            iv_shfw.setImageResource(R.drawable.image_shibai_3x);
            tv_shfw_shzt.setText("审核失败");
            tv_shfw_hint.setText("请你联系卖家协商，重新提交");
            tv_shfw_sckdbh.setVisibility(View.VISIBLE);
            tv_shfw_sckdbh.setText("重新提交");
        } else if (sShouhuoState.equals("3")) {
            iv_shfw.setImageResource(R.drawable.image_tongguo_3x);
            tv_shfw_shzt.setText("审核通过");
            tv_shfw_hint.setText("卖家审核通过，请上传快递编号");
            tv_shfw_sckdbh.setText("上传快递编号");
            // tv_shfw_sckdbh.setVisibility(View.VISIBLE);
/*            if (sShouhuotype == null) {
                tv_shfw_hint.setVisibility(View.GONE);
                tv_shfw_sckdbh.setVisibility(View.GONE);
            } else if (sShouhuotype.equals("1")) {
                tv_shfw_hint.setVisibility(View.GONE);
                tv_shfw_sckdbh.setVisibility(View.GONE);
            } else if (sShouhuotype.equals("2")) {
                tv_shfw_hint.setVisibility(View.VISIBLE);
                tv_shfw_sckdbh.setVisibility(View.VISIBLE);

            } else {
                tv_shfw_hint.setVisibility(View.GONE);
                tv_shfw_sckdbh.setVisibility(View.GONE);
            }*/
        } else if (sShouhuoState.equals("4")) {
            iv_shfw.setImageResource(R.drawable.image_tongguo_3x);
            tv_shfw_shzt.setText("审核通过");
            tv_shfw_hint.setText("卖家审核通过，等待卖家退款");
            tv_shfw_sckdbh.setVisibility(View.GONE);
        }
        sp_tkyy_tkgs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sCategory = list_category_id.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        kdgs();
    }


    /**
     * 获取快递公司
     */
    private void kdgs() {
        String url = Api.sUrl + "Api/Order/kuaidi/appId/" + Api.sApp_Id;
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
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                        list_category = new ArrayList<String>();
                        list_category_id = new ArrayList<String>();
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            list_category_id.add(jsonObject.getString("id"));
                            list_category.add(jsonObject.getString("name"));
                            if (i == 0) {
                                sCategory = jsonObject.getString("id");
                            }
                        }
                        arr_adapter = new ArrayAdapter<String>(ShouhouZtActivity.this, android.R.layout.simple_spinner_item, list_category);
                        //设置样式
                        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //加载适配器
                        sp_tkyy_tkgs.setAdapter(arr_adapter);

                    } else {

                    }
                } catch (JSONException e) {
                    hideDialogin();
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

    public void dialog() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        final View inflate = LayoutInflater.from(this).inflate(R.layout.shouhou_zt_dialog, null);
        // final TextView tv_lxmj_dialog = inflate.findViewById(R.id.tv_lxmj_dialog);
        sp_tkyy_tkgs = inflate.findViewById(R.id.sp_tkyy_tkgs);
        final EditText et_tkyy_tkdh = inflate.findViewById(R.id.et_tkyy_tkdh);
        TextView tv_tkyy_sc = inflate.findViewById(R.id.tv_tkyy_sc);
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (display.getWidth());
        dialogWindow.setAttributes(lp);
        // dialog.show();
        tv_tkyy_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_tkyy_tkdh.getText().toString().equals("")) {
                    Hint("快递单号不能为空！", HintDialog.WARM);
                } else {
                    dialogin("");
                    save(sId, et_tkyy_tkdh.getText().toString());
                    dialog.cancel();
                }
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

    private void save(String order_id, String KuaidiNumber) {
        String url = Api.sUrl + "Api/Order/servicekuaidi";
        RequestQueue requestQueue = Volley.newRequestQueue(ShouhouZtActivity.this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("order_id", order_id);
        params.put("kuaidi", KuaidiNumber);
        params.put("kuaidi_id", sCategory);
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
        loadingDialog = new LoadingDialog(ShouhouZtActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(ShouhouZtActivity.this, R.style.dialog, sHint, type, true).show();
    }

}
