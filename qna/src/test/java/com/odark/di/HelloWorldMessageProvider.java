package com.odark.di;

public class HelloWorldMessageProvider implements MessageProvider {

	@Override
	public String getMessage() {
		return "HelloWorld";
	}

}
