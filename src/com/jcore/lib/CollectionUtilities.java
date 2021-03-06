package com.jcore.lib;

import static com.jcore.lib.model.TailCall.ret;
import static com.jcore.lib.model.TailCall.sus;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import com.jcore.lib.model.TailCall;

public class CollectionUtilities {
	public static <T> List<T> list() {
		return emptyList();
	}

	public static <T> List<T> list(final T t) {
		return singletonList(t);
	}

	public static <T> List<T> list(final List<T> ts) {
		return unmodifiableList(copy(ts));
	}

	@SafeVarargs
	public static <T> List<T> list(final T... t) {
		return list(asList(t));
	}

	public static <T> List<T> prepend(final T t, final List<T> ts) {
		final var list = copy(ts);
		list.add(0, t);
		return unmodifiableList(list);
	}

	public static <T> List<T> append(final List<T> ts, final T t) {
		final var list = copy(ts);
		list.add(t);
		return unmodifiableList(list);
	}

	public static <T> T head(final List<T> ts) {
		if (ts.isEmpty()) {
			// TODO: this is not functional
			throw new IllegalArgumentException("head of empty ts");
		}

		return ts.get(0);
	}

	public static <T> List<T> tail(final List<T> ts) {
		if (ts.isEmpty()) {
			// TODO: this is not functional
			throw new IllegalArgumentException("tail of empty ts");
		}

		return unmodifiableList(copy(ts).subList(1, ts.size()));
	}

	public static <U, T> List<U> map(final List<T> ts, final ƒ<T, U> f) {
		return foldLeft(ts, list(), x -> y -> append(x, f.apply(y)));
	}

	public static <T, U> U foldLeft(final List<T> ts, final U identity, final ƒ<U, ƒ<T, U>> f) {
		U result = identity;
		for (T t : ts) {
			result = f.apply(result).apply(t);
		}
		return result;
	}

	public static <T, U> U foldRight(final List<T> ts, final U identity, final ƒ<T,  ƒ<U, U>> f) {
		U result = identity;
		for (int i = ts.size(); i > 0; i--) {
			result = f.apply(ts.get(i - 1)).apply(result);
		}

		return result;
	}

	public static <T> List<T> reverse(final List<T> ts) {
		return foldLeft(ts, list(), x -> y -> prepend(y, x));
		//return foldRight(ts, list(), x -> y -> append(y, x));
	}

	public static <T> void forEach(final Collection<T> ts, final Consumer<T> e) {
		for (T t : ts) {
			e.accept(t);
		}
	}

	public static List<Integer> range(int start, int end) {
		return unfold(start, x -> x + 1, x -> x < end);
	}

	public static List<Integer> rangeRecursive(int start, int end) {
		return rangeRecursive_(start, end, list()).eval();
	}

	public static TailCall<List<Integer>> rangeRecursive_(int start, int end, List<Integer> acc) {
		return start <= end
				? ret(list())
				: sus(() -> rangeRecursive_(start + 1, end, append(acc, start)));
	}

	public static <T> List<T> unfold(final T seed, final ƒ<T, T> f, final ƒ<T, Boolean> predicate) {
		List<T> result = list();

		T temp = seed;
		while (predicate.apply(temp)) {
			result = append(result, temp);
			temp = f.apply(temp);
		}

		return result;
	}

	public static <T, U> U foldLeftRecursive(final List<T> ts, final U identity, final ƒ<U,  ƒ<T, U>> f) {
		return foldLeftRecursive_(ts, identity, f).eval();
	}

	public static <T, U> TailCall<U> foldLeftRecursive_(final List<T> ts, final U identity, final ƒ<U,  ƒ<T, U>> f) {
		return ts.isEmpty()
				? ret(identity)
				: sus(() -> foldLeftRecursive_(tail(ts), f.apply(identity).apply(head(ts)), f));
	}

	public static <T, U> U foldRightRecursive(final List<T> ts, final U identity, final ƒ<T,  ƒ<U, U>> f) {
		return foldRightRecursive_(reverse(ts), f, identity).eval();
	}

	public static <T, U> TailCall<U> foldRightRecursive_(final List<T> ts, final ƒ<T,  ƒ<U, U>> f, U acc) {
		return ts.isEmpty()
				? ret(acc)
				: sus(() -> foldRightRecursive_(tail(ts), f, f.apply(head(ts)).apply(acc)));
	}

	public static <T> ƒ<T, T> composeAllViaFoldLeft(List<ƒ<T, T>> list) {
		return x -> foldLeft(reverse(list), x, a -> b -> b.apply(a));
	}

	public static <T> ƒ<T, T> composeAllViaFoldRight(List<ƒ<T, T>> list) {
		return x -> foldRight(list, x, a -> a::apply);
	}

	public static <T> ƒ<T, T> andThenAllViaFoldLeft(List<ƒ<T, T>> list) {
		return x -> foldLeft(list, x, a -> b -> b.apply(a));
	}

	public static <T> ƒ<T, T> andThenAllViaFoldRight(List<ƒ<T, T>> list) {
		return x -> foldRight(reverse(list), x, a -> a::apply);
	}

	private static <T> List<T> copy(final List<T> list) {
		return new ArrayList<>(list);
	}
}
