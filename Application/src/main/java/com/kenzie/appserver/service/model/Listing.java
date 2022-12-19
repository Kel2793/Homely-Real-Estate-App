package com.kenzie.appserver.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Listing {
    private final String listingNumber;
    private final String address;
    private final int squareFootage;
    private final int price;
    private final int numBedrooms;
    private final double numBathrooms;
    private final int numRooms;
    private final String listingStatus;
    private final String schoolDistrict;
    private final int lotSize;
    private final String buildingType;

    public Listing(String listingNumber, String address, int squareFootage, int price, int numBedrooms, double numBathrooms, int numRooms,
                   String buildingType, String schoolDistrict, int lotSize, String listingStatus) {
        this.listingNumber = listingNumber;
        this.address = address;
        this.squareFootage = squareFootage;
        this.price = price;
        this.numBedrooms = numBedrooms;
        this.numBathrooms = numBathrooms;
        this.numRooms = numRooms;
        this.schoolDistrict =schoolDistrict;
        this.lotSize = lotSize;
        this.buildingType = buildingType;
        this.listingStatus = listingStatus;
    }

    public String getListingNumber() {
        return listingNumber;
    }

    public String getAddress() {
        return address;
    }

    public int getSquareFootage() {
        return squareFootage;
    }

    public int getPrice() {
        return price;
    }

    public int getNumBedrooms() {
        return numBedrooms;
    }

    public double getNumBathrooms() {
        return numBathrooms;
    }

    public int getNumRooms() {
        return numRooms;
    }

    public String getListingStatus() {
        return listingStatus;
    }

    public String getSchoolDistrict() {
        return schoolDistrict;
    }

    public int getLotSize() {
        return lotSize;
    }

    public String getBuildingType() {
        return buildingType;
    }
}
