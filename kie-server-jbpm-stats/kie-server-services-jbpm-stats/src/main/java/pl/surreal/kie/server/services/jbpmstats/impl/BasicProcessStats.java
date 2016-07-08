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

public class BasicProcessStats implements IProcessStats
{
	private int instanceCount;
	private int completedCount;
	private double completedInstanceDuration;

	@Override
	public void updateStatistics(ProcessInstanceLog instanceLog,List<NodeInstanceLog> nodeInstances) {
		instanceCount++;		
		if(instanceLog.getStatus()==ProcessInstance.STATE_COMPLETED) {
			completedCount++;
			completedInstanceDuration+=instanceLog.getDuration();
		}
 	}

	@Override
	public ProcessStatistics getStatistics() {
		ProcessStatistics processStatistics = new ProcessStatistics();
		processStatistics.setTotalInstances(instanceCount);
		
		long avgCompletedDuration=0;
		if(completedCount>0) {
			avgCompletedDuration = Math.round(completedInstanceDuration/completedCount);
		}
		processStatistics.setCompletedDuration(avgCompletedDuration);
		return processStatistics;
	}
}
