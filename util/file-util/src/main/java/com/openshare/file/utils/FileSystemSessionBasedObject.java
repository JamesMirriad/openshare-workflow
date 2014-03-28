package com.openshare.file.utils;

import com.openshare.file.utils.exception.FileUtilException;
/**
 * FileSystemSessionBasedObject represents an entity in a file system which is "seesion" based, 
 * e, one where we ideally do NOT want to close the filesystem after ever operation to save overhead 
 * (for instance when dealing with an ftp/sftp or ram session)
 * 
 * @author james.mcilroy
 * 
 */
public interface FileSystemSessionBasedObject{
	
	/**
	 * one method used to close the fle system session, 
	 * ie cut the connection.
	 * @throws FileUtilException 
	 */
	public void endFileSystemSession() throws FileUtilException;
}
