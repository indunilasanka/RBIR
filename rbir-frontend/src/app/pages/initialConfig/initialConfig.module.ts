import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AppTranslationModule } from '../../app.translation.module';
import { NgaModule } from '../../theme/nga.module';
import { Ng2Bs3ModalModule } from 'ng2-bs3-modal/ng2-bs3-modal';

import { InitialConfig } from './initialConfig.component';
import { routing } from './initialConfig.routing';
import { ReplaceUnderscore } from './fileUploader/replaceUnderscore.pipe';
import { SecLvelel } from './fileUploader/secLvelel.pipe';
import { ResultChart } from './resultChart/resultChart.component';
import { PieChart } from './pieChart/pieChart.component';

import { ResultService } from './result/result.service';
import { FileUploader } from './fileUploader';
import { FileUploadService } from './fileUploader/fileUpload.service';
import { ResultChartService } from './resultChart/resultChart.service';
import { PieChartService } from './pieChart/pieChart.service';

import { Result } from './result/result.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    AppTranslationModule,
    NgaModule,
    routing,
    Ng2Bs3ModalModule,
  ],
  declarations: [
    InitialConfig,
    FileUploader,
    Result,
    ReplaceUnderscore,
    SecLvelel,
    ResultChart,
    PieChart,
  ],
  providers: [
    ResultService,
    FileUploadService,
    ResultChartService,
    PieChartService,
  ],
})

export class InitialConfigModule {}
