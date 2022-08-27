/*
 *ABC Bank 2022
 */
package com.abcbank.backend.schedule;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.abcbank.backend.entity.User;
import com.abcbank.backend.service.UserService;

/*
 *tharinda.jayamal@gmail.com
 */
@Component
public class InterestCalculate {

	@Autowired
	private UserService userService;

	@Scheduled(cron = "0 0 0 1 1/1 *")
	public void calculateInterest() {

		for (User user : userService.getAllUsers()) {
			if (user.getIndividualCustomer() != null) {
				if (user.getIndividualCustomer().getAccount().getAmount() != null) {
					user.getIndividualCustomer()
							.getAccount()
							.getAmount()
							.add(((user.getIndividualCustomer().getAccount()
									.getAmount().multiply(new BigDecimal(user
									.getIndividualCustomer().getAccount()
									.getAccountType().getInterest())))
									.divide(new BigDecimal(100)))
									.divide(new BigDecimal(12)));
				}
			} else if (user.getCorporateCustomer() != null) {
				if (user.getCorporateCustomer().getAccount().getAmount() != null) {
					user.getCorporateCustomer()
							.getAccount()
							.getAmount()
							.add(((user.getCorporateCustomer().getAccount()
									.getAmount().multiply(new BigDecimal(user
									.getCorporateCustomer().getAccount()
									.getAccountType().getInterest())))
									.divide(new BigDecimal(100)))
									.divide(new BigDecimal(12)));
				}
			}
			
			userService.addUpdateUser(user);
		}
	}
}
