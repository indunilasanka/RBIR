import { Pipe, PipeTransform } from '@angular/core';  

@Pipe({ name: 'secLvelel' } ) 
export class SecLvelel implements PipeTransform { 
   transform(value): string { 
    const newValue = 'SecL'+value.substr(-1) ;
    return newValue;
   } 
}