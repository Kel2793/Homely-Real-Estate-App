package com.kenzie.appserver.controller.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListingResponse {
    @JsonProperty("listingNumber")
    private String listingNumber;

    @JsonProperty("address")
    private String address;

    @JsonProperty("squareFootage")
    private int squareFootage;

    @JsonProperty("price")
    private int price;

    @JsonProperty("numBedrooms")
    private int numBedrooms;

    @JsonProperty("numBathrooms")
    private double numBathrooms;

    @JsonProperty("listingStatus")
    private String listingStatus;

    @JsonProperty("lotSize")
    private double lotSize;

    public String getListingNumber() {
        return listingNumber;
    }

    public void setListingNumber(String listingNumber) {
        this.listingNumber = listingNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSquareFootage() {
        return squareFootage;
    }

    public void setSquareFootage(int squareFootage) {
        this.squareFootage = squareFootage;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNumBedrooms() {
        return numBedrooms;
    }

    public void setNumBedrooms(int numBedrooms) {
        this.numBedrooms = numBedrooms;
    }

    public double getNumBathrooms() {
        return numBathrooms;
    }

    public void setNumBathrooms(double numBathrooms) {
        this.numBathrooms = numBathrooms;
    }

    public String getListingStatus() {
        return listingStatus;
    }

    public void setListingStatus(String listingStatus) {
        this.listingStatus = listingStatus;
    }

    public double getLotSize() {
        return lotSize;
    }

    public void setLotSize(double lotSize) {
        this.lotSize = lotSize;
    }
}
