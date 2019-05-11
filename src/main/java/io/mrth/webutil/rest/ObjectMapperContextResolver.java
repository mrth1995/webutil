package io.mrth.webutil.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mrth.webutil.json.Json;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return Json.getInstance().getObjectMapper();
	}
}
