package com.xx.lxz.bean;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by cyf on 2017/12/28.
 */

@Retention(RUNTIME)
public @interface RecyclerItemViewId {
    int value();//我们注解后面使用id值
}
