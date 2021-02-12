package com.jcore.chapter.six;

import static com.jcore.lib.model.List.list;
import static com.jcore.lib.Ø.hlift;
import static com.jcore.lib.Ø.lift;
import static com.jcore.lib.Ø.sequence;

import com.jcore.lib.model.List;
import com.jcore.lib.Ø;
import com.jcore.lib.ƒ;

public class Main {
	public static void main(String[] args) {
		System.out.println(Ø.some("X").or(() -> Ø.some("Y")));
		System.out.println(Ø.none().or(() -> Ø.some("Y")));

		final ƒ<Ø<String>, Ø<String>> upperOption = lift(String::toUpperCase);
		System.out.println(upperOption.apply(Ø.some("Some value")).getOrElse(() -> "default sizzle"));
		System.out.println(upperOption.apply(Ø.none()).getOrElse(() -> "default sizzle"));

		final ƒ<String, Ø<String>> upper = hlift(String::toUpperCase);
		System.out.println(upper.apply("Some value").getOrElse(() -> "default sizzle"));
		System.out.println(upper.apply(null).getOrElse(() -> "default sizzle"));

		System.out.println(sequence(list(Ø.some("X"), Ø.some("Y"))));
		System.out.println(sequence(list(Ø.some("X"), Ø.none())));

		System.out.println(sequenceWithMap2(list(Ø.some("X"), Ø.some("Y"))));
		System.out.println(sequenceWithMap2(list(Ø.some("X"), Ø.none())));


		//Example
		final ƒ<Integer, ƒ<String, Integer>> parseWithRadix = radix -> string -> Integer.parseInt(string, radix);
		final ƒ<String, Ø<Integer>> parse16 = hlift(parseWithRadix.apply(16));
		final Ø<List<Integer>> result = sequence(list("4", "5", "6", "7", "18", "19").map(parse16));
		System.out.println(result);
	}

	public static <A, B, C> Ø<C> map2(Ø<A> a, Ø<B> b, ƒ<A, ƒ<B, C>> f) {
		return a.flatMap(x -> b.map(y -> f.apply(x).apply(y)));
	}

	public static <A> Ø<List<A>> sequenceWithMap2(List<Ø<A>> list) {
		return list.foldRight(Ø.some(list()), x -> y -> map2(x, y, a -> b -> b.cons(a)));
	}
}