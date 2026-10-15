import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from './environment';
import { Paquete, PaqueteReporte, PaqueteRequest } from './models';

@Injectable({ providedIn: 'root' })
export class PaqueteService {
  private readonly base = `${environment.apiUrl}/paquete`;

  constructor(private http: HttpClient) {}

  registrar(dto: PaqueteRequest) {
    return this.http.post<Paquete>(this.base, dto);
  }

  obtenerPorId(id: number) {
    return this.http.get<Paquete>(`${this.base}/${id}`);
  }

  actualizar(id: number, dto: PaqueteRequest) {
    return this.http.put<Paquete>(`${this.base}/${id}`, dto);
  }

  eliminar(id: number) {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  listarTodos() {
    return this.http.get<PaqueteReporte[]>(this.base);
  }

  listarPorCliente(idCliente: number) {
    return this.http.get<PaqueteReporte[]>(`${this.base}/cliente/${idCliente}`);
  }

  listarPorEstado(estado: string) {
    return this.http.get<PaqueteReporte[]>(`${this.base}/estado/${estado}`);
  }

  enviarARuta(id: number, fecha: string) {
    const params = new HttpParams().set('fecha', fecha);
    return this.http.post(`${this.base}/${id}/envioARuta`, null, { params, responseType: 'text' });
  }

  marcarEntregado(id: number, fecha: string) {
    const params = new HttpParams().set('fecha', fecha);
    return this.http.patch(`${this.base}/${id}/paqueteEntregado`, null, { params, responseType: 'text' });
  }
}
