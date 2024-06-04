package GestionPagoMensual.club.Controllers;

import GestionPagoMensual.club.Entitys.Cliente;
import GestionPagoMensual.club.ManejoErrores.DniExistenteException;
import GestionPagoMensual.club.Services.ClienteService;
import GestionPagoMensual.club.dto.ClienteMontoTotalDTO;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/adm_clubes/clientes")
@CrossOrigin("*")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<?> obtenerTodosClientes() {
        try {
            List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
            if (clientes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron clientes para mostrar en pantalla");
            }
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error al procesar la solicitud");
        }
    }
    @GetMapping("/montoTotal/categoria/{categoria}/mes/{mes}")
    public ResponseEntity<?> obtenerClientesYMontosTotalesPorCategoriaYMes(
            @PathVariable("categoria") String categoria,
            @PathVariable("mes") String monthOfPayment) {

        ClienteMontoTotalDTO clientesConMontosTotales = clienteService.obtenerClientesYMontosTotalesPorCategoriaYMes(categoria, monthOfPayment);

        if (clientesConMontosTotales == null || clientesConMontosTotales.getClientes().isEmpty()) {
            return new ResponseEntity<>("No se encontraron pagos para la categoría " + categoria + " en el mes " + monthOfPayment, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(clientesConMontosTotales, HttpStatus.OK);
        }
    }

    @GetMapping("/buscarCliente")
    public List<Cliente> buscarClientesPorLetras(
            @RequestParam("letras") String letras) {
        return clienteService.buscarClientesPorLetras(letras,letras);
    }

    @PostMapping
    public ResponseEntity<?> crearCliente(@RequestBody Cliente cliente) {
        try {
            Cliente nuevoCliente = clienteService.crearCliente(cliente);
            return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("error", "Ya existe un Cliente registrado con el DNI ingresado, por favor vuelva a intentar");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
        }
    }
    @GetMapping("/categoria/{categoria}/mes/{mes}")
    public ResponseEntity<List<Cliente>> obtenerClientesPorCategoriaYMes(
            @PathVariable("categoria") String categoria,
            @PathVariable("mes") String mes) {

        List<Cliente> clientes = clienteService.obtenerClientesPorCategoriaYMes(categoria, mes);

        if (clientes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(clientes, HttpStatus.OK);
        }
    }


    @PutMapping("/{clienteId}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long clienteId, @RequestBody Cliente cliente) {
        try{
        Cliente clienteActualizado = clienteService.actualizarCliente(clienteId, cliente);
        return new ResponseEntity<>(clienteActualizado, HttpStatus.OK);
        }catch(Exception e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @PutMapping("/cliente/{id}/estadoNoPago")
    public ResponseEntity<Cliente> actualizarEstado(@PathVariable Long id){
        try{
            Cliente clienteEstadoNoPago = clienteService.actualizarEstado(id);
            return new ResponseEntity<>(clienteEstadoNoPago, HttpStatus.OK);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @DeleteMapping("/{clienteId}")
    public ResponseEntity<Void> borrarCliente(@PathVariable Long clienteId) {
        try {
            clienteService.borrarCliente(clienteId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}