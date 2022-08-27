import { Department } from "./Department";
import { Account } from "./Account";

export class IndividualCustomer {

    id: string;
	firstName: string;
	lastName: string;
	dob: Date
	ssn_passport: string;
	gender: string;
	account: Account;
}