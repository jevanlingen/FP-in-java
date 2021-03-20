package com.jcore.presentation.calculator;

import com.jcore.lib.Œ;
import com.jcore.lib.ƒ;

public enum Operation {
	PLUS    ('+', l -> r -> l + r),
	MINUS   ('-', l -> r -> l - r),
	MULTIPLY('*', l -> r -> l * r),
	DIVIDE  ('/', l -> r -> l / r);

	private final ƒ<Double, ƒ<Double, Double>> binaryOperator;

	Operation(final Character symbol, final ƒ<Double, ƒ<Double, Double>> binaryOperator) {
		this.binaryOperator = binaryOperator;
	}

	public static Œ<Operation> getBySymbol(final Character symbol) {
		return switch (symbol) {
			case '+' -> Œ.success(PLUS);
			case '-' -> Œ.success(MINUS);
			case '*' -> Œ.success(MULTIPLY);
			case '/' -> Œ.success(DIVIDE);
			default -> Œ.failure("Symbol '" + symbol + "' is not an operator");
		};
	}

	public double apply(final double left, final double right) {
		return binaryOperator.apply(left).apply(right);
	}
}