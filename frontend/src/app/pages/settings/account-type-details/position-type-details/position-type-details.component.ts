import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material';

@Component({
  selector: 'vr-position-type-details',
  templateUrl: './position-type-details.component.html',
  styleUrls: ['./position-type-details.component.scss']
})
export class PositionTypeDetailsComponent implements OnInit {

  constructor(
    private dialogRef: MatDialogRef<PositionTypeDetailsComponent>
  ) { }


  ngOnInit() {
  }

  send() {
    this.dialogRef.close('Position Type saved');
  }

}





