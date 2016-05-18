package com.janyee.bladea.Ioc.Annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kmlixh on 2016/1/12.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD})
@Documented
public @interface OnItemLongClick {
    int value();
}
