import { Component, Input, ViewChild } from '@angular/core';

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

  // @Input() getResult: any;
  @ViewChild('childComponent') childComponent;

  public doughnutData: Array<Object>;
  results: Array<CateResult>;
  finalResult: any = null;
  accuracy: number[];
  securityResult: any[];
  totalDoc: number = 0;

  constructor(private resultChartService: ResultChartService) {
    // this.doughnutData = resultChartService.getData();
    this.results = resultChartService.getAllResult();
  }

  private _loadDoughnutCharts() {
    let el = jQuery('.chart-area').get(0) as HTMLCanvasElement;
    new Chart(el.getContext('2d')).Doughnut(this.doughnutData, {
      segmentShowStroke: false,
      percentageInnerCutout : 64,
      responsive: true,
    });
  }

  doSomething(result: any) {
    if ( result ) {
      this.finalResult = result.doc_category;
      this.formatResult( result ); 
      this.childComponent._updatePieCharts(this.accuracy);
    }
  }

  formatResult( result ) {
    this.accuracy = [];
    console.log("result  ----", result);    
    this.accuracy.push(result.classifier_accuracy.KMeans);
    this.accuracy.push(result.classifier_accuracy.NaiveBaysian);
  
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
}

