import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from './auth/token-storage.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  private roles: string[];
  private authority: string;
  private userName: string;

  constructor(private tokenStorage: TokenStorageService) { }

  ngOnInit() {
    if (this.tokenStorage.getToken()) {
      this.userName = this.tokenStorage.getUsername();
      this.roles = this.tokenStorage.getAuthorities();
      this.roles.every(role => {
        if (role === 'administrator'){
          this.authority = 'administrator';
          return false;
        } else if (role === 'staff'){
          this.authority = 'staff';
          return false;
        }else if (role === 'customer'){
          this.authority = 'customer';
          return false;
        }
        this.authority = 'user';
        return true;
      });
    }
  }

  logout() {
    this.tokenStorage.signOut();
    this.authority = null;
  }
}
