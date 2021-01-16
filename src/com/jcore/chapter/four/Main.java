package com.jcore.chapter.four;

import static com.jcore.chapter.four.TailCall.ret;
import static com.jcore.chapter.four.TailCall.sus;

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

	public static void main(String[] args) {
		System.out.println(add(3, 100000000));
		System.out.println(add.apply(3).apply(100000000));
	}

	static int add(final int x, final int y) {
		return addRec(x, y).eval();
	}

	static TailCall<Integer> addRec(final int x, final int y) {
		return y == 0
				? ret(x)
				: sus(() -> addRec(x + 1, y - 1));
	}
}
