package com.jcore.lib.model;

import java.util.Objects;

public class Tuple<T, U> {
	public final T _1;
	public final U _2;

	public Tuple(T t, U u) {
		Objects.requireNonNull(t, "t must not be null");
		Objects.requireNonNull(u, "u must not be null");

		this._1 = t;
		this._2 = u;
	}
}
