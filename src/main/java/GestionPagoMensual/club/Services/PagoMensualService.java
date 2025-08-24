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

    public PagoMensual updatePagoMensual(Long pagoMensualId, PagoMensual pagoMensual) throws Exception {
        Optional<PagoMensual> pagoMensualOptional = pagoMensualRepository.findById(pagoMensualId);
        if (pagoMensualOptional.isPresent()) {
            PagoMensual pagoMensualExistente = pagoMensualOptional.get();
            if (pagoMensual.getMonto() != 0) {
                pagoMensualExistente.setMonto(pagoMensual.getMonto());
            }
            return pagoMensualRepository.save(pagoMensualExistente);
        } else {
            throw new Exception("Pago no existente con el id: " + pagoMensualId);
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

    public PagoMensual guardarFacturaMensual(PagoMensual nuevoPago, Long clienteId) throws Exception {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new Exception("Cliente no encontrado con ID: " + clienteId));

        ZonedDateTime horaActualLocal = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime fechaCreacionPago = horaActualLocal;

        Estado estadoAnterior = cliente.getEstado();
        cliente.setEstado(Estado.PAGO);
        cliente.setPago(true);
        cliente.setFechaCambioEstado(horaActualLocal);
        nuevoPago.setCliente(cliente);

        PagoMensual pagoMensualGuardado = pagoMensualRepository.save(nuevoPago);

        sendPaymentEmail(cliente, fechaCreacionPago);

        Runnable verificarEstadoCliente = () -> {
            ZonedDateTime horaActual = ZonedDateTime.now(ZoneId.systemDefault());

            int cantidadPagos = cliente.getCronogramaPagos().size();
            long diasTranscurridos = cantidadPagos * Duration.between(
                    fechaCreacionPago.toLocalDate().atStartOfDay(),
                    horaActual.toLocalDate().atStartOfDay()
            ).toDays();

            int limiteDias = estadoAnterior == Estado.PAGO ? (cantidadPagos + 1) : 1;

            if (diasTranscurridos >= limiteDias) {
                long diasTotales = Duration.between(
                        fechaCreacionPago.toLocalDate().atStartOfDay(),
                        horaActual.toLocalDate().atStartOfDay()
                ).toDays();
                if (diasTotales >= 1) {
                    cambiarEstadoCliente(clienteId);
                }
            }
        };

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(verificarEstadoCliente, 0, 1, TimeUnit.DAYS);

        return pagoMensualGuardado;
    }

    private void sendPaymentEmail(Cliente cliente, ZonedDateTime fechaCreacionPago) {
        String mensaje = "Hola " + cliente.getNombre() + ",\n\nGracias por realizar el pago de la cuota. " +
                "El pago se efectuó el día " + fechaCreacionPago.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                "\n\nSaludos,\nEl equipo de gestión del club";

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
