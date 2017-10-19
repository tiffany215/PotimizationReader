package shuhai.readercore.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import java.util.TimeZone;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class TimeFromatUtile {

	private static TimeFromatUtile instance;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat mDateFormatSecond = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private TimeFromatUtile() {

	}

	public static TimeFromatUtile newInstance() {

		if (null == instance) {
			return new TimeFromatUtile();
		}
		return instance;
	}
	
	
	/**
	 * 获取当前系统时间 格式为 yyyy-MM-dd  年月日
	 * @return
	 * @throws ParseException
	 */
	public static String getCurrentDate() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		return df.format(new Date());
	}															// everywhere :)

	/**
	 * 获取当前系统时间 格式为yyyy-MM-dd HH:mm:ss 年月日时分秒
	 * @return
	 * @throws ParseException
	 */
	public static String getCurrentDateTime() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		return df.format(new Date());
	}
	
	/**
	 * 获取系统当前时间秒值
	 * @return
	 * @throws ParseException
	 */
	public static int getCurrentDateTimeSeconds() {
		int a = (int)(System.currentTimeMillis() / 1000);
		return a;
	}
	
	
	/**
	 * 当前时间时候过期
	 * @param lasttime
	 * @return
	 */
	public static boolean isExpired(long lasttime){
		if((TimeFromatUtile.getCurrentDateTimeSeconds() - lasttime) > 60 * 60 * 24){
			return true;
		}
			return false;
	}
	
	
	/**
	 * 将日期时间转换成距1970.1.1以来的秒值
	 * @param dateStr 日期格式字符串
	 * @return
	 * @throws ParseException
	 */
	public static long dateConverDay(String dateStr)
			throws ParseException {
		if (dateStr == null || dateStr.trim().equals(""))
			return 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(dateStr);
			return (int) (date.getTime() / 1000);
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 将距1970以来的秒值转换成时间字符串 格式为  年月日 时分秒
	 * @param day 1970年以来的秒值
	 * @return
	 * @throws ParseException
	 */
	public static String formatTimeDay(String day)
			throws ParseException {
		long time = Long.parseLong(day); // 可用
		Timestamp ts = new Timestamp(time * 1000);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(ts);
	}
	
	
	/**
	 * 将距1970以来的秒值转换成时间字符串 格式为  年月日 时分秒
	 * @param day 1970年以来的秒值
	 * @return
	 * @throws ParseException
	 */
	public static String formatTimeDay(long day)
			throws ParseException {
		long time = day; // 可用
		Timestamp ts = new Timestamp(time * 1000);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(ts);
	}
	
	
	
	/**
	 * 将距1970以来的秒值转换成时间字符串 格式为  年月日 
	 * @param day 1970年以来的秒值
	 * @return
	 * @throws ParseException
	 */
	public static String formatTimeDay1(String day)
			throws ParseException {
		long time = Long.parseLong(day); // 可用
		Timestamp ts = new Timestamp(time * 1000);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(ts);
	}
	
	/**
	 * 将距1970以来的秒值转换成时间字符串 格式为  月日
	 * @param day 1970年以来的秒值
	 * @return
	 * @throws ParseException
	 */
	public static String formatTimeDay2(String day)
	throws ParseException {
		long time = Long.parseLong(day); // 可用
		Timestamp ts = new Timestamp(time * 1000);
		DateFormat df = new SimpleDateFormat("MM.dd");
		return df.format(ts);
	}
	
	/**
	 * 判断传入的字符串是否为空值或者是空字符串
	 * @param str 传入的字符串
	 * @return true 表示当前字符串为空值或空字符串 
	 */
	public static boolean isEmpty(String str) {
		if (str == null)
			return true;
		if (str.trim().length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断当前字符串是否是数字类型
	 * @param str 
	 * @return true 表示是数字类型的字符串
	 */
	public static boolean isNum(String str){
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}
	

	public String formatDateTime(long time) {
		if (0 == time) {
			return "";
		}
		return mDateFormat.format(new Date(time));
	}

	public String formatDateTimeSecond(long time) {
		if (0 == time) {
			return "";
		}

		return mDateFormatSecond.format(time);
	}

	@SuppressLint("SimpleDateFormat")
	public long calcTime(long fristTime, long secondTime) {

		String frist = formatDateTimeSecond(fristTime);
		String second = formatDateTimeSecond(secondTime);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d1 = df.parse(second);

			Date d2 = df.parse(frist);

			long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别

			long days = diff / (1000 * 60 * 60 * 24);

//			long hours = (diff - days * (1000 * 60 * 60 * 24))
//					/ (1000 * 60 * 60);

//			long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
//					* (1000 * 60 * 60))
//					/ (1000 * 60);

			return days;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
	/**
	 * 计算包月到期时间
	 * @return
	 */
	public int calcExpirationDate(long fristTime,long secondTime){
		return (int) ((fristTime - (secondTime / 1000)) /  86400);
	}
	
	

	/**
	 * 将字符串转位日期类型
	 * @param sdate
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static Date stringToDate(String sdate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format.parse(sdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}
	
	/**
	 * 将 时间类型转字符串类型
	 * @param sdate
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String dateToString(Date sdate){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(sdate);
	}
	
	/**
	 * 格林时间转化为本地时间
	 * @param gmt
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String convertGMTToLoacale(String gmt){
        String cc = gmt.substring(0, 19) + gmt.substring(33, 38);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy",new Locale("English"));
        try {
            Date date = sdf.parse(cc);
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String result = dateformat.format(date);
            return result;
        } catch (ParseException e) {
        }
        return "";
    }
	
	/**
	 * 格式化
	 * @param sdate
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatDateString(String sdate){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(sdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if (date != null) {
			return format.format(sdate);
		}
		return sdate;
	}
	
	/**
	 * 将 时间类型转字符串类型
	 * @param sdate
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String dateToString(Date sdate, String format){
		SimpleDateFormat dateformat = new SimpleDateFormat(format);
		return dateformat.format(sdate);
	}
	
	/**
	 * 以友好的方式显示时间
	 * @param sdate
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String friendly_time(String sdate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date time = stringToDate(sdate);
		if(time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();
		
		//判断是否是同一天
		String curDate = format.format(cal.getTime());
		String paramDate = format.format(time);
		if(curDate.equals(paramDate)){
			int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
			if(hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000,1)+"分钟前";
			else 
				ftime = hour+"小时前";
			return ftime;
		}
		
		long lt = time.getTime()/86400000;
		long ct = cal.getTimeInMillis()/86400000;
		int days = (int)(ct - lt);		
		if(days == 0){
			int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
			if(hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000,1)+"分钟前";
			else 
				ftime = hour+"小时前";
		}
		else if(days == 1){
			ftime = "昨天";
		}
		else if(days == 2){
			ftime = "前天";
		}
		else if(days > 2 && days <= 10){ 
			ftime = days+"天前";			
		}
		else if(days > 10){			
			ftime = format.format(time);
		}
		return ftime;
	}
	
	/**
	 * 判断给定字符串时间是否为今日
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate){
		boolean b = false;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date time = stringToDate(sdate);
		Date today = new Date();
		if(time != null){
			String nowDate = format.format(today);
			String timeDate = format.format(time);
			if(nowDate.equals(timeDate)){
				b = true;
			}
		}
		return b;
	}
	
	
	
	public static boolean isToady(long timeData){
			long current=System.currentTimeMillis();//当前时间毫秒数
			long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
			long twelve=zero+24*60*60*1000-1;//今天23点59分59秒的毫秒数
			if((zero / 1000) < timeData  && timeData < (twelve / 1000) ){
				return true;
			}
				return false;
	}
	

	/**
	 * 获取当前时间凌晨12点
	 * @return
	 */
	public static long getEarlyTime(){
		long current=System.currentTimeMillis();//当前时间毫秒数
		long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
		long twelve=zero+24*60*60*1000-1;//今天23点59分59秒的毫秒数
//		long yesterday=System.currentTimeMillis()-24*60*60*1000;//昨天的这一时间的毫秒数
		return twelve/1000;
	}
	
	
	
	

}
