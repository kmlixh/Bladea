package com.janyee.bladea.Dao;

/**
 * Created by kmlixh on 2016/1/14.
 */
public class DaoInitOptions {
    private boolean autoUpdateTableStructure=true;
    private DaoInitOptions() {

    }

    public static DaoInitOptions getInstance() {
        return new DaoInitOptions();
    }

    public boolean isAutoUpdateTableStructure() {
        return autoUpdateTableStructure;
    }

    public void setAutoUpdateTableStructure(boolean autoUpdateTableStructure) {
        this.autoUpdateTableStructure = autoUpdateTableStructure;
    }
    
}
