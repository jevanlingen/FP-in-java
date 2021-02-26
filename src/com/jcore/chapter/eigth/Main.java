package com.jcore.chapter.eigth;

import static com.jcore.lib.model.List.flattenResult;
import static com.jcore.lib.model.List.list;

import com.jcore.lib.model.List;
import com.jcore.lib.Œ;

public class Main {
	public static void main(String[] args) {
		final var letters = list("A", "B", "C");
		System.out.println(letters.lastOption());

		final var empty = list();
		System.out.println(empty.lastOption());

		final List<Œ<String>> optionList = list(Œ.empty(), Œ.success("BOE"), Œ.failure("Fail"), Œ.success("JAH"));
		System.out.println(flattenResult(optionList));
	}
}
