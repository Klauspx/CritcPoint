import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

export interface SimulacaoRequest {
  tipoAtivo: 'ACAO' | 'FII' | 'TESOURO';
  codigo: string;
  valorAporte: number;
  tipoAporte: 'UNICO' | 'MENSAL';
  quantidadeMeses: number;
}

export interface SimulacaoResponse {
  taxaAnualUtilizada: number;
  valorInvestido: number;
  valorFinal: number;
  totalRendimentos: number;
}

@Injectable({
  providedIn: 'root'
})
export class SimuladorService {
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  simular(dados: SimulacaoRequest) {
    return this.http.post<SimulacaoResponse>(`${this.baseUrl}/simulacao`, dados);
  }
}