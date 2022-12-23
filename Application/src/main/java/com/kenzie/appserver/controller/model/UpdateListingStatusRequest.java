package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class UpdateListingStatusRequest {

    @NotEmpty
    @JsonProperty("listingNumber")
    private String listingNumber;

    @NotEmpty
    @JsonProperty("listingStatus")
    private String listingStatus;

    public String getListingNumber() {
        return listingNumber;
    }

    public void setListingNumber(String listingNumber) {
        this.listingNumber = listingNumber;
    }

    public String getListingStatus() {
        return listingStatus;
    }

    public void setListingStatus(String listingStatus) {
        this.listingStatus = listingStatus;
    }
}
