package com.janyee.bladea.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by lixinghua on 15/10/31.
 */
public abstract class ModuleFrame<T> extends FrameLayout {
    T value;
    Context context;

    public ModuleFrame(Context context, T t) {
        super(context);
        this.context = context;
        this.value = t;
        init();
    }

    public ModuleFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ModuleFrame(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public void init() {
        init(context);
        update();
    }

    public abstract void init(Context context);

    public abstract void update();

    public void update(T t) {
        value = t;
        update();
    }

    public T getBoundData() {
        return value;
    }
}
