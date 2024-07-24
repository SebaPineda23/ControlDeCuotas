package GestionPagoMensual.club.Controllers;

import GestionPagoMensual.club.Entitys.PagoMensual;
import GestionPagoMensual.club.Services.ClienteService;
import GestionPagoMensual.club.Services.PagoMensualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("adm_clubes/pago_mensuales")
@CrossOrigin("*")
public class PagoMensualController {

    @Autowired
    private PagoMensualService pagoMensualService;
    @Autowired
    private ClienteService clienteService;

    @PostMapping("/{clienteId}/pagos")
    public ResponseEntity<String> crearPago(
            @PathVariable Long clienteId,
            @RequestBody PagoMensual nuevoPago
    ) throws Exception {
        try {
            PagoMensual pagoCreado = pagoMensualService.guardarFacturaMensual(nuevoPago, clienteId);
            return ResponseEntity.ok("Pago registrado exitosamente para el cliente con ID: " + clienteId +
                    ". ID del nuevo pago mensual: " + pagoCreado.getId());
        } catch (Exception e) {
            // Handle exception appropriately, maybe log it
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la solicitud, " + e.getMessage());
        }
    }
    @GetMapping("/pagos")
    public List<PagoMensual> getPagosByMes(@RequestParam String mesAno) {
        return pagoMensualService.getPagosByMes(mesAno);
    }


    @GetMapping
    public ResponseEntity<List<PagoMensual>> obtenerTodasLasFacturasMensuales() {
        List<PagoMensual> pagoMensuales = pagoMensualService.obtenerTodasLasFacturasMensuales();
        return new ResponseEntity<>(pagoMensuales, HttpStatus.OK);
    }
    @GetMapping("/cliente/{idCliente}/pagos")
    public ResponseEntity<?> obtenerPagosMensualesPorIdCliente(@PathVariable Long idCliente) {
        List<PagoMensual> pagosMensuales = pagoMensualService.obtenerPagosMensualesPorIdCliente(idCliente);
        if (!pagosMensuales.isEmpty()) {
            return ResponseEntity.ok(pagosMensuales);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron pagos mensuales asociados al Cliente con el ID: " + idCliente);
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<PagoMensual> actualizarPagoMensual(@PathVariable Long id, @RequestBody PagoMensual pagoMensual) {
        try {
            PagoMensual pagoActualizado = pagoMensualService.updatePagoMensual(id, pagoMensual);
            return ResponseEntity.ok(pagoActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFacturaMensual(@PathVariable Long id) {
        pagoMensualService.eliminarFacturaMensualPorId(id);
        return ResponseEntity.noContent().build();
    }
}
