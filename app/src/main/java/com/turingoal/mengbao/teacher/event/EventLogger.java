package com.turingoal.mengbao.teacher.event;

import com.turingoal.common.app.TgApplication;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * 打点统计
 */

public class EventLogger {

    /**
     * map打点
     */
    public static void logEvent(final String eventId, final Map<String, String> map) {
        MobclickAgent.onEvent(TgApplication.getContext(), eventId, map);
    }

    /**
     * 只有一个key和value的打点
     */
    public static void logEvent(final String eventId, final String key, final String value) {
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        MobclickAgent.onEvent(TgApplication.getContext(), eventId, map);
    }

    /**
     * dankey打点
     */
    public static void logEvent(final String eventId) {
        MobclickAgent.onEvent(TgApplication.getContext(), eventId);
    }
}
