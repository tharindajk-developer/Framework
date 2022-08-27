import { Component, OnInit } from '@angular/core';
import { Branch } from '../model/Branch';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { BranchService } from '../services/branch.service';
import { AllBranchResponse } from '../model/AllBranchResponse';
import { stringify } from 'querystring';
import { CommonResponse } from '../model/CommonResponse';
import { ConfirmationService } from 'primeng/api';

@Component({
  selector: 'app-user',
  templateUrl: './branch.component.html',
  styleUrls: ['./branch.component.css']
})
export class BranchComponent implements OnInit {

  constructor(private branchService: BranchService, private confirmationService: ConfirmationService) { }  
  
  loaded: boolean;
  errorMessage: string;
  successMessage: string;
  error: boolean;
  success: boolean;
  update: boolean;

  branch: Branch= {
    guid: null,
    name: null,
    address: null,
    code: null,
    id: null
  }

  response: CommonResponse= {
    code: null,
    message: null
  }

  allBranchResponse : AllBranchResponse;

  filterBranchName: string;
  
  page: number;
  paginator: boolean = true;
  loading: boolean;
  message: any;

  ngOnInit() {
    this.loaded = true;
    this.getAllBranches(true)
  }  
  
  save(){  
    this.errorMessage = "";
    this.successMessage = "";
    this.error = false;
    this.success = false;
    this.validate();
    if (!this.error) {
      this.loaded = false;
      this.branchService.createBranch(this.branch)
        .subscribe(r => {
          this.response = r;
          this.loaded = true;

          if(this.branch.id != null && this.branch.id.length > 0){
            this.successMessage = "Branch updated successfully.";
          }else{
            this.successMessage = "Branch Type successfully.";
          }
          
          this.success = true;
          this.ngOnInit();
          this.branch.name = "";
          this.branch.code = "";
          this.branch.address = "";
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
    if (this.branch.name == null || this.branch.name.length <= 0){
      this.errorMessage = this.errorMessage + "Name ";
    }

    if (this.branch.address == null || this.branch.address.length <= 0){
      if(this.errorMessage != null && this.errorMessage.length > 0){
        this.errorMessage = this.errorMessage + " / ";
      }
      this.errorMessage = this.errorMessage + "Address ";
    }

    if (this.branch.code == null || this.branch.code.length <= 0){
      if(this.errorMessage != null && this.errorMessage.length > 0){
        this.errorMessage = this.errorMessage + " / ";
      }
      this.errorMessage = this.errorMessage + "Code ";
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
    this.branch.name = "";
    this.branch.address = "";
    this.branch.code = "";
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

  fetchBranchLazy(event) {
    this.loading = true;
    this.page = event.first / 12;

    this.getAllBranches(false);
  }

  getAllBranches(reset: boolean) {
    if (reset) {
      this.page = 0;
    }

    this.branchService.findAllBranches(this.page)
      .subscribe((allBranchResponse: AllBranchResponse) => {
        this.allBranchResponse = allBranchResponse
        if (this.allBranchResponse.content.length == 0) {
          this.paginator = false;
        } else {
          this.paginator = true;
        }
        this.loading = false;
      }
      ); 
  }

  editBranch(branch: Branch){
    this.branch.name = branch.name;
    this.branch.address = branch.address;
    this.branch.code = branch.code;
    this.branch.guid = branch.guid;
    this.branch.id = branch.id;
    this.update = true;
  }

  updateBranch(){
    this.save();
    this.update = false;
  }

  delete(branch: Branch){
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete '+ branch.name + '?',
      accept: () => {
        this.deleteBranch(branch.id);
      }
    });
  }

  deleteBranch(id: string) {
    this.errorMessage = "";
    this.successMessage = "";
    this.error = false;
    this.success = false;

    this.branchService.deleteBranch(id).subscribe(r => {
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
