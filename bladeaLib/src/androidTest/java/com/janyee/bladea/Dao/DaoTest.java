package com.janyee.bladea.Dao;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.test.AndroidTestCase;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.janyee.bladea.Cache.CacheInfo;
import com.janyee.bladea.Cache.CacheManager;
import com.janyee.bladea.TestPojo.Sort;
import com.janyee.bladea.TestPojo.SubSort;
import com.janyee.bladea.TestPojo.UserInfo;
import com.janyee.bladea.TestPojo.Video;
import com.janyee.bladea.Views.PullListViewAdapter;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by kmlixh on 2016/4/24.
 */
public class DaoTest extends AndroidTestCase {
    static  String TAG="TEST.KMLIXH";

    @Test
    public void testQuery1() throws Exception {

        CacheManager manager=new CacheManager(getContext());
        Object obj=manager.get("videos");
        assertEquals(obj,null);
    }


}