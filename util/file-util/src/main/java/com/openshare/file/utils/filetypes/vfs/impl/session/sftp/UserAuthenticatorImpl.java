package com.openshare.file.utils.filetypes.vfs.impl.session.sftp;

import org.apache.commons.vfs2.auth.StaticUserAuthenticator;

import com.openshare.file.utils.filetypes.vfs.impl.DiskFile;
/**
 * User Authenticator imlementation as we want to be able to transmit a key fingerprint.
 * @author james.mcilroy
 *
 */
public class UserAuthenticatorImpl extends StaticUserAuthenticator {

	/** The user name */
    private final String keyFingerprint;
    private final String passPhrase;
    private final DiskFile privateKey;
    
	public UserAuthenticatorImpl(String domain, String username,
			String password,String passPhrase,String keyFingerprint,DiskFile privateKey) {
		//build on top of the StaticUserAuthenticator
		super(domain, username, password);
		this.keyFingerprint = keyFingerprint;
		this.privateKey = privateKey;
		this.passPhrase = passPhrase;
	}

	/**
	 * @return the keyFingerprint
	 */
	public String getKeyFingerprint() {
		return keyFingerprint;
	}

	/**
	 * @return the privateKey
	 */
	public DiskFile getPrivateKey() {
		return privateKey;
	}

	/**
	 * @return the passPhrase
	 */
	public String getPassPhrase() {
		return passPhrase;
	}

	
}
