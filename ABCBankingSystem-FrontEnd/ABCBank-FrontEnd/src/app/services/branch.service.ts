import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { TokenStorageService } from '../auth/token-storage.service';
import { Branch } from '../model/Branch';
import { AllBranchResponse } from '../model/AllBranchResponse';
import { CommonResponse } from '../model/CommonResponse';

@Injectable({
  providedIn: 'root'
})
export class BranchService {

  constructor(private http: HttpClient, private tokenService: TokenStorageService) { }

  readonly apiEndPoint = environment.apiEndPoint;
  public headers = new HttpHeaders({ 'Authorization': 'Bearer '+this.tokenService.getToken(), 'Content-Type': 'application/json' })

  createBranch(branch: Branch): Observable<CommonResponse> {
    const headers = new HttpHeaders({ 'Authorization': 'Bearer '+this.tokenService.getToken() });
    return this.http.post<CommonResponse>(this.apiEndPoint + 'branch/create', branch, { headers });
  }

  findAllBranches(page: number): Observable<AllBranchResponse> {
    const headers = this.headers
    return this.http.post<AllBranchResponse>(this.apiEndPoint + 'branch/search/' + page, { headers });
  }

  deleteBranch(id: string): Observable<CommonResponse> {
    const headers = this.headers;
    return this.http.delete<CommonResponse>(this.apiEndPoint + 'branch/delete?id='+id, { headers });
  }

  getAllBranches(): Observable<Branch[]> {
    const headers = this.headers;
    return this.http.get<Branch[]>(this.apiEndPoint + 'branch/all', { headers });
  }

  getBranch(branch: string): Observable<Branch> {
    const headers = this.headers;
    return this.http.get<Branch>(this.apiEndPoint + 'branch/getbyname?name='+branch, { headers });
  }
}
