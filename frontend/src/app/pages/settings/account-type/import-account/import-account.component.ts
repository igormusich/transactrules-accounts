import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { ApiClientService } from 'app/api-client-service';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { FileValidators } from 'app/core/input-file/file-validators';
import { FieldError } from 'app/errorModel/fielderror.model';
import { AccountType } from 'app/models/accounttype.model';
import { safeLoad } from 'js-yaml';
import { FileInput } from 'app/core/input-file/file-input.model';

@Component({
  selector: 'vr-import-account',
  templateUrl: './import-account.component.html',
  styleUrls: ['./import-account.component.scss']
})
export class ImportAccountComponent implements OnInit {

  form: FormGroup;
  accountType:AccountType;

  constructor(
    private dialogRef: MatDialogRef<ImportAccountComponent>,
    private apiService: ApiClientService,
    private fb: FormBuilder
  ) { }


  ngOnInit() {
    this.form = this.fb.group({
      requiredfile: [{ value: undefined, disabled: false }, [Validators.required, FileValidators.maxContentSize(524288)]],
      className: new FormControl (''), // <--- the FormControl called "name"
      labelName: new FormControl ('')
    });

    this.registerOnChange();
  }

  registerOnChange(): void {
    this.form.get('requiredfile').valueChanges.subscribe(
      value => {
        this.load();
    });
  }

  save() {

    this.accountType.className = this.form.get('className').value;
    this.accountType.labelName = this.form.get('labelName').value;

    this.apiService.createAccountType(this.accountType).subscribe( 
      response => {
        this.dialogRef.close( {
          'message':'Account Type created',
          'object': response.body
        } );
      },
      errorResponse => {
        if(errorResponse.status == 422){

          let buffer:string[] = [];
          
          for (let index in errorResponse.error.fieldErrors) {
            var fieldError: FieldError = errorResponse.error.fieldErrors[index];
            
            const control = this.form.get(fieldError.field);
            
            if(control == null){
               buffer.push('${fieldError.field}: ${fieldError.message}')
            }
            else {
              control.setErrors({ [fieldError.code] : true});
              control.markAsDirty();
            }

          }

          let globalErrorMessage = buffer.join('<br/>'); 
  
          this.validateAllFormFields(this.form);
        }
      });
  }

  load() {

    const fileControl : FileInput = this.form.get('requiredfile').value;

    const file: File =  fileControl.files[0];

    let reader = new FileReader();

    reader.onload = () => {
      this.accountType = safeLoad(reader.result);

      this.form.get('className').setValue(this.accountType.className);
      this.form.get('labelName').setValue(this.accountType.labelName);
    };

    reader.onerror = function(err) {
      console.log(err, err.error, file);
    }

    reader.readAsText(file);
  }

  validateAllFormFields(formGroup: FormGroup) {         //{1}
  Object.keys(formGroup.controls).forEach(field => {  //{2}
    const control = formGroup.get(field);             //{3}
    if (control instanceof FormControl) {             //{4}
      control.markAsTouched({ onlySelf: true });
    } else if (control instanceof FormGroup) {        //{5}
      this.validateAllFormFields(control);            //{6}
    }
  });
}


}
