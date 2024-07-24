package GestionPagoMensual.club.Services;



import GestionPagoMensual.club.Entitys.Cliente;
import GestionPagoMensual.club.Entitys.Estado;
import GestionPagoMensual.club.Entitys.PagoMensual;
import GestionPagoMensual.club.Repositories.ClienteRepository;
import GestionPagoMensual.club.Repositories.PagoMensualRepository;

import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
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
    public PagoMensual updatePagoMensual(Long pagoMensualId, PagoMensual pagoMensual) throws Exception{
        Optional<PagoMensual> pagoMensualOptional= pagoMensualRepository.findById(pagoMensualId);
        if(pagoMensualOptional.isPresent()){
            PagoMensual pagoMensualExistente =pagoMensualOptional.get();
            if (pagoMensual.getMonto()!=0){
                pagoMensualExistente.setMonto(pagoMensual.getMonto());
            }
            return pagoMensualRepository.save(pagoMensualExistente);
        }else{
            throw new Exception("Pago no existente con el id: "+pagoMensualId);
        }
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

        // Paso 2: Buscar el último pago del cliente
        PagoMensual ultimoPago = pagoMensualRepository.findFirstByClienteOrderByFechaVencimientoDesc(cliente);

        // Paso 3: Calcular la fecha de vencimiento del nuevo pago
        ZonedDateTime fechaActual = ZonedDateTime.now();
        ZonedDateTime fechaVencimientoNuevoPago;
        if (ultimoPago != null && ultimoPago.getFechaVencimiento().isAfter(fechaActual)) {
            fechaVencimientoNuevoPago = ultimoPago.getFechaVencimiento().plusDays(30); // Cambio aquí a 1 día
        } else {
            fechaVencimientoNuevoPago = fechaActual.plusDays(30); // Cambio aquí a 1 día
        }

        // Paso 4: Actualizar el estado del cliente
        if (ultimoPago != null && fechaActual.isAfter(ultimoPago.getFechaVencimiento())) {
            cliente.setEstado(Estado.NO_PAGO); // Estado se pone en NO_PAGO automáticamente
        } else {
            cliente.setEstado(Estado.PAGO);
        }

        // Guardar el nuevo pago
        nuevoPago.setCliente(cliente);
        nuevoPago.setFechaVencimiento(fechaVencimientoNuevoPago);
        PagoMensual pagoMensualGuardado = pagoMensualRepository.save(nuevoPago);

        // Actualizar el estado del cliente a PAGO después de guardar el nuevo pago
        cliente.setEstado(Estado.PAGO);
        cliente.setFechaCambioEstado(fechaActual);
        clienteRepository.save(cliente);

        // Enviar correo electrónico (opcional)
        sendPaymentEmail(cliente, fechaActual);

        // Programar una tarea para verificar el estado del cliente (opcional)
        programarVerificacionEstadoCliente(cliente, fechaActual, fechaVencimientoNuevoPago);

        return pagoMensualGuardado;
    }

    private void sendPaymentEmail(Cliente cliente, ZonedDateTime fechaCreacionPago) {
        String mensaje = "Hola " + cliente.getNombre() + ",\n\nGracias por realizar el pago de la cuota. El pago se efectuó el día " + fechaCreacionPago.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n\nSaludos,\nEl equipo de gestión del club";

        authMail.sendMessage(cliente.getEmail(), mensaje);
    }

    private void programarVerificacionEstadoCliente(Cliente cliente, ZonedDateTime fechaActual, ZonedDateTime fechaVencimientoNuevoPago) {
        Runnable verificarEstadoCliente = () -> {
            cambiarEstadoCliente(cliente);
        };

        // Calcular el tiempo de espera para la tarea de verificación del estado del cliente (1 día)
        long delay = Duration.between(fechaActual, fechaVencimientoNuevoPago).toDays();

        // Programar la tarea para que se ejecute cada 1 día
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(verificarEstadoCliente, delay, 1, TimeUnit.DAYS);
    }

    private void cambiarEstadoCliente(Cliente cliente) {
        PagoMensual ultimoPago = pagoMensualRepository.findFirstByClienteOrderByFechaVencimientoDesc(cliente);
        if (ultimoPago == null || ultimoPago.getFechaVencimiento().isBefore(ZonedDateTime.now())) {
            cliente.setEstado(Estado.NO_PAGO);
            clienteRepository.save(cliente);
        }
    }
}
