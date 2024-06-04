package GestionPagoMensual.club.Services;

import GestionPagoMensual.club.Entitys.Cliente;
import GestionPagoMensual.club.Entitys.Estado;
import GestionPagoMensual.club.Entitys.PagoMensual;
import GestionPagoMensual.club.Repositories.ClienteRepository;
import GestionPagoMensual.club.Repositories.PagoMensualRepository;
import GestionPagoMensual.club.dto.ClientesYTotal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public ClientesYTotal getClientesByPagoMesAndCategoria(String mesAno, String categoria) {
        List<Cliente> clientes = clienteRepository.findClientesByPagoMesAndCategoria(mesAno, categoria);

        // Filtrar los pagos que coinciden con el mes y año especificado
        clientes.forEach(cliente -> {
            List<PagoMensual> pagosFiltrados = cliente.getCronogramaPagos().stream()
                    .filter(pago -> mesAno.equals(pago.getFecha().substring(3, 10)))
                    .collect(Collectors.toList());
            cliente.setCronogramaPagos(pagosFiltrados);
        });

        Double montoTotal = pagoMensualRepository.calcularMontoTotalPorCategoriaYMes(mesAno, categoria);
        montoTotal = (montoTotal == null) ? 0.0 : montoTotal;

        return new ClientesYTotal(clientes, montoTotal);
    }


    public List<Cliente> buscarClientesPorLetras(String letras1, String letras2) {
        if (letras1 != null && !letras1.isEmpty() && letras2 != null && !letras2.isEmpty()) {
            return clienteRepository.findByNombreContainingOrApellidoContaining(letras1, letras2);
        } else {
            return null; // O devolver una lista vacía, dependiendo de los requisitos
        }
    }
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
        Optional<Cliente> clienteOptional = clienteRepository.findById(clienteId);
        if (clienteOptional.isPresent()) {
            Cliente clienteExistente = clienteOptional.get();

            // Actualizar solo los campos necesarios
            if (cliente.getNombre() != null) {
                clienteExistente.setNombre(cliente.getNombre());
            }
            if (cliente.getApellido() != null) {
                clienteExistente.setApellido(cliente.getApellido());
            }
            if (cliente.getDni() != null) {
                clienteExistente.setDni(cliente.getDni());
            }
            if (cliente.getFecha_nacimiento() != null) {
                clienteExistente.setFecha_nacimiento(cliente.getFecha_nacimiento());
            }
            if(cliente.getEmail() != null){
                clienteExistente.setEmail(cliente.getEmail());
            }
            if(cliente.getCategoria()!= null){
                clienteExistente.setCategoria(cliente.getCategoria());
            }
            // Guardar el cliente actualizado
            return clienteRepository.save(clienteExistente);
        } else {
            throw new Exception("Cliente no encontrado con ID: " + clienteId);
        }
    }
    public List<Cliente> obtenerClientePorCategoria(String categoria) {
        return clienteRepository.findClienteByCategoria(categoria);
    }
    public Cliente actualizarEstado(Long clienteId) throws Exception {
        Optional<Cliente> clienteOptional = clienteRepository.findById(clienteId);
        if (clienteOptional.isPresent()) {
            Cliente clienteExistente = clienteOptional.get();
            clienteExistente.setEstado(Estado.NO_PAGO);
            return clienteRepository.save(clienteExistente);
        } else {
            throw new ResourceNotFoundException("Cliente no encontrado con ID: " + clienteId);
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
