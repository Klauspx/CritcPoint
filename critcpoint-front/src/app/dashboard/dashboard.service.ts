import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export interface AlertaDashboard {
  id: number;
  codigoAcao: string;
  valorMinimo: number;
  valorMaximo: number;
  valorAtual: number;
  tipoAtivo?: string;
}

export interface NovoAlertaPayload {
  codigoAcao: string;
  valorMinimo: number;
  valorMaximo: number;
  tipoAtivo: 'ACAO' | 'FII';
  usuario: { id: number };
}

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  // Busca os alertas do usuário já com a cotação atual de cada ação
  getAlertas(usuarioId: number) {
    return this.http.get<AlertaDashboard[]>(`${this.baseUrl}/usuarios/${usuarioId}/dashboard`);
  }

  criarAlerta(payload: NovoAlertaPayload) {
    return this.http.post(`${this.baseUrl}/pontos-criticos`, payload);
  }

  deletarAlerta(id: number) {
    return this.http.delete(`${this.baseUrl}/pontos-criticos/${id}`, { responseType: 'text' });
  }
}
