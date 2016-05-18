package com.janyee.bladea.TestPojo;

import com.janyee.bladea.Dao.annotation.Column;
import com.janyee.bladea.Dao.annotation.ID;
import com.janyee.bladea.Dao.annotation.Table;

import java.io.Serializable;


/**
 * Created by kmlixh on 2015/7/30.
 */
@Table("videos")
public class Video implements Serializable {
    @ID
    String courseware_id;
    @Column
    String class_id;
    @Column
    String video_url;
    @Column
    int video_progress;
    String video_duration;
    @Column
    String image_url;
    @Column
    String title;
    String note;
    @Column
    int times;
    @Column
    String teacher_name;
    String video_rate;
    @Column
    String free_flag;
    String localPath;
    boolean isPlaying;
    int played;
    @Column
    int durations;

    public int getDurations() {
        return durations;
    }

    public void setDurations(int durations) {
        this.durations = durations;
    }

    public String getCourseware_id() {
        return courseware_id;
    }

    public void setCourseware_id(String courseware_id) {
        this.courseware_id = courseware_id;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public int getVideo_progress() {
        return video_progress;
    }

    public void setVideo_progress(int video_progress) {
        this.video_progress = video_progress;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(String video_duration) {
        this.video_duration = video_duration;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public String getVideo_rate() {
        return video_rate;
    }

    public void setVideo_rate(String video_rate) {
        this.video_rate = video_rate;
    }

    public String getFree_flag() {
        return free_flag;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public void setFree_flag(String free_flag) {
        this.free_flag = free_flag;
    }

    public String getDurationSeconds() {
        try {
            String[] strs = getVideo_duration().split(":");
            int result = 0;
            int hour = 0;
            if (strs[0] != null && !strs[0].equals("")) {
                hour = Integer.parseInt(strs[0]);
            }

            int min = 0;
            if (strs[0] != null && !strs[0].equals("")) {
                min = Integer.parseInt(strs[1]);
            }
            int second = 0;
            if (strs[0] != null && !strs[0].equals("")) {
                second = Integer.parseInt(strs[2]);
            }
            result = hour * 3600 + min * 60 + second;
            return String.valueOf(result);
        } catch (Exception e) {
            return "0";
        }
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }
}
