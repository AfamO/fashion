package com.longbridge.Util;

import java.lang.reflect.InvocationTargetException;

public class CustomBeanUtilsBean extends org.apache.commons.beanutils.BeanUtilsBean {
	@Override
	public void copyProperty(Object dest, String name, Object value)
			throws IllegalAccessException, InvocationTargetException {
		if (value == null)
			return;
		try {
			super.copyProperty(dest, name, value);
		} catch (IllegalArgumentException e) {
		}
	}
	
	private static CustomBeanUtilsBean bean;
	
	public static CustomBeanUtilsBean getInstance(){
		if(bean == null){
			bean = new CustomBeanUtilsBean();
		}
		return bean;
	}

}
