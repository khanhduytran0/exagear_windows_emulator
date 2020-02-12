package com.eltechs.axs.helpers;

import java.util.Date;

public class DateHelper {
    public static final long MSEC_IN_DAY = 86400000;
    public static final long MSEC_IN_HOUR = 3600000;
    public static final long MSEC_IN_MINUTE = 60000;
    public static final long MSEC_IN_SECOND = 1000;

    public static long getDiffSeconds(Date date, Date date2) {
        return (date.getTime() - date2.getTime()) / 1000;
    }

    public static long getDiffMinutes(Date date, Date date2) {
        return (date.getTime() - date2.getTime()) / MSEC_IN_MINUTE;
    }

    public static long getDiffHours(Date date, Date date2) {
        return (date.getTime() - date2.getTime()) / MSEC_IN_HOUR;
    }

    public static long getDiffDays(Date date, Date date2) {
        return (date.getTime() - date2.getTime()) / MSEC_IN_DAY;
    }
}
