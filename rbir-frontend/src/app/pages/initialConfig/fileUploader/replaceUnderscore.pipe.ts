import { Pipe, PipeTransform } from '@angular/core';  

@Pipe({ name: 'replaceUnderscore' } ) 
export class ReplaceUnderscore implements PipeTransform { 
   transform(value): string { 
    const newValue = 'Security Level '+value.substr(-1) ;
    return newValue;
   } 
} 
