package com.xx.lxz.bean;

import java.io.Serializable;

public class ProductProper implements Serializable{

    private String properName;
    private String properValue;
    private boolean isSeclect;
    private String cycleprice;//每期价格
    private String signprice;//签约价
    private String sku_id;//签约价


    public String getCycleprice() {
        return cycleprice;
    }

    public void setCycleprice(String cycleprice) {
        this.cycleprice = cycleprice;
    }

    public String getSignprice() {
        return signprice;
    }

    public void setSignprice(String signprice) {
        this.signprice = signprice;
    }

    public String getProperName() {
        return properName;
    }

    public void setProperName(String properName) {
        this.properName = properName;
    }

    public String getProperValue() {
        return properValue;
    }

    public void setProperValue(String properValue) {
        this.properValue = properValue;
    }

    public boolean isSeclect() {
        return isSeclect;
    }

    public void setSeclect(boolean seclect) {
        isSeclect = seclect;
    }
}
