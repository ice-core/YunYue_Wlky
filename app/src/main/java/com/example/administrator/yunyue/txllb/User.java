package com.example.administrator.yunyue.txllb;

/**
 * Created by Administrator on 2016/5/25.
 */
public class User implements Comparable<User> {

    private String id; // 姓名
    private String name; // 姓名
    private String headimgurl;//头像

    private String pinyin; // 姓名对应的拼音
    private String firstLetter; // 拼音的首字母

    public User() {
    }

    public User(String name, String id, String headimgurl) {
        this.id = id;
        this.name = name;
        this.headimgurl = headimgurl;
        pinyin = Cn2Spell.getPinYin(name);// 根据姓名获取拼音
        if (!pinyin.equals("")) {
            firstLetter = pinyin.substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
            if (!firstLetter.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
                firstLetter = "#";
            }
        } else {
            firstLetter = "#";
        }
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public String getPinyin() {
        return pinyin;
    }

    public String getFirstLetter() {
        return firstLetter;
    }


    @Override
    public int compareTo(User another) {
        if (firstLetter.equals("#") && !another.getFirstLetter().equals("#")) {
            return 1;
        } else if (!firstLetter.equals("#") && another.getFirstLetter().equals("#")) {
            return -1;
        } else {
            return pinyin.compareToIgnoreCase(another.getPinyin());
        }
    }
}
