import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { DashboardService, AlertaDashboard } from './dashboard.service';

interface UsuarioLogado {
  id: number;
  nome: string;
  email: string;
}

interface CardMercado {
  nome: string;
  valor: string;
  variacao: string;
  alta: boolean;
}

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {
  usuario: UsuarioLogado | null = null;

  // Alertas reais, vindos do backend (código da ação, min/máx e preço atual)
  alertas: AlertaDashboard[] = [];
  carregandoAlertas = false;
  erroAlertas = '';

  // Cards de índices/moedas: sem endpoint próprio ainda, então ficam mockados por enquanto
  mercado: CardMercado[] = [
    { nome: 'IBOVESPA', valor: '118.540 pts', variacao: '+1.45%', alta: true },
    { nome: 'DÓLAR BRL', valor: 'R$ 4,98', variacao: '-0.62%', alta: false },
    { nome: 'S&P 500', valor: '4.310 pts', variacao: '+0.88%', alta: true },
    { nome: 'NASDAQ', valor: '13.180 pts', variacao: '+1.15%', alta: true }
  ];

  // Formulário de novo alerta
  mostrarFormulario = false;
  novoAlerta = { codigoAcao: '', valorMinimo: null as number | null, valorMaximo: null as number | null };
  criandoAlerta = false;
  erroFormulario = '';

  // Controle de exclusão
  excluindoId: number | null = null;

  constructor(
    private dashboardService: DashboardService,
    private router: Router
  ) {}

  ngOnInit() {
    const usuarioSalvo = localStorage.getItem('usuarioLogado');

    if (!usuarioSalvo) {
      this.router.navigate(['/login']);
      return;
    }

    this.usuario = JSON.parse(usuarioSalvo);
    this.carregarAlertas();
  }

  carregarAlertas() {
    if (!this.usuario) return;

    this.carregandoAlertas = true;
    this.erroAlertas = '';

    this.dashboardService.getAlertas(this.usuario.id).subscribe({
      next: (alertas) => {
        this.alertas = alertas;
        this.carregandoAlertas = false;
      },
      error: (erro) => {
        console.error('Erro ao buscar alertas:', erro);
        this.erroAlertas = 'Não foi possível carregar seus alertas. Verifique se o backend está rodando.';
        this.carregandoAlertas = false;
      }
    });
  }

  // Calcula o quanto o preço atual está da faixa (pra colorir a linha da tabela)
  statusAlerta(alerta: AlertaDashboard): 'alta' | 'baixa' | 'normal' | 'indisponivel' {
    if (!alerta.valorAtual || alerta.valorAtual <= 0) return 'indisponivel';
    if (alerta.valorAtual >= alerta.valorMaximo) return 'alta';
    if (alerta.valorAtual <= alerta.valorMinimo) return 'baixa';
    return 'normal';
  }

  abrirFormulario() {
    this.mostrarFormulario = true;
    this.erroFormulario = '';
    this.novoAlerta = { codigoAcao: '', valorMinimo: null, valorMaximo: null };
  }

  fecharFormulario() {
    this.mostrarFormulario = false;
  }

  criarAlerta() {
    this.erroFormulario = '';

    if (!this.usuario) return;

    const { codigoAcao, valorMinimo, valorMaximo } = this.novoAlerta;

    if (!codigoAcao || valorMinimo === null || valorMaximo === null) {
      this.erroFormulario = 'Preencha o código da ação e os valores mínimo e máximo.';
      return;
    }

    if (valorMinimo >= valorMaximo) {
      this.erroFormulario = 'O valor mínimo precisa ser menor que o valor máximo.';
      return;
    }

    this.criandoAlerta = true;

    this.dashboardService.criarAlerta({
      codigoAcao: codigoAcao.toUpperCase().trim(),
      valorMinimo,
      valorMaximo,
      usuario: { id: this.usuario.id }
    }).subscribe({
      next: () => {
        this.criandoAlerta = false;
        this.mostrarFormulario = false;
        this.carregarAlertas();
      },
      error: (erro) => {
        this.criandoAlerta = false;
        this.erroFormulario = typeof erro.error === 'string'
          ? erro.error
          : 'Não foi possível criar o alerta. Tente novamente.';
        console.error('Erro ao criar alerta:', erro);
      }
    });
  }

  excluirAlerta(alerta: AlertaDashboard) {
    const confirmar = confirm(`Excluir o alerta de ${alerta.codigoAcao}?`);
    if (!confirmar) return;

    this.excluindoId = alerta.id;

    this.dashboardService.deletarAlerta(alerta.id).subscribe({
      next: () => {
        this.alertas = this.alertas.filter(a => a.id !== alerta.id);
        this.excluindoId = null;
      },
      error: (erro) => {
        console.error('Erro ao excluir alerta:', erro);
        alert('Não foi possível excluir o alerta.');
        this.excluindoId = null;
      }
    });
  }

  sair() {
    localStorage.removeItem('usuarioLogado');
    this.router.navigate(['/login']);
  }
}
