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

  // Cards de índices/moedas (iniciam vazios/carregando)
  mercado: CardMercado[] = [
    { nome: 'IBOVESPA', valor: 'Carregando...', variacao: '--', alta: true },
    { nome: 'DÓLAR BRL', valor: 'Carregando...', variacao: '--', alta: true },
    { nome: 'EURO BRL', valor: 'Carregando...', variacao: '--', alta: true },
    { nome: 'BITCOIN BRL', valor: 'Carregando...', variacao: '--', alta: true }
  ];

  // Formulário de novo alerta
  mostrarFormulario = false;
  novoAlerta = { codigoAcao: '', valorMinimo: null as number | null, valorMaximo: null as number | null };
  criandoAlerta = false;
  erroFormulario = '';

  // Controle de exclusão
  excluindoId: number | null = null;
  mostrarModalExclusao = false;
  alertaParaExcluir: AlertaDashboard | null = null;
  // Controle do Toast (Notificação)
  toastMensagem: string = '';
  toastTipo: 'sucesso' | 'erro' = 'sucesso';
  toastVisivel: boolean = false;
  toastTimeout: any;
  temaEscuro: boolean = false;

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

    const temaSalvo = localStorage.getItem('temaDashboard');
    if (temaSalvo === 'escuro') {
      this.temaEscuro = true;
    }
    
    this.carregarAlertas();
    
    this.carregarDadosDoMercado();

    setInterval(() => {
      this.carregarDadosDoMercado();
    }, 30000);
  }

async carregarDadosDoMercado() {
    // 1. Função auxiliar para formatar a porcentagem
    const formataVariacao = (valor: string | number) => {
      const numero = Number(valor);
      return (numero > 0 ? '+' : '') + numero.toFixed(2).replace('.', ',') + '%';
    };

    // 2. Preparamos variáveis temporárias (caso dê erro, elas assumem o valor de "Indisponível")
    let dadosIbov = { valor: 'Indisponível', variacao: '--', alta: false };
    let dadosDolar = { valor: 'Indisponível', variacao: '--', alta: false };
    let dadosEuro = { valor: 'Indisponível', variacao: '--', alta: false };
    let dadosBitcoin = { valor: 'Indisponível', variacao: '--', alta: false };

    // 3. Tenta buscar o IBOVESPA isoladamente
    try {
      const respostaBolsa = await fetch('https://api.hgbrasil.com/finance?format=json-cors');
      const jsonBolsa = await respostaBolsa.json();
      const ibovespa = jsonBolsa.results.stocks.IBOVESPA;
      
      dadosIbov = {
        valor: Number(ibovespa.points).toLocaleString('pt-BR') + ' pts',
        variacao: formataVariacao(ibovespa.variation),
        alta: Number(ibovespa.variation) >= 0
      };
    } catch (erro) {
      console.error('Erro ao carregar IBOVESPA:', erro);
    }

    // 4. Tenta buscar as Moedas isoladamente
    try {
      const respostaMoedas = await fetch('https://economia.awesomeapi.com.br/last/USD-BRL,EUR-BRL,BTC-BRL');
      const jsonMoedas = await respostaMoedas.json();

      dadosDolar = {
        valor: Number(jsonMoedas.USDBRL.ask).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
        variacao: formataVariacao(jsonMoedas.USDBRL.pctChange),
        alta: Number(jsonMoedas.USDBRL.pctChange) >= 0
      };

      dadosEuro = {
        valor: Number(jsonMoedas.EURBRL.ask).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
        variacao: formataVariacao(jsonMoedas.EURBRL.pctChange),
        alta: Number(jsonMoedas.EURBRL.pctChange) >= 0
      };

      dadosBitcoin = {
        valor: Number(jsonMoedas.BTCBRL.ask).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
        variacao: formataVariacao(jsonMoedas.BTCBRL.pctChange),
        alta: Number(jsonMoedas.BTCBRL.pctChange) >= 0
      };
    } catch (erro) {
      console.error('Erro ao carregar Moedas:', erro);
    }

    // 5. Atualiza a tela (O Angular detecta a mudança aqui)
    this.mercado = [
      { nome: 'IBOVESPA', valor: dadosIbov.valor, variacao: dadosIbov.variacao, alta: dadosIbov.alta },
      { nome: 'DÓLAR BRL', valor: dadosDolar.valor, variacao: dadosDolar.variacao, alta: dadosDolar.alta },
      { nome: 'EURO BRL', valor: dadosEuro.valor, variacao: dadosEuro.variacao, alta: dadosEuro.alta },
      { nome: 'BITCOIN BRL', valor: dadosBitcoin.valor, variacao: dadosBitcoin.variacao, alta: dadosBitcoin.alta }
    ];
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
        this.mostrarToast('Alerta criado com sucesso!', 'sucesso');
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

  // 1. Abre o modal de confirmação
  excluirAlerta(alerta: AlertaDashboard) {
    this.alertaParaExcluir = alerta;
    this.mostrarModalExclusao = true;
  }

  // 2. Fecha o modal se o usuário desistir
  cancelarExclusao() {
    this.mostrarModalExclusao = false;
    this.alertaParaExcluir = null;
  }

  // 3. Executa a exclusão de fato quando ele clica em "Sim"
  confirmarExclusao() {
    if (!this.alertaParaExcluir) return;

    this.excluindoId = this.alertaParaExcluir.id;
    const id = this.alertaParaExcluir.id;

    this.dashboardService.deletarAlerta(id).subscribe({
      next: () => {
        this.alertas = this.alertas.filter(a => a.id !== id);
        this.excluindoId = null;
        this.mostrarModalExclusao = false;
        this.alertaParaExcluir = null;
        this.mostrarToast('Alerta excluído!', 'sucesso');
      },
      error: (erro) => {
        console.error('Erro ao excluir alerta:', erro);
        this.excluindoId = null;
        this.mostrarModalExclusao = false;
        this.alertaParaExcluir = null;
        this.mostrarToast('Não foi possível excluir o alerta.', 'erro');
      }
    });
  }

  sair() {
    localStorage.removeItem('usuarioLogado');
    this.router.navigate(['/login']);
  }
  
  // Alterna entre tema claro e escuro
  toggleTema() {
    this.temaEscuro = !this.temaEscuro;
    localStorage.setItem('temaDashboard', this.temaEscuro ? 'escuro' : 'claro');
  }

  // Função para exibir a notificação bonita
  mostrarToast(mensagem: string, tipo: 'sucesso' | 'erro') {
    this.toastMensagem = mensagem;
    this.toastTipo = tipo;
    this.toastVisivel = true;

    // Limpa o timer anterior se o usuário clicar em várias coisas rápido
    if (this.toastTimeout) {
      clearTimeout(this.toastTimeout);
    }

    // Faz a mensagem sumir sozinha depois de 3.5 segundos
    this.toastTimeout = setTimeout(() => {
      this.toastVisivel = false;
    }, 3500);
  }
}
