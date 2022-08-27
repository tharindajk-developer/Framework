/*
 *ABC Bank 2022
 */
package com.abcbank.backend.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.abcbank.backend.entity.Login;
import com.abcbank.backend.entity.Transaction;
import com.abcbank.backend.entity.User;
import com.abcbank.backend.model.ChangePassword;
import com.abcbank.backend.repository.UserRepository;
import com.abcbank.backend.util.RequestModel;

/*
 *tharinda.jayamal@gmail.com
 */
@Service
public class UserService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailService emailService;

	public User addUpdateUser(User user) {

		String password = null;
		user.setUserName(user.getUserName().trim());
		if (StringUtils.isEmpty(user.getId())) {
			user.setGuid(getNext());
		} else {
			User userLogin = userRepository.findById(user.getId()).get();
			user.setLogin(userLogin.getLogin());
		}
		if (!StringUtils.isEmpty(user.getCreatedBy())) {
			user.setCreatedDate(new Date());
		}
		if (!StringUtils.isEmpty(user.getUpdatedBy())) {
			user.setUpdatedDate(new Date());
		}

		if (StringUtils.isEmpty(user.getId())) {
			Login login = new Login();
			login.setCreatedDate(new Date());
			password = randomPasswordGenerator();
			login.setPassword(hashPassword(password));
			user.setLogin(login);

			if (user.getStaff() != null
					&& StringUtils.isEmpty(user.getStaff().getEmpId())) {
				user.getStaff().setEmpId("EMP" + randomEmpIdGenerator());
			}

			if (user.getIndividualCustomer() != null
					&& user.getIndividualCustomer().getAccount() != null
					&& StringUtils.isEmpty(user.getIndividualCustomer()
							.getAccount().getAccountNo())) {
				user.getIndividualCustomer().getAccount()
						.setAccountNo(randomAccGenerator());
				user.getIndividualCustomer().getAccount()
						.setAmount(new BigDecimal("0.00"));
			}

			if (user.getCorporateCustomer() != null
					&& user.getCorporateCustomer().getAccount() != null
					&& StringUtils.isEmpty(user.getCorporateCustomer()
							.getAccount().getAccountNo())) {
				user.getCorporateCustomer().getAccount()
						.setAccountNo(randomAccGenerator());
				user.getCorporateCustomer().getAccount()
						.setAmount(new BigDecimal("0.00"));
			}
		} else {
			if (user.getIndividualCustomer() != null
					&& user.getIndividualCustomer().getAccount() != null
					&& user.getIndividualCustomer().getAccount().getAccountNo() != null) {

				User u = userRepository.findById(user.getId()).get();

				if (u.getIndividualCustomer().getAccount().getAmount() != null
						&& user.getIndividualCustomer().getAccount()
								.getAmount() != null
						&& u.getIndividualCustomer().getAccount().getAmount()
								.doubleValue() != user.getIndividualCustomer()
								.getAccount().getAmount().doubleValue()) {

					if (u.getIndividualCustomer().getAccount().getAmount()
							.doubleValue() > user.getIndividualCustomer()
							.getAccount().getAmount().doubleValue()) {
						Transaction transaction = new Transaction();
						transaction.setTransactionAmount(new BigDecimal(u
								.getIndividualCustomer().getAccount()
								.getAmount().doubleValue()
								- user.getIndividualCustomer().getAccount()
										.getAmount().doubleValue()));
						transaction.setTransactionDate(new Date());
						transaction.setTransactionRef("TFR"
								+ randomTFRGenerator());
						transaction.setTransactionType("Credit");

						if (u.getIndividualCustomer().getAccount()
								.getTransactions() != null) {
							List<Transaction> transactions = u
									.getIndividualCustomer().getAccount()
									.getTransactions();
							transactions.add(transaction);
							user.getIndividualCustomer().getAccount()
									.setTransactions(transactions);
						} else {
							List<Transaction> transactions = new ArrayList<>();
							transactions.add(transaction);
							user.getIndividualCustomer().getAccount()
									.setTransactions(transactions);
						}
					} else {
						Transaction transaction = new Transaction();
						transaction.setTransactionAmount(new BigDecimal(user
								.getIndividualCustomer().getAccount()
								.getAmount().doubleValue()
								- u.getIndividualCustomer().getAccount()
										.getAmount().doubleValue()));
						transaction.setTransactionDate(new Date());
						transaction.setTransactionRef("TFR"
								+ randomTFRGenerator());
						transaction.setTransactionType("Debit");

						if (u.getIndividualCustomer().getAccount()
								.getTransactions() != null) {
							List<Transaction> transactions = u
									.getIndividualCustomer().getAccount()
									.getTransactions();
							transactions.add(transaction);
							user.getIndividualCustomer().getAccount()
									.setTransactions(transactions);
						} else {
							List<Transaction> transactions = new ArrayList<>();
							transactions.add(transaction);
							user.getIndividualCustomer().getAccount()
									.setTransactions(transactions);
						}
					}
				}
			}

			if (user.getCorporateCustomer() != null
					&& user.getCorporateCustomer().getAccount() != null
					&& user.getCorporateCustomer().getAccount().getAccountNo() != null) {

				User u = userRepository.findById(user.getId()).get();

				if (u.getCorporateCustomer().getAccount().getAmount() != null
						&& user.getCorporateCustomer().getAccount().getAmount() != null
						&& u.getCorporateCustomer().getAccount().getAmount()
								.doubleValue() != user.getCorporateCustomer()
								.getAccount().getAmount().doubleValue()) {

					if (u.getCorporateCustomer().getAccount().getAmount()
							.doubleValue() > user.getCorporateCustomer()
							.getAccount().getAmount().doubleValue()) {
						Transaction transaction = new Transaction();
						transaction.setTransactionAmount(new BigDecimal(u
								.getCorporateCustomer().getAccount()
								.getAmount().doubleValue()
								- user.getCorporateCustomer().getAccount()
										.getAmount().doubleValue()));
						transaction.setTransactionDate(new Date());
						transaction.setTransactionRef("TFR"
								+ randomTFRGenerator());
						transaction.setTransactionType("Credit");

						if (u.getCorporateCustomer().getAccount()
								.getTransactions() != null) {
							List<Transaction> transactions = u
									.getCorporateCustomer().getAccount()
									.getTransactions();
							transactions.add(transaction);
							user.getCorporateCustomer().getAccount()
									.setTransactions(transactions);
						} else {
							List<Transaction> transactions = new ArrayList<>();
							transactions.add(transaction);
							user.getCorporateCustomer().getAccount()
									.setTransactions(transactions);
						}
					} else {
						Transaction transaction = new Transaction();
						transaction.setTransactionAmount(new BigDecimal(user
								.getCorporateCustomer().getAccount()
								.getAmount().doubleValue()
								- u.getCorporateCustomer().getAccount()
										.getAmount().doubleValue()));
						transaction.setTransactionDate(new Date());
						transaction.setTransactionRef("TFR"
								+ randomTFRGenerator());
						transaction.setTransactionType("Debit");

						if (u.getCorporateCustomer().getAccount()
								.getTransactions() != null) {
							List<Transaction> transactions = u
									.getCorporateCustomer().getAccount()
									.getTransactions();
							transactions.add(transaction);
							user.getCorporateCustomer().getAccount()
									.setTransactions(transactions);
						} else {
							List<Transaction> transactions = new ArrayList<>();
							transactions.add(transaction);
							user.getCorporateCustomer().getAccount()
									.setTransactions(transactions);
						}
					}
				}

			}
		}

		if (!StringUtils.isEmpty(password)) {
			emailService.sendPassword(password, user.getUserName());
		}
		return userRepository.save(user);

	}

	public Page<User> getAllUsers(int page) {

		log.debug("Fetching Users.");
		return userRepository.findAll(PageRequest.of(page, 12));
	}

	public User getUserById(String id) {

		log.debug("Fetching User for :" + id);
		if (userRepository.findById(id).isPresent()) {
			return userRepository.findById(id).get();
		} else {
			return null;
		}
	}

	public void deleteUser(String id) {

		log.debug("Deleting User : " + id);
		userRepository.deleteById(id);
	}

	private long getNext() {
		User user = userRepository.findTopByOrderByIdDesc();
		long lastId = 0;
		if (user != null) {
			lastId = user.getGuid();
		}
		return lastId + 1;
	}

	private String hashPassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt(12));
	}

	private String randomPasswordGenerator() {

		int length = 8;
		boolean useLetters = true;
		boolean useNumbers = false;

		return RandomStringUtils.random(length, useLetters, useNumbers);
	}

	private String randomEmpIdGenerator() {

		int length = 5;
		boolean useLetters = false;
		boolean useNumbers = true;

		return RandomStringUtils.random(length, useLetters, useNumbers);
	}

	public User findByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}

	public boolean checkExistingPassword(ChangePassword changePassword) {

		boolean isValid = false;
		log.debug("Checking passwords.");
		User user = findByUserName(changePassword.getUsername());
		if (user != null) {
			isValid = BCrypt.checkpw(changePassword.getPassword(), user
					.getLogin().getPassword());
		}
		return isValid;
	}

	public void changePassword(ChangePassword changePassword) {

		log.debug("Changing passwords.");
		User user = findByUserName(changePassword.getUsername());
		if (user != null && user.getLogin() != null) {
			user.getLogin().setPassword(
					hashPassword(changePassword.getNewPassword()));
			userRepository.save(user);
		}
	}

	private String randomAccGenerator() {

		int length = 12;
		boolean useLetters = false;
		boolean useNumbers = true;

		return RandomStringUtils.random(length, useLetters, useNumbers);
	}

	public void inactivateUser(String id) {

		log.debug("Inactivate account : " + id);

		if (userRepository.existsById(id)) {
			User user = userRepository.findById(id).get();
			user.setActive(false);
			userRepository.save(user);
		}
	}

	public Page<User> getAllUsers(RequestModel req, int page) {

		log.debug("Fetching Users.");

		if (req != null && !StringUtils.isEmpty(req.getName())) {
			return userRepository
					.findByIndividualCustomer_Account_AccountNoLike(
							req.getName(), PageRequest.of(page, 12));
		} else {
			return userRepository.findAll(PageRequest.of(page, 12));
		}
	}

	private String randomTFRGenerator() {

		int length = 8;
		boolean useLetters = false;
		boolean useNumbers = true;

		return RandomStringUtils.random(length, useLetters, useNumbers);
	}

	public User findByAccountNumber(String acc) {

		log.debug("Fetching Users.");

		User u = null;
		if (acc != null && !StringUtils.isEmpty(acc)) {
			u = userRepository.findByIndividualCustomer_Account_AccountNo(acc);

			if (u == null) {
				u = userRepository
						.findByCorporateCustomer_Account_AccountNo(acc);
			}
		}

		return u;
	}

	public User transfer(String id, Transaction transaction) {

		boolean isBeneficiarySaved = false;

		log.debug("Updating Accounts.");

		User usr = userRepository.findById(id).get();

		User u = null;
		if (transaction.getToAcc() != null
				&& !StringUtils.isEmpty(transaction.getToAcc())) {
			u = userRepository
					.findByIndividualCustomer_Account_AccountNo(transaction
							.getToAcc());

			if (u == null) {
				u = userRepository
						.findByCorporateCustomer_Account_AccountNo(transaction
								.getToAcc());
			}
		}

		if (u != null && u.getCorporateCustomer() != null) {
			Transaction t = new Transaction();
			t.setTransactionAmount(transaction.getTransactionAmount());
			t.setTransactionDate(new Date());
			t.setTransactionRef("TFR" + randomTFRGenerator());
			t.setTransactionType("Credit");

			if (usr.getCorporateCustomer() != null) {
				t.setFromAcc(usr.getCorporateCustomer().getAccount()
						.getAccountNo());
			} else {
				t.setFromAcc(usr.getIndividualCustomer().getAccount()
						.getAccountNo());
			}
			t.setToAcc(transaction.getToAcc());

			List<Transaction> transactions = u.getCorporateCustomer()
					.getAccount().getTransactions();

			if (transactions == null) {
				transactions = new ArrayList<>();
			}
			transactions.add(t);
			u.getCorporateCustomer().getAccount().setTransactions(transactions);

			u.getCorporateCustomer()
					.getAccount()
					.setAmount(
							u.getCorporateCustomer().getAccount().getAmount()
									.add(transaction.getTransactionAmount()));

			userRepository.save(u);

			isBeneficiarySaved = true;

		}
		if (u != null && u.getIndividualCustomer() != null) {
			Transaction t = new Transaction();
			t.setTransactionAmount(transaction.getTransactionAmount());
			t.setTransactionDate(new Date());
			t.setTransactionRef("TFR" + randomTFRGenerator());
			t.setTransactionType("Credit");

			if (usr.getCorporateCustomer() != null) {
				t.setFromAcc(usr.getCorporateCustomer().getAccount()
						.getAccountNo());
			} else {
				t.setFromAcc(usr.getIndividualCustomer().getAccount()
						.getAccountNo());
			}
			t.setToAcc(transaction.getToAcc());

			List<Transaction> transactions = u.getIndividualCustomer()
					.getAccount().getTransactions();

			if (transactions == null) {
				transactions = new ArrayList<>();
			}
			transactions.add(t);
			u.getIndividualCustomer().getAccount()
					.setTransactions(transactions);

			u.getIndividualCustomer()
					.getAccount()
					.setAmount(
							u.getIndividualCustomer().getAccount().getAmount()
									.add(transaction.getTransactionAmount()));

			userRepository.save(u);

			isBeneficiarySaved = true;

		}

		if (isBeneficiarySaved) {
			User initiator = userRepository.findById(id).get();
			if (initiator != null && initiator.getIndividualCustomer() != null) {
				Transaction tr = new Transaction();
				tr.setTransactionAmount(transaction.getTransactionAmount());
				tr.setTransactionDate(new Date());
				tr.setTransactionRef("TFR" + randomTFRGenerator());
				tr.setTransactionType("Debit");

				if (initiator.getCorporateCustomer() != null) {
					tr.setFromAcc(initiator.getCorporateCustomer().getAccount()
							.getAccountNo());
				} else {
					tr.setFromAcc(initiator.getIndividualCustomer()
							.getAccount().getAccountNo());
				}
				tr.setToAcc(transaction.getToAcc());

				List<Transaction> transactionsList = initiator
						.getIndividualCustomer().getAccount().getTransactions();

				if (transactionsList == null) {
					transactionsList = new ArrayList<>();
				}
				transactionsList.add(tr);
				initiator.getIndividualCustomer().getAccount()
						.setTransactions(transactionsList);

				initiator
						.getIndividualCustomer()
						.getAccount()
						.setAmount(
								initiator
										.getIndividualCustomer()
										.getAccount()
										.getAmount()
										.subtract(
												transaction
														.getTransactionAmount()));

				userRepository.save(initiator);

			}

			if (initiator != null && initiator.getCorporateCustomer() != null) {
				Transaction tr = new Transaction();
				tr.setTransactionAmount(transaction.getTransactionAmount());
				tr.setTransactionDate(new Date());
				tr.setTransactionRef("TFR" + randomTFRGenerator());
				tr.setTransactionType("Debit");
				if (initiator.getCorporateCustomer() != null) {
					tr.setFromAcc(initiator.getCorporateCustomer().getAccount()
							.getAccountNo());
				} else {
					tr.setFromAcc(initiator.getIndividualCustomer()
							.getAccount().getAccountNo());
				}
				tr.setToAcc(transaction.getToAcc());

				List<Transaction> transactionsList = initiator
						.getCorporateCustomer().getAccount().getTransactions();

				if (transactionsList == null) {
					transactionsList = new ArrayList<>();
				}
				transactionsList.add(tr);
				initiator.getCorporateCustomer().getAccount()
						.setTransactions(transactionsList);

				initiator
						.getCorporateCustomer()
						.getAccount()
						.setAmount(
								initiator
										.getCorporateCustomer()
										.getAccount()
										.getAmount()
										.subtract(
												transaction
														.getTransactionAmount()));

				userRepository.save(initiator);

			}
		}
		return u;
	}
	
	public List<User> getAllUsers() {
		
		return userRepository.findAll();
	}
}
