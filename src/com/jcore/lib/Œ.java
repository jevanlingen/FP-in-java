package com.jcore.lib;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.jcore.chapter.three.Result;

/**
 * Result class, holds either a value or a exception.
 *
 * @param <V> The value
 */
public abstract class Œ<V> implements Serializable {
	@SuppressWarnings("rawtypes")
	private static Œ empty = new Empty();

	private Œ() {}

	public abstract V getOrElse(final V defaultValue);

	public abstract V getOrElse(final Supplier<V> defaultValue);

	public abstract <U> Œ<U> map(final ƒ<V, U> f);

	public abstract <U> Œ<U> flatMap(final ƒ<V, Œ<U>> f);

	public abstract Œ<V> mapFailure(final String message);

	public abstract void forEach(Consumer<V> effect);

	public abstract void forEachOrThrow(Consumer<V> effect);

	public abstract Œ<RuntimeException> forEachOrException(Consumer<V> effect);

	public Œ<V> or(final Supplier<Œ<V>> defaultValue) {
		return map(x -> this).getOrElse(defaultValue);
	}

	public Œ<V> filter(final ƒ<V, Boolean> f) {
		return flatMap(x -> f.apply(x)
				? this
				: failure("Condition did not match"));
	}

	public Œ<V> filter(final ƒ<V, Boolean> f, String errorMessage) {
		return flatMap(x -> f.apply(x)
				? this
				: failure(errorMessage));
	}

	public boolean exists() {
		return exists(a -> true);
	}

	public boolean exists(final ƒ<V, Boolean> f) {
		return map(f).getOrElse(false);
	}

	public static <A, B, C> Œ<C> map2(final Œ<A> a, final Œ<B> b,final ƒ<A, ƒ<B, C>> f) {
		return lift2(f).apply(a).apply(b);
	}

	public static <A, B, C> ƒ<Œ<A>, ƒ<Œ<B>, Œ<C>>> lift2(final ƒ<A, ƒ<B, C>> f) {
		return a -> b -> a.map(f).flatMap(b::map);
	}

	private static class Empty<V> extends Œ<V> {
		@Override
		public V getOrElse(V defaultValue) {
			return defaultValue;
		}

		@Override
		public V getOrElse(Supplier<V> defaultValue) {
			return defaultValue.get();
		}

		@Override
		public <U> Œ<U> map(ƒ<V, U> f) {
			return empty();
		}

		@Override
		public <U> Œ<U> flatMap(ƒ<V, Œ<U>> f) {
			return empty();
		}

		@Override
		public Œ<V> mapFailure(String message) {
			return this;
		}

		@Override
		public void forEach(Consumer<V> effect) {
			// Do nothing
		}

		@Override
		public void forEachOrThrow(Consumer<V> effect) {
			// Do nothing
		}

		@Override
		public Œ<RuntimeException> forEachOrException(Consumer<V> effect) {
			return empty();
		}

		@Override
		public String toString() {
			return "Empty()";
		}
	}

	private static class Failure<V> extends Empty<V> {
		private final RuntimeException exception;

		private Failure(final String message) {
			this.exception = new IllegalStateException(message);
		}

		private Failure(final RuntimeException exception) {
			this.exception = exception;
		}

		private Failure(final Exception e) {
			this.exception = new IllegalStateException(e.getMessage(), e);
		}

		@Override
		public <U> Œ<U> map(ƒ<V, U> f) {
			return failure(exception);
		}

		@Override
		public <U> Œ<U> flatMap(ƒ<V, Œ<U>> f) {
			return failure(exception);
		}

		@Override
		public Œ<V> mapFailure(String message) {
			return failure(new IllegalStateException(message, exception));
		}

		@Override
		public void forEachOrThrow(Consumer<V> effect) {
			throw exception;
		}

		@Override
		public Œ<RuntimeException> forEachOrException(Consumer<V> effect) {
			return success(exception);
		}

		@Override
		public String toString() {
			return "Failure (" + exception.getMessage() + ")";
		}
	}

	private static class Success<V> extends Œ<V> {
		private final V value;

		private Success(final V value) {
			this.value = value;
		}

		@Override
		public V getOrElse(V defaultValue) {
			return value;
		}

		@Override
		public V getOrElse(Supplier<V> defaultValue) {
			return value;
		}

		@Override
		public <U> Œ<U> map(ƒ<V, U> f) {
			try {
				return success(f.apply(value));
			}
			catch (Exception e) {
				return failure(e);
			}
		}

		@Override
		public <U> Œ<U> flatMap(ƒ<V, Œ<U>> f) {
			try {
				return f.apply(value);
			}
			catch (Exception e) {
				return failure(e);
			}
		}

		@Override
		public Œ<V> mapFailure(String message) {
			return this;
		}

		@Override
		public void forEach(Consumer<V> effect) {
			effect.accept(value);
		}

		@Override
		public void forEachOrThrow(Consumer<V> effect) {
			effect.accept(value);
		}

		@Override
		public Œ<RuntimeException> forEachOrException(Consumer<V> effect) {
			forEach(effect);
			return empty();
		}

		@Override
		public String toString() {
			return "Success (" + value.toString() + ")";
		}
	}

	public static <V> Œ<V> failure(final String message) {
		return new Failure<>(message);
	}

	public static <V> Œ<V> failure(final RuntimeException exception) {
		return new Failure<>(exception);
	}

	public static <V> Œ<V> failure(final Exception exception) {
		return new Failure<>(exception);
	}

	public static <V> Œ<V> success(final V value) {
		return new Success<>(value);
	}

	public static <V> Œ<V> empty() {
		return empty;
	}

	public static <V> Œ<V> of(final V value) {
		return of(value, "Null value");
	}

	public static <V> Œ<V> of(final V value, final String errorMessage) {
		return value == null
				? failure(errorMessage)
				: success(value);
	}

	public static <V> Œ<V> of(ƒ<V, Boolean> f, final V value) {
		return of(f, value, "Null value");
	}

	public static <V> Œ<V> of(ƒ<V, Boolean> f, final V value, final String message) {
		try {
			return f.apply(value)
					? success(value)
					: failure(message);
		}
		catch (Exception e) {
			return failure(new IllegalStateException("Exception while evaluating: " + value, e));
		}
	}

	public static <A, B> ƒ<Œ<A>, Œ<B>> lift(final ƒ<A, B> f) {
		return x -> {
			try {
				return x.map(f);
			} catch (Exception e) {
				return failure(e);
			}
		};
	}

	public static <A, B> ƒ<A, Œ<B>> hlift(ƒ<A, B> f) {
		return x -> {
			try {
				return success(x).map(f);
			}
			catch (Exception e) {
				return failure(e);
			}
		};
	}

	public static <T> Œ<T> doTry(Supplier<T> f) {
		try {
			return Œ.success(f.get());
		} catch (Exception e) {
			return Œ.failure(e);
		}
	}
}
