import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
// 1. Importamos a ferramenta que faz os links funcionarem sem recarregar a tela
import { RouterLink, Router } from '@angular/router';
import { LoginService } from './login.service';

@Component({
  selector: 'app-login',
  // 2. Colocamos o RouterLink aqui na lista de importações
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  isDarkMode = false;
  email = '';
  senha = '';
  erro = '';
  carregando = false;

  constructor(private loginService: LoginService, private router: Router) {}

  toggleTheme() {
    this.isDarkMode = !this.isDarkMode;
  }

  fazerLogin() {
    this.erro = '';

    if (!this.email || !this.senha) {
      this.erro = 'Preencha e-mail e senha.';
      return;
    }

    this.carregando = true;

    this.loginService.fazerLogin({ email: this.email, senha: this.senha }).subscribe({
      next: (usuario) => {
        this.carregando = false;
        // Guardamos o usuário logado pra usar no Dashboard (buscar os alertas dele, etc.)
        localStorage.setItem('usuarioLogado', JSON.stringify(usuario));
        this.router.navigate(['/dashboard']);
      },
      error: (erro) => {
        this.carregando = false;
        if (erro.status === 401) {
          this.erro = 'E-mail ou senha inválidos.';
        } else {
          this.erro = 'Não foi possível conectar ao servidor. Tente novamente.';
        }
        console.error('Erro no login:', erro);
      }
    });
  }
}