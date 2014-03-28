package com.openshare.file.utils.filetypes.vfs.impl.session;

import java.io.Serializable;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.log4j.Logger;



import com.openshare.file.utils.FileSystemManagerImpl;
import com.openshare.file.utils.exception.MirriadFileParamsException;
import com.openshare.file.utils.filetypes.SupportedFileType;
import com.openshare.file.utils.filetypes.vfs.session.AbstractVfsSessionBasedImpl;
/**
 * File type representing a RAM file (ie one n memory) 
 * allowing us to have a memory based file system.
 * 
 * A word of warning, deleting a file in RAM also deletes the 
 * underlying folder structure unless there is another file in the same folder, 
 * be warned if you implement code checking for folders to exist if they are 
 * empty after you have deleted something. 
 * 
 * @TODO: investigate above issue to see if it can be negated
 * 
 * @author james.mcilroy
 *
 */
public class RAMFile extends AbstractVfsSessionBasedImpl implements Serializable{
	
	private static final long serialVersionUID = -1168165200847241308L;

	private static final Logger logger = Logger.getLogger(RAMFile.class);
	
	protected FileSystemOptions opts = null;
	
	/**
	 * constructor to force linking of this object to the SupportedFileType that created it
	 * @param fileType
	 */
	public RAMFile(SupportedFileType fileType) {
		super(fileType);
	}
	
	@Override
	protected String convertPathToFileSystemSpecific(String path) {
		if(path.startsWith("/") && path.length() > 1){
			path = path.substring(1);
		}
		if(!path.endsWith("/")){
			path = path.concat("/");
		}
		return path;
	}

	@Override
	public FileSystemManager getFileSystemManager() throws FileSystemException {
		return FileSystemManagerImpl.getInstance().getFileSystemManager();
	}
	
	/**
	 * generates the parent URI
	 */
	protected String generateParentUri() {
		//make sure file path is sanitized
		if(filePath.contains("\\")){
			filePath = filePath.replace("\\", "/");
		}
		String uri = new String(fileType.getUrlScheme() + ":/"  + this.filePath);
		if(!this.filePath.endsWith("/")){
			uri = uri.concat("/");
		}
		logger.debug("parent uri: " + uri);
		return uri;
	}

	@Override
	protected FileObject resolveFile() throws FileSystemException{
		return resolveFileWithOpts(); 
	}
	
	/**
	 * resolves a file with the current file system opts set for this file
	 * @return
	 * @throws FileSystemException
	 */
	private FileObject resolveFileWithOpts() throws FileSystemException{
		//set user authentication
		this.opts = new FileSystemOptions();
		//do either directory (parent) only or file
		return getFileSystemManager().resolveFile(generateParentUri(),opts); 
	}
	
	@Override
	/**
	 * makes sure we sanitize user input to what we want.
	 */
	public void setFilePath(String filePath){
		if(filePath.contains("\\")){
			this.filePath = filePath.replace("\\", "/");
		}
		else{
			this.filePath = filePath;
		}
	}
	
	@Override
	public void finalize(){
		//we may close this file, but do not want to close the entire file system...
	}
	
	@Override
	public void setParamsFromUri(String uri) throws MirriadFileParamsException {
		if(uri==null || uri.isEmpty()){
			throw new MirriadFileParamsException("invalid uri passed into setparamsFromUri");
		}
		//do some file seperator replacement, we'll just replace so that we only have 
		//"/" characters instead of "\" ones
		uri = uri.replace('\\', '/');
		//strip any leading characters as not needed:
		setFileNameAndPathParamsFromUri(uri);
	}
	
}
