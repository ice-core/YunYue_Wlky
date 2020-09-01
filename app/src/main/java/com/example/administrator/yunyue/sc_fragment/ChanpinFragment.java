package com.example.administrator.yunyue.sc_fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc_activity.SpjjActivity;
import com.example.administrator.yunyue.sc_gridview.GridViewAdapterSplb;
import com.example.administrator.yunyue.sc_gridview.GridViewAdapterchanpin;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChanpinFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChanpinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChanpinFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private int imageHeight;
    //  private ObservableScrollView sv_chanpin;
    private LinearLayout ll_chanpin_gao;

    private LinearLayout ll_chanpin_jrsc;
    RequestQueue queue = null;
    private SharedPreferences pref;
    private String goods_id;
    private String user_id;
    private LinearLayout ll_chanpin_jrgwc;
    private String data;

    private Dialog dialog;
    private View inflate;
    private ImageView iv_spttc_sc;
    private TextView tv_sptcc_number;
    private ImageView iv_sptcc_jia, iv_sptcc_jian;
    int sp_number = 1;
    ArrayList<HashMap<String, Object>> mylist_spec_goods_price;
    private GridView gv_sptcc;
    GridViewAdapterchanpin mAdapter;
    private TextView tv_sptcc_xuanze;
    private String sItemItemId;
    private Button bt_sptcc_save;
    private TextView tv_sptcc_price;
    private ImageView iv_sptcc_image;
    private TextView tv_sptcc_storecount;
    private LinearLayout ll_sptcc_jia, ll_sptcc_jian;
    private MyGridView gv_chenpin;
    private ImageView iv_chanpin_image, iv_chanpin_image1, iv_chanpin_image2, iv_chanpin_image3;
    private ImageView iv_chanpin_image1back, iv_chanpin_image2back, iv_chanpin_image3back;
    ArrayList<HashMap<String, Object>> mylistImage;
    private TextView tv_chanpin_goods_name, tv_chanpin_goods_remark;
    private TextView tv_chanpin_shop_price;
    private TextView tv_chanpin_goods_content;

    public ChanpinFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChanpinFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChanpinFragment newInstance(String param1, String param2) {
        ChanpinFragment fragment = new ChanpinFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            goods_id = bundle.getString("goods_id");
            data = bundle.getString("data");
        }
        return inflater.inflate(R.layout.fragment_chanpin, container, false);
    }

    public void onViewCreated(View view, Bundle saveInstancesState) {
        super.onViewCreated(view, saveInstancesState);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean isRemember = pref.getBoolean("user", false);
        user_id = pref.getString("user_id", "");
        queue = Volley.newRequestQueue(getActivity());
        //  sv_chanpin = (ObservableScrollView) view.findViewById(R.id.sv_chanpin);
        ll_chanpin_gao = (LinearLayout) view.findViewById(R.id.ll_chanpin_gao);
        ll_chanpin_jrsc = (LinearLayout) view.findViewById(R.id.ll_chanpin_jrsc);
        ll_chanpin_jrgwc = (LinearLayout) view.findViewById(R.id.ll_chanpin_jrgwc);
        gv_chenpin = (MyGridView) view.findViewById(R.id.gv_chenpin);
        iv_chanpin_image = (ImageView) view.findViewById(R.id.iv_chanpin_image);
        iv_chanpin_image1 = (ImageView) view.findViewById(R.id.iv_chanpin_image1);
        iv_chanpin_image2 = (ImageView) view.findViewById(R.id.iv_chanpin_image2);
        iv_chanpin_image3 = (ImageView) view.findViewById(R.id.iv_chanpin_image3);
        iv_chanpin_image1back = (ImageView) view.findViewById(R.id.iv_chanpin_image1back);
        iv_chanpin_image2back = (ImageView) view.findViewById(R.id.iv_chanpin_image2back);
        iv_chanpin_image3back = (ImageView) view.findViewById(R.id.iv_chanpin_image3back);
        tv_chanpin_goods_name = (TextView) view.findViewById(R.id.tv_chanpin_goods_name);
        tv_chanpin_goods_remark = (TextView) view.findViewById(R.id.tv_chanpin_goods_remark);
        tv_chanpin_shop_price = (TextView) view.findViewById(R.id.tv_chanpin_shop_price);
        tv_chanpin_goods_content = (TextView) view.findViewById(R.id.tv_chanpin_goods_content);
        iv_chanpin_image1back.setBackgroundResource(R.color.lucency);
        iv_chanpin_image2back.setBackgroundResource(R.color.bantou);
        iv_chanpin_image3back.setBackgroundResource(R.color.bantou);
        iv_chanpin_image1back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Glide.with(getActivity())
                            .load( Api.sUrl+ mylistImage.get(0).get("image_url"))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_chanpin_image);
                } catch (Exception e) {
                    iv_chanpin_image.setImageResource(R.mipmap.error);
                    e.printStackTrace();
                }

                iv_chanpin_image1back.setBackgroundResource(R.color.lucency);
                iv_chanpin_image2back.setBackgroundResource(R.color.bantou);
                iv_chanpin_image3back.setBackgroundResource(R.color.bantou);
            }
        });
        iv_chanpin_image2back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Glide.with(getActivity())
                            .load( Api.sUrl+mylistImage.get(1).get("image_url"))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_chanpin_image);
                } catch (Exception e) {
                    iv_chanpin_image.setImageResource(R.mipmap.error);
                    e.printStackTrace();
                }
                iv_chanpin_image1back.setBackgroundResource(R.color.bantou);
                iv_chanpin_image2back.setBackgroundResource(R.color.lucency);
                iv_chanpin_image3back.setBackgroundResource(R.color.bantou);
            }
        });
        iv_chanpin_image3back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Glide.with(getActivity())
                            .load( Api.sUrl+mylistImage.get(2).get("image_url"))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_chanpin_image);
                } catch (Exception e) {
                    iv_chanpin_image.setImageResource(R.mipmap.error);
                    e.printStackTrace();
                }
                iv_chanpin_image1back.setBackgroundResource(R.color.bantou);
                iv_chanpin_image2back.setBackgroundResource(R.color.bantou);
                iv_chanpin_image3back.setBackgroundResource(R.color.lucency);
            }
        });

        ll_chanpin_jrsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogin("");
                shoucang(goods_id);
            }
        });
        ll_chanpin_jrgwc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   dialogin("");
                // gouwuche(goods_id, "", "");
                show();
            }
        });
        gv_chenpin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> map = (HashMap<String, String>) gv_chenpin.getItemAtPosition(i);
                final String ItemId = map.get("tv_id");
                dialogin("");
                jianjei(ItemId);
            }
        });

        initialize();
        data(data);
    }

    private void initialize() {
        dialog = new Dialog(getActivity(), R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(getActivity()).inflate(R.layout.sptcc_item, null);
        iv_sptcc_image = (ImageView) inflate.findViewById(R.id.iv_sptcc_image);
        tv_sptcc_price = (TextView) inflate.findViewById(R.id.tv_sptcc_price);
        tv_sptcc_storecount = (TextView) inflate.findViewById(R.id.tv_sptcc_storecount);
        iv_spttc_sc = (ImageView) inflate.findViewById(R.id.iv_spttc_sc);
        tv_sptcc_number = (TextView) inflate.findViewById(R.id.tv_sptcc_number);
        iv_sptcc_jia = (ImageView) inflate.findViewById(R.id.iv_sptcc_jia);
        iv_sptcc_jian = (ImageView) inflate.findViewById(R.id.iv_sptcc_jia);
        ll_sptcc_jia = (LinearLayout) inflate.findViewById(R.id.ll_sptcc_jia);
        ll_sptcc_jian = (LinearLayout) inflate.findViewById(R.id.ll_sptcc_jian);
        tv_sptcc_xuanze = (TextView) inflate.findViewById(R.id.tv_sptcc_xuanze);
        bt_sptcc_save = (Button) inflate.findViewById(R.id.bt_sptcc_save);
        gv_sptcc = (GridView) inflate.findViewById(R.id.gv_sptcc);
        ll_sptcc_jia.setOnClickListener(this);
        ll_sptcc_jian.setOnClickListener(this);
        iv_spttc_sc.setOnClickListener(this);
        tv_sptcc_number.setText(String.valueOf(sp_number));
        gv_sptcc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mAdapter.setSelection(i);
                mAdapter.notifyDataSetChanged();
                HashMap<String, String> map = (HashMap<String, String>) gv_sptcc.getItemAtPosition(i);
                sItemItemId = map.get("ItemItemId");
                String sItemName = map.get("ItemName");
                tv_sptcc_storecount.setText("库存" + map.get("ItemStoreCount") + "件");
                tv_sptcc_price.setText("￥" + map.get("ItemPrice"));
                tv_sptcc_xuanze.setText("已选：" + sItemName);

            }
        });
        bt_sptcc_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogin("");
                gouwuche(goods_id, String.valueOf(sp_number), sItemItemId, "1");
            }
        });
    }

    public void show() {
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    private void gridviewdata(ArrayList<HashMap<String, Object>> myList) {
        //    myList = getMenuAdapter();
        mAdapter = new GridViewAdapterchanpin(getActivity(), myList);
        gv_sptcc.setAdapter(mAdapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_spttc_sc:
                sp_number = 1;
                dialog.dismiss();
                break;
            case R.id.ll_sptcc_jian:
                if (sp_number > 1) {
                    sp_number = sp_number - 1;
                    tv_sptcc_number.setText(String.valueOf(sp_number));
                }
                break;
            case R.id.ll_sptcc_jia:
                sp_number = sp_number + 1;
                tv_sptcc_number.setText(String.valueOf(sp_number));
        }
    }

    private void data(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String resultMsg = jsonObject.getString("msg");
            int resultCode = jsonObject.getInt("code");
            if (resultCode > 0) {
                String resultData = jsonObject.getString("data");
                JSONObject jsonObjectData = new JSONObject(resultData);
                String resultgoods = jsonObjectData.getString("goods");
                JSONObject jsonObjectgoods = new JSONObject(resultgoods);
                String resultgoods_name = jsonObjectgoods.getString("goods_name");
                String resultgoods_remark = jsonObjectgoods.getString("goods_remark");
                String resultshop_price1 = jsonObjectgoods.getString("shop_price");
                String resultgoods_content = jsonObjectgoods.getString("goods_content");

                tv_chanpin_goods_name.setText(resultgoods_name);
                tv_chanpin_goods_remark.setText(resultgoods_remark);
                tv_chanpin_shop_price.setText("￥：" + resultshop_price1);
                tv_chanpin_goods_content.setText(resultgoods_content);
                String resultgoods_images_list = jsonObjectData.getString("goods_images_list");
                JSONObject jsonObjectgoods_images_list = new JSONObject(resultgoods_images_list);
                JSONArray resultJsonArray1 = jsonObjectgoods_images_list.getJSONArray("1");
                mylistImage = new ArrayList<HashMap<String, Object>>();
                for (int i = 0; i < resultJsonArray1.length(); i++) {
                    JSONObject jsonObjecti = resultJsonArray1.getJSONObject(i);
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("goods_id", jsonObjecti.getString("goods_id"));
                    map.put("image_url", jsonObjecti.getString("image_url"));
                    mylistImage.add(map);
                }
                try {
                    Glide.with(getActivity())
                            .load( Api.sUrl+mylistImage.get(0).get("image_url"))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_sptcc_image);
                } catch (Exception e) {
                    iv_sptcc_image.setImageResource(R.mipmap.error);
                    e.printStackTrace();
                }
                try {
                    Glide.with(getActivity())
                            .load( Api.sUrl+ mylistImage.get(0).get("image_url"))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_chanpin_image);
                } catch (Exception e) {
                    iv_chanpin_image.setImageResource(R.mipmap.error);
                    e.printStackTrace();
                }
                try {
                    Glide.with(getActivity())
                            .load( Api.sUrl+ mylistImage.get(0).get("image_url"))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_chanpin_image1);
                } catch (Exception e) {
                    iv_chanpin_image1.setImageResource(R.mipmap.error);
                    e.printStackTrace();
                }
                try {
                    Glide.with(getActivity())
                            .load( Api.sUrl+mylistImage.get(1).get("image_url"))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_chanpin_image2);
                } catch (Exception e) {
                    iv_chanpin_image2.setImageResource(R.mipmap.error);
                    e.printStackTrace();
                }
                try {
                    Glide.with(getActivity())
                            .load( Api.sUrl+ mylistImage.get(2).get("image_url"))
                            .asBitmap()
                            .placeholder(R.mipmap.error)
                            .error(R.mipmap.error)
                            .dontAnimate()
                            .into(iv_chanpin_image3);
                } catch (Exception e) {
                    iv_chanpin_image3.setImageResource(R.mipmap.error);
                    e.printStackTrace();
                }


                JSONArray resultJsonArraytuijiangoods = jsonObjectData.getJSONArray("tuijiangoods");
                ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
                for (int i = 0; i < resultJsonArraytuijiangoods.length(); i++) {
                    JSONObject jsonObjecti = resultJsonArraytuijiangoods.getJSONObject(i);
                    String resultGoodsId = jsonObjecti.getString("goods_id");
                    String resultGoodsName = jsonObjecti.getString("goods_name");
                    String resultshop_price = jsonObjecti.getString("shop_price");
                    String resultoriginal_img = jsonObjecti.getString("original_img");
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("iv_splb", resultoriginal_img);
                    map.put("tv_id", resultGoodsId);
                    map.put("tv_splb", resultGoodsName);
                    map.put("tv_splb_q", "￥" + resultshop_price);
                    mylist.add(map);
                }
                gridviewdatatuijian(mylist);

                JSONArray resultJsonArray = jsonObject.getJSONArray("spec_goods_price");
                mylist_spec_goods_price = new ArrayList<HashMap<String, Object>>();
                for (int i = 0; i < resultJsonArray.length(); i++) {
                    jsonObject = resultJsonArray.getJSONObject(i);
                    String resultItemId = jsonObject.getString("item_id");
                    String resultName = jsonObject.getString("name");
                    String resultPrice = jsonObject.getString("price");
                    String resultStoreCount = jsonObject.getString("store_count");
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("ItemItemId", resultItemId);
                    map.put("ItemName", resultName);
                    map.put("ItemPrice", resultPrice);
                    map.put("ItemStoreCount", resultStoreCount);
                    mylist_spec_goods_price.add(map);
                    if (i == 0) {
                        sItemItemId = resultItemId;
                        tv_sptcc_storecount.setText("库存" + resultStoreCount + "件");
                        tv_sptcc_price.setText("￥" + resultPrice);
                        tv_sptcc_xuanze.setText("已选：" + resultName);
                        tv_sptcc_xuanze.setText("已选：" + resultName);
                    }
                }
                gridviewdata(mylist_spec_goods_price);

            } else {
                Hint(resultMsg, HintDialog.ERROR);
            }
        } catch (
                JSONException e)

        {

            e.printStackTrace();
        }
    }

    private void gridviewdatatuijian(ArrayList<HashMap<String, Object>> myList) {
        //    myList = getMenuAdapter();
        GridViewAdapterSplb mAdapter = new GridViewAdapterSplb(getActivity(), myList);
        gv_chenpin.setAdapter(mAdapter);

    }


    private void gouwuche(String goods_id, String goods_num, String item_id, String type) {
        String url = Api.sUrl + "Order/cartAdd/user_id/" + user_id + "/goods_id/" + goods_id + "/goods_num/" + goods_num + "" + "/item_id/" + item_id + "/type/" + type;
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
                        dialog.dismiss();
                        sp_number = 1;
                        Hint(resultMsg, HintDialog.SUCCESS);
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

    private void shoucang(String goods_id) {
        String url = Api.sUrl + "Order/collectAdd/user_id/" + user_id + "/goods_id/" + goods_id;
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
                        Hint(resultMsg, HintDialog.SUCCESS);
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

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(getActivity(), R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(getActivity(), R.style.dialog, sHint, type, true).show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void jianjei(final String pid) {
        String url = Api.sUrl + "Goods/goodsDetail/pid/" + pid;
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
                        Intent intent1 = new Intent(getActivity(), SpjjActivity.class);
                        intent1.putExtra("goods_id", pid);
                        intent1.putExtra("data", sDate);
                        intent1.putExtra("activty", "main");
                        startActivity(intent1);
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


    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        public ArrayList<String> arrHotimg;
        public ArrayList<String> arrHotName;
        public ArrayList<String> arrHotRemark;
        public ArrayList<String> arrHotPrice;
        public ArrayList<String> arrGoodsId;

        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arrHotimg = new ArrayList<String>();
            arrHotName = new ArrayList<String>();
            arrHotRemark = new ArrayList<String>();
            arrHotPrice = new ArrayList<String>();
            arrGoodsId = new ArrayList<String>();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arrHotimg.size();
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

        public Bitmap stringToBitmap(String string) {    // 将字符串转换成Bitmap类型
            Bitmap bitmap = null;
            try {
                byte[] bitmapArray;
                bitmapArray = Base64.decode(string, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }


        @Override
        public View getView(final int position, View view, ViewGroup arg2) {
            // TODO Auto-generated method stub
            if (view == null) {
                view = inflater.inflate(R.layout.shangcheng_hot_item, null);
            }
            ImageView iv_shangcheng_hot_img = (ImageView) view.findViewById(R.id.iv_shangcheng_hot_img);
            LinearLayout ll_shangcheng_hot_item = (LinearLayout) view.findViewById(R.id.ll_shangcheng_hot_item);
            TextView iv_shangcheng_hot_name = (TextView) view.findViewById(R.id.iv_shangcheng_hot_name);
            TextView iv_shangcheng_hot_remark = (TextView) view.findViewById(R.id.iv_shangcheng_hot_remark);
            TextView iv_shangcheng_hot_price = (TextView) view.findViewById(R.id.iv_shangcheng_hot_price);
            iv_shangcheng_hot_name.setText(arrHotName.get(position));
            iv_shangcheng_hot_remark.setText(arrHotRemark.get(position));
            iv_shangcheng_hot_price.setText(arrHotPrice.get(position));
            Glide.with(getActivity()).load( Api.sUrl+ arrHotimg.get(position))
                    .asBitmap()
                    .dontAnimate()
                /*    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)*/
                    .into(iv_shangcheng_hot_img);
            ll_shangcheng_hot_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogin("");
                    jianjei(arrGoodsId.get(position));
                }
            });

            return view;
        }
    }
}
