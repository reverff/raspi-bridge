package com.beowulfe.hap.sample;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.beowulfe.hap.HomekitAuthInfo;
import com.beowulfe.hap.HomekitServer;
import org.apache.log4j.Logger;

/**
 * This is a simple implementation that should never be used in actual production. The mac, salt, and privateKey
 * are being regenerated every time the application is started. The user store is also not persisted. This means pairing
 * needs to be re-done every time the app restarts.
 *
 * @author Andy Lintner
 */
public class RaspiBridgeAuthInfo implements HomekitAuthInfo {
	private static final Logger log = Logger.getLogger(RaspiBridgeAuthInfo.class);
	private static final String PIN = "004-02-994";
	
	private final String mac;
	private final BigInteger salt;
	private String privateKey;
	private final ConcurrentMap<String, byte[]> userKeyMap = new ConcurrentHashMap<>();
	
	public RaspiBridgeAuthInfo() throws InvalidAlgorithmParameterException {
		mac = HomekitServer.generateMac();
		salt = HomekitServer.generateSalt();
        setPrivateKey(HomekitServer.generateKey());
		log.info("Auth info is generated each time the sample application is started. Pairings are not persisted.");
		log.info("The PIN for pairing is " + PIN);
	}

	@Override
	public String getPin() {
		return PIN;
	}

	@Override
	public String getMac() {
		return mac;
	}

	@Override
	public BigInteger getSalt() {
		return salt;
	}

	@Override
	public byte[] getPrivateKey() {
		return privateKey.getBytes();
	}

    private void setPrivateKey(byte[] privateKey) {
        this.privateKey = new String(privateKey);
    }

    @Override
	public void createUser(String username, byte[] publicKey) {
		userKeyMap.putIfAbsent(username, publicKey);
		log.info("Added pairing for " + username);
	}

	@Override
	public void removeUser(String username) {
		userKeyMap.remove(username);
		log.info("Removed pairing for " + username);
	}

	@Override
	public byte[] getUserPublicKey(String username) {
		return userKeyMap.get(username);
	}

}
