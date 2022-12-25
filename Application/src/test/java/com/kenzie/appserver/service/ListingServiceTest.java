package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.model.ListingRecord;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

public class ListingServiceTest {
    private ListingServiceRepository listingServiceRepository;
    private ListingService listingService;

    @BeforeEach
    void setup() {
        concertRepository = mock(ConcertRepository.class);
        cacheStore = mock(CacheStore.class);
        concertService = new ConcertService(concertRepository, cacheStore);
    }

    @Test
    public void listingService_findAllListings_returnsAllListings(){
        ListingRecord record = new ListingRecord();
        record.setListingNumber(randomUUID.toString());
        record.setAddress("123 Main St, City, State 11111");
        record.setSquareFootage(1600);
        record.setPrice(250000);
        record.setNumBedrooms(3);
        record.setNumBathrooms(2);
        record.setListingStatus("For sale");
        record.setLotSize(2.00);

        ListingRecord record2 = new ListingRecord();
        record2.setListingNumber(randomUUID.toString());
        record2.setAddress("321 Main St, City, State 11111");
        record2.setSquareFootage(1850);
        record2.setPrice(300000);
        record2.setNumBedrooms(4);
        record2.setNumBathrooms(3);
        record2.setListingStatus("For sale");
        record2.setLotSize(1.50);

        List<ListingRecord> actualListings = new Arraylist<>();
        actualListings.add(record);
        actualListings.add(record2);

        listingServiceRepository.save(actualListings);

        when(listingService.findAllListings()).thenReturn(actualListings);

        List<Listing> listings = listingService.findAllListings();

        Assertions.assertNotNull(listings, "The listings list is returned");
        Assertions.assertEquals(2, listings.size(), "There are two listings");

        for ()
    }
}
