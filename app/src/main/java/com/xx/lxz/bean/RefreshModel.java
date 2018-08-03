package com.xx.lxz.bean;

import java.io.Serializable;

public class RefreshModel implements Serializable{

    private String active;//行为

    private String position;//刷新位置

    private int dataPosition;//列表位置

    private String id;//id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getDataPosition() {
        return dataPosition;
    }

    public void setDataPosition(int dataPosition) {
        this.dataPosition = dataPosition;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
