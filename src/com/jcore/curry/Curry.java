package com.jcore.curry;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import com.jcore.curry.model.Meat;
import com.jcore.curry.model.Onion;
import com.jcore.curry.model.Spices;
import com.jcore.lib.ƒ;

public class Curry {
	public static void main(String[] args) {
		System.out.println("----------------");

		// Add function, uncurried and curried
		System.out.println(add(1, 2));
		System.out.println(add(1).apply(2));

		System.out.println("----------------");

		// Original curry function
		createCurry(new Meat(Duration.ofMinutes(5)), new Onion(Duration.ofMinutes(5)), new Spices(Duration.ofSeconds(30)), Duration.ofMinutes(5), Duration.ofMinutes(8));

		System.out.println("----------------");

		createCurry()
				.apply(new Meat(Duration.ofMinutes(5)))
				.apply(new Onion(Duration.ofMinutes(5)))
				.apply(new Spices(Duration.ofSeconds(30)))
				.apply(Duration.ofMinutes(5))
				.apply(Duration.ofMinutes(8));

		System.out.println("----------------");

		createCurry2()
				.apply(new Meat(Duration.ofMinutes(5)))
				.apply(new Onion(Duration.ofMinutes(5)))
				.apply(new Spices(Duration.ofSeconds(30)))
				.apply(Duration.ofMinutes(5))
				.accept(Duration.ofMinutes(8));

		System.out.println("----------------");

		final ƒ<Duration, Void> unfinishedCurry = createCurry()
				.apply(new Meat(Duration.ofMinutes(5)))
				.apply(new Onion(Duration.ofMinutes(5)))
				.apply(new Spices(Duration.ofSeconds(30)))
				.apply(Duration.ofMinutes(5));

		IntStream.rangeClosed(5, 10).forEach(i -> {
			unfinishedCurry.apply(Duration.ofMinutes(i));
			System.out.println("----------------");
		});
	}

	private static int add(int a, int b) {
		return a + b;
	}

	private static ƒ<Integer, Integer> add(int a) {
		return b -> a + b;
	}

	public static void createCurry(Meat m, Onion o, Spices s, Duration simmerDuration, Duration cookingDuration) {
		System.out.println("• " + m);
		System.out.println("• " + o);
		System.out.println("• " + s);
		System.out.println("• Simmer away (" + simmerDuration.toMinutes() + " minutes)");
		System.out.println("• Cook the meat (" + cookingDuration.toMinutes() + " minutes)");
		System.out.println("• Enjoy!");
	}

	public static ƒ<Meat, ƒ<Onion, ƒ<Spices, ƒ<Duration, ƒ<Duration, Void>>>>> createCurry() {
		return m -> o -> s -> simmerDuration -> cookingDuration -> {
			System.out.println("• " + m);
			System.out.println("• " + o);
			System.out.println("• " + s);
			System.out.println("• Simmer away (" + simmerDuration.toMinutes() + " minutes)");
			System.out.println("• Cook the meat (" + cookingDuration.toMinutes() + " minutes)");
			System.out.println("• Enjoy!");
			return null;
		};
	}

	public static ƒ<Meat, ƒ<Onion, ƒ<Spices, ƒ<Duration, ƒ<Duration, Void>>>>> createCurryVerbose() {
		return m -> {
			return o -> {
				return s -> {
					return simmerDuration -> {
						return cookingDuration -> {
							System.out.println("• " + m);
							System.out.println("• " + o);
							System.out.println("• " + s);
							System.out.println("• Simmer away (" + simmerDuration.toMinutes() + " minutes)");
							System.out.println("• Cook the meat (" + cookingDuration.toMinutes() + " minutes)");
							System.out.println("• Enjoy!");
							return null;
						};
					};
				};
			};
		};
	}

	public static ƒ<Meat, ƒ<Onion, ƒ<Spices, ƒ<Duration, Consumer<Duration>>>>> createCurry2() {
		return m -> o -> s -> simmerDuration -> cookingDuration -> {
			System.out.println("• " + m);
			System.out.println("• " + o);
			System.out.println("• " + s);
			System.out.println("• Simmer away (" + simmerDuration.toMinutes() + " minutes)");
			System.out.println("• Cook the meat (" + cookingDuration.toMinutes() + " minutes)");
			System.out.println("• Enjoy!");
		};
	}
}
