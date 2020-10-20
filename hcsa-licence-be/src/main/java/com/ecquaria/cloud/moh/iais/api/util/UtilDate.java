
package com.ecquaria.cloud.moh.iais.api.util;


import com.ecquaria.cloud.moh.iais.api.config.GatewayStripeRefundConfig;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class UtilDate {

	private static final Random random = new Random();
	
	public  static String getOrderNum(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(GatewayStripeRefundConfig.dtLong);
		return df.format(date);
	}
	
	public  static String getDateFormatter(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(GatewayStripeRefundConfig.simple);
		return df.format(date);
	}
	
	public static String getDate(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(GatewayStripeRefundConfig.dtShort);
		return df.format(date);
	}
	
	public static String getThree(){
		return random.nextInt(1000)+"";
	}
}
