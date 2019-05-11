package io.mrth.webutil.persistence;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @param <E> Entity class
 * @param <K> Entity primary key class
 */
public abstract class JpaRepository<E, K> {

	private final Class<E> entityClass;

	public JpaRepository(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	public E find(K id) {
		if (id != null) {
			return getEntityManager().find(entityClass, id);
		} else {
			return null;
		}
	}

	public E findAndLock(K id, LockModeType lock) {
		if (id != null) {
			return getEntityManager().find(entityClass, id, lock);
		} else {
			return null;
		}
	}

	public List<E> findAll() {
		return findAll(null, null);
	}

	public List<E> findAll(Integer firstResult, Integer maxResult) {
		TypedQuery<E> queryFind = getEntityManager().createQuery(
				"SELECT e FROM " + getEntityName() + " e", entityClass);
		if (firstResult != null) {
			queryFind.setFirstResult(firstResult);
		}
		if (maxResult != null) {
			queryFind.setMaxResults(maxResult);
		}
		List<E> result = queryFind.getResultList();
		return result;
	}

	public abstract EntityManager getEntityManager();

	protected String getEntityName() {
		return entityClass.getSimpleName();
	}

	public void store(E entity) {
		getEntityManager().persist(entity);
	}

	public E update(E entity) {
		return getEntityManager().merge(entity);
	}

	public void delete(E entity) {
		getEntityManager().remove(getEntityManager().merge(entity));
	}
}
