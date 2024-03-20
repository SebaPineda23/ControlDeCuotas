package GestionPagoMensual.club.Services;



import GestionPagoMensual.club.Entitys.Cliente;
import GestionPagoMensual.club.Entitys.Estado;
import GestionPagoMensual.club.Entitys.PagoMensual;
import GestionPagoMensual.club.Repositories.ClienteRepository;
import GestionPagoMensual.club.Repositories.PagoMensualRepository;
import jakarta.transaction.Transactional;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;


@Service
public class PagoMensualService {
    private PagoMensualRepository pagoMensualRepository;
    private ClienteRepository clienteRepository;

    public PagoMensualService(PagoMensualRepository pagoMensualRepository, ClienteRepository clienteRepository) {
        this.pagoMensualRepository = pagoMensualRepository;
        this.clienteRepository = clienteRepository;
    }


    public PagoMensual guardarFacturaMensual(PagoMensual pagoMensual) {
        return pagoMensualRepository.save(pagoMensual);
    }

    public List<PagoMensual> obtenerTodasLasFacturasMensuales() {
        return pagoMensualRepository.findAll();
    }

    public Optional<PagoMensual> obtenerFacturaMensualPorId(Long id) {
        return pagoMensualRepository.findById(id);
    }

    public void eliminarFacturaMensualPorId(Long id) {
        pagoMensualRepository.deleteById(id);
    }

    public PagoMensual guardarFacturaMensual(PagoMensual nuevoPago, Long clienteId) throws Exception {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new Exception("Cliente no encontrado con ID: " + clienteId));

        cliente.setEstado(Estado.PAGO);
        cliente.setPago(true);
        cliente.setFechaCambioEstado(LocalDateTime.now());
        nuevoPago.setCliente(cliente);
        return pagoMensualRepository.save(nuevoPago);
    }

    @Scheduled(cron = "0 */5 * * * *") // Se ejecuta cada 5 minutos
    public void revertirCambiosPagos() {
        // Obtener la fecha actual
        LocalDateTime fechaActual = LocalDateTime.now();

        // Obtener la fecha límite
        LocalDateTime fechaLimite = LocalDateTime.now().minusMinutes(5); // Se puede modificar el tiempo

        // Si la fecha actual es posterior a la fecha límite, se revierten los cambios
        if (fechaActual.isAfter(fechaLimite)) {
            // Obtener los clientes que se modificaron después del tiempo determinado
            List<Cliente> clientesModificados = clienteRepository.findByFechaCambioEstadoAfter(fechaLimite);

            for (Cliente cliente : clientesModificados) {
                cliente.setEstado(Estado.NO_PAGO);
                cliente.setPago(false);
                clienteRepository.save(cliente); // Se actualiza el cliente
            }
        }
    }
}
