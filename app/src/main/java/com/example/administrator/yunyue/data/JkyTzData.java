package com.example.administrator.yunyue.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * 作者：HMY
 * 时间：2016/5/13
 */
public class JkyTzData implements Serializable {
    private static final long serialVersionUID = 2189052605715370758L;

    public List<String> imgList = new ArrayList<>();
    public boolean isShowAll = false;
    public String id = "";
    public String user_id = "";
    public String content = "";
    public String create_time = "";
    public String nickname = "";
    public String headimgurl = "";
}
