package com.xx.lxz.bean;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

public class Order implements Serializable{

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

    public Order.data getData() {
        return data;
    }

    public void setData(Order.data data) {
        this.data = data;
    }

    public class data implements Serializable{

        private int pageNum;
        private int pageSize;
        private int size;
        private String orderBy;
        private int startRow;
        private int endRow;
        private int total;
        private int pages;
        private int firstPage;
        private int prePage;
        private int lastPage;
        private boolean isFirstPage;
        private boolean isLastPage;
        private boolean hasPreviousPage;
        private boolean hasNextPage;
        private int navigatePages;
        private List<OrderMode> list;
        private List<String> navigatepageNums;


        public List<String> getNavigatepageNums() {
            return navigatepageNums;
        }

        public void setNavigatepageNums(List<String> navigatepageNums) {
            this.navigatepageNums = navigatepageNums;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getOrderBy() {
            return orderBy;
        }

        public void setOrderBy(String orderBy) {
            this.orderBy = orderBy;
        }

        public int getStartRow() {
            return startRow;
        }

        public void setStartRow(int startRow) {
            this.startRow = startRow;
        }

        public int getEndRow() {
            return endRow;
        }

        public void setEndRow(int endRow) {
            this.endRow = endRow;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getFirstPage() {
            return firstPage;
        }

        public void setFirstPage(int firstPage) {
            this.firstPage = firstPage;
        }

        public int getPrePage() {
            return prePage;
        }

        public void setPrePage(int prePage) {
            this.prePage = prePage;
        }

        public int getLastPage() {
            return lastPage;
        }

        public void setLastPage(int lastPage) {
            this.lastPage = lastPage;
        }

        public boolean isFirstPage() {
            return isFirstPage;
        }

        public void setFirstPage(boolean firstPage) {
            isFirstPage = firstPage;
        }

        public boolean isLastPage() {
            return isLastPage;
        }

        public void setLastPage(boolean lastPage) {
            isLastPage = lastPage;
        }

        public boolean isHasPreviousPage() {
            return hasPreviousPage;
        }

        public void setHasPreviousPage(boolean hasPreviousPage) {
            this.hasPreviousPage = hasPreviousPage;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public int getNavigatePages() {
            return navigatePages;
        }

        public void setNavigatePages(int navigatePages) {
            this.navigatePages = navigatePages;
        }

        public List<OrderMode> getList() {
            return list;
        }

        public void setList(List<OrderMode> list) {
            this.list = list;
        }

        public class OrderMode implements Serializable{

            private String bid;
            private String book_type;//
            private String product_name;//产品名称
            private String product_rate;//产品比率
            private String product_price;//产品总价值
            private String leaseterm_price;//每期价值
            private String leaseterm;//多少期
            private String state;//订单状态  1：待还中   2：已逾期
            private String repaytotal;//当前应还价值
            private String repaydate;//应还日期
            private String start_date;//开始时间
            private String expectday;//
            private String audit_date;//
            private String crtdate;//
            private List<OrderDetailsMode> paymentList;

            public String getAudit_date() {
                return audit_date;
            }

            public void setAudit_date(String audit_date) {
                this.audit_date = audit_date;
            }

            public String getCrtdate() {
                return crtdate;
            }

            public void setCrtdate(String crtdate) {
                this.crtdate = crtdate;
            }

            public String getBook_type() {
                return book_type;
            }

            public void setBook_type(String book_type) {
                this.book_type = book_type;
            }

            public String getProduct_rate() {
                return product_rate;
            }

            public void setProduct_rate(String product_rate) {
                this.product_rate = product_rate;
            }

            public String getLeaseterm() {
                return leaseterm;
            }

            public void setLeaseterm(String leaseterm) {
                this.leaseterm = leaseterm;
            }

            public String getStart_date() {
                return start_date;
            }

            public void setStart_date(String start_date) {
                this.start_date = start_date;
            }

            public String getExpectday() {
                return expectday;
            }

            public void setExpectday(String expectday) {
                this.expectday = expectday;
            }

            public List<OrderDetailsMode> getPaymentList() {
                return paymentList;
            }

            public void setPaymentList(List<OrderDetailsMode> paymentList) {
                this.paymentList = paymentList;
            }

            public String getBid() {
                return bid;
            }

            public void setBid(String bid) {
                this.bid = bid;
            }

            public String getProduct_name() {
                return product_name;
            }

            public void setProduct_name(String product_name) {
                this.product_name = product_name;
            }

            public String getProduct_price() {
                return product_price;
            }

            public void setProduct_price(String product_price) {
                this.product_price = product_price;
            }

            public String getLeaseterm_price() {
                return leaseterm_price;
            }

            public void setLeaseterm_price(String leaseterm_price) {
                this.leaseterm_price = leaseterm_price;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getRepaytotal() {
                return repaytotal;
            }

            public void setRepaytotal(String repaytotal) {
                this.repaytotal = repaytotal;
            }

            public String getRepaydate() {
                return repaydate;
            }

            public void setRepaydate(String repaydate) {
                this.repaydate = repaydate;
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

            public class OrderDetailsMode implements Serializable{
                private String pid;//账单id
                private int state;//本期账单状态  1：已还  2：待还 3：逾期 4：尚未开始
                private String totalmoney;//应还总金额
                private String date;//应还日期
                private String curphase;//
                private String realydate;//
                private String state_name;//状态名称

                public String getCurphase() {
                    return curphase;
                }

                public void setCurphase(String curphase) {
                    this.curphase = curphase;
                }

                public String getRealydate() {
                    return realydate;
                }

                public void setRealydate(String realydate) {
                    this.realydate = realydate;
                }

                public String getState_name() {
                    return state_name;
                }

                public void setState_name(String state_name) {
                    this.state_name = state_name;
                }

                public String getPid() {
                    return pid;
                }

                public void setPid(String pid) {
                    this.pid = pid;
                }

                public int getState() {
                    return state;
                }

                public void setState(int state) {
                    this.state = state;
                }

                public String getTotalmoney() {
                    return totalmoney;
                }

                public void setTotalmoney(String totalmoney) {
                    this.totalmoney = totalmoney;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
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
