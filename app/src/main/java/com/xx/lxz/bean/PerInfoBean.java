package com.xx.lxz.bean;

import java.io.Serializable;
import java.lang.reflect.Field;

public class PerInfoBean implements Serializable{

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
        private String cid;
        private String type;
        private String sex;
        private String ethnic;
        private String birthday;

        private String nativeplace;
        private String koseki;
        private String nowaddress;
        private String bankcard;
        private String bankaddress;
        private String company;
        private String income;
        private String consume;
        private String school;
        private String indatestring;
        private String studentcard;
        private String workcard;
        private String zhimfvideo;


        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getEthnic() {
            return ethnic;
        }

        public void setEthnic(String ethnic) {
            this.ethnic = ethnic;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getNativeplace() {
            return nativeplace;
        }

        public void setNativeplace(String nativeplace) {
            this.nativeplace = nativeplace;
        }

        public String getKoseki() {
            return koseki;
        }

        public void setKoseki(String koseki) {
            this.koseki = koseki;
        }

        public String getNowaddress() {
            return nowaddress;
        }

        public void setNowaddress(String nowaddress) {
            this.nowaddress = nowaddress;
        }

        public String getBankcard() {
            return bankcard;
        }

        public void setBankcard(String bankcard) {
            this.bankcard = bankcard;
        }

        public String getBankaddress() {
            return bankaddress;
        }

        public void setBankaddress(String bankaddress) {
            this.bankaddress = bankaddress;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        public String getConsume() {
            return consume;
        }

        public void setConsume(String consume) {
            this.consume = consume;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getIndatestring() {
            return indatestring;
        }

        public void setIndatestring(String indatestring) {
            this.indatestring = indatestring;
        }

        public String getStudentcard() {
            return studentcard;
        }

        public void setStudentcard(String studentcard) {
            this.studentcard = studentcard;
        }

        public String getWorkcard() {
            return workcard;
        }

        public void setWorkcard(String workcard) {
            this.workcard = workcard;
        }

        public String getZhimfvideo() {
            return zhimfvideo;
        }

        public void setZhimfvideo(String zhimfvideo) {
            this.zhimfvideo = zhimfvideo;
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
