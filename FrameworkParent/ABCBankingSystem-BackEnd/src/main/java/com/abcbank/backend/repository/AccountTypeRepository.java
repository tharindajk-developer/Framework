/*
 *ABC Bank 2022
 */
package com.abcbank.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.abcbank.backend.entity.AccountType;

/*
 *tharinda.jayamal@gmail.com
 */
@Repository
public interface AccountTypeRepository extends MongoRepository<AccountType, String>{

	AccountType findTopByOrderByIdDesc();
	
	AccountType findByName(String name);
}
