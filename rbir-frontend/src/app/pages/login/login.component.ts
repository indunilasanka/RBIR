import { Component } from '@angular/core';
import { FormGroup, AbstractControl, FormBuilder, Validators, FormControl} from '@angular/forms';
import { Router } from '@angular/router';

import { BaThemePreloader } from '../../theme/services';

import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'login',
  templateUrl: './login.html',
  styleUrls: ['./login.scss']
})
export class Login {

  public form:FormGroup;
  public email:AbstractControl;
  public password:AbstractControl;
  public submitted:boolean = false;
  public error: string;


  constructor(fb:FormBuilder,private router: Router,
    private authenticationService: AuthenticationService) {
    this.form = fb.group({
      'email': ['', Validators.compose([Validators.required, Validators.minLength(4)])],
      'password': ['', Validators.compose([Validators.required, Validators.minLength(4)])]
    });

    this.email = this.form.controls['email'];
    this.password = this.form.controls['password'];
  }

  ngOnInit() {
        // reset login status
        this.authenticationService.logout();
  }

  public onSubmit(values:Object):void {


    this.submitted = true;
    if (this.form.valid) {
      console.log("valid data ................");
      BaThemePreloader.registerLoader(this.logIn());
    }
  }

  logIn(): Promise<any> {
    return new Promise((resolve, reject) => {
      
      this.authenticationService.login(this.email.value, this.password.value).subscribe(
        result => {
           if (result === true) {
                      // login successful
                console.log("navigating to dashboard")
                resolve();
                this.router.navigate(['/pages/dashboard']);
          } 
        },
        error => {
          console.log(error);
          resolve();
        },
      );
    });
  }
}


