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

        // Guardar el nuevo pago
        nuevoPago.setCliente(cliente);
        PagoMensual pagoMensualGuardado = pagoMensualRepository.save(nuevoPago);

        // Actualizar el estado del cliente a PAGO después de guardar el nuevo pago
        ZonedDateTime fechaActual = ZonedDateTime.now();
        cliente.setEstado(Estado.PAGO);
        cliente.setPago(true);
        cliente.setFechaCambioEstado(fechaActual);
        clienteRepository.save(cliente);

        // Enviar correo electrónico (opcional)
        sendPaymentEmail(cliente, fechaActual);

        // Programar verificación del estado del cliente para el día 7 de cada mes
        programarVerificacionEstadoClienteDia7(cliente);

        return pagoMensualGuardado;
    }

    private void sendPaymentEmail(Cliente cliente, ZonedDateTime fechaCreacionPago) {
        String mensaje = "Hola " + cliente.getNombre() + ",\n\nGracias por realizar el pago de la cuota. El pago se efectuó el día " + fechaCreacionPago.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n\nSaludos,\nEl equipo de gestión del club";

        authMail.sendMessage(cliente.getEmail(), mensaje);
    }

    private void programarVerificacionEstadoClienteDia7(Cliente cliente) {
        // Obtener el próximo día 7 desde la fecha actual
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime nextDay7 = now.withDayOfMonth(7).withHour(0).withMinute(0).withSecond(0).withNano(0);

        // Si ya pasó el día 7 de este mes, calcular el día 7 del próximo mes
        if (now.getDayOfMonth() >= 7) {
            nextDay7 = nextDay7.plusMonths(1);
        }

        long initialDelay = Duration.between(now, nextDay7).toMillis();
        long period = Duration.ofDays(1).toMillis(); // Verificar diariamente hasta llegar al día 7

        // Programar la tarea para que se ejecute el día 7 de cada mes
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> cambiarEstadoCliente(cliente), initialDelay, period, TimeUnit.MILLISECONDS);
    }

    private void cambiarEstadoCliente(Cliente cliente) {
        // Verificar si hoy es el día 7
        if (ZonedDateTime.now().getDayOfMonth() == 7) {
            cliente.setEstado(Estado.NO_PAGO);
            clienteRepository.save(cliente);
        }
    }
}
