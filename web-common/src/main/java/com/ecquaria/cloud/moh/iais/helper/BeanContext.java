package com.ecquaria.cloud.moh.iais.helper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author: guyin
 * @date time:2/26/2020 10:56 AM
 * @description:
 */

@Component
public class BeanContext implements ApplicationContextAware {


	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		BeanContext.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext(){
		return applicationContext;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) throws BeansException {
		return (T)applicationContext.getBean(name);
	}

	public static <T> T getBean(Class<T> clz) throws BeansException {
		return (T)applicationContext.getBean(clz);
	}
}
