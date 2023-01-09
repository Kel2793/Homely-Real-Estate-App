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

    public List<Listing> findParameterizedListings(int squareFootage, int price, int numBedrooms, double numBathrooms, double lotSize) {
        List<Listing> parameterizedListings = new ArrayList<>();
        Iterable<ListingRecord> listingIterator = null;

        //All the search parameter fields were entered with valid data by the user
        if (squareFootage != 0 && price != 0 && numBedrooms != 0 && numBathrooms != 0.0 && lotSize != 0.0) {
            listingIterator = listingRepository.findBySquareFootageGreaterThanEqualAndPriceLessThanAndNumBedroomsGreaterThanEqualAndNumBathroomsGreaterThanEqualAndLotSizeGreaterThanEqual(squareFootage, price, numBedrooms, numBathrooms, lotSize);
        }

        //Only the squareFootage, price, numBedrooms and numBathrooms fields were entered with valid data by the user
        if (squareFootage != 0 && price != 0 && numBedrooms != 0 && numBathrooms != 0.0 && lotSize == 0.0) {
            listingIterator = listingRepository.findBySquareFootageGreaterThanEqualAndPriceLessThanAndNumBedroomsGreaterThanEqualAndNumBathroomsGreaterThanEqual(squareFootage, price, numBedrooms, numBathrooms);
        }

        //Only the squareFootage, price, numBedrooms and lotSize fields were entered with valid data by the user
        if (squareFootage != 0 && price != 0 && numBedrooms != 0 && numBathrooms == 0.0 && lotSize != 0.0) {
            listingIterator = listingRepository.findBySquareFootageGreaterThanEqualAndPriceLessThanAndNumBedroomsGreaterThanEqualAndLotSizeGreaterThanEqual(squareFootage, price, numBedrooms, lotSize);
        }

        //Only the squareFootage, price, numBathrooms and lotSize fields were entered with valid data by the user
        if (squareFootage != 0 && price != 0 && numBedrooms == 0 && numBathrooms != 0.0 && lotSize != 0.0) {
            listingIterator = listingRepository.findBySquareFootageGreaterThanEqualAndPriceLessThanAndNumBathroomsGreaterThanEqualAndLotSizeGreaterThanEqual(squareFootage, price, numBathrooms, lotSize);
        }

        //Only the squareFootage, numBedrooms, numBathrooms and lotSize fields were entered with valid data by the user
        if (squareFootage != 0 && price == 0 && numBedrooms != 0 && numBathrooms != 0.0 && lotSize != 0.0) {
            listingIterator = listingRepository.findBySquareFootageGreaterThanEqualAndNumBedroomsGreaterThanEqualAndNumBathroomsGreaterThanEqualAndLotSizeGreaterThanEqual(squareFootage, numBedrooms, numBathrooms, lotSize);
        }

        //Only the squareFootage, price and numBedrooms fields were entered with valid data by the user
        if (squareFootage != 0 && price != 0 && numBedrooms != 0 && numBathrooms == 0.0 && lotSize == 0.0) {
            listingIterator = listingRepository.findBySquareFootageGreaterThanEqualAndPriceLessThanAndNumBedroomsGreaterThanEqual(squareFootage, price, numBedrooms);
        }

        //Only the squareFootage, price and numBathrooms fields were entered with valid data by the user
        if (squareFootage != 0 && price != 0 && numBedrooms == 0 && numBathrooms != 0.0 && lotSize == 0.0) {
            listingIterator = listingRepository.findBySquareFootageGreaterThanEqualAndPriceLessThanAndNumBathroomsGreaterThanEqual(squareFootage, price, numBathrooms);
        }

        //Only the squareFootage, price and lotSize fields were entered with valid data by the user
        if (squareFootage != 0 && price != 0 && numBedrooms == 0 && numBathrooms == 0.0 && lotSize != 0.0) {
            listingIterator = listingRepository.findBySquareFootageGreaterThanEqualAndPriceLessThanAndLotSizeGreaterThanEqual(squareFootage, price, lotSize);
        }

        //Only the squareFootage, numBedrooms and numBathrooms fields were entered with valid data by the user
        if (squareFootage != 0 && price == 0 && numBedrooms != 0 && numBathrooms != 0.0 && lotSize == 0.0) {
            listingIterator = listingRepository.findBySquareFootageGreaterThanEqualAndNumBedroomsGreaterThanEqualAndNumBathroomsGreaterThanEqual(squareFootage, numBedrooms, numBathrooms);
        }

        //Only the squareFootage, numBedrooms and lotSize fields were entered with valid data by the user
        if (squareFootage != 0 && price == 0 && numBedrooms != 0 && numBathrooms == 0.0 && lotSize != 0.0) {
            listingIterator = listingRepository.findBySquareFootageGreaterThanEqualAndNumBedroomsGreaterThanEqualAndLotSizeGreaterThanEqual(squareFootage, numBedrooms, lotSize);
        }

        //Only the squareFootage, numBathrooms and lotSize fields were entered with valid data by the user
        if (squareFootage != 0 && price == 0 && numBedrooms == 0 && numBathrooms != 0.0 && lotSize != 0.0) {
            listingIterator = listingRepository.findBySquareFootageGreaterThanEqualAndNumBathroomsGreaterThanEqualAndLotSizeGreaterThanEqual(squareFootage, numBathrooms, lotSize);
        }

        //Only the squareFootage and price fields were entered with valid data by the user
        if (squareFootage != 0 && price != 0 && numBedrooms == 0 && numBathrooms == 0.0 && lotSize == 0.0) {
            listingIterator = listingRepository.findBySquareFootageGreaterThanEqualAndPriceLessThan(squareFootage, price);
        }

        //Only the squareFootage and numBedrooms fields were entered with valid data by the user
        if (squareFootage != 0 && price == 0 && numBedrooms != 0 && numBathrooms == 0.0 && lotSize == 0.0) {
            listingIterator = listingRepository.findBySquareFootageGreaterThanEqualAndNumBedroomsGreaterThanEqual(squareFootage, numBedrooms);
        }

        //Only the squareFootage and numBathrooms fields were entered with valid data by the user
        if (squareFootage != 0 && price == 0 && numBedrooms == 0 && numBathrooms != 0.0 && lotSize == 0.0) {
            listingIterator = listingRepository.findBySquareFootageGreaterThanEqualAndNumBathroomsGreaterThanEqual(squareFootage, numBathrooms);
        }

        //Only the squareFootage and lotSize fields were entered with valid data by the user
        if (squareFootage != 0 && price == 0 && numBedrooms != 0 && numBathrooms == 0.0 && lotSize == 0.0) {
            listingIterator = listingRepository.findBySquareFootageGreaterThanEqualAndLotSizeGreaterThanEqual(squareFootage, lotSize);
        }

        //Only the squareFootage field has valid data
        if (squareFootage != 0 && price == 0 && numBedrooms == 0 && numBathrooms == 0.0 && lotSize == 0.0) {
            listingIterator = listingRepository.findBySquareFootageGreaterThanEqual(squareFootage);
        }

        //Only the price field has valid data
        if (squareFootage == 0 && price != 0 && numBedrooms == 0 && numBathrooms == 0.0 && lotSize == 0.0) {
            listingIterator = listingRepository.findByPriceLessThan(price);
        }

        //Only the price and numBedrooms fields were entered with valid data by the user
        if (squareFootage == 0 && price != 0 && numBedrooms != 0 && numBathrooms == 0.0 && lotSize == 0.0) {
            listingIterator = listingRepository.findByPriceLessThanAndNumBedroomsGreaterThanEqual(price, numBedrooms);
        }

        //Only the price and numBathrooms fields were entered with valid data by the user
        if (squareFootage == 0 && price != 0 && numBedrooms == 0 && numBathrooms != 0.0 && lotSize == 0.0) {
            listingIterator = listingRepository.findByPriceLessThanAndNumBathroomsGreaterThanEqual(price, numBathrooms);
        }

        //Only the price and lotSize fields were entered with valid data by the user
        if (squareFootage == 0 && price != 0 && numBedrooms == 0 && numBathrooms != 0.0 && lotSize != 0.0) {
            listingIterator = listingRepository.findByPriceLessThanAndLotSizeGreaterThanEqual(price, lotSize);
        }

        //Only the price, numBedrooms and numBathrooms fields were entered with valid data by the user
        if (squareFootage == 0 && price != 0 && numBedrooms != 0 && numBathrooms != 0.0 && lotSize == 0.0) {
            listingIterator = listingRepository.findByPriceLessThanAndNumBedroomsGreaterThanEqualAndNumBathroomsGreaterThanEqual(price, numBedrooms, numBathrooms);
        }

        //Only the price, numBedrooms and lotSize fields were entered with valid data by the user
        if (squareFootage == 0 && price != 0 && numBedrooms != 0 && numBathrooms == 0.0 && lotSize != 0.0) {
            listingIterator = listingRepository.findByPriceLessThanAndNumBedroomsGreaterThanEqualAndLotSizeGreaterThanEqual(price, numBedrooms, lotSize);
        }

        //Only the price, numBathrooms and lotSize fields were entered with valid data by the user
        if (squareFootage == 0 && price != 0 && numBedrooms == 0 && numBathrooms != 0.0 && lotSize != 0.0) {
            listingIterator = listingRepository.findByPriceLessThanAndNumBathroomsGreaterThanEqualAndLotSizeGreaterThanEqual(price, numBathrooms, lotSize);
        }

        //Only the price, numBedrooms, numBathrooms and lotSize fields were entered with valid data by the user
        if (squareFootage == 0 && price != 0 && numBedrooms != 0 && numBathrooms != 0.0 && lotSize != 0.0) {
            listingIterator = listingRepository.findByPriceLessThanAndNumBedroomsGreaterThanEqualAndNumBathroomsGreaterThanEqualAndLotSizeGreaterThanEqual(price, numBedrooms, numBathrooms, lotSize);
        }

        //Only the numBedrooms field has valid data
        if (squareFootage == 0 && price == 0 && numBedrooms != 0 && numBathrooms == 0.0 && lotSize == 0.0) {
            listingIterator = listingRepository.findByNumBedroomsGreaterThanEqual(numBedrooms);
        }

        //Only the numBedrooms and numBathrooms fields were entered with valid data by the user
        if (squareFootage == 0 && price == 0 && numBedrooms != 0 && numBathrooms != 0.0 && lotSize == 0.0) {
            listingIterator = listingRepository.findByNumBedroomsGreaterThanEqualAndNumBathroomsGreaterThanEqual(numBedrooms, numBathrooms);
        }

        //Only the numBedrooms and lotSize fields were entered with valid data by the user
        if (squareFootage == 0 && price == 0 && numBedrooms != 0 && numBathrooms == 0.0 && lotSize != 0.0) {
            listingIterator = listingRepository.findByNumBedroomsGreaterThanEqualAndLotSizeGreaterThanEqual(numBedrooms, lotSize);
        }

        //Only the numBedrooms, numBathrooms and lotSize fields were entered with valid data by the user
        if (squareFootage == 0 && price == 0 && numBedrooms != 0 && numBathrooms != 0.0 && lotSize != 0.0) {
            listingIterator = listingRepository.findByNumBedroomsGreaterThanEqualAndNumBathroomsGreaterThanEqualAndLotSizeGreaterThanEqual(numBedrooms, numBathrooms, lotSize);
        }

        //Only the numBathrooms field has valid data
        if (squareFootage == 0 && price == 0 && numBedrooms == 0 && numBathrooms != 0.0 && lotSize == 0.0) {
            listingIterator = listingRepository.findByNumBathroomsGreaterThanEqual(numBathrooms);
        }

        //Only the numBathrooms and lotSize fields were entered with valid data by the user
        if (squareFootage == 0 && price == 0 && numBedrooms == 0 && numBathrooms != 0.0 && lotSize != 0.0) {
            listingIterator = listingRepository.findByNumBathroomsGreaterThanEqualAndLotSizeGreaterThanEqual(numBathrooms, lotSize);
        }

        //Only the lotSize field has valid data
        if (squareFootage == 0 && price == 0 && numBedrooms == 0 && numBathrooms == 0.0 && lotSize != 0.0) {
            listingIterator = listingRepository.findByLotSizeGreaterThanEqual(lotSize);
        }

        assert listingIterator != null;
        for(ListingRecord listingRecord : listingIterator){
            if(listingRecord.getListingStatus().equalsIgnoreCase(ListingStatus.FOR_SALE.label)) {
                parameterizedListings.add(new Listing(listingRecord.getListingNumber(),
                        listingRecord.getAddress(),
                        listingRecord.getSquareFootage(),
                        listingRecord.getPrice(),
                        listingRecord.getNumBedrooms(),
                        listingRecord.getNumBathrooms(),
                        listingRecord.getLotSize(),
                        listingRecord.getListingStatus()));
            }
        }

        return parameterizedListings;
    }

    public Listing createNewListing(Listing listing) {

        //if status is not one of the ENUMs, return null
        if(!listing.getListingStatus().equalsIgnoreCase(ListingStatus.FOR_SALE.label) &&
                !listing.getListingStatus().equalsIgnoreCase(ListingStatus.WITHDRAWN.label) &&
                !listing.getListingStatus().equalsIgnoreCase(ListingStatus.SOLD.label) &&
                !listing.getListingStatus().equalsIgnoreCase(ListingStatus.UNDER_CONTRACT.label)) {
            return null;
        }
        //if price is less than 1, invalid price passed, return null;
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
