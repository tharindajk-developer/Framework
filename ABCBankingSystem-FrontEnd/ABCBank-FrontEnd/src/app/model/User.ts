import { Role } from "./Role";
import { Department } from "./Department";
import { Address } from "./Address";
import { Staff } from "./Staff";
import { CorporateCustomer } from "./CorporateCustomer";
import { IndividualCustomer } from "./IndividualCustomer";

export class User {

    userName: string;
    role: Role;
    guid: number;
    active: boolean;
    address: Address;
    staff: Staff;
    corporateCustomer: CorporateCustomer;
    individualCustomer: IndividualCustomer;
    createdBy: string;
    updatedBy: string;
    id: string;
}