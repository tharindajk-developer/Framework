import { Component, OnInit } from '@angular/core';
import { User } from '../model/User';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { UserService } from '../services/user.service';
import { AllUsersResponse } from '../model/AllUsersResponse';
import { stringify } from 'querystring';
import { CommonResponse } from '../model/CommonResponse';
import { ConfirmationService, SelectItem } from 'primeng/api';
import { TokenStorageService } from '../auth/token-storage.service';
import { Department } from '../model/Department';
import { DepartmentService } from '../services/department.service';
import { Role } from '../model/Role';
import { RoleService } from '../services/role.service';
import { Address } from '../model/Address';
import { environment } from 'src/environments/environment';
import { Staff } from '../model/Staff';
import { AccountTypeService } from '../services/accounttype.service';
import { IndividualCustomer } from '../model/IndividualCustomer';
import { AccountType } from '../model/AccountType';
import { Account } from '../model/Account';
import { Branch } from '../model/Branch';
import { BranchService } from '../services/branch.service';
import { RequestModel } from '../model/RequestModel';

@Component({
  selector: 'app-user',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit {

  constructor(private userService:UserService, private confirmationService: ConfirmationService, private tokenStorage: TokenStorageService, private accountTypeService: AccountTypeService, private branchService: BranchService) { }  
  
  loaded: boolean;
  errorMessage: string;
  successMessage: string;
  error: boolean;
  success: boolean;
  update: boolean;
  isSelected: boolean;
  accountView: boolean;

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

  address: Address={
    id: null,
    line1: null,
    line2: null,
    city: null
  }

  individualCustomer: IndividualCustomer={
    id: null,
	  firstName: null,
	  lastName: null,
	  dob: null,
	  ssn_passport: null,
	  gender: null,
	  account: null,
  }

  account: Account={
    id: null,
    branch: null,
    accountNo: null,
    amount: null,
    openDate: null,
    accountType: null,
    transactions: null,
    totalElements: null
  }

  response: CommonResponse= {
    code: null,
    message: null
  }

  allUsersResponse : AllUsersResponse;
  filterUserName: string;
  
  page: number;
  paginator: boolean = true;
  loading: boolean;
  message: any;

  accountTypeSelectItems: Array<AccountType>;
  accountTypeSelectItem: AccountType;
  accountType: AccountType;

  branchSelectItems: Array<Branch>;
  branchSelectItem: Branch;
  branch: Branch;

  genderSelectItem: string;

  genderSelectItems: Array<string> = ['Male', 'Female', 'Prefer Not to Say'];

  request: RequestModel={
    name: null
  }
  filterAccountName: string;

  saveWithdrawAmount: number;

  ngOnInit() {


    this.accountTypeService.getAllAccountTypes()
      .subscribe(accountTypeSelectItems => {
        this.accountTypeSelectItems = accountTypeSelectItems;
    });

    this.branchService.getAllBranches()
      .subscribe(branchSelectItems => {
        this.branchSelectItems = branchSelectItems;
    });

    this.isSelected = false;
    this.loaded = false;
    this.getAllUsers(true);
    this.accountView = false;
    
    this.loaded = true;
  }  
  
  save(){  

    this.errorMessage = "";
    this.successMessage = "";
    this.error = false;
    this.success = false;
    
    this.validate();

    
    if (!this.error) {
      this.loaded = false;
      
      if(!this.update){
        this.user.createdBy = this.tokenStorage.getUsername();
        this.user.active = true;
      }
      if(this.individualCustomer){
        this.user.individualCustomer = this.individualCustomer;
      }    
      if(!this.user.individualCustomer.account){
        let date: Date = new Date();  
        this.account.openDate = date;
        this.user.individualCustomer.account = this.account;
      } 
      if(this.accountTypeSelectItem){
        this.user.individualCustomer.account.accountType = this.accountTypeSelectItem;
      }      
      if(this.branchSelectItem){
        this.user.individualCustomer.account.branch = this.branchSelectItem;
      }
      if(this.address){
        this.user.address = this.address;
      }
      if(this.genderSelectItem){
        this.individualCustomer.gender = this.genderSelectItem;
      }

      console.log(JSON.stringify(this.user));
      this.userService.createUser(this.user)
        .subscribe(r => {
          this.response = r;
          this.loaded = true;

          if(this.user.id != null && this.user.id.length > 0){
            this.successMessage = "Account updated successfully.";
          }else{
            this.successMessage = "Account created successfully.";
          }
          
          this.success = true;
          this.clear();
        }, error => {
          this.errorMessage = error.error.message;          
          this.loaded = true;
          this.error = true;
        });
    }else if(this.errorMessage != null && this.errorMessage.trim().length > 0){
      this.errorMessage = this.errorMessage;
      return
    }
  }

  validate(){

    if (this.individualCustomer.firstName == null || this.individualCustomer.firstName.length <= 0){
      this.errorMessage = this.errorMessage + "First Name ";
    }

    if (this.individualCustomer.lastName == null || this.individualCustomer.lastName.length <= 0){
      if(this.errorMessage != null && this.errorMessage.length > 0){
        this.errorMessage = this.errorMessage + " / ";
      }
      this.errorMessage = this.errorMessage + "Last Name ";
    }

    if (this.individualCustomer.ssn_passport == null || this.individualCustomer.ssn_passport.length <= 0){
      if(this.errorMessage != null && this.errorMessage.length > 0){
        this.errorMessage = this.errorMessage + " / ";
      }
      this.errorMessage = this.errorMessage + "SSN/Passport ";
    }

    if (this.genderSelectItem == null){
      if(this.errorMessage != null && this.errorMessage.length > 0){
        this.errorMessage = this.errorMessage + " / ";
      }
      this.errorMessage = this.errorMessage + "Gender ";
    }

    if (this.user.userName == null || this.user.userName.length <= 0){
      if(this.errorMessage != null && this.errorMessage.length > 0){
        this.errorMessage = this.errorMessage + " / ";
      }
      this.errorMessage = this.errorMessage + "Username ";
    }

    if (this.accountTypeSelectItem == null){
      if(this.errorMessage != null && this.errorMessage.length > 0){
        this.errorMessage = this.errorMessage + " / ";
      }
      this.errorMessage = this.errorMessage + "Account Type ";
    }

    if (this.address.line1 == null || this.address.line1.length <= 0){
      if(this.errorMessage != null && this.errorMessage.length > 0){
        this.errorMessage = this.errorMessage + " / ";
      }
      this.errorMessage = this.errorMessage + "Address Line 1 ";
    }

    if (this.address.line2 == null || this.address.line2.length <= 0){
      if(this.errorMessage != null && this.errorMessage.length > 0){
        this.errorMessage = this.errorMessage + " / ";
      }
      this.errorMessage = this.errorMessage + "Address Line 2 ";
    }

    if (this.address.city == null || this.address.city.length <= 0){
      if(this.errorMessage != null && this.errorMessage.length > 0){
        this.errorMessage = this.errorMessage + " / ";
      }
      this.errorMessage = this.errorMessage + "City ";
    }

    if(this.errorMessage != null && this.errorMessage.length > 0){
      this.errorMessage = this.errorMessage + "cannot be blank!";
    }

    if(this.accountTypeSelectItem != null){
      if (this.user.userName != null && this.user.userName.length > 0){

        const regularExpression = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if(!regularExpression.test(String(this.user.userName).toLowerCase())){
          if(this.errorMessage != null && this.errorMessage.length > 0){
            this.errorMessage = this.errorMessage + " ";
          }
          this.errorMessage = this.errorMessage + "Invalid Username!";
        }
        
      }
    }

    if(this.errorMessage != null && this.errorMessage.trim().length > 0){
      this.error = true;
    }else{
      this.error = false;
    }
  }

  clear(){

    this.individualCustomer.firstName = "";
    this.individualCustomer.lastName = "";
    this.individualCustomer.ssn_passport = "";
    this.individualCustomer.gender = null;
    this.user.userName = "";
    this.user.active = false;
    this.user.role = null;
    this.user.address = null;
    this.individualCustomer.account = null;
    
    this.user.id = "";
    this.user.guid = null;
    this.errorMessage = "";
    this.successMessage = "";
    this.error = false;
    this.success = false;
    this.update = false;
    this.address.id = null;
    this.address.line1 = null;
    this.address.line2 = null;
    this.address.city = null;
    this.genderSelectItem = null;
    this.accountTypeSelectItem = null;
    this.accountType = null;
    this.ngOnInit();
  }

  close(){
    this.error = false;
    this.success = false;
  }

  fetchUsersLazy(event) {
    this.loading = true;
    this.page = event.first / 12;

    this.filterAccountName = new URLSearchParams(stringify(event.filters.name)).get("value");

    this.getAllUsers(false);
  }

  getAllUsers(reset: boolean) {
    if (reset) {
      this.page = 0;
    }

    this.request.name = this.filterAccountName;
    this.userService.findAllUsersWithAccounts(this.request, this.page)
      .subscribe((allUsersResponse: AllUsersResponse) => {
        this.allUsersResponse = allUsersResponse
        if (this.allUsersResponse.content.length == 0) {
          this.paginator = false;
        } else {
          this.paginator = true;
        }
        this.loading = false;
      }
      ); 
  }

  updateUser(){
    this.user.updatedBy = this.tokenStorage.getUsername();
    this.save();
    this.update = false;
  }

  delete(user: User){
    this.confirmationService.confirm({
      message: 'Are you sure you want to inactive '+ user.individualCustomer.account.accountNo + '?',
      accept: () => {
        this.inactiveAccount(user.id);
      }
    });
  }


  inactiveAccount(id: string) {
    this.errorMessage = "";
    this.successMessage = "";
    this.error = false;
    this.success = false;

    this.userService.inactivateAccount(id).subscribe(r => {
      this.response = r;

      if(this.response.code != null && this.response.code == "200"){
        this.success = true;
        this.successMessage = this.response.message;
      }else{
        this.error = true;
        this.errorMessage = this.response.message;
      }
      this.ngOnInit();
    });
  } 


  selectAccountTypeChange($event){
    let accountTypeName = $event.target.options[$event.target.options.selectedIndex].text;
    this.accountTypeService.getAccountType(accountTypeName)
      .subscribe(accountTypeName => {
        this.accountTypeSelectItem = accountTypeName;
      });
  }


  selectGenderChange($event){
    let gender = $event.target.options[$event.target.options.selectedIndex].text;
    this.genderSelectItem = gender;
  }

  selectBranchChange($event){
    let branchName = $event.target.options[$event.target.options.selectedIndex].text;
    this.branchService.getBranch(branchName)
      .subscribe(branchName => {
        this.branchSelectItem = branchName;
      });
  }

  viewAccount(user: User){
    this.accountView = true;
    this.user = user;
  }

  closeAccountView(){
    this.accountView = false;
    this.clear();
    this.ngOnInit();
  }

  withdraw(){
    if (this.saveWithdrawAmount != null && this.saveWithdrawAmount > 0 && this.saveWithdrawAmount < this.user.individualCustomer.account.amount){
      this.user.individualCustomer.account.amount = this.user.individualCustomer.account.amount - this.saveWithdrawAmount;
      this.userService.createUser(this.user)
        .subscribe(r => {
          this.response = r;
          this.loaded = true;

          if(this.user.id != null && this.user.id.length > 0){
            this.successMessage = "Account updated successfully.";
          }else{
            this.successMessage = "Account created successfully.";
          }
          
          this.success = true;
          this.saveWithdrawAmount = null;
        }, error => {
          this.errorMessage = error.error.message;          
          this.loaded = true;
          this.error = true;
        });
    }else{
      if (this.saveWithdrawAmount == null || this.saveWithdrawAmount < 0 || this.saveWithdrawAmount > this.user.individualCustomer.account.amount){
        this.errorMessage = "Invalid withdraw amount!";
      }

      if(this.errorMessage != null && this.errorMessage.trim().length > 0){
        this.error = true;
      }else{
        this.error = false;
      }
    }
  }

  deposit(){
    if (this.saveWithdrawAmount != null && this.saveWithdrawAmount > 0){
      this.user.individualCustomer.account.amount = this.user.individualCustomer.account.amount + this.saveWithdrawAmount;
      this.userService.createUser(this.user)
        .subscribe(r => {
          this.response = r;
          this.loaded = true;

          if(this.user.id != null && this.user.id.length > 0){
            this.successMessage = "Account updated successfully.";
          }else{
            this.successMessage = "Account created successfully.";
          }
          
          this.success = true;
          this.saveWithdrawAmount = null;
        }, error => {
          this.errorMessage = error.error.message;          
          this.loaded = true;
          this.error = true;
        });
    }else{
      if (this.saveWithdrawAmount == null || this.saveWithdrawAmount < 0){
        this.errorMessage = "Invalid deposit amount!";
      }

      if(this.errorMessage != null && this.errorMessage.trim().length > 0){
        this.error = true;
      }else{
        this.error = false;
      }
    }
  }
}


