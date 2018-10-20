package com.turingoal.mengbao.teacher.bean;

import com.turingoal.common.utils.TgStringUtil;

import java.io.Serializable;

import lombok.Data;

/**
 * 收藏实体类
 */
@Data
public class CollectBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title; // title

    public String getTitle() {
        return TgStringUtil.getStr(title);
    }
}
