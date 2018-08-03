package com.xx.lxz.bean;

import java.io.Serializable;
import java.lang.reflect.Field;

public class InfoBean implements Serializable{

    private int code;
    private String message;
    private data data;

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

    public data getData() {
        return data;
    }

    public void setData(data data) {
        this.data = data;
    }

    public class data implements Serializable{
        private String product_price;
        private String product_name;
        private String product_lease;
        private String link;
        private String address;
        private String phone;
        private String name;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProduct_price() {
            return product_price;
        }

        public void setProduct_price(String product_price) {
            this.product_price = product_price;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getProduct_lease() {
            return product_lease;
        }

        public void setProduct_lease(String product_lease) {
            this.product_lease = product_lease;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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
