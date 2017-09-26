package com.wgmf.web;

import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;

import com.wgmf.common.web.JMSSender;

public class ApplicationListenerFailed implements
		ApplicationListener<ApplicationFailedEvent> {
	@Override
	public void onApplicationEvent(ApplicationFailedEvent event) {
		System.out.println(getClass().getSimpleName());
		System.out.println("fail startup");
		JMSSender.sender(event.getException());
	}

}