package com.example.administrator.yunyue.data;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2019/10/14.
 */

public class Fjsc_TuijianData implements Serializable {

    //商品规格
    public ArrayList<HashMap<String, String>> newgoodguige = new ArrayList<HashMap<String, String>>();
    //颜色：红色，尺寸：xl
    public JSONObject guige;
    //商品id
    public String id = "";
    //商家id
    public String shangjia_id = "";
    //商品图标
    public String logo = "";
    //商品名称
    public String name = "";
    //月售
    public String buy_count = "";
    //商品价格
    public String price = "";

}
