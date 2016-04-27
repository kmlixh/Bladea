package com.janyee.bladea.Dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.test.AndroidTestCase;

import com.janyee.bladea.TestPojo.SubVideo;
import com.janyee.bladea.TestPojo.UserInfo;
import com.janyee.bladea.TestPojo.Video;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by kmlixh on 2016/4/24.
 */
public class DaoTest extends AndroidTestCase {

    @Test
    public void testQuery() throws Exception {
        List<Video> videoList=new ArrayList<>();
        for(int i=0;i<20;i++){
            Video video=new Video();
            video.setId(i);
            video.setVideo_length(i+2);
            video.setVideoUrl("sdfasdfsadff"+i);
            video.setSortId("sdfas2dfdf");
            video.setVideo_name("sdfasdf2wefsdf");
            video.setTestInfo("sdfasdfasdfasdf234"+i);
            videoList.add(video);
        }
        Dao dao=Dao.getInstance(getContext());
        dao.save(videoList);
        assertEquals(dao.count(Video.class),20);
    }

    @Test
    public void testQuery1() throws Exception {
        SQLiteDatabase database=DBHelper.getInstance(getContext()).getWritableDatabase();
        StringBuilder stringBuilder=new StringBuilder("select * from sqlite_master where type='table' and name ='video_info'");
        Cursor cursor=database.rawQuery(stringBuilder.toString(),new String[]{});
        if(cursor.moveToNext()&&cursor.getCount()>0){
            String info=cursor.getString(2);
            assertEquals(info,"");
        }
    }

    @Test
    public void testQuery2() throws Exception {

    }

    @Test
    public void testQuery3() throws Exception {

    }

    @Test
    public void testFetch() throws Exception {

    }

    @Test
    public void testFetch1() throws Exception {

    }

    @Test
    public void testInsert() throws Exception {

    }

    @Test
    public void testInsert1() throws Exception {



    }

    @Test
    public void testInsert2() throws Exception {

    }

    @Test
    public void testInsert3() throws Exception {

    }

    @Test
    public void testInsert4() throws Exception {

    }

    @Test
    public void testInsert5() throws Exception {

    }

    @Test
    public void testSave() throws Exception {

    }

    @Test
    public void testSave1() throws Exception {

    }

    @Test
    public void testSave2() throws Exception {

    }

    @Test
    public void testSave3() throws Exception {

    }

    @Test
    public void testSave4() throws Exception {

    }

    @Test
    public void testSave5() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }

    @Test
    public void testDelete1() throws Exception {

    }

    @Test
    public void testDelete2() throws Exception {

    }

    @Test
    public void testDelete3() throws Exception {

    }

    @Test
    public void testDelete4() throws Exception {

    }

    @Test
    public void testDelete5() throws Exception {

    }

    @Test
    public void testDelete6() throws Exception {

    }

    @Test
    public void testDelete7() throws Exception {

    }

    @Test
    public void testDelete8() throws Exception {

    }

    @Test
    public void testDelete9() throws Exception {

    }

    @Test
    public void testDropTable() throws Exception {

    }

    @Test
    public void testDropTable1() throws Exception {

    }

    @Test
    public void testDropTable2() throws Exception {

    }

    @Test
    public void testDropTable3() throws Exception {

    }

    @Test
    public void testCreate() throws Exception {

    }

    @Test
    public void testCreate1() throws Exception {

    }

    @Test
    public void testCreateIfNotExist() throws Exception {

    }

    @Test
    public void testCreateIfNotExist1() throws Exception {

    }
}