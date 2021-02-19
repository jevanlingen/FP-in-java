package com.jcore.chapter.seven;

import com.jcore.lib.Œ;
import com.jcore.lib.ƒ;

public class Main {
	public static void main(String[] args) {
		System.out.println(Œ.doTry(() -> 2 / 0).map(i -> i * 2));
		System.out.println(Œ.doTry(() -> 4 / 2).map(i -> i * 2));
		System.out.println(Œ.doTry(() -> 4 / 2).map(i -> i * 2).map(i -> i / 0));

		System.out.println(Œ.doTry(() -> 4 / 2).map(i -> i * 2).filter(i -> i == 4));
		System.out.println(Œ.doTry(() -> 4 / 2).map(i -> i * 2).filter(i -> i == 3));
		System.out.println(Œ.doTry(() -> 4 / 2).map(i -> i * 2).filter(i -> i == 3, "Result is not a three..."));

		System.out.println(Œ.doTry(() -> 4 / 2).map(i -> i * 2).exists(i -> i == 4));
		System.out.println(Œ.doTry(() -> 4 / 2).map(i -> i * 2).exists(i -> i == 3));

		Œ.doTry(() -> 4 / 2).forEach(i -> System.out.println(i));
		Œ.doTry(() -> 2 / 0).forEach(i -> System.out.println(i));

		final ƒ<Œ<String>, Œ<String>> upperOption = Œ.lift(String::toUpperCase);
		System.out.println(upperOption.apply(Œ.success("Some value")).getOrElse(() -> "default sizzle"));
		System.out.println(upperOption.apply(Œ.failure("FAIL!!")).getOrElse(() -> "default sizzle"));

		final ƒ<String, Œ<String>> upper = Œ.hlift(String::toUpperCase);
		System.out.println(upper.apply("Some value").getOrElse(() -> "default sizzle"));
		System.out.println(upper.apply(null).getOrElse(() -> "default sizzle"));
	}

	
}
