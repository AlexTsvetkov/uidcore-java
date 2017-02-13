package com.uniquid.node.state.impl;

import org.bitcoinj.core.Transaction;
import org.bitcoinj.wallet.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uniquid.node.state.UniquidNodeState;
import com.uniquid.node.state.UniquidNodeStateContext;

/**
 * This class represent an Uniquid Node just created (empty) to imprint.
 * 
 */
public class ImprintingState implements UniquidNodeState {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ImprintingState.class.getName());
	
	private boolean alreadyImprinted;
	
	public ImprintingState() {

		this.alreadyImprinted = false;

	}
	
	@Override
	public void onCoinsSent(UniquidNodeStateContext nodeStateContext, Wallet wallet, Transaction tx) {
		
		/*if (wallet.equals(nodeStateContext.getRevokeWallet())) {
			
			LOGGER.info("A contract was revoked!!!");
			
		} else {*/
			
			LOGGER.info("We sent coins from a wallet that we don't expect!");
			
		//}
	}

	@Override
	public void onCoinsReceived(UniquidNodeStateContext nodeStateContext, Wallet wallet, Transaction tx) {
		
		if (wallet.equals(nodeStateContext.getProviderWallet())) {
			
			LOGGER.info("Received coins on provider wallet");
			
			try {
				
				// If is imprinting transaction...
				if (com.uniquid.node.utils.Utils.isValidImprintingTransaction(tx, nodeStateContext) && !alreadyImprinted) {
					
					LOGGER.info("Imprinting contract");
					
					// imprint!
					com.uniquid.node.utils.Utils.makeImprintContract(tx, nodeStateContext);
					
					alreadyImprinted = true;

				} else {
					
					LOGGER.info("Invalid imprinting contract");
					
				}
	
			} catch (Exception ex) {
	
				LOGGER.error("Exception while imprinting", ex);
	
			}
		
		} else if (wallet.equals(nodeStateContext.getUserWallet())) {
			
			LOGGER.info("Received coins on user wallet");
			
			try {
				
				com.uniquid.node.utils.Utils.makeUserContract(wallet, tx, nodeStateContext);
					
			} catch (Exception ex) {
	
				LOGGER.error("Exception while creating user contract", ex);
	
			}
			
		} else {
			
			LOGGER.warn("We received coins on a wallet that we don't expect!");
			
		}

	}
	
	public String toString() {

		return "Imprinting State";

	}

	@Override
	public EnumState getState() {
		
		return EnumState.IMPRINTING;
		
	}

}