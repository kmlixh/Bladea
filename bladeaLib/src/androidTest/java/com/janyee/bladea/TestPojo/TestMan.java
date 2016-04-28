package com.janyee.bladea.TestPojo;

import com.janyee.bladea.Dao.annotation.Column;
import com.janyee.bladea.Dao.annotation.ID;
import com.janyee.bladea.Dao.annotation.Table;

/**
 * Created by kmlixh on 2016/4/28.
 */
@Table("test_man")
public class TestMan {
    @ID
    String id;
    @Column
    int type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
