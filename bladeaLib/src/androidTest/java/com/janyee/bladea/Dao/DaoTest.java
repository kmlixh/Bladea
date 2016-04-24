package com.janyee.bladea.Dao;

import android.test.AndroidTestCase;

import com.janyee.bladea.TestPojo.SubVideo;
import com.janyee.bladea.TestPojo.UserInfo;
import com.janyee.bladea.TestPojo.Video;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by kmlixh on 2016/4/24.
 */
public class DaoTest extends AndroidTestCase {

    @Test
    public void testGetInstance() throws Exception {
        Dao.getInstance(getContext());
    }

    @Test
    public void testGetInstance1() throws Exception {
        DaoInitOptions options=DaoInitOptions.getInstance();
        options.setAutoUpdateTableStructure(true);
        Dao.getInstance(getContext(),options, Video.class, SubVideo.class, UserInfo.class);
    }

    @Test
    public void testPreCachingClass() throws Exception {
        Dao dao=Dao.getInstance(getContext());
        dao.preCachingClass(Video.class,SubVideo.class);
    }

    @Test
    public void testQuery() throws Exception {

    }

    @Test
    public void testQuery1() throws Exception {

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