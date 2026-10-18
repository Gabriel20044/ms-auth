import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from './environment';
import { Cliente, ClienteOpcion, ClienteReporte } from './models';

@Injectable({ providedIn: 'root' })
export class ClienteService {
  private readonly base = `${environment.apiUrl}/clientes`;

  constructor(private http: HttpClient) {}

  crear(cliente: Cliente) {
    return this.http.post<Cliente>(this.base, cliente);
  }

  listar() {
    return this.http.get<ClienteReporte[]>(this.base);
  }

  opciones() {
    return this.http.get<ClienteOpcion[]>(`${this.base}/opciones`);
  }

  obtener(id: number) {
    return this.http.get<Cliente>(`${this.base}/${id}`);
  }

  actualizar(id: number, cliente: Cliente) {
    return this.http.put<Cliente>(`${this.base}/${id}`, cliente);
  }

  eliminar(id: number) {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
