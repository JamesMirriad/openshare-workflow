package com.openshare.file.utils;

import java.lang.reflect.Constructor;

import org.apache.commons.vfs2.provider.FileProvider;
import org.apache.log4j.Logger;



import com.openshare.file.utils.exception.FileUtilException;
import com.openshare.file.utils.filetypes.SupportedFileType;
import com.openshare.file.utils.filetypes.vfs.FileAbstractVfsImpl;
/**
 * generic factory class that instantiates objects implementing the 
 * FileSystemObject interface that are listed in the 
 * SupportedFileType enumeration 
 * @author james.mcilroy
 *
 */
public class FileFactory {

	private static final Logger logger = Logger.getLogger(FileFactory.class);
	/**
	 * static instance for sinleton pattern
	 */
	private static FileFactory instance;
	
	/**
	 * private constructor
	 */
	private FileFactory(){
		
	}
	
	/**
	 * return an instance of the sagepay file factory
	 * @return
	 */
	public static FileFactory GetInstance(){
		if(instance==null){
			instance = new FileFactory();
		}
		return instance;
	}
	
	/**
	 * create a file given a uri
	 * @param fileUri
	 * @return
	 * @throws FileUtilException
	 */
	public FileSystemObject create(String fileUri) throws FileUtilException{
		//need to identify the file type so we can decompose the URI.
		if(fileUri.contains(":/")){
			String scheme = fileUri.substring(0, fileUri.indexOf(":/"));
			logger.info("creating file with known scheme: " + scheme);
			SupportedFileType type = SupportedFileType.getByScheme(scheme);
			FileSystemObject result = FileFactory.GetInstance().create(type);
			((FileAbstractVfsImpl)result).setParamsFromUri(fileUri);
			return result;
		}
		else{
			logger.info("creating file with unknown scheme, assuming local disk file");
			//default to disk file...
			FileSystemObject result = FileFactory.GetInstance().create(SupportedFileType.DISK_FILE);
			((FileAbstractVfsImpl)result).setParamsFromUri(fileUri);
			return result;
		}
	}
	/**
	 * create the corresponding SagePayFile type from the given params
	 * @param params
	 * @return
	 * @throws FileUtilException
	 */
	public FileSystemObject create(SupportedFileType requestedFileType) throws FileUtilException{
		//get the class we are interested in
		Class<?> clazz = requestedFileType.getImplementingClass();
		try {
			//try to find the constructor that takes in the SupportedFileType
			Constructor<?> constructor = clazz.getConstructor(SupportedFileType.class);
			//attempt o instantiate an object of this class type
			Object o = constructor.newInstance(requestedFileType);
			//if it is of the correct type, then we can return it.
			if(o instanceof FileSystemObject){
				logger.debug("created file of type: " + o.getClass().getName());
				return (FileSystemObject)o;
			}
			else{
				throw new FileUtilException("constructed an illegal class for FileFactory, type returned was: "+o.getClass().getName());
			}
		} 
		catch (Exception e) {
			throw new FileUtilException("failed to create file instance from params, cause: ",e);
		}
	}
	/**
	 * create the corresponding FileProvider type from the given params
	 * @param params
	 * @return
	 * @throws FileUtilException
	 */
	public FileProvider createFileProvider(SupportedFileType requestedFileType) throws FileUtilException{
		//get the class we are interested in
		Class<?> clazz = requestedFileType.geFileProviderClass();
		try {
			//attempt o instantiate an object of this class type
			Object o = clazz.newInstance();
			//if it is of the correct type, then we can return it.
			if(o instanceof FileProvider){
				return (FileProvider)o;
			}
			else{
				throw new FileUtilException("constructed an illegal class for FileFactory, type returned was: "+o.getClass().getName());
			}
		} 
		catch (Exception e) {
			throw new FileUtilException("failed to create file instance from params, cause: ",e);
		}
	}
}
