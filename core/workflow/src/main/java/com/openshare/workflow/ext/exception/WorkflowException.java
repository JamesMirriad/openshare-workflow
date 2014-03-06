/**
 * 
 */
package com.openshare.workflow.ext.exception;

import com.openshare.service.base.exception.OpenshareException;


/**
 * base class for exceptions in the orchestration layer
 * @author james.mcilroy
 *
 */
public class WorkflowException extends OpenshareException {

	/**
	 * 
	 */
	public WorkflowException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public WorkflowException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public WorkflowException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WorkflowException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public WorkflowException(String arg0, Throwable arg1,
			boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

}
