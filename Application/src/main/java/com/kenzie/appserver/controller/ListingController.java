package com.kenzie.appserver.controller;


import com.kenzie.appserver.controller.model.ListingCreateRequest;
import com.kenzie.appserver.controller.model.ListingResponse;
import com.kenzie.appserver.controller.model.UpdateListingPriceRequest;
import com.kenzie.appserver.controller.model.UpdateListingStatusRequest;
import com.kenzie.appserver.service.ListingService;
import com.kenzie.appserver.service.model.Listing;
import com.kenzie.appserver.service.model.ListingStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;

@RestController
@RequestMapping("/listing")
public class ListingController {
    private ListingService listingService;

    ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    @GetMapping("/{listingNumber}")
    public ResponseEntity<ListingResponse> searchListingByNumber(@PathVariable("listingNumber") String listingNumber) {

        Listing listing = listingService.findByListingNumber(listingNumber);
        if (listing == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(createListingResponse(listing));
    }

    @PostMapping
    public ResponseEntity<ListingResponse> addNewListing(@RequestBody ListingCreateRequest listingCreateRequest) {

        Listing listing = new Listing(randomUUID().toString(),listingCreateRequest.getAddress(),
                listingCreateRequest.getSquareFootage(),listingCreateRequest.getPrice(),
                listingCreateRequest.getNumBedrooms(), listingCreateRequest.getNumBathrooms(),
                listingCreateRequest.getLotSize(), listingCreateRequest.getListingStatus());

        Listing nullCheck = listingService.createNewListing(listing);
        if (nullCheck == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(createListingResponse(listing));
    }

    @PutMapping("/{price}")
    public ResponseEntity<ListingResponse> updatePrice (@PathVariable("price") int price, @RequestBody UpdateListingPriceRequest updateListingPriceRequest) {

        //check for proper price input
        //dont do anything if price is less then 1
        if(updateListingPriceRequest.getPrice() < 1) {
            return ResponseEntity.badRequest().build();
        }

        listingService.updatePrice(updateListingPriceRequest.getListingNumber(), updateListingPriceRequest.getPrice());
        return (ResponseEntity<ListingResponse>) ResponseEntity.ok();

    }

    @PutMapping("/{listingStatus}")
    public ResponseEntity<ListingResponse> updateStatus (@PathVariable("listingStatus") String listingStatus, @RequestBody UpdateListingStatusRequest updateListingStatusRequest) {

        //check for proper status inputs
        //do not allow to proceed if status is not allowed

        if(!updateListingStatusRequest.getListingStatus().equalsIgnoreCase(ListingStatus.FOR_SALE.label) &&
                !updateListingStatusRequest.getListingStatus().equalsIgnoreCase(ListingStatus.WITHDRAWN.label) &&
                !updateListingStatusRequest.getListingStatus().equalsIgnoreCase(ListingStatus.SOLD.label) &&
                !updateListingStatusRequest.getListingStatus().equalsIgnoreCase(ListingStatus.UNDER_CONTRACT.label)) {
           return ResponseEntity.badRequest().build();
        }

        // else we proceed
        listingService.updateStatus(updateListingStatusRequest.getListingNumber(), updateListingStatusRequest.getListingStatus());
        return (ResponseEntity<ListingResponse>) ResponseEntity.ok();
    }

    @GetMapping
    public ResponseEntity<List<ListingResponse>> getAllListings() {

        List<Listing> listingLists = listingService.findAllListings();

        // If there are no listingLists, then return a 204
        if (listingLists == null ||  listingLists.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Otherwise, convert the List of Listing objects into a List of ListingResponse and return it
        List<ListingResponse> response = listingLists.stream().map(listing -> createListingResponse(listing)).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/allOpen")
    public ResponseEntity<List<ListingResponse>> getAllOpenListings() {

        List<Listing> openListingLists = listingService.findAllOpenListings();

        // If there are no listingLists, then return a 204
        if (openListingLists == null ||  openListingLists.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Otherwise, convert the List of Listing objects into a List of ListingResponse and return it
        List<ListingResponse> response = openListingLists.stream().map(listing -> createListingResponse(listing)).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/query")
    public ResponseEntity<List<ListingResponse>> getParameterizedListings(@RequestParam(name = "squareFootage") int squareFootage,
                                                                          @RequestParam(name = "price") int price,
                                                                          @RequestParam(name = "numBedrooms") int numBedrooms,
                                                                          @RequestParam(name = "numBathrooms") int numBathrooms,
                                                                          @RequestParam(name = "lotSize") double lotSize) {

        List<Listing> parameterizedListings = listingService.findParameterizedListings(squareFootage, price, numBedrooms, numBathrooms, lotSize);

        // If there are no listingLists, then return a 204
        if (parameterizedListings == null ||  parameterizedListings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Otherwise, convert the List of Listing objects into a List of ListingResponse and return it
        List<ListingResponse> response = parameterizedListings.stream().map(listing -> createListingResponse(listing)).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{listingNumber}")
    public ResponseEntity<ListingResponse> deleteListingByNumber(@PathVariable("listingNumber") String listingNumber) {
        listingService.deleteListing(listingNumber);
        return ResponseEntity.noContent().build();
    }

    private ListingResponse createListingResponse(Listing listing) {
        ListingResponse listingResponse = new ListingResponse();
        listingResponse.setListingNumber(listing.getListingNumber());
        listingResponse.setAddress(listing.getAddress());
        listingResponse.setSquareFootage(listing.getSquareFootage());
        listingResponse.setPrice(listing.getPrice());
        listingResponse.setNumBedrooms(listing.getNumBedrooms());
        listingResponse.setNumBathrooms(listing.getNumBathrooms());
        listingResponse.setListingStatus(listing.getListingStatus());
        listingResponse.setLotSize(listing.getLotSize());

        return listingResponse;
    }
}
