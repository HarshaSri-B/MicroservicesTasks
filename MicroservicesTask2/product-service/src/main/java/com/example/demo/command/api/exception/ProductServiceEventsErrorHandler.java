package com.example.demo.command.api.exception;

import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.EventMessageHandler;
import org.axonframework.eventhandling.ListenerInvocationErrorHandler;

public class ProductServiceEventsErrorHandler implements ListenerInvocationErrorHandler{
	{
}

	@Override
	public void onError(Exception exception, EventMessage<?> event, EventMessageHandler eventHandler) throws Exception {
		// TODO Auto-generated method stub
		throw exception;
		
	}

}
