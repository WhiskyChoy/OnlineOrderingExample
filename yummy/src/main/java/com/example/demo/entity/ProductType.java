package com.example.demo.entity;

public enum ProductType {
    SINGLE("single"), COMBO("combo");

    private String value;

    ProductType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static ProductType getProductType(String value) {
        for (ProductType productType : values()) {
            if (productType.toString().equals(value)) {
                return productType;
            }
        }
        return null;
    }
}
