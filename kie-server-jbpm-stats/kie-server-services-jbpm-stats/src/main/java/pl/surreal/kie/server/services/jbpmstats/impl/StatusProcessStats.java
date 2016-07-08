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

package pl.surreal.kie.server.services.jbpmstats.impl;

import java.util.List;

import org.jbpm.process.audit.NodeInstanceLog;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.process.instance.ProcessInstance;

import pl.surreal.kie.server.api.jbpmstats.ProcessStatistics;
import pl.surreal.kie.server.api.jbpmstats.ProcessStatusStats;

public class StatusProcessStats extends ProcessStatsDecorator
{
	private int pending;
	private int active;
	private int completed;
	private int aborted;
	private int suspended;
	
	public StatusProcessStats(IProcessStats entryProcessStats) {
		super(entryProcessStats);
	}
	
	@Override
	public void updateStatistics(ProcessInstanceLog instanceLog,List<NodeInstanceLog> nodeInstances) {
		super.updateStatistics(instanceLog,nodeInstances);
		
		switch(instanceLog.getStatus()) {
			case ProcessInstance.STATE_PENDING:
				pending++;
				break;
			case ProcessInstance.STATE_ACTIVE:
				active++;
				break;
			case ProcessInstance.STATE_COMPLETED:
				completed++;
				break;
			case ProcessInstance.STATE_ABORTED:
				aborted++;
				break;
			case ProcessInstance.STATE_SUSPENDED:
				suspended++;
				break;
		}
	}
	
	@Override
	public ProcessStatistics getStatistics() {
		ProcessStatusStats statusStats = new ProcessStatusStats();
		statusStats.setPending(pending);
		statusStats.setActive(active);
		statusStats.setCompleted(completed);
		statusStats.setAborted(aborted);
		statusStats.setSuspended(suspended);
		
		ProcessStatistics processStatistics = super.getStatistics();
		processStatistics.setStatusStats(statusStats);
		return processStatistics;
	}
}
