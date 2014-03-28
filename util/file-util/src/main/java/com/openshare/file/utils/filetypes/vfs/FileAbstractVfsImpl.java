package com.openshare.file.utils.filetypes.vfs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.vfs2.AllFileSelector;
import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileNotFolderException;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileType;
import org.apache.log4j.Logger;



import com.openshare.file.utils.FileSystemObject;
import com.openshare.file.utils.exception.FileUtilException;
import com.openshare.file.utils.exception.MirriadFileParamsException;
import com.openshare.file.utils.filetypes.SupportedFileType;
/**
 * An apache commons VFS based representation on a local or remote file system.
 * VFS supports many filesystem types that are not implemented, for extensions, 
 * please add enum types to SupportedFileType and create class plus any overriding behaviour.
 * 
 * For more details on apache commons VFS visit:
 * http://commons.apache.org/vfs/
 * 
 * For more information on the file types VFS supports visit:
 * http://commons.apache.org/vfs/filesystems.html
 * 
 * @author james.mcilroy
 *
 */
public abstract class FileAbstractVfsImpl implements FileSystemObject,Cloneable,Serializable{

	private static final long serialVersionUID = 545837765918951937L;

	private static final Logger logger = Logger.getLogger(FileAbstractVfsImpl.class);
	
	public static final int BUFFER_SIZE = 1024;
	protected SupportedFileType fileType;
	protected String fileName;
	protected String filePath;
	/**
	 * super constructor as we need to enforce it to take a params object to initialise it.
	 * @throws FileSystemException 
	 */
	public FileAbstractVfsImpl(SupportedFileType fileType){
		this.fileType = fileType;
	}
	
	/**
	 * internal call to resolve a file and get the file object
	 * @return
	 * @throws FileSystemException
	 */
	protected FileObject resolveFile() throws FileSystemException{
		return getFileSystemManager().resolveFile(generateUri()); 
	}
	
	/**
	 * simply examines to see whether there is a filename
	 */
	public final boolean isFolder(){
		if(this.getFullPathName().endsWith("/") || this.getFullPathName().endsWith("\\")){
			return true;
		}
		return (fileName == null || fileName.isEmpty());
	}
	//ABSTRACT METHODS
	/**
	 * internal use required to generate the uri for the file we want
	 * @return
	 */
	protected abstract String generateParentUri();
	
	/**
	 * use a uri to set as many file params as possible
	 * @param uri
	 * @throws MirriadFileParamsException
	 */
	public abstract void setParamsFromUri(String uri) throws MirriadFileParamsException;
	
	/**
	 * returnsd the appropriate file system manager
	 * @return
	 * @throws FileSystemException 
	 */
	public abstract FileSystemManager getFileSystemManager() throws FileSystemException;

	//METHODS
	public long getSize() throws FileUtilException{
		int size = 0;
		try{
			if(exists()){
				if(isFolder()){
					//in the case of a folder, we should return the size of ALL files beneath it.
					List<FileSystemObject> allFiles = getFileSystemFileOnlyTree();
					for(FileSystemObject file : allFiles){
						if(!file.isFolder()){
							//add all the size of all the files together
							size += file.getSize();
						}
					}
				}
				else{
					//is a file, return the size
					FileObject fileObject = this.resolveFile();
					FileContent content = fileObject.getContent();
					if(content!=null){
						return content.getSize();
					}
				}
			}
		}
		catch(FileSystemException fse){
			throw new FileUtilException("failed to get size of object due to error, cause: ",fse);
		}
		finally{
			closeFileSystem();
		}
		return size;	
	}
	
	/**
	 * gets the time this file was last modified
	 * @return
	 * @throws FileUtilException
	 */
	public Date getModified() throws FileUtilException{
		try{
			if(exists()){
				//is a file, return the size
				FileObject fileObject = this.resolveFile();
				FileContent content = fileObject.getContent();
				if(content!=null){
					return new Date(content.getLastModifiedTime());
				}
			}
			throw new FileSystemException("cannot retrieve a last modified date as file does not exist");
		}
		catch(FileSystemException fse){
			throw new FileUtilException("failed to get size of object due to error, cause: ",fse);
		}
		finally{
			closeFileSystem();
		}
	}
	/**
	 * generates the uri of the file
	 */
	public String generateUri() {
		return generateParentUri() + (fileName!=null && !fileName.isEmpty() ? fileName : "");
	}

	
	/**
	 * generates full path name as per file system. For most things this generates a full uri, 
	 * except for some systems where a native lookup need the actual path with the protocol attached.
	 */
	public String getFullPathName(){
		return generateUri();
	}
	
