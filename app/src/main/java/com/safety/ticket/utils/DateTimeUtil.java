package com.safety.ticket.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {
    private static final SimpleDateFormat SDF_FULL = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    private static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    private static final SimpleDateFormat SDF_TIME = new SimpleDateFormat("HH:mm", Locale.CHINA);

    public static String getNowFull() {
        return SDF_FULL.format(new Date());
    }

    public static String getNowDate() {
        return SDF_DATE.format(new Date());
    }

    public static String formatFull(Date date) {
        if (date == null) return "";
        return SDF_FULL.format(date);
    }

    public static String formatDate(Date date) {
        if (date == null) return "";
        return SDF_DATE.format(date);
    }
}
