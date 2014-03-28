package com.openshare.file.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.apache.commons.vfs2.FileSystemException;

import com.openshare.file.utils.exception.FileUtilException;
import com.openshare.file.utils.exception.MirriadFileParamsException;
/**
 * FileSystemObject represents an entity in a file system which 
 * can be either a physical file or a folder.
 * Dependent on which it is, some operations can and cannot be performed 
 * (ie: you can't write to a folder object or read it)
 * 
 * Usage pattern example:
 * 1) Choose the file type you'd like (i.e., disk, ftp, sftp etc) 
 * and pass in the enumeration value to the factory to create the file:
 * 
 * 		FileFactory factory = FileFactory.GetInstance();
 * 		FileSystemObject file;
 *		try {
 *			file = (SFTPFile)factory.create(SupportedFileType.SFTP_FILE);
 *		} catch (FileUtilException e) {
 *			logger.error("failed to create file from factory");
 *		}
 * 2) Set parameters in the created file via getters and setters
 * 
 * 3) Use the below API to perform actions on the file/folder.
 * 
 * To add different types please see instructions in the 
 * FileFactory and SupportedFileType classes
 * 
 * @author james.mcilroy
 *
 * @TODO: change setup so we pass in a "params" object to create so we don't have to fill out the 
 * object via getters/setters when it comes back (although can have both implementations side by side)
 * 
 */
public interface FileSystemObject extends Comparable<FileSystemObject>{
	
	/**
	 * get the filename, null returned if folder
	 * @return
	 */
	public String getFileName();
	
	/**
	 * gets the file path relative to the root
	 * @return
	 */
	public String getFilePath();
	
	/**
	 * gets the base folder name of the folder this file lives in, or just the folder name if this 
	 * is the folder (excluding all parent folders)
	 * @return
	 * @throws FileSystemException 
	 */
	public String getBaseFolderName() throws FileUtilException;
	
	/**
	 * gets the base  name of the  file. this will be the folder name if a folder, otherwise the filename if a file.
	 * @return
	 * @throws FileSystemException 
	 */
	public String getBaseName() throws FileUtilException;
	
	/**
	 * gets the file name without extension
	 * returns null if a folder
	 * returns file name if there is no extension present (as demarcated by the last "." char in the name)
	 * @return
	 */
	public String getFileNameWithoutExtension() throws FileUtilException;
	
	/**
	 * gets the file extension
	 * returns null if a folder or not present
	 * returns file extension if present (as demarcated by the last "." char in the name)
	 * @return
	 */
	public String getFileExtension() throws FileUtilException;
	
	/**
	 * returns the system root (if local, the system volume, 
	 * if ftp/sftp based it returns the host, basically where the path lives on top of)
	 * @return
	 */
	public String getFileSystemRoot();
	
	/**
	 * generates full path name as per file system. For most things this generates a full uri, 
	 * except for some systems where a native lookup need the actual path with the protocol attached.
	 * examp-le name as it would be on the file system, ie 
	 * "C:\myfolder\subfolder\fime.ext"
	 * "ftp://myhost:34/folder/remotefile.ext"
	 * etc
	 * @return
	 */
	public String getFullPathName();
	
	/**
	 * returns the full path name, but with any user names and passwords obscured
	 * @return
	 */
	public String getFullObscuredPathName();
	
	/**
	 * returns the file size in bytes, if a folder, 
	 * the size of all child objects totalled together is returned
	 * @return
	 * @throws FileUtilException 
	 */
	public long getSize() throws FileUtilException;
	
	/**
	 * gets the date this file was last modified.
	 * throws exception if file does not exist (ie, never ever modified or not present)
	 * @return
	 * @throws FileUtilException
	 */
	public Date getModified() throws FileUtilException;
	
	/**
	 * writes an input stream to a file
	 * only works IF this is not a folder
	 * @param is
	 */
	public void write(InputStream is) throws FileUtilException;
	
	/**
	 * copies from the specified source into this file system object.
	 * note that: 
	 * - copying a folder into a file object WILL fail (as this makes no sense).
	 * - copying one file to another file will OVERWRITE the current file with the contents of the source.
	 * @param source
	 * @throws FileUtilException
	 */
	public void copyFrom(FileSystemObject source) throws FileUtilException;
	
