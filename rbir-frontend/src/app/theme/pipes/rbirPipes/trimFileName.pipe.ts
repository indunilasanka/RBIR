import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'trimFileName'} )
export class TrimFileNamePipe implements PipeTransform {

  transform(str, width) {
    var spn = $('<span style="visibility:hidden"></span>').text(str).appendTo('body');
    var txt = str;
    while (spn.width() > width) { txt = txt.slice(0, -1); spn.text(txt + "..."); }
    return txt;
  }
}
