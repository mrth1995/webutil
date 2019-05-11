package io.mrth.webutil.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.mrth.webutil.PropertiesReader;
import io.mrth.webutil.json.Json;
import io.mrth.webutil.rest.RestException;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpResponseException;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.BytesContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RestEndpoint implements Endpoint {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(RestEndpoint.class);
	protected static final String DEFAULT_ERROR_CODE = "99";

	public static final String PROP_TIMEOUT_SECONDS = "timeoutSeconds";
	public static final String PROP_RESPONSE_CODE_HEADER = "responseCodeHeader";
	public static final String PROP_RESPONSE_MESSAGE_HEADER = "responseMessageHeader";
	public static final String PROP_PARSE_JSON = "parseJson";
	public static final String PROP_HOST_ID = "hostId";

	private HttpClient client;
	private String baseUrl;
	private int timeoutSeconds;
	private String responseCodeHeader;
	private String responseMessageHeader;
	private String hostId;
	private boolean parseJson;

	public void initialize(HttpClient client, String baseUrl, Properties properties) {
		this.client = client;
		this.baseUrl = baseUrl;

		PropertiesReader reader = new PropertiesReader(properties);
		this.hostId = reader.getPropertyString(PROP_HOST_ID, getClass().getName());
		this.timeoutSeconds = reader.getPropertyInteger(PROP_TIMEOUT_SECONDS, 60);
		this.parseJson = reader.getPropertyBoolean(PROP_PARSE_JSON, true);
		this.responseCodeHeader = reader.getPropertyString(PROP_RESPONSE_CODE_HEADER, "RC");
		this.responseMessageHeader = reader.getPropertyString(PROP_RESPONSE_MESSAGE_HEADER, null);
	}

	@Override
	public String getHostId() {
		return hostId;
	}

	@Override
	public EndpointResponse send(EndpointRequest endpointRequest) throws EndpointException {
		try {
			Request request = createHttpRequest(client, endpointRequest);
			ContentResponse response = request.send();
			return readHttpResponse(endpointRequest, response);
		} catch (ExecutionException e) {
			LOG.error("Cannot connect to '" + endpointRequest.getResource() + "': " + e.getMessage(), e);
			if (e.getCause() instanceof HttpResponseException) {
				HttpResponseException httpResponseException = (HttpResponseException) e.getCause();
				return readHttpResponse(endpointRequest, (ContentResponse) httpResponseException.getResponse());
			}
			throw new EndpointException(DEFAULT_ERROR_CODE, e.getCause().getMessage());
		} catch (TimeoutException | InterruptedException e) {
			LOG.error("Cannot connect to '" + endpointRequest.getResource() + "': " + e.getMessage(), e);
			throw new EndpointException(DEFAULT_ERROR_CODE, e.getMessage());
		}
	}

	protected Request createHttpRequest(HttpClient client, EndpointRequest endpointRequest) throws EndpointException {
		String address = baseUrl;
		if (StringUtils.isNotEmpty(endpointRequest.getResource())) {
			String separator = (!baseUrl.endsWith("/") && !endpointRequest.getResource().startsWith("/")) ? "/" : "";
			address = baseUrl + separator + endpointRequest.getResource();
		}
		Request request = client.newRequest(address)
				.method(endpointRequest.getMethod())
				.idleTimeout(timeoutSeconds, TimeUnit.SECONDS);
		if (StringUtils.isNotEmpty(endpointRequest.getRequestId())) {
			request.header("requestId", endpointRequest.getRequestId());
		}
		if (StringUtils.isNotEmpty(endpointRequest.getCorrelationId())) {
			request.header("correlationId", endpointRequest.getCorrelationId());
		}
		if (endpointRequest.getMethod().equalsIgnoreCase("POST")
				|| endpointRequest.getMethod().equalsIgnoreCase("PUT")) {
			if (endpointRequest.getContent() != null) {
				endpointRequest.getProperties().entrySet().forEach((entry) -> {
					request.param(entry.getKey(), entry.getValue() != null ? entry.getValue().toString() : null);
				});
				request.content(new BytesContentProvider(
						endpointRequest.getContentType(), endpointRequest.getContent()));
				request.getHeaders().add(HttpHeader.CONTENT_TYPE, endpointRequest.getContentType());
			} else if (endpointRequest.getProperties() != null) {
				try {
					request.content(new BytesContentProvider(
							"application/json", Json.getWriter().writeValueAsBytes(endpointRequest.getProperties())));
					request.getHeaders().add(HttpHeader.CONTENT_TYPE, "application/json");
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			}
		} else { // GET, DELETE
			if (endpointRequest.getProperties() != null) {
				endpointRequest.getProperties().entrySet().forEach((entry) -> {
					request.param(entry.getKey(), entry.getValue() != null ? entry.getValue().toString() : null);
				});
			}
		}
		return request;
	}

	protected EndpointResponse readHttpResponse(EndpointRequest request, ContentResponse response) throws EndpointException {
		boolean isSuccess = response.getStatus() / 100 == 2;
		if (isSuccess) {
			Map<String, Object> properties = null;
			if (response.getMediaType() != null && response.getMediaType().equals("application/json") && parseJson) {
				String contentString = response.getContentAsString().trim();
				if (contentString.startsWith("{")) {
					try {
						properties = Json.getReader()
								.forType(new TypeReference<Map<String, Object>>() {
								})
								.readValue(contentString);
					} catch (IOException e) {
						throw new RuntimeException(e.getMessage(), e);
					}
				}
			}
			return new EndpointResponseBuilder()
					.requestId(request.getRequestId())
					.correlationId(request.getCorrelationId())
					.properties(properties)
					.content(response.getContent(), response.getMediaType())
					.build();
		} else {
			throw new RestException(response.getStatus(), readResponseCode(response), readResponseMessage(response));
		}
	}

	protected String readResponseCode(ContentResponse response) {
		String rc = null;
		if (StringUtils.isNotEmpty(responseCodeHeader)) {
			rc = response.getHeaders().get(responseCodeHeader);
		}
		if (StringUtils.isEmpty(rc)) {
			rc = DEFAULT_ERROR_CODE;
		}
		return rc;
	}

	protected String readResponseMessage(ContentResponse response) {
		if (StringUtils.isNotEmpty(responseMessageHeader)) {
			return response.getHeaders().get(responseMessageHeader);
		} else {
			String str = response.getContentAsString();
			if (str != null && str.length() > 255) {
				return str.substring(0, 255);
			} else {
				return str;
			}
		}
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public int getTimeoutSeconds() {
		return timeoutSeconds;
	}

	public String getResponseCodeHeader() {
		return responseCodeHeader;
	}

	public String getResponseMessageHeader() {
		return responseMessageHeader;
	}

	public boolean isParseJson() {
		return parseJson;
	}

}
