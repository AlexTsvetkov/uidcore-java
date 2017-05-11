package com.uniquid.core.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.bitcoinj.utils.ContextPropagatingThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uniquid.core.Core;
import com.uniquid.core.ProviderRequest;
import com.uniquid.core.ProviderResponse;
import com.uniquid.core.connector.Connector;
import com.uniquid.core.connector.ConnectorException;
import com.uniquid.core.connector.EndPoint;
import com.uniquid.core.provider.Function;
import com.uniquid.core.provider.exception.FunctionException;
import com.uniquid.core.provider.impl.ContractFunction;
import com.uniquid.core.provider.impl.EchoFunction;
import com.uniquid.core.provider.impl.FunctionConfigImpl;
import com.uniquid.node.UniquidNode;
import com.uniquid.node.UniquidNodeState;
import com.uniquid.register.RegisterFactory;

/**
 * Uniquid reference implementation of {@link Core}.
 */
public class UniquidSimplifier extends Core {

	private static final Logger LOGGER = LoggerFactory.getLogger(Core.class.getName());

	private final Map<Integer, Function> functionsMap = new HashMap<>();
	
	private ScheduledExecutorService scheduledExecutorService;
	private ScheduledExecutorService receiverExecutorService;

	/**
	 * Creates an instance from {@link RegisterFactory}, {@link Connector} and {@link UniquidNode}
	 * @param registerFactory the {@link RegisterFactory} to use
	 * @param connector the {@link Connector} to use
	 * @param node the {@link UniquidNode} to use
	 * @throws Exception in case an error occurs
	 */
	public UniquidSimplifier(RegisterFactory registerFactory, Connector connectorServiceFactory, UniquidNode node)
			throws Exception {

		// Call superclass
		super(registerFactory, connectorServiceFactory, node);

		// Register core functions
		try {

			addUniquidFunction(new ContractFunction(), 30);
			addUniquidFunction(new EchoFunction(), 31);

		} catch (FunctionException ex) {
			// This will never happens!
		}
	}
	
	@Override
	protected Function getFunction(ProviderRequest inputMessage) {

		int rpcMethod = inputMessage.getFunction();

		return functionsMap.get(rpcMethod);

	}

	/**
	 * Register a {@link Function} inside the library with the specified number.
	 * @param function the {@link Function} to register inside the library.
	 * @param functionNumber the number to assign to the {@link Function}
	 * @throws FunctionException in case a problem occurs.
	 */
	public void addFunction(Function function, int functionNumber) throws FunctionException {

		if (functionNumber >= 32) {

			FunctionConfigImpl functionConfigImpl = new FunctionConfigImpl(getFunctionContext());

			function.init(functionConfigImpl);

			functionsMap.put(functionNumber, function);

		} else {

			throw new FunctionException("Invalid function number!");

		}

	}

	/*
	 * Register an internal Function
	 */
	private void addUniquidFunction(Function function, int value) throws FunctionException {

		if (value >= 0 && value <= 31) {

			FunctionConfigImpl functionConfigImpl = new FunctionConfigImpl(getFunctionContext());

			function.init(functionConfigImpl);

			functionsMap.put(value, function);

		} else {

			throw new FunctionException("Invalid function number!");

		}

	}

	/**
	 * 
	 * Initialize the library and start the processing
	 * 
	 * @throws Exception in case a problem occurs.
	 */
	public void start() throws Exception {

		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ContextPropagatingThreadFactory("scheduledExecutorService"));
		receiverExecutorService = Executors.newSingleThreadScheduledExecutor(new ContextPropagatingThreadFactory("receiverExecutorService"));

		// initialize node if not yet initilized
		if (UniquidNodeState.CREATED.equals(getNode().getNodeState())) {
			
			LOGGER.info("Initializing node");
			getNode().initNode();
		
		}
		
		final Runnable walletSyncher = new Runnable() {
			
			public void run() {
				
				try {
					
					// Update node from blockchain
					getNode().updateNode();

				} catch (Exception e) {
					
					LOGGER.error("Exception", e);
				}

			}
		};

		final ScheduledFuture<?> updaterThread = scheduledExecutorService.scheduleWithFixedDelay(walletSyncher, 0, 1,
				TimeUnit.MINUTES);

		try {

			// start connector
			getConnector().start();

		} catch (ConnectorException e) {
			LOGGER.error("Exception", e);
		}

		// Create a thread to wait for messages
		final Runnable receiver = new Runnable() {

			@Override
			public void run() {

				// until not interrupted
				while (!Thread.currentThread().isInterrupted()) {

					try {

						// this will block until a message is received
						EndPoint endPoint = getConnector().accept();

						ProviderRequest inputMessage = endPoint.getInputMessage();

						ProviderResponse outputMessage = endPoint.getOutputMessage();
						
						if (!UniquidNodeState.READY.equals(getNode().getNodeState())) {
							LOGGER.warn("Node is not yet READY! Skipping request");
							
							continue;
						}

						LOGGER.info("Received input message from : " + inputMessage.getSender()
								+ " asking method " + inputMessage.getFunction());

						LOGGER.info("Checking sender...");

						// Check if sender is authorized or throw exception
						byte[] payload = checkSender(inputMessage);

						LOGGER.info("Performing function...");
						performProviderRequest(inputMessage, outputMessage, payload);

						endPoint.flush();
						
						LOGGER.info("Done!");

					} catch (Throwable t) {

						LOGGER.error("Throwable catched", t);

					}

				}

			}

		};

		// Start receiver
		receiverExecutorService.execute(receiver);

	}

	/**
	 * Stop the library and stop the processing
	 * 
	 * @throws Exception in case a problem occurs
	 */
	public void shutdown() throws Exception {

		scheduledExecutorService.shutdown();
		receiverExecutorService.shutdown();
		
 		try {
			
			scheduledExecutorService.awaitTermination(5, TimeUnit.SECONDS);
			receiverExecutorService.awaitTermination(5, TimeUnit.SECONDS);

		} catch (InterruptedException e) {

			LOGGER.error("Exception while awaiting for termination", e);

		}

		try {
			getConnector().stop();
		} catch (ConnectorException e) {
			LOGGER.error("Error", e);
		}
	}

}
