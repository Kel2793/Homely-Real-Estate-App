package com.kenzie.appserver.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.IntegrationTest;

import com.kenzie.appserver.config.ListingGenerator;
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
}