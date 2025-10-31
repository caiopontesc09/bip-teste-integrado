import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { BeneficioService, Beneficio } from '../services/beneficio.service';

@Component({
  selector: 'app-beneficio-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatButtonModule, MatIconModule, MatCardModule, MatSnackBarModule],
  template: `
    <mat-card class="beneficio-card">
      <mat-card-header>
        <mat-card-title>Lista de Benefícios</mat-card-title>
        <mat-card-subtitle>Gerencie seus benefícios</mat-card-subtitle>
      </mat-card-header>
      <mat-card-content>
        <div class="actions">
          <button mat-raised-button color="primary" (click)="loadBeneficios()">
            <mat-icon>refresh</mat-icon>
            Atualizar
          </button>
        </div>
        
        <table mat-table [dataSource]="beneficios" class="beneficios-table">
          <ng-container matColumnDef="id">
            <th mat-header-cell *matHeaderCellDef>ID</th>
            <td mat-cell *matCellDef="let beneficio">{{beneficio.id}}</td>
          </ng-container>

          <ng-container matColumnDef="nome">
            <th mat-header-cell *matHeaderCellDef>Nome</th>
            <td mat-cell *matCellDef="let beneficio">{{beneficio.nome}}</td>
          </ng-container>

          <ng-container matColumnDef="descricao">
            <th mat-header-cell *matHeaderCellDef>Descrição</th>
            <td mat-cell *matCellDef="let beneficio">{{beneficio.descricao}}</td>
          </ng-container>

          <ng-container matColumnDef="valor">
            <th mat-header-cell *matHeaderCellDef>Valor</th>
            <td mat-cell *matCellDef="let beneficio">{{beneficio.valor | currency:'BRL':'symbol':'1.2-2'}}</td>
          </ng-container>

          <ng-container matColumnDef="ativo">
            <th mat-header-cell *matHeaderCellDef>Status</th>
            <td mat-cell *matCellDef="let beneficio">
              <span class="status" [class.ativo]="beneficio.ativo" [class.inativo]="!beneficio.ativo">
                {{beneficio.ativo ? 'Ativo' : 'Inativo'}}
              </span>
            </td>
          </ng-container>

          <ng-container matColumnDef="acoes">
            <th mat-header-cell *matHeaderCellDef>Ações</th>
            <td mat-cell *matCellDef="let beneficio">
              <button mat-icon-button color="warn" (click)="deleteBeneficio(beneficio.id!)">
                <mat-icon>delete</mat-icon>
              </button>
            </td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
          <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
        </table>
      </mat-card-content>
    </mat-card>
  `,
  styles: [`
    .beneficio-card {
      margin: 20px;
      box-shadow: 0 4px 8px rgba(0,0,0,0.1);
      background-color: white;
    }
    .actions {
      margin-bottom: 20px;
    }
    .beneficios-table {
      width: 100%;
      background-color: white;
    }
    .status {
      padding: 4px 8px;
      border-radius: 12px;
      font-size: 12px;
      font-weight: 500;
    }
    .status.ativo {
      background-color: #e8f5e8;
      color: #2e7d32;
    }
    .status.inativo {
      background-color: #ffebee;
      color: #c62828;
    }
  `]
})
export class BeneficioListComponent implements OnInit {
  beneficios: Beneficio[] = [];
  displayedColumns: string[] = ['id', 'nome', 'descricao', 'valor', 'ativo', 'acoes'];

  constructor(
    private beneficioService: BeneficioService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.loadBeneficios();
  }

  loadBeneficios() {
    this.beneficioService.getBeneficios().subscribe({
      next: (data) => {
        this.beneficios = data;
        this.snackBar.open('Benefícios carregados com sucesso', 'Fechar', { duration: 3000 });
      },
      error: (error) => {
        this.snackBar.open('Erro ao carregar benefícios', 'Fechar', { duration: 3000 });
      }
    });
  }

  deleteBeneficio(id: number) {
    this.beneficioService.deleteBeneficio(id).subscribe({
      next: () => {
        this.loadBeneficios();
        this.snackBar.open('Benefício excluído com sucesso', 'Fechar', { duration: 3000 });
      },
      error: (error) => {
        this.snackBar.open('Erro ao excluir benefício', 'Fechar', { duration: 3000 });
      }
    });
  }
}