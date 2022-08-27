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

@Component({
  selector: 'app-user',
  templateUrl: './staff.component.html',
  styleUrls: ['./staff.component.css']
})
export class StaffComponent implements OnInit {

  constructor(private userService:UserService, private confirmationService: ConfirmationService, private tokenStorage: TokenStorageService, private departmentService: DepartmentService, private roleService: RoleService) { }  
  
  loaded: boolean;
  errorMessage: string;
  successMessage: string;
  error: boolean;
  success: boolean;
  update: boolean;
  selectedFile: ImageSnippet;
  isSelected: boolean;

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

  staff: Staff={
    id: null,
	  empId: null,
	  firstName: null,
	  lastName: null,
  	dob: null,
	  ssn_passport: null,
	  department: null,
	  gender: null,
	  image: null
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

  departmentSelectItems: Array<Department>;
  departmentSelectItem: Department;
  department: Department;

  roleSelectItems: Array<Role>;
  roleSelectItem: Role;
  role: Role;

  genderSelectItem: string;

  genderSelectItems: Array<string> = ['Male', 'Female', 'Prefer Not to Say'];

  ngOnInit() {

    this.departmentService.getAllDepartments()
      .subscribe(departmentSelectItems => {
        this.departmentSelectItems = departmentSelectItems;
    });

    this.roleService.getAllRoles()
      .subscribe(roleSelectItems => {
        this.roleSelectItems = roleSelectItems;
    });

    this.isSelected = false;
    this.loaded = false;
    this.getAllUsers(true);
    
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
      }
     
      if(this.roleSelectItem){
        this.user.role = this.roleSelectItem;
      }
      if(this.staff){
        this.user.staff = this.staff;
      }
      if(this.isSelected){
        this.user.staff.image = this.selectedFile.src;
      }
      if(this.departmentSelectItem){
        this.user.staff.department = this.departmentSelectItem;
      }
      if(this.address){
        this.user.address = this.address;
      }
      if(this.genderSelectItem){
        this.user.staff.gender = this.genderSelectItem;
      }

      console.log(JSON.stringify(this.user));
      this.userService.createUser(this.user)
        .subscribe(r => {
          this.response = r;
          this.loaded = true;

          if(this.user.id != null && this.user.id.length > 0){
            this.successMessage = "User updated successfully.";
          }else{
            this.successMessage = "User created successfully.";
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

    if (this.staff.firstName == null || this.staff.firstName.length <= 0){
      this.errorMessage = this.errorMessage + "First Name ";
    }

    if (this.staff.lastName == null || this.staff.lastName.length <= 0){
      if(this.errorMessage != null && this.errorMessage.length > 0){
        this.errorMessage = this.errorMessage + " / ";
      }
      this.errorMessage = this.errorMessage + "Last Name ";
    }

    if (this.staff.ssn_passport == null || this.staff.ssn_passport.length <= 0){
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

    if (this.roleSelectItem == null){
      if(this.errorMessage != null && this.errorMessage.length > 0){
        this.errorMessage = this.errorMessage + " / ";
      }
      this.errorMessage = this.errorMessage + "Role ";
    }

    if (this.departmentSelectItem == null){
      if(this.errorMessage != null && this.errorMessage.length > 0){
        this.errorMessage = this.errorMessage + " / ";
      }
      this.errorMessage = this.errorMessage + "Department ";
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

    if (!this.update && this.selectedFile == null){
      if(this.errorMessage != null && this.errorMessage.length > 0){
        this.errorMessage = this.errorMessage + " / ";
      }
      this.errorMessage = this.errorMessage + "Image ";
    }

    if (this.update && (this.staff.image == null || this.staff.image.length == 0)){
      if(this.errorMessage != null && this.errorMessage.length > 0){
        this.errorMessage = this.errorMessage + " / ";
      }
      this.errorMessage = this.errorMessage + "Image ";
    }

    if(this.errorMessage != null && this.errorMessage.length > 0){
      this.errorMessage = this.errorMessage + "cannot be blank!";
    }

    if(this.roleSelectItem != null){
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

    this.staff.firstName = "";
    this.staff.lastName = "";
    this.staff.image = "";
    this.staff.ssn_passport = "";
    this.user.userName = "";
    this.user.active = false;
    this.user.role = null;
    this.user.address = null;
    this.staff.department = null;
    this.staff.gender = null;
    this.selectedFile = null;
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
    this.roleSelectItem = null;
    this.genderSelectItem = null;
    this.departmentSelectItem = null;
    this.role = null;
    this.department = null;
    this.ngOnInit();
  }

  close(){
    this.error = false;
    this.success = false;
  }

  fetchUsersLazy(event) {
    this.loading = true;
    this.page = event.first / 12;

    this.getAllUsers(false);
  }

  getAllUsers(reset: boolean) {
    if (reset) {
      this.page = 0;
    }

    this.userService.findAllUsers(this.page)
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

  editUser(user: User){
    this.staff.firstName = user.staff.firstName;
    this.staff.lastName = user.staff.lastName;
    this.staff.image = user.staff.image;
    this.staff.ssn_passport = user.staff.ssn_passport;
    this.user.createdBy = user.createdBy;
    this.user.guid = user.guid;
    this.user.userName = user.userName;
    this.user.id = user.id;
    this.user.active = user.active;
    this.roleSelectItem = user.role;
    this.departmentSelectItem = user.staff.department;
    this.genderSelectItem = user.staff.gender;
    this.address.line1 = user.address.line1;
    this.address.line2 = user.address.line2;
    this.address.city = user.address.city;
    this.update = true;
  }

  updateUser(){
    this.user.updatedBy = this.tokenStorage.getUsername();
    this.save();
    this.update = false;
  }

  delete(user: User){
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete '+ user.staff.firstName + '?',
      accept: () => {
        this.deleteUser(user.id);
      }
    });
  }

  deleteUser(id: string) {
    this.errorMessage = "";
    this.successMessage = "";
    this.error = false;
    this.success = false;

    this.userService.deleteUser(id).subscribe(r => {
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

  processFile(imageInput: any) {
    this.isSelected = true;
    const file: File = imageInput.files[0];
    const reader = new FileReader();
    reader.addEventListener('load', (event: any) => {
      this.selectedFile = new ImageSnippet(event.target.result, file);
      this.staff.image = this.selectedFile.src;
    });
    reader.readAsDataURL(file);
  }

  selectRoleChange($event){
    let roleName = $event.target.options[$event.target.options.selectedIndex].text;
    this.roleService.getRole(roleName)
      .subscribe(role => {
        this.roleSelectItem = role;
      });
  }

  selectDepartmentChange($event){
    let departmentName = $event.target.options[$event.target.options.selectedIndex].text;
    this.departmentService.getDepartment(departmentName)
      .subscribe(department => {
        this.departmentSelectItem = department;
      });
  }

  selectGenderChange($event){
    let gender = $event.target.options[$event.target.options.selectedIndex].text;
    this.genderSelectItem = gender;
  }
}

class ImageSnippet {
  constructor(public src: string, public file: File) {}
}

