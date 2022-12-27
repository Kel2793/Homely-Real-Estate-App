package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.repositories.ListingRepository;
import com.kenzie.appserver.repositories.model.ListingRecord;
import com.kenzie.appserver.service.model.Listing;
import com.kenzie.appserver.service.model.ListingStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;

import java.util.*;

import static org.mockito.Mockito.*;

public class ListingServiceTest {
    private ListingRepository listingServiceRepository;
    private ListingService listingService;

    private CacheStore cacheStore;

    @BeforeEach
    void setup() {
        cacheStore = mock(CacheStore.class);
        listingServiceRepository = mock(ListingRepository.class);
        listingService = new ListingService(listingServiceRepository, cacheStore);
    }

    @Test
    public void findAllListings() {
        Listing listing1 = new Listing(UUID.randomUUID().toString(),
                "123 Main St, City, State 11111",
                1600,
                250000,
                3,
                2,
                2,
                "For sale");

        Listing listing2 = new Listing(UUID.randomUUID().toString(),
                "321 Main St, City, State 11111",
                1850,
                325000,
                4,
                3,
                3,
                "For sale");

        ListingRecord record1 = new ListingRecord();
        record1.setListingNumber(listing1.getListingNumber());
        record1.setAddress(listing1.getAddress());
        record1.setSquareFootage(listing1.getSquareFootage());
        record1.setPrice(listing1.getPrice());
        record1.setNumBedrooms(listing1.getNumBedrooms());
        record1.setNumBathrooms(listing1.getNumBathrooms());
        record1.setLotSize(listing1.getLotSize());
        record1.setListingStatus(listing1.getListingStatus());

        ListingRecord record2 = new ListingRecord();
        record2.setListingNumber(listing2.getListingNumber());
        record2.setAddress(listing2.getAddress());
        record2.setSquareFootage(listing2.getSquareFootage());
        record2.setPrice(listing2.getPrice());
        record2.setNumBedrooms(listing2.getNumBedrooms());
        record2.setNumBathrooms(listing2.getNumBathrooms());
        record2.setLotSize(listing2.getLotSize());
        record2.setListingStatus(listing2.getListingStatus());

        List<ListingRecord> records = new ArrayList<>();
        records.add(record1);
        records.add(record2);

        when(listingServiceRepository.findAll()).thenReturn(records);

        List<Listing> actualListings = listingService.findAllListings();

        Assertions.assertNotNull(actualListings, "The listings list is returned");
        Assertions.assertEquals(2, actualListings.size(), "There are two listings");

        for (Listing listing : actualListings) {
            if (listing.getListingNumber().equals(listing1.getListingNumber())) {
                Assertions.assertEquals(listing1.getAddress(), listing.getAddress());
                Assertions.assertEquals(listing1.getSquareFootage(), listing.getSquareFootage());
                Assertions.assertEquals(listing1.getPrice(), listing.getPrice());
                Assertions.assertEquals(listing1.getNumBedrooms(), listing.getNumBedrooms());
                Assertions.assertEquals(listing1.getNumBathrooms(), listing.getNumBathrooms());
                Assertions.assertEquals(listing1.getLotSize(), listing.getLotSize());
                Assertions.assertEquals(listing1.getListingStatus(), listing.getListingStatus());
            } else if (listing.getListingNumber().equals(listing2.getListingNumber())) {
                Assertions.assertEquals(listing2.getAddress(), listing.getAddress());
                Assertions.assertEquals(listing2.getSquareFootage(), listing.getSquareFootage());
                Assertions.assertEquals(listing2.getPrice(), listing.getPrice());
                Assertions.assertEquals(listing2.getNumBedrooms(), listing.getNumBedrooms());
                Assertions.assertEquals(listing2.getNumBathrooms(), listing.getNumBathrooms());
                Assertions.assertEquals(listing2.getLotSize(), listing.getLotSize());
                Assertions.assertEquals(listing2.getListingStatus(), listing.getListingStatus());
            } else {
                Assertions.assertTrue(false, "Listing returned that was not in the records!");
            }
        }
    }

