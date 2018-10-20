package com.turingoal.common.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.turingoal.mengbao.teacher.BuildConfig;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * 主应用
 */
public class TgApplication extends Application {
    private static Context context; //context
    private static int screenWidth; // 屏幕宽度
    private static int screenHeight; // 屏幕高度
    private static int dialogWidth; // 对话框宽度
    private static int dialogHeight; // 对话框高度
    private static TgUserPreferences userPreferences; // 用户数据存储
    private static ArrayList<Activity> signAtyList = new ArrayList<>(); // activity堆栈
    private static List<Activity> forgetAtyList = new ArrayList<>(); // 忘记密码activity堆栈
    private static List<Activity> changeAtyList = new ArrayList<>(); // 修改手机账号activity堆栈

    //各个平台的配置，建议放在全局Application或者程序入口
    {
        PlatformConfig.setAlipay("2018010801687492");
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setQQZone("1106655366", "qSuFDZUwscHH0L1d");
        PlatformConfig.setSinaWeibo("901979587", "a7dc505b5f857fa633d65cd2f850fe16", "http://sns.whalecloud.com");
    }

    @Override
    protected void attachBaseContext(final Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext(); //context
        Utils.init(this); // 初始化Utils工具类
        appInit(); // 初始化 app
        initLogger(); // 初始化Logger
        initARouter(); // 初始化ARouter
        initOkGo(); // 初始化OKhttp OkGO
        initUmeng();
    }

    private static final String APP_KEY = "5a4c7610f29d98689800015b";
    private static final String UMENG_MESSAGE_SECRET = "655b63094356aa005432a8203120608e";

