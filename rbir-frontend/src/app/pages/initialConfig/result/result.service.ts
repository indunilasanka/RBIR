import { Injectable } from '@angular/core';
import { BaThemeConfigProvider } from '../../../theme';

@Injectable()
export class ResultService {

  private _results = [
    {
      fileName: '2017 tax.doc',
      fileCategory: 'leger',
      securityLvl: 'level1',
    },
    {
      fileName: '2016 tax.doc',
      fileCategory: 'leger',
      securityLvl: 'level1',
    },
  ];

  private _data = {
    labelsPieData: {
      labels: ['Security Level 1', 'Security Level 2', 'Security Level 3', 'Security Level 4'  ],
      series: [100, 15, 40, 70],
    },
    labelsPieOptions: {
      fullWidth: true,
      height: '300px',
      weight: '300px',
      labelDirection: 'explode',
      labelInterpolationFnc: function (value) {
        return  'L' +value.substr(-1);
      },
    },
  }

  constructor(private _baConfig: BaThemeConfigProvider) {
  }

  public getAll() {
    return this._data;
  }


  public getResult() {
    return this._results;
  }

  public getResponsive(padding, offset) {
    return [
      ['screen and (min-width: 1550px)', {
        chartPadding: padding,
        labelOffset: offset,
        labelDirection: 'explode',
        labelInterpolationFnc: function (value) {
          return value;
        },
      }],
      ['screen and (max-width: 1200px)', {
        chartPadding: padding,
        labelOffset: offset,
        labelDirection: 'explode',
        labelInterpolationFnc: function (value) {
          return value;
        },
      }],
      ['screen and (max-width: 600px)', {
        chartPadding: 0,
        labelOffset: 0,
        labelInterpolationFnc: function (value) {
          return value[0];
        },
      }],
    ];
  }

  private setData(){

  }
}
