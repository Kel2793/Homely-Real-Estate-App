package com.kenzie.appserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kenzie.appserver.IntegrationTest;

import com.kenzie.appserver.config.ListingGenerator;
import com.kenzie.appserver.controller.model.ListingCreateRequest;
import com.kenzie.appserver.controller.model.ListingResponse;
import com.kenzie.appserver.controller.model.UpdateListingPriceRequest;
import com.kenzie.appserver.controller.model.UpdateListingStatusRequest;
import com.kenzie.appserver.service.ListingService;
import com.kenzie.appserver.service.model.Listing;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        for (int i = 0; i < 200; i++) {
            Listing generatedListing = listingGenerator.generateListing();
            newListings.add(generatedListing);
            listingService.createNewListing(generatedListing);
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
//        assertEquals(newListings.size(), allListingsResponseList.size());
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
        for(ListingResponse listingResponse : openListingsResponseList) {
            assertThat(listingResponse.getListingStatus()).isEqualToIgnoringCase("For Sale");
        }

    }

    @Test
    public void getParameterizedListings_byPriceLessThan_givenPrice_successful() throws Exception {
        // GIVEN
        // Already created newListings in setup()
        Integer price = 400000;
        String urlTemplate = "/listing/query?squareFootage=0&price=" + price.toString() + "&numBedrooms=0&lotSize=0.0";
        // WHEN
        MvcResult byPriceLessThanListingsResult =  mvc.perform(get(urlTemplate)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        // THEN
        String response = byPriceLessThanListingsResult.getResponse().getContentAsString();
        List<ListingResponse> byPriceLessThanListingsResponseList = mapper.readValue(response, new TypeReference<List<ListingResponse>>() {});
        for (ListingResponse listingResponse : byPriceLessThanListingsResponseList) {
            assertThat(listingResponse.getPrice()).isLessThan(price);
        }

    }

    @Test
    public void getParameterizedListings_bySquareFootageMoreThan_givenSquareFootage_successful() throws Exception {
        // GIVEN
        // Already created newListings in setup()
        Integer squareFootage = 2000;
        String urlTemplate = "/listing/query?squareFootage=" + squareFootage.toString() + "&price=0&numBedrooms=0&lotSize=0.0";

        // WHEN
        MvcResult bySquareFootageMoreThanListingsResult =  mvc.perform(get(urlTemplate)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        // THEN
        String response = bySquareFootageMoreThanListingsResult.getResponse().getContentAsString();
        List<ListingResponse> bySquareFootageMoreThanListingsResponseList = mapper.readValue(response, new TypeReference<List<ListingResponse>>() {});
        for (ListingResponse listingResponse : bySquareFootageMoreThanListingsResponseList) {
            assertThat(listingResponse.getSquareFootage()).isGreaterThanOrEqualTo(squareFootage);
        }

    }

    @Test
    public void getParameterizedListings_byNumOfBedroomsEqualsOrGreater_givenNumOfBedrooms_successful() throws Exception {
        // GIVEN
        // Already created newListings in setup()
        Integer numBedrooms = 3;
        String urlTemplate = "/listing/query?squareFootage=0&price=0&numBedrooms=" + numBedrooms.toString() + "&lotSize=0.0";
        // WHEN
        MvcResult byNumOfBedroomsEqualsListingsResult =  mvc.perform(get(urlTemplate)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        // THEN
        String response = byNumOfBedroomsEqualsListingsResult.getResponse().getContentAsString();
        List<ListingResponse> byNumOfBedroomsEqualsOrGreaterListingsResponseList = mapper.readValue(response, new TypeReference<List<ListingResponse>>() {});
        for (ListingResponse listingResponse : byNumOfBedroomsEqualsOrGreaterListingsResponseList) {
            assertThat(listingResponse.getNumBedrooms()).isGreaterThanOrEqualTo(numBedrooms);
        }
    }

    @Test
    public void getParameterizedListings_byLotSizeEqualsOrGreater_givenLotSize_successful() throws Exception {
        // GIVEN
        // Already created newListings in setup()
        Double lotSize = 1.0;
        String urlTemplate = "/listing/query?squareFootage=0&price=0&numBedrooms=0&lotSize=" + lotSize.toString();
        // WHEN
        MvcResult byLotSizeGreaterThen =  mvc.perform(get(urlTemplate)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        // THEN
        String response = byLotSizeGreaterThen.getResponse().getContentAsString();
        List<ListingResponse> byLotSizeGreaterThenListingsResponseList = mapper.readValue(response, new TypeReference<List<ListingResponse>>() {});
        for (ListingResponse listingResponse : byLotSizeGreaterThenListingsResponseList) {
            assertThat(listingResponse.getLotSize()).isGreaterThanOrEqualTo(lotSize);
        }

    }

    @Test
    public void getParameterizedListings_givenAllValidQueryParameters_successful() throws Exception {
        // GIVEN
        // Already created newListings in setup()
        Integer squareFootage = 0;
        Integer price = 100000000;
        Integer numBedrooms = 0;
        Double lotSize = 0.0;
        Double numBathrooms = 0.0;

        String urlTemplate = "/listing/query?squareFootage=" + squareFootage.toString()
                                + "&price=" + price.toString() + "&numBedrooms=" +numBedrooms.toString()
                                + "&numBathrooms=" + numBathrooms.toString() + "&lotSize=" + lotSize.toString();

        // WHEN
        MvcResult parameterizedListingsResult =  mvc.perform(get(urlTemplate)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        // THEN
        String response = parameterizedListingsResult.getResponse().getContentAsString();
        List<ListingResponse> parameterizedListingsResponseList = mapper.readValue(response, new TypeReference<List<ListingResponse>>() {});
        System.out.println("Found: " + parameterizedListingsResponseList.size() + " results");
        for(ListingResponse listingResponse : parameterizedListingsResponseList) {
            assertThat(listingResponse.getSquareFootage()).isGreaterThanOrEqualTo(squareFootage);
            assertThat(listingResponse.getPrice()).isLessThan(price);
            assertThat(listingResponse.getNumBedrooms()).isGreaterThanOrEqualTo(numBedrooms);
            assertThat(listingResponse.getLotSize()).isGreaterThanOrEqualTo(lotSize);
            assertThat(listingResponse.getNumBathrooms()).isGreaterThanOrEqualTo(numBathrooms);
        }

    }

    @Test
    public void getListingById_ReturnsProperListing() throws Exception {
        Listing listing = listingGenerator.generateListing();
        String id = listing.getListingNumber();
        String address = listing.getAddress();
        int price = listing.getPrice();
        int squareFootage = listing.getSquareFootage();
        int numBedrooms = listing.getNumBedrooms();
        double numBathrooms = listing.getNumBathrooms();
        double lotSize = listing.getLotSize();
        String status = listing.getListingStatus();

        Listing persistedListing = listingService.createNewListing(listing);
        newListings.add(persistedListing);

        // WHEN
        mvc.perform(get("/listing/{listingNumber}", persistedListing.getListingNumber())
                        .accept(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(jsonPath("listingNumber")
                        .value(is(id)))
                .andExpect(jsonPath("address")
                        .value(is(address)))
                .andExpect(jsonPath("price")
                        .value(is(price)))
                .andExpect(jsonPath("squareFootage")
                        .value(is(squareFootage)))
                .andExpect(jsonPath("numBedrooms")
                        .value(is(numBedrooms)))
                .andExpect(jsonPath("numBathrooms")
                        .value(is(numBathrooms)))
                .andExpect(jsonPath("lotSize")
                        .value(is(lotSize)))
                .andExpect(jsonPath("listingStatus")
                        .value(is(status)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateListingPrice() throws Exception {
        Listing listing = listingGenerator.generateListing();
        String id = listing.getListingNumber();
        String address = listing.getAddress();
        int price = listing.getPrice();
        int squareFootage = listing.getSquareFootage();
        int numBedrooms = listing.getNumBedrooms();
        double numBathrooms = listing.getNumBathrooms();
        double lotSize = listing.getLotSize();
        String status = listing.getListingStatus();

        Listing persistedListing = listingService.createNewListing(listing);
        newListings.add(persistedListing);

        int updatedPrice = price + 100;

        UpdateListingPriceRequest updatePricingRequest = new UpdateListingPriceRequest();
        updatePricingRequest.setListingNumber(id);
        updatePricingRequest.setPrice(updatedPrice);
        updatePricingRequest.setListingStatus(status);
        updatePricingRequest.setAddress(address);
        updatePricingRequest.setLotSize(lotSize);
        updatePricingRequest.setNumBathrooms(numBathrooms);
        updatePricingRequest.setNumBedrooms(numBedrooms);
        updatePricingRequest.setSquareFootage(squareFootage);


        mapper.registerModule(new JavaTimeModule());

        // WHEN
        mvc.perform(put("/listing/price/{price}", updatedPrice)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatePricingRequest)))
                .andExpect(status().isOk());

        mvc.perform(get("/listing/{listingNumber}", persistedListing.getListingNumber())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("price")
                        .value(is(updatedPrice)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateListingStatus() throws Exception {
        String id = UUID.randomUUID().toString();
        String listingStatus = "For Sale";
        String address = "123 Test Street, Test city, Test State, 00000";

        Listing listing = new Listing(id, address,
                1000, 750000, 4, 2.5, 0.7, "Under Contract");

        Listing persistedListing = listingService.createNewListing(listing);
        newListings.add(persistedListing);

        UpdateListingStatusRequest updateStatusRequest = new UpdateListingStatusRequest();
        updateStatusRequest.setListingNumber(id);
        updateStatusRequest.setListingStatus(listingStatus);
        updateStatusRequest.setAddress(address);
        updateStatusRequest.setPrice(750000);
        updateStatusRequest.setLotSize(0.7);
        updateStatusRequest.setNumBathrooms(2.5);
        updateStatusRequest.setNumBedrooms(4);
        updateStatusRequest.setSquareFootage(1000);


        mapper.registerModule(new JavaTimeModule());

        // WHEN
        mvc.perform(put("/listing/listingStatus/{listingStatus}", listingStatus)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateStatusRequest)));


        mvc.perform(get("/listing/{listingNumber}", persistedListing.getListingNumber())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("listingStatus")
                        .value(is(listingStatus)))
                .andExpect(status().isOk());
    }
}

