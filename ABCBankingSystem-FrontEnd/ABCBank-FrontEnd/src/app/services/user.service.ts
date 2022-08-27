import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenStorageService } from '../auth/token-storage.service';
import { environment } from 'src/environments/environment';
import { User } from '../model/User';
import { CommonResponse } from '../model/CommonResponse';
import { AllUsersResponse } from '../model/AllUsersResponse';
import { Login } from '../model/Login';
import { RequestModel } from '../model/RequestModel';
import { Transaction } from '../model/Transaction';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userUrl = 'http://localhost:8080/api/test/user';
  private pmUrl = 'http://localhost:8080/api/test/pm';
  private adminUrl = 'http://localhost:8080/api/test/admin';

  constructor(private http: HttpClient, private tokenService: TokenStorageService) { }

  getUserBoard(): Observable<string> {
    return this.http.get(this.userUrl, { responseType: 'text' });
  }

  getPMBoard(): Observable<string> {
    return this.http.get(this.pmUrl, { responseType: 'text' });
  }

  getAdminBoard(): Observable<string> {
    return this.http.get(this.adminUrl, { responseType: 'text' });
  }

  readonly apiEndPoint = environment.apiEndPoint;
  public headers = new HttpHeaders({ 'Authorization': 'Bearer '+this.tokenService.getToken(), 'Content-Type': 'application/json' })

  createUser(user: User): Observable<CommonResponse> {
    const headers = new HttpHeaders({ 'Authorization': 'Bearer '+this.tokenService.getToken() });
    return this.http.post<CommonResponse>(this.apiEndPoint + 'user/create', user, { headers });
  }

  findAllUsers(page: number): Observable<AllUsersResponse> {
    const headers = this.headers
    return this.http.post<AllUsersResponse>(this.apiEndPoint + 'user/search/' + page, { headers });
  }

  deleteUser(id: string): Observable<CommonResponse> {
    const headers = this.headers;
    return this.http.delete<CommonResponse>(this.apiEndPoint + 'user/delete?id='+id, { headers });
  }

  getAllUsers(): Observable<User[]> {
    const headers = this.headers;
    return this.http.get<User[]>(this.apiEndPoint + 'user/all', { headers });
  }

  getUser(email: string): Observable<User> {
    const headers = this.headers;
    return this.http.get<User>(this.apiEndPoint + 'user/getbyemail?email='+email, { headers });
  }

  updatePassword(login: Login): Observable<CommonResponse> {
    const headers = new HttpHeaders({ 'Authorization': 'Bearer '+this.tokenService.getToken() });
    return this.http.post<CommonResponse>(this.apiEndPoint + 'user/changepassword', login, { headers });
  }

  inactivateAccount(id: string): Observable<CommonResponse> {
    const headers = this.headers;
    return this.http.delete<CommonResponse>(this.apiEndPoint + 'user/inactive?id='+id, { headers });
  }

  findAllUsersWithAccounts(requestModel: RequestModel, page: number): Observable<AllUsersResponse> {
    const headers = this.headers
    return this.http.post<AllUsersResponse>(this.apiEndPoint + 'user/search/filter/' + page, requestModel, { headers });
  }

  findUserByEmail(requestModel: RequestModel): Observable<User> {
    const headers = this.headers
    return this.http.post<User>(this.apiEndPoint + 'user/search/username', requestModel, { headers });
  }

  transfer(user: User, transaction: Transaction): Observable<CommonResponse> {
    const headers = new HttpHeaders({ 'Authorization': 'Bearer '+this.tokenService.getToken() });
    return this.http.post<CommonResponse>(this.apiEndPoint + 'user/transfer?id='+user.id, transaction, { headers });
  }
}
