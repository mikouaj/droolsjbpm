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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.compiler.kie.builder.impl.KieContainerImpl;
import org.kie.api.runtime.KieContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DiagramReference
{
	private static final Logger logger = LoggerFactory.getLogger(DiagramReference.class);
	private InternalKieModule kieModule;
	
	public DiagramReference(KieContainer kieContainer) {
		kieModule = (InternalKieModule) ((KieContainerImpl)kieContainer).getKieModuleForKBase("defaultKieBase");
	}
	
	public byte[] getDiagramContent(String packagePath, String diagramFileName) {
		byte[] data = null;
		String[] paths =  { packagePath+"/", "" };
		for(String path : paths) {
			String diagramPath = path + diagramFileName + ".bpmn2";
			logger.debug("Trying to fetch diagram bytes from path '{}'",diagramPath);
			data = kieModule.getBytes(diagramPath);
			if(data!=null) {
				logger.debug("Got diagram bytes from path '{}\'!",diagramPath);
				break;
			}
		}
		if(data==null) {
			logger.debug("Couldnt get diagramFile '{}' from '{}'",diagramFileName,packagePath);
		}
		return data;
	}
	
	public byte[] findDiagramContent(String processId) {
		byte[] data = null;
		Collection<String> filePaths = kieModule.getFileNames();
		for(String filePath : filePaths) {
			if(filePath.matches("^.+\\.bpmn2$")) {
				logger.debug("Checking bpmn2 file '{}'",filePath);
				byte[] fileData = kieModule.getBytes(filePath);
				try {
					Document diagramDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(fileData));
					XPath xpath = XPathFactory.newInstance().newXPath();
					String diagramProcId = (String)xpath.compile("/*[local-name()='definitions']/*[local-name()='process']/@id").evaluate(diagramDoc, XPathConstants.STRING);
					if(diagramProcId.equals(processId)) {
						logger.debug("Found diagram matching processid '{}' in file '{}'",processId,filePath);
						data = fileData;
						break;
					}
				} catch (UnsupportedEncodingException e) {
	                logger.debug("UnsupportedEncodingException while building process diagram due to {}", e.getMessage());
				} catch (SAXException | IOException | ParserConfigurationException e) {
					logger.debug("Exception while building DOM document from diagram bytes due to {}", e.getMessage());
				} catch(XPathExpressionException e) {
					logger.debug("Exception while building XPath for process id due to {}", e.getMessage());
				}
			}
		}
		if(data==null) {
			logger.debug("Couldnt find diagram matching processid '{}'",processId);
		}
		return data;
	}
}