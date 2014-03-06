package com.openshare.service.base.exception;
/**
 * Service not found exception, indicates a service of requested type / name
 * does not exist or could not be found.
 * @TODO inherit from generalised mirriad exception base class to be created.
 * @author james.mcilroy
 *
 */
public class ServiceNotFoundException extends OpenshareServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -293454249232366319L;

	public ServiceNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	public ServiceNotFoundException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ServiceNotFoundException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ServiceNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public ServiceNotFoundException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

}