    @Test
    public void findByListingNumber() {
        String listingNumber = UUID.randomUUID().toString();

        Listing listing = new Listing(
                listingNumber,
                "123 Test St, City, State, 00000",
                2200,
                300000,
                4,
                2.5,
                1,
                "For sale");

        ListingRecord record = new ListingRecord();
        record.setListingNumber(listingNumber);
        record.setAddress(listing.getAddress());
        record.setSquareFootage(listing.getSquareFootage());
        record.setPrice(listing.getPrice());
        record.setNumBedrooms(listing.getNumBedrooms());
        record.setNumBathrooms(listing.getNumBathrooms());
        record.setLotSize(listing.getLotSize());
        record.setListingStatus(listing.getListingStatus());

        Optional<ListingRecord> optionalRecords = Optional.of(record);

        when(listingServiceRepository.findById(listingNumber)).thenReturn(optionalRecords);

        Listing actualListing = listingService.findByListingNumber(listingNumber);

        Assertions.assertNotNull(actualListing, "Listing should not be null");
        Assertions.assertEquals(listing.getListingNumber(), actualListing.getListingNumber());
        Assertions.assertEquals(listing.getAddress(), actualListing.getAddress());
        Assertions.assertEquals(listing.getSquareFootage(), actualListing.getSquareFootage());
        Assertions.assertEquals(listing.getPrice(), actualListing.getPrice());
        Assertions.assertEquals(listing.getNumBedrooms(), actualListing.getNumBedrooms());
        Assertions.assertEquals(listing.getNumBathrooms(), actualListing.getNumBathrooms());
        Assertions.assertEquals(listing.getLotSize(), actualListing.getLotSize());
        Assertions.assertEquals(listing.getListingStatus(), actualListing.getListingStatus());
    }

    @Test
    void findAllOpenListings() {
        Listing listing1 = new Listing(UUID.randomUUID().toString(),
                "123 Main St, City, State 11111",
                1700,
                425000,
                4,
                3.5,
                1,
                "For sale");

        Listing listing2 = new Listing(UUID.randomUUID().toString(),
                "321 Main St, City, State 11111",
                1600,
                650000,
                4,
                4,
                3,
                "Closed");

        ListingRecord record1 = new ListingRecord();
        record1.setListingNumber(listing1.getListingNumber());
        record1.setAddress(listing1.getAddress());
        record1.setSquareFootage(listing1.getSquareFootage());
        record1.setPrice(listing1.getPrice());
        record1.setNumBedrooms(listing1.getNumBedrooms());
        record1.setNumBathrooms(listing1.getNumBathrooms());
        record1.setLotSize(listing1.getLotSize());
        record1.setListingStatus(listing1.getListingStatus());

        ListingRecord record2 = new ListingRecord();
        record2.setListingNumber(listing2.getListingNumber());
        record2.setAddress(listing2.getAddress());
        record2.setSquareFootage(listing2.getSquareFootage());
        record2.setPrice(listing2.getPrice());
        record2.setNumBedrooms(listing2.getNumBedrooms());
        record2.setNumBathrooms(listing2.getNumBathrooms());
        record2.setLotSize(listing2.getLotSize());
        record2.setListingStatus(listing2.getListingStatus());

        List<ListingRecord> records = new ArrayList<>();
        records.add(record1);
        records.add(record2);

        when(listingServiceRepository.findAll()).thenReturn(records);

        List<Listing> actualList = listingService.findAllOpenListings();

        Assertions.assertNotNull(actualList, "There should be one open listing");
        Assertions.assertEquals(1, actualList.size());
        Assertions.assertEquals(listing1.getListingNumber(), actualList.get(0).getListingNumber());
        Assertions.assertEquals(listing1.getAddress(), actualList.get(0).getAddress());
        Assertions.assertEquals(listing1.getSquareFootage(), actualList.get(0).getSquareFootage());
        Assertions.assertEquals(listing1.getPrice(), actualList.get(0).getPrice());
        Assertions.assertEquals(listing1.getNumBedrooms(), actualList.get(0).getNumBedrooms());
        Assertions.assertEquals(listing1.getNumBathrooms(), actualList.get(0).getNumBathrooms());
        Assertions.assertEquals(listing1.getLotSize(), actualList.get(0).getLotSize());
        Assertions.assertEquals(listing1.getListingStatus(), actualList.get(0).getListingStatus());
    }

    @Test
    void createNewListing() {
        Listing listing = new Listing(UUID.randomUUID().toString(),
                "111 Test St., City, State, 11111",
                1900,
                450000,
                5,
                4,
                5,
                "For sale");

        Listing actual = listingService.createNewListing(listing);

        Assertions.assertNotNull(actual, "Listing should not be null");
        Assertions.assertEquals(listing.getListingNumber(), actual.getListingNumber());
        Assertions.assertEquals(listing.getAddress(), actual.getAddress());
        Assertions.assertEquals(listing.getPrice(), actual.getPrice());
        Assertions.assertEquals(listing.getNumBedrooms(), actual.getNumBedrooms());
        Assertions.assertEquals(listing.getLotSize(), actual.getLotSize());
        Assertions.assertEquals(listing.getListingStatus(), actual.getListingStatus());
    }

