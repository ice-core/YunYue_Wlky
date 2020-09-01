package com.example.administrator.yunyue.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2019/10/14.
 */

public class Fjsc_Shouye_SjlbData implements Serializable {

    //商品信息
    public ArrayList<HashMap<String, String>> commodity = new ArrayList<HashMap<String, String>>();
    //商家服务内容列表
    public List<String> service_id = new ArrayList<>();
    //距离
    public String distance = "";
    //商家id
    public String id = "";
    //商家名称
    public String shangjianame = "";
    //商家图片
    public String logo = "";
    //月售
    public String xiao_num = "";
    //商家评分
    public String shangjia_fen = "";
    //运费
    public String freight = "";

    //商家服务内容
    public String name = "";
    //起送价格
    public String uptosend = "";
    //配送时间
    public String duration = "";


}
