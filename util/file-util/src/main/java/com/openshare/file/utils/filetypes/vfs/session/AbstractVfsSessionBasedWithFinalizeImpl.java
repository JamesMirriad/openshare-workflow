package com.openshare.file.utils.filetypes.vfs.session;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.log4j.Logger;



import com.openshare.file.utils.FileSystemSessionBasedObject;
import com.openshare.file.utils.exception.FileUtilException;
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
public abstract class AbstractVfsSessionBasedWithFinalizeImpl extends AbstractVfsSessionBasedImpl implements FileSystemSessionBasedObject{

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(AbstractVfsSessionBasedWithFinalizeImpl.class);

	/**
	 * super constructor as we need to enforce it to take a params object to initialise it.
	 * @throws FileSystemException 
	 */
	public AbstractVfsSessionBasedWithFinalizeImpl(SupportedFileType fileType){
		super(fileType);
	}
	
	/**
	 * override of finalize method, for file systems where contents are not volatile, but connections are (such as ftp/sftp)
	 */
	@Override
	public void finalize(){
		//ENSURE we close the file system if we're no longer doing anything with this file.
		try {
			this.endFileSystemSession();
		} 
		catch (FileUtilException e) {
			//just debug log the error...actually, don't, some filesystems use this
			//logger.warn("attempted to close file system on object garbage collect, failed with cause: ",e);
		}
		super.finalize();
	}

}
