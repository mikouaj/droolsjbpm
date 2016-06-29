package pl.surreal.kie.server.rest.jbpmstats;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.kie.server.services.api.KieServerApplicationComponentsService;
import org.kie.server.services.api.KieServerRegistry;
import org.kie.server.services.api.SupportedTransports;

import pl.surreal.kie.server.services.jbpmstats.StatsServiceBase;

public class JbpmStatsRestApplicationComponentsService implements KieServerApplicationComponentsService
{
	private static final String OWNER_EXTENSION = "jBPM-Stats";

	public Collection<Object> getAppComponents(String extension, SupportedTransports type, Object... services) {
		if ( !OWNER_EXTENSION.equals(extension) ) {
			return Collections.emptyList();
		}
		StatsServiceBase statsServiceBase = null;
		KieServerRegistry context = null;

		for( Object object : services ) {
			if(object == null) {
                continue;
            }
			if(StatsServiceBase.class.isAssignableFrom(object.getClass())) {
				statsServiceBase = (StatsServiceBase) object;
                continue;
			} else if (KieServerRegistry.class.isAssignableFrom(object.getClass())) {
                context = (KieServerRegistry) object;
                continue;
            }
		}
		List<Object> components = new ArrayList<Object>();
		components.add(new StatsResource(statsServiceBase, context));
		return components;
	}
}