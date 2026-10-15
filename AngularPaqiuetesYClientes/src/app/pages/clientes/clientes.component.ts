import { Component, inject, signal, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ClienteService } from '../../core/cliente.service';
import { Cliente, ClienteReporte, TipoCliente, TipoDocumento } from '../../core/models';

@Component({
  selector: 'app-clientes',
  imports: [FormsModule, CommonModule],
  templateUrl: './clientes.component.html',
  styleUrl: './clientes.component.scss'
})
export class ClientesComponent implements OnInit {
  private readonly service = inject(ClienteService);

  readonly tiposCliente: TipoCliente[] = ['PERSONA_NATURAL', 'EMPRESA'];
  readonly tiposDocumento: TipoDocumento[] = ['DNI', 'RUC', 'PASAPORTE'];


  readonly lista = signal<ClienteReporte[]>([]);
  readonly cargandoLista = signal(false);

  readonly nuevo: Cliente = {
    tipoCliente: 'PERSONA_NATURAL',
    tipoDoc: 'DNI',
    documento: '',
    nombres: '',
    apellidos: '',
    telefono: '',
    correo: '',
    direccion: ''
  };
  readonly msgCrear = signal<string | null>(null);

  readonly editando = signal<Cliente | null>(null);
  readonly msgEditar = signal<string | null>(null);

  readonly msgGeneral = signal<string | null>(null);

  ngOnInit(): void {
    this.listar();
  }
  listar(): void {
    this.cargandoLista.set(true);
    this.service.listar().subscribe({
      next: (data) => {
        this.lista.set(data);
        this.cargandoLista.set(false);
      },
      error: () => {
        this.cargandoLista.set(false);
        this.msgGeneral.set('No se pudo listar los clientes.');
      }
    });
  }

  crear(): void {
    this.msgCrear.set(null);
    this.service.crear(this.nuevo).subscribe({
      next: (c) => {
        this.msgCrear.set('Cliente creado con id ' + c.id);
        this.nuevo.documento = '';
        this.nuevo.nombres = '';
        this.nuevo.apellidos = '';
        this.nuevo.telefono = '';
        this.nuevo.correo = '';
        this.nuevo.direccion = '';
        this.listar();
      },
      error: (err: any) =>
        this.msgCrear.set('Error al crear: ' + (err?.error?.message ?? err?.status ?? ''))
    });
  }

  editarFila(fila: ClienteReporte): void {
    this.editando.set(null);
    this.msgEditar.set(null);
    if (!fila.id) {
      this.msgEditar.set('El backend aun no envia el id: recrea el SP sp_listar_Clientes y reinicia ms-tracking-clientes.');
      return;
    }
    this.service.obtener(fila.id).subscribe({
      next: (c) => this.editando.set(c),
      error: (err: any) =>
        this.msgEditar.set(
          typeof err?.error === 'string' ? err.error : 'Cliente no hallado ' + fila.id
        )
    });
  }

  guardarEdicion(): void {
    const cliente = this.editando();
    if (!cliente?.id) return;
    this.msgEditar.set(null);
    this.service.actualizar(cliente.id, cliente).subscribe({
      next: (c) => {
        this.msgEditar.set('Cliente ' + c.id + ' actualizado.');
        this.editando.set(null);
        this.listar();
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
    if (!id) {
      this.msgGeneral.set('El backend aun no envia el id: recrea el SP sp_listar_Clientes y reinicia ms-tracking-clientes.');
      return;
    }
    if (!confirm('¿Eliminar cliente ' + id + '?')) return;
    this.service.eliminar(id).subscribe({
      next: () => {
        this.msgGeneral.set('Cliente ' + id + ' eliminado.');
        this.listar();
      },
      error: () => this.msgGeneral.set('No se pudo eliminar el cliente ' + id + '.')
    });
  }
}
