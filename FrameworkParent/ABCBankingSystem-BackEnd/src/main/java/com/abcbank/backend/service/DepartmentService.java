/*
 *ABC Bank 2022
 */
package com.abcbank.backend.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.abcbank.backend.entity.Department;
import com.abcbank.backend.repository.DepartmentRepository;

/*
 *tharinda.jayamal@gmail.com
 */
@Service
public class DepartmentService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DepartmentRepository departmentRepository;

	public Department addUpdateDepartment(Department department) {

		if (StringUtils.isEmpty(department.getId())) {
			department.setGuid(getNext());
		}
		return departmentRepository.save(department);

	}

	public Page<Department> getAllDepartments(int page) {

		log.debug("Fetching departments.");
		return departmentRepository.findAll(PageRequest.of(page, 12));
	}

	public Department getDepartmentById(String id) {

		log.debug("Fetching department for :" + id);
		if (departmentRepository.findById(id).isPresent()) {
			return departmentRepository.findById(id).get();
		} else {
			return null;
		}
	}

	public void deleteDepartment(String id) {

		log.debug("Deleting department : " + id);
		departmentRepository.deleteById(id);
	}

	private long getNext() {
		Department department = departmentRepository.findTopByOrderByIdDesc();
		long lastId = 0;
		if (department != null) {
			lastId = department.getGuid();
		}
		return lastId + 1;
	}

	public List<Department> getAllDepartments() {

		log.debug("Fetching all departments.");
		return departmentRepository.findAll();
	}

	public Department getAllDepartmentByName(String name) {

		log.debug("Fetching department by name "+name);
		return departmentRepository.findByName(name);
	}

}
