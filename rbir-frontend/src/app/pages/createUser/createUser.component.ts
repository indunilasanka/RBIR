/**
 * Created by EMS on 6/1/2017.
 */

import { Component } from '@angular/core';
import { FormGroup, AbstractControl, FormBuilder, Validators } from '@angular/forms';
import { EmailValidator, EqualPasswordsValidator } from '../../theme/validators';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { CreateUserService } from './createUser.service';
import { DefaultModal } from '../ui/components/modals/default-modal/default-modal.component';
 
@Component({
  selector: 'create_user',
  templateUrl: './createUser.html',
  styleUrls: ['./createUser.scss'],
})
export class CreateUser {

  public form: FormGroup;
  public name: AbstractControl;
  public lname: AbstractControl;
  public email: AbstractControl;
  public password: AbstractControl;
  public repeatPassword: AbstractControl;
  public passwords: FormGroup;
  public level: AbstractControl;

  public submitted: boolean = false;

  constructor(fb: FormBuilder, private createUserService: CreateUserService, private modalService: NgbModal ) {
    
        this.form = fb.group({
          'name': ['', Validators.compose([Validators.required, Validators.minLength(4)])],
          'lname': ['', Validators.compose([Validators.required, Validators.minLength(4)])],
          'email': ['', Validators.compose([Validators.required, EmailValidator.validate])],
          'level': ['', Validators.compose([Validators.required])],
          'passwords': fb.group({
            'password': ['', Validators.compose([Validators.required, Validators.minLength(4)])],
            'repeatPassword': ['', Validators.compose([Validators.required, Validators.minLength(4)])]
          }, { validator: EqualPasswordsValidator.validate('password', 'repeatPassword')})
        });
    
        this.name = this.form.controls['name'];
        this.email = this.form.controls['email'];
        this.passwords = <FormGroup> this.form.controls['passwords'];
        this.password = this.passwords.controls['password'];
        this.repeatPassword = this.passwords.controls['repeatPassword'];
        this.lname = this.form.controls['lname'];
        this.level = this.form.controls['level'];

  }

  onSubmit(values: Object ): void {
    this.submitted = true;
    if (this.form.valid) {

      this.createUserService.createUser(this.name.value , this.lname.value, this.email.value ,
        this.password.value , this.level.value).subscribe(
        result => {
          const activeModal = this.modalService.open(DefaultModal, {size: 'sm',
                                                                  backdrop: 'static'});
          activeModal.componentInstance.modalHeader = 'Create user';
          activeModal.componentInstance.modalContent = 'User created successfully';
          console.log("created new user..." );

        },
        error => {

          const activeModal = this.modalService.open(DefaultModal, {size: 'sm',
          backdrop: 'static'});
          activeModal.componentInstance.modalHeader = 'Create user';
          activeModal.componentInstance.modalContent = 'Create user unsuccessfull';
          console.log( "error create new user...." );
        }
      );
  
      }          
    }
      // your code goes here
      // console.log(values);
  }
