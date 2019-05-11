package io.mrth.webutil.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.ObserverException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 *
 * @param <T>
 */
public class DefaultExceptionMapper<T extends Throwable> implements ExceptionMapper<T> {

	private final static Logger LOG = LoggerFactory.getLogger(DefaultExceptionMapper.class);

	@Override
	public Response toResponse(T exception) {
		LOG.error("Map exception: {}", exception.getMessage(), exception);
		String responseCode = "99";
		String responseMessage = exception.getMessage();
		if (exception instanceof ObserverException) {
			ObserverException e = (ObserverException) exception;
			if (e.getCause() != null) {
				if (e.getCause() instanceof RestException) {
					RestException restException = (RestException) e.getCause();
					responseCode = restException.getResponseCode();
				}
				responseMessage = e.getCause().getMessage();
			}
		}
		return Response.status(500)
				.header("RC", responseCode)
				.header("Content-Type", "text/plain")
				.entity(responseMessage)
				.build();
	}

}
