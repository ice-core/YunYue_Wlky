package com.example.administrator.yunyue.sc_activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.bumptech.glide.Glide;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class PinglunActivity extends AppCompatActivity {
    private static final String TAG = PinglunActivity.class.getSimpleName();

    int iRank = 0;
    private Button bt_pl;
    private ImageView iv_pl_back;
    RequestQueue queue = null;
    private String sUser_id;
    private SharedPreferences pref;
    private String sId = "";
    private String sGoodId = "";
    private String sRecId = "";
    // private EditText et_pinglun_content;
    /**
     * 评论列表
     */
    private MyGridView mgv_pinglun;

    public static ArrayList<HashMap<String, String>> list;
    //调取系统摄像头的请求码
    private static final int MY_ADD_CASE_CALL_PHONE = 1;
    //打开相册的请求码
    private static final int MY_ADD_CASE_CALL_PHONE2 = 7;
    private MyAdapter myAdapter;
    private String sImage = "";
    private int item = 0;
    private static final int COMPLETED = 0;
    /**
     * 物流服务
     */
    private ImageView iv_pjfb_wl_1, iv_pjfb_wl_2, iv_pjfb_wl_3, iv_pjfb_wl_4, iv_pjfb_wl_5;
    /**
     * 服务态度
     */
    private ImageView iv_pjfb_fw_1, iv_pjfb_fw_2, iv_pjfb_fw_3, iv_pjfb_fw_4, iv_pjfb_fw_5;
    /**
     * 物流分数
     */
    int wl = 0;
    /**
     * 服务分数
     */
    int fw = 0;
    private LinearLayout ll_pl_back;

    private static final int REQUEST_TAKE_PHOTO = 0;// 拍照
    private static final int REQUEST_CROP = 1;// 裁剪
    private static final int SCAN_OPEN_PHONE = 2;// 相册
    private static final int REQUEST_PERMISSION = 100;

    private Uri imgUri; // 拍照时返回的uri
    private Uri mCutUri;// 图片裁剪时返回的uri
    private boolean hasPermission = false;
    private File imgFile;// 拍照保存的图片文件


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_pinglun);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        queue = Volley.newRequestQueue(PinglunActivity.this);
        Intent intent = getIntent();
        sId = intent.getStringExtra("id");
        iv_pl_back = (ImageView) findViewById(R.id.iv_pl_back);
        ll_pl_back = findViewById(R.id.ll_pl_back);
        ll_pl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bt_pl = (Button) findViewById(R.id.bt_pl);
        mgv_pinglun = findViewById(R.id.mgv_pinglun);
        myAdapter = new MyAdapter(PinglunActivity.this);
        setGridView();
        iv_pjfb_wl_1 = findViewById(R.id.iv_pjfb_wl_1);
        iv_pjfb_wl_2 = findViewById(R.id.iv_pjfb_wl_2);
        iv_pjfb_wl_3 = findViewById(R.id.iv_pjfb_wl_3);
        iv_pjfb_wl_4 = findViewById(R.id.iv_pjfb_wl_4);
        iv_pjfb_wl_5 = findViewById(R.id.iv_pjfb_wl_5);

        iv_pjfb_fw_1 = findViewById(R.id.iv_pjfb_fw_1);
        iv_pjfb_fw_2 = findViewById(R.id.iv_pjfb_fw_2);
        iv_pjfb_fw_3 = findViewById(R.id.iv_pjfb_fw_3);
        iv_pjfb_fw_4 = findViewById(R.id.iv_pjfb_fw_4);
        iv_pjfb_fw_5 = findViewById(R.id.iv_pjfb_fw_5);
        iv_pjfb_wl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wl = 2;
                iv_pjfb_wl_1.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_wl_2.setImageResource(R.drawable.shouchang);
                iv_pjfb_wl_3.setImageResource(R.drawable.shouchang);
                iv_pjfb_wl_4.setImageResource(R.drawable.shouchang);
                iv_pjfb_wl_5.setImageResource(R.drawable.shouchang);
            }
        });
        iv_pjfb_wl_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wl = 4;
                iv_pjfb_wl_1.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_wl_2.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_wl_3.setImageResource(R.drawable.shouchang);
                iv_pjfb_wl_4.setImageResource(R.drawable.shouchang);
                iv_pjfb_wl_5.setImageResource(R.drawable.shouchang);
            }
        });
        iv_pjfb_wl_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wl = 6;
                iv_pjfb_wl_1.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_wl_2.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_wl_3.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_wl_4.setImageResource(R.drawable.shouchang);
                iv_pjfb_wl_5.setImageResource(R.drawable.shouchang);
            }
        });
        iv_pjfb_wl_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wl = 8;
                iv_pjfb_wl_1.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_wl_2.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_wl_3.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_wl_4.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_wl_5.setImageResource(R.drawable.shouchang);
            }
        });
        iv_pjfb_wl_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wl = 10;
                iv_pjfb_wl_1.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_wl_2.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_wl_3.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_wl_4.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_wl_5.setImageResource(R.drawable.sc_dj_3x);
            }
        });


        iv_pjfb_fw_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fw = 2;
                iv_pjfb_fw_1.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_fw_2.setImageResource(R.drawable.shouchang);
                iv_pjfb_fw_3.setImageResource(R.drawable.shouchang);
                iv_pjfb_fw_4.setImageResource(R.drawable.shouchang);
                iv_pjfb_fw_5.setImageResource(R.drawable.shouchang);
            }
        });
        iv_pjfb_fw_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fw = 4;
                iv_pjfb_fw_1.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_fw_2.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_fw_3.setImageResource(R.drawable.shouchang);
                iv_pjfb_fw_4.setImageResource(R.drawable.shouchang);
                iv_pjfb_fw_5.setImageResource(R.drawable.shouchang);
            }
        });
        iv_pjfb_fw_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fw = 6;
                iv_pjfb_fw_1.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_fw_2.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_fw_3.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_fw_4.setImageResource(R.drawable.shouchang);
                iv_pjfb_fw_5.setImageResource(R.drawable.shouchang);
            }
        });
        iv_pjfb_fw_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fw = 8;
                iv_pjfb_fw_1.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_fw_2.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_fw_3.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_fw_4.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_fw_5.setImageResource(R.drawable.shouchang);
            }
        });
        iv_pjfb_fw_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fw = 10;
                iv_pjfb_fw_1.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_fw_2.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_fw_3.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_fw_4.setImageResource(R.drawable.sc_dj_3x);
                iv_pjfb_fw_5.setImageResource(R.drawable.sc_dj_3x);
            }
        });
        bt_pl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tjpl_sj();

            }
        });
    }

    /*
        String url = Api.sUrl + "Api/Order/pingjiaorder/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/order_id/" + order_id + "/shangjia_id/" + shangjia_id
                + "/dianpufen/" + fw + "/wuliufen/" + wl + "/goddfen/" + goddfen;*/
    private void tjpl(String order_id, String shangjia_id, String goddfen) {
        String url = Api.sUrl + "Api/Order/pingjiaorder";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("order_id", order_id);
        params.put("shangjia_id", shangjia_id);
        params.put("dianpufen", String.valueOf(fw));
        params.put("wuliufen", String.valueOf(wl));
        params.put("goddfen", goddfen);
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


                                Hint(resultMsg, HintDialog.SUCCESS);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 1500);
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
                    Log.e(TAG, error.getMessage(), error);
                }
            }
        });
        requestQueue.add(jsonRequest);
    }

    private void tjpl_sj() {
        String goddfen = "";
        boolean is = true;
        for (int i = 0; i < myAdapter.arrlist.size(); i++) {
            if (myAdapter.arrlist.get(i).get("goods_rank").equals("")) {
                is = false;
            } else {
                if (i == 0) {
                    goddfen = "[{\"" + myAdapter.arrlist.get(i).get("ItemGood_id") + "\":{\"0\":\"" + Integer.valueOf(myAdapter.arrlist.get(i).get("goods_rank")) * 2
                            + "\",\"1\":\""
                            + myAdapter.arrlist.get(i).get("content") + "\"";
                    if (!myAdapter.arrlist.get(i).get("Imge1").equals("")) {
                        goddfen = goddfen + ",\"2\":\"" + myAdapter.arrlist.get(i).get("Imge1");
                        if (!myAdapter.arrlist.get(i).get("Imge2").equals("")) {
                            goddfen = goddfen + ";" + myAdapter.arrlist.get(i).get("Imge2");
                        }
                        if (!myAdapter.arrlist.get(i).get("Imge3").equals("")) {
                            goddfen = goddfen + ";" + myAdapter.arrlist.get(i).get("Imge3");
                        }
                        goddfen = goddfen + "\"";
                    }
                    goddfen = goddfen + "}";
                } else {
                    goddfen = goddfen + ",\"" + myAdapter.arrlist.get(i).get("ItemGood_id") + "\":{\"0\":\"" + Integer.valueOf(myAdapter.arrlist.get(i).get("goods_rank")) * 2
                            + "\",\"1\":\""
                            + myAdapter.arrlist.get(i).get("content") + "\"";
                    if (!myAdapter.arrlist.get(i).get("Imge1").equals("")) {
                        goddfen = goddfen + ",\"2\":\"" + myAdapter.arrlist.get(i).get("Imge1");
                        if (!myAdapter.arrlist.get(i).get("Imge2").equals("")) {
                            goddfen = goddfen + ";" + myAdapter.arrlist.get(i).get("Imge2");
                        }
                        if (!myAdapter.arrlist.get(i).get("Imge3").equals("")) {
                            goddfen = goddfen + ";" + myAdapter.arrlist.get(i).get("Imge3");
                        }
                        goddfen = goddfen + "\"";
                    }
                    goddfen = goddfen + "}";
                }
            }
        }
        if (is) {
            goddfen = goddfen + "}]";
            if (fw > 0) {
                if (wl > 0) {
                    dialogin("");
                    tjpl(sId, myAdapter.arrlist.get(0).get("ItemShangjia_id"), goddfen);
                }
            }
        }
      /*  dialogin("");
        tjpl(sId, myAdapter.arrlist.get(0).get("ItemShangjia_id"), goddfen);*/
    }

    /**
     * 提交评论
     */
    private void tjpl_1(String order_id, String shangjia_id, String goddfen) {
        String url = Api.sUrl + "Api/Order/pingjiaorder/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/order_id/" + order_id + "/shangjia_id/" + shangjia_id
                + "/dianpufen/" + fw + "/wuliufen/" + wl + "/goddfen/" + goddfen;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    String resultMsg = jsonObject.getString("msg");
                    int resultCode = jsonObject.getInt("code");
                    if (resultCode > 0) {


                    } else {
                        Hint(resultMsg, HintDialog.ERROR);
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

    /**
     * 显示修改头像的对话框
     */
    public void dialog() {
        // mylist_cat
        final Dialog dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        final View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_touxiang, null);
        Button bt_tx_xc = inflate.findViewById(R.id.bt_tx_xc);
        Button bt_tx_xj = inflate.findViewById(R.id.bt_tx_xj);
        Button btn_cancel = inflate.findViewById(R.id.btn_cancel);
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.show();

        bt_tx_xc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //"点击了相册");
                //  6.0之后动态申请权限 SD卡写入权限
                checkPermissions();
                if (hasPermission) {
                    openGallery();
                }
                dialog.dismiss();
            }
        });
        bt_tx_xj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkPermissions();
                if (hasPermission) {
                    takePhoto();
                }
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SCAN_OPEN_PHONE);
    }


    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查是否有存储和拍照权限
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    ) {
                hasPermission = true;
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_PERMISSION);
            }
        }else {
            hasPermission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hasPermission = true;
            } else {
                Toast.makeText(this, "权限授予失败！", Toast.LENGTH_SHORT).show();
                hasPermission = false;
            }
        }
    }

    // 拍照
    private void takePhoto() {
        // 要保存的文件名
        String time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
        String fileName = "photo_" + time;
        // 创建一个文件夹
        String path = Environment.getExternalStorageDirectory() + "/take_photo";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        // 要保存的图片文件
        imgFile = new File(file, fileName + ".jpeg");
        // 将file转换成uri
        // 注意7.0及以上与之前获取的uri不一样了，返回的是provider路径
        imgUri = getUriForFile(this, imgFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 添加Uri读取权限
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        // 或者
//        grantUriPermission("com.rain.takephotodemo", imgUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // 添加图片保存位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    // 图片裁剪
    private void cropPhoto(Uri uri, boolean fromCapture) {
        Intent intent = new Intent("com.android.camera.action.CROP"); //打开系统自带的裁剪图片的intent
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");

        // 注意一定要添加该项权限，否则会提示无法裁剪
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        intent.putExtra("scale", true);

        // 设置裁剪区域的宽高比例
        intent.putExtra("aspectX", 0.1);
        intent.putExtra("aspectY", 0.1);

        // 设置裁剪区域的宽度和高度
        intent.putExtra("outputX", 800);
        intent.putExtra("outputY", 800);

        intent.putExtra("scale",true);
        intent.putExtra("scaleUpIfNeeded", true);

        // 取消人脸识别
        intent.putExtra("noFaceDetection", true);
        // 图片输出格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        // 若为false则表示不返回数据
        intent.putExtra("return-data", false);

        // 指定裁剪完成以后的图片所保存的位置,pic info显示有延时
        if (fromCapture) {
            // 如果是使用拍照，那么原先的uri和最终目标的uri一致,注意这里的uri必须是Uri.fromFile生成的
            mCutUri = Uri.fromFile(imgFile);
        } else { // 从相册中选择，那么裁剪的图片保存在take_photo中
            String time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
            String fileName = "photo_" + time;
            File mCutFile = new File(Environment.getExternalStorageDirectory() + "/take_photo/", fileName + ".jpeg");
            if (!mCutFile.getParentFile().exists()) {
                mCutFile.getParentFile().mkdirs();
            }
            mCutUri = Uri.fromFile(mCutFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCutUri);
        // Toast.makeText(this, "剪裁图片", Toast.LENGTH_SHORT).show();
        // 以广播方式刷新系统相册，以便能够在相册中找到刚刚所拍摄和裁剪的照片
        Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intentBc.setData(uri);
        this.sendBroadcast(intentBc);

        startActivityForResult(intent, REQUEST_CROP); //设置裁剪参数显示图片至ImageVie
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 拍照并进行裁剪
                case REQUEST_TAKE_PHOTO:
                    Log.e(TAG, "onActivityResult: imgUri:REQUEST_TAKE_PHOTO:" + imgUri.toString());
                    cropPhoto(imgUri, true);
                    break;

                // 裁剪后设置图片
                case REQUEST_CROP:
                    // iv_grzx_head_pic.setImageURI(mCutUri);
                    dialogin("");
                    upLoadImage(Api.sUrl + "Community/Api/getimg/", new File(mCutUri.getPath()));
                    Log.e(TAG, "onActivityResult: imgUri:REQUEST_CROP:" + mCutUri.toString());
                    break;
                // 打开图库获取图片并进行裁剪
                case SCAN_OPEN_PHONE:
                    Log.e(TAG, "onActivityResult: SCAN_OPEN_PHONE:" + data.getData().toString());
                    cropPhoto(data.getData(), false);
                    break;

                default:
                    break;
            }
        }
    }


    // 从file中获取uri
    // 7.0及以上使用的uri是contentProvider content://com.rain.takephotodemo.FileProvider/images/photo_20180824173621.jpg
    // 6.0使用的uri为file:///storage/emulated/0/take_photo/photo_20180824171132.jpg
    private static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), "com.example.administrator.yunyue.FileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    //上传头像
    public void upLoadImage(String upload_url, File file) {
        //创建okhClient

        OkHttpClient okHttpClient = new OkHttpClient();
        //创建MultiparBody
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //设置参数
        MediaType mediaType = MediaType.parse("image/png");

        builder.addFormDataPart("logo", file.getName(),
                RequestBody.create(mediaType, file));
        //添加其他参数
   /*     builder.addFormDataPart("user_id", sUser_id);*/
        //    sUser_id = "51a29d817fe243419b0344306b7575bc";
        builder.addFormDataPart("appId", Api.sApp_Id);
        MultipartBody body = builder.build();
        okhttp3.Request request = new okhttp3.Request.Builder().url(upload_url).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                hideDialogin();
                Log.d(TAG, "onFailure() returned: " + "shibai---" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                hideDialogin();
                try {
                    JSONObject jsonObject1 = new JSONObject(response.body().string());
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        String resultUrl = jsonObject1.getString("data");
              /*          JSONObject jsonObject = new JSONObject(resultData);
                        String resultUrl = jsonObject.getString("url");*/

                        for (int i = 0; i < myAdapter.arrlist.size(); i++) {
                            if (i == item) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("ItemOriginal_Img", myAdapter.arrlist.get(i).get("ItemOriginal_Img"));
                                map.put("ItemShangjia_id", myAdapter.arrlist.get(i).get("ItemShangjia_id"));
                                map.put("ItemGood_id", myAdapter.arrlist.get(i).get("ItemGood_id"));
                                if (sImage.equals("1")) {
                                    map.put("Imge1", resultUrl);
                                    map.put("Imge2", myAdapter.arrlist.get(i).get("Imge2"));
                                    map.put("Imge3", myAdapter.arrlist.get(i).get("Imge3"));
                                } else if (sImage.equals("2")) {
                                    map.put("Imge1", myAdapter.arrlist.get(i).get("Imge1"));
                                    map.put("Imge2", resultUrl);
                                    map.put("Imge3", myAdapter.arrlist.get(i).get("Imge3"));
                                } else if (sImage.equals("3")) {
                                    map.put("Imge1", myAdapter.arrlist.get(i).get("Imge1"));
                                    map.put("Imge2", myAdapter.arrlist.get(i).get("Imge2"));
                                    map.put("Imge3", resultUrl);
                                }
                                map.put("goods_rank", myAdapter.arrlist.get(i).get("goods_rank"));
                                map.put("content", myAdapter.arrlist.get(i).get("content"));
                                map.put("is_anonymous", myAdapter.arrlist.get(i).get("is_anonymous"));
                                myAdapter.arrlist.set(i, map);
                            }
                        }
                        Message message = new Message();
                        message.what = COMPLETED;
                        handler.sendMessage(message);
                        Looper.prepare();

                        // Hint(resultMsg, HintDialog.SUCCESS);
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Hint(resultMsg, HintDialog.ERROR);
                        Looper.loop();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onResponse() returned: " + response.body().string());
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED) {
                // imageView.setImageResource(R.drawable.finger); //UI更改操作
                myAdapter.notifyDataSetChanged();
            }
        }
    };

    /**
     * 设置GirdView参数，绑定数据
     */
    private void setGridView() {

        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < list.size(); i++) {
            // if (list.get(i).get("ItemOrder_Id").equals(sId)) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemOriginal_Img", list.get(i).get("ItemGood_pic"));
            map.put("ItemShangjia_id", list.get(i).get("ItemShangjia_id"));
            map.put("ItemGood_id", list.get(i).get("ItemGood_id"));
            map.put("Imge1", "");
            map.put("Imge2", "");
            map.put("Imge3", "");
            map.put("goods_rank", "");
            map.put("content", "");
            map.put("is_anonymous", "0");
            mylist.add(map);
            //   }
        }
        myAdapter.arrlist = mylist;
        mgv_pinglun.setAdapter(myAdapter);

    }

    /**
     * 评价修改
     */

    private void pj(int i, String goods_rank, String content, String is_anonymous) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("ItemOriginal_Img", myAdapter.arrlist.get(i).get("ItemOriginal_Img"));
        map.put("ItemShangjia_id", myAdapter.arrlist.get(i).get("ItemShangjia_id"));
        map.put("ItemGood_id", myAdapter.arrlist.get(i).get("ItemGood_id"));
        // map.put("Imge1", myAdapter.arrlist.get(i).get("Imge1"));
        map.put("Imge1", myAdapter.arrlist.get(i).get("Imge1"));
        map.put("Imge2", myAdapter.arrlist.get(i).get("Imge2"));
        map.put("Imge3", myAdapter.arrlist.get(i).get("Imge3"));
        if (goods_rank.equals("")) {
            map.put("goods_rank", myAdapter.arrlist.get(i).get("goods_rank"));
        } else {
            map.put("goods_rank", goods_rank);
        }
        if (content.equals("")) {
            map.put("content", myAdapter.arrlist.get(i).get("content"));
        } else {
            map.put("content", content);
        }
        if (is_anonymous.equals("")) {
            map.put("is_anonymous", myAdapter.arrlist.get(i).get("is_anonymous"));
        } else {
            map.put("is_anonymous", is_anonymous);
        }
        myAdapter.arrlist.set(i, map);
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        ArrayList<HashMap<String, String>> arrlist;

        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arrlist = new ArrayList<HashMap<String, String>>();
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
                view = inflater.inflate(R.layout.pingjia_item, null);
            }
            ImageView iv_pingjia = view.findViewById(R.id.iv_pingjia);
            Glide.with(PinglunActivity.this)
                    .load( Api.sUrl+ arrlist.get(position).get("ItemOriginal_Img"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_pingjia);
            ImageView iv_pingjia_img = view.findViewById(R.id.iv_pingjia_img);
            ImageView iv_pingjia_img1 = view.findViewById(R.id.iv_pingjia_img1);
            ImageView iv_pingjia_img2 = view.findViewById(R.id.iv_pingjia_img2);
            final ImageView iv_pj = view.findViewById(R.id.iv_pj);
            final ImageView iv_pj1 = view.findViewById(R.id.iv_pj1);
            final ImageView iv_pj2 = view.findViewById(R.id.iv_pj2);
            final ImageView iv_pj3 = view.findViewById(R.id.iv_pj3);
            final ImageView iv_pj4 = view.findViewById(R.id.iv_pj4);
            EditText et_pingjia_content = view.findViewById(R.id.et_pingjia_content);
            final CheckBox cb_pingjia_is_anonymous = view.findViewById(R.id.cb_pingjia_is_anonymous);

            iv_pj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    pj(position, "1", "", "");
                    iv_pj.setImageResource(R.drawable.sc_dj_3x);
                    iv_pj1.setImageResource(R.drawable.shouchang);
                    iv_pj2.setImageResource(R.drawable.shouchang);
                    iv_pj3.setImageResource(R.drawable.shouchang);
                    iv_pj4.setImageResource(R.drawable.shouchang);
                }
            });
            iv_pj1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pj(position, "2", "", "");
                    iv_pj.setImageResource(R.drawable.sc_dj_3x);
                    iv_pj1.setImageResource(R.drawable.sc_dj_3x);
                    iv_pj2.setImageResource(R.drawable.shouchang);
                    iv_pj3.setImageResource(R.drawable.shouchang);
                    iv_pj4.setImageResource(R.drawable.shouchang);
                }
            });
            iv_pj2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pj(position, "3", "", "");
                    iv_pj.setImageResource(R.drawable.sc_dj_3x);
                    iv_pj1.setImageResource(R.drawable.sc_dj_3x);
                    iv_pj2.setImageResource(R.drawable.sc_dj_3x);
                    iv_pj3.setImageResource(R.drawable.shouchang);
                    iv_pj4.setImageResource(R.drawable.shouchang);
                }
            });
            iv_pj3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pj(position, "4", "", "");
                    iv_pj.setImageResource(R.drawable.sc_dj_3x);
                    iv_pj1.setImageResource(R.drawable.sc_dj_3x);
                    iv_pj2.setImageResource(R.drawable.sc_dj_3x);
                    iv_pj3.setImageResource(R.drawable.sc_dj_3x);
                    iv_pj4.setImageResource(R.drawable.shouchang);
                }
            });
            iv_pj4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pj(position, "5", "", "");
                    iv_pj.setImageResource(R.drawable.sc_dj_3x);
                    iv_pj1.setImageResource(R.drawable.sc_dj_3x);
                    iv_pj2.setImageResource(R.drawable.sc_dj_3x);
                    iv_pj3.setImageResource(R.drawable.sc_dj_3x);
                    iv_pj4.setImageResource(R.drawable.sc_dj_3x);
                }
            });

            et_pingjia_content.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String sContent = charSequence.toString();
                    pj(position, "", sContent, "");
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            cb_pingjia_is_anonymous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cb_pingjia_is_anonymous.isChecked()) {
                        pj(position, "", "", "1");
                    } else {
                        pj(position, "", "", "0");
                    }
                }
            });
            iv_pingjia_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sImage = "1";
                    item = position;
                    dialog();
                }
            });
            iv_pingjia_img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sImage = "2";
                    item = position;
                    dialog();
                }
            });
            iv_pingjia_img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sImage = "3";
                    item = position;
                    dialog();
                }
            });

            if (arrlist.get(position).get("Imge1").equals("")) {

            } else {
                Glide.with(PinglunActivity.this)
                        .load( Api.sUrl+ arrlist.get(position).get("Imge1"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_pingjia_img);
                iv_pingjia_img1.setImageResource(R.drawable.add_pic);
            }

            if (arrlist.get(position).get("Imge2").equals("")) {

            } else {
                Glide.with(PinglunActivity.this)
                        .load( Api.sUrl+ arrlist.get(position).get("Imge2"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_pingjia_img1);
                iv_pingjia_img2.setImageResource(R.drawable.add_pic);
            }

            if (arrlist.get(position).get("Imge3").equals("")) {

            } else {
                Glide.with(PinglunActivity.this)
                        .load( Api.sUrl+ arrlist.get(position).get("Imge3"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_pingjia_img2);
            }
            return view;
        }
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

    private void Save(String order_id, String content, String goods_rank) {
        String url = Api.sUrl + "Order/commentAdd/user_id/" + sUser_id +
                "/order_id/" + order_id + "/rec_id/" + sRecId + "/content/" + content + "/goods_id/"
                + sGoodId + "/goods_rank/" + goods_rank;
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
        loadingDialog = new LoadingDialog(PinglunActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(PinglunActivity.this, R.style.dialog, sHint, type, true).show();
    }

}
