import { Injectable, signal } from '@angular/core';

export interface Toast {
  id: number;
  message: string;
  type: 'success' | 'error' | 'info';
}

@Injectable({ providedIn: 'root' })
export class ToastService {
  private contadorId = 0;
  toasts = signal<Toast[]>([]);

  show(message: string, type: 'success' | 'error' | 'info' = 'info', duracaoMs = 4000) {
    const id = ++this.contadorId;
    this.toasts.update(lista => [...lista, { id, message, type }]);
    setTimeout(() => this.remover(id), duracaoMs);
  }

  success(message: string) { this.show(message, 'success'); }
  error(message: string) { this.show(message, 'error'); }
  info(message: string) { this.show(message, 'info'); }

  remover(id: number) {
    this.toasts.update(lista => lista.filter(t => t.id !== id));
  }
}