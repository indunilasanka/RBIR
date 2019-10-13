import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AppTranslationModule } from '../../app.translation.module';
import { NgaModule } from '../../theme/nga.module';

import { Dashboard } from './dashboard.component';
import { routing } from './dashboard.routing';
import { FormsModule as AngularFormsModule } from '@angular/forms';

import { Calendar } from './calendar';
import { ResultChart } from './resultChart/resultChart.component';
import { FileUploader } from './fileUploader';
import { LineChart } from './lineChart/lineChart.component';
import { Todo } from './todo/todo.component';
import { PopularApp } from './popularApp/popularApp.component';
import { UsersMap } from './usersMap/usersMap.component';
import { TrafficChart } from './trafficChart/trafficChart.component';
import { TrimFileNamePipe } from './../../theme/pipes/rbirPipes/trimFileName.pipe';

import { CalendarService } from './calendar/calendar.service';
import { FileUploadService } from './fileUploader/fileUpload.service';
import { Ng2Bs3ModalModule } from 'ng2-bs3-modal/ng2-bs3-modal';
import { ResultChartService } from './resultChart/resultChart.service';
import { LineChartService } from './lineChart/lineChart.service';
import { SecLvelel } from './fileUploader/secLvelel.pipe';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    AppTranslationModule,
    NgaModule,
    routing,
    Ng2Bs3ModalModule,
    FormsModule,
  ],
  declarations: [
    ResultChart,
    SecLvelel,
    Dashboard,
    FileUploader,
    Calendar,
    LineChart,
    Todo,
    PopularApp,
    TrafficChart,
    UsersMap,
    TrimFileNamePipe,
  ],
  providers: [
    FileUploadService,
    ResultChartService,
    CalendarService,
    LineChartService,
  ],
})
export class DashboardModule {}
