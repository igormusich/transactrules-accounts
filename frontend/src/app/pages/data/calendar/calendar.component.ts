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
import { MatTableDataSource } from "@angular/material";


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
  dataSource: MatTableDataSource<Calendar> | null;
  

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private apiService: ApiClientService) {
    this.dataSource = new MatTableDataSource<Calendar>([]);
    this.dataSource.paginator = this.paginator;
  }

  ngOnInit() {
    var items = this.apiService.findAllCalendars();


    items.subscribe(calendars => {
      this.dataSource = new MatTableDataSource<Calendar>(calendars);
      this.dataSource.paginator = this.paginator;
      this.dataSource.filter = "";
    })

  }

  ngOnDestroy() {
  }

  createCalendar(){
    
  }
}