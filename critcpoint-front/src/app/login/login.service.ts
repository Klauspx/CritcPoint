import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root' // Isso avisa o Angular que esse serviço está disponível para o projeto todo
})
export class LoginService {

  // 1. O endereço do nosso Back-end em Java!
  private urlBackend = 'http://localhost:8080/usuarios'; 

  // 2. O Angular injeta o carteiro oficial (HttpClient) aqui
  constructor(private http: HttpClient) { }

  // 3. A função que vai pegar os dados da tela e mandar pro Java
  enviarParaOJava(dadosDoUsuario: any) {
    // Isso aqui é EXATAMENTE o que o Postman faz por debaixo dos panos!
    // Ele faz um POST na URL do Java mandando o JSON (dadosDoUsuario)
    return this.http.post(this.urlBackend, dadosDoUsuario);
  }

  // 4. Função de login: manda email e senha pro endpoint /login e recebe o usuário de volta
  fazerLogin(credenciais: { email: string; senha: string }) {
    return this.http.post<{ id: number; nome: string; email: string }>(
      'http://localhost:8080/login',
      credenciais
    );
  }
}