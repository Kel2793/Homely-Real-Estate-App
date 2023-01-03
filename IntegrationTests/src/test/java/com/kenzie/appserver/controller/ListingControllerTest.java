package com.kenzie.appserver.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kenzie.appserver.IntegrationTest;

import com.kenzie.appserver.config.ListingGenerator;
import com.kenzie.appserver.controller.model.ListingCreateRequest;
import com.kenzie.appserver.controller.model.ListingResponse;
import com.kenzie.appserver.repositories.model.ListingRecord;
import com.kenzie.appserver.service.ListingService;
import com.kenzie.appserver.service.model.Listing;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class ListingControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ListingService listingService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();
    private final ListingGenerator listingGenerator = new ListingGenerator();


    @Test
    public void getAllListings_successful() throws Exception {
        // GIVEN
        List<Listing> listings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            listings.add(listingGenerator.generateListing());
        }

        for (Listing listing : listings) {
            listingService.createNewListing(listing);
        }

        // WHEN
        MvcResult result =  mvc.perform(get("/listing")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        // THEN
        String response = result.getResponse().getContentAsString();
        assertThat(response).isNotNull();
        Assertions.assertTrue(response.contains("listingNumber"));

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
}