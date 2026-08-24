import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export interface SimulacaoRequest {
  tipoAtivo: 'ACAO' | 'TESOURO';
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
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  simular(dados: SimulacaoRequest) {
    return this.http.post<SimulacaoResponse>(`${this.baseUrl}/simulacao`, dados);
  }
}