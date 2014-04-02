package com.openshare.file.utils.filetypes.vfs.impl;

import java.io.File;
import java.io.Serializable;
import java.util.regex.Matcher;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.log4j.Logger;



import com.openshare.file.utils.FileSystemManagerImpl;
import com.openshare.file.utils.exception.MirriadFileParamsException;
import com.openshare.file.utils.filetypes.SupportedFileType;
import com.openshare.file.utils.filetypes.vfs.FileAbstractVfsImpl;
/**
 * Local File implementation representing a file on the disk.
 * set the oot to point to a disk, eg:
 * in windows "C:" or equivalent
 * in linux "mnt1" or equivalent
 * @author james.mcilroy
 *
 */
public class DiskFile extends FileAbstractVfsImpl implements Serializable {

	private static final long serialVersionUID = -8994859906558625606L;

	private static final Logger logger = Logger.getLogger(DiskFile.class);

	protected String rootName;
	
	/**
	 * constructor to force linking of this object to the SupportedFileType that created it
	 * @param fileType
	 */
	public DiskFile(SupportedFileType fileType) {
		super(fileType);
	}
	/**
	 * generates the parent URI
	 */
	protected String generateParentUri() {
		String uri = new String(fileType.getUrlScheme() + "://" + rootName + File.separator + this.filePath);
		if(!uri.endsWith("\\") && !uri.endsWith("/")){
			uri = uri.concat(File.separator);
		}
		logger.debug("parent uri: " + uri);
		return uri;
	}
	
	/**
	 * generates full path name as per file system.
	 */
	public String getFullPathName(){
		String fullPath = new String(rootName + File.separator + this.filePath);
		if(!fullPath.endsWith("\\") && !fullPath.endsWith("/")){
			fullPath = fullPath.concat(File.separator);
		}
		if(this.fileName!=null){
			fullPath = fullPath.concat(this.fileName);
		}
		if(!fullPath.contains(":\\") && !fullPath.contains("://")){
			if(!fullPath.startsWith("/")){
				fullPath = "/".concat(fullPath);
			}
		}
		return fullPath;
	}
	
	/**
	 * this does the same as above, BUT it means that any subclasses WILL 
	 * always generate the path as per diskfile (like hubfile, whic needs this to get the relative path)
	 * @return
	 */
	public final String generateRelativePath(){
		String fullPath = new String(rootName + File.separator + this.filePath);
		if(!fullPath.endsWith(File.separator)){
			fullPath = fullPath.concat(File.separator);
		}
		fullPath = fullPath.concat(this.fileName);
		return fullPath;
	}
	
	@Override
	public FileSystemManager getFileSystemManager() throws FileSystemException {
		return FileSystemManagerImpl.getInstance().getFileSystemManager();
	}

	/**
	 * @return the rootName
	 */
	public String getRootName() {
		return rootName;
	}

	/**
	 * @param rootName the rootName to set
	 */
	public void setRootName(String rootName) {
		this.rootName = rootName;
	}

	@Override
	protected String convertPathToFileSystemSpecific(String path) {
		String currentReplaceChar;
		if("/".equals(File.separator)){
			currentReplaceChar = "\\";
		}
		else{
			currentReplaceChar = "/";
		}
		path = path.replaceAll(Matcher.quoteReplacement(currentReplaceChar), Matcher.quoteReplacement(File.separator));
		if(path.startsWith(File.separator) && path.length() > 1){
			path = path.substring(1);
		}
		if(!path.endsWith(File.separator)){
			path = path.concat(File.separator);
		}
		return path;
	}
	
	/**
	 * we actually have a root, so return it.
	 */
	public String getFileSystemRoot(){
		return this.getRootName();
	}
	
	@Override
	public void setParamsFromUri(String uri) throws MirriadFileParamsException {
		if(uri==null || uri.isEmpty()){
			throw new MirriadFileParamsException("invalid uri passed into setparamsFromUri");
		}
		//do some file seperator replacement, we'll just replace so that we only have 
		//"/" characters instead of "\" ones
		uri = uri.replace('\\', '/');
		//try and find the root
		//support unix/linux:
		if(uri.startsWith("/"))
		{
			uri = uri.substring(1);
		}
		rootName = uri.substring(0,uri.indexOf('/'));
		//the next bit should be the path
		if(uri.length() > uri.indexOf('/')+1){
			uri = uri.substring(uri.indexOf('/'), uri.length());
			this.setFileNameAndPathParamsFromUri(uri);
		}
	}
}
