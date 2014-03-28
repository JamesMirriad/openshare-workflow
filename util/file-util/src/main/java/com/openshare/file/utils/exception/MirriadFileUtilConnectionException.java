package com.openshare.file.utils.exception;
/**
 * Main exception class for file related exceptions
 * @author james.mcilroy
 *
 */
public class MirriadFileUtilConnectionException extends Exception {

	private static final long serialVersionUID = 1907935693441659331L;

	public MirriadFileUtilConnectionException() {
		super();
	}

	public MirriadFileUtilConnectionException(String message) {
		super(message);
	}

	public MirriadFileUtilConnectionException(Throwable cause) {
		super(cause);
	}

	public MirriadFileUtilConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

}
