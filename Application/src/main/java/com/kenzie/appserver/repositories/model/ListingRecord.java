package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "Listing")
public class ListingRecord {
    private String listingNumber;
    private String address;
    private int squareFootage;
    private int price;
    private int numBedrooms;
    private double numBathrooms;
    private String listingStatus;
    private double lotSize;

    @DynamoDBHashKey(attributeName = "listingNumber")
    public String getListingNumber() {
        return listingNumber;
    }

    @DynamoDBAttribute(attributeName = "address")
    public String getAddress() {
        return address;
    }

    @DynamoDBAttribute(attributeName = "squareFootage")
    public int getSquareFootage() {
        return squareFootage;
    }

    @DynamoDBAttribute(attributeName = "price")
    public int getPrice() {
        return price;
    }

    @DynamoDBAttribute(attributeName = "numBedrooms")
    public int getNumBedrooms() {
        return numBedrooms;
    }

    @DynamoDBAttribute(attributeName = "numBathrooms")
    public double getNumBathrooms() {
        return numBathrooms;
    }

    @DynamoDBAttribute(attributeName = "listingStatus")
    public String getListingStatus() {
        return listingStatus;
    }

    @DynamoDBAttribute(attributeName = "lotSize")
    public double getLotSize() {
        return lotSize;
    }

    public void setListingNumber(String listingNumber) {
        this.listingNumber = listingNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSquareFootage(int squareFootage) {
        this.squareFootage = squareFootage;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setNumBedrooms(int numBedrooms) {
        this.numBedrooms = numBedrooms;
    }

    public void setNumBathrooms(double numBathrooms) {
        this.numBathrooms = numBathrooms;
    }

    public void setListingStatus(String listingStatus) {
        this.listingStatus = listingStatus;
    }

    public void setLotSize(double lotSize) {
        this.lotSize = lotSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListingRecord)) return false;
        ListingRecord that = (ListingRecord) o;
        return listingNumber.equals(that.listingNumber) && address.equals(that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listingNumber, address);
    }
}
