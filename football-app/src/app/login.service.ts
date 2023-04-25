import { Injectable } from '@angular/core';
import { map } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Credentials } from './credentials';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http :HttpClient) { }

  login(creds: Credentials) {
    return this.http.post("http://localhost:8080/login", creds, {
      observe: 'response'
    }).pipe(map((response: HttpResponse<any>) => {
      const body = response.body;
      const header = response.headers;

      const token: any = header.get("Authorization")?.replace("Bearer ", "");

      localStorage.setItem('token', token);

      return body;
    }))
  }

  getToken() {
    return localStorage.getItem('token');
  }

}