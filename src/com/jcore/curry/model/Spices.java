package com.jcore.curry.model;

import java.time.Duration;

public class Spices {
	public final Duration preparationTime;

	public Spices(Duration preparationTime) {
		this.preparationTime = preparationTime;
	}

	@Override
	public String toString() {
		return "Bloom the spices (" + preparationTime.toSeconds() + " seconds)";
	}
}
