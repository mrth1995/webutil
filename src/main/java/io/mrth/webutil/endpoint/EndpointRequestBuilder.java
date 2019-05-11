package io.mrth.webutil.endpoint;

import io.mrth.webutil.IDGen;

import java.util.HashMap;
import java.util.Map;

public class EndpointRequestBuilder {

	private final DefaultEndpointRequest request;

	public EndpointRequestBuilder() {
		request = new DefaultEndpointRequest();
		request.requestId = IDGen.generate();
		request.correlationId = request.requestId;
	}

	public EndpointRequest build() {
		return request;
	}

	public EndpointRequestBuilder requestId(String requestId) {
		request.requestId = requestId;
		return this;
	}

	public EndpointRequestBuilder correlationId(String correlationId) {
		request.correlationId = correlationId;
		return this;
	}
	
	public EndpointRequestBuilder resource(String resource) {
		request.resource = resource;
		return this;
	}
	
	public EndpointRequestBuilder method(String method) {
		request.method = method;
		return this;
	}

	public EndpointRequestBuilder properties(Map<String, Object> props) {
		if (props != null) {
			request.properties.putAll(props);
		}
		return this;
	}

	public EndpointRequestBuilder property(String key, Object value) {
		request.properties.put(key, value);
		return this;
	}

	public EndpointRequestBuilder content(byte[] content, String contentType) {
		request.content = content;
		request.contentType = contentType;
		return this;
	}

	public static class DefaultEndpointRequest implements EndpointRequest {

		private String requestId;
		private String correlationId;
		private String resource;
		private String method;
		private final Map<String, Object> properties = new HashMap<>();
		private byte[] content;
		private String contentType;

		private DefaultEndpointRequest() {
		}

		@Override
		public String getRequestId() {
			return requestId;
		}

		@Override
		public String getCorrelationId() {
			return correlationId;
		}

		@Override
		public String getResource() {
			return resource;
		}

		@Override
		public String getMethod() {
			return method;
		}

		@Override
		public Map<String, Object> getProperties() {
			return properties;
		}

		@Override
		public byte[] getContent() {
			return content;
		}

		@Override
		public String getContentType() {
			return contentType;
		}

	}

}
