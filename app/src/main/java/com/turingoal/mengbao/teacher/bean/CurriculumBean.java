package com.turingoal.mengbao.teacher.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * 课表的实体类
 */
@Data
public class CurriculumBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private int index;
    private String amContent; // 上午内容
    private String pmContent; // 下午内容
    private String userIdAm; // 上午上课老师id
    private String userIdPm; // 下午上课老师id
    private String amUserRealname; // 上午上课老师姓名
    private String pmUserRealname; // 下午上课老师姓名

    public CurriculumBean(final int index, final String amContent, final String pmContent, final String userIdAm, final String userIdPm, final String amUserRealname, final String pmUserRealname) {
        this.index = index;
        this.amContent = amContent;
        this.pmContent = pmContent;
        this.userIdAm = userIdAm;
        this.userIdPm = userIdPm;
        this.amUserRealname = amUserRealname;
        this.pmUserRealname = pmUserRealname;
    }
}
