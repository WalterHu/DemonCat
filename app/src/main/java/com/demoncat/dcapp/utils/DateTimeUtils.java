/**
 * Copyright 2018 hubohua
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.demoncat.dcapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Class: DateTimeUtils
 * @Description: Date time text utils
 * @Author: hubohua
 * @CreateDate: 2018/8/14
 */
public class DateTimeUtils {
    public static String DATE_SECOND_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String DATE_MINUTE_FORMAT = "yyyy-MM-dd HH:mm";
    public static String DATE_HOUR_FORMAT = "yyyy-MM-dd HH";
    public static String DATE_DAY_FORMAT = "yyyy-MM-dd";
    public static String DATE_MONTH_FORMAT = "yyyy-MM";
    public static String DATE_ONLY_DAY_FORMAT = "MM-dd";

    @SuppressLint("SimpleDateFormat")
    public static String getDateTime(String timeFormat) {
        return new SimpleDateFormat(timeFormat).format(new Date());
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateTime(Date date, String timeFormat) {
        return new SimpleDateFormat(timeFormat).format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static String transDateTime(String sourceDate, String fromFormat, String toFormat) {
        try {
            Date date = string2Date(sourceDate, fromFormat);
            return new SimpleDateFormat(toFormat).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Date time string to Date object
     * @param SourceDate
     * @param dateFormat
     * @return
     * @throws ParseException
     */
    public static Date string2Date(String SourceDate, String dateFormat) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.parse(SourceDate);
    }

    /**
     * Get current year
     * @return
     */
    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * Get current month
     * @return
     */
    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    /**
     * Get day of month
     * @return
     */
    public static int getDayOfMonth() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Get current hour
     * @return
     */
    public static int get24Hour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Get current minute
     * @return
     */
    public static int getMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    /**
     * Get date and time prompt
     * @param context
     * @param dateTime
     * @return Default return "更早"
     */
    public static String getSpacingPrompt(Context context, String dateTime) {
        if (TextUtils.isEmpty(dateTime))
            return "";
        if (dateTime.contains(" ")) {
            dateTime = dateTime.substring(0, dateTime.indexOf(" "));
        }
        String nowDate = getDateTime(DATE_DAY_FORMAT);
        int year = Integer.parseInt(nowDate.substring(0, nowDate.indexOf("-")));
        int month = Integer.parseInt(nowDate.substring(nowDate.indexOf("-") + 1, nowDate.lastIndexOf("-")));
        int day = Integer.parseInt(nowDate.substring(nowDate.lastIndexOf("-") + 1));
        int dateYear = Integer.parseInt(dateTime.substring(0, dateTime.indexOf("-")));
        int dateMonth = Integer.parseInt(dateTime.substring(dateTime.indexOf("-") + 1, dateTime.lastIndexOf("-")));
        int dateDay = Integer.parseInt(dateTime.substring(dateTime.lastIndexOf("-") + 1));
        final String earlierStr = "更早";
        if (dateYear < year) {
            // return (year - dateYear) + "年前";
            return earlierStr;
        } else if (dateMonth < month) {
            // return (month - dateMonth) + "月前";
            return earlierStr;
        } else {
            int spa = day - dateDay;
            if (spa <= 0) {
                return "今天";
            } else if (spa <= 1) {
                return "昨天";
            } else if (spa <= 2) {
                // return "前天";
            } else if (spa <= 7) {
                // return spa + "天前";
            } else {
                // return (spa / 7) + "周前";
            }
            return earlierStr;
        }
    }

    /**
     * Get format date time string for 'yyyy年MM月dd日 HH:mm'
     * @return
     */
    public static String getFormatTime() {
        Date currentTime = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        return formatter.format(currentTime);
    }

    /**
     * Calculate date for input year/month/day
     *
     * @param year  正数代表"加", 负数代表"减"
     * @param month
     * @param day
     * @return
     */
    public static Calendar calculateData(int year, int month, int day) {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.add(Calendar.YEAR, year);
        newCalendar.add(Calendar.MONTH, month);
        newCalendar.add(Calendar.DAY_OF_YEAR, day);
        return newCalendar;
    }

    /**
     * Calculate date time for input time string and format
     * @param oldTime
     * @param min
     * @param timeFormat
     * @return
     */
    public static String calculateData(String oldTime, int min, String timeFormat){
        String resultTime = oldTime;
        try {
            Calendar newCalendar = Calendar.getInstance();
            Date date = string2Date(oldTime, timeFormat);
            newCalendar.setTime(date);
            newCalendar.add(Calendar.MINUTE, min);
            resultTime = getFormatTime(newCalendar, timeFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultTime;
    }

    /**
     * Get formate date string according to corresponding time format
     *
     * @param calendar
     * @param timeFormat
     * @return
     */
    public static String getFormatTime(Calendar calendar, String timeFormat) {
        return getDateTime(calendar.getTime(), timeFormat);
    }

    /**
     * Get format time
     * @param year
     * @param month
     * @param day
     * @param timeFormat
     * @return
     */
    public static String getFormatTime(int year, int month, int day, String timeFormat) {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.add(Calendar.YEAR, year);
        newCalendar.add(Calendar.MONTH, month);
        newCalendar.add(Calendar.DAY_OF_YEAR, day);
        return getDateTime(newCalendar.getTime(), timeFormat);
    }

    /**
     * Get month in year
     * @return
     */
    public static int getMonthInYear(Date date){
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.MONTH) + 1;
    }

    /**
     * Get day in month
     * @return
     */
    public static int getDayInMonth(Date date){
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Get day count in month
     * @param date
     * @return
     */
    public static int getHasDayInMonth(Date date){
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * Check date1 is before date2
     * @param date1
     * @param date2
     * @param dateformat
     * @return
     */
    public static boolean isDate1BeforeDate2(String date1, String date2, String dateformat) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);
        if (!TextUtils.isEmpty(date1) && !TextUtils.isEmpty(date2)) {
            try {
                Date d1 = dateFormat.parse(date1);
                Date d2 = dateFormat.parse(date2);
                return d1.getTime() < d2.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Check is date time before today
     * @param date
     * @param dateformat
     * @return
     */
    public static boolean isDateTimeBeforeToday(String date, String dateformat) {
        return !TextUtils.isEmpty(date)
                && isDate1BeforeDate2(date, getDateTime(dateformat), dateformat);
    }

    /**
     * Is date time after today
     * @param date
     * @param dateformat
     * @return
     */
    public static boolean isDateTimeAfterToday(String date, String dateformat) {
        return !TextUtils.isEmpty(date)
                && isDate1BeforeDate2(getDateTime(dateformat), date, dateformat);
    }

    /**
     * Is date time before today
     * @param date
     * @param dateformat
     * @return
     */
    public static boolean isDateTimeNotBeforeToday(String date, String dateformat) {
        return !TextUtils.isEmpty(date)
                && !isDate1BeforeDate2(date, getDateTime(dateformat), dateformat);
    }

    /**
     * Format date time
     * @param dateTime
     * @param dateformat
     * @return
     */
    public static String formatDateTime(String dateTime, String dateformat) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);
        try {
            Date date = dateFormat.parse(dateTime);
            return getDateTime(date, dateformat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("SimpleDateFormat")
    public static String longToString(long times, String format) {
        Date date = new Date(times);
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * Get max date time in year
     * @param year
     * @return
     */
    public static String getMaxDateTimeInYear(String year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.valueOf(year));
        int maxMonth = calendar.getActualMaximum(Calendar.MONTH);
        calendar.set(Calendar.MONTH, maxMonth);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, maxDay);
        int maxHour = calendar.getActualMaximum(Calendar.HOUR_OF_DAY);
        calendar.set(Calendar.HOUR_OF_DAY, maxHour);
        int maxMin = calendar.getActualMaximum(Calendar.MINUTE);
        calendar.set(Calendar.MINUTE, maxMin);
        Date date = new Date(calendar.getTimeInMillis());
        return getDateTime(date, DATE_HOUR_FORMAT);
    }

    /**
     * Return previous thirty date
     * @return
     */
    public static String getPrevThirtyDays() {
        Calendar calendar = Calendar.getInstance();
        // calendar.set(Calendar.DATE, 1); // 把日期设置为当月第一天
        // calendar.roll(Calendar.DATE, -1); // Roll back a day to last day
        // int maxDate = calendar.get(Calendar.DATE);
        // LogUtils.logd("test",
        //         "getPrevThirtyDays max date: " + maxDate);
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -30 + 1);
        Date date = calendar.getTime();
        return new SimpleDateFormat(DATE_DAY_FORMAT).format(date);
    }
}
