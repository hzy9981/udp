package com.jinmei.s2pdsudp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {
	public static String FULL="yyyy-MM-dd hh:mm:ss";
	
	/**
	 * 得到当前时间
	 * @param pattern
	 * @return
	 */
	public static String getNow(String pattern){
		SimpleDateFormat sf=new SimpleDateFormat(pattern);
		return sf.format(Calendar.getInstance().getTime());
	}
	
	/**
	 * 解析时间
	 * @param time
	 * @return
	 */
	public static String parseTime(String time){
		String ret=null;
		if(time==null)return null;
		SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddHHmmssS");
		SimpleDateFormat sf2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		try {
			ret=sf2.format(sf.parse(time));
		} catch (ParseException e) {
			return null;
		}
		return ret;
	}
	
}
