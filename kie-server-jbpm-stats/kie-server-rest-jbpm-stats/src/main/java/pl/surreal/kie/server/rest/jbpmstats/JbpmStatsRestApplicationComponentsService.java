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