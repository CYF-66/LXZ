package com.xx.lxz.bean;

import java.io.Serializable;
import java.lang.reflect.Field;

public class BillDetails implements Serializable{
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
        private String pid;//本期账单id
        private String state;//账单状态 1：已还（该状态就不显示还款按钮） 2：待还 3：逾期， 5：尚未开始（该状态不需要显示还款按钮）
        private String money;//基本费用
        private String curphase;//周期 第几期
        private String expectfee;//逾期费
        private String date;//最后还款日
        private String realydate;//时间还款日
        private String expectday;//时间还款日
        private String totalmoney;//总价
        private String book_no;//订单id

        public String getExpectday() {
            return expectday;
        }

        public void setExpectday(String expectday) {
            this.expectday = expectday;
        }

        public String getTotalmoney() {
            return totalmoney;
        }

        public void setTotalmoney(String totalmoney) {
            this.totalmoney = totalmoney;
        }

        public String getBook_no() {
            return book_no;
        }

        public void setBook_no(String book_no) {
            this.book_no = book_no;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getCurphase() {
            return curphase;
        }

        public void setCurphase(String curphase) {
            this.curphase = curphase;
        }

        public String getExpectfee() {
            return expectfee;
        }

        public void setExpectfee(String expectfee) {
            this.expectfee = expectfee;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getRealydate() {
            return realydate;
        }

        public void setRealydate(String realydate) {
            this.realydate = realydate;
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
