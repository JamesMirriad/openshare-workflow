package com.openshare.service.base.rpc.impl.handler.mapping;

import com.openshare.service.base.exception.OpenshareException;
import com.openshare.service.base.rpc.MethodHandler;
import com.openshare.service.base.rpc.OpenShareResponse;
import com.openshare.service.base.rpc.StatusEnum;
import com.openshare.service.base.rpc.impl.palyoad.OperationType;
import com.openshare.service.base.rpc.impl.palyoad.mapping.TriggerWorkflowMappingPayload;
import com.openshare.workflow.domain.TriggerWorkflowMapping;
import com.openshare.workflow.persistence.DaoFactory;
import com.openshare.workflow.persistence.dao.TriggerWorkflowMappingDao;
import com.openshare.workflow.persistence.results.ResultsSet;

public class WorkflowMappingListHandler extends MethodHandler<Object> {

	@Override
	protected OpenShareResponse executeWithConvertedPayload(
			Object convertedPayload) throws OpenshareException{
		
		OpenShareResponse response = new OpenShareResponse();
		
		TriggerWorkflowMappingDao dao = DaoFactory.getInstance().getTriggerWorkflowMappingDao();
		ResultsSet<TriggerWorkflowMapping> allItems = dao.findByTriggerNames(null, null);
		response.setPayload(allItems.getResults());
		response.setStatus(StatusEnum.OK);
		return response;
	}

}
