import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { BeneficioService } from '../services/beneficio.service';

@Component({
  selector: 'app-beneficio-form',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    MatCardModule, 
    MatFormFieldModule, 
    MatInputModule, 
    MatButtonModule, 
    MatIconModule,
    MatSnackBarModule
  ],
  template: `
    <mat-card class="form-card">
      <mat-card-header>
        <mat-card-title>Criar Novo Benefício</mat-card-title>
        <mat-card-subtitle>Adicione um novo benefício ao sistema</mat-card-subtitle>
      </mat-card-header>
      <mat-card-content>
        <form [formGroup]="beneficioForm" (ngSubmit)="onSubmit()" class="beneficio-form">
          <mat-form-field appearance="fill" class="full-width">
            <mat-label>Nome</mat-label>
            <input matInput formControlName="nome">
            <mat-error *ngIf="beneficioForm.get('nome')?.hasError('required')">
              Nome é obrigatório
            </mat-error>
          </mat-form-field>

          <mat-form-field appearance="fill" class="full-width">
            <mat-label>Descrição</mat-label>
            <textarea matInput formControlName="descricao" rows="3"></textarea>
          </mat-form-field>

          <mat-form-field appearance="fill" class="full-width">
            <mat-label>Valor</mat-label>
            <input matInput type="number" formControlName="valor" step="0.01">
            <span matPrefix>R$ </span>
            <mat-error *ngIf="beneficioForm.get('valor')?.hasError('required')">
              Valor é obrigatório
            </mat-error>
            <mat-error *ngIf="beneficioForm.get('valor')?.hasError('min')">
              Valor deve ser maior que zero
            </mat-error>
          </mat-form-field>

          <div class="form-actions">
            <button mat-raised-button color="primary" type="submit" [disabled]="beneficioForm.invalid">
              <mat-icon>add</mat-icon>
              Criar Benefício
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
    .form-card {
      margin: 20px;
      box-shadow: 0 4px 8px rgba(0,0,0,0.1);
      background-color: white;
    }
    .beneficio-form {
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
  `]
})
export class BeneficioFormComponent {
  beneficioForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private beneficioService: BeneficioService,
    private snackBar: MatSnackBar
  ) {
    this.beneficioForm = this.fb.group({
      nome: ['', Validators.required],
      descricao: [''],
      valor: ['', [Validators.required, Validators.min(0.01)]]
    });
  }

  onSubmit() {
    if (this.beneficioForm.valid) {
      const beneficio = {
        ...this.beneficioForm.value,
        ativo: true
      };

      this.beneficioService.createBeneficio(beneficio).subscribe({
        next: () => {
          this.snackBar.open('Benefício criado com sucesso', 'Fechar', { duration: 3000 });
          this.resetForm();
        },
        error: (error) => {
          this.snackBar.open('Erro ao criar benefício', 'Fechar', { duration: 3000 });
        }
      });
    }
  }

  resetForm() {
    this.beneficioForm.reset();
  }
}