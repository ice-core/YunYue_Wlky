package com.example.administrator.yunyue.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2019/10/14.
 */

public class LjzxData implements Serializable {

    //商品列表
    public ArrayList<HashMap<String, String>> goods = new ArrayList<HashMap<String, String>>();
    //优惠卷id
    public String id = "";
    //商家id
    public String shangjia_id = "";
    //商家名称
    public String shangjia_name = "";
    //优惠券金额
    public String youhui = "";
    //优惠券名称
    public String name = "";
    //用户是否领取 1为已领取
    public String state = "";

}
