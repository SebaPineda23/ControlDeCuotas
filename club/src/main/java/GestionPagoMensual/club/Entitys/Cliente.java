package GestionPagoMensual.club.Entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cliente {
    @Id
    @GeneratedValue
    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String fecha_nacimiento;
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PagoMensual> cronogramaPagos;
    private Estado estado;
    private boolean pago;

    public void registrarPago(double monto) {
        // Lógica para registrar un nuevo pago
        PagoMensual nuevoPago = new PagoMensual();
        // Asignar la fecha actual al pago, aquí podrías usar LocalDateTime.now() por ejemplo
        nuevoPago.setFecha("2024-02-15");
        nuevoPago.setMonto(monto);
        // Agregar el pago al cronograma de pagos del cliente
        cronogramaPagos.add(nuevoPago);
        // Actualizar el estado del cliente a "PAGO"
        this.estado = Estado.PAGO;
        // Actualizar el estado de pago a true
        this.pago = true;
    }

    }
