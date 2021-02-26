package com.jcore.lib.model;

import static com.jcore.lib.model.TailCall.ret;
import static com.jcore.lib.model.TailCall.sus;

import com.jcore.lib.Œ;
import com.jcore.lib.ƒ;

public abstract class List<E> {
	@SuppressWarnings("rawtypes")
	public static final List NIL = new Nil();

	public abstract E head();

	public abstract Œ<E> headOption();

	public abstract List<E> setHead(E e);

	public abstract Œ<E> lastOption();

	public abstract List<E> tail();

	public abstract boolean isEmpty();

	// [1, NIL]

	/**
	 * Drop last element
	 *
	 * @return
	 */
	public abstract List<E> init();

	public abstract List<E> reverse();

	public abstract <B> List<B> map(ƒ<E, B> f);

	public abstract <B> List<B> flatMap(ƒ<E, List<B>> f);

	public abstract List<E> filter(ƒ<E, Boolean> f);

	public abstract <B> B foldLeft(B identity, ƒ<B, ƒ<E, B>> f);

	public abstract <B> B foldRight(B identity, ƒ<E, ƒ<B, B>> f);

	public abstract int lengthMemoized();

	private List() {
	}

	/**
	 * Add element at the beginning
	 *
	 * @param e
	 * @return
	 */
	public List<E> cons(E e) {
		return new Cons<>(e, this);
	}

	public List<E> drop(int n) {
		return drop_(this, n).eval();
	}

	private TailCall<List<E>> drop_(List<E> list, int n) {
		return list.isEmpty() || n <= 0
				? ret(list)
				: sus(() -> drop_(list.tail(), n - 1));
	}

	public List<E> dropWhile(ƒ<E, Boolean> f) {
		return dropWhile_(this, f).eval();
	}

	private TailCall<List<E>> dropWhile_(List<E> list, ƒ<E, Boolean> f) {
		return list.isEmpty() || !f.apply(list.head())
				? ret(list)
				: sus(() -> dropWhile_(list.tail(), f));
	}

	public int length() {
		return foldLeft(0, x -> y -> x + 1);
	}

	@Override
	public String toString() {
		return String.format("[%sNIL]", toString_(new StringBuilder(), this).eval());
	}

	private TailCall<StringBuilder> toString_(StringBuilder acc, List<E> list) {
		return list.isEmpty()
				? ret(acc)
				: sus(() -> toString_(acc.append(list.head()).append(", "), list.tail()));
	}

	private static class Nil<E> extends List<E> {
		private Nil() {
		}

		@Override
		public E head() {
			throw new IllegalArgumentException("Empty list does not contain a head");
		}

		@Override
		public Œ<E> headOption() {
			return Œ.empty();
		}

		@Override
		public List<E> setHead(E a) {
			throw new IllegalStateException("You cannot call setHead on an empty list");
		}

		@Override
		public Œ<E> lastOption() {
			return Œ.empty();
		}

		@Override
		public List<E> tail() {
			throw new IllegalArgumentException("Empty list does not contain a tail");
		}

		@Override
		public boolean isEmpty() {
			return true;
		}

		@Override
		public List<E> init() {
			throw new IllegalArgumentException("Empty list cannot execute init");
		}

		@Override
		public List<E> reverse() {
			return this;
		}

		@Override
		public <B> List<B> map(ƒ<E, B> f) {
			return list();
		}

		@Override
		public <B> List<B> flatMap(ƒ<E, List<B>> f) {
			return list();
		}

		@Override
		public List<E> filter(ƒ<E, Boolean> f) {
			return list();
		}

		@Override
		public <B> B foldLeft(B identity, ƒ<B, ƒ<E, B>> f) {
			return identity;
		}

		@Override
		public <B> B foldRight(B identity, ƒ<E, ƒ<B, B>> f) {
			return identity;
		}

		@Override
		public int lengthMemoized() {
			return 0;
		}
	}

	private static class Cons<E> extends List<E> {
		private final E head;
		private final List<E> tail;
		private final int length;

		private Cons(final E head, final List<E> tail) {
			this.head = head;
			this.tail = tail;
			this.length = tail.length() + 1;
		}

		@Override
		public E head() {
			return head;
		}

		@Override
		public Œ<E> headOption() {
			return Œ.success(head);
		}

		@Override
		public List<E> setHead(E a) {
			return new Cons<>(a, this.tail());
		}

		@Override
		public Œ<E> lastOption() {
			return foldLeft(Œ.empty(), t -> Œ::success);
		}

		@Override
		public List<E> tail() {
			return tail;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public List<E> init() {
			return this.reverse().tail().reverse();
		}

		@Override
		public List<E> reverse() {
			return reverse_(list(), this).eval();
		}

		private TailCall<List<E>> reverse_(List<E> acc, List<E> list) {
			return list.isEmpty()
					? ret(acc)
					: sus(() -> reverse_(new Cons<>(list.head(), acc), list.tail()));
		}

		@Override
		public <B> List<B> map(ƒ<E, B> f) {
			return foldRight(list(), h -> t -> t.cons(f.apply(h)));
		}

		@Override
		public <B> List<B> flatMap(ƒ<E, List<B>> f) {
			return foldRight(list(), h -> t -> concat(f.apply(h), t));
		}

		@Override
		public List<E> filter(ƒ<E, Boolean> f) {
			return foldRight(list(), h -> t -> f.apply(h) ? t.cons(h) : t);
		}

		@Override
		public <B> B foldLeft(B identity, ƒ<B, ƒ<E, B>> f) {
			return foldLeft_(this, identity, f).eval();
		}

		public <E, B> TailCall<B> foldLeft_(List<E> list, B acc, ƒ<B, ƒ<E, B>> f) {
			return list.isEmpty()
					? ret(acc)
					: sus(() -> foldLeft_(list.tail(), f.apply(acc).apply(list.head()), f));
		}

		@Override
		public <B> B foldRight(B identity, ƒ<E, ƒ<B, B>> f) {
			return foldRight_(this.reverse(), identity, f).eval();
		}

		public <E, B> TailCall<B> foldRight_(List<E> list, B acc, ƒ<E, ƒ<B, B>> f) {
			return list.isEmpty()
					? ret(acc)
					: sus(() -> foldRight_(list.tail(), f.apply(list.head()).apply(acc), f));
		}

		@Override
		public int lengthMemoized() {
			return length;
		}
	}

	@SuppressWarnings("unchecked")
	public static <A> List<A> list() {
		return NIL;
	}

	public static <E> List<E> list(E... elements) {
		List<E> n = list();
		for (int i = elements.length - 1; i >= 0; i--) {
			n = new Cons<>(elements[i], n);
		}

		return n;
	}

	public static <E> List<E> concat(List<E> list1, List<E> list2) {
		return list1.foldRight(list2, x -> y -> y.cons(x));
	}

	public static <E> List<E> flatten(List<List<E>> list) {
		return list.flatMap(x -> x);
		//return list.foldLeft(list(), x -> y -> concat(x, y));
	}

	public static <E> List<E> flattenResult(List<Œ<E>> list) {
		return list.foldRight(list(), x -> y -> x.map(y::cons).getOrElse(y));
	}
}
