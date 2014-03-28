package com.openshare.file.utils;

import org.apache.commons.vfs2.CacheStrategy;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.cache.DefaultFilesCache;
import org.apache.commons.vfs2.impl.DefaultFileSystemManager;
import org.apache.log4j.Logger;

import com.openshare.file.utils.filetypes.SupportedFileType;
import com.openshare.file.utils.filetypes.vfs.FileAbstractVfsImpl;
import com.openshare.util.properties.PropertiesLoader;
/**
 * Our implementation of the file system manager, use this to configure all options, operations that we want. 
 * This should load in everything we need, set default File Providers and set up all options required.
 * To set the required file provider for a file type, see the SupportedFileType enum and add it there. 
 * This allows us to custom extend/override file providers if we need to.
 * @author james.mcilroy
 *
 */
public class FileSystemManagerImpl {

	private static final Logger logger = Logger.getLogger(FileSystemManagerImpl.class);
	
	private static final String FILE_SYSTEMS_SETTINGS_PROPERTIES_FILE = "file-systems-settings.properties";
	private static final int DEFAILT_SESSION_DATA_TIMEOUT = 120;
	private static final int DEFAULT_SESSION_SOCKET_TIMEOUT = 120;
	/*
	 * use a standard file manager, if one is to be creatd from scratch completely, 
	 * try DefaultFileSystemManager, but everything would need to be set. This way we just 
	 * override the "standard" one with our own capabilities as and when needed.
	 */
	private static DefaultFileSystemManager manager = null;
	private static FileSystemManagerImpl instance = null;
	
	private boolean useExternalVfsConfig = false;
	private String externalConfigUri=null;
	
	private int defaultSessionDataTimeOut = DEFAILT_SESSION_DATA_TIMEOUT;
	private int defaultSessionSocketTimeOut = DEFAULT_SESSION_SOCKET_TIMEOUT;
	
	/**
	 * default cnstructor
	 */
	private FileSystemManagerImpl(){
		try {
			setupFileSystemManager();
		} catch (FileSystemException e) {
			logger.error("returning default VFS file manager as custom instantiation of mirriad file system manager encountered an error",e);
		}
	}
	
	/**
	 * private constructor for external cnfgs via supplied file
	 * @param useExternalVfsConfig
	 * @param externalConfigUri
	 */
	private FileSystemManagerImpl(boolean useExternalVfsConfig,String externalConfigUri){
		//set variables
		this.useExternalVfsConfig = useExternalVfsConfig;
		this.externalConfigUri = externalConfigUri;
		//go setup file system manager
		try {
			setupFileSystemManager();
		} catch (FileSystemException e) {
			logger.error("returning default VFS file manager as custom instantiation of mirriad file system manager encountered an error",e);
		}
	}
	/**
	 * get instance of the file system manager which contains all providers
	 * @return
	 */
	public synchronized static FileSystemManagerImpl getInstance(){
		return getInstance(false,null);
	}
	
	/**
	 * get instance of the file system manager which contains all providers
	 * @return
	 */
	public synchronized static FileSystemManagerImpl getInstance(boolean useExternalVfsConfig,String externalConfigUri){
		if (instance==null){
			instance = new FileSystemManagerImpl(useExternalVfsConfig,externalConfigUri);
		}
		return instance;
	}
	
	/**
	 * get hold of our custom manager
	 * @return
	 */
	public synchronized FileSystemManager getFileSystemManager(){
		try {
			if(manager==null){
				setupFileSystemManager();
			}
			return manager;
		} catch (FileSystemException e) {
			//failure
			logger.error("returning default VFS file manager as custom instantiation of sage pay file system manager encountered an error",e);
			try {
				return VFS.getManager();
			} catch (FileSystemException e1) {
				logger.error("total failure getting a file syste manager, cause: ",e1);
				return null;
			}
		}
	}
	
