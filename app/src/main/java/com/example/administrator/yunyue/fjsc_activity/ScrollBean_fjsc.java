package com.example.administrator.yunyue.fjsc_activity;

import com.chad.library.adapter.base.entity.SectionEntity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Raul_lsj on 2018/3/22.
 */

public class ScrollBean_fjsc extends SectionEntity<ScrollBean_fjsc.ScrollItemBean> {

    public ScrollBean_fjsc(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public ScrollBean_fjsc(ScrollBean_fjsc.ScrollItemBean bean) {
        super(bean);
    }

    public static class ScrollItemBean {
        private String id;
        private String text;
        private String type;
        private String imge;
        private String price;
        private String buy_count;
        private ArrayList<HashMap<String, String>> mylistGuiGe;

        private JSONObject jsonObjectGuige;

        public ScrollItemBean(String id, String text, String imge, String price, String buy_count, ArrayList<HashMap<String, String>> mylistGuiGe,
                              JSONObject jsonObjectGuige, String type) {
            this.id = id;
            this.text = text;
            this.imge = imge;
            this.price = price;
            this.buy_count = buy_count;
            this.mylistGuiGe = mylistGuiGe;
            this.jsonObjectGuige = jsonObjectGuige;
            this.type = type;

        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public ArrayList<HashMap<String, String>> getMylistGuiGe() {
            return mylistGuiGe;
        }

        public void setMylistGuiGe(ArrayList<HashMap<String, String>> mylistGuiGe) {
            this.mylistGuiGe = mylistGuiGe;
        }


        public JSONObject getJsonObjectGuige() {
            return jsonObjectGuige;
        }

        public void setJsonObjectGuige(JSONObject jsonObjectGuige) {
            this.jsonObjectGuige = jsonObjectGuige;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getBuy_count() {
            return buy_count;
        }

        public void setBuy_count(String buy_count) {
            this.buy_count = buy_count;
        }

        public String getImge() {
            return imge;
        }

        public void setImge(String imge) {
            this.imge = imge;
        }
    }
}
