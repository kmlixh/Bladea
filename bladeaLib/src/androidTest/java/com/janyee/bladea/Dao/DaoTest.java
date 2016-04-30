package com.janyee.bladea.Dao;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.test.AndroidTestCase;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.janyee.bladea.Cache.CacheInfo;
import com.janyee.bladea.TestPojo.UserInfo;
import com.janyee.bladea.TestPojo.Video;

import org.junit.Test;

/**
 * Created by kmlixh on 2016/4/24.
 */
public class DaoTest extends AndroidTestCase {
    static  String TAG="TEST.KMLIXH";

    @Test
    public void testQuery1() throws Exception {
       Dao.getInstance(getContext()).preCachingClass(Video.class, UserInfo.class, CacheInfo.class);
    }


}