package com.jcore.presentation.calculator;

import static com.jcore.lib.model.List.list;
import static com.jcore.lib.model.TailCall.ret;
import static com.jcore.lib.model.TailCall.sus;
import static com.jcore.presentation.calculator.Operation.getBySymbol;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.jcore.lib.model.List;
import com.jcore.lib.model.TailCall;
import com.jcore.lib.model.Tuple;
import com.jcore.lib.Ø;
import com.jcore.lib.ƒ;

public class Calculator {
	private static final Pattern IS_VALID = Pattern.compile("(-|\\+|\\*|/|\\d)*");

	public static void main(String[] args) {
		final var scanner = new Scanner(System.in);

		while (true) {
			System.out.print("> ");
			final var input = scanner.nextLine().trim().replaceAll(" +", "");

			if ("exit".equals(input)) {
				break;
			}

			if (!IS_VALID.matcher(input).matches()) {
				System.out.println("Cannot compute current input");
				continue;
			}

			final Tuple<List<String>, List<Operation>> partsAndOperators = foldRightWitPrev(fromString(input), new Tuple<>(list(), list()), x -> y -> z -> getBySymbol(x).exists()
					? new Tuple<>(z._1, z._2.cons(getBySymbol(x).getOrElse(() -> null)))
					: new Tuple<>(!y.exists() || y.exists(k -> getBySymbol(k).exists())
						? z._1.cons(String.valueOf(x))
						: z._1.setHead(x + z._1.head()),
					z._2));

			// Do actual calculation

			System.out.println(partsAndOperators);
		}
	}

	private static List<Character> fromString(final String s) {
		// TODO make immutable
		List<Character> list = list();
		for (char c : s.toCharArray()) {
			list = list.cons(c);
		}

		return list.reverse();
	}

	private static <E, B> B foldRightWitPrev(List<E> list, B identity, ƒ<E, ƒ<Ø<E>, ƒ<B, B>>> f) {
		return foldRightWithPrev_(list.reverse(), identity, Ø.none(), f).eval();
	}

	private static <E, B> TailCall<B> foldRightWithPrev_(List<E> list, B acc, Ø<E> prev, ƒ<E, ƒ<Ø<E>, ƒ<B, B>>> f) {
		return list.isEmpty()
				? ret(acc)
				: sus(() -> foldRightWithPrev_(list.tail(), f.apply(list.head()).apply(prev).apply(acc), Ø.some(list.head()), f));
	}
}