	/**
	 * as default, just use the normal pathname we will override in classes where we need to
	 */
	public String getFullObscuredPathName(){
		return getFullPathName();
	}
	
	/**
	 * returns a "file" representation of the parent folder
	 * if we attempt to get parent and this is the root, then we return "null"
	 * @return
	 */
	public FileSystemObject getParent() throws FileUtilException{
		try {
			//clone current object as a fast way of keeping data such as server or root...
			FileObject fileObject = this.resolveFile();
			FileObject parent = fileObject.getParent();
			if (parent==null){
				return null;
			}
			//do a clone
			FileAbstractVfsImpl clone = (FileAbstractVfsImpl)this.clone();
			//if parent is a file type, then return that
			if(parent.getType()==FileType.FILE){
				clone.setFileName(parent.getName().getBaseName());
				clone.setFilePath(this.getFilePath());
			}
			//otherwise we are dealing with a folder
			else{
				clone.setFileName(null);
				String path = this.getFilePath();
				//take the file path.
				if(this.getFilePath().endsWith("\\")){
					path = path.substring(0,path.lastIndexOf("\\"));
					path = path.substring(0,path.lastIndexOf("\\")+1);
				}
				else if(this.getFilePath().endsWith("/")){
					path = path.substring(0,path.lastIndexOf("/"));
					path = path.substring(0,path.lastIndexOf("/")+1);
				}
				clone.setFilePath(path);
			}
			return clone;
		} 
		catch (FileSystemException e) {
			 throw new FileUtilException("failed to retrieve parent of this object, cause: ",e);
		} 
		catch (CloneNotSupportedException e) {
			throw new FileUtilException("failed to create parent object as sage pay file type is not cloneable, cause: ",e);
		}
		finally{
			closeFileSystem();
		}
	}
	
	protected abstract String convertPathToFileSystemSpecific(String path);
	/**
	 * get associated file system manager
	 * @return
	 * @throws FileUtilException
	 */
	public FileSystem getFileSystem() throws FileUtilException{
		try{
			return this.resolveFile().getFileSystem();
		} catch (FileSystemException e) {
			throw new FileUtilException("failure accessing file object, cause: ",e);
		}
	}
	//SHARED METHODS
	/**
	 * is the file readable
	 * @return
	 * @throws FileUtilException
	 */
	public boolean canRead() throws FileUtilException{
		try {
			return exists() && this.resolveFile().isReadable();
		} catch (FileSystemException e) {
			throw new FileUtilException("failure accessing file object, cause: ",e);
		}
		finally{
			closeFileSystem();
		}
	}
	
	/**
	 * is the file writeable
	 * @return
	 * @throws FileUtilException
	 */
	public boolean canWrite() throws FileUtilException{
		try {
			return resolveFile().isWriteable();
		} catch (FileSystemException e) {
			throw new FileUtilException("failure accessing file object, cause: ",e);
		}
		finally{
			closeFileSystem();
		}
	}
	
	/**
	 * does the file exist
	 * @return
	 * @throws FileUtilException
	 */
	public boolean exists() throws FileUtilException{
		try{
			return resolveFile().exists();
		} catch (FileSystemException e) {
			throw new FileUtilException("failure accessing file object, cause: ",e);
		}
		finally{
			closeFileSystem();
		}
	}
	/**
	 * is the file hidden
	 * @return
	 * @throws FileUtilException
	 */
	public boolean isHidden() throws FileUtilException{
		try {
			return exists() && resolveFile().isHidden();
		} catch (FileSystemException e) {
			throw new FileUtilException("failure accessing file object, cause: ",e);
		}
		finally{
			closeFileSystem();
		}
	}

	/**
	 * closes the file system associated with this file object
	 * @throws FileUtilException
	 */
	public void closeFileSystem() throws FileUtilException{
		try{
			FileObject fileObject = resolveFile();
			if(fileObject!=null){
				this.getFileSystemManager().closeFileSystem(fileObject.getFileSystem());
			}
		}
		catch(Exception e){
			throw new FileUtilException("failed to close file system, cause: ",e);
		}
	}
	
	/**
	 * convenience method so we can write data from a supplied byte array
	 * @param data
	 * @throws FileUtilException
	 */
	public void write(byte [] data) throws FileUtilException{
		//convert to an input stream
		InputStream is = new ByteArrayInputStream(data);
		write(is);
	}
	
