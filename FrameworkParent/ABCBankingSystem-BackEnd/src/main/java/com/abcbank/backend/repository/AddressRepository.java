/*
 *ABC Bank 2022
 */
package com.abcbank.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.abcbank.backend.entity.Address;

/*
 *tharinda.jayamal@gmail.com
 */
public interface AddressRepository extends MongoRepository<Address, String>{
	
}
