package com.example.administrator.yunyue.sc_fenlei;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by Raul_lsj on 2018/3/22.
 */

public class ScrollBean extends SectionEntity<ScrollBean.ScrollItemBean> {

    public ScrollBean(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public ScrollBean(ScrollBean.ScrollItemBean bean) {
        super(bean);
    }

    public static class ScrollItemBean {
        private String id;
        private String text;
        private String type;
        private String imge;

        public ScrollItemBean(String id, String text, String imge, String type) {
            this.id = id;
            this.text = text;
            this.imge = imge;
            this.type = type;

        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getImge() {
            return imge;
        }

        public void setImge(String imge) {
            this.imge = imge;
        }
    }
}
