package com.janyee.bladea.Dao.Cache;

import com.janyee.bladea.Dao.SqlDataType;
import com.janyee.bladea.Dao.annotation.Column;
import com.janyee.bladea.Dao.annotation.ID;

/**
 * Created by kmlixh on 2016/4/22.
 */
public class CacheInfo {
    @ID("cache_id")
    String cacheId;
    @Column(type = SqlDataType.BLOB)
    byte[] info;

    public String getCacheId() {
        return cacheId;
    }

    public void setCacheId(String cacheId) {
        this.cacheId = cacheId;
    }

    public byte[] getInfo() {
        return info;
    }

    public void setInfo(byte[] info) {
        this.info = info;
    }
}
