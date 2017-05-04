package com.uniquid.core;

/**
 * This interface represents an input RPC message 
 *
 * @param <T>
 */
public interface ProviderRequest {
	
	/**
	 * Returns the sender of the request.
	 * 
	 * @return the sender of the request.
	 */
	public String getSender();
	
	/**
	 * Returns the integer representing the function to execute.
	 * 
	 * @return the integer representing the function to execute.
	 */
	public int getFunction();
	
	/**
	 * Returns the parameters of the function as a string.
	 * 
	 * @return a String representing the parameters of the function.
	 */
	public String getParams();
	
}