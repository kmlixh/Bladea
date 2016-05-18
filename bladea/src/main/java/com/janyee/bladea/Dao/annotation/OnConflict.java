package com.janyee.bladea.Dao.annotation;

import com.janyee.bladea.Dao.OnConflictType;

/**
 * Created by kmlixh on 2016/4/21.
 */
public @interface OnConflict {
    OnConflictType value();

}
