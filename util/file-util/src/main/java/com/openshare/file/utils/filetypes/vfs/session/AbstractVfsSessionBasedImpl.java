package com.openshare.file.utils.filetypes.vfs.session;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.log4j.Logger;



import com.openshare.file.utils.FileSystemSessionBasedObject;
import com.openshare.file.utils.exception.FileUtilException;
import com.openshare.file.utils.filetypes.SupportedFileType;
import com.openshare.file.utils.filetypes.vfs.FileAbstractVfsImpl;
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
public abstract class AbstractVfsSessionBasedImpl extends FileAbstractVfsImpl implements FileSystemSessionBasedObject{
	
	private static final long serialVersionUID = 1233318039697901921L;
	
	private static final Logger logger = Logger.getLogger(AbstractVfsSessionBasedImpl.class);

	/**
	 * super constructor as we need to enforce it to take a params object to initialise it.
	 * @throws FileSystemException 
	 */
	public AbstractVfsSessionBasedImpl(SupportedFileType fileType){
		super(fileType);
	}
	
	/**
	 * one method used to close the file system session, 
	 * ie cut the connection. 
	 * @throws FileUtilException 
	 */
	public void endFileSystemSession() throws FileUtilException{
		//we can just use the super class implementation
		logger.debug("closing session based file system");
		super.closeFileSystem();
	}
	
	/**
	 * closes the file system associated with this file object
	 * @throws FileUtilException
	 */
	@Override
	public void closeFileSystem() throws FileUtilException{
		logger.debug("Session Based File System, close call ignored to keep session alive");
		/* do absolutely nothing, files of this type should be calling 
		 * endFileSystemSession() explicitly
		 */
//		logger.debug("closing session based file system");
//		super.closeFileSystem();
	}
}
