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
import com.bumptech.glide.Glide;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc_activity.ZffsActivity;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import master.flame.danmaku.danmaku.model.FBDanmaku;

public class YlyTjddActivity extends AppCompatActivity {
    private static final String TAG = YlyTjddActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_yly_tjdd_back;
    /**
     * 房间logo
     */
    private ImageView iv_yly_tjdd_logo;
    /**
     * 养老院名称
     */
    private TextView tv_yly_tjdd_name;
    /**
     * 房间标题
     */
    private TextView tv_yly_tjdd_title;
    /**
     * 房间价格
     */
    private TextView tv_yly_tjdd_price;
    /**
     * 定金工分
     */
    private TextView tv_yly_tjdd_deposit;
    /**
     * 日期
     */
    private LinearLayout ll_yly_tjdd_rq;
    /**
     * 入住人数
     */
    private EditText et_yly_txdd_checkInNum;
    /**
     * 房间数量
     */
    private EditText et_yly_txdd_roomNum;
    /**
     * 住客姓名
     */
    private EditText et_yly_txdd_lodgerName;
    /**
     * 身份证号码
     */
    private EditText et_yly_txdd_IDnumber;
    /**
     * 联系人
     */
    private EditText et_yly_txdd_linkman;
    /**
     * 联系人电话
     */
    private EditText et_yly_txdd_linkphone;
    /**
     * 预计到院时间
     */
    private LinearLayout ll_yly_tjdd_predictTime;
    private TextView tv_yly_tjdd_predictTime;
    private TimePickerView pvCustomLunar;
    /**
     * 备注
     */
    private EditText et_yly_txdd_remark;
    /**
     * 定金
     */
    private TextView tv_yly_txdd_dj;
    /**
     * 立即预定
     */
    private TextView tv_yly_tjdd_ljyd;
    private String sUser_id = "";
    private SharedPreferences pref;
    private String sRoom_id = "";
    private String sRest_id = "";
    /**
     * 入住时间
     */
    private TextView tv_yly_tjdd_check_time;

