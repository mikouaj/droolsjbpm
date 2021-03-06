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

import static org.kie.server.remote.rest.common.util.RestUtils.getContentType;
import static org.kie.server.remote.rest.common.util.RestUtils.getVariant;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Variant;

import org.kie.server.api.marshalling.Marshaller;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.services.api.KieContainerInstance;
import org.kie.server.services.api.KieServerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.surreal.kie.server.services.jbpmstats.StatsServiceBase;
import static org.kie.server.remote.rest.common.util.RestUtils.createResponse;

@Path("/server/containers/{containerId}/processes")
public class StatsResource
{
	private static Logger logger = LoggerFactory.getLogger(StatsResource.class);
	private StatsServiceBase statsServiceBase;
	private KieServerRegistry registry;
	
	public StatsResource(StatsServiceBase statsServiceBase, KieServerRegistry registry) {
		this.statsServiceBase = statsServiceBase;
		this.registry = registry;
	}

	@GET
	@Path("/{processId}/statsTest")
	@Produces({MediaType.APPLICATION_XML})
	public Response test(@Context HttpHeaders headers, @PathParam("containerId") String containerId, @PathParam("processId") String processId) {
		Variant v = getVariant(headers);
		String contentType = getContentType(headers);
		MarshallingFormat format = MarshallingFormat.fromType(contentType);
		if (format == null) {
            format = MarshallingFormat.valueOf(contentType);
        }		
		KieContainerInstance kci = registry.getContainer(containerId);
		Marshaller marshaller = kci.getMarshaller(format);
		String result = marshaller.marshall(statsServiceBase.getProcessStats(processId));
		return createResponse(result, v, Response.Status.OK);
	}
}
