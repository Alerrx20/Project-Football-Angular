import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Credentials } from '../credentials';
import { LoginService } from '../login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  creds: Credentials = {
    email: '',
    password: ''
  };

  constructor(private loginService: LoginService,
    private router: Router) {}

  login(form: NgForm) {
    console.log('form value', form.value);
    this.loginService.login(this.creds)
    .subscribe(response => {
      this.router.navigate(['/']);
    });
  }

}