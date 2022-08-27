/*
 *ABC Bank 2022
 */
package com.abcbank.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.abcbank.backend.entity.Branch;

/*
 *tharinda.jayamal@gmail.com
 */
@Repository
public interface BranchRepository extends MongoRepository<Branch, String>{

	Branch findTopByOrderByIdDesc();
	
	Branch findByName(String name);
}
