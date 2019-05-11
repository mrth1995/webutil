package io.mrth.webutil.persistence;

import javax.persistence.*;
import java.util.*;

/**
 *
 * @param <E>
 */
public class DataTable<E> {

	private final EntityManager entityManager;
	private final String entityVar;
	private final String querySelect;
	private final String queryCount;
	private final String queryGroupBySelect;
	private final String queryGroupByCount;
	private final Map<String, Object> additionalParams;

	public DataTable(EntityManager entityManager, Class<E> entityClass) {
		this(entityManager, entityClass, null, null);
	}

	public DataTable(EntityManager entityManager,
			String entityVar,
			String selectQuery,
			String countQuery,
			String selectGroupBy,
			String countGroupBy,
			Map<String, Object> additionalParams) {
		this.entityManager = entityManager;
		this.entityVar = entityVar;
		this.querySelect = selectQuery;
		this.queryCount = countQuery;
		this.queryGroupBySelect = selectGroupBy;
		this.queryGroupByCount = countGroupBy;
		this.additionalParams = additionalParams;
	}

	public DataTable(EntityManager entityManager, Class<E> entityClass, String selectGroupBy, String countGroupBy) {
		this.entityManager = entityManager;
		String entityName = entityClass.getSimpleName();
		this.entityVar = generateEntityVar(entityName);

		// SELECT-FROM-WHERE
		this.querySelect = "SELECT " + entityVar
				+ " FROM " + entityName + " " + entityVar
				+ " WHERE TRUE = TRUE";

		// SELECT-COUNT-FROM-WHERE
		this.queryCount = "SELECT COUNT(" + entityVar + ")"
				+ " FROM " + entityName + " " + entityVar
				+ " WHERE TRUE = TRUE";

		this.queryGroupBySelect = selectGroupBy;
		this.queryGroupByCount = countGroupBy;
		this.additionalParams = null;
	}

	@SuppressWarnings("unchecked")
	public TableResult<E> load(TableParam paginatedParam, FilterGenerator filterGenerator) {
		StringBuilder filterBuilder = new StringBuilder(64);
		List<Object> params = new ArrayList<>();
		Map<String, Object> filters = paginatedParam.getFilters();
		if (filters != null && !filters.isEmpty()) {
			int i = 0;
			for (Map.Entry<String, Object> entry : filters.entrySet()) {
				Object value = entry.getValue();
				FilterItem filterItem = null;
				if (filterGenerator != null) {
					filterItem = filterGenerator.createFilterItem(entry.getKey(), value);
				}
				if (filterItem == null) {
					filterItem = FilterItem.stringLikeFilter(queryVar(entry.getKey()), value);
				}

				// AND (operand operator ?i)
				filterBuilder.append(" AND (")
						.append(filterItem.getOperand())
						.append(" ").append(filterItem.getOperator())
						.append(" :").append(entityVar).append(i).append(")");
				params.add(filterItem.getValue());
				i++;
			}
		}

		StringBuilder sortingBuilder = new StringBuilder();
		if (paginatedParam.getSortField() != null && !paginatedParam.getSortField().isEmpty()) {
			if (sortingBuilder.length() > 0) {
				sortingBuilder.append(", ");
			}
			sortingBuilder.append(queryVar(paginatedParam.getSortField()))
					.append(" ")
					.append(paginatedParam.getSortOrder() == TableParam.SortOrder.DESCENDING ? "DESC" : "ASC");
		}

		String sorting = "";
		if (sortingBuilder.length() > 0) {
			sorting = " ORDER BY " + sortingBuilder.toString();
		}

		String filterStr = filterBuilder.toString();
		String queryFindStr = querySelect + filterStr + (queryGroupBySelect != null ? (" GROUP BY " + queryGroupBySelect) : "") + sorting;
		String queryCountStr = queryCount + filterStr + (queryGroupByCount != null ? (" GROUP BY " + queryGroupByCount) : "");

		Query queryFindGenerated = entityManager.createQuery(queryFindStr);
		TypedQuery<Long> queryCountGenerated = entityManager.createQuery(queryCountStr, Long.class);

		if (paginatedParam.getLimit() != null) {
			queryFindGenerated.setMaxResults(paginatedParam.getLimit());
		}
		if (paginatedParam.getOffset() != null) {
			queryFindGenerated.setFirstResult(paginatedParam.getOffset());
		}

		int i = 0;
		for (Object param : params) {
			queryFindGenerated.setParameter(entityVar + i, param);
			queryCountGenerated.setParameter(entityVar + i, param);
			i++;
		}

		if (additionalParams != null) {
			for (Map.Entry<String, Object> entry : additionalParams.entrySet()) {
				Object value = entry.getValue();
				if (value != null && value instanceof Date) {
					TemporalType temporalType = entry.getKey().toLowerCase().contains("date") 
							? TemporalType.DATE : TemporalType.TIMESTAMP;
					queryFindGenerated.setParameter(entry.getKey(), (Date) value, temporalType);
					queryCountGenerated.setParameter(entry.getKey(), (Date) value, temporalType);
				} else {
					queryFindGenerated.setParameter(entry.getKey(), value);
					queryCountGenerated.setParameter(entry.getKey(), value);
				}
			}
		}

		List<E> result = queryFindGenerated.getResultList();
		Long count;
		try {
			count = queryCountGenerated.getSingleResult();
		} catch (NoResultException e) {
			count = 0L;
		}

		return new TableResult<>(result, count);
	}

	public TableResult<E> load(TableParam paginatedParam) {
		return load(paginatedParam, null);
	}

	public String queryVar(String param) {
		// TODO: Sanitize
		if (param.startsWith(":")) {
			return param.substring(1);
		} else {
			return entityVar + "." + param;
		}
	}

	private String generateEntityVar(String entityName) {
		return entityName.substring(0, 1).toLowerCase(Locale.getDefault()) + entityName.substring(1);
	}
}
