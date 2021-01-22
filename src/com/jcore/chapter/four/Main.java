package com.jcore.chapter.four;

import static com.jcore.lib.model.TailCall.ret;
import static com.jcore.lib.model.TailCall.sus;
import static com.jcore.lib.CollectionUtilities.andThenAllViaFoldLeft;
import static com.jcore.lib.CollectionUtilities.andThenAllViaFoldRight;
import static com.jcore.lib.CollectionUtilities.append;
import static com.jcore.lib.CollectionUtilities.composeAllViaFoldLeft;
import static com.jcore.lib.CollectionUtilities.composeAllViaFoldRight;
import static com.jcore.lib.CollectionUtilities.foldLeft;
import static com.jcore.lib.CollectionUtilities.head;
import static com.jcore.lib.CollectionUtilities.list;
import static com.jcore.lib.CollectionUtilities.map;
import static com.jcore.lib.CollectionUtilities.range;
import static com.jcore.lib.CollectionUtilities.tail;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.jcore.lib.model.Memoizer;
import com.jcore.lib.model.TailCall;
import com.jcore.lib.ƒ;

public class Main {
	static ƒ<Integer, ƒ<Integer, Integer>> add = x -> y -> {
		class AddHelper {
			ƒ<Integer, ƒ<Integer, TailCall<Integer>>> addHelper =
					a -> b -> b == 0
							? ret(a)
							: sus(() -> this.addHelper.apply(a + 1).apply(b - 1));
		}

		return new AddHelper().addHelper.apply(x).apply(y).eval();
	};

	final static ConcurrentHashMap<Integer, Integer> cache = new ConcurrentHashMap<>();

	public static void main(String[] args) {
		System.out.println(add(3, 100000));
		System.out.println(add.apply(3).apply(100000));
		System.out.println(fibonacci(9));

		final ƒ<Integer, Integer> add = x -> x + 1;
		final var mapOfFunctions = map(range(0, 500), x -> add);
		System.out.println(composeAllViaFoldLeft(mapOfFunctions).apply(0));

		final ƒ<String, String> f1 = x -> "{a" + x + "}";
		final ƒ<String, String> f2 = x -> "{b" + x + "}";
		final ƒ<String, String> f3 = x -> "{c" + x + "}";

		System.out.println(composeAllViaFoldLeft(list(f1, f2, f3)).apply("x"));
		System.out.println(composeAllViaFoldRight(list(f1, f2, f3)).apply("x"));
		System.out.println(andThenAllViaFoldLeft(list(f1, f2, f3)).apply("x"));
		System.out.println(andThenAllViaFoldRight(list(f1, f2, f3)).apply("x"));

		System.out.println(printFibonacci(0));
		System.out.println(printFibonacci(1));
		System.out.println(printFibonacci(8));

		System.out.println(doubleValue(2));
		System.out.println(doubleValue(2)); // retrieves from cache (memoization example)

		final ƒ<Integer, Integer> f = x -> x * 2;
		final ƒ<Integer, Integer> g = Memoizer.memoize(f);

		System.out.println(g.apply(2));
		System.out.println(g.apply(2));
	}

	static Integer doubleValue(Integer x) {
		return cache.computeIfAbsent(x, y -> y * 2);
	}

	static int add(final int x, final int y) {
		return add_(x, y).eval();
	}

	static BigInteger fibonacci(final int number) {
		return fib_(BigInteger.valueOf(number), ZERO, ONE).eval();
	}

	static String printFibonacci(final int number) {
		final var list = fib_(BigInteger.valueOf(number), list(ZERO), ZERO, ONE).eval();
		return list.isEmpty()
			? ""
			: tail(list).isEmpty()
				? head(list).toString()
				: head(list) + foldLeft(tail(list), "", x -> y -> x + ", " + y);
	}

	private static TailCall<Integer> add_(final int x, final int y) {
		return y == 0
				? ret(x)
				: sus(() -> add_(x + 1, y - 1));
	}

	private static TailCall<BigInteger> fib_(final BigInteger x, final BigInteger acc1, final BigInteger acc2) {
		if (x.equals(ZERO)) {
			return ret(x);
		} else if (x.equals(ONE)) {
			return ret(acc1.add(acc2));
		}

		return sus(() -> fib_(x.subtract(ONE), acc2, acc1.add(acc2)));
	}

	private static TailCall<List<BigInteger>> fib_(final BigInteger x, final List<BigInteger> acc, final BigInteger acc1, final BigInteger acc2) {
		if (x.equals(ZERO)) {
			return ret(acc);
		} else if (x.equals(ONE)) {
			return ret(append(acc, acc1.add(acc2)));
		}

		return sus(() -> fib_(x.subtract(ONE), append(acc, acc1.add(acc2)), acc2, acc1.add(acc2)));
	}
}