    /**
     * 离开时间
     */
    private TextView tv_yly_tjdd_expire_time;
    /**
     * 时长
     */
    private TextView tv_yly_tjdd_time;
    private EditText et_zhifu_shuru;
    private int year = 0;
    private int month = 0;
    private int date = 0;
    private String yue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_yly_tjdd);
        pref = PreferenceManager.getDefaultSharedPreferences(YlyTjddActivity.this);
        sUser_id = pref.getString("user_id", "");
        sRoom_id = getIntent().getStringExtra("id");
        sRest_id = getIntent().getStringExtra("home_id");
        ll_yly_tjdd_back = findViewById(R.id.ll_yly_tjdd_back);
        ll_yly_tjdd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_yly_tjdd_check_time = findViewById(R.id.tv_yly_tjdd_check_time);
        tv_yly_tjdd_expire_time = findViewById(R.id.tv_yly_tjdd_expire_time);
        tv_yly_tjdd_time = findViewById(R.id.tv_yly_tjdd_time);


        Calendar cal = Calendar.getInstance();

        year = cal.get(cal.YEAR);
        month = cal.get(cal.MONTH) + 1;//老外把一月份整成了0，翻译成中国月份要加1
        date = cal.get(cal.DATE);
        if (month < 10) {
            yue = "0" + month;
        } else {
            yue = month + "";
        }


        iv_yly_tjdd_logo = findViewById(R.id.iv_yly_tjdd_logo);
        Glide.with(YlyTjddActivity.this)
                .load( Api.sUrl+FangjianxiangqingActivity.sImg)
                .asBitmap()
                .placeholder(R.mipmap.error)
                .error(R.mipmap.error)
                .dontAnimate()
                .into(iv_yly_tjdd_logo);
        tv_yly_tjdd_name = findViewById(R.id.tv_yly_tjdd_name);
        tv_yly_tjdd_name.setText(FangjianxiangqingActivity.sHome);
        tv_yly_tjdd_title = findViewById(R.id.tv_yly_tjdd_title);
        tv_yly_tjdd_title.setText(FangjianxiangqingActivity.sTitle);
        tv_yly_tjdd_price = findViewById(R.id.tv_yly_tjdd_price);
        tv_yly_tjdd_price.setText(FangjianxiangqingActivity.sPrice);
        tv_yly_tjdd_deposit = findViewById(R.id.tv_yly_tjdd_deposit);
        tv_yly_tjdd_deposit.setText(FangjianxiangqingActivity.sDeposit + "工分起");
        ll_yly_tjdd_rq = findViewById(R.id.ll_yly_tjdd_rq);
        ll_yly_tjdd_rq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YlyTjddActivity.this, TjddXzrqActivity.class);
                startActivity(intent);
            }
        });
        et_yly_txdd_checkInNum = findViewById(R.id.et_yly_txdd_checkInNum);
        et_yly_txdd_roomNum = findViewById(R.id.et_yly_txdd_roomNum);
        et_yly_txdd_lodgerName = findViewById(R.id.et_yly_txdd_lodgerName);
        et_yly_txdd_IDnumber = findViewById(R.id.et_yly_txdd_IDnumber);
        et_yly_txdd_linkman = findViewById(R.id.et_yly_txdd_linkman);
        et_yly_txdd_linkphone = findViewById(R.id.et_yly_txdd_linkphone);
        ll_yly_tjdd_predictTime = findViewById(R.id.ll_yly_tjdd_predictTime);
        tv_yly_tjdd_predictTime = findViewById(R.id.tv_yly_tjdd_predictTime);
        ll_yly_tjdd_predictTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvCustomLunar.show();
            }
        });
        et_yly_txdd_remark = findViewById(R.id.et_yly_txdd_remark);
        tv_yly_txdd_dj = findViewById(R.id.tv_yly_txdd_dj);
        tv_yly_txdd_dj.setText(FangjianxiangqingActivity.sDeposit + "工分起");
        tv_yly_tjdd_ljyd = findViewById(R.id.tv_yly_tjdd_ljyd);
        tv_yly_tjdd_ljyd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });
        initLunarPicker();
    }

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (TjddXzrqActivity.sEnd_time.equals("")) {
            tv_yly_tjdd_check_time.setText(year + "-" + yue + "-" + date);
            tv_yly_tjdd_expire_time.setText(year + "-" + yue + "-" + (date + 1));
            tv_yly_tjdd_time.setText("共1晚");
        } else {
            tv_yly_tjdd_check_time.setText(TjddXzrqActivity.sStart_time);
            tv_yly_tjdd_expire_time.setText(TjddXzrqActivity.sEnd_time);
            tv_yly_tjdd_time.setText("共" + TjddXzrqActivity.sRq.size() + "晚");
        }
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
        tv_zhifu_deposit.setText(FangjianxiangqingActivity.sDeposit + "工分");
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
                        String check_time = tv_yly_tjdd_check_time.getText().toString();
                        String expire_time = tv_yly_tjdd_expire_time.getText().toString();
                        String checkInNum = et_yly_txdd_checkInNum.getText().toString();
                        String linkman = et_yly_txdd_linkman.getText().toString();
                        String linkphone = et_yly_txdd_linkphone.getText().toString();
                        String roomNum = et_yly_txdd_roomNum.getText().toString();
                        String lodgerName = et_yly_txdd_lodgerName.getText().toString();
                        String IDnumber = et_yly_txdd_IDnumber.getText().toString();
                        String predictTime = tv_yly_tjdd_predictTime.getText().toString();
                        String remark = et_yly_txdd_remark.getText().toString();
                        if (check_time.equals("")) {
                            Hint("请先选择入住时间！", HintDialog.WARM);
                        } else if (expire_time.equals("")) {
                            Hint("请先选择离开时间！", HintDialog.WARM);
                        } else if (checkInNum.equals("")) {
                            Hint("请先填写入住人数！", HintDialog.WARM);
                        } else if (linkman.equals("")) {
                            Hint("请先填写联系人名称！", HintDialog.WARM);
                        } else if (linkphone.equals("")) {
                            Hint("请先填写联系人电话！", HintDialog.WARM);
                        } else if (roomNum.equals("")) {
                            Hint("请先填写房间数量！", HintDialog.WARM);
                        } else if (lodgerName.equals("")) {
                            Hint("请先填写入住人姓名！", HintDialog.WARM);
                        } else if (IDnumber.equals("")) {
                            Hint("请先填写身份证号！", HintDialog.WARM);
                        } else if (predictTime.equals("")) {
                            Hint("请先选择预计到院时间！", HintDialog.WARM);
                        } else {
                            sSetRestHomeOrder(check_time, expire_time, checkInNum, linkman, linkphone, roomNum,
                                    lodgerName, IDnumber, predictTime, String.valueOf(editable), remark);
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
                tv_yly_tjdd_predictTime.setText(getTime(date));
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


    /**
     * 方法名：sSetRestHomeOrder()
     * 功  能：添加养老院房间订单接口
     * 参  数：appId
     */
    private void sSetRestHomeOrder(String sCheck_time, String sExpire_time, String sCheckInNum, String sLinkman,
                                   String sLinkphone, String sRoomNum, String sLodgerName, String sIDnumber,
                                   String sPredictTime, String sCode, String sRemark) {
        hideDialogin();
        dialogin("");
        String url = Api.sUrl + Api.sSetRestHomeOrder;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("room_id", sRoom_id);
        params.put("rest_id", sRest_id);
        params.put("check_time", sCheck_time);//入住时间
        params.put("expire_time", sExpire_time);//离开时间
        params.put("checkInNum", sCheckInNum);//入住人数
        params.put("linkman", sLinkman);//联系人名称
        params.put("linkphone", sLinkphone);//	联系人电话
        params.put("roomNum", sRoomNum);//房间数量
        params.put("lodgerName", sLodgerName);//	入住人姓名
        params.put("IDnumber", sIDnumber);//身份证号
        params.put("predictTime", sPredictTime);//	预计到院时间
        params.put("code", sCode);//支付密码
        if (!sRemark.equals("")) {
            params.put("remark", sRemark);//备注
        }
        JSONObject jsonObject = new JSONObject(params);
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialogin();
                        String sDate = response.toString();
                        System.out.println(sDate);
                        try {
                            JSONObject jsonObject0 = new JSONObject(sDate);
                            String resultMsg = jsonObject0.getString("msg");
                            int resultCode = jsonObject0.getInt("code");
                            if (resultCode > 0) {

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

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(YlyTjddActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(YlyTjddActivity.this, R.style.dialog, sHint, type, true).show();
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
}
