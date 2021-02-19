package com.jcore.lib.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.jcore.lib.Œ;

public class Map<T, U> {
	private final ConcurrentMap<T,U> map = new ConcurrentHashMap<>();

	public static <T,U> Map<T, U> empty() {
		return new Map<>();
	}

	public static <T,U> Map<T,U> add(final Map<T,U> m, final T t, final U u) {
		m.map.put(t, u);
		return m;
	}

	public Œ<U> get(final T t) {
		return this.map.containsKey(t)
				? Œ.success(this.map.get(t))
				: Œ.empty();
	}

	public Map<T, U> put(final T t, final U u) {
		return add(this, t, u);
	}

	public Map<T, U> removeKey(final T t) {
		this.map.remove(t);
		return this;
	}
}
