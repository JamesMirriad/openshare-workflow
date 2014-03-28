package com.openshare.file.utils.filetypes.vfs.impl.session;

import java.io.Serializable;
import java.util.regex.Pattern;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.UserAuthenticator;
import org.apache.commons.vfs2.auth.StaticUserAuthenticator;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.apache.log4j.Logger;

import com.openshare.file.utils.FileSystemManagerImpl;
import com.openshare.file.utils.exception.MirriadFileParamsException;
import com.openshare.file.utils.filetypes.SupportedFileType;
import com.openshare.file.utils.filetypes.vfs.session.AbstractVfsSessionBasedWithFinalizeImpl;

/**
 * Non secure Ftp file, for normal ftp connections.
 * Set server to be server address or ip address 
 * (if you are using a name that needs resolution and it fails, 
 * then add it to your machine hosts file)
 * @author james.mcilroy
 */
public class FTPFile extends AbstractVfsSessionBasedWithFinalizeImpl implements Serializable {

	private static final long serialVersionUID = 671476675597753255L;

	private static final Logger logger = Logger.getLogger(FTPFile.class);
	
	protected String host;
	protected String user;
	protected String password;
	protected Integer port;
	protected transient FileSystemOptions opts = null;
	
	/**
	 * constructor to force linking of this object to the SupportedFileType that created it
	 * @param fileType
	 */
	public FTPFile(SupportedFileType fileType) {
		super(fileType);
	}
	
	protected String generateParentUri() {
		//if filePath = null, then it is considered as root
		if(this.filePath == null || filePath.isEmpty()){
			String uri = generateServerUri();	
			return uri;	
		}
		else{
			//example uri : ftp://myusername:mypassword@somehost/pub/downloads/somefile.tgz
			String uri = generateServerUri() + ((this.filePath.startsWith("/")?this.filePath.substring(1):this.filePath));	
			if(!this.filePath.endsWith("/")){
				uri = uri.concat("/");
			}
			if(uri.contains(this.user+":"+this.password+"@")){
				logger.debug("directory only uri: " + uri.replace(this.user+":"+this.password+"@", "*****:*****@"));
			}
			else if(uri.contains(this.user+"@")){
				logger.debug("directory only uri: " + uri.replace(this.user+":"+this.password+"@", "*****@"));
			}
			else{
				logger.debug("directory only uri: " + uri);
			}
			return uri;
		}
	}

	protected String generateServerUri(){
		String uri = new String(fileType.getUrlScheme() + "://");
		//only add user + pswd in if both are non null and non empty.
		if((user != null && !user.isEmpty()) && (password != null && !password.isEmpty())){
			//for usr+pswd
			uri = uri.concat(user + ":" + password + "@");
		}
		else if((user != null && !user.isEmpty())&& (password == null || password.isEmpty())){
			//if no pswd (ie, usr requires no pswd)
			uri = uri.concat(user + "@");
		}
		uri = uri.concat(host + ((port!=null)?":"+port.toString() : "")+"/");
		if(uri.contains(this.user+":"+this.password+"@")){
			logger.debug("directory only uri: " + uri.replace(this.user+":"+this.password+"@", "*****:*****@"));
		}
		else if(uri.contains(this.user+"@")){
			logger.debug("directory only uri: " + uri.replace(this.user+":"+this.password+"@", "*****@"));
		}
		else{
			logger.debug("directory only uri: " + uri);
		}
		return uri;
	}

	/**
	 * as default, just use the normal pathname we will override in classes where we need to
	 */
	public String getFullObscuredPathName(){
		String uri =  getFullPathName();
		if(uri.contains(this.user+":"+this.password+"@")){
			uri=  uri.replace(this.user+":"+this.password+"@", "*****:*****@");
		}
		else if(uri.contains(this.user+"@")){
			uri=  uri.replace(this.user+":"+this.password+"@", "*****@");
		}
		return uri;
	}
	
	/**
	 * we actually have a root, so return it.
	 */
	public String getFileSystemRoot(){
		return host;
	}
	
	@Override
	public FileSystemManager getFileSystemManager() throws FileSystemException {
		return FileSystemManagerImpl.getInstance().getFileSystemManager();
	}
	
