package com.example.administrator.yunyue.fjsc_activity;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
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

import org.json.JSONArray;
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

public class Fjsc_SqtkActivity extends AppCompatActivity {
    private static final String TAG = Fjsc_SqtkActivity.class.getSimpleName();
    RequestQueue queue = null;
    private String sUser_id;
    private SharedPreferences pref;
    private String sId;

    private MyGridView gv_qgsc_sqtk_sp;
    private Dialog dialog;
    private View inflate;
    private LinearLayout ll_qgsc_sqtk_tkyy;
    private GridView gv_tkyy;
    private TextView tv_qgsc_sqtk_tkyy;
    private String sTkyy = "";
    private ImageView iv_shouhou_back;

    private String sImagePath1 = "";
    private String sImagePath2 = "";
    private String sImagePath3 = "";
    private ImageView iv_qgsc_sqtk_image;
    private TextView tv_qgsc_sqtk_tj;
    private EditText et_qgsc_sqtk_ms;
    private ImageView iv_qgsc_sqtk_image1;
    private ImageView iv_qgsc_sqtk_image2;
    private ImageView iv_qgsc_sqtk_image_delete, iv_qgsc_sqtk_image1_delete, iv_qgsc_sqtk_image2_delete;
    private String sState = "";
    private static final int REQUEST_TAKE_PHOTO = 0;// 拍照
    private static final int REQUEST_CROP = 1;// 裁剪
    private static final int SCAN_OPEN_PHONE = 2;// 相册
    private static final int REQUEST_PERMISSION = 100;

