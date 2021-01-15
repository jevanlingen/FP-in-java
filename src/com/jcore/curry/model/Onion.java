package com.jcore.curry.model;

import java.time.Duration;

public class Onion {
	public final Duration preparationTime;

	public Onion(Duration preparationTime) {
		this.preparationTime = preparationTime;
	}

	@Override
	public String toString() {
		return "Sweat your onions (" + preparationTime.toMinutes() + " minutes)";
	}
}
