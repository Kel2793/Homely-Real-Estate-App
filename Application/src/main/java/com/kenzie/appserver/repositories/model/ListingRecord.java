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
    private int numRooms;
    private String listingStatus;
    private String schoolDistrict;
    private int lotSize;
    private String buildingType;


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

    @DynamoDBAttribute(attributeName = "numRooms")
    public int getNumRooms() {
        return numRooms;
    }

    @DynamoDBAttribute(attributeName = "listingStatus")
    public String getListingStatus() {
        return listingStatus;
    }

    @DynamoDBAttribute(attributeName = "schoolDistrict")
    public String getSchoolDistrict() {
        return schoolDistrict;
    }

    @DynamoDBAttribute(attributeName = "lotSize")
    public int getLotSize() {
        return lotSize;
    }

    @DynamoDBAttribute(attributeName = "buildingType")
    public String getBuildingType() {
        return buildingType;
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

    public void setNumRooms(int numRooms) {
        this.numRooms = numRooms;
    }

    public void setListingStatus(String listingStatus) {
        this.listingStatus = listingStatus;
    }

    public void setSchoolDistrict(String schoolDistrict) {
        this.schoolDistrict = schoolDistrict;
    }

    public void setLotSize(int lotSize) {
        this.lotSize = lotSize;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
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
