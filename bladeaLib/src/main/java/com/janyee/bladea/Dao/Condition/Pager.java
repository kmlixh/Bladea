package com.janyee.bladea.Dao.Condition;

/**
 * Created by Administrator on 2014/11/26.
 */
public class Pager {
    int pageIndex = 0;
    int pageLength;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageLength() {
        return pageLength;
    }

    public void setPageLength(int pageLength) {
        this.pageLength = pageLength;
    }

    public Pager(int PageIndex, int Length) {
        pageIndex = PageIndex;
        pageLength = Length;
    }
    @Override
    public String toString(){
       return  " LIMIT " + getPageIndex() + "," + getPageLength();
    }
}
