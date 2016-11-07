package com.janyee.bladea.Dao.Pojo;

import com.janyee.bladea.Dao.Factory.extds.ConfigDOHelperFactory;
import com.janyee.bladea.Dao.annotation.Column;
import com.janyee.bladea.Dao.annotation.ID;
import com.janyee.bladea.Dao.annotation.Table;

/**
 * Created by kmlixh on 2016/1/25.
 */
@Table(value="tableVersion",factory = ConfigDOHelperFactory.class)
public class TableVersion {
    @ID
    String tabs;
    @Column
    String vers;

    public TableVersion() {
    }

    public TableVersion(String tabs, String vers) {
        this.tabs = tabs;
        this.vers = vers;
    }

    public String getTabs() {
        return tabs;
    }

    public void setTabs(String tabs) {
        this.tabs = tabs;
    }

    public String getVers() {
        return vers;
    }

    public void setVers(String vers) {
        this.vers = vers;
    }
}
