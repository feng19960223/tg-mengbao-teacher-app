package com.turingoal.mengbao.teacher.bean;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 用户类
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id; // 用户
    private String token; // token
    private String username; // 用户名
    private String realname; // 真实姓名
    private String cellphoneNum; // 手机号
    private String qq; // QQuid
    private String wechat; // 微信uid
    private String sina; // 新浪uid
    private String signature; // 个性签名
    private String avatar;  //头像
    private Integer userType; // 用户类型
    private String classId; // 班级id
    private String classCodeNum; // 班级编号
    private String classTitle; // 班级名称
    private Integer classGrade; // 班级年级
    private String schoolId; // 学校id
    private String schoolCodeNum; // 学校编号
    private String schoolTitle; // 学校名称
    private Integer verifiCode; // 验证码
    private Date codeCreateTime; // 验证码创建时间
}
