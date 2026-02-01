package com.example.cinema.pricing;

public class TieredPricingStrategy implements PricingStrategy {
    @Override
    public double priceForIndex(int index) {
        return 10.0 + (index % 3) * 2.5;
    }
}
