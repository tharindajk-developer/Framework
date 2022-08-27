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

import com.abcbank.backend.entity.AccountType;
import com.abcbank.backend.repository.AccountTypeRepository;

/*
 *tharinda.jayamal@gmail.com
 */
@Service
public class AccountTypeService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AccountTypeRepository accountTypeRepository;

	public AccountType addUpdateAccountType(AccountType accountType) {

		if (StringUtils.isEmpty(accountType.getId())) {
			accountType.setGuid(getNext());
		}
		return accountTypeRepository.save(accountType);

	}

	public Page<AccountType> getAllAccountTypes(int page) {

		log.debug("Fetching accountTypes.");
		return accountTypeRepository.findAll(PageRequest.of(page, 12));
	}

	public AccountType getAccountTypeById(String id) {

		log.debug("Fetching accountType for :" + id);
		if (accountTypeRepository.findById(id).isPresent()) {
			return accountTypeRepository.findById(id).get();
		} else {
			return null;
		}
	}

	public void deleteAccountType(String id) {

		log.debug("Deleting accountType : " + id);
		accountTypeRepository.deleteById(id);
	}

	private long getNext() {
		AccountType accountType = accountTypeRepository.findTopByOrderByIdDesc();
		long lastId = 0;
		if (accountType != null) {
			lastId = accountType.getGuid();
		}
		return lastId + 1;
	}
	
	public List<AccountType> getAllAccountTypes() {

		log.debug("Fetching all accountTypes.");
		return accountTypeRepository.findAll();
	}
	
	public AccountType getAllAccountTypeByName(String name) {

		log.debug("Fetching department by name "+name);
		return accountTypeRepository.findByName(name);
	}

}
