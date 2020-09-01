package com.example.administrator.yunyue.sc_activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.example.administrator.yunyue.MainActivity_Wlky;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.activity.ZhmmActivity;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.dialog.PromptDialog;
import com.example.administrator.yunyue.sc.CircleImageView;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class ZhszActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;


    private static final String TAG = ZhszActivity.class.getSimpleName();
    RequestQueue queue = null;
    private String sUser_id, sMobile;
    private TextView tv_grzx_nickname;
    private TextView tv_grzx_sex;

    private TextView tv_grzx_mobile;
    private CircleImageView iv_grzx_head_pic;
    private LinearLayout ll_grzx_nick;
    private LinearLayout ll_grzx_sex;
    private LinearLayout ll_grzx_mobile;
    private Button bt_lg_sl;
    private Button bt_lg_zc;
    private Button cancel;
    private Dialog dialog;
    private View inflate;
    private static final int TAKE_PHOTO_REQUEST_TWO = 444;
    private static final int TAKE_PHOTO_REQUEST_ONE = 333;
    private static final int TAKE_PHOTO_REQUEST_THREE = 555;
    private Uri imageUri;
    private String onclick;
    private LinearLayout ll_grxx_tuxiang;
    public static final int CHOOSE_PHOTO = 2;
    String imagePath;
    private LinearLayout ll_zhsz_back;
    private LinearLayout ll_grxx_xgmm;
    private TextView tv_grzx_userModify;
    private Button bt_zhsz_tuichu;


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
    /**
     * 我的收货地址
     */
    private LinearLayout ll_grxx_wdshdz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_zhsz);
        queue = Volley.newRequestQueue(ZhszActivity.this);
        bt_zhsz_tuichu = (Button) findViewById(R.id.bt_zhsz_tuichu);
        tv_grzx_nickname = (TextView) findViewById(R.id.tv_grzx_nickname);
        tv_grzx_sex = (TextView) findViewById(R.id.tv_grzx_sex);
        tv_grzx_mobile = (TextView) findViewById(R.id.tv_grzx_mobile);
        iv_grzx_head_pic = (CircleImageView) findViewById(R.id.iv_grzx_head_pic);
        ll_grzx_nick = (LinearLayout) findViewById(R.id.ll_grzx_nick);
        ll_grzx_sex = (LinearLayout) findViewById(R.id.ll_grzx_sex);
        ll_grzx_mobile = (LinearLayout) findViewById(R.id.ll_grzx_mobile);
        ll_grxx_tuxiang = (LinearLayout) findViewById(R.id.ll_grxx_tuxiang);
        ll_zhsz_back = findViewById(R.id.ll_zhsz_back);
        ll_grxx_xgmm = (LinearLayout) findViewById(R.id.ll_grxx_xgmm);
        tv_grzx_userModify = (TextView) findViewById(R.id.tv_grzx_userModify);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        sMobile = pref.getString("mobile", "");
        editor = pref.edit();
        bt_zhsz_tuichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        ll_grzx_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ZhszActivity.this, XgncActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ll_grzx_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick = "ll_grzx_sex";
                show();
            }
        });
        ll_grzx_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ZhszActivity.this, GgsjActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ll_grxx_tuxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
         /*       if (ContextCompat.checkSelfPermission(ZhszActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ZhszActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }*/
                dialog();
            }
        });
        ll_grxx_xgmm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent intent = new Intent(ZhszActivity.this, WjmmActivity.class);
                startActivity(intent);*/
                Intent intent = new Intent(ZhszActivity.this, ZhmmActivity.class);
                startActivity(intent);
                //   finish();
            }
        });

        ll_zhsz_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        ll_grxx_wdshdz = findViewById(R.id.ll_grxx_wdshdz);
        ll_grxx_wdshdz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ZhszActivity.this, ShdzActivity.class);
                startActivity(intent);
            }
        });
        dialogin("");
        huoqu(sUser_id);
    }


    /**
     * 显示修改头像的对话框
     */
    public void dialog() {
        // mylist_cat
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
        } else {
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

        intent.putExtra("scale", true);
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
                    iv_grzx_head_pic.setImageURI(mCutUri);
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
                touxiang(resultUrl);
            }
        }
    };


    /**
     * 获取压缩图片的options
     *
     * @return
     */
    public static BitmapFactory.Options getOptions(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 4;      //此项参数可以根据需求进行计算
        options.inJustDecodeBounds = false;
        return options;
    }

    /**
     *  * 方法名：showDialog()
     *  * 功    能：退出消息确认
     *  * 参    数：无
     *  * 返回值：无
     */
    protected void showDialog() {
        PromptDialog pd = new PromptDialog(ZhszActivity.this, R.style.dialog, "确定要退出吗?", new PromptDialog.OnClickListener() {
            @Override
            public void onCancelClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    dialog.dismiss();
                    //dialog.dismiss();
                }
            }

            @Override
            public void onConfimClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    editor.remove("password");
                    editor.apply();
                    dialog.dismiss();
                    Intent intent = new Intent(ZhszActivity.this, com.example.administrator.yunyue.activity.LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }
            }
        });
        pd.setPositiveName("是");
        pd.setNegativeName("否");
        pd.show();

    }

    private void huoqu(String id) {
        String url = Api.sUrl + "Api/Login/udetails/appId/" + Api.sApp_Id + "/user_id/" + id + "/mobile/" + sMobile;
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
                        JSONObject jsonObjectdate = jsonObject1.getJSONObject("data");
                        String id = jsonObjectdate.getString("id");
                        String nickname = jsonObjectdate.getString("nickname");
                        String mobile = jsonObjectdate.getString("mobile");
                        String headimgurl = jsonObjectdate.getString("headimgurl");
                        String sex = jsonObjectdate.getString("sex");
                        String token = jsonObjectdate.getString("token");
                        //     String email = jsonObjectdate.getString("email");
                        //  String signature = jsonObjectdate.getString("signature");
                        tv_grzx_sex.setText(sex);
                        tv_grzx_mobile.setText(mobile);
                        tv_grzx_nickname.setText(nickname);
                        Glide.with(ZhszActivity.this)
                                .load( Api.sUrl+headimgurl)
                                .asBitmap()
                                /*       .placeholder(R.mipmap.error)
                                       .error(R.mipmap.error)*/
                                .dontAnimate()
                                .into(iv_grzx_head_pic);

                        editor = pref.edit();
                        editor.putString("head_pic", headimgurl);
                        editor.putString("nickname", nickname);
                        editor.putString("mobile", mobile);
                        editor.putString("sex", mobile);

                        editor.apply();
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


    public void show() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(this).inflate(R.layout.dialog_login, null);
        bt_lg_sl = (Button) inflate.findViewById(R.id.bt_lg_sl);
        bt_lg_zc = (Button) inflate.findViewById(R.id.bt_lg_zc);
        cancel = (Button) inflate.findViewById(R.id.btn_cancel);
        bt_lg_sl.setOnClickListener(this);
        bt_lg_zc.setOnClickListener(this);
        cancel.setOnClickListener(this);
        if (onclick.equals("ll_grzx_sex")) {
            bt_lg_sl.setText("男");
            bt_lg_zc.setText("女");
        } else if (onclick.equals("iv_grzx_head_pic")) {
            bt_lg_sl.setText("拍照");
            bt_lg_zc.setText("相册");
        }
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_lg_sl:
                if (onclick.equals("ll_grzx_sex")) {
                    tv_grzx_sex.setText("男");
                    dialogin("");
                    Save("1");
                } else if (onclick.equals("iv_grzx_head_pic")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, TAKE_PHOTO_REQUEST_TWO);
                }
                dialog.dismiss();
                break;
            case R.id.bt_lg_zc:
                if (onclick.equals("ll_grzx_sex")) {
                    tv_grzx_sex.setText("女");
                    dialogin("");
                    Save("2");
                } else if (onclick.equals("iv_grzx_head_pic")) {
                    // pickImageFromAlbum();
                }
                dialog.dismiss();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
        }
    }

    private void Save(String sex) {
        String url = Api.sUrl + "Api/Login/resex/appId/" + Api.sApp_Id
                + "/mobile/" + sMobile + "/sex/" + sex;
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
                        dialogin("");
                        huoqu(sUser_id);

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
     * 方法名：sReheard()
     * 功  能：修改头像
     * 参  数：
     * appId
     * headimgurl--头像地址
     */
    private void touxiang(String headimgurl) {
        String url = Api.sUrl + Api.sReheard + "/appId/" + Api.sApp_Id
                + "/mobile/" + sMobile + "/?headimgurl=" + headimgurl;
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
                        dialogin("");
                        huoqu(sUser_id);

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
        Intent intent = new Intent(this, MainActivity_Wlky.class);
        intent.putExtra("ID", "5");
        startActivity(intent);
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
        loadingDialog = new LoadingDialog(ZhszActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(ZhszActivity.this, R.style.dialog, sHint, type, true).show();
    }

}
