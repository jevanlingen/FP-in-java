package com.jcore.chapter.three;

import java.util.function.Consumer;

public interface Result<T> {
	void ifSuccessOrElse(Consumer<T> success, Consumer<String> failure);

	class Success<T> implements Result<T> {
		private final T value;

		public Success(final T t) {
			this.value = t;
		}

		@Override
		public void ifSuccessOrElse(Consumer<T> success, Consumer<String> failure) {
			success.accept(value);
		}
	}

	class Failure implements Result<String> {
		private final String errorMessage;

		public Failure(final String s) {
			this.errorMessage = s;
		}

		public String getMessage() {
			return errorMessage;
		}
		@Override
		public void ifSuccessOrElse(Consumer<String> success, Consumer<java.lang.String> failure) {
			failure.accept(errorMessage);
		}
	}
}
