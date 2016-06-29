package pl.surreal.kie.server.services.jbpmstats;

import java.util.List;

import org.jbpm.process.audit.JPAAuditLogService;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatsServiceBase
{
	private static final Logger logger = LoggerFactory.getLogger(StatsServiceBase.class);
	private JPAAuditLogService auditLogService;
	
	public StatsServiceBase(JPAAuditLogService auditLogService) {
		this.auditLogService = auditLogService;
	}
	
	public int doTheTest(String processId) {
		List<ProcessInstanceLog> list = auditLogService.findProcessInstances(processId);
		int size = list.size();
		logger.debug("Found '{}' process instance logs",size);
		for(ProcessInstanceLog pil : list) {
			logger.debug("PIL '{}'",pil.toString());
		}
		return size;
	}
}
