import { AccountType } from "./AccountType";
import { Branch } from "./Branch";
import { Transaction } from "./Transaction";

export class Account {

    id: string;
    branch: Branch;
    accountNo: string;
    amount: number;
    openDate: Date;
    accountType: AccountType;
    transactions: Transaction[];
    totalElements : number;
}