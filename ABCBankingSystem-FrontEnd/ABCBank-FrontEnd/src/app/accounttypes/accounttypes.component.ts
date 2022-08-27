import { Component, OnInit } from '@angular/core';
import { AccountType } from '../model/AccountType';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { AccountTypeService } from '../services/accounttype.service';
import { AllAccountTypesResponse } from '../model/AllAccountTypesResponse';
import { stringify } from 'querystring';
import { CommonResponse } from '../model/CommonResponse';
import { ConfirmationService } from 'primeng/api';

@Component({
  selector: 'app-user',
  templateUrl: './accounttypes.component.html',
  styleUrls: ['./accounttypes.component.css']
})
export class AccountTypesComponent implements OnInit {

  constructor(private accountTypeService: AccountTypeService, private confirmationService: ConfirmationService) { }  
  
  loaded: boolean;
  errorMessage: string;
  successMessage: string;
  error: boolean;
  success: boolean;
  update: boolean;

  accounttype: AccountType= {
    guid: null,
    name: null,
    description: null,
    interest: null,
    id: null
  }

  response: CommonResponse= {
    code: null,
    message: null
  }

  allAccountTypesResponse : AllAccountTypesResponse;

  filterAccountTypeName: string;
  
  page: number;
  paginator: boolean = true;
  loading: boolean;
  message: any;

  ngOnInit() {
    this.loaded = true;
    this.getAllAccountTypes(true)
  }  
  
  save(){  
    this.errorMessage = "";
    this.successMessage = "";
    this.error = false;
    this.success = false;
    this.validate();
    if (!this.error) {
      this.loaded = false;
      this.accountTypeService.createAccountType(this.accounttype)
        .subscribe(r => {
          this.response = r;
          this.loaded = true;

          if(this.accounttype.id != null && this.accounttype.id.length > 0){
            this.successMessage = "Account Type updated successfully.";
          }else{
            this.successMessage = "Account Type successfully.";
          }
          
          this.success = true;
          this.ngOnInit();
          this.accounttype.name = "";
          this.accounttype.description = "";
          this.accounttype.interest = 0;
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
    if (this.accounttype.name == null || this.accounttype.name.length <= 0){
      this.errorMessage = this.errorMessage + "Name ";
    }

    if (this.accounttype.description == null || this.accounttype.description.length <= 0){
      if(this.errorMessage != null && this.errorMessage.length > 0){
        this.errorMessage = this.errorMessage + " / ";
      }
      this.errorMessage = this.errorMessage + "Description ";
    }

    if (this.accounttype.interest == null || this.accounttype.interest <= 0){
      if(this.errorMessage != null && this.errorMessage.length > 0){
        this.errorMessage = this.errorMessage + " / ";
      }
      this.errorMessage = this.errorMessage + "Interest ";
    }

    if(this.errorMessage != null && this.errorMessage.length > 0){
      this.errorMessage = this.errorMessage + "cannot be blank.";
    }

    if(this.errorMessage != null && this.errorMessage.trim().length > 0){
      this.error = true;
    }else{
      this.error = false;
    }
  }

  clear(){
    this.accounttype.name = "";
    this.accounttype.description = "";
    this.accounttype.interest = 0;
    this.errorMessage = "";
    this.successMessage = "";
    this.error = false;
    this.success = false;
    this.update = false;
  }

  close(){
    this.error = false;
    this.success = false;
  }

  fetchAccountTypesLazy(event) {
    this.loading = true;
    this.page = event.first / 12;

    this.getAllAccountTypes(false);
  }

  getAllAccountTypes(reset: boolean) {
    if (reset) {
      this.page = 0;
    }

    this.accountTypeService.findAllAccountTypes(this.page)
      .subscribe((allAccountTypesResponse: AllAccountTypesResponse) => {
        this.allAccountTypesResponse = allAccountTypesResponse
        if (this.allAccountTypesResponse.content.length == 0) {
          this.paginator = false;
        } else {
          this.paginator = true;
        }
        this.loading = false;
      }
      ); 
  }

  editAccountType(accounttype: AccountType){
    this.accounttype.name = accounttype.name;
    this.accounttype.description = accounttype.description;
    this.accounttype.interest = accounttype.interest;
    this.accounttype.guid = accounttype.guid;
    this.accounttype.id = accounttype.id;
    this.update = true;
  }

  updateAccountType(){
    this.save();
    this.update = false;
  }

  delete(accounttype: AccountType){
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete '+ accounttype.name + '?',
      accept: () => {
        this.deleteAccountType(accounttype.id);
      }
    });
  }

  deleteAccountType(id: string) {
    this.errorMessage = "";
    this.successMessage = "";
    this.error = false;
    this.success = false;

    this.accountTypeService.deleteAccountType(id).subscribe(r => {
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
}
