package com.uniquid.core.provider.impl;

import java.util.Enumeration;

import com.uniquid.core.provider.FunctionConfig;
import com.uniquid.core.provider.FunctionContext;

public class FunctionConfigImpl implements FunctionConfig {
	
	private FunctionContext functionContext;
	
	public FunctionConfigImpl(FunctionContext functionContext) {
		this.functionContext = functionContext;
	}

	@Override
	public FunctionContext getFunctionContext() {
		return functionContext;
	}

	@Override
	public String getInitParameter(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getInitParameterNames() {
		// TODO Auto-generated method stub
		return null;
	}

}