package com.xx.lxz.bean;

import java.io.Serializable;
import java.lang.reflect.Field;

public class CusInfoBean implements Serializable{

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
        private String name;
        private String phone;
        private String sex;
        private String ethnic;
        private String birthday;
        private String identity;
        private String headimg;
        private String identity_front;
        private String identity_reverse;
        private String cus_photo;
        private String identity_flg;
        private String person_flg;
        private String link_flg;
        private String family_flg;
        private String mobile_flg;
        private String taobao_flg;
        private String zhifb_flg;
        private String student_flg;
        private String shipaddress;

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

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public String getIdentity_front() {
            return identity_front;
        }

        public void setIdentity_front(String identity_front) {
            this.identity_front = identity_front;
        }

        public String getIdentity_reverse() {
            return identity_reverse;
        }

        public void setIdentity_reverse(String identity_reverse) {
            this.identity_reverse = identity_reverse;
        }

        public String getCus_photo() {
            return cus_photo;
        }

        public void setCus_photo(String cus_photo) {
            this.cus_photo = cus_photo;
        }

        public String getIdentity_flg() {
            return identity_flg;
        }

        public void setIdentity_flg(String identity_flg) {
            this.identity_flg = identity_flg;
        }

        public String getPerson_flg() {
            return person_flg;
        }

        public void setPerson_flg(String person_flg) {
            this.person_flg = person_flg;
        }

        public String getLink_flg() {
            return link_flg;
        }

        public void setLink_flg(String link_flg) {
            this.link_flg = link_flg;
        }

        public String getFamily_flg() {
            return family_flg;
        }

        public void setFamily_flg(String family_flg) {
            this.family_flg = family_flg;
        }

        public String getMobile_flg() {
            return mobile_flg;
        }

        public void setMobile_flg(String mobile_flg) {
            this.mobile_flg = mobile_flg;
        }

        public String getTaobao_flg() {
            return taobao_flg;
        }

        public void setTaobao_flg(String taobao_flg) {
            this.taobao_flg = taobao_flg;
        }

        public String getZhifb_flg() {
            return zhifb_flg;
        }

        public void setZhifb_flg(String zhifb_flg) {
            this.zhifb_flg = zhifb_flg;
        }

        public String getStudent_flg() {
            return student_flg;
        }

        public void setStudent_flg(String student_flg) {
            this.student_flg = student_flg;
        }

        public String getShipaddress() {
            return shipaddress;
        }

        public void setShipaddress(String shipaddress) {
            this.shipaddress = shipaddress;
        }

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
