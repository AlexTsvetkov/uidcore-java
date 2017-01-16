package com.uniquid.uniquid_core.connector;

import com.uniquid.uniquid_core.InputMessage;
import com.uniquid.uniquid_core.OutputMessage;

/**
 * Represent a communication between a Provider and a User
 * 
 * @author giuseppe
 *
 */
public interface EndPoint<T> {
	
	/**
	 * Returns the request performed by the User
	 * @return
	 */
	public InputMessage<T> getInputMessage();

	/**
	 * Returns the response to be returned to the User
	 * @return
	 */
	public OutputMessage<T> getOutputMessage();
	
	/**
	 * Close this EndPoint sending all the communication to the User.
	 */
	public void close();

}
