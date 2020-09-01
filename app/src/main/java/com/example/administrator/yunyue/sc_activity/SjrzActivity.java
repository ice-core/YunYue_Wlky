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
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.example.administrator.yunyue.sc.Validator;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class SjrzActivity extends AppCompatActivity {
    private static final String TAG = SjrzActivity.class.getSimpleName();
    private LinearLayout ll_sjrz_back;
    private EditText et_sjrz_address;
    private TextView tv_sjrz_detailed;
    private TextView tv_sjrz_number;
    private SharedPreferences pref;
    private String sUser_id;
    private EditText et_sjrz_name;
    private EditText et_sjrz_phone;
    private EditText et_sjrz_storename;
    private EditText et_sjrz_beizhu;
    private Spinner sp_sjrz_category;
    private Button bt_sjrz_tj;
    private ImageView iv_sjrz_image;
    public static final int CHOOSE_PHOTO = 2;
    //  String imagePath = "";
    public static String sLatitude = "", sLongitude = "";
    //   private String tjid = "", xq = "", qu = "", sName = "", sPhone = "", sStorename = "", sImage = "", sType = "", sBeizhu = "";
    private String sCategory = "", sType = "";
    RequestQueue queue = null;
    private List<String> list_category;
    private List<String> list_category_id;
    private ArrayAdapter<String> arr_adapter;
    private Spinner sp_sjrz_type;
    Validator validator = new Validator();
    private ImageView iv_sjrz_back;
    private EditText et_sjrz_id;
    private CheckBox cb_sjrz;
    private ImageView iv_sjrz_image_logo;


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
     * 0--营业执照
     * 1--店铺头像
     */
    private int iImge_type = 0;
    private String sYyzz = "";
    private String sDptx = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_sjrz);

        queue = Volley.newRequestQueue(SjrzActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(SjrzActivity.this);
        sUser_id = pref.getString("user_id", "");
        if (sType == null) {
            sType = "0";
        }
        cb_sjrz = (CheckBox) findViewById(R.id.cb_sjrz);
        et_sjrz_id = (EditText) findViewById(R.id.et_sjrz_id);
        sp_sjrz_category = (Spinner) findViewById(R.id.sp_sjrz_category);
        sp_sjrz_type = (Spinner) findViewById(R.id.sp_sjrz_type);
        iv_sjrz_image = (ImageView) findViewById(R.id.iv_sjrz_image);
        bt_sjrz_tj = (Button) findViewById(R.id.bt_sjrz_tj);
        et_sjrz_name = (EditText) findViewById(R.id.et_sjrz_name);
        et_sjrz_phone = (EditText) findViewById(R.id.et_sjrz_phone);
        et_sjrz_beizhu = (EditText) findViewById(R.id.et_sjrz_beizhu);
        et_sjrz_storename = (EditText) findViewById(R.id.et_sjrz_storename);
        tv_sjrz_detailed = (TextView) findViewById(R.id.tv_sjrz_detailed);
        et_sjrz_address = (EditText) findViewById(R.id.et_sjrz_address);
        tv_sjrz_number = (TextView) findViewById(R.id.tv_sjrz_number);
        ll_sjrz_back = (LinearLayout) findViewById(R.id.ll_sjrz_back);
        iv_sjrz_back = (ImageView) findViewById(R.id.iv_sjrz_back);
        iv_sjrz_image_logo = findViewById(R.id.iv_sjrz_image_logo);

        iv_sjrz_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        et_sjrz_phone.setInputType(InputType.TYPE_CLASS_NUMBER);
        ll_sjrz_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        bt_sjrz_tj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validator.isMobile(et_sjrz_phone.getText().toString())) {
                    if (et_sjrz_name.getText().toString().equals("")) {
                        Hint("请输入联系人！", HintDialog.WARM);
                    } else if (et_sjrz_storename.getText().toString().equals("")) {
                        Hint("请输入店铺名称！", HintDialog.WARM);
                    } else if (et_sjrz_address.getText().toString().equals("")) {
                        Hint("请填写所在地址！", HintDialog.WARM);
                    } else if (sYyzz.equals("")) {
                        Hint("请上传营业执照！", HintDialog.WARM);
                    } else if (sDptx.equals("")) {
                        Hint("请上传店铺！", HintDialog.WARM);
                    } else {
                        hideDialogin();
                        dialogin("");
                        tjsh(et_sjrz_address.getText().toString(), et_sjrz_storename.getText().toString(),
                                et_sjrz_name.getText().toString(), et_sjrz_phone.getText().toString());
                        //upLoadImage(Api.sUrl + "Order/residence/", new File(imagePath));
                    }
                } else {
                    Hint("请输入正确的手机号", HintDialog.WARM);
                    //  Toast.makeText(NoteActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }

            }
        });
        iv_sjrz_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iImge_type = 0;
                dialog();
            }
        });
        iv_sjrz_image_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iImge_type = 1;
                dialog();
            }
        });
        sp_sjrz_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sCategory = list_category_id.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        et_sjrz_beizhu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tv_sjrz_number.setText(et_sjrz_beizhu.getText().length() + "/500");
            }
        });
        hideDialogin();
        dialogin("");
        fenlei();
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

    /**
     * UI更改操作
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED) {
                if (iImge_type == 0) {
                    sYyzz = resultUrl;
                    Glide.with(SjrzActivity.this)
                            .load( Api.sUrl+ resultUrl)
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_sjrz_image);
                } else if (iImge_type == 1) {
                    sDptx = resultUrl;
                    Glide.with(SjrzActivity.this)
                            .load( Api.sUrl+ resultUrl)
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_sjrz_image_logo);
                }


            }
        }
    };


    /**
     * 提交审核
     */
    private void tjsh(String sAddress, String sShangjianame, String sLianxiname, String sLianxiphone) {
        String url = Api.sUrl + "Api/Good/shangjiaenter";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("address", sAddress);
        params.put("shangjianame", sShangjianame);
        params.put("lianxiname", sLianxiname);
        params.put("lianxiphone", sLianxiphone);
        params.put("type_id", sCategory);
        params.put("xukezheng", sYyzz);
        params.put("logo", sDptx);
        if (!et_sjrz_beizhu.getText().toString().equals("")) {
            params.put("comment", et_sjrz_beizhu.getText().toString());
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
                                }, 1250);
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


    /**
     * 店铺分类
     */
    private void fenlei() {
        String url = Api.sUrl + "Api/Good/typelist/appId/" + Api.sApp_Id;
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
                        arr_adapter = new ArrayAdapter<String>(SjrzActivity.this, android.R.layout.simple_spinner_item, list_category);
                        //设置样式
                        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //加载适配器
                        sp_sjrz_category.setAdapter(arr_adapter);

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

    public void Hint(String hint, final String Data) {
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
                Intent intent = new Intent(SjrzActivity.this, ZhifuActivity.class);
                intent.putExtra("residence_id", Data);
                startActivity(intent);
                finish();
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

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(SjrzActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(SjrzActivity.this, R.style.dialog, sHint, type, true).show();
    }

    private void back() {
        finish();
    }

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

}