	/**
	 * moves this source object to the target.
	 * note that:
	 * - moving a folder to a file WILL fail.
	 * - after the move the object will be automatically deleted, and ONLY the target object will still exist.
	 * - moving a file of the same name to another file will overwrite the target with the contents of this file.
	 * @param target
	 * @throws FileUtilException
	 */
	public void moveTo(FileSystemObject target) throws FileUtilException;
	
	/**
	 * convenience method so we can write data from a supplied byte array
	 * @param data
	 * @throws FileUtilException
	 */
	public void write(byte [] data) throws FileUtilException;
	
	/**
	 * read the file contents into an output stream.
	 * only works IF this is not a folder
	 * @param os
	 * @throws FileUtilException
	 */
	public void read(OutputStream os) throws FileUtilException;
	
	/**
	 * get the file input stream
	 * @return
	 * @throws FileUtilException
	 */
	public InputStream getFileInputStream() throws FileUtilException;
	
	/**
	 * read the file contents into an output stream.
	 * only works IF this is not a folder
	 * @param os
	 * @throws FileUtilException
	 */
	public byte [] read() throws FileUtilException;
	
	/**
	 * deletes the file / folder this represents
	 * @throws FileUtilException
	 */
	public void delete() throws FileUtilException;
	
	/**
	 * can the file be read
	 * @return
	 * @throws FileUtilException
	 */
	public boolean canRead() throws FileUtilException;
	
	/**
	 * is the file writeable (ie, have we got write permissions)
	 * @return
	 * @throws FileUtilException
	 */
	public boolean canWrite() throws FileUtilException;
	
	/**
	 * is the file hidden (hidden files often are in the form: .filename 
	 * and cannot normally be seen in GUI file explorers)
	 * @return
	 * @throws FileUtilException
	 */
	public boolean isHidden() throws FileUtilException;
	
	/**
	 * does the file exist (does a file or folder with this 
	 * name exist on the file system we are examining?)
	 * @return
	 * @throws FileUtilException
	 */
	public boolean exists() throws FileUtilException;
	
	/**
	 * is this a folder within the file system?
	 * @return
	 */
	public boolean isFolder();
	
	/**
	 * returns the parent object, or null if no parent. THis will normally be a folder, 
	 * but in some cases could be a file if we eventually add in file archive implementations 
	 * (the ability to write/read from an archive as if it were a file system) 
	 * (ie, object is in root directory, otherwise parent directory)
	 * @return
	 * @throws FileUtilException 
	 */
	public FileSystemObject getParent() throws FileUtilException;
	
	/**
	 * gets all the child objects of this file system object
	 * @return
	 * @throws FileUtilException
	 */
	public List<FileSystemObject> getChildren() throws FileUtilException;
	
	/**
	 * checks whether the file is a is a child of the root, ie, whether this file
	 * lies under that hierarchy
	 * @param root
	 * @return
	 * @throws FileUtilException
	 */
	public boolean isChildOf(FileSystemObject root) throws FileUtilException;
	
	/**
	 * gets ALL files beneath this file/folder as root.
	 * @return
	 * @throws FileUtilException
	 */
	public List<FileSystemObject> getFileSystemTree() throws FileUtilException;
	
	/**
	 * Gets all children and all children of subsequent subfolders, only returns all of type folder...
	 * @return
	 * @throws FileUtilException
	 */
	public List<FileSystemObject> getFileSystemDirectoryTree() throws FileUtilException;
	
	/**
	 * Gets all children and all children of subsequent subfolders,only returns all of type file...
	 * @return
	 * @throws FileUtilException
	 */
	public List<FileSystemObject> getFileSystemFileOnlyTree() throws FileUtilException;
	
	/**
	 * convenience function to retrieve directory sub folders only
	 * @return
	 * @throws FileUtilException
	 */
	public List<FileSystemObject> getSubDirectories() throws FileUtilException;
	
	/**
	 * convenience function to retrieve directory sub files only
	 * @return
	 * @throws FileUtilException
	 */
	public List<FileSystemObject> getSubFiles() throws FileUtilException;
	
	/**
	 * allows an attempt at setting file params from a uri that is fed in.
	 * @param uri
	 * @throws MirriadFileParamsException
	 */
	public void setParamsFromUri(String uri) throws MirriadFileParamsException;
	
	/**
	 * call to close the file explicitly. used for session based files to terminate sessions?
	 * @throws FileUtilException 
	 */
	public void closeFile() throws FileUtilException;
	
}
