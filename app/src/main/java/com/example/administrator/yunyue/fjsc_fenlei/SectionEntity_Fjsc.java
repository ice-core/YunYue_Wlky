package com.example.administrator.yunyue.fjsc_fenlei;

import java.io.Serializable;

public abstract class SectionEntity_Fjsc<T> implements Serializable {
    public boolean isHeader;
    public T t;
    public String header;
    public String logo;

    public SectionEntity_Fjsc(boolean isHeader, String header, String logo) {
        this.isHeader = isHeader;
        this.header = header;
        this.t = null;
        this.logo = logo;
    }

    public SectionEntity_Fjsc(T t) {
        this.isHeader = false;
        this.header = null;
        this.t = t;
        this.logo = null;
    }
}