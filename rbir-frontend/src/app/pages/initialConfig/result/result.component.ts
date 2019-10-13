import { Component } from '@angular/core';

import { ResultService } from './result.service';
import { CateResult } from '../../../models/categorization_result.model';

@Component({
  selector: 'result',
  templateUrl: './result.html',
  styleUrls: ['./result.scss'],
})
export class Result {

  public feed: Array<Object>;
  data: any;
  results: Array<CateResult>;
  cateResult: any;

  constructor(private _resultService: ResultService) {
  }

  getResponsive(padding, offset) {
    return this._resultService.getResponsive(padding, offset);
  }

  ngOnInit() {
    this.data = this._resultService.getAll();
    this.results = this._resultService.getResult();
  }

}
