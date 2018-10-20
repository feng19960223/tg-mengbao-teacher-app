package com.turingoal.common.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.turingoal.mengbao.teacher.R;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期时间选择控件 使用方法： private EditText inputDate; //需要设置的日期时间文本编辑框 private String
 */
public class TgDateTimePickUtil {
    private DatePicker datePicker;
    private TimePicker timePicker;
    private AlertDialog ad;
    private Date initDateTime; // 初始时间
    private Date minDate; // 最小时间
    private Date maxDate; // 最大时间
    private Activity activity;
    private String title; // 标题

    /**
     * 日期时间弹出选择框构造函数
     */
    public TgDateTimePickUtil(final Activity activityParm, final Date initDateTimeParm, final Date minDateParm, final Date maxDateParm, final String titleParm) {
        this.activity = activityParm;
        if (initDateTimeParm != null) {
            this.initDateTime = initDateTimeParm;
        } else {
            this.initDateTime = new Date();
        }
        this.minDate = minDateParm;
        this.maxDate = maxDateParm;
        this.title = titleParm;
    }

    /**
     * 初始化
     */
    public void initDatePicker() {
        Calendar calendar = Calendar.getInstance();
        if (initDateTime != null) {
            calendar.setTime(initDateTime);
        }
        datePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), null);
        datePicker.setFirstDayOfWeek(android.icu.util.Calendar.MONDAY); //设置周一为第一天
        if (minDate != null) {
            datePicker.setMinDate(minDate.getTime()); // 设置最小时间
        }
        if (maxDate != null) {
            datePicker.setMaxDate(maxDate.getTime()); // 设置最大时间
        }
    }

    /**
     * 弹出日期时间选择框方法
     */
    public DatePicker dateTimePickDialog(final OnSubmitSelectDateListener submitLitener) {
        LinearLayout dateTimeLayout = (LinearLayout) activity
                .getLayoutInflater().inflate(R.layout.common_view_datetime, null);
        datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
        initDatePicker(); // 初始化
        ad = new AlertDialog.Builder(activity).setView(dateTimeLayout)
                .setPositiveButton("确 定", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int whichButton) {
                        Calendar calendar = Calendar.getInstance(); // 获得日历实例
                        calendar.set(datePicker.getYear(), datePicker.getMonth(),
                                datePicker.getDayOfMonth());
                        Date date = calendar.getTime();
                        submitLitener.onSumbitSelect(date);
                    }
                })
                .setNegativeButton("取 消", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int whichButton) {
                    }
                }).create();
        if (!TextUtils.isEmpty(title)) {
            ad.setTitle(title);
        }
        ad.show();
        return datePicker;
    }

    /**
     * 确定选择日期
     */
    public interface OnSubmitSelectDateListener {
        /**
         * 确定选择
         */
        void onSumbitSelect(Date date);
    }
}
