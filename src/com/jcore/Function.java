package com.jcore;

public interface Function<T, U>{
	U apply(T arg);

	static <T, U, V> Function<Function<U, V>, Function<Function<T, U>, Function<T, V>>> higherCompose() {
		return x -> y -> z -> x.apply(y.apply(z));
	}

	static <T, U, V> Function<Function<T, U>, Function<Function<U, V>, Function<T, V>>> higherAndThen() {
		return x -> y -> z -> y.apply(x.apply(z));
	}
}
