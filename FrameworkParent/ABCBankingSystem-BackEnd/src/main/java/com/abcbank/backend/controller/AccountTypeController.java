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

import com.abcbank.backend.entity.AccountType;
import com.abcbank.backend.service.AccountTypeService;
import com.abcbank.backend.util.ResponseModel;

/*
 *tharinda.jayamal@gmail.com
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/accounttype")
public class AccountTypeController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AccountTypeService accountTypeService;

	@PostMapping("/create")
	public ResponseEntity<ResponseModel> createAccountType(HttpServletRequest httpServletRequestWrapper,
			@RequestBody AccountType accountType) {

		log.debug("Post: /accountType/create");

		try {
			log.debug("Adding accountType..");
			accountType = accountTypeService.addUpdateAccountType(accountType);
			log.debug("AccountType added succesfully " + accountType.getId());
			ResponseModel responseModel = new ResponseModel("Succesfully added the accountType " + accountType.getGuid(), "200");
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.OK);
		} catch (Exception e) {
			log.error("An error occured while adding the accountType. Please try again." + accountType.getId());
			ResponseModel responseModel = new ResponseModel(
					"An error occured while adding the accountType. Please try again." + accountType.getGuid(), "500");
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/search/{page}")
	public Page<AccountType> searchOrders(HttpServletRequestWrapper httpServletRequestWrapper,
			@PathVariable(value = "page") int page) {

		log.info("Post: /accountType/search");
		return accountTypeService.getAllAccountTypes(page);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<ResponseModel> deleteAccountType(HttpServletRequest httpServletRequestWrapper,
			@RequestParam(value = "id", required = false) String id) {

		log.debug("Delete: /accountType/delete");
		try {
			log.debug("Deleting accountType.." + id);
			accountTypeService.deleteAccountType(id);
			log.debug("AccountType deleted succesfully " + id);
			ResponseModel responseModel = new ResponseModel("Succesfully deleted the accountType", "200");
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.OK);
		} catch (Exception e) {
			log.error("An error occured while deleting the accountType. Please try again." + id);
			ResponseModel responseModel = new ResponseModel(
					"An error occured while deleting the accountType. Please try again.", "500");
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/all")
	public List<AccountType> getAccountTypes(HttpServletRequest request) {

		log.info("Get: /accountType/all");
		return accountTypeService.getAllAccountTypes();
	}
	
	@GetMapping("/getbyname")
	public AccountType getAccountType(HttpServletRequest request, @RequestParam(value = "name", required = false) String name) {

		log.info("Get: /accountType/getbyname");
		return accountTypeService.getAllAccountTypeByName(name);
	}
}
