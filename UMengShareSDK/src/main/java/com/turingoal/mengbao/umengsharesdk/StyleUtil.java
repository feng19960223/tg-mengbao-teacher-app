package com.turingoal.mengbao.umengsharesdk;

import android.app.Activity;

import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

/**
 * UMeng各分享应用支持的分享类型判断
 */

public class StyleUtil {
    public static final String TEXT = "纯文本";
    public static final String IMAGELOCAL = "纯图片本地";
    public static final String IMAGEURL = "纯图片http";
    public static final String TEXTANDIMAGE = "图文";
    public static final String MUSIC00 = "音乐（无标题，无内容）";
    public static final String MUSIC01 = "音乐（无标题，有内容）";
    public static final String MUSIC10 = "音乐（有标题，无内容）";
    public static final String MUSIC11 = "音乐（有标题，有内容）";
    public static final String VIDEO00 = "视频（无标题，无内容）";
    public static final String VIDEO01 = "视频（无标题，有内容）";
    public static final String VIDEO10 = "视频（有标题，无内容）";
    public static final String VIDEO11 = "视频（有标题，有内容）";
    public static final String WEB00 = "链接（无标题，无内容）";
    public static final String WEB01 = "链接（无标题，有内容）";
    public static final String WEB10 = "链接（有标题，无内容）";
    public static final String WEB11 = "链接（有标题，有内容）";
    public static final String EMOJI = "微信表情";
    public static final String FILE = "文件";
    public static final String MINAPP = "小程序（测试）";

    /**
     * 得到支持style并且安裝了app的SHARE_MEDIA数组
     */
    public static SHARE_MEDIA[] getShareMedia(final Activity context, final String style) {
        // 自己规定顺序
        List<SHARE_MEDIA> shareMediaList = new ArrayList<>();
        shareMediaList.add(SHARE_MEDIA.WEIXIN); // 微信
        shareMediaList.add(SHARE_MEDIA.WEIXIN_CIRCLE); // 微信朋友圈
        shareMediaList.add(SHARE_MEDIA.WEIXIN_FAVORITE); // 微信收藏
        shareMediaList.add(SHARE_MEDIA.QQ); // QQ
        shareMediaList.add(SHARE_MEDIA.ALIPAY); // 支付宝
        shareMediaList.add(SHARE_MEDIA.QZONE); // QQ空间
        // shareMediaList.add(SHARE_MEDIA.EVERNOTE); // 印象笔记
        // shareMediaList.add(SHARE_MEDIA.YNOTE); // 有道云笔记
        // shareMediaList.add(SHARE_MEDIA.DINGTALK); // 钉钉
        // shareMediaList.add(SHARE_MEDIA.LINKEDIN); // 领英
        // shareMediaList.add(SHARE_MEDIA.LAIWANG); // 点点虫，阿里巴巴交友软件，来往
        // shareMediaList.add(SHARE_MEDIA.LAIWANG_DYNAMIC); // 点点虫动态
        // shareMediaList.add(SHARE_MEDIA.YIXIN); // 易信 程序伪崩潰
        // shareMediaList.add(SHARE_MEDIA.YIXIN_CIRCLE); //易信朋友圈 程序伪崩潰
        // shareMediaList.add(SHARE_MEDIA.FACEBOOK); // facebook脸书，外国
        // shareMediaList.add(SHARE_MEDIA.FACEBOOK_MESSAGER); // facebook message
        // shareMediaList.add(SHARE_MEDIA.TWITTER); // 推特，外国
        // shareMediaList.add(SHARE_MEDIA.INSTAGRAM); // 照片墙，外国
        // shareMediaList.add(SHARE_MEDIA.GOOGLEPLUS); // GooglePlus,外国
        // shareMediaList.add(SHARE_MEDIA.PINTEREST); // 拼趣，外国
        // shareMediaList.add(SHARE_MEDIA.POCKET); // 阅读器，外国
        // shareMediaList.add(SHARE_MEDIA.FOURSQUARE); // 地理位置，外国
        // shareMediaList.add(SHARE_MEDIA.WHATSAPP); // WhatsApp,外国
        // shareMediaList.add(SHARE_MEDIA.LINE); // 连我,外国
        // shareMediaList.add(SHARE_MEDIA.FLICKR); // 雅虎网络相册，外国
        // shareMediaList.add(SHARE_MEDIA.TUMBLR); // 汤不热，全球最大的轻博客，外国
        // shareMediaList.add(SHARE_MEDIA.KAKAO); // 免费聊天，外国
        // shareMediaList.add(SHARE_MEDIA.DROPBOX); // 网络文件同步工具，外国
        // shareMediaList.add(SHARE_MEDIA.VKONTAKTE); // vk,俄罗斯交友软件，外国
        // shareMediaList.add(SHARE_MEDIA.GENERIC); // 未知
        List<SHARE_MEDIA> instalList = new ArrayList<>();
        for (SHARE_MEDIA shareMedia : shareMediaList) {
            // 判断是否安装了该app
            if (UMShareAPI.get(context).isInstall(context, shareMedia)) {
                instalList.add(shareMedia);
            }
        }
        // 不需要判断是否安裝应用
        instalList.add(0, SHARE_MEDIA.SINA); // 新浪微信QQ属于分享第一队列
        // instalList.add(SHARE_MEDIA.TENCENT); // 腾讯微博
        // instalList.add(SHARE_MEDIA.RENREN); // 人人网，不需要安装也可以分享
        // instalList.add(SHARE_MEDIA.DOUBAN); // 豆瓣，不需要安装也可以分享
        // 无法不用判断是否安装的分享平台
        instalList.add(SHARE_MEDIA.SMS); // 短信
        instalList.add(SHARE_MEDIA.EMAIL); // 邮箱
        instalList.add(SHARE_MEDIA.MORE); // 更多
        List<SHARE_MEDIA> allowList = new ArrayList<>();
        for (SHARE_MEDIA shareMedia : instalList) {
            // 是否支持该类型
            if (isAllowStyle(shareMedia, style)) {
                allowList.add(shareMedia);
            }
        }
        // 返回SHARE_MEDIA数组
        return allowList.toArray(new SHARE_MEDIA[allowList.size()]);
    }

