package com.kenzie.appserver.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kenzie.appserver.IntegrationTest;

import com.kenzie.appserver.config.ListingGenerator;
import com.kenzie.appserver.controller.model.ListingCreateRequest;
import com.kenzie.appserver.controller.model.ListingResponse;
import com.kenzie.appserver.service.ListingService;
import com.kenzie.appserver.service.model.Listing;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ListingControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ListingService listingService;
    private ObjectMapper mapper;
    private ListingGenerator listingGenerator;
    private List<Listing> newListings;

    @BeforeAll
    public void setup() {
        mapper = new ObjectMapper();
        listingGenerator = new ListingGenerator();

        newListings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            newListings.add(listingGenerator.generateListing());
        }

        for (Listing listing : newListings) {
            listingService.createNewListing(listing);
        }
    }

    @AfterAll
    public void cleanUp() {
        System.out.println("After All cleanUp() method called");
        for (Listing listing : newListings) {
            listingService.deleteListing(listing.getListingNumber());
        }
    }

    @Test
    public void getAllListings_successful() throws Exception {
        // GIVEN
        // Already created newListings in setup()

        // WHEN
        MvcResult allListingsResult =  mvc.perform(get("/listing")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        // THEN
        String response = allListingsResult.getResponse().getContentAsString();
        List<ListingResponse> allListingsResponseList = mapper.readValue(response, new TypeReference<List<ListingResponse>>() {});
        assertThat(allListingsResponseList).isNotEmpty()
                .hasSizeGreaterThan(0);
        assertEquals(newListings.size(), allListingsResponseList.size());
    }

    @Test
    public void createListing_createSuccessful() throws Exception {
        // GIVEN
        Listing listing = listingGenerator.generateListing();

        ListingCreateRequest request = new ListingCreateRequest();
        request.setAddress(listing.getAddress());
        request.setPrice(listing.getPrice());
        request.setSquareFootage(listing.getSquareFootage());
        request.setNumBedrooms(listing.getNumBedrooms());
        request.setNumBathrooms(listing.getNumBathrooms());
        request.setLotSize(listing.getLotSize());
        request.setListingStatus(listing.getListingStatus());

        // WHEN
        mapper.registerModule(new JavaTimeModule());

        mvc.perform(post("/listing")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(jsonPath("listingNumber")
                        .exists())
                .andExpect(jsonPath("address")
                        .value(is(listing.getAddress())))
                .andExpect(jsonPath("squareFootage")
                        .value(is(listing.getSquareFootage())))
                .andExpect(jsonPath("price")
                        .value(is(listing.getPrice())))
                .andExpect(jsonPath("numBedrooms")
                        .value(is(listing.getNumBedrooms())))
                .andExpect(jsonPath("numBathrooms")
                        .value(is(listing.getNumBathrooms())))
                .andExpect(jsonPath("listingStatus")
                        .value(is(listing.getListingStatus())))
                .andExpect(jsonPath("lotSize")
                        .value(is(listing.getLotSize())))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteListing_deleteSuccessful() throws Exception {
        // GIVEN
        Listing listing = listingGenerator.generateListing();

        Listing persistedListing = listingService.createNewListing(listing);

        // WHEN
        mvc.perform(delete("/listing/{listingNumber}", persistedListing.getListingNumber())
                        .accept(MediaType.APPLICATION_JSON))

        // THEN
                .andExpect(status().isNoContent());
        assertThat(listingService.findByListingNumber(listing.getListingNumber())).isNull();

    }

    @Test
    public void getAllOpenListings_successful() throws Exception {
        // GIVEN
        // Already created newListings in setup()

        // WHEN
        MvcResult openListingsResult =  mvc.perform(get("/listing/allOpen")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        // THEN
        String response = openListingsResult.getResponse().getContentAsString();
        List<ListingResponse> openListingsResponseList = mapper.readValue(response, new TypeReference<List<ListingResponse>>() {});
        assertThat(openListingsResponseList).isNotEmpty().hasSizeGreaterThan(0);
        assertThat(openListingsResponseList.get(0).getListingStatus()).isEqualToIgnoringCase("For Sale");
    }

}