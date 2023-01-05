package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.config.ListingGenerator;
import com.kenzie.appserver.repositories.ListingRepository;
import com.kenzie.appserver.repositories.model.ListingRecord;
import com.kenzie.appserver.service.model.Listing;
import com.kenzie.appserver.service.model.ListingStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;

import java.util.*;
import java.util.concurrent.TimeUnit;

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

        ListingRecord record1 = createListingRecord(listing1);

        ListingRecord record2 = createListingRecord(listing2);

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

        ListingRecord record = createListingRecord(listing);

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
    public void findByListingNumber_cacheIsUsed() {
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

        when(cacheStore.get(listingNumber)).thenReturn(listing);

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
    void findByListingNumber_optionalRecordNotPresent_returnsNull() {
        String listingNumber = UUID.randomUUID().toString();

        Listing listing = new Listing(
                listingNumber,
                "123 Test St, City, State, 00000",
                2500,
                750000,
                5,
                3.5,
                0.5,
                "For sale");

        Optional<ListingRecord> optionalRecords = Optional.empty();

        when(listingServiceRepository.findById(listingNumber)).thenReturn(optionalRecords);

        Listing actual = listingService.findByListingNumber(listingNumber);

        Assertions.assertNull(actual);
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

        ListingRecord record1 = createListingRecord(listing1);

        ListingRecord record2 = createListingRecord(listing2);

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
    void createNewListing_forSaleStatus_createsListing() {
        Listing listing = new Listing(UUID.randomUUID().toString(),
                "111 Test St., City, State, 11111",
                1900,
                450000,
                5,
                4,
                1.75,
                ListingStatus.FOR_SALE.label);

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
    void createNewListing_forWithdrawnStatus_createsListing() {
        Listing listing = new Listing(UUID.randomUUID().toString(),
                "111 Test St., City, State, 11111",
                1900,
                450000,
                5,
                4,
                1.75,
                ListingStatus.WITHDRAWN.label);

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
    void createNewListing_forSoldStatus_createsListing() {
        Listing listing = new Listing(UUID.randomUUID().toString(),
                "111 Test St., City, State, 11111",
                1900,
                450000,
                5,
                4,
                1.75,
                ListingStatus.SOLD.label);

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
    void createNewListing_forUnderContractStatus_createsListing() {
        Listing listing = new Listing(UUID.randomUUID().toString(),
                "111 Test St., City, State, 11111",
                1900,
                450000,
                5,
                4,
                1.75,
                ListingStatus.UNDER_CONTRACT.label);

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
    void createListing_invalidStatus_returnsNull() {
        Listing listing = new Listing(UUID.randomUUID().toString(),
                "111 Test St., City, State, 11111",
                1675,
                199000,
                2,
                2,
                5.5,
                "Not a status!");

        Listing actual = listingService.createNewListing(listing);

        Assertions.assertNull(actual);
    }

    @Test
    void createListing_invalidPrice_returnsNull() {
        Listing listing = new Listing(UUID.randomUUID().toString(),
                "111 Test St., City, State, 11111",
                1375,
                0,
                2,
                2,
                0.25,
                "For sale");

        Listing actual = listingService.createNewListing(listing);

        Assertions.assertNull(actual);
    }

    @Test
    void updatePrice() {
        String listingNumber = UUID.randomUUID().toString();

        Listing listing = new Listing(listingNumber,
                "123 Test Road, City, State, 10101",
                2000,
                500000,
                4,
                4,
                1,
                "For sale");

        ListingRecord record = createListingRecord(listing);

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
    void updateListingStatus() {
        String listingNumber = UUID.randomUUID().toString();

        Listing listing = new Listing(listingNumber,
                "123 Test Drive, City, State, 10101",
                2500,
                600000,
                5,
                4,
                1,
                "For sale");

        ListingRecord record = createListingRecord(listing);

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
    void updateStatus_doesNotExistById_doesNothing() {
        String listingNumber = UUID.randomUUID().toString();

        Listing listing = new Listing(listingNumber,
                "123 Test Drive, City, State, 10101",
                2500,
                600000,
                5,
                4,
                1,
                "For sale");

        ListingRecord record = createListingRecord(listing);

        when(listingServiceRepository.existsById(listingNumber)).thenReturn(false);

        listingService.updateStatus(listingNumber, "Closed");

        Assertions.assertEquals("For sale", listing.getListingStatus());
    }

    @Test
    void updatePrice_doesNotExistById_doesNothing() {
        String listingNumber = UUID.randomUUID().toString();

        Listing listing = new Listing(listingNumber,
                "123 Test Drive, City, State, 10101",
                2300,
                389900,
                3,
                4,
                2.4,
                "For sale");

        ListingRecord record = createListingRecord(listing);

        when(listingServiceRepository.existsById(listingNumber)).thenReturn(false);

        listingService.updatePrice(listingNumber, 375000);

        Assertions.assertEquals(389900, listing.getPrice());
    }

    @Test
    void deleteListing() {
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

    public ListingRecord createListingRecord(Listing listing) {
        ListingRecord record = new ListingRecord();
        record.setListingNumber(listing.getListingNumber());
        record.setAddress(listing.getAddress());
        record.setSquareFootage(listing.getSquareFootage());
        record.setPrice(listing.getPrice());
        record.setNumBedrooms(listing.getNumBedrooms());
        record.setNumBathrooms(listing.getNumBathrooms());
        record.setLotSize(listing.getLotSize());
        record.setListingStatus(listing.getListingStatus());

        return record;
    }

    @Test
    void findParameterizedListings_allParametersEntered_returnsSortedList() {
        Listing listing1 = new Listing(UUID.randomUUID().toString(),
                "123 Main St, City, State 11111",
                1700,
                349000,
                4,
                3.5,
                1,
                "For sale");

        ListingRecord record1 = createListingRecord(listing1);

        List<ListingRecord> expected = new ArrayList<>();
        expected.add(record1);

        when(listingServiceRepository.findBySquareFootageGreaterThanEqualAndPriceLessThanAndNumBedroomsGreaterThanEqualAndNumBathroomsGreaterThanEqualAndLotSizeGreaterThanEqual(1800, 350000, 5, 0.0, 2.00)).thenReturn(expected);

        List<Listing> actualList = listingService.findParameterizedListings(1800, 350000, 5, 0,2.00);

        Assertions.assertNotNull(actualList, "There should be one listing that matches the criteria");
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
    void findParameterizedListings_onlySquareFootageEntered_returnsSortedList() {
        Listing listing1 = new Listing(UUID.randomUUID().toString(),
                "123 Main St, City, State 11111",
                1200,
                500000,
                2,
                2,
                0.5,
                "For sale");

        ListingRecord record1 = createListingRecord(listing1);

        List<ListingRecord> expected = new ArrayList<>();
        expected.add(record1);

        when(listingServiceRepository.findBySquareFootageGreaterThanEqual(1300)).thenReturn(expected);

        List<Listing> actualList = listingService.findParameterizedListings(1300, 0, 0, 0, 0.0);

        Assertions.assertNotNull(actualList, "There should be one listing that matches the criteria");
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
    void findParameterizedListings_onlyPriceEntered_returnsSortedList() {
        Listing listing1 = new Listing(UUID.randomUUID().toString(),
                "123 Main St, City, State 11111",
                1350,
                400000,
                3,
                2,
                0.5,
                "For sale");

        Listing listing2 = new Listing(UUID.randomUUID().toString(),
                "321 Main St, City, State 11111",
                1302,
                350000,
                3,
                2,
                1.2,
                "For sale");

        ListingRecord record1 = createListingRecord(listing1);
        ListingRecord record2 = createListingRecord(listing2);

        List<ListingRecord> expected = new ArrayList<>();
        expected.add(record1);
        expected.add(record2);

        when(listingServiceRepository.findByPriceLessThan(500000)).thenReturn(expected);

        List<Listing> actualList = listingService.findParameterizedListings(0, 500000, 0, 0, 0.0);

        Assertions.assertNotNull(actualList, "There should be two listings that match the criteria");
        Assertions.assertEquals(2, actualList.size());
        Assertions.assertEquals(listing1.getListingNumber(), actualList.get(0).getListingNumber());
        Assertions.assertEquals(listing1.getAddress(), actualList.get(0).getAddress());
        Assertions.assertEquals(listing1.getSquareFootage(), actualList.get(0).getSquareFootage());
        Assertions.assertEquals(listing1.getPrice(), actualList.get(0).getPrice());
        Assertions.assertEquals(listing1.getNumBedrooms(), actualList.get(0).getNumBedrooms());
        Assertions.assertEquals(listing1.getNumBathrooms(), actualList.get(0).getNumBathrooms());
        Assertions.assertEquals(listing1.getLotSize(), actualList.get(0).getLotSize());
        Assertions.assertEquals(listing1.getListingStatus(), actualList.get(0).getListingStatus());

        Assertions.assertEquals(listing2.getListingNumber(), actualList.get(1).getListingNumber());
        Assertions.assertEquals(listing2.getAddress(), actualList.get(1).getAddress());
        Assertions.assertEquals(listing2.getSquareFootage(), actualList.get(1).getSquareFootage());
        Assertions.assertEquals(listing2.getPrice(), actualList.get(1).getPrice());
        Assertions.assertEquals(listing2.getNumBedrooms(), actualList.get(1).getNumBedrooms());
        Assertions.assertEquals(listing2.getNumBathrooms(), actualList.get(1).getNumBathrooms());
        Assertions.assertEquals(listing2.getLotSize(), actualList.get(1).getLotSize());
        Assertions.assertEquals(listing2.getListingStatus(), actualList.get(1).getListingStatus());
    }

    @Test
    void findParameterizedListings_onlyNumBedroomsEntered_returnsSortedList() {
        Listing listing1 = new Listing(UUID.randomUUID().toString(),
                "123 Main St, City, State 11111",
                1200,
                500000,
                2,
                2,
                0.5,
                "For sale");

        Listing listing2 = new Listing(UUID.randomUUID().toString(),
                "123 Main St, City, State 11111",
                1250,
                365000,
                2,
                1.5,
                1.5,
                "For sale");

        ListingRecord record1 = createListingRecord(listing1);
        ListingRecord record2 = createListingRecord(listing2);

        List<ListingRecord> expected = new ArrayList<>();
        expected.add(record1);
        expected.add(record2);

        when(listingServiceRepository.findByNumBedroomsGreaterThanEqual(2)).thenReturn(expected);

        List<Listing> actualList = listingService.findParameterizedListings(0, 0, 2, 0, 0);

        Assertions.assertNotNull(actualList, "There should be two listings that matches the criteria");
        Assertions.assertEquals(2, actualList.size());
        Assertions.assertEquals(listing1.getListingNumber(), actualList.get(0).getListingNumber());
        Assertions.assertEquals(listing1.getAddress(), actualList.get(0).getAddress());
        Assertions.assertEquals(listing1.getSquareFootage(), actualList.get(0).getSquareFootage());
        Assertions.assertEquals(listing1.getPrice(), actualList.get(0).getPrice());
        Assertions.assertEquals(listing1.getNumBedrooms(), actualList.get(0).getNumBedrooms());
        Assertions.assertEquals(listing1.getNumBathrooms(), actualList.get(0).getNumBathrooms());
        Assertions.assertEquals(listing1.getLotSize(), actualList.get(0).getLotSize());
        Assertions.assertEquals(listing1.getListingStatus(), actualList.get(0).getListingStatus());

        Assertions.assertEquals(listing2.getListingNumber(), actualList.get(1).getListingNumber());
        Assertions.assertEquals(listing2.getAddress(), actualList.get(1).getAddress());
        Assertions.assertEquals(listing2.getSquareFootage(), actualList.get(1).getSquareFootage());
        Assertions.assertEquals(listing2.getPrice(), actualList.get(1).getPrice());
        Assertions.assertEquals(listing2.getNumBedrooms(), actualList.get(1).getNumBedrooms());
        Assertions.assertEquals(listing2.getNumBathrooms(), actualList.get(1).getNumBathrooms());
        Assertions.assertEquals(listing2.getLotSize(), actualList.get(1).getLotSize());
        Assertions.assertEquals(listing2.getListingStatus(), actualList.get(1).getListingStatus());
    }

    @Test
    void findParameterizedListings_onlyLotSizeEntered_returnsSortedList() {
        Listing listing1 = new Listing(UUID.randomUUID().toString(),
                "123 Main St, City, State 11111",
                1425,
                625000,
                3,
                2.0,
                1.9,
                "For sale");

        ListingRecord record1 = createListingRecord(listing1);

        List<ListingRecord> expected = new ArrayList<>();
        expected.add(record1);

        when(listingServiceRepository.findByLotSizeGreaterThanEqual(2.00)).thenReturn(expected);

        List<Listing> actualList = listingService.findParameterizedListings(0, 0, 0, 2.0, 0.0);

        Assertions.assertNotNull(actualList, "There should be one listing that matches the criteria");
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
}
