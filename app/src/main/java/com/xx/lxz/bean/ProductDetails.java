package com.xx.lxz.bean;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

public class ProductDetails implements Serializable{

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
        private String product_id;//产品id
        private String product_name;//产品名称
        private String product_leaseterm;//租期
        private List<String> leasetermArray;
        private String product_detimg;//产品详细页图片地址
        private List<ProductPrice> prodPrices;
        private List<ProductList> propList;

        public List<ProductList> getPropList() {
            return propList;
        }

        public void setPropList(List<ProductList> propList) {
            this.propList = propList;
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

        public String getProduct_detimg() {
            return product_detimg;
        }

        public void setProduct_detimg(String product_detimg) {
            this.product_detimg = product_detimg;
        }

        public List<ProductPrice> getProdPrices() {
            return prodPrices;
        }

        public void setProdPrices(List<ProductPrice> prodPrices) {
            this.prodPrices = prodPrices;
        }


        public class ProductList implements Serializable{
            private String prop_name;
            private List<ProdPropValList> prodPropValList;

            public String getProp_name() {
                return prop_name;
            }

            public void setProp_name(String prop_name) {
                this.prop_name = prop_name;
            }

            public List<ProdPropValList> getProdPropValList() {
                return prodPropValList;
            }

            public void setProdPropValList(List<ProdPropValList> prodPropValList) {
                this.prodPropValList = prodPropValList;
            }

            public class ProdPropValList implements Serializable{
                private String prop_val;

                public String getProp_val() {
                    return prop_val;
                }

                public void setProp_val(String prop_val) {
                    this.prop_val = prop_val;
                }
            }
        }
        public class ProductPrice implements Serializable{

            private String sku_id;
            private String price;//产品实际价格
            private String cycleprice;//每期价格
            private String signprice;//签约价
            private List<ProductPropes> prodProps;

            public String getCycleprice() {
                return cycleprice;
            }

            public void setCycleprice(String cycleprice) {
                this.cycleprice = cycleprice;
            }

            public String getSignprice() {
                return signprice;
            }

            public void setSignprice(String signprice) {
                this.signprice = signprice;
            }

            public String getSku_id() {
                return sku_id;
            }

            public void setSku_id(String sku_id) {
                this.sku_id = sku_id;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public List<ProductPropes> getProdProps() {
                return prodProps;
            }

            public void setProdProps(List<ProductPropes> prodProps) {
                this.prodProps = prodProps;
            }

            public class ProductPropes implements Serializable{
                private String pid;
                private String prop_name;
                private String prop_val;

                public String getPid() {
                    return pid;
                }

                public void setPid(String pid) {
                    this.pid = pid;
                }

                public String getProp_name() {
                    return prop_name;
                }

                public void setProp_name(String prop_name) {
                    this.prop_name = prop_name;
                }

                public String getProp_val() {
                    return prop_val;
                }

                public void setProp_val(String prop_val) {
                    this.prop_val = prop_val;
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
