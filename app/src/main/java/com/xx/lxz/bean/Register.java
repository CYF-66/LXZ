package com.xx.lxz.bean;

import java.io.Serializable;
import java.lang.reflect.Field;

public class Register implements Serializable{

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
        private Custom custom;
        private String token;

        public Custom getCustom() {
            return custom;
        }

        public void setCustom(Custom custom) {
            this.custom = custom;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public class Custom implements Serializable{
            private String cid;
            private String phone;
            private int family_flg;
            private int link_flg;
            private int identity_flg;
            private int person_flg;
            private int taobao_flg;
            private int zhifb_flg;
            private int mobile_flg;
            private int student_flg;

            public int getFamily_flg() {
                return family_flg;
            }

            public void setFamily_flg(int family_flg) {
                this.family_flg = family_flg;
            }

            public int getLink_flg() {
                return link_flg;
            }

            public void setLink_flg(int link_flg) {
                this.link_flg = link_flg;
            }

            public int getIdentity_flg() {
                return identity_flg;
            }

            public void setIdentity_flg(int identity_flg) {
                this.identity_flg = identity_flg;
            }

            public int getPerson_flg() {
                return person_flg;
            }

            public void setPerson_flg(int person_flg) {
                this.person_flg = person_flg;
            }

            public int getTaobao_flg() {
                return taobao_flg;
            }

            public void setTaobao_flg(int taobao_flg) {
                this.taobao_flg = taobao_flg;
            }

            public int getZhifb_flg() {
                return zhifb_flg;
            }

            public void setZhifb_flg(int zhifb_flg) {
                this.zhifb_flg = zhifb_flg;
            }

            public int getMobile_flg() {
                return mobile_flg;
            }

            public void setMobile_flg(int mobile_flg) {
                this.mobile_flg = mobile_flg;
            }

            public int getStudent_flg() {
                return student_flg;
            }

            public void setStudent_flg(int student_flg) {
                this.student_flg = student_flg;
            }

            public String getCid() {
                return cid;
            }

            public void setCid(String cid) {
                this.cid = cid;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
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
