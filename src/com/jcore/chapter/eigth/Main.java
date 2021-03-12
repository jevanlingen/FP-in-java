package com.jcore.chapter.eigth;

import static com.jcore.lib.model.List.filterFailure;
import static com.jcore.lib.model.List.filterSuccess;
import static com.jcore.lib.model.List.list;
import static com.jcore.lib.model.List.sequence;
import static com.jcore.lib.model.List.sequenceAlternative;
import static com.jcore.lib.model.List.zipWith;

import com.jcore.lib.model.List;
import com.jcore.lib.Œ;

public class Main {
	public static void main(String[] args) {
		final var letters = list("A", "B", "C");
		System.out.println(letters.lastOption());

		final var empty = list();
		System.out.println(empty.lastOption());

		final List<Œ<Integer>> optionList = list(Œ.empty(), Œ.success(22), Œ.failure("Fail"), Œ.success(34));
		System.out.println(filterSuccess(optionList));
		System.out.println(filterFailure(optionList));

		final List<Œ<Integer>> optionListWithoutEmpty = list(Œ.success(22), Œ.failure("Fail"), Œ.success(34));
		final List<Œ<String>> optionListSuccess = list(Œ.success("BOE"), Œ.success("JAH"));
		final List<Œ<String>> optionListFailure = list(Œ.failure("Fail"), Œ.failure("Twice"));
		System.out.println("------------------------");
		System.out.println(sequence(list()));
		System.out.println(sequence(optionList));
		System.out.println(sequence(optionListWithoutEmpty));
		System.out.println(sequence(optionListSuccess));
		System.out.println(sequence(optionListFailure));

		System.out.println("------------------------");
		System.out.println(sequenceAlternative(list()));
		System.out.println(sequenceAlternative(optionList));
		System.out.println(sequenceAlternative(optionListWithoutEmpty));
		System.out.println(sequenceAlternative(optionListSuccess));
		System.out.println(sequenceAlternative(optionListFailure));

		System.out.println("------------------------");

		System.out.println(zipWith(list("A", "B", "C"), list("X", "y", "Z"), a -> b -> a + b));
	}
}
