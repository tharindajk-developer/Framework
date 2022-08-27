/*
 *ABC Bank 2022
 */
package com.abcbank.backend.controller;

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

import com.abcbank.backend.entity.Transaction;
import com.abcbank.backend.entity.User;
import com.abcbank.backend.model.ChangePassword;
import com.abcbank.backend.service.UserService;
import com.abcbank.backend.util.RequestModel;
import com.abcbank.backend.util.ResponseModel;

/*
 *tharinda.jayamal@gmail.com
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/user")
public class UserController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;

	@PostMapping("/create")
	public ResponseEntity<ResponseModel> createUser(HttpServletRequest httpServletRequestWrapper,
			@RequestBody User user) {

		log.debug("Post: /user/create");

		try {
			User u = userService.findByUserName(user.getUserName());
			if (u != null && user.getId() == null) {
				ResponseModel responseModel = new ResponseModel(
						"An error occured while adding the user. User name cannot be duplicated.", "500");
				return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				log.debug("Adding user..");
				user = userService.addUpdateUser(user);
				log.debug("User added succesfully " + user.getId());
				ResponseModel responseModel = new ResponseModel("Succesfully added the user " + user.getGuid(), "200");
				return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error("An error occured while adding the user. Please try again." + user.getId());
			ResponseModel responseModel = new ResponseModel(
					"An error occured while adding the user. Please try again." + user.getGuid(), "500");
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/search/{page}")
	public Page<User> searchUsers(HttpServletRequestWrapper httpServletRequestWrapper,
			@PathVariable(value = "page") int page) {

		log.info("Post: /user/search");
		return userService.getAllUsers(page);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<ResponseModel> deleteUser(HttpServletRequest httpServletRequestWrapper,
			@RequestParam(value = "id", required = false) String id) {

		log.debug("Delete: /user/delete");
		try {
			log.debug("Deleting user.." + id);
			userService.deleteUser(id);
			log.debug("User deleted succesfully " + id);
			ResponseModel responseModel = new ResponseModel("Succesfully deleted the user", "200");
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.OK);
		} catch (Exception e) {
			log.error("An error occured while deleting the user. Please try again." + id);
			ResponseModel responseModel = new ResponseModel(
					"An error occured while deleting the user. Please try again.", "500");
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getbyusername")
	public User getUser(HttpServletRequest request, @RequestParam(value = "email", required = false) String email) {

		log.info("Get: /user/getbyusername");
		return userService.findByUserName(email);
	}
	
	@PostMapping("/changepassword")
	public ResponseEntity<ResponseModel> changepassword(HttpServletRequestWrapper httpServletRequestWrapper,
			@RequestBody ChangePassword changePassword) {

		log.info("Post: /user/changepassword");
		
		try {
			if (!userService.checkExistingPassword(changePassword)) {
				ResponseModel responseModel = new ResponseModel(
						"An error occured while changing the password. Current password you entered is invalid.", "500");
				return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				log.debug("Changing password..");
				userService.changePassword(changePassword);
				ResponseModel responseModel = new ResponseModel("Succesfully changed the password.", "200");
				return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error("An error occured while changing the password. Please try again.");
			ResponseModel responseModel = new ResponseModel(
					"An error occured while changing the password. Please try again.", "500");
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/inactive")
	public ResponseEntity<ResponseModel> inactivateUser(HttpServletRequest httpServletRequestWrapper,
			@RequestParam(value = "id", required = false) String id) {

		log.debug("Delete: /user/inactive");
		try {
			log.debug("Inactivating account.." + id);
			userService.inactivateUser(id);
			log.debug("Account inactivated succesfully " + id);
			ResponseModel responseModel = new ResponseModel("Succesfully inactivated the account.", "200");
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.OK);
		} catch (Exception e) {
			log.error("An error occured while inactivating the account. Please try again." + id);
			ResponseModel responseModel = new ResponseModel(
					"An error occured while inactivating the account. Please try again.", "500");
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/search/filter/{page}")
	public Page<User> searchUsersFilter(HttpServletRequestWrapper httpServletRequestWrapper,
			@PathVariable(value = "page") int page, @RequestBody RequestModel requestModel) {

		log.info("Post: /user/search/filter");
		return userService.getAllUsers(requestModel, page);
	}
	
	@PostMapping("/search/username")
	public User getUser(HttpServletRequest request, @RequestBody RequestModel requestModel) {

		log.info("Get: /user/search/username");
		return userService.findByUserName(requestModel.getName());
	}
	
	@PostMapping("/transfer")
	public ResponseEntity<ResponseModel> transfer(HttpServletRequest httpServletRequestWrapper,
			@RequestParam(value = "id", required = false) String id, @RequestBody Transaction transaction) {

		log.debug("Post: /user/transfer");
		try {
			log.debug("Updating account.." + id);
			
			User u = userService.findByAccountNumber(transaction.getToAcc());
			
			if(u == null){
				log.error("No Such Account Available " + transaction.getToAcc());
				ResponseModel responseModel = new ResponseModel("No Such Account Available.", "500");
				return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			userService.transfer(id, transaction);
			log.debug("Account updated succesfully " + id);
			ResponseModel responseModel = new ResponseModel("Succesfully updated the account.", "200");
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.OK);
		} catch (Exception e) {
			log.error("An error occured while updating the account. Please try again." + id);
			ResponseModel responseModel = new ResponseModel(
					"An error occured while updating the account. Please try again.", "500");
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
