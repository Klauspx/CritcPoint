import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { SimuladorService, SimulacaoResponse } from './simulador.service';

@Component({
  selector: 'app-simulador',
  imports: [CommonModule, FormsModule],
  templateUrl: './simulador.html',
  styleUrl: './simulador.css'
})
export class Simulador {

  tipoAtivo: 'ACAO' | 'FII' | 'TESOURO' = 'ACAO';
  codigo = '';
  valorAporte: number | null = null;
  tipoAporte: 'UNICO' | 'MENSAL' = 'UNICO';
  quantidadeMeses: number | null = null;

  carregando = false;
  erro = '';
  resultado: SimulacaoResponse | null = null;

  constructor(private simuladorService: SimuladorService, private router: Router) {}

  voltarParaDashboard() {
    this.router.navigate(['/dashboard']);
  }

  simular() {
    this.erro = '';
    this.resultado = null;

    if (!this.codigo || !this.valorAporte || !this.quantidadeMeses) {
      this.erro = 'Preencha todos os campos antes de simular.';
      return;
    }

    this.carregando = true;

      this.simuladorService.simular({
      tipoAtivo: this.tipoAtivo,
      codigo: this.codigo.trim(),
      valorAporte: this.valorAporte,
      tipoAporte: this.tipoAporte,
      quantidadeMeses: this.quantidadeMeses
    }).subscribe({
      next: (resposta) => {
        this.carregando = false;
        this.resultado = resposta;
      },
      error: (erro) => {
        this.carregando = false;
        this.erro = 'Não foi possível calcular a simulação. Verifique o código digitado.';
        console.error('Erro na simulação:', erro);
      }
    });
  }
}