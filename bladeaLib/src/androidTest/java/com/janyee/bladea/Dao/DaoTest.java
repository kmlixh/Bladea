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
        String path="/storage/emulated/0/com.aierxin.aierxin/";
        delete(path);
    }

    public static boolean delete(String path){
        File dir=new File(path);
        if(dir.isFile()){
            return dir.delete();
        }
        if(dir.exists()&&dir.isDirectory()){
            if(dir.list()!=null&&dir.list().length>0){
                for(File temp:dir.listFiles()){
                    if(!temp.isFile()){
                        delete(temp.getAbsolutePath());
                    }
                    temp.delete();
                }
            }
            dir.delete();
        }
        return true;
    }
}