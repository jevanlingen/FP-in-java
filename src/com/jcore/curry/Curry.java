package com.jcore.curry;

import java.time.Duration;
import java.util.function.Consumer;

import com.jcore.curry.model.Meat;
import com.jcore.curry.model.Onion;
import com.jcore.curry.model.Spices;
import com.jcore.lib.ƒ;

public class Curry {
	public static void main(String[] args) {
		createCurry(new Meat(Duration.ofMinutes(5)), new Onion(Duration.ofMinutes(5)), new Spices(Duration.ofSeconds(30)), Duration.ofMinutes(5), Duration.ofMinutes(8));
		System.out.println("----------------");
		createCurry()
			.apply(new Meat(Duration.ofMinutes(5)))
			.apply(new Onion(Duration.ofMinutes(5)))
			.apply(new Spices(Duration.ofSeconds(30)))
			.apply(Duration.ofMinutes(5))
			.accept(Duration.ofMinutes(5));
	}

	public static void createCurry(Meat m, Onion o, Spices s, Duration simmerDuration, Duration cookingDuration) {
		System.out.println("• " + m);
		System.out.println("• " + o);
		System.out.println("• " + s);
		System.out.println("• Simmer away (" + simmerDuration.toMinutes() + " minutes)");
		System.out.println("• Cook the meat (" + cookingDuration.toMinutes() + " minutes)");
		System.out.println("• Enjoy!");
	}

	public static ƒ<Meat, ƒ<Onion, ƒ<Spices, ƒ<Duration, Consumer<Duration>>>>> createCurry() {
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
