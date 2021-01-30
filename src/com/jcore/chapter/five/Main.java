package com.jcore.chapter.five;

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
		System.out.println(reverseViaFoldLeft(list(100, 200)));
		System.out.println(List.flatten(list(list(100), list(200), list(300))));
		System.out.println(triple(list(1, 2, 3, 4, 5)));
		System.out.println(allToString(list(1.1, 2.2, 3.3, 4.4, 5.5)));
		System.out.println(list(1, 2, 3, 4, 5).map(x -> x * 3));
		System.out.println(list(1, 2, 3, 4, 5).filter(x -> x == 3 || x == 5));
		System.out.println(list(1, 2, 3, 4, 5).flatMap(x -> list(x, -x)));
	}

	public static Integer sum(List<Integer> ints) {
		return ints.foldLeft(0, x -> y -> x + y);
		//return foldRight(ints, 0, x -> y -> x + y);
	}

	@StackBasedTailRec
	public static Integer sumOld(List<Integer> ints) {
		return ints.isEmpty()
				? 0
				: ints.head() + sum(ints.tail());
	}

	public static Double product(List<Double> doubles) {
		return doubles.foldLeft(1.0, x -> y -> x * y);
		//return foldRight(doubles, 1.0, x -> y -> x * y);
	}

	@StackBasedTailRec
	public static Double productOld(List<Double> doubles) {
		return doubles.isEmpty()
				? 1.0
				: doubles.head() + product(doubles.tail());
	}

	public static <A> List<A> reverseViaFoldLeft(List<A> list) {
		return list.foldLeft(list(), x -> x::cons);
	}

	public static List<Integer> triple(List<Integer> list) {
		return list.foldRight(list(), x -> y -> y.cons(x * 3));
	}

	public static List<String> allToString(List<Double> list) {
		return list.foldRight(list(), x -> y -> y.cons(x.toString()));
	}

}
