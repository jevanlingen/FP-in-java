package com.jcore.chapter.three;

import java.util.function.Supplier;

import com.jcore.Tuple;

public class Case<T> extends Tuple<Supplier<Boolean>, Supplier<Result<T>>> {

	public Case(final Supplier<Boolean> booleanSupplier, final Supplier<Result<T>> resultSupplier) {
		super(booleanSupplier, resultSupplier);
	}

	public static <T> Case<T> mcase(final Supplier<Boolean> condition, final Supplier<Result<T>> value) {
		return new Case<>(condition, value);
	}

	public static <T> DefaultCase<T> mcase(final Supplier<Result<T>> value) {
		return new DefaultCase<>(() -> true, value);
	}

	public static <T> Result<T> match(final DefaultCase<T> defaultCase, final Case<T>... matchers) {
		for (Case<T> tCase : matchers) {
			if (tCase._1.get()) {
				return tCase._2.get();
			}
		}
		return defaultCase._2.get();
	}

	private static class DefaultCase<T> extends Case<T> {
		public DefaultCase(final Supplier<Boolean> booleanSupplier, final Supplier<Result<T>> resultSupplier) {
			super(booleanSupplier, resultSupplier);
		}
	}
}
