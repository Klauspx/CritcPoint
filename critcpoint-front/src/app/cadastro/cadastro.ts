import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { ToastService } from '../shared/toast.service';
import { LoginService } from '../login/login.service'; 

@Component({
  selector: 'app-cadastro',
  imports: [FormsModule, RouterLink],
  templateUrl: './cadastro.html',
  styleUrl: './cadastro.css'
})
export class CadastroComponent {
  isDarkMode = false;
  
  // Nossas variáveis para o cadastro
  nome = '';
  email = '';
  senha = '';

  constructor(
    private loginService: LoginService,
    private router: Router,
    private toastService: ToastService
  ) {}

  toggleTheme() {
    this.isDarkMode = !this.isDarkMode;
  }

  cadastrar() {
    const pacoteDeDados = {
      nome: this.nome,
      email: this.email,
      senha: this.senha
    };

    console.log("Enviando pacote de cadastro...", pacoteDeDados);

        this.loginService.enviarParaOJava(pacoteDeDados).subscribe({
      next: (resposta) => {
        this.toastService.success("Conta criada com sucesso! Faça login pra continuar.");
        this.router.navigate(['/login']);
      },
      error: (erro) => {
        console.error("Erro no cadastro:", erro);
        const mensagem = typeof erro.error === 'string'
          ? erro.error
          : "Erro ao conectar com o servidor.";
        this.toastService.error(mensagem);
      }
    });
  }
}