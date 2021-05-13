package com.hoanganhbk.schoolBus.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static String formatPattern = "yyyy/MM/dd HH:mm:ss";

    public static String format(Date date) {
        return format(date, formatPattern);
    }

    public static String format(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }
}