	/**
	 * setup authentication and set file options
	 * @return
	 */
	protected void setUserAuthenticator() throws FileSystemException{
		UserAuthenticator auth = new StaticUserAuthenticator(null, this.user,
                this.password);
        this.opts = new FileSystemOptions();
        FtpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);
//        FtpFileSystemConfigBuilder.getInstance().setDataTimeout(opts, FileSystemManagerImpl.getInstance().getDefaultSessionDataTimeOut()*1000);
//        FtpFileSystemConfigBuilder.getInstance().setSoTimeout(opts, FileSystemManagerImpl.getInstance().getDefaultSessionSocketTimeOut()*2000);
        DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts,auth);
	}
	
	@Override
	protected FileObject resolveFile() throws FileSystemException{
		return resolveFileWithOpts(false); 
	}
	
	/**
	 * resolves a file with the current file system opts set for this file
	 * @return
	 * @throws FileSystemException
	 */
	private FileObject resolveFileWithOpts(boolean dirOnly) throws FileSystemException{
		//set user authentication
		setUserAuthenticator();
		//do either directory (parent) only or file.
		if(dirOnly){
			return getFileSystemManager().resolveFile(generateParentUri(),opts); 
		}
		else{
			return getFileSystemManager().resolveFile(generateUri(),opts); 
		}
	}
	//Getters and Setters

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the port
	 */
	public Integer getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(Integer port) {
		this.port = port;
	}

	@Override
	protected String convertPathToFileSystemSpecific(String path) {
		//we don't mess with this as we're seeing a ftp/sftp implementation so leave alone.
		if(path.startsWith("/") && path.length() > 1){
			path = path.substring(1);
		}
		if(!path.endsWith("/")){
			path = path.concat("/");
		}
		return path;
	}

	@Override
	public void setParamsFromUri(String uri) throws MirriadFileParamsException {
		//so we can support both ftp and sftp without rewriting code :)
		String startOfUri = fileType.getUrlScheme() + "://";
		//need to decode the start of the protocol. this HAS to start with ftp://
		if(uri == null || uri.isEmpty()){
			throw new MirriadFileParamsException("invalid uri passed in");
		}
		if(uri.startsWith(startOfUri)){
			//strip off ftp
			uri = uri.substring(startOfUri.length());
			String hostDetails = null;
			if(uri.contains("/")){
				hostDetails = uri.substring(0,uri.indexOf("/"));
			}
			//assume we are just dealing with a hostname details, ie, root login to server
			else{
				hostDetails = uri;
			}
			checkForIllegalHostCharacters(hostDetails);
			String filePathAndName = null;
			if(uri.indexOf("/") >= 0){
				filePathAndName = uri.substring(uri.indexOf("/"),uri.length());
			}
			else{
				filePathAndName = "";
			}
			//now decode host details. should at least have a hostname, user, password and port are optional.
			String [] hostDetailsSplit = hostDetails.split("@");
			String hostNameAndPort;
			if(hostDetailsSplit.length > 1){
				//we succesfull split the string, we have login details
				String [] loginDetailsSplit = hostDetailsSplit[0].split(":");
				if(loginDetailsSplit.length > 1){
					//we have a user AND a password
					this.user = loginDetailsSplit[0];
					this.password = loginDetailsSplit[1];
				}
				else if(loginDetailsSplit.length > 2){
					throw new MirriadFileParamsException("invalid uri passed in, invalid character \":\" in login details, can only have one");
				}
				else{
					this.user = hostDetailsSplit[0];
				}
				hostNameAndPort = hostDetailsSplit[1];
			}
			else{
				hostNameAndPort = hostDetails;
			}
			//parse hostname and port if there is one
			String [] hostPortDetailsSplit = hostNameAndPort.split(":");
			if(hostPortDetailsSplit.length > 1){
				//we have a user AND a password
				this.host = hostPortDetailsSplit[0];
				try{
					this.port = Integer.parseInt(hostPortDetailsSplit[1]);
				}
				catch(NumberFormatException nfe){
					throw new MirriadFileParamsException("invalid port passed in, must be an integer, " + hostPortDetailsSplit[1] + " s not valid");
				}
			}
			else if(hostPortDetailsSplit.length > 2){
				throw new MirriadFileParamsException("invalid uri passed in, invalid character \":\" in host details, can only have one");
			}
			else{
				this.host = hostNameAndPort;
			}
			//do file path and name part
			this.setFileNameAndPathParamsFromUri(filePathAndName);
		}
		else{
			throw new MirriadFileParamsException("invalid uri passed in, must start with ftp://");
		}
	}
	
	
	/**
	 * to be implemented by subclasses so that they can do some pre validation on the incoming uri
	 * this implementation is for file name and path
	 * @param uri
	 */
	protected void checkForIllegalHostCharacters(String uri) throws MirriadFileParamsException{
		String [] invalidCharSequences = {"::",";","*","~","#","^","/"};
		String [] canOnlyOccurOnce = {"@"};
		String [] cannlyOccurTwiceMax = {":"};
		
		for(String invalid : invalidCharSequences){
			if(uri.contains(invalid)){
				throw new MirriadFileParamsException("uri for fileName/filePath connot contain character sequence: " + invalid);
			}
		}
		
		for(String invalid : canOnlyOccurOnce){
			if(uri.split(Pattern.quote(invalid)).length > 2){
				throw new MirriadFileParamsException("uri for fileName/filePath connot contain character sequence more than once: " + invalid);
			}
		}
		
		for(String invalid : cannlyOccurTwiceMax){
			if(uri.split(Pattern.quote(invalid)).length > 3){
				throw new MirriadFileParamsException("uri for fileName/filePath connot contain character sequence more than twice: " + invalid);
			}
		}
	}
}
