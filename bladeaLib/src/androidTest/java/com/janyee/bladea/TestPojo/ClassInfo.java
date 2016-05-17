package com.janyee.bladea.TestPojo;

import com.janyee.bladea.Dao.annotation.Column;
import com.janyee.bladea.Dao.annotation.ID;
import com.janyee.bladea.Dao.annotation.Table;

import java.io.Serializable;


/**
 * Created by kmlixh on 2015/7/27.
 */
@Table("class_info")
public class ClassInfo implements Serializable {
    @ID
    private String class_id;
    @Column
    private String title;
    @Column
    private String category_id;
    @Column
    private float price;
    @Column
    private String note;
    @Column
    private int member_count;
    @Column
    private String image_url;
    @Column
    private String teacher_name;
    @Column
    private int teach_years;
    @Column
    private String teacher_image_url;
    @Column
    private String teacher_note;
    @Column
    private String purchase_flag;
    @Column
    private String favor_flag;
    private String share_url;
    boolean isChoosed = false;
    boolean isEditMode = false;
    private String hour;
    private String total;
    private String finish;
    private String per_count;
    private int timespan;
    @Column
    private int necessary;
    @Column
    private boolean cached;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }


    public String getPurchase_flag() {
        return purchase_flag;
    }

    public void setPurchase_flag(String purchase_flag) {
        this.purchase_flag = purchase_flag;
    }

    public String getFavor_flag() {
        return favor_flag;
    }

    public void setFavor_flag(String favor_flag) {
        this.favor_flag = favor_flag;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getMember_count() {
        return member_count;
    }

    public void setMember_count(int member_count) {
        this.member_count = member_count;
    }


    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public int getTeach_years() {
        return teach_years;
    }

    public void setTeach_years(int teach_years) {
        this.teach_years = teach_years;
    }

    public String getTeacher_image_url() {
        return teacher_image_url;
    }

    public void setTeacher_image_url(String teacher_image_url) {
        this.teacher_image_url = teacher_image_url;
    }

    public String getTeacher_note() {
        return teacher_note;
    }

    public void setTeacher_note(String teacher_note) {
        this.teacher_note = teacher_note;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setIsChoosed(boolean isChoosed) {
        this.isChoosed = isChoosed;
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setIsEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    public String getPer_count() {
        return per_count;
    }

    public void setPer_count(String per_count) {
        this.per_count = per_count;
    }

    public int getTimespan() {
        return timespan;
    }

    public void setTimespan(int timespan) {
        this.timespan = timespan;
    }

    public int getNecessary() {
        return necessary;
    }

    public void setNecessary(int necessary) {
        this.necessary = necessary;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    public boolean isCached() {
        return cached;
    }

    public void setCached(boolean cached) {
        this.cached = cached;
    }
}
