package com.janyee.bladea.TestPojo;

import com.janyee.bladea.Dao.annotation.Column;
import com.janyee.bladea.Dao.annotation.Table;

/**
 * Created by kmlixh on 2016/5/5.
 */
@Table("sub_sort")
public class SubSort  extends Sort{
    @Column
    boolean isOk;
    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }
}
