/*
 *ABC Bank 2022
 */
package com.abcbank.backend.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abcbank.backend.entity.Department;
import com.abcbank.backend.service.DepartmentService;
import com.abcbank.backend.util.ResponseModel;

/*
 *tharinda.jayamal@gmail.com
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/department")
public class DepartmentController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DepartmentService departmentService;

	@PostMapping("/create")
	public ResponseEntity<ResponseModel> createDepartment(HttpServletRequest httpServletRequestWrapper,
			@RequestBody Department department) {

		log.debug("Post: /department/create");

		try {
			log.debug("Adding department..");
			department = departmentService.addUpdateDepartment(department);
			log.debug("Department added succesfully " + department.getId());
			ResponseModel responseModel = new ResponseModel("Succesfully added the department " + department.getGuid(), "200");
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.OK);
		} catch (Exception e) {
			log.error("An error occured while adding the department. Please try again." + department.getId());
			ResponseModel responseModel = new ResponseModel(
					"An error occured while adding the department. Please try again." + department.getGuid(), "500");
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/search/{page}")
	public Page<Department> searchDepartments(HttpServletRequestWrapper httpServletRequestWrapper,
			@PathVariable(value = "page") int page) {

		log.info("Post: /department/search");
		return departmentService.getAllDepartments(page);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<ResponseModel> deleteDepartment(HttpServletRequest httpServletRequestWrapper,
			@RequestParam(value = "id", required = false) String id) {

		log.debug("Delete: /department/delete");
		try {
			log.debug("Deleting department.." + id);
			departmentService.deleteDepartment(id);
			log.debug("Department deleted succesfully " + id);
			ResponseModel responseModel = new ResponseModel("Succesfully deleted the department", "200");
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.OK);
		} catch (Exception e) {
			log.error("An error occured while deleting the department. Please try again." + id);
			ResponseModel responseModel = new ResponseModel(
					"An error occured while deleting the department. Please try again.", "500");
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/all")
	public List<Department> getDepartments(HttpServletRequest request) {

		log.info("Get: /department/all");
		return departmentService.getAllDepartments();
	}
	
	@GetMapping("/getbyname")
	public Department getDepartment(HttpServletRequest request, @RequestParam(value = "name", required = false) String name) {

		log.info("Get: /department/getbyname");
		return departmentService.getAllDepartmentByName(name);
	}
}
