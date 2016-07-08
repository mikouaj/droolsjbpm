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

@XmlRootElement(name="node-statistics")
@XStreamAlias( "node-statistics" )
public class ProcessNodeStats
{
	private int nodeCount;
	private long longestDuration;
	private String longestId;
	private String longestName;
	
	public ProcessNodeStats() {
	}

	@XmlElement(name="nodes-total")
	public int getNodeCount() {
		return nodeCount;
	}

	public void setNodeCount(int nodeCount) {
		this.nodeCount = nodeCount;
	}

	@XmlElement(name="longest-node-time")
	public long getLongestDuration() {
		return longestDuration;
	}

	public void setLongestDuration(long longestDuration) {
		this.longestDuration = longestDuration;
	}

	@XmlElement(name="longest-node-id")
	public String getLongestId() {
		return longestId;
	}

	public void setLongestId(String longestId) {
		this.longestId = longestId;
	}

	@XmlElement(name="longest-node-name")
	public String getLongestName() {
		return longestName;
	}

	public void setLongestName(String longestName) {
		this.longestName = longestName;
	}
}
