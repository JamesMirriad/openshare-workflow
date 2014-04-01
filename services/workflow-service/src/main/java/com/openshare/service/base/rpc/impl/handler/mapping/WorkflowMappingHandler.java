package com.openshare.service.base.rpc.impl.handler.mapping;

import com.openshare.service.base.rpc.MethodHandler;
import com.openshare.service.base.rpc.OpenShareResponse;
import com.openshare.service.base.rpc.StatusEnum;
import com.openshare.service.base.rpc.impl.palyoad.OperationType;
import com.openshare.service.base.rpc.impl.palyoad.mapping.TriggerWorkflowMappingPayload;
import com.openshare.workflow.domain.TriggerWorkflowMapping;
import com.openshare.workflow.persistence.DaoFactory;
import com.openshare.workflow.persistence.dao.TriggerWorkflowMappingDao;
import com.openshare.workflow.persistence.results.ResultsSet;

public class WorkflowMappingHandler extends MethodHandler<TriggerWorkflowMappingPayload> {

	@Override
	protected OpenShareResponse executeWithConvertedPayload(
			TriggerWorkflowMappingPayload convertedPayload) {
		
		OpenShareResponse response = new OpenShareResponse();
		
		if(convertedPayload!=null){
			String triggerName = convertedPayload.getTriggerName();
			String workflow = convertedPayload.getWorkflow();
			String id = convertedPayload.getId();
			OperationType operation = convertedPayload.getOperation();
			if(operation == null){
				response.setStatus(StatusEnum.INVALID);
				response.setPayload("No operation specified");
			}
			else{
				if((id==null || id.isEmpty()) && operation!=OperationType.CREATE){
					response.setStatus(StatusEnum.INVALID);
					response.setPayload("no id supplied, cannot perform " + operation);
					return response;
				}
				TriggerWorkflowMappingDao dao = DaoFactory.getInstance().getTriggerWorkflowMappingDao();
				TriggerWorkflowMapping present = dao.findByTriggerNameAndWorkflow(triggerName, workflow);
				TriggerWorkflowMapping mapping = null;
				switch(operation){
				case CREATE:
					if(present!=null){
						response.setStatus(StatusEnum.INVALID);
						response.setPayload("triggerName <-> workflow pairing already present, cannot add");
						return response;
					}
					else{
						mapping = new TriggerWorkflowMapping();
						mapping.setTriggerName(triggerName);
						mapping.setWorkflowName(workflow);
						dao.persist(mapping);
						response.setPayload(mapping);
						response.setStatus(StatusEnum.OK);
					}
					break;
				case READ:
					mapping = dao.load(id);
					response.setPayload(mapping);
					response.setStatus(StatusEnum.OK);
					break;
				case UPDATE:
					mapping = dao.load(id);
					mapping.setTriggerName(triggerName);
					mapping.setWorkflowName(workflow);
					dao.update(mapping);
					response.setPayload(mapping);
					response.setStatus(StatusEnum.OK);
					break;
				case DELETE:
					dao.deleteById(id);
					response.setPayload(null);
					response.setStatus(StatusEnum.OK);
					break;
				default:
					response.setStatus(StatusEnum.INVALID);
					response.setPayload("operation type " + operation + " is invalid/unrecognised");
					return response;
				}
			}
		}
		else{
			response.setStatus(StatusEnum.INVALID);
			response.setPayload("No payload in method");
		}
		return response;
	}

}
