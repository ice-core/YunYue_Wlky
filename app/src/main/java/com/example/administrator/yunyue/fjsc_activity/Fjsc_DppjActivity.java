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
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
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

public class Fjsc_DppjActivity extends AppCompatActivity {
    private static final String TAG = Fjsc_DppjActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_fjsc_dppj_back;
    /**
     * 店铺名称
     */
    private TextView tv_fjsc_dppj_shangjia_name;
    /**
     * 店铺logo
     */
    private ImageView iv_fjsc_dppj_logo;
    /**
     * 店铺评分
     */
    private int iDianpuFen = 0;
    private GridView gv_fjsc_dppj_dianpufen;
    private MyAdapter myAdapter;
    private static final int COMPLETED = 0;
    private static final int REQUEST_TAKE_PHOTO = 0;// 拍照
    private static final int REQUEST_CROP = 1;// 裁剪
    private static final int SCAN_OPEN_PHONE = 2;// 相册
    private static final int REQUEST_PERMISSION = 100;

    private Uri imgUri; // 拍照时返回的uri
    private Uri mCutUri;// 图片裁剪时返回的uri
    private boolean hasPermission = false;
    private File imgFile;// 拍照保存的图片文件
    private String sImage = "";
    private ImageView iv_fjsc_dppj_img1, iv_fjsc_dppj_img2, iv_fjsc_dppj_img3;
    ArrayList img = new ArrayList();
    private SharedPreferences pref;
    private String sUser_id = "";
    /**
     * 订单id
     */
    public static String sOrder_id = "";
    /**
     * 商家ID
     */
    public static String sShangjia_id = "";
    /**
     * 商家logo
     */
    public static String sLogo = "";
    /**
     * 商家名称
     */
    public static String sShangjia_name;
    /**
     * 评价内容
     */
    private EditText et_fjsc_dppj_text;
    /**
     * 提交
     */
    private TextView et_fjsc_dppj_tj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_fjsc__dppj);
        pref = PreferenceManager.getDefaultSharedPreferences(Fjsc_DppjActivity.this);
        sUser_id = pref.getString("user_id", "");
        img.add("");
        img.add("");
        img.add("");
        ll_fjsc_dppj_back = findViewById(R.id.ll_fjsc_dppj_back);
        ll_fjsc_dppj_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_fjsc_dppj_shangjia_name = findViewById(R.id.tv_fjsc_dppj_shangjia_name);
        tv_fjsc_dppj_shangjia_name.setText(sShangjia_name);
        iv_fjsc_dppj_logo = findViewById(R.id.iv_fjsc_dppj_logo);
        gv_fjsc_dppj_dianpufen = findViewById(R.id.gv_fjsc_dppj_dianpufen);
        iv_fjsc_dppj_img1 = findViewById(R.id.iv_fjsc_dppj_img1);
        iv_fjsc_dppj_img2 = findViewById(R.id.iv_fjsc_dppj_img2);
        iv_fjsc_dppj_img3 = findViewById(R.id.iv_fjsc_dppj_img3);
        et_fjsc_dppj_text = findViewById(R.id.et_fjsc_dppj_text);
        et_fjsc_dppj_tj = findViewById(R.id.et_fjsc_dppj_tj);
        Glide.with(Fjsc_DppjActivity.this)
                .load( Api.sUrl+sLogo)
                .asBitmap()
                .placeholder(R.mipmap.error)
                .error(R.mipmap.error)
                .dontAnimate()
                .into(iv_fjsc_dppj_logo);
        setGridView();
        iv_fjsc_dppj_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sImage = "1";
                dialog();
            }
        });
        iv_fjsc_dppj_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sImage = "2";
                dialog();
            }
        });
        iv_fjsc_dppj_img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sImage = "3";
                dialog();
            }
        });
        et_fjsc_dppj_tj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sUrlList = "";
                for (int i = 0; i < img.size(); i++) {
                    if (i == 0) {
                        sUrlList = img.get(i).toString();
                    } else {
                        sUrlList = sUrlList + ";" + img.get(i).toString();
                    }
                }
                String sText = et_fjsc_dppj_text.getText().toString();
                if (!sText.equals("")) {
                    dialogin("");
                    sPingjiaorder(sText, sUrlList);
                } else {
                    Hint("评价内容不能为空！", HintDialog.WARM);
                }
            }
        });
    }

    /**
     * 方法名：sPingjiaorder()
     * 功  能：订单评论
     * 参  数：appId
     * order_id--订单id
     * shangjia_id--商家id
     * dianpufen--店铺分数 10 8 6
     * text--评价内容
     * url--评价图片url 1.jpg;2.jpg
     */
    private void sPingjiaorder(String text, String urllist) {
        String url = Api.sUrl + Api.sPingjiaorder;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("order_id", sOrder_id);
        params.put("shangjia_id", sShangjia_id);
        params.put("dianpufen", String.valueOf(iDianpuFen * 2));
        params.put("text", text);
        if (!urllist.equals("")) {
            params.put("url", urllist);
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

                        if (sImage.equals("1")) {
                            img.set(0, resultUrl);
                        } else if (sImage.equals("2")) {
                            img.set(1, resultUrl);
                        } else if (sImage.equals("3")) {
                            img.set(2, resultUrl);
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
                img();
            }
        }
    };

    private void img() {
        if (!img.get(0).equals("")) {
            Glide.with(Fjsc_DppjActivity.this)
                    .load( Api.sUrl+ img.get(0))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_fjsc_dppj_img1);
            iv_fjsc_dppj_img2.setImageResource(R.drawable.add_pic);
        }
        if (!img.get(1).equals("")) {
            Glide.with(Fjsc_DppjActivity.this)
                    .load( Api.sUrl+ img.get(1))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_fjsc_dppj_img2);
            iv_fjsc_dppj_img3.setImageResource(R.drawable.add_pic);
        }
        if (!img.get(2).equals("")) {
            Glide.with(Fjsc_DppjActivity.this)
                    .load( Api.sUrl+ img.get(2))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_fjsc_dppj_img3);
            // iv_fjsc_dppj_img3.setImageResource(R.drawable.add_pic);
        }

    }

    private void setGridView() {
        myAdapter = new MyAdapter(Fjsc_DppjActivity.this);
        gv_fjsc_dppj_dianpufen.setAdapter(myAdapter);
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater1;

        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater1 = LayoutInflater.from(context);

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 5;
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
                view = inflater1.inflate(R.layout.fjsc_dppj_pf_item, null);

            }
            ImageView iv_fjsc_dppj_dianpufen = view.findViewById(R.id.iv_fjsc_dppj_dianpufen);
            iv_fjsc_dppj_dianpufen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iDianpuFen = position + 1;
                    notifyDataSetChanged();
                }
            });
            if (iDianpuFen > position) {
                iv_fjsc_dppj_dianpufen.setImageResource(R.drawable.sc_dj_3x);
            } else {
                iv_fjsc_dppj_dianpufen.setImageResource(R.drawable.shouchang);
            }
            return view;
        }
    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(Fjsc_DppjActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(Fjsc_DppjActivity.this, R.style.dialog, sHint, type, true).show();
    }
}
