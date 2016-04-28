package com.janyee.bladea.Dao.annotation;

import com.janyee.bladea.Dao.Factory.DataOpenHelperFactory;
import com.janyee.bladea.Dao.Factory.extds.DefaultDOHelperFactory;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2014/11/24.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Table {
    String value();
    Class<? extends DataOpenHelperFactory> factory() default DefaultDOHelperFactory.class;
}
