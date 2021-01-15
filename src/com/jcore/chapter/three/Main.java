package com.jcore.chapter.three;

import static com.jcore.lib.CollectionUtilities.foldLeft;
import static com.jcore.lib.CollectionUtilities.foldRight;
import static com.jcore.lib.CollectionUtilities.foldRightRecursive;
import static com.jcore.lib.CollectionUtilities.forEach;
import static com.jcore.lib.CollectionUtilities.list;
import static com.jcore.lib.CollectionUtilities.map;
import static com.jcore.lib.CollectionUtilities.range;
import static com.jcore.lib.CollectionUtilities.rangeRecursive;
import static com.jcore.lib.CollectionUtilities.reverse;

import java.util.function.Consumer;

import com.jcore.lib.ƒ;

public class Main {
	public static void main(String[] args) {
		System.out.println(foldLeft(list(1, 2, 3, 4, 5), 0, x -> y -> x + y));
		System.out.println(foldLeft(list(1, 2, 3, 4, 5), 1, x -> y -> x * y));
		System.out.println(foldLeft(list(1, 2, 3, 4, 5), "0", s -> i -> addSi(s, i)));
		System.out.println(foldRight(list(1, 2, 3, 4, 5), "0", s -> i -> addIs(s, i)));
		System.out.println(foldRightRecursive(list(1, 2, 3, 4, 5), "0", s -> i -> addIs(s, i)));
		System.out.println(reverse(list(1, 2, 3, 4, 5)));
		System.out.println(map(list(1, 2, 3, 4, 5), x -> x + 22));
		forEach(list(1, 2, 3, 4, 5), System.out::println);

		// ---------------------- //
		ƒ<Double, Double> addTax = x -> x * 1.09;
		ƒ<Double, Double> addShipping = x -> x + 3.50;

		final Consumer<Double> printWithTwoDecimals = x -> {
			System.out.printf("%.2f", x);
			System.out.println();
		};
		final var pricesIncludingTaxAndShipping = map(list(1.0, 2.0, 3.3, 4.2), addTax.andThen(addShipping));

		final ƒ<Runnable, ƒ<Runnable, Runnable>> compose = x -> y -> () -> {
			x.run();
			y.run();
		};
		final Runnable zero = () -> {};

		final var program = foldLeft(pricesIncludingTaxAndShipping, zero, e -> d -> compose.apply(e).apply(() -> printWithTwoDecimals.accept(d)));
		program.run();

		// ---------------------- //
		System.out.println(range(0, 10));
		System.out.println(rangeRecursive(0, 10));
	}

	private static String addSi(String s, Integer i) {
		return "(" + s + " + " + i + ")";
	}

	private static String addIs(Integer i, String s) {
		return "(" + i + " + " + s + ")";
	}
}
