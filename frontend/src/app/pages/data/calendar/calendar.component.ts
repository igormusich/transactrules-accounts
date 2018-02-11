import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { DataSource } from '@angular/cdk/table';
import { MatPaginator, MatSort } from '@angular/material';
import { Observable } from 'rxjs/Observable';
import { List } from '../../../core/list/list.interface';
import 'rxjs/add/operator/first';
import 'rxjs/add/operator/startWith';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/observable/merge';
import 'rxjs/add/observable/of';
import 'rxjs/add/observable/interval';
import 'rxjs/add/operator/map';
import * as moment from 'moment';
import { ROUTE_TRANSITION } from '../../../app.animation';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Calendar } from 'app/models/calendar.model';
import { ApiClientService } from 'app/api-client-service';

@Component({
  selector: 'vr-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss'],
  animations: [...ROUTE_TRANSITION],
  host: { '[@routeTransition]': '' }
})
export class CalendarComponent implements OnInit {
  scrollbar: any;

  displayedColumns = ['name'];
  dataSource: CalendarSource;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private _apiService: ApiClientService) {

  }

  ngOnInit() {
    this.dataSource = new CalendarSource(this._apiService);

  }

  ngOnDestroy() {
  }

  createCalendar(){
    
  }
}

export class CalendarSource extends DataSource<any> {
  items: Observable<Calendar[]>;
  constructor(private apiService: ApiClientService) {
    super();
  }
  connect(): Observable<Calendar[]> {
    this.items = this.apiService.findAllCalendars();

    return this.items;
  }
  disconnect() { }

  isLoadingResults() {
    return false;
  }
}