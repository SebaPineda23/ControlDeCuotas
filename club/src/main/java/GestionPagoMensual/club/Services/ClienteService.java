package GestionPagoMensual.club.Services;

import GestionPagoMensual.club.Entitys.Cliente;
import GestionPagoMensual.club.Entitys.Estado;
import GestionPagoMensual.club.Repositories.ClienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;


    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }
    public Cliente obtenerClientePorId(Long clienteId) throws Exception {
        Optional<Cliente> clienteOptional = clienteRepository.findById(clienteId);
        if (clienteOptional.isPresent()) {
            return clienteOptional.get();
        } else {
            throw new Exception("Cliente no encontrado con ID: " + clienteId);
        }
    }

    public Cliente crearCliente(Cliente cliente) {
        cliente.setEstado(Estado.NO_PAGO);
        return clienteRepository.save(cliente);
    }

    public Cliente actualizarCliente(Long clienteId, Cliente cliente) throws Exception {
        if (clienteRepository.existsById(clienteId)) {
            cliente.setId(clienteId);
            return clienteRepository.save(cliente);
        } else {
            throw new Exception("Cliente no encontrado con ID: " + clienteId);
        }
    }

    public void borrarCliente(Long clienteId) throws Exception {
        if (clienteRepository.existsById(clienteId)) {
            clienteRepository.deleteById(clienteId);
        } else {
            throw new Exception("Cliente no encontrado con ID: " + clienteId);
        }
    }


}
