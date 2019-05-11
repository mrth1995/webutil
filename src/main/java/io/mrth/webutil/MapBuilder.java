package io.mrth.webutil;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @param <K>
 * @param <V>
 */
public class MapBuilder<K, V> {

	private final Map<K, V> map = new HashMap<>();

	public MapBuilder() {
	}

	public MapBuilder(Map<? extends K, ? extends V> existingMap) {
		if (existingMap != null) {
			map.putAll(existingMap);
		}
	}

	public MapBuilder<K, V> put(K key, V value) {
		map.put(key, value);
		return this;
	}

	public MapBuilder<K, V> putAll(Map<? extends K, ? extends V> existingMap) {
		if (existingMap != null) {
			map.putAll(existingMap);
		}
		return this;
	}
	
	public Map<K, V> build() {
		return map;
	}
}
