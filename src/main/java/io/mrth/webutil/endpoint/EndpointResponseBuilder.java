package io.mrth.webutil.endpoint;

import java.util.HashMap;
import java.util.Map;

public class EndpointResponseBuilder {

	private final DefaultEndpointResponse response;

	public EndpointResponseBuilder() {
		response = new DefaultEndpointResponse();
	}

	public EndpointResponse build() {
		return response;
	}

	public EndpointResponseBuilder requestId(String requestId) {
		response.requestId = requestId;
		return this;
	}

	public EndpointResponseBuilder correlationId(String correlationId) {
		response.correlationId = correlationId;
		return this;
	}

	public EndpointResponseBuilder properties(Map<String, Object> props) {
		if (props != null) {
			response.properties.putAll(props);
		}
		return this;
	}

	public EndpointResponseBuilder property(String key, Object value) {
		response.properties.put(key, value);
		return this;
	}

	public EndpointResponseBuilder content(byte[] content, String contentType) {
		response.content = content;
		response.contentType = contentType;
		return this;
	}

	public static class DefaultEndpointResponse implements EndpointResponse {

		private String requestId;
		private String correlationId;
		private final Map<String, Object> properties = new HashMap<>();
		private byte[] content;
		private String contentType;

		private DefaultEndpointResponse() {
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
