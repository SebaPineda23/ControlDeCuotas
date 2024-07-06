package GestionPagoMensual.club.Services;



import GestionPagoMensual.club.Entitys.Cliente;
import GestionPagoMensual.club.Entitys.Estado;
import GestionPagoMensual.club.Entitys.PagoMensual;
import GestionPagoMensual.club.Repositories.ClienteRepository;
import GestionPagoMensual.club.Repositories.PagoMensualRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
public class PagoMensualService {
    private PagoMensualRepository pagoMensualRepository;
    private ClienteRepository clienteRepository;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    AuthMail authMail;

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
    public List<PagoMensual> getPagosByMes(String mesAno) {
        return pagoMensualRepository.findPagosByMes(mesAno);
    }

    public double calcularMontoTotalPorCategoriaYMes(String mesAno, String categoria) {
        return pagoMensualRepository.calcularMontoTotalPorCategoriaYMes(mesAno, categoria);
    }

    public void eliminarFacturaMensualPorId(Long id) {
        pagoMensualRepository.deleteById(id);
    }

    public PagoMensual guardarFacturaMensual(PagoMensual nuevoPago, Long clienteId) {
        // Paso 1: Obtener el cliente
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ExpressionException("Cliente no encontrado con ID: " + clienteId));

        // Actualizar el estado del cliente
        ZonedDateTime fechaActual = ZonedDateTime.now();

        // Guardar el nuevo pago (si es necesario)
        nuevoPago.setCliente(cliente);
        cliente.setFechaCambioEstado(fechaActual);
        cliente.setEstado(Estado.PAGO);
        PagoMensual pagoMensualGuardado = pagoMensualRepository.save(nuevoPago);

        // Enviar correo electrónico (opcional)
        sendPaymentEmail(cliente, fechaActual);

        // Programar una tarea para verificar el estado del cliente el día 7 de cada mes
        programarVerificacionEstadoCliente(cliente);

        return pagoMensualGuardado;
    }


    private void sendPaymentEmail(Cliente cliente, ZonedDateTime fechaCreacionPago) {
        String mensaje = "Hola " + cliente.getNombre() + ",\n\nGracias por realizar el pago de la cuota. El pago se efectuó el día " + fechaCreacionPago.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n\nSaludos,\nEl equipo de gestión del club";

        authMail.sendMessage(cliente.getEmail(), mensaje);
    }

    private void programarVerificacionEstadoCliente(Cliente cliente) {
        Runnable verificarEstadoCliente = () -> {
            cambiarEstadoCliente(cliente);
        };

        // Calcular el tiempo de espera hasta el próximo día 6 del mes
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime nextSixthOfMonth = now.withDayOfMonth(6).withHour(0).withMinute(0).withSecond(0).withNano(0);

        // Si hoy es después del día 6, programar para el 6 del mes siguiente
        if (now.getDayOfMonth() > 6) {
            nextSixthOfMonth = nextSixthOfMonth.plusMonths(1);
        }

        long initialDelay = Duration.between(now, nextSixthOfMonth).toMillis();
        long period = Duration.ofDays(30).toMillis(); // Aproximadamente cada mes

        // Programar la tarea para que se ejecute el día 6 de cada mes
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(verificarEstadoCliente, initialDelay, period, TimeUnit.MILLISECONDS);
    }



    private void cambiarEstadoCliente(Cliente cliente) {
        cliente.setEstado(Estado.NO_PAGO);
        clienteRepository.save(cliente);
    }

}
