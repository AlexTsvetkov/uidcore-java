package com.uniquid.node;

import com.uniquid.node.exception.NodeException;
import com.uniquid.node.listeners.UniquidNodeEventListener;
import com.uniquid.node.state.UniquidNodeState;
import com.uniquid.node.state.UniquidNodeState.EnumState;

/**
 * An Uniquid node is a network node that use the blockchain to create and use smart contracts
 * 
 * @author Giuseppe Magnotta
 */
public interface UniquidNode {

	/**
	 * Returns the imprinting address of this node
	 * 
	 * @return imprinting address of this node
	 */
	public String getImprintingAddress();

	/**
	 * Returns the public key of this node
	 * 
	 * @return public key of this node
	 */
	public String getPublicKey();

	/**
	 * Returns the node name
	 * 
	 * @return the name of this node
	 */
	public String getNodeName();
	
	/**
	 * Returns the creation time of this node
	 * 
	 * @return creation time of this node
	 */
	public long getCreationTime();
	
	/**
	 * Return the spendable balance of this node
	 * 
	 * @return the spendable balance of this node
	 */
	public String getSpendableBalance();

	/**
	 * Initialize this node with some random byte entropy 
	 */
	public void initNode() throws NodeException;
	
	/**
	 * Initialize this node starting with specified byte array entropy and creation date
	 * 
	 * @param hexEntropy the entropy represented in hex format
	 * @param creationTime the creation time in seconds
	 */
	public void initNodeFromHexEntropy(String hexEntropy, long creationTime) throws NodeException;

	/**
	 * Synchronize the node against the blockchain.
	 */
	public void updateNode() throws NodeException;

	/**
	 * Returns the {@link UniquidNodeState} of this node.
	 * 
	 * @return the State of this node
	 */
	public EnumState getNodeState();

	/**
	 * Register an event listener
	 * 
	 * @param uniquidNodeEventListener the event listener that will receive callbacks
	 */
	public void addUniquidNodeEventListener(UniquidNodeEventListener uniquidNodeEventListener);

	/**
	 * Unregister an event listener
	 * 
	 * @param uniquidNodeEventListener the event listener that will be removed
	 */
	public void removeUniquidNodeEventListener(UniquidNodeEventListener uniquidNodeEventListener);

}