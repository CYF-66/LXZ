package com.xx.lxz.bean;

import java.io.Serializable;
import java.util.List;

public class OrderDTO implements Serializable{

    private Order.data.OrderMode contentDates;
    private List<Order.data.OrderMode.OrderDetailsMode> detailsDates;

    public List<Order.data.OrderMode.OrderDetailsMode> getDetailsDates() {
        return detailsDates;
    }

    public void setDetailsDates(List<Order.data.OrderMode.OrderDetailsMode> detailsDates) {
        this.detailsDates = detailsDates;
    }

    public Order.data.OrderMode getContentDates() {
        return contentDates;
    }

    public void setContentDates(Order.data.OrderMode contentDates) {
        this.contentDates = contentDates;
    }
}
