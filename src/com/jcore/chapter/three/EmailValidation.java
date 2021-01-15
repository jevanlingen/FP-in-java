package com.jcore.chapter.three;

import static com.jcore.chapter.three.Result.*;
import static com.jcore.lib.Case.match;
import static com.jcore.lib.Case.mcase;

import java.util.function.Consumer;
import java.util.regex.Pattern;

import com.jcore.lib.ƒ;

public class EmailValidation {
	static Pattern emailPattern = Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$");

	static Consumer<String> success = s -> System.out.println("Mail sent to " + s);
	static Consumer<String> failure = s -> System.err.println("Error message logged: " + s);

	static ƒ<String, Result<String>> emailChecker = s -> match(
			mcase(() -> new Success<>(s)),
			mcase(() -> s == null, () -> new Failure("email must not be null")),
			mcase(() -> s.length() == 0, () -> new Failure("email must not be empty")),
			mcase(() -> !emailPattern.matcher(s).matches(), () -> new Failure("email " + s + "is not valid"))
	);

	public static void main(String[] args) {
		emailChecker.apply("this.is@my.email").ifSuccessOrElse(success, failure);
		emailChecker.apply(null).ifSuccessOrElse(success, failure);
		emailChecker.apply("").ifSuccessOrElse(success, failure);
		emailChecker.apply("john.doe@acme.com").ifSuccessOrElse(success, failure);
	}
}
