package GestionPagoMensual.club.Services;

import GestionPagoMensual.club.Entitys.Cliente;
import GestionPagoMensual.club.Entitys.Estado;
import GestionPagoMensual.club.Entitys.PagoMensual;
import GestionPagoMensual.club.Repositories.ClienteRepository;
import GestionPagoMensual.club.Repositories.PagoMensualRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PagoMensualRepository pagoMensualRepository;


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
    public List<Cliente> buscarClientesPorLetras(String letras) {
        return clienteRepository.findByNombreContainingOrApellidoContaining(letras, letras);
    }
   // public List<Cliente> buscarClientePorNombreYApellido(String nombre, String apellido) {
   //     return clienteRepository.findByNombreAndApellido(nombre, apellido);
  //  }

    public Cliente crearCliente(Cliente cliente) throws Exception{
        // Verificar si ya existe un cliente con el mismo DNI
        Cliente clienteExistente = clienteRepository.findByDni(cliente.getDni());
        if (clienteExistente != null) {
            // Si ya existe un cliente con el mismo DNI, lanzar una excepción o manejar el caso apropiadamente
            throw new Exception("El DNI ya está registrado");
        }
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
