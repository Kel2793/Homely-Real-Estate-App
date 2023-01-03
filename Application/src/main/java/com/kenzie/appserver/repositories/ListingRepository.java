package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.ListingRecord;
import com.kenzie.appserver.service.model.Listing;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface ListingRepository extends CrudRepository<ListingRecord, String> {
    public List<ListingRecord> findBySquareFootageLessThan(Integer squareFootage);
    public List<ListingRecord> findByPriceLessThan(Integer price);
    public List<ListingRecord> findByNumBedroomsEquals(Integer numBedrooms);
    public List<ListingRecord> findByLotSizeEquals(Double lotSize);
}
