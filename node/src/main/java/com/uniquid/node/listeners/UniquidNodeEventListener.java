package com.uniquid.node.listeners;

import java.util.Date;

import com.uniquid.register.provider.ProviderChannel;
import com.uniquid.register.user.UserChannel;

/**
 * UniquidNodeEventListener allow to listen for events from Uniquid network
 * 
 * @author Giuseppe Magnotta
 *
 */
public interface UniquidNodeEventListener {
	
	/**
	 * Called when a provider channel is created
	 * 
	 * @param providerChannel the ProviderChannel created
	 */
	public void onProviderContractCreated(ProviderChannel providerChannel);
	
	/**
	 * Called when a provider channel is revoked
	 * 
	 * @param providerChannel the ProviderChannel revoked
	 */
	public void onProviderContractRevoked(ProviderChannel providerChannel);
	
	/**
	 * Called when a user channel is created
	 * 
	 * @param userChannel the UserChannel created
	 */
	public void onUserContractCreated(UserChannel userChannel);
	
	
	/**
	 * Called when a user channel is revoked
	 * 
	 * @param userChannel the UserChannel revoked
	 */
	public void onUserContractRevoked(UserChannel userChannel);
	
	/**
	 * Called when a sync from blockchain starts
	 * 
	 * @param blocks the numer of blocks to download
	 */
	public void onSyncStarted(final int blocks);
	
	/**
	 * Called when a sync progress
	 * 
	 */
	public void onSyncProgress(double pct, final int blocksSoFar, Date date);
	
	/**
	 * Called when a sync from blockchain terminates
	 * 
	 */
	public void onSyncEnded();

}