package com.janyee.bladea.Dao;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.test.AndroidTestCase;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.janyee.bladea.Cache.CacheInfo;
import com.janyee.bladea.TestPojo.Sort;
import com.janyee.bladea.TestPojo.UserInfo;
import com.janyee.bladea.TestPojo.Video;

import org.junit.Test;

import java.util.UUID;

/**
 * Created by kmlixh on 2016/4/24.
 */
public class DaoTest extends AndroidTestCase {
    static  String TAG="TEST.KMLIXH";

    @Test
    public void testQuery1() throws Exception {
        String id=UUID.randomUUID().toString().replace("-","");
        Sort sort=new Sort();
        sort.setSort_name("dsfasdfsfasd");
        sort.setSort_id(id);
        sort.setOk(false);
        sort.setMark("mark");
        Dao.getInstance(getContext()).save(sort);
        Sort s2=Dao.getInstance(getContext()).fetch(Sort.class,id);
        assertEquals(sort.isOk(),s2.isOk());
    }


}