	/**
	 * writes an input stream to a file
	 * @param is
	 */
	public void write(InputStream is) throws FileUtilException{
		
		OutputStream os=null;
		try {
			FileObject fileObject = resolveFile();
			if(isFolder()){
				if(!fileObject.exists()){
					fileObject.createFile();
				}
			}
			else if(canWrite() && is!=null){
				//if it doesn't exist, create it
				if(!fileObject.exists()){
					fileObject.createFile();
				}
				os = fileObject.getContent().getOutputStream();
				byte[] buffer = new byte[BUFFER_SIZE];
				
				int bytesread = 0;
				while(true){
					bytesread = is.read(buffer);
					if (bytesread == -1)
						break;
					os.write(buffer,0,bytesread);
				}
				fileObject.close();
			}
		} 
		catch (FileSystemException e) {
			throw new FileUtilException("could not write file, cause:",e);
		} 
		catch (IOException e) {
			throw new FileUtilException("could not write file, cause:",e);
		} 
		finally{
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			closeFileSystem();
		}
	}
	
	/**
	 * move a file to this location. if a folder, then add into there, if a 
	 * file then overwrite with new version if write permission is obtained on existing file...
	 * @param target
	 * @throws FileUtilException
	 */
	public void moveTo(FileSystemObject target) throws FileUtilException{
		//find out if this is possible
		if(!this.canRead()){
			throw new FileUtilException("cannot move from a folder/file that doesn't have read permissions");
		}
		if(!target.canWrite()){
			throw new FileUtilException("cannot move to a write protected folder/file");
		}
		//create a VFS cast
		FileAbstractVfsImpl cast;
		if(target instanceof FileAbstractVfsImpl){
			cast = (FileAbstractVfsImpl)target;
		}
		else{
			throw new FileUtilException("currently do not support moving a file object from VFS to non-VFS file system");
		}
		try{
			if( (!target.isFolder() && cast.isFolder()) ){
				throw new FileUtilException("cannot move a folder to the location of a file");
			}
			else{
				//if file object doesn't exist (target) then create it
				FileObject fileObject = cast.resolveFile();
				if(!fileObject.exists()){
					fileObject.createFile();
				}
				//move the file object.
				this.getUnderLyingFileObject().moveTo(cast.getUnderLyingFileObject());
				//now we've moved it succesfully, we should really "delete" this file as it no longer exists.
				this.delete();
			}
		}
		catch(FileSystemException fse){
			throw new FileUtilException("failed to move file, cause: ",fse);
		}
		finally{
			this.closeFileSystem();
		}
	}
	
	/**
	 * copies from the source object to the location this file sytem object points at
	 * @param source
	 */
	public void copyFrom(FileSystemObject source) throws FileUtilException{
		//let's get some error checking out of the way to make sure we have correct 
		//file permissions to perform the operation we want.
		if(!source.canRead()){
			throw new FileUtilException("cannot copy from a folder/file that doesn't have read permissions");
		}
		if(!this.canWrite()){
			throw new FileUtilException("cannot copy to a folder/file that doesn't have write permissions");
		}
		FileAbstractVfsImpl cast;
		if(source instanceof FileAbstractVfsImpl){
			cast = (FileAbstractVfsImpl)source;
		}
		else{
			throw new FileUtilException("currently do not support copying a file object from VFS to non-VFS file system");
		}
		try{
			if( (!this.isFolder() && cast.isFolder()) ){
				throw new FileUtilException("cannot copy a folder to the location of a file");
			}
			else{
				//cpy the file object and ALL descendants if they are there (ensure folder to folder copies will work).
				this.getUnderLyingFileObject().copyFrom(cast.getUnderLyingFileObject(), new AllFileSelector ());
			}
		}
		catch(FileSystemException fse){
			throw new FileUtilException("failed to copy file, cause: ",fse);
		}
		finally{
			this.closeFileSystem();
		}
	}
	
	/**
	 * convenience method
	 */
	public byte [] read() throws FileUtilException{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		read(os);
		return os.toByteArray();
	}
	
	/**
	 * get the file input stream
	 * @return
	 * @throws FileUtilException
	 */
	public InputStream getFileInputStream() throws FileUtilException{
		try {
			FileObject fileObject = resolveFile();
			return fileObject.getContent().getInputStream();
		}
		catch(Exception e){
			throw new FileUtilException("could not read file, cause: ",e);
		}
		finally{
			this.closeFileSystem();
		}
	}
	
