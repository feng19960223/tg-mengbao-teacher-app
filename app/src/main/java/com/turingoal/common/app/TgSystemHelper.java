package com.turingoal.common.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgStringUtil;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.event.EventConsts;
import com.turingoal.mengbao.teacher.event.EventLogger;
import com.umeng.analytics.MobclickAgent;

import java.util.Map;

/**
 * 公用方法
 */
public final class TgSystemHelper {
    private static long exitTime = 0; // 退出系统
    private static final long EXIT_DELAY_TIME = 2000; // 再按一次退出系统，间隔时间

    private TgSystemHelper() {
        throw new Error("工具类不能实例化！");
    }

    /**
     * 再按一次退出系统
     */
    public static void dbClickExit(final Context context) {
        if ((System.currentTimeMillis() - exitTime) > EXIT_DELAY_TIME) {
            TgDialogUtil.showToast(TgApplication.getContext().getString(R.string.dbclick_exit));
            exitTime = System.currentTimeMillis();
        } else {
            MobclickAgent.onKillProcess(context); // 保存统计数据
            TgApplication.clearActivitys(); // 清除Activity栈
            System.exit(0);
        }
    }

    /**
     * 处理跳转
     */
    public static void handleIntent(final String path) {
        EventLogger.logEvent(EventConsts.e_Id_Intent);
        EventLogger.logEvent(EventConsts.e_Id_Intent, EventConsts.e_Key_Intent, path);
        ARouter.getInstance().build(path).navigation();
    }

    /**
     * 处理跳转
     */
    public static void handleIntent(final String path, final Activity mContext, final int requestCode) {
        EventLogger.logEvent(EventConsts.e_Id_Intent);
        EventLogger.logEvent(EventConsts.e_Id_Intent, EventConsts.e_Key_Intent, path);
        ARouter.getInstance().build(path).navigation(mContext, requestCode);
    }

    /**
     * 处理跳转
     */
    public static void handleIntent(final String path, final Activity mContext, final int requestCode, final NavigationCallback callback) {
        EventLogger.logEvent(EventConsts.e_Id_Intent);
        EventLogger.logEvent(EventConsts.e_Id_Intent, EventConsts.e_Key_Intent, path);
        ARouter.getInstance().build(path).navigation(mContext, requestCode, callback);
    }

    /**
     * 处理跳转,带参数过去
     */
    public static void handleIntentWithObj(final String path, final String objName, final Object obj) {
        EventLogger.logEvent(EventConsts.e_Id_Intent);
        EventLogger.logEvent(EventConsts.e_Id_Intent, EventConsts.e_Key_Intent, path);
        ARouter.getInstance().build(path)
                .withObject(objName, obj)
                .navigation();
    }

    /**
     * 处理跳转,带参数过去
     */
    public static void handleIntentWithObj(final String path, final String key, final String value) {
        EventLogger.logEvent(EventConsts.e_Id_Intent);
        EventLogger.logEvent(EventConsts.e_Id_Intent, EventConsts.e_Key_Intent, path);
        ARouter.getInstance().build(path)
                .withString(key, value)
                .navigation();
    }

    /**
     * 处理跳转,带参数过去
     */
    public static void handleIntentWithObj(final String path, final String objName, final Object obj, final Activity mContext, final int requestCode) {
        EventLogger.logEvent(EventConsts.e_Id_Intent);
        EventLogger.logEvent(EventConsts.e_Id_Intent, EventConsts.e_Key_Intent, path);
        ARouter.getInstance().build(path)
                .withObject(objName, obj)
                .navigation(mContext, requestCode);
    }

    /**
     * 处理跳转,带参数过去
     */
    public static void handleIntentWithBundle(final String path, final String objName, final Bundle bundle) {
        EventLogger.logEvent(EventConsts.e_Id_Intent);
        EventLogger.logEvent(EventConsts.e_Id_Intent, EventConsts.e_Key_Intent, path);
        ARouter.getInstance().build(path)
                .withBundle(objName, bundle)
                .navigation();
    }

