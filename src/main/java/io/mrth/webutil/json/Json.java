package io.mrth.webutil.json;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

public class Json {

	private static Json instance;
	private final ObjectMapper objectMapper;
	private final ObjectReader reader;
	private final ObjectWriter writer;

	public static Json getInstance() {
		if (instance == null) {
			instance = new Json();
		}
		return instance;
	}

	private Json() {
		objectMapper = new ObjectMapper()
				.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
				.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, false);
		JaxbAnnotationModule jaxbModule = new JaxbAnnotationModule();
		objectMapper.registerModule(jaxbModule);
		reader = objectMapper.reader();
		writer = objectMapper.writer();
	}

	public static ObjectReader getReader() {
		return getInstance().reader;
	}

	public static ObjectWriter getWriter() {
		return getInstance().writer;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}
}
