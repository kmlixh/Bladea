package com.janyee.bladea.Views.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.janyee.bladea.Utils.FastAdapter;

import java.util.List;


/**
 * Created by lier on kmlixh on 2015/11/3.
 */
public abstract class CalendarAdapter<T> extends FastAdapter<T> {
    MonthView monthView;
    private CalendarUtil util;
    private TextView title;
    RelativeLayout monthBar;
    LinearLayout weekBar;

    public CalendarAdapter(MonthView monthView, Context ctx, CalendarUtil util, List<T> list) {
        super(ctx, list);
        this.util = util;
        this.monthView = monthView;
    }

    public void setUtil(CalendarUtil util) {
        this.util = util;
        title.setText(util.getMonthText());
    }

    public View getTitleView() {
        int textColor = Color.parseColor("#212121");
        int textMargins=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, context.getResources().getDisplayMetrics());
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams reparam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        monthBar = new RelativeLayout(context);
        monthBar.setLayoutParams(reparam);
        title = new TextView(context);
        title.setText(util.getMonthText());
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        params.topMargin= (int) (textMargins*2.5);
        params.bottomMargin= (int) (textMargins*2.5);
        title.setLayoutParams(params);
        title.setTextColor(textColor);
        monthBar.addView(title);
        monthBar.setBackgroundColor(Color.WHITE);
        monthBar.setVisibility(View.GONE);
        layout.addView(monthBar);
        weekBar = new LinearLayout(context);
        weekBar.setBackgroundColor(Color.WHITE);
        weekBar.setOrientation(LinearLayout.HORIZONTAL);
        String[] titles = WeekDayMaker.getWeekTitles(util.weekTag);
        for (String temp : titles) {
            TextView ttp = new TextView(context);
            ttp.setTextColor(Color.BLACK);
            ttp.setText(temp);
            ttp.setTextColor(textColor);
            LinearLayout.LayoutParams pps = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            pps.weight = 1;
            pps.topMargin=textMargins;
            pps.bottomMargin= textMargins;
            ttp.setGravity(Gravity.CENTER);
            ttp.setLayoutParams(pps);
            ttp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            weekBar.addView(ttp);
        }
        LinearLayout.LayoutParams weekparam=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        weekparam.topMargin= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f, context.getResources().getDisplayMetrics());
        weekBar.setLayoutParams(weekparam);
        layout.addView(weekBar);
        layout.setBackgroundColor(Color.parseColor("#d1d1d1"));
        return layout;
    }
    public void setTitleVisibility(int visibility){
        monthBar.setVisibility(visibility);
    }
    public void setWeekBarVisibility(int visibility){
        weekBar.setVisibility(visibility);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        view.setTag(monthView);
        return view;
    }
}
