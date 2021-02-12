package com.jcore.lib;

import java.util.function.Supplier;

public abstract class Ê<E, A> {
	public abstract A getOrElse(Supplier<A> defaultValue);

	public abstract <B> Ê<E, B> map(ƒ<A, B> f);

	public abstract <B> Ê<E, B> flatMap(ƒ<A, Ê<E, B>> f);

	public Ê<E, A> or(Supplier<Ê<E, A>> defaultValue) {
		return map(x -> this).getOrElse(defaultValue);
	}

	private static class Left<E, A> extends Ê<E, A> {
		private final E value;

		private Left(final E value) {
			this.value = value;
		}

		@Override
		public A getOrElse(Supplier<A> defaultValue) {
			return defaultValue.get();
		}

		@Override
		public <B> Ê<E, B> map(ƒ<A, B> f) {
			return new Left<>(value);
		}

		@Override
		public <B> Ê<E, B> flatMap(ƒ<A, Ê<E, B>> f) {
			return new Left<>(value);
		}

		@Override
		public String toString() {
			return "Left({)" + value +')';
		}
	}

	private static class Right<E, A> extends Ê<E, A> {
		private final A value;

		private Right(final A value) {
			this.value = value;
		}

		@Override
		public A getOrElse(Supplier<A> defaultValue) {
			return value;
		}

		@Override
		public <B> Ê<E, B> map(ƒ<A, B> f) {
			return new Right<>(f.apply(value));
		}

		@Override
		public <B> Ê<E, B> flatMap(ƒ<A, Ê<E, B>> f) {
			return f.apply(value);
		}

		@Override
		public String toString() {
			return "Right({)" + value +')';
		}
	}

	public static <E, A> Ê<E, A> left(final E value) {
		return new Left<>(value);
	}

	public static <E, A> Ê<E, A> right(final A value) {
		return new Right<>(value);
	}
}