    /**
     * 处理跳转,带参数过去
     */
    public static void handleIntentWithObj(final String path, final String objName, final Object obj, final Activity mContext, final int requestCode, final NavigationCallback callback) {
        EventLogger.logEvent(EventConsts.e_Id_Intent);
        EventLogger.logEvent(EventConsts.e_Id_Intent, EventConsts.e_Key_Intent, path);
        ARouter.getInstance().build(path)
                .withObject(objName, obj)
                .navigation(mContext, requestCode, callback);
    }

    /**
     * 处理跳转，关闭当前页面
     */
    public static void handleIntentAndFinish(final String path, final Context context) {
        EventLogger.logEvent(EventConsts.e_Id_Intent);
        EventLogger.logEvent(EventConsts.e_Id_Intent, EventConsts.e_Key_Intent, path);
        ARouter.getInstance().build(path).navigation(context, new NavCallback() {
            @Override
            public void onArrival(final Postcard postcard) {
                ((TgBaseActivity) context).defaultFinish(); // 关闭当前页面
            }
        });
    }

    /**
     * 处理跳转，关闭当前页面， 带参数过去
     */
    public static void handleIntentAndFinishWithObj(final String path, final String objName, final Object obj, final TgBaseActivity context) {
        EventLogger.logEvent(EventConsts.e_Id_Intent);
        EventLogger.logEvent(EventConsts.e_Id_Intent, EventConsts.e_Key_Intent, path);
        ARouter.getInstance().build(path).withObject(objName, obj).navigation(context, new NavCallback() {
            @Override
            public void onArrival(final Postcard postcard) {
                context.defaultFinish(); // 关闭当前页面
            }
        });
    }

    /**
     * 获取用户toekn
     */
    public static String getToekn() {
        return TgApplication.getTgUserPreferences().getToken();
    }

    /**
     * 检查token
     */
    public static boolean checkToken(final TgResponseBean result, final Context context) {
        boolean flag = false;
        if (result == null) {
            return false;
        }
        if (result.isTokenValidateResult()) {
            flag = true;
        } else {
            TgDialogUtil.showToast(TgApplication.getContext().getString(R.string.token_expired)); // 弹出错误信息
            if (context != null) {
                logout(context); //注销并跳转到登录页面
            }
        }
        return flag;
    }

    /**
     * 清空用户个人信息
     */
    public static void clearUserInfo() {
        TgApplication.getTgUserPreferences().clear();
    }

    /**
     * 注销
     */
    public static void logout(final Context context) {
        clearUserInfo(); // 清空用户个人信息
        TgApplication.clearActivitys(); // 清空activiti堆栈
        TgSystemHelper.handleIntentAndFinish(ConstantActivityPath.LOGIN, context); // 跳转到登录页面
    }

    /**
     * 获取登录信息
     */
    public static void setUserInfo(final TgResponseBean result) {
        Map<String, Object> map = (Map<String, Object>) result.getData();
        String token = (String) map.get("token");
        TgApplication.getTgUserPreferences().setToken(token); // 存储token
        String userId = (String) map.get("userId");
        TgApplication.getTgUserPreferences().setUserId(userId); // 存储userId
        String userName = (String) map.get("username");
        TgApplication.getTgUserPreferences().setUsername(userName); // 存储username
        String realname = (String) map.get("realname");
        TgApplication.getTgUserPreferences().setRealname(realname); // 存储realname
        String cellphoneNumber = (String) map.get("cellphoneNum");
        TgApplication.getTgUserPreferences().setPhone(cellphoneNumber); // 存储cellphoneNumber
        String avatar = (String) map.get("avatar");
        TgApplication.getTgUserPreferences().setAvatar(avatar); // 存储用户头像
        String resume = (String) map.get("resume");
        TgApplication.getTgUserPreferences().setResume(resume); // 存储个性签名
        boolean isBindingQQ = !TgStringUtil.isEmpty((String) map.get("uidQQ"));
        TgApplication.getTgUserPreferences().setBindingQQ(isBindingQQ); // 存储是否绑定QQ
        boolean isBindingWeChat = !TgStringUtil.isEmpty((String) map.get("uidWeChat"));
        TgApplication.getTgUserPreferences().setBindingWeChat(isBindingWeChat); // 存储是否绑定微信
        boolean isBindingSina = !TgStringUtil.isEmpty((String) map.get("uidSina"));
        TgApplication.getTgUserPreferences().setBindingSina(isBindingSina); // 存储是否绑定新浪
        String schoolId = (String) map.get("schoolId");
        TgApplication.getTgUserPreferences().setSchoolId(schoolId); // 存储学校id
        String schoolEmail = (String) map.get("schoolEmail");
        TgApplication.getTgUserPreferences().setSchoolEmail(schoolEmail); // 存储园长邮箱
        String classId = (String) map.get("classId");
        TgApplication.getTgUserPreferences().setClassId(classId); // 存储班级id
        int informCount = (Integer) map.get("informCount");
        TgApplication.getTgUserPreferences().setInformCount(informCount); // 存储未读消息数
    }

