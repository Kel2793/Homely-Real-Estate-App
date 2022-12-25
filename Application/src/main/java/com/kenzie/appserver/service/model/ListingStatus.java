package com.kenzie.appserver.service.model;

public enum ListingStatus {
    FOR_SALE("For Sale"), SOLD("Sold"), UNDER_CONTRACT("Under Contract"), WITHDRAWN("Withdrawn");

    public final String label;

    private ListingStatus(String label) {
        this.label = label;
    }
}
