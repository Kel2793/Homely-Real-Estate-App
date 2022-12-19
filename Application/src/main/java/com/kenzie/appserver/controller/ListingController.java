package com.kenzie.appserver.controller;


import com.kenzie.appserver.controller.model.ListingCreateRequest;
import com.kenzie.appserver.controller.model.ListingResponse;
import com.kenzie.appserver.controller.model.UpdateListingPriceRequest;
import com.kenzie.appserver.service.ListingService;
import com.kenzie.appserver.service.model.Listing;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/concerts")
public class ListingController {
    private ListingService listingService;

    ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    @GetMapping("/{listingNumber}")
    public ResponseEntity<ListingResponse> searchListingByNumber(@PathVariable("listingNumber") String listingNumber) {
        return null;
//        listingService.findByListingNumber();
    }

    @PostMapping
    public ResponseEntity<ListingResponse> addNewListing(@RequestBody ListingCreateRequest listingCreateRequest) {
        return null;
//        listingService.createNewListing();
//        generate listingNumber upon creation
    }

    @PutMapping
    public ResponseEntity<ListingResponse> updatePrice (@RequestBody UpdateListingPriceRequest updateListingPriceRequest) {
        return null;
//        listingService.updatePrice(updateListingPrice.getListingNumber, updateListingPriceRequest.getPrice);
    }

    @GetMapping
    public ResponseEntity<List<ListingResponse>> getAllListings() {
        return null;
//        listingService.findAllListings();
    }

    @DeleteMapping("/{listingNumber}")
    public ResponseEntity deleteListingByNumber(@PathVariable("listingNumber") String listingNumber) {
        return null;
//        listingService.deleteListing();
    }

    private ListingResponse createListingResponse(Listing listing) {
        ListingResponse listingResponse = new ListingResponse();
        listingResponse.setListingNumber(listing.getListingNumber());
        listingResponse.setAddress(listing.getAddress());
        listingResponse.setSquareFootage(listing.getSquareFootage());
        listingResponse.setPrice(listing.getPrice());
        listingResponse.setNumBedrooms(listing.getNumBedrooms());
        listingResponse.setNumBathrooms(listing.getNumBathrooms());
        listingResponse.setNumRooms(listing.getNumRooms());
        listingResponse.setListingStatus(listing.getListingStatus());
        listingResponse.setSchoolDistrict(listing.getSchoolDistrict());
        listingResponse.setLotSize(listing.getLotSize());
        listingResponse.setBuildingType(listing.getBuildingType());

        return listingResponse;
    }
}
