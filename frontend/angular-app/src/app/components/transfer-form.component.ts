import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { BeneficioService, Beneficio } from '../services/beneficio.service';

@Component({
  selector: 'app-transfer-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule
  ],
  template: `
    <mat-card class="transfer-card">
      <mat-card-header>
        <mat-card-title>Transferência de Valores</mat-card-title>
        <mat-card-subtitle>Transfira valores entre benefícios</mat-card-subtitle>
      </mat-card-header>
      <mat-card-content>
        <form [formGroup]="transferForm" (ngSubmit)="onSubmit()" class="transfer-form">
          <mat-form-field appearance="fill" class="full-width">
            <mat-label>Benefício de Origem</mat-label>
            <mat-select formControlName="fromId">
              <mat-option *ngFor="let beneficio of beneficios" [value]="beneficio.id">
                {{beneficio.nome}} ({{beneficio.valor | currency:'BRL':'symbol':'1.2-2'}})
              </mat-option>
            </mat-select>
            <mat-error *ngIf="transferForm.get('fromId')?.hasError('required')">
              Selecione o benefício de origem
            </mat-error>
          </mat-form-field>

          <mat-form-field appearance="fill" class="full-width">
            <mat-label>Benefício de Destino</mat-label>
            <mat-select formControlName="toId">
              <mat-option *ngFor="let beneficio of beneficios" [value]="beneficio.id">
                {{beneficio.nome}} ({{beneficio.valor | currency:'BRL':'symbol':'1.2-2'}})
              </mat-option>
            </mat-select>
            <mat-error *ngIf="transferForm.get('toId')?.hasError('required')">
              Selecione o benefício de destino
            </mat-error>
          </mat-form-field>

          <mat-form-field appearance="fill" class="full-width">
            <mat-label>Valor a Transferir</mat-label>
            <input matInput type="number" formControlName="amount" step="0.01">
            <span matPrefix>R$ </span>
            <mat-error *ngIf="transferForm.get('amount')?.hasError('required')">
              Valor é obrigatório
            </mat-error>
            <mat-error *ngIf="transferForm.get('amount')?.hasError('min')">
              Valor deve ser maior que zero
            </mat-error>
          </mat-form-field>

          <div class="form-actions">
            <button mat-raised-button color="accent" type="submit" [disabled]="transferForm.invalid">
              <mat-icon>swap_horiz</mat-icon>
              Transferir
            </button>
            <button mat-button type="button" (click)="resetForm()">
              <mat-icon>clear</mat-icon>
              Limpar
            </button>
          </div>
        </form>
      </mat-card-content>
    </mat-card>
  `,
  styles: [`
    .transfer-card {
      margin: 20px;
      box-shadow: 0 4px 8px rgba(0,0,0,0.1);
      background-color: white;
    }
    .transfer-form {
      display: flex;
      flex-direction: column;
      gap: 16px;
    }
    .full-width {
      width: 100%;
    }
    .form-actions {
      display: flex;
      gap: 12px;
      margin-top: 16px;
    }
    ::ng-deep .mat-mdc-select-panel {
      background-color: #f5f5f5 !important;
    }
  `]
})
export class TransferFormComponent implements OnInit {
  transferForm: FormGroup;
  beneficios: Beneficio[] = [];

  constructor(
    private fb: FormBuilder,
    private beneficioService: BeneficioService,
    private snackBar: MatSnackBar
  ) {
    this.transferForm = this.fb.group({
      fromId: ['', Validators.required],
      toId: ['', Validators.required],
      amount: ['', [Validators.required, Validators.min(0.01)]]
    });
  }

  ngOnInit() {
    this.loadBeneficios();
  }

  loadBeneficios() {
    this.beneficioService.getBeneficios().subscribe({
      next: (data) => {
        this.beneficios = data;
      },
      error: (error) => {
        this.snackBar.open('Erro ao carregar benefícios', 'Fechar', { duration: 3000 });
      }
    });
  }

  onSubmit() {
    if (this.transferForm.valid) {
      const transferData = this.transferForm.value;
      
      if (transferData.fromId === transferData.toId) {
        this.snackBar.open('Benefício de origem e destino devem ser diferentes', 'Fechar', { duration: 3000 });
        return;
      }

      this.beneficioService.transfer(transferData).subscribe({
        next: (message) => {
          this.snackBar.open(message, 'Fechar', { duration: 3000 });
          this.resetForm();
          this.loadBeneficios();
        },
        error: (error) => {
          this.snackBar.open('Erro na transferência: ' + error.error, 'Fechar', { duration: 3000 });
        }
      });
    }
  }

  resetForm() {
    this.transferForm.reset();
  }
}