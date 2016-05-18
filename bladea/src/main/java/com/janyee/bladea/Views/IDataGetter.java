package com.janyee.bladea.Views;

import java.util.List;

/**
 * Created by kmlixh on 2016/5/18.
 */
public interface IDataGetter<T> {
    List<T> refreshWork();
    List<T> loadMoreWork();
}
