package com.openshare.service.base.exception;


/**
 * General class to signify a problem with a service or service call
 * @author james.mcilroy
 *
 */
public class OpenshareServiceException extends OpenshareException {

	private static final long serialVersionUID = -4316274748628262820L;

	public OpenshareServiceException() {
	}

	public OpenshareServiceException(String arg0) {
		super(arg0);
	}

	public OpenshareServiceException(Throwable arg0) {
		super(arg0);
	}

	public OpenshareServiceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public OpenshareServiceException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
