package io.mrth.webutil.endpoint;

import java.util.Map;

public interface EndpointRequest {
	
	String getRequestId();
	
	String getCorrelationId();
	
	String getResource();
			
	String getMethod();
	
	Map<String, Object> getProperties();
	
	byte[] getContent();
	
	String getContentType();
}