    private void initUmeng() {
        // 初始化common库
        // 即使用户已经在项目AndroidManifest.xml清单配置中设置了appkey和channel,
        // 新版组件化SDK还是要求必须在项目工程的自定义application中的onCreate方法中调用初始化函数UMConfigure.init，
        // 已经在AndroidManifest.xml清单配置中设置了appkey和channel参数的用户，初始化函数中appkey和channel参数可以传入null。
        // 参数1:上下文，不能为空
        // 参数2:友盟 app key
        // 参数3:友盟 channel，多渠道打包
        // 参数4:设备类型，必须参数，传参数为UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
        // 参数5:Push推送业务的secret，需要集成Push功能时必须传入Push的secret，否则传空
        UMConfigure.init(this, APP_KEY, "", UMConfigure.DEVICE_TYPE_PHONE, UMENG_MESSAGE_SECRET);
        // 设置组件化的Log开关
        // 参数: boolean 默认为false，如需查看LOG设置为true
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        //开启ShareSDK debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        Config.DEBUG = BuildConfig.DEBUG;
        // push
        // 注册推送服务，每次调用register方法都会回调该接口
        PushAgent.getInstance(this).register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(final String deviceToken) {
                Log.d("UMeng*****", "onSuccess: " + deviceToken);
            }

            @Override
            public void onFailure(final String s, final String s1) {
                Log.d("UMeng*****", "onSuccess: " + s + "****" + s1);
            }
        });
    }

    /**
     * 系统初始化配置
     */
    private void appInit() {
        userPreferences = new TgUserPreferences(this); // 初始化 TgUserPreferences
        screenWidth = this.getResources().getDisplayMetrics().widthPixels; // 屏幕宽度
        screenHeight = this.getResources().getDisplayMetrics().heightPixels; // 屏幕高度
        dialogWidth = screenWidth * 9 / 10;  // 对话框宽度
        dialogHeight = screenHeight * 2 / 3; // 对话框高度
    }

    /**
     * 初始化Logger
     */
    private void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false) // 显示线程信息
                .tag(TgSystemConfig.LOG_TAG)   // LOG_TAG
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {

            @Override
            public boolean isLoggable(final int priority, final String tag) {
                return BuildConfig.DEBUG; // 是否debug模式
            }
        });
    }

    /**
     * 初始化ARouter
     */
    private void initARouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog();     // ARouter打印日志
            ARouter.openDebug();   // ARouter开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 初始化ARouter
    }

    /**
     * 初始化OKhttp OkGo
     */
    private void initOkGo() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(TgSystemConfig.LOG_TAG); //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY); //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);  //全局的读取超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS); //全局的写入超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS); //全局的连接超时时间
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this))); //使用sp保持cookie，如果cookie不过期，则一直有效
        OkGo.getInstance().init(this)                         //必须调用初始化
                .setOkHttpClient(builder.build())             //必须设置OkHttpClient
                .setCacheMode(CacheMode.NO_CACHE)             //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE) //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3);                            //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0;                       //全局公共参数
    }

    /**
     * 添加 Activity 到堆栈
     */
    public static void addActivity(final Activity activity) {
        if (signAtyList != null) {
            boolean contains = signAtyList.contains(activity);
            if (contains) {
                signAtyList.remove(activity);
            }
            signAtyList.add(activity);
        }
    }

    /**
     * 从堆栈删除Activity
     */
    public static void deleteActivity(final Activity activity) {
        if (signAtyList != null) {
            boolean contains = signAtyList.contains(activity);
            if (contains) {
                signAtyList.remove(activity);
            }
        }
    }

    /**
     * 从堆栈finish Activity
     */
    public static void finishActivity(final Class<?> cls) {
        for (Activity activity : signAtyList) {
            if (activity.getClass().equals(cls)) {
                activity.finish();
            }
        }
    }

    /**
     * 清空Activity堆栈
     */
    public static void clearActivitys() {
        if (signAtyList != null) {
            final int size = signAtyList.size();
            for (int i = 0; i < size; i++) {
                Activity aty = signAtyList.get(i);
                if (aty != null) {
                    aty.finish();
                }
            }
        }
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth() {
        return screenWidth;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight() {
        return screenHeight;
    }

    /**
     * 获取对话框宽度
     */
    public static int getDialogWidth() {
        return dialogWidth;
    }

    /**
     * 获取对话框高度
     */
    public static int getDialogHeight() {
        return dialogHeight;
    }

    /**
     * getContext
     */
    public static Context getContext() {
        return context;
    }

    /**
     * getTgUserPreferences
     */
    public static TgUserPreferences getTgUserPreferences() {
        return userPreferences;
    }

    /**
     * 添加 Activity 到堆栈
     */
    public static void addForgetActivity(final Activity activity) {
        if (forgetAtyList != null) {
            boolean contains = forgetAtyList.contains(activity);
            if (contains) {
                forgetAtyList.remove(activity);
            }
            forgetAtyList.add(activity);
        }
    }

    /**
     * 从堆栈删除Activity
     */
    public static void deleteForgetActivity(final Activity activity) {
        if (forgetAtyList != null) {
            boolean contains = forgetAtyList.contains(activity);
            if (contains) {
                forgetAtyList.remove(activity);
            }
        }
    }

    /**
     * 清空Activity堆栈
     */
    public static void clearForgetActivitys() {
        if (forgetAtyList != null) {
            final int size = forgetAtyList.size();
            for (int i = 0; i < size; i++) {
                Activity aty = forgetAtyList.get(i);
                if (aty != null) {
                    aty.finish();
                }
            }
        }
    }

    /**
     * 添加 Activity 到堆栈
     */
    public static void addChangeActivity(final Activity activity) {
        if (changeAtyList != null) {
            boolean contains = changeAtyList.contains(activity);
            if (contains) {
                changeAtyList.remove(activity);
            }
            changeAtyList.add(activity);
        }
    }

    /**
     * 从堆栈删除Activity
     */
    public static void deleteChangeActivity(final Activity activity) {
        if (changeAtyList != null) {
            boolean contains = changeAtyList.contains(activity);
            if (contains) {
                changeAtyList.remove(activity);
            }
        }
    }

    /**
     * 清空Activity堆栈
     */
    public static void clearChangeActivitys() {
        if (changeAtyList != null) {
            final int size = changeAtyList.size();
            for (int i = 0; i < size; i++) {
                Activity aty = changeAtyList.get(i);
                if (aty != null) {
                    aty.finish();
                }
            }
        }
    }
}
