package com.turingoal.mengbao.teacher.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * 食谱的实体类
 */
@Data
public class CookbookBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private int index;
    private String breakfast; // 早餐
    private String earlyLunch; // 早中
    private String lunch; // 午餐
    private String earlyDinner; // 午中
    private String dinner; // 晚餐

    public CookbookBean(final int index, final String breakfast, final String earlyLunch, final String lunch, final String earlyDinner, final String dinner) {
        this.index = index;
        this.breakfast = breakfast;
        this.earlyLunch = earlyLunch;
        this.lunch = lunch;
        this.earlyDinner = earlyDinner;
        this.dinner = dinner;
    }
}
