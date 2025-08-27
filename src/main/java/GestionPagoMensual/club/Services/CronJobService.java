package GestionPagoMensual.club.Services;

import GestionPagoMensual.club.Entitys.Cliente;
import GestionPagoMensual.club.Entitys.Estado;
import GestionPagoMensual.club.Entitys.PagoMensual;
import GestionPagoMensual.club.Repositories.ClienteRepository;
import GestionPagoMensual.club.Repositories.PagoMensualRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class CronJobService implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final PagoMensualRepository pagoMensualRepository;

    @Autowired
    public CronJobService(ClienteRepository clienteRepository, PagoMensualRepository pagoMensualRepository) {
        this.clienteRepository = clienteRepository;
        this.pagoMensualRepository = pagoMensualRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Iniciando la tarea de verificación de pagos vencidos...");
        verificarYActualizarPagosVencidos();
        System.out.println("Tarea de verificación de pagos vencidos finalizada.");
        // Opcional: Para asegurar que el cronjob se cierre al terminar.
        // System.exit(0);
    }

    public void verificarYActualizarPagosVencidos() {
        ZonedDateTime fechaActual = ZonedDateTime.now();
        List<Cliente> clientes = clienteRepository.findAll();

        for (Cliente cliente : clientes) {
            Optional<PagoMensual> ultimoPagoOpt = Optional.ofNullable(pagoMensualRepository.findFirstByClienteOrderByFechaVencimientoDesc(cliente));

            if (ultimoPagoOpt.isPresent()) {
                PagoMensual ultimoPago = ultimoPagoOpt.get();
                // Si el último pago existe y su fecha de vencimiento ha pasado...
                if (ultimoPago.getFechaVencimiento().isBefore(fechaActual)) {
                    // Y el cliente no está marcado como NO_PAGO, lo actualizamos.
                    if (cliente.getEstado() != Estado.NO_PAGO) {
                        cliente.setEstado(Estado.NO_PAGO);
                        cliente.setFechaCambioEstado(fechaActual);
                        clienteRepository.save(cliente);
                        System.out.println("Cliente " + cliente.getId() + " actualizado a NO_PAGO.");
                    }
                } else {
                    // Si el pago no está vencido y el cliente no está en estado de pago, lo actualizamos.
                    if (cliente.getEstado() != Estado.PAGO) {
                        cliente.setEstado(Estado.PAGO);
                        cliente.setFechaCambioEstado(fechaActual);
                        clienteRepository.save(cliente);
                        System.out.println("Cliente " + cliente.getId() + " actualizado a PAGO.");
                    }
                }
            } else {
                // Si el cliente no tiene pagos registrados, su estado debería ser NO_PAGO por defecto.
                if (cliente.getEstado() != Estado.NO_PAGO) {
                    cliente.setEstado(Estado.NO_PAGO);
                    cliente.setFechaCambioEstado(fechaActual);
                    clienteRepository.save(cliente);
                    System.out.println("Cliente " + cliente.getId() + " sin pagos, actualizado a NO_PAGO.");
                }
            }
        }
    }
}