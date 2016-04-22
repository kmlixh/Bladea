package com.janyee.bladea;

import android.test.AndroidTestCase;

import com.janyee.bladea.Dao.Dao;
import com.janyee.bladea.TestPojo.Sort;
import com.janyee.bladea.TestPojo.Video;

import org.junit.Test;

/**
 * Created by kmlixh on 2016/4/19.
 */
public class DaoUnitTest extends AndroidTestCase {

    @Test
    public void testSave1() throws Exception {
        Sort sort=new Sort();
        sort.setSort_id("sdfasdfas");
        sort.setSort_name("测试分类");
        Video video=new Video();
        video.setId(234324);
        video.setVideo_length(23424);
        video.setVideo_name("dsff324sdfsdf");
        video.setSortId("sdfasdfas");
        video.setVideoUrl("sdfasfasfsadfsadfs");
        Dao.getInstance(getContext()).save(sort);
        Dao.getInstance(getContext()).save(video);

    }

    @Test
    public void testSave2() throws Exception {

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
    public void testDropTable() throws Exception {

    }

    @Test
    public void testDropTable1() throws Exception {

    }

    @Test
    public void testCreate() throws Exception {

    }

    @Test
    public void testCreateIfNotExist() throws Exception {

    }
}