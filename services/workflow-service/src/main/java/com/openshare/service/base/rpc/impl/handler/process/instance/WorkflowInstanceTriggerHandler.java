package com.openshare.service.base.rpc.impl.handler.process.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLinkType;
import org.apache.log4j.Logger;

import com.openshare.service.base.constants.OpenShareConstants;
import com.openshare.service.base.rpc.MethodHandler;
import com.openshare.service.base.rpc.OpenShareResponse;
import com.openshare.service.base.rpc.impl.palyoad.process.instance.WorkflowInstanceTriggerPayload;
import com.openshare.workflow.conf.ActivitiHelper;
import com.openshare.workflow.domain.TriggerWorkflowMapping;
import com.openshare.workflow.ext.constants.WorkflowConstants;
import com.openshare.workflow.persistence.DaoFactory;
import com.openshare.workflow.persistence.dao.TriggerWorkflowMappingDao;
import com.openshare.workflow.persistence.results.ResultsSet;
/**
 * executes a run time instance given a deployment id of the associated
 * process definition
 * @author james.mcilroy
 *
 */
public class WorkflowInstanceTriggerHandler  extends MethodHandler<WorkflowInstanceTriggerPayload> {

	private static final Logger logger = Logger.getLogger(WorkflowInstanceTriggerHandler.class);

	@Override
	protected OpenShareResponse executeWithConvertedPayload(
			WorkflowInstanceTriggerPayload convertedPayload) {
		//get hold of the execution engine
		ActivitiHelper activityHelper = ActivitiHelper.getInstance();
		ProcessEngine engine = activityHelper.getProcessEngine();
		TriggerWorkflowMappingDao dao = DaoFactory.getInstance().getTriggerWorkflowMappingDao();
		List<String> triggerNames = new ArrayList<String>();
		triggerNames.add(convertedPayload.getTriggerName());
		ResultsSet<TriggerWorkflowMapping> workflowMappings = dao.findByTriggerNames(null, triggerNames);
		logger.info("found " + workflowMappings.getNumberOfResults() + " mappings, triggering all workflows");
		List<String> processIds = new ArrayList<String>();
		for(TriggerWorkflowMapping mapping : workflowMappings.getResults()){
			//find definition from deployment id
			RuntimeService runtimeService = engine.getRuntimeService();
			Map<String,Object> processVariables = new HashMap<String,Object>();
			processVariables.put(WorkflowConstants.SOURCE_FILE, convertedPayload.getFileUri());
			final ProcessInstance process = runtimeService.startProcessInstanceByKey(mapping.getWorkflowName());
			//assign sysadmin as default owner
			runtimeService.addUserIdentityLink(process.getId(), OpenShareConstants.DEFAULT_USER, IdentityLinkType.OWNER);
			runtimeService.addUserIdentityLink(process.getId(), OpenShareConstants.DEFAULT_USER, IdentityLinkType.STARTER);
			processIds.add(process.getId());
		}
		//return whatever we need to here...
		OpenShareResponse response = new OpenShareResponse();
		response.setTxid(this.getTransactionId());
		response.setPayload(processIds);
		return response;
	}

}
