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
        ZonedDateTime horaActualLocal = ZonedDateTime.now(ZoneId.systemDefault());

        // Obtener la fecha de creación del pago mensual
        ZonedDateTime fechaCreacionPago = horaActualLocal;
        cliente.setEstado(Estado.PAGO);
        cliente.setPago(true);
        cliente.setFechaCambioEstado(horaActualLocal);
        nuevoPago.setCliente(cliente);


        // Guardar el pago mensual
        PagoMensual pagoMensualGuardado = pagoMensualRepository.save(nuevoPago);

        // Programar una tarea para verificar si ha pasado un día desde la creación del pago mensual
        Runnable verificarEstadoCliente = () -> {
            ZonedDateTime horaActual = ZonedDateTime.now(ZoneId.systemDefault());
            long diasTranscurridos = Duration.between(fechaCreacionPago.toLocalDate().atStartOfDay(), horaActual.toLocalDate().atStartOfDay()).toDays();
            if (diasTranscurridos >= 31) { // Cambiar estado después de 31 días
                cambiarEstadoCliente(clienteId);
            }
        };

        // Programar la tarea para que se ejecute diariamente
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(verificarEstadoCliente, 0, 1, TimeUnit.DAYS);

        return pagoMensualGuardado;
    }

    private void cambiarEstadoCliente(Long clienteId) {
        Cliente clienteParaActualizar = clienteRepository.findById(clienteId).orElse(null);
        if (clienteParaActualizar != null) {
            clienteParaActualizar.setEstado(Estado.NO_PAGO);
            clienteRepository.save(clienteParaActualizar);
        }
    }
}



