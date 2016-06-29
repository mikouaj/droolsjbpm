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
