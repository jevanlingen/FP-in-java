package com.jcore.curry.model;

import java.time.Duration;
import java.time.Period;

public class Meat {
	public final Duration preparationTime;

	public Meat(Duration preparationTime) {
		this.preparationTime = preparationTime;
	}

	@Override
	public String toString() {
		return "Brown the meat (" + preparationTime.toMinutes() + " minutes)";
	}
}
