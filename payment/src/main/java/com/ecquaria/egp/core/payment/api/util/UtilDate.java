
package com.ecquaria.egp.core.payment.api.util;

import com.ecquaria.egp.core.payment.api.config.GatewayConfig;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class UtilDate {

	private static final Random random = new Random();
	
	public  static String getOrderNum(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(GatewayConfig.dtLong);
		return df.format(date);
	}
	
	public  static String getDateFormatter(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(GatewayConfig.simple);
		return df.format(date);
	}
	
	public static String getDate(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(GatewayConfig.dtShort);
		return df.format(date);
	}
	
	public static String getThree(){
		return random.nextInt(1000)+"";
	}
}
