package com.odark.di;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MessageRenderer {
	private MessageProvider messageProvider;

	public void setMessageProvider(MessageProvider messageProvider) {
		this.messageProvider = messageProvider;
	}

	public void render() {
		System.out.println(messageProvider.getMessage());
	}

	public static void main(String[] args) {
// 수동처리		
//		MessageRenderer render = new MessageRenderer();
//		render.setMessageProvider(new HelloWorldMessageProvider());
//		render.render();
//		
//		render.setMessageProvider(new HiWorldMessageProvider());
//		render.render();
		
// 스프링 처리
		ApplicationContext ac = new ClassPathXmlApplicationContext("di.xml");
		MessageRenderer render = (MessageRenderer)ac.getBean("messageRenderer");
		render.render();
	}
}
