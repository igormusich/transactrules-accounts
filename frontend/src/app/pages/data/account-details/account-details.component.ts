import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { ApiClientService } from 'app/api-client-service';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { Account, AccountType, Transaction, InstalmentValue, InstalmentSet , PositionType} from 'app/models';
import { MatTableDataSource , MatPaginator} from "@angular/material";
import { Observable } from 'rxjs/Observable';
import { HttpResponse } from '@angular/common/http/src/response';
import { ROUTE_TRANSITION } from '../../../app.animation';
import { toDate, toIsoString }  from 'app/core/utils/format-date';
import { forEach } from '@angular/router/src/utils/collection';

@Component({
  selector: 'vr-account-details',
  templateUrl: './account-details.component.html',
  styleUrls: ['./account-details.component.scss'],
  animations: [...ROUTE_TRANSITION]
})
export class AccountDetailsComponent implements OnInit {

  accountNumber: string;
  account: Account;
  accountType: AccountType;
  form: FormGroup;
  transactionQueryForm: FormGroup;

  fromDate:string;
  toDate:string;
  positionType1:string;

  transactionDataSource: MatTableDataSource<Transaction> | null;
  instalmentDataSource: MatTableDataSource<InstalmentValue> | null;

  transactions: Observable<HttpResponse<Transaction[]>>;

  positionTypes: string[] = [];

  transactionDisplayedColumns = ['image', 'actionDate', 'valueDate', 'transactionType', 'amount', 'balance', 'actions'];
  instalmentDisplayedColumns = ['data', 'amount', 'hasFixedValue', 'actions'];

  @ViewChild(MatPaginator) transactionPaginator: MatPaginator;
  @ViewChild('instalmentPaginator') instalmentPaginator: MatPaginator;

  constructor(private route: ActivatedRoute,
    public apiClient: ApiClientService,
    private fb: FormBuilder) {

    this.transactionDataSource = new MatTableDataSource<Transaction>([]);
    this.transactionDataSource.paginator = this.transactionPaginator;
    this.instalmentDataSource = new MatTableDataSource<InstalmentValue>([]);
    this.instalmentDataSource.paginator = this.instalmentPaginator;

    this.route.params.subscribe(
      params => {
        this.accountNumber = params['accountNumber'];


      });
  }

  getInstalmentSetValues(account: Account): InstalmentValue[] {
    var keys = Object.keys(account.instalmentSets);

    var set: InstalmentSet = account.instalmentSets[keys[0]].instalments;

    var dates: any[] = Object.keys(set);

    var instalments = new Array<InstalmentValue>();

    dates.forEach((data: string) => {
      var value: InstalmentValue = set[data];
      var instalmentValue = new InstalmentValue();
      instalmentValue.from(value, data);

      instalments.push(instalmentValue);
    });

    return instalments;
  }


  parseForm(formValues:any){

    this.fromDate= toIsoString(this.form.value.fromDate);
    this.toDate = toIsoString(this.form.value.toDate);
    this.positionType1 = this.form.value.positionType1;

    if(this.form.valid){

    };

  }

  createForm() {

  }

  createTransaction() {

  }

  reverseTransaction(transaction: Transaction) {

  }

  getStartOfMonth(){
    return "2018-02-01";
  }

  getEndOfMonth(){
    return "2018-02-28";
  }

  getPrincipalPosition(positionTypes:PositionType[] ):string{
    var principal:PositionType = positionTypes.find(pt=> pt.principal === true);

    return principal.propertyName;
  }

  ngOnInit() {

    this.form = this.fb.group({
      accountNumber: new FormControl({ value: '' }), // <--- the FormControl called "name"
      active: new FormControl({ value: '' }),
      dateActivated: new FormControl({ value: ''}),
      accountTypeName: new FormControl({ value: ''})
    });

    this.transactionQueryForm = this.fb.group({
      fromDate: new FormControl({value: ''}),
      toDate: new FormControl({value: ''}),
      positionType1: new FormControl({value: ''})
    });

    this.apiClient.findAccountByAccountNumber(this.accountNumber).subscribe(result => {
      this.account = result.body;
      this.instalmentDataSource.data = this.getInstalmentSetValues(this.account);
      this.instalmentDataSource.paginator = this.instalmentPaginator;
      this.instalmentDataSource.filter= "";

      this.form.setValue(
        {
          accountNumber: this.account.accountNumber,
          accountTypeName: this.account.accountTypeName,
          active: this.account.active,
          dateActivated: this.account.dateActivated
        }
      );

      var accountTypeObservable = this.apiClient.getAccountType(this.account.accountTypeName);

      accountTypeObservable.subscribe(response=> {
        this.positionTypes = response.body.positionTypes.map(pt=> pt.propertyName);

        var formData = {
          fromDate: toDate(this.getStartOfMonth()),
          toDate: toDate(this.getEndOfMonth()),
          positionType1: this.getPrincipalPosition(response.body.positionTypes)
        };

        this.transactionQueryForm.setValue(formData);

      })
    });


  }

  searchTransactions(){

    this.parseForm(this.form.value);

    this.transactions = this.apiClient.getTransactions(this.accountNumber, this.getStartOfMonth(), this.getEndOfMonth(), this.positionType1 )
    this.transactions.subscribe(response => {
      this.transactionDataSource.data = response.body;
      this.transactionDataSource.paginator = this.transactionPaginator;
      this.transactionDataSource.filter = "";
    })
  }

}
