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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="response")
@XmlType(name="ServiceResponse",namespace="pl.surreal.kie.server.api.jbpmstats")
@XmlAccessorType(XmlAccessType.NONE)
public class ServiceResponse<T>
{
	@XmlType(name="ResponseType",namespace="pl.surreal.kie.server.api.jbpmstats")
    public static enum ResponseType {
        SUCCESS, FAILURE;
    }
    
    @XmlAttribute
    private ServiceResponse.ResponseType type;
    @XmlAttribute
    private String msg;
    
    @XmlElements({        
        @XmlElement(name = "process-statistics", type = ProcessStatistics.class)
        })
    private T result;
    
    public ServiceResponse() {
    }
    
    public ServiceResponse(ServiceResponse.ResponseType type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public ServiceResponse(ServiceResponse.ResponseType type, String msg, T result) {
        this.type = type;
        this.msg = msg;
        this.result = result;
    }

    public ServiceResponse.ResponseType getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

    public void setType(ServiceResponse.ResponseType type) {
        this.type = type;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ServiceResponse[" + type + ", msg='" + msg + "']";
    }    
}
