/* Copyright 2016 Mikolaj Stefaniak
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package pl.surreal.kie.server.services.jbpmstats;

import java.util.List;

import org.jbpm.process.audit.JPAAuditLogService;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.surreal.kie.server.api.jbpmstats.ProcessStatistics;
import pl.surreal.kie.server.api.jbpmstats.ServiceResponse;

public class StatsServiceBase
{
	private static final Logger logger = LoggerFactory.getLogger(StatsServiceBase.class);
	private JPAAuditLogService auditLogService;
	
	public StatsServiceBase(JPAAuditLogService auditLogService) {
		this.auditLogService = auditLogService;
	}
	
	public ServiceResponse<ProcessStatistics> getProcessStats(String processId) {
		List<ProcessInstanceLog> list = auditLogService.findProcessInstances(processId);
		int size = list.size();
		logger.debug("Found '{}' process instance logs",size);
		ProcessStatistics stats = new ProcessStatistics();
		stats.setTotalInstances(size);
		return new ServiceResponse<ProcessStatistics>(ServiceResponse.ResponseType.SUCCESS,"Process statistics",stats);
	}
}
