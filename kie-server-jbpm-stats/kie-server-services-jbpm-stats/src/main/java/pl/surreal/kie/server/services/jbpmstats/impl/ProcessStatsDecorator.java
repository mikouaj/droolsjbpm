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

import pl.surreal.kie.server.api.jbpmstats.ProcessStatistics;

public abstract class ProcessStatsDecorator implements IProcessStats
{
	protected IProcessStats entryProcessStats;
	
	public ProcessStatsDecorator(IProcessStats entryProcessStats) {
		this.entryProcessStats = entryProcessStats;
	}
	
	@Override
	public void updateStatistics(ProcessInstanceLog instanceLog,List<NodeInstanceLog> nodeInstances) {
		entryProcessStats.updateStatistics(instanceLog,nodeInstances);
	}
	
	@Override
	public ProcessStatistics getStatistics() {
		return entryProcessStats.getStatistics();
	}
}