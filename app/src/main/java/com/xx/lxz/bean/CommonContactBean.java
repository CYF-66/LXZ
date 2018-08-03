package com.xx.lxz.bean;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

public class CommonContactBean implements Serializable{

    private int code;
    private String message;
    private List<Data> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data implements Serializable{
        private String lname;
        private String lsex;
        private String lrelation;
        private String lphone;
        private String lmemo;

        public String getLname() {
            return lname;
        }

        public void setLname(String lname) {
            this.lname = lname;
        }

        public String getLsex() {
            return lsex;
        }

        public void setLsex(String lsex) {
            this.lsex = lsex;
        }

        public String getLrelation() {
            return lrelation;
        }

        public void setLrelation(String lrelation) {
            this.lrelation = lrelation;
        }

        public String getLphone() {
            return lphone;
        }

        public void setLphone(String lphone) {
            this.lphone = lphone;
        }

        public String getLmemo() {
            return lmemo;
        }

        public void setLmemo(String lmemo) {
            this.lmemo = lmemo;
        }

        @Override
        public String toString() {
            String s = "";
            Field[] arr = this.getClass().getFields();
            for (Field f : getClass().getFields()) {
                try {
                    s += f.getName() + "=" + f.get(this) + "\n,";
                } catch (Exception e) {
                }
            }
            return getClass().getSimpleName() + "{"
                    + (arr.length == 0 ? s : s.substring(0, s.length() - 1)) + "}";
        }
    }

    @Override
    public String toString() {
        String s = "";
        Field[] arr = this.getClass().getFields();
        for (Field f : getClass().getFields()) {
            try {
                s += f.getName() + "=" + f.get(this) + "\n,";
            } catch (Exception e) {
            }
        }
        return getClass().getSimpleName() + "{"
                + (arr.length == 0 ? s : s.substring(0, s.length() - 1)) + "}";
    }
}
