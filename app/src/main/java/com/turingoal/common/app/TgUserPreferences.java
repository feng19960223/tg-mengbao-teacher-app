package com.turingoal.common.app;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * 用户数据_参数保存服务
 */

public class TgUserPreferences {
    private SharedPreferences sharedPreferences;

    public TgUserPreferences(final Context context) {
        sharedPreferences = context.getSharedPreferences(TgSystemConfig.SP_NAME, Context.MODE_PRIVATE); //name 在TgSystemConfig中统一配置
    }

    /**
     * 清空信息
     */
    public void clear() {
        sharedPreferences.edit().clear().apply();
    }

    /**
     * 设置token
     */
    public void setToken(final String token) {
        sharedPreferences.edit().putString(SharedPreferencesKey.TOKEN, token).apply();
    }

    /**
     * 获取token
     */
    public String getToken() {
        return sharedPreferences.getString(SharedPreferencesKey.TOKEN, "");
    }


    /**
     * 设置用户id
     */
    public void setUserId(final String userId) {
        sharedPreferences.edit().putString(SharedPreferencesKey.USER_ID, userId).apply();
    }

    /**
     * 获取用户
     */
    public String getUserId() {
        return sharedPreferences.getString(SharedPreferencesKey.USER_ID, "");
    }

    /**
     * 设置用户编号
     */
    public void setUserCodeNum(final String userCodeNum) {
        sharedPreferences.edit().putString(SharedPreferencesKey.USER_CODE_NUM, userCodeNum).apply();
    }

    /**
     * 获取用户编号
     */
    public String getUserCodeNum() {
        return sharedPreferences.getString(SharedPreferencesKey.USER_CODE_NUM, "");
    }

    /**
     * 设置用户民
     */
    public void setUsername(final String username) {
        sharedPreferences.edit().putString(SharedPreferencesKey.USER_NAME, username).apply();
    }

    /**
     * 获取用户名
     */
    public String getUsername() {
        return sharedPreferences.getString(SharedPreferencesKey.USER_NAME, "");
    }


    /**
     * 设置用户真实名
     */
    public void setRealname(final String realname) {
        sharedPreferences.edit().putString(SharedPreferencesKey.USER_REAL_NAME, realname).apply();
    }

    /**
     * 获取用户真实名
     */
    public String getRealname() {
        return sharedPreferences.getString(SharedPreferencesKey.USER_REAL_NAME, "");
    }

    /**
     * 设置电话
     */
    public void setPhone(final String phone) {
        sharedPreferences.edit().putString(SharedPreferencesKey.USER_PHONE, phone).apply();
    }

    /**
     * 获取电话
     */
    public String getPhone() {
        return sharedPreferences.getString(SharedPreferencesKey.USER_PHONE, "");
    }

    /**
     * 设置头像
     */
    public void setAvatar(final String avatar) {
        sharedPreferences.edit().putString(SharedPreferencesKey.USER_AVATAR, avatar).apply();
    }

    /**
     * 获取头像
     */
    public String getAvatar() {
        return sharedPreferences.getString(SharedPreferencesKey.USER_AVATAR, "");
    }

    /**
     * 设置个性签名
     */
    public void setResume(final String resume) {
        sharedPreferences.edit().putString(SharedPreferencesKey.USER_RESUME, resume).apply();
    }

    /**
     * 获取个性签名
     */
    public String getResume() {
        return sharedPreferences.getString(SharedPreferencesKey.USER_RESUME, "");
    }

    /**
     * 设置绑定QQ
     */
    public void setBindingQQ(final boolean isBindingQQ) {
        sharedPreferences.edit().putBoolean(SharedPreferencesKey.APP_BINDING_QQ, isBindingQQ).apply();
    }

    /**
     * 获取是否绑定QQ
     */
    public boolean isBindingQQ() {
        return sharedPreferences.getBoolean(SharedPreferencesKey.APP_BINDING_QQ, false);
    }

    /**
     * 设置绑定微信
     */
    public void setBindingWeChat(final boolean isBindingWeChat) {
        sharedPreferences.edit().putBoolean(SharedPreferencesKey.APP_BINDING_WECHAT, isBindingWeChat).apply();
    }

    /**
     * 获取是否绑定WeChat
     */
    public boolean isBindingWeChat() {
        return sharedPreferences.getBoolean(SharedPreferencesKey.APP_BINDING_WECHAT, false);
    }

    /**
     * 设置绑定新浪
     */
    public void setBindingSina(final boolean isBindingSina) {
        sharedPreferences.edit().putBoolean(SharedPreferencesKey.APP_BINDING_SINA, isBindingSina).apply();
    }

    /**
     * 获取是否绑定Sina
     */
    public boolean isBindingSina() {
        return sharedPreferences.getBoolean(SharedPreferencesKey.APP_BINDING_SINA, false);
    }

    /**
     * 设置是否记住密码
     */
    public void setRememberPassword(final boolean isRememberPassword) {
        sharedPreferences.edit().putBoolean(SharedPreferencesKey.REMEMBER_PASSWORD, isRememberPassword).apply();
    }

    /**
     * 获取是否记住密码
     */
    public boolean isRememberPassword() {
        return sharedPreferences.getBoolean(SharedPreferencesKey.REMEMBER_PASSWORD, false);
    }

    /**
     * 设置登录密码
     */
    public void setLoginPassword(final String loginPassword) {
        sharedPreferences.edit().putString(SharedPreferencesKey.LOGIN_PASSWORD, loginPassword).apply();
    }

