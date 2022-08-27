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

import com.abcbank.backend.entity.Branch;
import com.abcbank.backend.repository.BranchRepository;

/*
 *tharinda.jayamal@gmail.com
 */
@Service
public class BranchService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private BranchRepository branchRepository;

	public Branch addUpdateBranch(Branch branch) {

		if (StringUtils.isEmpty(branch.getId())) {
			branch.setGuid(getNext());
		}
		return branchRepository.save(branch);

	}

	public Page<Branch> getAllBranches(int page) {

		log.debug("Fetching branches.");
		return branchRepository.findAll(PageRequest.of(page, 12));
	}

	public Branch getBranchById(String id) {

		log.debug("Fetching branch for :" + id);
		if (branchRepository.findById(id).isPresent()) {
			return branchRepository.findById(id).get();
		} else {
			return null;
		}
	}

	public void deleteBranch(String id) {

		log.debug("Deleting branch : " + id);
		branchRepository.deleteById(id);
	}

	private long getNext() {
		Branch branch = branchRepository.findTopByOrderByIdDesc();
		long lastId = 0;
		if (branch != null) {
			lastId = branch.getGuid();
		}
		return lastId + 1;
	}
	
	public List<Branch> getAllBranches() {

		log.debug("Fetching all branches.");
		return branchRepository.findAll();
	}
	
	public Branch getBranchByName(String name) {

		log.debug("Fetching department by name "+name);
		return branchRepository.findByName(name);
	}

}
