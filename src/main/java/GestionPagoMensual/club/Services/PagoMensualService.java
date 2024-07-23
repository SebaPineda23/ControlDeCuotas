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

    public PagoMensual guardarFacturaMensual(PagoMensual nuevoPago, Long clienteId, int opcionPago) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ExpressionException("Cliente no encontrado con ID: " + clienteId));

        // Establecer la fecha actual en la zona horaria de Argentina
        ZonedDateTime fechaActualArgentina = ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));

        nuevoPago.setCliente(cliente);
        nuevoPago.setFechaPago(fechaActualArgentina);

        switch (opcionPago) {
            case 1:
                // Pagar meses adeudados sin la cuota actual, no cambia el estado a PAGO
                break;
            case 2:
                // Realizar pago mes actual, cambiar el estado a PAGO y establecer la fecha de vencimiento
                cliente.setEstado(Estado.PAGO);
                cliente.setPago(true);
                cliente.setFechaCambioEstado(fechaActualArgentina);

                // Establecer la fecha de vencimiento como el primer día del mes siguiente
                ZonedDateTime fechaVencimiento = fechaActualArgentina.plusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
                nuevoPago.setFechaVencimiento(fechaVencimiento);
                break;
            default:
                throw new IllegalArgumentException("Opción de pago no válida");
        }

        // Guardar el nuevo pago
        PagoMensual pagoMensualGuardado = pagoMensualRepository.save(nuevoPago);

        // Enviar correo electrónico (opcional)
        String mensajeCorreo = null;
        if (cliente.getEmail() != null) {
            LocalDate fecha = LocalDate.parse(nuevoPago.getFecha(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            String nombreMes = obtenerNombreMesDesdeFecha(fecha);
            sendPaymentEmail(cliente, fechaActualArgentina, nombreMes);
        } else {
            mensajeCorreo = "Se registró el pago pero el Socio no cuenta con un email registrado.";
        }

        // Programar verificación del estado del cliente para el día 1 de cada mes
        programarVerificacionEstadoClienteDia1(cliente);

        // Guardar cambios del cliente
        clienteRepository.save(cliente);

        // Si hay un mensaje de correo, mostrarlo en la consola o registrar en el log
        if (mensajeCorreo != null) {
            System.out.println(mensajeCorreo);
        }

        return pagoMensualGuardado;
    }
    private String obtenerNombreMesDesdeFecha(LocalDate fecha) {
        // Obtener el nombre del mes en español desde la fecha
         String mes = fecha.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        return mayuscula(mes);
    }
    private String mayuscula(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    private void sendPaymentEmail(Cliente cliente, ZonedDateTime fechaCreacionPago, String nombreMes) {
        String mensaje = "Hola " + cliente.getNombre() + ",\n\nGracias por realizar el pago de la cuota correspondiente al mes de " + nombreMes + ". El pago se efectuó el día " + fechaCreacionPago.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n\nSaludos,\nEl equipo de gestión del club";

        authMail.sendMessage(cliente.getEmail(), mensaje);
    }

    private void programarVerificacionEstadoClienteDia1(Cliente cliente) {
        // Obtener el próximo día 1 desde la fecha actual
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime nextDay1 = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        // Si ya pasó el día 1 de este mes, calcular el día 1 del próximo mes
        if (now.getDayOfMonth() >= 1) {
            nextDay1 = nextDay1.plusMonths(1);
        }

        long initialDelay = Duration.between(now, nextDay1).toMillis();
        long period = Duration.ofDays(1).toMillis(); // Verificar diariamente hasta llegar al día 7

        // Programar la tarea para que se ejecute el día 1 de cada mes
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> cambiarEstadoCliente(cliente), initialDelay, period, TimeUnit.MILLISECONDS);
    }

    private void cambiarEstadoCliente(Cliente cliente) {
        // Verificar si hoy es el día 1
        if (ZonedDateTime.now().getDayOfMonth() == 1) {
            cliente.setEstado(Estado.NO_PAGO);
            clienteRepository.save(cliente);
        }
    }
}
