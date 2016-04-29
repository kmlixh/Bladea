package com.janyee.bladea.Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.test.AndroidTestCase;

import com.alibaba.fastjson.JSON;
import com.janyee.bladea.Dao.Cache.CacheInfo;
import com.janyee.bladea.Dao.Condition.Condition;
import com.janyee.bladea.TestPojo.Sort;
import com.janyee.bladea.TestPojo.SubVideo;
import com.janyee.bladea.TestPojo.TestMan;
import com.janyee.bladea.TestPojo.UserInfo;
import com.janyee.bladea.TestPojo.Video;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by kmlixh on 2016/4/24.
 */
public class DaoTest extends AndroidTestCase {


    @Test
    public void testQuery1() throws Exception {
        String id=UUID.randomUUID().toString().replace("-","");
        Dao dao=Dao.getInstance(getContext());
        Sort sort=new Sort();
        sort.setSort_id(id);
        sort.setSort_name("测试分类");
        dao.save(sort);
        List<Video> videoList=new ArrayList<>();
        for(int i=0;i<100;i++){
            Video video=new Video();
            video.setId(i);
            video.setSortId(id);
            video.setTestInfo("sdfafasdf"+i);
            video.setVideo_length(12312);
            video.setVideo_name("fasdf23sdfsdf"+i);
            video.setVideoUrl("http://www.baidu.com/");
            videoList.add(video);
        }
        int i=dao.save(videoList);
        dao.queryLinks(sort);
        assertEquals(100,i);
    }


}