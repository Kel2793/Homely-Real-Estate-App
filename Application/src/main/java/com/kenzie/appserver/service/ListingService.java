package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.ListingRepository;
import com.kenzie.appserver.repositories.model.ListingRecord;
import com.kenzie.appserver.service.model.Listing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListingService {
    private ListingRepository listingRepository;

    @Autowired
    public ListingService(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public List<Listing> findAllListings() {
        return null;
    }

    public List<Listing> findAllOpenListings() {
        return null;
    }

    public Listing findByListingNumber(String listingNumber) {
        return null;
    }

    public List<Listing> findParameterizedListings(int squareFootage, int price, int numBedrooms, int numBathrooms, int lotSize) {
        return null;
    }

    public Listing createNewListing(Listing listing) {
        return null;
    }

    public void updateStatus(String listingNumber, String updatedListingStatus) {

    }
    public void updatePrice(String listingNumber, int updatedPrice) {
        Listing listing = findByListingNumber(listingNumber);
        ListingRecord listingRecord = new ListingRecord();

        listingRecord.setListingNumber(listing.getListingNumber());
        listingRecord.setAddress(listing.getAddress());
        listingRecord.setSquareFootage(listing.getSquareFootage());
        listingRecord.setPrice(updatedPrice);
        listingRecord.setNumBedrooms(listing.getNumBedrooms());
        listingRecord.setNumBathrooms(listing.getNumBathrooms());
        listingRecord.setListingStatus(listing.getListingStatus());
        listingRecord.setLotSize(listing.getLotSize());

        listingRepository.save(listingRecord);

    }

    public void deleteListing(String listingNumber){
    }

}
