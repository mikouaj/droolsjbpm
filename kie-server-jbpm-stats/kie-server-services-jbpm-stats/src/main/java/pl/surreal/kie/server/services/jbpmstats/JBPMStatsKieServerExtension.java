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

package pl.surreal.kie.server.services.jbpmstats;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import javax.persistence.EntityManagerFactory;

import org.jbpm.process.audit.JPAAuditLogService;
import org.jbpm.runtime.manager.impl.jpa.EntityManagerFactoryManager;
import org.kie.server.api.KieServerConstants;
import org.kie.server.services.api.KieContainerInstance;
import org.kie.server.services.api.KieServerApplicationComponentsService;
import org.kie.server.services.api.KieServerExtension;
import org.kie.server.services.api.KieServerRegistry;
import org.kie.server.services.api.SupportedTransports;
import org.kie.server.services.impl.KieServerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JBPMStatsKieServerExtension implements KieServerExtension
{
	public static final String EXTENSION_NAME = "jBPM-Stats";
	private static final Logger logger = LoggerFactory.getLogger(JBPMStatsKieServerExtension.class);
	private static final Boolean disabled = Boolean.parseBoolean(System.getProperty("pl.surreal.jbpmstats.server.ext.disabled", "false"));
	private static final Boolean jbpmDisabled = Boolean.parseBoolean(System.getProperty(KieServerConstants.KIE_JBPM_SERVER_EXT_DISABLED, "false"));
	
	private List<Object> services = new ArrayList<Object>();
	private boolean initialized = false;
	
	private String persistenceUnitName = "org.jbpm.domain";
	private StatsServiceBase statsServiceBase;
	
//    private ConcurrentMap<String, DiagramReference> diagramReferences = new ConcurrentHashMap<String, DiagramReference>();
//    private DiagramServiceBase diagramServiceBase;
    private KieServerRegistry registry;
    
    public void init(KieServerImpl kieServer, KieServerRegistry registry) {
    	this.registry = registry;
		KieServerExtension jbpmExtension = registry.getServerExtension("jBPM");
		if(jbpmExtension==null) {
			logger.warn("jBPM extension not found, jBPM Stats extension cannot be initialized");
			initialized=false;
			return;
		}
		
		// EMF for org.jbpm.domain PU will be created by BPM extension
		EntityManagerFactory emf = EntityManagerFactoryManager.get().getOrCreate(persistenceUnitName);
		JPAAuditLogService auditLogService = new JPAAuditLogService(emf);
		statsServiceBase = new StatsServiceBase(auditLogService);
		services.add(statsServiceBase);
		initialized=true;
    }
    
	public void createContainer(String id, KieContainerInstance kieContainerInstance, Map<String, Object> parameters) {
	}
	
	public void destroy(KieServerImpl kieServer, KieServerRegistry registry) {
		if(!initialized) { return; }
	}

	public void disposeContainer(String id, KieContainerInstance kieContainerInstance, Map<String, Object> parameters) {
	}

	public List<Object> getAppComponents(SupportedTransports type) {
		List<Object> appComponentsList = new ArrayList<Object>();
		if(!initialized) {
			return appComponentsList;
		}
        ServiceLoader<KieServerApplicationComponentsService> appComponentsServices = ServiceLoader.load(KieServerApplicationComponentsService.class);
        Object[] services = {statsServiceBase, registry};
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
		return "BPM-Stats";
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
