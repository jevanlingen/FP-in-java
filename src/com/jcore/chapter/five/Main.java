package com.jcore.chapter.five;

import static com.jcore.lib.model.List.foldRight;
import static com.jcore.lib.model.List.list;
import static com.jcore.lib.model.List.concat;

import com.jcore.lib.StackBasedTailRec;
import com.jcore.lib.model.List;

public class Main {
	public static void main(String[] args) {
		System.out.println(list(1, 2, 3, 4, 5));
		System.out.println(list().drop(-2));
		System.out.println(list().drop(2));
		System.out.println(list(1, 2, 3, 4, 5).drop(2));
		System.out.println(list(1, 2, 3, 4, 5).drop(1));
		System.out.println(list(1, 2, 3, 4, 5).dropWhile(i -> i <= 3));
		System.out.println(list(1, 2, 3, 4, 5).reverse());
		System.out.println(list(1, 2, 3, 4, 5).init());
		System.out.println(sum(list(1, 2, 3, 4, 5)));
		System.out.println(concat(list(1, 2, 3, 4, 5), list(6, 7)));
		System.out.println(list(100, 200).length());
	}

	@StackBasedTailRec
	public static Integer sum(List<Integer> ints) {
		return foldRight(ints, 0, x -> y -> x + y);
	}

	@StackBasedTailRec
	public static Integer sumOld(List<Integer> ints) {
		return ints.isEmpty()
				? 0
				: ints.head() + sum(ints.tail());
	}

	@StackBasedTailRec
	public static Double product(List<Double> doubles) {
		return foldRight(doubles, 1.0, x -> y -> x * y);
	}

	@StackBasedTailRec
	public static Double productOld(List<Double> doubles) {
		return doubles.isEmpty()
				? 1.0
				: doubles.head() + product(doubles.tail());
	}


}
