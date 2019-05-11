package io.mrth.webutil.endpoint;

public class EndpointException extends Exception {

	private static final long serialVersionUID = 1L;

	private final String responseCode;

	public EndpointException(String responseCode, String message) {
		super(message);
		this.responseCode = responseCode;
	}

	public EndpointException(String responseCode, String message, Throwable cause) {
		super(message, cause);
		this.responseCode = responseCode;
	}
	
	public String getResponseCode() {
		return responseCode;
	}

}
