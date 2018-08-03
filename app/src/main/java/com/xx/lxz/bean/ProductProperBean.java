package com.xx.lxz.bean;

import java.io.Serializable;

public class ProductProperBean implements Serializable{

    private String sku_id;
    private String signprice;
    private String cycleprice;
    private String color;
    private String room;
    private String network;
    private String zuqi;

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public String getSignprice() {
        return signprice;
    }

    public void setSignprice(String signprice) {
        this.signprice = signprice;
    }

    public String getCycleprice() {
        return cycleprice;
    }

    public void setCycleprice(String cycleprice) {
        this.cycleprice = cycleprice;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getZuqi() {
        return zuqi;
    }

    public void setZuqi(String zuqi) {
        this.zuqi = zuqi;
    }
}
