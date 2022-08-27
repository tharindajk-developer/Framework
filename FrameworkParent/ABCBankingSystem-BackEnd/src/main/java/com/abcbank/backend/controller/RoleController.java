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

import com.abcbank.backend.entity.Role;
import com.abcbank.backend.service.RoleService;
import com.abcbank.backend.util.ResponseModel;

/*
 *tharinda.jayamal@gmail.com
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/role")
public class RoleController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RoleService roleService;

	@PostMapping("/create")
	public ResponseEntity<ResponseModel> createRole(HttpServletRequest httpServletRequestWrapper,
			@RequestBody Role role) {

		log.debug("Post: /role/create");

		try {
			log.debug("Adding role..");
			role = roleService.addUpdateRole(role);
			log.debug("Role added succesfully " + role.getId());
			ResponseModel responseModel = new ResponseModel("Succesfully added the role " + role.getGuid(), "200");
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.OK);
		} catch (Exception e) {
			log.error("An error occured while adding the role. Please try again." + role.getId());
			ResponseModel responseModel = new ResponseModel(
					"An error occured while adding the role. Please try again." + role.getGuid(), "500");
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/search/{page}")
	public Page<Role> searchOrders(HttpServletRequestWrapper httpServletRequestWrapper,
			@PathVariable(value = "page") int page) {

		log.info("Post: /role/search");
		return roleService.getAllRoles(page);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<ResponseModel> deleteRole(HttpServletRequest httpServletRequestWrapper,
			@RequestParam(value = "id", required = false) String id) {

		log.debug("Delete: /role/delete");
		try {
			log.debug("Deleting role.." + id);
			roleService.deleteRole(id);
			log.debug("Role deleted succesfully " + id);
			ResponseModel responseModel = new ResponseModel("Succesfully deleted the role", "200");
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.OK);
		} catch (Exception e) {
			log.error("An error occured while deleting the role. Please try again." + id);
			ResponseModel responseModel = new ResponseModel(
					"An error occured while deleting the role. Please try again.", "500");
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/all")
	public List<Role> getRoles(HttpServletRequest request) {

		log.info("Get: /role/all");
		return roleService.getAllRoles();
	}
	
	@GetMapping("/getbyname")
	public Role getRole(HttpServletRequest request, @RequestParam(value = "name", required = false) String name) {

		log.info("Get: /role/getbyname");
		return roleService.getAllRoleByName(name);
	}
}
