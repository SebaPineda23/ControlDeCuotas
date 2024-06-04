package GestionPagoMensual.club.Repositories;

import GestionPagoMensual.club.Entitys.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findByDni(String dni);
    //List<Cliente> findByNombreAndApellido(String nombre, String apellido);
    List<Cliente> findByNombreContainingOrApellidoContaining(String letras1, String letras2);
    List<Cliente> findByFechaCambioEstadoAfter(LocalDateTime fechaLimite);
    List<Cliente> findClienteByCategoria(String categoria);
    @Query(value = "SELECT DISTINCT c.* FROM Cliente c JOIN pago_mensual p ON c.id = p.cliente_id WHERE SUBSTRING(p.fecha, 4, 7) = :mesAno AND c.categoria = :categoria", nativeQuery = true)
    List<Cliente> findClientesByPagoMesAndCategoria(@Param("mesAno") String mesAno, @Param("categoria") String categoria);
}
