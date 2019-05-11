package io.mrth.webutil.persistence;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TableParam {

	private Integer limit;
	private Integer offset;
	private String sortField;
	private SortOrder sortOrder;
	private Map<String, Object> filters;

	public TableParam() {
	}

	public TableParam(Integer limit, Integer offset) {
		this.limit = limit;
		this.offset = offset;
	}

	public TableParam(Integer limit, Integer offset, String sortField, SortOrder sortOrder) {
		this.limit = limit;
		this.offset = offset;
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	public TableParam(Integer limit, Integer offset, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		this.limit = limit;
		this.offset = offset;
		this.sortField = sortField;
		this.sortOrder = sortOrder;
		this.filters = filters;
	}

	public TableParam(Map<String, List<String>> parameters) {		
		filters = new HashMap<>();
		for (Map.Entry<String, List<String>> entry : parameters.entrySet()) {
			switch (entry.getKey()) {
				case "limit":
					limit = Integer.parseInt(entry.getValue().get(0));
					break;
				case "offset":
					offset = Integer.parseInt(entry.getValue().get(0));
					break;
				case "sortField":
					sortField = entry.getValue().get(0);
					break;
				case "sortOrder":
					sortOrder = SortOrder.valueOf(entry.getValue().get(0));
					break;
				default:
					filters.put(entry.getKey(), entry.getValue().get(0));
			}
		}
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public SortOrder getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Map<String, Object> getFilters() {
		return filters;
	}

	public void setFilters(Map<String, Object> filters) {
		this.filters = filters;
	}

	public static enum SortOrder {
		ASCENDING, DESCENDING, UNSORTED
	}
}