    /**
     * 获取登录密码
     */
    public String getLoginPassword() {
        return sharedPreferences.getString(SharedPreferencesKey.LOGIN_PASSWORD, "");
    }

    // ****************华丽分割线，上面的是每个应用基本上都需要，下面是每个应用各自需要的*******************

    /**
     * 设置学校id
     */
    public void setSchoolId(final String schoolId) {
        sharedPreferences.edit().putString(SharedPreferencesKey.APP_SCHOOL_ID, schoolId).apply();
    }

    /**
     * 获取学校id
     */
    public String getSchoolId() {
        return sharedPreferences.getString(SharedPreferencesKey.APP_SCHOOL_ID, "");
    }

    /**
     * 设置班级id
     */
    public void setClassId(final String classId) {
        sharedPreferences.edit().putString(SharedPreferencesKey.APP_CLASS_ID, classId).apply();
    }

    /**
     * 获取班级id
     */
    public String getClassId() {
        return sharedPreferences.getString(SharedPreferencesKey.APP_CLASS_ID, "");
    }

    /**
     * 设置园长邮箱
     */
    public void setSchoolEmail(final String schoolEmail) {
        sharedPreferences.edit().putString(SharedPreferencesKey.APP_SCHOOL_EMAIL, schoolEmail).apply();
    }

    /**
     * 获取园长邮箱
     */
    public String getSchoolEmail() {
        return sharedPreferences.getString(SharedPreferencesKey.APP_SCHOOL_EMAIL, "");
    }

    /**
     * 设置园长邮箱
     */
    public void setInformCount(final int informCount) {
        sharedPreferences.edit().putInt(SharedPreferencesKey.APP_INFORM_COUNT, informCount).apply();
    }

    /**
     * 获取未读消息数量
     */
    public int getInformCount() {
        return sharedPreferences.getInt(SharedPreferencesKey.APP_INFORM_COUNT, 0);
    }

    /**
     * 设置学校编号
     */
    public void setSchoolCodeNum(final String schoolCodeNum) {
        sharedPreferences.edit().putString(SharedPreferencesKey.APP_SCHOOL_CODE_NUM, schoolCodeNum).apply();
    }

    /**
     * 获取学校编号
     */
    public String getSchoolCodeNum() {
        return sharedPreferences.getString(SharedPreferencesKey.APP_SCHOOL_CODE_NUM, "");
    }

    /**
     * 设置学校名称
     */
    public void setSchoolName(final String schoolName) {
        sharedPreferences.edit().putString(SharedPreferencesKey.APP_SCHOOL_NAME, schoolName).apply();
    }

    /**
     * 获取学校名称
     */
    public String getSchoolName() {
        return sharedPreferences.getString(SharedPreferencesKey.APP_SCHOOL_NAME, "");
    }

    /**
     * 设置班级编号
     */
    public void setClassCodeNum(final String classCodeNum) {
        sharedPreferences.edit().putString(SharedPreferencesKey.APP_CLASS_CODE_NUM, classCodeNum).apply();
    }

    /**
     * 获取班级编号
     */
    public String getClassCodeNum() {
        return sharedPreferences.getString(SharedPreferencesKey.APP_CLASS_CODE_NUM, "");
    }

    /**
     * 设置班级名称
     */
    public void getClassName(final String className) {
        sharedPreferences.edit().putString(SharedPreferencesKey.APP_CLASS_NAME, className).apply();
    }

    /**
     * 获取班级名称
     */
    public String getClassName() {
        return sharedPreferences.getString(SharedPreferencesKey.APP_CLASS_NAME, "");
    }

    /**
     * 设置班级年级
     */
    public void setClassGrade(final int classGrade) {
        sharedPreferences.edit().putInt(SharedPreferencesKey.APP_CLASS_GRADE, classGrade).apply();
    }

    /**
     * 获取班级年级
     */
    public int getClassGrade() {
        return sharedPreferences.getInt(SharedPreferencesKey.APP_CLASS_GRADE, 0);
    }
}

interface SharedPreferencesKey {
    // 系统
    String TOKEN = "token";
    // 用户
    String USER_ID = "userId"; // 用户id
    String USER_CODE_NUM = "userCodeNum"; // 用户编号
    String USER_NAME = "userName"; // 用户名字
    String USER_REAL_NAME = "userRealName"; // 用户真实姓名
    String USER_PHONE = "userPhone"; // 用户电话
    String USER_AVATAR = "userAvatar"; // 用户头像
    String USER_RESUME = "userResume"; // 个性签名
    String APP_BINDING_QQ = "bindingQQ"; // 绑定QQ
    String APP_BINDING_WECHAT = "bindingWeChat"; // 绑定微信
    String APP_BINDING_SINA = "bindingSina"; // 绑定新浪
    // 登录
    String REMEMBER_PASSWORD = "rememberPassword"; // 是否记住登录用户名和密码
    String LOGIN_PASSWORD = "loginPassword"; // 登录密码
    // 本APP
    String APP_SCHOOL_ID = "schoolId"; // 学校id
    String APP_SCHOOL_CODE_NUM = "schoolCodeNum"; // 学校编号
    String APP_SCHOOL_NAME = "schoolName"; // 学校名称
    String APP_SCHOOL_EMAIL = "email"; // 园长邮箱
    String APP_CLASS_ID = "classId"; // 班级id
    String APP_CLASS_CODE_NUM = "classCodeNum"; // 班级编号
    String APP_CLASS_NAME = "className"; // 班级名称
    String APP_CLASS_GRADE = "classGrade"; // 班级年级
    String APP_INFORM_COUNT = "informCount"; // 未读消息数量
}
