package com.openshare.file.utils.exception;
/**
 * Main exception class for file tool manager related exceptions
 * @author james.mcilroy
 *
 */
public class MirriadFileManagerException extends Exception {

	private static final long serialVersionUID = -8084733601895485156L;

	public MirriadFileManagerException() {
		super();
	}

	public MirriadFileManagerException(String message) {
		super(message);
	}

	public MirriadFileManagerException(Throwable cause) {
		super(cause);
	}

	public MirriadFileManagerException(String message, Throwable cause) {
		super(message, cause);
	}

}
