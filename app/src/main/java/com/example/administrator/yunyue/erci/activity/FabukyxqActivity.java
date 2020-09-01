package com.example.administrator.yunyue.erci.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc_activity.ShdzActivity;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import master.flame.danmaku.danmaku.model.FBDanmaku;

public class FabukyxqActivity extends AppCompatActivity {
    private static final String TAG = FabukyxqActivity.class.getSimpleName();
    private SharedPreferences pref;
    private String sUser_id = "";
    /**
     * 返回
     */
    private LinearLayout ll_fbkyxq_back;
    /**
     * 发布
     */
    private TextView tv_fbkyxq_fb;
    /**
     * 预算工分
     */
    private EditText et_fbkyxq_budget;
    /**
     * 服务地点
     */
    private TextView tv_fbkyxq_service_place;
    private LinearLayout ll_fbkyxq_service_place;
    public static String sService_place = "";
    /**
     * 服务时间
     */
    private TextView tv_fbkyxq_service_time;
    private LinearLayout ll_fbkyxq_service_time;
    /**
     * 标题
     */
    private EditText et_fbkyxq_title;
    /**
     * 描述
     */
    private EditText et_fbkyxq_describe;
    /**
     * 联系人名称
     */
    private EditText et_fbkyxq_linkman;
    /**
     * 联系人电话
     */
    private EditText et_fbkyxq_linkphone;
    private TimePickerView pvCustomLunar;
    private EditText et_zhifu_shuru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white);
        }
        setContentView(R.layout.activity_fabukyxq);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        ll_fbkyxq_back = findViewById(R.id.ll_fbkyxq_back);
        ll_fbkyxq_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_fbkyxq_fb = findViewById(R.id.tv_fbkyxq_fb);
        tv_fbkyxq_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });
        et_fbkyxq_budget = findViewById(R.id.et_fbkyxq_budget);
        tv_fbkyxq_service_place = findViewById(R.id.tv_fbkyxq_service_place);
        tv_fbkyxq_service_time = findViewById(R.id.tv_fbkyxq_service_time);
        et_fbkyxq_title = findViewById(R.id.et_fbkyxq_title);
        et_fbkyxq_describe = findViewById(R.id.et_fbkyxq_describe);
        et_fbkyxq_linkman = findViewById(R.id.et_fbkyxq_linkman);
        et_fbkyxq_linkphone = findViewById(R.id.et_fbkyxq_linkphone);
        ll_fbkyxq_service_place = findViewById(R.id.ll_fbkyxq_service_place);
        ll_fbkyxq_service_time = findViewById(R.id.ll_fbkyxq_service_time);
        ll_fbkyxq_service_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FabukyxqActivity.this, ShdzActivity.class);
                startActivity(intent);
            }
        });
        ll_fbkyxq_service_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvCustomLunar.show();
            }
        });
        initLunarPicker();
    }


    public void dialog() {
        Dialog dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        final View inflate = LayoutInflater.from(this).inflate(R.layout.zhifu_item, null);
        TextView tv_zhifu_hint = inflate.findViewById(R.id.tv_zhifu_hint);
        final ImageView iv_zhifu_close = inflate.findViewById(R.id.iv_zhifu_close);
        final ImageView iv_zhifu_pic = inflate.findViewById(R.id.iv_zhifu_pic);
        iv_zhifu_pic.setVisibility(View.GONE);
        final TextView tv_zhifu_deposit = inflate.findViewById(R.id.tv_zhifu_deposit);
        LinearLayout ll_zhifu_type = inflate.findViewById(R.id.ll_zhifu_type);
        ll_zhifu_type.setVisibility(View.GONE);
        et_zhifu_shuru = inflate.findViewById(R.id.et_zhifu_shuru);
        final TextView tv_zhifu_paypwd = inflate.findViewById(R.id.tv_zhifu_paypwd);
        final TextView tv_zhifu_paypwd1 = inflate.findViewById(R.id.tv_zhifu_paypwd1);
        final TextView tv_zhifu_paypwd2 = inflate.findViewById(R.id.tv_zhifu_paypwd2);
        final TextView tv_zhifu_paypwd3 = inflate.findViewById(R.id.tv_zhifu_paypwd3);
        final TextView tv_zhifu_paypwd4 = inflate.findViewById(R.id.tv_zhifu_paypwd4);
        final TextView tv_zhifu_paypwd5 = inflate.findViewById(R.id.tv_zhifu_paypwd5);
        tv_zhifu_hint.setVisibility(View.GONE);
        ImageView iv_zhifu_type = inflate.findViewById(R.id.iv_zhifu_type);
        TextView tv_zhifu_type = inflate.findViewById(R.id.tv_zhifu_type);
        tv_zhifu_deposit.setText(et_fbkyxq_budget.getText().toString() + "工分");
        iv_zhifu_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dialogin("");\
                dialog.dismiss();

            }
        });
        et_zhifu_shuru.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int a = editable.length();
                tv_zhifu_paypwd.setText("");
                tv_zhifu_paypwd1.setText("");
                tv_zhifu_paypwd2.setText("");
                tv_zhifu_paypwd3.setText("");
                tv_zhifu_paypwd4.setText("");
                tv_zhifu_paypwd5.setText("");

                switch (editable.length()) {
                    case 6:
                        tv_zhifu_paypwd5.setText(editable.subSequence(5, 6));
                        tv_zhifu_paypwd5.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        String sBudget = et_fbkyxq_budget.getText().toString();
                        String sService_time = tv_fbkyxq_service_time.getText().toString();
                        String sTitle = et_fbkyxq_title.getText().toString();
                        String sDescribe = et_fbkyxq_describe.getText().toString();
                        String sLinkman = et_fbkyxq_linkman.getText().toString();
                        String sLinkphone = et_fbkyxq_linkphone.getText().toString();
                        if (sBudget.equals("")) {
                            Hint("请先填写预算工分！", HintDialog.WARM);
                        } else if (sService_place.equals("")) {
                            Hint("请先选择服务地址！", HintDialog.WARM);
                        } else if (sService_time.equals("")) {
                            Hint("请先选择服务时间！", HintDialog.WARM);
                        } else if (sTitle.equals("")) {
                            Hint("服务内容不能为空！", HintDialog.WARM);
                        } else if (sDescribe.equals("")) {
                            Hint("服务描述不能为空！", HintDialog.WARM);
                        } else if (sLinkman.equals("")) {
                            Hint("联系人名称不能为空！", HintDialog.WARM);
                        } else if (sLinkphone.equals("")) {
                            Hint("联系人电话不能为空！", HintDialog.WARM);
                        } else {
                            sSetRecoveryNeed(sBudget, sService_place, sService_time, sTitle, sDescribe,
                                    sLinkman, sLinkphone, String.valueOf(editable));
                        }
                        dialog.dismiss();
                    case 5:
                        tv_zhifu_paypwd4.setText(editable.subSequence(4, 5));
                        tv_zhifu_paypwd4.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    case 4:
                        tv_zhifu_paypwd3.setText(editable.subSequence(3, 4));
                        tv_zhifu_paypwd3.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    case 3:
                        tv_zhifu_paypwd2.setText(editable.subSequence(2, 3));
                        tv_zhifu_paypwd2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    case 2:
                        tv_zhifu_paypwd1.setText(editable.subSequence(1, 2));
                        tv_zhifu_paypwd1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    case 1:
                        tv_zhifu_paypwd.setText(editable.subSequence(0, 1));
                        tv_zhifu_paypwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    default:
                        break;
                }
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

    /**
     * 农历时间已扩展至 ： 1900 - 2100年
     */
    private void initLunarPicker() {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2069, 2, 28);
        //时间选择器 ，自定义布局
        pvCustomLunar = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // Toast.makeText(FbxxActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
                tv_fbkyxq_service_time.setText(getTime(date));
            }
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_lunar, new CustomListener() {

                    @Override
                    public void customLayout(final View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        TextView tv_cancel = v.findViewById(R.id.tv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomLunar.returnData();
                                pvCustomLunar.dismiss();
                            }
                        });
                        tv_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomLunar.dismiss();
                            }
                        });
                        //公农历切换
                      /*  CheckBox cb_lunar = (CheckBox) v.findViewById(R.id.cb_lunar);
                        cb_lunar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                pvCustomLunar.setLunarCalendar(!pvCustomLunar.isLunarCalendar());
                                //自适应宽
                                setTimePickerChildWeight(v, isChecked ? 0.8f : 1f, isChecked ? 1f : 1.1f);
                            }
                        });*/

                    }

                    /**
                     * 公农历切换后调整宽
                     * @param v
                     * @param yearWeight
                     * @param weight
                     */
                    private void setTimePickerChildWeight(View v, float yearWeight, float weight) {
                        ViewGroup timePicker = (ViewGroup) v.findViewById(R.id.timepicker);
                        View year = timePicker.getChildAt(0);
                        LinearLayout.LayoutParams lp = ((LinearLayout.LayoutParams) year.getLayoutParams());
                        lp.weight = yearWeight;
                        year.setLayoutParams(lp);
                        for (int i = 1; i < timePicker.getChildCount(); i++) {
                            View childAt = timePicker.getChildAt(i);
                            LinearLayout.LayoutParams childLp = ((LinearLayout.LayoutParams) childAt.getLayoutParams());
                            childLp.weight = weight;
                            childAt.setLayoutParams(childLp);
                        }
                    }
                })
                .setType(new boolean[]{true, true, true, true, true, false})
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.RED)
                .build();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }


    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        tv_fbkyxq_service_place.setText(sService_place);
    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(FabukyxqActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(FabukyxqActivity.this, R.style.dialog, sHint, type, true).show();
    }

    private void error(VolleyError error) {
        Log.e(TAG, error.getMessage(), error);
        if (error != null) {
            if (error instanceof TimeoutError) {
                // Toast.makeText(mActivity,"网络请求超时，请重试！",Toast.LENGTH_SHORT).show();
                Hint("网络请求超时，请重试！", HintDialog.ERROR);
                return;
            }
            if (error instanceof ServerError) {
                //  Toast.makeText(mActivity,"服务器异常",Toast.LENGTH_SHORT).show();
                Hint("服务器异常", HintDialog.ERROR);
                return;
            }
            if (error instanceof NetworkError) {
                // Toast.makeText(mActivity,"请检查网络",Toast.LENGTH_SHORT).show();
                Hint("请检查网络", HintDialog.ERROR);
                return;
            }
            if (error instanceof ParseError) {
                Hint("数据格式错误", HintDialog.ERROR);
                //  Toast.makeText(mActivity,"数据格式错误",Toast.LENGTH_SHORT).show();
                return;
            }

            Hint(error.toString(), HintDialog.ERROR);
        }
    }

    /**
     * 方法名：sSetRecoveryNeed()
     * 功  能：发布需求接口
     * 参  数：appId
     */
    private void sSetRecoveryNeed(String sBudget, String sService_place, String sService_time, String sTitle,
                                  String sDescribe, String sLinkman, String sLinkphone, String sCode) {
        hideDialogin();
        dialogin("");
        String url = Api.sUrl + Api.sSetRecoveryNeed;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("budget", sBudget);
        params.put("service_place", sService_place);
        params.put("service_time", sService_time);
        params.put("title", sTitle);
        params.put("describe", sDescribe);
        params.put("linkman", sLinkman);
        params.put("linkphone", sLinkphone);
        params.put("code", sCode);
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
                                String resultData = jsonObject1.getString("data");
                                Hint(resultMsg, HintDialog.SUCCESS);
                                try {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                finish();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, 1200);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

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
                //  Hint(error.getMessage(), HintDialog.ERROR);
                error(error);

            }
        });
        requestQueue.add(jsonRequest);
    }
}