	/**
	 * get an input stream to read the file. 
	 */
	public void read(OutputStream os) throws FileUtilException{
		InputStream is = null;
		try {
			FileObject fileObject = resolveFile();
			if(exists() && canRead() && os!=null && !isFolder()){
				is = fileObject.getContent().getInputStream();
				byte [] buffer = new byte [BUFFER_SIZE];
				int bytesread = 0;
				while(true){
					bytesread = is.read(buffer);
					if (bytesread == -1){
						break;
					}
					os.write(buffer,0,bytesread);
				}
				logger.debug("file read completed");
			}
			else{
				throw new FileUtilException("could not read file, attempt to call read on a folder,blocked read access or does not exist");
			}
		}
		catch(Exception e){
			throw new FileUtilException("could not read file, cause: ",e);
		}
		finally{
			if(is!=null){
				try {
					is.close();
				} 
				catch (IOException e) {}
			}
			this.closeFileSystem();
		}
	}

	/**
	 * deletes the file
	 * @throws FileUtilException
	 */
	public void delete() throws FileUtilException{
		try {
			//if it doesn't exist, then no point going through the motions...
			if(exists()){
				FileObject fileObject = resolveFile();
				fileObject.delete(new AllFileSelector ());
			}
		} catch (FileSystemException e) {
			throw new FileUtilException("could not delete file, cause:",e);
		}
	}
	
