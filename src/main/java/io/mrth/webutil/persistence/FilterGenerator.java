package io.mrth.webutil.persistence;

public interface FilterGenerator {

	public FilterItem createFilterItem(String fieldName, Object valueStr);
}
