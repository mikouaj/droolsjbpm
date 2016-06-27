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

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jbpm.services.api.RuntimeDataService;
import org.jbpm.services.api.model.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiagramServiceBase
{
	private static final Logger logger = LoggerFactory.getLogger(DiagramServiceBase.class);
    private RuntimeDataService dataService;
    private Map<String, DiagramReference> diagramReferenceMap;
    
	public DiagramServiceBase(RuntimeDataService dataService, Map<String, DiagramReference> diagramReferenceMap) {
		this.dataService = dataService;
		this.diagramReferenceMap = diagramReferenceMap;
	}

	private byte[] getProcessDiagramBytes(String containerId, String processId) {
		ProcessDefinition procDef = dataService.getProcessesByDeploymentIdProcessId(containerId, processId);
		if( procDef == null ) {
            throw new IllegalArgumentException("No process found for " + processId + " within container " + containerId);
        }
		
		logger.debug("Process original path {}",procDef.getOriginalPath());
		String processPath = "";
        if (procDef.getPackageName() != null && !procDef.getPackageName().trim().isEmpty()) {
        	processPath = procDef.getPackageName().replaceAll("\\.", "/");
        }	
        
        byte[] diagramBytes = diagramReferenceMap.get(containerId).getDiagramContent(processPath, processId);
        if(diagramBytes==null) {
            logger.warn("Could not find diagram file for process '" + processId + "' within container " + containerId);
            return null;
        }
        return diagramBytes;
	}
	
	public String getProcessDiagram(String containerId, String processId) {
		String diagramString = null;
		byte[] diagramBytes = getProcessDiagramBytes(containerId, processId);
		if(diagramBytes!=null) {
            try {
            	diagramString = new String(diagramBytes, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.debug("UnsupportedEncodingException while building process diagram due to {}", e.getMessage());
            }
		}
		return diagramString;
	}	
}
