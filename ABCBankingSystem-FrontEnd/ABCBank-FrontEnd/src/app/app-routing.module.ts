import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { StaffComponent } from './staff/staff.component';
import { AdminComponent } from './admin/admin.component';
import { RolesComponent } from './roles/roles.component';
import { DepartmentsComponent } from './departments/departments.component';
import { AccountTypesComponent } from './accounttypes/accounttypes.component';
import { BranchComponent } from './branch/branch.component';
import { AccountComponent } from './account/account.component';
import { CorporateAccountComponent } from './corporateaccount/corporateaccount.component';
import { CustomerComponent } from './customer/customer.component';

const routes: Routes = [
    {
        path: 'home',
        //component: HomeComponent
        component: LoginComponent
    },
    {
        path: 'staff',
        component: StaffComponent
    },
    {
        path: 'admin',
        component: AdminComponent
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: 'roles',
        component: RolesComponent
    },
    {
        path: 'departments',
        component: DepartmentsComponent
    },
    {
        path: 'accounttypes',
        component: AccountTypesComponent
    },
    {
        path: 'branch',
        component: BranchComponent
    },
    {
        path: 'accounts',
        component: AccountComponent
    },
    {
        path: 'corporateaccounts',
        component: CorporateAccountComponent
    },
    {
        path: 'customer',
        component: CustomerComponent
    },
    {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
    },
    
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
