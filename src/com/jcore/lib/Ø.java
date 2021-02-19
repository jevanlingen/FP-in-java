package com.jcore.lib;

import static com.jcore.lib.model.List.list;

import java.util.Objects;
import java.util.function.Supplier;

import com.jcore.lib.model.List;

/**
 * Option class, holds either a value or nothing.
 *
 * @param <A> The value
 */
public abstract class Ø<A> {
	@SuppressWarnings("rawtypes")
	private static final Ø none = new None();

	private Ø() {}

	public abstract A getOrElse(Supplier<A> defaultValue);

	public abstract A getOrThrow();

	public abstract <B> Ø<B> map(ƒ<A, B> f);

	public Ø<A> or(Supplier<Ø<A>> defaultValue) {
		return map(x -> this).getOrElse(defaultValue);
	}

	public <B> Ø<B> flatMap(ƒ<A, Ø<B>> f) {
		return map(f).getOrElse(Ø::none);
	}

	public Ø<A> filter(ƒ<A, Boolean> f) {
		return flatMap(x -> f.apply(x) ? this : none());
	}

	private static class None<A> extends Ø<A> {
		private None() {
		}

		@Override
		public A getOrElse(Supplier<A> defaultValue) {
			return defaultValue.get();
		}

		@Override
		public A getOrThrow() {
			throw new IllegalStateException("Empty value cannot be retrieved");
		}

		@Override
		public <B> Ø<B> map(ƒ<A, B> f) {
			return none();
		}

		@Override
		public String toString() {
			return "None";
		}

		@Override
		public boolean equals(Object o) {
			return o instanceof None;
		}

		@Override
		public int hashCode() {
			return 0;
		}
	}

	private static class Some<A> extends Ø<A> {
		private final A value;

		private Some(final A a) {
			value = a;
		}

		@Override
		public A getOrElse(Supplier<A> defaultValue) {
			return this.value;
		}

		@Override
		public A getOrThrow() {
			return this.value;
		}

		@Override
		public <B> Ø<B> map(ƒ<A, B> f) {
			return some(f.apply(this.value));
		}

		@Override
		public String toString() {
			return "Some(" + this.value + ")";
		}

		@Override
		public boolean equals(Object o) {
			return o instanceof Some && this.value.equals(((Some<?>) o).value);
		}

		@Override
		public int hashCode() {
			return Objects.hash(value);
		}
	}

	public static <A> Ø<A> some(A a) {
		return new Some<>(a);
	}

	@SuppressWarnings("unchecked")
	public static <A> Ø<A> none() {
		return none;
	}

	public static <A, B> ƒ<Ø<A>, Ø<B>> lift(ƒ<A, B> f) {
		return x -> {
			try {
				return x.map(f);
			}
			catch (Exception e) {
				return none();
			}
		};
	}

	public static <A, B> ƒ<A, Ø<B>> hlift(ƒ<A, B> f) {
		return x -> {
			try {
				return some(x).map(f);
			}
			catch (Exception e) {
				return none();
			}
		};
	}

	public static <A> Ø<List<A>> sequence(List<Ø<A>> list) {
		return list.foldRight(Ø.some(list()), x -> y -> x.flatMap(d -> y.map(ls -> ls.cons(d))));
	}
}
