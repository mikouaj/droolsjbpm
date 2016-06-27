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

package pl.surreal.kie.server.rest.jbpmdiagram;

import static org.kie.server.remote.rest.common.util.RestUtils.buildConversationIdHeader;
import static org.kie.server.remote.rest.common.util.RestUtils.createResponse;
import static org.kie.server.remote.rest.common.util.RestUtils.getVariant;
import static org.kie.server.remote.rest.common.util.RestUtils.internalServerError;
import static org.kie.server.remote.rest.common.util.RestUtils.notFound;

import java.text.MessageFormat;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Variant;

import org.kie.server.remote.rest.common.Header;
import org.kie.server.services.api.KieServerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.surreal.kie.server.services.jbpmdiagram.DiagramServiceBase;

@Path("/server/containers/{containerId}/processes")
public class DiagramResource
{
	private static Logger logger = LoggerFactory.getLogger(DiagramResource.class);
	private DiagramServiceBase diagramServiceBase;
	private KieServerRegistry registry;
	
	public DiagramResource(DiagramServiceBase diagramServiceBase, KieServerRegistry registry) {
		this.diagramServiceBase = diagramServiceBase;
		this.registry = registry;
	}
	
	@GET
	@Path("/{processId}/diagram")
	@Produces({MediaType.APPLICATION_XML})
	public Response getDiagram(@Context HttpHeaders headers, @PathParam("containerId") String containerId, @PathParam("processId") String processId) {
		Variant v = getVariant(headers);
		Header conversationIdHeader = buildConversationIdHeader(containerId, registry, headers);
		try {
			String diagramString = diagramServiceBase.getProcessDiagram(containerId, processId);
			return createResponse(diagramString, v, Response.Status.OK, conversationIdHeader);
		}  catch (IllegalArgumentException e) {
            return notFound("Diagram for process id " + processId + " not found", v, conversationIdHeader);
        } catch (Exception e) {
            logger.error("Unexpected error during processing {}", e.getMessage(), e);
            return internalServerError(MessageFormat.format("Unexpected error encountered", e.getMessage()), v, conversationIdHeader);
        }
	}
}
