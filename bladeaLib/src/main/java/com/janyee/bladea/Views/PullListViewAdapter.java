package com.janyee.bladea.Views;

import android.content.Context;

import com.janyee.bladea.Utils.FastAdapter;

import java.util.List;

/**
 * Created by kmlixh on 2016/5/6.
 */
public abstract class PullListViewAdapter<T> extends FastAdapter<T> {
    public PullListViewAdapter(Context context) {
        super(context);
    }

    public PullListViewAdapter(Context ctx, List<T> wp) {
        super(ctx, wp);
    }
    protected abstract void onRefresh();
    protected abstract void onGetMore();
}
