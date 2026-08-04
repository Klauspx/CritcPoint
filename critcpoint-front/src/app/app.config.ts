import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';

// 1. A peça que estava faltando para o SSR funcionar
import { provideClientHydration } from '@angular/platform-browser';
// 2. O Carteiro atualizado para as versões novas
import { provideHttpClient, withFetch } from '@angular/common/http';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideClientHydration(), // <-- O salvador da pátria está de volta aqui!
    provideHttpClient(withFetch()) 
  ]
};