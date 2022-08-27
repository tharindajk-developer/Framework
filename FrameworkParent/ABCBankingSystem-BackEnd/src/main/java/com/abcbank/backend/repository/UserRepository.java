/*
 *ABC Bank 2022
 */
package com.abcbank.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.abcbank.backend.entity.User;

/*
 *tharinda.jayamal@gmail.com
 */
public interface UserRepository extends MongoRepository<User, String>{

	User findByUserName(String userName);
	
	User findTopByOrderByIdDesc();
	
	Page<User> findByIndividualCustomer_Account_AccountNoLike(String name, Pageable pageable);
	
	User findByIndividualCustomer_Account_AccountNo(String name);
	
	User findByCorporateCustomer_Account_AccountNo(String name);
}
