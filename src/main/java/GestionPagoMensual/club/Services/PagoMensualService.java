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
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


@Service
public class PagoMensualService {
    private PagoMensualRepository pagoMensualRepository;
    private ClienteRepository clienteRepository;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    AuthMail authMail;
    private Map<Long, ScheduledFuture<?>> scheduledTasks = new HashMap<>();

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

        // Obtener la hora actual en la zona horaria de Argentina (Buenos Aires)
        ZoneId zonaHorariaArgentina = ZoneId.of("America/Argentina/Buenos_Aires");
        ZonedDateTime horaActualArgentina = ZonedDateTime.now(zonaHorariaArgentina);

        // Obtener la fecha de creación del pago mensual en la zona horaria de Argentina
        ZonedDateTime fechaCreacionPago = horaActualArgentina;

        // Establecer los atributos del cliente y el nuevo pago mensual
        cliente.setEstado(Estado.PAGO);
        cliente.setPago(true);
        cliente.setFechaCambioEstado(fechaCreacionPago);
        nuevoPago.setCliente(cliente);

        // Guardar el pago mensual
        PagoMensual pagoMensualGuardado = pagoMensualRepository.save(nuevoPago);

        // Enviar correo electrónico de confirmación
        sendPaymentEmail(cliente, fechaCreacionPago);

        // Cancelar cualquier tarea programada previamente para este cliente, si existe
        ScheduledFuture<?> existingTask = scheduledTasks.get(clienteId);
        if (existingTask != null) {
            existingTask.cancel(true);
        }

        // Programar la tarea para verificar el estado del cliente
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        Runnable verificarEstadoCliente = () -> {
            ZonedDateTime horaActual = ZonedDateTime.now(zonaHorariaArgentina); // Usar la zona horaria de Argentina
            long diasTranscurridos = Duration.between(fechaCreacionPago.toLocalDate().atStartOfDay(), horaActual.toLocalDate().atStartOfDay()).toDays();
            if (diasTranscurridos >= 1) { // Cambiar estado después de 1 día
                try {
                    cambiarEstadoCliente(clienteId);
                } catch (Exception e) {
                    e.printStackTrace(); // Manejar la excepción según corresponda
                }
            }
        };
        ScheduledFuture<?> task = executorService.scheduleAtFixedRate(verificarEstadoCliente, 0, 1, TimeUnit.DAYS);
        scheduledTasks.put(clienteId, task);

        return pagoMensualGuardado;
    }

    private void sendPaymentEmail(Cliente cliente, ZonedDateTime fechaCreacionPago) {
        String mensaje = "Hola " + cliente.getNombre() + ",\n\nGracias por realizar el pago de la cuota. El pago se efectuó el día " + fechaCreacionPago.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n\nSaludos,\nEl equipo de gestión del club";

        authMail.sendMessage(cliente.getEmail(), mensaje);
    }

    private void cambiarEstadoCliente(Long clienteId) {
        Cliente clienteParaActualizar = clienteRepository.findById(clienteId).orElse(null);
        if (clienteParaActualizar != null) {
            clienteParaActualizar.setEstado(Estado.NO_PAGO);
            clienteRepository.save(clienteParaActualizar);
        }
    }
}



