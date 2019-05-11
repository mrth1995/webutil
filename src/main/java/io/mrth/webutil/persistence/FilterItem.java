package io.mrth.webutil.persistence;

import java.util.Locale;

public class FilterItem {

	private final String operator;
	private final String operand;
	private final Object value;

	public FilterItem(String operand, String operator, Object value) {
		this.operator = operator;
		this.operand = operand;
		this.value = value;
	}

	public String getOperator() {
		return operator;
	}

	public String getOperand() {
		return operand;
	}

	public Object getValue() {
		return value;
	}

	public static FilterItem booleanFilter(String operand, Object value) {
		String valueStr = "" + value;
		return new FilterItem(operand, " = ", Boolean.valueOf(valueStr));
	}

	public static FilterItem stringLikeFilter(String operand, Object value) {
		String valueStr = "" + value;
		return new FilterItem(
				"lower(" + operand + ")",
				"LIKE",
				"%" + valueStr.toLowerCase(Locale.getDefault()) + "%");
	}

	public static FilterItem stringMatchFilter(String operand, Object value) {
		String valueStr = "" + value;
		return new FilterItem(operand, " = ", valueStr);
	}
}
