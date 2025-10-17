package GestionPagoMensual.club.Services;

import GestionPagoMensual.club.Entitys.Cliente;
import GestionPagoMensual.club.Entitys.Estado;
import GestionPagoMensual.club.Entitys.PagoMensual;
import GestionPagoMensual.club.Repositories.ClienteRepository;
import GestionPagoMensual.club.Repositories.PagoMensualRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class PagoMensualService {

    private final PagoMensualRepository pagoMensualRepository;
    private final ClienteRepository clienteRepository;
    private final AuthMail authMail;

    @Autowired
    public PagoMensualService(PagoMensualRepository pagoMensualRepository, ClienteRepository clienteRepository, AuthMail authMail) {
        this.pagoMensualRepository = pagoMensualRepository;
        this.clienteRepository = clienteRepository;
        this.authMail = authMail;
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

    @Transactional
    public PagoMensual guardarFacturaMensual(PagoMensual nuevoPago, Long clienteId) {
        // 1. Obtener el cliente
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado con ID: " + clienteId));

        // 2. Calcular la fecha de vencimiento del nuevo pago
        PagoMensual ultimoPago = pagoMensualRepository.findFirstByClienteOrderByFechaVencimientoDesc(cliente);
        ZonedDateTime fechaActual = ZonedDateTime.now();
        ZonedDateTime fechaVencimientoNuevoPago;

        if (ultimoPago != null) {
            fechaVencimientoNuevoPago = ultimoPago.getFechaVencimiento().plusDays(1);
        } else {
            fechaVencimientoNuevoPago = fechaActual.plusDays(1);
        }

        // 3. Guardar el nuevo pago
        nuevoPago.setCliente(cliente);
        nuevoPago.setFechaVencimiento(fechaVencimientoNuevoPago);
        PagoMensual pagoMensualGuardado = pagoMensualRepository.save(nuevoPago);

        // 4. Actualizar el estado del cliente a PAGO
        cliente.setEstado(Estado.PAGO);
        cliente.setFechaCambioEstado(fechaActual);
        clienteRepository.save(cliente);

        // 5. Enviar correo electrónico
        //sendPaymentEmail(cliente, fechaActual);

        return pagoMensualGuardado;
    }

//    private void sendPaymentEmail(Cliente cliente, ZonedDateTime fechaCreacionPago) {
//        String mensaje = String.format(
//                "Hola %s,\n\nGracias por realizar el pago de la cuota. El pago se efectuó el día %s\n\nSaludos,\nEl equipo de gestión del club",
//                cliente.getNombre(),
//                fechaCreacionPago.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
//        );
//        authMail.sendMessage(cliente.getEmail(), mensaje);
//    }
}