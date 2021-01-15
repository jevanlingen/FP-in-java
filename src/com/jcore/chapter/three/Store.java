package com.jcore.chapter.three;

import static com.jcore.lib.CollectionUtilities.foldLeft;
import static com.jcore.lib.CollectionUtilities.list;

import com.jcore.chapter.three.model.OrderLine;
import com.jcore.chapter.three.model.Price;
import com.jcore.chapter.three.model.Product;
import com.jcore.chapter.three.model.Weight;

public class Store {
	public static void main(String[] args) {
		final var toothPaste = new Product("Tooth paste", Price.of(1.5), Weight.of(0.5));
		final var toothBrush = new Product("Tooth brush", Price.of(3.5), Weight.of(0.3));

		final var order = list(new OrderLine(toothPaste, 2), new OrderLine(toothBrush, 3));

		final var price = foldLeft(order, Price.ZERO, Price.sum);
		final var weight = foldLeft(order, Weight.ZERO, Weight.sum);

		System.out.println("Total weight: " + weight);
		System.out.println("Total price: " + price);
	}
}
