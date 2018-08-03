package com.xx.lxz.bean;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

public class Product implements Serializable {

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

        private String product_id;//产品id
        private String product_name;//产品名称
        private String product_price;//产品总价
        private String product_leaseterm;//租期  6,12  多个租期分号分割
        private List<String> leasetermArray;
        private String leaseunit;//租期单位 1：月 2：周  3：天
        private String eachprice;//每期价格（单位元）
        private String product_deep;//产品新旧程度
        private String product_img;//产品图片地址
        private String product_detimg;//产品详细页图片地址

        public String getProduct_deep() {
            return product_deep;
        }

        public void setProduct_deep(String product_deep) {
            this.product_deep = product_deep;
        }

        public String getEachprice() {
            return eachprice;
        }

        public void setEachprice(String eachprice) {
            this.eachprice = eachprice;
        }

        public String getProduct_price() {
            return product_price;
        }

        public void setProduct_price(String product_price) {
            this.product_price = product_price;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getProduct_leaseterm() {
            return product_leaseterm;
        }

        public void setProduct_leaseterm(String product_leaseterm) {
            this.product_leaseterm = product_leaseterm;
        }

        public List<String> getLeasetermArray() {
            return leasetermArray;
        }

        public void setLeasetermArray(List<String> leasetermArray) {
            this.leasetermArray = leasetermArray;
        }

        public String getLeaseunit() {
            return leaseunit;
        }

        public void setLeaseunit(String leaseunit) {
            this.leaseunit = leaseunit;
        }

        public String getProduct_img() {
            return product_img;
        }

        public void setProduct_img(String product_img) {
            this.product_img = product_img;
        }

        public String getProduct_detimg() {
            return product_detimg;
        }

        public void setProduct_detimg(String product_detimg) {
            this.product_detimg = product_detimg;
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
