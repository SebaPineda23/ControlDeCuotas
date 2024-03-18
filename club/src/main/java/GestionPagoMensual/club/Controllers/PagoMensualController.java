package GestionPagoMensual.club.Controllers;

import GestionPagoMensual.club.Entitys.PagoMensual;
import GestionPagoMensual.club.Services.PagoMensualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("prueba/pago_mensuales")
public class PagoMensualController {

    @Autowired
    private PagoMensualService pagoMensualService;

    @PostMapping
    public ResponseEntity<PagoMensual> crearFacturaMensual(@RequestBody PagoMensual facturaMensual) {
        PagoMensual nuevaFacturaMensual = pagoMensualService.guardarFacturaMensual(facturaMensual);
        return new ResponseEntity<>(nuevaFacturaMensual, HttpStatus.CREATED);
    }
    @PutMapping("/{clienteId}/pagos/{pagoMensualId}")
    public ResponseEntity<String> actualizarPago(
            @PathVariable Long pagoMensualId,
            @PathVariable Long clienteId
    ) throws Exception {
        pagoMensualService.actualizarPago(pagoMensualId, clienteId);
        return ResponseEntity.ok("Pago registrado exitosamente para el cliente con ID: " + clienteId);
    }

    @GetMapping
    public ResponseEntity<List<PagoMensual>> obtenerTodasLasFacturasMensuales() {
        List<PagoMensual> pagoMensuales = pagoMensualService.obtenerTodasLasFacturasMensuales();
        return new ResponseEntity<>(pagoMensuales, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoMensual> obtenerFacturaMensualPorId(@PathVariable Long id) {
        return pagoMensualService.obtenerFacturaMensualPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFacturaMensual(@PathVariable Long id) {
        pagoMensualService.eliminarFacturaMensualPorId(id);
        return ResponseEntity.noContent().build();
    }
}
