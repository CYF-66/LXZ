package com.xx.lxz.bean;

import java.io.Serializable;
import java.lang.reflect.Field;

public class ContactBean implements Serializable{

    private int code;
    private String message;
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data implements Serializable{
        private String family_linkone_name;
        private String family_linkone_sex;
        private String family_linkone_relation;
        private String family_linkone_phone;
        private String family_linkone_address;

        private String family_linktwo_name;
        private String family_linktwo_sex;
        private String family_linktwo_relation;
        private String family_linktwo_phone;
        private String family_linktwo_address;


        public String getFamily_linkone_name() {
            return family_linkone_name;
        }

        public void setFamily_linkone_name(String family_linkone_name) {
            this.family_linkone_name = family_linkone_name;
        }

        public String getFamily_linkone_sex() {
            return family_linkone_sex;
        }

        public void setFamily_linkone_sex(String family_linkone_sex) {
            this.family_linkone_sex = family_linkone_sex;
        }

        public String getFamily_linkone_relation() {
            return family_linkone_relation;
        }

        public void setFamily_linkone_relation(String family_linkone_relation) {
            this.family_linkone_relation = family_linkone_relation;
        }

        public String getFamily_linkone_phone() {
            return family_linkone_phone;
        }

        public void setFamily_linkone_phone(String family_linkone_phone) {
            this.family_linkone_phone = family_linkone_phone;
        }

        public String getFamily_linkone_address() {
            return family_linkone_address;
        }

        public void setFamily_linkone_address(String family_linkone_address) {
            this.family_linkone_address = family_linkone_address;
        }

        public String getFamily_linktwo_name() {
            return family_linktwo_name;
        }

        public void setFamily_linktwo_name(String family_linktwo_name) {
            this.family_linktwo_name = family_linktwo_name;
        }

        public String getFamily_linktwo_sex() {
            return family_linktwo_sex;
        }

        public void setFamily_linktwo_sex(String family_linktwo_sex) {
            this.family_linktwo_sex = family_linktwo_sex;
        }

        public String getFamily_linktwo_relation() {
            return family_linktwo_relation;
        }

        public void setFamily_linktwo_relation(String family_linktwo_relation) {
            this.family_linktwo_relation = family_linktwo_relation;
        }

        public String getFamily_linktwo_phone() {
            return family_linktwo_phone;
        }

        public void setFamily_linktwo_phone(String family_linktwo_phone) {
            this.family_linktwo_phone = family_linktwo_phone;
        }

        public String getFamily_linktwo_address() {
            return family_linktwo_address;
        }

        public void setFamily_linktwo_address(String family_linktwo_address) {
            this.family_linktwo_address = family_linktwo_address;
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
