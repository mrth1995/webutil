package io.mrth.webutil.rest;

import io.mrth.webutil.endpoint.EndpointException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EndpointExceptionMapper implements ExceptionMapper<EndpointException> {

	private final static Logger LOG = LoggerFactory.getLogger(EndpointExceptionMapper.class);

	@Override
	public Response toResponse(EndpointException exception) {
		LOG.error("Map exception " + exception.getMessage(), exception);
		int status = 500;
		if (exception instanceof RestException) {
			status = ((RestException) exception).getHttpStatus();
		}
		return Response.status(status)
				.header("RC", exception.getResponseCode())
				.header("Content-Type", "text/plain")
				.entity(exception.getMessage())
				.build();
	}

}
