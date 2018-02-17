import { Component, OnInit } from '@angular/core';
import { ROUTE_TRANSITION } from '../../../../app.animation';
import { AccountCreateService } from '../../../../account-create.service'
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { ApiClientService } from 'app/api-client-service';
import { Router } from '@angular/router';
import { InstalmentSet, Account, AccountType } from 'app/models';
import { InstalmentValue } from 'app/models/instalmentvalue.model';
import { MatTableDataSource, MatDialog,MatSnackBar } from '@angular/material';
import { EditInstalmentComponent } from 'app/pages/data/instalment/edit-instalment/edit-instalment.component';

@Component({
  selector: 'vr-edit-account-instalments',
  templateUrl: './edit-account-instalments.component.html',
  styleUrls: ['./edit-account-instalments.component.scss']
})
export class EditAccountInstalmentsComponent implements OnInit {

  accountType: AccountType;
  account: Account;
  dataSource: MatTableDataSource<InstalmentValue> | null;
  displayedColumns = ['data','amount','hasFixedValue','actions'];

  constructor(public accountCreateService: AccountCreateService,
    private fb: FormBuilder,
    public apiClient: ApiClientService,
    public router: Router,
    public composeDialog: MatDialog,
    private snackBar: MatSnackBar ) { }

  ngOnInit() {
    this.accountType = this.accountCreateService.getAccountType();
    this.account = this.accountCreateService.getAccount();
    this.dataSource = new MatTableDataSource<InstalmentValue>(this.getInstalmentSetValues(this.account));
  }

  getInstalmentSetValues(account:Account):InstalmentValue[] {
    var keys = Object.keys(account.instalmentSets);

    var set:InstalmentSet  = account.instalmentSets[keys[0]].instalments;

    var dates: any[] = Object.keys(set);

    var instalments = new Array<InstalmentValue>();

    dates.forEach((data: string) => {
      var value:InstalmentValue = set[data];
      var instalmentValue = new InstalmentValue();
      instalmentValue.from(value, data);

      instalments.push(instalmentValue);
    });

    return instalments;
  }

  create(){
    //show dialog to create new instalment value
  }

  update(instalmentValue:InstalmentValue ){

      const dialogRef = this.composeDialog.open(EditInstalmentComponent);

      dialogRef.componentInstance.instalmentValue = instalmentValue;

      dialogRef.afterClosed().subscribe(result => {
        this.onCalculate();
      });

  }

  delete(instalmentValue:InstalmentValue ){

  }

  mapFormToAccount(){

    var keys = Object.keys(this.account.instalmentSets);

    var set:InstalmentSet  = this.account.instalmentSets[keys[0]].instalments;

    this.dataSource.data.forEach((value:InstalmentValue)=> {
      set[value.data].amount = value.amount;
      set[value.data].hasFixedValue = value.hasFixedValue;
    })

  }

  onPreviousStep(){
    this.router.navigate(['/data/create-account/schedules']);
  }

  onCalculate(){

    this.mapFormToAccount();

    this.apiClient.getSolvedInstalments(this.account).subscribe( result => {
      this.account= result.body;
      //this.dataSource = new MatTableDataSource<InstalmentValue>(this.getInstalmentSetValues(this.account));
      this.dataSource.data.splice(0, this.dataSource.data.length);
      var instalments = this.getInstalmentSetValues(this.account);

      instalments.forEach(
        (instalment:InstalmentValue)=> this.dataSource.data.push(instalment));

      this.dataSource.filter = "";

    }, error => {
      var errorMessage:string= "Instalments can't be calculated";

      if(error.status = 422){
        if(error.error.globalErrors != null && error.error.globalErrors.length>0 ){
          errorMessage = error.error.globalErrors[0].message;
        }
      }

      this.snackBar.open(errorMessage, null, {duration:3000});
    });
  }

  onSave(){
    this.mapFormToAccount();

    this.account.active = true;

    this.apiClient.saveAccount (this.account).subscribe( result => {
      this.account= result.body;
      this.accountCreateService.setAccount(this.account);
      this.router.navigate(['/']);
    },
  error => {

    var errorMessage:string= "Account can't be saved";

      if(error.status = 422){
        if(error.error.globalErrors != null && error.error.globalErrors.length>0 ){
          errorMessage = error.error.globalErrors[0].message;
        }
      }

      this.snackBar.open(errorMessage, null, {duration:3000});

  });
  }


}
