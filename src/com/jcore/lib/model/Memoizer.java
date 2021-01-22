package com.jcore.lib.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jcore.lib.ƒ;

public class Memoizer<T, U> {
	private final Map<T, U> cache = new ConcurrentHashMap<>();

	private Memoizer() {
	}

	public static <T, U>ƒ<T, U> memoize(final ƒ<T, U> f) {
		return new Memoizer<T, U>().doMemoize(f);
	}

	private ƒ<T,U> doMemoize(ƒ<T, U> f) {
		return i -> cache.computeIfAbsent(i, f::apply);
	}
}
