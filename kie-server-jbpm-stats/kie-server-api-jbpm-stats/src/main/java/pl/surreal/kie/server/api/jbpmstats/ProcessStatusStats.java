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

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement(name="status-statistics")
@XStreamAlias( "status-statistics" )
public class ProcessStatusStats
{
	private int pending;
	private int active;
	private int completed;
	private int aborted;
	private int suspended;
	
	public ProcessStatusStats() {
	}
	
	public int getPending() {
		return pending;
	}
	
	public void setPending(int pending) {
		this.pending = pending;
	}
	
	public int getActive() {
		return active;
	}
	
	public void setActive(int active) {
		this.active = active;
	}
	
	public int getCompleted() {
		return completed;
	}
	
	public void setCompleted(int completed) {
		this.completed = completed;
	}
	
	public int getAborted() {
		return aborted;
	}
	
	public void setAborted(int aborted) {
		this.aborted = aborted;
	}
	
	public int getSuspended() {
		return suspended;
	}
	
	public void setSuspended(int suspended) {
		this.suspended = suspended;
	}
}