    /**
     * 获取用户id
     */
    public static String getUserId() {
        return TgApplication.getTgUserPreferences().getUserId();
    }

    /**
     * 获取用户编号
     */
    public static String getUserCodeNum() {
        return TgApplication.getTgUserPreferences().getUserCodeNum();
    }

    /**
     * 获取用户名
     */
    public static String getUsername() {
        return TgApplication.getTgUserPreferences().getUsername();
    }

    /**
     * 获取真实姓名
     */
    public static String getRealname() {
        return TgApplication.getTgUserPreferences().getRealname();
    }

    /**
     * 获取手机号
     */
    public static String getPhone() {
        return TgApplication.getTgUserPreferences().getPhone();
    }

    /**
     * 获取头像
     */
    public static String getAvatar() {
        return TgApplication.getTgUserPreferences().getAvatar();
    }

    /**
     * 获取个性签名
     */
    public static String getResume() {
        return TgApplication.getTgUserPreferences().getResume();
    }

    /**
     * 是否记住登录用户名和密码
     */
    public static boolean isRememberPassword() {
        return TgApplication.getTgUserPreferences().isRememberPassword();
    }

    /**
     * 登录密码
     */
    public static String getLoginPassword() {
        return TgApplication.getTgUserPreferences().getLoginPassword();
    }

    /**
     * 是否绑定QQ
     */
    public static boolean isBindingQQ() {
        return TgApplication.getTgUserPreferences().isBindingQQ();
    }

    /**
     * 是否绑定微信
     */
    public static boolean isBindingWeChat() {
        return TgApplication.getTgUserPreferences().isBindingWeChat();
    }

    /**
     * 是否绑定新浪
     */
    public static boolean isBindingSina() {
        return TgApplication.getTgUserPreferences().isBindingSina();
    }

    /**
     * 获取学校id
     */
    public static String getSchoolId() {
        return TgApplication.getTgUserPreferences().getSchoolId();
    }

    /**
     * 获取学校编号
     */
    public static String getSchoolCodeNum() {
        return TgApplication.getTgUserPreferences().getSchoolCodeNum();
    }

    /**
     * 获取学校编号
     */
    public static String getSchoolName() {
        return TgApplication.getTgUserPreferences().getSchoolName();
    }

    /**
     * 获取班级id
     */
    public static String getClassId() {
        return TgApplication.getTgUserPreferences().getClassId();
    }

    /**
     * 获取园长邮箱
     */
    public static String getSchoolEmail() {
        return TgApplication.getTgUserPreferences().getSchoolEmail();
    }

    /**
     * 获取未读消息数量
     */
    public static int getInformCount() {
        return TgApplication.getTgUserPreferences().getInformCount();
    }

    /**
     * 获取班级编号
     */
    public static String getClassCodeNum() {
        return TgApplication.getTgUserPreferences().getClassCodeNum();
    }

    /**
     * 获取班级名称
     */
    public static String getClassName() {
        return TgApplication.getTgUserPreferences().getClassName();
    }

    /**
     * 获取班级年级
     */
    public static int getClassGrade() {
        return TgApplication.getTgUserPreferences().getClassGrade();
    }
}