    private Uri imgUri; // 拍照时返回的uri
    private Uri mCutUri;// 图片裁剪时返回的uri
    private boolean hasPermission = false;
    private File imgFile;// 拍照保存的图片文件
    private static final int COMPLETED = 0;
    private String resultUrl = "";
    private String sShangjia_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_sqtk);
        queue = Volley.newRequestQueue(Fjsc_SqtkActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        Intent intent = getIntent();

        sId = intent.getStringExtra("id");

        sState = intent.getStringExtra("state");
        gv_qgsc_sqtk_sp = findViewById(R.id.gv_qgsc_sqtk_sp);
        ll_qgsc_sqtk_tkyy = findViewById(R.id.ll_qgsc_sqtk_tkyy);
        tv_qgsc_sqtk_tkyy = findViewById(R.id.tv_qgsc_sqtk_tkyy);
        iv_shouhou_back = findViewById(R.id.iv_shouhou_back);
        tv_qgsc_sqtk_tj = findViewById(R.id.tv_qgsc_sqtk_tj);
        et_qgsc_sqtk_ms = findViewById(R.id.et_qgsc_sqtk_ms);
        iv_qgsc_sqtk_image = findViewById(R.id.iv_qgsc_sqtk_image);
        iv_qgsc_sqtk_image1 = findViewById(R.id.iv_qgsc_sqtk_image1);
        iv_qgsc_sqtk_image2 = findViewById(R.id.iv_qgsc_sqtk_image2);
        iv_qgsc_sqtk_image_delete = findViewById(R.id.iv_qgsc_sqtk_image_delete);
        iv_qgsc_sqtk_image1_delete = findViewById(R.id.iv_qgsc_sqtk_image1_delete);
        iv_qgsc_sqtk_image2_delete = findViewById(R.id.iv_qgsc_sqtk_image2_delete);
        ll_qgsc_sqtk_tkyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });
        iv_shouhou_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        iv_qgsc_sqtk_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog();
            }
        });
        iv_qgsc_sqtk_image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sImagePath3 = "";
                iv_qgsc_sqtk_image_delete.setVisibility(View.GONE);
                iv_qgsc_sqtk_image.setImageResource(R.drawable.icon_xiangji_shangchuan_3x);
            }
        });
        iv_qgsc_sqtk_image1_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sImagePath1 = "";
                iv_qgsc_sqtk_image1.setVisibility(View.GONE);
                iv_qgsc_sqtk_image1_delete.setVisibility(View.GONE);
            }
        });
        iv_qgsc_sqtk_image2_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sImagePath2 = "";
                iv_qgsc_sqtk_image2.setVisibility(View.GONE);
                iv_qgsc_sqtk_image2_delete.setVisibility(View.GONE);
            }
        });
        dialogin("");
        sOrderservice(sId);
        initialize();
        tv_qgsc_sqtk_tj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sTulist = "";
                if (!sImagePath1.equals("")) {
                    sTulist = sImagePath1;
                }
                if (!sImagePath2.equals("")) {
                    if (sTulist.equals("")) {
                        sTulist = sImagePath2;
                    } else {
                        sTulist = sTulist + ";" + sImagePath2;
                    }
                }
                if (!sImagePath3.equals("")) {
                    if (sTulist.equals("")) {
                        sTulist = sImagePath3;
                    } else {
                        sTulist = sTulist + ";" + sImagePath3;
                    }
                }
                hideDialogin();
                dialogin("");
                sOrderaftersale(et_qgsc_sqtk_ms.getText().toString(), sTulist);
            }
        });
    }

    /**
     * 方法名：sOrderaftersale()
     * 功  能：申请售后提交接口
     * 参  数：appId
     * order_id--订单id
     * user_id--用户id
     * type--售后类型 1仅退款 2退货退款
     * cause_id--申请原因id
     * shangjia_id--商家id
     * good_info--问题描述
     * good_img--图片URL img;img
     */
    private void sOrderaftersale(String sYY, String sTu) {
        String url = Api.sUrl + Api.sOrderaftersale;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("order_id", sId);
        params.put("user_id", sUser_id);
        params.put("type", "1");
        params.put("cause_id", sTkyy);
        params.put("shangjia_id", sShangjia_id);
        if (!sYY.equals("")) {
            params.put("good_info", sYY);
        }
        if (!sTu.equals("")) {
            params.put("good_img", sTu);
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
                error(error);
            }
        });
        requestQueue.add(jsonRequest);
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
     * 显示修改头像的对话框
     */
    public void dialog() {
        checkPermissions();
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
    @SuppressLint("WrongConstant")
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
                    //  iv_grzx_head_pic.setImageURI(mCutUri);
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
                        resultUrl = jsonObject1.getString("data");
              /*          JSONObject jsonObject = new JSONObject(resultData);
                        String resultUrl = jsonObject.getString("url");*/


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
                diaplayImage(resultUrl);
            }
        }
    };


    private void diaplayImage(String imagePath) {
        if (imagePath != null) {
            // Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            if (sImagePath1.equals("")) {
                sImagePath1 = imagePath;
                Glide.with(Fjsc_SqtkActivity.this)
                        .load( Api.sUrl+ imagePath)
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_qgsc_sqtk_image1);
                //   iv_qgsc_sqtk_image1.setImageBitmap(bitmap);
                iv_qgsc_sqtk_image1.setVisibility(View.VISIBLE);
                iv_qgsc_sqtk_image1_delete.setVisibility(View.VISIBLE);
            } else if (sImagePath2.equals("")) {
                sImagePath2 = imagePath;
                Glide.with(Fjsc_SqtkActivity.this)
                        .load( Api.sUrl+ imagePath)
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_qgsc_sqtk_image2);
                iv_qgsc_sqtk_image2.setVisibility(View.VISIBLE);
                iv_qgsc_sqtk_image2_delete.setVisibility(View.VISIBLE);
            } else if (sImagePath3.equals("")) {
                sImagePath3 = imagePath;
                Glide.with(Fjsc_SqtkActivity.this)
                        .load( Api.sUrl+imagePath)
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_qgsc_sqtk_image);
                iv_qgsc_sqtk_image.setVisibility(View.VISIBLE);
                iv_qgsc_sqtk_image_delete.setVisibility(View.VISIBLE);
            }

        } else {
            //  Toast.makeText(this, "failed to get iamge", Toast.LENGTH_SHORT).show();
        }
    }

    public void show() {
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

    private void initialize() {
        dialog = new Dialog(Fjsc_SqtkActivity.this, R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(Fjsc_SqtkActivity.this).inflate(R.layout.tkyy_dialog, null);
        TextView tv_tkyy_gb = (TextView) inflate.findViewById(R.id.tv_tkyy_gb);
        gv_tkyy = inflate.findViewById(R.id.gv_tkyy);
        tv_tkyy_gb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    /**
     * 方法名：sOrderservice()
     * 功  能：售后服务页面接口
     * 参  数：appId
     * order_id--订单id
     * user_id--用户id
     * state--订单状态
     */
    private void sOrderservice(String sId) {
        String url = Api.sUrl + Api.sOrderservice;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("order_id", sId);
        params.put("state", sState);
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
                                JSONObject jsonObjectdate = jsonObject1.getJSONObject("data");
                                sShangjia_id = jsonObjectdate.getString("shangjia_id");
                                JSONArray resultJsonArraylist = jsonObjectdate.getJSONArray("list");
                                MyAdapter myAdapter = new MyAdapter(Fjsc_SqtkActivity.this);
                                ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < resultJsonArraylist.length(); i++) {
                                    JSONObject jsonObjectResultlist = resultJsonArraylist.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    //商品id
                                    map.put("ItemGoods_id", jsonObjectResultlist.getString("goods_id"));
                                    //商品图片
                                    map.put("ItemGood_pic", jsonObjectResultlist.getString("good_pic"));
                                    //商品名称
                                    map.put("ItemGoods_Name", jsonObjectResultlist.getString("goods_name"));
                                    //商品规格
                                    map.put("ItemSpec_Sey_Name", jsonObjectResultlist.getString("spec_key_name"));
                                    //商品金额
                                    map.put("ItemGoods_Price", jsonObjectResultlist.getString("goods_price"));
                                    //商品数量
                                    map.put("ItemGoods_Num", jsonObjectResultlist.getString("goods_num"));

                                    mylist.add(map);
                                }
                                myAdapter.arrlist = mylist;
                                gv_qgsc_sqtk_sp.setAdapter(myAdapter);

                                JSONArray resultJsonArraytype = jsonObject1.getJSONArray("type");
                                MyAdapterTkyy myAdapterTkyy = new MyAdapterTkyy(Fjsc_SqtkActivity.this);
                                ArrayList<HashMap<String, String>> mylist_type = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < resultJsonArraytype.length(); i++) {
                                    JSONObject jsonObject = resultJsonArraytype.getJSONObject(i);
                                    if (i == 0) {
                                        sTkyy = jsonObject.getString("id");
                                        tv_qgsc_sqtk_tkyy.setText(jsonObject.getString("cause"));
                                    }
                                    String resultId = jsonObject.getString("id");
                                    String resultName = jsonObject.getString("cause");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("ItemId", resultId);
                                    map.put("ItemName", resultName);
                                    myAdapterTkyy.arr_Cb.add("0");
                                    mylist_type.add(map);
                                }
                                myAdapterTkyy.arrlist = mylist_type;
                                gv_tkyy.setAdapter(myAdapterTkyy);

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
                error(error);
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
        loadingDialog = new LoadingDialog(Fjsc_SqtkActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(Fjsc_SqtkActivity.this, R.style.dialog, sHint, type, true).show();
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
                view = inflater.inflate(R.layout.sqtk_item, null);
            }

            ImageView iv_ckdd_image = view.findViewById(R.id.iv_ckdd_image);
            TextView tv_ckdd_goods_name = view.findViewById(R.id.tv_ckdd_goods_name);
            TextView tv_ckdd_order_amount = view.findViewById(R.id.tv_ckdd_order_amount);
            TextView tv_ckdd_goods_num = view.findViewById(R.id.tv_ckdd_goods_num);
            Glide.with(Fjsc_SqtkActivity.this)
                    .load( Api.sUrl+ arrlist.get(position).get("ItemGood_pic"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_ckdd_image);
            tv_ckdd_goods_name.setText(arrlist.get(position).get("ItemGoods_Name"));
            tv_ckdd_order_amount.setText("￥" + arrlist.get(position).get("ItemGoods_Price"));
            tv_ckdd_goods_num.setText(arrlist.get(position).get("ItemGoods_Num"));


            return view;
        }
    }

    private class MyAdapterTkyy extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;
        public ArrayList<String> arr_Cb;

        public MyAdapterTkyy(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arrlist = new ArrayList<HashMap<String, String>>();
            arr_Cb = new ArrayList<String>();

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
                view = inflater.inflate(R.layout.tkyy_dialog_item, null);
            }
            final TextView tv_tkyy_dialog_item_name = view.findViewById(R.id.tv_tkyy_dialog_item_name);
            final CheckBox cb_tkyy_dialog_item = view.findViewById(R.id.cb_tkyy_dialog_item);

            tv_tkyy_dialog_item_name.setText(arrlist.get(position).get("ItemName"));
            if (arr_Cb.get(position).equals("0")) {
                cb_tkyy_dialog_item.setChecked(false);
            } else {
                cb_tkyy_dialog_item.setChecked(true);
            }
            cb_tkyy_dialog_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cb_tkyy_dialog_item.isChecked()) {
                        tv_qgsc_sqtk_tkyy.setText(tv_tkyy_dialog_item_name.getText().toString());
                        sTkyy = arrlist.get(position).get("ItemId");
                        for (int i = 0; i < arr_Cb.size(); i++) {
                            if (i == position) {
                                arr_Cb.set(i, "1");
                            } else {
                                arr_Cb.set(i, "0");
                            }
                        }
                    } else {
                        sTkyy = "";
                        tv_qgsc_sqtk_tkyy.setText("");
                        for (int i = 0; i < arr_Cb.size(); i++) {
                            arr_Cb.set(i, "0");
                        }
                    }
                    notifyDataSetChanged();
                }
            });

            return view;
        }
    }
}
