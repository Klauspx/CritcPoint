import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfirmService } from '../confirm.service';

@Component({
  selector: 'app-confirm-dialog',
  imports: [CommonModule],
  templateUrl: './confirm-dialog.html',
  styleUrl: './confirm-dialog.css'
})
export class ConfirmDialog {
  constructor(public confirmService: ConfirmService) {}
}