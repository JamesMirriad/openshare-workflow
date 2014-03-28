package com.openshare.file.utils.exception;
/**
 * exception caused by trying to set incorrect file params
 * @author james.mcilroy
 *
 */
public class MirriadFileParamsException extends FileUtilException {

	private static final long serialVersionUID = -6153760742255860926L;

	public MirriadFileParamsException() {
		super();
	}

	public MirriadFileParamsException(String message) {
		super(message);
	}

	public MirriadFileParamsException(Throwable cause) {
		super(cause);
	}

	public MirriadFileParamsException(String message, Throwable cause) {
		super(message, cause);
	}
}
