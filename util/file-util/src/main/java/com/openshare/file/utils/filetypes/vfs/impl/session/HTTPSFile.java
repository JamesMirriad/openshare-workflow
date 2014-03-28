package com.openshare.file.utils.filetypes.vfs.impl.session;

import org.apache.log4j.Logger;

import com.openshare.file.utils.filetypes.SupportedFileType;
/**
 * an https file
 * @author james.mcilroy
 *
 */
public class HTTPSFile extends HTTPFile {

	private static final long serialVersionUID = -270701732315506005L;

	private static final Logger logger = Logger.getLogger(HTTPSFile.class);
	
	public HTTPSFile(SupportedFileType fileType) {
		super(fileType);
		// TODO Auto-generated constructor stub
	}
}
