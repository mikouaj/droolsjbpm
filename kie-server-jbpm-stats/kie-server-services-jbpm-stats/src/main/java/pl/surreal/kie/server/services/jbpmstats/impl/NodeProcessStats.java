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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.jbpm.process.audit.NodeInstanceLog;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.surreal.kie.server.api.jbpmstats.ProcessNodeStats;
import pl.surreal.kie.server.api.jbpmstats.ProcessStatistics;

public class NodeProcessStats extends ProcessStatsDecorator
{
	private static final Logger logger = LoggerFactory.getLogger(NodeProcessStats.class);
	private HashMap<String,NodeStats> statistics;

	public NodeProcessStats(IProcessStats entryProcessStats) {
		super(entryProcessStats);
		statistics = new HashMap<>();
	}
	
	private HashMap<String,Date[]> fillNodeDates(HashMap<String,Date[]> nodeDates,NodeInstanceLog nodeInstanceLog) {
		Date[] dates =  nodeDates.get(nodeInstanceLog.getNodeId());
		if(dates==null) {
			dates = new Date[2];
			nodeDates.put(nodeInstanceLog.getNodeId(),dates);
		}
		switch(nodeInstanceLog.getType()) {
			case NodeInstanceLog.TYPE_ENTER:
				dates[0] = nodeInstanceLog.getDate();
				break;
			case NodeInstanceLog.TYPE_EXIT:
				dates[1] = nodeInstanceLog.getDate();
				break;
			default:
				logger.warn("Unsupported NodeInstanceLog type '{}'",nodeInstanceLog.getType());	
		}
		return nodeDates;
	}
	
	@Override
	public void updateStatistics(ProcessInstanceLog instanceLog,List<NodeInstanceLog> nodeInstances) {
		super.updateStatistics(instanceLog,nodeInstances);
		
		HashMap<String,Date[]> nodeDates = new HashMap<>();
		for(NodeInstanceLog nodeInstanceLog : nodeInstances) {
			fillNodeDates(nodeDates,nodeInstanceLog);
			if(!statistics.containsKey(nodeInstanceLog.getNodeId())) {
				statistics.put(nodeInstanceLog.getNodeId(),new NodeStats(nodeInstanceLog.getNodeId(),nodeInstanceLog.getNodeName()));
			}
		}
		
		for(String nodeId : nodeDates.keySet()) {
			Date[] dates = nodeDates.get(nodeId);
			long duration;
			if(dates[0] == null || dates[1] == null) {
				logger.warn("Can't calculate duration for nodeId '{}' due to missing dates",nodeId);
				duration=0;
			} else {
				duration = dates[1].getTime() - dates[0].getTime();
			}
			statistics.get(nodeId).updateDuration(duration);
		}
	}

	@Override
	public ProcessStatistics getStatistics() {
		ProcessStatistics processStatistics = super.getStatistics();
		if(statistics.size()>0) {
			List<NodeStats> statsByDuration = new ArrayList<NodeStats>(statistics.values());
			Collections.sort(statsByDuration);
			NodeStats longestNode = statsByDuration.get(statsByDuration.size()-1);
			
			ProcessNodeStats nodeStats = new ProcessNodeStats();
			nodeStats.setLongestDuration(longestNode.getAvgDuration());
			nodeStats.setLongestId(longestNode.getId());
			nodeStats.setLongestName(longestNode.getName());
			nodeStats.setNodeCount(statistics.size());
			processStatistics.setNodeStats(nodeStats);
		}
		return processStatistics;
	}
}
