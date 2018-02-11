import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { DataSource } from '@angular/cdk/table';
import { MatPaginator, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { MatTableDataSource } from "@angular/material";
import { Observable } from 'rxjs/Observable';
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
import { AccountType } from 'app/models/accounttype.model';
import { ApiClientService } from 'app/api-client-service';
import { Router} from "@angular/router";
import { SelectedAccountTypeService } from 'app/selected-account-type.service';
import { CreateAccountTypeComponent } from 'app/pages/settings/account-type/create-account-type/create-account-type.component';
import { saveAs } from 'file-saver/FileSaver';
import {  dump } from 'js-yaml';
import { ImportAccountComponent } from 'app/pages/settings/account-type/import-account/import-account.component';

@Component({
  selector: 'vr-account-type',
  templateUrl: './account-type.component.html',
  styleUrls: ['./account-type.component.scss'],
  animations: [...ROUTE_TRANSITION],
  host: { '[@routeTransition]': '' }
})
export class AccountTypeComponent implements OnInit {

  scrollbar: any;

  displayedColumns = ['image','labelName','className','actions'];
  dataSource: MatTableDataSource<AccountType> | null;
  items:Observable<AccountType[]>;


  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    public composeDialog: MatDialog,
    private apiService: ApiClientService,
    private router:Router,
    private selectedAccountTypeService: SelectedAccountTypeService,
    private snackBar: MatSnackBar ) {

      this.dataSource = new MatTableDataSource<AccountType>([]);
      this.dataSource.paginator = this.paginator;

  }

  ngOnInit() {
    //this.loadData();
    this.items = this.apiService.findAllAccountTypes();
    this.items.subscribe( accountTypes => {
      this.dataSource = new MatTableDataSource<AccountType>( accountTypes);
      this.dataSource.paginator = this.paginator;
      this.dataSource.filter = "";
    } )
  }

  ngOnDestroy() {
  }


  private saveToJson(accountType:AccountType) {
    const fileName = accountType.className + ".json";
    const contentDispositionHeader: string = 'Content-Disposition: attachment; filename="' + fileName + '"'

    const blob = new Blob([JSON.stringify(accountType)], { type: 'application/json' });
    saveAs(blob, fileName);
  }

  private saveToYaml(accountType:AccountType) {
    const fileName = accountType.className + ".yaml";
    const contentDispositionHeader: string = 'Content-Disposition: attachment; filename="' + fileName + '"'
    
    const blob = new Blob([dump(accountType)], { type: 'text/json' });
    saveAs(blob, fileName);
  }

  create(){
    const dialogRef = this.composeDialog.open(CreateAccountTypeComponent);
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.dataSource.data.push(result.object);
        this.dataSource.filter = "";
        this.snackBar.open(result.message, null, {
          duration: 3000
        });
      }
    });
  }

  import(){
    const dialogRef = this.composeDialog.open(ImportAccountComponent);
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.dataSource.data.push(result.object);
        this.dataSource.filter = "";
        this.snackBar.open(result.message, null, {
          duration: 3000
        });
      }
    });
  }

  update(accountType:AccountType ){
    this.selectedAccountTypeService.set(accountType);
    this.router.navigate(['settings/account-type-details']);
  }

  delete(accountType:AccountType){
    this.apiService.deleteAccountTypeByClassName(accountType.className)
      .subscribe(response => {
        var index= this.dataSource.data.findIndex((item:AccountType) => item.className===accountType.className);
        this.dataSource.data.splice(index,1);
        this.dataSource.filter = "";
        
        this.snackBar.open("deleted account type", null, {duration:3000});
      });
  }
}




