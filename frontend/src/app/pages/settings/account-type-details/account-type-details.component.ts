import { Component, OnInit } from '@angular/core';
import { ROUTE_TRANSITION } from '../../../app.animation';
import { MatDialog, MatSnackBar } from '@angular/material';
import { MatTableDataSource } from "@angular/material";
import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription';
import { PositionType } from 'app/models/positiontype.model';
import { AmountType } from 'app/models/amounttype.model';
import { DataSource } from '@angular/cdk/collections';
import { AccountType } from 'app/models/accounttype.model';
import { PositionTypeDetailsComponent } from 'app/pages/settings/account-type-details/position-type-details/position-type-details.component';
import { ApiClientService } from 'app/api-client-service';
import { SelectedAccountTypeService } from 'app/selected-account-type.service';
import { FormGroup, FormBuilder, FormControl,Validators } from '@angular/forms';
import { retry } from 'rxjs/operator/retry';
import { DateType } from 'app/models/datetype.model';
import { TransactionType } from 'app/models/transactiontype.model';
import { RateType } from 'app/models/ratetype.model';
import { ScheduleType } from 'app/models/scheduletype.model';
import { OptionType } from 'app/models/optiontype.model';
import { InstalmentType } from 'app/models/instalmenttype.model';
import { ScheduledTransaction } from 'app/models/scheduledtransaction.model';


@Component({
  selector: 'vr-account-type-details',
  templateUrl: './account-type-details.component.html',
  styleUrls: ['./account-type-details.component.scss'],
  animations: [...ROUTE_TRANSITION],
  host: { '[@routeTransition]': '' }
})
export class AccountTypeDetailsComponent implements OnInit {

  displayedColumns = ['image','labelName','propertyName','actions'];


  positionDataSource: MatTableDataSource<PositionType> | null;
  amountDataSource: MatTableDataSource<AmountType> | null;
  dateDataSource: MatTableDataSource<DateType> | null;
  transactionDataSource: MatTableDataSource<TransactionType> | null;
  rateDataSource: MatTableDataSource<RateType> | null;
  scheduleDataSource: MatTableDataSource<ScheduleType> | null;
  optionsDataSource: MatTableDataSource<OptionType> | null;
  instalmentsDataSource: MatTableDataSource<InstalmentType> | null;
  scheduledTransactionsDataSource: MatTableDataSource<ScheduledTransaction> | null;

  accountType: AccountType;

  form: FormGroup;

  constructor(
    public composeDialog: MatDialog,
    private snackBar: MatSnackBar,
    private apiService: ApiClientService,
    private selectedAccountTypeService: SelectedAccountTypeService,
    private fb: FormBuilder) { 
      this.createForm();

  }

  createForm(){
    this.form = this.fb.group({
      className: new FormControl ({ value:''}), // <--- the FormControl called "name"
      labelName: new FormControl ({ value:'' })
    });
  }

  ngOnInit() {
    this.accountType = this.selectedAccountTypeService.get();
    this.form.setValue({
      className: this.accountType.className,
      labelName: this.accountType.labelName
    });

    this.positionDataSource = new MatTableDataSource<PositionType>(this.accountType.positionTypes);
    this.amountDataSource = new MatTableDataSource<AmountType>(this.accountType.amountTypes);
    this.dateDataSource = new MatTableDataSource<DateType> (this.accountType.dateTypes);
    this.transactionDataSource= new MatTableDataSource<TransactionType>(this.accountType.transactionTypes);
    this.rateDataSource= new MatTableDataSource<RateType>(this.accountType.rateTypes);
    this.scheduleDataSource= new MatTableDataSource<ScheduleType>(this.accountType.scheduleTypes);
    this.optionsDataSource= new  MatTableDataSource<OptionType> (this.accountType.optionTypes);
    this.instalmentsDataSource= new MatTableDataSource<InstalmentType>(this.accountType.instalmentTypes);
    this.scheduledTransactionsDataSource = new MatTableDataSource<ScheduledTransaction>(this.accountType.scheduledTransactions);
  
  }

  ngOnDestroy() {

  }

  createPositionType() {
    const dialogRef = this.composeDialog.open(PositionTypeDetailsComponent);
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.snackBar.open(result, null, {
          duration: 3000
        });
      }
    });
  }

  updatePositionType(position:PositionType){

  }

  deletePositionType(position:PositionType){

  }

  createAmountType() {
    /*const dialogRef = this.composeDialog.open(PositionTypeDetailsComponent);
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.snackBar.open(result, null, {
          duration: 3000
        });
      }
    });*/
  }

}