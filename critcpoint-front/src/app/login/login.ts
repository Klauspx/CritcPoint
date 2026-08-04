import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
// 1. Importamos a ferramenta que faz os links funcionarem sem recarregar a tela
import { RouterLink } from '@angular/router'; 

@Component({
  selector: 'app-login',
  // 2. Colocamos o RouterLink aqui na lista de importações
  imports: [FormsModule, RouterLink], 
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  isDarkMode = false;
  email = '';
  senha = '';

  toggleTheme() {
    this.isDarkMode = !this.isDarkMode;
  }

  fazerLogin() {
    // Como a gente transferiu a lógica de salvar pro Cadastro, 
    // vamos deixar um alerta provisório aqui até criarmos a rota de conferir senha no Java!
    alert("Em breve: Integração de Login para entrar no Dashboard!");
  }
}