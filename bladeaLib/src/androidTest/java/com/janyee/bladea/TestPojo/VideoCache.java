package com.janyee.bladea.TestPojo;

import com.janyee.bladea.Dao.annotation.Column;
import com.janyee.bladea.Dao.annotation.Table;

/**
 * Created by kmlixh on 2016/5/12.
 */
@Table("video_cache")
public class VideoCache extends Video {
    @Column("cache_status")
    int cacheStatus=0;// -1表示缓存失败，0表示未开始缓存，1表示缓存中，2表示缓存暂停，3表示缓存完成
    @Column("fileSize")
    long fileSize;
    @Column("download_size")
    long downloadedSize;
    public int getCacheStatus() {
        return cacheStatus;
    }

    public void setCacheStatus(int cacheStatus) {// -1表示缓存失败，0表示未开始缓存，1表示缓存中，2表示缓存暂停，3表示缓存完成
        this.cacheStatus = cacheStatus;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getDownloadedSize() {
        return downloadedSize;
    }

    public void setDownloadedSize(long downloadedSize) {
        this.downloadedSize = downloadedSize;
    }
}
