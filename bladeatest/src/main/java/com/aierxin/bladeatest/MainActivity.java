package com.aierxin.bladeatest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.janyee.bladea.Views.PullListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PullListView<String,TextView> pullListView=new PullListView<String, TextView>(getApplicationContext()) {
            int page=1;
            @Override
            protected TextView getView(Context context, int position, String data) {
                TextView textView=new TextView(context);
                textView.setText(data);
                return textView;
            }

            @Override
            protected TextView update(TextView textView, int position, String data) {
                textView.setText(data);
                return textView;
            }

            @Override
            public List<String> refresh() {
                List<String> stringList=new ArrayList<>();
                for(int i=0;i<10;i++){
                    stringList.add("page:"+page+",seed:"+i);
                }
                OnRefreshDataFinish();
                return stringList;
            }

            @Override
            public List<String> loadMore() {
                page++;
                List<String> stringList=new ArrayList<>();
                for(int i=0;i<10;i++){
                    stringList.add("page:"+page+",seed:"+i);
                }
                OnRefreshDataFinish();
                return stringList;
            }
        };
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        pullListView.setLayoutParams(params);
        pullListView.setEnabledLoadMore(true);
        pullListView.setEnabledPullDownRefresh(true);
        setContentView(R.layout.activity_main);
        RelativeLayout holder= (RelativeLayout) findViewById(R.id.main_holder);
        holder.addView(pullListView);
    }
}
