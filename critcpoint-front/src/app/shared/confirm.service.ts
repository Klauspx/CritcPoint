import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ConfirmService {
  aberto = signal(false);
  mensagem = signal('');
  private resolver?: (valor: boolean) => void;

  pedir(mensagem: string): Promise<boolean> {
    this.mensagem.set(mensagem);
    this.aberto.set(true);
    return new Promise(resolve => {
      this.resolver = resolve;
    });
  }

  confirmar() {
    this.aberto.set(false);
    this.resolver?.(true);
  }

  cancelar() {
    this.aberto.set(false);
    this.resolver?.(false);
  }
}