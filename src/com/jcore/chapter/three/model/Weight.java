package com.jcore.chapter.three.model;

import com.jcore.lib.ƒ;

public class Weight {
	public static final Weight ZERO = new Weight(0.0);
	public static ƒ<Weight, ƒ<OrderLine, Weight>> sum = x -> y -> x.add(y.getWeight());

	public final double value;

	private Weight(double value) {
		this.value = value;
	}

	public static Weight of(double value) {
		return new Weight(value);
	}

	public Weight add(Weight that) {
		return new Weight(this.value + that.value);
	}

	public Weight mult(int count) {
		return new Weight(this.value * count);
	}

	@Override
	public String toString() {
		return value + "";
	}
}