	/**
	 * gets an underlying file object from the VFS impelementation
	 * @return
	 * @throws FileUtilException
	 */
	protected FileObject getUnderLyingFileObject() throws FileUtilException{
		try {
			FileObject fileObject = resolveFile();
			return fileObject;
		} catch (FileSystemException e) {
			throw new FileUtilException("could not retrieve underlying file object, cause:",e);
		}
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * default implementation, just return the "scheme" of the file system + //
	 */
	public String getFileSystemRoot(){
		return this.fileType.getUrlScheme() + "://";
	}
	
	public void finalize(){
		//make sure we closer the file system underlying this object...
		try {
			closeFileSystem();
		} 
		catch (FileUtilException e) {
			logger.warn("could not clean up file system");
		}
	}
	
	/**
	 * Gets all children and all children of subsequent subfolders...
	 * @return
	 * @throws FileUtilException
	 */
	public List<FileSystemObject> getFileSystemTree() throws FileUtilException{
		List<FileSystemObject> folderChildren = new ArrayList<FileSystemObject>();
		for(FileSystemObject child : getChildren()){
			if(!child.isFolder()){
				folderChildren.add(child);
			}
			else{
				folderChildren.addAll(child.getFileSystemTree());
			}
		}
		return folderChildren;
	}
	
	/**
	 * Gets all children and all children of subsequent subfolders...
	 * @return
	 * @throws FileUtilException
	 */
	public List<FileSystemObject> getFileSystemDirectoryTree() throws FileUtilException{
		List<FileSystemObject> folderChildren = new ArrayList<FileSystemObject>();
		for(FileSystemObject child : getFileSystemTree()){
			if(!child.isFolder()){
				folderChildren.add(child);
			}
			else{
				folderChildren.addAll(child.getFileSystemTree());
			}
		}
		return folderChildren;
	}
	
	/**
	 * Gets all children and all children of subsequent subfolders...
	 * @return
	 * @throws FileUtilException
	 */
	public List<FileSystemObject> getFileSystemFileOnlyTree() throws FileUtilException{
		List<FileSystemObject> folderChildren = new ArrayList<FileSystemObject>();
		for(FileSystemObject child : getFileSystemTree()){
			if(!child.isFolder()){
				folderChildren.add(child);
			}
			else{
				folderChildren.addAll(child.getFileSystemTree());
			}
		}
		return folderChildren;
	}
	/**
	 * convenience function to retrieve directory sub folders only
	 * @return
	 * @throws FileUtilException
	 */
	public List<FileSystemObject> getSubDirectories() throws FileUtilException{
		List<FileSystemObject> folderChildren = new ArrayList<FileSystemObject>();
		for(FileSystemObject child : getChildren()){
			if(child.isFolder()){
				folderChildren.add(child);
			}
		}
		return folderChildren;
	}
	
	/**
	 * convenience function to retrieve directory sub files only
	 * @return
	 * @throws FileUtilException
	 */
	public List<FileSystemObject> getSubFiles() throws FileUtilException{
		List<FileSystemObject> fileChildren = new ArrayList<FileSystemObject>();
		for(FileSystemObject child : getChildren()){
			if(!child.isFolder()){
				fileChildren.add(child);
			}
		}
		return fileChildren;
	}
	/**
	 * goes and gets all the child objects, converts them into mirriad file system objects
	 */
	public List<FileSystemObject> getChildren() throws FileUtilException{
		List<FileSystemObject> children = new ArrayList<FileSystemObject>();
		try {
			//if it exists and we have read permissions, then continue
			if(canRead() && this.isFolder()){
				FileObject fileObject = resolveFile();
				if(fileObject!=null){
					FileObject [] fileObjectChildren = fileObject.getChildren();
					if(fileObjectChildren!=null){
						for(FileObject fo : fileObjectChildren){
							FileName fileName = fileObject.getName();
							if(fileName!=null){
								//we really need to check some stuff here, we can only support getting the children of a 
								//filesystem if the children are of the same file system, otherwise there are substantial complications.
								if(this.getUnderLyingFileObject().getName().getRootURI().equals(fileName.getRootURI())){
									FileAbstractVfsImpl clone = (FileAbstractVfsImpl)this.clone();
									//if parent is a file type, then return that
									if(fo.getType()==FileType.FILE){
										clone.setFileName(fo.getName().getBaseName());
										clone.setFilePath(this.getFilePath());
										children.add(clone);
									}
									//otherwise we are dealing with a folder. only add if we are allowing folders
									else if(fo.getType()==FileType.FOLDER){
										//no file name
										clone.setFileName(null);
										//go and find the last bit of the path, this is the folder name.
										String filePathFo = fo.getName().getPath();
										filePathFo.replace("\\", "/");
										if(filePathFo.endsWith("/")){
											filePathFo = filePathFo.substring(0, filePathFo.length()-1);
										}
										filePathFo = filePathFo.substring(filePathFo.lastIndexOf("/")+1);
										//append folder name to base file path of parent.
										clone.setFilePath(this.getFilePath()+filePathFo);
										children.add(clone);
									}
								}
								else{
									throw new FileUtilException("Currently do not support getting children of a different file system type from a file / folder");
								}
							}
						}
					}
				}
			}
		}
		catch(FileNotFolderException fnfe){
			//specifically catch this exception, as we can recover from this by returning a zero sized collection indicating this is a file
			return new ArrayList<FileSystemObject>();
		}
		catch(Exception e){
			throw new FileUtilException("failed to get child objects, cause: ",e);
		}
		return children;
	}
	

	/**
	 * checks whether the file is a is a child of the root, ie, whether this file
	 * lies under that hierarchy
	 * @param root
	 * @return
	 * @throws FileUtilException
	 */
	public boolean isChildOf(FileSystemObject root) throws FileUtilException{
		if(root==null){
			//if no root, then it cant have children
			return false;
		}
		//if it is itself, then return true
		if(this.getFullPathName().startsWith(root.getFullPathName())){
			return true;
		}
		return false;
	}
	/**
	 * sets params from a uri through decoding it
	 * @param strippedFilePathAndNamePart
	 * @throws MirriadFileParamsException
	 */
	protected void setFileNameAndPathParamsFromUri(String strippedFilePathAndNamePart) throws MirriadFileParamsException{
		//do validation check
		checkForIllegalCharacters(strippedFilePathAndNamePart);
		//sanitize
		if(strippedFilePathAndNamePart.startsWith("/"))
		{
			strippedFilePathAndNamePart = strippedFilePathAndNamePart.substring(1);
		}
		//the next bit should be the path
		if(strippedFilePathAndNamePart.length() >= strippedFilePathAndNamePart.indexOf('/')+1){
			filePath = strippedFilePathAndNamePart.substring(0, strippedFilePathAndNamePart.lastIndexOf('/')+1);
			fileName = null;
			if(strippedFilePathAndNamePart.length() > strippedFilePathAndNamePart.lastIndexOf('/') +1){
				fileName = strippedFilePathAndNamePart.substring(strippedFilePathAndNamePart.lastIndexOf('/')+1, strippedFilePathAndNamePart.length());
			}
		}
	}
	
	/**
	 * gets the base folder name of the folder this file lives in, or just the folder name if this 
	 * is the folder (excluding all parent folders)
	 * @return
	 * @throws FileSystemException 
	 */
	public String getBaseFolderName() throws FileUtilException{
		try {
			if(exists()){
				FileObject fileObject = this.resolveFile();
				if(isFolder() && fileObject.getName()!=null){
					return fileObject.getName().getBaseName();
				}
				else{
					if(fileObject.getParent() != null && fileObject.getParent().getName()!=null){
						return fileObject.getParent().getName().getBaseName();
					}
				}
			}
			//otherwise return null
			return null;
		} 
		catch (Exception e) {
			throw new FileUtilException("failed to get base name of folder object, cause: ",e);
		} 
	}
	
	/**
	 * gets the base  name of the  file. this will be the folder name if a folder, otherwise the filename if a file.
	 * @return
	 * @throws FileSystemException 
	 */
	public String getBaseName() throws FileUtilException{
		try {
			if(exists()){
				FileObject fileObject = this.resolveFile();
				return fileObject.getName().getBaseName();
			}
			//otherwise return null
			return null;
		} 
		catch (Exception e) {
			throw new FileUtilException("failed to get base name of folder object, cause: ",e);
		} 
	}
	
	/**
	 * gets the file name without extension
	 * returns null if a folder
	 * returns file name if there is no extension present (as demarcated by the last "." char in the name)
	 * @return
	 */
	public String getFileNameWithoutExtension() throws FileUtilException{
		String fullFileName = this.getFileName();
		if(fullFileName!=null && fullFileName.lastIndexOf(".")>0){
			int lastIndexOf = fullFileName.lastIndexOf(".");
			return fullFileName.substring(0, (lastIndexOf>0) ? lastIndexOf : fullFileName.length()-1);
		}
		return fullFileName;
	}
	
	/**
	 * gets the file extension
	 * returns null if a folder or not prsent
	 * returns file extension if present (as demarcated by the last "." char in the name)
	 * @return
	 */
	public String getFileExtension() throws FileUtilException{
		if(!this.isFolder()){
			int index = this.getFileName().lastIndexOf('.');
			if(index+1 < this.getFileName().length()){
				return this.getFileName().substring(index+1);
			}
			else{
				return null;
			}
//			String [] parts = this.getFileName().split(".");
//			if(parts!=null && parts.length > 0){
//				return parts[parts.length-1];
//			}
		}
		//default return null aseither a folder or there is no file extension (weird, but can happen)
		return null;
	}
	
	/**
	 * to be implemented by subclasses so that they can do some pre validation on the incoming uri
	 * this implementation is for file name and path
	 * @param uri
	 */
	protected void checkForIllegalCharacters(String uri) throws MirriadFileParamsException{
		String [] invalidCharSequences = {"@",":",";","*","~","#","^","//"};
		for(String invalid : invalidCharSequences){
			if(uri.contains(invalid)){
				throw new MirriadFileParamsException("uri for fileName/filePath connot contain character sequence: " + invalid);
			}
		}
	}
	

	/**
	 * compare to method, we just compare file names for ordering,
	 * if that is equal, then we go by date on the filename, followed by size
	 */
	public int compareTo(FileSystemObject o) {
		//may not be null
		if(o!=null){
			if(o.isFolder()!=this.isFolder()){
				return 1;
			}
			int comp = this.getFullPathName().compareTo(o.getFullPathName());
			if(comp!=0){
				return comp;
			}
			//only valid to compare this on actual files
			if(!o.isFolder()){
				comp = this.fileName.compareTo(o.getFileName());
				if(comp!=0){
					return comp;
				}
			}
			try {
				comp = this.getModified().compareTo(o.getModified());
				if(comp!=0){
					return comp;
				}
			} catch (FileUtilException e) {
				//not doing anything, we just catch, and attempt size...
			}
			//don't do this on folders as might take a while.
			if(!o.isFolder()){
				try {
					comp = new Long(this.getSize()).compareTo(new Long(o.getSize()));
					if(comp!=0){
						return comp;
					}
				} catch (FileUtilException e) {
					//not doing anything, we just catch, and assume they're equal...
				}
			}
			//assume equal if it has got this far...
			return 0;
		}
		else{
			return 1;
		}
	}
	
	/**
	 * closes the file
	 * @throws FileUtilException 
	 */
	public void closeFile() throws FileUtilException{
		try{
			if(this.exists()){
			FileObject fileObject = this.resolveFile();
				if(fileObject!=null){
					fileObject.close();
				}
			}
		} 
		catch (FileSystemException e) {
			logger.error("failed to close file, cause: ",e);
		}
	}

}