    /**
     * shareMedia是否支持style类型的分享
     */
    public static boolean isAllowStyle(final SHARE_MEDIA shareMedia, final String style) {
        List<String> styles = new ArrayList<>();
        // 设置当前SHARE_MEDIA所支持的类型
        if (shareMedia == SHARE_MEDIA.QQ) {
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.TEXTANDIMAGE);
            styles.add(StyleUtil.MUSIC00);
            styles.add(StyleUtil.MUSIC01);
            styles.add(StyleUtil.MUSIC10);
            styles.add(StyleUtil.MUSIC11);
            styles.add(StyleUtil.VIDEO00);
            styles.add(StyleUtil.VIDEO01);
            styles.add(StyleUtil.VIDEO10);
            styles.add(StyleUtil.VIDEO11);
            styles.add(StyleUtil.WEB00);
            styles.add(StyleUtil.WEB01);
            styles.add(StyleUtil.WEB10);
            styles.add(StyleUtil.WEB11);
        } else if (shareMedia == SHARE_MEDIA.QZONE) {
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.TEXTANDIMAGE);
            styles.add(StyleUtil.MUSIC00);
            styles.add(StyleUtil.MUSIC01);
            styles.add(StyleUtil.MUSIC10);
            styles.add(StyleUtil.MUSIC11);
            styles.add(StyleUtil.VIDEO00);
            styles.add(StyleUtil.VIDEO01);
            styles.add(StyleUtil.VIDEO10);
            styles.add(StyleUtil.VIDEO11);
            styles.add(StyleUtil.WEB00);
            styles.add(StyleUtil.WEB01);
            styles.add(StyleUtil.WEB10);
            styles.add(StyleUtil.WEB11);
        } else if (shareMedia == SHARE_MEDIA.SINA) {
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.TEXTANDIMAGE);
            styles.add(StyleUtil.MUSIC00);
            styles.add(StyleUtil.MUSIC01);
            styles.add(StyleUtil.MUSIC10);
            styles.add(StyleUtil.MUSIC11);
            styles.add(StyleUtil.VIDEO00);
            styles.add(StyleUtil.VIDEO01);
            styles.add(StyleUtil.VIDEO10);
            styles.add(StyleUtil.VIDEO11);
            styles.add(StyleUtil.WEB00);
            styles.add(StyleUtil.WEB01);
            styles.add(StyleUtil.WEB10);
            styles.add(StyleUtil.WEB11);
        } else if (shareMedia == SHARE_MEDIA.WEIXIN) {
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.TEXTANDIMAGE);
            styles.add(StyleUtil.MUSIC00);
            styles.add(StyleUtil.MUSIC01);
            styles.add(StyleUtil.MUSIC10);
            styles.add(StyleUtil.MUSIC11);
            styles.add(StyleUtil.VIDEO00);
            styles.add(StyleUtil.VIDEO01);
            styles.add(StyleUtil.VIDEO10);
            styles.add(StyleUtil.VIDEO11);
            styles.add(StyleUtil.WEB00);
            styles.add(StyleUtil.WEB01);
            styles.add(StyleUtil.WEB10);
            styles.add(StyleUtil.WEB11);
            styles.add(StyleUtil.EMOJI);
            styles.add(StyleUtil.MINAPP);
        } else if (shareMedia == SHARE_MEDIA.WEIXIN_CIRCLE) {
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.TEXTANDIMAGE);
            styles.add(StyleUtil.MUSIC00);
            styles.add(StyleUtil.MUSIC01);
            styles.add(StyleUtil.MUSIC10);
            styles.add(StyleUtil.MUSIC11);
            styles.add(StyleUtil.VIDEO00);
            styles.add(StyleUtil.VIDEO01);
            styles.add(StyleUtil.VIDEO10);
            styles.add(StyleUtil.VIDEO11);
            styles.add(StyleUtil.WEB00);
            styles.add(StyleUtil.WEB01);
            styles.add(StyleUtil.WEB10);
            styles.add(StyleUtil.WEB11);
        } else if (shareMedia == SHARE_MEDIA.WEIXIN_FAVORITE) {
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.TEXTANDIMAGE);
            styles.add(StyleUtil.MUSIC00);
            styles.add(StyleUtil.MUSIC01);
            styles.add(StyleUtil.MUSIC10);
            styles.add(StyleUtil.MUSIC11);
            styles.add(StyleUtil.VIDEO00);
            styles.add(StyleUtil.VIDEO01);
            styles.add(StyleUtil.VIDEO10);
            styles.add(StyleUtil.VIDEO11);
            styles.add(StyleUtil.WEB00);
            styles.add(StyleUtil.WEB01);
            styles.add(StyleUtil.WEB10);
            styles.add(StyleUtil.WEB11);
            styles.add(StyleUtil.FILE);
        } else if (shareMedia == SHARE_MEDIA.ALIPAY) {
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.TEXTANDIMAGE);
            styles.add(StyleUtil.WEB11);
            styles.add(StyleUtil.WEB00);
            styles.add(StyleUtil.WEB10);
            styles.add(StyleUtil.WEB01);
            styles.add(StyleUtil.MUSIC11);
            styles.add(StyleUtil.MUSIC00);
            styles.add(StyleUtil.MUSIC10);
            styles.add(StyleUtil.MUSIC01);
            styles.add(StyleUtil.VIDEO11);
            styles.add(StyleUtil.VIDEO00);
            styles.add(StyleUtil.VIDEO10);
            styles.add(StyleUtil.VIDEO01);
        } else if (shareMedia == SHARE_MEDIA.EMAIL) {
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.TEXTANDIMAGE);
            styles.add(StyleUtil.WEB11);
            styles.add(StyleUtil.WEB00);
            styles.add(StyleUtil.WEB10);
            styles.add(StyleUtil.WEB01);
        } else if (shareMedia == SHARE_MEDIA.SMS) {
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.TEXTANDIMAGE);
        } else if (shareMedia == SHARE_MEDIA.MORE) {
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.TEXTANDIMAGE);
        }
        // 如果支持该类型
        for (String styleStr : styles) {
            if (style.equals(styleStr)) {
                return true;
            }
        }
        return false;
    }
}
