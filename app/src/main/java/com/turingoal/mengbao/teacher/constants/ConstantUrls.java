package com.turingoal.mengbao.teacher.constants;

import com.turingoal.common.app.TgSystemConfig;

/**
 * 网络请求url路径
 */
public interface ConstantUrls {
    String URL_TOKEN_CHECK = TgSystemConfig.getServerBaseUrl() + "/tg/app/checkToken.app"; // 校验token 欢迎

    String URL_LOGIN = TgSystemConfig.getServerBaseUrl() + "/tg/app/login.app"; // 登录 username+password

    String URL_SDK_QQ = TgSystemConfig.getServerBaseUrl() + "/tg/app/sdkQQ.app"; // 是否上来就QQ登录
    String URL_SDK_SINA = TgSystemConfig.getServerBaseUrl() + "/tg/app/sdkSina.app"; // 是否上来就新浪登录
    String URL_SDK_WECHAT = TgSystemConfig.getServerBaseUrl() + "/tg/app/sdkWeChat.app"; // 是否上来就微信登录

    String URL_SDK_BINDING_USER_QQ = TgSystemConfig.getServerBaseUrl() + "/tg/app/sdkBindingUserQQ.app"; // 上来就QQ登录，绑定账号
    String URL_SDK_BINDING_USER_SINA = TgSystemConfig.getServerBaseUrl() + "/tg/app/sdkBindingUserSina.app"; // 上来就新浪登录，绑定账号
    String URL_SDK_BINDING_USER_WECHAT = TgSystemConfig.getServerBaseUrl() + "/tg/app/sdkBindingUserWeChat.app"; // 上来就微信登录，绑定账号

    String URL_LOGIN_UID_QQ = TgSystemConfig.getServerBaseUrl() + "/tg/app/loginQQ.app"; // 登录 QQuid
    String URL_LOGIN_UID_SINA = TgSystemConfig.getServerBaseUrl() + "/tg/app/loginSina.app"; // 登录 新浪uid
    String URL_LOGIN_UID_WECHAT = TgSystemConfig.getServerBaseUrl() + "/tg/app/loginWeChat.app"; // 登录 微信uid

    String URL_VERIFICATION_PASSWORD = TgSystemConfig.getServerBaseUrl() + "/tg/app/verificationPassword.app"; // 发送验证码,验证是否存在此用户的手机号,找回密码,模板1
    String URL_VERIFICATION_PHONE = TgSystemConfig.getServerBaseUrl() + "/tg/app/verificationPhone.app"; // 发送验证码,验证是否存在此用户的手机号,修改手机号，模板2

    String URL_PASSWORD_VERIFICATION = TgSystemConfig.getServerBaseUrl() + "/tg/app/PhoneVerification.app"; // 校验验证码是否正确phone+verification
    String URL_PASSWORD_PHONE = TgSystemConfig.getServerBaseUrl() + "/tg/app/PhonePassword.app"; // 直接设置新密码phone+password
    String URL_USERID_PHONE_VERIFICATION = TgSystemConfig.getServerBaseUrl() + "/tg/app/UserIdPhoneVerification.app"; // 修改新手机号userId+phone+verification
    String URL_CHANGE_PASS = TgSystemConfig.getServerBaseUrl() + "/tg/app/changePass.app"; // 修改密码 个人信息用

    String URL_USER_BINDING_SDK_QQ = TgSystemConfig.getServerBaseUrl() + "/tg/app/userBindingSdkQQ.app"; // 账号绑定QQ
    String URL_USER_BINDING_SDK_SINA = TgSystemConfig.getServerBaseUrl() + "/tg/app/userBindingSdkSina.app"; // 账号绑定新浪
    String URL_USER_BINDING_SDK_WECHAT = TgSystemConfig.getServerBaseUrl() + "/tg/app/userBindingSdkWeChat.app"; //账号绑定微信

