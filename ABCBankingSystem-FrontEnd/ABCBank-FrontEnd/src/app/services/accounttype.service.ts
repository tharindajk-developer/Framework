import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { TokenStorageService } from '../auth/token-storage.service';
import { AccountType } from '../model/AccountType';
import { AllAccountTypesResponse } from '../model/AllAccountTypesResponse';
import { CommonResponse } from '../model/CommonResponse';

@Injectable({
  providedIn: 'root'
})
export class AccountTypeService {

  constructor(private http: HttpClient, private tokenService: TokenStorageService) { }

  readonly apiEndPoint = environment.apiEndPoint;
  public headers = new HttpHeaders({ 'Authorization': 'Bearer '+this.tokenService.getToken(), 'Content-Type': 'application/json' })

  createAccountType(accounttype: AccountType): Observable<CommonResponse> {
    const headers = new HttpHeaders({ 'Authorization': 'Bearer '+this.tokenService.getToken() });
    return this.http.post<CommonResponse>(this.apiEndPoint + 'accounttype/create', accounttype, { headers });
  }

  findAllAccountTypes(page: number): Observable<AllAccountTypesResponse> {
    const headers = this.headers
    return this.http.post<AllAccountTypesResponse>(this.apiEndPoint + 'accounttype/search/' + page, { headers });
  }

  deleteAccountType(id: string): Observable<CommonResponse> {
    const headers = this.headers;
    return this.http.delete<CommonResponse>(this.apiEndPoint + 'accounttype/delete?id='+id, { headers });
  }

  getAllAccountTypes(): Observable<AccountType[]> {
    const headers = this.headers;
    return this.http.get<AccountType[]>(this.apiEndPoint + 'accounttype/all', { headers });
  }

  getAccountType(accounttype: string): Observable<AccountType> {
    const headers = this.headers;
    return this.http.get<AccountType>(this.apiEndPoint + 'accounttype/getbyname?name='+accounttype, { headers });
  }
}
