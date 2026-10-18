import { Component, inject, signal, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { PaqueteService } from '../../core/paquete.service';
import { ClienteService } from '../../core/cliente.service';
import { ClienteOpcion, EstadoPaquete, Paquete, PaqueteReporte, PaqueteRequest } from '../../core/models';

interface PaqueteEdicion {
  id: number;
  descripcion: string;
  pesoKG: number;
}

@Component({
  selector: 'app-paquetes',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './paquetes.component.html',
  styleUrl: './paquetes.component.scss'
})
export class PaquetesComponent implements OnInit {
  private readonly service = inject(PaqueteService);
  private readonly clienteService = inject(ClienteService);

  readonly estados: EstadoPaquete[] = ['REGISTRADO', 'EN_TRANSITO', 'ENTREGADO'];

  // Señales de carga y datos principales con sus respectivos alias para el HTML
  readonly cargandoTodos = signal(false);
  get cargandoLista() { return this.cargandoTodos; }

  readonly todos = signal<PaqueteReporte[]>([]);
  get lista() { return this.todos; }

  readonly clientes = signal<ClienteOpcion[]>([]);

  readonly nuevo: PaqueteRequest = { descripcion: '', pesoKG: null as unknown as number, idCliente: null as unknown as number };
  clienteSeleccionado = '';
  readonly paqueteCreado = signal<Paquete | null>(null);
  readonly msgCrear = signal<string | null>(null);

  readonly editando = signal<PaqueteEdicion | null>(null);
  readonly msgEditar = signal<string | null>(null);

  idCliente = '';
  buscadoCliente = false;
  readonly porCliente = signal<PaqueteReporte[]>([]);

  estado = 'REGISTRADO';
  readonly porEstado = signal<PaqueteReporte[]>([]);

  idEnvio = '';
  fechaEnvio = '';
  readonly msgEnvio = signal<string | null>(null);

  idEntrega = '';
  fechaEntrega = '';
  readonly msgEntrega = signal<string | null>(null);

  readonly msgGeneral = signal<string | null>(null);

  ngOnInit(): void {
    this.listarTodos();
    this.cargarClientes();
  }

  cargarClientes(): void {
    this.clienteService.opciones().subscribe({
      next: (data) => this.clientes.set(data),
      error: () => this.clientes.set([])
    });
  }

  crear(): void {
    this.msgCrear.set(null);
    this.paqueteCreado.set(null);
    const dto: PaqueteRequest = {
      descripcion: this.nuevo.descripcion,
      pesoKG: Number(this.nuevo.pesoKG),
      idCliente: Number(this.clienteSeleccionado)
    };
    if (!dto.descripcion || !dto.pesoKG || !dto.idCliente) {
      this.msgCrear.set('Complete descripcion, peso y cliente.');
      return;
    }
    this.service.registrar(dto).subscribe({
      next: (p) => {
        this.paqueteCreado.set(p);
        this.msgCrear.set('Paquete registrado con id ' + p.id + ' · ' + p.codigoRastreo);
        this.nuevo.descripcion = '';
        this.nuevo.pesoKG = null as unknown as number;
        this.clienteSeleccionado = '';
        this.listarTodos();
      },
      error: (err: any) => this.msgCrear.set('Error: ' + (err?.error?.message ?? err?.status ?? ''))
    });
  }

  listarTodos(): void {
    this.cargandoTodos.set(true);
    this.service.listarTodos().subscribe({
      next: (data) => {
        this.todos.set(data);
        this.cargandoTodos.set(false);
      },
      error: () => {
        this.cargandoTodos.set(false);
        this.msgGeneral.set('No se pudieron listar los paquetes.');
      }
    });
  }

  editarFila(p: PaqueteReporte): void {
    this.msgEditar.set(null);
    if (!p.id) return;
    this.editando.set({ id: p.id, descripcion: p.descripcion, pesoKG: Number(p.pesoEnKg) });
  }

  guardarEdicion(): void {
    const e = this.editando();
    if (!e) return;
    this.msgEditar.set(null);
    const dto: PaqueteRequest = { descripcion: e.descripcion, pesoKG: e.pesoKG, idCliente: 0 };
    this.service.actualizar(e.id, dto).subscribe({
      next: (p) => {
        this.msgEditar.set('Paquete ' + p.id + ' actualizado.');
        this.editando.set(null);
        this.listarTodos();
      },
      error: (err: any) =>
        this.msgEditar.set('Error al guardar: ' + (err?.error?.message ?? err?.status ?? ''))
    });
  }

  cancelarEdicion(): void {
    this.editando.set(null);
    this.msgEditar.set(null);
  }

  eliminar(id: number): void {
    this.msgGeneral.set(null);
    if (!id) return;
    if (!confirm('¿Eliminar paquete ' + id + '?')) return;
    this.service.eliminar(id).subscribe({
      next: () => {
        this.msgGeneral.set('Paquete ' + id + ' eliminado.');
        this.listarTodos();
      },
      error: () => this.msgGeneral.set('No se pudo eliminar el paquete ' + id + '.')
    });
  }

  listarPorCliente(): void {
    const id = Number(this.idCliente);
    this.porCliente.set([]);
    this.buscadoCliente = true;
    if (!id) return;
    this.service.listarPorCliente(id).subscribe((data) => this.porCliente.set(data));
  }

  listarPorEstado(): void {
    this.service.listarPorEstado(this.estado).subscribe((data) => this.porEstado.set(data));
  }

  enviarARuta(): void {
    const id = Number(this.idEnvio);
    this.msgEnvio.set(null);
    if (!id || !this.fechaEnvio) {
      this.msgEnvio.set('Indica id y fecha (YYYY-MM-DD).');
      return;
    }
    this.service.enviarARuta(id, this.fechaEnvio).subscribe({
      next: (r) => {
        this.msgEnvio.set(String(r));
        this.listarTodos();
      },
      error: () => this.msgEnvio.set('No se pudo enviar el paquete ' + id + ' a ruta.')
    });
  }

  marcarEntregado(): void {
    const id = Number(this.idEntrega);
    this.msgEntrega.set(null);
    if (!id || !this.fechaEntrega) {
      this.msgEntrega.set('Indica id y fecha (YYYY-MM-DD).');
      return;
    }
    this.service.marcarEntregado(id, this.fechaEntrega).subscribe({
      next: (r) => {
        this.msgEntrega.set(String(r));
        this.listarTodos();
      },
      error: () => this.msgEntrega.set('No se pudo marcar entregado el paquete ' + id + '.')
    });
  }
}
