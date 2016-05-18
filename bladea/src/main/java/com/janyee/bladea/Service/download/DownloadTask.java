package com.janyee.bladea.Service.download;

/**
 * Created by kmlixh on 2016/1/6.
 */
public class DownloadTask {
    private String name;
    private String url;
    private String path;
    private Long start;
    private Long end;
    public DownloadTask(String name,String url,String storePath){
        this.name=name;
        this.url=url;
        this.path=storePath;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
