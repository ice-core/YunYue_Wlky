package com.example.administrator.yunyue.Im;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.erci.activity.CwhyActivity;
import com.example.administrator.yunyue.sq_activity.DakaActivity;
import com.example.administrator.yunyue.sq_activity.HyxqActivity;
import com.example.administrator.yunyue.sq_activity.QlzxActivity;
import com.example.administrator.yunyue.sq_activity.TjhyActivity;
import com.example.administrator.yunyue.sq_activity.XdpyActivity;
import com.example.administrator.yunyue.txllb.SideBar;
import com.example.administrator.yunyue.txllb.User;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class HaoyouActivity extends AppCompatActivity {
    private static final String TAG = HaoyouActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_haoyou_back;

    /**
     * 添加好友
     */
    private LinearLayout ll_haoyou_add;
    private SharedPreferences pref;
    private String sUser_id = "";
    private String sIs_vip = "";

    RequestQueue queue = null;
    /**
     * 好友列表
     */
    private GridView mgv_haoyou;

    /**
     * 新的朋友
     */
    private LinearLayout ll_haoyou_xdpy;
    /**
     * 群聊
     */
    private LinearLayout ll_haoyou_qunliao;
    public static String sId = "";

    private SideBar sideBar;
    private ArrayList<User> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_haoyou);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(HaoyouActivity.this);
        sUser_id = pref.getString("user_id", "");
        sIs_vip = pref.getString("is_vip", "");
        ll_haoyou_back = findViewById(R.id.ll_haoyou_back);
        ll_haoyou_add = findViewById(R.id.ll_haoyou_add);
        ll_haoyou_xdpy = findViewById(R.id.ll_haoyou_xdpy);
        ll_haoyou_xdpy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten = new Intent(HaoyouActivity.this, XdpyActivity.class);
                startActivity(inten);
            }
        });
        ll_haoyou_qunliao = findViewById(R.id.ll_haoyou_qunliao);

        ll_haoyou_qunliao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten = new Intent(HaoyouActivity.this, QlzxActivity.class);
                QlzxActivity.community_id = "";
                startActivity(inten);
            }
        });
        ll_haoyou_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        ll_haoyou_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(HaoyouActivity.this, TjhyActivity.class);
                    startActivity(intent);

            }
        });

        mgv_haoyou = findViewById(R.id.mgv_haoyou);
        list = new ArrayList<>();
        sideBar = (SideBar) findViewById(R.id.side_bar);
        sideBar.setOnStrSelectCallBack(new SideBar.ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                for (int i = 0; i < list.size(); i++) {
                    if (selectStr.equalsIgnoreCase(list.get(i).getFirstLetter())) {
                        mgv_haoyou.setSelection(i); // 选择到首字母出现的位置
                        return;
                    }
                }
            }
        });
        dialogin("");
        query();
    }

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initview();
    }

    private void initview() {
        query();
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

    private void query() {
        String url = Api.sUrl
                + "Community/Api/friends/appId/" + Api.sApp_Id + "/user_id/" + sUser_id;
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
                        MyAdapter myAdapter = new MyAdapter(HaoyouActivity.this);
                        list = new ArrayList<>();
                        /*  list.add(new User("亳州", "1"));*/
                        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            String resultNickname = jsonObject.getString("note");
                            String resultFriend_id = jsonObject.getString("friend_id");
                            String resultHeadimgurl = jsonObject.getString("headimgurl");
                            String resultPinyin = jsonObject.getString("pinyin");
                            list.add(new User(resultNickname, resultFriend_id, resultHeadimgurl));
                        }
                        Collections.sort(list);
                        mgv_haoyou.setAdapter(myAdapter);
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
                Hint(String.valueOf(volleyError), HintDialog.ERROR);
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(HaoyouActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(HaoyouActivity.this, R.style.dialog, sHint, type, true).show();
    }


    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        //  public ArrayList<HashMap<String, String>> mylist;
        /*    private ArrayList<User> list;*/

        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            //  mylist = new ArrayList<HashMap<String, String>>();
            /*list = new ArrayList<>();*/
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
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
                view = inflater.inflate(R.layout.haoyou_item, null);
            }
            final User user = list.get(position);
            LinearLayout ll_haoyou_item = view.findViewById(R.id.ll_haoyou_item);
            LinearLayout llCatalog = view.findViewById(R.id.llCatalog);
            TextView tvCatalog = view.findViewById(R.id.tvCatalog);
            ImageView iv_haoyou_item = view.findViewById(R.id.iv_haoyou_item);
            TextView tv_haoyou_item_name = view.findViewById(R.id.tv_haoyou_item_name);
            Glide.with(HaoyouActivity.this)
                    .load( Api.sUrl+ list.get(position).getHeadimgurl())
                    .asBitmap()
                    .dontAnimate()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .into(iv_haoyou_item);
            tv_haoyou_item_name.setText(list.get(position).getName());
            ll_haoyou_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HaoyouActivity.this, HyxqActivity.class);
                    HyxqActivity.sId = "";
                    TjhyActivity.resultId = "";
                    sId = list.get(position).getId();
                    startActivity(intent);
                    /*          RongIM.getInstance().startPrivateChat(HaoyouActivity.this, mylist.get(position).get("friend_id").toString(), mylist.get(position).get("nickname").toString());
                     */
                }
            });
            //根据position获取首字母作为目录catalog
            String catalog = list.get(position).getFirstLetter();
            //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(catalog)) {
                llCatalog.setVisibility(View.VISIBLE);
                tvCatalog.setText(user.getFirstLetter().toUpperCase());
            } else {
                llCatalog.setVisibility(View.GONE);
            }


            return view;
        }

        /**
         * 获取catalog首次出现位置
         */
        public int getPositionForSection(String catalog) {
            for (int i = 0; i < getCount(); i++) {
                String sortStr = list.get(i).getFirstLetter();
                if (catalog.equalsIgnoreCase(sortStr)) {
                    return i;
                }
            }
            return -1;
        }
    }


    public static class SortAdapter extends BaseAdapter {

        private List<User> list = null;
        private Context mContext;

        public SortAdapter(Context mContext, List<User> list) {
            this.mContext = mContext;
            this.list = list;
        }

        public int getCount() {
            return this.list.size();
        }

        public Object getItem(int position) {
            return list.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View view, ViewGroup arg2) {
            ViewHolder viewHolder;
            final User user = list.get(position);
            if (view == null) {
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(mContext).inflate(R.layout.haoyouliebiao_item, null);
                viewHolder.name = (TextView) view.findViewById(R.id.name);
                viewHolder.llCatalog = (LinearLayout) view.findViewById(R.id.llCatalog);
                viewHolder.catalog = (TextView) view.findViewById(R.id.catalog);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            //根据position获取首字母作为目录catalog
            String catalog = list.get(position).getFirstLetter();

            //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(catalog)) {
                viewHolder.llCatalog.setVisibility(View.VISIBLE);
                viewHolder.catalog.setText(user.getFirstLetter().toUpperCase());
            } else {
                viewHolder.llCatalog.setVisibility(View.GONE);
            }

            viewHolder.name.setText(this.list.get(position).getName());
            viewHolder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, list.get(position).getName(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(mContext, list.get(position).getId(), Toast.LENGTH_SHORT).show();
                }
            });

            return view;

        }

        final static class ViewHolder {
            TextView catalog;
            TextView name;
            LinearLayout llCatalog;
        }

        /**
         * 获取catalog首次出现位置
         */
        public int getPositionForSection(String catalog) {
            for (int i = 0; i < getCount(); i++) {
                String sortStr = list.get(i).getFirstLetter();
                if (catalog.equalsIgnoreCase(sortStr)) {
                    return i;
                }
            }
            return -1;
        }

    }

}
