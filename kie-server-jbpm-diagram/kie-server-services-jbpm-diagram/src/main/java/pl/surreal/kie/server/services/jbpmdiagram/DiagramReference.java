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

package pl.surreal.kie.server.services.jbpmdiagram;

import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.compiler.kie.builder.impl.KieContainerImpl;
import org.kie.api.runtime.KieContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiagramReference
{
	private static final Logger logger = LoggerFactory.getLogger(DiagramReference.class);
	private static final String[] fileExts = {"bpmn2", "bpmn"};
	private InternalKieModule kieModule;
	
	public DiagramReference(KieContainer kieContainer) {
		kieModule = (InternalKieModule) ((KieContainerImpl)kieContainer).getKieModuleForKBase("defaultKieBase");
	}
	
	public byte[] getDiagramContent(String location, String processid) {
		byte[] data = null;
		String[] paths =  { location+"/", "" };
		for(String path : paths) {
			for(String fileExt : fileExts) {
				String diagramPath = path + processid + "." + fileExt;
				logger.debug("Trying to fetch diagram bytes from path \"{}\"",diagramPath);
				data = kieModule.getBytes(diagramPath);
				if(data!=null) {
					logger.debug("Got diagram bytes from path \"{}\"!",diagramPath);
					break;
				}
			}
		}
		if(data==null) {
			logger.warn("Process \"{}\" diagram file not found in \"{}\"",processid,location);
		}
		return data;
	}
}