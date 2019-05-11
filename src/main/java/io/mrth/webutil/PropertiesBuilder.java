package io.mrth.webutil;

import java.util.Properties;

public class PropertiesBuilder {
	
	private final Properties properties;

	public PropertiesBuilder() {
		properties = new Properties();
	}
	
	public Properties build() {
		return properties;
	}
	
	public PropertiesBuilder propertyString(String key, String value) {
		properties.setProperty(key, value);
		return this;
	}
	
	public PropertiesBuilder propertyInteger(String key, Integer value) {
		properties.setProperty(key, value != null ? value.toString() : null);
		return this;
	}
	
	public PropertiesBuilder propertyLong(String key, Long value) {
		properties.setProperty(key, value != null ? value.toString() : null);
		return this;
	}
	
	public PropertiesBuilder propertyFloat(String key, Float value) {
		properties.setProperty(key, value != null ? value.toString() : null);
		return this;
	}
	
	public PropertiesBuilder propertyDouble(String key, Double value) {
		properties.setProperty(key, value != null ? value.toString() : null);
		return this;
	}
	
	public PropertiesBuilder propertyBoolean(String key, Boolean value) {
		properties.setProperty(key, value != null ? value.toString() : null);
		return this;
	}
}
