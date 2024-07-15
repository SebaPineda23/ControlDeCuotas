package GestionPagoMensual.club.Entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PagoMensual {
    @Id
    @GeneratedValue
    private Long id;
    private String fecha;
    private double monto;
    private LocalDateTime fechaCambioEstado;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    }
