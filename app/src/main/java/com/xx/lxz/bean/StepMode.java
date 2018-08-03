package com.xx.lxz.bean;

import java.io.Serializable;

public class StepMode implements Serializable{

    private int step;
    private String textInfo;

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getTextInfo() {
        return textInfo;
    }

    public void setTextInfo(String textInfo) {
        this.textInfo = textInfo;
    }
}
