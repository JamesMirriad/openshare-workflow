package com.openshare.file.utils.exception;
/**
 * Main exception class for file related exceptions
 * @author james.mcilroy
 *
 */
public class FileUtilException extends Exception {

	private static final long serialVersionUID = 1907935693441659331L;

	public FileUtilException() {
		super();
	}

	public FileUtilException(String message) {
		super(message);
	}

	public FileUtilException(Throwable cause) {
		super(cause);
	}

	public FileUtilException(String message, Throwable cause) {
		super(message, cause);
	}

}
