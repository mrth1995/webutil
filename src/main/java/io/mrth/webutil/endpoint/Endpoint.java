package io.mrth.webutil.endpoint;

import java.io.Serializable;

public interface Endpoint extends Serializable {
	
	EndpointResponse send(EndpointRequest request) throws EndpointException;

	String getHostId();
}
