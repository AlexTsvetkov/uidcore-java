package com.uniquid.node.impl;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.script.Script;
import org.bitcoinj.signers.LocalTransactionSigner;
import org.bitcoinj.signers.MissingSigResolutionSigner;
import org.bitcoinj.signers.TransactionSigner;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.RedeemData;
import org.bitcoinj.wallet.SendRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.encoders.Hex;

import com.google.common.collect.ImmutableList;
import com.uniquid.node.exception.NodeException;
import com.uniquid.node.impl.utils.NodeUtils;

/**
 * Implementation of an Uniquid Node with BitcoinJ library
 */
public class UniquidNodeImpl<T extends UniquidNodeConfiguration> extends UniquidWatchingNodeImpl<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(UniquidNodeImpl.class.getName());

	private final DeterministicSeed deterministicSeed;

	/**
	 * Creates a new instance
	 * 
	 * @param uniquidNodeConfiguration
	 * @throws NodeException
	 */
	protected UniquidNodeImpl(T uniquidNodeConfiguration, DeterministicSeed deterministicSeed) throws NodeException {

		super(uniquidNodeConfiguration);
		
		this.deterministicSeed = deterministicSeed;

	}

	/*
	 * 
	 * Begin of public part for implementing UniquidNode
	 *
	 */
	@Override
	public synchronized String getHexSeed() {
		return deterministicSeed.toHexString();
	}

	@Override
	public synchronized String signTransaction(final String s_tx, final List<String> paths) throws NodeException {

		try {
			
			DeterministicKey deterministicKey = NodeUtils
					.createDeterministicKeyFromByteArray(deterministicSeed.getSeedBytes());
			
			DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(deterministicKey);
			
			UniquidKeyBag keyBag = new UniquidKeyBag();

			for (String path : paths) {
				
				ImmutableList<ChildNumber> list = listFromPath(path);

				DeterministicKey signingKey = deterministicHierarchy.get(list, true, true);
			
				keyBag.addDeterministicKey(signingKey);
				
			}
			
			Transaction originalTransaction = uniquidNodeConfiguration.getNetworkParameters().getDefaultSerializer()
					.makeTransaction(Hex.decode(s_tx));

			SendRequest req = SendRequest.forTx(originalTransaction);

			Transaction tx = req.tx;

			int numInputs = tx.getInputs().size();
			for (int i = 0; i < numInputs; i++) {
				TransactionInput txIn = tx.getInput(i);

				// Fetch input tx from wallet TODO use a proper wallet!
				Transaction inputTransaction = providerWallet.getTransaction(txIn.getOutpoint().getHash());

				if (inputTransaction == null) {

					throw new NodeException("Input TX not found in wallet!");

				}

				TransactionOutput outputToUse = inputTransaction.getOutput(txIn.getOutpoint().getIndex());

				originalTransaction.getInput(i).connect(outputToUse);
				Script scriptPubKey = txIn.getConnectedOutput().getScriptPubKey();
				RedeemData redeemData = txIn.getConnectedRedeemData(keyBag);
				txIn.setScriptSig(scriptPubKey.createEmptyInputScript(redeemData.keys.get(0), redeemData.redeemScript));

			}

			TransactionSigner.ProposedTransaction proposal = new TransactionSigner.ProposedTransaction(tx);
			TransactionSigner signer = new LocalTransactionSigner();
			if (!signer.signInputs(proposal, keyBag)) {
				LOGGER.info("{} returned false for the tx", signer.getClass().getName());
				throw new NodeException("Cannot sign TX!");
			}

			// resolve missing sigs if any
			new MissingSigResolutionSigner(req.missingSigsMode).signInputs(proposal, keyBag);

			return Hex.toHexString(originalTransaction.bitcoinSerialize());

		} catch (Exception ex) {

			throw new NodeException("Exception while signing", ex);

		}
	}
	
	public DeterministicSeed getDeterministicSeed() {
		return deterministicSeed;
	}
	
	private static ImmutableList<ChildNumber> listFromPath(String path) {
			
			StringTokenizer tokenizer = new StringTokenizer(path, "/");
			
			List<ChildNumber> start = new ArrayList<ChildNumber>();
			
			start.add(new ChildNumber(44, true));
			start.add(new ChildNumber(0, true));
			start.add(new ChildNumber(0, false));
			
			while (tokenizer.hasMoreTokens()) {
				
				String next = tokenizer.nextToken();
				
				start.add(new ChildNumber(Integer.valueOf(next), false));
				
			}
			
			return ImmutableList.copyOf(start);
			
		}

	/**
	 * Builder for UniquidNodeImpl
	 */
	public static class UniquidNodeBuilder<B extends UniquidNodeBuilder<B, T, C>, T extends UniquidNodeConfiguration, C extends UniquidNodeConfigurationImpl> extends UniquidWatchingNodeImpl.WatchingNodeBuilder<B, T, C> {
		
		/**
		 * Build a new instance
		 * 
		 * @return
		 * @throws Exception
		 */
		public UniquidNodeImpl<T> build() throws NodeException {

			SecureRandom random = new SecureRandom();
			byte[] entropy = new byte[32];
			random.nextBytes(entropy);
			long creationTime = System.currentTimeMillis() / 1000;

			DeterministicSeed detSeed = new DeterministicSeed(entropy, "", creationTime);

			_uniquidNodeConfiguration.setCreationTime(creationTime);
			_uniquidNodeConfiguration.setPublicKey(deriveXpub(_uniquidNodeConfiguration.getNetworkParameters(), detSeed));

			return createUniquidNode(_uniquidNodeConfiguration, detSeed);

		}
		
		@Deprecated
		public UniquidNodeImpl<T> buildFromHexSeed(final String hexSeed, final long creationTime) throws NodeException {

			try {
				
				DeterministicSeed detSeed = new DeterministicSeed("", org.bitcoinj.core.Utils.HEX.decode(hexSeed), "", creationTime);
	
				_uniquidNodeConfiguration.setCreationTime(creationTime);
				_uniquidNodeConfiguration.setPublicKey(deriveXpub(_uniquidNodeConfiguration.getNetworkParameters(), detSeed));
	
				return createUniquidNode(_uniquidNodeConfiguration, detSeed);
				
			} catch (Exception ex) {
				
				throw new NodeException("Exception while building node from hex seed", ex);
				
			}

		}

		public UniquidNodeImpl<T> buildFromMnemonic(final String mnemonic, final long creationTime) throws NodeException {

			try {
				DeterministicSeed detSeed = new DeterministicSeed(mnemonic, null, "", creationTime);
	
				_uniquidNodeConfiguration.setCreationTime(creationTime);
				_uniquidNodeConfiguration.setPublicKey(deriveXpub(_uniquidNodeConfiguration.getNetworkParameters(), detSeed));
	
				return createUniquidNode(_uniquidNodeConfiguration, detSeed);
			
			} catch (Exception ex) {
				
				throw new NodeException("Exception while building node from mnemonic", ex);
				
			}

		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		protected UniquidNodeImpl<T> createUniquidNode(C uniquidNodeConfiguration, DeterministicSeed deterministicSeed) throws NodeException {
			return new UniquidNodeImpl(uniquidNodeConfiguration, deterministicSeed);
		}
		
		private static final String deriveXpub(NetworkParameters networkParameters, DeterministicSeed detSeed) {
			
			DeterministicKey deterministicKey = NodeUtils.createDeterministicKeyFromByteArray(detSeed.getSeedBytes());

			DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(deterministicKey);

			ImmutableList<ChildNumber> IMPRINTING_PATH = ImmutableList.of(new ChildNumber(44, true),
					new ChildNumber(0, true));

			DeterministicKey imprintingKey = deterministicHierarchy.get(IMPRINTING_PATH, true, true);

			return imprintingKey.serializePubB58(networkParameters);
			
		}

	}

}