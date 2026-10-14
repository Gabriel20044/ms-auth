import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';
import { environment } from './environment';
import { AuthResponse, LoginRequest } from './models';

const TOKEN_KEY = 'tracking_token';
const USER_KEY = 'tracking_user';

@Injectable({ providedIn: 'root' })
export class AuthService {
  readonly currentUser = signal<string | null>(localStorage.getItem(USER_KEY));

  constructor(private http: HttpClient) {}

  login(credenciales: LoginRequest) {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/login`, credenciales).pipe(
      tap((res) => {
        localStorage.setItem(TOKEN_KEY, res.token);
        localStorage.setItem(USER_KEY, res.username);
        this.currentUser.set(res.username);
      })
    );
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    this.currentUser.set(null);
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }
}
