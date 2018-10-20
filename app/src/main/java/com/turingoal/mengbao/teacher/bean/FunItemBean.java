package com.turingoal.mengbao.teacher.bean;

import lombok.Data;

/**
 * 主菜单功能，实体类
 */
@Data
public class FunItemBean {
    private int resId = 1; // 图片
    private String title = ""; // 标题
    private String path = ""; // 路径
    private String count = ""; // 未读消息数

    public FunItemBean(final int resIdParm, final String titleParm, final String pathParm) {
        this.resId = resIdParm;
        this.title = titleParm;
        this.path = pathParm;
    }

    public void setCount(int countParm) {
        if (countParm > 0) {
            this.count = "" + countParm;
            if (countParm > 99) { // 99以上显示 99+
                this.count = "99+";
            }
        } else {
            this.count = "";
        }
    }
}
