/**
 * Created by Kelum Bandara on 6/1/2017.
*/


import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
 
@Injectable()
export class AuthGuard implements CanActivate {
 
    constructor(private router: Router) { 
        console.log("came for auth");
    }
 
    canActivate() {
        console.log("came for auth");
        if (localStorage.getItem('currentUser')) {
            // logged in so return true
            return true;
        }
        
        console.log("came for auth");
        
        // not logged in so redirect to login page
        this.router.navigate(['/login']);
        return false;
    }
}