	/**
	 * sets up a new file system manager
	 */
	private synchronized void setupFileSystemManager() throws FileSystemException{
		logger.info("re-initializing file manager instance");
		try{
			PropertiesLoader loader = new PropertiesLoader(FILE_SYSTEMS_SETTINGS_PROPERTIES_FILE);
			defaultSessionDataTimeOut = Integer.parseInt(loader.getProperty("file.system.ftp.timeout.data"));
			defaultSessionSocketTimeOut = Integer.parseInt(loader.getProperty("file.system.ftp.timeout.socket"));
		}
		catch(Throwable t){
			//if we caught an error, just ignore it, no properties file was available, use default settings instead
			logger.warn("could not find properties file: " + FILE_SYSTEMS_SETTINGS_PROPERTIES_FILE + " using default settings");
		}
		logger.info("ftp data timeout       : " + defaultSessionDataTimeOut+"s");
		logger.info("ftp/sftp socket timeout: " + defaultSessionSocketTimeOut+"s");
		//setup all the file system options. close an existing manager if it exists.
		if(manager!=null){
			manager.close();
			manager = null;
		}
		//create a new file system manager7
		logger.info("Creating new file system manager");
		manager = new  DefaultFileSystemManager();
		FileFactory factory = FileFactory.GetInstance();
		//add all the roviders as we have set them per file type
		for(SupportedFileType spft : SupportedFileType.values()){
			try {
				//only for classes that implement vfs, only add base types to avoid adding in multiple schemes.
				if(spft.isBaseType() && factory.create(spft) instanceof FileAbstractVfsImpl){
					try {
						logger.info("Adding provider for " + spft.getUrlScheme());
						manager.addProvider(spft.getUrlScheme(), factory.createFileProvider(spft));
					} catch (Exception e) {
						logger.error("failed to add " + spft.toString() + " implementation to global file system manager, cause: ", e);
					}
				}

			} catch (Exception e) {
				logger.error("failed to add " + spft.toString() + " implementation to global file system manager, cause: ", e);
			}
		}
		manager.setFilesCache(new DefaultFilesCache());
		//set caching strategy
		manager.setCacheStrategy(CacheStrategy.ON_RESOLVE);
		//load in a custom config.xml? (if not it'll look for the default in the vfs jar, we do
		//this as web containers complain about this a little bit :)
//		if(useExternalVfsConfig){
//			URL url2;
//			try {
//				url2 = new URL(externalConfigUri);
//				if(url2!=null){
//					manager.setConfiguration(url2);
//				}
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
		//initialise the manager so it's ready for use
		logger.info("initialising manager");
		manager.init();
		logger.info("initialised manager");
	}
	
	/**
	 * forces the file system associated with the supplied file type to be closed. Can be used in 
	 * the event of wanting to clean up a file system that is not handled automatically (RAM for instance) 
	 * so resources can be freed up. Take note that closing a file system closes external connections or
	 * in the case of volatile storage removes anything on it.
	 * @param spft
	 */
	public synchronized void forceCloseFileSystem(FileSystemObject sysObject){
		if(manager!=null){
			try {
				if(sysObject instanceof FileAbstractVfsImpl){
					FileAbstractVfsImpl cast = (FileAbstractVfsImpl)sysObject;
					cast.closeFileSystem();
				}
				else{
					logger.info("no close file system implementation available, object is not a VFS supported object, ignoring");
				}
			} catch (Exception e) {
				logger.error("failed to close associated file system manager, cause: ", e);
			}
		}
	}
	/**
	 * closes the internal file manager an all providers.
	 */
	public synchronized void close(){
		if(manager!=null){
			manager.close();
		}
		manager = null;
	}
	
	

	/**
	 * @return the useExternalVfsConfig
	 */
	public boolean isUseExternalVfsConfig() {
		return useExternalVfsConfig;
	}

	/**
	 * @param useExternalVfsConfig the useExternalVfsConfig to set
	 */
	public void setUseExternalVfsConfig(boolean useExternalVfsConfig) {
		this.useExternalVfsConfig = useExternalVfsConfig;
	}

	/**
	 * @return the externalConfigUri
	 */
	public String getExternalConfigUri() {
		return externalConfigUri;
	}

	/**
	 * @param externalConfigUri the externalConfigUri to set
	 */
	public void setExternalConfigUri(String externalConfigUri) {
		this.externalConfigUri = externalConfigUri;
	}

	/**
	 * finalize to enforce object cleanup.
	 */
	@Override
	public void finalize(){
		close();
		//call finalize on superclass.
		try {
			super.finalize();
		} catch (Throwable e) {
			logger.error("cleanup of manager encountered an error, cause: ", e);
		}
	}

	public int getDefaultSessionDataTimeOut() {
		return defaultSessionDataTimeOut;
	}

	public void setDefaultSessionDataTimeOut(int defaultSessionDataTimeOut) {
		this.defaultSessionDataTimeOut = defaultSessionDataTimeOut;
	}

	public int getDefaultSessionSocketTimeOut() {
		return defaultSessionSocketTimeOut;
	}

	public void setDefaultSessionSocketTimeOut(int defaultSessionSocketTimeOut) {
		this.defaultSessionSocketTimeOut = defaultSessionSocketTimeOut;
	}
	
	
}
