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

import com.abcbank.backend.entity.Role;
import com.abcbank.backend.repository.RoleRepository;

/*
 *tharinda.jayamal@gmail.com
 */
@Service
public class RoleService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RoleRepository roleRepository;

	public Role addUpdateRole(Role role) {

		if (StringUtils.isEmpty(role.getId())) {
			role.setGuid(getNext());
		}
		return roleRepository.save(role);

	}

	public Page<Role> getAllRoles(int page) {

		log.debug("Fetching roles.");
		return roleRepository.findAll(PageRequest.of(page, 12));
	}

	public Role getRoleById(String id) {

		log.debug("Fetching role for :" + id);
		if (roleRepository.findById(id).isPresent()) {
			return roleRepository.findById(id).get();
		} else {
			return null;
		}
	}

	public void deleteRole(String id) {

		log.debug("Deleting role : " + id);
		roleRepository.deleteById(id);
	}

	private long getNext() {
		Role role = roleRepository.findTopByOrderByIdDesc();
		long lastId = 0;
		if (role != null) {
			lastId = role.getGuid();
		}
		return lastId + 1;
	}
	
	public List<Role> getAllRoles() {

		log.debug("Fetching all roles.");
		return roleRepository.findAll();
	}
	
	public Role getAllRoleByName(String name) {

		log.debug("Fetching department by name "+name);
		return roleRepository.findByName(name);
	}

}
