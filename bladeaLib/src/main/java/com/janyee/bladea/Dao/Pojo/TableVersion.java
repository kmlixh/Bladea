package com.janyee.bladea.Dao.Pojo;

import com.janyee.bladea.Dao.annotation.Column;
import com.janyee.bladea.Dao.annotation.ID;
import com.janyee.bladea.Dao.annotation.Table;

/**
 * Created by kmlixh on 2016/1/25.
 */
@Table(value="tableVersion")
public class TableVersion {
    @ID
    String table;
    @Column
    String version;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
