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
public class FastAdapter<T> extends BaseAdapter {
    public List<T> mlist;
    protected Context context;
    ViewBinder vbx;
    public FastAdapter(Context context) {
        this.context = context;
    }

    public FastAdapter(Context ctx, List<T> wp, ViewBinder vb) {
        mlist = wp;
        context = ctx;
        vbx = vb;


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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (null == convertView) {
            convertView = vbx.getView(position, context, mlist.get(position));
        } else {
            vbx.update(convertView, position, mlist.get(position));
        }

        return convertView;
    }

    public interface ViewBinder<T> {
        public View getView(int position, Context ctx, T data);

        public View update(View v, int position, T data);
    }


}