package com.jcore.chapter.three.model;

public class Product {
	private final String name;
	private final Price price;
	private final Weight weight;

	public Product(final String name, final Price price, final Weight weight) {
		this.name = name;
		this.price = price;
		this.weight = weight;
	}

	public Price getPrice() {
		return price;
	}

	public Weight getWeight() {
		return weight;
	}

	public String getName() {
		return name;
	}
}
