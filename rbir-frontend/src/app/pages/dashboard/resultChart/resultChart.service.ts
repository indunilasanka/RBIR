import { Injectable } from '@angular/core';
import { BaThemeConfigProvider, colorHelper } from '../../../theme';


@Injectable()
export class ResultChartService {

  constructor(private _baConfig: BaThemeConfigProvider) {
  }

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

  getAllResult() {
    return this._results;
  }

  getData(secLevl: number[], precentages: number[], categories: string[]) {
    let dashboardColors = this._baConfig.get().colors.dashboard;
    return [
      {
        value: secLevl[0],
        color: dashboardColors.white,
        highlight: colorHelper.shade(dashboardColors.white, 15),
        label: categories[0],
        percentage: precentages[0],
        order: 1,
      }, {
        value: secLevl[1],
        color: dashboardColors.gossip,
        highlight: colorHelper.shade(dashboardColors.gossip, 15),
        label: categories[1],
        percentage: precentages[1],
        order: 4,
      }, {
        value: secLevl[2],
        color: dashboardColors.silverTree,
        highlight: colorHelper.shade(dashboardColors.silverTree, 15),
        label: categories[2],
        percentage: precentages[2],
        order: 3,
      }, {
        value: secLevl[3],
        color: dashboardColors.surfieGreen,
        highlight: colorHelper.shade(dashboardColors.surfieGreen, 15),
        label: categories[3],
        percentage: precentages[3],
        order: 2,
      }, {
        value: secLevl[4],
        color: dashboardColors.blueStone,
        highlight: colorHelper.shade(dashboardColors.blueStone, 15),
        label: categories[4],
        percentage: precentages[4],
        order: 0,
      }, {
        value: secLevl[5],
        color: dashboardColors.gossip,
        highlight: colorHelper.shade(dashboardColors.gossip, 15),
        label: categories[5],
        percentage: precentages[5],
        order: 4,
      }, {
        value: secLevl[6],
        color: dashboardColors.silverTree,
        highlight: colorHelper.shade(dashboardColors.silverTree, 15),
        label: categories[6],
        percentage: precentages[6],
        order: 3,
      }, 
    ];
  }
}
