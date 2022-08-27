import { Component, OnInit } from '@angular/core';
import { User } from '../model/User';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { UserService } from '../services/user.service';
import { AllUsersResponse } from '../model/AllUsersResponse';
import { stringify } from 'querystring';
import { CommonResponse } from '../model/CommonResponse';
import { ConfirmationService, SelectItem } from 'primeng/api';
import { TokenStorageService } from '../auth/token-storage.service';
import { AccountTypeService } from '../services/accounttype.service';
import { BranchService } from '../services/branch.service';
import { RequestModel } from '../model/RequestModel';
import { Transaction } from '../model/Transaction';
import { ReportService } from '../services/report.service';

@Component({
  selector: 'app-user',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.css']
})
export class CustomerComponent implements OnInit {

  constructor(private userService:UserService, private confirmationService: ConfirmationService, private tokenStorage: TokenStorageService, private accountTypeService: AccountTypeService, private branchService: BranchService, private reportService: ReportService) { }  
  
  loaded: boolean;
  errorMessage: string;
  successMessage: string;
  error: boolean;
  success: boolean;
  update: boolean;
  isSelected: boolean;
  accountView: boolean;
  tfrAmount: number;
  toAccount: string;

  user: User= {
    guid: null,
    id: null,
    active: null,
    role: null,
    address: null,
    createdBy: null,
    updatedBy: null,
    userName: null,
    staff: null,
    corporateCustomer: null,
    individualCustomer: null,
  }

  transaction : Transaction={
    id: null,
	  transactionType: null,
	  transactionAmount: null,
  	fromAcc: null,
  	toAcc: null,
	  transactionDate: null,
	  transactionRef: null
  }

  response: CommonResponse= {
    code: null,
    message: null
  }
  
  page: number;
  paginator: boolean = true;
  loading: boolean;
  message: any;

  request: RequestModel={
    name: null
  }
  filterAccountName: string;

  ngOnInit() {

    this.isSelected = false;
    this.loaded = false;
    this.getUser(true);
    this.accountView = false;
    
    this.loaded = true;
  }  
  

  fetchUsersLazy(event) {
    this.loading = true;
    this.page = event.first / 12;

    this.filterAccountName = new URLSearchParams(stringify(event.filters.name)).get("value");

    //this.getAllUsers(false);
  }

  getUser(reset: boolean) {

    this.request.name = this.tokenStorage.getUsername();
    this.userService.findUserByEmail(this.request).subscribe(user => {
      this.user = user;
    });

  }

  viewAccount(user: User){
    this.accountView = true;
    this.user = user;
  }

  transfer(){

    console.log(this.toAccount.length)
    if (this.tfrAmount != null && this.tfrAmount > 0 && this.toAccount != null){
      if(this.user.individualCustomer){
        this.transaction.fromAcc = this.user.individualCustomer.account.accountNo;

        if(this.tfrAmount > this.user.individualCustomer.account.amount){
          this.errorMessage = "Invalid transfer amount!   ";
        }
      }else{
        this.transaction.fromAcc = this.user.corporateCustomer.account.accountNo;
        if(this.tfrAmount > this.user.corporateCustomer.account.amount){
          this.errorMessage = "Invalid transfer amount!   ";
        }
      }
      this.transaction.toAcc = this.toAccount;
      this.transaction.transactionAmount = this.tfrAmount;

      if(this.errorMessage != null && this.errorMessage.trim().length > 0){
        this.error = true;
      }else{
        this.error = false;
      }

      if(!this.error){
        this.userService.transfer(this.user, this.transaction)
          .subscribe(r => {
            this.response = r;
            this.loaded = true;

            this.ngOnInit();
            this.successMessage = "Transaction Success";

            this.success = true;
            this.tfrAmount = null;
            this.toAccount = null;

          }, error => {
            this.errorMessage = error.error.message;          
            this.loaded = true;
            this.error = true;
          });
      }
    }else{
      if (this.tfrAmount == null || this.tfrAmount < 0){
        this.errorMessage = "Invalid transfer amount!   ";
      }

      if (this.toAccount == null){
        this.errorMessage = "   Invalid beneficiery account!";
      }

      if(this.errorMessage != null && this.errorMessage.trim().length > 0){
        this.error = true;
      }else{
        this.error = false;
      }
    }

    this.ngOnInit();
  }

  clear(){
    this.tfrAmount = null;
    this.toAccount = null;
    this.successMessage = null;
    this.errorMessage = null;
  }

  export(){
    this.loaded = false;
    this.reportService.getTransactionReport(this.user.id).subscribe((response)=>{
      console.log(JSON.stringify(response));
      let file = new Blob([response], { type: 'application/pdf' });            
      var fileURL = URL.createObjectURL(file);
      this.loaded = true;
      window.open(fileURL);
    });
  }
  
}


