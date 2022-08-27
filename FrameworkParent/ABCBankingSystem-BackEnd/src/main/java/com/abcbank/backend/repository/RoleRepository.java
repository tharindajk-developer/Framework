/*
 *ABC Bank 2022
 */
package com.abcbank.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.abcbank.backend.entity.Role;

/*
 *tharinda.jayamal@gmail.com
 */
@Repository
public interface RoleRepository extends MongoRepository<Role, String>{

	Role findTopByOrderByIdDesc();
	
	Role findByName(String name);
}
