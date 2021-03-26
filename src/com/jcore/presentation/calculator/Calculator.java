package com.jcore.presentation.calculator;

import static com.jcore.lib.model.List.list;
import static com.jcore.lib.model.TailCall.ret;
import static com.jcore.lib.model.TailCall.sus;
import static com.jcore.presentation.calculator.Operation.DIVIDE;
import static com.jcore.presentation.calculator.Operation.MINUS;
import static com.jcore.presentation.calculator.Operation.MULTIPLY;
import static com.jcore.presentation.calculator.Operation.PLUS;
import static com.jcore.presentation.calculator.Operation.getBySymbol;

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
				System.out.println("Parse error");
				continue;
			}

			final var partsAndOperators = processInput(input);
			final var r = calculate(partsAndOperators, o -> o == MULTIPLY || o == DIVIDE);
			final var k = calculate(r, o -> o == PLUS || o == MINUS);

			System.out.println(k._1.head());
		}
	}

	private static Tuple<List<Double>, List<Operation>> processInput(final String input) {
		final Tuple<List<String>, List<Operation>> a = foldRightWitPrev(fromString(input, list()), new Tuple<>(list(), list()), x -> y -> z -> getBySymbol(x).exists()
				? new Tuple<>(z._1, z._2.cons(getBySymbol(x).getOrElse(() -> null)))
				: new Tuple<>(!y.exists() || y.exists(k -> getBySymbol(k).exists())
				? z._1.cons(String.valueOf(x))
				: z._1.setHead(x + z._1.head()),
				z._2));

		return new Tuple<>(a._1.map(Double::valueOf), a._2);
	}

	private static List<Character> fromString(String s, List<Character> acc) {
		return s.isEmpty()
				? acc
				: fromString(s.substring(1), acc.cons(s.charAt(0)));
	}

	private static Tuple<List<Double>, List<Operation>> calculate(Tuple<List<Double>, List<Operation>> toCalculate, ƒ<Operation, Boolean> f) {
		return _calculateMultiplyDivide(toCalculate._1, toCalculate._2, new Tuple<>(list(), list()), f).eval();
	}

	private static TailCall<Tuple<List<Double>, List<Operation>>> _calculateMultiplyDivide(List<Double> numbers, List<Operation> operators, Tuple<List<Double>, List<Operation>> acc, ƒ<Operation, Boolean> f) {
		return operators.isEmpty()
				? ret(new Tuple<>(acc._1.cons(numbers.head()), acc._2))
				: sus(() -> f.apply(operators.head())
					? _calculateMultiplyDivide(numbers.tail().tail().cons(operators.head().apply(numbers.head(), numbers.tail().head())), operators.tail(), acc, f)
					: _calculateMultiplyDivide(numbers.tail(), operators.tail(), new Tuple<>(acc._1.cons(numbers.head()), acc._2.cons(operators.head())), f));
	}

	private static <E, B> B foldRightWitPrev(List<E> list, B identity, ƒ<E, ƒ<Ø<E>, ƒ<B, B>>> f) {
		return _foldRightWithPrev(list.reverse(), identity, Ø.none(), f).eval();
	}

	private static <E, B> TailCall<B> _foldRightWithPrev(List<E> list, B acc, Ø<E> prev, ƒ<E, ƒ<Ø<E>, ƒ<B, B>>> f) {
		return list.isEmpty()
				? ret(acc)
				: sus(() -> _foldRightWithPrev(list.tail(), f.apply(list.head()).apply(prev).apply(acc), Ø.some(list.head()), f));
	}
}
