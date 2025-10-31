import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { BeneficioListComponent } from './components/beneficio-list.component';
import { BeneficioFormComponent } from './components/beneficio-form.component';
import { TransferFormComponent } from './components/transfer-form.component';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet, 
    MatToolbarModule, 
    MatIconModule, 
    MatTabsModule,
    BeneficioListComponent,
    BeneficioFormComponent,
    TransferFormComponent
  ],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('Sistema de Benefícios');
}
