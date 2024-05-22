package com.gadarts.parashoot.model;

/**
 * Created by Gad on 09/08/2017.
 */

public class PurchaseItem {
    private final String price;
    private final String currencyCode;
    private final String id;
    private final String title;

    public PurchaseItem(String productId, String price, String currencyCode, String title) {
        this.id = productId;
        this.price = price;
        this.currencyCode = currencyCode;
        this.title = title;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
