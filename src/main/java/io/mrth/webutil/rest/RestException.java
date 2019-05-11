package io.mrth.webutil.rest;

import io.mrth.webutil.endpoint.EndpointException;

/**
 *
 * @author Ginan <ginanjarpramadita@gmail.com>
 */
public class RestException extends EndpointException {

	private static final long serialVersionUID = 1L;

	private int httpStatus;
	private String originalResponseCode;
	private String prefixResponseCode;
	
	public RestException(int httpStatus, String responseCode, String message) {
		super(responseCode, message);
		this.originalResponseCode = responseCode;
		this.httpStatus = httpStatus;
	}
	
	public RestException(int httpStatus, String responseCode, String message, String prefixResponseCode) {
		super(prefixResponseCode != null ? prefixResponseCode + responseCode : responseCode, message);
		this.prefixResponseCode = prefixResponseCode;
		this.originalResponseCode = responseCode;
		this.httpStatus = httpStatus;
	}
	
	public RestException(String message) {
		this(400, "99", message);
	}

	public RestException() {
		this(400, "99", "Unknown Error");
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public String getPrefixResponseCode() {
		return prefixResponseCode;
	}

	public String getOriginalResponseCode() {
		return originalResponseCode;
	}

}
