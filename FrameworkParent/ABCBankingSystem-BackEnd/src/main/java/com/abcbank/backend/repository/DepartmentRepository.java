/*
 *ABC Bank 2022
 */
package com.abcbank.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.abcbank.backend.entity.Department;

/*
 *tharinda.jayamal@gmail.com
 */
@Repository
public interface DepartmentRepository extends
		MongoRepository<Department, String> {

	Department findTopByOrderByIdDesc();

	Department findByName(String name);
}
