package com.openshare.file.utils.filetypes;

import org.apache.commons.vfs2.provider.FileProvider;
import org.apache.commons.vfs2.provider.ftp.FtpFileProvider;
import org.apache.commons.vfs2.provider.http.HttpFileProvider;
import org.apache.commons.vfs2.provider.https.HttpsFileProvider;
import org.apache.commons.vfs2.provider.local.DefaultLocalFileProvider;
import org.apache.commons.vfs2.provider.ram.RamFileProvider;

import com.openshare.file.utils.FileSystemObject;
import com.openshare.file.utils.exception.MirriadFileTypeNotRecognisedException;
import com.openshare.file.utils.filetypes.vfs.impl.DiskFile;
import com.openshare.file.utils.filetypes.vfs.impl.session.FTPFile;
import com.openshare.file.utils.filetypes.vfs.impl.session.HTTPFile;
import com.openshare.file.utils.filetypes.vfs.impl.session.HTTPSFile;
import com.openshare.file.utils.filetypes.vfs.impl.session.RAMFile;
import com.openshare.file.utils.filetypes.vfs.impl.session.SFTPFile;
import com.openshare.file.utils.filetypes.vfs.impl.session.sftp.SftpFileProviderImpl;
/**
 * Enumeration of all available file types currently supported 
 * in the system and tying them to their underlying implementations.
 * 
 * To add an implemetation in VFS extend FileAbstractVfsImpl or one of it's subclasses
 * To add a non VFS implementation implement the FileSystemObject and go from there.
 * 
 * if non VFS objects are added this will need a different constructor as te FileProvider + urlScheme field will be useless (can be nulled though)
 * 
 * VFS:
 * - set the url scheme this file is applicable to
 * - set the class we'd like to use. We can assign any class to any uri scheme (ie, we could make 
 * up one called "sagepay" and set this to use a RAMFile with a CustomRamFileProvider 
 * if we wished, just here to provide that flexibility, also for complete custom implementations)
 * - Set the provider class we'd like to use
 * 
 * @author james.mcilroy
 *
 * @TODO: if we need to start associating specified mime types and other configurable bits to do with file 
 * systems, we should link them in here and then extract them in the FileSystemManagerImpl class when we're 
 * programatically creating our custom file manager singleton instance
 */
public enum SupportedFileType {
	//File base types, any extended types should be in the below extended file types. 
	//This is because the manager uses these to set the uri base and file provider extensions 
	//which should only happen once (one uri base PER file provider)
	/**
	 * BASE TYPES
	 */
	DISK_FILE 		(true,DiskFile.class,"file",DefaultLocalFileProvider.class),
	FTP_FILE 		(true,FTPFile.class	,"ftp", FtpFileProvider.class),
	SFTP_FILE  		(true,SFTPFile.class,"sftp",SftpFileProviderImpl.class),
	RAM_FILE		(true,RAMFile.class ,"ram", RamFileProvider.class),
	HTTP_FILE		(true,HTTPFile.class,"http", HttpFileProvider.class),
	HTTPS_FILE		(true,HTTPSFile.class,"https", HttpsFileProvider.class);
	/**
	 * EXTENDED TYPES
	 */
	
	//tied down to only allow instances of the interface type
	private final Class<? extends FileSystemObject> implementingClass;
	private final Class<? extends FileProvider> fileProviderClass;
	private final String urlScheme;
	private final boolean baseType;
	/**
	 * private enum constructor
	 * @param implementingClass
	 */
	private SupportedFileType(boolean baseType,Class<? extends FileSystemObject> implementingClass,String urlScheme, Class<? extends FileProvider> fileProviderClass){
		this.implementingClass = implementingClass;
		this.fileProviderClass = fileProviderClass;
		this.urlScheme = urlScheme;
		this.baseType = baseType;
	}

	/**
	 * return the implementing class
	 * @return
	 */
	public Class<? extends FileSystemObject> getImplementingClass() {
		return implementingClass;
	}
	
	/**
	 * return the implementing class
	 * @return
	 */
	public Class<? extends FileProvider> geFileProviderClass() {
		return fileProviderClass;
	}

	/**
	 * @return the urlScheme
	 */
	public String getUrlScheme() {
		return urlScheme;
	}
	
	
	/**
	 * @return the baseType
	 */
	public boolean isBaseType() {
		return baseType;
	}

	/**
	 * if the object passed in is a valid file system object, then return the type!
	 * @param object
	 * @return
	 * @throws MirriadFileTypeNotRecognisedException
	 */
	public static SupportedFileType findFileType(FileSystemObject object) throws MirriadFileTypeNotRecognisedException{
		for(SupportedFileType spft : SupportedFileType.values()){
			if(object.getClass().getName().equals(spft.getImplementingClass().getName())){
				return spft;
			}
		}
		throw new MirriadFileTypeNotRecognisedException("the object: " + object.getClass().getName() + " is not a file system object type");
	}
	
	public static SupportedFileType getByScheme(String scheme) throws MirriadFileTypeNotRecognisedException{
		//figure out what file type corresponds top the requested scheme
		for(SupportedFileType spft : SupportedFileType.values()){
			if(spft.getUrlScheme().equals(scheme)){
				return spft;
			}
		}
		throw new MirriadFileTypeNotRecognisedException("the scheme: " + scheme + " could not be paired with a supported file type");
	}
}
