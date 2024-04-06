package GestionPagoMensual.club.Services;



import GestionPagoMensual.club.Entitys.Cliente;
import GestionPagoMensual.club.Entitys.Estado;
import GestionPagoMensual.club.Entitys.PagoMensual;
import GestionPagoMensual.club.ManejoErrores.EntityStateChangeEvent;
import GestionPagoMensual.club.Repositories.ClienteRepository;
import GestionPagoMensual.club.Repositories.PagoMensualRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
public class PagoMensualService {
    private PagoMensualRepository pagoMensualRepository;
    private ClienteRepository clienteRepository;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public PagoMensualService(PagoMensualRepository pagoMensualRepository, ClienteRepository clienteRepository) {
        this.pagoMensualRepository = pagoMensualRepository;
        this.clienteRepository = clienteRepository;
    }


    public List<PagoMensual> obtenerTodasLasFacturasMensuales() {
        return pagoMensualRepository.findAll();
    }

    public List<PagoMensual> obtenerPagosMensualesPorIdCliente(Long idCliente) {
        return pagoMensualRepository.findByClienteId(idCliente);
    }

    public void eliminarFacturaMensualPorId(Long id) {
        pagoMensualRepository.deleteById(id);
    }

    public PagoMensual guardarFacturaMensual(PagoMensual nuevoPago, Long clienteId) throws Exception {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new Exception("Cliente no encontrado con ID: " + clienteId));

        // Obtener la hora actual en la zona horaria del servidor de aplicaciones
        ZonedDateTime horaActual = ZonedDateTime.now(ZoneId.systemDefault());

        // Actualizar el estado del cliente
        cliente.setEstado(Estado.PAGO);
        cliente.setPago(true);
        cliente.setFechaCambioEstado(horaActual);
        nuevoPago.setCliente(cliente);

        // Guardar el pago mensual
        PagoMensual pagoMensualGuardado = pagoMensualRepository.save(nuevoPago);

        // Calcular la fecha de cambio de estado del cliente (un día después)
        ZonedDateTime fechaCambioEstadoCliente = horaActual.plusDays(1);

        // Crear una tarea para cambiar el estado del cliente
        Runnable cambiarEstadoCliente = () -> {
            Cliente clienteParaActualizar = clienteRepository.findById(clienteId).orElse(null);
            if (clienteParaActualizar != null) {
                clienteParaActualizar.setEstado(Estado.NO_PAGO);
                clienteRepository.save(clienteParaActualizar);
            }
        };

        // Programar la tarea para que se ejecute en la fecha de cambio de estado
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        long delayMillis = Duration.between(horaActual, fechaCambioEstadoCliente).toMillis();
        executorService.schedule(cambiarEstadoCliente, delayMillis, TimeUnit.MILLISECONDS);

        return pagoMensualGuardado;
    }



//    @Async
//    public void programarRevertirEstado(Cliente cliente) {
//        try {
//            // Espera 1 día antes de revertir el estado a "NO_PAGO"
//            TimeUnit.DAYS.sleep(1);
//            cliente.setEstado(Estado.NO_PAGO);
//            cliente.setFechaCambioEstado(LocalDateTime.now());
//            eventPublisher.publishEvent(new EntityStateChangeEvent(cliente));
//        } catch (InterruptedException e) {
//            // Manejar interrupciones
//            Thread.currentThread().interrupt();
//        }
//    }

//    @Scheduled(cron = "0 0 */2 * * ?") // Se ejecuta cada 2 días
//    public void revertirCambiosPagos() {
//        // Obtener la fecha actual
//        LocalDateTime fechaActual = LocalDateTime.now();
//
//        // Sumar 2 días a la fecha actual para obtener la fecha límite
//        LocalDateTime fechaLimite = LocalDateTime.now().plusDays(2);
//
//        // Si la fecha actual es posterior a la fecha límite, se revierten los cambios
//        if (fechaActual.isAfter(fechaLimite)) {
//            // Obtener los clientes que se modificaron después del tiempo determinado
//            List<Cliente> clientesModificados = clienteRepository.findByFechaCambioEstadoAfter(fechaLimite);
//
//            for (Cliente cliente : clientesModificados) {
//                cliente.setEstado(Estado.NO_PAGO);
//                cliente.setPago(false);
//                clienteRepository.save(cliente); // Se actualiza el cliente
//            }
//        }
//    }

}
