import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
// 1. Importamos o nosso arquivo Service
import { LoginService } from './login.service'; 

@Component({
  selector: 'app-login',
  imports: [FormsModule], 
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  isDarkMode = false;
  email = '';
  senha = '';

  // 2. Injetamos o Entregador aqui no construtor para podermos usá-lo
  constructor(private loginService: LoginService) {}

  toggleTheme() {
    this.isDarkMode = !this.isDarkMode;
  }

  fazerLogin() {
    // 3. Montamos o "pacote" (JSON) com os dados que o usuário digitou
    const pacoteDeDados = {
      email: this.email,
      senha: this.senha
    };

    console.log("Enviando pacote para o Java...", pacoteDeDados);

    // 4. Chamamos o Service e ficamos "inscritos" (subscribe) esperando a resposta do Java
    this.loginService.enviarParaOJava(pacoteDeDados).subscribe({
      next: (resposta) => {
        console.log("Sucesso! O Java respondeu:", resposta);
        alert("Usuário criado com sucesso no banco de dados!");
      },
      error: (erro) => {
        console.error("Ops, a entrega falhou:", erro);
        alert("Erro ao conectar com o Java. Veja o F12!");
      }
    });
  }
}