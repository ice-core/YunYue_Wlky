package com.example.administrator.yunyue.erci.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sq_activity.DakaActivity;
import com.example.administrator.yunyue.utils.NullTranslator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.rong.imlib.IFwLogCallback;
import master.flame.danmaku.danmaku.model.FBDanmaku;

public class TjddXzrqActivity extends AppCompatActivity {
    private LinearLayout ll_xzrq_back;
    private GridView gv_daka;
    private int year = 0;
    private int month = 0;
    private int tian = 0;
    private int yuechu = 0;
    private int yuemo = 0;
    private int zong = 0;
    private int date = 0;
    RequestQueue queue = null;
    private SharedPreferences pref;
    private String sUser_id = "";
    private String sIs_vip = "";
    //private FrameLayout fl_daka_qd;
    private String[] sDkjl;
    public static List<String> sRq;
    private TextView tv_dk_rq;
    private String yue = "";
    private TextView tv_xzrq_year_month;

    public static String sStart_time = "";
    public static String sEnd_time = "";
    private MyAdapter myAdapter;
    /**
     * 入驻时间
     */
    private TextView tv_xzrq_start_tiem;
    /**
     * 离开日期
     */
    private TextView tv_xzrq_end_tiem;
    /**
     * 上一个月
     */
    private ImageView iv_xzrq_shang;
    /**
     * 下一个月
     */
    private ImageView iv_xzrq_xia;
    /**
     * 保存
     */
    private TextView tv_xzrq_save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_tjdd_xzrq);
        ll_xzrq_back = findViewById(R.id.ll_xzrq_back);
        ll_xzrq_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_xzrq_year_month = findViewById(R.id.tv_xzrq_year_month);
        tv_xzrq_start_tiem = findViewById(R.id.tv_xzrq_start_tiem);
        tv_xzrq_end_tiem = findViewById(R.id.tv_xzrq_end_tiem);
        iv_xzrq_shang = findViewById(R.id.iv_xzrq_shang);
        iv_xzrq_xia = findViewById(R.id.iv_xzrq_xia);
        tv_xzrq_save = findViewById(R.id.tv_xzrq_save);
        tv_xzrq_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sStart_time.equals("")) {
                    Hint("请先选择入住时间！", HintDialog.WARM);
                } else if (sEnd_time.equals("")) {
                    Hint("请先选择离开时间！", HintDialog.WARM);
                } else {
                    finish();
                }
            }
        });

        gv_daka = findViewById(R.id.gv_daka);
        Calendar cal = Calendar.getInstance();

        year = cal.get(cal.YEAR);
        month = cal.get(cal.MONTH) + 1;//老外把一月份整成了0，翻译成中国月份要加1
        date = cal.get(cal.DATE);
        if (month < 10) {
            yue = "0" + month;
        } else {
            yue = month + "";
        }
        sRq = new ArrayList<>();
        sRq.add(year + "-" + yue + "-" + date);
        sRq.add(year + "-" + yue + "-" + (date + 1));
        sDkjl = sRq.toArray(new String[sRq.size()]);

        tv_xzrq_year_month.setText(year + "年" + yue + "月");
        tv_xzrq_start_tiem.setText(yue + "月" + date + "日");
        tv_xzrq_end_tiem.setText(yue + "月" + (date + 1) + "日");
        sStart_time = year + "-" + yue + "-" + date;
        sEnd_time = year + "-" + yue + "-" + (date + 1);
        intent();

        iv_xzrq_shang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (month == 1) {
                    year = year - 1;
                    month = 12;
                } else {
                    month = month - 1;
                }
                if (month < 10) {
                    yue = "0" + month;
                } else {
                    yue = month + "";
                }
                tv_xzrq_year_month.setText(year + "年" + yue + "月");
                intent();
            }
        });
        iv_xzrq_xia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (month == 12) {
                    year = year + 1;
                    month = 1;
                } else {
                    month = month + 1;
                }
                if (month < 10) {
                    yue = "0" + month;
                } else {
                    yue = month + "";
                }
                tv_xzrq_year_month.setText(year + "年" + yue + "月");
                intent();
            }
        });
    }

    private void intent() {
        tian = getDaysOfMonth(year, month);
        yuechu = getDayofweek(year + "-" + month + "-1") - 1;
        yuemo = getDayofweek(year + "-" + month + "-" + tian) - 1;
        zong = tian + yuechu;
        zong = zong + (6 - yuemo);


        gv_qunliao();
        //tv_dk_rq.setText(year + "-" + yue + "-" + date);
    }


    /**
     * 获取两个日期之间的所有日期
     *
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return
     */
    public static List<String> getDays(String startTime, String endTime) {

        // 返回的日期集合
        List<String> days = new ArrayList<String>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                days.add(dateFormat.format(tempStart.getTime()));
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }


    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(TjddXzrqActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(TjddXzrqActivity.this, R.style.dialog, sHint, type, true).show();
    }

    public static int getDayofweek(String date) {
        Calendar cal = Calendar.getInstance();
//   cal.setTime(new Date(System.currentTimeMillis()));
        if (date.equals("")) {
            cal.setTime(new Date(System.currentTimeMillis()));
        } else {
            cal.setTime(new Date(getDateByStr2(date).getTime()));
        }
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static Date getDateByStr2(String dd) {

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = sd.parse(dd);
        } catch (ParseException e) {
            date = null;
            e.printStackTrace();
        }
        return date;
    }

    //取得某个月有多少天
    public static int getDaysOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        int days_of_month = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return days_of_month;
    }

    /**
     * 群聊
     */
    private void gv_qunliao() {
        myAdapter = new MyAdapter(this);
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < zong; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            if (i < yuechu) {
                map.put("ItemId", "");
                map.put("ItemIs", "");
                mylist.add(map);
            } else if (i > (tian + yuechu - 1)) {
                map.put("ItemId", "");
                map.put("ItemIs", "");
                mylist.add(map);
            } else {
                String day = "";
                if ((i - yuechu + 1) < 10) {
                    day = "0" + (i - yuechu + 1);
                } else {
                    day = "" + (i - yuechu + 1);
                }
                if (findStr(sDkjl, year + "-" + yue + "-" + day)) {
                    map.put("ItemIs", "");
                } else {
                    map.put("ItemIs", "1");
                }

                map.put("ItemId", String.valueOf(i - yuechu + 1));
                mylist.add(map);
            }
        }
        myAdapter.arrlist = mylist;
        gv_daka.setAdapter(myAdapter);

    }

    public boolean findStr(String[] args, String str) {
        boolean result = false;
        //第一种：List
        result = Arrays.asList(args).contains(str);
        return result;
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;


        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            new ArrayList<HashMap<String, String>>();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arrlist.size();
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
                view = inflater.inflate(R.layout.xzrq_item, null);
            }

            TextView tv_xzrq_item = view.findViewById(R.id.tv_xzrq_item);
            tv_xzrq_item.setText(arrlist.get(position).get("ItemId"));
            if (arrlist.get(position).get("ItemId").equals("")) {
            } else {
                if (arrlist.get(position).get("ItemIs").equals("1")) {
                    tv_xzrq_item.setTextColor(tv_xzrq_item.getResources().getColor(R.color.black));
                    tv_xzrq_item.setBackgroundResource(R.color.white);
                    //  iv_daka_item.setImageResource(R.drawable.icon_zuanshi_normal_3x);
                } else {
                    String year_month = tv_xzrq_year_month.getText().toString();
                    String sStart = year_month.replace("年", "-").replace("月", "-");

                    if (Integer.valueOf(arrlist.get(position).get("ItemId")) < 10) {
                        sStart = sStart + "0" + arrlist.get(position).get("ItemId");
                    } else {
                        sStart = sStart + arrlist.get(position).get("ItemId");
                    }
                    if (sRq.get(0).equals(sStart)) {
                        tv_xzrq_item.setBackgroundResource(R.mipmap.start_time);
                        tv_xzrq_item.setTextColor(tv_xzrq_item.getResources().getColor(R.color.theme));
                    } else if (sRq.get(sRq.size() - 1).equals(sStart)) {
                        tv_xzrq_item.setBackgroundResource(R.mipmap.end_tiem);
                        tv_xzrq_item.setTextColor(tv_xzrq_item.getResources().getColor(R.color.black));
                    } else {
                        tv_xzrq_item.setBackgroundResource(R.color.black);
                        tv_xzrq_item.setTextColor(tv_xzrq_item.getResources().getColor(R.color.theme));
                    }
                }
            }
            tv_xzrq_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!arrlist.get(position).get("ItemId").equals("")) {
                        String sNnaYue = tv_xzrq_year_month.getText().toString().replace("年", "-").replace("月", "-");
                        String beginTime = "";
                        if (Integer.valueOf(arrlist.get(position).get("ItemId")) < 10) {
                            beginTime = sNnaYue + "0" + arrlist.get(position).get("ItemId");
                        } else {
                            beginTime = sNnaYue + arrlist.get(position).get("ItemId");
                        }
                       /* SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        Date sd1 = null;
                        Date sd2 = null;
                        try {
                            sd1 = df.parse(beginTime);
                            sd2 = df.parse(sStart_time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        System.out.println(sd1.before(sd2));
                        System.out.println(sd1.after(sd2));*/
                        if (sRq.size() >= 2) {
                            tv_xzrq_start_tiem.setText(subString(tv_xzrq_year_month.getText().toString(), "年", "月")
                                    + "月" + arrlist.get(position).get("ItemId") + "日");
                            sRq = new ArrayList<>();
                            sRq.add(beginTime);
                            sStart_time = beginTime;
                            sDkjl = sRq.toArray(new String[sRq.size()]);
                            tv_xzrq_end_tiem.setText("");
                        } else {
                            tv_xzrq_end_tiem.setText(subString(tv_xzrq_year_month.getText().toString(), "年", "月")
                                    + "月" + arrlist.get(position).get("ItemId") + "日");
                            List<String> days = getDays(sRq.get(0), beginTime);
                            sEnd_time = beginTime;
                            sRq = new ArrayList<>();
                            sRq = days;
                            sDkjl = sRq.toArray(new String[sRq.size()]);
                        }
                        gv_qunliao();
                    }
                }
            });

            return view;
        }
    }

    /**
     * 截取字符串str中指定字符 strStart、strEnd之间的字符串
     *
     * @return
     * @param-string
     * @param-str1
     * @param-str2
     */
    public static String subString(String str, String strStart, String strEnd) {

        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);

        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strStart + ", 无法截取目标字符串";
        }
        if (strEndIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strEnd + ", 无法截取目标字符串";
        }
        /* 开始截取 */
        String result = str.substring(strStartIndex, strEndIndex).substring(strStart.length());
        return result;
    }
}
