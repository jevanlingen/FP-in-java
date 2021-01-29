package com.jcore.lib.model;

import static com.jcore.lib.model.TailCall.ret;
import static com.jcore.lib.model.TailCall.sus;

import com.jcore.lib.StackBasedTailRec;
import com.jcore.lib.ƒ;

public abstract class List<E> {
	@SuppressWarnings("rawtypes")
	public static final List NIL = new Nil();

	public abstract E head();

	public abstract List<E> setHead(E e);

	public abstract List<E> tail();

	public abstract boolean isEmpty();

	/**
	 * Drop last element
	 * @return
	 */
	public abstract List<E> init();

	public abstract List<E> reverse();

	private List() {
	}

	/**
	 * Add element at the beginning
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
		return foldRight(this, 0, x -> y -> y + 1);
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
		public List<E> setHead(E a) {
			throw new IllegalStateException("You cannot call setHead on an empty list");
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
	}

	private static class Cons<E> extends List<E> {
		private final E head;
		private final List<E> tail;

		private Cons(final E head, final List<E> tail) {
			this.head = head;
			this.tail = tail;
		}

		@Override
		public E head() {
			return head;
		}

		@Override
		public List<E> setHead(E a) {
			return new Cons<>(a, this.tail());
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
		return foldRight(list1, list2, x -> y -> y.cons(x));
	}

	@StackBasedTailRec
	public static <E, B> B foldRight(List<E> list, B identity, ƒ<E, ƒ<B, B>> f) {
		return list.isEmpty()
				? identity
				: f.apply(list.head()).apply(foldRight(list.tail(), identity, f));
	}
}
