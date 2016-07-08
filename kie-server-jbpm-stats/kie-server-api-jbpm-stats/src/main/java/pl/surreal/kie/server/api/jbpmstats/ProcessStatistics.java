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

package pl.surreal.kie.server.api.jbpmstats;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement(name="process-statistics")
@XStreamAlias( "process-statistics" )
public class ProcessStatistics
{
	private int totalInstances;
	private long completedDuration;
	private ProcessStatusStats statusStats;
	private ProcessNodeStats nodeStats;
	
	public ProcessStatistics() {
		super();
	}
	
	@XmlElement(name="instances-total")
	public int getTotalInstances() {
		return totalInstances;
	}
	
	public void setTotalInstances(int totalInstances) {
		this.totalInstances = totalInstances;
	}

	@XmlElement(name="status-statistics")
	public ProcessStatusStats getStatusStats() {
		return statusStats;
	}

	public void setStatusStats(ProcessStatusStats statusStats) {
		this.statusStats = statusStats;
	}

	@XmlElement(name="avg-completion-time")
	public long getCompletedDuration() {
		return completedDuration;
	}

	public void setCompletedDuration(long completedDuration) {
		this.completedDuration = completedDuration;
	}

	@XmlElement(name="node-statistics")
	public ProcessNodeStats getNodeStats() {
		return nodeStats;
	}
	
	public void setNodeStats(ProcessNodeStats nodeStats) {
		this.nodeStats = nodeStats;
	}
}