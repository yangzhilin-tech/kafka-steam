package com.wgmf.web;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 
 *
 */
@Component
public class AppContextHolder implements ApplicationContextAware {

	private static ApplicationContext instance;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		initInstance(applicationContext);
	}

	public static ApplicationContext getApplicationContext() {
		return instance;
	}

	private static void initInstance(ApplicationContext applicationContext) {
		instance = applicationContext;
	}

}
