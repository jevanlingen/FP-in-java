package com.jcore;

public class Main {

	public static void main(String[] args) {
		final Function<Integer, Integer> triple = x -> x * 3;
		final Function<Integer, Integer> square = x -> x * x;
		final Function<Integer, Function<Integer, Integer>> addOld = x -> y -> x + y;
		final BinaryOperator add = x -> y -> x + y;

		Function<Function<Integer, Integer>,
				Function<Function<Integer, Integer>, Function<Integer, Integer>>> integerCompose =
				x -> y -> z -> x.apply(y.apply(z));

		Function<Double, Function<Double, Double>> addTax = taxRate -> price -> price + price * taxRate;
		final Function<Double, Double> tax9 = addTax.apply(0.09);
		final Function<Double, Double> tax12 = addTax.apply(0.12);

		System.out.println(square.apply(2));
		System.out.println(composeOld(triple, square).apply(3));
		System.out.println(integerCompose.apply(triple).apply(square).apply(3));
		System.out.println(Function.<Integer, Integer, Integer>higherCompose().apply(triple).apply(square).apply(3));
		System.out.println(add.apply(2).apply(2));
		System.out.println(tax9.apply(12.0));
		System.out.println(tax12.apply(12.0));
	}

	static <T> Function<T, T> composeOld(final Function<T, T> f1, final Function<T, T> f2) {
		return arg -> f1.apply(f2.apply(arg));
	}
}
