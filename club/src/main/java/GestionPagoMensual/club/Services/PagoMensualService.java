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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


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

    @Transactional
    public PagoMensual actualizarPago(Long idCliente, Long idPagoMensual) {
        try {
            PagoMensual pagoMensual = pagoMensualRepository.findById(idCliente)
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + idCliente));

            Cliente cliente = clienteRepository.findById(idPagoMensual)
                    .orElseThrow(() -> new ResourceNotFoundException("El pago mensual no fue encontrado con id: " + idPagoMensual));

            pagoMensual.setCliente(cliente);
            cliente.getCronogramaPagos().add(pagoMensual);

            cliente.setEstado(Estado.PAGO);
            cliente.setPago(true);

            pagoMensual.setFechaCambioEstado(LocalDateTime.now()); // Se actualiza la fecha de cambio de estado



            clienteRepository.save(cliente);
            return pagoMensualRepository.save(pagoMensual);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el pago", e);
        }
    }

    @Scheduled(cron = "0 */5 * * * *") // Se ejecuta cada minuto
    public void revertirCambiosPagos() {
        // Obtener la fecha actual
        LocalDateTime fechaActual = LocalDateTime.now();

        // Obtener la fecha límite
        LocalDateTime fechaLimite = LocalDateTime.now().minusMinutes(5); // Se puede modificar el tiempo

        // Si la fecha actual es posterior a la fecha límite, se revierten los cambios
        if (fechaActual.isAfter(fechaLimite)) {
            // Obtener los pagos que se modificaron después del tiempo determinado
            List<PagoMensual> pagosModificados = pagoMensualRepository.findByFechaCambioEstadoAfter(fechaLimite);

            for (PagoMensual pagoMensual : pagosModificados) {
                Cliente cliente = pagoMensual.getCliente(); // Se obtiene el cliente asociado al pago

                cliente.setEstado(Estado.NO_PAGO);
                cliente.setPago(false);

                clienteRepository.save(cliente); // Se actualiza el cliente
            }
        }
    }
}
