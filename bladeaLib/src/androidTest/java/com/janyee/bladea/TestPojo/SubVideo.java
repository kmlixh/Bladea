package com.janyee.bladea.TestPojo;

import com.janyee.bladea.Dao.OnConflictType;
import com.janyee.bladea.Dao.SqlDataType;
import com.janyee.bladea.Dao.annotation.Column;
import com.janyee.bladea.Dao.annotation.NotNull;
import com.janyee.bladea.Dao.annotation.OnConflict;
import com.janyee.bladea.Dao.annotation.Table;
import com.janyee.bladea.Dao.annotation.Unique;

/**
 * Created by kmlixh on 2016/4/21.
 */
@Table("history")
public class SubVideo extends Video {
    @Unique
    @Column(type= SqlDataType.NVARCHAR,length = 32)
    String class_id;
    @Column
    @NotNull
    @OnConflict(OnConflictType.ABORT)
    String video_singel_name;

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getVideo_singel_name() {
        return video_singel_name;
    }

    public void setVideo_singel_name(String video_singel_name) {
        this.video_singel_name = video_singel_name;
    }
}
