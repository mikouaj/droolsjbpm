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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jbpm.services.api.RuntimeDataService;
import org.kie.api.runtime.KieContainer;
import org.kie.server.api.KieServerConstants;
import org.kie.server.services.api.KieContainerInstance;
import org.kie.server.services.api.KieServerApplicationComponentsService;
import org.kie.server.services.api.KieServerExtension;
import org.kie.server.services.api.KieServerRegistry;
import org.kie.server.services.api.SupportedTransports;
import org.kie.server.services.impl.KieServerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JBPMDiagramKieServerExtension implements KieServerExtension
{
	public static final String EXTENSION_NAME = "jBPM-Diagram";
	private static final Logger logger = LoggerFactory.getLogger(JBPMDiagramKieServerExtension.class);
	private static final Boolean disabled = Boolean.parseBoolean(System.getProperty("pl.surreal.jbpmdiagram.server.ext.disabled", "false"));
	private static final Boolean jbpmDisabled = Boolean.parseBoolean(System.getProperty(KieServerConstants.KIE_JBPM_SERVER_EXT_DISABLED, "false"));
	private static final Boolean extendedSearchEnabled = Boolean.parseBoolean(System.getProperty("pl.surreal.jbpmdiagram.server.extendedsearch.enabled", "false"));
	
	private List<Object> services = new ArrayList<Object>();
	private boolean initialized = false;
    private ConcurrentMap<String, DiagramReference> diagramReferences = new ConcurrentHashMap<String, DiagramReference>();
	
    private DiagramServiceBase diagramServiceBase;
    private KieServerRegistry registry;
    
    public void init(KieServerImpl kieServer, KieServerRegistry registry) {
    	this.registry = registry;
		KieServerExtension jbpmExtension = registry.getServerExtension("jBPM");
		if(jbpmExtension==null) {
			logger.warn("jBPM extension not found, jBPM Diagram extension cannot be initialized");
			initialized=false;
			return;
		}
		
		List<Object> jbpmServices = jbpmExtension.getServices();
		RuntimeDataService runtimeDataService = null;
		
		for( Object object : jbpmServices ) {
			if (object == null) {
                continue;
            }
			if( RuntimeDataService.class.isAssignableFrom(object.getClass()) ) {
                runtimeDataService = (RuntimeDataService) object;
                continue;
			}
		}
		diagramServiceBase = new DiagramServiceBase(runtimeDataService,diagramReferences);
		diagramServiceBase.setExtendedSearchEnabled(extendedSearchEnabled);
		services.add(diagramServiceBase);
		initialized=true;
    }
    
	public void createContainer(String id, KieContainerInstance kieContainerInstance, Map<String, Object> parameters) {
		if(!initialized) { return; }
		try {
			KieContainer kieContainer = kieContainerInstance.getKieContainer();
			diagramReferences.putIfAbsent(id, new DiagramReference(kieContainer));
		} catch (Exception e) {
			logger.warn("Unable to create diagram reference for container {} due to {}", id, e.getMessage());
        }
	}
	
	public void destroy(KieServerImpl kieServer, KieServerRegistry registry) {
		if(!initialized) { return; }
	}

	public void disposeContainer(String id, KieContainerInstance kieContainerInstance, Map<String, Object> parameters) {
		if(!initialized) { return; }
		diagramReferences.remove(id);
	}

	public List<Object> getAppComponents(SupportedTransports type) {
		List<Object> appComponentsList = new ArrayList<Object>();
		if(!initialized) {
			return appComponentsList;
		}
        ServiceLoader<KieServerApplicationComponentsService> appComponentsServices = ServiceLoader.load(KieServerApplicationComponentsService.class);
        Object[] services = {diagramServiceBase, registry};
        for( KieServerApplicationComponentsService appComponentsService : appComponentsServices ) {
            appComponentsList.addAll(appComponentsService.getAppComponents(EXTENSION_NAME, type, services));
        }
        return appComponentsList;
	}

	public <T> T getAppComponents(Class<T> serviceType) {
		return null;
	}

	public String getExtensionName() {
		return EXTENSION_NAME;
	}

	public String getImplementedCapability() {
		return "BPM-Diagram";
	}

	public List<Object> getServices() {
		return services;
	}

	public Integer getStartOrder() {
		return 20;
	}

	public boolean isActive() {
		return disabled == false && jbpmDisabled == false;
	}

    @Override
    public String toString() {
        return EXTENSION_NAME + " KIE Server extension";
    }	
}