import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { InstalmentValue } from 'app/models/instalmentvalue.model';
import { MatDialog, MatSnackBar, MatDialogRef} from '@angular/material';

@Component({
  selector: 'vr-edit-instalment',
  templateUrl: './edit-instalment.component.html',
  styleUrls: ['./edit-instalment.component.scss']
})
export class EditInstalmentComponent implements OnInit {

  public instalmentValue: InstalmentValue;
  form: FormGroup;

  constructor(private dialogRef: MatDialogRef<EditInstalmentComponent>,
    private fb: FormBuilder) { }

  ngOnInit() {
    if(!this.instalmentValue.hasFixedValue){
      this.instalmentValue.hasFixedValue = true;
    }

    this.form = this.fb.group({
      date: new FormControl ( { value:this.instalmentValue.date,disabled: true }  ),
      amount: new FormControl (this.instalmentValue.amount, Validators.required),
      hasFixedValue:  new FormControl (this.boolToString(this.instalmentValue.hasFixedValue), Validators.required)
    });
  }

  save(){
    var value = this.form.value;

    this.instalmentValue.amount = value.amount;
    this.instalmentValue.hasFixedValue = this.stringToBool(value.hasFixedValue);

    this.dialogRef.close( {
      'message':'new Account created',
      'instalmentValue': this.instalmentValue
    } );

  }

  boolToString(value:boolean):string{
    if(value){
      return "true";
    }

    return "false";
  }

  stringToBool(value:string):boolean{
    if(value=="false"){
      return false;
    }

    return true;
  }

}
