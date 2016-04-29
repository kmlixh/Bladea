package com.janyee.bladea.Dao.annotation;

import com.janyee.bladea.Dao.Util.IDataParser;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kmlixh on 2015/9/22.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface DataParseBy {
    Class<? extends IDataParser> value();
}
