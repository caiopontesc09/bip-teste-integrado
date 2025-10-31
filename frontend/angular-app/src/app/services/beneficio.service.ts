import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Beneficio {
  id?: number;
  nome: string;
  descricao: string;
  valor: number;
  ativo: boolean;
}

export interface TransferRequest {
  fromId: number;
  toId: number;
  amount: number;
}

@Injectable({
  providedIn: 'root'
})
export class BeneficioService {
  private apiUrl = 'http://localhost:8080/api/v1/beneficios';

  constructor(private http: HttpClient) { }

  getBeneficios(): Observable<Beneficio[]> {
    return this.http.get<Beneficio[]>(this.apiUrl);
  }

  createBeneficio(beneficio: Beneficio): Observable<Beneficio> {
    return this.http.post<Beneficio>(this.apiUrl, beneficio);
  }

  deleteBeneficio(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  transfer(transferData: TransferRequest): Observable<string> {
    return this.http.post(`${this.apiUrl}/transfer`, transferData, { responseType: 'text' });
  }
}