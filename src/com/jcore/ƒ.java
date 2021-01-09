package com.jcore;

import java.util.function.Function;

public interface ƒ<T, R> extends Function<T,R> {
	static <T, U, V> ƒ<V, U> compose(ƒ<T, U> f, ƒ<V, T> g) {
		return x -> f.apply(g.apply(x));
	}

	static <T, U, V> ƒ<T, V> andThen(ƒ<T, U> f, ƒ<U, V> g) {
		return x -> g.apply(f.apply(x));
	}

	static <T, U, V> ƒ<ƒ<T, U>, ƒ<ƒ<U, V>, ƒ<T, V>>> compose() {
		return x -> y -> (ƒ) y.compose(x);
	}

	static <T, U, V> ƒ<ƒ<T, U>, ƒ<ƒ<V, T>, ƒ<V, U>>> andThen() {
		return x -> y -> (ƒ) y.andThen(x);
	}

	static <T, U, V> ƒ<ƒ<U, V>, ƒ<ƒ<T, U>, ƒ<T, V>>> higherCompose() {
		return x -> y -> z -> x.apply(y.apply(z));
	}

	static <T, U, V> ƒ<ƒ<T, U>, ƒ<ƒ<U, V>, ƒ<T, V>>> higherAndThen() {
		return x -> y -> z -> y.apply(x.apply(z));
	}
}
