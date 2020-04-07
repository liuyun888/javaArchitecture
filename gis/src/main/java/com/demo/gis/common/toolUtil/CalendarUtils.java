package com.demo.gis.common.toolUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @program: javaArchitecture
 * @description: java.util.Calendar处理时间
 * @author: LiuYunKai
 * @create: 2020-04-07 14:37
 **/
public class CalendarUtils {

    /**
     * 取得1970/1/1距离1900/1/1的小时数
     *
     * @return
     */
    public static int getHourNum() {
        int hourNum = 0;
        GregorianCalendar gd = new GregorianCalendar(1970,1,1);
        Date dt = gd.getTime();
        GregorianCalendar gd1 = new GregorianCalendar(1900, 1, 1);
        Date dt1 = gd1.getTime();
        hourNum = (int) ((dt.getTime() - dt1.getTime()) / (60 * 60 * 1000));
        return hourNum;
    }

    /**
     * 获取当前时间的秒数 1970/01/01至今的秒数，,等于new Date().getTime()/1000
     * @return
     * @throws Exception
     */
    public static long getNowTimeStamp()
    {
        long stamp = 0L;
        Date date1 = new Date();
        Date date2 = null;
        try {
            date2 = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).parse("1970/01/01 08:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        stamp = (date1.getTime() - date2.getTime()) / 1000L;
        return stamp;
    }

    /**
     * 获取当前时间的毫秒数 1970/01/01至今的毫秒数,等于new Date().getTime()
     * @return
     * @throws Exception
     */
    public static long getNowTimeStampMs(){
        long stamp = 0L;
        Date date1 = new Date();
        Date date2 = null;
        try {
            date2 = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).parse("1970/01/01 08:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        stamp = (date1.getTime() - date2.getTime());
        return stamp;
    }




    /**
     * 时间转换成秒 1970/01/01至今的秒数（Date转long），等于new Date().getTime()/1000
     * @param date
     * @return
     * @throws Exception
     */
    public static long getTimeStampByDate(Date date)
    {
        long stamp = 0L;
        Date date1 = date;
        Date date2 = null;
        try {
            date2 = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).parse("1970/01/01 08:00:00");
            stamp = (date1.getTime() - date2.getTime()) / 1000L;
        } catch (Exception e) {
            stamp = 0L;
        }

        return stamp;
    }

    /**
     * 时间转换成秒 1970/01/01至今的豪秒数（Date转long）
     * @param date
     * @return
     * @throws Exception
     */
    public static long getTimeStampMsByDate(Date date)
    {
        long stamp = 0L;
        Date date1 = date;
        Date date2 = null;
        try {
            date2 = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).parse("1970/01/01 08:00:00");
            stamp = (date1.getTime() - date2.getTime());
        } catch (Exception e) {
            stamp = 0L;
        }

        return stamp;
    }


    /**
     * 将时间由秒转换成指定格式，如(long转：yyyy-MM-dd HH:mm:ss)
     * @param second
     * @param format
     * @return
     * @throws Exception
     */
    public static String getYYYYByTimeStamp(Long second, String format)
    {
        if(second==null||second==0){
            return "";
        }
        Date da = null;
        try {
            da = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse("1970-01-01 08:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date date = new Date(da.getTime() + second * 1000L);
        return (new SimpleDateFormat(format)).format(date);
    }
    /**
     * 将时间由毫秒转换成指定格式，如(long转：yyyy-MM-dd HH:mm:ss)
     * @param second
     * @param format
     * @return
     * @throws Exception
     */
    public static String getYYYYbyTimeStampMs(Long second, String format)
    {
        if(second==null||second==0){
            return "";
        }
        Date da = null;
        try {
            da = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse("1970-01-01 08:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date date = new Date(da.getTime() + second );
        return (new SimpleDateFormat(format)).format(date);
    }




    /**
     * 1970/01/01至今的秒数转换成Date
     * @param TimeStamp
     * @return
     */
    public static Date getDateByTimeStamp(Long TimeStamp){
        return new Date(TimeStamp*1000);
    }

    /**
     * 1970/01/01至今的豪秒数转换成Date
     * @param TimeStampMs
     * @return
     */
    public static Date getDateByTimeStampMs(Long TimeStampMs){
        return new Date(TimeStampMs);
    }

}
