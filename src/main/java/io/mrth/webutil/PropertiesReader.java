package io.mrth.webutil;

import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

public class PropertiesReader {

	private final Properties properties;

	public PropertiesReader(Properties properties) {
		if (properties != null) {
			this.properties = properties;
		} else {
			this.properties = new Properties();
		}
	}

	public String getPropertyString(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	public Boolean getPropertyBoolean(String key, Boolean defaultValue) {
		String value = properties.getProperty(key);
		if (StringUtils.isEmpty(value)) {
			return defaultValue;
		} else {
			return Boolean.parseBoolean(value);
		}
	}

	public Integer getPropertyInteger(String key, Integer defaultValue) {
		String value = properties.getProperty(key);
		if (StringUtils.isEmpty(value)) {
			return defaultValue;
		} else {
			return Integer.parseInt(value);
		}
	}

	public Long getPropertyLong(String key, Long defaultValue) {
		String value = properties.getProperty(key);
		if (StringUtils.isEmpty(value)) {
			return defaultValue;
		} else {
			return Long.parseLong(value);
		}
	}

	public Float getPropertyFloat(String key, Float defaultValue) {
		String value = properties.getProperty(key);
		if (StringUtils.isEmpty(value)) {
			return defaultValue;
		} else {
			return Float.parseFloat(value);
		}
	}

	public Double getPropertyDouble(String key, Double defaultValue) {
		String value = properties.getProperty(key);
		if (StringUtils.isEmpty(value)) {
			return defaultValue;
		} else {
			return Double.parseDouble(value);
		}
	}
}
