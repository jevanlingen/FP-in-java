package com.jcore.chapter.two;

import com.jcore.lib.model.Tuple;
import com.jcore.lib.ƒ;

public class Main {
	static final ƒ<Integer, Integer> factorial = n -> n <= 1 ? n : n * Main.factorial.apply(n - 1);

	public static void main(String[] args) {
		final ƒ<Integer, Integer> twice = e -> e * 2;
		final ƒ<Integer, Integer> triple = x -> x * 3;
		final ƒ<Integer, Integer> square = x -> x * x;
		final ƒ<Integer, ƒ<Integer, Integer>> add = x -> y -> x + y;

		System.out.println(twice.compose(square).apply(4));
		System.out.println(triple.compose(square).apply(3));
		System.out.println(twice.andThen(square).apply(4));
		System.out.println(twice.andThen(square).andThen(triple).apply(4));

		ƒ<ƒ<Integer, Integer>,
				ƒ<ƒ<Integer, Integer>, ƒ<Integer, Integer>>> integerCompose =
				x -> y -> z -> x.apply(y.apply(z));

		System.out.println(square.apply(2));
		System.out.println(composeOld(triple, square).apply(3));
		System.out.println(integerCompose.apply(triple).apply(square).apply(3));
		System.out.println(ƒ.<Integer, Integer, Integer>higherCompose().apply(triple).apply(square).apply(3));
		System.out.println(add.apply(2).apply(2));

		// TAX
		ƒ<Double, ƒ<Double, Double>> addTax = taxRate -> price -> price + price * taxRate;
		final ƒ<Double, Double> tax9 = addTax.apply(0.09);
		final ƒ<Double, Double> tax12 = addTax.apply(0.12);

		System.out.println(tax9.apply(12.0));
		System.out.println(tax12.apply(12.0));

		// inverse tax rate, where price is first argument (just for test,  makes no sense at all)
		ƒ<Double, ƒ<Double, Double>> addTaxTwo = price -> taxRate -> price + price * taxRate;

		System.out.println(addTaxTwo.apply(12.0).apply(0.09));
		System.out.println(reverseArgs(addTaxTwo).apply(0.09).apply(12.00));

		// PRINT COMBINATION OF A, B, C and D
		System.out.println(combine("A", "B", "C", "D"));
		System.out.println(combineCurried().apply("A").apply("B").apply("C").apply("D"));

		// Recursion
		System.out.println(factorial.apply(4));
	}

	static <T> ƒ<T, T> composeOld(final ƒ<T, T> f1, final ƒ<T, T> f2) {
		return arg -> f1.apply(f2.apply(arg));
	}

	static <A, B, C> ƒ<B, C> partialA(A a, ƒ<A, ƒ<B, C>> f) {
		return f.apply(a);
	}

	static <A, B, C> ƒ<A, C> partialB(B b, ƒ<A, ƒ<B, C>> f) {
		return a -> f.apply(a).apply(b);
	}

	static <A, B, C, D> String combine(A a, B b, C c, D d) {
		return String.format("%s, %s, %s, %s", a, b, c, d);
	}

	// ƒ(A -> B -> C -> D)
	// ƒ<A, ƒ<B, ƒ<C, ƒ<D, String>>>>
	static <A, B, C, D> ƒ<A, ƒ<B, ƒ<C, ƒ<D, String>>>> combineCurried() {
		return a -> b -> c -> d -> String.format("%s, %s, %s, %s", a, b, c, d);
	}

	static <A, B, C> ƒ<A, ƒ<B, C>> curry(ƒ<Tuple<A, B>, C> f) {
		return a -> b -> f.apply(new Tuple<>(a, b));
	}

	static <T, U, V> ƒ<U, ƒ<T, V>> reverseArgs(ƒ<T, ƒ<U, V>> f) {
		return u -> t -> f.apply(t).apply(u);
	}
}
