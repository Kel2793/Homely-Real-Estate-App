package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.ListingRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface ListingRepository extends CrudRepository<ListingRecord, String> {
}
