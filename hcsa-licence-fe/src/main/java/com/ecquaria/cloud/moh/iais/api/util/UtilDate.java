
package com.ecquaria.cloud.moh.iais.api.util;


import com.ecquaria.cloud.moh.iais.api.config.GatewayConfig;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilDate {

	private static final SecureRandom random = new SecureRandom();
	
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
