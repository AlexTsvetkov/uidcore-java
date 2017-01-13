package com.uniquid.uniquid_core.connector;

import com.uniquid.uniquid_core.InputMessage;
import com.uniquid.uniquid_core.OutputMessage;

/**
 * The connector component support a custom transport protocol and enables the system to works independently from its implementation.
 */
public interface Connector {

	/**
	 * Starts the connector
	 */
	public void start() throws ConnectorException;
	
	/**
	 * Stop the connector
	 */
	public void stop() throws ConnectorException;
	
	/**
	 * Listens for a connection to be made to this connector and accepts it. The method blocks until a connection is made
	 * 
	 * @return {@link EndPoint}
	 * @throws ConnectorException
	 */
	public abstract EndPoint accept() throws ConnectorException;
	
	/**
	 * This method will return an OutputMessage that later can be submitter to this connector to be sent
	 * @return
	 * @throws ConnectorException
	 */
	public OutputMessage<?> createOutputMessage() throws ConnectorException;
	
	/**
	 * Sends the message
	 * @param outputMessage
	 * @param timeout
	 * @return
	 */
	public InputMessage<?> sendOutputMessage(OutputMessage<?> outputMessage, long timeout);
	
}
