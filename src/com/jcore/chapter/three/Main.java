package com.jcore.chapter.three;

import static com.jcore.chapter.three.CollectionUtilities.foldLeft;
import static com.jcore.chapter.three.CollectionUtilities.foldRight;
import static com.jcore.chapter.three.CollectionUtilities.foldRightRecursive;
import static com.jcore.chapter.three.CollectionUtilities.list;
import static com.jcore.chapter.three.CollectionUtilities.map;
import static com.jcore.chapter.three.CollectionUtilities.reverse;

public class Main {
	public static void main(String[] args) {
		System.out.println(foldLeft(list(1, 2, 3, 4, 5), 0, x -> y -> x + y));
		System.out.println(foldLeft(list(1, 2, 3, 4, 5), 1, x -> y -> x * y));
		System.out.println(foldLeft(list(1, 2, 3, 4, 5), "0", s -> i -> addSi(s, i)));
		System.out.println(foldRight(list(1, 2, 3, 4, 5), "0", s -> i -> addIs(s, i)));
		System.out.println(foldRightRecursive(list(1, 2, 3, 4, 5), "0", s -> i -> addIs(s, i)));
		System.out.println(reverse(list(1, 2, 3, 4, 5)));
		System.out.println(map(list(1, 2, 3, 4, 5), x -> x + 22));
	}

	private static String addSi(String s, Integer i) {
		return "(" + s + " + " + i + ")";
	}

	private static String addIs(Integer i, String s) {
		return "(" + i + " + " + s + ")";
	}
}
