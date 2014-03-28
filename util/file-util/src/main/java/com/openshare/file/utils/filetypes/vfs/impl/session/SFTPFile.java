package com.openshare.file.utils.filetypes.vfs.impl.session;

import java.io.Serializable;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.UserAuthenticator;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.apache.log4j.Logger;

import com.openshare.file.utils.FileSystemManagerImpl;
import com.openshare.file.utils.filetypes.SupportedFileType;
import com.openshare.file.utils.filetypes.vfs.impl.DiskFile;
import com.openshare.file.utils.filetypes.vfs.impl.session.sftp.UserAuthenticatorImpl;
/**
 * SFTP file representation, abstracts a file on an SSH ftp server connection.
 * @author james.mcilroy
 * if no user or password supplied system will automatically try to fall back to using keys.
 * you will need to set:
 * vfs.sftp.sshdir ( -> the directory cntaining key files)
 * in the system properties so the VFS library can find your key directory.
 * 
 * @TODO: check implementation works with key based authentication
 */
public class SFTPFile extends FTPFile  implements Serializable{
	
	private static final long serialVersionUID = 4882865598390596874L;

	private static final Logger logger = Logger.getLogger(SFTPFile.class);

	private String keyFingerprint;
	private String privateKeyPassPhrase;
	//because of jsch using a file path this NEEDS to be on the file system.
	private DiskFile privateKey;
	/**
	 * constructor to force linking of this object to the SupportedFileType that created it
	 * @param fileType
	 */
	public SFTPFile(SupportedFileType fileType) {
		super(fileType);
	}
	
	@Override
	protected String generateServerUri(){
		String uri = new String(fileType.getUrlScheme() + "://");
		//user password taken care of va authentication classes so they don't appear in uri
		uri = uri.concat(host + ((port!=null)?":"+port.toString() : "")+"/");
		logger.debug("sftp server only uri: " + uri);
		return uri;
	}
	
	/**
	 * setup authentication and set file options
	 * @return
	 */
	@Override
	protected void setUserAuthenticator() throws FileSystemException{
		//use our custom extension of the user authenticator which allows for keys to be used.
		UserAuthenticator auth = new UserAuthenticatorImpl(null, this.user,
                this.password,this.privateKeyPassPhrase,keyFingerprint,privateKey);
        this.opts = new FileSystemOptions();
        SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);
        SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, FileSystemManagerImpl.getInstance().getDefaultSessionSocketTimeOut()*1000);
        DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts,auth);
	}

	/**
	 * @return the keyFingerprint
	 */
	public String getKeyFingerprint() {
		return keyFingerprint;
	}

	/**
	 * @param keyFingerprint the keyFingerprint to set
	 */
	public void setKeyFingerprint(String keyFingerprint) {
		this.keyFingerprint = keyFingerprint;
	}

	/**
	 * @return the privateKey
	 */
	public DiskFile getPrivateKey() {
		return privateKey;
	}

	/**
	 * @param privateKey the privateKey to set
	 */
	public void setPrivateKey(DiskFile privateKey) {
		this.privateKey = privateKey;
	}

	/**
	 * @return the privateKeyPassPhrase
	 */
	public String getPrivateKeyPassPhrase() {
		return privateKeyPassPhrase;
	}

	/**
	 * @param privateKeyPassPhrase the privateKeyPassPhrase to set
	 */
	public void setPrivateKeyPassPhrase(String privateKeyPassPhrase) {
		this.privateKeyPassPhrase = privateKeyPassPhrase;
	}
	
	
}
