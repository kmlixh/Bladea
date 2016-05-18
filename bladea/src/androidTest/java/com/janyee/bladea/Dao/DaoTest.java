package com.janyee.bladea.Dao;

import android.test.AndroidTestCase;

import com.janyee.bladea.TestPojo.ClassInfo;

import org.junit.Test;

/**
 * Created by kmlixh on 2016/4/24.
 */
public class DaoTest extends AndroidTestCase {
    static  String TAG="TEST.KMLIXH";

    @Test
    public void testQuery1() throws Exception {

        ClassInfo tt=Dao.getInstance(getContext()).fetch(ClassInfo.class,"fdsasdf");
        assertEquals(SqlFactory.getSave(tt),"");
    }


}