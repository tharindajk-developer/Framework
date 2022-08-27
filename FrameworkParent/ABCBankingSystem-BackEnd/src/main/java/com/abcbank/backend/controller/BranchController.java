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

import com.abcbank.backend.entity.Branch;
import com.abcbank.backend.service.BranchService;
import com.abcbank.backend.util.ResponseModel;

/*
 *tharinda.jayamal@gmail.com
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/branch")
public class BranchController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private BranchService branchService;

	@PostMapping("/create")
	public ResponseEntity<ResponseModel> createBranch(HttpServletRequest httpServletRequestWrapper,
			@RequestBody Branch branch) {

		log.debug("Post: /branch/create");

		try {
			log.debug("Adding branch..");
			branch = branchService.addUpdateBranch(branch);
			log.debug("Branch added succesfully " + branch.getId());
			ResponseModel responseModel = new ResponseModel("Succesfully added the branch " + branch.getGuid(), "200");
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.OK);
		} catch (Exception e) {
			log.error("An error occured while adding the branch. Please try again." + branch.getId());
			ResponseModel responseModel = new ResponseModel(
					"An error occured while adding the branch. Please try again." + branch.getGuid(), "500");
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/search/{page}")
	public Page<Branch> searchOrders(HttpServletRequestWrapper httpServletRequestWrapper,
			@PathVariable(value = "page") int page) {

		log.info("Post: /branch/search");
		return branchService.getAllBranches(page);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<ResponseModel> deleteBranch(HttpServletRequest httpServletRequestWrapper,
			@RequestParam(value = "id", required = false) String id) {

		log.debug("Delete: /branch/delete");
		try {
			log.debug("Deleting branch.." + id);
			branchService.deleteBranch(id);
			log.debug("Branch deleted succesfully " + id);
			ResponseModel responseModel = new ResponseModel("Succesfully deleted the branch", "200");
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.OK);
		} catch (Exception e) {
			log.error("An error occured while deleting the branch. Please try again." + id);
			ResponseModel responseModel = new ResponseModel(
					"An error occured while deleting the branch. Please try again.", "500");
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/all")
	public List<Branch> getBranches(HttpServletRequest request) {

		log.info("Get: /branch/all");
		return branchService.getAllBranches();
	}
	
	@GetMapping("/getbyname")
	public Branch getBranch(HttpServletRequest request, @RequestParam(value = "name", required = false) String name) {

		log.info("Get: /branch/getbyname");
		return branchService.getBranchByName(name);
	}
}
