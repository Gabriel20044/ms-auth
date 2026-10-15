
export interface LoginRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  type: string;
  username: string;
  rol: string;
  mensaje: string;
}

export type TipoCliente = 'PERSONA_NATURAL' | 'EMPRESA';
export type TipoDocumento = 'DNI' | 'RUC' | 'PASAPORTE';
export type EstadoPaquete = 'REGISTRADO' | 'EN_TRANSITO' | 'ENTREGADO';

export interface Cliente {
  id?: number;
  tipoCliente: TipoCliente;
  tipoDoc: TipoDocumento;
  documento: string;
  nombres: string;
  apellidos: string;
  telefono: string;
  correo?: string;
  direccion?: string;
  paquetes?: unknown[];
}

export interface ClienteReporte {
  id: number;
  nombre: string;
  tipoPersona: string;
  tipoDocumento: string;
  documento: string;
  telefono: string;
  correo: string;
  direccion: string;
}

export interface ClienteOpcion {
  id: number;
  nombre: string;
}

export interface PaqueteRequest {
  descripcion: string;
  pesoKG: number;
  idCliente: number;
}

export interface Paquete {
  id: number;
  codigoRastreo: string;
  descripcion: string;
  pesoKG: number;
  estado: EstadoPaquete;
  fechaPedido: string;
  fechaSalidaPedido?: string;
  fechaEntregaEstimada?: string;
  fechaEntrega?: string;
}

export interface PaqueteReporte {
  id: number;
  descripcion: string;
  codigoRastreo: string;
  pesoEnKg: number;
  cliente: string;
  fechaPedido: string;
  fechaSalidaPedido: string | null;
  fechaEntregaEstimada: string | null;
  fechaEntrega: string | null;
  estadoPaquete: string;
}
