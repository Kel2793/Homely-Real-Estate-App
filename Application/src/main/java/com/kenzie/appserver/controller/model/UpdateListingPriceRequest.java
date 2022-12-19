package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class UpdateListingPriceRequest {
    @NotEmpty
    @JsonProperty("listingNumber")
    private String listingNumber;

    @Min(0)
    @JsonProperty("price")
    private int price;



    public String getListingNumber() {
        return listingNumber;
    }

    public void setListingNumber(String listingNumber) {
        this.listingNumber = listingNumber;
    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