    @Test
    void updatePrice(){
        String listingNumber = UUID.randomUUID().toString();

        Listing listing = new Listing(listingNumber,
                "123 Test Road, City, State, 10101",
                2000,
                500000,
                4,
                4,
                1,
                "For sale");

        ListingRecord record = new ListingRecord();
        record.setListingNumber(listingNumber);
        record.setAddress(listing.getAddress());
        record.setSquareFootage(listing.getSquareFootage());
        record.setPrice(listing.getPrice());
        record.setNumBedrooms(listing.getNumBedrooms());
        record.setNumBathrooms(listing.getNumBathrooms());
        record.setLotSize(listing.getLotSize());
        record.setListingStatus(listing.getListingStatus());

        Optional<ListingRecord> optionalRecords = Optional.of(record);

        ArgumentCaptor<ListingRecord> listingRecordCaptor = ArgumentCaptor.forClass(ListingRecord.class);
        when(listingServiceRepository.existsById(listingNumber)).thenReturn(true);
        when(listingServiceRepository.findById(listingNumber)).thenReturn(optionalRecords);

        listingService.updatePrice(listingNumber, 479900);

        verify(listingServiceRepository).save(listingRecordCaptor.capture());
        ListingRecord record1 = listingRecordCaptor.getValue();

        Assertions.assertNotNull(record1, "Listing record should not be null after updating price");
        Assertions.assertEquals(listing.getListingNumber(), record1.getListingNumber());
        Assertions.assertEquals(listing.getAddress(), record1.getAddress());
        Assertions.assertEquals(listing.getSquareFootage(), record1.getSquareFootage());
        Assertions.assertEquals(479900, record1.getPrice());
        Assertions.assertEquals(listing.getNumBedrooms(), record1.getNumBedrooms());
        Assertions.assertEquals(listing.getNumBathrooms(), record1.getNumBathrooms());
        Assertions.assertEquals(listing.getLotSize(), record1.getLotSize());
        Assertions.assertEquals(listing.getListingStatus(), record1.getListingStatus());
    }

    @Test
    void updateListingStatus(){
        String listingNumber = UUID.randomUUID().toString();

        Listing listing = new Listing(listingNumber,
                "123 Test Drive, City, State, 10101",
                2500,
                600000,
                5,
                4,
                1,
                "For sale");

        ListingRecord record = new ListingRecord();
        record.setListingNumber(listingNumber);
        record.setAddress(listing.getAddress());
        record.setSquareFootage(listing.getSquareFootage());
        record.setPrice(listing.getPrice());
        record.setNumBedrooms(listing.getNumBedrooms());
        record.setNumBathrooms(listing.getNumBathrooms());
        record.setLotSize(listing.getLotSize());
        record.setListingStatus(listing.getListingStatus());

        Optional<ListingRecord> optionalRecords = Optional.of(record);

        ArgumentCaptor<ListingRecord> listingRecordCaptor = ArgumentCaptor.forClass(ListingRecord.class);

        when(listingServiceRepository.existsById(listingNumber)).thenReturn(true);
        when(listingServiceRepository.findById(listingNumber)).thenReturn(optionalRecords);

        listingService.updateStatus(listingNumber, "Closed");

        verify(listingServiceRepository).save(listingRecordCaptor.capture());
        ListingRecord record1 = listingRecordCaptor.getValue();

        Assertions.assertNotNull(record1, "Listing record should not be null after updating status");
        Assertions.assertEquals(listing.getListingNumber(), record1.getListingNumber());
        Assertions.assertEquals(listing.getAddress(), record1.getAddress());
        Assertions.assertEquals(listing.getSquareFootage(), record1.getSquareFootage());
        Assertions.assertEquals(listing.getPrice(), record1.getPrice());
        Assertions.assertEquals(listing.getNumBedrooms(), record1.getNumBedrooms());
        Assertions.assertEquals(listing.getNumBathrooms(), record1.getNumBathrooms());
        Assertions.assertEquals(listing.getLotSize(), record1.getLotSize());
        Assertions.assertEquals("Closed", record1.getListingStatus());
    }

    @Test
    void deleteListing(){
        String listingNumber = UUID.randomUUID().toString();

        Listing listing = new Listing(listingNumber,
                "123 Test Drive, City, State, 10101",
                3200,
                750000,
                6,
                5.5,
                2,
                "For sale");

        listingService.createNewListing(listing);
        listingService.deleteListing(listingNumber);

        verify(listingServiceRepository).deleteById(listingNumber);
    }
}
