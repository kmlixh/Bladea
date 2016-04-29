package com.janyee.bladea.Cache;

import com.janyee.bladea.Dao.annotation.Column;
import com.janyee.bladea.Dao.annotation.ID;
import com.janyee.bladea.Dao.annotation.Table;

/**
 * Created by kmlixh on 2016/4/22.
 */
@Table("cache_info")
public class CacheInfo {
    @ID("id")
    String cacheId;
    @Column("className")
    String className;
    @Column()
    String info;

    public String getCacheId() {
        return cacheId;
    }

    public void setCacheId(String cacheId) {
        this.cacheId = cacheId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
