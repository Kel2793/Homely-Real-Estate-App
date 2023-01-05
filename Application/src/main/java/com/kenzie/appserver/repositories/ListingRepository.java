package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.ListingRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface ListingRepository extends CrudRepository<ListingRecord, String> {
    public List<ListingRecord> findBySquareFootageGreaterThanEqual(Integer squareFootage);
    public List<ListingRecord> findBySquareFootageGreaterThanEqualAndPriceLessThan(Integer squareFootage, Integer price);
    public List<ListingRecord> findBySquareFootageGreaterThanEqualAndNumBedroomsGreaterThanEqual(Integer squareFootage, Integer numBedrooms);
    public List<ListingRecord> findBySquareFootageGreaterThanEqualAndNumBathroomsGreaterThanEqual(Integer squareFootage, Double numBathrooms);
    public List<ListingRecord> findBySquareFootageGreaterThanEqualAndLotSizeGreaterThanEqual(Integer squareFootage, Double lotSize);
    public List<ListingRecord> findBySquareFootageGreaterThanEqualAndPriceLessThanAndNumBedroomsGreaterThanEqual(Integer squareFootage, Integer price, Integer numBedrooms);
    public List<ListingRecord> findBySquareFootageGreaterThanEqualAndPriceLessThanAndNumBathroomsGreaterThanEqual(Integer squareFootage, Integer price, Double numBathrooms);
    public List<ListingRecord> findBySquareFootageGreaterThanEqualAndPriceLessThanAndLotSizeGreaterThanEqual(Integer squareFootage, Integer price, Double lotSize);
    public List<ListingRecord> findBySquareFootageGreaterThanEqualAndNumBedroomsGreaterThanEqualAndNumBathroomsGreaterThanEqual(Integer squareFootage, Integer numBedrooms, Double numBathrooms);
    public List<ListingRecord> findBySquareFootageGreaterThanEqualAndNumBedroomsGreaterThanEqualAndLotSizeGreaterThanEqual(Integer squareFootage, Integer numBedrooms, Double lotSize);
    public List<ListingRecord> findBySquareFootageGreaterThanEqualAndNumBathroomsGreaterThanEqualAndLotSizeGreaterThanEqual(Integer squareFootage, Double numBathrooms, Double lotSize);
    public List<ListingRecord> findBySquareFootageGreaterThanEqualAndPriceLessThanAndNumBedroomsGreaterThanEqualAndNumBathroomsGreaterThanEqual(Integer squareFootage, Integer price, Integer numBedrooms, Double numBathrooms);
    public List<ListingRecord> findBySquareFootageGreaterThanEqualAndPriceLessThanAndNumBedroomsGreaterThanEqualAndLotSizeGreaterThanEqual(Integer squareFootage, Integer price, Integer numBedrooms, Double lotSize);
    public List<ListingRecord> findBySquareFootageGreaterThanEqualAndPriceLessThanAndNumBathroomsGreaterThanEqualAndLotSizeGreaterThanEqual(Integer squareFootage, Integer price, Double numBathrooms, Double lotSize);
    public List<ListingRecord> findBySquareFootageGreaterThanEqualAndNumBedroomsGreaterThanEqualAndNumBathroomsGreaterThanEqualAndLotSizeGreaterThanEqual(Integer squareFootage, Integer numBedrooms, Double numBathrooms, Double lotSize);
    public List<ListingRecord> findBySquareFootageGreaterThanEqualAndPriceLessThanAndNumBedroomsGreaterThanEqualAndNumBathroomsGreaterThanEqualAndLotSizeGreaterThanEqual(Integer squareFootage, Integer price, Integer numBedrooms, Double numBathrooms, Double lotSize);
    public List<ListingRecord> findByPriceLessThan(Integer price);
    public List<ListingRecord> findByPriceLessThanAndNumBedroomsGreaterThanEqual(Integer price, Integer numBedrooms);
    public List<ListingRecord> findByPriceLessThanAndNumBathroomsGreaterThanEqual(Integer price, Double numBathrooms);
    public List<ListingRecord> findByPriceLessThanAndLotSizeGreaterThanEqual(Integer price, Double lotSize);
    public List<ListingRecord> findByPriceLessThanAndNumBedroomsGreaterThanEqualAndNumBathroomsGreaterThanEqual(Integer price, Integer numBedrooms, Double numBathrooms);
    public List<ListingRecord> findByPriceLessThanAndNumBedroomsGreaterThanEqualAndLotSizeGreaterThanEqual(Integer price, Integer numBedrooms, Double lotSize);
    public List<ListingRecord> findByPriceLessThanAndNumBathroomsGreaterThanEqualAndLotSizeGreaterThanEqual(Integer price, Double numBathrooms, Double lotSize);
    public List<ListingRecord> findByPriceLessThanAndNumBedroomsGreaterThanEqualAndNumBathroomsGreaterThanEqualAndLotSizeGreaterThanEqual(Integer price, Integer numBedrooms, Double numBathrooms, Double lotSize);
    public List<ListingRecord> findByNumBedroomsGreaterThanEqual(Integer numBedrooms);
    public List<ListingRecord> findByNumBedroomsGreaterThanEqualAndNumBathroomsGreaterThanEqual(Integer numBedrooms, Double numBathrooms);
    public List<ListingRecord> findByNumBedroomsGreaterThanEqualAndLotSizeGreaterThanEqual(Integer numBedrooms, Double lotSize);
    public List<ListingRecord> findByNumBedroomsGreaterThanEqualAndNumBathroomsGreaterThanEqualAndLotSizeGreaterThanEqual(Integer numBedrooms, Double numBathrooms, Double lotSize);
    public List<ListingRecord> findByNumBathroomsGreaterThanEqual(Double numBathrooms);
    public List<ListingRecord> findByNumBathroomsGreaterThanEqualAndLotSizeGreaterThanEqual(Double numBathrooms, Double lotSize);
    public List<ListingRecord> findByLotSizeGreaterThanEqual(Double lotSize);

}
