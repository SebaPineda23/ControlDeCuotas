package GestionPagoMensual.club.Controllers;

import GestionPagoMensual.club.Entitys.Cliente;
import GestionPagoMensual.club.Services.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prueba/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> obtenerTodosClientes() {
        List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<Cliente> obtenerClientePorId(@PathVariable Long clienteId) throws Exception {
        Cliente cliente = clienteService.obtenerClientePorId(clienteId);
        return new ResponseEntity<>(cliente, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.crearCliente(cliente);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }


    @PutMapping("/{clienteId}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long clienteId, @RequestBody Cliente cliente) throws Exception {
        Cliente clienteActualizado = clienteService.actualizarCliente(clienteId, cliente);
        return new ResponseEntity<>(clienteActualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<Void> borrarCliente(@PathVariable Long clienteId) throws Exception {
        clienteService.borrarCliente(clienteId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}