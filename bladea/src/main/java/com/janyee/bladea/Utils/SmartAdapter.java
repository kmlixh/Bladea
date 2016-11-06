package com.janyee.bladea.Utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by kmlixh on 2015/12/31.
 */
public abstract class SmartAdapter<K,V extends View> extends BaseAdapter {
    List<K> viewData;
    public SmartAdapter(){

    }
    public abstract List<K> getData(Context context);
    public abstract V getView(int position,K dataItem,View convertView,ViewGroup parent);
    @Override
    public int getCount() {
        if(viewData!=null){
            return viewData.size();
        }else{
            return 0;
        }
    }

    @Override
    public K getItem(int position) {
        if(viewData!=null){
            return viewData.get(position);
        }else{
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
            convertView = getView( context,position, mlist.get(position));
        } else {
            update((V) convertView,position,mlist.get(position));
        }

        return convertView;
    }
}
