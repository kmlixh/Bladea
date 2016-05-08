package com.janyee.bladea.Utils;

/**
 * Created by kmlixh on 2014/8/28.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @author nuatar
 *         性能优化过的Adapter，适用于ListView，GridView等
 */
public abstract class FastAdapter<T> extends BaseAdapter {
    public List<T> mlist;
    protected Context context;
    public FastAdapter(Context context) {
        this.context = context;
    }

    public FastAdapter(Context ctx, List<T> wp) {
        mlist = wp;
        context = ctx;
    }

    @Override
    public int getCount() {

        if (null != mlist) {
            return mlist.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {

        if (mlist != null && mlist.size() > position) {
            return mlist.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {

        return position;
    }
    public abstract View getView(Context context,int position,T data);
    public abstract View update(View v, int position, T data);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (null == convertView) {
            convertView = getView( context,position, mlist.get(position));
        } else {
            update(convertView,position,mlist.get(position));
        }

        return convertView;
    }

}