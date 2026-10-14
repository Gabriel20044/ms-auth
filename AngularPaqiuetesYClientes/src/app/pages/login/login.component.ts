import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth.service';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  username = 'tracking_admin';
  password = '';
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly exito = signal<string | null>(null);

  enviar(): void {
    this.error.set(null);
    this.exito.set(null);

    if (!this.username || !this.password) {
      this.error.set('Ingresa usuario y contraseña.');
      return;
    }

    this.loading.set(true);
    this.auth.login({ username: this.username, password: this.password }).subscribe({
      next: (res) => {
        this.loading.set(false);
        this.exito.set(res.mensaje ?? 'Bienvenido ' + res.username);
        this.router.navigate(['/clientes']);
      },
      error: (err: any) => {
        this.loading.set(false);
        this.error.set(typeof err?.error === 'string' ? err.error : 'Credenciales invalidas.');
      }
    });
  }
}
