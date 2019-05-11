package io.mrth.webutil.endpoint;

import java.util.Map;

public interface EndpointResponse {

	String getRequestId();
	
	String getCorrelationId();
	
	Map<String, Object> getProperties();
	
	byte[] getContent();
	
	String getContentType();
}
