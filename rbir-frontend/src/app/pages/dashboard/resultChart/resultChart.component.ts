import {Component} from '@angular/core';

import { ResultChartService } from './resultChart.service';
import { CateResult } from '../../../models/categorization_result.model';


import * as Chart from 'chart.js';

@Component({
  selector: 'result-chart',
  templateUrl: './resultChart.html',
  styleUrls: ['./resultChart.scss']
})

// TODO: move chart.js to it's own component
export class ResultChart {

  totalDoc: number = 0;
  public doughnutData: Array<Object>;
  results: Array<CateResult>;
  finalResult: any = null;
  accuracy: number[];
  securityResult: any[];
 
  

  constructor(private resultChartService: ResultChartService) {
    // this.doughnutData = resultChartService.getData();
    this.results = resultChartService.getAllResult();
  }


  doSomething(result: any) {
    if ( result ) {
      this.finalResult = result.doc_category;
      this.formatResult( result ); 
    }
  }

  formatResult( result ) {
    
    const sizes: number[] = [0 , 0, 0, 0, 0, 0 , 0, 0, 0];
    const categories: string[] = ['Level', 'Level', 'Level', 'Level', 'Level', 'Level', 'Level', 'Level', 'Level'];
    const precentages: number[] = [0 , 0, 0, 0, 0, 0 , 0, 0, 0];
    let docCount: number = 0;
    this.totalDoc = 0;
    let i: number = 0;

    result.num_doc_category.forEach(element => {
      docCount = Number(element.size);
      sizes[i] = docCount;
      categories[i] = 'Level '+ element.category.substr(-1);
      this.totalDoc = this.totalDoc + docCount;
      i++;
    });

    for (let j = 0; j < 9; j ++) {
      const precentage = sizes[j] * 100 / this.totalDoc;
      precentages[j] = parseFloat(precentage.toFixed(2));
    }

      this.doughnutData = this.resultChartService.getData(sizes, precentages, categories);
      this._loadDoughnutCharts();
  }

  

  private _loadDoughnutCharts() {
    let el = jQuery('.chart-area').get(0) as HTMLCanvasElement;
    new Chart(el.getContext('2d')).Doughnut(this.doughnutData, {
      segmentShowStroke: false,
      percentageInnerCutout : 64,
      responsive: true
    });
  }
}
