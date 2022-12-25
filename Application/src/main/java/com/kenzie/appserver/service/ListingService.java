package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.repositories.ListingRepository;
import com.kenzie.appserver.repositories.model.ListingRecord;
import com.kenzie.appserver.service.model.Listing;
import com.kenzie.appserver.service.model.ListingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ListingService {
    private ListingRepository listingRepository;
    private CacheStore cache;

    @Autowired
    public ListingService(ListingRepository listingRepository, CacheStore cache) {
        this.listingRepository = listingRepository;
        this.cache = cache;
    }

    public List<Listing> findAllListings() {
        List<Listing> listings = new ArrayList<>();

        Iterable<ListingRecord> listingIterator = listingRepository.findAll();
        for(ListingRecord record : listingIterator){
            listings.add(new Listing(record.getListingNumber(),
                    record.getAddress(),
                    record.getSquareFootage(),
                    record.getPrice(),
                    record.getNumBedrooms(),
                    record.getNumBathrooms(),
                    record.getLotSize(),
                    record.getListingStatus()));
        }
        return listings;
    }

    public List<Listing> findAllOpenListings() {
        List<Listing> openListings = new ArrayList<>();

        Iterable<ListingRecord> listingIterator = listingRepository.findAll();
        for(ListingRecord record : listingIterator){
            if (record.getListingStatus().equalsIgnoreCase("For sale")){
                openListings.add(new Listing(record.getListingNumber(),
                        record.getAddress(),
                        record.getSquareFootage(),
                        record.getPrice(),
                        record.getNumBedrooms(),
                        record.getNumBathrooms(),
                        record.getLotSize(),
                        record.getListingStatus()));
            }
        }

        return openListings;
    }

    public Listing findByListingNumber(String listingNumber) {

        // Attempts to pull the listing from the cache
        // Immediately returns if cache is found
        Listing cachedListing = cache.get(listingNumber);
        if (cachedListing != null) {
            return cachedListing;
        }

        //otherwise use repository
        Optional<ListingRecord> optionalRecord = listingRepository.findById(listingNumber);

        if (optionalRecord.isPresent()){

            ListingRecord record = optionalRecord.get();
            Listing returnedListing = new Listing(record.getListingNumber(),
                    record.getAddress(),
                    record.getSquareFootage(),
                    record.getPrice(),
                    record.getNumBedrooms(),
                    record.getNumBathrooms(),
                    record.getLotSize(),
                    record.getListingStatus());

            // add listing to cache before returning
            cache.add(record.getListingNumber(), returnedListing);

            return returnedListing;
        } else {
            return null;
        }
    }

    public List<Listing> findParameterizedListings(int squareFootage, int price, int numBedrooms, int numBathrooms, int lotSize) {
        return null;
    }

    public Listing createNewListing(Listing listing) {

        //if status is not one of the ENUMs, return null
        if(!listing.getListingStatus().equalsIgnoreCase(ListingStatus.FOR_SALE.label) ||
                !listing.getListingStatus().equalsIgnoreCase(ListingStatus.WITHDRAWN.label) ||
                !listing.getListingStatus().equalsIgnoreCase(ListingStatus.SOLD.label) ||
                !listing.getListingStatus().equalsIgnoreCase(ListingStatus.UNDER_CONTRACT.label)) {
            return null;
        }
        //if price is less then 1, invalid price passed, return null;
        if(listing.getPrice() < 1) {
            return null;
        }

        //else build a new listing
        ListingRecord record = new ListingRecord();
        record.setListingNumber(listing.getListingNumber());
        record.setAddress(listing.getAddress());
        record.setSquareFootage(listing.getSquareFootage());
        record.setPrice(listing.getPrice());
        record.setPrice(listing.getPrice());
        record.setNumBedrooms(listing.getNumBedrooms());
        record.setNumBathrooms(listing.getNumBathrooms());
        record.setLotSize(listing.getLotSize());
        record.setListingStatus(listing.getListingStatus());

        listingRepository.save(record);

        return listing;
    }

    public void updateStatus(String listingNumber, String updatedListingStatus) {
        if (listingRepository.existsById(listingNumber)) {
            Listing listing = findByListingNumber(listingNumber);
            ListingRecord listingRecord = new ListingRecord();

            listingRecord.setListingNumber(listing.getListingNumber());
            listingRecord.setAddress(listing.getAddress());
            listingRecord.setSquareFootage(listing.getSquareFootage());
            listingRecord.setPrice(listing.getPrice());
            listingRecord.setNumBedrooms(listing.getNumBedrooms());
            listingRecord.setNumBathrooms(listing.getNumBathrooms());
            listingRecord.setListingStatus(updatedListingStatus);
            listingRecord.setLotSize(listing.getLotSize());

            listingRepository.save(listingRecord);
            cache.evict(listingNumber);
        }
    }
    public void updatePrice(String listingNumber, int updatedPrice) {
        if (listingRepository.existsById(listingNumber)) {
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
            cache.evict(listingNumber);
        }
    }

    public void deleteListing(String listingNumber){
        listingRepository.deleteById(listingNumber);
        cache.evict(listingNumber);
    }

}
