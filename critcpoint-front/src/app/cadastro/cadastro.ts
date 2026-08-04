import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
// Importamos o nosso carteiro que já está configurado na pasta login
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

  constructor(private loginService: LoginService) {}

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
        alert("Show! Usuário CADASTRADO com sucesso no banco de dados!");
        // Depois vamos ensinar o Angular a voltar para o Login sozinho aqui!
      },
      error: (erro) => {
        console.error("Erro no cadastro:", erro);
        alert("Erro ao conectar com o Java. Veja o F12!");
      }
    });
  }
}