    String URL_COURSES = TgSystemConfig.getServerBaseUrl() + "/tg/app/curriculum/findWeeks.app"; // 课表 1
    String URL_COOKBOOK = TgSystemConfig.getServerBaseUrl() + "/tg/app/cookbook/findWeeks.app"; // 食谱 1
    String URL_ADDRESS_BOOK = TgSystemConfig.getServerBaseUrl() + "/tg/app/addressBook/findAddressBook.app"; // 通讯录 1
    String URL_ATTENDANCE = TgSystemConfig.getServerBaseUrl() + "/tg/app/attendance/findByClass.app"; // 某天考勤 1
    String URL_ATTENDANCE_CHILD = TgSystemConfig.getServerBaseUrl() + "/tg/app/child/findByClass.app"; // 考勤全班级的孩子 1
    String URL_ATTENDANCE_ADD = TgSystemConfig.getServerBaseUrl() + "/tg/app/attendance/add.app"; // 增加考勤 1
    String URL_ALBUM = TgSystemConfig.getServerBaseUrl() + "/tg/app/album/findByClass.app"; // 相册 1
    String URL_ALBUM_ADD = TgSystemConfig.getServerBaseUrl() + "/tg/app/album/add.app"; // 增加相册 1
    String URL_ALBUM_DELETE = TgSystemConfig.getServerBaseUrl() + "/tg/app/album/delete.app"; // 删除相册 1
    String URL_ALBUM_TITLE_RENAME = TgSystemConfig.getServerBaseUrl() + "/tg/app/album/update.app"; // 相册title重命名 1
    String URL_ALBUM_DESCRIPTION_RENAME = TgSystemConfig.getServerBaseUrl() + "/tg/app/album/update.app"; // 相册description重命名 1
    String URL_ALBUM_COVER = TgSystemConfig.getServerBaseUrl() + "/tg/app/album/updateCoverImage.app"; // 相册修改封面 1
    String URL_ALBUM_GROUP = TgSystemConfig.getServerBaseUrl() + "/tg/app/albumGroup/findByAlbum.app"; // 相册组 1
    String URL_ALBUM_GROUP_ADD = TgSystemConfig.getServerBaseUrl() + "/tg/app/albumGroup/add.app"; // 增加照片组 1
    String URL_ALBUM_PHOTO_DELETE = TgSystemConfig.getServerBaseUrl() + "/tg/app/albumPhoto/delete.app"; // 删除照片 1
    String URL_SCHOOL_PHOTO = TgSystemConfig.getServerBaseUrl() + "/tg/app/schoolPhoto/findBySchool.app"; // 学校风采照片 1
    String URL_RANK = TgSystemConfig.getServerBaseUrl() + "/tg/app/rank/findRank.app"; // 班级排行榜 1
    String URL_LOVE_REMARK = TgSystemConfig.getServerBaseUrl() + "/tg/app/loveRemarks/findByClass.app"; // 爱心备注 1
    String URL_LOVE_RECEIVED = TgSystemConfig.getServerBaseUrl() + "/tg/app/loveRemarks/update.app"; // 收到爱心备注 1
    String URL_MESSAGE = TgSystemConfig.getServerBaseUrl() + "/tg/app/message/findByUser.app"; // 消息 1
    String URL_MESSAGE_DELETE = TgSystemConfig.getServerBaseUrl() + "/tg/app/message/delete.app"; // 删除消息 1
    String URL_INFORM = TgSystemConfig.getServerBaseUrl() + "/tg/app/noticeRecord/findByPage.app"; // 通知 1
    String URL_INFORM_READ = TgSystemConfig.getServerBaseUrl() + "/tg/app/noticeRecord/get.app"; // 读一条通知 1
    String URL_INFORM_COUNT = TgSystemConfig.getServerBaseUrl() + "/tg/app/noticeRecord/getUnReadCount.app"; // 未读通知数 1
    String URL_HOMEWORK = TgSystemConfig.getServerBaseUrl() + "/tg/app/homework/findByUser.app"; // 亲子作业 1
    String URL_HOMEWORK_ADD = TgSystemConfig.getServerBaseUrl() + "/tg/app/homework/add.app"; // 增加亲子作业详情
    String URL_HOMEWORK_RECORD_YES = TgSystemConfig.getServerBaseUrl() + "/tg/app/homeworkRecord/finished.app"; // 完成亲子作业 1
    String URL_HOMEWORK_RECORD_NO = TgSystemConfig.getServerBaseUrl() + "/tg/app/homeworkRecord/unFinished.app"; // 未完成亲子作业 1
    String URL_HOMEWORK_RECORD_FLOWER = TgSystemConfig.getServerBaseUrl() + "/tg/app/homeworkRecord/updateFlower.app"; // 完成亲子作业送花 1
    String URL_HOMEWORK_RECORD_FLOWER_COUNT = TgSystemConfig.getServerBaseUrl() + "/tg/app/homeworkRecord/findCount.app"; // 剩余作业送花数量 1
    String URL_COMMENTS_CHILD = TgSystemConfig.getServerBaseUrl() + "/tg/app/child/findChild.app"; // 得到孩子列表是否点评 1
    String URL_COMMENTS_ADD = TgSystemConfig.getServerBaseUrl() + "/tg/app/comments/add.app"; // 增加一条点评 1
    String URL_COMMENTS = TgSystemConfig.getServerBaseUrl() + "/tg/app/comments/findByChild.app"; // 得到一个孩子的点评历史 1
    String URL_COMMENTS_FLOWER = TgSystemConfig.getServerBaseUrl() + "/tg/app/comments/findCount.app"; // 剩余点评送花数量 1
    String URL_USER_AVATAR = TgSystemConfig.getServerBaseUrl() + "/tg/app/updateAvatar.app"; // 老师头像 1
    String URL_USER_RESUME = TgSystemConfig.getServerBaseUrl() + "/tg/app/updateSignature.app"; // 老师个性签名 1
    String URL_USER_DETAIL = TgSystemConfig.getServerBaseUrl() + "/tg/app/get.app"; // 得到老师信息 1
}
