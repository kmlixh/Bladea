package com.janyee.bladea.TestPojo;

import com.janyee.bladea.Dao.SqlDataType;
import com.janyee.bladea.Dao.annotation.Column;
import com.janyee.bladea.Dao.annotation.ID;
import com.janyee.bladea.Dao.annotation.Table;

/**
 * Created by kmlixh on 2016/4/21.
 */
@Table("video_sort")
public class Sort {
    @ID(type = SqlDataType.NVARCHAR,length = 32)
    String sort_id;
    @Column
    String sort_name;

    public String getSort_id() {
        return sort_id;
    }

    public void setSort_id(String sort_id) {
        this.sort_id = sort_id;
    }

    public String getSort_name() {
        return sort_name;
    }

    public void setSort_name(String sort_name) {
        this.sort_name = sort_name;
    }
}
